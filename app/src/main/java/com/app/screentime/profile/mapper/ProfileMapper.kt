package com.app.screentime.profile.mapper

import com.app.screentime.core.network.model.DeviceRegistrationResponse
import com.app.screentime.profile.model.ProfileUiModel
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.utils.DateUtils
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
        val data = preferencesManager.getUserInformation()
        return ProfileUiModel(
            username = data?.username,
            userId = data?.userId,
            joinedOn = DateUtils.formatDate(data?.createdAt ?: "")
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

