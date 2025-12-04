package com.telekom.odsystem.atoms.textfield

import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageMode
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps
import com.telekom.odsystem.slots.textfieldicon.ODSTextFieldIconButtonProps
import com.telekom.odsystem.slots.textfieldicon.ODSTextFieldIconProps
import com.telekom.odsystem.slots.textfieldicon.ODSTextFieldIconSize
import com.telekom.odsystem.slots.textfieldicon.ODSTextFieldIconType
import java.util.WeakHashMap

/**
 * Defines the mode of the ODS text field.
 */
enum class ODSTextFieldMode {
    /**
     * A standard text field with no additional styling.
     */
    STANDARD,

    /**
     * A text field with an informative message.
     */
    INFORMATIVE,

    /**
     * A text field with an error message.
     */
    ERROR,
}

/**
 * Defines the size of the ODS text field.
 */
enum class ODSTextFieldSize {
    /**
     * A large-sized text field.
     */
    LARGE,

    /**
     * A small-sized text field.
     */
    SMALL,
}

/**
 * Properties for configuring the support message of an ODS text field.
 *
 * @property message The support message text to display.
 */
data class ODSTextFieldSupportMessageProps(
    var message: String? = null,
)

/**
 * Properties describing an icon displayed inside a text field.
 *
 * @property icon icon shown inside the field
 * @property type container type for the icon
 * @property buttonProps properties for an interactive icon button
 */
data class ODSTextFieldTextFieldIconProps(
    var icon: ODSIconModel? = null,
    var type: ODSTextFieldIconType = ODSTextFieldIconType.ICON_CONTAINER,
    var buttonProps: ODSTextFieldIconButtonProps? = null,
)

internal fun ODSTextFieldTextFieldIconProps.toODSTextFieldIconProps(size: ODSTextFieldSize): ODSTextFieldIconProps {
    return ODSTextFieldIconProps(
        type = this.type,
        icon = this.icon,
        buttonProps = this.buttonProps,
        size = when (size) {
            ODSTextFieldSize.SMALL -> ODSTextFieldIconSize.SMALL
            ODSTextFieldSize.LARGE -> ODSTextFieldIconSize.LARGE
        },
    )
}

/**
 * Converts [ODSTextFieldSupportMessageProps] to [ODSSupportMessageProps].
 *
 * Not Exported from plugin
 * @param mode The mode of the text field to determine the support message type.
 * @param disabled Indicates whether the text field is disabled.
 * @return A new [ODSSupportMessageProps] instance with the converted properties.
 */
internal fun ODSTextFieldSupportMessageProps.toODSSupportMessageProps(
    mode: ODSTextFieldMode,
    disabled: Boolean,
): ODSSupportMessageProps {
    return ODSSupportMessageProps(
        helperText = this.message,
        mode = when (mode) {
            ODSTextFieldMode.INFORMATIVE -> ODSSupportMessageMode.INFORMATIVE
            ODSTextFieldMode.ERROR -> ODSSupportMessageMode.ERROR
            else -> ODSSupportMessageMode.INFORMATIVE
        },
        disabled = disabled
    )
}

/**
 * Properties used to configure the appearance and behavior of an ODS text field.
 *
 * @property counterText The counter value displayed in the text field (if applicable).
 * @property disabled Indicates whether the text field is disabled and non-interactive.
 * @property inputText The current input value of the text field.
 * @property leftIcon The icon displayed to the left of the text field (if applicable).
 * @property mode The mode of the text field (e.g., standard, informative, error).
 * @property placeholderText The placeholder text displayed when the text field is empty.
 * @property prefixText The prefix text displayed inside the text field (if applicable).
 * @property readOnly Indicates whether the text field is read-only.
 * @property required Indicates whether the text field is required.
 * @property rightIcon The icon displayed to the right of the text field (if applicable). Deprecated in favor of using [textFieldIconProps] with type [ODSTextFieldIconType.ICON_CONTAINER].
 * @property size The size of the text field (e.g., large, small).
 * @property suffixText The suffix text displayed inside the text field (if applicable).
 * @property isPasswordField Indicates whether the text field is a password field. Deprecated in favor of using [textFieldIconProps] with type [ODSTextFieldIconType.BUTTON_CONTAINER].
 * @property hidePassword Indicates whether the password should be hidden (masked).
 * @property supportMessageProps The properties for configuring the support message of the text field.
 * @property label The label text displayed above the text field (if applicable).
 * @property showRightIcon Indicates whether to show the right icon in the text field.
 * @property textFieldIconProps The properties describing the icon or the button displayed inside the text field.
 * @property showCounter Indicates whether to show a character counter in the text field.
 */
data class ODSTextFieldProps(
    var counterText: String? = null,
    var disabled: Boolean = false,
    var inputText: String? = null,
    var label: String? = null,
    var leftIcon: ODSIconModel? = null,
    var mode: ODSTextFieldMode = ODSTextFieldMode.STANDARD,
    var placeholderText: String? = null,
    var prefixText: String? = null,
    var readOnly: Boolean = false,
    var required: Boolean = false,
    @Deprecated("Use textFieldIconProps with type ICON_CONTAINER instead with showRightIcon true")
    var rightIcon: ODSIconModel? = null,
    var size: ODSTextFieldSize = ODSTextFieldSize.LARGE,
    var suffixText: String? = null,
    @Deprecated("Use textFieldIconProps with type BUTTON_CONTAINER instead with showRightIcon true")
    var isPasswordField: Boolean = false,
    var hidePassword: Boolean = false,
    var supportMessageProps: ODSTextFieldSupportMessageProps? = null,
    var showRightIcon: Boolean = false,
    var textFieldIconProps: ODSTextFieldTextFieldIconProps? = null,
    var showCounter: Boolean = false,
)

internal val ODSTextFieldProps.filled: Boolean
    get() = !inputText.isNullOrEmpty() || isFocused

internal val ODSTextFieldProps.showPlaceholder: Boolean
    get() = !placeholderText.isNullOrEmpty() && inputText.isNullOrBlank()

private val isFocusedStorage = WeakHashMap<ODSTextFieldProps, Boolean>()

internal var ODSTextFieldProps.isFocused: Boolean
    get() = isFocusedStorage[this] ?: false
    set(value) {
        isFocusedStorage[this] = value
    }
