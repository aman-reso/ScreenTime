package com.telekom.odsystem.atoms.datepickerinputfield

import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageMode
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps
import java.util.WeakHashMap

/**
 * Defines the mode of the ODS date picker input field.
 */
enum class ODSDatePickerInputFieldMode {
    /**
     * The date picker input field is in an error state.
     */
    ERROR,

    /**
     * The date picker input field is in an informative state.
     */
    INFORMATIVE,

    /**
     * The date picker input field is in the standard state.
     */
    STANDARD,
}

/**
 * Defines the size of the ODS date picker input field.
 */
enum class ODSDatePickerInputFieldSize {
    /**
     * A large-sized date picker input field.
     */
    LARGE,

    /**
     * A small-sized date picker input field.
     */
    SMALL,
}

internal enum class ODSDatePickerInputFieldStatus {
    UNFILLED,
    EDITING,
    FILLED,
}

/**
 * Properties for configuring the support message of an ODS date picker input field.
 *
 * @property message The support message text to display.
 */
data class ODSDatePickerSupportMessageProps(
    var message: String? = null,
)

/**
 * Converts [ODSDatePickerSupportMessageProps] to [ODSSupportMessageProps].
 *
 * Not Exported from plugin
 * @param mode The mode of the date picker input field to determine the support message type.
 * @param disabled Indicates whether the date picker input field is disabled.
 * @return A new [ODSSupportMessageProps] instance with the converted properties.
 */
internal fun ODSDatePickerSupportMessageProps.toODSSupportMessageProps(
    mode: ODSDatePickerInputFieldMode,
    disabled: Boolean
): ODSSupportMessageProps {
    return ODSSupportMessageProps(
        helperText = this.message,
        disabled = disabled,
        mode = when (mode) {
            ODSDatePickerInputFieldMode.ERROR -> ODSSupportMessageMode.ERROR
            ODSDatePickerInputFieldMode.STANDARD,
            ODSDatePickerInputFieldMode.INFORMATIVE -> ODSSupportMessageMode.INFORMATIVE
        }
    )
}

/**
 * Properties used to configure the appearance and behavior of an ODS date picker input field.
 *
 * @property dateFormat The format of the date displayed in the input field (e.g., "dd.MM.yyyy"). Not exported from plugin
 * @property disabled Indicates whether the date picker input field is disabled and non-interactive.
 * @property inputText The current input value of the date picker input field.
 * @property label The label text displayed above the date picker input field.
 * @property mode The mode of the date picker input field (e.g., standard, informative, error).
 * @property placeholderText The placeholder text displayed when the date picker input field is empty.
 * @property readOnly Indicates whether the date picker input field is read-only.
 * @property size The size of the date picker input field (e.g., large, small).
 * @property supportMessageProps The properties for configuring the support message of the date picker input field.
 * @property required Indicates whether the date picker input field is required for form submission.
 */
data class ODSDatePickerInputFieldProps(
    var dateFormat: String = "dd.MM.yyyy",
    var disabled: Boolean = false,
    var inputText: String? = null,
    var label: String? = null,
    var mode: ODSDatePickerInputFieldMode = ODSDatePickerInputFieldMode.STANDARD,
    var placeholderText: String? = null,
    var readOnly: Boolean = false,
    var size: ODSDatePickerInputFieldSize = ODSDatePickerInputFieldSize.LARGE,
    var supportMessageProps: ODSDatePickerSupportMessageProps? = null,
    var required: Boolean = false,
)

private val isFocusedStorage = WeakHashMap<ODSDatePickerInputFieldProps, Boolean>()

internal var ODSDatePickerInputFieldProps.isFocused: Boolean
    get() = isFocusedStorage[this] ?: false
    set(value) {
        isFocusedStorage[this] = value
    }

internal val ODSDatePickerInputFieldProps.status: ODSDatePickerInputFieldStatus
    get() = if (!inputText.isNullOrEmpty()) {
        ODSDatePickerInputFieldStatus.FILLED
    } else if (isFocused) {
        ODSDatePickerInputFieldStatus.EDITING
    } else {
        ODSDatePickerInputFieldStatus.UNFILLED
    }
