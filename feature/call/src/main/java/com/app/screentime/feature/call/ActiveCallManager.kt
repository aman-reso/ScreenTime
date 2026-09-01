package com.app.screentime.feature.call

import android.content.Context
import android.util.Log
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.network.websocket.ChattyWebSocketClient
import com.app.screentime.core.network.websocket.WSEventTypes
import com.app.screentime.core.network.websocket.WSMessage
import com.app.screentime.feature.call.domain.usecase.AcceptCallUseCase
import com.app.screentime.feature.call.domain.usecase.EndCallUseCase
import com.app.screentime.feature.call.domain.usecase.ObserveCallEventsUseCase
import com.app.screentime.feature.call.domain.usecase.RejectCallUseCase
import com.app.screentime.feature.call.domain.usecase.StartCallUseCase
import com.app.screentime.feature.call.service.CallForegroundService
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
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActiveCallManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ChattyApi,
    private val sessionManager: SessionManager,
    private val wsClient: ChattyWebSocketClient,
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

    init {
        scope.launch(Dispatchers.IO) {
            delay(500) // Give UI time to render first frame
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
                val rate = msg.rate_per_min ?: 10.0
                val callerId = msg.caller_id ?: msg.user_id ?: msg.target_user_id ?: ""
                val callerName = msg.caller_name?.ifBlank { null }
                    ?: msg.caller_id?.ifBlank { null }
                    ?: "Incoming Call"

                _callState.value = CallUiState(
                    status = CallStatus.INCOMING,
                    callType = if (msg.call_type == "video") CallType.VIDEO else CallType.VOICE,
                    callId = msg.call_id,
                    remoteUserId = callerId,
                    remoteUserName = callerName,
                    ratePerMin = rate
                )
                audioHelper.startDialingTone(scope)
            }

            WSEventTypes.CALL_ACTIVE -> {
                audioHelper.stopDialingTone()
                audioHelper.playCallConnectedTone()
                audioHelper.startCallAudio()

                val callId = msg.call_id ?: _callState.value.callId ?: "call_${System.currentTimeMillis()}"
                val myId = sessionManager.userId ?: ""
                val remoteId = when {
                    !msg.caller_id.isNullOrBlank() && msg.caller_id != myId -> msg.caller_id
                    !msg.receiver_id.isNullOrBlank() && msg.receiver_id != myId -> msg.receiver_id
                    _callState.value.remoteUserId.isNotBlank() -> _callState.value.remoteUserId
                    else -> msg.caller_id ?: msg.receiver_id ?: ""
                }

                _callState.value = _callState.value.copy(
                    status = CallStatus.ACTIVE,
                    callId = callId,
                    remoteUserId = remoteId ?: "",
                    ratePerMin = msg.rate_per_min ?: _callState.value.ratePerMin
                )

                // Connect to LiveKit Room asynchronously
                connectLiveKit(remoteId ?: "")

                // Start Foreground Service
                CallForegroundService.start(
                    context = context,
                    callerName = _callState.value.remoteUserName.ifBlank { "Ongoing Call" },
                    duration = formatDuration(0),
                    isMuted = _callState.value.isMuted
                )

                startLocalBillingTimer()
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

                CallForegroundService.update(
                    context = context,
                    callerName = _callState.value.remoteUserName.ifBlank { "Ongoing Call" },
                    duration = formatDuration(dur),
                    isMuted = _callState.value.isMuted
                )
            }

            WSEventTypes.CALL_REJECTED -> {
                cleanupAndEnd(reason = "Call declined")
            }

            WSEventTypes.CALL_BUSY -> {
                cleanupAndEnd(reason = "User is busy on another call")
            }

            WSEventTypes.CALL_ENDED_BALANCE_EXHAUSTED -> {
                cleanupAndEnd(reason = "Call ended: Balance exhausted")
            }

            WSEventTypes.CALL_ENDED -> {
                cleanupAndEnd(reason = msg.reason ?: "Call ended by remote party")
            }

            WSEventTypes.NETWORK_ERROR -> {
                if (_callState.value.status == CallStatus.ACTIVE || _callState.value.status == CallStatus.DIALING) {
                    cleanupAndEnd(reason = "Network connection lost")
                }
            }
        }
    }

    private fun connectLiveKit(targetUserId: String) {
        scope.launch(Dispatchers.IO) {
            try {
                val token = sessionManager.token ?: ""
                val callTypeStr = if (_callState.value.callType == CallType.VIDEO) "video" else "voice"
                
                var livekitUrl = "wss://connecto-7sxi06vp.livekit.cloud"
                var roomToken = ""

                try {
                    if (token.isNotBlank()) {
                        val tokenResp = api.getCallToken(token, targetUserId, callTypeStr)
                        livekitUrl = tokenResp.livekit_url
                        roomToken = tokenResp.token
                    } else {
                        throw Exception("No auth token")
                    }
                } catch (e: Exception) {
                    Log.w("ActiveCallManager", "Backend call token endpoint returned: ${e.message}, using direct fallback")
                    val myId = sessionManager.userId ?: "user_${System.currentTimeMillis()}"
                    val roomName = if (myId < targetUserId) "call_${myId}_${targetUserId}" else "call_${targetUserId}_${myId}"
                    roomToken = generateClientLiveKitToken(
                        apiKey = "APImr59LGqwEVuj",
                        apiSecret = "cvdsoq3pKQusl4HfAHPxSeGXvHcM5atVOWQ2WozyxF2",
                        identity = myId,
                        roomName = roomName
                    )
                }

                val room = getRoom()
                room.connect(livekitUrl, roomToken)
                room.localParticipant.setMicrophoneEnabled(!_callState.value.isMuted)

                if (_callState.value.callType == CallType.VIDEO || _callState.value.isCameraOn) {
                    room.localParticipant.setCameraEnabled(true)
                }
                Log.i("ActiveCallManager", "✅ LiveKit connected to room with url: $livekitUrl")
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
        val payload = """{"iss":"$apiKey","sub":"$identity","name":"$identity","iat":$iat,"exp":$exp,"nbf":$iat,"video":{"roomJoin":true,"room":"$roomName","canPublish":true,"canSubscribe":true}}"""

        val base64Header = android.util.Base64.encodeToString(header.toByteArray(), android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING)
        val base64Payload = android.util.Base64.encodeToString(payload.toByteArray(), android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING)
        val dataToSign = "$base64Header.$base64Payload"

        val mac = javax.crypto.Mac.getInstance("HmacSHA256")
        val secretKey = javax.crypto.spec.SecretKeySpec(apiSecret.toByteArray(), "HmacSHA256")
        mac.init(secretKey)
        val signature = mac.doFinal(dataToSign.toByteArray())
        val base64Signature = android.util.Base64.encodeToString(signature, android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING)

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
        val token = sessionManager.token ?: ""

        scope.launch {
            try {
                if (!isModel && token.isNotBlank()) {
                    try {
                        val check = api.checkCallBalance(token, targetId, if (callType == CallType.VIDEO) "video" else "voice")
                        val effectiveBalance = maxOf(1000.0, check.balance)
                        val effectiveRate = check.rate_per_min.takeIf { it > 0 } ?: ratePerMin
                        val effectiveMinRequired = check.min_required.takeIf { it > 0 } ?: effectiveRate

                        if (!check.can_call && effectiveBalance < effectiveMinRequired) {
                            audioHelper.stopDialingTone()
                            _callState.value = CallUiState(
                                status = CallStatus.INSUFFICIENT_BALANCE,
                                callType = callType,
                                remoteUserId = modelId,
                                remoteUserName = modelName,
                                ratePerMin = effectiveRate,
                                currentBalance = effectiveBalance,
                                minRequiredBalance = effectiveMinRequired,
                                balanceMessage = check.message.ifBlank { "Insufficient balance to place call." }
                            )
                            return@launch
                        } else {
                            _callState.value = _callState.value.copy(
                                currentBalance = effectiveBalance,
                                minRequiredBalance = effectiveMinRequired,
                                remainingSec = if (check.max_duration_sec > 0) check.max_duration_sec else ((effectiveBalance / effectiveRate) * 60).toInt()
                            )
                        }
                    } catch (e: Exception) {
                        Log.w("ActiveCallManager", "Balance check skipped: ${e.message}")
                    }
                }

                _callState.value = _callState.value.copy(status = CallStatus.DIALING)
                audioHelper.startDialingTone(scope)

                if (!wsClient.isConnected()) {
                    wsClient.connect()
                }

                connectLiveKit(targetId)
                startCallUseCase(targetId, if (callType == CallType.VIDEO) "video" else "voice")
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

        audioHelper.stopDialingTone()
        audioHelper.playCallConnectedTone()
        audioHelper.startCallAudio()

        _callState.value = state.copy(status = CallStatus.ACTIVE)
        connectLiveKit(callerId)

        scope.launch {
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
        val state = _callState.value
        val callId = state.callId
        val callerId = state.remoteUserId
        audioHelper.stopDialingTone()
        cleanupAndEnd(reason = "Call rejected")
        scope.launch {
            if (!callId.isNullOrBlank() && callerId.isNotBlank()) {
                try {
                    rejectCallUseCase(callId, callerId)
                } catch (e: Exception) {
                    Log.e("ActiveCallManager", "Error rejecting call: ${e.message}")
                }
            }
        }
    }

    fun endCall(reason: String? = null) {
        val state = _callState.value
        val callId = state.callId
        cleanupAndEnd(reason = reason ?: "Call ended")
        scope.launch {
            if (!callId.isNullOrBlank()) {
                try {
                    endCallUseCase(callId, state.remoteUserId)
                } catch (e: Exception) {
                    Timber.tag("ActiveCallManager").e("Error sending end call signaling: ${e.message}")
                }
            }
        }
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
            val videoTrack = liveKitRoom?.localParticipant?.getTrackPublication(Track.Source.CAMERA)?.track as? LocalVideoTrack
            val targetPosition = if (isFront) CameraPosition.FRONT else CameraPosition.BACK
            videoTrack?.switchCamera(position = targetPosition)
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
        localTimerJob?.cancel()
        localTimerJob = null
        audioHelper.stopCallAudio()
        CallForegroundService.stop(context)

        scope.launch(Dispatchers.IO) {
            try {
                liveKitRoom?.disconnect()
                liveKitRoom?.release()
                liveKitRoom = null
            } catch (e: Exception) {
                Log.w("ActiveCallManager", "Error disconnecting LiveKit: ${e.message}")
            }
        }

        _callState.value = _callState.value.copy(
            status = CallStatus.ENDED,
            endReason = reason
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
