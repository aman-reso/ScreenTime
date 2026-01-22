package com.telekom.odsystem.organisms.bottomnavigation

import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.navigationitem.ODSNavigationItemBadgeNumberProps
import com.telekom.odsystem.atoms.navigationitem.ODSNavigationItemProps

/**
 * Properties used to configure the appearance and behavior of the ODS bottom navigation.
 *
 * @property items The list of navigation items to display in the bottom navigation bar.
 * @property labels Indicates whether labels should be displayed alongside icons in the navigation items.
 */
data class ODSBottomNavigationProps(
    var items: List<ODSBottomNavigationItemProps>? = null,
    var labels: Boolean = true,
)

/**
 * Properties used to define an individual item in the ODS bottom navigation.
 *
 * Not Exported from plugin
 * @property active Indicates whether the navigation item is currently active.
 * @property disabled Indicates whether the navigation item is disabled and non-interactive.
 * @property iconActive The icon to display when the navigation item is active.
 * @property icon The default icon to display for the navigation item.
 * @property text The label text for the navigation item.
 * @property badgeNumberProps The properties for displaying a badge number on the navigation item (if applicable).
 */
data class ODSBottomNavigationItemProps(
    var active: Boolean = false,
    var disabled: Boolean = false,
    var iconActive: ODSIconModel? = null,
    var icon: ODSIconModel? = null,
    var text: String? = null,
    var textRes: Int,
    var badgeNumberProps: ODSNavigationItemBadgeNumberProps? = null,
)

/**
 * Converts an [ODSBottomNavigationItemProps] instance to an [ODSNavigationItemProps] instance.
 *
 * Not Exported from plugin
 * @param showLabel Indicates whether the label text should be included in the converted navigation item.
 * @return A new [ODSNavigationItemProps] instance with the converted properties.
 */
internal fun ODSBottomNavigationItemProps.toODSNavigationItemProps(showLabel: Boolean): ODSNavigationItemProps {
    return ODSNavigationItemProps(
        active = active,
        disabled = disabled,
        iconActive = iconActive,
        icon = icon,
        text = if (showLabel) text else null,
        badgeNumberProps = badgeNumberProps,
        textRes = textRes
    )
}

/**
 * Converts an [ODSBottomNavigationItemProps] instance to an [ODSNavigationItemProps] instance.
 *
 * Not Exported from plugin
 * @param showLabel Indicates whether the label text should be included in the converted navigation item.
 * @param active Indicates whether the navigation item is currently active, overriding the original `active` property.
 * @return A new [ODSNavigationItemProps] instance with the converted properties.
 */
internal fun ODSBottomNavigationItemProps.toODSNavigationItemProps(
    showLabel: Boolean,
    active: Boolean,
): ODSNavigationItemProps {
    return ODSNavigationItemProps(
        textRes = this.textRes,
        active = active,
        disabled = disabled,
        iconActive = iconActive,
        icon = icon,
        text = if (showLabel) text else null,
        badgeNumberProps = badgeNumberProps
    )
}
