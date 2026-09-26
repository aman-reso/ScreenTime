package com.app.screentime.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateRequest(
    val name: String? = null,
    val display_name: String? = null,
    val dob: String? = null,
    val date_of_birth: String? = null,
    val height: Int? = null,
    val height_cm: Int? = null,
    val gender: String? = null,
    val interested_in: String? = null,
    val relationship_intention: String? = null,
    val dating_intent: String? = null,
    val relation_type: String? = null,
    val bio: String? = null,
    val city: String? = null,
    val area: String? = null,
    val job: String? = null,
    val occupation: String? = null,
    val education: String? = null,
    val languages: List<String>? = null,
    val income: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val avatar_url: String? = null,
    val photos: List<String>? = null
)

@Serializable
data class ProfileUpdateResponse(
    val success: Boolean = true,
    val message: String = ""
)
