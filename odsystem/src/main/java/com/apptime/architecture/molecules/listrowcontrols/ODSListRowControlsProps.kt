package com.telekom.odsystem.molecules.listrowcontrols

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.controls.ODSControlsType
import com.telekom.odsystem.atoms.icon.ODSIconModel

enum class ODSListRowControlsVariant {
    /** Row with a switch control only. */
    STANDARD,

    /** Row displaying an image. */
    IMAGE,

    /** Row with an icon. */
    ICON,
}

/**
 * Properties describing a list row with controls.
 *
 * @property disabled Disables all interactions when true.
 * @property icon Optional icon shown with the label.
 * @property labelText Main label text.
 * @property labelTitle Title displayed above the label.
 * @property readOnly Indicates if the control is read-only.
 * @property variant Visual variant of the row.
 * @property image Optional image displayed instead of icon.
 * @property type Underlying control type.
 * @property selected Current selection state.
 */
data class ODSListRowControlsProps(
    var disabled: Boolean = false,
    var icon: ODSIconModel? = null,
    var labelText: String? = null,
    var labelTitle: String? = null,
    var readOnly: Boolean = false,
    var variant: ODSListRowControlsVariant = ODSListRowControlsVariant.STANDARD,
    var image: ODSImageModel? = null, // Not exported from plugin
    var type: ODSControlsType = ODSControlsType.SWITCH_ICON, // Not exported from plugin
    var selected: Boolean = false, // Not exported from plugin
)
