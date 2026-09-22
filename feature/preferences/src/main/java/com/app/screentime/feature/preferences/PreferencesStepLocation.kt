package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Step 11: Location Settings (with Skip).
 * Recreated with 100% ODS components strictly following GEMINI.md rules.
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
            text = "Where are you based?",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Discover matches around you within your preferred radius. Exact coordinates are never shared.",
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
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
        border = ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(
                ODSColorModel(
                    hexColor = if (isLocationEnabled) scheme.basicAccent else scheme.basicStroke
                )
            )
        ),
        padding = ODSPadding(all = ODSVariables.spacingLayout1)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent4,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ODSBox(
                modifier = Modifier.size(64.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_map_pin,
                        contentDescription = "Location Pin"
                    ),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(28.dp)
                )
            }

            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent1,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ODSText(
                    text = if (isLocationEnabled) currentCity else "Location Not Set",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextDominant
                )
                ODSText(
                    text = if (isLocationEnabled) "Active • Searching within 20 km" else "Enable location to find nearby profiles",
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
            }

            // Enable / Change Location Button
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onEnableLocation
                    ),
                background = listOf(
                    ODSColorModel(
                        hexColor = if (isLocationEnabled) scheme.basicBackgroundCardSubtle else scheme.basicAccent
                    )
                ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                border = if (isLocationEnabled) ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                ) else null,
                contentAlignment = Alignment.Center
            ) {
                ODSText(
                    text = if (isLocationEnabled) "Change Location" else "Enable Location Access",
                    style = ODSTextStyles.bodySBold,
                    color = if (isLocationEnabled) scheme.basicTextDominant else scheme.basicBackgroundCard
                )
            }
        }
    }
}

@Composable
fun LocationSkipAction(
    onSkip: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSkip
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSText(
            text = "Skip for now",
            style = ODSTextStyles.bodySBold,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun PreferencesStepLocation(
    currentCity: String,
    isLocationEnabled: Boolean,
    onEnableLocation: () -> Unit,
    onSkip: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
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
            scheme = scheme
        )
        LocationSkipAction(
            onSkip = onSkip,
            scheme = scheme
        )
    }
}
