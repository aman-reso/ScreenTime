package com.telekom.odsystem.atoms.carouseldot

import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners

data class ODSCarouselDotTokens(
    var borderRadius: ODSCorners,
    var border: Dp,
    var width: Dp,
    var height: Dp,
    var clipContent: Boolean
)

var defaultODSCarouselDotTokens = ODSCarouselDotTokens(
    borderRadius = ODSCorners(all = DSVariables.radiusFull),
    border = DSVariables.strokes1,
    width = DSVariables.sizingComponent4,
    height = DSVariables.sizingComponent4,
    clipContent = true
)

var DSCarouselDotTokens: ODSCarouselDotTokens = defaultODSCarouselDotTokens
