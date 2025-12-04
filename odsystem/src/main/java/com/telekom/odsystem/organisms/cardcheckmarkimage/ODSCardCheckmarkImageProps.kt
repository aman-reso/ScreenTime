package com.telekom.odsystem.organisms.cardcheckmarkimage

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.foundations.ODSAspectRatio

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-11 (v1.33.1) - uid: 5ac57ab5
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=8756-24496
 */

/**
 * Properties for the ODS Card Checkmark Image component.
 *
 * @property disabled Whether the card is disabled.
 * @property filled Whether the card has a filled background.
 * @property readOnly Whether the card is in a read-only state.
 * @property selected Whether the card is currently selected.
 * @property subtle Whether the card should have a subtle appearance.
 * @property imageAspectRatio The aspect ratio for the image.
 * @property image The image model to display.
 */
data class ODSCardCheckmarkImageProps(
    var disabled: Boolean = false,
    var filled: Boolean = true,
    var readOnly: Boolean = false,
    var selected: Boolean = false,
    var subtle: Boolean = false,
    var imageAspectRatio: ODSAspectRatio = ODSAspectRatio.VALUE_4_3, // Not exported by the plugin
    var image: ODSImageModel? = null // Not exported by the plugin
)
