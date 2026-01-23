package com.app.screentime.navigation

import com.app.screentime.wallpaper.api.model.ImageItem
import kotlinx.serialization.Serializable

/**
 * Data classes for screen parameters in Navigation 3
 */
@Serializable
data class ChallengeDetailParams(
    val challengeId: String
)

@Serializable
data class RecordDetailParams(
    val username: String
)
@Serializable
data class SingleAppUsageDetailParams(
    val packageName: String
)

@Serializable
data class AppDetailsParams(
    val packageName: String
)
@Serializable
data class FullScreenWallpaperParams(
    val wallpaperId: String,
    val imageItemJson: ImageItem? = null
)

























