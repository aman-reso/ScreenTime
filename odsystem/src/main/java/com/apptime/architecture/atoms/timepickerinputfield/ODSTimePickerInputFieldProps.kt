package com.telekom.odsystem.atoms.timepickerinputfield

import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageMode
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps
import java.util.WeakHashMap

/**
 * Defines the mode of the ODS time picker input field.
 */
enum class ODSTimePickerInputFieldMode {
    /**
     * The time picker input field is in an error state.
     */
    ERROR,

    /**
     * The time picker input field is in an informative state.
     */
    INFORMATIVE,

    /**
     * The time picker input field is in the standard state.
     */
    STANDARD,
}

/**
 * Defines the size of the ODS time picker input field.
 */
enum class ODSTimePickerInputFieldSize {
    /**
     * A large-sized time picker input field.
     */
    LARGE,

    /**
     * A small-sized time picker input field.
     */
    SMALL,
}

internal enum class ODSTimePickerInputFieldStatus {
    UNFILLED,
    EDITING,
    FILLED,
}

/**
 * Properties for configuring the support message of an ODS time picker input field.
 *
 * @property message The support message text to display.
 */
data class ODSTimePickerSupportMessageProps(
    var message: String? = null,
)

/**
 * Converts [ODSTimePickerSupportMessageProps] to [ODSSupportMessageProps].
 *
 * Not Exported from plugin
 * @param mode The mode of the time picker input field to determine the support message type.
 * @param disabled Indicates whether the time picker input field is disabled.
 * @return A new [ODSSupportMessageProps] instance with the converted properties.
 */
internal fun ODSTimePickerSupportMessageProps.toODSSupportMessageProps(
    mode: ODSTimePickerInputFieldMode,
    disabled: Boolean
): ODSSupportMessageProps {
    return ODSSupportMessageProps(
        helperText = this.message,
        disabled = disabled,
        mode = when (mode) {
            ODSTimePickerInputFieldMode.INFORMATIVE -> ODSSupportMessageMode.INFORMATIVE
            ODSTimePickerInputFieldMode.ERROR -> ODSSupportMessageMode.ERROR
            else -> ODSSupportMessageMode.INFORMATIVE
        },
    )
}

/**
 * Properties used to configure the appearance and behavior of an ODS time picker input field.
 *
 * Not Exported from plugin
 * @property disabled Indicates whether the time picker input field is disabled and non-interactive.
 * @property inputText The current input value of the time picker input field.
 * @property label The label text displayed above the time picker input field.
 * @property mode The mode of the time picker input field (e.g., standard, informative, error).
 * @property placeholderText The placeholder text displayed when the time picker input field is empty.
 * @property readOnly Indicates whether the time picker input field is read-only.
 * @property size The size of the time picker input field (e.g., large, small).
 * @property required Indicates whether the time picker input field is required for form submission.
 * @property supportMessageProps The properties for configuring the support message of the time picker input field.
 */
data class ODSTimePickerInputFieldProps(
    var disabled: Boolean = false,
    var inputText: String? = null,
    var label: String? = null,
    var mode: ODSTimePickerInputFieldMode = ODSTimePickerInputFieldMode.STANDARD,
    var placeholderText: String? = null,
    var readOnly: Boolean = false,
    var size: ODSTimePickerInputFieldSize = ODSTimePickerInputFieldSize.LARGE,
    var required: Boolean = false,
    var supportMessageProps: ODSTimePickerSupportMessageProps? = null, // Not exported from plugin
)

private val isFocusedStorage = WeakHashMap<ODSTimePickerInputFieldProps, Boolean>()

internal var ODSTimePickerInputFieldProps.isFocused: Boolean
    get() = isFocusedStorage[this] ?: false
    set(value) {
        isFocusedStorage[this] = value
    }

internal val ODSTimePickerInputFieldProps.status: ODSTimePickerInputFieldStatus
    get() = if (!inputText.isNullOrEmpty()) {
        ODSTimePickerInputFieldStatus.FILLED
    } else if (isFocused) {
        ODSTimePickerInputFieldStatus.EDITING
    } else {
        ODSTimePickerInputFieldStatus.UNFILLED
    }
