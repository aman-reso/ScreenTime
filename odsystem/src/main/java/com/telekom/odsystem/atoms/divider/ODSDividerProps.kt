package com.telekom.odsystem.atoms.divider

/**
 * Defines the orientation variants for an ODS Divider.
 *
 * This enum is used to control whether the divider is drawn vertically or horizontally.
 */
enum class ODSDividerVariant {
    /** Represents a divider that is oriented vertically. */
    VERTICAL,

    /** Represents a divider that is oriented horizontally. */
    HORIZONTAL,
}

/**
 * Represents the properties for configuring an ODS (presumably "OD System") Divider component.
 * @property variant The [ODSDividerVariant] that defines the orientation of the divider.
 *                   Defaults to [ODSDividerVariant.VERTICAL].
 * @property inset Determines if the divider should have an inset (padding from the edges).
 *                 Defaults to `false`.
 * @property spacing Determines if the divider should include spacing around it.
 *                   Defaults to `false`.
 */
data class ODSDividerProps(
    var variant: ODSDividerVariant = ODSDividerVariant.VERTICAL,
    var inset: Boolean = false,
    var spacing: Boolean = false,
)
