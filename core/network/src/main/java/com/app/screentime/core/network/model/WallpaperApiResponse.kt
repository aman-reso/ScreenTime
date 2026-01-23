package com.app.screentime.core.network.model

import kotlinx.serialization.Serializable

/**
 * API response model for wallpaper list
 */
@Serializable
data class WallpaperApiResponse(
    val type: String,
    val imageUrls: List<String>
)
