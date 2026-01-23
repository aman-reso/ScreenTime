package com.app.screentime.core.network.model

import kotlinx.serialization.Serializable

/**
 * Base API response wrapper
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean? = null,
    val status: Int? = null,
    val data: T? = null,
    val message: String? = null,
    val timestamp: String? = null,
    val error: ApiError? = null
)

/**
 * API error model
 */
@Serializable
data class ApiError(
    val code: String? = null,
    val message: String? = null,
    val details: Map<String, String>? = null
)

/**
 * Device registration response
 */
@Serializable
data class DeviceRegistrationResponse(
    val userId: String,
    val username: String,
    val createdAt: String,
    val totpSecret: String? = null,
    val totpEnabled: Boolean = false,
    val totpPeriod: Int = 60,
    val updatedAt: String? = null,
    val lastSyncTime: String? = null,
    val email: String? = null,
    val name: String? = null
) {
    val deviceId: String get() = userId
}
