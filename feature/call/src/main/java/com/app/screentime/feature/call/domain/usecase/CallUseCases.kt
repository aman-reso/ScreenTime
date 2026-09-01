package com.app.screentime.feature.call.domain.usecase

import com.app.screentime.core.network.NetworkAuthBridge
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.network.websocket.ChattyWebSocketClient
import com.app.screentime.core.network.websocket.WSMessage
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class StartCallUseCase @Inject constructor(
    private val wsClient: ChattyWebSocketClient,
    private val sessionManager: SessionManager
) {
    operator fun invoke(receiverId: String, callType: String = "voice"): Boolean {
        if (!sessionManager.hasValidSession()) {
            sessionManager.clearSession()
            NetworkAuthBridge.unauthorizedHandler?.onUnauthorized()
            return false
        }
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
        wsClient.requestCall(receiverId, callType)
        return true
    }
}

class AcceptCallUseCase @Inject constructor(
    private val wsClient: ChattyWebSocketClient
) {
    operator fun invoke(callId: String, callerId: String? = null) {
        wsClient.acceptCall(callId, callerId)
    }
}

class RejectCallUseCase @Inject constructor(
    private val wsClient: ChattyWebSocketClient
) {
    operator fun invoke(callId: String, callerId: String) {
        wsClient.rejectCall(callId, callerId)
    }
}

class EndCallUseCase @Inject constructor(
    private val wsClient: ChattyWebSocketClient
) {
    operator fun invoke(callId: String, peerId: String? = null) {
        wsClient.endCall(callId, peerId)
    }
}

class ObserveCallEventsUseCase @Inject constructor(
    private val wsClient: ChattyWebSocketClient
) {
    operator fun invoke(): SharedFlow<WSMessage> {
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
        return wsClient.eventsFlow
    }
}
