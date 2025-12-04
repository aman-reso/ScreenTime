package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSCardContentBasicTokens(
    var gap: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var labelHeadingGap: Dp,
    var labelHeadingVerticalAlignment: Alignment.Vertical,
    var labelHeadingHorizontalAlignment: Alignment.Horizontal,
    var labelHeadingVerticalArrangement: Arrangement.Vertical,
    var contentGap: Dp,
    var contentVerticalAlignment: Alignment.Vertical,
    var contentHorizontalAlignment: Alignment.Horizontal,
    var contentVerticalArrangement: Arrangement.Vertical,
    var labelTextStyle: ODSTextStyle,
    var labelTextAlign: TextAlign,
    var headingTextStyle: ODSTextStyle,
    var headingTextAlign: TextAlign,
    var subtitleTextStyle: ODSTextStyle,
    var subtitleTextAlign: TextAlign,
    var bodyTextTextStyle: ODSTextStyle,
    var bodyTextTextAlign: TextAlign
)

var defaultODSCardContentBasicTokens = ODSCardContentBasicTokens(
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Center,
    labelHeadingGap = DSVariables.spacingComponent3,
    labelHeadingVerticalAlignment = Alignment.CenterVertically,
    labelHeadingHorizontalAlignment = Alignment.Start,
    labelHeadingVerticalArrangement = Arrangement.Center,
    contentGap = DSVariables.spacingComponent5,
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.Start,
    contentVerticalArrangement = Arrangement.Center,
    labelTextStyle = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Left,
    headingTextStyle = DSTextStyles.titleS,
    headingTextAlign = TextAlign.Left,
    subtitleTextStyle = DSTextStyles.bodyL,
    subtitleTextAlign = TextAlign.Left,
    bodyTextTextStyle = DSTextStyles.bodySRegular,
    bodyTextTextAlign = TextAlign.Left
)

var DSCardContentBasicTokens: ODSCardContentBasicTokens = defaultODSCardContentBasicTokens
