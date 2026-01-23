package com.app.screentime.wallpaper.mapper

import com.app.screentime.wallpaper.api.model.CategoryItem
import com.app.screentime.wallpaper.api.model.ImageItem
import com.app.screentime.wallpaper.model.Wallpaper
import com.app.screentime.wallpaper.model.WallpaperCategory
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mapper for WallpapersCraft API responses
 */
@Singleton
class WallpapersCraftMapper @Inject constructor() {

    /**
     * Map CategoryItem to a simple category representation
     */
    fun mapCategory(categoryItem: CategoryItem): Pair<Int, String> {
        return Pair(categoryItem.id, categoryItem.title)
    }

    /**
     * Map ImageItem to Wallpaper model
     */
    fun mapToWallpaper(imageItem: ImageItem): Wallpaper {
        // Use preview_small for thumbnail, adapted for full image
        val thumbnailUrl = imageItem.variations?.preview_small?.url
        val imageUrl = imageItem.variations?.adapted?.url
            ?: imageItem.variations?.original?.url
            ?: imageItem.variations?.preview_small?.url

        return Wallpaper(
            id = "wallpaperscraft_${imageItem.id}",
            name = imageItem.title ?: "Wallpaper ${imageItem.id}",
            thumbnailUrl = thumbnailUrl,
            imageUrl = imageUrl,
            localPath = null,
            category = mapCategoryIdToCategory(imageItem.category_id),
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
