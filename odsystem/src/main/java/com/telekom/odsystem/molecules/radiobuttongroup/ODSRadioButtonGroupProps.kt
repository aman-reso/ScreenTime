package com.telekom.odsystem.molecules.radiobuttongroup

import com.telekom.odsystem.atoms.radiobutton.ODSRadioButtonMode
import com.telekom.odsystem.atoms.radiobutton.ODSRadioButtonProps
import com.telekom.odsystem.atoms.radiobutton.ODSRadioButtonSize
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps

enum class ODSRadioButtonGroupSize {
    /** Small radio buttons. */
    SMALL,

    /** Large radio buttons. */
    LARGE,
}

/**
 * Configuration for a single radio button within the group.
 *
 * @property label Text label displayed next to the radio button.
 * @property mode Visual mode of the radio button.
 * @property selected Whether the radio button is selected.
 * @property disabled Disables the control when true.
 * @property readOnly Prevents changes when true.
 */
data class ODSRadioButtonGroupRadioButtonProps(
    var label: String? = null,
    var mode: ODSRadioButtonMode = ODSRadioButtonMode.STANDARD,
    var selected: Boolean = false,
    var disabled: Boolean = false,
    var readOnly: Boolean = false,
)

internal fun ODSRadioButtonGroupRadioButtonProps.toODSRadioButtonProps(size: ODSRadioButtonGroupSize): ODSRadioButtonProps {
    return ODSRadioButtonProps(
        label = this.label,
        mode = this.mode,
        selected = this.selected,
        disabled = this.disabled,
        readOnly = this.readOnly,
        size = when (size) {
            ODSRadioButtonGroupSize.SMALL -> ODSRadioButtonSize.SMALL
            ODSRadioButtonGroupSize.LARGE -> ODSRadioButtonSize.LARGE
        }
    )
}

/**
 * Properties describing an ODS radio button group.
 *
 * @property size Size of each radio button.
 * @property titleText Optional title above the group.
 * @property supportMessageProps Support message shown below the group.
 * @property radioButtonProps Collection of individual radio buttons.
 */
data class ODSRadioButtonGroupProps(
    var size: ODSRadioButtonGroupSize = ODSRadioButtonGroupSize.LARGE,
    var titleText: String? = null,
    var supportMessageProps: ODSSupportMessageProps? = null,
    var radioButtonProps: List<ODSRadioButtonGroupRadioButtonProps>? = null
)
