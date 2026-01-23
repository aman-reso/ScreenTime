package com.app.screentime.location.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.config.R
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.location.usecase.LocationUseCase
import com.app.screentime.network.model.LocationData
import com.app.screentime.network.model.UserLastLocationData
import com.app.screentime.analytics.AnalyticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Location Management Screen
 * Manages UI state and coordinates with UseCase
 */
@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationUseCase: LocationUseCase,
    private val analyticsUseCase: AnalyticsUseCase,
    private val preferencesManager: PreferencesManager,
    @ApplicationContext private val context: android.content.Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    init {
        analyticsUseCase.trackLocationScreen()
        fetchUserLastLocation()
    }

    /**
     * Fetch current location from device
     * @param useHighAccuracy Whether to use high accuracy (GPS) or balanced (network)
     */
    fun fetchCurrentLocation(useHighAccuracy: Boolean = true) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                state = LocationState.LOADING,
                error = null
            )

            locationUseCase.fetchCurrentLocation(useHighAccuracy).fold(
                onSuccess = { locationData ->
                    // Preserve shareLocation status from current state
                    val currentShareLocation = _uiState.value.location?.shareLocation ?: false
                    val updatedLocation = locationData.copy(shareLocation = currentShareLocation)

                    _uiState.value = _uiState.value.copy(
                        state = LocationState.SUCCESS,
                        location = updatedLocation,
                        error = null
                    )
                    if (locationData.latitude != null && locationData.longitude != null) {
                        syncLocationToServer(updatedLocation)
                    }
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        state = LocationState.ERROR,
                        error = exception.message
                            ?: context.getString(R.string.failed_to_fetch_location)
                    )
                }
            )
        }
    }

    /**
     * Sync location to server (internal method)
     */
    private fun syncLocationToServer(locationData: LocationData) {
        viewModelScope.launch {
            locationUseCase.syncLocationToServer(locationData).fold(
                onSuccess = {
                    // Location synced successfully, update lastUpdated if needed
                    _uiState.value = _uiState.value.copy(
                        location = _uiState.value.location?.copy(
                            lastUpdated = it.syncedAt ?: locationData.lastUpdated
                        )
                    )
                },
                onFailure = {
                    // Silently fail - don't show error to user for background sync
                    // Error is logged but doesn't affect UI
                }
            )
        }
    }

    /**
     * Toggle share location status and sync to server if enabled
     */
    fun toggleShareLocation(shareLocation: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdating = true, error = null)

            val currentLocation = _uiState.value.location
            val updatedLocation = currentLocation?.copy(shareLocation = shareLocation)
                ?: LocationData(shareLocation = shareLocation)

            // If shareLocation is enabled and we have location data, sync to server
            if (shareLocation && updatedLocation.latitude != null && updatedLocation.longitude != null) {
                locationUseCase.syncLocationToServer(updatedLocation).fold(
                    onSuccess = { syncResponse ->
                        _uiState.value = _uiState.value.copy(
                            location = updatedLocation.copy(
                                lastUpdated = syncResponse.syncedAt ?: updatedLocation.lastUpdated
                            ),
                            isUpdating = false,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        // Show error if sync fails when user explicitly toggles ON
                        _uiState.value = _uiState.value.copy(
                            location = updatedLocation,
                            isUpdating = false,
                            error = exception.message
                                ?: context.getString(R.string.failed_to_sync_location)
                        )
                    }
                )
            } else {
                // Just update the state if disabled or no location data
                _uiState.value = _uiState.value.copy(
                    location = updatedLocation,
                    isUpdating = false
                )
            }
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Set permission denied state
     */
    fun setPermissionDenied() {
        _uiState.value = _uiState.value.copy(
            state = LocationState.PERMISSION_DENIED,
            error = context.getString(R.string.location_permission_denied)
        )
    }

    /**
     * Set location dialog denied state (when user denies location settings dialog)
     */
    fun setLocationDialogDenied() {
        _uiState.value = _uiState.value.copy(
            state = LocationState.LOCATION_DIALOG_DENIED,
            error = context.getString(R.string.location_permission_required)
        )
    }

    /**
     * Fetch user's last location from server
     */
    fun fetchUserLastLocation() {
        val username = preferencesManager.getUsername()
        if (username == null) return

        viewModelScope.launch {
            locationUseCase.getUserLastLocation(username).fold(
                onSuccess = { locationData ->
                    _uiState.value = _uiState.value.copy(
                        userLastLocation = locationData
                    )
                },
                onFailure = {
                    // Silently fail - don't show error for last location fetch
                }
            )
        }
    }
}

/**
 * Location state enum
 */
enum class LocationState {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR,
    PERMISSION_DENIED,
    LOCATION_DIALOG_DENIED
}

/**
 * UI State for Location Management Screen
 */
data class LocationUiState(
    val state: LocationState = LocationState.IDLE,
    val location: LocationData? = null,
    val userLastLocation: UserLastLocationData? = null,
    val isUpdating: Boolean = false,
    val error: String? = null
) {
    /**
     * Convenience property to check if loading
     */
    val isLoading: Boolean
        get() = state == LocationState.LOADING
}

