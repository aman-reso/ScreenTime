package com.telekom.odsystem.atoms.cardengagement

import com.telekom.odsystem.atoms.ODSImageModel

/**
 * Properties used to configure the appearance and behavior of an ODS card engagement.
 *
 * @property label The label text displayed on the card engagement.
 * @property image The image displayed on the card engagement (if applicable).
 */
data class ODSCardEngagementProps(
    var label: String? = null,
    var image: ODSImageModel? = null
)
