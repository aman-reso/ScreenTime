package com.telekom.odsystem.slots.textfieldicon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables

/**
 * Code generated with ODS RADD Code Generator
 * 2025-10-20 (v1.33.1) - uid: 3e102fec
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=147-12487
 */

data class ODSTextFieldIconTokens(
    val width: Dp,
    val height: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val iconWidthSizeLargeTypeIconContainer: Dp,
    val iconWidthSizeSmallTypeIconContainer: Dp,
    val iconHeightSizeLargeTypeIconContainer: Dp,
    val iconHeightSizeSmallTypeIconContainer: Dp,
)

val defaultODSTextFieldIconTokens = ODSTextFieldIconTokens(
    width = DSVariables.sizingComponent14,
    height = DSVariables.sizingComponent14,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    iconWidthSizeLargeTypeIconContainer = 24.dp,
    iconWidthSizeSmallTypeIconContainer = 16.dp,
    iconHeightSizeLargeTypeIconContainer = 24.dp,
    iconHeightSizeSmallTypeIconContainer = 16.dp
)

var DSTextFieldIconTokens: ODSTextFieldIconTokens = defaultODSTextFieldIconTokens
