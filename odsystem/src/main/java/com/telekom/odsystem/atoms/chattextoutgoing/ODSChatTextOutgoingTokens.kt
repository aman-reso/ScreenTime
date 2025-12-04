package com.telekom.odsystem.atoms.chattextoutgoing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSChatTextOutgoingTokens(
    val padding: ODSPadding,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val textStyle: ODSTextStyle,
    val textTextAlign: TextAlign,
    val textMaxWidth: Dp
)

val defaultODSChatTextOutgoingTokens = ODSChatTextOutgoingTokens(
    padding = ODSPadding(
        top = DSVariables.spacingComponent3,
        bottom = DSVariables.spacingComponent3,
        left = DSVariables.spacingComponent5,
        right = DSVariables.spacingComponent5
    ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    textStyle = DSTextStyles.bodyMRegular,
    textTextAlign = TextAlign.Left,
    textMaxWidth = 228.dp
)

var DSChatTextOutgoingTokens: ODSChatTextOutgoingTokens = defaultODSChatTextOutgoingTokens
