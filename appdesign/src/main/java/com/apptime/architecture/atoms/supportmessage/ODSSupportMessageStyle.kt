package com.telekom.odsystem.atoms.supportmessage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSSupportMessageTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSSupportMessageStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var informationColor: HexColor? = null
    var informationWidth: Dp? = null
    var informationHeight: Dp? = null
    var errorColor: HexColor? = null
    var errorWidth: Dp? = null
    var errorHeight: Dp? = null
    var successColor: HexColor? = null
    var successWidth: Dp? = null
    var successHeight: Dp? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSSupportMessageProps
    ): ODSSupportMessageStyle {
        var style = ODSSupportMessageStyle()
        style.gap = DSSupportMessageTokens.gap
        style.verticalAlignment = DSSupportMessageTokens.verticalAlignment
        style.horizontalAlignment = DSSupportMessageTokens.horizontalAlignment
        style.horizontalArrangement = DSSupportMessageTokens.horizontalArrangement
        if (props.mode == ODSSupportMessageMode.INFORMATIVE) {
            style.informationWidth = DSSupportMessageTokens.informationWidthTypeInformative
            style.informationHeight = DSSupportMessageTokens.informationHeightTypeInformative
        }
        if (props.mode == ODSSupportMessageMode.INFORMATIVE && !props.disabled) {
            style.informationColor = scheme.basicTextRecessive
        }
        if (props.mode == ODSSupportMessageMode.INFORMATIVE && props.disabled) {
            style.informationColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.mode == ODSSupportMessageMode.ERROR) {
            style.errorWidth = DSSupportMessageTokens.errorWidthTypeError
            style.errorHeight = DSSupportMessageTokens.errorHeightTypeError
        }
        if (props.mode == ODSSupportMessageMode.ERROR && !props.disabled) {
            style.errorColor = scheme.functionalDestructiveStandard
        }
        if (props.mode == ODSSupportMessageMode.ERROR && props.disabled) {
            style.errorColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.mode == ODSSupportMessageMode.SUCCESS) {
            style.successWidth = DSSupportMessageTokens.successWidthTypeSuccess
            style.successHeight = DSSupportMessageTokens.successHeightTypeSuccess
        }
        if (props.mode == ODSSupportMessageMode.SUCCESS && !props.disabled) {
            style.successColor = scheme.functionalSuccessStandard
        }
        if (props.mode == ODSSupportMessageMode.SUCCESS && props.disabled) {
            style.successColor = scheme.interactionStatesDisabledTextDisabled
        }
        style.labelTextStyle = DSSupportMessageTokens.labelTextStyle
        style.labelTextAlign = DSSupportMessageTokens.labelTextAlign
        if (props.disabled) {
            style.labelColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (!props.disabled) {
            style.labelColor = scheme.basicText
        }
        return style
    }
}
