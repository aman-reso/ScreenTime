package com.telekom.odsystem.organisms.slider

import com.telekom.odsystem.atoms.sliderinputfieldgroup.ODSSliderInputFieldGroupProps

/**
 * Data class representing the properties for configuring an ODS Slider component.
 *
 * An ODS Slider allows users to select a value from a continuous or discrete range of values.
 *
 * @property max The maximum value of the slider. Defaults to `null`, indicating no specific maximum.
 * @property min The minimum value of the slider. Defaults to `null`, indicating no specific minimum.
 * @property fractionDecimal The number of decimal places to display for the slider's value. Defaults to `0`.
 * @property inputFieldGroupProps Properties for configuring an optional input field group associated with the slider, allowing direct numeric input. Defaults to an [ODSSliderInputFieldGroupProps] instance with its default values.
 */
data class ODSSliderProps(
    var max: Float? = null,
    var min: Float? = null,
    var fractionDecimal: Int? = 0,
    var inputFieldGroupProps: ODSSliderInputFieldGroupProps = ODSSliderInputFieldGroupProps(),
)
