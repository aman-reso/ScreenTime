package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding

data class ODSFlyoutMenuTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val flyoutContainerWidthMenuSizeLarge: Dp,
    val flyoutContainerWidthMenuSizeSmall: Dp,
    val flyoutContainerOffset: ODSOffset,
    val flyoutContainerVerticalAlignment: Alignment.Vertical,
    val flyoutContainerHorizontalAlignment: Alignment.Horizontal,
    val flyoutContainerVerticalArrangement: Arrangement.Vertical,
    val flyoutContainerContentAlignment: Alignment,
    val odsFlyoutListContainerLargeLevel2ContentAlignmentMenuSizeLarge: Alignment,
    val odsFlyoutListContainerLargeLevel2WidthMenuSizeLarge: Dp,
    val odsFlyoutListContainerLargeLevel2OffsetMenuSizeLarge: ODSOffset,
    val odsFlyoutListContainerSmallLevel2ContentAlignmentMenuSizeSmall: Alignment,
    val odsFlyoutListContainerSmallLevel2WidthMenuSizeSmall: Dp,
    val odsFlyoutListContainerSmallLevel2OffsetMenuSizeSmall: ODSOffset,
    val odsFlyoutListContainerSmallWidthMenuSizeSmallExpandedClosedDisabled: Dp,
    val dropdownBorderRadius: ODSCorners? = null, // Not exported from the plugin
    val dropdownPaddingLarge: ODSPadding? = null, // Not exported from the plugin
    val dropdownPaddingSmall: ODSPadding? = null, // Not exported from the plugin
    val dropdownOffset: DpOffset? = null, // Not exported from the plugin
    val dropdownBorderWidth: Dp? = null // Not exported from the plugin
)

val defaultODSFlyoutMenuTokens = ODSFlyoutMenuTokens(
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    contentAlignment = Alignment.TopStart,
    flyoutContainerWidthMenuSizeLarge = 320.dp,
    flyoutContainerWidthMenuSizeSmall = 240.dp,
    flyoutContainerOffset = ODSOffset(x = 0.dp, y = 48.dp),
    flyoutContainerVerticalAlignment = Alignment.Top,
    flyoutContainerHorizontalAlignment = Alignment.Start,
    flyoutContainerVerticalArrangement = Arrangement.Top,
    flyoutContainerContentAlignment = Alignment.TopStart,
    odsFlyoutListContainerLargeLevel2ContentAlignmentMenuSizeLarge = Alignment.TopStart,
    odsFlyoutListContainerLargeLevel2WidthMenuSizeLarge = 320.dp,
    odsFlyoutListContainerLargeLevel2OffsetMenuSizeLarge = ODSOffset(x = 312.dp, y = 89.dp),
    odsFlyoutListContainerSmallLevel2ContentAlignmentMenuSizeSmall = Alignment.TopStart,
    odsFlyoutListContainerSmallLevel2WidthMenuSizeSmall = 240.dp,
    odsFlyoutListContainerSmallLevel2OffsetMenuSizeSmall = ODSOffset(x = 235.dp, y = 61.dp),
    odsFlyoutListContainerSmallWidthMenuSizeSmallExpandedClosedDisabled = 240.dp,
    dropdownBorderRadius = ODSCorners(all = DSVariables.radiusMedium),
    dropdownPaddingLarge = ODSPadding(
        top = 8.dp,
        bottom = 8.dp,
        left = 16.dp,
        right = 16.dp
    ),
    dropdownPaddingSmall = ODSPadding(
        top = 4.dp,
        bottom = 4.dp,
        left = 12.dp,
        right = 12.dp
    ),
    dropdownOffset = DpOffset(DSVariables.spacingComponent0, DSVariables.spacingComponent0),
    dropdownBorderWidth = 1.dp
)

var DSFlyoutMenuTokens: ODSFlyoutMenuTokens = defaultODSFlyoutMenuTokens
