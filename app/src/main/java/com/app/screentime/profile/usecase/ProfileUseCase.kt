package com.app.screentime.profile.usecase

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.app.screentime.BuildConfig
import com.app.screentime.config.R
import com.app.screentime.core.network.preferences.PreferencesManager
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
import com.app.screentime.widget.ScreenTimeXmlWidgetProvider
import dagger.hilt.android.qualifiers.ApplicationContext
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
    // private val blockedSitesUseCase: BlockedSitesUseCase, // Removed - BlockedSites feature disabled
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
                if (updatedData != null) {
                    currentUserInfo.copy(
                        username = updatedData.username,
                        // Preserve other fields from current user info
                        userId = updatedData.userId,
                        createdAt = updatedData.createdAt,
                        totpSecret = updatedData.totpSecret ?: currentUserInfo.totpSecret,
                        totpEnabled = updatedData.totpEnabled,
                        totpPeriod = updatedData.totpPeriod
                    )
                } else {
                    null
                }
            }
            updatedUserInfo?.let {
                preferencesManager.saveUserInformation(updatedUserInfo)
            }
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

            // App Restrictions Section - Removed
            // add(ProfileSettingsUi.SectionTitle(R.string.app_restrictions))

            // Features Section - Removed

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
                    R.string.share_app,
                    url = "",
                    key = ProfileSettingsKey.SHARE_APP
                )
            )
            add(
                ProfileSettingsUi.Other(
                    R.string.help_support,
                    url = "",
                    key = ProfileSettingsKey.HELP_SUPPORT
                )
            )
            add(
                ProfileSettingsUi.Other(
                    R.string.feedback,
                    url = "",
                    key = ProfileSettingsKey.FEEDBACK
                )
            )

            // Note: Device Admin removed - not suitable for consumer apps
            // Google Play rejects device admin for non-enterprise apps
            // Use Focus Mode and App Blocking instead
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

        return profileUiMapper.toUiProps(
            profile = profile,
            settingsList = settingsList,
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
        url: String
    ): SettingsItemClickResult {

        return when (key) {
            ProfileSettingsKey.THEME -> SettingsItemClickResult.ShowDialog(DialogType.THEME)
            ProfileSettingsKey.LANGUAGE -> SettingsItemClickResult.ShowDialog(DialogType.LANGUAGE)
            ProfileSettingsKey.WIDGET -> SettingsItemClickResult.RequestWidgetSetup
            ProfileSettingsKey.SHARE_APP -> SettingsItemClickResult.ShareApp
            ProfileSettingsKey.HELP_SUPPORT -> SettingsItemClickResult.ShowDialog(DialogType.HELP_SUPPORT)
            ProfileSettingsKey.FEEDBACK -> SettingsItemClickResult.ShowDialog(DialogType.FEEDBACK)
            ProfileSettingsKey.CONTROL_CENTER -> SettingsItemClickResult.NavigateToScreen("control_center")
            ProfileSettingsKey.MANAGE_LOCATION -> SettingsItemClickResult.NavigateToScreen("manage_location")
            ProfileSettingsKey.RECOVER_NOTIFICATION -> SettingsItemClickResult.NavigateToScreen("recover_notification")
            ProfileSettingsKey.APP_LOCK -> SettingsItemClickResult.NavigateToScreen("app_lock")
            ProfileSettingsKey.FILE_MANAGER -> SettingsItemClickResult.NavigateToScreen("file_manager")
            ProfileSettingsKey.WALLPAPER -> SettingsItemClickResult.NavigateToScreen("wallpaper") // Removed - Wallpaper feature disabled

            else -> {
                if (url.isNotEmpty()) {
                    SettingsItemClickResult.OpenUrl(url)
                } else {
                    SettingsItemClickResult.None
                }
            }
        }
    }

    suspend fun requestWidgetSetup() {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, ScreenTimeXmlWidgetProvider::class.java)
        
        // Check if widget pinning is supported (Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                // Request to pin the widget
                val successCallback = android.app.PendingIntent.getBroadcast(
                    context,
                    0,
                    android.content.Intent(),
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                )
                
                appWidgetManager.requestPinAppWidget(componentName, null, successCallback)
            } else {
                // Fallback: Open widget picker or show instructions
                openWidgetPicker(context)
            }
        } else {
            // For older Android versions, open widget picker
            openWidgetPicker(context)
        }
    }
    
    /**
     * Open widget picker for older Android versions or as fallback
     */
    private fun openWidgetPicker(context: Context) {
        try {
            val intent = Intent(android.appwidget.AppWidgetManager.ACTION_APPWIDGET_PICK)
            intent.putExtra(android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID, android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            // If widget picker is not available, try to open app info
            try {
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = android.net.Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (e2: Exception) {
                // Last resort: open main activity
                val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
}

