package com.telekom.odsystem.organisms.cardanchoredimage

enum class ODSCardAnchoredImageSize {
    MEDIUM,
    SMALL,
}

/**
 * Properties describing an anchored image card.
 *
 * @property heading Optional heading displayed above the image.
 * @property label Supporting label text.
 * @property size Size of the card variant.
 * @property alignActionSlotToBottom Whether the action slot is aligned to the bottom.
 */
data class ODSCardAnchoredImageProps(
    var heading: String? = null,
    var label: String? = null,
    var size: ODSCardAnchoredImageSize = ODSCardAnchoredImageSize.MEDIUM,
    var alignActionSlotToBottom: Boolean = false, // Not exported by plugin
)
