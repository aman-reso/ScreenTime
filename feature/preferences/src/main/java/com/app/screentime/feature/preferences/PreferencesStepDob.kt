package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R as ODSR
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelect
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectMode
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectOptions
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectProps
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectSupportMessageProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

val AGE_OPTIONS: List<Int> = (18..40).toList()

/**
 * Step 3: Age Preference.
 * Uses Telekom ODS ODSDropdownSelect launching an ODS BottomSheet age picker (18 to 40).
 * 100% Telekom ODS components.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesStepDob(
    ageYears: String,
    onAgeYearsChange: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    error: String? = null,
    // Deprecated parameters retained for backwards compatibility
    day: String = "",
    month: String = "",
    year: String = "",
    onDayChange: (String) -> Unit = {},
    onMonthChange: (String) -> Unit = {},
    onYearChange: (String) -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    val primaryTextColor = scheme.basicText
    val secondaryTextColor = scheme.basicTextRecessive

    var showBottomSheet by remember { mutableStateOf(false) }

    val displayLabel = if (ageYears.isBlank()) null else "$ageYears years old"

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = 28.dp,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // ── Heading & Subtitle ──────────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 8.dp,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "How old are you?",
                style = ODSTextStyles.bodyL,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Tap below to select your age.",
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        // ── Age Dropdown Select ──────────────────────────────────────────────
        ODSDropdownSelect(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSDropdownSelectProps(
                label = "Age",
                selectedValue = displayLabel?.let { ODSDropdownSelectOptions(label = it) },
                mode = if (!error.isNullOrBlank()) ODSDropdownSelectMode.ERROR else ODSDropdownSelectMode.STANDARD,
                supportMessageProps = error?.let { ODSDropdownSelectSupportMessageProps(message = it) }
            ),
            onClick = { showBottomSheet = true }
        )
    }

    // ── BottomSheet Age Picker ─────────────────────────────────────────
    if (showBottomSheet) {
        ODSBottomSheet(
            showBottomSheet = true,
            onDismissRequest = { showBottomSheet = false },
            onCloseClicked = { showBottomSheet = false },
            contentSlot = {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    gap = 12.dp
                ) {
                    ODSText(
                        text = "Select Age",
                        style = ODSTextStyles.bodyMBold,
                        color = primaryTextColor
                    )
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 4.dp
                    ) {
                        AGE_OPTIONS.forEachIndexed { idx, age ->
                            val isSelected = age.toString() == ageYears
                            ODSColumn(modifier = Modifier.fillMaxWidth()) {
                                ODSRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onAgeYearsChange(age.toString())
                                            showBottomSheet = false
                                        }
                                        .padding(vertical = 14.dp, horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ODSText(
                                        text = "$age years old",
                                        style = if (isSelected) ODSTextStyles.bodySBold else ODSTextStyles.bodySRegular,
                                        color = if (isSelected) primaryTextColor else secondaryTextColor
                                    )
                                    if (isSelected) {
                                        ODSIcon(
                                            iconModel = ODSIconModel(
                                                drawableRes = ODSR.drawable.ic_check,
                                                contentDescription = "Selected"
                                            ),
                                            tint = primaryTextColor.getColor(),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                if (idx < AGE_OPTIONS.size - 1) {
                                    ODSBox(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp),
                                        background = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
