package com.app.screentime.record.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

import com.app.screentime.utils.DateUtils
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelect
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectOptions
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectProps
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectSize
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun DateSpinner(
    onDateSelected: (String) -> Unit = {}, scheme: ODSTheme = neutralScheme
) {

    val today = DateUtils.today()
    val dates = (0..2).map { today.minusDays(it) } // Only 3 days: today, yesterday, day before yesterday

    val dateDisplayMap = dates.mapIndexed { index, date ->
        val apiDate = date.toString("yyyy-MM-dd")
        val displayLabel = when (index) {
            0 -> "Today"
            1 -> "Yesterday"
            2 -> "Day before yesterday"
            else -> date.toString("d MMM")
        }
        apiDate to displayLabel
    }.toMap()

    var expanded by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(today.toString("yyyy-MM-dd")) }

    // Convert dates to ODSDropdownSelectOptions
    val options = remember(dates, dateDisplayMap) {
        dates.map { date ->
            val apiDate = date.toString("yyyy-MM-dd")
            val displayString = dateDisplayMap[apiDate] ?: ""
            ODSDropdownSelectOptions(
                label = displayString,
                id = apiDate
            )
        }
    }

    val selectedOption = remember(selectedDate, options) {
        options.find { it.id == selectedDate } ?: options.firstOrNull()
    }

    ODSDropdownSelect(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scaleY = 0.8f, scaleX = 1f),
        scheme = scheme,
        props = ODSDropdownSelectProps(
            expanded = expanded,
            selectedValue = selectedOption,
            options = options,
            size = ODSDropdownSelectSize.SMALL
        ),
        onClick = {
            expanded = !expanded
        },
        selectedOption = { option ->
            val apiDate = option.id
            selectedDate = apiDate
            expanded = false
            onDateSelected(apiDate)
        }
    )
}

