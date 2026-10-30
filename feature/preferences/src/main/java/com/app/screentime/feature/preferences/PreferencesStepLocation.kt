package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R as ConfigR
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Location Settings Step (Mandatory Access, No Skip).
 * 100% Telekom ODS UI components using ODSButtonIconButton.
 */

@Composable
fun LocationHeader(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(ConfigR.string.pref_based_title),
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicText
        )
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(ConfigR.string.pref_based_subtitle),
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun LocationStatusCard(
    isLocationEnabled: Boolean,
    currentCity: String,
    onEnableLocation: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = 8.dp
    ) {
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onEnableLocation
                ),
            padding = ODSPadding(horizontal = 16.dp, vertical = 14.dp),
            cornerRadius = ODSCorners(all = 16.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            border = if (!error.isNullOrBlank()) ODSBorder(
                width = 2.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicAccent))
            ) else null
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Location Icon + Text Column
                ODSRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 12.dp
                ) {
                    // Location Icon Container (Left side)
                    ODSBox(
                        modifier = Modifier.size(32.dp),
                        cornerRadius = ODSCorners(all = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_map_pin,
                                contentDescription = "Location Pin"
                            ),
                            tint = scheme.basicText.getColor(),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Middle Text Column
                    ODSColumn(
                        modifier = Modifier.weight(1f),
                        gap = 2.dp
                    ) {
                        ODSText(
                            text = if (isLocationEnabled && currentCity.isNotBlank()) currentCity else "Please allow location",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = if (isLocationEnabled && currentCity.isNotBlank()) {
                                "Location active · Matches within 50 km"
                            } else {
                                "Find exciting dates & matches near you!"
                            },
                            style = ODSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }

                // Right: Telekom ODS ODSButtonIconButton
                ODSButton(
                    props = ODSButtonProps(
                        buttonIcon = ODSIconModel(
                            drawableRes = if (isLocationEnabled && currentCity.isNotBlank()) R.drawable.ic_check else com.app.screentime.feature.preferences.R.drawable.outline_my_location_24,
                            contentDescription = "Enable Location"
                        ),
                        variant = ODSButtonVariant.SECONDARY,
                        buttonType = ODSButtonButtonType.ICON_ONLY
                    ),
                    onClick = onEnableLocation,
                    scheme = scheme
                )
            }
        }

        if (!error.isNullOrBlank()) {
            ODSText(
                text = error,
                style = ODSTextStyles.microcopyBold,
                color = scheme.basicAccent,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun PreferencesStepLocation(
    currentCity: String,
    isLocationEnabled: Boolean,
    onEnableLocation: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent6
    ) {
        LocationHeader(scheme = scheme)
        LocationStatusCard(
            isLocationEnabled = isLocationEnabled,
            currentCity = currentCity,
            onEnableLocation = onEnableLocation,
            scheme = scheme,
            error = error
        )
    }
}
