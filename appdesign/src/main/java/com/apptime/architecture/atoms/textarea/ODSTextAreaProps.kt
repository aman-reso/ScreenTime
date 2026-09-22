package com.telekom.odsystem.atoms.textarea

import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageMode
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps
import java.util.WeakHashMap

/**
 * Defines the visual modes for an [ODSTextAreaProps].
 *
 * This enum is used to control the appearance of the text area,
 * often to indicate its current state or the nature of its content.
 */
enum class ODSTextAreaMode {
    /** Standard appearance for the text area. */
    STANDARD,

    /** Informative appearance, possibly to highlight helpful text or suggestions. */
    INFORMATIVE,

    /** Error appearance, used to indicate validation failures or other issues. */
    ERROR,
}

/**
 * Defines the size variants for an [ODSTextAreaProps].
 *
 * This enum controls the overall dimensions and possibly internal padding
 * or font size of the text area.
 */
enum class ODSTextAreaSize {
    /** A larger visual presentation for the text area. */
    LARGE,

    /** A smaller, more compact visual presentation for the text area. */
    SMALL,
}

/**
 * Represents the properties for a support message associated with an [ODSTextAreaProps].
 *
 * This is primarily used to display auxiliary information or error messages
 * beneath the text area.
 *
 * @property message The text content of the support message. Can be `null` if no message is needed.
 */
data class ODSTextAreaSupportMessageProps(
    var message: String? = null,
)

/**
 * Converts [ODSTextAreaSupportMessageProps] to the more generic [ODSSupportMessageProps].
 *
 * This extension function facilitates the transformation of text area specific support message
 * properties into a common support message format, likely used by a shared support message component.
 * The type of the resulting [ODSSupportMessageProps] is determined by the [mode] of the text area.
 *
 * @param mode The current [ODSTextAreaMode] of the text area, which influences the type of the support message.
 * @param disabled A boolean indicating whether the support message should appear disabled.
 * @return An [ODSSupportMessageProps] instance configured based on the receiver's properties,
 *         the provided [mode], and [disabled] state.
 */
internal fun ODSTextAreaSupportMessageProps.toODSSupportMessageProps(
    mode: ODSTextAreaMode,
    disabled: Boolean
): ODSSupportMessageProps {
    return ODSSupportMessageProps(
        helperText = this.message,
        mode = when (mode) {
            ODSTextAreaMode.INFORMATIVE -> ODSSupportMessageMode.INFORMATIVE
            ODSTextAreaMode.ERROR -> ODSSupportMessageMode.ERROR
            else -> ODSSupportMessageMode.INFORMATIVE
        },
        disabled = disabled
    )
}

/**
 * Represents the complete set of properties for configuring an ODS (presumably "OD System") Text Area component.
 *
 * This data class allows comprehensive customization of the text area's appearance,
 * behavior, and associated elements like labels, counters, and support messages.
 *
 * @property disabled Determines if the text area is interactive. If `true`, input is blocked. Defaults to `false`.
 * @property inputText The current text content within the text area. Can be `null`.
 * @property labelText The text for the label associated with the text area. Can be `null`.
 * @property counterText The current count, typically used for character counting. Can be `null`.
 *                      It's an [Int] but might represent the current length of `inputText`.
 * @property readOnly If `true`, the text area content cannot be modified by the user but can still be selected. Defaults to `false`.
 * @property required If `true`, indicates that input in this text area is mandatory. Defaults to `false`.
 *                     This might affect styling (e.g., adding an asterisk to the label).
 * @property mode The visual mode of the text area, affecting its styling (e.g., for errors). Defaults to [ODSTextAreaMode.STANDARD].
 * @property size The size variant of the text area. Defaults to [ODSTextAreaSize.LARGE].
 * @property supportMessageProps Properties for an optional support message displayed below the text area.
 *                               Can be `null` if no support message is needed.
 */
data class ODSTextAreaProps(
    var disabled: Boolean = false,
    var inputText: String? = null,
    var labelText: String? = null,
    var counterText: Int? = null,
    var readOnly: Boolean = false,
    var required: Boolean = false,
    var mode: ODSTextAreaMode = ODSTextAreaMode.STANDARD,
    var size: ODSTextAreaSize = ODSTextAreaSize.LARGE,
    var supportMessageProps: ODSTextAreaSupportMessageProps? = null
)

internal val ODSTextAreaProps.filled: Boolean
    get() = !inputText.isNullOrEmpty() || isFocused

private val isFocusedStorage = WeakHashMap<ODSTextAreaProps, Boolean>()

internal var ODSTextAreaProps.isFocused: Boolean
    get() = isFocusedStorage[this] ?: false
    set(value) {
        isFocusedStorage[this] = value
    }
