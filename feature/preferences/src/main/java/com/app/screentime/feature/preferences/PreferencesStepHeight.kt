package com.app.screentime.feature.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R as ODSR
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
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelect
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectMode
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectOptions
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectProps
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectSupportMessageProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.ODSVariables

data class HeightFeetOption(val feet: Int, val label: String)
data class HeightInchOption(val inch: Int, val label: String)

val FEET_OPTIONS: List<HeightFeetOption> = listOf(
    HeightFeetOption(4, "4 ft"),
    HeightFeetOption(5, "5 ft"),
    HeightFeetOption(6, "6 ft"),
    HeightFeetOption(7, "7 ft")
)

val INCH_OPTIONS: List<HeightInchOption> = (0..11).map { inch ->
    HeightInchOption(inch, "$inch in")
}

/**
 * Step 4: Height Preference.
 * Uses Telekom ODS ODSDropdownSelect flyouts for Feet and Inches (4 ft to 7 ft, 0 in to 11 in).
 * 100% Telekom ODS components conforming to GEMINI.md design system rules.
 */
@Composable
fun PreferencesStepHeight(
    heightCm: String,
    onHeightChange: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    val primaryTextColor = scheme.basicText
    val secondaryTextColor = scheme.basicTextRecessive
    val accentColor = scheme.basicAccent

    val cmNum = heightCm.toIntOrNull()
    val totalInches = if (cmNum != null && cmNum > 0) {
        kotlin.math.round(cmNum / 2.54).toInt()
    } else null

    val currentFeet = totalInches?.let { (it / 12).coerceIn(4, 7) }
    val currentInches = totalInches?.let { (it % 12).coerceIn(0, 11) }

    var isFeetExpanded by remember { mutableStateOf(false) }
    var isInchExpanded by remember { mutableStateOf(false) }

    val feetDropdownOptions = remember {
        FEET_OPTIONS.map { ODSDropdownSelectOptions(label = it.label) }
    }
    val inchDropdownOptions = remember {
        INCH_OPTIONS.map { ODSDropdownSelectOptions(label = it.label) }
    }

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = 24.dp,
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
                style = ODSTextStyles.bodyMBold,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Select your height in feet and inches.",
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        // ── 2. Feet & Inches ODSDropdownSelect Flyouts ─────────────────────
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = 12.dp,
            verticalAlignment = Alignment.Top
        ) {
            // Feet Flyout Dropdown
            ODSDropdownSelect(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = ODSDropdownSelectProps(
                    label = "Feet",
                    expanded = isFeetExpanded,
                    selectedValue = currentFeet?.let { ODSDropdownSelectOptions(label = "$it ft") },
                    options = feetDropdownOptions,
                    mode = if (error != null) ODSDropdownSelectMode.ERROR else ODSDropdownSelectMode.STANDARD,
                    supportMessageProps = error?.let { ODSDropdownSelectSupportMessageProps(message = it) }
                ),
                onClick = {
                    isFeetExpanded = !isFeetExpanded
                    isInchExpanded = false
                },
                selectedOption = { option ->
                    val selectedFeetVal = option.label?.filter { it.isDigit() }?.toIntOrNull() ?: 5
                    val currentInchVal = currentInches ?: 0
                    val newTotalInches = (selectedFeetVal * 12) + currentInchVal
                    val newCm = kotlin.math.round(newTotalInches * 2.54).toInt()
                    onHeightChange(newCm.toString())
                    isFeetExpanded = false
                }
            )

            // Inches Flyout Dropdown
            ODSDropdownSelect(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = ODSDropdownSelectProps(
                    label = "Inches",
                    expanded = isInchExpanded,
                    selectedValue = currentInches?.let { ODSDropdownSelectOptions(label = "$it in") },
                    options = inchDropdownOptions,
                    mode = if (error != null) ODSDropdownSelectMode.ERROR else ODSDropdownSelectMode.STANDARD
                ),
                onClick = {
                    isInchExpanded = !isInchExpanded
                    isFeetExpanded = false
                },
                selectedOption = { option ->
                    val selectedInchVal = option.label?.filter { it.isDigit() }?.toIntOrNull() ?: 0
                    val currentFeetVal = currentFeet ?: 5
                    val newTotalInches = (currentFeetVal * 12) + selectedInchVal
                    val newCm = kotlin.math.round(newTotalInches * 2.54).toInt()
                    onHeightChange(newCm.toString())
                    isInchExpanded = false
                }
            )
        }

        // ── 3. Height Preview Confirmation Badge ───────────────────────────
        if (currentFeet != null && currentInches != null && cmNum != null && cmNum in 80..250) {
            ODSBox(
                modifier = Modifier.fillMaxWidth(),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                ),
                padding = ODSPadding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSRow(
                        gap = 8.dp,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = ODSR.drawable.ic_check,
                                contentDescription = "Selected Height"
                            ),
                            tint = accentColor.getColor(),
                            modifier = Modifier.size(16.dp)
                        )
                        ODSText(
                            text = "Selected Height",
                            style = ODSTextStyles.bodySRegular,
                            color = secondaryTextColor
                        )
                    }
                    ODSText(
                        text = "$currentFeet ft $currentInches in  •  $cmNum cm",
                        style = ODSTextStyles.bodyMBold,
                        color = accentColor
                    )
                }
            }
        }
    }
}
