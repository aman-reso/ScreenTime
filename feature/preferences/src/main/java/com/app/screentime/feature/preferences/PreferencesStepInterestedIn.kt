package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

val DEFAULT_INTERESTED_IN_LIST = listOf("Women", "Men", "Everyone")

/**
 * Step 5: Interested In Preference.
 * Matches unified Figma design system layout:
 * - "Who are you interested in?"
 * - "Select who you would like to explore connections with."
 * - Options: Women, Men, Everyone
 */
@Composable
fun PreferencesStepInterestedIn(
    selectedInterestedIn: String,
    onInterestedInChange: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    options: List<String> = DEFAULT_INTERESTED_IN_LIST
) {
    val primaryTextColor = scheme.basicTextDominant
    val secondaryTextColor = scheme.basicTextRecessive
    val accentColor = scheme.basicAccent
    val inactiveBorderColor = scheme.basicStrokeSubtle

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = 28.dp,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // ── 1. Heading & Subtitle ──────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 8.dp,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Who are you interested in?",
                style = ODSTextStyles.titleL,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Select who you would like to explore connections with.",
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        // ── 2. Option Cards ────────────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 12.dp,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            options.forEach { option ->
                val isSelected = selectedInterestedIn.equals(option, ignoreCase = true)
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onInterestedInChange(option) }
                        ),
                    cornerRadius = ODSCorners(all = 16.dp),
                    border = ODSBorder(
                        width = if (isSelected) 2.dp else 1.dp,
                        colorList = listOf(
                            ODSColorModel(
                                hexColor = if (isSelected) accentColor else inactiveBorderColor
                            )
                        )
                    ),
                    background = listOf(
                        ODSColorModel(
                            hexColor = if (isSelected) scheme.basicAccentSecondary else scheme.basicBackgroundCard
                        )
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = option,
                        style = ODSTextStyles.bodyMBold,
                        color = if (isSelected) accentColor else primaryTextColor
                    )
                }
            }
        }
    }
}
