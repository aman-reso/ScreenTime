package com.telekom.odsystem.atoms.starlistitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSStarListItemTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSStarListItemStyle {
    var width: Dp? = null
    var height: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var starColor: HexColor? = null
    var starWidth: Dp? = null
    var starHeight: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSStarListItemProps,
        state: ODSActions
    ): ODSStarListItemStyle {
        val style = ODSStarListItemStyle()
        style.width = DSStarListItemTokens.width
        style.height = DSStarListItemTokens.height
        style.verticalAlignment = DSStarListItemTokens.verticalAlignment
        style.horizontalAlignment = DSStarListItemTokens.horizontalAlignment
        style.horizontalArrangement = DSStarListItemTokens.horizontalArrangement
        style.starWidth = DSStarListItemTokens.starWidth
        style.starHeight = DSStarListItemTokens.starHeight
        if (!props.selected && !props.readOnly && !props.disabled) {
            style.starColor = scheme.basicStroke
        }
        if (!props.selected && props.readOnly && !props.disabled) {
            style.starColor = scheme.interactionStatesDisabledBackgroundDisabled
        }
        if (!props.selected && !props.readOnly && props.disabled) {
            style.starColor = scheme.interactionStatesDisabledBackgroundDisabled
        }
        if (props.selected && !props.readOnly && !props.disabled) {
            style.starColor = scheme.basicAccent
        }
        if (props.selected && props.readOnly && !props.disabled) {
            style.starColor = scheme.basicText
        }
        if (state == ODSActions.HOVERED && !props.selected && !props.readOnly && !props.disabled) {
            style.starColor = scheme.interactionStatesHoverAccentHover
        }
        if (state == ODSActions.PRESSED && !props.selected && !props.readOnly && !props.disabled) {
            style.starColor = scheme.interactionStatesPressedAccentPressed
        }
        return style
    }
}
