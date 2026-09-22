package com.telekom.odsystem.componenttokens

import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSLogoTokens(
    var minHeight: Dp,
    var minWidth: Dp,
    var width: Dp,
    var height: Dp
)

var defaultODSLogoTokens = ODSLogoTokens(
    minHeight = DSVariables.sizingMinimumTappableArea,
    minWidth = DSVariables.sizingMinimumTappableArea,
    width = DSVariables.sizingComponent14,
    height = DSVariables.sizingComponent14
)

var DSLogoTokens: ODSLogoTokens = defaultODSLogoTokens
