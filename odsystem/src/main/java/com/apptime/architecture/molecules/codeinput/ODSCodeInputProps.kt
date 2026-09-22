package com.telekom.odsystem.molecules.codeinput

import com.telekom.odsystem.atoms.inputitem.ODSInputItemMode
import com.telekom.odsystem.atoms.inputitem.ODSInputItemProps
import com.telekom.odsystem.foundations.DEFAULT_SUPPORT_TEXT_MESSAGE

enum class ODSCodeInputMode {
    /** Normal input mode. */
    STANDARD,

    /** Displays error styling. */
    ERROR,
}

/**
 * Properties configuring the ODS code input component.
 *
 * @property disabled Disables all input when true.
 * @property masked Hides entered characters.
 * @property readOnly Prevents user changes.
 * @property mode Display mode controlling colors and messages.
 * @property errorMessage Text shown when in error mode.
 * @property inputItems List of individual input item models.
 */
data class ODSCodeInputProps(
    var disabled: Boolean = false,
    var masked: Boolean = false,
    var readOnly: Boolean = false,
    var mode: ODSCodeInputMode = ODSCodeInputMode.STANDARD,
    var errorMessage: String = DEFAULT_SUPPORT_TEXT_MESSAGE,
    var inputItems: List<ODSInputItemModel> = listOf(),
)

/**
 * Model describing a single input slot.
 * @property placeHolder Placeholder text shown when empty.
 * @property inputText Deprecated. Use `inputText` instead.
 */
data class ODSInputItemModel(
    var inputText: String? = null,
    var placeHolder: String? = null,
) {
    fun toODSInputItemProps(props: ODSCodeInputProps): ODSInputItemProps {
        return ODSInputItemProps(
            disabled = props.disabled,
            inputText = inputText,
            masked = props.masked,
            mode = if (props.mode == ODSCodeInputMode.STANDARD) ODSInputItemMode.STANDARD else ODSInputItemMode.ERROR,
            placeholder = placeHolder,
            readOnly = props.readOnly
        )
    }
}
