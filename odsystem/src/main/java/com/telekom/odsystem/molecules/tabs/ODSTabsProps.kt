package com.telekom.odsystem.molecules.tabs

import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.tabitem.ODSTabItemProps
import com.telekom.odsystem.atoms.tabitem.ODSTabItemSize
import com.telekom.odsystem.atoms.tabitem.ODSTabItemVariant
import java.util.UUID

enum class ODSTabsSize {
    /** Large tabs with generous spacing. */
    LARGE,

    /** Compact tabs for tight layouts. */
    SMALL,
}

enum class ODSTabsVariant {
    /** Tabs size to their content. */
    HUG,

    /** Tabs stretch to fill the width. */
    FILL,
}

/**
 * Properties describing a set of tabs.
 *
 * @property showDividerFrame Shows a divider below the tabs when true.
 * @property size Size of each tab.
 * @property variant Layout behaviour of the tabs.
 * @property tabElements List of tab definitions.
 */
data class ODSTabsProps(
    var showDividerFrame: Boolean = false,
    var size: ODSTabsSize = ODSTabsSize.LARGE,
    var variant: ODSTabsVariant = ODSTabsVariant.HUG,
    var tabElements: List<ODSTabItemModel> = emptyList()
)

// Not exported by plugin
/**
 * Model representing a single tab element.
 *
 * @property id Unique identifier for comparison.
 * @property label Optional text label for the tab.
 * @property icon Optional icon displayed with the label.
 * @property showBadge Whether to display a badge indicator.
 */
data class ODSTabItemModel(
    var id: String = UUID.randomUUID().toString(),
    var label: String? = null,
    var icon: ODSIconModel? = null,
    var showBadge: Boolean = false
)

// Not exported by plugin
internal fun ODSTabsSize.toTabItemSize(): ODSTabItemSize {
    return when (this) {
        ODSTabsSize.LARGE -> ODSTabItemSize.LARGE
        ODSTabsSize.SMALL -> ODSTabItemSize.SMALL
    }
}

// Not exported by plugin
internal fun ODSTabsVariant.toTabItemVariant(): ODSTabItemVariant {
    return when (this) {
        ODSTabsVariant.HUG -> ODSTabItemVariant.HUG
        ODSTabsVariant.FILL -> ODSTabItemVariant.FILL
    }
}

// Not exported by plugin
internal fun ODSTabItemModel.toODSTabItemProps(
    selected: Boolean,
    size: ODSTabsSize,
    variant: ODSTabsVariant
): ODSTabItemProps {
    return ODSTabItemProps(
        icon = icon,
        label = label,
        selected = selected,
        showBadge = showBadge,
        size = size.toTabItemSize(),
        variant = variant.toTabItemVariant()
    )
}
