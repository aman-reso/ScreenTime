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
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Top Navigation Bar and Progress Indicator for Onboarding Preferences.
 * Follows Figma node-id 10-51 and design system rules.
 */
@Composable
fun PreferencesTopBar(
    currentStep: Int,
    totalSteps: Int,
    scheme: ODSTheme,
    onBack: () -> Unit,
    onHelp: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent4
    ) {
        // Top Header Row: [Back circular button] -- [Step X of 5] -- [Help circular button]
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Back Button
            ODSBox(
                modifier = Modifier
                    .size(44.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onBack
                    ),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                border = ODSBorder(
                    width = ODSVariables.strokes2,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_arrow_left,
                        contentDescription = "Back"
                    ),
                    tint = scheme.basicTextDominant.getColor(),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Step Indicator (Figma node-id: 10-51)
            ODSText(
                text = "Step $currentStep of $totalSteps",
                style = ODSTextStyles.bodySBold,
                color = scheme.basicTextDominant
            )

            // Circular Help Button
            ODSBox(
                modifier = Modifier
                    .size(44.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onHelp
                    ),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                border = ODSBorder(
                    width = ODSVariables.strokes2,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_help_circle,
                        contentDescription = "Help"
                    ),
                    tint = scheme.basicTextRecessive.getColor(),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Progress Bar
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            height = 4.dp,
            background = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle)),
            cornerRadius = ODSCorners(all = ODSVariables.radiusExtraSmall)
        ) {
            val progressFraction = (currentStep.toFloat() / totalSteps.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
            ODSBox(
                modifier = Modifier.fillMaxWidth(fraction = progressFraction),
                height = 4.dp,
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusExtraSmall)
            )
        }
    }
}
