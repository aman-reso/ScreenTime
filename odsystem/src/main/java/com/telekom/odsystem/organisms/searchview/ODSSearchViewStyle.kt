package com.telekom.odsystem.organisms.searchview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSSearchViewStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var searchContainerVerticalAlignment: Alignment.Vertical? = null
    var searchContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var odsButtonContentAlignment: Alignment? = null
    var resultContainerVerticalAlignment: Alignment.Vertical? = null
    var resultContainerHorizontalAlignment: Alignment.Horizontal? = null
    var resultContainerVerticalArrangement: Arrangement.Vertical? = null
    fun getStyle(scheme: ODSTheme): ODSSearchViewStyle {
        val style = ODSSearchViewStyle()
        style.gap = DSSearchViewTokens.gap
        style.verticalAlignment = DSSearchViewTokens.verticalAlignment
        style.horizontalAlignment = DSSearchViewTokens.horizontalAlignment
        style.verticalArrangement = DSSearchViewTokens.verticalArrangement
        style.searchContainerVerticalAlignment = DSSearchViewTokens.searchContainerVerticalAlignment
        style.searchContainerHorizontalArrangement =
            DSSearchViewTokens.searchContainerHorizontalArrangement
        style.odsButtonContentAlignment = DSSearchViewTokens.odsButtonContentAlignment
        style.resultContainerVerticalAlignment = DSSearchViewTokens.resultContainerVerticalAlignment
        style.resultContainerHorizontalAlignment =
            DSSearchViewTokens.resultContainerHorizontalAlignment
        style.resultContainerVerticalArrangement =
            DSSearchViewTokens.resultContainerVerticalArrangement
        return style
    }
}
