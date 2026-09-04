package com.app.screentime.feature.call.webrtc

import android.content.Context
import android.util.Log
import com.app.screentime.core.network.websocket.ChattyWebSocketClient
import com.app.screentime.core.network.websocket.WSEventTypes
import kotlinx.coroutines.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import livekit.org.webrtc.*
import java.util.concurrent.Executors

class WebRtcCallClient(
    private val context: Context,
    private val wsClient: ChattyWebSocketClient,
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

            if (isCaller) {
                createOffer()
            }

            // Fallback timeout: if P2P does not connect within 10 seconds, trigger LiveKit fallback
            connectionTimeoutJob = scope.launch {
                delay(10000)
                if (!isClosed && peerConnection?.iceConnectionState() != PeerConnection.IceConnectionState.CONNECTED &&
                    peerConnection?.iceConnectionState() != PeerConnection.IceConnectionState.COMPLETED
                ) {
                    Log.w(tag, "⏱️ P2P WebRTC connection timed out (NAT/firewall). Falling back to LiveKit SFU...")
                    onFallbackToLiveKit("P2P direct timeout, switched to LiveKit backup")
                }
            }
        }
    }

    private fun createPeerConnection() {
        val iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun2.l.google.com:19302").createIceServer()
        )

        val rtcConfig = PeerConnection.RTCConfiguration(iceServers).apply {
            tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED
            bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE
            rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE
            continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
            keyType = PeerConnection.KeyType.ECDSA
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
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
                }
            }

            override fun onTrack(transceiver: RtpTransceiver?) {
                val track = transceiver?.receiver?.track()
                if (track is VideoTrack) {
                    Log.i(tag, "🎥 Received Remote VideoTrack via Transceiver!")
                    scope.launch { onRemoteVideoTrackReady(track) }
                }
            }
        })
    }

    private fun setupMediaTracks() {
        val f = factory ?: return
        val pc = peerConnection ?: return

        // Audio Track
        val audioConstraints = MediaConstraints()
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
            peerConnection?.addIceCandidate(candidate)
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
            put("sdp_mid", candidate.sdpMid)
            put("sdp_m_line_index", candidate.sdpMLineIndex)
            put("candidate", candidate.sdp)
        }
        wsClient.sendWebRTCSignaling(WSEventTypes.WEBRTC_ICE_CANDIDATE, callId, remoteUserId, payloadObj.toString())
    }

    fun toggleMute(isMuted: Boolean) {
        localAudioTrack?.setEnabled(!isMuted)
    }

    fun toggleCamera(isOn: Boolean) {
        localVideoTrack?.setEnabled(isOn)
    }

    fun close() {
        if (isClosed) return
        isClosed = true
        connectionTimeoutJob?.cancel()

        executor.execute {
            try {
                videoCapturer?.stopCapture()
                videoCapturer?.dispose()
                surfaceTextureHelper?.dispose()
                localVideoTrack?.dispose()
                localVideoSource?.dispose()
                localAudioTrack?.dispose()
                localAudioSource?.dispose()
                peerConnection?.close()
                peerConnection?.dispose()
                factory?.dispose()
                eglBase.release()
            } catch (e: Exception) {
                Log.w(tag, "Error closing WebRTC resources: ${e.message}")
            }
        }
    }
}
