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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
 * Step 5: Height Preference.
 * Recreated with 100% ODS components strictly following GEMINI.md rules.
 */

val DEFAULT_HEIGHT_PRESETS = listOf("160", "168", "175", "180", "185")

@Composable
fun HeightHeader(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "And finally, how tall are you?",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Matches love to know! You can choose to show or hide this on your public card anytime.",
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun HeightInputCard(
    heightCm: String,
    onHeightChange: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val funnelFontSemiBold = remember {
        FontFamily(Font(R.font.funnelsans_semibold, FontWeight.SemiBold))
    }

    val heightInFeet = remember(heightCm) {
        val cm = heightCm.toIntOrNull() ?: 175
        val totalInches = (cm / 2.54).toInt()
        val feet = totalInches / 12
        val inches = totalInches % 12
        "$feet'$inches\""
    }

    ODSBox(
        modifier = modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
        border = ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        ),
        padding = ODSPadding(all = ODSVariables.spacingLayout1)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent3,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ODSText(
                text = "HEIGHT IN CENTIMETERS",
                style = ODSTextStyles.microcopyBold,
                color = scheme.basicTextRecessive
            )

            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                gap = ODSVariables.spacingComponent2
            ) {
                BasicTextField(
                    value = heightCm,
                    onValueChange = { if (it.length <= 3) onHeightChange(it) },
                    textStyle = TextStyle(
                        color = scheme.basicTextDominant.getColor(),
                        fontSize = 16.sp,
                        fontFamily = funnelFontSemiBold,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    ),
                    cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.size(width = 60.dp, height = 28.dp)
                )

                ODSText(
                    text = "cm  ($heightInFeet)",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicAccent
                )
            }
        }
    }
}

@Composable
fun HeightPresetsRow(
    selectedHeight: String,
    onHeightSelect: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    presets: List<String> = DEFAULT_HEIGHT_PRESETS
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2
    ) {
        ODSText(
            text = "QUICK SELECT",
            style = ODSTextStyles.microcopyBold,
            color = scheme.basicTextRecessive
        )

        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent2,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            presets.forEach { preset ->
                val isSelected = selectedHeight == preset
                ODSBox(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onHeightSelect(preset) }
                        ),
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
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "$preset cm",
                        style = if (isSelected) ODSTextStyles.bodySBold else ODSTextStyles.bodySRegular,
                        color = if (isSelected) scheme.basicAccent else scheme.basicTextDominant
                    )
                }
            }
        }
    }
}

@Composable
fun PreferencesStepHeight(
    heightCm: String,
    onHeightChange: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent6
    ) {
        HeightHeader(scheme = scheme)
        HeightInputCard(
            heightCm = heightCm,
            onHeightChange = onHeightChange,
            scheme = scheme
        )
        HeightPresetsRow(
            selectedHeight = heightCm,
            onHeightSelect = onHeightChange,
            scheme = scheme
        )
    }
}
