package com.telekom.odsystem.molecules.searchresultlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSSearchResultListStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var labelContainerPadding: ODSPadding? = null
    var labelContainerVerticalAlignment: Alignment.Vertical? = null
    var labelContainerHorizontalAlignment: Alignment.Horizontal? = null
    var labelContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var resultListContainerGap: Dp? = null
    var resultListContainerVerticalAlignment: Alignment.Vertical? = null
    var resultListContainerHorizontalAlignment: Alignment.Horizontal? = null
    var resultListContainerVerticalArrangement: Arrangement.Vertical? = null
    var odsResultItemContentAlignment: Alignment? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSSearchResultListStyle {
        val style = ODSSearchResultListStyle()
        style.gap = DSSearchResultListTokens.gap
        style.verticalAlignment = DSSearchResultListTokens.verticalAlignment
        style.horizontalAlignment = DSSearchResultListTokens.horizontalAlignment
        style.verticalArrangement = DSSearchResultListTokens.verticalArrangement
        style.labelContainerPadding = DSSearchResultListTokens.labelContainerPadding
        style.labelContainerVerticalAlignment =
            DSSearchResultListTokens.labelContainerVerticalAlignment
        style.labelContainerHorizontalAlignment =
            DSSearchResultListTokens.labelContainerHorizontalAlignment
        style.labelContainerHorizontalArrangement =
            DSSearchResultListTokens.labelContainerHorizontalArrangement
        style.labelTextStyle = DSSearchResultListTokens.labelTextStyle
        style.labelColor = scheme.basicText
        style.labelTextAlign = DSSearchResultListTokens.labelTextAlign
        style.resultListContainerGap = DSSearchResultListTokens.resultListContainerGap
        style.resultListContainerVerticalAlignment =
            DSSearchResultListTokens.resultListContainerVerticalAlignment
        style.resultListContainerHorizontalAlignment =
            DSSearchResultListTokens.resultListContainerHorizontalAlignment
        style.resultListContainerVerticalArrangement =
            DSSearchResultListTokens.resultListContainerVerticalArrangement
        style.odsResultItemContentAlignment = DSSearchResultListTokens.odsResultItemContentAlignment
        return style
    }
}
