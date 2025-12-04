package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSControlsTokens(
    val minHeight: Dp,
    val minWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal
)

val defaultODSControlsTokens = ODSControlsTokens(
    minHeight = DSVariables.sizingMinimumTappableArea,
    minWidth = DSVariables.sizingMinimumTappableArea,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.End,
    horizontalArrangement = Arrangement.End
)

var DSControlsTokens: ODSControlsTokens = defaultODSControlsTokens
