package com.app.screentime.landing.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.app.screentime.BuildConfig
import com.app.screentime.config.R
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardquickactiondeprecated.ODSCardQuickActionDeprecated
import com.telekom.odsystem.organisms.cardquickactiondeprecated.ODSCardQuickActionDeprecatedProps
import com.telekom.odsystem.organisms.cardquickactiondeprecated.ODSCardQuickActionDeprecatedVariant
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.guacamoleSecondaryScheme
import com.telekom.odsystem.tokens.tokens.iguanaSecondaryScheme
import com.telekom.odsystem.tokens.tokens.jacuzziSecondaryScheme
import com.telekom.odsystem.tokens.tokens.lagoonSecondaryScheme
import com.telekom.odsystem.tokens.tokens.macawSecondaryScheme
import com.telekom.odsystem.tokens.tokens.orchidSecondaryScheme

/**
 * Quick Action item data
 */
data class QuickAction(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val cardScheme: ODSTheme,
    val onClick: () -> Unit
)

/**
 * Quick Actions Section component
 * Displays quick access to features from the profile screen
 */
fun LazyListScope.quickActionsSection(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    quickActions: List<QuickAction>) {
    item {
        ODSText(
            text = stringResource(R.string.features),
            style = DSTextStyles.bodyMBold,
            color = scheme.basicText
        )
    }
    item {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.features_subtitle),
            style = DSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
    item {
        Spacer(
            modifier = Modifier.height(DSVariables.spacingComponent2)
        )
    }

    items(items = quickActions) { action ->
        QuickActionCard(action = action)
    }
}

@Composable
fun rememberQuickActions(
    onNavigateToControlCenter: () -> Unit,
    onNavigateToManageLocation: () -> Unit,
    onNavigateToRecoverNotification: () -> Unit,
    onNavigateToAppLock: () -> Unit,
    onNavigateToFileManager: () -> Unit,
    onNavigateToWallpaper: () -> Unit
): List<QuickAction> {

    return buildList {
        add(
            QuickAction(
                id = "control_center",
                title = stringResource(R.string.control_center),
                description = stringResource(R.string.control_center_description),
                icon = Icons.Outlined.Settings,
                cardScheme = macawSecondaryScheme,
                onClick = onNavigateToControlCenter
            )
        )
        add(
            QuickAction(
                id = "manage_location",
                title = stringResource(R.string.manage_location),
                description = stringResource(R.string.manage_location_description),
                icon = Icons.Outlined.LocationOn,
                cardScheme = jacuzziSecondaryScheme,
                onClick = onNavigateToManageLocation
            )
        )
        add(
            QuickAction(
                id = "recover_notification",
                title = stringResource(R.string.recover_deleted_notifications),
                description = stringResource(R.string.recover_deleted_notifications_description),
                icon = Icons.Outlined.Notifications,
                cardScheme = guacamoleSecondaryScheme,
                onClick = onNavigateToRecoverNotification
            )
        )
        add(
            QuickAction(
                id = "app_lock",
                title = stringResource(R.string.app_lock),
                description = stringResource(R.string.app_lock_description),
                icon = Icons.Outlined.Lock,
                cardScheme = iguanaSecondaryScheme,
                onClick = onNavigateToAppLock
            )
        )
        add(
            QuickAction(
                id = "wallpaper",
                title = stringResource(R.string.set_wallpaper),
                description = stringResource(R.string.set_wallpaper_description),
                icon = Icons.Outlined.Wallpaper,
                cardScheme = orchidSecondaryScheme,
                onClick = onNavigateToWallpaper
            )
        )

        if (BuildConfig.DEBUG) {
            add(
                QuickAction(
                    id = "file_manager",
                    title = stringResource(R.string.file_manager),
                    description = stringResource(R.string.file_manager_description),
                    icon = Icons.Outlined.Folder,
                    cardScheme = lagoonSecondaryScheme,
                    onClick = onNavigateToFileManager
                )
            )
        }
    }
}

/**
 * Individual Quick Action Card using ODSCardQuickActionDeprecated
 */
@Composable
private fun QuickActionCard(action: QuickAction) {
    ODSCardQuickActionDeprecated(
        modifier = Modifier.fillMaxWidth(),
        scheme = action.cardScheme,
        props = ODSCardQuickActionDeprecatedProps(
            variant = ODSCardQuickActionDeprecatedVariant.TITLE,
            title = action.title,
            subtitle = action.description,
            logo = ODSImageModel(
                imageVector = action.icon,
                tint = action.cardScheme.basicText,
                contentDescription = action.title
            )
        ),
        onClick = action.onClick
    )
}

