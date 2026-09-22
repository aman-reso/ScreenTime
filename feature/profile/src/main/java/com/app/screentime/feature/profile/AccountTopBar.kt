package com.app.screentime.feature.profile

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
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
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
 * Top App Bar for "My Account" Screen (Figma node-id 10-1134).
 *
 * Rules:
 * 1. 100% ODS components.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Max text size 16sp (bodyMBold).
 * 4. Padding and margins from `ODSVariables`.
 */
@Composable
fun AccountTopBar(
    title: String = "My Account",
    scheme: ODSTheme = zonaODSTheme,
    onEditClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = ODSVariables.spacingComponent4,
                bottom = ODSVariables.spacingComponent4,
                start = ODSVariables.spacingLayout1,
                end = ODSVariables.spacingLayout1
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ODSText(
            text = title,
            style = ODSTextStyles.bodyMBold, // 16sp max text size
            color = scheme.basicText
        )

        ODSRow(
            gap = ODSVariables.spacingComponent3,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Notifications Icon Button
            ODSBox(
                modifier = Modifier
                    .size(44.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onNotificationsClick
                    ),
                padding = ODSPadding(all = ODSVariables.spacingComponent4),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_bell,
                        contentDescription = "Notifications"
                    ),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(ODSVariables.sizingComponent8)
                )
            }

            // Edit Profile Button
            ODSBox(
                modifier = Modifier
                    .size(44.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onEditClick
                    ),
                padding = ODSPadding(all = ODSVariables.spacingComponent4),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_edit_3,
                        contentDescription = "Edit Profile"
                    ),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(ODSVariables.sizingComponent8)
                )
            }
        }
    }
}
