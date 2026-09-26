package com.app.screentime.core.network.websocket

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

/**
 * Generic WebSocket Envelope Schema matching the standard backend specification:
 * {
 *   "type": "string",
 *   "event": "string (optional)",
 *   "session_id": "string (optional)",
 *   "sender_id": "string (optional)",
 *   "target_id": "string (optional)",
 *   "signal_type": "string (optional)",
 *   "payload": "any / object / string (optional)",
 *   "content": "string (optional)",
 *   "conversation_id": "string (optional)",
 *   "data": "object / array (optional)",
 *   "error": "string (optional)",
 *   "online": "boolean (optional)",
 *   "is_typing": "boolean (optional)",
 *   "timestamp": 1790320000000
 * }
 */
@Serializable
data class WSMessage(
    val type: String = "",
    val event: String? = null,
    val session_id: String? = null,
    val sender_id: String? = null,
    val target_id: String? = null,
    val signal_type: String? = null,
    val payload: JsonElement? = null,
    val content: String? = null,
    val conversation_id: String? = null,
    val data: JsonElement? = null,
    val error: String? = null,
    val online: Boolean? = null,
    val is_typing: Boolean? = null,
    val token: String? = null,
    val timestamp: Long? = null,

    // Aliases / Compatibility fields
    val receiver_id: String? = null,
    val call_id: String? = null,
    val room_id: String? = null,
    val caller_id: String? = null,
    val caller_name: String? = null,
    val caller_avatar: String? = null,
    val target_user_id: String? = null,
    val targetUserId: String? = null,
    val model_id: String? = null,
    val user_id: String? = null,
    val call_type: String? = null,
    val rate_per_min: Double? = null,
    val credits_per_minute: Double? = null,
    val duration_sec: Int? = null,
    val remaining_sec: Int? = null,
    val cost: Double? = null,
    val reason: String? = null,
    val message: String? = null,
    val sdp: String? = null,
    val candidate: String? = null,
    val sdp_mid: String? = null,
    val sdp_m_line_index: Int? = null
) {
    val effectiveTargetId: String?
        get() = target_id ?: receiver_id ?: target_user_id ?: targetUserId

    val effectiveSenderId: String?
        get() = sender_id ?: caller_id ?: user_id

    val effectiveSessionId: String?
        get() = session_id ?: call_id

    val effectiveConversationId: String?
        get() = conversation_id ?: room_id

    fun payloadAsString(): String {
        return when (val p = payload) {
            is JsonPrimitive -> p.content
            null -> ""
            else -> p.toString()
        }
    }
}

object WSEventTypes {
    // 1. Connection & Authentication
    const val CONNECTED = "connected"
    const val AUTH = "auth"
    const val ERROR = "error"
    const val PING = "ping"
    const val PONG = "pong"

    // 2. User Presence
    const val USER_ONLINE = "user_online"
    const val USER_OFFLINE = "user_offline"
    const val PRESENCE = "presence"

    // 3. Real-Time Chat & Instant Messaging
    const val CHAT_MESSAGE = "chat_message"
    const val MESSAGE_SENT = "message_sent"
    const val NEW_MESSAGE = "new_message"
    const val CHAT_MESSAGE_RECEIVED = "chat_message_received"
    const val TYPING = "typing"
    const val USER_TYPING = "user_typing"
    const val MESSAGE_READ = "message_read"

    // 4. Audio & Video Call Lifecycle
    const val CALL_INITIATE = "call_initiate"
    const val INCOMING_CALL = "incoming_call"
    const val CALL_BUSY = "call_busy"
    const val CALL_ACCEPT = "call_accept"
    const val CALL_ACCEPTED = "call_accepted"
    const val CALL_REJECT = "call_reject"
    const val CALL_REJECTED = "call_rejected"
    const val CALL_END = "call_end"
    const val CALL_ENDED = "call_ended"
    const val CALL_CANCEL = "call_cancel"
    const val CALL_CANCELLED = "call_cancelled"

    // 5. WebRTC Peer-to-Peer Signaling (SDP & ICE)
    const val SIGNAL = "signal"
    const val WEBRTC_SIGNAL = "webrtc_signal"
    const val WEBRTC_OFFER = "offer"
    const val WEBRTC_ANSWER = "answer"
    const val WEBRTC_ICE_CANDIDATE = "ice-candidate"

    // App Internal Events
    const val CALL_ACTIVE = "CALL_ACTIVE"
    const val CALL_REQUEST = "CALL_REQUEST"
    const val CALL_INVITE = "call_invite"
    const val CALL_SIGNAL = "call_signal"
    const val CALL_OFFLINE = "CALL_OFFLINE"
    const val CALL_INSUFFICIENT_BALANCE = "CALL_INSUFFICIENT_BALANCE"
    const val BALANCE_LOW_WARNING = "BALANCE_LOW_WARNING"
    const val CALL_ENDED_BALANCE_EXHAUSTED = "CALL_ENDED_BALANCE_EXHAUSTED"
    const val CALL_TICK = "CALL_TICK"
    const val PRESENCE_UPDATE = "PRESENCE_UPDATE"
    const val SESSION_TERMINATED = "SESSION_TERMINATED"
    const val NETWORK_ERROR = "NETWORK_ERROR"
}

enum class SocketEventType(val event: String) {
    // Client -> Server
    SEND_MESSAGE("send_message"),
    JOIN_CONVERSATION("join_conversation"),
    LEAVE_CONVERSATION("leave_conversation"),
    MARK_READ("mark_read"),
    TYPING_START("typing_start"),
    TYPING_STOP("typing_stop"),
    PING("ping"),
    // Server -> Client
    MESSAGE_RECEIVED("message_received"),
    MESSAGE_DELIVERED("message_delivered"),
    MESSAGE_READ("message_read"),
    USER_TYPING("user_typing"),
    USER_ONLINE("user_online"),
    USER_OFFLINE("user_offline"),
    BALANCE_UPDATE("balance_update"),
    PONG("pong"),
    // Calling & WebRTC (Bidirectional)
    INCOMING_CALL("incoming_call"),
    CALL_ANSWERED("call_answered"),
    CALL_REJECTED("call_rejected"),
    CALL_ENDED("call_ended"),
    CALL_SIGNAL("call_signal");

    companion object {
        fun fromEvent(event: String?): SocketEventType? =
            values().firstOrNull { it.event.equals(event, ignoreCase = true) }
    }
}

