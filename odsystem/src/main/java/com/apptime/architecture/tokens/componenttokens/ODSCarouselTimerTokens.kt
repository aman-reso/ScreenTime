package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSCarouselTimerTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal
)

val defaultODSCarouselTimerTokens = ODSCarouselTimerTokens(
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start
)

var DSCarouselTimerTokens: ODSCarouselTimerTokens = defaultODSCarouselTimerTokens
