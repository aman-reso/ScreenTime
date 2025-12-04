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
     * Map profile data, settings list, VPN status, and blocked sites count to UI Props
     */
    fun toUiProps(
        profile: ProfileUiModel?,
        settingsList: List<ProfileSettingsUi>,
        isVpnRunning: Boolean,
        blockedSitesCount: Int,
        isLoading: Boolean = false,
        isUpdating: Boolean = false,
        error: String? = null
    ): ProfileUiProps {
        // Update VPN service setting text based on VPN status
        val updatedSettingsList = settingsList.map { item ->
            if (item.key == ProfileSettingsKey.VPN_SERVICE) {
                ProfileSettingsUi.Other(
                    text = if (isVpnRunning) R.string.disable_vpn else R.string.enable_vpn,
                    url = "",
                    key = ProfileSettingsKey.VPN_SERVICE
                )
            } else {
                item
            }
        }

        return ProfileUiProps(
            settingsList = updatedSettingsList,
            profile = profile,
            isVpnRunning = isVpnRunning,
            blockedSitesCount = blockedSitesCount,
            isLoading = isLoading,
            isUpdating = isUpdating,
            error = error
        )
    }
}

