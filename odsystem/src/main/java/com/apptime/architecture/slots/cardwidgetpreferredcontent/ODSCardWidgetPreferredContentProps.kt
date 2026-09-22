package com.telekom.odsystem.slots.cardwidgetpreferredcontent

/**
 * Properties for the preferred content area of an `ODSCardWidget`.
 *
 * This class is used to configure the title, subtitle, and their visibility within the card's
 * preferred content slot.
 *
 * @property title The main title text to be displayed.
 * @property subtitle The secondary text (subtitle) to be displayed below the title.
 * @property showSubtitle A boolean flag to control the visibility of the subtitle. Defaults to `true`.
 */
data class ODSCardWidgetPreferredContentProps(
    var showSubtitle: Boolean = true,
    var subtitle: String? = null,
    var title: String? = null,
)
