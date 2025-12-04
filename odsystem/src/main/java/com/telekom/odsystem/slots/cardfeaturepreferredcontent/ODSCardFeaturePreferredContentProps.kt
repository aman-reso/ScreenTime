package com.telekom.odsystem.slots.cardfeaturepreferredcontent

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-25 (v1.33.1) - uid: 506748f4
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=9079-18873
 */

/**
 * Represents the properties for the ODSCardFeaturePreferredContent component.
 *
 * This component is used to display preferred content within a card feature, typically
 * showing product information like name and price.
 *
 * @property productName The name of the product to be displayed.
 * @property productPrice The price of the product to be displayed.
 */
data class ODSCardFeaturePreferredContentProps(
    var productName: String? = null,
    var productPrice: String? = null,
)
