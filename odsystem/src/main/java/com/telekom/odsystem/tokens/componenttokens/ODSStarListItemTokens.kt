package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSStarListItemTokens(
    val width: Dp,
    val height: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val starWidth: Dp,
    val starHeight: Dp
)

val defaultODSStarListItemTokens = ODSStarListItemTokens(
    width = DSVariables.sizingComponent14,
    height = DSVariables.sizingComponent14,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    starWidth = DSVariables.sizingComponent12,
    starHeight = DSVariables.sizingComponent12
)

var DSStarListItemTokens: ODSStarListItemTokens = defaultODSStarListItemTokens
