package com.telekom.odsystem.atoms.inputstepperbutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSInputStepperButtonStyle {
    var maxHeight: Dp? = null
    var maxWidth: Dp? = null
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var contentAlignment: Alignment? = null
    var buttonBgBorderRadius: ODSCorners? = null
    var buttonBgHeight: Dp? = null
    var buttonBgWidth: Dp? = null
    var buttonBgContentAlignment: Alignment? = null
    var buttonBgBackgroundColor: List<ODSColorModel>? = null
    var buttonIconColor: HexColor? = null
    var buttonIconWidth: Dp? = null
    var buttonIconHeight: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSInputStepperButtonProps,
        state: ODSActions
    ): ODSInputStepperButtonStyle {
        val style = ODSInputStepperButtonStyle()
        style.maxHeight = DSInputStepperButtonTokens.maxHeight
        style.maxWidth = DSInputStepperButtonTokens.maxWidth
        style.minHeight = DSInputStepperButtonTokens.minHeight
        style.minWidth = DSInputStepperButtonTokens.minWidth
        style.verticalAlignment = DSInputStepperButtonTokens.verticalAlignment
        style.horizontalAlignment = DSInputStepperButtonTokens.horizontalAlignment
        style.horizontalArrangement = DSInputStepperButtonTokens.horizontalArrangement
        style.contentAlignment = DSInputStepperButtonTokens.contentAlignment
        style.buttonBgBorderRadius = DSInputStepperButtonTokens.buttonBgBorderRadius
        style.buttonBgContentAlignment = DSInputStepperButtonTokens.buttonBgContentAlignment
        if (props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        }
        if (props.size == ODSInputStepperButtonSize.SMALL) {
            style.buttonBgHeight = DSInputStepperButtonTokens.buttonBgHeightSizeSmall
            style.buttonBgWidth = DSInputStepperButtonTokens.buttonBgWidthSizeSmall
        }
        if (props.size == ODSInputStepperButtonSize.LARGE) {
            style.buttonBgHeight = DSInputStepperButtonTokens.buttonBgHeightSizeLarge
            style.buttonBgWidth = DSInputStepperButtonTokens.buttonBgWidthSizeLarge
        }
        if (!props.disabled && state == ODSActions.HOVERED) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (!props.disabled && state == ODSActions.PRESSED) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
        }
        style.buttonIconWidth = DSInputStepperButtonTokens.buttonIconWidth
        style.buttonIconHeight = DSInputStepperButtonTokens.buttonIconHeight
        if (props.disabled) {
            style.buttonIconColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.disabled) {
            style.buttonIconColor = scheme.basicText
        }
        return style
    }
}
