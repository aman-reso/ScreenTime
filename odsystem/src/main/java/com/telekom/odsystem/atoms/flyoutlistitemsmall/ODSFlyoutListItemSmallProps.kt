package com.telekom.odsystem.atoms.flyoutlistitemsmall

import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectOptions
import com.telekom.odsystem.molecules.flyoutmenu.ODSFlyoutMenuOptions

enum class ODSFlyoutListItemSmallVariant {
    /** Standard list item. */
    STANDARD,

    /** List item shown as checked. */
    CHECKED,
}

/**
 * Properties describing a small flyout list item.
 *
 * @property disabled Prevents selection when true.
 * @property helperText Optional helper text displayed below the label.
 * @property iconAfter Optional icon displayed after the label.
 * @property iconBefore Optional icon displayed before the label.
 * @property label Text shown in the item.
 * @property variant Visual variant of the item.
 */
data class ODSFlyoutListItemSmallProps(
    var disabled: Boolean = false,
    var helperText: String? = null,
    var iconAfter: ODSIconModel? = null,
    var iconBefore: ODSIconModel? = null,
    var label: String? = null,
    var variant: ODSFlyoutListItemSmallVariant = ODSFlyoutListItemSmallVariant.STANDARD,
) {
    constructor(options: ODSFlyoutMenuOptions) : this(
        iconAfter = options.iconAfter,
        iconBefore = options.iconBefore,
        label = options.label,
        helperText = options.helperText,
        disabled = options.disabled,
        variant = ODSFlyoutListItemSmallVariant.STANDARD,
    )

    constructor(options: ODSDropdownSelectOptions, selected: Boolean) : this(
        iconBefore = options.iconBefore,
        label = options.label,
        helperText = options.helperText,
        disabled = options.disabled,
        variant = if (selected) ODSFlyoutListItemSmallVariant.CHECKED else ODSFlyoutListItemSmallVariant.STANDARD,
    )
}
