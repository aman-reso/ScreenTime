package com.app.screentime.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ProfilePrompt
import com.app.screentime.core.model.User
import com.app.screentime.feature.profile.domain.usecase.FetchUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileDetailUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val userId: String = "",
    val displayName: String = "",
    val age: Int = 24,
    val gender: String = "",
    val bio: String = "",
    val aboutMe: String = "",
    val datingIntent: String = "",
    val heightCm: Int? = null,
    val heightFormatted: String = "",
    val city: String = "",
    val area: String = "",
    val locationFormatted: String = "Online",
    val job: String = "",
    val education: String = "",
    val interests: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val photos: List<String> = emptyList(),
    val prompts: List<ProfilePrompt> = emptyList(),
    val matchPercentage: Int = 90,
    val qualityScore: Double = 0.90,
    val error: String? = null
)

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val fetchUserProfileUseCase: FetchUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileDetailUiState())
    val uiState: StateFlow<ProfileDetailUiState> = _uiState.asStateFlow()

    fun loadProfile(targetUserId: String, initialName: String = "", initialAge: Int = 24) {
        val resolvedTargetId = targetUserId.trim()
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            userId = resolvedTargetId,
            displayName = initialName.ifBlank { _uiState.value.displayName },
            age = initialAge,
            error = null
        )

        viewModelScope.launch {
            fetchUserProfileUseCase(resolvedTargetId.takeIf { it.isNotBlank() && it != "me" })
                .onSuccess { user ->
                    val totalInches = user.heightCm?.let { kotlin.math.round(it / 2.54).toInt() }
                    val feet = totalInches?.let { it / 12 }
                    val inches = totalInches?.let { it % 12 }
                    val heightStr = when {
                        user.heightCm != null && feet != null && inches != null -> "${user.heightCm} cm ($feet'$inches\")"
                        !user.height.isNullOrBlank() -> user.height!!
                        else -> ""
                    }

                    val locStr = when {
                        !user.area.isNullOrBlank() && !user.city.isNullOrBlank() -> "${user.area}, ${user.city}"
                        !user.city.isNullOrBlank() -> user.city!!
                        !user.area.isNullOrBlank() -> user.area!!
                        else -> "Online"
                    }

                    val resolvedPhotos = user.photos.filter { it.isNotBlank() }.ifEmpty {
                        listOfNotNull(user.avatarUrl).filter { it.isNotBlank() }
                    }

                    val resolvedAge = user.age ?: user.dateOfBirth?.let { dob ->
                        try {
                            val parts = dob.split("-")
                            if (parts.size >= 3) {
                                val y = parts[0].toInt()
                                val m = parts[1].toInt()
                                val d = parts[2].toInt()
                                java.time.Period.between(
                                    java.time.LocalDate.of(y, m, d),
                                    java.time.LocalDate.now()
                                ).years
                            } else null
                        } catch (_: Exception) {
                            null
                        }
                    } ?: initialAge

                    val matchPct = (user.qualityScore * 100).toInt().coerceIn(75, 99)

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = user,
                        userId = user.id,
                        displayName = user.name,
                        age = resolvedAge,
                        gender = user.gender ?: "",
                        bio = user.bio ?: "",
                        aboutMe = user.aboutMe ?: user.bio ?: "",
                        datingIntent = user.datingIntent ?: user.relationType ?: "",
                        heightCm = user.heightCm,
                        heightFormatted = heightStr,
                        city = user.city ?: "",
                        area = user.area ?: "",
                        locationFormatted = locStr,
                        job = user.job ?: user.occupation ?: "",
                        education = user.education ?: "",
                        interests = user.interests,
                        languages = user.languages,
                        photos = resolvedPhotos,
                        prompts = user.prompts,
                        matchPercentage = if (matchPct > 0) matchPct else 90,
                        qualityScore = user.qualityScore,
                        error = null
                    )
                }
                .onFailure { err ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = err.localizedMessage ?: "Failed to load profile"
                    )
                }
        }
    }
}
