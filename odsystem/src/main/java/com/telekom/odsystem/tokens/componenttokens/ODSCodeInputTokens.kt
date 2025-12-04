package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSCodeInputTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val inputContainerGap: Dp,
    val inputContainerVerticalAlignment: Alignment.Vertical,
    val inputContainerHorizontalAlignment: Alignment.Horizontal,
    val inputContainerHorizontalArrangement: Arrangement.Horizontal
)

val defaultODSCodeInputTokens = ODSCodeInputTokens(
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    inputContainerGap = DSVariables.spacingComponent3,
    inputContainerVerticalAlignment = Alignment.Top,
    inputContainerHorizontalAlignment = Alignment.Start,
    inputContainerHorizontalArrangement = Arrangement.Start
)

var DSCodeInputTokens: ODSCodeInputTokens = defaultODSCodeInputTokens
