package com.telekom.odsystem.atoms.switch

/**
 * Defines the size of the ODS switch.
 */
enum class ODSSwitchSize {
    /**
     * A large-sized switch.
     */
    LARGE,

    /**
     * A small-sized switch.
     */
    SMALL,
}

/**
 * Properties used to configure the appearance and behavior of an ODS switch.
 *
 * @property disabled Indicates whether the switch is disabled and non-interactive.
 * @property label The label text displayed next to the switch.
 * @property readOnly If `true`, the switch is in a read-only state, meaning it cannot be toggled but its state is visible.
 * @property selected Indicates whether the switch is in the "on" (selected) state.
 * @property size The size of the switch (e.g., large, small).
 */
data class ODSSwitchProps(
    var disabled: Boolean = false,
    var label: String? = null,
    var readOnly: Boolean = false,
    var selected: Boolean = false,
    var size: ODSSwitchSize = ODSSwitchSize.LARGE,
)
