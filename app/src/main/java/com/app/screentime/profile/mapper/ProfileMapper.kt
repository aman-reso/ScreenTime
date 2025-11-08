package com.app.screentime.profile.mapper

import com.app.screentime.profile.model.ProfileUiModel
import com.app.screentime.preferences.PreferencesManager
import javax.inject.Inject

/**
 * Mapper for profile data between preferences and UI models
 */
class ProfileMapper @Inject constructor() {

    /**
     * Map preferences to ProfileUiModel
     */
    fun toUiModel(
        preferencesManager: PreferencesManager
    ): ProfileUiModel {
        return ProfileUiModel(
            username = preferencesManager.getUsername(),
            userId = preferencesManager.getUserId()
        )
    }

    /**
     * Map ProfileUiModel back to preferences (for updates)
     */
    fun toPreferences(
        uiModel: ProfileUiModel,
        preferencesManager: PreferencesManager
    ) {

    }
}

