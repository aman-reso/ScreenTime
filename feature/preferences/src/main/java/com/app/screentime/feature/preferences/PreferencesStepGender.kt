package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
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
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Step 2: Gender Identity Preference.
 * Recreated with 100% ODS components strictly following GEMINI.md rules.
 */

val DEFAULT_GENDER_OPTIONS = listOf(
    "Woman",
    "Man",
    "Non-binary",
    "Genderqueer",
    "Agender"
)

@Composable
fun GenderHeader(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "How do you identify?",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Choose the identity that best represents you.",
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun GenderOptionCard(
    gender: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect
            ),
        height = 52.dp,
        background = listOf(
            ODSColorModel(
                hexColor = if (isSelected) scheme.basicAccentSecondary else scheme.basicBackgroundCard
            )
        ),
        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
        border = ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(
                ODSColorModel(
                    hexColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle
                )
            )
        ),
        padding = ODSPadding(
            horizontal = ODSVariables.spacingLayout1,
            vertical = ODSVariables.spacingComponent3
        )
    ) {
        ODSRow(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSText(
                text = gender,
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicTextDominant
            )

            if (isSelected) {
                ODSBox(
                    modifier = Modifier.size(22.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_check,
                            contentDescription = "Selected"
                        ),
                        tint = scheme.basicBackgroundCard.getColor(),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PreferencesStepGender(
    selectedGender: String,
    onGenderSelect: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    genders: List<String> = DEFAULT_GENDER_OPTIONS
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent6
    ) {
        GenderHeader(scheme = scheme)

        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent3
        ) {
            genders.forEach { gender ->
                GenderOptionCard(
                    gender = gender,
                    isSelected = selectedGender == gender,
                    onSelect = { onGenderSelect(gender) },
                    scheme = scheme
                )
            }
        }
    }
}
