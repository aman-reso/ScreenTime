package com.app.screentime.profile.model

import androidx.annotation.StringRes

/**
 * UI Props for Profile Screen
 * Contains all the data and state needed to render the profile screen
 * This is the only data structure the UI layer should use
 */
data class ProfileUiProps(
    val settingsList: List<ProfileSettingsUi>,
    val profile: ProfileUiModel?,
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val error: String? = null
)

/**
 * Props for handling settings item clicks
 */
data class SettingsItemClickProps(
    val key: ProfileSettingsKey,
    val url: String
)

/**
 * Result of handling a settings item click
 */
sealed class SettingsItemClickResult {
    data class NavigateToScreen(val route: String) : SettingsItemClickResult()
    data class ShowDialog(val type: DialogType) : SettingsItemClickResult()
    data class OpenUrl(val url: String) : SettingsItemClickResult()
    object RequestWidgetSetup : SettingsItemClickResult()
    object ShareApp : SettingsItemClickResult()
    object None : SettingsItemClickResult()
}

enum class DialogType {
    THEME,
    LANGUAGE,
    HELP_SUPPORT,
    BLOCKED_SITES,
    EDIT_USERNAME,
    FEEDBACK
}

