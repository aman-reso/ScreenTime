package com.telekom.odsystem.atoms.togglechip

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Represents the properties for configuring an ODS (presumably "OD System") Toggle Chip component.
 *
 * This data class allows customization of the chip's state (selected, disabled),
 * content (label, icon, image), and visual presentation (whether to show an image/picture).
 *
 * @property disabled Determines if the toggle chip is interactive. If `true`, it cannot be toggled. Defaults to `false`.
 * @property icon The [ODSIconModel] to be displayed on the chip, typically on the leading side. Can be `null` if no icon is needed.
 * @property image The [ODSImageModel] to be displayed on the chip, often as an alternative to an icon or as a richer visual element. Can be `null`.
 * @property label The text content to be displayed on the chip. Can be `null` if the chip is icon/image only.
 * @property selected Indicates the current toggle state of the chip. `true` if selected/checked, `false` otherwise. Defaults to `false`.
 * @property showImage If `true`, the image will be displayed on the chip. This is a more explicit way to control image visibility.
 */
data class ODSToggleChipProps(
    var disabled: Boolean = false,
    var icon: ODSIconModel? = null,
    var image: ODSImageModel? = null,
    var label: String? = null,
    var selected: Boolean = false,
    var showImage: Boolean = false,
)
