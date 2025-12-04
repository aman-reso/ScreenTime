package com.telekom.odsystem.atoms.switchicon

import com.telekom.odsystem.foundations.ODSActions

/**
 * Defines the size variants for an [ODSSwitchIconProps].
 *
 * This enum controls the overall dimensions of the switch icon component.
 */
enum class ODSSwitchIconSize {
    /** A larger visual presentation for the switch icon. */
    LARGE,

    /** A smaller, more compact visual presentation for the switch icon. */
    SMALL,
}

/**
 * Represents the properties for configuring an ODS (presumably "OD System") Switch Icon component.
 *
 * This data class allows customization of the switch icon's state (disabled, selected),
 * its size, and its interaction state (e.g., default, hovered, pressed - though this is often
 * handled by the component internally based on user interaction).
 *
 * @property disabled Determines if the switch icon is interactive. If `true`, it cannot be toggled and will likely appear grayed out. Defaults to `false`.
 * @property selected Indicates the current toggle state of the switch icon. `true` if the switch is in the "on" or selected state, `false` otherwise. Defaults to `false`.
 * @property readOnly If `true`, the switch icon cannot be toggled by the user, but its state can still be visually represented. Defaults to `false`.
 * @property size The [ODSSwitchIconSize] that defines the visual size of the switch icon. Defaults to [ODSSwitchIconSize.LARGE].
 * @property state The current interaction state of the switch icon, typically an [ODSActions] enum value. This might be used to apply different visual styles for hover, press, etc. Defaults to [ODSActions.DEFAULT].
 */
data class ODSSwitchIconProps(
    var disabled: Boolean = false,
    var selected: Boolean = false,
    var readOnly: Boolean = false,
    var size: ODSSwitchIconSize = ODSSwitchIconSize.LARGE,
    var state: ODSActions = ODSActions.DEFAULT,
)
