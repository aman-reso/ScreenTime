package com.telekom.odsystem.atoms.badgenumber

/**
 * Defines the type of the ODS badge number.
 */
enum class ODSBadgeNumberVariant {
    NOTIFICATION,
    NEUTRAL
}

/**
 * Defines the size of the ODS badge number.
 */
enum class ODSBadgeNumberSize {
    /**
     * A large-sized badge number.
     */
    LARGE,

    /**
     * A small-sized badge number.
     */
    SMALL,

    /**
     * A standard-sized badge number.
     */
    STANDARD,
}

/**
 * Properties used to configure the appearance and behavior of an ODS badge number.
 *
 * @property notificationNumber The number to display in the badge.
 * @property size The size of the badge number (e.g., large, small, standard).
 * @property variant The visual variant of the badge number (e.g., notification, neutral).
 */
data class ODSBadgeNumberProps(
    var notificationNumber: String? = null,
    var variant: ODSBadgeNumberVariant = ODSBadgeNumberVariant.NOTIFICATION,
    var size: ODSBadgeNumberSize = ODSBadgeNumberSize.LARGE,
)
