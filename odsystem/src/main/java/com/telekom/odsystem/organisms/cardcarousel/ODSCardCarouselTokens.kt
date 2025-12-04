package com.telekom.odsystem.organisms.cardcarousel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.SCALE_FACTOR

data class ODSCardCarouselTokens(
    var gap: Dp,
    var width: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var pagerGap: Dp? = null, // Not exported by plugin
    var scaleFactor: Float?, // Not exported by plugin
)

var defaultODSCardCarouselTokens = ODSCardCarouselTokens(
    gap = DSVariables.spacingComponent1,
    width = 317.dp,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    scaleFactor = SCALE_FACTOR, // Not exported by plugin
    pagerGap = DSVariables.spacingComponent3 // Not exported by plugin
)

var DSCardCarouselTokens: ODSCardCarouselTokens = defaultODSCardCarouselTokens
