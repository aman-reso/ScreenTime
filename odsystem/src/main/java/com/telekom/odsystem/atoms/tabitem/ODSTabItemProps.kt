package com.telekom.odsystem.atoms.tabitem

import com.telekom.odsystem.atoms.icon.ODSIconModel

enum class ODSTabItemSize {
    LARGE,
    SMALL,
}

enum class ODSTabItemVariant {
    HUG,
    FILL
}

data class ODSTabItemProps(
    var label: String? = null,
    var selected: Boolean = false,
    var size: ODSTabItemSize = ODSTabItemSize.LARGE,
    var icon: ODSIconModel? = null,
    var showBadge: Boolean = false,
    var variant: ODSTabItemVariant = ODSTabItemVariant.HUG
)
