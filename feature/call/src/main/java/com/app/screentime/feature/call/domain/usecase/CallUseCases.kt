package com.app.screentime.feature.call.domain.usecase

import com.app.screentime.core.network.websocket.ChattyWebSocketClient
import com.app.screentime.core.network.websocket.WSMessage
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class StartCallUseCase @Inject constructor(
    private val wsClient: ChattyWebSocketClient
) {
    operator fun invoke(receiverId: String) {
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
        wsClient.requestCall(receiverId)
    }
}

class AcceptCallUseCase @Inject constructor(
    private val wsClient: ChattyWebSocketClient
) {
    operator fun invoke(callId: String) {
        wsClient.acceptCall(callId)
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
    operator fun invoke(callId: String) {
        wsClient.endCall(callId)
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
