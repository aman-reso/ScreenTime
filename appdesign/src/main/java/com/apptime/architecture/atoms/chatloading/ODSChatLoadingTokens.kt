package com.telekom.odsystem.atoms.chatloading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding

data class ODSChatLoadingTokens(
    val padding: ODSPadding,
    val width: Dp,
    val height: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical
)

val defaultODSChatLoadingTokens = ODSChatLoadingTokens(
    padding = ODSPadding(all = DSVariables.spacingComponent5),
    width = DSVariables.sizingComponent15,
    height = DSVariables.sizingComponent13,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top
)

var DSChatLoadingTokens: ODSChatLoadingTokens = defaultODSChatLoadingTokens
