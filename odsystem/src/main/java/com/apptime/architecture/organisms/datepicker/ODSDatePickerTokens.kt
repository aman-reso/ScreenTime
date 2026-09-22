package com.telekom.odsystem.organisms.datepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables

data class ODSDatePickerTokens(
    val gap: Dp,
    val width: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val odsDatePickerFlyoutLargeYSizeLarge: Dp,
    val odsDatePickerFlyoutLargeYSizeSmall: Dp
)

val defaultODSDatePickerTokens = ODSDatePickerTokens(
    gap = DSVariables.spacingComponent3,
    width = 482.dp,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    contentAlignment = Alignment.TopStart,
    odsDatePickerFlyoutLargeYSizeLarge = 80.dp,
    odsDatePickerFlyoutLargeYSizeSmall = 66.dp
)

var DSDatePickerTokens: ODSDatePickerTokens = defaultODSDatePickerTokens
