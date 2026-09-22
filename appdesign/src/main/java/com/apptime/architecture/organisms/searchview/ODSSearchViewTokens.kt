package com.telekom.odsystem.organisms.searchview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSSearchViewTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val searchContainerVerticalAlignment: Alignment.Vertical,
    val searchContainerHorizontalArrangement: Arrangement.Horizontal,
    val odsButtonContentAlignment: Alignment,
    val resultContainerVerticalAlignment: Alignment.Vertical,
    val resultContainerHorizontalAlignment: Alignment.Horizontal,
    val resultContainerVerticalArrangement: Arrangement.Vertical
)

val defaultODSSearchViewTokens = ODSSearchViewTokens(
    gap = DSVariables.spacingComponent4,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    searchContainerVerticalAlignment = Alignment.CenterVertically,
    searchContainerHorizontalArrangement = Arrangement.SpaceBetween,
    odsButtonContentAlignment = Alignment.TopStart,
    resultContainerVerticalAlignment = Alignment.Top,
    resultContainerHorizontalAlignment = Alignment.Start,
    resultContainerVerticalArrangement = Arrangement.Top
)

var DSSearchViewTokens: ODSSearchViewTokens = defaultODSSearchViewTokens
