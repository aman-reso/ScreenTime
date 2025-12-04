package com.telekom.odsystem.molecules.flyoutmenu

import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import java.util.UUID

enum class ODSFlyoutMenuMenuSize {
    /** Full-size menu button. */
    LARGE,

    /** Compact menu button. */
    SMALL,
}

/**
 * Properties configuring the trigger button of a flyout menu.
 *
 * @property buttonIcon Icon displayed on the button.
 * @property variant Visual variant of the button.
 * @property size Button size.
 * @property disabled Disables the button when true.
 */
data class ODSFlyoutMenuButtonProps(
    var buttonIcon: ODSIconModel? = null,
    var variant: ODSButtonVariant = ODSButtonVariant.PRIMARY,
    var size: ODSButtonSize = ODSButtonSize.LARGE,
    var disabled: Boolean = false,
)

fun ODSFlyoutMenuButtonProps.toODSButtonProps(): ODSButtonProps {
    return ODSButtonProps(
        buttonIcon = this.buttonIcon,
        variant = this.variant,
        size = this.size,
        disabled = this.disabled,
        buttonType = ODSButtonButtonType.ICON_ONLY
    )
}

/**
 * Model describing an option inside the flyout menu.
 *
 * @property id Unique identifier for state handling.
 * @property iconAfter Icon displayed after the label.
 * @property iconBefore Icon displayed before the label.
 * @property label Option label text.
 * @property helperText Supporting text shown below the label.
 * @property disabled Disables the option when true.
 */
data class ODSFlyoutMenuOptions(
    var id: String = UUID.randomUUID().toString(), // Not exported by plugin
    var iconAfter: ODSIconModel? = null,
    var iconBefore: ODSIconModel? = null,
    var label: String? = null,
    var helperText: String? = null,
    var disabled: Boolean = false,
)

/**
 * Properties describing an ODS flyout menu.
 *
 * @property expanded Controls the visibility of the menu.
 * @property menuSize Size of the menu trigger button.
 * @property buttonProps Configuration of the trigger button.
 * @property options Available menu options.
 */
data class ODSFlyoutMenuProps(
    var expanded: Boolean = false,
    var menuSize: ODSFlyoutMenuMenuSize = ODSFlyoutMenuMenuSize.LARGE,
    var buttonProps: ODSFlyoutMenuButtonProps? = null,
    var options: List<ODSFlyoutMenuOptions>? = null,
)
