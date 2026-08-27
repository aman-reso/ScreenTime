package com.app.screentime.calling.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Payload for WebRTC SDP Offers and Answers.
 */
@Serializable
data class SdpPayload(
    @SerialName("type")
    val type: String,

    @SerialName("sdp")
    val sdp: String
)

/**
 * Payload for WebRTC ICE Candidates.
 */
@Serializable
data class IceCandidatePayload(
    @SerialName("candidate")
    val candidate: String,

    @SerialName("sdp_mid")
    val sdpMid: String? = null,

    @SerialName("sdp_m_line_index")
    val sdpMLineIndex: Int? = null
)

/**
 * Payload for incoming call metadata (caller profile).
 */
@Serializable
data class IncomingCallPayload(
    @SerialName("caller_name")
    val callerName: String? = null,

    @SerialName("caller_avatar")
    val callerAvatar: String? = null
)
