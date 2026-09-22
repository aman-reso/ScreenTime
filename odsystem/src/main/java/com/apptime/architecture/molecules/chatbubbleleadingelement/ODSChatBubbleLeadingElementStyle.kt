package com.telekom.odsystem.molecules.chatbubbleleadingelement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSChatBubbleLeadingElementStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSChatBubbleLeadingElementStyle {
        val style = ODSChatBubbleLeadingElementStyle()
        style.verticalAlignment = DSChatBubbleLeadingElementTokens.verticalAlignment
        style.horizontalAlignment = DSChatBubbleLeadingElementTokens.horizontalAlignment
        style.horizontalArrangement = DSChatBubbleLeadingElementTokens.horizontalArrangement
        return style
    }
}
