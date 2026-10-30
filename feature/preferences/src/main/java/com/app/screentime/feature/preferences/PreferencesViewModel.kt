package com.app.screentime.feature.preferences

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.faceverification.FaceVerificationManager
import com.app.screentime.core.faceverification.model.FaceVerificationResult
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.dto.ProfileUpdateRequest
import com.app.screentime.core.network.dto.UploadImagePart
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Period
import com.app.screentime.feature.preferences.domain.model.OnboardingCategory
import com.app.screentime.feature.preferences.domain.model.OnboardingSubStep
import javax.inject.Inject

data class PreferencesUiState(
    val category: OnboardingCategory = OnboardingCategory.ESSENTIAL_DETAILS,
    val currentStep: Int = 1,
    val totalSteps: Int = 3,
    val preferenceSubStep: Int = 1,
    val name: String = "",
    val nameError: String? = null,
    val gender: String = "",
    val genderError: String? = null,
    val birthDay: String = "",
    val birthMonth: String = "",
    val birthYear: String = "",
    val ageYears: String = "",
    val dobError: String? = null,
    val heightCm: String = "",
    val heightError: String? = null,
    val interestedIn: String = "",
    val relationshipIntent: String = "",
    val languages: List<String> = emptyList(),
    val profession: String = "",
    val education: String = "",
    val interests: List<String> = emptyList(),
    val visibility: String = "public",
    val income: String = "",
    val bio: String = "",
    val bioError: String? = null,
    val uploadedPhotos: List<String> = emptyList(),
    val isUploadingPhoto: Boolean = false,
    val photoError: String? = null,
    val livePhotoUri: String? = null,
    val isVerifyingFace: Boolean = false,
    val isFaceVerified: Boolean = false,
    val faceSimilarityScore: Float? = null,
    val faceVerificationError: String? = null,
    val currentCity: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isLocationEnabled: Boolean = false,
    val locationError: String? = null,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager,
    private val api: WinterApi,
    private val faceVerificationManager: FaceVerificationManager
) : ViewModel() {

    companion object {
        private const val DRAFT_PREFS = "preferences_onboarding_draft"
        private const val LIST_DELIMITER = "|||"
        private const val KEY_DRAFT_NAME = "draft_name"
        private const val KEY_DRAFT_GENDER = "draft_gender"
        private const val KEY_DRAFT_BIRTH_DAY = "draft_birth_day"
        private const val KEY_DRAFT_BIRTH_MONTH = "draft_birth_month"
        private const val KEY_DRAFT_BIRTH_YEAR = "draft_birth_year"
        private const val KEY_DRAFT_HEIGHT = "draft_height_cm"
        private const val KEY_DRAFT_INTERESTED_IN = "draft_interested_in"
        private const val KEY_DRAFT_INTENT = "draft_intent"
        private const val KEY_DRAFT_LANGUAGES = "draft_languages"
        private const val KEY_DRAFT_INCOME = "draft_income"
        private const val KEY_DRAFT_BIO = "draft_bio"
        private const val KEY_DRAFT_CURRENT_STEP = "draft_current_step"
        private const val KEY_DRAFT_SUB_STEP = "draft_sub_step"
        private const val KEY_DRAFT_PHOTOS = "draft_photos"
        private const val KEY_DRAFT_LIVE_PHOTO = "draft_live_photo"
        private const val KEY_DRAFT_CITY = "draft_city"
        private const val KEY_DRAFT_LATITUDE = "draft_latitude"
        private const val KEY_DRAFT_LONGITUDE = "draft_longitude"
    }

    private val draftPrefs by lazy {
        context.getSharedPreferences(DRAFT_PREFS, Context.MODE_PRIVATE)
    }

    private val _uiState = MutableStateFlow(loadInitialState())
    val uiState: StateFlow<PreferencesUiState> = _uiState.asStateFlow()

    init {
        val sessionName = (sessionManager.userName ?: preferencesManager.getUsername() ?: "")
            .takeUnless { it.matches(Regex("(?i)^User(\\s*\\d+)?$")) } ?: ""
        if (sessionName.isNotBlank() && _uiState.value.name.isBlank()) {
            val updated = _uiState.value.copy(name = sessionName)
            _uiState.value = updated
            saveDraft(updated)
        }
        val savedLivePhoto = _uiState.value.livePhotoUri
        if (!savedLivePhoto.isNullOrBlank()) {
            verifyLivePhoto(savedLivePhoto)
        }
    }

    private fun loadInitialState(): PreferencesUiState {
        val draft = context.getSharedPreferences(DRAFT_PREFS, Context.MODE_PRIVATE)
        val sessionName = (sessionManager.userName ?: preferencesManager.getUsername() ?: "")
            .takeUnless { it.matches(Regex("(?i)^User(\\s*\\d+)?$")) } ?: ""

        val savedName = draft.getString(KEY_DRAFT_NAME, null) ?: sessionName
        val savedGender = draft.getString(KEY_DRAFT_GENDER, null) ?: ""
        val savedBirthDay = draft.getString(KEY_DRAFT_BIRTH_DAY, null) ?: ""
        val savedBirthMonth = draft.getString(KEY_DRAFT_BIRTH_MONTH, null) ?: ""
        val savedBirthYear = draft.getString(KEY_DRAFT_BIRTH_YEAR, null) ?: ""
        val savedHeight = draft.getString(KEY_DRAFT_HEIGHT, null) ?: ""
        val savedInterestedIn = draft.getString(KEY_DRAFT_INTERESTED_IN, null) ?: ""
        val savedIntent = draft.getString(KEY_DRAFT_INTENT, null) ?: ""
        val savedLanguagesStr = draft.getString(KEY_DRAFT_LANGUAGES, null)
        val savedLanguages = if (!savedLanguagesStr.isNullOrBlank()) {
            savedLanguagesStr.split(LIST_DELIMITER).map { it.trim() }.filter { it.isNotEmpty() }
        } else {
            emptyList()
        }
        val savedIncome = draft.getString(KEY_DRAFT_INCOME, null) ?: ""
        val savedBio = draft.getString(KEY_DRAFT_BIO, null) ?: ""
        val savedSubStep = draft.getInt(KEY_DRAFT_SUB_STEP, 1).coerceIn(1, 6)
        val savedCurrentStep = draft.getInt(KEY_DRAFT_CURRENT_STEP, 1).coerceIn(1, 3)
        val savedPhotosStr = draft.getString(KEY_DRAFT_PHOTOS, null)
        val savedPhotos = if (!savedPhotosStr.isNullOrBlank()) {
            savedPhotosStr.split(LIST_DELIMITER).map { it.trim() }.filter { it.isNotEmpty() }
        } else {
            emptyList()
        }
        val savedLivePhoto = draft.getString(KEY_DRAFT_LIVE_PHOTO, null)
        val isLivePhotoFileValid = savedLivePhoto?.let { uriStr ->
            try {
                val path = if (uriStr.startsWith("file://")) uriStr.removePrefix("file://") else android.net.Uri.parse(uriStr).path
                if (path != null) {
                    val file = java.io.File(path)
                    file.exists() && file.length() > 0
                } else false
            } catch (_: Exception) {
                false
            }
        } ?: false

        val savedLat = draft.getString(KEY_DRAFT_LATITUDE, null)?.toDoubleOrNull() ?: preferencesManager.getUserLatitude()
        val savedLng = draft.getString(KEY_DRAFT_LONGITUDE, null)?.toDoubleOrNull() ?: preferencesManager.getUserLongitude()
        val savedCity = draft.getString(KEY_DRAFT_CITY, null) ?: preferencesManager.getUserCity() ?: ""
        val hasValidCoords = savedLat != null && savedLng != null && savedLat != 0.0 && savedLng != 0.0
        val isLocationTrulyEnabled = hasValidCoords && savedCity.isNotBlank() &&
                !savedCity.equals("Detecting location...", ignoreCase = true) &&
                !savedCity.equals("Location Off", ignoreCase = true) &&
                !savedCity.equals("Location detection failed", ignoreCase = true)

        return PreferencesUiState(
            currentStep = savedCurrentStep,
            totalSteps = 3,
            preferenceSubStep = savedSubStep,
            name = savedName,
            gender = savedGender,
            birthDay = savedBirthDay,
            birthMonth = savedBirthMonth,
            birthYear = savedBirthYear,
            heightCm = savedHeight,
            interestedIn = savedInterestedIn,
            relationshipIntent = savedIntent,
            languages = savedLanguages,
            income = savedIncome,
            bio = savedBio,
            uploadedPhotos = savedPhotos,
            livePhotoUri = if (isLivePhotoFileValid) savedLivePhoto else null,
            isFaceVerified = false,
            currentCity = if (isLocationTrulyEnabled) savedCity else "",
            latitude = if (isLocationTrulyEnabled) savedLat else null,
            longitude = if (isLocationTrulyEnabled) savedLng else null,
            isLocationEnabled = isLocationTrulyEnabled
        )
    }

    private fun saveDraft(state: PreferencesUiState = _uiState.value) {
        draftPrefs.edit()
            .putString(KEY_DRAFT_NAME, state.name)
            .putString(KEY_DRAFT_GENDER, state.gender)
            .putString(KEY_DRAFT_BIRTH_DAY, state.birthDay)
            .putString(KEY_DRAFT_BIRTH_MONTH, state.birthMonth)
            .putString(KEY_DRAFT_BIRTH_YEAR, state.birthYear)
            .putString(KEY_DRAFT_HEIGHT, state.heightCm)
            .putString(KEY_DRAFT_INTERESTED_IN, state.interestedIn)
            .putString(KEY_DRAFT_INTENT, state.relationshipIntent)
            .putString(KEY_DRAFT_LANGUAGES, state.languages.joinToString(LIST_DELIMITER))
            .putString(KEY_DRAFT_INCOME, state.income)
            .putString(KEY_DRAFT_BIO, state.bio)
            .putInt(KEY_DRAFT_SUB_STEP, state.preferenceSubStep)
            .putInt(KEY_DRAFT_CURRENT_STEP, state.currentStep)
            .putString(KEY_DRAFT_PHOTOS, state.uploadedPhotos.joinToString(LIST_DELIMITER))
            .putString(KEY_DRAFT_LIVE_PHOTO, state.livePhotoUri)
            .putString(KEY_DRAFT_CITY, state.currentCity)
            .putString(KEY_DRAFT_LATITUDE, state.latitude?.toString())
            .putString(KEY_DRAFT_LONGITUDE, state.longitude?.toString())
            .apply()
    }

    fun clearDraft() {
        draftPrefs.edit().clear().apply()
    }

    fun resetState() {
        draftPrefs.edit().clear().apply()
        _uiState.value = PreferencesUiState()
    }

    fun updateName(name: String) {
        val error = when {
            name.isBlank() -> "Name cannot be empty"
            name.trim().length < 2 -> "Name must be at least 2 characters"
            name.trim().length > 50 -> "Name cannot exceed 50 characters"
            else -> null
        }
        val newState = _uiState.value.copy(name = name, nameError = error)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateGender(gender: String) {
        val newState = _uiState.value.copy(gender = gender, genderError = null)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateAgeYears(age: String) {
        val ageNum = age.toIntOrNull()
        val error = when {
            age.isBlank() -> "Please select your age"
            ageNum == null -> "Please enter a valid number"
            ageNum < 18 || ageNum > 40 -> "Age must be between 18 and 40 years"
            else -> null
        }
        val calculatedYear = (2026 - (ageNum ?: 24)).toString()
        val newState = _uiState.value.copy(
            ageYears = age,
            birthYear = calculatedYear,
            dobError = error
        )
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateBirthDay(day: String) {
        val newState = _uiState.value.copy(birthDay = day, dobError = null)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateBirthMonth(month: String) {
        val newState = _uiState.value.copy(birthMonth = month, dobError = null)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateBirthYear(year: String) {
        val newState = _uiState.value.copy(birthYear = year, dobError = null)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateHeight(heightCm: String) {
        val heightNum = heightCm.toIntOrNull()
        val error = if (heightNum != null && (heightNum < 80 || heightNum > 250)) {
            "Please enter a valid height (80-250 cm)"
        } else null
        val newState = _uiState.value.copy(heightCm = heightCm, heightError = error)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun addPhoto(url: String) {
        val current = _uiState.value.uploadedPhotos
        if (current.size < 2) {
            val newState = _uiState.value.copy(uploadedPhotos = current + url)
            _uiState.value = newState
            saveDraft(newState)
        }
    }

    fun uploadPhotos(uris: List<Uri>, onComplete: ((List<String>) -> Unit)? = null) {
        if (uris.isEmpty()) return
        val token = sessionManager.getToken() ?: ""
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isUploadingPhoto = true, error = null, photoError = null)
            try {
                // Detect face using Google ML Kit first before uploading
                for (uri in uris) {
                    val faceDetected = faceVerificationManager.hasFace(uri)
                    if (!faceDetected) {
                        _uiState.value = _uiState.value.copy(
                            isUploadingPhoto = false,
                            photoError = "No face detected in photo. Please choose a photo where your face is clearly visible.",
                            error = "No face detected in photo. Please choose a photo where your face is clearly visible."
                        )
                        return@launch
                    }
                }

                val parts = uris.mapIndexedNotNull { index, uri ->
                    com.app.screentime.core.network.util.ImageCompressor.compressUriToPart(context, uri, index)
                }

                if (parts.isEmpty()) {
                    _uiState.value = _uiState.value.copy(isUploadingPhoto = false, error = "No valid image files found")
                    return@launch
                }

                val response = api.uploadImageParts(token, parts)
                val urls = response.getUploadedUrls()
                if (urls.isNotEmpty()) {
                    val updated = (_uiState.value.uploadedPhotos + urls).take(2)
                    val newState = _uiState.value.copy(
                        uploadedPhotos = updated,
                        isUploadingPhoto = false,
                        photoError = null,
                        error = null
                    )
                    _uiState.value = newState
                    saveDraft(newState)
                    onComplete?.invoke(urls)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isUploadingPhoto = false,
                        error = response.message.ifBlank { "Failed to upload image" }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUploadingPhoto = false,
                    error = e.localizedMessage ?: "Image upload failed"
                )
            }
        }
    }

    fun removePhoto(index: Int) {
        val current = _uiState.value.uploadedPhotos.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            val newState = _uiState.value.copy(uploadedPhotos = current)
            _uiState.value = newState
            saveDraft(newState)
        }
    }



    fun updateLocation(city: String?, lat: Double? = null, lng: Double? = null, isEnabled: Boolean) {
        val cleanCity = city?.trim()?.takeIf { it.isNotBlank() } ?: ""
        val newLat = lat ?: _uiState.value.latitude
        val newLng = lng ?: _uiState.value.longitude
        val hasValidCoords = newLat != null && newLng != null && (newLat != 0.0 || newLng != 0.0)
        val isCityValid = cleanCity.isNotBlank() &&
                !cleanCity.equals("Detecting location...", ignoreCase = true) &&
                !cleanCity.equals("Location Off", ignoreCase = true) &&
                !cleanCity.equals("Location detection failed", ignoreCase = true)
        val validEnabled = isEnabled && hasValidCoords && isCityValid

        val newState = _uiState.value.copy(
            currentCity = cleanCity,
            latitude = if (validEnabled) newLat else null,
            longitude = if (validEnabled) newLng else null,
            isLocationEnabled = validEnabled,
            locationError = null
        )
        if (validEnabled && newLat != null && newLng != null) {
            preferencesManager.setUserLocation(newLat, newLng)
            preferencesManager.setUserCity(cleanCity)
        }
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateInterestedIn(option: String) {
        val newState = _uiState.value.copy(interestedIn = option)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateRelationshipIntent(intent: String) {
        val newState = _uiState.value.copy(relationshipIntent = intent)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun toggleLanguage(language: String) {
        val current = _uiState.value.languages.toMutableList()
        if (current.contains(language)) {
            if (current.size > 1) {
                current.remove(language)
            }
        } else if (current.size < 5) {
            current.add(language)
        }
        val newState = _uiState.value.copy(languages = current)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateIncome(income: String) {
        val newState = _uiState.value.copy(income = income)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateProfession(profession: String) {
        val newState = _uiState.value.copy(profession = profession)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateEducation(education: String) {
        val newState = _uiState.value.copy(education = education)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun toggleInterest(interest: String) {
        val current = _uiState.value.interests.toMutableList()
        if (current.contains(interest)) {
            current.remove(interest)
        } else {
            current.add(interest)
        }
        val newState = _uiState.value.copy(interests = current)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateVisibility(visibility: String) {
        val newState = _uiState.value.copy(visibility = visibility)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun updateBio(bio: String) {
        val error = if (bio.length > 250) "Bio cannot exceed 250 characters" else null
        val newState = _uiState.value.copy(bio = bio, bioError = error)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun setPreferenceSubStep(step: Int) {
        val maxStep = if (_uiState.value.currentStep == 1) 6 else 6
        val newState = _uiState.value.copy(preferenceSubStep = step.coerceIn(1, maxStep))
        _uiState.value = newState
        saveDraft(newState)
    }

    private fun monthToInt(monthStr: String): Int {
        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val index = months.indexOfFirst { it.equals(monthStr, ignoreCase = true) }
        return if (index != -1) index + 1 else (monthStr.toIntOrNull() ?: 1).coerceIn(1, 12)
    }

    fun validateSubStep(step: Int): Boolean {
        val state = _uiState.value
        if (state.currentStep == 1) {
            when (step) {
                1 -> {
                    if (state.name.isBlank() || state.name.trim().length < 2) {
                        _uiState.value = state.copy(nameError = "Please enter your name (min 2 characters)")
                        return false
                    }
                }
                2 -> {
                    if (state.gender.isBlank()) {
                        _uiState.value = state.copy(genderError = "Please select your gender")
                        return false
                    }
                }
                3 -> {
                    val ageNum = state.ageYears.toIntOrNull()
                    if (state.ageYears.isBlank() || ageNum == null || ageNum < 18 || ageNum > 40) {
                        _uiState.value = state.copy(dobError = "Please select your age (must be between 18 and 40)")
                        return false
                    }
                }
                4 -> {
                    val heightNum = state.heightCm.toIntOrNull()
                    if (state.heightCm.isBlank() || heightNum == null || heightNum < 80 || heightNum > 250) {
                        _uiState.value = state.copy(heightError = "Please select your height")
                        return false
                    }
                }
                5 -> {
                    if (state.languages.isEmpty()) {
                        _uiState.value = state.copy(error = "Please select at least one language")
                        return false
                    }
                }
                6 -> {
                    val hasValidCoords = state.latitude != null && state.longitude != null && (state.latitude != 0.0 || state.longitude != 0.0)
                    val isCityValid = state.currentCity.isNotBlank() &&
                            !state.currentCity.equals("Detecting location...", ignoreCase = true) &&
                            !state.currentCity.equals("Location Off", ignoreCase = true) &&
                            !state.currentCity.equals("Location detection failed", ignoreCase = true)
                    if (!state.isLocationEnabled || !hasValidCoords || !isCityValid) {
                        _uiState.value = state.copy(locationError = "Location access is required to find matches nearby. Please enable location.")
                        return false
                    }
                }
            }
        }
        return true
    }

    fun nextPreferenceSubStep(): Boolean {
        val state = _uiState.value
        if (!validateSubStep(state.preferenceSubStep)) {
            return false
        }
        if (state.currentStep == 1) {
            if (state.preferenceSubStep < 6) {
                val newState = state.copy(preferenceSubStep = state.preferenceSubStep + 1, error = null)
                _uiState.value = newState
                saveDraft(newState)
            } else {
                // Category 2 is skipped: directly navigate to Step 2 (Photos Upload)
                val newState = state.copy(
                    currentStep = 2,
                    photoError = null,
                    error = null
                )
                _uiState.value = newState
                saveDraft(newState)
            }
        }
        return true
    }

    fun previousPreferenceSubStep(onBack: () -> Unit) {
        val state = _uiState.value
        if (state.currentStep == 1) {
            if (state.preferenceSubStep > 1) {
                val newState = state.copy(preferenceSubStep = state.preferenceSubStep - 1)
                _uiState.value = newState
                saveDraft(newState)
            } else {
                onBack()
            }
        } else if (state.currentStep == 2) {
            // From Step 2 (Photos) back to Step 1 (Essential Details - Location)
            val newState = state.copy(
                currentStep = 1,
                preferenceSubStep = 6
            )
            _uiState.value = newState
            saveDraft(newState)
        } else if (state.currentStep == 3) {
            // From Step 3 (Face Verification) back to Step 2 (Photos)
            val newState = state.copy(
                currentStep = 2
            )
            _uiState.value = newState
            saveDraft(newState)
        } else {
            onBack()
        }
    }

    fun nextStep(onComplete: () -> Unit = {}) {
        val state = _uiState.value
        when (state.currentStep) {
            1 -> {
                nextPreferenceSubStep()
            }
            2 -> {
                if (state.uploadedPhotos.isEmpty()) {
                    _uiState.value = state.copy(
                        photoError = "Please upload at least one photo to continue"
                    )
                    return
                }
                val newState = state.copy(
                    currentStep = 3,
                    photoError = null,
                    error = null
                )
                _uiState.value = newState
                saveDraft(newState)
            }
            3 -> {
                if (state.isVerifyingFace) {
                    return
                }
                if (!state.isFaceVerified || state.livePhotoUri.isNullOrBlank()) {
                    _uiState.value = state.copy(
                        faceVerificationError = "Please take a selfie to verify your face before continuing"
                    )
                    return
                }
                submitPreferences(onSuccess = onComplete)
            }
            else -> submitPreferences(onSuccess = onComplete)
        }
    }

    fun verifyLivePhoto(uriString: String) {
        val uri = Uri.parse(uriString)
        val intermediateState = _uiState.value.copy(
            livePhotoUri = uriString,
            isVerifyingFace = true,
            faceVerificationError = null,
            error = null
        )
        _uiState.value = intermediateState
        saveDraft(intermediateState)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val hasFace = faceVerificationManager.hasFace(uri)
                withContext(Dispatchers.Main) {
                    if (hasFace) {
                        val verifiedState = _uiState.value.copy(
                            isVerifyingFace = false,
                            isFaceVerified = true,
                            faceVerificationError = null,
                            faceSimilarityScore = null
                        )
                        _uiState.value = verifiedState
                        saveDraft(verifiedState)
                    } else {
                        val errorState = _uiState.value.copy(
                            isVerifyingFace = false,
                            isFaceVerified = false,
                            livePhotoUri = null,
                            faceVerificationError = "No face detected in selfie. Please make sure your face is clearly visible."
                        )
                        _uiState.value = errorState
                        saveDraft(errorState)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val errorState = _uiState.value.copy(
                        isVerifyingFace = false,
                        isFaceVerified = false,
                        livePhotoUri = null,
                        faceVerificationError = e.localizedMessage ?: "Selfie verification failed. Please try again."
                    )
                    _uiState.value = errorState
                    saveDraft(errorState)
                }
            }
        }
    }

    fun previousStep(onBack: () -> Unit) {
        previousPreferenceSubStep(onBack)
    }

    fun submitPreferences(onSuccess: () -> Unit) {
        val state = _uiState.value

        // Strict guard: location must be enabled with valid coordinates
        val hasValidCoords = state.latitude != null && state.longitude != null && (state.latitude != 0.0 || state.longitude != 0.0)
        val isCityValid = state.currentCity.isNotBlank() &&
                !state.currentCity.equals("Detecting location...", ignoreCase = true) &&
                !state.currentCity.equals("Location Off", ignoreCase = true) &&
                !state.currentCity.equals("Location detection failed", ignoreCase = true)
        if (!state.isLocationEnabled || !hasValidCoords || !isCityValid) {
            _uiState.value = state.copy(
                currentStep = 1,
                preferenceSubStep = 6,
                locationError = "Location access is required to find matches nearby. Please enable location."
            )
            return
        }

        // Strict guard: uploaded photos must not be empty
        if (state.uploadedPhotos.isEmpty()) {
            _uiState.value = state.copy(
                currentStep = 2,
                photoError = "Please upload at least one photo to continue"
            )
            return
        }

        // Strict guard: selfie face verification must be completed
        if (!state.isFaceVerified || state.livePhotoUri.isNullOrBlank()) {
            _uiState.value = state.copy(
                currentStep = 3,
                faceVerificationError = "Please take a selfie to verify your face before continuing"
            )
            return
        }

        val token = sessionManager.getToken() ?: ""
        val mInt = monthToInt(state.birthMonth)
        val formattedDob = "${state.birthYear}-${mInt.toString().padStart(2, '0')}-${state.birthDay.padStart(2, '0')}"
        val heightInt = state.heightCm.toIntOrNull()

        if (state.latitude != null && state.longitude != null) {
            preferencesManager.setUserLocation(state.latitude, state.longitude)
        }

        val request = ProfileUpdateRequest(
            name = state.name.trim(),
            display_name = state.name.trim(),
            gender = state.gender.lowercase(),
            dob = formattedDob,
            date_of_birth = formattedDob,
            height = heightInt,
            height_cm = heightInt,
            interested_in = state.interestedIn,
            relationship_intention = state.relationshipIntent,
            dating_intent = state.relationshipIntent,
            job = state.profession.ifBlank { null },
            occupation = state.profession.ifBlank { null },
            education = state.education.ifBlank { null },
            languages = state.languages,
            income = null,
            bio = state.bio.takeIf { it.isNotBlank() },
            photos = state.uploadedPhotos,
            avatar_url = state.uploadedPhotos.firstOrNull(),
            video_url = null,
            city = state.currentCity,
            latitude = state.latitude,
            longitude = state.longitude
        )

        viewModelScope.launch {
            _uiState.value = state.copy(isSubmitting = true, error = null)
            try {
                if (token.isNotBlank()) {
                    api.updateProfile(token, request)
                }
                sessionManager.userName = state.name.trim()
                preferencesManager.setUsername(state.name.trim())
                sessionManager.isOnboardingCompleted = true
                preferencesManager.setOnboardingCompleted(true)
                clearDraft()
                _uiState.value = _uiState.value.copy(isSubmitting = false, isSuccess = true)
                onSuccess()
            } catch (e: Exception) {
                sessionManager.userName = state.name.trim()
                preferencesManager.setUsername(state.name.trim())
                sessionManager.isOnboardingCompleted = true
                preferencesManager.setOnboardingCompleted(true)
                clearDraft()
                _uiState.value = _uiState.value.copy(isSubmitting = false, isSuccess = true)
                onSuccess()
            }
        }
    }
}
