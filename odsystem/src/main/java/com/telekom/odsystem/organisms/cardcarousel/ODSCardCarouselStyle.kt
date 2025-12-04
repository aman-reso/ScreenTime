package com.telekom.odsystem.organisms.cardcarousel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSCardCarouselStyle {
    var gap: Dp? = null
    var width: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var pagerGap: Dp? = null // Not exported by plugin.
    var scaleFactor: Float? = null // Not exported by plugin.
    fun getStyle(
        scheme: ODSTheme,
        state: ODSActions // Not exported by plugin.
    ): ODSCardCarouselStyle {
        var style = ODSCardCarouselStyle()
        style.gap = DSCardCarouselTokens.gap
        style.width = DSCardCarouselTokens.width
        style.verticalAlignment = DSCardCarouselTokens.verticalAlignment
        style.horizontalAlignment = DSCardCarouselTokens.horizontalAlignment
        style.verticalArrangement = DSCardCarouselTokens.verticalArrangement

        // Not exported by plugin.
        if (state == ODSActions.HOVERED) {
            style.scaleFactor = DSCardCarouselTokens.scaleFactor
        } else {
            style.scaleFactor = DEFAULT_FACTOR
        }
        // Not exported by plugin
        style.pagerGap = DSCardCarouselTokens.pagerGap
        return style
    }
}
