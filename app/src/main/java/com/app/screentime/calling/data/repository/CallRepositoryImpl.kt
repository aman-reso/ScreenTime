package com.app.screentime.calling.data.repository

import com.app.screentime.calling.data.model.CallMessageTypes
import com.app.screentime.calling.data.model.CallSocketMessage
import com.app.screentime.calling.data.socket.ConnectWebSocketClient
import com.app.screentime.calling.domain.model.CallSession
import com.app.screentime.calling.domain.model.CallState
import com.app.screentime.calling.domain.repository.CallRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallRepositoryImpl @Inject constructor(
    private val socketClient: ConnectWebSocketClient
) : CallRepository {

    private val _callState = MutableStateFlow<CallState>(CallState.Idle)
    override val callState: StateFlow<CallState> = _callState.asStateFlow()

    override val incomingEvents: Flow<CallSocketMessage> = socketClient.incomingMessages

    override suspend fun connect(baseUrl: String, token: String) {
        socketClient.connect(baseUrl, token)
    }

    override suspend fun disconnect() {
        socketClient.disconnect()
        _callState.value = CallState.Idle
    }

    override suspend fun requestCall(receiverId: String, receiverName: String, ratePerMin: Double) {
        _callState.value = CallState.OutgoingRinging(receiverId, receiverName, ratePerMin)
        socketClient.sendMessage(
            CallSocketMessage(
                type = CallMessageTypes.CALL_REQUEST,
                receiverId = receiverId,
                ratePerMin = ratePerMin
            )
        )
    }

    override suspend fun acceptCall(callId: String, callerId: String) {
        socketClient.sendMessage(
            CallSocketMessage(
                type = CallMessageTypes.CALL_ACCEPT,
                callId = callId,
                callerId = callerId
            )
        )
    }

    override suspend fun rejectCall(callId: String, callerId: String) {
        socketClient.sendMessage(
            CallSocketMessage(
                type = CallMessageTypes.CALL_REJECT,
                callId = callId,
                callerId = callerId,
                reason = "Call declined"
            )
        )
        _callState.value = CallState.Idle
    }

    override suspend fun endCall(callId: String, peerId: String) {
        socketClient.sendMessage(
            CallSocketMessage(
                type = CallMessageTypes.CALL_END,
                callId = callId,
                receiverId = peerId,
                reason = "Call ended by user"
            )
        )
        val current = _callState.value
        val session = if (current is CallState.Active) current.session else null
        _callState.value = CallState.Ended("Call ended", session)
    }

    override suspend fun sendSdpOffer(targetUserId: String, callId: String, sdpJson: String) {
        socketClient.sendMessage(
            CallSocketMessage(
                type = CallMessageTypes.WEBRTC_OFFER,
                callId = callId,
                receiverId = targetUserId
            )
        )
    }

    override suspend fun sendSdpAnswer(targetUserId: String, callId: String, sdpJson: String) {
        socketClient.sendMessage(
            CallSocketMessage(
                type = CallMessageTypes.WEBRTC_ANSWER,
                callId = callId,
                receiverId = targetUserId
            )
        )
    }

    override suspend fun sendIceCandidate(targetUserId: String, callId: String, candidateJson: String) {
        socketClient.sendMessage(
            CallSocketMessage(
                type = CallMessageTypes.WEBRTC_ICE_CANDIDATE,
                callId = callId,
                receiverId = targetUserId
            )
        )
    }

    override fun updateSession(transform: (CallSession) -> CallSession) {
        val current = _callState.value
        if (current is CallState.Active) {
            _callState.value = current.copy(session = transform(current.session))
        }
    }

    override fun setCallState(state: CallState) {
        _callState.value = state
    }
}
