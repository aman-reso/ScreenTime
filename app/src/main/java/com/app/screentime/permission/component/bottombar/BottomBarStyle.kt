package com.app.screentime.permission.component.bottombar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Style configuration for BottomBar component.
 */
class BottomBarStyle {
    var separatorColor: HexColor? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var padding: ODSPadding? = null

    fun getStyle(scheme: ODSTheme): BottomBarStyle {
        val style = BottomBarStyle()
        style.separatorColor = scheme.basicStroke
        style.horizontalAlignment = Alignment.CenterHorizontally
        style.verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent5)
        style.horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent5)
        style.padding = ODSPadding(all = DSVariables.spacingComponent3)
        return style
    }
}

