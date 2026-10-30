package com.app.screentime.feature.discover.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.constraintlayout.compose.ConstraintLayout
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Top App Bar for Home / Discovery Feed (Figma node-id 25-14).
 * Built with ConstraintLayout and 100% Telekom ODS design system:
 * - Brand Color: scheme.basicAccent
 * - Icon Tint: scheme.basicText / scheme.basicAccent
 * - Sizing & Spacing: ODSVariables tokens
 */
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    canUndo: Boolean = false,
    onUndoClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onPreferencesClick: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = ODSVariables.spacingComponent4,
                end = ODSVariables.spacingComponent4
            )
    ) {
        val (logoText, actionsGroup) = createRefs()

        ODSText(
            text = "winter",
            style = ODSTextStyle(
                fontFamily = R.font.pacifico_regular,
                fontSize = 24,
                lineHeight = 48,
                fontWeight = FontWeight.Bold
            ),
            color = scheme.basicAccent,
            modifier = Modifier.constrainAs(logoText) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            }
        )

        ODSRow(
            gap = ODSVariables.spacingComponent3,
            horizontalAlignment = Alignment.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(actionsGroup) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        ) {
            if (canUndo) {
                ODSBox(
                    modifier = Modifier
                        .size(ODSVariables.sizingComponent10)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onUndoClick
                        ),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = com.app.screentime.feature.discover.R.drawable.ic_undo,
                            contentDescription = "Undo Swipe"
                        ),
                        tint = scheme.basicText.getColor(),
                        modifier = Modifier.size(ODSVariables.sizingComponent7)
                    )
                }
            }

            ODSBox(
                modifier = Modifier
                    .size(ODSVariables.sizingComponent10)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onPreferencesClick
                    ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_sliders,
                        contentDescription = "Preferences"
                    ),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(ODSVariables.sizingComponent7)
                )
            }
        }
    }
}
