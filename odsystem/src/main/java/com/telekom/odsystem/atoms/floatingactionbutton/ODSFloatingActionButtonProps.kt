package com.telekom.odsystem.atoms.floatingactionbutton

import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Defines the size of the ODS floating action button.
 */
enum class ODSFloatingActionButtonSize {
    /**
     * A large-sized floating action button.
     */
    LARGE,

    /**
     * A small-sized floating action button.
     */
    SMALL,
}

/**
 * Defines the type of the ODS floating action button.
 */
enum class ODSFloatingActionButtonType {
    /**
     * A standard floating action button.
     */
    STANDARD,

    /**
     * An extended floating action button with a label.
     */
    EXTENDED,
}

/**
 * Defines the visual variant of the ODS floating action button.
 */
enum class ODSFloatingActionButtonVariant {
    /**
     * The primary visual style for the floating action button.
     */
    PRIMARY,

    /**
     * The secondary visual style for the floating action button.
     */
    SECONDARY,

    /**
     * An outlined visual style for the floating action button.
     */
    OUTLINE,
}

/**
 * Properties used to configure the appearance and behavior of an ODS floating action button.
 *
 * @property icon The icon displayed on the floating action button. If `buttonIcon` is set, it will be used as the icon.
 * @property disabled Indicates whether the floating action button is disabled and non-interactive.
 * @property label The label text displayed on the floating action button (if applicable).
 * @property leftIcon Indicates whether the icon is positioned to the left of the label.
 * @property rightIcon Indicates whether the icon is positioned to the right of the label.
 * @property size The size of the floating action button (e.g., large, small).
 * @property type The type of the floating action button (e.g., standard, extended).
 * @property variant The visual variant of the floating action button (e.g., primary, secondary, outline).
 */
data class ODSFloatingActionButtonProps(
    var icon: ODSIconModel? = null,
    var disabled: Boolean = false,
    var label: String? = null,
    var leftIcon: Boolean = false,
    var rightIcon: Boolean = false,
    var size: ODSFloatingActionButtonSize = ODSFloatingActionButtonSize.LARGE,
    var type: ODSFloatingActionButtonType = ODSFloatingActionButtonType.EXTENDED,
    var variant: ODSFloatingActionButtonVariant = ODSFloatingActionButtonVariant.PRIMARY,
)
