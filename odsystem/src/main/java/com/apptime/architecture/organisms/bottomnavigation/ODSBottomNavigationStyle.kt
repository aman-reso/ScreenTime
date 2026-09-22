package com.telekom.odsystem.organisms.bottomnavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-26 (v1.33.1) - uid: 38dca227
 * Figma link: https://figma.com/design/cpaNsDgmzEjyHilbYDSKS9/Exploration File_Part 2?node-id=2347-7802
 */

class ODSBottomNavigationStyle {
    var background: List<ODSColorModel>? = null
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var lineTopBackground: List<ODSColorModel>? = null
    var lineTopHeight: Dp? = null
    var lineTopClipContent: Boolean? = null
    var actionsVerticalAlignment: Alignment.Vertical? = null
    var actionsHorizontalAlignment: Alignment.Horizontal? = null
    var actionsHorizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(
        scheme: ODSTheme,
    ): ODSBottomNavigationStyle {
        val style = ODSBottomNavigationStyle()
        style.background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        style.minHeight = DSBottomNavigationTokens.minHeight
        style.verticalAlignment = DSBottomNavigationTokens.verticalAlignment
        style.horizontalAlignment = DSBottomNavigationTokens.horizontalAlignment
        style.verticalArrangement = DSBottomNavigationTokens.verticalArrangement
        style.lineTopBackground = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        style.lineTopHeight = DSBottomNavigationTokens.lineTopHeight
        style.lineTopClipContent = DSBottomNavigationTokens.lineTopClipContent
        style.actionsVerticalAlignment = DSBottomNavigationTokens.actionsVerticalAlignment
        style.actionsHorizontalAlignment = DSBottomNavigationTokens.actionsHorizontalAlignment
        style.actionsHorizontalArrangement = DSBottomNavigationTokens.actionsHorizontalArrangement
        return style
    }
}
