package com.app.screentime.feature.call.webrtc

import android.content.Context
import android.util.Log
import com.app.screentime.core.network.websocket.ChattyWebSocketClient
import com.app.screentime.core.network.websocket.WSEventTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpReceiver
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import org.webrtc.audio.JavaAudioDeviceModule
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class SdpPayload(
    val type: String,
    val sdp: String
)

@Serializable
data class IceCandidatePayload(
    val sdpMid: String,
    val sdpMLineIndex: Int,
    val candidate: String
)

@Singleton
class WebRTCClient @Inject constructor(
    private val wsClient: ChattyWebSocketClient
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val json = Json { ignoreUnknownKeys = true }

    private var peerConnectionFactory: PeerConnectionFactory? = null
    private var peerConnection: PeerConnection? = null
    private var localAudioTrack: AudioTrack? = null
    private var localAudioSource: AudioSource? = null

    private var currentCallId: String? = null
    private var remoteUserId: String? = null

    private val iceServers = listOf(
        PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),
        PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302").createIceServer(),
        PeerConnection.IceServer.builder("stun:stun2.l.google.com:19302").createIceServer()
    )

    var onIceConnectionFailed: (() -> Unit)? = null

    fun initialize(context: Context) {
        if (peerConnectionFactory != null) return

        val initOptions = PeerConnectionFactory.InitializationOptions.builder(context)
            .setEnableInternalTracer(false)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(initOptions)

        val factoryOptions = PeerConnectionFactory.Options()
        val audioDeviceModule = JavaAudioDeviceModule.builder(context)
            .setUseHardwareAcousticEchoCanceler(true)
            .setUseHardwareNoiseSuppressor(true)
            .createAudioDeviceModule()

        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(factoryOptions)
            .setAudioDeviceModule(audioDeviceModule)
            .createPeerConnectionFactory()

        createLocalAudioTrack()
    }

    private fun createLocalAudioTrack() {
        val factory = peerConnectionFactory ?: return
        val audioConstraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("googEchoCancellation", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("googAutoGainControl", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("googHighpassFilter", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("googNoiseSuppression", "true"))
        }
        localAudioSource = factory.createAudioSource(audioConstraints)
        localAudioTrack = factory.createAudioTrack("ARDAMSa0", localAudioSource)
        localAudioTrack?.setEnabled(true)
    }

    fun startPeerConnection(callId: String, targetUserId: String, isCaller: Boolean) {
        currentCallId = callId
        remoteUserId = targetUserId

        val rtcConfig = PeerConnection.RTCConfiguration(iceServers).apply {
            tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED
            bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE
            rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE
            continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
            keyType = PeerConnection.KeyType.ECDSA
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
        }

        peerConnection = peerConnectionFactory?.createPeerConnection(
            rtcConfig,
            object : PeerConnection.Observer {
                override fun onSignalingChange(state: PeerConnection.SignalingState?) {
                    Log.d("WebRTC", "Signaling state: $state")
                }

                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
                    Log.d("WebRTC", "ICE connection state: $state")
                    if (state == PeerConnection.IceConnectionState.FAILED) {
                        onIceConnectionFailed?.invoke()
                    }
                }

                override fun onIceConnectionReceivingChange(receiving: Boolean) {}

                override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {
                    Log.d("WebRTC", "ICE gathering state: $state")
                }

                override fun onIceCandidate(candidate: IceCandidate?) {
                    candidate ?: return
                    val payload = IceCandidatePayload(
                        sdpMid = candidate.sdpMid ?: "",
                        sdpMLineIndex = candidate.sdpMLineIndex,
                        candidate = candidate.sdp
                    )
                    val jsonStr = json.encodeToString(payload)
                    wsClient.sendWebRTCSignaling(
                        type = WSEventTypes.WEBRTC_ICE_CANDIDATE,
                        callId = callId,
                        receiverId = targetUserId,
                        payload = jsonStr
                    )
                }

                override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}

                override fun onAddStream(stream: MediaStream?) {
                    Log.d("WebRTC", "Stream added with ${stream?.audioTracks?.size} audio tracks")
                    stream?.audioTracks?.forEach { it.setEnabled(true) }
                }

                override fun onRemoveStream(stream: MediaStream?) {}
                override fun onDataChannel(dc: DataChannel?) {}
                override fun onRenegotiationNeeded() {}
                override fun onAddTrack(receiver: RtpReceiver?, streams: Array<out MediaStream>?) {
                    Log.d("WebRTC", "Track added: ${receiver?.track()?.kind()}")
                    if (receiver?.track() is AudioTrack) {
                        (receiver.track() as AudioTrack).setEnabled(true)
                    }
                }
            }
        )

        localAudioTrack?.let {
            peerConnection?.addTrack(it, listOf("ARDAMS"))
        }

        if (isCaller) {
            createOffer()
        }
    }

    private fun createOffer() {
        val sdpConstraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"))
        }
        peerConnection?.createOffer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription?) {
                desc ?: return
                peerConnection?.setLocalDescription(SdpObserverAdapter(), desc)
                val payload = SdpPayload(type = "offer", sdp = desc.description)
                val jsonStr = json.encodeToString(payload)
                val callId = currentCallId ?: return
                val targetId = remoteUserId
                wsClient.sendWebRTCSignaling(
                    type = WSEventTypes.WEBRTC_OFFER,
                    callId = callId,
                    receiverId = targetId,
                    payload = jsonStr
                )
            }
        }, sdpConstraints)
    }

    fun handleRemoteOffer(sdpJson: String) {
        try {
            val offer = json.decodeFromString<SdpPayload>(sdpJson)
            val sessionDesc = SessionDescription(SessionDescription.Type.OFFER, offer.sdp)
            peerConnection?.setRemoteDescription(object : SdpObserverAdapter() {
                override fun onSetSuccess() {
                    createAnswer()
                }
            }, sessionDesc)
        } catch (e: Exception) {
            Log.e("WebRTC", "Failed to handle remote offer: ${e.message}")
        }
    }

    private fun createAnswer() {
        val sdpConstraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"))
        }
        peerConnection?.createAnswer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription?) {
                desc ?: return
                peerConnection?.setLocalDescription(SdpObserverAdapter(), desc)
                val payload = SdpPayload(type = "answer", sdp = desc.description)
                val jsonStr = json.encodeToString(payload)
                val callId = currentCallId ?: return
                val targetId = remoteUserId
                wsClient.sendWebRTCSignaling(
                    type = WSEventTypes.WEBRTC_ANSWER,
                    callId = callId,
                    receiverId = targetId,
                    payload = jsonStr
                )
            }
        }, sdpConstraints)
    }

    fun handleRemoteAnswer(sdpJson: String) {
        try {
            val answer = json.decodeFromString<SdpPayload>(sdpJson)
            val sessionDesc = SessionDescription(SessionDescription.Type.ANSWER, answer.sdp)
            peerConnection?.setRemoteDescription(SdpObserverAdapter(), sessionDesc)
        } catch (e: Exception) {
            Log.e("WebRTC", "Failed to handle remote answer: ${e.message}")
        }
    }

    fun handleRemoteIceCandidate(iceJson: String) {
        try {
            val ice = json.decodeFromString<IceCandidatePayload>(iceJson)
            val candidate = IceCandidate(ice.sdpMid, ice.sdpMLineIndex, ice.candidate)
            peerConnection?.addIceCandidate(candidate)
        } catch (e: Exception) {
            Log.e("WebRTC", "Failed to add remote ICE candidate: ${e.message}")
        }
    }

    fun setMuted(isMuted: Boolean) {
        localAudioTrack?.setEnabled(!isMuted)
    }

    fun close() {
        try {
            peerConnection?.dispose()
            peerConnection = null
            currentCallId = null
            remoteUserId = null
        } catch (e: Exception) {
            Log.e("WebRTC", "Failed to close peer connection: ${e.message}")
        }
    }
}

open class SdpObserverAdapter : SdpObserver {
    override fun onCreateSuccess(desc: SessionDescription?) {}
    override fun onSetSuccess() {}
    override fun onCreateFailure(error: String?) {
        Log.e("WebRTC", "SDP Create Failure: $error")
    }
    override fun onSetFailure(error: String?) {
        Log.e("WebRTC", "SDP Set Failure: $error")
    }
}
