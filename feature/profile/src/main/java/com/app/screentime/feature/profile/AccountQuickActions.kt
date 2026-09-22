package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
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
 * Quick Action Grid Component for "My Account" Screen (Figma node-id 10-1184).
 *
 * Rules:
 * 1. 100% ODS components.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Max text size 12sp (microcopyBold).
 * 4. Padding and margins from `ODSVariables`.
 */
@Composable
fun AccountQuickActions(
    scheme: ODSTheme = zonaODSTheme,
    onMatchesClick: () -> Unit = {},
    onLikesInfoClick: () -> Unit = {},
    onSecurityClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ODSVariables.spacingLayout1),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Matches
        AccountQuickActionItem(
            label = "Matches",
            iconRes = R.drawable.ic_zap,
            scheme = scheme,
            onClick = onMatchesClick,
        )

        // Likes Info
        AccountQuickActionItem(
            label = "Likes Info",
            iconRes = R.drawable.ic_heart,
            scheme = scheme,
            onClick = onLikesInfoClick,
        )

        // Security
        AccountQuickActionItem(
            label = "Security",
            iconRes = R.drawable.ic_shield_check,
            scheme = scheme,
            onClick = onSecurityClick,
        )

        // Help
        AccountQuickActionItem(
            label = "Help",
            iconRes = R.drawable.ic_help_circle,
            scheme = scheme,
            onClick = onHelpClick,
        )
    }
}

@Composable
private fun AccountQuickActionItem(
    label: String,
    iconRes: Int,
    scheme: ODSTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier
            .width(76.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        gap = ODSVariables.spacingComponent3,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 56x52 Rounded Box with Icon
        ODSBox(
            modifier = Modifier
                .width(56.dp)
                .height(52.dp),
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
                    drawableRes = iconRes,
                    contentDescription = label
                ),
                tint = scheme.basicText.getColor(),
                modifier = Modifier.size(ODSVariables.sizingComponent8)
            )
        }

        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = label,
            style = ODSTextStyles.microcopyBold, // 12sp
            color = scheme.basicText,
            textAlign = TextAlign.Center
        )
    }
}
