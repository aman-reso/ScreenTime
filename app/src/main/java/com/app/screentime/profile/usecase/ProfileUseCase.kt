package com.app.screentime.profile.usecase

import android.content.Context
import android.content.Intent
import android.net.VpnService
import com.app.screentime.R
import com.app.screentime.network.model.UsernameUpdateRequest
import com.app.screentime.profile.mapper.ProfileMapper
import com.app.screentime.profile.mapper.ProfileUiMapper
import com.app.screentime.profile.model.DialogType
import com.app.screentime.profile.model.ProfileSettingsKey
import com.app.screentime.profile.model.ProfileSettingsUi
import com.app.screentime.profile.model.ProfileUiModel
import com.app.screentime.profile.model.ProfileUiProps
import com.app.screentime.profile.model.SettingsItemClickResult
import com.app.screentime.profile.repository.ProfileRepository
import com.app.screentime.profile.usecase.BlockedSitesUseCase
import com.app.screentime.preferences.PreferencesManager
import com.app.screentime.service.ScreenTimeVpnService
import com.app.screentime.service.VpnPermissionManager
import com.app.screentime.widget.WidgetSetupHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Use case for profile operations
 * Contains all business logic for the profile screen
 */
class ProfileUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val profileMapper: ProfileMapper,
    private val profileUiMapper: ProfileUiMapper,
    private val profileRepository: ProfileRepository,
    private val blockedSitesUseCase: BlockedSitesUseCase,
    @ApplicationContext private val context: Context
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
     * Update username
     * Updates both the API profile and local preferences
     * @return true if successful (success == true), false otherwise
     */
    suspend fun updateUsername(newUsername: String): Boolean {
        val currentUserInfo =
            preferencesManager.getUserInformation() ?: throw Exception("User information not found")

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

    /**
     * Create list of ProfileSettingsUi items based on profile data
     * This contains the business logic for organizing profile settings
     */
    fun createProfileSettingsList(): List<ProfileSettingsUi> {
        return buildList {
            // Profile Section
            add(ProfileSettingsUi.ProfileData(R.string.profile))

            // App Restrictions Section
            add(ProfileSettingsUi.SectionTitle(R.string.app_restrictions))
            add(
                ProfileSettingsUi.Other(
                    R.string.block_app,
                    url = "",
                    key = ProfileSettingsKey.BLOCK_APP
                )
            )
            add(
                ProfileSettingsUi.Other(
                    R.string.set_app_limit,
                    url = "",
                    key = ProfileSettingsKey.SET_APP_LIMIT
                )
            )
            add(
                ProfileSettingsUi.Other(
                    R.string.set_app_launch_limit,
                    url = "",
                    key = ProfileSettingsKey.SET_APP_LAUNCH_LIMIT
                )
            )
            add(
                ProfileSettingsUi.Other(
                    R.string.enable_vpn,
                    url = "",
                    key = ProfileSettingsKey.VPN_SERVICE
                )
            )

            // Appearance Section
            add(ProfileSettingsUi.SectionTitle(R.string.appearance))
            add(ProfileSettingsUi.Other(R.string.theme, url = "", key = ProfileSettingsKey.THEME))
            add(
                ProfileSettingsUi.Other(
                    R.string.language,
                    url = "",
                    key = ProfileSettingsKey.LANGUAGE
                )
            )
            add(
                ProfileSettingsUi.Other(
                    R.string.set_widget,
                    url = "",
                    key = ProfileSettingsKey.WIDGET
                )
            )

            // About App Section
            add(ProfileSettingsUi.SectionTitle(R.string.about_app))
            add(
                ProfileSettingsUi.Other(
                    R.string.privacy_policy,
                    "https://aman-reso.github.io/AppTime-HTML/privacy-policy.html",
                    key = ProfileSettingsKey.PRIVACY_POLICY
                )
            )
            add(
                ProfileSettingsUi.Other(
                    R.string.terms_of_service,
                    "https://aman-reso.github.io/AppTime-HTML/terms-and-conditions.html",
                    key = ProfileSettingsKey.TERMS_OF_SERVICE
                )
            )
            add(
                ProfileSettingsUi.Other(
                    R.string.help_support,
                    url = "",
                    key = ProfileSettingsKey.HELP_SUPPORT
                )
            )
        }
    }

    /**
     * Check if VPN service is currently running
     */
    fun isVpnServiceRunning(): Boolean {
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        return activityManager.getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == ScreenTimeVpnService::class.java.name }
    }

    /**
     * Check if VPN is running (has permission and service is running)
     */
    fun isVpnRunning(): Boolean {
        val vpnPermissionManager = VpnPermissionManager(context)
        return vpnPermissionManager.hasVpnPermission() && isVpnServiceRunning()
    }

    /**
     * Get blocked sites count
     * Returns 0 if VPN is not running
     */
    suspend fun getBlockedSitesCount(): Int {
        return if (isVpnRunning()) {
            blockedSitesUseCase.getBlockedSitesCount()
        } else {
            0
        }
    }

    /**
     * Get Profile UI Props
     * This is the main method that returns all UI state needed for the profile screen
     */
    suspend fun getProfileUiProps(
        isLoading: Boolean = false,
        isUpdating: Boolean = false,
        error: String? = null
    ): ProfileUiProps {
        val profile = getProfile()
        val settingsList = createProfileSettingsList()
        val isVpnRunning = isVpnRunning()
        val blockedSitesCount = getBlockedSitesCount()

        return profileUiMapper.toUiProps(
            profile = profile,
            settingsList = settingsList,
            isVpnRunning = isVpnRunning,
            blockedSitesCount = blockedSitesCount,
            isLoading = isLoading,
            isUpdating = isUpdating,
            error = error
        )
    }

    /**
     * Handle settings item click
     * Returns a result that indicates what action should be taken
     */
    fun handleSettingsItemClick(
        key: ProfileSettingsKey,
        url: String,
        isVpnRunning: Boolean
    ): SettingsItemClickResult {
        val vpnPermissionManager = VpnPermissionManager(context)

        return when (key) {
            ProfileSettingsKey.THEME -> SettingsItemClickResult.ShowDialog(DialogType.THEME)
            ProfileSettingsKey.LANGUAGE -> SettingsItemClickResult.ShowDialog(DialogType.LANGUAGE)
            ProfileSettingsKey.WIDGET -> SettingsItemClickResult.RequestWidgetSetup
            ProfileSettingsKey.HELP_SUPPORT -> SettingsItemClickResult.ShowDialog(DialogType.HELP_SUPPORT)
            ProfileSettingsKey.BLOCK_APP,
            ProfileSettingsKey.SET_APP_LIMIT,
            ProfileSettingsKey.SET_APP_LAUNCH_LIMIT,
            ProfileSettingsKey.AD_BLOCKING -> SettingsItemClickResult.NavigateToScreen("app_blocking")

            ProfileSettingsKey.VPN_SERVICE -> {
                handleVpnServiceClick(isVpnRunning, vpnPermissionManager)
            }

            else -> {
                if (url.isNotEmpty()) {
                    SettingsItemClickResult.OpenUrl(url)
                } else {
                    SettingsItemClickResult.None
                }
            }
        }
    }

    /**
     * Handle VPN service click - returns the appropriate action
     */
    private fun handleVpnServiceClick(
        isVpnRunning: Boolean,
        vpnPermissionManager: VpnPermissionManager
    ): SettingsItemClickResult {
        return if (isVpnRunning) {
            // Stop VPN
            val stopIntent = Intent(context, ScreenTimeVpnService::class.java)
            stopIntent.putExtra("stop", true)
            SettingsItemClickResult.StopVpnService(stopIntent)
        } else {
            // Start VPN
            if (vpnPermissionManager.hasVpnPermission()) {
                val intent = Intent(context, ScreenTimeVpnService::class.java)
                SettingsItemClickResult.StartVpnService(intent)
            } else {
                // Request permission
                val intent = VpnService.prepare(context)
                if (intent != null) {
                    SettingsItemClickResult.RequestVpnPermission(intent)
                } else {
                    // Permission already granted, start service
                    val serviceIntent = Intent(context, ScreenTimeVpnService::class.java)
                    SettingsItemClickResult.StartVpnService(serviceIntent)
                }
            }
        }
    }

    /**
     * Request widget setup
     */
    suspend fun requestWidgetSetup() {
        WidgetSetupHelper.requestWidgetSetup(context)
    }
}

