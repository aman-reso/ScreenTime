package com.app.screentime.core.network.websocket

import android.util.Log
import com.app.screentime.core.network.NetworkAuthBridge
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.session.SessionManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import okhttp3.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WinterWebSocketClient @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager
) : WebSocketListener() {

    private val tag = "WinterWebSocket"
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var webSocket: WebSocket? = null

    private val resilientDns = object : Dns {
        override fun lookup(hostname: String): List<java.net.InetAddress> {
            return try {
                val addresses = Dns.SYSTEM.lookup(hostname)
                if (addresses.isNotEmpty()) addresses else throw java.net.UnknownHostException("No address found for $hostname")
            } catch (e: Exception) {
                try {
                    val all = java.net.InetAddress.getAllByName(hostname).toList()
                    if (all.isNotEmpty()) all else throw e
                } catch (e2: Exception) {
                    Log.w(tag, "⚠️ DNS resolution failed for $hostname: ${e2.message}")
                    throw java.net.UnknownHostException("Unable to resolve host \"$hostname\": ${e2.message}")
                }
            }
        }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .dns(resilientDns)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS) // Indefinite read timeout for WebSocket
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false
        isLenient = true
        explicitNulls = false
    }

    private val _eventsFlow = MutableSharedFlow<WSMessage>(extraBufferCapacity = 128)
    val eventsFlow: SharedFlow<WSMessage> = _eventsFlow.asSharedFlow()

    private val _rawMessagesFlow = MutableSharedFlow<String>(extraBufferCapacity = 64)
    val rawMessagesFlow: SharedFlow<String> = _rawMessagesFlow.asSharedFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnectedFlow: StateFlow<Boolean> = _isConnected.asStateFlow()

    private var reconnectJob: Job? = null
    private var pingJob: Job? = null
    private var reconnectAttempts = 0

    fun connect() {
        if (sessionManager.isTokenExpired()) {
            Log.w(tag, "⚠️ Token expired, clearing session.")
            sessionManager.clearSession()
            NetworkAuthBridge.unauthorizedHandler?.onUnauthorized()
            return
        }
        val token = sessionManager.getToken()
        if (token.isNullOrBlank()) {
            Log.w(tag, "⚠️ Connect aborted: token is null or blank.")
            return
        }
        val wsUrl = api.getWsUrl(token)
        Log.i(tag, "🌐 Connecting to WebSocket: $wsUrl")

        try {
            val request = Request.Builder()
                .url(wsUrl)
                .build()

            // Close existing socket cleanly before reconnecting
            stopHeartbeat()
            webSocket?.close(1000, "Reconnecting")
            webSocket = okHttpClient.newWebSocket(request, this)
        } catch (e: Exception) {
            Log.e(tag, "❌ Failed to initiate WebSocket connection: ${e.message}", e)
            scheduleReconnect()
        }
    }

    fun isConnected(): Boolean = _isConnected.value && webSocket != null

    // ── Heartbeat Keepalive (Ping / Pong every 25s) ─────────────────────────
    private fun startHeartbeat() {
        stopHeartbeat()
        pingJob = scope.launch {
            while (isActive && isConnected()) {
                delay(25_000) // 25 seconds keepalive to prevent Cloud Run 30-60s timeout
                try {
                    val pingJson = """{"type":"ping"}"""
                    val sent = webSocket?.send(pingJson) ?: false
                    if (sent) {
                        Log.d(tag, "💓 [PING] Keepalive sent")
                    } else {
                        Log.w(tag, "⚠️ [PING] Failed to send keepalive, connection might be stale")
                    }
                } catch (e: Exception) {
                    Log.w(tag, "⚠️ Error sending keepalive ping: ${e.message}")
                }
            }
        }
    }

    private fun stopHeartbeat() {
        pingJob?.cancel()
        pingJob = null
    }

    // ── Send Outgoing Messages ──────────────────────────────────────────────
    fun sendWSMessage(msg: WSMessage) {
        scope.launch {
            try {
                val str = json.encodeToString(msg)
                Log.i(tag, "📤 [SENT] [${msg.type}] -> $str")
                if (webSocket == null || !_isConnected.value) {
                    Log.w(tag, "WebSocket not connected, queuing connect before send...")
                    connect()
                    var retries = 0
                    while ((webSocket == null || !_isConnected.value) && retries < 20) {
                        delay(100)
                        retries++
                    }
                }
                webSocket?.send(str)
            } catch (e: Exception) {
                Log.e(tag, "❌ Failed to send WSMessage: ${e.message}", e)
            }
        }
    }

    /**
     * Send Chat Message (Client → Server: send_message)
     */
    fun sendChatMessage(targetId: String, content: String, conversationId: String? = null) {
        val trimmed = content.trim()
        if (trimmed.isBlank()) return
        val dataObj = buildJsonObject {
            if (!conversationId.isNullOrBlank()) put("conversation_id", conversationId)
            put("receiver_id", targetId)
            put("content", trimmed)
        }
        sendWSMessage(
            WSMessage(
                type = SocketEventType.SEND_MESSAGE.event,
                event = SocketEventType.SEND_MESSAGE.event,
                target_id = targetId,
                receiver_id = targetId,
                conversation_id = conversationId,
                content = trimmed,
                data = dataObj
            )
        )
    }

    /**
     * Join active conversation room (Client → Server: join_conversation)
     */
    fun joinConversation(conversationId: String) {
        if (conversationId.isBlank()) return
        val dataObj = buildJsonObject {
            put("conversation_id", conversationId)
        }
        sendWSMessage(
            WSMessage(
                type = SocketEventType.JOIN_CONVERSATION.event,
                event = SocketEventType.JOIN_CONVERSATION.event,
                conversation_id = conversationId,
                data = dataObj
            )
        )
    }

    /**
     * Leave conversation room (Client → Server: leave_conversation)
     */
    fun leaveConversation(conversationId: String) {
        if (conversationId.isBlank()) return
        val dataObj = buildJsonObject {
            put("conversation_id", conversationId)
        }
        sendWSMessage(
            WSMessage(
                type = SocketEventType.LEAVE_CONVERSATION.event,
                event = SocketEventType.LEAVE_CONVERSATION.event,
                conversation_id = conversationId,
                data = dataObj
            )
        )
    }

    /**
     * Typing Start Indicator (Client → Server: typing_start)
     */
    fun sendTypingStart(conversationId: String, targetId: String? = null) {
        val dataObj = buildJsonObject {
            put("conversation_id", conversationId)
            if (!targetId.isNullOrBlank()) put("receiver_id", targetId)
        }
        sendWSMessage(
            WSMessage(
                type = SocketEventType.TYPING_START.event,
                event = SocketEventType.TYPING_START.event,
                conversation_id = conversationId,
                target_id = targetId,
                receiver_id = targetId,
                is_typing = true,
                data = dataObj
            )
        )
    }

    /**
     * Typing Stop Indicator (Client → Server: typing_stop)
     */
    fun sendTypingStop(conversationId: String, targetId: String? = null) {
        val dataObj = buildJsonObject {
            put("conversation_id", conversationId)
            if (!targetId.isNullOrBlank()) put("receiver_id", targetId)
        }
        sendWSMessage(
            WSMessage(
                type = SocketEventType.TYPING_STOP.event,
                event = SocketEventType.TYPING_STOP.event,
                conversation_id = conversationId,
                target_id = targetId,
                receiver_id = targetId,
                is_typing = false,
                data = dataObj
            )
        )
    }

    /**
     * Typing Indicator (Client → Server)
     */
    fun sendTyping(targetId: String, isTyping: Boolean) {
        if (isTyping) {
            sendTypingStart(conversationId = "", targetId = targetId)
        } else {
            sendTypingStop(conversationId = "", targetId = targetId)
        }
    }

    /**
     * Mark Messages Read (Client → Server: mark_read)
     */
    fun markRead(conversationId: String, messageId: String? = null) {
        val dataObj = buildJsonObject {
            put("conversation_id", conversationId)
            if (!messageId.isNullOrBlank()) put("message_id", messageId)
        }
        sendWSMessage(
            WSMessage(
                type = SocketEventType.MARK_READ.event,
                event = SocketEventType.MARK_READ.event,
                conversation_id = conversationId,
                call_id = messageId,
                data = dataObj
            )
        )
    }

    fun sendReadReceipt(targetId: String, conversationId: String) {
        markRead(conversationId = conversationId)
    }

    /**
     * 4A. Initiate Call (Caller → Server)
     */
    fun initiateCall(targetId: String, sessionId: String, callType: String = "voice") {
        val payloadObj = buildJsonObject {
            put("call_type", callType)
        }
        sendWSMessage(
            WSMessage(
                type = WSEventTypes.CALL_INITIATE,
                target_id = targetId,
                session_id = sessionId,
                payload = payloadObj
            )
        )
    }

    fun requestCall(receiverId: String, callType: String = "voice") {
        val sessionId = "call_${System.currentTimeMillis()}"
        initiateCall(targetId = receiverId, sessionId = sessionId, callType = callType)
    }

    /**
     * Accept Call (Receiver → Server: call_answered / call_accept)
     */
    fun acceptCall(targetId: String, sessionId: String) {
        val dataObj = buildJsonObject {
            put("session_id", sessionId)
            put("call_id", sessionId)
            if (targetId.isNotBlank()) put("target_id", targetId)
        }
        sendWSMessage(
            WSMessage(
                type = SocketEventType.CALL_ANSWERED.event,
                event = SocketEventType.CALL_ANSWERED.event,
                target_id = targetId,
                session_id = sessionId,
                call_id = sessionId,
                data = dataObj
            )
        )
    }

    /**
     * Reject Call (Receiver → Server: call_rejected / call_reject)
     */
    fun rejectCall(targetId: String, sessionId: String) {
        val dataObj = buildJsonObject {
            put("session_id", sessionId)
            put("call_id", sessionId)
            if (targetId.isNotBlank()) put("target_id", targetId)
        }
        sendWSMessage(
            WSMessage(
                type = SocketEventType.CALL_REJECTED.event,
                event = SocketEventType.CALL_REJECTED.event,
                target_id = targetId,
                session_id = sessionId,
                call_id = sessionId,
                data = dataObj
            )
        )
    }

    /**
     * End / Hangup Call (Either Party → Server: call_ended / call_end)
     */
    fun endCall(targetId: String, sessionId: String) {
        val dataObj = buildJsonObject {
            put("session_id", sessionId)
            put("call_id", sessionId)
            if (targetId.isNotBlank()) put("target_id", targetId)
        }
        sendWSMessage(
            WSMessage(
                type = SocketEventType.CALL_ENDED.event,
                event = SocketEventType.CALL_ENDED.event,
                target_id = targetId,
                session_id = sessionId,
                call_id = sessionId,
                data = dataObj
            )
        )
    }

    /**
     * WebRTC Peer-to-Peer Signaling (SDP & ICE: call_signal / signal)
     */
    fun sendWebRTCSignaling(signalType: String, sessionId: String, targetId: String?, payloadJsonString: String) {
        val normSignalType = when (signalType.lowercase()) {
            "offer", WSEventTypes.WEBRTC_OFFER -> "offer"
            "answer", WSEventTypes.WEBRTC_ANSWER -> "answer"
            "candidate", "ice-candidate", "ice_candidate", WSEventTypes.WEBRTC_ICE_CANDIDATE -> "ice-candidate"
            else -> signalType.lowercase()
        }
        val dataObj = buildJsonObject {
            put("session_id", sessionId)
            put("call_id", sessionId)
            put("signal_type", normSignalType)
            if (!targetId.isNullOrBlank()) put("target_id", targetId)
        }
        sendWSMessage(
            WSMessage(
                type = SocketEventType.CALL_SIGNAL.event,
                event = SocketEventType.CALL_SIGNAL.event,
                target_id = targetId,
                session_id = sessionId,
                call_id = sessionId,
                signal_type = normSignalType,
                payload = JsonPrimitive(payloadJsonString),
                data = dataObj
            )
        )
    }


    fun disconnect() {
        Log.i(tag, "🛑 Disconnecting WebSocket...")
        stopHeartbeat()
        reconnectJob?.cancel()
        reconnectJob = null
        reconnectAttempts = 0
        _isConnected.value = false
        webSocket?.close(1000, "App closed")
        webSocket = null
    }

    private fun scheduleReconnect() {
        if (reconnectJob?.isActive == true) return
        val token = sessionManager.getToken()
        if (token.isNullOrBlank() || sessionManager.isTokenExpired()) return

        reconnectJob = scope.launch {
            reconnectAttempts++
            val delayMs = when {
                reconnectAttempts <= 1 -> 2000L
                reconnectAttempts <= 3 -> 4000L
                reconnectAttempts <= 5 -> 8000L
                else -> 15000L
            }
            Log.i(tag, "🔄 Scheduling WebSocket reconnect attempt #$reconnectAttempts in ${delayMs}ms...")
            delay(delayMs)
            connect()
        }
    }

    // ── OkHttp WebSocketListener Callbacks ──────────────────────────────────
    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.i(tag, "🟢 [CONNECTED] WebSocket connection established successfully! HTTP ${response.code}")
        _isConnected.value = true
        reconnectAttempts = 0
        reconnectJob?.cancel()
        reconnectJob = null
        startHeartbeat()

        // 1B. Send Post-Handshake Auth Frame as guaranteed auth fallback
