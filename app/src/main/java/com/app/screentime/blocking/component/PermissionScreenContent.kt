package com.app.screentime.blocking.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
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
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
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
 * @param hasAccessibilityPermission Whether accessibility permission is granted
 * @param hasOverlayPermission Whether overlay permission is granted
 * @param onAccessibilityPermissionClick Callback when accessibility permission card is clicked
 * @param onOverlayPermissionClick Callback when overlay permission card is clicked
 * @param onContinue Callback when continue button is clicked (only shown when all permissions granted)
 * @param scheme ODS theme scheme
 */
@Composable
fun PermissionScreenContent(
    modifier: Modifier = Modifier,
    hasAccessibilityPermission: Boolean,
    hasOverlayPermission: Boolean,
    onAccessibilityPermissionClick: () -> Unit,
    onOverlayPermissionClick: () -> Unit,
    onContinue: () -> Unit = {},
    scheme: ODSTheme
) {
    val allPermissionsGranted = hasAccessibilityPermission || hasOverlayPermission

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            padding = ODSPadding(horizontal = DSVariables.spacingComponent3),
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = DSVariables.spacingComponent4
        ) {
            ODSBox(height = DSVariables.spacingComponent5) {}

            // Hero Icon with accent background
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
                text = stringResource(R.string.app_blocking_permissions_title),
                style = DSTextStyles.titleS,
                color = scheme.basicText,
                textAlign = TextAlign.Center
            )

            // Subtitle
            ODSText(
                text = stringResource(R.string.app_blocking_permissions_description),
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
                    title = stringResource(R.string.accessibility_service_title),
                    description = stringResource(R.string.accessibility_service_description),
                    isGranted = hasAccessibilityPermission,
                    icon = ODSIconModel(imageVector = Icons.Default.Accessibility),
                    onClick = onAccessibilityPermissionClick,
                    scheme = scheme
                )

                PermissionCard(
                    title = stringResource(R.string.display_overlay_title),
                    description = stringResource(R.string.display_overlay_description),
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

