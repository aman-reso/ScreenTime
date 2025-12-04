package com.telekom.odsystem.slots.cardfeaturepreferredcontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-25 (v1.33.1) - uid: 506748f4
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=9079-18873
 */

data class ODSCardFeaturePreferredContentTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val productNameStyle: ODSTextStyle,
    val productNameTextAlign: TextAlign,
    val productPriceStyle: ODSTextStyle,
    val productPriceTextAlign: TextAlign,
)

val defaultODSCardFeaturePreferredContentTokens = ODSCardFeaturePreferredContentTokens(
    gap = DSVariables.spacingComponent2,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Center,
    productNameStyle = DSTextStyles.bodyMBold,
    productNameTextAlign = TextAlign.Left,
    productPriceStyle = DSTextStyles.bodyMBold,
    productPriceTextAlign = TextAlign.Left
)

var DSCardFeaturePreferredContentTokens: ODSCardFeaturePreferredContentTokens =
    defaultODSCardFeaturePreferredContentTokens
