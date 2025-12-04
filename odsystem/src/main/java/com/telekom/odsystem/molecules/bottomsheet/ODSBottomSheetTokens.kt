package com.telekom.odsystem.molecules.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-04 (v1.32.3) - uid: 2df8b99b
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=17924-192
 */

data class ODSBottomSheetTokens(
    val zStackWidth: Dp,
    val zStackClipContent: Boolean,
    val zStackContentAlignment: Alignment,
    val cornerRadius: ODSCorners,
    val width: Dp,
    val clipContent: Boolean,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val scrollContainerClipContent: Boolean,
    val scrollContainerVerticalAlignment: Alignment.Vertical,
    val scrollContainerHorizontalAlignment: Alignment.Horizontal,
    val scrollContainerVerticalArrangement: Arrangement.Vertical,
    val imageContainerZStackContentAlignment: Alignment,
    val imageContainerVerticalAlignment: Alignment.Vertical,
    val imageContainerHorizontalAlignment: Alignment.Horizontal,
    val imageContainerVerticalArrangement: Arrangement.Vertical,
    val imageContainerContentAlignment: Alignment,
    val imageContentScale: ContentScale,
    val titleContainerGap: Dp,
    val titleContainerPadding: ODSPadding,
    val titleContainerVerticalAlignment: Alignment.Vertical,
    val titleContainerHorizontalAlignment: Alignment.Horizontal,
    val titleContainerVerticalArrangement: Arrangement.Vertical,
    val slotContainerPadding: ODSPadding,
    val slotContainerVerticalAlignment: Alignment.Vertical,
    val slotContainerHorizontalAlignment: Alignment.Horizontal,
    val slotContainerHorizontalArrangement: Arrangement.Horizontal,
    val dividerContainerHeight: Dp,
    val dividerContainerVerticalAlignment: Alignment.Vertical,
    val dividerContainerHorizontalAlignment: Alignment.Horizontal,
    val dividerContainerVerticalArrangement: Arrangement.Vertical,
    val slotComponentContainerPadding: ODSPadding,
    val slotComponentContainerVerticalAlignment: Alignment.Vertical,
    val slotComponentContainerHorizontalAlignment: Alignment.Horizontal,
    val slotComponentContainerVerticalArrangement: Arrangement.Vertical,
    val actionSlotContainerPadding: ODSPadding,
    val actionSlotContainerVerticalAlignment: Alignment.Vertical,
    val actionSlotContainerHorizontalAlignment: Alignment.Horizontal,
    val actionSlotContainerVerticalArrangement: Arrangement.Vertical,
    val odsCloseButtonAbsoluteContentAlignment: Alignment,
    val odsCloseButtonAbsoluteOffset: ODSOffset,
    val handleAbsoluteOffset: ODSOffset,
    val handleAbsoluteContentAlignment: Alignment,
    val handleCornerRadius: ODSCorners,
    val handleHeight: Dp,
    val handleWidth: Dp,
    val handleVerticalAlignment: Alignment.Vertical,
    val handleHorizontalAlignment: Alignment.Horizontal,
    val handleHorizontalArrangement: Arrangement.Horizontal
)

val defaultODSBottomSheetTokens = ODSBottomSheetTokens(
    zStackWidth = DSVariables.sizingViewport,
    zStackClipContent = true,
    zStackContentAlignment = Alignment.TopStart,
    cornerRadius = ODSCorners(
        topLeft = DSVariables.radiusLarge,
        topRight = DSVariables.radiusLarge,
        bottomLeft = 0.dp,
        bottomRight = 0.dp
    ),
    width = DSVariables.sizingViewport,
    clipContent = true,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    contentAlignment = Alignment.TopStart,
    scrollContainerClipContent = true,
    scrollContainerVerticalAlignment = Alignment.Top,
    scrollContainerHorizontalAlignment = Alignment.Start,
    scrollContainerVerticalArrangement = Arrangement.Top,
    imageContainerZStackContentAlignment = Alignment.TopStart,
    imageContainerVerticalAlignment = Alignment.Top,
    imageContainerHorizontalAlignment = Alignment.Start,
    imageContainerVerticalArrangement = Arrangement.Top,
    imageContainerContentAlignment = Alignment.TopStart,
    imageContentScale = ContentScale.Crop,
    titleContainerGap = DSVariables.spacingComponent8,
    titleContainerPadding = ODSPadding(
        top = DSVariables.spacingComponent6,
        left = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent3
    ),
    titleContainerVerticalAlignment = Alignment.CenterVertically,
    titleContainerHorizontalAlignment = Alignment.Start,
    titleContainerVerticalArrangement = Arrangement.Center,
    slotContainerPadding = ODSPadding(right = DSVariables.spacingLayout8),
    slotContainerVerticalAlignment = Alignment.CenterVertically,
    slotContainerHorizontalAlignment = Alignment.Start,
    slotContainerHorizontalArrangement = Arrangement.Start,
    dividerContainerHeight = DSVariables.sizingComponent1,
    dividerContainerVerticalAlignment = Alignment.Top,
    dividerContainerHorizontalAlignment = Alignment.Start,
    dividerContainerVerticalArrangement = Arrangement.Top,
    slotComponentContainerPadding = ODSPadding(
        left = DSVariables.spacingComponent3, right = DSVariables.spacingComponent3
    ),
    slotComponentContainerVerticalAlignment = Alignment.Top,
    slotComponentContainerHorizontalAlignment = Alignment.Start,
    slotComponentContainerVerticalArrangement = Arrangement.Top,
    actionSlotContainerPadding = ODSPadding(
        top = DSVariables.spacingComponent8,
        bottom = DSVariables.spacingComponent9,
        left = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent3
    ),
    actionSlotContainerVerticalAlignment = Alignment.Top,
    actionSlotContainerHorizontalAlignment = Alignment.Start,
    actionSlotContainerVerticalArrangement = Arrangement.Top,
    odsCloseButtonAbsoluteContentAlignment = Alignment.TopEnd,
    odsCloseButtonAbsoluteOffset = ODSOffset(x = -24.dp, y = 24.dp),
    handleAbsoluteOffset = ODSOffset(y = 12.dp),
    handleAbsoluteContentAlignment = Alignment.TopCenter,
    handleCornerRadius = ODSCorners(all = DSVariables.radiusFull),
    handleHeight = DSVariables.sizingComponent3,
    handleWidth = 60.dp,
    handleVerticalAlignment = Alignment.Top,
    handleHorizontalAlignment = Alignment.Start,
    handleHorizontalArrangement = Arrangement.Start
)

var DSBottomSheetTokens: ODSBottomSheetTokens = defaultODSBottomSheetTokens
