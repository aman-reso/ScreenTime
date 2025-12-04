package com.telekom.odsystem.slots.popoverpreferredactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSPopoverPreferredActionsTokens

class ODSPopoverPreferredActionsStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(): ODSPopoverPreferredActionsStyle {
        var style = ODSPopoverPreferredActionsStyle()
        style.gap = DSPopoverPreferredActionsTokens.gap
        style.verticalAlignment = DSPopoverPreferredActionsTokens.verticalAlignment
        style.horizontalAlignment = DSPopoverPreferredActionsTokens.horizontalAlignment
        style.horizontalArrangement = DSPopoverPreferredActionsTokens.horizontalArrangement
        return style
    }
}
