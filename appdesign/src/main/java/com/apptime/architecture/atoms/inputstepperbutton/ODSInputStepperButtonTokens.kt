package com.telekom.odsystem.atoms.inputstepperbutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners

data class ODSInputStepperButtonTokens(
    val maxHeight: Dp,
    val maxWidth: Dp,
    val minHeight: Dp,
    val minWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val contentAlignment: Alignment,
    val buttonBgBorderRadius: ODSCorners,
    val buttonBgHeightSizeSmall: Dp,
    val buttonBgHeightSizeLarge: Dp,
    val buttonBgWidthSizeSmall: Dp,
    val buttonBgWidthSizeLarge: Dp,
    val buttonBgContentAlignment: Alignment,
    val buttonIconWidth: Dp,
    val buttonIconHeight: Dp,
)

val defaultODSInputStepperButtonTokens = ODSInputStepperButtonTokens(
    maxHeight = DSVariables.sizingComponent14,
    maxWidth = DSVariables.sizingComponent14,
    minHeight = DSVariables.sizingComponent14,
    minWidth = DSVariables.sizingComponent14,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    contentAlignment = Alignment.Center,
    buttonBgBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    buttonBgHeightSizeSmall = DSVariables.sizingComponent10,
    buttonBgHeightSizeLarge = DSVariables.sizingComponent13,
    buttonBgWidthSizeSmall = DSVariables.sizingComponent10,
    buttonBgWidthSizeLarge = DSVariables.sizingComponent13,
    buttonBgContentAlignment = Alignment.Center,
    buttonIconWidth = DSVariables.sizingComponent7,
    buttonIconHeight = DSVariables.sizingComponent7,
)

var DSInputStepperButtonTokens: ODSInputStepperButtonTokens = defaultODSInputStepperButtonTokens
