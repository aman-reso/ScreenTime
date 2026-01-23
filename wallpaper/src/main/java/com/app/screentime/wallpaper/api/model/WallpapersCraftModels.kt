package com.app.screentime.wallpaper.api.model

import com.app.screentime.wallpaper.model.Wallpaper
import com.app.screentime.wallpaper.model.WallpaperCategory
import kotlinx.serialization.Serializable

/**
 * Response model for categories API
 */
@Serializable
data class CategoriesResponse(
    val count: Int,
    val items: List<CategoryItem>,
    val response_time: String? = null
)

/**
 * Category item from API
 */
@Serializable
data class CategoryItem(
    val count_new: Int,
    val id: Int,
    val title: String
)

/**
 * Response model for images API
 */
@Serializable
data class ImagesResponse(
    val count: Int = 0, // Default 0 for endpoints that don't provide count (like new images)
    val items: List<ImageItem>,
    val response_time: String? = null,
    val first_published_id: Int? = null // For new images endpoint
)

/**
 * Image item from API
 */
@Serializable
data class ImageItem(
    val id: Int,
    val category_id: Int,
    val title: String? = null,
    val description: String? = null,
    val author: String? = null,
    val rating: Int? = null,
    val downloads: Int? = null,
    val favorites: Int? = null,
    val cost: Int? = null,
    val variations: ImageVariations?,
    val colors: List<List<Int>>? = null,
    val tags: List<String>? = null,
    val uploaded_at: String? = null
) {
    /**
     * Convert ImageItem to Wallpaper object
     */
    fun toWallpaper(): Wallpaper {
        // Use preview_small for thumbnail, adapted for full image
        val thumbnailUrl = variations?.preview_small?.url
        val imageUrl = variations?.adapted?.url
            ?: variations?.original?.url
            ?: variations?.preview_small?.url

        return Wallpaper(
            id = "wallpaperscraft_$id",
            name = title ?: "Wallpaper $id",
            thumbnailUrl = thumbnailUrl,
            imageUrl = imageUrl,
            localPath = null,
            category = mapCategoryIdToCategory(category_id),
            isLocal = false
        )
    }

    /**
     * Map category ID to WallpaperCategory enum
     * Since we don't know all categories, we'll use DEFAULT for now
     * This can be enhanced later with a mapping table
     */
    private fun mapCategoryIdToCategory(categoryId: Int): WallpaperCategory {
        // You can add specific mappings here if needed
        return WallpaperCategory.DEFAULT
    }
}

/**
 * Image variations with different resolutions
 */
@Serializable
data class ImageVariations(
    val preview_small: ImageVariation? = null,
    val adapted: ImageVariation? = null,
    val adapted_landscape: ImageVariation? = null,
    val original: ImageVariation? = null
)

/**
 * Image variation details
 */
@Serializable
data class ImageVariation(
    val url: String,
    val resolution: ImageResolution,
    val size: Int
)

/**
 * Image resolution
 */
@Serializable
data class ImageResolution(
    val width: Int,
    val height: Int
)
