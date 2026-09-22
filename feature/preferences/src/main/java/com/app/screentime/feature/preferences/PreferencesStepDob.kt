package com.app.screentime.feature.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
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
 * Step 3: Date of Birth Preference.
 * Recreated with 100% ODS components strictly following GEMINI.md rules.
 */

@Composable
fun DobHeader(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "When's your birthday?",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Your age will be visible on your profile card, but your exact date of birth is kept private.",
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun DobInputFields(
    day: String,
    month: String,
    year: String,
    onDayChange: (String) -> Unit,
    onMonthChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val funnelFontSemiBold = remember {
        FontFamily(Font(R.font.funnelsans_semibold, FontWeight.SemiBold))
    }

    ODSRow(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent3,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Day input
        ODSBox(
            modifier = Modifier.weight(1f),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            border = ODSBorder(
                width = ODSVariables.strokes2,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            padding = ODSPadding(
                horizontal = ODSVariables.spacingComponent3,
                vertical = ODSVariables.spacingComponent4
            )
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent1,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ODSText(
                    text = "DD",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicTextRecessive
                )
                BasicTextField(
                    value = day,
                    onValueChange = { if (it.length <= 2) onDayChange(it) },
                    textStyle = TextStyle(
                        color = scheme.basicTextDominant.getColor(),
                        fontSize = 16.sp,
                        fontFamily = funnelFontSemiBold,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    ),
                    cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Month input
        ODSBox(
            modifier = Modifier.weight(1f),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            border = ODSBorder(
                width = ODSVariables.strokes2,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            padding = ODSPadding(
                horizontal = ODSVariables.spacingComponent3,
                vertical = ODSVariables.spacingComponent4
            )
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent1,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ODSText(
                    text = "MM",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicTextRecessive
                )
                BasicTextField(
                    value = month,
                    onValueChange = { if (it.length <= 2) onMonthChange(it) },
                    textStyle = TextStyle(
                        color = scheme.basicTextDominant.getColor(),
                        fontSize = 16.sp,
                        fontFamily = funnelFontSemiBold,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    ),
                    cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Year input
        ODSBox(
            modifier = Modifier.weight(1.4f),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            border = ODSBorder(
                width = ODSVariables.strokes2,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            padding = ODSPadding(
                horizontal = ODSVariables.spacingComponent3,
                vertical = ODSVariables.spacingComponent4
            )
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent1,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ODSText(
                    text = "YYYY",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicTextRecessive
                )
                BasicTextField(
                    value = year,
                    onValueChange = { if (it.length <= 4) onYearChange(it) },
                    textStyle = TextStyle(
                        color = scheme.basicTextDominant.getColor(),
                        fontSize = 16.sp,
                        fontFamily = funnelFontSemiBold,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    ),
                    cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun DobAgeBadge(
    calculatedAge: Int,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier,
        gap = ODSVariables.spacingComponent3,
        padding = ODSPadding(
            horizontal = ODSVariables.spacingLayout1,
            vertical = ODSVariables.spacingComponent3
        ),
        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSIcon(
            iconModel = ODSIconModel(
                drawableRes = R.drawable.ic_cake,
                contentDescription = "Age"
            ),
            tint = scheme.basicAccent.getColor(),
            modifier = Modifier.size(ODSVariables.spacingComponent5)
        )
        ODSText(
            text = "You will appear as $calculatedAge years old",
            style = ODSTextStyles.bodySBold,
            color = scheme.basicTextDominant
        )
    }
}

@Composable
fun PreferencesStepDob(
    day: String,
    month: String,
    year: String,
    onDayChange: (String) -> Unit,
    onMonthChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val calculatedAge = remember(day, month, year) {
        try {
            val y = year.toIntOrNull() ?: 1999
            val m = month.toIntOrNull() ?: 1
            val d = day.toIntOrNull() ?: 1
            val currentYear = java.time.LocalDate.now().year
            val birthDate = java.time.LocalDate.of(
                y.coerceIn(1920, currentYear),
                m.coerceIn(1, 12),
                d.coerceIn(1, 31)
            )
            java.time.Period.between(birthDate, java.time.LocalDate.now()).years.coerceAtLeast(18)
        } catch (_: Exception) {
            24
        }
    }

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent6
    ) {
        DobHeader(scheme = scheme)
        DobInputFields(
            day = day,
            month = month,
            year = year,
            onDayChange = onDayChange,
            onMonthChange = onMonthChange,
            onYearChange = onYearChange,
            scheme = scheme
        )
        DobAgeBadge(
            calculatedAge = calculatedAge,
            scheme = scheme
        )
    }
}
