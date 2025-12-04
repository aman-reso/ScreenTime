package com.telekom.odsystem.organisms.cardwidget

import com.telekom.odsystem.atoms.ODSImageModel

enum class ODSCardWidgetType {
    BOTTOM_IMAGE,
    TOP_IMAGE,
    NO_IMAGE,
}

/**
 * Properties describing an ODS card widget component.
 *
 * @property image Optional header image displayed in the widget.
 * @property logo Optional brand logo shown on top of the card.
 * @property subtle If `true`, the card will have a subtle appearance with a lighter background and no shadow.
 * @property showLogo If `true`, the brand logo will be displayed.
 * @property type Defines whether the widget shows a slot or an image.
 */
data class ODSCardWidgetProps(
    var image: ODSImageModel? = null,
    var logo: ODSImageModel? = null,
    var subtle: Boolean = false,
    var showLogo: Boolean = true,
    var type: ODSCardWidgetType = ODSCardWidgetType.BOTTOM_IMAGE,
)
