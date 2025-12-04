package com.telekom.odsystem.atoms.radiobutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSRadioButtonTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-12 (v1.31.6) - uid: 41eea73d
 * Figma link: https://figma.com/design/eQMS6upybd4Lu9HDpAxd4M/ODS_Base_Production_Library?node-id=4398-37849
 */

class ODSRadioButtonStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var labelMessageGap: Dp? = null
    var labelMessagePadding: ODSPadding? = null
    var labelMessageVerticalAlignment: Alignment.Vertical? = null
    var labelMessageHorizontalAlignment: Alignment.Horizontal? = null
    var labelMessageVerticalArrangement: Arrangement.Vertical? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSRadioButtonProps
    ): ODSRadioButtonStyle {
        val style = ODSRadioButtonStyle()
        style.minHeight = DSRadioButtonTokens.minHeight
        style.verticalAlignment = DSRadioButtonTokens.verticalAlignment
        style.horizontalAlignment = DSRadioButtonTokens.horizontalAlignment
        style.horizontalArrangement = DSRadioButtonTokens.horizontalArrangement
        if (props.size == ODSRadioButtonSize.LARGE) {
            style.gap = DSRadioButtonTokens.gapSizeLarge
            style.padding = DSRadioButtonTokens.paddingSizeLarge
        }
        if (props.size == ODSRadioButtonSize.SMALL) {
            style.gap = DSRadioButtonTokens.gapSizeSmall
            style.padding = DSRadioButtonTokens.paddingSizeSmall
        }
        style.labelMessageGap = DSRadioButtonTokens.labelMessageGap
        style.labelMessageVerticalAlignment = DSRadioButtonTokens.labelMessageVerticalAlignment
        style.labelMessageHorizontalAlignment = DSRadioButtonTokens.labelMessageHorizontalAlignment
        style.labelMessageVerticalArrangement = DSRadioButtonTokens.labelMessageVerticalArrangement
        if (props.size == ODSRadioButtonSize.LARGE) {
            style.labelMessagePadding = DSRadioButtonTokens.labelMessagePaddingSizeLarge
        }
        if (props.size == ODSRadioButtonSize.SMALL) {
            style.labelMessagePadding = DSRadioButtonTokens.labelMessagePaddingSizeSmall
        }
        style.labelTextAlign = DSRadioButtonTokens.labelTextAlign
        if (!props.disabled) {
            style.labelColor = scheme.basicText
        }
        if (props.size == ODSRadioButtonSize.LARGE) {
            style.labelTextStyle = DSRadioButtonTokens.labelTextStyleSizeLarge
        }
        if (props.size == ODSRadioButtonSize.SMALL) {
            style.labelTextStyle = DSRadioButtonTokens.labelTextStyleSizeSmall
        }
        if (props.mode == ODSRadioButtonMode.INFORMATIVE && props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.mode == ODSRadioButtonMode.STANDARD && props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        return style
    }
}
