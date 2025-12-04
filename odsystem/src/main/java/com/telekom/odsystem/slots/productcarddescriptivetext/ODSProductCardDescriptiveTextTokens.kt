package com.telekom.odsystem.slots.productcarddescriptivetext

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSProductCardDescriptiveTextTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val headingStyle: ODSTextStyle,
    val headingTextAlign: TextAlign,
    val bodyTextStyle: ODSTextStyle,
    val bodyTextTextAlign: TextAlign,
    val listContainerGap: Dp,
    val listContainerVerticalAlignment: Alignment.Vertical,
    val listContainerHorizontalAlignment: Alignment.Horizontal,
    val listContainerVerticalArrangement: Arrangement.Vertical
)

val defaultODSProductCardDescriptiveTextTokens = ODSProductCardDescriptiveTextTokens(
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Center,
    headingStyle = DSTextStyles.subtitle,
    headingTextAlign = TextAlign.Left,
    bodyTextStyle = DSTextStyles.bodyMRegular,
    bodyTextTextAlign = TextAlign.Left,
    listContainerGap = DSVariables.spacingComponent0,
    listContainerVerticalAlignment = Alignment.CenterVertically,
    listContainerHorizontalAlignment = Alignment.Start,
    listContainerVerticalArrangement = Arrangement.Center
)

var DSProductCardDescriptiveTextTokens: ODSProductCardDescriptiveTextTokens =
    defaultODSProductCardDescriptiveTextTokens
