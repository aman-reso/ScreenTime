package com.telekom.odsystem.atoms.controls

import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconProps
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconProps
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIconProps

/**
 * Defines the type of ODS controls.
 */
enum class ODSControlsType {
    /**
     * A radio button control.
     */
    RADIO_ICON,

    /**
     * A checkbox control.
     */
    CHECKBOX_ICON,

    /**
     * A switch control.
     */
    SWITCH_ICON,
}

/**
 * Properties used to configure the appearance and behavior of ODS controls.
 *
 * @property type The type of the control (e.g., radio, checkbox, switch).
 * @property switchIconProps The properties for configuring a switch control (if applicable).
 * @property checkboxIconProps The properties for configuring a checkbox control (if applicable).
 * @property radioIconProps The properties for configuring a radio button control (if applicable).
 */
data class ODSControlsProps(
    var type: ODSControlsType = ODSControlsType.SWITCH_ICON,
    var switchIconProps: ODSSwitchIconProps? = null,
    var checkboxIconProps: ODSCheckboxIconProps? = null,
    var radioIconProps: ODSRadioIconProps? = null
)
