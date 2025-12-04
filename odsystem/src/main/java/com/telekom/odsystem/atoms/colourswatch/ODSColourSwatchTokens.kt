package com.telekom.odsystem.atoms.colourswatch

import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners

data class ODSColourSwatchTokens(
    val cornerRadius: ODSCorners,
    val border: Dp,
    val width: Dp,
    val height: Dp,
    val clipContent: Boolean
)

val defaultODSColourSwatchTokens = ODSColourSwatchTokens(
    cornerRadius = ODSCorners(all = DSVariables.radiusFull),
    border = DSVariables.strokes1,
    width = DSVariables.sizingComponent5,
    height = DSVariables.sizingComponent5,
    clipContent = true
)

var DSColourSwatchTokens: ODSColourSwatchTokens = defaultODSColourSwatchTokens
