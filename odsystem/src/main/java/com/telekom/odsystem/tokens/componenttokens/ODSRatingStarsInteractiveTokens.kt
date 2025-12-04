package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSRatingStarsInteractiveTokens(
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val labelContainerGap: Dp,
    val labelContainerPadding: ODSPadding,
    val labelContainerVerticalAlignment: Alignment.Vertical,
    val labelContainerHorizontalAlignment: Alignment.Horizontal,
    val labelContainerHorizontalArrangement: Arrangement.Horizontal,
    val ratingLabelTextStyle: ODSTextStyle,
    val ratingLabelTextAlign: TextAlign,
    val ratingGap: Dp,
    val ratingVerticalAlignment: Alignment.Vertical,
    val ratingHorizontalAlignment: Alignment.Horizontal,
    val ratingHorizontalArrangement: Arrangement.Horizontal,
    val starsListContainerVerticalAlignment: Alignment.Vertical,
    val starsListContainerHorizontalAlignment: Alignment.Horizontal,
    val starsListContainerHorizontalArrangement: Arrangement.Horizontal,
    val helperTextTextStyle: ODSTextStyle,
    val helperTextTextAlign: TextAlign
)

val defaultODSRatingStarsInteractiveTokens = ODSRatingStarsInteractiveTokens(
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    labelContainerGap = DSVariables.spacingComponent3,
    labelContainerPadding = ODSPadding(all = DSVariables.spacingComponent3),
    labelContainerVerticalAlignment = Alignment.CenterVertically,
    labelContainerHorizontalAlignment = Alignment.CenterHorizontally,
    labelContainerHorizontalArrangement = Arrangement.Center,
    ratingLabelTextStyle = DSTextStyles.titleS,
    ratingLabelTextAlign = TextAlign.Left,
    ratingGap = DSVariables.spacingComponent3,
    ratingVerticalAlignment = Alignment.CenterVertically,
    ratingHorizontalAlignment = Alignment.Start,
    ratingHorizontalArrangement = Arrangement.Start,
    starsListContainerVerticalAlignment = Alignment.CenterVertically,
    starsListContainerHorizontalAlignment = Alignment.Start,
    starsListContainerHorizontalArrangement = Arrangement.Start,
    helperTextTextStyle = DSTextStyles.bodySRegular,
    helperTextTextAlign = TextAlign.Left
)

var DSRatingStarsInteractiveTokens: ODSRatingStarsInteractiveTokens =
    defaultODSRatingStarsInteractiveTokens
