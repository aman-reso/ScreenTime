package com.telekom.odsystem.slots.cardwidgetpreferredcontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-10-10 (v1.33.1) - uid: aac0bf
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=141-12150
 */

data class ODSCardWidgetPreferredContentTokens(
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val titleStyle: ODSTextStyle,
    val titleTextAlign: TextAlign,
    val subtitleStyle: ODSTextStyle,
    val subtitleTextAlign: TextAlign,
)

val defaultODSCardWidgetPreferredContentTokens = ODSCardWidgetPreferredContentTokens(
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    titleStyle = DSTextStyles.bodyMBold,
    titleTextAlign = TextAlign.Left,
    subtitleStyle = DSTextStyles.bodyMBold,
    subtitleTextAlign = TextAlign.Left
)

var DSCardWidgetPreferredContentTokens: ODSCardWidgetPreferredContentTokens =
    defaultODSCardWidgetPreferredContentTokens
