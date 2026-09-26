package com.app.screentime.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val phone: String,
    val name: String,
    val role: String   // "user" or "model"
)

@Serializable
data class GoogleAuthRequest(
    val email: String? = null,
    val google_id: String? = null,
    val name: String? = null,
    val photo_url: String? = null,
    val id_token: String? = null,
    val idToken: String? = null,
    val token: String? = null,
    val credential: String? = null,
    val avatar_url: String? = null,
    val avatarUrl: String? = null,
    val birth_date: String? = null,
    val role: String = "user"
)

@Serializable
data class RechargeRequest(
    val amount: Double
)

@Serializable
data class CreateRoomRequest(
    val title: String,
    val rate_per_min: Double
)

@Serializable
data class ReportRequest(
    val reported_id: String,
    val reason: String
)

@Serializable
data class ModelOnboardRequest(
    val bio: String,
    val voice_rate_per_min: Double,
    val group_rate_per_min: Double,
    val chat_rate_per_msg: Double
)

@Serializable
data class CheckCallBalanceRequest(
    val model_id: String,
    val call_type: String = "voice"
)

@Serializable
data class SendChatMessageRequest(
    val receiver_id: String,
    val content: String
)

@Serializable
data class RegisterFcmTokenRequest(
    val fcm_token: String,
    val platform: String = "android",
    val device_id: String? = null,
    val app_version: String? = null
)
