package com.telekom.odsystem.organisms.cardimage

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.foundations.ODSAspectRatio

enum class ODSCardImageImagePosition {
    /** Image is placed at the top. */
    TOP,

    /** Image is placed at the bottom. */
    BOTTOM,

    LEFT
}

/**
 * Properties configuring an image card.
 *
 * @property imagePosition Position of the main image.
 * @property image Main card image.
 * @property imageAspectRatio Aspect ratio applied to the image.
 * @property logo Optional logo overlay.
 * @property isHorizontal Layout orientation.
 */
data class ODSCardImageProps(
    var imagePosition: ODSCardImageImagePosition = ODSCardImageImagePosition.TOP,
    var image: ODSImageModel? = null,
    var imageAspectRatio: ODSAspectRatio = ODSAspectRatio.VALUE_4_3,
    var logo: ODSImageModel? = null,
    var isHorizontal: Boolean = false,
)
