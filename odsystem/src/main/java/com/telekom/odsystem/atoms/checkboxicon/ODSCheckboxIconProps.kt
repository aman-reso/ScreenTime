package com.telekom.odsystem.atoms.checkboxicon

import com.telekom.odsystem.foundations.ODSActions

/**
 * Defines the selection state of the ODS checkbox icon.
 */
enum class ODSCheckboxIconSelected {
    /**
     * The checkbox icon is in an indeterminate state.
     */
    INDETERMINATE,

    /**
     * The checkbox icon is selected.
     */
    SELECTED,

    /**
     * The checkbox icon is unselected.
     */
    UNSELECTED,
}

/**
 * Defines the size of the ODS checkbox icon.
 */
enum class ODSCheckboxIconSize {
    /**
     * A large-sized checkbox icon.
     */
    LARGE,

    /**
     * A small-sized checkbox icon.
     */
    SMALL,
}

/**
 * Properties used to configure the appearance and behavior of an ODS checkbox icon.
 *
 * @property disabled Indicates whether the checkbox icon is disabled and non-interactive.
 * @property error Indicates whether the checkbox icon is in an error state.
 * @property readOnly Indicates whether the checkbox icon is read-only.
 * @property selected The selection state of the checkbox icon (e.g., selected, unselected, indeterminate).
 * @property size The size of the checkbox icon (e.g., large, small).
 * @property state The state of the checkbox icon (e.g., default, active, disabled).
 */
data class ODSCheckboxIconProps(
    var disabled: Boolean = false,
    var error: Boolean = false,
    var readOnly: Boolean = false,
    var selected: ODSCheckboxIconSelected = ODSCheckboxIconSelected.UNSELECTED,
    var size: ODSCheckboxIconSize = ODSCheckboxIconSize.LARGE,
    var state: ODSActions = ODSActions.DEFAULT,
)
