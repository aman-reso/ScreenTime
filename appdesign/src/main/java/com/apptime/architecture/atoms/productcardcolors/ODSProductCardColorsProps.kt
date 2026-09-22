package com.telekom.odsystem.atoms.productcardcolors

import com.telekom.odsystem.atoms.colourswatch.ODSColourSwatchProps

/**
 * Not exported from plugin
 * Properties for the colors section of an ODSProductCard.
 * @property colourSwatchProps Optional list of [ODSColourSwatchProps] for color swatches.
 */
data class ODSProductCardColorsProps(
    val colourSwatchProps: List<ODSColourSwatchProps>? = null
)
