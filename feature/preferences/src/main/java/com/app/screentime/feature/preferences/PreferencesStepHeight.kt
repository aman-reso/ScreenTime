package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

val DEFAULT_HEIGHT_PRESETS = listOf("160", "168", "175", "180", "185")

/**
 * Step 4: Height Preference.
 * Matches design system layout:
 * - "How tall are you?"
 * - "Matches love to know! You can change this later."
 * - Height display with quick-select pills.
 */
@Composable
fun PreferencesStepHeight(
    heightCm: String,
    onHeightChange: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    presets: List<String> = DEFAULT_HEIGHT_PRESETS,
    error: String? = null
) {
    val primaryTextColor = scheme.basicTextDominant
    val secondaryTextColor = scheme.basicTextRecessive
    val accentColor = scheme.basicAccent
    val inactiveBorderColor = scheme.basicStrokeSubtle

    val heightInFeet = remember(heightCm) {
        val cm = heightCm.toIntOrNull() ?: 175
        val totalInches = (cm / 2.54).toInt()
        val feet = totalInches / 12
        val inches = totalInches % 12
        "$feet'$inches\""
    }

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
                text = "How tall are you?",
                style = ODSTextStyles.titleL,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Matches love to know! You can choose to show or hide this later.",
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        // ── 2. Height Display & Stepper Card ───────────────────────────────
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = 16.dp),
            border = ODSBorder(
                width = 2.dp,
                colorList = listOf(ODSColorModel(hexColor = if (error != null) scheme.functionalDestructiveStandard else accentColor))
            ),
            padding = ODSPadding(all = 20.dp)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = 12.dp,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    gap = 8.dp
                ) {
                    BasicTextField(
                        value = heightCm,
                        onValueChange = { if (it.length <= 3) onHeightChange(it) },
                        textStyle = ODSTextStyles.titleL.toTextStyle().copy(
                            color = primaryTextColor.getColor(),
                            textAlign = TextAlign.Center
                        ),
                        cursorBrush = SolidColor(accentColor.getColor()),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.size(width = 80.dp, height = 44.dp)
                    )

                    ODSText(
                        text = "cm",
                        style = ODSTextStyles.bodyL,
                        color = secondaryTextColor
                    )
                }

                ODSText(
                    text = "≈ $heightInFeet",
                    style = ODSTextStyles.bodyMBold,
                    color = accentColor
                )
            }
        }

        if (error != null) {
            ODSText(
                text = error,
                style = ODSTextStyles.microcopyRegular,
                color = scheme.functionalDestructiveStandard
            )
        }

        // ── 3. Quick Select Presets ────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 10.dp
        ) {
            ODSText(
                text = "QUICK SELECT",
                style = ODSTextStyles.microcopyBold,
                color = secondaryTextColor
            )

            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = 8.dp,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                presets.forEach { preset ->
                    val isSelected = heightCm == preset
                    ODSBox(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onHeightChange(preset) }
                            ),
                        background = listOf(
                            ODSColorModel(
                                hexColor = if (isSelected) scheme.basicAccentSecondary else scheme.basicBackgroundCard
                            )
                        ),
                        cornerRadius = ODSCorners(all = 12.dp),
                        border = ODSBorder(
                            width = if (isSelected) 2.dp else 1.dp,
                            colorList = listOf(
                                ODSColorModel(
                                    hexColor = if (isSelected) accentColor else inactiveBorderColor
                                )
                            )
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = "$preset cm",
                            style = ODSTextStyles.bodySBold,
                            color = if (isSelected) accentColor else primaryTextColor
                        )
                    }
                }
            }
        }
    }
}
