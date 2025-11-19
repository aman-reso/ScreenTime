package com.app.screentime.profile.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.R
import com.app.screentime.profile.model.ProfileSettingUiData
import com.app.screentime.profile.model.ProfileSettingsUi
import com.app.screentime.profile.model.ProfileUiModel
import com.app.screentime.profile.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSettingUiData())
    val uiState: StateFlow<ProfileSettingUiData> = _uiState.asStateFlow()

    private val _internalState = MutableStateFlow(ProfileInternalState())

    init {
        loadProfile()
    }

    /**
     * Load profile data and create ProfileSettingUiData
     */
    fun loadProfile() {
        viewModelScope.launch {
            _internalState.value = _internalState.value.copy(isLoading = true, error = null)

            try {
                val profile = profileUseCase.getProfile()
                val profileSettingsList = createProfileSettingsList(profile)

                _uiState.value = ProfileSettingUiData(data = profileSettingsList)
                _internalState.value = _internalState.value.copy(
                    isLoading = false,
                    profile = profile,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = ProfileSettingUiData(data = null)
                _internalState.value = _internalState.value.copy(
                    isLoading = false,
                    error = context.getString(R.string.failed_to_load_profile, e.message ?: ""),
                    profile = null
                )
            }
        }
    }

    /**
     * Create list of ProfileSettingsUi items based on profile data
     */
    private fun createProfileSettingsList(profile: ProfileUiModel): List<ProfileSettingsUi> {
        return buildList {
            // Profile Section
            add(ProfileSettingsUi.ProfileData(context.getString(R.string.profile)))

            // App Restrictions Section (Productivity) - First with icon
            add(ProfileSettingsUi.SectionTitle(context.getString(R.string.app_restrictions)))
            add(
                ProfileSettingsUi.Other(
                    context.getString(R.string.block_app),
                    url = "",
                    key = "block_app"
                )
            )
            add(
                ProfileSettingsUi.Other(
                    context.getString(R.string.set_app_limit),
                    url = "",
                    key = "set_app_limit"
                )
            )
            add(
                ProfileSettingsUi.Other(
                    context.getString(R.string.set_app_launch_limit),
                    url = "",
                    key = "set_app_launch_limit"
                )
            )
            add(
                ProfileSettingsUi.Other(
                    context.getString(R.string.enable_vpn),
                    url = "",
                    key = "vpn_service"
                )
            )

            // Appearance Section - Second
            add(ProfileSettingsUi.SectionTitle(context.getString(R.string.appearance)))
            add(ProfileSettingsUi.Other(context.getString(R.string.theme), url = "", key = "theme"))
            add(ProfileSettingsUi.Other(context.getString(R.string.language), "", key = "language"))
            add(
                ProfileSettingsUi.Other(
                    context.getString(R.string.set_widget),
                    url = "",
                    key = "widget"
                )
            )

            // Other items
            add(
                ProfileSettingsUi.AccountDetails(
                    text = context.getString(R.string.one_time_password),
                    key = "totp"
                )
            )

            // About App Section
            add(ProfileSettingsUi.SectionTitle(context.getString(R.string.about_app)))
            add(
                ProfileSettingsUi.Other(
                    context.getString(R.string.privacy_policy),
                    "https://aman-reso.github.io/AppTime-HTML/privacy-policy.html",
                    key = "privacy_policy"
                )
            )
            add(
                ProfileSettingsUi.Other(
                    context.getString(R.string.terms_of_service),
                    "https://aman-reso.github.io/AppTime-HTML/terms-and-conditions.html",
                    key = "terms_of_service"
                )
            )
            add(
                ProfileSettingsUi.Other(
                    context.getString(R.string.help_support),
                    "",
                    key = "help_support"
                )
            )
        }
    }

    /**
     * Update profile
     */
    fun updateProfile(profile: ProfileUiModel) {
        viewModelScope.launch {
            _internalState.value = _internalState.value.copy(isUpdating = true, error = null)

            try {
                profileUseCase.updateProfile(profile)
                val profileSettingsList = createProfileSettingsList(profile)

                _uiState.value = ProfileSettingUiData(data = profileSettingsList)
                _internalState.value = _internalState.value.copy(
                    isUpdating = false,
                    profile = profile,
                    error = null,
                    isUpdated = true
                )
            } catch (e: Exception) {
                _internalState.value = _internalState.value.copy(
                    isUpdating = false,
                    error = context.getString(R.string.failed_to_update_profile, e.message ?: "")
                )
            }
        }
    }

    /**
     * Update consent preferences
     */
    fun updateConsent(
        hasConsent: Boolean,
        dataSharing: Boolean,
        analytics: Boolean,
        marketing: Boolean
    ) {
        viewModelScope.launch {
            _internalState.value = _internalState.value.copy(isUpdating = true, error = null)

            try {
                profileUseCase.updateConsent(
                    hasConsent = hasConsent,
                    dataSharing = dataSharing,
                    analytics = analytics,
                    marketing = marketing
                )

                // Reload profile to reflect changes
                val updatedProfile = profileUseCase.getProfile()
                val profileSettingsList = createProfileSettingsList(updatedProfile)

                _uiState.value = ProfileSettingUiData(data = profileSettingsList)
                _internalState.value = _internalState.value.copy(
                    isUpdating = false,
                    profile = updatedProfile,
                    error = null,
                    isUpdated = true
                )
            } catch (e: Exception) {
                _internalState.value = _internalState.value.copy(
                    isUpdating = false,
                    error = context.getString(R.string.failed_to_update_consent, e.message ?: "")
                )
            }
        }
    }

    /**
     * Clear profile data
     */
    fun clearProfile() {
        viewModelScope.launch {
            _internalState.value = _internalState.value.copy(isUpdating = true, error = null)

            try {
                profileUseCase.clearProfile()
                _uiState.value = ProfileSettingUiData(data = emptyList())
                _internalState.value = _internalState.value.copy(
                    isUpdating = false,
                    profile = null,
                    error = null,
                    isUpdated = true
                )
            } catch (e: Exception) {
                _internalState.value = _internalState.value.copy(
                    isUpdating = false,
                    error = context.getString(R.string.failed_to_clear_profile, e.message ?: "")
                )
            }
        }
    }

    /**
     * Get internal state (for loading/error states)
     */
    fun getInternalState(): ProfileInternalState = _internalState.value

    /**
     * Clear error
     */
    fun clearError() {
        _internalState.value = _internalState.value.copy(error = null)
    }

    /**
     * Reset update status
     */
    fun resetUpdateStatus() {
        _internalState.value = _internalState.value.copy(isUpdated = false)
    }

    /**
     * Update username
     * Returns true if successful, false otherwise
     */
    fun updateUsername(newUsername: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _internalState.value = _internalState.value.copy(isUpdating = true, error = null)

            try {
                val result = profileUseCase.updateUsername(newUsername)
                if (result) {
                    // Reload profile to reflect changes
                    val updatedProfile = profileUseCase.getProfile()
                    val profileSettingsList = createProfileSettingsList(updatedProfile)

                    _uiState.value = ProfileSettingUiData(data = profileSettingsList)
                    _internalState.value = _internalState.value.copy(
                        isUpdating = false,
                        profile = updatedProfile,
                        error = null,
                        isUpdated = true
                    )
                    onSuccess() // Call success callback to dismiss bottom sheet
                } else {
                    _internalState.value = _internalState.value.copy(
                        isUpdating = false,
                        error = context.getString(R.string.failed_to_update_username, "Unknown error")
                    )
                }
            } catch (e: Exception) {
                val errorMessage = if (e.message?.contains("already taken", ignoreCase = true) == true) {
                    context.getString(R.string.username_already_taken)
                } else {
                    context.getString(R.string.failed_to_update_username, e.message ?: "")
                }
                _internalState.value = _internalState.value.copy(
                    isUpdating = false,
                    error = errorMessage
                )
            }
        }
    }
}

/**
 * Internal UI State for Profile Screen (for loading/error handling)
 */
data class ProfileInternalState(
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val isUpdated: Boolean = false,
    val profile: ProfileUiModel? = null,
    val error: String? = null
)

