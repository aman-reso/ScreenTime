package com.telekom.odsystem.atoms.radioicon

import com.telekom.odsystem.foundations.ODSActions

/**
 * Defines the size variants for an [ODSRadioIconProps].
 *
 * This enum controls the overall dimensions of the radio icon component.
 */
enum class ODSRadioIconSize {
    /** A larger visual presentation for the radio icon. */
    LARGE,

    /** A smaller, more compact visual presentation for the radio icon. */
    SMALL,
}

/**
 * Configuration properties for the ODS Radio Icon component.
 *
 * Allows customization of the radio icon's state (e.g., disabled, selected, error, read-only),
 * visual size, and interaction state. Typically used in groups where only one option is selected.
 *
 * @property disabled If `true`, the icon is non-interactive and appears grayed out. Default is `false`.
 * @property error If `true`, displays the icon in an error state (e.g., different color). Default is `false`.
 * @property selected Indicates if the icon is currently selected. Default is `false`.
 * @property readonly If `true`, selection state is fixed but still visible. Default is `false`.
 * @property size Specifies the visual size of the icon. Default is [ODSRadioIconSize.LARGE].
 * @property state The interaction state (e.g., hover, press) as [ODSActions]. Default is [ODSActions.DEFAULT].
 *                 Not exported from plugin—likely for internal use.
 */
data class ODSRadioIconProps(
    var disabled: Boolean = false,
    var error: Boolean = false,
    var selected: Boolean = false,
    var readonly: Boolean = false,
    var size: ODSRadioIconSize = ODSRadioIconSize.LARGE,
    var state: ODSActions = ODSActions.DEFAULT,
)
