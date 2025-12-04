package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

data class ODSCardsPreferredActionsTokens(
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangementTypeSingleAction: Arrangement.Horizontal,
    var horizontalArrangementTypeDoubleAction: Arrangement.Horizontal,
    var horizontalArrangementTypeMoreActions: Arrangement.Horizontal,
    var gapTypeDoubleAction: Dp,
    var gapTypeMoreActions: Dp,
    var gapTypeMoreActionsExpanded: Dp,
    var verticalArrangementTypeMoreActionsExpanded: Arrangement.Vertical,
    var actionsListContainerGapTypeMoreActions: Dp,
    var actionsListContainerGapTypeMoreActionsExpanded: Dp,
    var actionsListContainerVerticalAlignmentTypeMoreActions: Alignment.Vertical,
    var actionsListContainerVerticalAlignmentTypeMoreActionsExpanded: Alignment.Vertical,
    var actionsListContainerHorizontalAlignmentTypeMoreActions: Alignment.Horizontal,
    var actionsListContainerHorizontalAlignmentTypeMoreActionsExpanded: Alignment.Horizontal,
    var actionsListContainerHorizontalArrangementTypeMoreActions: Arrangement.Horizontal,
    var actionsListContainerVerticalArrangementTypeMoreActionsExpanded: Arrangement.Vertical
)

var defaultODSCardsPreferredActionsTokens = ODSCardsPreferredActionsTokens(
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangementTypeSingleAction = Arrangement.Start,
    horizontalArrangementTypeDoubleAction = Arrangement.Start,
    horizontalArrangementTypeMoreActions = Arrangement.Start,
    gapTypeDoubleAction = DSVariables.spacingComponent3,
    gapTypeMoreActions = DSVariables.spacingComponent3,
    gapTypeMoreActionsExpanded = DSVariables.spacingComponent1,
    verticalArrangementTypeMoreActionsExpanded = Arrangement.Top,
    actionsListContainerGapTypeMoreActions = DSVariables.spacingComponent3,
    actionsListContainerGapTypeMoreActionsExpanded = DSVariables.spacingComponent1,
    actionsListContainerVerticalAlignmentTypeMoreActions = Alignment.Top,
    actionsListContainerVerticalAlignmentTypeMoreActionsExpanded = Alignment.Top,
    actionsListContainerHorizontalAlignmentTypeMoreActions = Alignment.Start,
    actionsListContainerHorizontalAlignmentTypeMoreActionsExpanded = Alignment.Start,
    actionsListContainerHorizontalArrangementTypeMoreActions = Arrangement.Start,
    actionsListContainerVerticalArrangementTypeMoreActionsExpanded = Arrangement.Top
)

var DSCardsPreferredActionsTokens: ODSCardsPreferredActionsTokens =
    defaultODSCardsPreferredActionsTokens
