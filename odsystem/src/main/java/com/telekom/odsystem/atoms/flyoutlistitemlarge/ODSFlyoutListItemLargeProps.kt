package com.telekom.odsystem.atoms.flyoutlistitemlarge

import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectOptions
import com.telekom.odsystem.molecules.flyoutmenu.ODSFlyoutMenuOptions

enum class ODSFlyoutListItemLargeVariant {
    STANDARD,
    CHECKED,
}

data class ODSFlyoutListItemLargeProps(
    var disabled: Boolean = false,
    var helperText: String? = null,
    var iconAfter: ODSIconModel? = null,
    var iconBefore: ODSIconModel? = null,
    var label: String? = null,
    var variant: ODSFlyoutListItemLargeVariant = ODSFlyoutListItemLargeVariant.STANDARD,
) {
    constructor(options: ODSFlyoutMenuOptions) : this(
        iconAfter = options.iconAfter,
        iconBefore = options.iconBefore,
        label = options.label,
        helperText = options.helperText,
        disabled = options.disabled,
        variant = ODSFlyoutListItemLargeVariant.STANDARD,
    )

    constructor(options: ODSDropdownSelectOptions, selected: Boolean) : this(
        iconBefore = options.iconBefore,
        label = options.label,
        helperText = options.helperText,
        disabled = options.disabled,
        variant = if (selected) ODSFlyoutListItemLargeVariant.CHECKED else ODSFlyoutListItemLargeVariant.STANDARD,
    )
}
