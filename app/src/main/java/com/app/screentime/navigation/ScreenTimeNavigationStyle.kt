package com.app.screentime.navigation

import androidx.compose.ui.Alignment
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Style configuration for ScreenTime Navigation.
 * Currently minimal as navigation primarily uses ODS components.
 */
class ScreenTimeNavigationStyle {
    var scaffoldBackground: List<ODSColorModel>? = null
    var bottomBarAlignment: Alignment? = null

    fun getStyle(scheme: ODSTheme): ScreenTimeNavigationStyle {
        val style = ScreenTimeNavigationStyle()
        style.scaffoldBackground = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        style.bottomBarAlignment = Alignment.BottomCenter
        return style
    }
}

