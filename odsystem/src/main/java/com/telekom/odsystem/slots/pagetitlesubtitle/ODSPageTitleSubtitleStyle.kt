package com.telekom.odsystem.slots.pagetitlesubtitle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSPageTitleSubtitleStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var odsPageTitleVerticalAlignment: Alignment.Vertical? = null
    var odsPageTitleHorizontalAlignment: Alignment.Horizontal? = null
    var odsPageTitleHorizontalArrangement: Arrangement.Horizontal? = null
    var pageTitleStyle: ODSTextStyle? = null
    var pageTitleColor: HexColor? = null
    var pageTitleTextAlign: TextAlign? = null
    var pageTitleOverflow: TextOverflow? = null
    var pageTitleMaxLines: Int? = null
    var odsPageSubtitleVerticalAlignment: Alignment.Vertical? = null
    var odsPageSubtitleHorizontalAlignment: Alignment.Horizontal? = null
    var odsPageSubtitleHorizontalArrangement: Arrangement.Horizontal? = null
    var pageSubtitleStyle: ODSTextStyle? = null
    var pageSubtitleColor: HexColor? = null
    var pageSubtitleTextAlign: TextAlign? = null
    var pageSubtitleOverflow: TextOverflow? = null
    var pageSubtitleMaxLines: Int? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSPageTitleSubtitleProps
    ): ODSPageTitleSubtitleStyle {
        val style = ODSPageTitleSubtitleStyle()
        style.gap = DSPageTitleSubtitleTokens.gap
        style.verticalAlignment = DSPageTitleSubtitleTokens.verticalAlignment
        style.horizontalAlignment = DSPageTitleSubtitleTokens.horizontalAlignment
        style.verticalArrangement = DSPageTitleSubtitleTokens.verticalArrangement
        style.odsPageTitleVerticalAlignment =
            DSPageTitleSubtitleTokens.odsPageTitleVerticalAlignment
        style.odsPageTitleHorizontalAlignment =
            DSPageTitleSubtitleTokens.odsPageTitleHorizontalAlignment
        style.odsPageTitleHorizontalArrangement =
            DSPageTitleSubtitleTokens.odsPageTitleHorizontalArrangement
        style.pageTitleColor = scheme.basicText
        if (props.type == ODSPageTitleSubtitleType.MAIN_PAGE_TITLE) {
            style.pageTitleStyle = DSPageTitleSubtitleTokens.pageTitleStyleTypeMainPageTitle
            style.pageTitleTextAlign = DSPageTitleSubtitleTokens.pageTitleTextAlignTypeMainPageTitle
        }
        if (props.type == ODSPageTitleSubtitleType.SUB_PAGE_TITLE && !props.truncation) {
            style.pageTitleStyle = DSPageTitleSubtitleTokens.pageTitleStyleTypeSubPageTitle
            style.pageTitleTextAlign = DSPageTitleSubtitleTokens.pageTitleTextAlignTypeSubPageTitle
        }
        if (props.type == ODSPageTitleSubtitleType.MAIN_PAGE_TITLE && props.truncation) {
            style.pageTitleOverflow =
                DSPageTitleSubtitleTokens.pageTitleOverflowTypeMainPageTitleTruncation
            style.pageTitleMaxLines =
                DSPageTitleSubtitleTokens.pageTitleMaxLinesTypeMainPageTitleTruncation
        }
        style.odsPageSubtitleVerticalAlignment =
            DSPageTitleSubtitleTokens.odsPageSubtitleVerticalAlignment
        style.odsPageSubtitleHorizontalAlignment =
            DSPageTitleSubtitleTokens.odsPageSubtitleHorizontalAlignment
        style.odsPageSubtitleHorizontalArrangement =
            DSPageTitleSubtitleTokens.odsPageSubtitleHorizontalArrangement
        style.pageSubtitleColor = scheme.basicText
        if (props.type == ODSPageTitleSubtitleType.MAIN_PAGE_TITLE) {
            style.pageSubtitleStyle = DSPageTitleSubtitleTokens.pageSubtitleStyleTypeMainPageTitle
            style.pageSubtitleTextAlign =
                DSPageTitleSubtitleTokens.pageSubtitleTextAlignTypeMainPageTitle
        }
        if (props.type == ODSPageTitleSubtitleType.SUB_PAGE_TITLE && !props.truncation) {
            style.pageSubtitleStyle = DSPageTitleSubtitleTokens.pageSubtitleStyleTypeSubPageTitle
            style.pageSubtitleTextAlign =
                DSPageTitleSubtitleTokens.pageSubtitleTextAlignTypeSubPageTitle
        }
        if (props.type == ODSPageTitleSubtitleType.MAIN_PAGE_TITLE && props.truncation) {
            style.pageSubtitleOverflow =
                DSPageTitleSubtitleTokens.pageSubtitleOverflowTypeMainPageTitleTruncation
            style.pageSubtitleMaxLines =
                DSPageTitleSubtitleTokens.pageSubtitleMaxLinesTypeMainPageTitleTruncation
        }
        return style
    }
}
