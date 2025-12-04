package com.telekom.odsystem.organisms.timepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables

data class ODSTimePickerTokens(
    val gapStatusUnfilled: Dp,
    val gapStatusFilled: Dp,
    val gapStatusEditing: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val odsTimePickerFlyoutLargeYSizeLargeStatusUnfilled: Dp,
    val odsTimePickerFlyoutLargeYSizeLargeStatusFilled: Dp,
    val odsTimePickerFlyoutLargeYSizeLargeStatusEditing: Dp,
    val odsTimePickerFlyoutLargeYSizeSmallStatusUnfilled: Dp,
    val odsTimePickerFlyoutLargeYSizeSmallStatusFilled: Dp,
    val odsTimePickerFlyoutLargeYSizeSmallStatusEditing: Dp
)

val defaultODSTimePickerTokens = ODSTimePickerTokens(
    gapStatusUnfilled = DSVariables.spacingComponent3,
    gapStatusFilled = DSVariables.spacingComponent3,
    gapStatusEditing = DSVariables.spacingComponent4,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    contentAlignment = Alignment.TopStart,
    odsTimePickerFlyoutLargeYSizeLargeStatusUnfilled = 80.dp,
    odsTimePickerFlyoutLargeYSizeLargeStatusFilled = 80.dp,
    odsTimePickerFlyoutLargeYSizeLargeStatusEditing = 84.dp,
    odsTimePickerFlyoutLargeYSizeSmallStatusUnfilled = 66.dp,
    odsTimePickerFlyoutLargeYSizeSmallStatusFilled = 66.dp,
    odsTimePickerFlyoutLargeYSizeSmallStatusEditing = 70.dp
)

var DSTimePickerTokens: ODSTimePickerTokens = defaultODSTimePickerTokens
