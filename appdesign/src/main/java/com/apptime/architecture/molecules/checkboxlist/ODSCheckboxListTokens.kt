package com.telekom.odsystem.molecules.checkboxlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSCheckboxListTokens(
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var listContainerGapSizeLarge: Dp,
    var listContainerGapSizeSmall: Dp,
    var listContainerVerticalAlignment: Alignment.Vertical,
    var listContainerHorizontalAlignment: Alignment.Horizontal,
    var listContainerVerticalArrangement: Arrangement.Vertical
)

var defaultODSCheckboxListTokens = ODSCheckboxListTokens(
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    listContainerGapSizeLarge = DSVariables.spacingComponent1,
    listContainerGapSizeSmall = DSVariables.spacingComponent0,
    listContainerVerticalAlignment = Alignment.Top,
    listContainerHorizontalAlignment = Alignment.Start,
    listContainerVerticalArrangement = Arrangement.Top
)

var DSCheckboxListTokens: ODSCheckboxListTokens = defaultODSCheckboxListTokens
