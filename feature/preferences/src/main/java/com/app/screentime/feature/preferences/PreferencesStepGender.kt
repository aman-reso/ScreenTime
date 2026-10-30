package com.app.screentime.feature.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelect
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectMode
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectOptions
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectProps
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectSupportMessageProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

val DEFAULT_GENDER_SELECTIONS = listOf(
    "Male",
    "Female",
    "Non-binary"
)

/**
 * Step 2: Gender Preference.
 * Uses Telekom ODS ODSDropdownSelect flyout component instead of radio buttons.
 * 100% Telekom ODS components.
 */
@Composable
fun PreferencesStepGender(
    selectedGender: String,
    onGenderSelect: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    genders: List<String> = DEFAULT_GENDER_SELECTIONS,
    error: String? = null
) {
    val primaryTextColor = scheme.basicText
    val secondaryTextColor = scheme.basicTextRecessive

    var isExpanded by remember { mutableStateOf(false) }

    val dropdownOptions = remember(genders) {
        genders.map { gender ->
            ODSDropdownSelectOptions(label = gender)
        }
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
                text = "What's your gender?",
                style = ODSTextStyles.bodyL,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Select the option that best describes you.",
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        // ── 2. Gender Dropdown / Flyout Select ──────────────────────────────
        ODSDropdownSelect(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSDropdownSelectProps(
                label = "Gender",
                expanded = isExpanded,
                selectedValue = if (selectedGender.isBlank()) null else ODSDropdownSelectOptions(label = selectedGender),
                options = dropdownOptions,
                mode = if (!error.isNullOrBlank()) ODSDropdownSelectMode.ERROR else ODSDropdownSelectMode.STANDARD,
                supportMessageProps = error?.let { ODSDropdownSelectSupportMessageProps(message = it) }
            ),
            onClick = { isExpanded = !isExpanded },
            selectedOption = { option ->
                option.label?.let { onGenderSelect(it) }
                isExpanded = false
            }
        )
    }
}
