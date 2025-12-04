package com.telekom.odsystem.atoms.chatloading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSChatLoadingStyle {
    var padding: ODSPadding? = null
    var width: Dp? = null
    var height: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSChatLoadingStyle {
        val style = ODSChatLoadingStyle()
        style.padding = DSChatLoadingTokens.padding
        style.width = DSChatLoadingTokens.width
        style.height = DSChatLoadingTokens.height
        style.verticalAlignment = DSChatLoadingTokens.verticalAlignment
        style.horizontalAlignment = DSChatLoadingTokens.horizontalAlignment
        style.verticalArrangement = DSChatLoadingTokens.verticalArrangement
        return style
    }
}
