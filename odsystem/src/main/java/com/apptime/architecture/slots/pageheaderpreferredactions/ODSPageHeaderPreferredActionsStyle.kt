package com.telekom.odsystem.slots.pageheaderpreferredactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSPageHeaderPreferredActionsStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSPageHeaderPreferredActionsStyle {
        val style = ODSPageHeaderPreferredActionsStyle()
        style.gap = DSPageHeaderPreferredActionsTokens.gap
        style.verticalAlignment = DSPageHeaderPreferredActionsTokens.verticalAlignment
        style.horizontalAlignment = DSPageHeaderPreferredActionsTokens.horizontalAlignment
        style.horizontalArrangement = DSPageHeaderPreferredActionsTokens.horizontalArrangement
        return style
    }
}
