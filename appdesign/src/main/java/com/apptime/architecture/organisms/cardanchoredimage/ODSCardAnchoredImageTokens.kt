package com.telekom.odsystem.organisms.cardanchoredimage

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
import com.telekom.odsystem.foundations.SCALE_FACTOR

data class ODSCardAnchoredImageTokens(
    var padding: ODSPadding,
    var minHeightSizeMedium: Dp,
    var minHeightSizeSmall: Dp,
    var width: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var cardBackgroundBorderRadius: ODSCorners,
    var cardBackgroundWidth: Dp,
//    var cardBackgroundWidthStatePressed: Dp,
//    var cardBackgroundWidthStateHovered: Dp,
    var cardBackgroundHeightSizeMedium: Dp,
//    var cardBackgroundHeightSizeMediumStatePressed: Dp,
    var cardBackgroundHeightSizeSmall: Dp,
//    var cardBackgroundHeightSizeSmallStatePressed: Dp,
//    var cardBackgroundHeightCustomHeight: Dp,
//    var cardBackgroundHeightCustomHeightStatePressed: Dp,
//    var cardBackgroundHeightSizeMediumStateHovered: Dp,
//    var cardBackgroundHeightSizeSmallStateHovered: Dp,
//    var cardBackgroundHeightCustomHeightStateHovered: Dp,
    var cardBackgroundClipContent: Boolean,
    var cardBackgroundVerticalAlignment: Alignment.Vertical,
    var cardBackgroundHorizontalAlignment: Alignment.Horizontal,
    var cardBackgroundVerticalArrangement: Arrangement.Vertical,
    var contentContainerGapSizeMedium: Dp,
    var contentContainerGapSizeSmall: Dp,
    var contentContainerMinHeightSizeMedium: Dp,
    var contentContainerVerticalAlignment: Alignment.Vertical,
    var contentContainerHorizontalAlignment: Alignment.Horizontal,
    var contentContainerVerticalArrangement: Arrangement.Vertical,
    var headingLabelContainerGap: Dp,
    var headingLabelContainerVerticalAlignment: Alignment.Vertical,
    var headingLabelContainerHorizontalAlignment: Alignment.Horizontal,
    var headingLabelContainerVerticalArrangement: Arrangement.Vertical,
    var contentSlotContainerVerticalAlignment: Alignment.Vertical,
    var contentSlotContainerHorizontalAlignment: Alignment.Horizontal,
    var contentSlotContainerHorizontalArrangement: Arrangement.Horizontal,
    var actionSlotContainerPadding: ODSPadding,
    var actionSlotContainerVerticalAlignment: Alignment.Vertical,
    var actionSlotContainerHorizontalAlignment: Alignment.Horizontal,
    var actionSlotContainerVerticalArrangement: Arrangement.Vertical,
    var headingTextStyleSizeMedium: ODSTextStyle,
    var headingTextStyleSizeSmall: ODSTextStyle,
    var headingTextAlign: TextAlign,
    var labelTextStyle: ODSTextStyle,
    var labelTextAlign: TextAlign,
    var scaleFactor: Float, // Not exported by plugin
    val clipContent: Boolean // Not exported by plugin
)

var defaultODSCardAnchoredImageTokens = ODSCardAnchoredImageTokens(
    padding = ODSPadding(all = DSVariables.spacingComponent7),
    minHeightSizeMedium = 188.dp,
    minHeightSizeSmall = 100.dp,
    width = DSVariables.columns6Columns,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    cardBackgroundBorderRadius = ODSCorners(all = DSVariables.radiusMedium),
    cardBackgroundWidth = 312.dp,
//    cardBackgroundWidthStatePressed = 312.dp,
//    cardBackgroundWidthStateHovered = 320.dp,
    cardBackgroundHeightSizeMedium = 210.dp,
//    cardBackgroundHeightSizeMediumStatePressed = 210.dp,
    cardBackgroundHeightSizeSmall = 100.dp,
//    cardBackgroundHeightSizeSmallStatePressed = 100.dp,
//    cardBackgroundHeightCustomHeight = 274.dp,
//    cardBackgroundHeightCustomHeightStatePressed = 274.dp,
//    cardBackgroundHeightSizeMediumStateHovered = 218.dp,
//    cardBackgroundHeightSizeSmallStateHovered = 108.dp,
//    cardBackgroundHeightCustomHeightStateHovered = 282.dp,
    cardBackgroundClipContent = true,
    cardBackgroundVerticalAlignment = Alignment.Bottom,
    cardBackgroundHorizontalAlignment = Alignment.End,
    cardBackgroundVerticalArrangement = Arrangement.Bottom,
    contentContainerGapSizeMedium = DSVariables.spacingComponent3,
    contentContainerGapSizeSmall = DSVariables.spacingComponent2,
    contentContainerMinHeightSizeMedium = 44.dp,
    contentContainerVerticalAlignment = Alignment.Top,
    contentContainerHorizontalAlignment = Alignment.Start,
    contentContainerVerticalArrangement = Arrangement.Top,
    headingLabelContainerGap = DSVariables.spacingComponent2,
    headingLabelContainerVerticalAlignment = Alignment.Top,
    headingLabelContainerHorizontalAlignment = Alignment.Start,
    headingLabelContainerVerticalArrangement = Arrangement.Top,
    contentSlotContainerVerticalAlignment = Alignment.Top,
    contentSlotContainerHorizontalAlignment = Alignment.Start,
    contentSlotContainerHorizontalArrangement = Arrangement.Start,
    actionSlotContainerPadding = ODSPadding(top = DSVariables.spacingComponent10),
    actionSlotContainerVerticalAlignment = Alignment.Bottom,
    actionSlotContainerHorizontalAlignment = Alignment.Start,
    actionSlotContainerVerticalArrangement = Arrangement.Bottom,
    headingTextStyleSizeMedium = DSTextStyles.titleS,
    headingTextStyleSizeSmall = DSTextStyles.paragraph,
    headingTextAlign = TextAlign.Left,
    labelTextStyle = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Left,
    scaleFactor = SCALE_FACTOR,
    clipContent = true
)

var DSCardAnchoredImageTokens: ODSCardAnchoredImageTokens = defaultODSCardAnchoredImageTokens
