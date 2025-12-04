package com.telekom.odsystem.atoms.listitem

import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Defines the prefix type for an ODS list item.
 */
enum class ODSListItemVariant {
    NUMBER,
    ICON,
    BULLETPOINT,
    OUTLINE_BULLET,
}

/**
 * Properties used to configure the appearance and behavior of an ODS list item.
 *
 * @property icon The icon displayed in the list item (if applicable).
 * @property link Indicates whether the list item is a clickable link.
 * @property number The number to display as the prefix (if applicable).
 * @property text The text content of the list item.
 * @property variant The visual variant of the list item (e.g., number, icon, bullet point).
 */
data class ODSListItemProps(
    var icon: ODSIconModel? = null,
    var link: Boolean = false,
    var variant: ODSListItemVariant = ODSListItemVariant.BULLETPOINT,
    var number: String? = null,
    var text: String? = null,
)
