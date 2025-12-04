package com.telekom.odsystem.molecules.chatbubbleleadingelement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment

data class ODSChatBubbleLeadingElementTokens(
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal
)

val defaultODSChatBubbleLeadingElementTokens = ODSChatBubbleLeadingElementTokens(
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start
)

var DSChatBubbleLeadingElementTokens: ODSChatBubbleLeadingElementTokens =
    defaultODSChatBubbleLeadingElementTokens
