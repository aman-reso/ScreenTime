package com.telekom.odsystem.atoms.productcardtag

/**
 * Defines the different variants of tags that can be displayed on a product card.
 *
 * Each variant represents a specific type of tag to highlight product information.
 */
enum class ODSProductCardTagVariant {
    DEFAULT,
    DISCOUNT,
    PRE_ORDER,
}

/**
 * Properties for a tag displayed in an ODS Product Card.
 *
 * Defines the text and variant of a tag used to highlight product information.
 *
 * @property labelText Text content of the tag.
 * @property variant The variant of the tag. Defaults to [ODSProductCardTagVariant.DEFAULT].
 */
data class ODSProductCardTagProps(
    var labelText: String? = null,
    var variant: ODSProductCardTagVariant = ODSProductCardTagVariant.DEFAULT,
)
