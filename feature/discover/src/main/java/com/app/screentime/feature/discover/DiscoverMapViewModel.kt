package com.app.screentime.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.discover.domain.usecase.GetDiscoveryDeckUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.sin

data class MapCandidate(
    val profile: ModelProfile,
    val latLng: LatLng
)

data class DiscoverMapUiState(
    val isLoading: Boolean = true,
    val candidates: List<MapCandidate> = emptyList(),
    val selectedCandidate: MapCandidate? = null,
    val userLocation: LatLng = LatLng(12.9716, 77.5946),
    val cityName: String = "Nearby",
    val isEmptyRadius: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DiscoverMapViewModel @Inject constructor(
    private val getDiscoveryDeckUseCase: GetDiscoveryDeckUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverMapUiState(isLoading = true))
    val uiState: StateFlow<DiscoverMapUiState> = _uiState.asStateFlow()

    init {
        loadCandidates()
    }

    fun loadCandidates(limit: Int = 30) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = getDiscoveryDeckUseCase(page = 1, limit = limit)

            result.fold(
                onSuccess = { models ->
                    val defaultCenterLat = 12.9716
                    val defaultCenterLng = 77.5946

                    val firstWithCoords = models.firstOrNull {
                        it.lat != null && it.lng != null && it.lat != 0.0 && it.lng != 0.0
                    }
                    val centerLat = firstWithCoords?.lat ?: defaultCenterLat
                    val centerLng = firstWithCoords?.lng ?: defaultCenterLng
                    val userLocation = LatLng(centerLat, centerLng)

                    val cityName = models.firstOrNull { it.location.isNotBlank() }?.location ?: "Nearby"

                    val candidates = models.mapIndexed { index, model ->
                        val lat = if (model.lat != null && model.lat != 0.0) {
                            model.lat!!
                        } else {
                            val angle = (index * 45) * (Math.PI / 180)
                            val offsetDist = 0.006 * (1 + (index % 3))
                            centerLat + (cos(angle) * offsetDist)
                        }

                        val lng = if (model.lng != null && model.lng != 0.0) {
                            model.lng!!
                        } else {
                            val angle = (index * 45) * (Math.PI / 180)
                            val offsetDist = 0.006 * (1 + (index % 3))
                            centerLng + (sin(angle) * offsetDist)
                        }

                        MapCandidate(
                            profile = model,
                            latLng = LatLng(lat, lng)
                        )
                    }

                    val currentSelectedId = _uiState.value.selectedCandidate?.profile?.id
                    val selected = candidates.firstOrNull { it.profile.id == currentSelectedId }
                        ?: candidates.firstOrNull()

                    _uiState.value = DiscoverMapUiState(
                        isLoading = false,
                        candidates = candidates,
                        selectedCandidate = selected,
                        userLocation = userLocation,
                        cityName = cityName,
                        isEmptyRadius = false,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.localizedMessage ?: "Failed to load candidates"
                    )
                }
            )
        }
    }

    fun selectCandidate(candidate: MapCandidate) {
        _uiState.value = _uiState.value.copy(selectedCandidate = candidate)
    }

    fun toggleEmptyRadius() {
        _uiState.value = _uiState.value.copy(isEmptyRadius = !_uiState.value.isEmptyRadius)
    }

    fun setEmptyRadius(empty: Boolean) {
        _uiState.value = _uiState.value.copy(isEmptyRadius = empty)
    }
}
