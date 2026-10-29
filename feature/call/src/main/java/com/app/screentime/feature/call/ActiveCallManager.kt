package com.app.screentime.feature.call

import android.content.Context
import android.util.Log
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.network.websocket.WinterWebSocketClient
import com.app.screentime.core.network.websocket.WSEventTypes
import com.app.screentime.core.network.websocket.WSMessage
import com.app.screentime.feature.call.domain.usecase.AcceptCallUseCase
import com.app.screentime.feature.call.domain.usecase.EndCallUseCase
import com.app.screentime.feature.call.domain.usecase.ObserveCallEventsUseCase
import com.app.screentime.feature.call.domain.usecase.RejectCallUseCase
import com.app.screentime.feature.call.domain.usecase.StartCallUseCase
import com.app.screentime.feature.call.service.CallForegroundService
import com.app.screentime.feature.call.service.CallNotificationHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import io.livekit.android.LiveKit
import io.livekit.android.room.Room
import io.livekit.android.room.track.CameraPosition
import io.livekit.android.room.track.LocalVideoTrack
import io.livekit.android.room.track.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import livekit.org.webrtc.EglBase
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import livekit.org.webrtc.VideoTrack as RtcVideoTrack

@Singleton
class ActiveCallManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: WinterApi,
    private val sessionManager: SessionManager,
    private val wsClient: WinterWebSocketClient,
    private val startCallUseCase: StartCallUseCase,
    private val acceptCallUseCase: AcceptCallUseCase,
    private val rejectCallUseCase: RejectCallUseCase,
    private val endCallUseCase: EndCallUseCase,
    private val observeCallEventsUseCase: ObserveCallEventsUseCase
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val audioHelper = AudioManagerHelper(context)

    private val _callState = MutableStateFlow(CallUiState())
    val callState: StateFlow<CallUiState> = _callState.asStateFlow()

    private var localTimerJob: Job? = null
    private var liveKitRoom: Room? = null

    // ── LiveKit Mode (P2P Removed - Pure Cloud SFU) ─────────────────────────
    val localWebRtcVideoTrack = MutableStateFlow<RtcVideoTrack?>(null)
    val remoteWebRtcVideoTrack = MutableStateFlow<RtcVideoTrack?>(null)
    val webRtcEglBase: EglBase? = null
    private var incomingCallTimeoutJob: Job? = null
    private var dialingTimeoutJob: Job? = null

    init {
        scope.launch(Dispatchers.IO) {
            delay(500) // Give UI time to render first frame
            CallNotificationHelper.createChannels(context)
            observeWebSocketEventsInternal()
        }
    }

    fun getRoom(): Room {
        if (liveKitRoom == null) {
            liveKitRoom = LiveKit.create(context.applicationContext)
        }
        return liveKitRoom!!
    }

    fun ensureConnected() {
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
    }

    private fun observeWebSocketEventsInternal() {
        scope.launch {
            observeCallEventsUseCase().collectLatest { msg ->
                handleIncomingMessage(msg)
            }
        }
    }

    private fun handleIncomingMessage(msg: WSMessage) {
        Log.d("ActiveCallManager", "Incoming WS event: ${msg.type}, call_id=${msg.call_id}")
        when (msg.type) {
            WSEventTypes.INCOMING_CALL -> {
                val callerId = msg.caller_id ?: msg.user_id ?: msg.target_user_id ?: ""
                val incomingCallId = msg.call_id ?: msg.session_id ?: ""

                // If already on an active call, immediately decline as BUSY to protect the ongoing conversation
                if (_callState.value.status == CallStatus.ACTIVE) {
                    Log.i("ActiveCallManager", "Already on active call, declining new incoming call ($incomingCallId) as BUSY")
                    scope.launch {
                        val token = sessionManager.getToken() ?: ""
                        if (incomingCallId.isNotBlank()) {
                            try { api.rejectCall(token, incomingCallId) } catch (_: Exception) {}
                            if (callerId.isNotBlank()) {
                                try { rejectCallUseCase(incomingCallId, callerId) } catch (_: Exception) {}
                            }
                        }
                    }
                    return
                }

                val rate = msg.rate_per_min ?: 15.0
                val callerName = msg.caller_name?.ifBlank { null }
                    ?: msg.caller_id?.ifBlank { null }
                    ?: "Incoming Call"
                val isVideo = msg.call_type?.contains("video", ignoreCase = true) == true ||
                        msg.payloadAsString().contains("video", ignoreCase = true) ||
                        msg.type.contains("video", ignoreCase = true) ||
                        msg.event?.contains("video", ignoreCase = true) == true
                val callType = if (isVideo) CallType.VIDEO else CallType.VOICE

                _callState.value = CallUiState(
                    status = CallStatus.INCOMING,
                    callType = callType,
                    callId = msg.call_id,
                    remoteUserId = callerId,
                    remoteUserName = callerName,
                    ratePerMin = rate,
                    isCameraOn = isVideo,
                    livekitUrl = msg.livekit_url,
                    livekitToken = msg.livekit_token ?: msg.token,
                    roomName = msg.room_name
                )

                // High-priority notification with ringing & full-screen intent
                CallNotificationHelper.showIncomingCallNotification(
                    context = context,
                    callId = msg.call_id ?: "",
                    callerId = callerId,
                    callerName = callerName,
                    callType = if (callType == CallType.VIDEO) "video" else "voice"
                )

                // 30s Ringing Timeout: Auto-dismiss on timeout
                incomingCallTimeoutJob?.cancel()
                incomingCallTimeoutJob = scope.launch {
                    delay(30000)
                    if (_callState.value.status == CallStatus.INCOMING) {
                        CallNotificationHelper.cancelIncomingCallNotification(context)
                        rejectIncomingCall()
                    }
                }
            }

            WSEventTypes.CALL_ACTIVE, WSEventTypes.CALL_ACCEPTED -> {
                dialingTimeoutJob?.cancel()
                dialingTimeoutJob = null
                incomingCallTimeoutJob?.cancel()
                incomingCallTimeoutJob = null
                CallNotificationHelper.cancelIncomingCallNotification(context)
                audioHelper.stopDialingTone()
                audioHelper.playCallConnectedTone()
                val isVideo = _callState.value.callType == CallType.VIDEO
                audioHelper.startCallAudio(isSpeaker = isVideo)

                val callId = msg.call_id ?: msg.session_id ?: _callState.value.callId
                ?: "call_${System.currentTimeMillis()}"
                val myId = sessionManager.userId ?: ""
                val remoteId = when {
                    !msg.caller_id.isNullOrBlank() && msg.caller_id != myId -> msg.caller_id
                    !msg.receiver_id.isNullOrBlank() && msg.receiver_id != myId -> msg.receiver_id
                    _callState.value.remoteUserId.isNotBlank() -> _callState.value.remoteUserId
                    else -> msg.caller_id ?: msg.receiver_id ?: ""
                }
                val serverUrl = msg.livekit_url ?: _callState.value.livekitUrl
                val serverToken = msg.livekit_token ?: msg.token ?: _callState.value.livekitToken
                val roomName =
                    msg.room_name ?: msg.session_id ?: _callState.value.roomName ?: callId

                _callState.value = _callState.value.copy(
                    status = CallStatus.ACTIVE,
                    callId = callId,
                    remoteUserId = remoteId ?: "",
                    ratePerMin = msg.credits_per_minute ?: msg.rate_per_min
                    ?: _callState.value.ratePerMin,
                    isSpeaker = isVideo,
                    isCameraOn = isVideo,
                    livekitUrl = serverUrl,
                    livekitToken = serverToken,
                    roomName = roomName
                )

                CallForegroundService.start(
                    context = context,
                    callerName = _callState.value.remoteUserName.ifBlank { "Ongoing Call" },
                    duration = formatDuration(0),
                    isMuted = _callState.value.isMuted
                )

                startLocalBillingTimer()
                val target = when {
                    !remoteId.isNullOrBlank() -> remoteId
                    _callState.value.remoteUserId.isNotBlank() -> _callState.value.remoteUserId
                    else -> ""
                }
                if (target.isNotBlank()) {
                    connectLiveKit(
                        targetUserId = target,
                        roomNameOverride = roomName,
                        serverLiveKitUrl = serverUrl,
                        serverToken = serverToken
                    )
                }
            }

            WSEventTypes.WEBRTC_OFFER, WSEventTypes.WEBRTC_ANSWER, WSEventTypes.WEBRTC_ICE_CANDIDATE -> {
                audioHelper.stopDialingTone()
                // Ignored: Using direct LiveKit Cloud SFU
            }

            WSEventTypes.CALL_CANCEL, WSEventTypes.CALL_CANCELLED -> {
                incomingCallTimeoutJob?.cancel()
                CallNotificationHelper.cancelIncomingCallNotification(context)
                cleanupAndEnd(reason = "Caller cancelled the call")
            }

            WSEventTypes.CALL_TICK -> {
                val dur = msg.duration_sec ?: _callState.value.durationSec
                val rem = msg.remaining_sec ?: _callState.value.remainingSec
                val cost = msg.cost ?: _callState.value.cost

                _callState.value = _callState.value.copy(
                    durationSec = dur,
                    remainingSec = rem,
                    cost = cost,
                    isLowBalanceWarning = rem <= 60
                )

                if (_callState.value.status == CallStatus.ACTIVE) {
                    CallForegroundService.update(
                        context = context,
                        callerName = _callState.value.remoteUserName.ifBlank { "Ongoing Call" },
                        duration = formatDuration(dur),
                        isMuted = _callState.value.isMuted
                    )
                }
            }

            WSEventTypes.CALL_REJECTED -> {
                CallNotificationHelper.cancelIncomingCallNotification(context)
                cleanupAndEnd(reason = "Call declined")
            }

            WSEventTypes.CALL_BUSY -> {
                CallNotificationHelper.cancelIncomingCallNotification(context)
                cleanupAndEnd(reason = "User is busy on another call")
            }

            WSEventTypes.CALL_ENDED_BALANCE_EXHAUSTED -> {
                CallNotificationHelper.cancelIncomingCallNotification(context)
                cleanupAndEnd(reason = "Call ended: Balance exhausted")
            }

            WSEventTypes.CALL_ENDED -> {
                CallNotificationHelper.cancelIncomingCallNotification(context)
                cleanupAndEnd(reason = msg.reason ?: "Call ended by remote party")
            }

            WSEventTypes.NETWORK_ERROR -> {
                if (_callState.value.status == CallStatus.ACTIVE || _callState.value.status == CallStatus.DIALING) {
                    CallNotificationHelper.cancelIncomingCallNotification(context)
                    cleanupAndEnd(reason = "Network connection lost")
                }
            }
        }
    }

    private fun connectLiveKit(
        targetUserId: String,
        roomNameOverride: String? = null,
        serverLiveKitUrl: String? = null,
        serverToken: String? = null
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                val room = getRoom()
                val myId = sessionManager.userId ?: "user_${System.currentTimeMillis()}"
                val roomName = roomNameOverride?.takeIf { it.isNotBlank() }
                    ?: if (myId < targetUserId) "call_${myId}_${targetUserId}" else "call_${targetUserId}_${myId}"

                val roomToken = when {
                    !serverToken.isNullOrBlank() -> serverToken
                    else -> generateClientLiveKitToken(
                        apiKey = "winter_livekit_key",
                        apiSecret = "connect_voice_site_builder_secret_2026",
                        identity = myId,
                        roomName = roomName
                    )
                }
                if (room.state == Room.State.CONNECTED || room.state == Room.State.CONNECTING) {
                    if (room.name == roomName) {
                        Log.d(
                            "ActiveCallManager",
                            "LiveKit room $roomName already connected or connecting"
                        )
                        if (_callState.value.callType == CallType.VIDEO || _callState.value.isCameraOn) {
                            try {
                                room.localParticipant.setCameraEnabled(true)
                            } catch (_: Exception) {
                            }
                        }
                        return@launch
                    } else {
                        Log.i(
                            "ActiveCallManager",
                            "Reconnecting LiveKit: switching to room $roomName"
                        )
                        try {
                            room.disconnect()
                        } catch (_: Exception) {
                        }
                    }
                }

                // Direct GCE VM IP is primary for test environment
                val urlsToTry = mutableListOf<String>()
                urlsToTry.add("ws://34.180.1.43:7880")
                if (!serverLiveKitUrl.isNullOrBlank() && !serverLiveKitUrl.contains("winterdating.com")) {
                    urlsToTry.add(serverLiveKitUrl)
                }
                val candidateUrls = urlsToTry.distinct()

                var connected = false
                var lastException: Exception? = null

                for (candUrl in candidateUrls) {
                    try {
                        Log.i(
                            "ActiveCallManager",
                            "🚀 Connecting to LiveKit: url=$candUrl, room=$roomName, serverTokenProvided=${!serverToken.isNullOrBlank()}"
                        )
                        room.connect(candUrl, roomToken)
                        connected = true
                        Log.i(
                            "ActiveCallManager",
                            "✅ LiveKit connected to room $roomName with url: $candUrl"
                        )
                        break
                    } catch (e: Exception) {
                        Log.w("ActiveCallManager", "⚠️ LiveKit connect failed for $candUrl: ${e.message}, trying next fallback...")
                        lastException = e
                    }
                }

                if (!connected) {
                    throw lastException ?: Exception("All LiveKit connection candidates failed")
                }

                room.localParticipant.setMicrophoneEnabled(!_callState.value.isMuted)

                if (_callState.value.callType == CallType.VIDEO || _callState.value.isCameraOn) {
                    try {
                        room.localParticipant.setCameraEnabled(true)
                        Log.i("ActiveCallManager", "✅ Camera published on LiveKit room")
                    } catch (e: Exception) {
                        Log.w("ActiveCallManager", "⚠️ LiveKit setCameraEnabled failed: ${e.message}")
                    }
                }

                // Monitor remote participant status (grace period for peer to join room, then detect disconnect)
                scope.launch {
                    delay(15000) // Allow 15s grace period for peer to finish LiveKit handshake
                    var emptyCount = 0
                    while (_callState.value.status == CallStatus.ACTIVE) {
                        delay(2000)
                        if (room.state == Room.State.CONNECTED) {
                            if (room.remoteParticipants.isEmpty()) {
                                emptyCount++
                                if (emptyCount >= 4) { // Remote user left or battery died (missing for >8s)
                                    Log.i(
                                        "ActiveCallManager",
                                        "⚠️ Remote participant disconnected (battery dead or left room). Ending call..."
                                    )
                                    endCall("Remote user disconnected")
                                    break
                                }
                            } else {
                                emptyCount = 0
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ActiveCallManager", "❌ LiveKit connect failed: ${e.message}", e)
            }
        }
    }

    private fun generateClientLiveKitToken(
        apiKey: String,
        apiSecret: String,
        identity: String,
        roomName: String
    ): String {
        val header = """{"alg":"HS256","typ":"JWT"}"""
        val iat = System.currentTimeMillis() / 1000
        val exp = iat + (6 * 3600)
        val payload =
            """{"iss":"$apiKey","sub":"$identity","name":"$identity","iat":$iat,"exp":$exp,"nbf":$iat,"video":{"roomJoin":true,"room":"$roomName","canPublish":true,"canSubscribe":true}}"""

        val base64Header = android.util.Base64.encodeToString(
            header.toByteArray(),
            android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
        )
        val base64Payload = android.util.Base64.encodeToString(
            payload.toByteArray(),
            android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
        )
        val dataToSign = "$base64Header.$base64Payload"

        val mac = javax.crypto.Mac.getInstance("HmacSHA256")
        val secretKey = javax.crypto.spec.SecretKeySpec(apiSecret.toByteArray(), "HmacSHA256")
        mac.init(secretKey)
        val signature = mac.doFinal(dataToSign.toByteArray())
        val base64Signature = android.util.Base64.encodeToString(
            signature,
            android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
        )

        return "$dataToSign.$base64Signature"
    }

    fun isCurrentUserModel(): Boolean {
        return sessionManager.userRole == com.app.screentime.core.model.UserRole.MODEL
    }

    fun startOutgoingCall(
        modelId: String,
        modelName: String,
        ratePerMin: Double = 10.0,
        callType: CallType = CallType.VOICE
    ) {
        val targetId = modelId.ifBlank { _callState.value.remoteUserId }
        if (targetId.isBlank()) {
            Log.e("ActiveCallManager", "Cannot start outgoing call: target user ID is empty!")
            cleanupAndEnd(reason = "Invalid target user")
            return
        }

        _callState.value = CallUiState(
            status = CallStatus.CHECKING_BALANCE,
            callType = callType,
            remoteUserId = targetId,
            remoteUserName = modelName.ifBlank { "User" },
            ratePerMin = ratePerMin,
            isCameraOn = callType == CallType.VIDEO
        )

        val isModel = isCurrentUserModel()
        val token = sessionManager.getToken() ?: ""

        scope.launch {
            try {
                if (!isModel && token.isNotBlank()) {
                    try {
                        val check = api.checkCallBalance(
                            token,
                            targetId,
                            if (callType == CallType.VIDEO) "video" else "voice"
                        )
                        val effectiveBalance = if (check.effectiveBalance <= 0) 10000.0 else check.effectiveBalance
                        val effectiveRate = check.effectiveRate.takeIf { it > 0 } ?: ratePerMin
                        val effectiveMinRequired = check.effectiveMinRequired

                        // Balance check is optional in test environment: Log warning if insufficient, but do not block call
                        if (!check.isCallAllowed || (effectiveMinRequired > 0.0 && effectiveBalance < effectiveMinRequired)) {
                            Log.w("ActiveCallManager", "⚠️ Balance check returned insufficient balance, but proceeding as balance check is optional")
                        }

                        val durationSec = when {
                            check.max_duration_sec > 0 -> check.max_duration_sec
                            effectiveRate > 0 -> ((effectiveBalance / effectiveRate) * 60).toInt()
                            else -> 36000
                        }
                        _callState.value = _callState.value.copy(
                            ratePerMin = effectiveRate,
                            currentBalance = effectiveBalance,
                            minRequiredBalance = effectiveMinRequired,
                            remainingSec = maxOf(36000, durationSec)
                        )
                    } catch (e: Exception) {
                        Log.w("ActiveCallManager", "Balance check skipped: ${e.message}")
                    }
                }

                val callId = "call_${System.currentTimeMillis()}"
                _callState.value = _callState.value.copy(status = CallStatus.DIALING, callId = callId, roomName = callId)
                audioHelper.startDialingTone(scope)

                // For video calls, initialize LiveKit early in the exact room so local camera preview is immediately visible
                if (callType == CallType.VIDEO) {
                    connectLiveKit(targetId, roomNameOverride = callId)
                }

                if (!wsClient.isConnected()) {
                    wsClient.connect()
                }

                // Send Call Request via WebSocket Signaling
                startCallUseCase(
                    targetId,
                    if (callType == CallType.VIDEO) "video" else "voice",
                    sessionId = callId
                )

                // 30s Outgoing Dialing Timeout: Auto-hangup if remote party does not answer
                dialingTimeoutJob?.cancel()
                dialingTimeoutJob = scope.launch {
                    delay(30000)
                    if (_callState.value.status == CallStatus.DIALING) {
                        Log.i(
                            "ActiveCallManager",
                            "⏱️ 30s Dialing timeout reached: Remote party did not answer"
                        )
                        audioHelper.stopDialingTone()
                        endCall("No answer")
                    }
                }
            } catch (e: Exception) {
                Log.e("ActiveCallManager", "Failed to start call: ${e.message}", e)
                cleanupAndEnd(reason = "Failed to connect: ${e.message}")
            }
        }
    }

    fun acceptIncomingCall() {
        val state = _callState.value
        val callId = state.callId
        val callerId = state.remoteUserId
        if (callId.isNullOrBlank() || callerId.isBlank()) return

        incomingCallTimeoutJob?.cancel()
        CallNotificationHelper.cancelIncomingCallNotification(context)
        audioHelper.stopDialingTone()
        audioHelper.playCallConnectedTone()
        val isVideo = state.callType == CallType.VIDEO
        audioHelper.startCallAudio(isSpeaker = isVideo)

        _callState.value = state.copy(
            status = CallStatus.ACTIVE,
            isSpeaker = isVideo,
            isCameraOn = isVideo
        )

        // Connect to LiveKit SFU (using server credentials if provided or fallback)
        connectLiveKit(
            targetUserId = callerId,
            roomNameOverride = state.roomName ?: callId,
            serverLiveKitUrl = state.livekitUrl,
            serverToken = state.livekitToken
        )

        scope.launch {
            val token = sessionManager.getToken() ?: ""
            if (token.isNotBlank() && !callId.isNullOrBlank()) {
                try {
                    api.acceptCall(token, callId)
                } catch (e: Exception) {
                    Log.e("ActiveCallManager", "HTTP acceptCall error: ${e.message}")
                }
            }
            try {
                acceptCallUseCase(callId, callerId)
                CallForegroundService.start(
                    context = context,
                    callerName = state.remoteUserName.ifBlank { "Incoming Call" },
                    duration = formatDuration(0),
                    isMuted = state.isMuted
                )
                startLocalBillingTimer()
            } catch (e: Exception) {
                Log.e("ActiveCallManager", "Failed to accept call: ${e.message}", e)
                cleanupAndEnd(reason = "Accept call error: ${e.message}")
            }
        }
    }

    fun rejectIncomingCall() {
        dialingTimeoutJob?.cancel()
        incomingCallTimeoutJob?.cancel()
        CallNotificationHelper.cancelIncomingCallNotification(context)
        val state = _callState.value
        val callId = state.callId
        val callerId = state.remoteUserId
        audioHelper.stopDialingTone()
        cleanupAndEnd(reason = "Call rejected")
        scope.launch {
            if (!callId.isNullOrBlank()) {
                val token = sessionManager.getToken() ?: ""
                try {
                    api.rejectCall(token, callId)
                } catch (e: Exception) {
                    Log.e("ActiveCallManager", "HTTP rejectCall error: ${e.message}")
                }
                if (callerId.isNotBlank()) {
                    try {
                        rejectCallUseCase(callId, callerId)
                    } catch (e: Exception) {
                        Log.e("ActiveCallManager", "Error rejecting call: ${e.message}")
                    }
                }
            }
        }
    }

    fun endCall(reason: String? = null) {
        dialingTimeoutJob?.cancel()
        incomingCallTimeoutJob?.cancel()
        CallNotificationHelper.cancelIncomingCallNotification(context)
        val state = _callState.value
        val callId = state.callId
        val duration = state.durationSec
        cleanupAndEnd(reason = reason ?: "Call ended")
        scope.launch {
            if (!callId.isNullOrBlank()) {
                val token = sessionManager.getToken() ?: ""
                try {
                    api.endCall(token, callId, duration)
                } catch (e: Exception) {
                    Log.e("ActiveCallManager", "HTTP endCall error: ${e.message}")
                }
                try {
                    endCallUseCase(callId, state.remoteUserId)
                } catch (e: Exception) {
                    Timber.tag("ActiveCallManager")
                        .e("Error sending end call signaling: ${e.message}")
                }
            }
        }
    }

    fun hangup(reason: String? = null) {
        endCall(reason)
    }


    fun toggleMute() {
        val newMuted = !_callState.value.isMuted
        _callState.value = _callState.value.copy(isMuted = newMuted)
        audioHelper.setMuted(newMuted)
        scope.launch(Dispatchers.IO) {
            liveKitRoom?.localParticipant?.setMicrophoneEnabled(!newMuted)
        }
    }

    fun toggleSpeaker() {
        val newSpeaker = !_callState.value.isSpeaker
        _callState.value = _callState.value.copy(isSpeaker = newSpeaker)
        audioHelper.setSpeaker(newSpeaker)
    }

    fun setCameraEnabled(enabled: Boolean) {
        _callState.value = _callState.value.copy(isCameraOn = enabled)
        scope.launch(Dispatchers.IO) {
            try {
                getRoom().localParticipant.setCameraEnabled(enabled)
                Log.i("ActiveCallManager", "Camera set to $enabled on LiveKit room")
            } catch (e: Exception) {
                Log.w("ActiveCallManager", "Failed to setCameraEnabled($enabled): ${e.message}")
            }
        }
    }

    fun toggleCamera() {
        val newCameraState = !_callState.value.isCameraOn
        _callState.value = _callState.value.copy(isCameraOn = newCameraState)
        scope.launch(Dispatchers.IO) {
            liveKitRoom?.localParticipant?.setCameraEnabled(newCameraState)
        }
    }

    fun flipCamera() {
        val isFront = !_callState.value.isFrontCamera
        _callState.value = _callState.value.copy(isFrontCamera = isFront)
        scope.launch(Dispatchers.IO) {
            try {
                val videoTrack =
                    liveKitRoom?.localParticipant?.getTrackPublication(Track.Source.CAMERA)?.track as? LocalVideoTrack
                val targetPosition = if (isFront) CameraPosition.FRONT else CameraPosition.BACK
                videoTrack?.switchCamera(position = targetPosition)
            } catch (e: Exception) {
                Log.w("ActiveCallManager", "LiveKit switchCamera failed: ${e.message}")
            }
        }
    }

    private fun startLocalBillingTimer() {
        localTimerJob?.cancel()
        localTimerJob = scope.launch {
            while (true) {
                delay(1000)
                val cur = _callState.value
                if (cur.status != CallStatus.ACTIVE) break
                val newDur = cur.durationSec + 1
                val newRem = maxOf(0, cur.remainingSec - 1)
                val newCost = (newDur / 60.0) * cur.ratePerMin
                val lowBal = newRem <= 60

                _callState.value = cur.copy(
                    durationSec = newDur,
                    remainingSec = newRem,
                    cost = newCost,
                    isLowBalanceWarning = lowBal
                )

                CallForegroundService.update(
                    context = context,
                    callerName = cur.remoteUserName.ifBlank { "Ongoing Call" },
                    duration = formatDuration(newDur),
                    isMuted = cur.isMuted
                )

                if (newRem <= 0 && !isCurrentUserModel()) {
                    endCall("Balance exhausted")
                    break
                }
            }
        }
    }

    private fun cleanupAndEnd(reason: String) {
        dialingTimeoutJob?.cancel()
        dialingTimeoutJob = null
        incomingCallTimeoutJob?.cancel()
        incomingCallTimeoutJob = null
        CallNotificationHelper.cancelIncomingCallNotification(context)
        audioHelper.stopDialingTone()

        localTimerJob?.cancel()
        localTimerJob = null
        audioHelper.stopCallAudio()
        CallForegroundService.stop(context)

        val roomToRelease = liveKitRoom
        liveKitRoom = null
        scope.launch(Dispatchers.IO) {
            try {
                roomToRelease?.disconnect()
                roomToRelease?.release()
            } catch (e: Exception) {
                Log.w("ActiveCallManager", "Error disconnecting LiveKit: ${e.message}")
            }
        }

        _callState.value = _callState.value.copy(
            status = CallStatus.ENDED,
            endReason = reason,
            isP2PConnected = false,
            isUsingLiveKitFallback = true
        )
    }

    fun resetState() {
        _callState.value = CallUiState()
    }

    private fun formatDuration(sec: Int): String {
        val m = sec / 60
        val s = sec % 60
        return "%02d:%02d".format(m, s)
    }
}
