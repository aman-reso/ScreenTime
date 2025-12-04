package com.telekom.odsystem.slots.productcardfeature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSProductCardFeatureTokens(
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val listContainerHorizontalGap: Dp,
    val listContainerVerticalGap: Dp,
    val listContainerVerticalAlignment: Alignment.Vertical,
    val listContainerHorizontalAlignment: Alignment.Horizontal,
    val listContainerHorizontalArrangement: Arrangement.Horizontal
)

val defaultODSProductCardFeatureTokens = ODSProductCardFeatureTokens(
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    listContainerHorizontalGap = DSVariables.spacingComponent2,
    listContainerVerticalGap = DSVariables.spacingComponent2,
    listContainerVerticalAlignment = Alignment.Top,
    listContainerHorizontalAlignment = Alignment.Start,
    listContainerHorizontalArrangement = Arrangement.Start
)

var DSProductCardFeatureTokens: ODSProductCardFeatureTokens = defaultODSProductCardFeatureTokens
