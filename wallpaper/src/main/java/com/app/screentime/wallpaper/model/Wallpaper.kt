package com.app.screentime.wallpaper.model

/**
 * Represents a wallpaper item
 */
data class Wallpaper(
    val id: String,
    val name: String,
    val thumbnailUrl: String? = null,
    val imageUrl: String? = null,
    val localPath: String? = null,
    val category: WallpaperCategory = WallpaperCategory.DEFAULT,
    val isLocal: Boolean = false
)

/**
 * Wallpaper categories
 */
enum class WallpaperCategory {
    DEFAULT,
    NATURE,
    ABSTRACT,
    MINIMALIST,
    DARK,
    LIGHT,
    CUSTOM
}

/**
 * Wallpaper type (home screen, lock screen, or both)
 */
enum class WallpaperType {
    HOME,
    LOCK,
    BOTH
}

