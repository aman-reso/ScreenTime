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
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VpnLock
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
 * VPN Permission Screen Content
 * Shows when VPN permission is not granted
 */
@Composable
fun VpnPermissionScreenContent(
    modifier: Modifier = Modifier,
    hasVpnPermission: Boolean,
    onVpnPermissionClick: () -> Unit,
    onBackClick: () -> Unit = {},
    scheme: ODSTheme
) {
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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

            // Hero Icon
            ODSBox(
                modifier = Modifier.size(DSVariables.sizingComponent15),
                background = listOf(ODSColorModel(scheme.basicAccent)),
                cornerRadius = ODSCorners(all = DSVariables.radiusLarge),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.Default.VpnLock,
                        tint = scheme.basicTextOnAccent,
                        contentDescription = "VPN Permission"
                    ),
                    width = DSVariables.sizingComponent11,
                    height = DSVariables.sizingComponent11
                )
            }

            // Title
            ODSText(
                text = "VPN Permission Required",
                style = DSTextStyles.bodyL,
                color = scheme.basicText,
                textAlign = TextAlign.Center
            )

            // Description
            ODSText(
                text = "To block websites and protect your browsing, ScreenTime needs VPN permission. This allows us to intercept and block access to distracting sites.",
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicTextRecessive,
                textAlign = TextAlign.Center
            )

            // Permission Card
            PermissionCard(
                title = "VPN Service",
                description = "Required to block websites and filter internet traffic",
                isGranted = hasVpnPermission,
                icon = ODSIconModel(imageVector = Icons.Default.Security),
                onClick = onVpnPermissionClick,
                scheme = scheme
            )

            // Request Permission Button - Show if permission not granted
            if (!hasVpnPermission) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Request VPN Permission",
                        size = ODSButtonSize.SMALL,
                        variant = ODSButtonVariant.PRIMARY
                    ),
                    onClick = onVpnPermissionClick
                )
            }

            if (hasVpnPermission) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Continue"
                    ),
                    onClick = {
                        // Permission granted, will show main content
                    }
                )
            }
        }
    }
}

