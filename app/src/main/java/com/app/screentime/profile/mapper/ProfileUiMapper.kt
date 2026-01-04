package com.app.screentime.profile.mapper

import com.app.screentime.R
import com.app.screentime.profile.model.ProfileSettingsKey
import com.app.screentime.profile.model.ProfileSettingsUi
import com.app.screentime.profile.model.ProfileUiModel
import com.app.screentime.profile.model.ProfileUiProps
import javax.inject.Inject

/**
 * Mapper that converts use case results to UI Props
 * This is the only way the UI layer should receive data
 */
class ProfileUiMapper @Inject constructor() {

    /**
     * Map profile data and settings list to UI Props
     */
    fun toUiProps(
        profile: ProfileUiModel?,
        settingsList: List<ProfileSettingsUi>,
        isLoading: Boolean = false,
        isUpdating: Boolean = false,
        error: String? = null
    ): ProfileUiProps {
        return ProfileUiProps(
            settingsList = settingsList,
            profile = profile,
            isLoading = isLoading,
            isUpdating = isUpdating,
            error = error
        )
    }
}