//        val token = sessionManager.getToken()
//        if (!token.isNullOrBlank()) {
//            val authMsg = """{"type":"auth","token":"$token"}"""
//            webSocket.send(authMsg)
//            Log.d(tag, "🔑 Sent post-handshake auth frame")
//        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.i(tag, "📥 [RECEIVED] $text")
        scope.launch {
            _rawMessagesFlow.emit(text)

            // Fast-path keepalive ping & pong
            if (text.contains("\"type\":\"ping\"") || text.contains("\"type\": \"ping\"")) {
                webSocket.send("""{"type":"pong","timestamp":${System.currentTimeMillis()}}""")
                Log.d(tag, "💚 [PONG] Answered server ping")
                return@launch
            }
            if (text.contains("\"type\":\"pong\"") || text.contains("\"type\": \"pong\"")) {
                Log.d(tag, "💚 [PONG] Server heartbeat acknowledged")
                return@launch
            }

            try {
                val jsonTree = json.parseToJsonElement(text) as? JsonObject ?: return@launch
                val typeStr = jsonTree["type"]?.jsonPrimitive?.contentOrNull.orEmpty()
                val eventStr = jsonTree["event"]?.jsonPrimitive?.contentOrNull.orEmpty()
                val sessionId = jsonTree["session_id"]?.jsonPrimitive?.contentOrNull.orEmpty()
                val senderId = jsonTree["sender_id"]?.jsonPrimitive?.contentOrNull.orEmpty()
                val targetId = jsonTree["target_id"]?.jsonPrimitive?.contentOrNull.orEmpty()
                val content = jsonTree["content"]?.jsonPrimitive?.contentOrNull.orEmpty()
                val convId = jsonTree["conversation_id"]?.jsonPrimitive?.contentOrNull.orEmpty()
                val timestamp = jsonTree["timestamp"]?.jsonPrimitive?.longOrNull ?: System.currentTimeMillis()
                val errorStr = jsonTree["error"]?.jsonPrimitive?.contentOrNull
                val onlineVal = jsonTree["online"]?.jsonPrimitive?.booleanOrNull
                val isTypingVal = jsonTree["is_typing"]?.jsonPrimitive?.booleanOrNull
                val signalType = jsonTree["signal_type"]?.jsonPrimitive?.contentOrNull.orEmpty()
                val payloadElement = jsonTree["payload"]
                val dataObj = jsonTree["data"] as? JsonObject

                // 1A. Server Initial Connection ACK
                if (typeStr == WSEventTypes.CONNECTED || eventStr == WSEventTypes.CONNECTED) {
                    val userId = dataObj?.get("user_id")?.jsonPrimitive?.contentOrNull ?: senderId
                    Log.i(tag, "🎉 Authenticated on real-time socket for user: $userId")
                    _eventsFlow.emit(
                        WSMessage(
                            type = WSEventTypes.CONNECTED,
                            event = WSEventTypes.CONNECTED,
                            user_id = userId,
                            sender_id = userId,
                            data = dataObj,
                            timestamp = timestamp
                        )
                    )
                    return@launch
                }

                // 1C & 6. Authentication or Protocol Error
                if (typeStr == WSEventTypes.ERROR || eventStr == WSEventTypes.ERROR) {
                    Log.e(tag, "⚠️ Server returned error: $errorStr")
                    if (errorStr?.contains("Unauthorized", ignoreCase = true) == true) {
                        sessionManager.clearSession()
                        NetworkAuthBridge.unauthorizedHandler?.onUnauthorized()
                    }
                    _eventsFlow.emit(
                        WSMessage(
                            type = WSEventTypes.ERROR,
                            event = WSEventTypes.ERROR,
                            error = errorStr,
                            reason = errorStr,
                            timestamp = timestamp
                        )
                    )
                    return@launch
                }

                // 2A & 2B. User Presence Broadcast
                if (typeStr == WSEventTypes.USER_ONLINE || typeStr == WSEventTypes.USER_OFFLINE || eventStr == WSEventTypes.PRESENCE) {
                    val isOnline = onlineVal ?: (typeStr == WSEventTypes.USER_ONLINE)
                    val presenceMsg = WSMessage(
                        type = if (isOnline) WSEventTypes.USER_ONLINE else WSEventTypes.USER_OFFLINE,
                        event = WSEventTypes.PRESENCE,
                        sender_id = senderId,
                        user_id = senderId,
                        online = isOnline,
                        timestamp = timestamp
                    )
                    _eventsFlow.emit(presenceMsg)
                    _eventsFlow.emit(presenceMsg.copy(type = WSEventTypes.PRESENCE_UPDATE))
                    return@launch
                }

                // 3B. Message Delivery Ack (Server → Sender)
                if (typeStr == WSEventTypes.MESSAGE_SENT || eventStr == WSEventTypes.MESSAGE_SENT) {
                    _eventsFlow.emit(
                        WSMessage(
                            type = WSEventTypes.MESSAGE_SENT,
                            event = WSEventTypes.MESSAGE_SENT,
                            target_id = targetId,
                            conversation_id = convId,
                            content = content,
                            timestamp = timestamp
                        )
                    )
                    return@launch
                }

                // 3C. Incoming Chat Message (Server → Receiver)
                val isChatEvent = typeStr.equals(WSEventTypes.NEW_MESSAGE, ignoreCase = true) ||
                        eventStr.equals(WSEventTypes.NEW_MESSAGE, ignoreCase = true) ||
                        typeStr.equals(WSEventTypes.CHAT_MESSAGE_RECEIVED, ignoreCase = true) ||
                        eventStr.equals(WSEventTypes.CHAT_MESSAGE_RECEIVED, ignoreCase = true) ||
                        typeStr.equals(WSEventTypes.CHAT_MESSAGE, ignoreCase = true) ||
                        eventStr.equals(WSEventTypes.CHAT_MESSAGE, ignoreCase = true) ||
                        typeStr.equals("message", ignoreCase = true) ||
                        eventStr.equals("message", ignoreCase = true) ||
                        typeStr.equals("send_message", ignoreCase = true) ||
                        eventStr.equals("send_message", ignoreCase = true) ||
                        typeStr.equals("direct_message", ignoreCase = true) ||
                        eventStr.equals("direct_message", ignoreCase = true) ||
                        typeStr.equals("dm", ignoreCase = true) ||
                        eventStr.equals("dm", ignoreCase = true)

                if (isChatEvent) {
                    val payloadObj = payloadElement as? JsonObject
                    val resolvedContent = content.ifBlank {
                        jsonTree["text"]?.jsonPrimitive?.contentOrNull
                            ?: jsonTree["message"]?.jsonPrimitive?.contentOrNull
                            ?: jsonTree["msg"]?.jsonPrimitive?.contentOrNull
                            ?: jsonTree["body"]?.jsonPrimitive?.contentOrNull
                            ?: dataObj?.get("content")?.jsonPrimitive?.contentOrNull
                            ?: dataObj?.get("text")?.jsonPrimitive?.contentOrNull
                            ?: dataObj?.get("message")?.jsonPrimitive?.contentOrNull
                            ?: payloadObj?.get("content")?.jsonPrimitive?.contentOrNull
                            ?: payloadObj?.get("text")?.jsonPrimitive?.contentOrNull
                            ?: payloadObj?.get("message")?.jsonPrimitive?.contentOrNull
                            ?: (payloadElement as? JsonPrimitive)?.contentOrNull
                            ?: ""
                    }
                    val resolvedConvId = convId.ifBlank {
                        jsonTree["room_id"]?.jsonPrimitive?.contentOrNull
                            ?: jsonTree["conv_id"]?.jsonPrimitive?.contentOrNull
                            ?: dataObj?.get("conversation_id")?.jsonPrimitive?.contentOrNull
                            ?: dataObj?.get("room_id")?.jsonPrimitive?.contentOrNull
                            ?: payloadObj?.get("conversation_id")?.jsonPrimitive?.contentOrNull
                            ?: ""
                    }
                    val resolvedSender = senderId.ifBlank {
                        jsonTree["from"]?.jsonPrimitive?.contentOrNull
                            ?: jsonTree["user_id"]?.jsonPrimitive?.contentOrNull
                            ?: jsonTree["caller_id"]?.jsonPrimitive?.contentOrNull
                            ?: jsonTree["senderId"]?.jsonPrimitive?.contentOrNull
                            ?: dataObj?.get("sender_id")?.jsonPrimitive?.contentOrNull
                            ?: dataObj?.get("from")?.jsonPrimitive?.contentOrNull
                            ?: dataObj?.get("user_id")?.jsonPrimitive?.contentOrNull
                            ?: payloadObj?.get("sender_id")?.jsonPrimitive?.contentOrNull
                            ?: payloadObj?.get("from")?.jsonPrimitive?.contentOrNull
                            ?: ""
                    }
                    val resolvedTarget = targetId.ifBlank {
                        jsonTree["to"]?.jsonPrimitive?.contentOrNull
                            ?: jsonTree["receiver_id"]?.jsonPrimitive?.contentOrNull
                            ?: jsonTree["target_user_id"]?.jsonPrimitive?.contentOrNull
                            ?: jsonTree["targetUserId"]?.jsonPrimitive?.contentOrNull
                            ?: dataObj?.get("receiver_id")?.jsonPrimitive?.contentOrNull
                            ?: dataObj?.get("target_id")?.jsonPrimitive?.contentOrNull
                            ?: dataObj?.get("to")?.jsonPrimitive?.contentOrNull
                            ?: payloadObj?.get("receiver_id")?.jsonPrimitive?.contentOrNull
                            ?: payloadObj?.get("target_id")?.jsonPrimitive?.contentOrNull
                            ?: ""
                    }
                    val resolvedMsgId = jsonTree["id"]?.jsonPrimitive?.contentOrNull
                        ?: jsonTree["message_id"]?.jsonPrimitive?.contentOrNull
                        ?: dataObj?.get("id")?.jsonPrimitive?.contentOrNull
                        ?: dataObj?.get("message_id")?.jsonPrimitive?.contentOrNull
                        ?: payloadObj?.get("id")?.jsonPrimitive?.contentOrNull
                        ?: "msg_${timestamp}_${java.util.UUID.randomUUID().toString().take(8)}"

                    val createdAtStr = dataObj?.get("created_at")?.jsonPrimitive?.contentOrNull
                        ?: jsonTree["created_at"]?.jsonPrimitive?.contentOrNull
                    val resolvedTimestamp = try {
                        if (!createdAtStr.isNullOrBlank()) {
                            java.time.Instant.parse(createdAtStr).toEpochMilli()
                        } else {
                            timestamp
                        }
                    } catch (_: Exception) {
                        timestamp
                    }

                    val resolvedEvent = if (eventStr.isNotBlank()) eventStr else WSEventTypes.NEW_MESSAGE

                    val chatMsg = WSMessage(
                        type = WSEventTypes.CHAT_MESSAGE,
                        event = resolvedEvent,
                        call_id = resolvedMsgId,
                        room_id = resolvedConvId,
                        conversation_id = resolvedConvId,
                        caller_id = resolvedSender,
                        sender_id = resolvedSender,
                        receiver_id = resolvedTarget,
                        target_id = resolvedTarget,
                        content = resolvedContent,
                        message = resolvedContent,
                        data = dataObj,
                        timestamp = resolvedTimestamp
                    )
                    Log.i(tag, "💬 [INCOMING CHAT] event=$resolvedEvent, id=$resolvedMsgId, from=$resolvedSender, to=$resolvedTarget, in=$resolvedConvId: \"$resolvedContent\"")
                    _eventsFlow.emit(chatMsg)
                    return@launch
                }

                // 3E. Typing Indicator Relayed
                if (typeStr == WSEventTypes.USER_TYPING || eventStr == WSEventTypes.USER_TYPING || typeStr == WSEventTypes.TYPING) {
                    _eventsFlow.emit(
                        WSMessage(
                            type = WSEventTypes.USER_TYPING,
                            event = WSEventTypes.USER_TYPING,
                            sender_id = senderId,
                            target_id = targetId,
                            is_typing = isTypingVal ?: true,
                            timestamp = timestamp
                        )
                    )
                    return@launch
                }

                // 3F. Read Receipt Relayed
                if (typeStr == WSEventTypes.MESSAGE_READ || eventStr == WSEventTypes.MESSAGE_READ) {
                    _eventsFlow.emit(
                        WSMessage(
                            type = WSEventTypes.MESSAGE_READ,
                            event = WSEventTypes.MESSAGE_READ,
                            sender_id = senderId,
                            target_id = targetId,
                            conversation_id = convId,
                            timestamp = timestamp
                        )
                    )
                    return@launch
                }

                // 4B. Incoming Call (Server → Receiver)
                if (typeStr == WSEventTypes.INCOMING_CALL || eventStr == WSEventTypes.INCOMING_CALL ||
                    typeStr == WSEventTypes.CALL_INVITE || eventStr == WSEventTypes.CALL_INVITE) {
                    val resolvedSession = sessionId.ifBlank { dataObj?.get("session_id")?.jsonPrimitive?.contentOrNull.orEmpty() }
                    val resolvedCaller = senderId.ifBlank { dataObj?.get("caller_id")?.jsonPrimitive?.contentOrNull.orEmpty() }

                    val callType = (payloadElement as? JsonObject)?.get("call_type")?.jsonPrimitive?.contentOrNull
                        ?: dataObj?.get("call_type")?.jsonPrimitive?.contentOrNull
                        ?: jsonTree["call_type"]?.jsonPrimitive?.contentOrNull
                        ?: "voice"

                    val credits = (payloadElement as? JsonObject)?.get("credits_per_minute")?.jsonPrimitive?.doubleOrNull
                        ?: dataObj?.get("credits_per_minute")?.jsonPrimitive?.doubleOrNull
                        ?: 10.0

                    val callMsg = WSMessage(
                        type = WSEventTypes.INCOMING_CALL,
                        event = WSEventTypes.INCOMING_CALL,
                        call_id = resolvedSession,
                        session_id = resolvedSession,
                        caller_id = resolvedCaller,
                        sender_id = resolvedCaller,
                        target_id = targetId,
                        call_type = callType,
                        rate_per_min = credits,
                        credits_per_minute = credits,
                        payload = payloadElement,
                        data = dataObj,
                        timestamp = timestamp
                    )
                    Log.i(tag, "📞 [INCOMING CALL] from $resolvedCaller, session=$resolvedSession, type=$callType")
                    _eventsFlow.emit(callMsg)
                    return@launch
                }

                // 4C. Target Busy Error (Server → Caller)
                if (typeStr == WSEventTypes.CALL_BUSY || eventStr == WSEventTypes.CALL_BUSY) {
                    _eventsFlow.emit(
                        WSMessage(
                            type = WSEventTypes.CALL_BUSY,
                            event = WSEventTypes.CALL_BUSY,
                            target_id = targetId,
                            error = errorStr ?: "Target user is on another call",
                            reason = errorStr ?: "Target user is on another call",
                            timestamp = timestamp
                        )
                    )
                    return@launch
                }

                // 4D. Call Accepted (Caller Receives)
                if (typeStr == WSEventTypes.CALL_ACCEPTED || eventStr == WSEventTypes.CALL_ACCEPTED) {
                    val callActiveMsg = WSMessage(
                        type = WSEventTypes.CALL_ACTIVE,
                        event = WSEventTypes.CALL_ACCEPTED,
                        call_id = sessionId,
                        session_id = sessionId,
                        caller_id = senderId,
                        sender_id = senderId,
                        target_id = targetId,
                        timestamp = timestamp
                    )
                    _eventsFlow.emit(callActiveMsg)
                    _eventsFlow.emit(callActiveMsg.copy(type = WSEventTypes.CALL_ACCEPTED))
                    return@launch
                }

                // 4E. Call Rejected (Caller Receives)
                if (typeStr == WSEventTypes.CALL_REJECTED || eventStr == WSEventTypes.CALL_REJECTED) {
                    _eventsFlow.emit(
                        WSMessage(
                            type = WSEventTypes.CALL_REJECTED,
                            event = WSEventTypes.CALL_REJECTED,
                            call_id = sessionId,
                            session_id = sessionId,
                            caller_id = senderId,
                            sender_id = senderId,
                            target_id = targetId,
                            reason = "Call declined",
                            timestamp = timestamp
                        )
                    )
                    return@launch
                }

                // 4F. Call Ended (Both Parties Receive)
                if (typeStr == WSEventTypes.CALL_ENDED || eventStr == WSEventTypes.CALL_ENDED) {
                    _eventsFlow.emit(
                        WSMessage(
                            type = WSEventTypes.CALL_ENDED,
                            event = WSEventTypes.CALL_ENDED,
                            call_id = sessionId,
                            session_id = sessionId,
                            caller_id = senderId,
                            sender_id = senderId,
                            reason = "Call ended by remote party",
                            timestamp = timestamp
                        )
                    )
                    return@launch
                }

                // 5B, 5D. Relayed WebRTC Signal (SDP Offer / Answer / ICE Candidate)
                if (typeStr == WSEventTypes.WEBRTC_SIGNAL || eventStr == WSEventTypes.WEBRTC_SIGNAL ||
                    typeStr == WSEventTypes.SIGNAL || typeStr == WSEventTypes.CALL_SIGNAL || eventStr == WSEventTypes.CALL_SIGNAL) {
                    val resolvedSignalType = signalType.ifBlank {
                        dataObj?.get("signal_type")?.jsonPrimitive?.contentOrNull.orEmpty()
                    }

                    // Parse inner payload if stringified JSON
                    val innerPayloadObj: JsonObject? = when (payloadElement) {
                        is JsonObject -> payloadElement
                        is JsonPrimitive -> {
                            try {
                                json.parseToJsonElement(payloadElement.content) as? JsonObject
                            } catch (_: Exception) {
                                null
                            }
                        }
                        else -> null
                    }

                    when (resolvedSignalType.lowercase()) {
                        "offer" -> {
                            val sdp = innerPayloadObj?.get("sdp")?.jsonPrimitive?.contentOrNull
                                ?: (payloadElement as? JsonPrimitive)?.content.orEmpty()
                            val offerMsg = WSMessage(
                                type = WSEventTypes.WEBRTC_OFFER,
                                event = WSEventTypes.WEBRTC_SIGNAL,
                                call_id = sessionId,
                                session_id = sessionId,
                                caller_id = senderId,
                                sender_id = senderId,
                                target_id = targetId,
                                signal_type = "offer",
                                sdp = sdp,
                                payload = payloadElement,
                                timestamp = timestamp
                            )
                            Log.i(tag, "📶 [WEBRTC OFFER] for session=$sessionId from $senderId")
                            _eventsFlow.emit(offerMsg)
                            return@launch
                        }
                        "answer" -> {
                            val sdp = innerPayloadObj?.get("sdp")?.jsonPrimitive?.contentOrNull
                                ?: (payloadElement as? JsonPrimitive)?.content.orEmpty()
                            val answerMsg = WSMessage(
                                type = WSEventTypes.WEBRTC_ANSWER,
                                event = WSEventTypes.WEBRTC_SIGNAL,
                                call_id = sessionId,
                                session_id = sessionId,
                                caller_id = senderId,
                                sender_id = senderId,
                                target_id = targetId,
                                signal_type = "answer",
                                sdp = sdp,
                                payload = payloadElement,
                                timestamp = timestamp
                            )
                            Log.i(tag, "📶 [WEBRTC ANSWER] for session=$sessionId from $senderId")
                            _eventsFlow.emit(answerMsg)
                            return@launch
                        }
                        "candidate", "ice-candidate", "ice_candidate" -> {
                            val cand = innerPayloadObj?.get("candidate")?.jsonPrimitive?.contentOrNull
                                ?: (payloadElement as? JsonPrimitive)?.content.orEmpty()
                            val mid = innerPayloadObj?.get("sdpMid")?.jsonPrimitive?.contentOrNull
                                ?: innerPayloadObj?.get("sdp_mid")?.jsonPrimitive?.contentOrNull
                                ?: "0"
                            val mLine = innerPayloadObj?.get("sdpMLineIndex")?.jsonPrimitive?.intOrNull
                                ?: innerPayloadObj?.get("sdp_m_line_index")?.jsonPrimitive?.intOrNull
                                ?: 0

                            val iceMsg = WSMessage(
                                type = WSEventTypes.WEBRTC_ICE_CANDIDATE,
                                event = WSEventTypes.WEBRTC_SIGNAL,
                                call_id = sessionId,
                                session_id = sessionId,
                                caller_id = senderId,
                                sender_id = senderId,
                                target_id = targetId,
                                signal_type = "ice-candidate",
                                candidate = cand,
                                sdp_mid = mid,
                                sdp_m_line_index = mLine,
                                payload = payloadElement,
                                timestamp = timestamp
                            )
                            Log.i(tag, "📶 [WEBRTC ICE] for session=$sessionId from $senderId")
                            _eventsFlow.emit(iceMsg)
                            return@launch
                        }
                        else -> {
                            val signalMsg = WSMessage(
                                type = WSEventTypes.CALL_SIGNAL,
                                event = WSEventTypes.WEBRTC_SIGNAL,
                                call_id = sessionId,
                                session_id = sessionId,
                                caller_id = senderId,
                                sender_id = senderId,
                                target_id = targetId,
                                signal_type = resolvedSignalType,
                                payload = payloadElement,
                                timestamp = timestamp
                            )
                            Log.i(tag, "📶 [WEBRTC SIGNAL: $resolvedSignalType] for session=$sessionId from $senderId")
                            _eventsFlow.emit(signalMsg)
                            return@launch
                        }
                    }
                }

                // Balance Update (Server → Client: balance_update)
                if (typeStr.equals(SocketEventType.BALANCE_UPDATE.event, ignoreCase = true) ||
                    eventStr.equals(SocketEventType.BALANCE_UPDATE.event, ignoreCase = true)) {
                    val newBalance = dataObj?.get("balance")?.jsonPrimitive?.doubleOrNull
                        ?: (payloadElement as? JsonObject)?.get("balance")?.jsonPrimitive?.doubleOrNull
                        ?: jsonTree["balance"]?.jsonPrimitive?.doubleOrNull
                    if (newBalance != null) {
                        sessionManager.walletBalance = newBalance
                        Log.i(tag, "💰 [BALANCE UPDATE] New wallet balance: $newBalance")
                    }
                    _eventsFlow.emit(
                        WSMessage(
                            type = SocketEventType.BALANCE_UPDATE.event,
                            event = SocketEventType.BALANCE_UPDATE.event,
                            cost = newBalance,
                            data = dataObj,
                            timestamp = timestamp
                        )
                    )
                    return@launch
                }

                // Keepalive Pong (Server → Client: pong)
                if (typeStr.equals(SocketEventType.PONG.event, ignoreCase = true) ||
                    eventStr.equals(SocketEventType.PONG.event, ignoreCase = true)) {
                    Log.d(tag, "🏓 [PONG] Server heartbeat acknowledged")
                    return@launch
                }

                // General fallback decoding
                val parsed = json.decodeFromString<WSMessage>(text)
                if (parsed.type == WSEventTypes.SESSION_TERMINATED) {
                    sessionManager.clearSession()
                    NetworkAuthBridge.unauthorizedHandler?.onUnauthorized()
                }
                _eventsFlow.emit(parsed)
            } catch (e: Exception) {
                Log.e(tag, "❌ Failed to parse WS message: ${e.message} | Raw: $text", e)
            }
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e(tag, "❌ [FAILURE] WebSocket error: ${t.message}, ResponseCode: ${response?.code}", t)
        _isConnected.value = false
        this.webSocket = null
        stopHeartbeat()
        scope.launch {
            _eventsFlow.emit(
                WSMessage(
                    type = WSEventTypes.NETWORK_ERROR,
                    reason = t.message ?: "Network failure"
                )
            )
        }
        scheduleReconnect()
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.w(tag, "🔴 [CLOSED] WebSocket closed: code=$code, reason=$reason")
        _isConnected.value = false
        this.webSocket = null
        stopHeartbeat()
        if (code != 1000) {
            scope.launch {
                _eventsFlow.emit(
                    WSMessage(
                        type = WSEventTypes.NETWORK_ERROR,
                        reason = reason.ifBlank { "Closed unexpectedly" }
                    )
                )
            }
            scheduleReconnect()
        }
    }
}
