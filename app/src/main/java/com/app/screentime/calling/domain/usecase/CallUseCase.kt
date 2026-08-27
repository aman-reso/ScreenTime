package com.app.screentime.calling.domain.usecase

import com.app.screentime.calling.data.model.CallMessageTypes
import com.app.screentime.calling.data.model.CallSocketMessage
import com.app.screentime.calling.domain.model.CallSession
import com.app.screentime.calling.domain.model.CallState
import com.app.screentime.calling.domain.repository.CallRepository
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CallUseCase @Inject constructor(
    private val repository: CallRepository,
    private val billingHandler: BillingTickHandler,
    private val api: ChattyApi,
    private val sessionManager: SessionManager
) {
    val callState: StateFlow<CallState> = repository.callState
    val incomingEvents: Flow<CallSocketMessage> = repository.incomingEvents

    suspend fun startCall(receiverId: String, receiverName: String, ratePerMin: Double, callType: String = "voice") {
        val token = sessionManager.token
        if (!token.isNullOrBlank()) {
            try {
                val check = api.checkCallBalance(token, receiverId, callType)
                if (!check.can_call) {
                    val errorMsg = check.message.ifBlank { "Insufficient balance to place call. Please recharge." }
                    repository.setCallState(CallState.Ended(errorMsg, null))
                    return
                }
            } catch (e: Exception) {
                // Fallback to initiating if check endpoint times out
            }
        }
        repository.requestCall(receiverId, receiverName, ratePerMin)
    }

    suspend fun acceptCall(callId: String, callerId: String, callerName: String, rate: Double) {
        val session = CallSession(
            callId = callId,
            peerId = callerId,
            peerName = callerName,
            isIncoming = true,
            ratePerMin = rate
        )
        repository.setCallState(CallState.Active(session))
        repository.acceptCall(callId, callerId)
    }

    suspend fun declineCall(callId: String, callerId: String) {
        repository.rejectCall(callId, callerId)
    }

    suspend fun endCurrentCall() {
        val current = repository.callState.value
        if (current is CallState.Active) {
            repository.endCall(current.session.callId, current.session.peerId)
        } else {
            repository.setCallState(CallState.Idle)
        }
    }

    fun handleIncomingSignaling(msg: CallSocketMessage) {
        val current = repository.callState.value
        when (msg.type) {
            CallMessageTypes.INCOMING_CALL -> {
                val callId = msg.callId.orEmpty()
                val callerId = msg.callerId.orEmpty()
                val rate = msg.ratePerMin ?: 0.0
                repository.setCallState(
                    CallState.IncomingRinging(
                        callId = callId,
                        callerId = callerId,
                        callerName = "Creator $callerId",
                        callerAvatar = null,
                        ratePerMin = rate
                    )
                )
            }
            CallMessageTypes.CALL_ACTIVE -> {
                if (current is CallState.OutgoingRinging) {
                    val session = CallSession(
                        callId = msg.callId.orEmpty(),
                        peerId = current.peerId,
                        peerName = current.peerName,
                        ratePerMin = current.ratePerMin
                    )
                    repository.setCallState(CallState.Active(session))
                }
            }
            CallMessageTypes.CALL_TICK -> {
                val nextState = billingHandler.processTick(msg, current)
                repository.setCallState(nextState)
            }
            CallMessageTypes.BALANCE_LOW_WARNING -> {
                if (current is CallState.Active) {
                    repository.setCallState(
                        current.copy(
                            isLowBalance = true,
                            warningMessage = msg.reason ?: "Low balance! Call will end soon."
                        )
                    )
                }
            }
            CallMessageTypes.BALANCE_EXHAUSTED -> {
                val session = if (current is CallState.Active) current.session else null
                repository.setCallState(CallState.Ended("Balance exhausted", session))
            }
            CallMessageTypes.CALL_REJECTED -> {
                repository.setCallState(CallState.Ended("Call was declined", null))
            }
            CallMessageTypes.CALL_ENDED -> {
                val session = if (current is CallState.Active) current.session else null
                repository.setCallState(CallState.Ended(msg.reason ?: "Call ended", session))
            }
            CallMessageTypes.SESSION_TERMINATED -> {
                repository.setCallState(CallState.Error("Logged in from another device"))
            }
        }
    }

    fun toggleAudioMute() {
        repository.updateSession { it.copy(isAudioMuted = !it.isAudioMuted) }
    }

    fun toggleSpeaker() {
        repository.updateSession { it.copy(isSpeakerOn = !it.isSpeakerOn) }
    }
}
