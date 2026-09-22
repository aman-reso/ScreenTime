package com.telekom.odsystem.atoms.sliderinputfieldgroup

import com.telekom.odsystem.atoms.sliderinputfield.ODSSliderInputFieldProps

/**
 * Defines the layout variants for an [ODSSliderInputFieldGroup].
 *
 * This enum determines how the min and max slider input fields are arranged within the group.
 */
enum class ODSSliderInputFieldGroupVariant {
    /** Arranges the min and max input fields horizontally, side by side. */
    SIDE_BY_SIDE,
    /** Arranges the min and max input fields vertically, stacked one above the other. */
    STACKED,
    /** Displays a single input field, typically used when only one bound (min or max) is relevant. */
    SINGLE,
}

/**
 * Represents the properties for configuring an ODS Slider Input Field Group component.
 *
 * This component groups together input fields, often used in conjunction with a slider
 * to display and allow direct manipulation of its minimum and maximum values.
 *
 * @property labelMin Optional label for the minimum value input field.
 * @property labelMax Optional label for the maximum value input field.
 * @property variant The [ODSSliderInputFieldGroupVariant] that defines the layout of the input fields.
 *                   Defaults to [ODSSliderInputFieldGroupVariant.SIDE_BY_SIDE].
 * @property sliderInputMaxProps Optional properties for configuring the maximum value slider input field.
 *                             See [ODSSliderInputFieldProps].
 * @property sliderInputMinProps Optional properties for configuring the minimum value slider input field.
 *                             See [ODSSliderInputFieldProps].
 */
data class ODSSliderInputFieldGroupProps(
    var labelMin: String? = null,
    var labelMax: String? = null,
    var variant: ODSSliderInputFieldGroupVariant = ODSSliderInputFieldGroupVariant.SIDE_BY_SIDE,
    var sliderInputMaxProps: ODSSliderInputFieldProps? = null,
    var sliderInputMinProps: ODSSliderInputFieldProps? = null
)
