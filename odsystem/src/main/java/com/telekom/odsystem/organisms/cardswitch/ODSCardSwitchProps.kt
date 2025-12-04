package com.telekom.odsystem.organisms.cardswitch

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps

enum class ODSCardSwitchVariant {
    TITLE,
    BRAND,
}

/**
 * Properties configuring a card switch component.
 *
 * @property logo Optional logo displayed in brand mode.
 * @property selected Whether the card is currently selected.
 * @property tag1Props Optional first tag properties.
 * @property tag2Props Optional second tag properties.
 * @property subtitle Subtitle text shown below the title.
 * @property title Main title of the card.
 * @property variant Visual variant of the card, either title or brand.
 */
data class ODSCardSwitchProps(
    var logo: ODSImageModel? = null, // Not exported from plugin
    var selected: Boolean = false,
    var subtitle: String? = null,
    var title: String? = null,
    var variant: ODSCardSwitchVariant = ODSCardSwitchVariant.TITLE,
    var tag1Props: ODSTagStaticProps? = null,
    var tag2Props: ODSTagStaticProps? = null
)
