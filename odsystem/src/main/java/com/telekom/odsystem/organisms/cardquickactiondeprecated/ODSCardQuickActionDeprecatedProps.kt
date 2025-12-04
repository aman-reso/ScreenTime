package com.telekom.odsystem.organisms.cardquickactiondeprecated

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.sparkline.ODSSparklineProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps

enum class ODSCardQuickActionDeprecatedVariant {
    TITLE,
    BRAND,
}

/**
 * Properties describing an ODS quick action card.
 *
 * @property logo Optional logo displayed at the top.
 * @property sparklineProps Optional sparkline chart configuration.
 * @property tag1Props Optional first tag.
 * @property tag2Props Optional second tag.
 * @property subtitle Subtitle text beneath the title.
 * @property title Main title of the card.
 * @property variant Visual variant of the card, either title or brand.
 */
data class ODSCardQuickActionDeprecatedProps(
    var logo: ODSImageModel? = null,
    var subtitle: String? = null,
    var title: String? = null,
    var sparklineProps: ODSSparklineProps? = null,
    var variant: ODSCardQuickActionDeprecatedVariant = ODSCardQuickActionDeprecatedVariant.TITLE,
    var tag1Props: ODSTagStaticProps? = null,
    var tag2Props: ODSTagStaticProps? = null,
)
