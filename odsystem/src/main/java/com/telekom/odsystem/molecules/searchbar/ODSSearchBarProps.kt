package com.telekom.odsystem.molecules.searchbar

import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Converts [ODSSearchBarButtonProps] to [ODSButtonProps] for use within the search bar.
 *
 * This extension function simplifies the creation of button properties specifically tailored for the
 * search bar's clear or custom action button. It defaults to an icon-only button, small size,
 * and ghost variant.
 *
 * @param disabled Whether the resulting button should be disabled. Defaults to `false`.
 * @return An [ODSButtonProps] instance configured for the search bar button.
 */
fun ODSSearchBarButtonProps.toODSButtonProps(disabled: Boolean = false): ODSButtonProps {
    return ODSButtonProps(
        buttonIcon = this.buttonIcon,
        buttonType = ODSButtonButtonType.ICON_ONLY,
        disabled = disabled,
        size = ODSButtonSize.SMALL,
        variant = ODSButtonVariant.GHOST
    )
}

/**
 * Represents the properties for a button within an [ODSSearchBar].
 *
 * This is typically used for the clear button or a custom action button.
 *
 * @property buttonIcon The icon to be displayed on the button. See [ODSIconModel].
 */
data class ODSSearchBarButtonProps(
    var buttonIcon: ODSIconModel? = null
)

/**
 * Represents the properties required to configure an ODSSearchBar component.
 *
 * @property disabled Whether the search bar is disabled and non-interactive. Defaults to `false`.
 * @property input The current text input value of the search bar. Defaults to `null`.
 * @property placeholder The placeholder text to display when the input is empty. Defaults to `null`.
 * @property buttonProps The properties for the optional button displayed within the search bar (e.g., a clear button).
 *                       See [ODSSearchBarButtonProps]. Defaults to `null`, meaning no button is shown.
 */
data class ODSSearchBarProps(
    var disabled: Boolean = false,
    var input: String? = null,
    var placeholder: String? = null,
    var buttonProps: ODSSearchBarButtonProps? = null,
)

internal val ODSSearchBarProps.filled: Boolean
    get() = !input.isNullOrEmpty()
