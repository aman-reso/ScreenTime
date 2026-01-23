package com.app.screentime.wallpaper.mapper

import com.app.screentime.core.network.model.WallpaperApiResponse
import com.app.screentime.wallpaper.model.Wallpaper
import com.app.screentime.wallpaper.model.WallpaperCategory
import javax.inject.Inject

/**
 * Mapper for wallpaper-related models
 */
class WallpaperMapper @Inject constructor() {

    /**
     * Map a List of WallpaperApiResponse to a List of Wallpaper
     */
    fun mapToUiModels(apiResponseData: List<WallpaperApiResponse>): List<Wallpaper> {
        return apiResponseData.flatMap { wallpaperSet ->
            wallpaperSet.imageUrls.mapIndexed { index, url ->
                Wallpaper(
                    id = "remote_${wallpaperSet.type}_$index",
                    name = "Wallpaper ${wallpaperSet.type.capitalizeWords()} $index",
                    imageUrl = url,
                    category = mapToCategory(wallpaperSet.type),
                    isLocal = false
                )
            }
        }
    }

    /**
     * Map type string to WallpaperCategory enum
     */
    private fun mapToCategory(type: String): WallpaperCategory {
        return try {
            WallpaperCategory.valueOf(type.uppercase())
        } catch (e: Exception) {
            WallpaperCategory.DEFAULT
        }
    }

    /**
     * Helper to capitalize words in a string
     */
    private fun String.capitalizeWords(): String {
        return split("_", " ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }
}
