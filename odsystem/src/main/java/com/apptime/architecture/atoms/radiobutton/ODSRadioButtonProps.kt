package com.telekom.odsystem.atoms.radiobutton

import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageMode
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps

/**
 * Defines the mode of the ODS radio button.
 */
enum class ODSRadioButtonMode {
    /**
     * A standard radio button with no additional styling.
     */
    STANDARD,

    /**
     * A radio button with an informative message.
     */
    INFORMATIVE,

    /**
     * A radio button with an error message.
     */
    ERROR,
}

/**
 * Defines the size of the ODS radio button.
 */
enum class ODSRadioButtonSize {
    /**
     * A large-sized radio button.
     */
    LARGE,

    /**
     * A small-sized radio button.
     */
    SMALL,
}

/**
 * Properties for configuring the support message of an ODS radio button.
 *
 * @property message The support message text to display.
 */
data class ODSRadioButtonSupportMessageProps(
    var message: String? = null,
)

/**
 * Converts [ODSRadioButtonSupportMessageProps] to [ODSSupportMessageProps].
 *
 * Not Exported from plugin
 * @param mode The mode of the radio button to determine the support message type.
 * @param disabled Indicates whether the radio button is disabled.
 * @return A new [ODSSupportMessageProps] instance with the converted properties.
 */
internal fun ODSRadioButtonSupportMessageProps.toODSSupportMessageProps(
    mode: ODSRadioButtonMode,
    disabled: Boolean
): ODSSupportMessageProps {
    return ODSSupportMessageProps(
        helperText = this.message,
        mode = when (mode) {
            ODSRadioButtonMode.INFORMATIVE -> ODSSupportMessageMode.INFORMATIVE
            ODSRadioButtonMode.ERROR -> ODSSupportMessageMode.ERROR
            else -> ODSSupportMessageMode.INFORMATIVE
        },
        disabled = disabled
    )
}

/**
 * Properties used to configure the appearance and behavior of an ODS radio button.
 *
 * @property disabled Indicates whether the radio button is disabled and non-interactive.
 * @property label The label text displayed next to the radio button.
 * @property mode The mode of the radio button (e.g., standard, informative, error).
 * @property readOnly Indicates whether the radio button is read-only.
 * @property selected Indicates whether the radio button is selected.
 * @property size The size of the radio button (e.g., large, small).
 * @property supportMessageProps The properties for configuring the support message of the radio button.
 */
data class ODSRadioButtonProps(
    var disabled: Boolean = false,
    var label: String? = null,
    var mode: ODSRadioButtonMode = ODSRadioButtonMode.STANDARD,
    var readOnly: Boolean = false,
    var selected: Boolean = false,
    var size: ODSRadioButtonSize = ODSRadioButtonSize.LARGE,
    var supportMessageProps: ODSRadioButtonSupportMessageProps? = null
)
