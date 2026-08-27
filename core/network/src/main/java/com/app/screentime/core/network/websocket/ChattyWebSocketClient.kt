package com.app.screentime.core.network.websocket

import android.util.Log
import com.app.screentime.core.network.NetworkAuthBridge
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChattyWebSocketClient @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager
) : WebSocketListener() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var webSocket: WebSocket? = null
    private val okHttpClient = OkHttpClient.Builder().build()
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    private val _eventsFlow = MutableSharedFlow<WSMessage>(extraBufferCapacity = 64)
    val eventsFlow: SharedFlow<WSMessage> = _eventsFlow.asSharedFlow()

    private val _rawMessagesFlow = MutableSharedFlow<String>(extraBufferCapacity = 64)
    val rawMessagesFlow: SharedFlow<String> = _rawMessagesFlow.asSharedFlow()

    fun connect() {
        if (sessionManager.isTokenExpired()) {
            Log.w("CONNECT_WS", "⚠️ Token expired, clearing session.")
            sessionManager.clearSession()
            NetworkAuthBridge.unauthorizedHandler?.onUnauthorized()
            return
        }
        val token = sessionManager.token
        if (token.isNullOrBlank()) {
            Log.w("CONNECT_WS", "⚠️ Connect aborted: token is null or blank.")
            return
        }
        val wsUrl = api.getWsUrl(token)
        Log.i("CONNECT_WS", "🌐 Connecting to WebSocket: $wsUrl")
        val request = Request.Builder().url(wsUrl).build()
        webSocket = okHttpClient.newWebSocket(request, this)
    }

    fun isConnected(): Boolean = webSocket != null

    fun sendWSMessage(msg: WSMessage) {
        try {
            val str = json.encodeToString(msg)
            Log.i("CONNECT_WS", "📤 [SENT] [${msg.type}] -> $str")
            webSocket?.send(str)
        } catch (e: Exception) {
            Log.e("CONNECT_WS", "❌ Failed to send WSMessage: ${e.message}", e)
        }
    }

    fun requestCall(receiverId: String) {
        sendWSMessage(WSMessage(type = WSEventTypes.CALL_REQUEST, receiver_id = receiverId))
    }

    fun acceptCall(callId: String) {
        sendWSMessage(WSMessage(type = WSEventTypes.CALL_ACCEPT, call_id = callId))
    }

    fun rejectCall(callId: String, callerId: String) {
        sendWSMessage(WSMessage(type = WSEventTypes.CALL_REJECT, call_id = callId, caller_id = callerId))
    }

    fun endCall(callId: String) {
        sendWSMessage(WSMessage(type = WSEventTypes.CALL_END, call_id = callId))
    }

    fun sendChatMessage(receiverId: String, text: String) {
        sendWSMessage(WSMessage(type = WSEventTypes.CHAT_MESSAGE, receiver_id = receiverId, payload = kotlinx.serialization.json.JsonPrimitive(text)))
    }

    fun sendWebRTCSignaling(type: String, callId: String, receiverId: String?, payload: String) {
        val element = try {
            json.parseToJsonElement(payload)
        } catch (e: Exception) {
            kotlinx.serialization.json.JsonPrimitive(payload)
        }
        sendWSMessage(WSMessage(type = type, call_id = callId, receiver_id = receiverId, payload = element))
    }

    fun disconnect() {
        Log.i("CONNECT_WS", "🛑 Disconnecting WebSocket...")
        webSocket?.close(1000, "App closed")
        webSocket = null
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.i("CONNECT_WS", "🟢 [CONNECTED] WebSocket connection established successfully! Code: ${response.code}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.i("CONNECT_WS", "📥 [RECEIVED] $text")
        scope.launch {
            _rawMessagesFlow.emit(text)
            try {
                val parsed = json.decodeFromString<WSMessage>(text)
                Log.i("CONNECT_WS", "✅ [PARSED EVENT] Type=${parsed.type}, CallID=${parsed.call_id}, Caller=${parsed.caller_id} (${parsed.caller_name}), Receiver=${parsed.receiver_id}")
                if (parsed.type == WSEventTypes.SESSION_TERMINATED) {
                    sessionManager.clearSession()
                    NetworkAuthBridge.unauthorizedHandler?.onUnauthorized()
                }
                _eventsFlow.emit(parsed)
            } catch (e: Exception) {
                Log.e("CONNECT_WS", "❌ Failed to parse WS message: ${e.message} | Raw: $text", e)
            }
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("CONNECT_WS", "❌ [FAILURE] WebSocket error: ${t.message}, ResponseCode: ${response?.code}", t)
        this.webSocket = null
        scope.launch {
            _eventsFlow.emit(
                WSMessage(type = WSEventTypes.NETWORK_ERROR, reason = t.message ?: "Network failure")
            )
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.w("CONNECT_WS", "🔴 [CLOSED] WebSocket closed: code=$code, reason=$reason")
        this.webSocket = null
        if (code != 1000) {
            scope.launch {
                _eventsFlow.emit(
                    WSMessage(type = WSEventTypes.NETWORK_ERROR, reason = reason.ifBlank { "Closed unexpectedly" })
                )
            }
        }
    }
}
