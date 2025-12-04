package com.telekom.odsystem.molecules.carouselnavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSCarouselNavigationStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var containerGap: Dp? = null
    var containerVerticalAlignment: Alignment.Vertical? = null
    var containerHorizontalAlignment: Alignment.Horizontal? = null
    var containerHorizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSCarouselNavigationStyle {
        var style = ODSCarouselNavigationStyle()
        style.gap = DSCarouselNavigationTokens.gap
        style.verticalAlignment = DSCarouselNavigationTokens.verticalAlignment
        style.horizontalAlignment = DSCarouselNavigationTokens.horizontalAlignment
        style.horizontalArrangement = DSCarouselNavigationTokens.horizontalArrangement
        style.containerGap = DSCarouselNavigationTokens.containerGap
        style.containerVerticalAlignment = DSCarouselNavigationTokens.containerVerticalAlignment
        style.containerHorizontalAlignment = DSCarouselNavigationTokens.containerHorizontalAlignment
        style.containerHorizontalArrangement =
            DSCarouselNavigationTokens.containerHorizontalArrangement
        return style
    }
}
