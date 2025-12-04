package com.telekom.odsystem.atoms.checkbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSCheckboxTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-16 (v1.31.6) - uid: 4592c67e
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=15509-8039
 */

class ODSCheckboxStyle {
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
    var labelStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCheckboxProps
    ): ODSCheckboxStyle {
        val style = ODSCheckboxStyle()
        style.minHeight = DSCheckboxTokens.minHeight
        style.verticalAlignment = DSCheckboxTokens.verticalAlignment
        style.horizontalAlignment = DSCheckboxTokens.horizontalAlignment
        style.horizontalArrangement = DSCheckboxTokens.horizontalArrangement
        if (props.size == ODSCheckboxSize.LARGE) {
            style.gap = DSCheckboxTokens.gapSizeLarge
            style.padding = DSCheckboxTokens.paddingSizeLarge
        }
        if (props.size == ODSCheckboxSize.SMALL) {
            style.gap = DSCheckboxTokens.gapSizeSmall
            style.padding = DSCheckboxTokens.paddingSizeSmall
        }
        style.labelMessageGap = DSCheckboxTokens.labelMessageGap
        style.labelMessageVerticalAlignment = DSCheckboxTokens.labelMessageVerticalAlignment
        style.labelMessageHorizontalAlignment = DSCheckboxTokens.labelMessageHorizontalAlignment
        style.labelMessageVerticalArrangement = DSCheckboxTokens.labelMessageVerticalArrangement
        if (props.size == ODSCheckboxSize.LARGE) {
            style.labelMessagePadding = DSCheckboxTokens.labelMessagePaddingSizeLarge
        }
        if (props.size == ODSCheckboxSize.SMALL) {
            style.labelMessagePadding = DSCheckboxTokens.labelMessagePaddingSizeSmall
        }
        style.labelTextAlign = DSCheckboxTokens.labelTextAlign
        if (!props.disabled) {
            style.labelColor = scheme.basicText
        }
        if (props.size == ODSCheckboxSize.LARGE) {
            style.labelStyle = DSCheckboxTokens.labelStyleSizeLarge
        }
        if (props.size == ODSCheckboxSize.SMALL) {
            style.labelStyle = DSCheckboxTokens.labelStyleSizeSmall
        }
        if (props.mode == ODSCheckboxMode.INFORMATIVE && props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.mode == ODSCheckboxMode.STANDARD && props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        return style
    }
}
