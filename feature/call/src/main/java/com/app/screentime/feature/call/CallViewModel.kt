package com.app.screentime.feature.call

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.network.websocket.WSEventTypes
import com.app.screentime.feature.call.domain.usecase.*
import com.app.screentime.feature.call.webrtc.WebRTCClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val startCallUseCase: StartCallUseCase,
    private val acceptCallUseCase: AcceptCallUseCase,
    private val rejectCallUseCase: RejectCallUseCase,
    private val endCallUseCase: EndCallUseCase,
    private val observeCallEventsUseCase: ObserveCallEventsUseCase,
    private val webRTCClient: WebRTCClient
) : ViewModel() {

    private val audioHelper = AudioManagerHelper(context)

    private val _callState = MutableStateFlow(CallUiState())
    val callState: StateFlow<CallUiState> = _callState.asStateFlow()

    private var localTimerJob: Job? = null

    init {
        webRTCClient.initialize(context)
        observeWebSocketEvents()
    }

    private fun observeWebSocketEvents() {
        viewModelScope.launch {
            observeCallEventsUseCase().collectLatest { msg ->
                when (msg.type) {
                    WSEventTypes.INCOMING_CALL -> {
                        _callState.value = CallUiState(
                            status = CallStatus.INCOMING,
                            callId = msg.call_id,
                            remoteUserId = msg.caller_id ?: "",
                            remoteUserName = msg.caller_id ?: "Incoming Call",
                            ratePerMin = msg.rate_per_min ?: 10.0
                        )
                        audioHelper.startDialingTone(viewModelScope)
                    }

                    WSEventTypes.CALL_ACTIVE -> {
                        audioHelper.stopDialingTone()
                        audioHelper.playCallConnectedTone()
                        audioHelper.startCallAudio()
                        val callId = msg.call_id ?: _callState.value.callId ?: "call_${System.currentTimeMillis()}"
                        val remoteId = msg.receiver_id.takeIf { it != _callState.value.remoteUserId } ?: msg.caller_id ?: _callState.value.remoteUserId

                        _callState.value = _callState.value.copy(
                            status = CallStatus.ACTIVE,
                            callId = callId,
                            ratePerMin = msg.rate_per_min ?: _callState.value.ratePerMin
                        )

                        // Start P2P WebRTC Audio streaming
                        val isCaller = _callState.value.status == CallStatus.DIALING || msg.caller_id != remoteId
                        webRTCClient.startPeerConnection(callId, remoteId, isCaller)

                        startLocalFallbackTimer()
                    }

                    WSEventTypes.WEBRTC_OFFER -> {
                        msg.payload?.let { webRTCClient.handleRemoteOffer(it) }
                    }

                    WSEventTypes.WEBRTC_ANSWER -> {
                        msg.payload?.let { webRTCClient.handleRemoteAnswer(it) }
                    }

                    WSEventTypes.WEBRTC_ICE_CANDIDATE -> {
                        msg.payload?.let { webRTCClient.handleRemoteIceCandidate(it) }
                    }

                    WSEventTypes.CALL_TICK -> {
                        _callState.value = _callState.value.copy(
                            durationSec = msg.duration_sec ?: _callState.value.durationSec,
                            remainingSec = msg.remaining_sec ?: _callState.value.remainingSec,
                            cost = msg.cost ?: _callState.value.cost
                        )
                    }

                    WSEventTypes.BALANCE_LOW_WARNING -> {
                        _callState.value = _callState.value.copy(
                            isLowBalanceWarning = true,
                            remainingSec = msg.remaining_sec ?: _callState.value.remainingSec
                        )
                    }

                    WSEventTypes.CALL_REJECTED -> {
                        audioHelper.stopCallAudio()
                        webRTCClient.close()
                        stopLocalTimer()
                        _callState.value = _callState.value.copy(
                            status = CallStatus.ENDED,
                            endReason = msg.reason ?: "Call Declined"
                        )
                    }

                    WSEventTypes.CALL_BUSY -> {
                        audioHelper.stopCallAudio()
                        webRTCClient.close()
                        stopLocalTimer()
                        _callState.value = _callState.value.copy(
                            status = CallStatus.ENDED,
                            endReason = "User is on another call"
                        )
                    }

                    WSEventTypes.CALL_INSUFFICIENT_BALANCE -> {
                        audioHelper.stopCallAudio()
                        webRTCClient.close()
                        stopLocalTimer()
                        _callState.value = _callState.value.copy(
                            status = CallStatus.ENDED,
                            endReason = msg.reason ?: "Insufficient balance"
                        )
                    }

                    WSEventTypes.CALL_ENDED, WSEventTypes.CALL_ENDED_BALANCE_EXHAUSTED -> {
                        audioHelper.stopCallAudio()
                        webRTCClient.close()
                        stopLocalTimer()
                        _callState.value = _callState.value.copy(
                            status = CallStatus.ENDED,
                            durationSec = msg.duration_sec ?: _callState.value.durationSec,
                            cost = msg.cost ?: _callState.value.cost,
                            endReason = if (msg.type == WSEventTypes.CALL_ENDED_BALANCE_EXHAUSTED) {
                                "Call ended: Balance exhausted"
                            } else {
                                msg.reason ?: "Call ended"
                            }
                        )
                    }
                }
            }
        }
    }

    fun startOutgoingCall(modelId: String, modelName: String, ratePerMin: Double = 10.0) {
        _callState.value = CallUiState(
            status = CallStatus.DIALING,
            remoteUserId = modelId,
            remoteUserName = modelName,
            ratePerMin = ratePerMin
        )
        audioHelper.startDialingTone(viewModelScope)

        startCallUseCase(modelId)

        // Local simulation / fallback for offline test
        viewModelScope.launch {
            delay(3500)
            if (_callState.value.status == CallStatus.DIALING) {
                audioHelper.stopDialingTone()
                audioHelper.playCallConnectedTone()
                audioHelper.startCallAudio()
                val callId = "call_${System.currentTimeMillis()}"
                _callState.value = _callState.value.copy(
                    status = CallStatus.ACTIVE,
                    callId = callId
                )
                webRTCClient.startPeerConnection(callId, modelId, true)
                startLocalFallbackTimer()
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
        startLocalFallbackTimer()
    }

    fun rejectIncomingCall() {
        val callId = _callState.value.callId ?: ""
        val callerId = _callState.value.remoteUserId
        audioHelper.stopCallAudio()
        webRTCClient.close()
        rejectCallUseCase(callId, callerId)
        _callState.value = CallUiState(status = CallStatus.IDLE)
    }

    fun endCall() {
        val callId = _callState.value.callId ?: ""
        audioHelper.stopCallAudio()
        webRTCClient.close()
        stopLocalTimer()
        endCallUseCase(callId)
        _callState.value = _callState.value.copy(status = CallStatus.ENDED)
    }

    fun toggleMute() {
        val next = !_callState.value.isMuted
        audioHelper.setMuted(next)
        webRTCClient.setMuted(next)
        _callState.value = _callState.value.copy(isMuted = next)
    }

    fun toggleSpeaker() {
        val next = !_callState.value.isSpeaker
        audioHelper.setSpeaker(next)
        _callState.value = _callState.value.copy(isSpeaker = next)
    }

    private fun startLocalFallbackTimer() {
        stopLocalTimer()
        localTimerJob = viewModelScope.launch {
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
                    if (nextRemaining <= 0) {
                        endCall()
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

    override fun onCleared() {
        super.onCleared()
        audioHelper.release()
        webRTCClient.close()
        stopLocalTimer()
    }
}
