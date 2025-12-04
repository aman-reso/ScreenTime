package com.telekom.odsystem.atoms.chattextincoming

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSChatTextIncomingStyle {
    var padding: ODSPadding? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var textStyle: ODSTextStyle? = null
    var textColor: HexColor? = null
    var textTextAlign: TextAlign? = null
    var textMaxWidth: Dp? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSChatTextIncomingStyle {
        val style = ODSChatTextIncomingStyle()
        style.padding = DSChatTextIncomingTokens.padding
        style.verticalAlignment = DSChatTextIncomingTokens.verticalAlignment
        style.horizontalAlignment = DSChatTextIncomingTokens.horizontalAlignment
        style.horizontalArrangement = DSChatTextIncomingTokens.horizontalArrangement
        style.textStyle = DSChatTextIncomingTokens.textStyle
        style.textColor = scheme.basicText
        style.textTextAlign = DSChatTextIncomingTokens.textTextAlign
        style.textMaxWidth = DSChatTextIncomingTokens.textMaxWidth
        return style
    }
}
