package com.app.screentime.location.repository

import com.app.screentime.location.service.LocationService
import com.app.screentime.network.model.LocationData
import com.app.screentime.network.model.LocationSyncRequest
import com.app.screentime.network.model.LocationSyncResponse
import com.app.screentime.network.model.UserLastLocationData
import com.app.screentime.network.model.UserLastLocationResponse
import javax.inject.Inject

/**
 * Repository for Location operations
 */
class LocationRepository @Inject constructor(
    private val locationService: LocationService
) {

    suspend fun syncLocation(request: LocationSyncRequest): Result<LocationSyncResponse> {
        return locationService.syncLocation(request).fold(
            onSuccess = { apiResponse ->
                if (apiResponse.data != null) {
                    Result.success(apiResponse.data!!)
                } else {
                    Result.failure(Exception(apiResponse.message ?: "No data received"))
                }
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    suspend fun getUserLastLocation(username: String): Result<UserLastLocationData> {
        return locationService.getUserLastLocation(username).fold(
            onSuccess = { apiResponse ->
                if (apiResponse.data?.location != null) {
                    Result.success(apiResponse.data!!.location!!)
                } else {
                    Result.failure(Exception(apiResponse.message ?: "No location data received"))
                }
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

}

