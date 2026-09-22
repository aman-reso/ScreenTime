package com.telekom.odsystem.slots.cardanchoredimagepreferredcontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSCardAnchoredImagePreferredContentTokens(
    var paddingContentProgressBar: ODSPadding,
    var paddingContentOverview: ODSPadding,
    var paddingContentBars: ODSPadding,
    var verticalAlignmentContentProgressBar: Alignment.Vertical,
    var verticalAlignmentContentOverview: Alignment.Vertical,
    var verticalAlignmentContentBars: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangement: Arrangement.Horizontal,
    var gapContentOverview: Dp,
    var dataGapContentProgressBar: Dp,
    var dataGapContentOverview: Dp,
    var dataVerticalAlignmentContentProgressBar: Alignment.Vertical,
    var dataVerticalAlignmentContentOverview: Alignment.Vertical,
    var dataHorizontalAlignmentContentProgressBar: Alignment.Horizontal,
    var dataHorizontalAlignmentContentOverview: Alignment.Horizontal,
    var dataVerticalArrangementContentProgressBar: Arrangement.Vertical,
    var dataVerticalArrangementContentOverview: Arrangement.Vertical,
    var dataPaddingContentOverview: ODSPadding,
    var usageGapContentOverview: Dp,
    var usageGapContentBars: Dp,
    var usageVerticalAlignmentContentOverview: Alignment.Vertical,
    var usageVerticalAlignmentContentBars: Alignment.Vertical,
    var usageHorizontalAlignmentContentOverview: Alignment.Horizontal,
    var usageHorizontalAlignmentContentBars: Alignment.Horizontal,
    var usageVerticalArrangementContentOverview: Arrangement.Vertical,
    var usageVerticalArrangementContentBars: Arrangement.Vertical,
    var progressLabelTextStyleContentOverview: ODSTextStyle,
    var progressLabelTextAlignContentOverview: TextAlign,
    var progressLabelMaxWidthContentOverview: Dp,
    var progressLabelMinWidthContentOverview: Dp,
    var barsLabelTextStyleContentOverview: ODSTextStyle,
    var barsLabelTextStyleContentBars: ODSTextStyle,
    var barsLabelTextAlignContentOverview: TextAlign,
    var barsLabelTextAlignContentBars: TextAlign,
    var barsLabelMaxWidthContentOverview: Dp,
    var barsLabelMinWidthContentOverview: Dp,
    var barsLabelMinWidthContentBars: Dp
)

var defaultODSCardAnchoredImagePreferredContentTokens = ODSCardAnchoredImagePreferredContentTokens(
    paddingContentProgressBar = ODSPadding(top = DSVariables.spacingComponent1),
    paddingContentOverview = ODSPadding(
        top = DSVariables.spacingLayout3,
        bottom = DSVariables.spacingLayout8
    ),
    paddingContentBars = ODSPadding(top = DSVariables.spacingComponent8),
    verticalAlignmentContentProgressBar = Alignment.Bottom,
    verticalAlignmentContentOverview = Alignment.Top,
    verticalAlignmentContentBars = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    gapContentOverview = DSVariables.spacingLayout5,
    dataGapContentProgressBar = DSVariables.spacingComponent5,
    dataGapContentOverview = DSVariables.spacingComponent5,
    dataVerticalAlignmentContentProgressBar = Alignment.Top,
    dataVerticalAlignmentContentOverview = Alignment.Top,
    dataHorizontalAlignmentContentProgressBar = Alignment.Start,
    dataHorizontalAlignmentContentOverview = Alignment.Start,
    dataVerticalArrangementContentProgressBar = Arrangement.Top,
    dataVerticalArrangementContentOverview = Arrangement.Top,
    dataPaddingContentOverview = ODSPadding(top = DSVariables.spacingComponent7),
    usageGapContentOverview = DSVariables.spacingComponent5,
    usageGapContentBars = DSVariables.spacingComponent5,
    usageVerticalAlignmentContentOverview = Alignment.Top,
    usageVerticalAlignmentContentBars = Alignment.Top,
    usageHorizontalAlignmentContentOverview = Alignment.Start,
    usageHorizontalAlignmentContentBars = Alignment.Start,
    usageVerticalArrangementContentOverview = Arrangement.Top,
    usageVerticalArrangementContentBars = Arrangement.Top,
    progressLabelTextStyleContentOverview = DSTextStyles.bodyL,
    progressLabelTextAlignContentOverview = TextAlign.Left,
    progressLabelMaxWidthContentOverview = 130.dp,
    progressLabelMinWidthContentOverview = 80.dp,
    barsLabelTextStyleContentOverview = DSTextStyles.bodyL,
    barsLabelTextStyleContentBars = DSTextStyles.bodyL,
    barsLabelTextAlignContentOverview = TextAlign.Left,
    barsLabelTextAlignContentBars = TextAlign.Left,
    barsLabelMaxWidthContentOverview = 130.dp,
    barsLabelMinWidthContentOverview = 80.dp,
    barsLabelMinWidthContentBars = 130.dp
)

var DSCardAnchoredImagePreferredContentTokens: ODSCardAnchoredImagePreferredContentTokens =
    defaultODSCardAnchoredImagePreferredContentTokens
