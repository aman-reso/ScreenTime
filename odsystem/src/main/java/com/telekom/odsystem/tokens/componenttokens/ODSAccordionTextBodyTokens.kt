package com.telekom.odsystem.tokens.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-23 (v1.31.6) - uid: 427e26ec
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=4627-4269
 */

data class ODSAccordionTextBodyTokens(
    val padding: ODSPadding,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val paragraphStyle: ODSTextStyle,
    val paragraphTextAlign: TextAlign
)

val defaultODSAccordionTextBodyTokens = ODSAccordionTextBodyTokens(
    padding = ODSPadding(
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent7
    ),
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    paragraphStyle = DSTextStyles.bodySRegular,
    paragraphTextAlign = TextAlign.Left
)

var DSAccordionTextBodyTokens: ODSAccordionTextBodyTokens = defaultODSAccordionTextBodyTokens
