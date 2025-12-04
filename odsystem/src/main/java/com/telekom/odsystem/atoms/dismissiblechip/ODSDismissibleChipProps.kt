package com.telekom.odsystem.atoms.dismissiblechip

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Defines the type of the ODS dismissible chip.
 */
enum class ODSDismissibleChipVariant {
    STANDARD,
    WITH_ICON,
    WITH_IMAGE,
}

/**
 * Properties used to configure the appearance and behavior of an ODS dismissible chip.
 *
 * @property disabled Indicates whether the dismissible chip is disabled and non-interactive.
 * @property icon The icon displayed on the chip (if applicable).
 * @property image The image displayed on the chip (if applicable).
 * @property label The label text displayed on the chip.
 * @property variant The visual variant of the dismissible chip (e.g., standard, with icon, with image).
 */
data class ODSDismissibleChipProps(
    var disabled: Boolean = false,
    var icon: ODSIconModel? = null,
    var image: ODSImageModel? = null,
    var label: String? = null,
    var variant: ODSDismissibleChipVariant = ODSDismissibleChipVariant.WITH_ICON,
)
