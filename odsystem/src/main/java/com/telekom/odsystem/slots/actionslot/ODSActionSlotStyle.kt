package com.telekom.odsystem.slots.actionslot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSActionSlotStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSActionSlotStyle {
        val style = ODSActionSlotStyle()
        style.verticalAlignment = DSActionSlotTokens.verticalAlignment
        style.horizontalAlignment = DSActionSlotTokens.horizontalAlignment
        style.horizontalArrangement = DSActionSlotTokens.horizontalArrangement
        return style
    }
}
