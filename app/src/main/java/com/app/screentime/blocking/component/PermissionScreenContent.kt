package com.app.screentime.blocking.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.app.screentime.R
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Permission screen content component for displaying permission requirements and status.
 * Shows hero icon, title, description, permission cards, and continue button when all permissions are granted.
 *
 * @param modifier Modifier to be applied to the component
 * @param hasAccessibilityPermission Whether accessibility permission is granted (or USAGE_STATS for app lock)
 * @param hasOverlayPermission Whether overlay permission is granted
 * @param onAccessibilityPermissionClick Callback when accessibility permission card is clicked
 * @param onOverlayPermissionClick Callback when overlay permission card is clicked
 * @param onContinue Callback when continue button is clicked (only shown when all permissions granted)
 * @param onBackClick Callback when back button is clicked
 * @param scheme ODS theme scheme
 * @param firstPermissionTitle Optional title for first permission card (defaults to accessibility_service_title)
 * @param firstPermissionDescription Optional description for first permission card (defaults to accessibility_service_description)
 * @param screenTitle Optional title for the permission screen (defaults to app_blocking_permissions_title)
 * @param screenDescription Optional description for the permission screen (defaults to app_blocking_permissions_description)
 * @param overlayDescription Optional description for overlay permission (defaults to display_overlay_description)
 */
@Composable
fun PermissionScreenContent(
    modifier: Modifier = Modifier,
    hasAccessibilityPermission: Boolean,
    hasOverlayPermission: Boolean,
    onAccessibilityPermissionClick: () -> Unit,
    onOverlayPermissionClick: () -> Unit,
    onContinue: () -> Unit = {},
    onBackClick: () -> Unit = {},
    scheme: ODSTheme,
    firstPermissionTitle: String? = null,
    firstPermissionDescription: String? = null,
    screenTitle: String? = null,
    screenDescription: String? = null,
    overlayDescription: String? = null
) {
    val allPermissionsGranted = hasAccessibilityPermission && hasOverlayPermission

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {}

        ODSColumn(
            modifier = Modifier
                .fillMaxSize(),
            padding = ODSPadding(horizontal = DSVariables.spacingComponent4),
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = DSVariables.spacingComponent4
        ) {
            // Back button
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        buttonIcon = ODSIconModel(
                            drawableRes = com.telekom.odsystem.R.drawable.left_condensed_type_standard_size_standard,
                            tint = scheme.basicText,
                            contentDescription = "Back"
                        ),
                        buttonType = ODSButtonButtonType.ICON_ONLY,
                        variant = ODSButtonVariant.GHOST,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onBackClick
                )
            }

            ODSBox(height = DSVariables.spacingComponent2) {}

            ODSBox(
                modifier = Modifier.size(DSVariables.sizingComponent15),
                background = listOf(ODSColorModel(scheme.basicAccent)),
                cornerRadius = ODSCorners(all = DSVariables.radiusLarge),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.Default.Security,
                        tint = scheme.basicTextOnAccent,
                        contentDescription = stringResource(R.string.content_description_permissions_icon)
                    ),
                    width = DSVariables.sizingComponent11,
                    height = DSVariables.sizingComponent11
                )
            }

            // Title
            ODSText(
                text = screenTitle ?: stringResource(R.string.app_blocking_permissions_title),
                style = DSTextStyles.bodyL,
                color = scheme.basicText,
                textAlign = TextAlign.Center
            )

            // Subtitle
            ODSText(
                text = screenDescription ?: stringResource(R.string.app_blocking_permissions_description),
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicTextRecessive,
                textAlign = TextAlign.Center
            )

            // Permission Cards
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                PermissionCard(
                    title = firstPermissionTitle ?: "Usage Access",
                    description = firstPermissionDescription ?: "Required to detect which app is currently running",
                    isGranted = hasAccessibilityPermission,
                    icon = ODSIconModel(imageVector = Icons.Default.DataUsage),
                    onClick = onAccessibilityPermissionClick,
                    scheme = scheme
                )

                PermissionCard(
                    title = stringResource(R.string.display_overlay_title),
                    description = overlayDescription ?: stringResource(R.string.display_overlay_description),
                    isGranted = hasOverlayPermission,
                    icon = ODSIconModel(imageVector = Icons.Default.Layers),
                    onClick = onOverlayPermissionClick,
                    scheme = scheme
                )
            }

            if (allPermissionsGranted) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = stringResource(R.string.continue_label)
                    ),
                    onClick = onContinue
                )
            }
        }
    }
}

