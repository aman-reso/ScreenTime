package com.telekom.odsystem.atoms.inputitem

/**
 * Defines the mode of the ODS input item.
 */
enum class ODSInputItemMode {
    /**
     * The input item is in an error state.
     */
    ERROR,

    /**
     * The input item is in the standard state.
     */
    STANDARD
}

/**
 * Properties used to configure the appearance and behavior of an ODS input item.
 *
 * @property disabled Indicates whether the input item is disabled and non-interactive.
 * @property inputText The text displayed in the input item. If `inputValue` is set, it will be used as the text.
 * @property masked Indicates whether the input value should be masked (e.g., for passwords).
 * @property mode The mode of the input item (e.g., standard, error).
 * @property placeholder The placeholder text displayed when the input is empty.
 * @property readOnly Indicates whether the input item is read-only.
 * @property isFocused Indicates whether the input item is currently focused.
 */
data class ODSInputItemProps(
    var disabled: Boolean = false,
    var inputText: String? = null,
    var masked: Boolean = false,
    var mode: ODSInputItemMode = ODSInputItemMode.STANDARD,
    var placeholder: String? = null,
    var readOnly: Boolean = false,
    var isFocused: Boolean = false,
)
