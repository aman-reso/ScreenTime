package com.telekom.odsystem.atoms.chattextoutgoing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSChatTextOutgoingStyle {
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
    ): ODSChatTextOutgoingStyle {
        val style = ODSChatTextOutgoingStyle()
        style.padding = DSChatTextOutgoingTokens.padding
        style.verticalAlignment = DSChatTextOutgoingTokens.verticalAlignment
        style.horizontalAlignment = DSChatTextOutgoingTokens.horizontalAlignment
        style.horizontalArrangement = DSChatTextOutgoingTokens.horizontalArrangement
        style.textStyle = DSChatTextOutgoingTokens.textStyle
        style.textColor = scheme.basicTextOnAccentSecondary
        style.textTextAlign = DSChatTextOutgoingTokens.textTextAlign
        style.textMaxWidth = DSChatTextOutgoingTokens.textMaxWidth
        return style
    }
}
