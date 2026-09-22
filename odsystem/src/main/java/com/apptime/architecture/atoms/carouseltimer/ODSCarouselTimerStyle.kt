package com.telekom.odsystem.atoms.carouseltimer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSCarouselTimerTokens

class ODSCarouselTimerStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(): ODSCarouselTimerStyle {
        val style = ODSCarouselTimerStyle()
        style.gap = DSCarouselTimerTokens.gap
        style.verticalAlignment = DSCarouselTimerTokens.verticalAlignment
        style.horizontalAlignment = DSCarouselTimerTokens.horizontalAlignment
        style.horizontalArrangement = DSCarouselTimerTokens.horizontalArrangement
        return style
    }
}
