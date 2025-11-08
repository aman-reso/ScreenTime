package com.app.screentime.profile.model

/**
 * UI model for profile data
 */
data class ProfileUiModel(
    val username: String? = null,
    val userId: String? = null
)

data class ProfileSettingUiData(val data: List<ProfileSettingsUi>? = null)

sealed interface ProfileSettingsUi {
    val text: String
    val icon: Int?
    val iconUrl: String?
    val key: String?

    data class ProfileData(
        override val text: String, override val icon: Int? = null,
        override val iconUrl: String? = null,
        override val key: String? = null
    ) : ProfileSettingsUi

    data class AccountDetails(
        override val text: String,
        val profileUiModel: ProfileUiModel? = null,
        override val icon: Int? = null,
        override val iconUrl: String? = null,
        override val key: String? = null
    ) : ProfileSettingsUi

    data class Other(
        override val text: String,
        val url: String,
        override val icon: Int? = null,
        override val iconUrl: String? = null,
        override val key: String? = null
    ) : ProfileSettingsUi

    data class SectionTitle(
        override val text: String,
        override val icon: Int? = null,
        override val iconUrl: String? = null,
        override val key: String? = null
    ) : ProfileSettingsUi

    data class Restriction(
        override val text: String,
        override val icon: Int? = null,
        override val iconUrl: String? = null,
        override val key: String? = null
    ) : ProfileSettingsUi
}

