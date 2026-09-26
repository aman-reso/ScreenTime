package com.app.screentime.feature.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.User
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.network.dto.ProfileUpdateRequest
import com.app.screentime.core.ui.security.BiometricAuthManager
import com.app.screentime.core.ui.theme.AppThemeManager
import com.app.screentime.feature.profile.domain.usecase.FetchUserProfileUseCase
import com.app.screentime.feature.profile.domain.usecase.GetCurrentUserUseCase
import com.app.screentime.feature.profile.domain.usecase.LogoutUseCase
import com.app.screentime.feature.profile.domain.usecase.SubmitModelOnboardingUseCase
import com.app.screentime.feature.profile.domain.usecase.UploadImagesUseCase
import com.app.screentime.feature.profile.domain.usecase.UpdatePreferencesUseCase
import com.app.screentime.feature.wallet.domain.usecase.GetWalletInfoUseCase
import android.net.Uri
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val displayName: String = "",
    val email: String = "",
    val bio: String = "",
    val role: UserRole = UserRole.USER,
    val isModelDetailsVerified: Boolean = true,
    val age: Int = 22,
    val country: String = "India",
    val photoUrl: String = "",
    val isUploadingPhoto: Boolean = false,
    val selectedTheme: String = AppThemeManager.currentThemeName.value,
    val selectedLanguage: String = "English",
    val isFingerprintLockEnabled: Boolean = false,
    val favoritesCount: Int = 0,
    val walletCoins: Int = 0,
    val voiceMinutesAvailable: Int = 0,
    val videoMinutesAvailable: Int = 0,
    val isOnboardingSubmitted: Boolean = false,
    val error: String? = null,

    // Form fields for Profile Editing (handled in ViewModel, no disconnected mutableState)
    val editFullName: String = "",
    val editFullNameError: String? = null,
    val editBio: String = "",
    val editBioError: String? = null,
    val editGender: String = "Male",
    val editDob: String = "",
    val editDobError: String? = null,
    val editLanguages: String = "English",
    val editHeight: String = "",
    val editHeightError: String? = null,
    val editIncome: String = "",
    val editIntent: String = "Long-term",
    val editInterestedIn: String = "Women",
    val editLocation: String = "",
    val editJob: String = "",
    val editEducation: String = "",
    val isSavingProfile: Boolean = false,
    val saveProfileSuccess: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val fetchUserProfileUseCase: FetchUserProfileUseCase,
    private val getWalletInfoUseCase: GetWalletInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val submitModelOnboardingUseCase: SubmitModelOnboardingUseCase,
    private val updatePreferencesUseCase: UpdatePreferencesUseCase,
    private val uploadImagesUseCase: UploadImagesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(
            isFingerprintLockEnabled = BiometricAuthManager.isFingerprintLockEnabled(context)
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUser()
        viewModelScope.launch {
            AppThemeManager.currentThemeName.collect { themeName ->
                _uiState.value = _uiState.value.copy(selectedTheme = themeName)
            }
        }
    }

    fun loadUser() {
        val cachedUser = getCurrentUserUseCase()
        val initialCoins =
            if (cachedUser.walletBalance < 1000.0) 1000 else cachedUser.walletBalance.toInt()

        populateStateWithUser(cachedUser, initialCoins)

        viewModelScope.launch {
            getWalletInfoUseCase().onSuccess { info ->
                _uiState.value = _uiState.value.copy(
                    walletCoins = if (info.credit_balance > 0) info.credit_balance else _uiState.value.walletCoins,
                    voiceMinutesAvailable = info.voice_minutes_available,
                    videoMinutesAvailable = info.video_minutes_available
                )
            }
            fetchUserProfileUseCase().onSuccess { user ->
                val finalCoins =
                    if (user.walletBalance < 1000.0) 1000 else user.walletBalance.toInt()
                populateStateWithUser(user, finalCoins)
            }
        }
    }

    private fun populateStateWithUser(user: User, coins: Int) {
        val loc = listOfNotNull(user.city, user.area).filter { it.isNotBlank() }.joinToString(", ")
        val langs = if (user.languages.isNotEmpty()) user.languages.joinToString(", ") else "English"

        _uiState.value = _uiState.value.copy(
            user = user,
            displayName = user.name,
            email = user.email ?: "",
            bio = user.bio ?: "",
            photoUrl = user.avatarUrl ?: user.photos.firstOrNull() ?: _uiState.value.photoUrl,
            role = UserRole.USER,
            isModelDetailsVerified = true,
            walletCoins = if (_uiState.value.walletCoins > 0) _uiState.value.walletCoins else coins,
            // Prepopulate form editing state dynamically from real user model
            editFullName = user.name,
            editFullNameError = null,
            editBio = user.bio ?: "",
            editBioError = null,
            editGender = user.gender?.takeIf { it.isNotBlank() } ?: _uiState.value.editGender,
            editDob = user.dateOfBirth ?: user.dob ?: _uiState.value.editDob,
            editLanguages = langs,
            editHeight = user.height ?: _uiState.value.editHeight,
            editIntent = user.relationType ?: user.datingIntent ?: _uiState.value.editIntent,
            editLocation = loc.ifBlank { _uiState.value.editLocation },
            editJob = user.job ?: user.occupation ?: _uiState.value.editJob,
            editEducation = user.education ?: _uiState.value.editEducation
        )
    }

    // ── Form Editing & Validation Handlers ────────────────────────────────────

    fun onEditFullNameChange(name: String) {
        val error = when {
            name.isBlank() -> "Full name cannot be empty"
            name.trim().length < 2 -> "Name must be at least 2 characters"
            name.trim().length > 50 -> "Name cannot exceed 50 characters"
            else -> null
        }
        _uiState.value = _uiState.value.copy(editFullName = name, editFullNameError = error)
    }

    fun onEditBioChange(bio: String) {
        val error = if (bio.length > 500) "Bio cannot exceed 500 characters" else null
        _uiState.value = _uiState.value.copy(editBio = bio, editBioError = error)
    }

    fun onEditGenderChange(gender: String) {
        _uiState.value = _uiState.value.copy(editGender = gender)
    }

    fun onEditDobChange(dob: String) {
        val error = if (dob.isBlank()) "Date of birth cannot be empty" else null
        _uiState.value = _uiState.value.copy(editDob = dob, editDobError = error)
    }

    fun onEditLanguagesChange(languages: String) {
        _uiState.value = _uiState.value.copy(editLanguages = languages)
    }

    fun onEditHeightChange(height: String) {
        val digitsOnly = height.filter { it.isDigit() }.toIntOrNull()
        val error = if (digitsOnly != null && (digitsOnly < 80 || digitsOnly > 250)) {
            "Please enter a valid height (80-250 cm)"
        } else null
        _uiState.value = _uiState.value.copy(editHeight = height, editHeightError = error)
    }

    fun onEditIncomeChange(income: String) {
        _uiState.value = _uiState.value.copy(editIncome = income)
    }

    fun onEditIntentChange(intent: String) {
        _uiState.value = _uiState.value.copy(editIntent = intent)
    }

    fun onEditInterestedInChange(interestedIn: String) {
        _uiState.value = _uiState.value.copy(editInterestedIn = interestedIn)
    }

    fun onEditLocationChange(location: String) {
        _uiState.value = _uiState.value.copy(editLocation = location)
    }

    fun onEditJobChange(job: String) {
        _uiState.value = _uiState.value.copy(editJob = job)
    }

    fun onEditEducationChange(education: String) {
        _uiState.value = _uiState.value.copy(editEducation = education)
    }

    /**
     * Validates and submits all edited profile changes to the backend.
     */
    fun validateAndSaveProfile(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val current = _uiState.value

        // Validate Full Name
        if (current.editFullName.isBlank() || current.editFullName.trim().length < 2) {
            _uiState.value = _uiState.value.copy(editFullNameError = "Name must be at least 2 characters")
            onError("Please enter a valid full name")
            return
        }

        // Validate Bio length
        if (current.editBio.length > 500) {
            _uiState.value = _uiState.value.copy(editBioError = "Bio cannot exceed 500 characters")
            onError("Bio cannot exceed 500 characters")
            return
        }

        val heightInt = current.editHeight.filter { it.isDigit() }.toIntOrNull()
        val langList = current.editLanguages.split(",", ";").map { it.trim() }.filter { it.isNotBlank() }

        val request = ProfileUpdateRequest(
            name = current.editFullName.trim(),
            display_name = current.editFullName.trim(),
            bio = current.editBio.trim(),
            gender = current.editGender,
            dob = current.editDob.ifBlank { null },
            date_of_birth = current.editDob.ifBlank { null },
            height = heightInt,
            height_cm = heightInt,
            languages = langList.ifEmpty { null },
            income = current.editIncome.ifBlank { null },
            relationship_intention = current.editIntent,
            dating_intent = current.editIntent,
            relation_type = current.editIntent,
            interested_in = current.editInterestedIn,
            city = current.editLocation.substringBefore(",").trim().ifBlank { null },
            area = current.editLocation.substringAfter(",", "").trim().ifBlank { null },
            job = current.editJob.ifBlank { null },
            occupation = current.editJob.ifBlank { null },
            education = current.editEducation.ifBlank { null }
        )

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSavingProfile = true, error = null)
            updatePreferencesUseCase(request).onSuccess {
                _uiState.value = _uiState.value.copy(
                    isSavingProfile = false,
                    saveProfileSuccess = true,
                    displayName = current.editFullName.trim(),
                    bio = current.editBio.trim()
                )
                fetchUserProfile()
                onSuccess()
            }.onFailure { err ->
                val errorMsg = err.localizedMessage ?: "Failed to save profile changes"
                _uiState.value = _uiState.value.copy(
                    isSavingProfile = false,
                    error = errorMsg
                )
                onError(errorMsg)
            }
        }
    }

    fun updateProfile(name: String, email: String, bio: String) {
        _uiState.value = _uiState.value.copy(
            displayName = name.ifBlank { _uiState.value.displayName },
            email = email.ifBlank { _uiState.value.email },
            bio = bio,
            user = _uiState.value.user?.copy(
                name = name.ifBlank { _uiState.value.displayName },
                email = email.ifBlank { _uiState.value.email },
                bio = bio
            )
        )
    }

    fun submitModelVerificationDetails(name: String, age: Int, country: String, photoUrl: String?) {
        _uiState.value = _uiState.value.copy(
            displayName = name.ifBlank { _uiState.value.displayName },
            age = age,
            country = country,
            photoUrl = photoUrl.orEmpty(),
            isModelDetailsVerified = true,
            user = _uiState.value.user?.copy(
                name = name.ifBlank { _uiState.value.displayName }
            )
        )
        viewModelScope.launch {
            submitModelOnboardingUseCase("Country: $country, Age: $age", 15.0, 5.0)
        }
    }

    fun setTheme(theme: String) {
        _uiState.value = _uiState.value.copy(selectedTheme = theme)
        AppThemeManager.setTheme(theme)
    }

    fun setLanguage(language: String) {
        _uiState.value = _uiState.value.copy(selectedLanguage = language)
    }

    fun toggleFingerprintLock(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isFingerprintLockEnabled = enabled)
        BiometricAuthManager.setFingerprintLockEnabled(context, enabled)
    }

    fun logout() {
        _uiState.value = ProfileUiState(
            isFingerprintLockEnabled = BiometricAuthManager.isFingerprintLockEnabled(context)
        )
        logoutUseCase()
    }

    fun submitOnboarding(bio: String, voiceRate: Double, chatRate: Double) {
        viewModelScope.launch {
            submitModelOnboardingUseCase(bio, voiceRate, chatRate).onSuccess {
                _uiState.value =
                    _uiState.value.copy(isOnboardingSubmitted = true, isModelDetailsVerified = true)
            }
        }
    }

    fun fetchUserProfile() {
        loadUser()
    }

    fun uploadProfilePhotos(
        uris: List<Uri>,
        isAvatar: Boolean = true,
        onComplete: ((List<String>) -> Unit)? = null
    ) {
        if (uris.isEmpty()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploadingPhoto = true)
            uploadImagesUseCase(uris)
                .onSuccess { urls ->
                    val firstUrl = urls.firstOrNull()
                    if (isAvatar && !firstUrl.isNullOrBlank()) {
                        _uiState.value = _uiState.value.copy(
                            photoUrl = firstUrl,
                            user = _uiState.value.user?.copy(avatarUrl = firstUrl),
                            isUploadingPhoto = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(isUploadingPhoto = false)
                    }
                    onComplete?.invoke(urls)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isUploadingPhoto = false,
                        error = error.message
                    )
                }
        }
    }

    fun updatePreferences(request: ProfileUpdateRequest) {
        viewModelScope.launch {
            updatePreferencesUseCase(request).onSuccess {
                fetchUserProfile()
            }.onFailure {
                _uiState.value = _uiState.value.copy(error = it.message)
            }
        }
    }
}
