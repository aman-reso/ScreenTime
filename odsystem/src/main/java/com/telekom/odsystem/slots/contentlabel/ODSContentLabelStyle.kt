package com.telekom.odsystem.slots.contentlabel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSContentLabelStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var rightTextTextStyle: ODSTextStyle? = null
    var rightTextColor: HexColor? = null
    var rightTextTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSContentLabelStyle {
        var style = ODSContentLabelStyle()
        style.verticalAlignment = DSContentLabelTokens.verticalAlignment
        style.horizontalAlignment = DSContentLabelTokens.horizontalAlignment
        style.horizontalArrangement = DSContentLabelTokens.horizontalArrangement
        style.rightTextTextStyle = DSContentLabelTokens.rightTextTextStyle
        style.rightTextColor = scheme.basicText
        style.rightTextTextAlign = DSContentLabelTokens.rightTextTextAlign
        return style
    }
}
