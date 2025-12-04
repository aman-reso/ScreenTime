package com.telekom.odsystem.atoms.inputitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSInputItemTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("All")
class ODSInputItemStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var borderRadius: ODSCorners? = null
    var border: Dp? = null
    var borderColor: List<ODSColorModel>? = null
    var width: Dp? = null
    var height: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var placeholderTextStyle: ODSTextStyle? = null
    var placeholderColor: HexColor? = null
    var placeholderTextAlign: TextAlign? = null
    var inputValueTextStyle: ODSTextStyle? = null
    var inputValueColor: HexColor? = null
    var inputValueTextAlign: TextAlign? = null
    var dotColor: List<ODSColorModel>? = null // Not exported from the plugin
    var dotWidth: Dp? = null // Not exported from the plugin
    var dotHeight: Dp? = null // Not exported from the plugin

    fun getStyle(
        scheme: ODSTheme,
        props: ODSInputItemProps
    ): ODSInputItemStyle {
        val style = ODSInputItemStyle()
        style.borderRadius = DSInputItemTokens.borderRadius
        style.width = DSInputItemTokens.width
        style.height = DSInputItemTokens.height
        style.verticalAlignment = DSInputItemTokens.verticalAlignment
        style.horizontalAlignment = DSInputItemTokens.horizontalAlignment
        style.horizontalArrangement = DSInputItemTokens.horizontalArrangement
        if (props.mode == ODSInputItemMode.STANDARD) {
            style.border = DSInputItemTokens.borderModeStandard
        }
        if (!props.disabled && !props.readOnly) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (props.mode == ODSInputItemMode.ERROR && !props.disabled && !props.readOnly) {
            style.border = DSInputItemTokens.borderModeError
            style.borderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (props.mode == ODSInputItemMode.STANDARD && !props.disabled && !props.readOnly) {
            style.borderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.mode == ODSInputItemMode.STANDARD && props.disabled && !props.readOnly && !props.masked) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.borderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.mode == ODSInputItemMode.STANDARD && !props.disabled && props.readOnly && !props.inputText.isNullOrEmpty() && !props.masked) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.borderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.mode == ODSInputItemMode.STANDARD && props.disabled && props.readOnly && !props.inputText.isNullOrEmpty() && !props.masked) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.borderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (!props.readOnly && props.inputText.isNullOrEmpty() && !props.masked) {
            style.placeholderTextStyle = DSInputItemTokens.placeholderTextStyle
            style.placeholderTextAlign = DSInputItemTokens.placeholderTextAlign
        }
        if (!props.disabled && !props.readOnly && props.inputText.isNullOrEmpty() && !props.masked) {
            style.placeholderColor = scheme.basicTextRecessive
        }
        if (props.mode == ODSInputItemMode.STANDARD && props.disabled && !props.readOnly && props.inputText.isNullOrEmpty() && !props.masked) {
            style.placeholderColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.disabled && !props.inputText.isNullOrEmpty() && !props.masked) {
            style.inputValueTextStyle = DSInputItemTokens.inputValueTextStyleFilled
            style.inputValueColor = scheme.basicText
            style.inputValueTextAlign = DSInputItemTokens.inputValueTextAlignFilled
        }

        // Custom addition
        style.dotColor =
            listOf(ODSColorModel(hexColor = scheme.basicText)) // Not exported from the plugin
        style.dotWidth = DSInputItemTokens.dotWidth // Not exported from the plugin
        style.dotHeight = DSInputItemTokens.dotHeight // Not exported from the plugin
        if (props.isFocused && props.mode != ODSInputItemMode.ERROR) {
            style.border = DSInputItemTokens.borderStateFocused
            style.borderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        return style
    }
}
