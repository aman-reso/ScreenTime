package com.telekom.odsystem.atoms.badgeicon

/**
 * Defines the size of the ODS badge icon.
 */
enum class ODSBadgeIconSize {
    /**
     * A large-sized badge icon.
     */
    LARGE,

    /**
     * A small-sized badge icon.
     */
    SMALL,

    /**
     * A standard-sized badge icon.
     */
    STANDARD,
}

/**
 * Defines the type of the ODS badge icon.
 */
enum class ODSBadgeIconMode {
    SUCCESS,
    ERROR,
}

/**
 * Properties used to configure the appearance and behavior of an ODS badge icon.
 *
 * @property size The size of the badge icon (e.g., large, small, standard).
 * @property mode The mode of the badge icon, which can be either success or error.
 */
data class ODSBadgeIconProps(
    var size: ODSBadgeIconSize = ODSBadgeIconSize.LARGE,
    var mode: ODSBadgeIconMode = ODSBadgeIconMode.SUCCESS,
)
