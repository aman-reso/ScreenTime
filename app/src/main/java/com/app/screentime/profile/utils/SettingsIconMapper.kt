package com.app.screentime.profile.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.AdsClick
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.ui.graphics.vector.ImageVector
import com.app.screentime.profile.model.ProfileSettingsKey

object SettingsIconMapper {
    private val iconMap: Map<ProfileSettingsKey, ImageVector> = mapOf(
        ProfileSettingsKey.BLOCK_APP to Icons.Outlined.Block,
        ProfileSettingsKey.SET_APP_LIMIT to Icons.Outlined.Timer,
        ProfileSettingsKey.SET_APP_LAUNCH_LIMIT to Icons.Outlined.PlayArrow,
        ProfileSettingsKey.APP_LOCK to Icons.Outlined.Lock,
        ProfileSettingsKey.AD_BLOCKING to Icons.Outlined.AdsClick,
        ProfileSettingsKey.THEME to Icons.Outlined.Palette,
        ProfileSettingsKey.LANGUAGE to Icons.Outlined.Language,
        ProfileSettingsKey.WIDGET to Icons.Outlined.Widgets,
        ProfileSettingsKey.TOTP to Icons.Outlined.Security,
        ProfileSettingsKey.HELP_SUPPORT to Icons.Outlined.Help,
        ProfileSettingsKey.PRIVACY_POLICY to Icons.Outlined.PrivacyTip,
        ProfileSettingsKey.TERMS_OF_SERVICE to Icons.Outlined.Description,
        ProfileSettingsKey.SHARE_APP to Icons.Outlined.Share,
        ProfileSettingsKey.RECOVER_NOTIFICATION to Icons.Outlined.Notifications,
        ProfileSettingsKey.WALLPAPER to Icons.Outlined.Wallpaper

    )

    private val textBasedIconMap: Map<String, ImageVector> = mapOf(
        "privacy" to Icons.Outlined.PrivacyTip,
        "terms" to Icons.Outlined.Description
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
        return Icons.AutoMirrored.Outlined.ShowChart
    }
}

