package com.app.screentime.wallpaper.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.app.screentime.wallpaper.model.Wallpaper
import com.app.screentime.wallpaper.model.WallpaperCategory
import com.app.screentime.wallpaper.model.WallpaperType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.model.WallpaperApiResponse
import com.app.screentime.core.network.service.WallpaperService

@Serializable
data class WallpaperData(
    val id: String,
    val name: String,
    val thumbnailUrl: String? = null,
    val imageUrl: String? = null,
    val localPath: String? = null,
    val category: String = "DEFAULT",
    val isLocal: Boolean = false
)

@Singleton
class WallpaperRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val wallpaperService: WallpaperService,
    private val mapper: com.app.screentime.wallpaper.mapper.WallpaperMapper
) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("wallpaper_prefs", Context.MODE_PRIVATE)
    }

    private val json = Json { ignoreUnknownKeys = true }

    private val WALLPAPER_LIST_KEY = "wallpaper_list"
    private val CURRENT_HOME_WALLPAPER_KEY = "current_home_wallpaper"
    private val CURRENT_LOCK_WALLPAPER_KEY = "current_lock_wallpaper"

    /**
     * Save a wallpaper to the list
     */
    fun saveWallpaper(wallpaper: Wallpaper) {
        val wallpapers = getAllWallpapers().toMutableList()
        val existingIndex = wallpapers.indexOfFirst { it.id == wallpaper.id }

        if (existingIndex >= 0) {
            wallpapers[existingIndex] = wallpaper
        } else {
            wallpapers.add(wallpaper)
        }

        saveWallpaperList(wallpapers)
    }

    /**
     * Get all saved wallpapers
     */
    fun getAllWallpapers(): List<Wallpaper> {
        val jsonString = prefs.getString(WALLPAPER_LIST_KEY, null) ?: return emptyList()
        return try {
            val dataList: List<WallpaperData> = json.decodeFromString(jsonString)
            dataList.map { it.toWallpaper() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Get wallpaper by ID
     */
    fun getWallpaperById(id: String): Wallpaper? {
        return getAllWallpapers().find { it.id == id }
    }

    /**
     * Delete a wallpaper
     */
    fun deleteWallpaper(id: String) {
        val wallpapers = getAllWallpapers().toMutableList()
        wallpapers.removeAll { it.id == id }
        saveWallpaperList(wallpapers)

        // Also remove from current wallpaper if it's set
        if (getCurrentHomeWallpaperId() == id) {
            clearCurrentHomeWallpaper()
        }
        if (getCurrentLockWallpaperId() == id) {
            clearCurrentLockWallpaper()
        }
    }

    /**
     * Set current wallpaper for home screen
     */
    fun setCurrentHomeWallpaper(wallpaperId: String) {
        prefs.edit {
            putString(CURRENT_HOME_WALLPAPER_KEY, wallpaperId)
        }
    }

    /**
     * Set current wallpaper for lock screen
     */
    fun setCurrentLockWallpaper(wallpaperId: String) {
        prefs.edit {
            putString(CURRENT_LOCK_WALLPAPER_KEY, wallpaperId)
        }
    }

    /**
     * Get current home wallpaper ID
     */
    fun getCurrentHomeWallpaperId(): String? {
        return prefs.getString(CURRENT_HOME_WALLPAPER_KEY, null)
    }

    /**
     * Get current lock wallpaper ID
     */
    fun getCurrentLockWallpaperId(): String? {
        return prefs.getString(CURRENT_LOCK_WALLPAPER_KEY, null)
    }

    /**
     * Get current home wallpaper
     */
    fun getCurrentHomeWallpaper(): Wallpaper? {
        val id = getCurrentHomeWallpaperId() ?: return null
        return getWallpaperById(id)
    }

    /**
     * Get current lock wallpaper
     */
    fun getCurrentLockWallpaper(): Wallpaper? {
        val id = getCurrentLockWallpaperId() ?: return null
        return getWallpaperById(id)
    }

    /**
     * Clear current home wallpaper
     */
    fun clearCurrentHomeWallpaper() {
        prefs.edit {
            remove(CURRENT_HOME_WALLPAPER_KEY)
        }
    }

    /**
     * Clear current lock wallpaper
     */
    fun clearCurrentLockWallpaper() {
        prefs.edit {
            remove(CURRENT_LOCK_WALLPAPER_KEY)
        }
    }

    private fun saveWallpaperList(wallpapers: List<Wallpaper>) {
        val dataList = wallpapers.map { it.toWallpaperData() }
        val jsonString = json.encodeToString(dataList)
        prefs.edit {
            putString(WALLPAPER_LIST_KEY, jsonString)
        }
    }

    private fun Wallpaper.toWallpaperData(): WallpaperData {
        return WallpaperData(
            id = id,
            name = name,
            thumbnailUrl = thumbnailUrl,
            imageUrl = imageUrl,
            localPath = localPath,
            category = category.name,
            isLocal = isLocal
        )
    }

    private fun WallpaperData.toWallpaper(): Wallpaper {
        return Wallpaper(
            id = id,
            name = name,
            thumbnailUrl = thumbnailUrl,
            imageUrl = imageUrl,
            localPath = localPath,
            category = try {
                WallpaperCategory.valueOf(category)
            } catch (e: Exception) {
                WallpaperCategory.DEFAULT
            },
            isLocal = isLocal
        )
    }

    /**
     * Fetch remote wallpapers from API
     */
    suspend fun fetchRemoteWallpapers(): List<Wallpaper> {
        val result = wallpaperService.getWallpapers()

        return result.fold(
            onSuccess = { apiResponse ->
                if (apiResponse.success == true && apiResponse.data != null) {
                    mapper.mapToUiModels(apiResponse.data!!)
                } else {
                    getDummyWallpapers()
                }
            },
            onFailure = {
                // Fallback to dummy as requested
                getDummyWallpapers()
            }
        )
    }

    private fun getDummyWallpapers(): List<Wallpaper> {
        return listOf(
            Wallpaper(
                id = "dummy_error",
                name = "Sample Wallpaper",
                imageUrl = "https://images.unsplash.com/photo-1624555130581-1d9cca783bc0?q=80&w=1471&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                category = WallpaperCategory.DEFAULT,
                isLocal = false
            )
        )
    }
}
