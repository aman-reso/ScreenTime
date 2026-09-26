package com.app.screentime.feature.call.domain.usecase

import com.app.screentime.core.network.NetworkAuthBridge
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.network.websocket.WinterWebSocketClient
import com.app.screentime.core.network.websocket.WSMessage
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class StartCallUseCase @Inject constructor(
    private val wsClient: WinterWebSocketClient,
    private val sessionManager: SessionManager
) {
    operator fun invoke(
        receiverId: String,
        callType: String = "voice",
        sessionId: String? = null
    ): Boolean {
        if (!sessionManager.hasValidSession()) {
            sessionManager.clearSession()
            NetworkAuthBridge.unauthorizedHandler?.onUnauthorized()
            return false
        }
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
        if (!sessionId.isNullOrBlank()) {
            wsClient.initiateCall(targetId = receiverId, sessionId = sessionId, callType = callType)
        } else {
            wsClient.requestCall(receiverId, callType)
        }
        return true
    }
}

class AcceptCallUseCase @Inject constructor(
    private val wsClient: WinterWebSocketClient
) {
    operator fun invoke(callId: String, callerId: String? = null) {
        if (!callerId.isNullOrBlank()) {
            wsClient.acceptCall(targetId = callerId, sessionId = callId)
        } else {
            wsClient.acceptCall(callId, callerId ?: "")
        }
    }
}

class RejectCallUseCase @Inject constructor(
    private val wsClient: WinterWebSocketClient
) {
    operator fun invoke(callId: String, callerId: String) {
        wsClient.rejectCall(targetId = callerId, sessionId = callId)
    }
}

class EndCallUseCase @Inject constructor(
    private val wsClient: WinterWebSocketClient
) {
    operator fun invoke(callId: String, peerId: String? = null) {
        if (!peerId.isNullOrBlank()) {
            wsClient.endCall(targetId = peerId, sessionId = callId)
        } else {
            wsClient.endCall(callId, peerId ?: "")
        }
    }
}

class ObserveCallEventsUseCase @Inject constructor(
    private val wsClient: WinterWebSocketClient
) {
    operator fun invoke(): SharedFlow<WSMessage> {
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
        return wsClient.eventsFlow
    }
}
