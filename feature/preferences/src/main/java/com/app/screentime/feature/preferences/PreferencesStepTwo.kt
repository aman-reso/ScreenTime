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
 * Step 2: Identity & Match Preferences (Gender & Interested In).
 */
@Composable
fun PreferencesStepTwo(
    selectedGender: String,
    interestedIn: String,
    scheme: ODSTheme,
    onGenderSelect: (String) -> Unit,
    onInterestedInChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val genders = listOf("Woman", "Man", "Non-binary", "Genderqueer", "Agender")
    val interestedOptions = listOf("Women", "Men", "Everyone")

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent6
    ) {
        // ── 1. Gender Selection Section ────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent4
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
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
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
            }

            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent3
            ) {
                genders.forEach { gender ->
                    val isSelected = selectedGender == gender
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onGenderSelect(gender) }
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
                                    hexColor = if (isSelected) scheme.basicAccent else scheme.basicStroke
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
            }
        }

        // ── 2. Interested In Section ───────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent4
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent2
            ) {
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Who are you interested in?",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextDominant
                )
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Select who you would like to explore connections with.",
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
            }

            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent3,
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                interestedOptions.forEach { option ->
                    val isSelected = interestedIn == option
                    ODSBox(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onInterestedInChange(option) }
                            ),
                        height = 48.dp,
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
                                    hexColor = if (isSelected) scheme.basicAccent else scheme.basicStroke
                                )
                            )
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = option,
                            style = if (isSelected) ODSTextStyles.bodyMBold else ODSTextStyles.bodySBold,
                            color = if (isSelected) scheme.basicAccent else scheme.basicTextDominant
                        )
                    }
                }
            }
        }
    }
}
