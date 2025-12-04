package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSSliderInputFieldTokens(
    var minHeight: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangement: Arrangement.Horizontal,
    var contentGap: Dp,
    var contentPadding: ODSPadding,
    var contentBorderRadius: ODSCorners,
    var contentBorder: Dp,
    var contentMinWidth: Dp,
    var contentVerticalAlignment: Alignment.Vertical,
    var contentHorizontalAlignment: Alignment.Horizontal,
    var contentHorizontalArrangement: Arrangement.Horizontal,
    var inputValueVerticalAlignment: Alignment.Vertical,
    var inputValueHorizontalAlignment: Alignment.Horizontal,
    var inputValueHorizontalArrangement: Arrangement.Horizontal,
    var prefixTextStyle: ODSTextStyle,
    var prefixTextAlign: TextAlign,
    var prefixTextOverflow: TextOverflow,
    var inputValueTextStyle: ODSTextStyle,
    var inputValueTextAlign: TextAlign,
    var inputValueTextOverflow: TextOverflow
)

var defaultODSSliderInputFieldTokens = ODSSliderInputFieldTokens(
    minHeight = DSVariables.sizingMinimumTappableArea,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    contentGap = DSVariables.spacingComponent2,
    contentPadding = ODSPadding(
        top = DSVariables.spacingComponent3,
        bottom = DSVariables.spacingComponent3,
        left = DSVariables.spacingComponent4,
        right = DSVariables.spacingComponent4
    ),
    contentBorderRadius = ODSCorners(all = DSVariables.radiusSmall),
    contentBorder = DSVariables.strokes1,
    contentMinWidth = DSVariables.sizingComponent18,
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.Start,
    contentHorizontalArrangement = Arrangement.Start,
    inputValueVerticalAlignment = Alignment.CenterVertically,
    inputValueHorizontalAlignment = Alignment.Start,
    inputValueHorizontalArrangement = Arrangement.Start,
    prefixTextStyle = DSTextStyles.bodyMBold,
    prefixTextAlign = TextAlign.Left,
    prefixTextOverflow = TextOverflow.Ellipsis,
    inputValueTextStyle = DSTextStyles.bodyMBold,
    inputValueTextAlign = TextAlign.Left,
    inputValueTextOverflow = TextOverflow.Ellipsis
)

var DSSliderInputFieldTokens: ODSSliderInputFieldTokens = defaultODSSliderInputFieldTokens
