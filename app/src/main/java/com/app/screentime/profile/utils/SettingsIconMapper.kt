package com.app.screentime.profile.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.app.screentime.profile.model.ProfileSettingsKey

object SettingsIconMapper {
    private val iconMap: Map<ProfileSettingsKey, ImageVector> = mapOf(
        ProfileSettingsKey.BLOCK_APP to Icons.Default.Block,
        ProfileSettingsKey.SET_APP_LIMIT to Icons.Default.Timer,
        ProfileSettingsKey.SET_APP_LAUNCH_LIMIT to Icons.Default.PlayArrow,
        ProfileSettingsKey.AD_BLOCKING to Icons.Default.AdsClick,
        ProfileSettingsKey.THEME to Icons.Default.Palette,
        ProfileSettingsKey.LANGUAGE to Icons.Default.Language,
        ProfileSettingsKey.WIDGET to Icons.Default.Widgets,
        ProfileSettingsKey.TOTP to Icons.Default.Security,
        ProfileSettingsKey.VPN_SERVICE to Icons.Default.VpnLock,
        ProfileSettingsKey.HELP_SUPPORT to Icons.Default.Help,
        ProfileSettingsKey.PRIVACY_POLICY to Icons.Default.PrivacyTip,
        ProfileSettingsKey.TERMS_OF_SERVICE to Icons.Default.Description
    )

    private val textBasedIconMap: Map<String, ImageVector> = mapOf(
        "privacy" to Icons.Default.PrivacyTip,
        "terms" to Icons.Default.Description
    )

    fun getIcon(key: ProfileSettingsKey?, text: String): ImageVector {
        // First try to get icon by key
        key?.let {
            iconMap[it]?.let { icon ->
                return icon
            }
        }

        // Then try to get icon by text content
        textBasedIconMap.forEach { (keyword, icon) ->
            if (text.contains(keyword, ignoreCase = true)) {
                return icon
            }
        }

        // Default icon
        return Icons.AutoMirrored.Filled.ShowChart
    }
}

