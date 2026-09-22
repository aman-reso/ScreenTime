package com.telekom.odsystem.slots.cardcontentbasic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSCardContentBasicTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSCardContentBasicStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var labelHeadingGap: Dp? = null
    var labelHeadingVerticalAlignment: Alignment.Vertical? = null
    var labelHeadingHorizontalAlignment: Alignment.Horizontal? = null
    var labelHeadingVerticalArrangement: Arrangement.Vertical? = null
    var contentGap: Dp? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentVerticalArrangement: Arrangement.Vertical? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var headingTextStyle: ODSTextStyle? = null
    var headingColor: HexColor? = null
    var headingTextAlign: TextAlign? = null
    var subtitleTextStyle: ODSTextStyle? = null
    var subtitleColor: HexColor? = null
    var subtitleTextAlign: TextAlign? = null
    var bodyTextTextStyle: ODSTextStyle? = null
    var bodyTextColor: HexColor? = null
    var bodyTextTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSCardContentBasicStyle {
        var style = ODSCardContentBasicStyle()
        style.gap = DSCardContentBasicTokens.gap
        style.verticalAlignment = DSCardContentBasicTokens.verticalAlignment
        style.horizontalAlignment = DSCardContentBasicTokens.horizontalAlignment
        style.verticalArrangement = DSCardContentBasicTokens.verticalArrangement
        style.labelHeadingGap = DSCardContentBasicTokens.labelHeadingGap
        style.labelHeadingVerticalAlignment = DSCardContentBasicTokens.labelHeadingVerticalAlignment
        style.labelHeadingHorizontalAlignment =
            DSCardContentBasicTokens.labelHeadingHorizontalAlignment
        style.labelHeadingVerticalArrangement =
            DSCardContentBasicTokens.labelHeadingVerticalArrangement
        style.contentGap = DSCardContentBasicTokens.contentGap
        style.contentVerticalAlignment = DSCardContentBasicTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSCardContentBasicTokens.contentHorizontalAlignment
        style.contentVerticalArrangement = DSCardContentBasicTokens.contentVerticalArrangement
        style.labelTextStyle = DSCardContentBasicTokens.labelTextStyle
        style.labelColor = scheme.basicText
        style.labelTextAlign = DSCardContentBasicTokens.labelTextAlign
        style.headingTextStyle = DSCardContentBasicTokens.headingTextStyle
        style.headingColor = scheme.basicText
        style.headingTextAlign = DSCardContentBasicTokens.headingTextAlign
        style.subtitleTextStyle = DSCardContentBasicTokens.subtitleTextStyle
        style.subtitleColor = scheme.basicText
        style.subtitleTextAlign = DSCardContentBasicTokens.subtitleTextAlign
        style.bodyTextTextStyle = DSCardContentBasicTokens.bodyTextTextStyle
        style.bodyTextColor = scheme.basicText
        style.bodyTextTextAlign = DSCardContentBasicTokens.bodyTextTextAlign
        return style
    }
}
