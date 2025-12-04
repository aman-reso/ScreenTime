package com.telekom.odsystem.organisms.bottomnavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-26 (v1.33.1) - uid: 38dca227
 * Figma link: https://figma.com/design/cpaNsDgmzEjyHilbYDSKS9/Exploration File_Part 2?node-id=2347-7802
 */

data class ODSBottomNavigationTokens(
    val minHeight: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val lineTopHeight: Dp,
    val lineTopClipContent: Boolean,
    val actionsVerticalAlignment: Alignment.Vertical,
    val actionsHorizontalAlignment: Alignment.Horizontal,
    val actionsHorizontalArrangement: Arrangement.Horizontal,
)

val defaultODSBottomNavigationTokens = ODSBottomNavigationTokens(
    minHeight = DSVariables.sizingMinimumTappableArea,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
    lineTopHeight = 1.dp,
    lineTopClipContent = true,
    actionsVerticalAlignment = Alignment.Top,
    actionsHorizontalAlignment = Alignment.CenterHorizontally,
    actionsHorizontalArrangement = Arrangement.Center
)

var DSBottomNavigationTokens: ODSBottomNavigationTokens = defaultODSBottomNavigationTokens
