package com.telekom.odsystem.organisms.cardwidgetdeprecated

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.foundations.ODSAspectRatio

enum class ODSCardWidgetDeprecatedType {
    SLOT,
    IMAGE,
}

/**
 * Properties describing an ODS card widget component.
 *
 * @property image Optional header image displayed in the widget.
 * @property imageAspectRatio Aspect ratio of the header image.
 * @property logo Optional brand logo shown on top of the card.
 * @property subtitle Subtitle text displayed below the title.
 * @property title Title text of the widget.
 * @property type Defines whether the widget shows a slot or an image.
 */
data class ODSCardWidgetDeprecatedProps(
    var image: ODSImageModel? = null,
    var imageAspectRatio: ODSAspectRatio = ODSAspectRatio.VALUE_16_9,
    var logo: ODSImageModel? = null,
    var subtitle: String? = null,
    var title: String? = null,
    var type: ODSCardWidgetDeprecatedType = ODSCardWidgetDeprecatedType.SLOT,
)
