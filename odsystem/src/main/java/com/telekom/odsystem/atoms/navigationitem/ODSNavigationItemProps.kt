package com.telekom.odsystem.atoms.navigationitem

import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumberProps
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumberSize
import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Properties describing a badge number for a navigation item.
 *
 * @property notificationNumber Text displayed inside the badge.
 * @property size Visual size of the badge.
 */
data class ODSNavigationItemBadgeNumberProps(
    var notificationNumber: String? = null,
    var size: ODSBadgeNumberSize = ODSBadgeNumberSize.LARGE,
)

internal fun ODSNavigationItemBadgeNumberProps.toODSBadgeNumberProps(): ODSBadgeNumberProps {
    return ODSBadgeNumberProps(
        notificationNumber = this.notificationNumber,
        size = this.size
    )
}

/**
 * Properties describing an item within a navigation bar.
 *
 * @property active Indicates if the item is currently selected.
 * @property disabled Disables the item when true.
 * @property iconActive Icon shown when the item is active.
 * @property icon Default icon.
 * @property text Label text.
 * @property badgeNumberProps Optional badge number configuration.
 */
data class ODSNavigationItemProps(
    var active: Boolean = false,
    var disabled: Boolean = false,
    var iconActive: ODSIconModel? = null,
    var icon: ODSIconModel? = null,
    var text: String? = null,
    var badgeNumberProps: ODSNavigationItemBadgeNumberProps? = null,
    var textRes: Int = -1,
)
