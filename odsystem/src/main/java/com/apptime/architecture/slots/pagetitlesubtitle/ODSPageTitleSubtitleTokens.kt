package com.telekom.odsystem.slots.pagetitlesubtitle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSPageTitleSubtitleTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val odsPageTitleVerticalAlignment: Alignment.Vertical,
    val odsPageTitleHorizontalAlignment: Alignment.Horizontal,
    val odsPageTitleHorizontalArrangement: Arrangement.Horizontal,
    val pageTitleStyleTypeMainPageTitle: ODSTextStyle,
    val pageTitleStyleTypeSubPageTitle: ODSTextStyle,
    val pageTitleTextAlignTypeMainPageTitle: TextAlign,
    val pageTitleTextAlignTypeSubPageTitle: TextAlign,
    val pageTitleOverflowTypeMainPageTitleTruncation: TextOverflow,
    val pageTitleMaxLinesTypeMainPageTitleTruncation: Int,
    val odsPageSubtitleVerticalAlignment: Alignment.Vertical,
    val odsPageSubtitleHorizontalAlignment: Alignment.Horizontal,
    val odsPageSubtitleHorizontalArrangement: Arrangement.Horizontal,
    val pageSubtitleStyleTypeMainPageTitle: ODSTextStyle,
    val pageSubtitleStyleTypeSubPageTitle: ODSTextStyle,
    val pageSubtitleTextAlignTypeMainPageTitle: TextAlign,
    val pageSubtitleTextAlignTypeSubPageTitle: TextAlign,
    val pageSubtitleOverflowTypeMainPageTitleTruncation: TextOverflow,
    val pageSubtitleMaxLinesTypeMainPageTitleTruncation: Int
)

val defaultODSPageTitleSubtitleTokens = ODSPageTitleSubtitleTokens(
    gap = DSVariables.spacingComponent0,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Center,
    odsPageTitleVerticalAlignment = Alignment.CenterVertically,
    odsPageTitleHorizontalAlignment = Alignment.Start,
    odsPageTitleHorizontalArrangement = Arrangement.Start,
    pageTitleStyleTypeMainPageTitle = DSTextStyles.titleS,
    pageTitleStyleTypeSubPageTitle = DSTextStyles.bodyMBold,
    pageTitleTextAlignTypeMainPageTitle = TextAlign.Left,
    pageTitleTextAlignTypeSubPageTitle = TextAlign.Center,
    pageTitleOverflowTypeMainPageTitleTruncation = TextOverflow.Ellipsis,
    pageTitleMaxLinesTypeMainPageTitleTruncation = 1,
    odsPageSubtitleVerticalAlignment = Alignment.CenterVertically,
    odsPageSubtitleHorizontalAlignment = Alignment.Start,
    odsPageSubtitleHorizontalArrangement = Arrangement.Start,
    pageSubtitleStyleTypeMainPageTitle = DSTextStyles.bodyMRegular,
    pageSubtitleStyleTypeSubPageTitle = DSTextStyles.bodySRegular,
    pageSubtitleTextAlignTypeMainPageTitle = TextAlign.Left,
    pageSubtitleTextAlignTypeSubPageTitle = TextAlign.Center,
    pageSubtitleOverflowTypeMainPageTitleTruncation = TextOverflow.Ellipsis,
    pageSubtitleMaxLinesTypeMainPageTitleTruncation = 1
)

var DSPageTitleSubtitleTokens: ODSPageTitleSubtitleTokens = defaultODSPageTitleSubtitleTokens
