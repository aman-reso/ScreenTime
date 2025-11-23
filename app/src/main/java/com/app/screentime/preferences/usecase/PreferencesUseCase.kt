package com.app.screentime.preferences.usecase

import android.content.Context
import com.app.screentime.preferences.PreferencesManager
import javax.inject.Inject

/**
 * Use case for managing preferences
 * This is a singleton but not a ViewModel since it doesn't follow ViewModel lifecycle
 */
class PreferencesUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    /**
     * Check if device is registered
     */
    fun isDeviceRegistered(): Boolean = preferencesManager.isDeviceRegistered()

    /**
     * Get stored user ID
     */
    fun getUserId(): String? = preferencesManager.getUserId()

    /**
     * Get stored device ID (same as userId)
     */
    fun getDeviceId(): String? = preferencesManager.getUserId()

    /**
     * Check if consent screen has been shown
     */
    fun isConsentScreenShown(): Boolean = preferencesManager.isConsentScreenShown()

    /**
     * Check if should show consent sheet
     */
    fun shouldShowConsentSheet(): Boolean = !preferencesManager.isConsentScreenShown()

    /**
     * Mark consent sheet as shown
     */
    fun markConsentSheetShown() {
        preferencesManager.setConsentScreenShown(true)
        preferencesManager.setFirstLaunchCompleted()
    }

    /**
     * Check if we should ask for usage stats permission (first time only)
     */
    fun shouldAskForUsageStatsPermission(): Boolean = !preferencesManager.isUsageStatsPermissionRequested()

    /**
     * Mark usage stats permission as requested
     */
    fun markUsageStatsPermissionRequested() {
        preferencesManager.setUsageStatsPermissionRequested(true)
    }
}

