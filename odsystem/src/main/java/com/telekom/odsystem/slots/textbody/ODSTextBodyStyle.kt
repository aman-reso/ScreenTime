package com.telekom.odsystem.slots.textbody

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.componenttokens.DSTextBodyTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSTextBodyStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var paragraphTextStyle: ODSTextStyle? = null
    var paragraphColor: HexColor? = null
    var paragraphTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSTextBodyStyle {
        var style = ODSTextBodyStyle()
        style.verticalAlignment = DSTextBodyTokens.verticalAlignment
        style.horizontalAlignment = DSTextBodyTokens.horizontalAlignment
        style.verticalArrangement = DSTextBodyTokens.verticalArrangement
        style.paragraphTextStyle = DSTextBodyTokens.paragraphTextStyle
        style.paragraphColor = scheme.basicText
        style.paragraphTextAlign = DSTextBodyTokens.paragraphTextAlign
        return style
    }
}
