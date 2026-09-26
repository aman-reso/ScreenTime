package com.app.screentime.feature.preferences

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import javax.inject.Inject

data class PreferencesUiState(
    val currentStep: Int = 1,
    val totalSteps: Int = 3,
    val preferenceSubStep: Int = 1,
    val name: String = "",
    val nameError: String? = null,
    val gender: String = "Male",
    val genderError: String? = null,
    val birthDay: String = "15",
    val birthMonth: String = "Jan",
    val birthYear: String = "1998",
    val dobError: String? = null,
    val heightCm: String = "175",
    val heightError: String? = null,
    val interestedIn: String = "Women",
    val relationshipIntent: String = "Long-term partner",
    val languages: List<String> = listOf("English", "Hindi"),
    val income: String = "$50,000 – $100,000",
    val bio: String = "",
    val bioError: String? = null,
    val uploadedPhotos: List<String> = emptyList(),
    val isUploadingPhoto: Boolean = false,
    val currentCity: String = "Detecting location...",
    val isLocationEnabled: Boolean = false,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager,
    private val api: WinterApi
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
    }

    private fun loadInitialState(): PreferencesUiState {
        val draft = context.getSharedPreferences(DRAFT_PREFS, Context.MODE_PRIVATE)
        val sessionName = (sessionManager.userName ?: preferencesManager.getUsername() ?: "")
            .takeUnless { it.matches(Regex("(?i)^User(\\s*\\d+)?$")) } ?: ""

        val savedName = draft.getString(KEY_DRAFT_NAME, null) ?: sessionName
        val savedGender = draft.getString(KEY_DRAFT_GENDER, null) ?: "Male"
        val savedBirthDay = draft.getString(KEY_DRAFT_BIRTH_DAY, null) ?: "15"
        val savedBirthMonth = draft.getString(KEY_DRAFT_BIRTH_MONTH, null) ?: "Jan"
        val savedBirthYear = draft.getString(KEY_DRAFT_BIRTH_YEAR, null) ?: "1998"
        val savedHeight = draft.getString(KEY_DRAFT_HEIGHT, null) ?: "175"
        val savedInterestedIn = draft.getString(KEY_DRAFT_INTERESTED_IN, null) ?: "Women"
        val savedIntent = draft.getString(KEY_DRAFT_INTENT, null) ?: "Long-term partner"
        val savedLanguagesStr = draft.getString(KEY_DRAFT_LANGUAGES, null)
        val savedLanguages = if (!savedLanguagesStr.isNullOrBlank()) {
            savedLanguagesStr.split(LIST_DELIMITER).map { it.trim() }.filter { it.isNotEmpty() }
        } else {
            listOf("English", "Hindi")
        }
        val savedIncome = draft.getString(KEY_DRAFT_INCOME, null) ?: "$50,000 – $100,000"
        val savedBio = draft.getString(KEY_DRAFT_BIO, null) ?: ""
        val savedSubStep = draft.getInt(KEY_DRAFT_SUB_STEP, 1).coerceIn(1, 9)
        val savedCurrentStep = draft.getInt(KEY_DRAFT_CURRENT_STEP, 1).coerceIn(1, 3)
        val savedPhotosStr = draft.getString(KEY_DRAFT_PHOTOS, null)
        val savedPhotos = if (!savedPhotosStr.isNullOrBlank()) {
            savedPhotosStr.split(LIST_DELIMITER).map { it.trim() }.filter { it.isNotEmpty() }
        } else {
            emptyList()
        }

        return PreferencesUiState(
            currentStep = savedCurrentStep,
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
            uploadedPhotos = savedPhotos
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
            .apply()
    }

    fun clearDraft() {
        draftPrefs.edit().clear().apply()
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
        if (current.size < 5) {
            val newState = _uiState.value.copy(uploadedPhotos = current + url)
            _uiState.value = newState
            saveDraft(newState)
        }
    }

    fun uploadPhotos(uris: List<Uri>, onComplete: ((List<String>) -> Unit)? = null) {
        if (uris.isEmpty()) return
        val token = sessionManager.getToken() ?: ""
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isUploadingPhoto = true, error = null)
            try {
                val parts = uris.mapIndexedNotNull { index, uri ->
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        val bytes = stream.readBytes()
                        val detectedMime = context.contentResolver.getType(uri)?.lowercase()
                        val mimeType = when {
                            detectedMime?.contains("webp") == true -> "image/webp"
                            detectedMime?.contains("png") == true -> "image/png"
                            detectedMime?.contains("gif") == true -> "image/gif"
                            detectedMime?.contains("heic") == true || detectedMime?.contains("heif") == true -> "image/heic"
                            bytes.size >= 12 && bytes[0] == 0x52.toByte() && bytes[1] == 0x49.toByte() &&
                                    bytes[2] == 0x46.toByte() && bytes[3] == 0x46.toByte() &&
                                    bytes[8] == 0x57.toByte() && bytes[9] == 0x45.toByte() &&
                                    bytes[10] == 0x42.toByte() && bytes[11] == 0x50.toByte() -> "image/webp"
                            bytes.size >= 8 && bytes[0] == 0x89.toByte() && bytes[1] == 0x50.toByte() &&
                                    bytes[2] == 0x4E.toByte() && bytes[3] == 0x47.toByte() -> "image/png"
                            else -> "image/jpeg"
                        }
                        val extension = when (mimeType) {
                            "image/webp" -> "webp"
                            "image/png" -> "png"
                            "image/gif" -> "gif"
                            "image/heic" -> "heic"
                            else -> "jpg"
                        }
                        val fileName = "photo_${System.currentTimeMillis()}_$index.$extension"
                        UploadImagePart(
                            filename = fileName,
                            bytes = bytes,
                            mimeType = mimeType
                        )
                    }
                }

                if (parts.isEmpty()) {
                    _uiState.value = _uiState.value.copy(isUploadingPhoto = false, error = "No valid image files found")
                    return@launch
                }

                val response = api.uploadImageParts(token, parts)
                val urls = response.getUploadedUrls()
                if (urls.isNotEmpty()) {
                    val updated = (_uiState.value.uploadedPhotos + urls).take(5)
                    val newState = _uiState.value.copy(
                        uploadedPhotos = updated,
                        isUploadingPhoto = false,
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

    fun updateLocation(city: String, isEnabled: Boolean) {
        _uiState.value = _uiState.value.copy(currentCity = city, isLocationEnabled = isEnabled)
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

    fun updateBio(bio: String) {
        val error = if (bio.length > 250) "Bio cannot exceed 250 characters" else null
        val newState = _uiState.value.copy(bio = bio, bioError = error)
        _uiState.value = newState
        saveDraft(newState)
    }

    fun setPreferenceSubStep(step: Int) {
        val newState = _uiState.value.copy(preferenceSubStep = step.coerceIn(1, 9))
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
                val day = state.birthDay.toIntOrNull() ?: 1
                val month = monthToInt(state.birthMonth)
                val year = state.birthYear.toIntOrNull() ?: 1998
                val age = try {
                    val birthDate = LocalDate.of(year, month, day)
                    Period.between(birthDate, LocalDate.now()).years
                } catch (_: Exception) {
                    -1
                }
                if (age < 18) {
                    _uiState.value = state.copy(dobError = "You must be at least 18 years old to join")
                    return false
                }
            }
            4 -> {
                val heightNum = state.heightCm.toIntOrNull()
                if (heightNum == null || heightNum < 80 || heightNum > 250) {
                    _uiState.value = state.copy(heightError = "Please enter a valid height (80-250 cm)")
                    return false
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
        val newState = if (state.preferenceSubStep < 9) {
            state.copy(preferenceSubStep = state.preferenceSubStep + 1)
        } else {
            state.copy(currentStep = 2)
        }
        _uiState.value = newState
        saveDraft(newState)
        return newState.currentStep == 2
    }

    fun previousPreferenceSubStep(onBack: () -> Unit) {
        val state = _uiState.value
        if (state.preferenceSubStep > 1) {
            val newState = state.copy(preferenceSubStep = state.preferenceSubStep - 1)
            _uiState.value = newState
            saveDraft(newState)
        } else {
            onBack()
        }
    }

    fun validateStepOne(): Boolean {
        return validateSubStep(1) && validateSubStep(2) && validateSubStep(3) && validateSubStep(4)
    }

    fun nextStep(onComplete: () -> Unit = {}) {
        val state = _uiState.value
        when (state.currentStep) {
            1 -> {
                nextPreferenceSubStep()
            }
            2 -> {
                if (state.uploadedPhotos.isEmpty()) {
                    _uiState.value = state.copy(error = "Please upload at least one photo")
                } else {
                    val newState = state.copy(currentStep = 3, error = null)
                    _uiState.value = newState
                    saveDraft(newState)
                }
            }
            3 -> {
                submitPreferences(onSuccess = onComplete)
            }
        }
    }

    fun previousStep(onBack: () -> Unit) {
        val state = _uiState.value
        when (state.currentStep) {
            1 -> previousPreferenceSubStep(onBack)
            2 -> {
                val newState = state.copy(currentStep = 1, preferenceSubStep = 9)
                _uiState.value = newState
                saveDraft(newState)
            }
            3 -> {
                val newState = state.copy(currentStep = 2)
                _uiState.value = newState
                saveDraft(newState)
            }
            else -> onBack()
        }
    }

    fun submitPreferences(onSuccess: () -> Unit) {
        val state = _uiState.value
        val token = sessionManager.getToken() ?: ""
        val mInt = monthToInt(state.birthMonth)
        val formattedDob = "${state.birthYear}-${mInt.toString().padStart(2, '0')}-${state.birthDay.padStart(2, '0')}"
        val heightInt = state.heightCm.toIntOrNull()

        val request = ProfileUpdateRequest(
            name = state.name.trim(),
            display_name = state.name.trim(),
            gender = state.gender,
            dob = formattedDob,
            date_of_birth = formattedDob,
            height = heightInt,
            height_cm = heightInt,
            interested_in = state.interestedIn,
            relationship_intention = state.relationshipIntent,
            dating_intent = state.relationshipIntent,
            languages = state.languages,
            income = state.income,
            bio = state.bio.takeIf { it.isNotBlank() },
            photos = state.uploadedPhotos,
            avatar_url = state.uploadedPhotos.firstOrNull(),
            city = state.currentCity.takeIf { it != "Location Off" && it != "Detecting location..." }
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
