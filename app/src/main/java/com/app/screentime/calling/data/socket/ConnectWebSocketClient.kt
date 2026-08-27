package com.app.screentime.calling.data.socket

import com.app.screentime.calling.data.model.CallSocketMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectWebSocketClient @Inject constructor(
    private val httpClient: HttpClient
) {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    private var session: DefaultClientWebSocketSession? = null
    private var listenJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    private val _incomingMessages = MutableSharedFlow<CallSocketMessage>(extraBufferCapacity = 64)
    val incomingMessages: SharedFlow<CallSocketMessage> = _incomingMessages.asSharedFlow()

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    suspend fun connect(baseUrl: String, token: String) {
        if (_connectionState.value is ConnectionState.Connected) return
        _connectionState.value = ConnectionState.Connecting

        try {
            val wsUrl = buildWsUrl(baseUrl, token)
            val newSession = httpClient.webSocketSession { url(wsUrl) }
            session = newSession
            _connectionState.value = ConnectionState.Connected

            listenJob?.cancel()
            listenJob = scope.launch { listenIncomingFrames(newSession) }
        } catch (e: Exception) {
            _connectionState.value = ConnectionState.Error(e.message ?: "Connection failed")
            disconnect()
        }
    }

    private suspend fun listenIncomingFrames(wsSession: DefaultClientWebSocketSession) {
        try {
            for (frame in wsSession.incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    try {
                        val message = json.decodeFromString<CallSocketMessage>(text)
                        _incomingMessages.emit(message)
                    } catch (_: Exception) {}
                }
            }
        } catch (_: Exception) {
        } finally {
            _connectionState.value = ConnectionState.Disconnected
        }
    }

    suspend fun sendMessage(msg: CallSocketMessage): Boolean {
        return try {
            val activeSession = session
            if (activeSession != null && activeSession.isActive) {
                val text = json.encodeToString(msg)
                activeSession.send(Frame.Text(text))
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun disconnect() {
        listenJob?.cancel()
        listenJob = null
        try {
            session?.close()
        } catch (_: Exception) {}
        session = null
        _connectionState.value = ConnectionState.Disconnected
    }

    private fun buildWsUrl(baseUrl: String, token: String): String {
        val scheme = if (baseUrl.startsWith("https://")) "wss://" else "ws://"
        val cleanHost = baseUrl.removePrefix("https://").removePrefix("http://").removeSuffix("/")
        return "$scheme$cleanHost/ws?token=$token"
    }

    sealed interface ConnectionState {
        data object Disconnected : ConnectionState
        data object Connecting : ConnectionState
        data object Connected : ConnectionState
        data class Error(val message: String) : ConnectionState
    }
}
