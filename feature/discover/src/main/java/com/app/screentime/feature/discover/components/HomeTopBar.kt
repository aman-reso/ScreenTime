package com.app.screentime.feature.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Top App Bar for Home / Discovery Feed (Figma node-id 25-14).
 * Fully tokenized with Telekom ODS design system:
 * - Text Style: ODSTextStyles.titleS (24sp)
 * - Brand Color: scheme.basicAccent
 * - Icon Tint: scheme.basicText
 * - Padding & Sizing: ODSVariables spacing & sizing tokens
 */
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    onNotificationsClick: () -> Unit = {},
    onPreferencesClick: () -> Unit = {}
) {
    ODSRow(
        modifier = modifier.fillMaxWidth(),
        padding = ODSPadding(
            top = ODSVariables.spacingComponent3,
            left = ODSVariables.spacingComponent5,
            right = ODSVariables.spacingComponent5
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ODSText(
            text = "Winter",
            style = ODSTextStyle(
                fontFamily = R.font.pacifico_regular,
                fontSize = 24,
                lineHeight = 24,
                fontWeight = FontWeight.Bold
            ),
            color = scheme.basicAccent
        )
        ODSRow(
            gap = ODSVariables.spacingComponent5,
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ODSColumn(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onNotificationsClick
                ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                width = ODSVariables.sizingComponent10,
                height = ODSVariables.sizingComponent10
            ) {
                ODSColumn(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    width = ODSVariables.sizingComponent9,
                    height = ODSVariables.sizingComponent9
                ) {
                    ODSBox(
                        clipContent = true,
                        contentAlignment = Alignment.Center,
                        width = ODSVariables.sizingComponent9,
                        height = ODSVariables.sizingComponent9
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_bell,
                                contentDescription = "Notifications"
                            ),
                            tint = scheme.basicText.getColor(),
                            modifier = Modifier.size(ODSVariables.sizingComponent9)
                        )
                    }
                }
            }
        }
    }
}
