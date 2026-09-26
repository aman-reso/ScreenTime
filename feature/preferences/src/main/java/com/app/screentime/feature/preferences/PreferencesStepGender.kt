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

/**
 * Step 2: Gender Preference.
 * Matches Figma mockup (Frame 2):
 * - "What's your gender?"
 * - "Select the option that best describes you."
 * - Options: Male, Female, Non-binary
 * - Selected: 2.dp #A7344D border, #A7344D text
 * - Unselected: 1.dp #E2E8F0 border, #11111A text
 */

val DEFAULT_GENDER_SELECTIONS = listOf(
    "Male",
    "Female",
    "Non-binary"
)

@Composable
fun PreferencesStepGender(
    selectedGender: String,
    onGenderSelect: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    genders: List<String> = DEFAULT_GENDER_SELECTIONS,
    error: String? = null
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
                text = "What's your gender?",
                style = ODSTextStyles.titleL,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Select the option that best describes you.",
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
            genders.forEach { gender ->
                val isSelected = selectedGender.equals(gender, ignoreCase = true)
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onGenderSelect(gender) }
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
                        text = gender,
                        style = ODSTextStyles.bodyMBold,
                        color = if (isSelected) accentColor else primaryTextColor
                    )
                }
            }
        }
    }
}
