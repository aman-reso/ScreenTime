package com.telekom.odsystem.organisms.cardpromo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding

/**
 * Code generated with ODS RADD Code Generator
 * 2025-08-06 (v1.32.3) - uid: 48019f32
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=16907-23794
 */

data class ODSCardPromoTokens(
    val zStackMinWidth: Dp,
    val zStackClipContent: Boolean,
    val zStackContentAlignmentTypeCard: Alignment,
    val zStackContentAlignmentTypeFade: Alignment,
    val cornerRadius: ODSCorners,
    val minWidth: Dp,
    val clipContent: Boolean,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignmentTypeCard: Alignment.Horizontal,
    val horizontalAlignmentTypeFade: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignmentTypeCard: Alignment,
    val contentAlignmentTypeFade: Alignment,
    val spacerMinHeight: Dp,
    val spacerClipContent: Boolean,
    val spacerVerticalAlignment: Alignment.Vertical,
    val spacerHorizontalAlignment: Alignment.Horizontal,
    val spacerHorizontalArrangement: Arrangement.Horizontal,
    val bottomFadePaddingTypeCard: ODSPadding,
    val bottomFadeVerticalAlignment: Alignment.Vertical,
    val bottomFadeHorizontalAlignment: Alignment.Horizontal,
    val bottomFadeVerticalArrangement: Arrangement.Vertical,
    val contentPaddingTypeCard: ODSPadding,
    val contentPaddingTypeFade: ODSPadding,
    val contentCornerRadiusTypeCard: ODSCorners,
    val contentClipContent: Boolean,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentVerticalArrangement: Arrangement.Vertical,
    val topFadeAbsoluteOffsetTypeFade: ODSOffset,
    val topFadeAbsoluteContentAlignmentTypeFade: Alignment,
    val topFadeHeightTypeFade: Dp,
    val topFadeVerticalAlignmentTypeFade: Alignment.Vertical,
    val topFadeHorizontalAlignmentTypeFade: Alignment.Horizontal,
    val topFadeVerticalArrangementTypeFade: Arrangement.Vertical
)

val defaultODSCardPromoTokens = ODSCardPromoTokens(
    zStackMinWidth = 312.dp,
    zStackClipContent = true,
    zStackContentAlignmentTypeCard = Alignment.BottomStart,
    zStackContentAlignmentTypeFade = Alignment.BottomCenter,
    cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
    minWidth = 312.dp,
    clipContent = true,
    verticalAlignment = Alignment.Bottom,
    horizontalAlignmentTypeCard = Alignment.Start,
    horizontalAlignmentTypeFade = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Bottom,
    contentAlignmentTypeCard = Alignment.BottomStart,
    contentAlignmentTypeFade = Alignment.BottomCenter,
    spacerMinHeight = 350.dp,
    spacerClipContent = true,
    spacerVerticalAlignment = Alignment.Top,
    spacerHorizontalAlignment = Alignment.Start,
    spacerHorizontalArrangement = Arrangement.Start,
    bottomFadePaddingTypeCard = ODSPadding(
        top = DSVariables.spacingComponent8,
        bottom = DSVariables.spacingComponent3,
        left = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent3
    ),
    bottomFadeVerticalAlignment = Alignment.Bottom,
    bottomFadeHorizontalAlignment = Alignment.CenterHorizontally,
    bottomFadeVerticalArrangement = Arrangement.Bottom,
    contentPaddingTypeCard = ODSPadding(all = DSVariables.spacingComponent5),
    contentPaddingTypeFade = ODSPadding(
        top = DSVariables.spacingComponent8,
        bottom = DSVariables.spacingComponent5,
        left = DSVariables.spacingComponent5,
        right = DSVariables.spacingComponent5
    ),
    contentCornerRadiusTypeCard = ODSCorners(all = DSVariables.radiusSmall),
    contentClipContent = true,
    contentVerticalAlignment = Alignment.Top,
    contentHorizontalAlignment = Alignment.Start,
    contentVerticalArrangement = Arrangement.Top,
    topFadeAbsoluteOffsetTypeFade = ODSOffset(y = 0.dp),
    topFadeAbsoluteContentAlignmentTypeFade = Alignment.TopStart,
    topFadeHeightTypeFade = 60.dp,
    topFadeVerticalAlignmentTypeFade = Alignment.Bottom,
    topFadeHorizontalAlignmentTypeFade = Alignment.Start,
    topFadeVerticalArrangementTypeFade = Arrangement.Bottom
)

var DSCardPromoTokens: ODSCardPromoTokens = defaultODSCardPromoTokens
