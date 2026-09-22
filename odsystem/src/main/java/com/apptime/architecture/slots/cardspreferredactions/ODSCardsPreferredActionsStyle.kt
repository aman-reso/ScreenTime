package com.telekom.odsystem.slots.cardspreferredactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSCardsPreferredActionsTokens
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSCardsPreferredActionsStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var gap: Dp? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var actionsListContainerGap: Dp? = null
    var actionsListContainerVerticalAlignment: Alignment.Vertical? = null
    var actionsListContainerHorizontalAlignment: Alignment.Horizontal? = null
    var actionsListContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var actionsListContainerVerticalArrangement: Arrangement.Vertical? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardsPreferredActionsProps
    ): ODSCardsPreferredActionsStyle {
        var style = ODSCardsPreferredActionsStyle()
        style.verticalAlignment = DSCardsPreferredActionsTokens.verticalAlignment
        style.horizontalAlignment = DSCardsPreferredActionsTokens.horizontalAlignment
        if (props.type == ODSCardsPreferredActionsType.MORE_ACTIONS) {
            style.horizontalArrangement =
                DSCardsPreferredActionsTokens.horizontalArrangementTypeMoreActions
            style.gap = DSCardsPreferredActionsTokens.gapTypeMoreActions
        }
        if (props.type == ODSCardsPreferredActionsType.SINGLE_ACTION) {
            style.horizontalArrangement =
                DSCardsPreferredActionsTokens.horizontalArrangementTypeSingleAction
        }
        if (props.type == ODSCardsPreferredActionsType.DOUBLE_ACTION) {
            style.horizontalArrangement =
                DSCardsPreferredActionsTokens.horizontalArrangementTypeDoubleAction
            style.gap = DSCardsPreferredActionsTokens.gapTypeDoubleAction
        }
        if (props.type == ODSCardsPreferredActionsType.MORE_ACTIONS_EXPANDED) {
            style.gap = DSCardsPreferredActionsTokens.gapTypeMoreActionsExpanded
            style.verticalArrangement =
                DSCardsPreferredActionsTokens.verticalArrangementTypeMoreActionsExpanded
        }
        if (props.type == ODSCardsPreferredActionsType.MORE_ACTIONS) {
            style.actionsListContainerGap =
                DSCardsPreferredActionsTokens.actionsListContainerGapTypeMoreActions
            style.actionsListContainerVerticalAlignment =
                DSCardsPreferredActionsTokens.actionsListContainerVerticalAlignmentTypeMoreActions
            style.actionsListContainerHorizontalAlignment =
                DSCardsPreferredActionsTokens.actionsListContainerHorizontalAlignmentTypeMoreActions
            style.actionsListContainerHorizontalArrangement =
                DSCardsPreferredActionsTokens.actionsListContainerHorizontalArrangementTypeMoreActions
        }
        if (props.type == ODSCardsPreferredActionsType.MORE_ACTIONS_EXPANDED) {
            style.actionsListContainerGap =
                DSCardsPreferredActionsTokens.actionsListContainerGapTypeMoreActionsExpanded
            style.actionsListContainerVerticalAlignment =
                DSCardsPreferredActionsTokens.actionsListContainerVerticalAlignmentTypeMoreActionsExpanded
            style.actionsListContainerHorizontalAlignment =
                DSCardsPreferredActionsTokens.actionsListContainerHorizontalAlignmentTypeMoreActionsExpanded
            style.actionsListContainerVerticalArrangement =
                DSCardsPreferredActionsTokens.actionsListContainerVerticalArrangementTypeMoreActionsExpanded
        }
        return style
    }
}
