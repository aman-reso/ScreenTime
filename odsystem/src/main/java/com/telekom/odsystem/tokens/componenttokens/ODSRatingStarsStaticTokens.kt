package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSRatingStarsStaticTokens(
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var labelContainerGap: Dp,
    var labelContainerPadding: ODSPadding,
    var labelContainerVerticalAlignment: Alignment.Vertical,
    var labelContainerHorizontalAlignment: Alignment.Horizontal,
    var labelContainerHorizontalArrangement: Arrangement.Horizontal,
    var ratingGap: Dp,
    var ratingVerticalAlignment: Alignment.Vertical,
    var ratingHorizontalAlignment: Alignment.Horizontal,
    var ratingHorizontalArrangement: Arrangement.Horizontal,
    var starsListContainerGap: Dp,
    var starsListContainerVerticalAlignment: Alignment.Vertical,
    var starsListContainerHorizontalAlignment: Alignment.Horizontal,
    var starsListContainerHorizontalArrangement: Arrangement.Horizontal,
    var starWidth: Dp,
    var starHeight: Dp,
    var helperTextTextStyle: ODSTextStyle,
    var helperTextTextAlign: TextAlign,
    var ratingLabelTextStyle: ODSTextStyle,
    var ratingLabelTextAlign: TextAlign
)

var defaultODSRatingStarsStaticTokens = ODSRatingStarsStaticTokens(
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    labelContainerGap = DSVariables.spacingComponent3,
    labelContainerPadding = ODSPadding(
        top = DSVariables.spacingComponent3,
        bottom = DSVariables.spacingComponent3,
        left = DSVariables.spacingComponent1,
        right = DSVariables.spacingComponent1
    ),
    labelContainerVerticalAlignment = Alignment.CenterVertically,
    labelContainerHorizontalAlignment = Alignment.CenterHorizontally,
    labelContainerHorizontalArrangement = Arrangement.Center,
    ratingGap = DSVariables.spacingComponent3,
    ratingVerticalAlignment = Alignment.CenterVertically,
    ratingHorizontalAlignment = Alignment.Start,
    ratingHorizontalArrangement = Arrangement.Start,
    starsListContainerGap = DSVariables.spacingComponent2,
    starsListContainerVerticalAlignment = Alignment.Top,
    starsListContainerHorizontalAlignment = Alignment.Start,
    starsListContainerHorizontalArrangement = Arrangement.Start,
    starWidth = DSVariables.sizingComponent8,
    starHeight = DSVariables.sizingComponent8,
    helperTextTextStyle = DSTextStyles.bodySRegular,
    helperTextTextAlign = TextAlign.Left,
    ratingLabelTextStyle = DSTextStyles.bodyMBold,
    ratingLabelTextAlign = TextAlign.Left
)

var DSRatingStarsStaticTokens: ODSRatingStarsStaticTokens = defaultODSRatingStarsStaticTokens
