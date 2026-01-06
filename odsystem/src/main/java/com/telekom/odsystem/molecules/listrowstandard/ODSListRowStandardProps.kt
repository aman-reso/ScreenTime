package com.telekom.odsystem.molecules.listrowstandard

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.icon.ODSIconModel

enum class ODSListRowStandardVariant {
    /** Standard row displaying only text. */
    STANDARD,

    /** Row with a leading image. */
    IMAGE,

    /** Row with a leading icon. */
    ICON,
}

/**
 * Configuration for a standard list row.
 *
 * @property descriptionText Secondary text content.
 * @property descriptionTitle Title shown above the description.
 * @property labelText Primary label text.
 * @property variant Visual variant of the row.
 * @property image Optional image used in the image variant.
 * @property icon Optional icon used in the icon variant.
 * @property label Primary label text. If `labelText` is set, it will be used as the label.
 * @property showDescriptionTitle Indicates whether to display the description title.
 * @property labelTextHtml HTML string for label text. If set, will be used instead of labelText.
 * @property descriptionTextHtml HTML string for description text. If set, will be used instead of descriptionText.
 *
 */
data class ODSListRowStandardProps(
    var descriptionText: String? = null,
    var descriptionTitle: String? = null,
    var labelText: String? = null,
    var label: String? = null,
    var icon: ODSIconModel? = null,
    var showDescriptionTitle: Boolean = true,
    var variant: ODSListRowStandardVariant = ODSListRowStandardVariant.STANDARD,
    var image: ODSImageModel? = null, // Not exported from the plugin
    var labelTextHtml: String? = null,
    var descriptionTextHtml: String? = null
)
