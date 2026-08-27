package com.app.screentime.core.network.websocket

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class WSMessage(
    val type: String,
    val call_id: String? = null,
    val room_id: String? = null,
    val caller_id: String? = null,
    val caller_name: String? = null,
    val caller_avatar: String? = null,
    val receiver_id: String? = null,
    val call_type: String? = null,
    val rate_per_min: Double? = null,
    val duration_sec: Int? = null,
    val remaining_sec: Int? = null,
    val cost: Double? = null,
    val reason: String? = null,
    val payload: JsonElement? = null
)

object WSEventTypes {
    const val CALL_REQUEST = "CALL_REQUEST"
    const val INCOMING_CALL = "INCOMING_CALL"
    const val CALL_ACCEPT = "CALL_ACCEPT"
    const val CALL_ACTIVE = "CALL_ACTIVE"
    const val CALL_REJECT = "CALL_REJECT"
    const val CALL_REJECTED = "CALL_REJECTED"
    const val CALL_END = "CALL_END"
    const val CALL_ENDED = "CALL_ENDED"
    const val CALL_BUSY = "CALL_BUSY"
    const val CALL_OFFLINE = "CALL_OFFLINE"
    const val CALL_INSUFFICIENT_BALANCE = "CALL_INSUFFICIENT_BALANCE"
    const val BALANCE_LOW_WARNING = "BALANCE_LOW_WARNING"
    const val CALL_ENDED_BALANCE_EXHAUSTED = "CALL_ENDED_BALANCE_EXHAUSTED"
    const val CALL_TICK = "CALL_TICK"
    const val PRESENCE_UPDATE = "PRESENCE_UPDATE"
    const val SESSION_TERMINATED = "SESSION_TERMINATED"
    const val CHAT_MESSAGE = "CHAT_MESSAGE"
    const val CHAT_RECEIVED = "CHAT_RECEIVED"
    const val WEBRTC_OFFER = "WEBRTC_OFFER"
    const val WEBRTC_ANSWER = "WEBRTC_ANSWER"
    const val WEBRTC_ICE_CANDIDATE = "WEBRTC_ICE_CANDIDATE"
    const val NETWORK_ERROR = "NETWORK_ERROR"
}
