package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSSliderTokens(
    var gap: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var sliderContainerGap: Dp,
    var sliderContainerVerticalAlignment: Alignment.Vertical,
    var sliderContainerHorizontalAlignment: Alignment.Horizontal,
    var sliderContainerVerticalArrangement: Arrangement.Vertical,
    var trackLabelsGap: Dp,
    var trackLabelsVerticalAlignment: Alignment.Vertical,
    var trackLabelsHorizontalAlignment: Alignment.Horizontal,
    var trackLabelsVerticalArrangement: Arrangement.Vertical,
    var trackContainerPadding: ODSPadding,
    var trackContainerBorderRadius: ODSCorners,
    var trackContainerHeight: Dp,
    var trackContainerVerticalAlignment: Alignment.Vertical,
    var trackContainerHorizontalArrangement: Arrangement.Horizontal,
    var indicatorBorderRadius: ODSCorners,
    var indicatorWidth: Dp,
    var indicatorHeight: Dp,
    var indicatorClipContent: Boolean,
    var indicator1BorderRadius: ODSCorners,
    var indicator1Width: Dp,
    var indicator1Height: Dp,
    var indicator1ClipContent: Boolean,
    var progressBorderRadius: ODSCorners,
    var progressWidthRangeMax: Dp,
    var progressWidthRangeSelected: Dp,
    var progressWidthTwoThumbsRangeSelected: Dp,
    var progressClipContent: Boolean,
    var progressVerticalAlignment: Alignment.Vertical,
    var progressHorizontalAlignment: Alignment.Horizontal,
    var progressHorizontalArrangement: Arrangement.Horizontal,
    var trackLabelContainerVerticalAlignment: Alignment.Vertical,
    var trackLabelContainerHorizontalArrangement: Arrangement.Horizontal,
    var minLabelTextStyle: ODSTextStyle,
    var minLabelTextAlign: TextAlign,
    var maxLabelTextStyle: ODSTextStyle,
    var maxLabelTextAlign: TextAlign
)

var defaultODSSliderTokens = ODSSliderTokens(
    gap = DSVariables.spacingComponent4,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    sliderContainerGap = DSVariables.spacingComponent8,
    sliderContainerVerticalAlignment = Alignment.Top,
    sliderContainerHorizontalAlignment = Alignment.Start,
    sliderContainerVerticalArrangement = Arrangement.Top,
    trackLabelsGap = DSVariables.spacingComponent4,
    trackLabelsVerticalAlignment = Alignment.Top,
    trackLabelsHorizontalAlignment = Alignment.Start,
    trackLabelsVerticalArrangement = Arrangement.Top,
    trackContainerPadding = ODSPadding(
        left = 6.dp,
        right = 6.dp
    ),
    trackContainerBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    trackContainerHeight = DSVariables.sizingComponent7,
    trackContainerVerticalAlignment = Alignment.CenterVertically,
    trackContainerHorizontalArrangement = Arrangement.SpaceBetween,
    indicatorBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    indicatorWidth = 4.dp,
    indicatorHeight = 4.dp,
    indicatorClipContent = true,
    indicator1BorderRadius = ODSCorners(all = DSVariables.radiusFull),
    indicator1Width = 4.dp,
    indicator1Height = 4.dp,
    indicator1ClipContent = true,
    progressBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    progressWidthRangeMax = 394.dp,
    progressWidthRangeSelected = 253.dp,
    progressWidthTwoThumbsRangeSelected = 136.dp,
    progressClipContent = true,
    progressVerticalAlignment = Alignment.Top,
    progressHorizontalAlignment = Alignment.Start,
    progressHorizontalArrangement = Arrangement.Start,
    trackLabelContainerVerticalAlignment = Alignment.Bottom,
    trackLabelContainerHorizontalArrangement = Arrangement.SpaceBetween,
    minLabelTextStyle = DSTextStyles.bodySBold,
    minLabelTextAlign = TextAlign.Left,
    maxLabelTextStyle = DSTextStyles.bodySBold,
    maxLabelTextAlign = TextAlign.Right
)

var DSSliderTokens: ODSSliderTokens = defaultODSSliderTokens
