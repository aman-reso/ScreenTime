package com.app.screentime.feature.preferences.domain.model

/**
 * Domain model representing user discovery and matching preferences.
 * Corresponds to Figma node-id 84-1040.
 */
data class UserPreferences(
    val showMe: String = "Men",
    val minAge: Float = 22f,
    val maxAge: Float = 30f,
    val maxDistanceKm: Float = 50f,
    val isGlobal: Boolean = false,
    val religion: String = "No Preference",
    val heightRange: String = "5ft 2in - 6ft 0in",
    val minHeight: Int? = 160,
    val maxHeight: Int? = 185,
    val education: String = "Any",
    val drinking: String = "Social Drinker",
    val smoking: String = "Never",
    val children: String = "Open to Kids"
)
