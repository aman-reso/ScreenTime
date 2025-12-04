package com.telekom.odsystem.slots.pageheaderpreferredactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSPageHeaderPreferredActionsTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal
)

val defaultODSPageHeaderPreferredActionsTokens = ODSPageHeaderPreferredActionsTokens(
    gap = DSVariables.spacingComponent2,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.End,
    horizontalArrangement = Arrangement.End
)

var DSPageHeaderPreferredActionsTokens: ODSPageHeaderPreferredActionsTokens =
    defaultODSPageHeaderPreferredActionsTokens
