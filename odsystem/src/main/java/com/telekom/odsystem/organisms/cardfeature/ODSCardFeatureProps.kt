package com.telekom.odsystem.organisms.cardfeature

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.button.ODSButtonProps

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-25 (v1.33.1) - uid: 12eefc0c
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=8864-15653
 */

/**
 * Properties for the ODS Card Feature component.
 *
 * @property buttonProps Properties for the button within the card feature.
 * @property image The image model to display in the card feature.
 */
data class ODSCardFeatureProps(
    var buttonProps: ODSButtonProps? = null,
    var image: ODSImageModel? = null, // Not exported by the plugin
)
