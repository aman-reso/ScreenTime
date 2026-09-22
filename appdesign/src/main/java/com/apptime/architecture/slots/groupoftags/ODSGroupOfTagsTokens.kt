package com.telekom.odsystem.slots.groupoftags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding

data class ODSGroupOfTagsTokens(
    var gap: Dp,
    var padding: ODSPadding,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangement: Arrangement.Horizontal
)

var defaultODSGroupOfTagsTokens = ODSGroupOfTagsTokens(
    gap = DSVariables.spacingComponent3,
    padding = ODSPadding(left = DSVariables.spacingComponent10),
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start
)

var DSGroupOfTagsTokens: ODSGroupOfTagsTokens = defaultODSGroupOfTagsTokens
