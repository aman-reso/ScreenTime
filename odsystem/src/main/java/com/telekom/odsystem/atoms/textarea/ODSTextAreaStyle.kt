package com.telekom.odsystem.atoms.textarea

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSTextAreaTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: a7bb15c
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-22305
 */
@Suppress("ALL")
class ODSTextAreaStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var inputFieldZStackMinHeight: Dp? = null // Not used in mobile
    var inputFieldGap: Dp? = null
    var inputFieldBackground: List<ODSColorModel>? = null
    var inputFieldPadding: ODSPadding? = null
    var inputFieldCornerRadius: ODSCorners? = null
    var inputFieldBorder: Dp? = null
    var inputFieldBorderColor: List<ODSColorModel>? = null
    var inputFieldMinHeight: Dp? = null
    var inputFieldVerticalAlignment: Alignment.Vertical? = null
    var inputFieldHorizontalAlignment: Alignment.Horizontal? = null
    var inputFieldHorizontalArrangement: Arrangement.Horizontal? = null
    var contentGap: Dp? = null
    var contentClipContent: Boolean? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentVerticalArrangement: Arrangement.Vertical? = null
    var eyebrowGap: Dp? = null
    var eyebrowVerticalAlignment: Alignment.Vertical? = null
    var eyebrowHorizontalAlignment: Alignment.Horizontal? = null
    var eyebrowHorizontalArrangement: Arrangement.Horizontal? = null
    var labelStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var requiredStyle: ODSTextStyle? = null
    var requiredColor: HexColor? = null
    var requiredTextAlign: TextAlign? = null // Not used cause of the annotated string
    var inputGap: Dp? = null
    var inputClipContent: Boolean? = null
    var inputVerticalAlignment: Alignment.Vertical? = null
    var inputHorizontalAlignment: Alignment.Horizontal? = null
    var inputHorizontalArrangement: Arrangement.Horizontal? = null
    var inputValueStyle: ODSTextStyle? = null
    var inputValueColor: HexColor? = null
    var inputValueTextAlign: TextAlign? = null
    var inputValueHeight: Dp? = null // Not used in mobile
    var supportTextPadding: ODSPadding? = null
    var supportTextVerticalAlignment: Alignment.Vertical? = null
    var supportTextHorizontalAlignment: Alignment.Horizontal? = null
    var supportTextHorizontalArrangement: Arrangement.Horizontal? = null
    var counterStyle: ODSTextStyle? = null
    var counterColor: HexColor? = null
    var counterTextAlign: TextAlign? = null
    var counterWidth: Dp? = null // Not used in mobile
    var inputCursorColor: HexColor? = null // Not exported from the plugin
    var contentContainerAlignment: Alignment? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSTextAreaProps,
        state: ODSActions,
    ): ODSTextAreaStyle {
        val style = ODSTextAreaStyle()
        style.gap = DSTextAreaTokens.gap
        style.verticalAlignment = DSTextAreaTokens.verticalAlignment
        style.horizontalAlignment = DSTextAreaTokens.horizontalAlignment
        style.verticalArrangement = DSTextAreaTokens.verticalArrangement
        style.inputFieldZStackMinHeight = DSTextAreaTokens.inputFieldZStackMinHeight
        style.inputFieldGap = DSTextAreaTokens.inputFieldGap
        style.inputFieldPadding = DSTextAreaTokens.inputFieldPadding
        style.inputFieldCornerRadius = DSTextAreaTokens.inputFieldCornerRadius
        style.inputFieldMinHeight = DSTextAreaTokens.inputFieldMinHeight
        style.inputFieldVerticalAlignment = DSTextAreaTokens.inputFieldVerticalAlignment
        style.inputFieldHorizontalAlignment = DSTextAreaTokens.inputFieldHorizontalAlignment
        style.inputFieldHorizontalArrangement = DSTextAreaTokens.inputFieldHorizontalArrangement
        if (props.mode == ODSTextAreaMode.STANDARD) {
            style.inputFieldBorder = DSTextAreaTokens.inputFieldBorderModeStandard
        }
        if (props.mode == ODSTextAreaMode.INFORMATIVE) {
            style.inputFieldBorder = DSTextAreaTokens.inputFieldBorderModeInformative
        }
        if (!props.disabled && !props.readOnly) {
            style.inputFieldBackground = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (!props.filled && props.mode == ODSTextAreaMode.INFORMATIVE && props.disabled) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (!props.filled && props.mode == ODSTextAreaMode.STANDARD && props.disabled) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.mode == ODSTextAreaMode.ERROR && !props.disabled && !props.readOnly) {
            style.inputFieldBorder = DSTextAreaTokens.inputFieldBorderModeError
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (props.mode == ODSTextAreaMode.INFORMATIVE && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.mode == ODSTextAreaMode.STANDARD && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.filled && props.mode == ODSTextAreaMode.INFORMATIVE && !props.disabled && props.readOnly) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.filled && props.mode == ODSTextAreaMode.STANDARD && !props.disabled && props.readOnly) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (!props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (props.mode == ODSTextAreaMode.ERROR && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveHovered))
        }
        if (props.filled && props.mode == ODSTextAreaMode.STANDARD && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (!props.filled && props.mode == ODSTextAreaMode.INFORMATIVE && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (!props.filled && props.mode == ODSTextAreaMode.STANDARD && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (props.filled && props.mode == ODSTextAreaMode.INFORMATIVE && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBorderColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
        style.contentClipContent = DSTextAreaTokens.contentClipContent
        style.contentVerticalAlignment = DSTextAreaTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSTextAreaTokens.contentHorizontalAlignment
        style.contentVerticalArrangement = DSTextAreaTokens.contentVerticalArrangement
        if (props.size == ODSTextAreaSize.LARGE) {
            style.contentGap = DSTextAreaTokens.contentGapSizeLarge
        }
        if (props.size == ODSTextAreaSize.SMALL) {
            style.contentGap = DSTextAreaTokens.contentGapSizeSmall
        }
        style.eyebrowGap = DSTextAreaTokens.eyebrowGap
        style.eyebrowVerticalAlignment = DSTextAreaTokens.eyebrowVerticalAlignment
        style.eyebrowHorizontalAlignment = DSTextAreaTokens.eyebrowHorizontalAlignment
        style.eyebrowHorizontalArrangement = DSTextAreaTokens.eyebrowHorizontalArrangement
        style.labelTextAlign = DSTextAreaTokens.labelTextAlign
        if (!props.disabled) {
            style.labelColor = scheme.basicTextRecessive
        }
        if (props.size == ODSTextAreaSize.LARGE && props.filled) {
            style.labelStyle = DSTextAreaTokens.labelStyleSizeLargeFilled
        }
        if (props.size == ODSTextAreaSize.SMALL && !props.filled) {
            style.labelStyle = DSTextAreaTokens.labelStyleSizeSmall
        }
        if (props.size == ODSTextAreaSize.LARGE && !props.filled && !props.readOnly) {
            style.labelStyle = DSTextAreaTokens.labelStyleSizeLarge
        }
        if (props.size == ODSTextAreaSize.SMALL && props.filled && !props.disabled) {
            style.labelStyle = DSTextAreaTokens.labelStyleSizeSmallFilled
        }
        if (!props.filled && props.mode == ODSTextAreaMode.INFORMATIVE && props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (!props.filled && props.mode == ODSTextAreaMode.STANDARD && props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.requiredTextAlign = DSTextAreaTokens.requiredTextAlign
        if (!props.disabled) {
            style.requiredColor = scheme.basicTextRecessive
        }
        if (props.size == ODSTextAreaSize.LARGE && props.filled) {
            style.requiredStyle = DSTextAreaTokens.requiredStyleSizeLargeFilled
        }
        if (props.size == ODSTextAreaSize.SMALL && !props.filled) {
            style.requiredStyle = DSTextAreaTokens.requiredStyleSizeSmall
        }
        if (props.size == ODSTextAreaSize.LARGE && !props.filled && !props.readOnly) {
            style.requiredStyle = DSTextAreaTokens.requiredStyleSizeLarge
        }
        if (props.size == ODSTextAreaSize.SMALL && props.filled && !props.disabled) {
            style.requiredStyle = DSTextAreaTokens.requiredStyleSizeSmallFilled
        }
        if (!props.filled && props.mode == ODSTextAreaMode.INFORMATIVE && props.disabled && !props.readOnly) {
            style.requiredColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (!props.filled && props.mode == ODSTextAreaMode.STANDARD && props.disabled && !props.readOnly) {
            style.requiredColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.inputGap = DSTextAreaTokens.inputGap
        style.inputClipContent = DSTextAreaTokens.inputClipContent
        style.inputVerticalAlignment = DSTextAreaTokens.inputVerticalAlignment
        style.inputHorizontalAlignment = DSTextAreaTokens.inputHorizontalAlignment
        style.inputHorizontalArrangement = DSTextAreaTokens.inputHorizontalArrangement
        style.inputValueColor = scheme.basicText
        style.inputValueTextAlign = DSTextAreaTokens.inputValueTextAlign
        if (props.size == ODSTextAreaSize.LARGE) {
            style.inputValueStyle = DSTextAreaTokens.inputValueStyleSizeLarge
        }
        if (props.size == ODSTextAreaSize.SMALL) {
            style.inputValueStyle = DSTextAreaTokens.inputValueStyleSizeSmall
        }
        if (props.size == ODSTextAreaSize.LARGE && !props.filled && !props.readOnly) {
            style.inputValueHeight = DSTextAreaTokens.inputValueHeightSizeLarge
        }
        if (props.size == ODSTextAreaSize.SMALL && !props.filled && !props.readOnly) {
            style.inputValueHeight = DSTextAreaTokens.inputValueHeightSizeSmall
        }
        style.supportTextPadding = DSTextAreaTokens.supportTextPadding
        style.supportTextVerticalAlignment = DSTextAreaTokens.supportTextVerticalAlignment
        style.supportTextHorizontalAlignment = DSTextAreaTokens.supportTextHorizontalAlignment
        style.supportTextHorizontalArrangement = DSTextAreaTokens.supportTextHorizontalArrangement
        style.counterStyle = DSTextAreaTokens.counterStyle
        style.counterTextAlign = DSTextAreaTokens.counterTextAlign
        style.counterWidth = DSTextAreaTokens.counterWidth
        if (!props.disabled) {
            style.counterColor = scheme.basicTextRecessive
        }
        if (!props.filled && props.mode == ODSTextAreaMode.INFORMATIVE && props.disabled && !props.readOnly) {
            style.counterColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (!props.filled && props.mode == ODSTextAreaMode.STANDARD && props.disabled && !props.readOnly) {
            style.counterColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }

        // Custom additions
        style.inputCursorColor = scheme.basicAccent
        style.contentContainerAlignment = DSTextAreaTokens.contentContainerAlignment
        return style
    }
}
