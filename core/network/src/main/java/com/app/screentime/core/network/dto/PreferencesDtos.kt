package com.app.screentime.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Dedicated Preferences API request DTO.
 * PUT /api/preferences
 */
@Serializable
data class UpdatePreferencesRequest(
    @SerialName("show_me") val show_me: String? = null,
    @SerialName("min_age") val min_age: Int? = null,
    @SerialName("max_age") val max_age: Int? = null,
    @SerialName("distance") val distance: Int? = null,
    @SerialName("min_height") val min_height: Int? = null,
    @SerialName("max_height") val max_height: Int? = null,
    @SerialName("religion") val religion: String? = null,
    @SerialName("education") val education: String? = null,
    @SerialName("drinking") val drinking: String? = null,
    @SerialName("smoking") val smoking: String? = null
)

/**
 * Preferences API response DTO.
 */
@Serializable
data class PreferencesResponse(
    val success: Boolean = true,
    val message: String = ""
)
