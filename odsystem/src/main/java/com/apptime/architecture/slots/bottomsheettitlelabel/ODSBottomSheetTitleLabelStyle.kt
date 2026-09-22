package com.telekom.odsystem.slots.bottomsheettitlelabel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.componenttokens.DSBottomSheetTitleLabelTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSBottomSheetTitleLabelStyle {
    var padding: ODSPadding? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var titleLabelTextStyle: ODSTextStyle? = null
    var titleLabelColor: HexColor? = null
    var titleLabelTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSBottomSheetTitleLabelStyle {
        var style = ODSBottomSheetTitleLabelStyle()
        style.padding = DSBottomSheetTitleLabelTokens.padding
        style.verticalAlignment = DSBottomSheetTitleLabelTokens.verticalAlignment
        style.horizontalAlignment = DSBottomSheetTitleLabelTokens.horizontalAlignment
        style.horizontalArrangement = DSBottomSheetTitleLabelTokens.horizontalArrangement
        style.titleLabelTextStyle = DSBottomSheetTitleLabelTokens.titleLabelTextStyle
        style.titleLabelColor = scheme.basicText
        style.titleLabelTextAlign = DSBottomSheetTitleLabelTokens.titleLabelTextAlign
        return style
    }
}
