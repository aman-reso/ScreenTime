package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSPopoverPreferredActionsTokens(
    var gap: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangement: Arrangement.Horizontal
)

var defaultODSPopoverPreferredActionsTokens = ODSPopoverPreferredActionsTokens(
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start
)

var DSPopoverPreferredActionsTokens: ODSPopoverPreferredActionsTokens =
    defaultODSPopoverPreferredActionsTokens
