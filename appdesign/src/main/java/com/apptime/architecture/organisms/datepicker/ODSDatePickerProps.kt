package com.telekom.odsystem.organisms.datepicker

import com.telekom.odsystem.atoms.datepickerinputfield.ODSDatePickerInputFieldProps

/**
 * Properties configuring an ODS date picker component.
 *
 * @property expanded Controls the visibility of the date picker dialog.
 * @property datePickerInputFieldProps Optional properties for the input field.
 * @property startDate Initially selected start date in ISO format.
 * @property endDate Initially selected end date in ISO format.
 * @property shouldDisableDate Callback used to disable particular dates.
 */
data class ODSDatePickerProps(
    var expanded: Boolean = false,
    var datePickerInputFieldProps: ODSDatePickerInputFieldProps? = null,
    val startDate: String? = null, // Not exported from plugin
    val endDate: String? = null, // Not exported from plugin
    val shouldDisableDate: ((utcTimeMillis: Long) -> Boolean)? = null, // Not exported from plugin
)
