package com.telekom.odsystem.atoms.checkbox

import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageMode
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps

/**
 * Defines the mode of the ODS checkbox.
 */
enum class ODSCheckboxMode {
    /**
     * A standard checkbox with no additional styling.
     */
    STANDARD,

    /**
     * A checkbox with an informative message.
     */
    INFORMATIVE,

    /**
     * A checkbox with an error message.
     */
    ERROR,
}

/**
 * Defines the selection state of the ODS checkbox.
 */
enum class ODSCheckboxSelected {
    /**
     * The checkbox is unselected.
     */
    UNSELECTED,

    /**
     * The checkbox is selected.
     */
    SELECTED,

    /**
     * The checkbox is in an indeterminate state.
     */
    INDETERMINATE,
}

/**
 * Defines the size of the ODS checkbox.
 */
enum class ODSCheckboxSize {
    /**
     * A small-sized checkbox.
     */
    SMALL,

    /**
     * A large-sized checkbox.
     */
    LARGE,
}

/**
 * Properties for configuring the support message of an ODS checkbox.
 *
 * @property message The support message text to display.
 */
data class ODSCheckboxSupportMessageProps(
    var message: String? = null,
)

/**
 * Converts [ODSCheckboxSupportMessageProps] to [ODSSupportMessageProps].
 *
 * Not Exported from plugin
 * @param mode The mode of the checkbox to determine the support message type.
 * @param disabled Indicates whether the checkbox is disabled.
 * @return A new [ODSSupportMessageProps] instance with the converted properties.
 */
internal fun ODSCheckboxSupportMessageProps.toODSSupportMessageProps(
    mode: ODSCheckboxMode,
    disabled: Boolean
): ODSSupportMessageProps {
    return ODSSupportMessageProps(
        helperText = this.message,
        mode = when (mode) {
            ODSCheckboxMode.INFORMATIVE -> ODSSupportMessageMode.INFORMATIVE
            ODSCheckboxMode.ERROR -> ODSSupportMessageMode.ERROR
            else -> ODSSupportMessageMode.INFORMATIVE
        },
        disabled = disabled
    )
}

/**
 * Properties used to configure the appearance and behavior of an ODS checkbox.
 *
 * @property disabled Indicates whether the checkbox is disabled and non-interactive.
 * @property label The label text displayed next to the checkbox.
 * @property mode The mode of the checkbox (e.g., standard, informative, error).
 * @property readOnly Indicates whether the checkbox is read-only.
 * @property selected The selection state of the checkbox (e.g., selected, unselected, indeterminate).
 * @property size The size of the checkbox (e.g., small, large).
 * @property supportMessageProps The properties for configuring the support message of the checkbox.
 */
data class ODSCheckboxProps(
    var disabled: Boolean = false,
    var label: String? = null,
    var mode: ODSCheckboxMode = ODSCheckboxMode.STANDARD,
    var readOnly: Boolean = false,
    var selected: ODSCheckboxSelected = ODSCheckboxSelected.UNSELECTED,
    var size: ODSCheckboxSize = ODSCheckboxSize.LARGE,
    var supportMessageProps: ODSCheckboxSupportMessageProps? = null
)
