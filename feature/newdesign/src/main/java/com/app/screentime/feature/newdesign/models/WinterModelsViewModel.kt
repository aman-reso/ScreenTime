package com.app.screentime.feature.newdesign.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.dto.ForYouOfferDto
import com.app.screentime.core.network.dto.ProfileUpdateRequest
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.feature.newdesign.R
import com.app.screentime.feature.newdesign.components.CreatorStatus
import com.app.screentime.feature.newdesign.dialogs.GenderOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WinterModelsUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val trendingProfiles: List<CreatorModel> = emptyList(),
    val offers: List<ForYouOfferDto> = emptyList(),
    val models: List<CreatorModel> = emptyList(),
    val showGenderDialog: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class WinterModelsViewModel @Inject constructor(
    private val winterApi: WinterApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(WinterModelsUiState())
    val uiState: StateFlow<WinterModelsUiState> = _uiState.asStateFlow()

    init {
        loadForYouFeed()
        checkGenderPrompt()
    }

    private fun checkGenderPrompt() {
        val alreadyAsked = preferencesManager.isGenderAsked()
        val currentGender = preferencesManager.getUserGender()
        if (!alreadyAsked && currentGender.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(showGenderDialog = true)
        }
    }

    fun onGenderSelected(genderOption: GenderOption) {
        val genderStr = if (genderOption == GenderOption.MAN) "male" else "female"
        preferencesManager.setUserGender(genderStr)
        preferencesManager.setGenderAsked(true)
        _uiState.value = _uiState.value.copy(showGenderDialog = false)

        viewModelScope.launch {
            try {
                val token = sessionManager.getToken()
                if (!token.isNullOrBlank()) {
                    winterApi.updateProfile(
                        token = token,
                        request = ProfileUpdateRequest(gender = genderStr)
                    )
                }
            } catch (e: Exception) {
                Log.e("WinterModelsVM", "Failed to sync gender to backend: ${e.message}")
            }
        }
    }

    fun dismissGenderDialog() {
        preferencesManager.setGenderAsked(true)
        _uiState.value = _uiState.value.copy(showGenderDialog = false)
    }

    fun loadForYouFeed(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = !isRefresh && _uiState.value.models.isEmpty(),
                isRefreshing = isRefresh,
                error = null
            )

            try {
                val token = sessionManager.getToken()
                val response = winterApi.getForYouFeed(token = token, page = 1, limit = 20)

                val trendingMapped = (response.trending.ifEmpty { response.trending_profiles }).map { dto ->
                    CreatorModel(
                        id = dto.id.ifBlank { dto.user_id },
                        name = dto.name,
                        age = dto.age,
                        languages = dto.language?.replace(",", " ·") ?: "Hindi · English",
                        photoUrl = dto.getResolvedPhotoUrl().takeIf { it.isNotBlank() },
                        drawableResId = null,
                        status = if (dto.is_online) CreatorStatus.AVAILABLE else CreatorStatus.OFFLINE,
                        audioRate = "₹${dto.audio_rate} / minute",
                        videoRate = "₹${dto.video_rate} / minute"
                    )
                }

                val modelsMapped = response.models.map { dto ->
                    CreatorModel(
                        id = dto.id.ifBlank { dto.user_id },
                        name = dto.name,
                        age = dto.age,
                        languages = dto.language?.replace(",", " ·") ?: "Hindi · English",
                        photoUrl = dto.getResolvedPhotoUrl().takeIf { it.isNotBlank() },
                        drawableResId = null,
                        status = if (dto.is_online) CreatorStatus.AVAILABLE else CreatorStatus.OFFLINE,
                        audioRate = "₹${dto.audio_rate} / minute",
                        videoRate = "₹${dto.video_rate} / minute"
                    )
                }

                // If API returned items, update state; otherwise use fallback models
                if (modelsMapped.isNotEmpty() || trendingMapped.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        trendingProfiles = trendingMapped.ifEmpty { getDefaultTrending() },
                        offers = response.offers.ifEmpty { getDefaultOffers() },
                        models = modelsMapped.ifEmpty { getDefaultModels() },
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        trendingProfiles = getDefaultTrending(),
                        offers = getDefaultOffers(),
                        models = getDefaultModels(),
                        error = null
                    )
                }
            } catch (e: Exception) {
                Log.e("WinterModelsVM", "Error loading foryou feed: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    trendingProfiles = getDefaultTrending(),
                    offers = getDefaultOffers(),
                    models = getDefaultModels(),
                    error = e.localizedMessage
                )
            }
        }
    }

    private fun getDefaultTrending(): List<CreatorModel> {
        return listOf(
            CreatorModel(
                id = "user_model_maya",
                name = "Maya",
                age = 23,
                languages = "Hindi · English",
                drawableResId = R.drawable.trending_live_maya,
                status = CreatorStatus.AVAILABLE,
                audioRate = "₹15 / minute",
                videoRate = "₹30 / minute"
            ),
            CreatorModel(
                id = "user_model_ananya",
                name = "Ananya",
                age = 24,
                languages = "Hindi · English",
                drawableResId = R.drawable.creator_ananya,
                status = CreatorStatus.AVAILABLE,
                audioRate = "₹20 / minute",
                videoRate = "₹40 / minute"
            )
        )
    }

    private fun getDefaultOffers(): List<ForYouOfferDto> {
        return listOf(
            ForYouOfferDto(
                id = "offer_first_recharge",
                title = "First Recharge 50% Bonus",
                subtitle = "Get 50% extra coins on first recharge!",
                badge_text = "50% EXTRA",
                banner_image_url = "",
                action_type = "wallet",
                action_target = "wallet/recharge",
                discount_pct = 50
            ),
            ForYouOfferDto(
                id = "offer_welcome_deal",
                title = "Welcome Creator Special",
                subtitle = "Connect with top models at flat introductory rates.",
                badge_text = "SPECIAL DEAL",
                banner_image_url = "",
                action_type = "model",
                action_target = "foryou",
                discount_pct = 20
            )
        )
    }

    private fun getDefaultModels(): List<CreatorModel> {
        return listOf(
            CreatorModel(
                id = "user_model_ananya",
                name = "Ananya",
                age = 24,
                languages = "Hindi · English",
                drawableResId = R.drawable.creator_ananya,
                status = CreatorStatus.AVAILABLE,
                audioRate = "₹20 / minute",
                videoRate = "₹40 / minute"
            ),
            CreatorModel(
                id = "user_model_riya",
                name = "Riya",
                age = 27,
                languages = "English · Punjabi",
                drawableResId = R.drawable.creator_riya,
                status = CreatorStatus.AVAILABLE,
                audioRate = "₹25 / minute",
                videoRate = "₹50 / minute"
            ),
            CreatorModel(
                id = "user_model_meera",
                name = "Meera",
                age = 25,
                languages = "Hindi · Marathi",
                drawableResId = R.drawable.creator_meera,
                status = CreatorStatus.AVAILABLE,
                audioRate = "₹20 / minute",
                videoRate = "₹35 / minute"
            ),
            CreatorModel(
                id = "user_model_sana",
                name = "Sana",
                age = 29,
                languages = "English · Hindi",
                drawableResId = R.drawable.creator_sana,
                status = CreatorStatus.AVAILABLE,
                audioRate = "₹30 / minute",
                videoRate = "₹60 / minute"
            )
        )
    }
}
