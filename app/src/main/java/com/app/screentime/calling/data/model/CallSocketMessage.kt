package com.app.screentime.calling.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * WebSocket signaling envelope matching Connect backend generic protocol.
 */
@Serializable
data class CallSocketMessage(
    @SerialName("type")
    val type: String,

    @SerialName("event")
    val event: String? = null,

    @SerialName("session_id")
    val sessionId: String? = null,

    @SerialName("sender_id")
    val senderId: String? = null,

    @SerialName("target_id")
    val targetId: String? = null,

    @SerialName("signal_type")
    val signalType: String? = null,

    @SerialName("payload")
    val payload: JsonElement? = null,

    @SerialName("content")
    val content: String? = null,

    @SerialName("conversation_id")
    val conversationId: String? = null,

    @SerialName("data")
    val data: JsonElement? = null,

    @SerialName("error")
    val error: String? = null,

    @SerialName("online")
    val online: Boolean? = null,

    @SerialName("is_typing")
    val isTyping: Boolean? = null,

    @SerialName("timestamp")
    val timestamp: Long? = null,

    // Aliases & legacy fields
    @SerialName("call_id")
    val callId: String? = null,

    @SerialName("room_id")
    val roomId: String? = null,

    @SerialName("caller_id")
    val callerId: String? = null,

    @SerialName("receiver_id")
    val receiverId: String? = null,

    @SerialName("rate_per_min")
    val ratePerMin: Double? = null,

    @SerialName("duration_sec")
    val durationSec: Int? = null,

    @SerialName("remaining_sec")
    val remainingSec: Int? = null,

    @SerialName("cost")
    val cost: Double? = null,

    @SerialName("reason")
    val reason: String? = null
)

/**
 * Constants for WebSocket signaling message types.
 */
object CallMessageTypes {
    const val CONNECTED = "connected"
    const val AUTH = "auth"
    const val ERROR = "error"
    const val PING = "ping"
    const val PONG = "pong"

    const val USER_ONLINE = "user_online"
    const val USER_OFFLINE = "user_offline"
    const val PRESENCE = "presence"

    const val CHAT_MESSAGE = "chat_message"
    const val MESSAGE_SENT = "message_sent"
    const val NEW_MESSAGE = "new_message"
    const val TYPING = "typing"
    const val USER_TYPING = "user_typing"
    const val MESSAGE_READ = "message_read"

    const val CALL_INITIATE = "call_initiate"
    const val INCOMING_CALL = "incoming_call"
    const val CALL_ACCEPT = "call_accept"
    const val CALL_ACCEPTED = "call_accepted"
    const val CALL_ACTIVE = "CALL_ACTIVE"
    const val CALL_REJECT = "call_reject"
    const val CALL_REJECTED = "call_rejected"
    const val CALL_END = "call_end"
    const val CALL_ENDED = "call_ended"
    const val CALL_CANCEL = "call_cancel"
    const val CALL_CANCELLED = "call_cancelled"
    const val CALL_BUSY = "call_busy"
    const val CALL_OFFLINE = "CALL_OFFLINE"

    const val SIGNAL = "signal"
    const val WEBRTC_SIGNAL = "webrtc_signal"
    const val WEBRTC_OFFER = "offer"
    const val WEBRTC_ANSWER = "answer"
    const val WEBRTC_ICE_CANDIDATE = "ice-candidate"

    const val CALL_REQUEST = "CALL_REQUEST"
    const val INSUFFICIENT_BALANCE = "CALL_INSUFFICIENT_BALANCE"
    const val BALANCE_LOW_WARNING = "BALANCE_LOW_WARNING"
    const val BALANCE_EXHAUSTED = "CALL_ENDED_BALANCE_EXHAUSTED"
    const val CALL_TICK = "CALL_TICK"
    const val PRESENCE_UPDATE = "PRESENCE_UPDATE"
    const val SESSION_TERMINATED = "SESSION_TERMINATED"
}
