package com.app.screentime.profile.usecase

import com.app.screentime.network.model.DeviceRegistrationResponse
import com.app.screentime.network.model.UsernameUpdateRequest
import com.app.screentime.profile.mapper.ProfileMapper
import com.app.screentime.profile.model.ProfileUiModel
import com.app.screentime.profile.repository.ProfileRepository
import com.app.screentime.preferences.PreferencesManager
import javax.inject.Inject

/**
 * Use case for profile operations
 */
class ProfileUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val profileMapper: ProfileMapper,
    private val profileRepository: ProfileRepository
) {
    /**
     * Get profile UI model
     * @return ProfileUiModel from preferences
     */
    fun getProfile(): ProfileUiModel {
        return profileMapper.toUiModel(preferencesManager)
    }

    /**
     * Update profile preferences
     * @param profile The profile UI model to save
     */
    fun updateProfile(profile: ProfileUiModel) {
        profileMapper.toPreferences(profile, preferencesManager)
    }

    /**
     * Update consent preferences
     * @param hasConsent Consent given
     * @param dataSharing Data sharing consent
     * @param analytics Analytics consent
     * @param marketing Marketing consent
     */
    fun updateConsent(
        hasConsent: Boolean,
        dataSharing: Boolean,
        analytics: Boolean,
        marketing: Boolean
    ) {

    }

    /**
     * Clear all profile data
     */
    fun clearProfile() {
        preferencesManager.clearDeviceRegistration()
    }

    /**
     * Check if device is registered
     */
    fun isDeviceRegistered(): Boolean {
        return preferencesManager.isDeviceRegistered()
    }

    /**
     * Get user ID
     */
    fun getUserId(): String? {
        return preferencesManager.getUserId()
    }

    /**
     * Get device ID (same as userId)
     */
    fun getDeviceId(): String? {
        return preferencesManager.getUserId()
    }

    /**
     * Update username
     * Updates both the API profile and local preferences
     * @return true if successful (success == true), false otherwise
     */
    suspend fun updateUsername(newUsername: String): Boolean {
        val currentUserInfo = preferencesManager.getUserInformation() ?: throw Exception("User information not found")

        // Update username via API using the dedicated endpoint
        val request = UsernameUpdateRequest(username = newUsername)
        val updateResult = profileRepository.updateUsername(request)
        val response = updateResult.getOrThrow() // Throw exception if update fails

        // Check if API response indicates success
        if (response.success == true && response.data != null) {
            // Update local preferences with new username from API response
            val updatedUserInfo = response.data.let { updatedData ->
                currentUserInfo.copy(
                    username = updatedData.username,
                    // Preserve other fields from current user info
                    userId = updatedData.userId,
                    createdAt = updatedData.createdAt,
                    totpSecret = updatedData.totpSecret ?: currentUserInfo.totpSecret,
                    totpEnabled = updatedData.totpEnabled,
                    totpPeriod = updatedData.totpPeriod
                )
            }
            preferencesManager.saveUserInformation(updatedUserInfo)
            return true
        } else {
            // API returned success=false
            throw Exception(response.message ?: "Failed to update username")
        }
    }
}

