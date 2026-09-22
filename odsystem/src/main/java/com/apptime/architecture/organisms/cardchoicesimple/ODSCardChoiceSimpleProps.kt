package com.telekom.odsystem.organisms.cardchoicesimple

@Deprecated("Use ODSCardChoiceSimpleType instead", ReplaceWith("ODSCardChoiceSimpleType"))
enum class ODSCardChoiceSimpleVariant {
    FILLED,
    OUTLINE;

    fun toODSCardChoiceSimpleType(): ODSCardChoiceSimpleType = when (this) {
        FILLED -> ODSCardChoiceSimpleType.FILLED
        OUTLINE -> ODSCardChoiceSimpleType.OUTLINE
    }
}

enum class ODSCardChoiceSimpleType {
    FILLED,
    OUTLINE,
}

/**
 * Configuration object for a very small choice card.
 *
 * @property heading Optional heading text displayed above the card.
 * @property labelBottom Text shown below the card content.
 * @property labelTop Text shown above the card content.
 * @property selected Indicates whether the card is currently selected.
 * @property variant Deprecated. Visual style of the card.
 * @property type Type of the card, either filled or outlined.
 */
data class ODSCardChoiceSimpleProps(
    var heading: String? = null,
    var labelBottom: String? = null,
    var labelTop: String? = null,
    var selected: Boolean = false,
    @Deprecated("Use type instead", ReplaceWith("type"))
    var variant: ODSCardChoiceSimpleVariant = ODSCardChoiceSimpleVariant.FILLED,
    var type: ODSCardChoiceSimpleType = ODSCardChoiceSimpleType.FILLED,
) {
    init {
        if (variant != ODSCardChoiceSimpleVariant.FILLED && type == ODSCardChoiceSimpleType.FILLED) {
            type = variant.toODSCardChoiceSimpleType()
        }
    }
}
