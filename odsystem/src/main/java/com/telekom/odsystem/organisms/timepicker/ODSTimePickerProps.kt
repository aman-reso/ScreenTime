package com.telekom.odsystem.organisms.timepicker

import com.telekom.odsystem.atoms.timepickerinputfield.ODSTimePickerInputFieldProps

/**
 * Properties configuring a time picker composed of multiple fields.
 *
 * @property timePickerInputFieldProps Settings for the individual input fields.
 */
data class ODSTimePickerProps(
    var showTimePicker: Boolean = false,
    var timePickerInputFieldProps: ODSTimePickerInputFieldProps? = null
)
