package com.telekom.odsystem.slots.cardcontentbasic

/**
 * Properties configuring a basic card content layout.
 *
 * @property heading Optional heading displayed on top.
 * @property label Small label text above the heading.
 * @property subtitle Subtitle shown below the heading.
 * @property content Main content text of the card.
 */
data class ODSCardContentBasicProps(
    var content: String? = null,
    var heading: String? = null,
    var label: String? = null,
    var subtitle: String? = null,
)
