package com.app.screentime.navigation

import com.app.screentime.wallpaper.api.model.ImageItem

/**
 * Data classes for screen parameters in Navigation 3
 */
data class ChallengeDetailParams(
    val challengeId: String
)

data class RecordDetailParams(
    val username: String
)

data class SingleAppUsageDetailParams(
    val packageName: String
)

data class AppDetailsParams(
    val packageName: String
)

data class FullScreenWallpaperParams(
    val wallpaperId: String,
    val imageItemJson: ImageItem? = null
)

























