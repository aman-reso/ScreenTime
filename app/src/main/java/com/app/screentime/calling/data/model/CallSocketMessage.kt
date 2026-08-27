package com.app.screentime.calling.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * WebSocket signaling envelope matching Connect backend protocol.
 */
@Serializable
data class CallSocketMessage(
    @SerialName("type")
    val type: String,

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
    val reason: String? = null,

    @SerialName("payload")
    val payload: JsonElement? = null
)

/**
 * Constants for WebSocket signaling message types.
 */
object CallMessageTypes {
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
    const val INSUFFICIENT_BALANCE = "CALL_INSUFFICIENT_BALANCE"
    const val BALANCE_LOW_WARNING = "BALANCE_LOW_WARNING"
    const val BALANCE_EXHAUSTED = "CALL_ENDED_BALANCE_EXHAUSTED"
    const val CALL_TICK = "CALL_TICK"
    const val PRESENCE_UPDATE = "PRESENCE_UPDATE"
    const val SESSION_TERMINATED = "SESSION_TERMINATED"

    const val WEBRTC_OFFER = "WEBRTC_OFFER"
    const val WEBRTC_ANSWER = "WEBRTC_ANSWER"
    const val WEBRTC_ICE_CANDIDATE = "WEBRTC_ICE_CANDIDATE"
}
