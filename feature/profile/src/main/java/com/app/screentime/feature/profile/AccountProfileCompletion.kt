package com.app.screentime.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Profile Completion Progress Card (Figma node-id 10-1157).
 *
 * Rules:
 * 1. 100% ODS components.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Max text size 12sp (microcopyBold).
 * 4. Padding and margins from `ODSVariables`.
 */
@Composable
fun AccountProfileCompletion(
    percentage: Int = 80,
    scheme: ODSTheme = zonaODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ODSVariables.spacingLayout1),
        gap = ODSVariables.spacingComponent3,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSText(
                text = "Profile Completion",
                style = ODSTextStyles.microcopyBold, // 12sp
                color = scheme.basicText
            )
            ODSText(
                text = "$percentage%",
                style = ODSTextStyles.microcopyBold, // 12sp
                color = scheme.basicAccent
            )
        }

        // Progress Bar (Track & Fill)
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
            background = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle)),
            contentAlignment = Alignment.CenterStart
        ) {
            // Active Progress Fill
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth(fraction = (percentage / 100f).coerceIn(0f, 1f))
                    .height(6.dp),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
            )
        }
    }
}
