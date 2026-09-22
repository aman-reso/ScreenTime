package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding

data class ODSDataProgressTrackTokens(
    var paddingSizeLarge: ODSPadding,
    var paddingSizeMedium: ODSPadding,
    var paddingSizeSmall: ODSPadding,
    var borderRadius: ODSCorners,
    var heightSizeLarge: Dp,
    var heightSizeMedium: Dp,
    var heightSizeSmall: Dp,
    var clipContent: Boolean,
    var verticalAlignment: Alignment.Vertical,
    var horizontalArrangement: Arrangement.Horizontal,
    var indicatorEndBorderRadius: ODSCorners,
    var indicatorEndWidth: Dp,
    var indicatorEndHeight: Dp,
    var indicatorEndClipContent: Boolean,
    var indicatorStartBorderRadius: ODSCorners,
    var indicatorStartWidth: Dp,
    var indicatorStartHeight: Dp,
    var indicatorStartClipContent: Boolean,
//    var progressWidthTypeStandardProgress0SizeLarge: Dp,
//    var progressWidthTypeErrorProgress25: Dp,
//    var progressWidthTypeStandardProgress25: Dp,
//    var progressWidthTypeErrorProgress50SizeLarge: Dp,
//    var progressWidthTypeErrorProgress50SizeSmall: Dp,
//    var progressWidthTypeStandardProgress50: Dp,
//    var progressWidthTypeErrorProgress75: Dp,
//    var progressWidthTypeStandardProgress75: Dp,
//    var progressWidthTypeSuccessProgress100: Dp,
//    var progressWidthTypeStandardProgress100: Dp,
//    var progressWidthTypeStandardProgress0SizeMedium: Dp,
//    var progressWidthTypeStandardProgress0SizeSmall: Dp,
//    var progressWidthTypeDisabledProgress0: Dp,
//    var progressWidthTypeErrorProgress0: Dp,
//    var progressWidthTypeErrorProgress50SizeMedium: Dp,
    var progressHeightSizeLarge: Dp,
    var progressHeightSizeMedium: Dp,
    var progressHeightSizeSmall: Dp,

    // Custom Addition
    var dotHorizontalPaddingSmall: ODSPadding,
    var dotHorizontalArrangementSmall: Arrangement.Horizontal,
    var dotHorizontalPaddingMedium: ODSPadding,
    var dotHorizontalArrangementMedium: Arrangement.Horizontal,
    var dotHorizontalPaddingLarge: ODSPadding,
    var dotHorizontalArrangementLarge: Arrangement.Horizontal,
)

var defaultODSDataProgressTrackTokens = ODSDataProgressTrackTokens(
    paddingSizeLarge = ODSPadding(
        left = 14.dp,
        right = 14.dp
    ),
    paddingSizeMedium = ODSPadding(
        left = 6.dp,
        right = 6.dp
    ),
    paddingSizeSmall = ODSPadding(
        left = DSVariables.spacingComponent1,
        right = DSVariables.spacingComponent1
    ),
    borderRadius = ODSCorners(all = DSVariables.radiusMedium),
    heightSizeLarge = DSVariables.sizingComponent14,
    heightSizeMedium = DSVariables.sizingComponent7,
    heightSizeSmall = DSVariables.sizingComponent4,
    clipContent = true,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    indicatorEndBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    indicatorEndWidth = DSVariables.sizingComponent3,
    indicatorEndHeight = DSVariables.sizingComponent3,
    indicatorEndClipContent = true,
    indicatorStartBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    indicatorStartWidth = DSVariables.sizingComponent3,
    indicatorStartHeight = DSVariables.sizingComponent3,
    indicatorStartClipContent = true,
//    progressWidthTypeStandardProgress0SizeLarge = 1.dp,
//    progressWidthTypeErrorProgress25 = 150.dp,
//    progressWidthTypeStandardProgress25 = 150.dp,
//    progressWidthTypeErrorProgress50SizeLarge = 300.dp,
//    progressWidthTypeErrorProgress50SizeSmall = 300.dp,
//    progressWidthTypeStandardProgress50 = 300.dp,
//    progressWidthTypeErrorProgress75 = 450.dp,
//    progressWidthTypeStandardProgress75 = 450.dp,
//    progressWidthTypeSuccessProgress100 = 600.dp,
//    progressWidthTypeStandardProgress100 = 600.dp,
//    progressWidthTypeStandardProgress0SizeMedium = 1.5.dp,
//    progressWidthTypeStandardProgress0SizeSmall = 1.5.dp,
//    progressWidthTypeDisabledProgress0 = 1.5.dp,
//    progressWidthTypeErrorProgress0 = 1.5.dp,
//    progressWidthTypeErrorProgress50SizeMedium = 299.dp,
    progressHeightSizeLarge = 48.dp,
    progressHeightSizeMedium = 16.dp,
    progressHeightSizeSmall = 8.dp,
    dotHorizontalPaddingSmall = ODSPadding(horizontal = DSVariables.spacingComponent2),
    dotHorizontalArrangementSmall = Arrangement.SpaceBetween,
    dotHorizontalPaddingMedium = ODSPadding(horizontal = DSVariables.spacingComponent3),
    dotHorizontalArrangementMedium = Arrangement.SpaceBetween,
    dotHorizontalPaddingLarge = ODSPadding(horizontal = DSVariables.spacingComponent5),
    dotHorizontalArrangementLarge = Arrangement.SpaceBetween,
)

var DSDataProgressTrackTokens: ODSDataProgressTrackTokens = defaultODSDataProgressTrackTokens
