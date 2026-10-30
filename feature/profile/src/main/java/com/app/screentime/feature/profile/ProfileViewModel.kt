package com.app.screentime.feature.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.User
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.network.dto.ProfileUpdateRequest
import com.app.screentime.core.ui.security.BiometricAuthManager
import com.app.screentime.feature.profile.domain.usecase.FetchUserProfileUseCase
import com.app.screentime.feature.profile.domain.usecase.GetCurrentUserUseCase
import com.app.screentime.feature.profile.domain.usecase.LogoutUseCase
import com.app.screentime.feature.profile.domain.usecase.SubmitModelOnboardingUseCase
import com.app.screentime.feature.profile.domain.usecase.UpdateProfileVisibilityUseCase
import com.app.screentime.feature.profile.domain.usecase.UploadImagesUseCase
import com.app.screentime.feature.profile.domain.usecase.UpdatePreferencesUseCase
import android.net.Uri
import com.app.screentime.config.language.AppLanguageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import com.app.screentime.core.faceverification.FaceVerificationManager
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
    val country: String = "",
    val photoUrl: String = "",
    val isUploadingPhoto: Boolean = false,
    val videoUrl: String = "",
    val isUploadingVideo: Boolean = false,
    val selectedLanguage: String = "",
    val profileVisibility: String = "",
    val isUpdatingVisibility: Boolean = false,
    val isFingerprintLockEnabled: Boolean = false,
    val favoritesCount: Int = 0,
    val walletCoins: Int = 0,
    val voiceMinutesAvailable: Int = 0,
    val videoMinutesAvailable: Int = 0,
    val isOnboardingSubmitted: Boolean = false,
    val error: String? = null,
    val editFullName: String = "",
    val editFullNameError: String? = null,
    val editBio: String = "",
    val editBioError: String? = null,
    val editGender: String = "",
    val editDob: String = "",
    val editDobError: String? = null,
    val editAge: String = "",
    val editAgeError: String? = null,
    val isPhoneVerified: Boolean = false,
    val isDetectingLocation: Boolean = false,
    val editPhone: String = "",
    val editEmail: String = "",
    val editLanguages: String = "",
    val editHeight: String = "",
    val editHeightError: String? = null,
    val editIncome: String = "",
    val editIntent: String = "",
    val editInterestedIn: String = "",
    val editLocation: String = "",
    val editJob: String = "",
    val editCompany: String = "",
    val editEducation: String = "",
    val isSavingProfile: Boolean = false,
    val saveProfileSuccess: Boolean = false,
    val isDeletingAccount: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val appLanguageManager: AppLanguageManager,
    private val preferencesManager: com.app.screentime.core.network.preferences.PreferencesManager,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val fetchUserProfileUseCase: FetchUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val submitModelOnboardingUseCase: SubmitModelOnboardingUseCase,
    private val updateProfileVisibilityUseCase: UpdateProfileVisibilityUseCase,
    private val updatePreferencesUseCase: UpdatePreferencesUseCase,
    private val uploadImagesUseCase: UploadImagesUseCase,
    private val uploadVideoUseCase: com.app.screentime.feature.profile.domain.usecase.UploadVideoUseCase,
    private val deleteAccountUseCase: com.app.screentime.feature.profile.domain.usecase.DeleteAccountUseCase,
    private val faceVerificationManager: FaceVerificationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(
            isFingerprintLockEnabled = BiometricAuthManager.isFingerprintLockEnabled(context),
            selectedLanguage = appLanguageManager.getLanguageCode(context)
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUser()
    }

    fun loadUser() {
        val cachedUser = getCurrentUserUseCase()
        val initialCoins =
            if (cachedUser.walletBalance < 1000.0) 1000 else cachedUser.walletBalance.toInt()

        populateStateWithUser(cachedUser, initialCoins)

        viewModelScope.launch {
            fetchUserProfileUseCase().onSuccess { user ->
                val finalCoins =
                    if (user.walletBalance < 1000.0) 1000 else user.walletBalance.toInt()
                populateStateWithUser(user, finalCoins)
            }
        }
    }

    private fun populateStateWithUser(user: User, coins: Int) {
        var loc = listOfNotNull(user.area, user.city, user.country).filter { it.isNotBlank() }.joinToString(", ")
        if (loc.isBlank()) {
            loc = listOfNotNull(user.city, user.country).filter { it.isNotBlank() }.joinToString(", ")
        }
        if (loc.isBlank()) {
            loc = user.city.orEmpty().ifBlank { user.area.orEmpty() }
        }
        if (loc.isBlank()) {
            loc = preferencesManager.getUserCity().orEmpty()
        }

        val resolvedLat = user.latitude ?: preferencesManager.getUserLatitude()
        val resolvedLng = user.longitude ?: preferencesManager.getUserLongitude()
        if (loc.isBlank() && resolvedLat != null && resolvedLng != null) {
            viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                try {
                    val geocoder = android.location.Geocoder(context, java.util.Locale.getDefault())
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(resolvedLat, resolvedLng, 1)
                    val address = addresses?.firstOrNull()
                    val city = address?.locality ?: address?.subAdminArea ?: address?.adminArea
                    val stateOrCountry = address?.adminArea ?: address?.countryName
                    val resolved = when {
                        city != null && stateOrCountry != null && city != stateOrCountry -> "$city, $stateOrCountry"
                        city != null -> city
                        stateOrCountry != null -> stateOrCountry
                        else -> null
                    }
                    if (!resolved.isNullOrBlank()) {
                        preferencesManager.setUserCity(resolved)
                        _uiState.value = _uiState.value.copy(editLocation = resolved)
                    }
                } catch (e: Exception) {
                    // Ignore geocoder lookup failure
                }
            }
        }

        val resolvedAge = user.age?.takeIf { it > 0 }?.toString() ?: if (_uiState.value.age > 0) _uiState.value.age.toString() else "22"
        val isPhoneVerified = user.isPhoneVerified || (preferencesManager.isPhoneVerified() && user.phone.isNotBlank() && !user.phone.contains("@") && user.phone.filter { it.isDigit() }.length >= 6)

        val langs =
            if (user.languages.isNotEmpty()) user.languages.joinToString(", ") else "English"
        val visibilityStatus = if (user.isPrivate || user.visibility.equals(
                "private",
                ignoreCase = true
            )
        ) "Private" else "Public"

        _uiState.value = _uiState.value.copy(
            user = user,
            displayName = user.name,
            email = user.email ?: "",
            bio = user.bio ?: "",
            country = user.country ?: _uiState.value.country,
            photoUrl = user.avatarUrl ?: user.photos.firstOrNull() ?: _uiState.value.photoUrl,
            videoUrl = user.videoUrl ?: _uiState.value.videoUrl,
            role = UserRole.USER,
            isModelDetailsVerified = true,
            walletCoins = if (_uiState.value.walletCoins > 0) _uiState.value.walletCoins else coins,
            profileVisibility = visibilityStatus,
            // Prepopulate form editing state dynamically from real user model
            editFullName = user.name,
            editFullNameError = null,
            editBio = user.bio ?: "",
            editBioError = null,
            editGender = user.gender?.takeIf { it.isNotBlank() } ?: _uiState.value.editGender,
            editDob = user.dateOfBirth ?: user.dob ?: _uiState.value.editDob,
            editAge = resolvedAge,
            editAgeError = null,
            isPhoneVerified = isPhoneVerified,
            editPhone = user.phone,
            editEmail = user.email ?: "",
            editLanguages = langs,
            editHeight = user.height ?: _uiState.value.editHeight,
            editIntent = user.relationType ?: user.datingIntent ?: _uiState.value.editIntent,
            editLocation = loc.ifBlank { _uiState.value.editLocation },
            editJob = user.profession ?: user.job ?: user.occupation ?: _uiState.value.editJob,
            editCompany = user.company ?: _uiState.value.editCompany,
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

    fun onEditAgeChange(age: String) {
        val digits = age.filter { it.isDigit() }.take(3)
        val ageInt = digits.toIntOrNull()
        val error = when {
            digits.isBlank() -> "Age cannot be empty"
            ageInt == null || ageInt < 18 -> "Must be at least 18 years old"
            ageInt > 100 -> "Please enter a valid age"
            else -> null
        }
        _uiState.value = _uiState.value.copy(editAge = digits, editAgeError = error)
    }

    fun onEditPhoneChange(phone: String) {
        // Mobile number is linked to the authentication account and cannot be updated through profile
    }

    fun onEditEmailChange(email: String) {
        // Email is linked to the authentication account and cannot be updated through profile
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

    fun updateLocation(location: String, lat: Double? = null, lng: Double? = null) {
        val cleanLoc = location.trim()
        _uiState.value = _uiState.value.copy(
            editLocation = cleanLoc,
            isDetectingLocation = false
        )
        if (cleanLoc.isNotBlank()) {
            preferencesManager.setUserCity(cleanLoc)
        }
        if (lat != null && lng != null) {
            preferencesManager.setUserLocation(lat, lng)
        }
    }

    fun setDetectingLocation(isDetecting: Boolean) {
        _uiState.value = _uiState.value.copy(isDetectingLocation = isDetecting)
    }

    fun onEditJobChange(job: String) {
        _uiState.value = _uiState.value.copy(editJob = job)
    }

    fun onEditCompanyChange(company: String) {
        _uiState.value = _uiState.value.copy(editCompany = company)
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
            _uiState.value =
                _uiState.value.copy(editFullNameError = "Name must be at least 2 characters")
            onError("Please enter a valid full name")
            return
        }

        // Validate Age (we are using age instead of dob)
        val ageInt = current.editAge.toIntOrNull()
        if (ageInt == null || ageInt < 18 || ageInt > 100) {
            _uiState.value = _uiState.value.copy(editAgeError = "Please enter a valid age (18-100)")
            onError("Please enter a valid age (18-100)")
            return
        }

        // Validate Bio length
        if (current.editBio.length > 500) {
            _uiState.value = _uiState.value.copy(editBioError = "Bio cannot exceed 500 characters")
            onError("Bio cannot exceed 500 characters")
            return
        }

        val heightInt = current.editHeight.filter { it.isDigit() }.toIntOrNull()
        val langList =
            current.editLanguages.split(",", ";").map { it.trim() }.filter { it.isNotBlank() }

        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val calculatedBirthYear = currentYear - ageInt
        val calculatedDob = "$calculatedBirthYear-01-01"

        val request = ProfileUpdateRequest(
            name = current.editFullName.trim(),
            display_name = current.editFullName.trim(),
            bio = current.editBio.trim(),
            gender = current.editGender,
            age = ageInt,
            dob = current.editDob.ifBlank { calculatedDob },
            date_of_birth = current.editDob.ifBlank { calculatedDob },
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
            company = current.editCompany.ifBlank { null },
            education = current.editEducation.ifBlank { null }
        )

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSavingProfile = true, error = null)
            updatePreferencesUseCase(request).onSuccess {
                if (current.editLocation.isNotBlank()) {
                    preferencesManager.setUserCity(current.editLocation.trim())
                }
                _uiState.value = _uiState.value.copy(
                    isSavingProfile = false,
                    saveProfileSuccess = true,
                    displayName = current.editFullName.trim(),
                    bio = current.editBio.trim(),
                    age = ageInt
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


    fun setLanguage(language: String) {
        _uiState.value = _uiState.value.copy(selectedLanguage = language)
    }

    fun changeAppLanguage(languageCode: String) {
        appLanguageManager.changeLanguage(context, languageCode)
        _uiState.value = _uiState.value.copy(selectedLanguage = languageCode)
    }

    fun toggleFingerprintLock(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isFingerprintLockEnabled = enabled)
        BiometricAuthManager.setFingerprintLockEnabled(context, enabled)
    }

    fun logout() {
        BiometricAuthManager.setFingerprintLockEnabled(context, false)
        BiometricAuthManager.setUnlocked(false)
        _uiState.value = ProfileUiState(
            isFingerprintLockEnabled = false
        )
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    fun deleteAccount(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeletingAccount = true)
            deleteAccountUseCase().onSuccess {
                BiometricAuthManager.setFingerprintLockEnabled(context, false)
                BiometricAuthManager.setUnlocked(false)
                _uiState.value = ProfileUiState(isFingerprintLockEnabled = false)
                onSuccess()
            }.onFailure { err ->
                _uiState.value =
                    _uiState.value.copy(isDeletingAccount = false, error = err.localizedMessage)
                onError(err.localizedMessage ?: "Failed to delete account")
            }
        }
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
            _uiState.value = _uiState.value.copy(isUploadingPhoto = true, error = null)

            // Step 1: Detect face using Google ML Kit first before uploading
            for (uri in uris) {
                val hasFace = faceVerificationManager.hasFace(uri)
                if (!hasFace) {
                    _uiState.value = _uiState.value.copy(
                        isUploadingPhoto = false,
                        error = "No face detected in photo. Please choose a photo where your face is clearly visible."
                    )
                    return@launch
                }
            }

            // Step 2: Upload images if face is detected
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

    fun uploadProfileVideo(
        uri: Uri,
        onComplete: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploadingVideo = true, error = null)
            uploadVideoUseCase(uri)
                .onSuccess { url ->
                    _uiState.value = _uiState.value.copy(
                        videoUrl = url,
                        user = _uiState.value.user?.copy(videoUrl = url),
                        isUploadingVideo = false
                    )
                    updatePreferencesUseCase(ProfileUpdateRequest(video_url = url))
                    onComplete?.invoke(url)
                }
                .onFailure { err ->
                    _uiState.value = _uiState.value.copy(
                        isUploadingVideo = false,
                        error = err.localizedMessage ?: "Failed to upload video"
                    )
                }
        }
    }

    fun removeProfileVideo(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                videoUrl = "",
                user = _uiState.value.user?.copy(videoUrl = null)
            )
            updatePreferencesUseCase(ProfileUpdateRequest(video_url = ""))
            onComplete?.invoke()
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

    fun updateProfileVisibility(
        visibility: String,
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        val formatted = if (visibility.equals("private", ignoreCase = true)) "Private" else "Public"
        _uiState.value = _uiState.value.copy(
            profileVisibility = formatted,
            isUpdatingVisibility = true
        )
        viewModelScope.launch {
            updateProfileVisibilityUseCase(formatted.lowercase())
                .onSuccess { data ->
                    val resolved = if (data.is_private || data.visibility.equals(
                            "private",
                            ignoreCase = true
                        )
                    ) "Private" else "Public"
                    _uiState.value = _uiState.value.copy(
                        profileVisibility = resolved,
                        isUpdatingVisibility = false,
                        user = _uiState.value.user?.copy(
                            visibility = data.visibility,
                            isPrivate = data.is_private,
                            discoveryEnabled = data.discovery_enabled
                        )
                    )
                    onResult(true, "Profile visibility updated to $resolved")
                }
                .onFailure { error ->
                    val previous =
                        if (_uiState.value.user?.isPrivate == true || _uiState.value.user?.visibility.equals(
                                "private",
                                ignoreCase = true
                            )
                        ) "Private" else "Public"
                    _uiState.value = _uiState.value.copy(
                        profileVisibility = previous,
                        isUpdatingVisibility = false
                    )
                    onResult(false, error.message ?: "Failed to update profile visibility")
                }
        }
    }
}
