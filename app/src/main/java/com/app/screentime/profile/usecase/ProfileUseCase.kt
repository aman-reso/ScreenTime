package com.app.screentime.profile.usecase

import com.app.screentime.profile.mapper.ProfileMapper
import com.app.screentime.profile.model.ProfileUiModel
import com.app.screentime.preferences.PreferencesManager
import javax.inject.Inject

/**
 * Use case for profile operations
 */
class ProfileUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val profileMapper: ProfileMapper
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
}

