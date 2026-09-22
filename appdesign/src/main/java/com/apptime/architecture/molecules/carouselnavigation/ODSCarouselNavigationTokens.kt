package com.telekom.odsystem.molecules.carouselnavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSCarouselNavigationTokens(
    var gap: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangement: Arrangement.Horizontal,
    var containerGap: Dp,
    var containerVerticalAlignment: Alignment.Vertical,
    var containerHorizontalAlignment: Alignment.Horizontal,
    var containerHorizontalArrangement: Arrangement.Horizontal
)

var defaultODSCarouselNavigationTokens = ODSCarouselNavigationTokens(
    gap = DSVariables.spacingComponent1,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    containerGap = DSVariables.spacingComponent3,
    containerVerticalAlignment = Alignment.CenterVertically,
    containerHorizontalAlignment = Alignment.CenterHorizontally,
    containerHorizontalArrangement = Arrangement.Center
)

var DSCarouselNavigationTokens: ODSCarouselNavigationTokens = defaultODSCarouselNavigationTokens
