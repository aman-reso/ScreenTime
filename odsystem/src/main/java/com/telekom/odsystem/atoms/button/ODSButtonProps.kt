package com.telekom.odsystem.atoms.button

import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Defines the type of the ODS button.
 */
enum class ODSButtonButtonType {
    /**
     * A standard button with text and optional icon.
     */
    STANDARD,

    /**
     * A button that displays only an icon.
     */
    ICON_ONLY,
}

/**
 * Defines the visual style variant of the ODS button.
 */
enum class ODSButtonVariant {
    /**
     * A button with no background and minimal styling.
     */
    GHOST,

    /**
     * A button with an outlined border.
     */
    OUTLINE,

    /**
     * The primary action button with prominent styling.
     */
    PRIMARY,

    /**
     * A secondary action button with less visual emphasis than the primary.
     */
    SECONDARY,
}

/**
 * Defines the size of the ODS button.
 */
enum class ODSButtonSize {
    /**
     * A large-sized button.
     */
    LARGE,

    /**
     * A small-sized button.
     */
    SMALL,
}

/**
 * Properties used to configure the appearance and behavior of an ODS button.
 *
 * @property label The text displayed inside the button. Use this instead of `buttonLabel`.
 * @property buttonIcon The icon displayed inside the button (if applicable).
 * @property leftIcon Whether the icon should appear to the left of the label.
 * @property rightIcon Whether the icon should appear to the right of the label.
 * @property buttonType Defines whether the button is standard or icon-only.
 * @property variant The visual variant of the button (e.g., primary, outline).
 * @property size The size of the button (e.g., large or small).
 * @property disabled If true, the button is disabled and non-interactive.
 */
data class ODSButtonProps(
    var label: String? = null,
    var buttonIcon: ODSIconModel? = null,
    var leftIcon: Boolean = false,
    var rightIcon: Boolean = false,
    var buttonType: ODSButtonButtonType = ODSButtonButtonType.STANDARD,
    var variant: ODSButtonVariant = ODSButtonVariant.PRIMARY,
    var size: ODSButtonSize = ODSButtonSize.LARGE,
    var disabled: Boolean = false,
)
