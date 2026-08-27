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
import com.app.screentime.feature.call.webrtc.WebRTCClient
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val observeCallEventsUseCase: ObserveCallEventsUseCase,
    private val webRTCClient: WebRTCClient
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val audioHelper = AudioManagerHelper(context)

    private val _callState = MutableStateFlow(CallUiState())
    val callState: StateFlow<CallUiState> = _callState.asStateFlow()

    private var localTimerJob: Job? = null

    init {
        webRTCClient.initialize(context)
        webRTCClient.onIceConnectionFailed = {
            scope.launch {
                if (_callState.value.status == CallStatus.ACTIVE) {
                    Log.w("ActiveCallManager", "WebRTC ICE Connection Failed -> ending call")
                    endCall("Connection lost")
                }
            }
        }
        observeWebSocketEvents()
        ensureConnected()
    }

    fun ensureConnected() {
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
    }

    private fun observeWebSocketEvents() {
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
                val callerName = msg.caller_name?.ifBlank { null } ?: msg.caller_id ?: "Incoming Call"
                _callState.value = CallUiState(
                    status = CallStatus.INCOMING,
                    callId = msg.call_id,
                    remoteUserId = msg.caller_id ?: "",
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
                val remoteId = msg.receiver_id.takeIf { !it.isNullOrBlank() && it != _callState.value.remoteUserId }
                    ?: msg.caller_id ?: _callState.value.remoteUserId

                val isCaller = _callState.value.status == CallStatus.DIALING || msg.caller_id != remoteId

                _callState.value = _callState.value.copy(
                    status = CallStatus.ACTIVE,
                    callId = callId,
                    ratePerMin = msg.rate_per_min ?: _callState.value.ratePerMin
                )

                // Start Real WebRTC Peer Connection
                webRTCClient.startPeerConnection(callId, remoteId, isCaller)

                // Start Foreground Service
                CallForegroundService.start(
                    context = context,
                    callerName = _callState.value.remoteUserName.ifBlank { "Ongoing Call" },
                    duration = formatDuration(0),
                    isMuted = _callState.value.isMuted
                )

                startLocalBillingTimer()
            }

            WSEventTypes.WEBRTC_OFFER -> {
                msg.payload?.let {
                    val rawStr = if (it is kotlinx.serialization.json.JsonPrimitive) it.content else it.toString()
                    webRTCClient.handleRemoteOffer(rawStr)
                }
            }

            WSEventTypes.WEBRTC_ANSWER -> {
                msg.payload?.let {
                    val rawStr = if (it is kotlinx.serialization.json.JsonPrimitive) it.content else it.toString()
                    webRTCClient.handleRemoteAnswer(rawStr)
                }
            }

            WSEventTypes.WEBRTC_ICE_CANDIDATE -> {
                msg.payload?.let {
                    val rawStr = if (it is kotlinx.serialization.json.JsonPrimitive) it.content else it.toString()
                    webRTCClient.handleRemoteIceCandidate(rawStr)
                }
            }

            WSEventTypes.CALL_TICK -> {
                val dur = msg.duration_sec ?: _callState.value.durationSec
                val rem = msg.remaining_sec ?: _callState.value.remainingSec
                val cost = msg.cost ?: _callState.value.cost

                _callState.value = _callState.value.copy(
                    durationSec = dur,
                    remainingSec = rem,
                    cost = cost,
                    isLowBalanceWarning = rem in 1..30
                )

                CallForegroundService.update(
                    context = context,
                    callerName = _callState.value.remoteUserName.ifBlank { "Ongoing Call" },
                    duration = formatDuration(dur),
                    isMuted = _callState.value.isMuted
                )

                if (rem <= 0) {
                    endCall("Balance exhausted")
                }
            }

            WSEventTypes.BALANCE_LOW_WARNING -> {
                _callState.value = _callState.value.copy(
                    isLowBalanceWarning = true,
                    remainingSec = msg.remaining_sec ?: _callState.value.remainingSec
                )
            }

            WSEventTypes.CALL_INSUFFICIENT_BALANCE -> {
                audioHelper.stopDialingTone()
                audioHelper.stopCallAudio()
                webRTCClient.close()
                CallForegroundService.stop(context)

                _callState.value = _callState.value.copy(
                    status = CallStatus.INSUFFICIENT_BALANCE,
                    endReason = msg.reason ?: "Insufficient wallet balance",
                    balanceMessage = msg.reason ?: "Insufficient balance to place call.",
                    ratePerMin = msg.rate_per_min ?: _callState.value.ratePerMin,
                    currentBalance = msg.cost ?: _callState.value.currentBalance
                )
            }

            WSEventTypes.CALL_REJECTED -> {
                cleanupAndEnd(reason = msg.reason ?: "Call Declined")
            }

            WSEventTypes.CALL_BUSY -> {
                cleanupAndEnd(reason = msg.reason ?: "Model is busy on another call")
            }

            WSEventTypes.CALL_OFFLINE -> {
                cleanupAndEnd(reason = msg.reason ?: "Model is currently offline")
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

    /**
     * Initiates a real call with pre-call wallet balance check.
     */
    fun startOutgoingCall(modelId: String, modelName: String, ratePerMin: Double = 10.0) {
        // 1. Initial State: Set immediately to DIALING so UI opens and tone starts
        _callState.value = CallUiState(
            status = CallStatus.DIALING,
            remoteUserId = modelId,
            remoteUserName = modelName,
            ratePerMin = ratePerMin
        )
        audioHelper.startDialingTone(scope)

        val token = sessionManager.token ?: ""

        scope.launch {
            try {
                // 2. Perform Pre-Call Balance Check against Server if token is present
                if (token.isNotBlank()) {
                    val check = api.checkCallBalance(token, modelId, "voice")
                    if (!check.can_call) {
                        Log.w("ActiveCallManager", "Pre-call balance check failed: ${check.message}")
                        audioHelper.stopDialingTone()
                        _callState.value = CallUiState(
                            status = CallStatus.INSUFFICIENT_BALANCE,
                            remoteUserId = modelId,
                            remoteUserName = modelName,
                            ratePerMin = check.rate_per_min.takeIf { it > 0 } ?: ratePerMin,
                            currentBalance = check.balance,
                            minRequiredBalance = check.min_required.takeIf { it > 0 } ?: ratePerMin,
                            balanceMessage = check.message.ifBlank { "Insufficient balance to place call." }
                        )
                        return@launch
                    } else {
                        _callState.value = _callState.value.copy(
                            currentBalance = check.balance,
                            minRequiredBalance = check.min_required.takeIf { it > 0 } ?: ratePerMin,
                            remainingSec = check.max_duration_sec
                        )
                    }
                }

                // 3. Balance is sufficient -> Ensure WS connection & initiate call
                if (!wsClient.isConnected()) {
                    wsClient.connect()
                }
                startCallUseCase(modelId)
            } catch (e: Exception) {
                Log.e("ActiveCallManager", "Error in startOutgoingCall: ${e.message}")
                // In case of balance check network issue, still proceed to request call via WebSocket
                if (!wsClient.isConnected()) {
                    wsClient.connect()
                }
                startCallUseCase(modelId)
            }
        }
    }

    fun acceptIncomingCall() {
        val callId = _callState.value.callId ?: return
        audioHelper.stopDialingTone()
        audioHelper.playCallConnectedTone()
        audioHelper.startCallAudio()
        acceptCallUseCase(callId)
        _callState.value = _callState.value.copy(status = CallStatus.ACTIVE)
        webRTCClient.startPeerConnection(callId, _callState.value.remoteUserId, false)

        CallForegroundService.start(
            context = context,
            callerName = _callState.value.remoteUserName.ifBlank { "Incoming Call" },
            duration = "00:00",
            isMuted = false
        )

        startLocalBillingTimer()
    }

    fun rejectIncomingCall() {
        val callId = _callState.value.callId ?: ""
        val callerId = _callState.value.remoteUserId
        audioHelper.stopCallAudio()
        audioHelper.stopDialingTone()
        webRTCClient.close()
        rejectCallUseCase(callId, callerId)
        _callState.value = CallUiState(status = CallStatus.IDLE)
        CallForegroundService.stop(context)
    }

    fun endCall(reason: String? = null) {
        val callId = _callState.value.callId ?: ""
        if (callId.isNotBlank()) {
            endCallUseCase(callId)
        }
        cleanupAndEnd(reason = reason ?: "Call ended")
    }

    fun toggleMute() {
        val next = !_callState.value.isMuted
        audioHelper.setMuted(next)
        webRTCClient.setMuted(next)
        _callState.value = _callState.value.copy(isMuted = next)

        CallForegroundService.update(
            context = context,
            callerName = _callState.value.remoteUserName.ifBlank { "Ongoing Call" },
            duration = formatDuration(_callState.value.durationSec),
            isMuted = next
        )
    }

    fun toggleSpeaker() {
        val next = !_callState.value.isSpeaker
        audioHelper.setSpeaker(next)
        _callState.value = _callState.value.copy(isSpeaker = next)
    }

    private fun cleanupAndEnd(reason: String) {
        stopLocalTimer()
        audioHelper.stopCallAudio()
        audioHelper.stopDialingTone()
        webRTCClient.close()
        CallForegroundService.stop(context)

        _callState.value = _callState.value.copy(
            status = CallStatus.ENDED,
            endReason = reason
        )
    }

    private fun startLocalBillingTimer() {
        stopLocalTimer()
        localTimerJob = scope.launch {
            while (true) {
                delay(1000)
                val current = _callState.value
                if (current.status == CallStatus.ACTIVE) {
                    val nextDur = current.durationSec + 1
                    val ratePerSec = current.ratePerMin / 60.0
                    val cost = nextDur * ratePerSec
                    val nextRemaining = (current.remainingSec - 1).coerceAtLeast(0)

                    _callState.value = current.copy(
                        durationSec = nextDur,
                        remainingSec = nextRemaining,
                        cost = cost,
                        isLowBalanceWarning = nextRemaining in 1..30
                    )

                    CallForegroundService.update(
                        context = context,
                        callerName = current.remoteUserName.ifBlank { "Ongoing Call" },
                        duration = formatDuration(nextDur),
                        isMuted = current.isMuted
                    )

                    if (nextRemaining <= 0) {
                        endCall("Balance exhausted")
                        break
                    }
                } else {
                    break
                }
            }
        }
    }

    private fun stopLocalTimer() {
        localTimerJob?.cancel()
        localTimerJob = null
    }

    private fun formatDuration(sec: Int): String {
        val mins = sec / 60
        val s = sec % 60
        return "%02d:%02d".format(mins, s)
    }

    fun resetState() {
        _callState.value = CallUiState()
    }
}

