package com.telekom.odsystem.atoms.filterchipdropdownitem

import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Properties describing an item in a filter chip drop-down.
 *
 * @property disabled Disables selection when true.
 * @property selected Marks the option as selected.
 * @property leftIcon Optional icon displayed to the left of the label.
 * @property label Text displayed for the option. Use this instead of `labelText`.
 *
 */
data class ODSFilterChipDropdownItemProps(
    var disabled: Boolean = false,
    var leftIcon: ODSIconModel? = null,
    var label: String? = null,
    var selected: Boolean = false,
)
