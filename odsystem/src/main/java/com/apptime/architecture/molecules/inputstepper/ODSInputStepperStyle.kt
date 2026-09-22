package com.telekom.odsystem.molecules.inputstepper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSInputStepperStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var backgroundBackgroundColor: List<ODSColorModel>? = null
    var backgroundBorderRadius: ODSCorners? = null
    var backgroundBorder: Dp? = null
    var backgroundBorderColor: List<ODSColorModel>? = null
    var backgroundClipContent: Boolean? = null
    var contentBorderRadius: ODSCorners? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var contentPadding: ODSPadding? = null
    var valueTextStyle: ODSTextStyle? = null
    var valueColor: HexColor? = null
    var valueTextAlign: TextAlign? = null
    var valueMinWidth: Dp? = null
    var valueTextOverflow: TextOverflow? = null
    var backgroundPadding: ODSPadding? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSInputStepperProps
    ): ODSInputStepperStyle {
        val style = ODSInputStepperStyle()
        style.verticalAlignment = DSInputStepperTokens.verticalAlignment
        style.horizontalAlignment = DSInputStepperTokens.horizontalAlignment
        style.verticalArrangement = DSInputStepperTokens.verticalArrangement
        style.contentAlignment = DSInputStepperTokens.contentAlignment
        style.backgroundBorderRadius = DSInputStepperTokens.backgroundBorderRadius
        style.backgroundClipContent = DSInputStepperTokens.backgroundClipContent
        if (props.disabled) {
            style.backgroundBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        }
        if (props.type == ODSInputStepperType.OUTLINE) {
            style.backgroundBorder = DSInputStepperTokens.backgroundBorderTypeOutline
        }
        if (!props.disabled && !props.readOnly) {
            style.backgroundBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicBackground))
            style.backgroundBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (!props.disabled && props.readOnly) {
            style.backgroundBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        }
        if (props.type == ODSInputStepperType.OUTLINE && props.disabled) {
            style.backgroundBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.type == ODSInputStepperType.GHOST && !props.disabled && !props.readOnly) {
            style.backgroundBorder = DSInputStepperTokens.backgroundBorderTypeGhost
        }
        if (props.type == ODSInputStepperType.OUTLINE && !props.disabled && props.readOnly) {
            style.backgroundBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        style.contentBorderRadius = DSInputStepperTokens.contentBorderRadius
        style.contentVerticalAlignment = DSInputStepperTokens.contentVerticalAlignment
        style.contentHorizontalArrangement = DSInputStepperTokens.contentHorizontalArrangement
        if (props.size == ODSInputStepperSize.LARGE) {
            style.contentPadding = DSInputStepperTokens.contentPaddingSizeLarge
        }
        style.valueTextAlign = DSInputStepperTokens.valueTextAlign
        style.valueMinWidth = DSInputStepperTokens.valueMinWidth
        style.valueTextOverflow = DSInputStepperTokens.valueTextOverflow
        if (!props.disabled) {
            style.valueColor = scheme.basicText
        }
        if (props.size == ODSInputStepperSize.SMALL) {
            style.valueTextStyle = DSInputStepperTokens.valueTextStyleSizeSmall
        }
        if (props.size == ODSInputStepperSize.LARGE) {
            style.valueTextStyle = DSInputStepperTokens.valueTextStyleSizeLarge
        }
        if (props.disabled && !props.readOnly) {
            style.valueColor = scheme.interactionStatesDisabledTextDisabled
        }

        // Custom Addition
        if (props.size == ODSInputStepperSize.SMALL) {
            style.backgroundPadding = DSInputStepperTokens.backgroundPaddingSmall
        }
        return style
    }
}
