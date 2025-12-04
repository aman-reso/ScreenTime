package com.telekom.odsystem.organisms.cardproduct

import com.telekom.odsystem.atoms.productcardcolors.ODSProductCardColorsProps
import com.telekom.odsystem.atoms.productcardtag.ODSProductCardTagProps

/**
 * Specifies the size of the product card.
 */
enum class ODSCardProductSize {
    SMALL_H,
    SMALL_V,
    MEDIUM,
    LARGE,
}

/**
 * Properties for configuring an ODS Card Product component.
 *
 * This data class defines the customizable aspects of an ODS Card Product.
 *
 * @property size The card product's size. Defaults to [ODSCardProductSize.SMALL_V].
 * @property productCardTagProps Optional tag properties. See [ODSProductCardTagProps].
 * @property productCardColorsProps Optional color customization.
 *
 */
data class ODSCardProductProps(
    var size: ODSCardProductSize = ODSCardProductSize.SMALL_V,
    var productCardTagProps: ODSProductCardTagProps? = null,
    var productCardColorsProps: ODSProductCardColorsProps? = null // Not exported by plugin
)
