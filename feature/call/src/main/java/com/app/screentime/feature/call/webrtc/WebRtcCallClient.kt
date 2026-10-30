package com.app.screentime.feature.call.webrtc

import android.content.Context
import android.util.Log
import com.app.screentime.core.network.websocket.WinterWebSocketClient
import com.app.screentime.core.network.websocket.WSEventTypes
import kotlinx.coroutines.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import livekit.org.webrtc.*
import java.util.concurrent.Executors

class WebRtcCallClient(
    private val context: Context,
    private val wsClient: WinterWebSocketClient,
    private val callId: String,
    private val remoteUserId: String,
    private val isVideo: Boolean,
    private val onConnected: () -> Unit,
    private val onFallbackToLiveKit: (reason: String) -> Unit,
    private val onRemoteVideoTrackReady: (VideoTrack) -> Unit,
    private val onLocalVideoTrackReady: (VideoTrack) -> Unit
) {
    private val tag = "WebRtcCallClient"
    private val executor = Executors.newSingleThreadExecutor()
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    val eglBase: EglBase = EglBase.create()
    private var factory: PeerConnectionFactory? = null
    private var peerConnection: PeerConnection? = null

    private var localVideoSource: VideoSource? = null
    private var localVideoTrack: VideoTrack? = null
    private var localAudioSource: AudioSource? = null
    private var localAudioTrack: AudioTrack? = null
    private var videoCapturer: VideoCapturer? = null
    private var surfaceTextureHelper: SurfaceTextureHelper? = null

    private var isInitiator = false
    private var isClosed = false
    private var connectionTimeoutJob: Job? = null
    private val pendingIceCandidates = java.util.Collections.synchronizedList(mutableListOf<IceCandidate>())
    @Volatile
    private var isRemoteDescriptionSet = false

    init {
        initFactory()
    }

    private fun initFactory() {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .setEnableInternalTracer(false)
                .createInitializationOptions()
        )

        val encoderFactory = DefaultVideoEncoderFactory(eglBase.eglBaseContext, true, true)
        val decoderFactory = DefaultVideoDecoderFactory(eglBase.eglBaseContext)

        factory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory)
            .setOptions(PeerConnectionFactory.Options())
            .createPeerConnectionFactory()
    }

    fun start(isCaller: Boolean) {
        this.isInitiator = isCaller
        executor.execute {
            createPeerConnection()
            setupMediaTracks()

            // Callers do NOT send offer immediately here.
            // The offer is sent via sendOffer() only after the server confirms CALL_ACCEPTED.
            // Receivers create their answer inside handleRemoteOffer().

            // Allow up to 15 seconds for Google STUN P2P ICE negotiation.
            // Mobile networks + NAT traversal can take 10-12 seconds. Only fall back to LiveKit if P2P truly fails.
            connectionTimeoutJob = scope.launch {
                delay(15000L)
                if (!isClosed && peerConnection?.iceConnectionState() != PeerConnection.IceConnectionState.CONNECTED &&
                    peerConnection?.iceConnectionState() != PeerConnection.IceConnectionState.COMPLETED
                ) {
                    Log.w(tag, "⏱️ Google STUN P2P timeout after 15s (NAT/firewall). Falling back to LiveKit SFU...")
                    onFallbackToLiveKit("Google STUN P2P timeout, switched to LiveKit backup")
                }
            }
        }
    }

    /**
     * Called by ActiveCallManager after CALL_ACCEPTED to initiate the WebRTC offer.
     * Only the caller (initiator) should call this. Idempotent — safe to call multiple times.
     */
    @Volatile private var offerSent = false
    fun sendOffer() {
        if (!isInitiator || offerSent) return
        offerSent = true
        executor.execute {
            Log.i(tag, "📤 Sending WebRTC OFFER as caller (post CALL_ACCEPTED)...")
            createOffer()
        }
    }

    private fun createPeerConnection() {
        // Prioritized Google STUN servers cluster for fastest NAT traversal
        val iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun2.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun3.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun4.l.google.com:19302").createIceServer()
        )

        val rtcConfig = PeerConnection.RTCConfiguration(iceServers).apply {
            tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED
            bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE
            rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE
            continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
            keyType = PeerConnection.KeyType.ECDSA
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
            iceCandidatePoolSize = 2
        }

        peerConnection = factory?.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
            override fun onSignalingChange(state: PeerConnection.SignalingState?) {
                Log.d(tag, "SignalingState: $state")
            }

            override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
                Log.i(tag, "🧊 IceConnectionState: $state")
                when (state) {
                    PeerConnection.IceConnectionState.CONNECTED,
                    PeerConnection.IceConnectionState.COMPLETED -> {
                        connectionTimeoutJob?.cancel()
                        Log.i(tag, "✅ WebRTC Connected Successfully via Google STUN P2P!")
                        scope.launch { onConnected() }
                    }
                    PeerConnection.IceConnectionState.FAILED -> {
                        Log.e(tag, "❌ ICE Connection Failed. Triggering LiveKit backup...")
                        scope.launch { onFallbackToLiveKit("ICE connection failed") }
                    }
                    PeerConnection.IceConnectionState.DISCONNECTED -> {
                        Log.w(tag, "⚠️ ICE Connection Disconnected")
                    }
                    else -> {}
                }
            }

            override fun onIceConnectionReceivingChange(receiving: Boolean) {}

            override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {
                Log.d(tag, "IceGatheringState: $state")
            }

            override fun onIceCandidate(candidate: IceCandidate?) {
                if (candidate != null) {
                    Log.i(tag, "❄️ Gathered ICE Candidate via Google STUN [${candidate.sdpMid}]: ${candidate.sdp}")
                    sendIceCandidate(candidate)
                }
            }

            override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}

            override fun onAddStream(stream: MediaStream?) {}

            override fun onRemoveStream(stream: MediaStream?) {}

            override fun onDataChannel(channel: DataChannel?) {}

            override fun onRenegotiationNeeded() {}

            override fun onAddTrack(receiver: RtpReceiver?, streams: Array<out MediaStream>?) {
                val track = receiver?.track()
                if (track is VideoTrack) {
                    Log.i(tag, "🎥 Received Remote VideoTrack via P2P WebRTC!")
                    scope.launch { onRemoteVideoTrackReady(track) }
                } else if (track is AudioTrack) {
                    Log.i(tag, "🔊 Received Remote AudioTrack via P2P WebRTC!")
                    track.setEnabled(true)
                    track.setVolume(1.0)
                }
            }

            override fun onTrack(transceiver: RtpTransceiver?) {
                val track = transceiver?.receiver?.track()
                if (track is VideoTrack) {
                    Log.i(tag, "🎥 Received Remote VideoTrack via Transceiver!")
                    scope.launch { onRemoteVideoTrackReady(track) }
                } else if (track is AudioTrack) {
                    Log.i(tag, "🔊 Received Remote AudioTrack via Transceiver!")
                    track.setEnabled(true)
                    track.setVolume(1.0)
                }
            }
        })
    }

    private fun setupMediaTracks() {
        val f = factory ?: return
        val pc = peerConnection ?: return

        // Audio Track with Echo Cancellation and Noise Suppression
        val audioConstraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("googEchoCancellation", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("googAutoGainControl", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("googHighpassFilter", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("googNoiseSuppression", "true"))
        }
        localAudioSource = f.createAudioSource(audioConstraints)
        localAudioTrack = f.createAudioTrack("ARDAMSa0", localAudioSource)
        localAudioTrack?.setEnabled(true)
        pc.addTrack(localAudioTrack, listOf("ARDAMS"))

        // Video Track (if video call)
        if (isVideo) {
            val enumerator = Camera2Enumerator(context)
            val deviceName = enumerator.deviceNames.firstOrNull { enumerator.isFrontFacing(it) }
                ?: enumerator.deviceNames.firstOrNull()

            if (deviceName != null) {
                videoCapturer = enumerator.createCapturer(deviceName, null)
                surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBase.eglBaseContext)
                localVideoSource = f.createVideoSource(videoCapturer!!.isScreencast)
                videoCapturer!!.initialize(surfaceTextureHelper, context, localVideoSource!!.capturerObserver)
                videoCapturer!!.startCapture(640, 480, 30)

                localVideoTrack = f.createVideoTrack("ARDAMSv0", localVideoSource)
                localVideoTrack?.setEnabled(true)
                pc.addTrack(localVideoTrack, listOf("ARDAMS"))

                localVideoTrack?.let { track ->
                    scope.launch { onLocalVideoTrackReady(track) }
                }
            }
        }
    }

    private fun createOffer() {
        val constraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", if (isVideo) "true" else "false"))
            optional.add(MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"))
        }

        peerConnection?.createOffer(object : SdpObserver {
            override fun onCreateSuccess(desc: SessionDescription?) {
                if (desc == null) return
                peerConnection?.setLocalDescription(object : SdpObserver {
                    override fun onCreateSuccess(p0: SessionDescription?) {}
                    override fun onSetSuccess() {
                        sendSdp(WSEventTypes.WEBRTC_OFFER, desc.description)
                    }
                    override fun onCreateFailure(err: String?) {}
                    override fun onSetFailure(err: String?) {
                        Log.e(tag, "Failed to set local SDP: $err")
                    }
                }, desc)
            }

            override fun onSetSuccess() {}
            override fun onCreateFailure(err: String?) {
                Log.e(tag, "Failed to create offer: $err")
            }
            override fun onSetFailure(err: String?) {}
        }, constraints)
    }

    fun handleRemoteOffer(sdpDescription: String) {
        executor.execute {
            val desc = SessionDescription(SessionDescription.Type.OFFER, sdpDescription)
            peerConnection?.setRemoteDescription(object : SdpObserver {
                override fun onCreateSuccess(p0: SessionDescription?) {}
                override fun onSetSuccess() {
                    isRemoteDescriptionSet = true
                    drainPendingIceCandidates()
                    createAnswer()
                }
                override fun onCreateFailure(p0: String?) {}
                override fun onSetFailure(err: String?) {
                    Log.e(tag, "Failed to set remote offer SDP: $err")
                }
            }, desc)
        }
    }

    private fun createAnswer() {
        val constraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", if (isVideo) "true" else "false"))
            optional.add(MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"))
        }

        peerConnection?.createAnswer(object : SdpObserver {
            override fun onCreateSuccess(desc: SessionDescription?) {
                if (desc == null) return
                peerConnection?.setLocalDescription(object : SdpObserver {
                    override fun onCreateSuccess(p0: SessionDescription?) {}
                    override fun onSetSuccess() {
                        sendSdp(WSEventTypes.WEBRTC_ANSWER, desc.description)
                    }
                    override fun onCreateFailure(p0: String?) {}
                    override fun onSetFailure(err: String?) {
                        Log.e(tag, "Failed to set local answer SDP: $err")
                    }
                }, desc)
            }

            override fun onSetSuccess() {}
            override fun onCreateFailure(err: String?) {
                Log.e(tag, "Failed to create answer: $err")
            }
            override fun onSetFailure(err: String?) {}
        }, constraints)
    }

    fun handleRemoteAnswer(sdpDescription: String) {
        executor.execute {
            val desc = SessionDescription(SessionDescription.Type.ANSWER, sdpDescription)
            peerConnection?.setRemoteDescription(object : SdpObserver {
                override fun onCreateSuccess(p0: SessionDescription?) {}
                override fun onSetSuccess() {
                    Log.i(tag, "✅ Remote Answer SDP applied successfully!")
                    isRemoteDescriptionSet = true
                    drainPendingIceCandidates()
                }
                override fun onCreateFailure(p0: String?) {}
                override fun onSetFailure(err: String?) {
                    Log.e(tag, "Failed to set remote answer SDP: $err")
                }
            }, desc)
        }
    }

    fun handleRemoteIceCandidate(sdpMid: String, sdpMLineIndex: Int, candidateStr: String) {
        executor.execute {
            val candidate = IceCandidate(sdpMid, sdpMLineIndex, candidateStr)
            if (isRemoteDescriptionSet && peerConnection != null) {
                peerConnection?.addIceCandidate(candidate)
            } else {
                pendingIceCandidates.add(candidate)
            }
        }
    }

    private fun drainPendingIceCandidates() {
        val pc = peerConnection ?: return
        synchronized(pendingIceCandidates) {
            val iterator = pendingIceCandidates.iterator()
            while (iterator.hasNext()) {
                val candidate = iterator.next()
                pc.addIceCandidate(candidate)
                iterator.remove()
            }
        }
    }

    private fun sendSdp(type: String, sdp: String) {
        val payloadObj: JsonObject = buildJsonObject {
            put("type", if (type == WSEventTypes.WEBRTC_OFFER) "offer" else "answer")
            put("sdp", sdp)
        }
        wsClient.sendWebRTCSignaling(type, callId, remoteUserId, payloadObj.toString())
    }

    private fun sendIceCandidate(candidate: IceCandidate) {
        val payloadObj = buildJsonObject {
            put("candidate", candidate.sdp)
            put("sdpMid", candidate.sdpMid)
            put("sdpMLineIndex", candidate.sdpMLineIndex)
            put("sdp_mid", candidate.sdpMid)
            put("sdp_m_line_index", candidate.sdpMLineIndex)
        }
        wsClient.sendWebRTCSignaling(WSEventTypes.WEBRTC_ICE_CANDIDATE, callId, remoteUserId, payloadObj.toString())
    }

    fun toggleMute(isMuted: Boolean) {
        localAudioTrack?.setEnabled(!isMuted)
    }

    fun toggleCamera(isOn: Boolean) {
        localVideoTrack?.setEnabled(isOn)
    }

    fun switchCamera(onCompleted: ((Boolean) -> Unit)? = null) {
        val cameraCapturer = videoCapturer as? CameraVideoCapturer ?: run {
            Log.w(tag, "switchCamera called but videoCapturer is not CameraVideoCapturer: $videoCapturer")
            return
        }
        cameraCapturer.switchCamera(object : CameraVideoCapturer.CameraSwitchHandler {
            override fun onCameraSwitchDone(isFrontCamera: Boolean) {
                Log.d(tag, "📷 Camera switch completed successfully! isFrontCamera=$isFrontCamera")
                scope.launch(Dispatchers.Main) {
                    onCompleted?.invoke(isFrontCamera)
                }
            }

            override fun onCameraSwitchError(errorDescription: String?) {
                Log.e(tag, "❌ Camera switch error: $errorDescription")
            }
        })
    }

    fun close() {
        if (isClosed) return
        isClosed = true
        isRemoteDescriptionSet = false
        offerSent = false
        pendingIceCandidates.clear()
        connectionTimeoutJob?.cancel()
        scope.cancel()

        executor.execute {
            try {
                videoCapturer?.stopCapture()
                videoCapturer?.dispose()
                videoCapturer = null
                surfaceTextureHelper?.dispose()
                surfaceTextureHelper = null
                localVideoTrack?.dispose()
                localVideoTrack = null
                localVideoSource?.dispose()
                localVideoSource = null
                localAudioTrack?.dispose()
                localAudioTrack = null
                localAudioSource?.dispose()
                localAudioSource = null
                peerConnection?.close()
                peerConnection?.dispose()
                peerConnection = null
                factory?.dispose()
                factory = null
                eglBase.release()
            } catch (e: Exception) {
                Log.w(tag, "Error closing WebRTC resources: ${e.message}")
            } finally {
                try {
                    executor.shutdown()
                } catch (e: Exception) {
                    Log.w(tag, "Error shutting down WebRTC executor: ${e.message}")
                }
            }
        }
    }
}
