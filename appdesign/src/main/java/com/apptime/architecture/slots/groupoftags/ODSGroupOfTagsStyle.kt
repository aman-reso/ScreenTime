package com.telekom.odsystem.slots.groupoftags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSPadding

class ODSGroupOfTagsStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(): ODSGroupOfTagsStyle {
        var style = ODSGroupOfTagsStyle()
        style.gap = DSGroupOfTagsTokens.gap
        style.padding = DSGroupOfTagsTokens.padding
        style.verticalAlignment = DSGroupOfTagsTokens.verticalAlignment
        style.horizontalAlignment = DSGroupOfTagsTokens.horizontalAlignment
        style.horizontalArrangement = DSGroupOfTagsTokens.horizontalArrangement
        return style
    }
}
