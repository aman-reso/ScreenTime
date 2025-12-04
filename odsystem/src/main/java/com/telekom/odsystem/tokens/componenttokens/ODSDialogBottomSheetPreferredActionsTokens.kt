package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSDialogBottomSheetPreferredActionsTokens(
    var gap: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangementVariantSideBySide: Arrangement.Horizontal,
    var horizontalArrangementVariantSideBySideFill: Arrangement.Horizontal,
    var verticalArrangementVariantStacked: Arrangement.Vertical
)

var defaultODSDialogBottomSheetPreferredActionsTokens = ODSDialogBottomSheetPreferredActionsTokens(
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.End,
    horizontalArrangementVariantSideBySide = Arrangement.End,
    horizontalArrangementVariantSideBySideFill = Arrangement.End,
    verticalArrangementVariantStacked = Arrangement.Center
)

var DSDialogBottomSheetPreferredActionsTokens: ODSDialogBottomSheetPreferredActionsTokens =
    defaultODSDialogBottomSheetPreferredActionsTokens
