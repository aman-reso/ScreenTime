package com.app.screentime.profile.model

import androidx.annotation.StringRes

/**
 * UI model for profile data
 */
data class ProfileUiModel(
    val username: String? = null,
    val userId: String? = null
)

data class ProfileSettingUiData(val data: List<ProfileSettingsUi>? = null)

sealed interface ProfileSettingsUi {
    val text: Int
    val icon: Int?
    val iconUrl: String?
    val key: ProfileSettingsKey?

    data class ProfileData(
        @StringRes override val text: Int, override val icon: Int? = null,
        override val iconUrl: String? = null,
        override val key: ProfileSettingsKey? = null
    ) : ProfileSettingsUi


    data class Other(
        @StringRes override val text: Int,
        val url: String,
        override val icon: Int? = null,
        override val iconUrl: String? = null,
        override val key: ProfileSettingsKey? = null
    ) : ProfileSettingsUi

    data class SectionTitle(
        @StringRes override val text: Int,
        override val icon: Int? = null,
        override val iconUrl: String? = null,
        override val key: ProfileSettingsKey? = null
    ) : ProfileSettingsUi

    data class Restriction(
        @StringRes override val text: Int,
        override val icon: Int? = null,
        override val iconUrl: String? = null,
        override val key: ProfileSettingsKey? = null
    ) : ProfileSettingsUi
}

