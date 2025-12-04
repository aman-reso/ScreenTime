package com.telekom.odsystem.atoms.loadingbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-12 (v1.31.6) - uid: 747e6161
 * Figma link: https://figma.com/design/MpQgyLR8JN6QeprILJwaD4/ODS_Feedback-Components_Exploration?node-id=1501-51354
 */

data class ODSLoadingBarTokens(
    val zStackClipContent: Boolean,
    val zStackContentAlignment: Alignment,
    val cornerRadius: ODSCorners,
    val clipContent: Boolean,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val strokeAbsoluteOffset: ODSOffset,
    val strokeAbsoluteContentAlignment: Alignment,
    val strokeCornerRadius: ODSCorners,
    val strokeHeight: Dp,
    val strokeVerticalAlignment: Alignment.Vertical,
    val strokeHorizontalAlignment: Alignment.Horizontal,
    val strokeHorizontalArrangement: Arrangement.Horizontal
)

val defaultODSLoadingBarTokens = ODSLoadingBarTokens(
    zStackClipContent = true,
    zStackContentAlignment = Alignment.TopStart,
    cornerRadius = ODSCorners(all = DSVariables.radiusFull),
    clipContent = true,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    contentAlignment = Alignment.TopStart,
    strokeAbsoluteOffset = ODSOffset(y = 0.dp),
    strokeAbsoluteContentAlignment = Alignment.TopStart,
    strokeCornerRadius = ODSCorners(all = DSVariables.radiusFull),
    strokeHeight = DSVariables.sizingComponent2,
    strokeVerticalAlignment = Alignment.Top,
    strokeHorizontalAlignment = Alignment.CenterHorizontally,
    strokeHorizontalArrangement = Arrangement.Center
)

var DSLoadingBarTokens: ODSLoadingBarTokens = defaultODSLoadingBarTokens
