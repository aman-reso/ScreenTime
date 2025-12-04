package com.telekom.odsystem.slots.actionslot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment

data class ODSActionSlotTokens(
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal
)

val defaultODSActionSlotTokens = ODSActionSlotTokens(
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start
)

var DSActionSlotTokens: ODSActionSlotTokens = defaultODSActionSlotTokens
