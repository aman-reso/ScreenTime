package com.app.screentime.location.service

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.LocationData
import com.app.screentime.network.model.LocationResponse
import com.app.screentime.network.model.LocationSyncRequest
import com.app.screentime.network.model.LocationSyncResponse
import com.app.screentime.network.model.ShareLocationRequest
import com.app.screentime.network.model.UserLastLocationResponse

/**
 * Service interface for Location operations
 */
interface LocationService {


    /**
     * Sync location to server
     */
    suspend fun syncLocation(request: LocationSyncRequest): Result<ApiResponse<LocationSyncResponse>>
    
    /**
     * Get user's last location
     */
    suspend fun getUserLastLocation(username: String): Result<ApiResponse<UserLastLocationResponse>>
}

