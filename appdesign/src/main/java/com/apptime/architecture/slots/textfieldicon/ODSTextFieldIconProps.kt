package com.telekom.odsystem.slots.textfieldicon

import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Defines the type of container for the text field's icon.
 */
enum class ODSTextFieldIconType {
    ICON_CONTAINER,
    BUTTON_CONTAINER,
}

/**
 * Defines the size of the icon within an `ODSTextField`.
 * This size is applied whether the icon is a simple icon or part of a button.
 */
enum class ODSTextFieldIconSize {
    LARGE,
    SMALL,
}

/**
 * Properties for the icon button within an `ODSTextField`.
 *
 * This class defines the configuration for a button that can be displayed as a trailing icon
 * inside a text field. It allows for customization of the button's appearance and behavior.
 *
 * @property buttonIcon The icon to be displayed on the button. See [ODSIconModel].
 * @property variant The visual style of the button. See [ODSButtonVariant]. Defaults to [ODSButtonVariant.PRIMARY].
 * @property disabled If true, the button will be in a disabled state and will not be interactive. Defaults to `false`.
 */
data class ODSTextFieldIconButtonProps(
    var buttonIcon: ODSIconModel? = null,
    var variant: ODSButtonVariant = ODSButtonVariant.PRIMARY,
    var disabled: Boolean = false,
)

internal fun ODSTextFieldIconButtonProps.toODSButtonProps(): ODSButtonProps {
    return ODSButtonProps(
        buttonIcon = this.buttonIcon,
        variant = this.variant,
        buttonType = ODSButtonButtonType.ICON_ONLY,
        disabled = this.disabled,
        size = ODSButtonSize.SMALL,
    )
}

/**
 * Code generated with ODS RADD Code Generator
 * 2025-10-20 (v1.33.1) - uid: 929f428
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=146-12440
 */
/**
 * Defines the properties for an icon displayed within a text field.
 * This can be either a simple icon or a button with an icon.
 *
 * @property icon The model for the icon to be displayed. Used when `type` is `ICON_CONTAINER`.
 * @property type The type of the container for the icon, determining whether it's a simple icon or a button.
 * @property size The size of the icon container, affecting both the icon and the button size.
 * @property buttonProps The properties for the button, used only when `type` is `BUTTON_CONTAINER`.
 */
data class ODSTextFieldIconProps(
    var icon: ODSIconModel? = null,
    var type: ODSTextFieldIconType = ODSTextFieldIconType.ICON_CONTAINER,
    var size: ODSTextFieldIconSize = ODSTextFieldIconSize.LARGE,
    var buttonProps: ODSTextFieldIconButtonProps? = null,
)
