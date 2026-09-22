package com.telekom.odsystem.atoms.avatar

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.badgeicon.ODSBadgeIconMode
import com.telekom.odsystem.atoms.badgeicon.ODSBadgeIconProps
import com.telekom.odsystem.atoms.badgeicon.ODSBadgeIconSize
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumberProps
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumberSize
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumberVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Defines the type of badge displayed on the ODS avatar.
 */
enum class ODSAvatarBadgeType {
    /**
     * A badge displaying a number.
     */
    NUMBER,

    /**
     * A badge displaying an icon.
     */
    ICON,
}

/**
 * Defines the size of the ODS avatar.
 */
enum class ODSAvatarSize {
    /**
     * A large-sized avatar.
     */
    LARGE,

    /**
     * A medium-sized avatar.
     */
    MEDIUM,

    /**
     * A small-sized avatar.
     */
    SMALL,
}

/**
 * Defines the visual variant of the ODS avatar.
 */
enum class ODSAvatarVariant {
    /**
     * An avatar displaying an image or initials.
     */
    AVATAR,

    /**
     * An avatar displaying only an icon.
     */
    ICON,

    /**
     * An avatar displaying only initials.
     */
    INITIALS,
}

/**
 * Properties for configuring a badge with a number on the ODS avatar.
 *
 * @property notificationNumber The number to display in the badge.
 * @property variant The visual variant of the badge (e.g., notification, neutral).
 */
data class ODSAvatarBadgeNumberProps(
    var notificationNumber: String? = null,
    var variant: ODSBadgeNumberVariant = ODSBadgeNumberVariant.NOTIFICATION,
)

/**
 * Converts [ODSAvatarBadgeNumberProps] to [ODSBadgeNumberProps].
 *
 * Not Exported from plugin
 * @param size The size of the avatar to determine the badge size.
 * @return A new [ODSBadgeNumberProps] instance with the converted properties.
 */
internal fun ODSAvatarBadgeNumberProps.toODSBadgeNumberProps(size: ODSAvatarSize): ODSBadgeNumberProps {
    return ODSBadgeNumberProps(
        notificationNumber = this.notificationNumber,
        variant = this.variant,
        size = when (size) {
            ODSAvatarSize.LARGE -> ODSBadgeNumberSize.LARGE
            ODSAvatarSize.MEDIUM -> ODSBadgeNumberSize.STANDARD
            ODSAvatarSize.SMALL -> ODSBadgeNumberSize.SMALL
        }
    )
}

/**
 * Properties for configuring a badge with an icon on the ODS avatar.
 * @property mode The mode of the badge icon, which can be either success or error.
 */
data class ODSAvatarBadgeIconProps(
    var mode: ODSBadgeIconMode = ODSBadgeIconMode.SUCCESS,
)

/**
 * Converts [ODSAvatarBadgeIconProps] to [ODSBadgeIconProps].
 *
 * Not Exported from plugin
 * @param size The size of the avatar to determine the badge size.
 * @return A new [ODSBadgeIconProps] instance with the converted properties.
 */
internal fun ODSAvatarBadgeIconProps.toODSBadgeIconProps(size: ODSAvatarSize): ODSBadgeIconProps {
    return ODSBadgeIconProps(
        mode = this.mode,
        size = when (size) {
            ODSAvatarSize.LARGE -> ODSBadgeIconSize.LARGE
            ODSAvatarSize.MEDIUM -> ODSBadgeIconSize.STANDARD
            ODSAvatarSize.SMALL -> ODSBadgeIconSize.SMALL
        }
    )
}

/**
 * Properties used to configure the appearance and behavior of an ODS avatar.
 *
 * @property badgeType The type of badge to display (e.g., number or icon).
 * @property icon The icon to display in the avatar (if applicable).
 * @property image The image to display in the avatar (if applicable).
 * @property initials The initials to display in the avatar (if applicable).
 * @property showBadge Indicates whether the badge should be displayed.
 * @property size The size of the avatar (e.g., large, medium, small).
 * @property variant The visual variant of the avatar (e.g., avatar, icon, initials).
 * @property badgeNumberProps The properties for configuring a badge with a number.
 * @property badgeIconProps The properties for configuring a badge with an icon.
 */
data class ODSAvatarProps(
    var badgeType: ODSAvatarBadgeType = ODSAvatarBadgeType.NUMBER,
    var icon: ODSIconModel? = null,
    var image: ODSImageModel? = null,
    var initials: String? = null,
    var showBadge: Boolean = true,
    var size: ODSAvatarSize = ODSAvatarSize.LARGE,
    var variant: ODSAvatarVariant = ODSAvatarVariant.AVATAR,
    var badgeNumberProps: ODSAvatarBadgeNumberProps? = null,
    var badgeIconProps: ODSAvatarBadgeIconProps? = null
)
