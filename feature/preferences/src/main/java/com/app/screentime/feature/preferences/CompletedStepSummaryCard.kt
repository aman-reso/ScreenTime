package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Compact summary card displayed for previously completed preference steps.
 * 100% styled via scheme tokens:
 * - Background: scheme.basicBackgroundSubtle
 * - Border: scheme.basicStrokeSubtle
 * - Corners: 16.dp
 * - Label: scheme.basicTextRecessive
 * - Value: scheme.basicTextDominant
 * - Trailing icon: scheme.basicTextRecessive
 */
@Composable
fun CompletedStepSummaryCard(
    label: String,
    value: String,
    onEdit: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onEdit
            ),
        padding = ODSPadding(all = 16.dp),
        cornerRadius = ODSCorners(all = 16.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
    ) {
        ODSRow(
            gap = 8.dp,
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ODSText(
                text = label,
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )
            ODSText(
                text = value,
                style = ODSTextStyles.bodySBold,
                color = scheme.basicTextDominant
            )
        }

        ODSBox(
            modifier = Modifier.size(20.dp),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_edit_3,
                    contentDescription = "Edit $label"
                ),
                tint = scheme.basicTextRecessive.getColor(),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
