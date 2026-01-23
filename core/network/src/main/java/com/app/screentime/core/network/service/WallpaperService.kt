package com.app.screentime.core.network.service

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.core.network.model.WallpaperApiResponse

/**
 * Service interface for Wallpaper related API operations
 */
interface WallpaperService {
    suspend fun getWallpapers(): Result<ApiResponse<List<WallpaperApiResponse>>>
}
