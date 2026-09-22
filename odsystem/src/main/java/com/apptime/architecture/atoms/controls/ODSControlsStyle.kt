package com.telekom.odsystem.atoms.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSControlsTokens
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSControlsStyle {
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSControlsStyle {
        val style = ODSControlsStyle()
        style.minHeight = DSControlsTokens.minHeight
        style.minWidth = DSControlsTokens.minWidth
        style.verticalAlignment = DSControlsTokens.verticalAlignment
        style.horizontalAlignment = DSControlsTokens.horizontalAlignment
        style.horizontalArrangement = DSControlsTokens.horizontalArrangement
        return style
    }
}
