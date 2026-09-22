package com.telekom.odsystem.atoms.sliderinputfield

/**
 * Represents the properties for configuring an ODS (presumably "OD System") Slider Input Field component.
 *
 * This data class allows customization of the input field's current value,
 * an optional prefix for the input value, and its focus state.
 * It's typically used for components where a user can input a numeric value,
 * possibly in conjunction with a slider for selection.
 *
 * @property inputValue The current text value entered or displayed in the input field.
 *                      Can be `null` if the field is empty or uninitialized.
 * @property prefix An optional string to be displayed before the `inputValue` (e.g., a currency symbol, unit).
 *                  Can be `null` if no prefix is needed. Defaults to `null`.
 */
data class ODSSliderInputFieldProps(
    var inputValue: String? = null,
    var prefix: String? = null,
)
