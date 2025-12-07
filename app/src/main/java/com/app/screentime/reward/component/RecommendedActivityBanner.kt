package com.app.screentime.reward.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Recommended Activity Banner Component
 * Displays colorful promotional banner
 */
@Composable
fun RecommendedActivityBanner(
    title: String = "October Surprise",
    subtitle: String = "Double point exchange",
    dateRange: String = "From October 1st to October 31st",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    scheme: ODSTheme
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .customClickable(
                onClick = onClick,
                isPressed = {}
            ),
        background = listOf(
            com.telekom.odsystem.foundations.ODSColorModel(
                gradient = ODSLinearGradientModel(
                    Pair(0f, scheme.functionalInformationalStandard),
                    Pair(0.5f, scheme.functionalWarningStandard),
                    Pair(1f, scheme.basicAccent),
                    angleInDegrees = 45f
                )
            )
        ),
        cornerRadius = ODSCorners(all = 16.dp),
        padding = ODSPadding(all = DSVariables.spacingComponent4)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ODSColumn {
                ODSText(
                    text = subtitle,
                    style = com.telekom.odsystem.DSTextStyles.oxBodySRegular,
                    color = scheme.basicTextOnAccent
                )
                ODSText(
                    text = dateRange,
                    style = com.telekom.odsystem.DSTextStyles.oxBodySRegular,
                    color = scheme.basicTextOnAccent
                )
            }

            // Title at bottom right
            ODSBox(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                ODSText(
                    text = title,
                    style = com.telekom.odsystem.DSTextStyles.oxTitleM,
                    color = scheme.basicTextOnAccent
                )
            }
        }
    }
}
