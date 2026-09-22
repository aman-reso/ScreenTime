package com.telekom.odsystem.molecules.segmentedbutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding

data class ODSSegmentedButtonTokens(
    val gap: Dp,
    val paddingSizeLarge: ODSPadding,
    val paddingSizeSmall: ODSPadding,
    val borderRadius: ODSCorners,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal
)

val defaultODSSegmentedButtonTokens = ODSSegmentedButtonTokens(
    gap = DSVariables.spacingComponent2,
    paddingSizeLarge = ODSPadding(all = DSVariables.spacingComponent2),
    paddingSizeSmall = ODSPadding(
        left = DSVariables.spacingComponent2,
        right = DSVariables.spacingComponent2
    ),
    borderRadius = ODSCorners(all = DSVariables.radiusFull),
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start
)

var DSSegmentedButtonTokens: ODSSegmentedButtonTokens = defaultODSSegmentedButtonTokens
