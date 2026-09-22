package com.telekom.odsystem.atoms.datepickerinputfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod", "ComplexCondition", "MaximumLineLength")
/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: 1bd061e7
 * Figma link: https://figma.com/design/ZSwasQrEi7Qi0JRbX3dMuB/Untitled?node-id=33-6401
 */

class ODSDatePickerInputFieldStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var inputFieldBackground: List<ODSColorModel>? = null
    var inputFieldPadding: ODSPadding? = null
    var inputFieldCornerRadius: ODSCorners? = null
    var inputFieldBorder: Dp? = null
    var inputFieldBorderColor: List<ODSColorModel>? = null
    var inputFieldMinHeight: Dp? = null
    var inputFieldClipContent: Boolean? = null
    var inputFieldVerticalAlignment: Alignment.Vertical? = null
    var inputFieldHorizontalAlignment: Alignment.Horizontal? = null
    var inputFieldHorizontalArrangement: Arrangement.Horizontal? = null
    var contentGap: Dp? = null
    var contentPadding: ODSPadding? = null
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
    var labelOverflow: TextOverflow? = null
    var labelMaxLines: Int? = null
    var requiredStyle: ODSTextStyle? = null
    var requiredColor: HexColor? = null
    var requiredTextAlign: TextAlign? = null // Not used cause of annotated string
    var inputValueVerticalAlignment: Alignment.Vertical? = null
    var inputValueHorizontalAlignment: Alignment.Horizontal? = null
    var inputValueHorizontalArrangement: Arrangement.Horizontal? = null
    var placeholderStyle: ODSTextStyle? = null
    var placeholderColor: HexColor? = null
    var placeholderTextAlign: TextAlign? = null
    var placeholderOverflow: TextOverflow? = null
    var placeholderMaxLines: Int? = null
    var dateInputStyle: ODSTextStyle? = null
    var dateInputColor: HexColor? = null
    var dateInputTextAlign: TextAlign? = null
    var dateInputOverflow: TextOverflow? = null
    var dateInputMaxLines: Int? = null
    var supportTextPadding: ODSPadding? = null
    var supportTextVerticalAlignment: Alignment.Vertical? = null
    var supportTextHorizontalAlignment: Alignment.Horizontal? = null
    var supportTextHorizontalArrangement: Arrangement.Horizontal? = null
    var inputValueCursorColor: HexColor? = null // Not exported from the plugin
    var contentContainerAlignment: Alignment? = null // Not exported from plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSDatePickerInputFieldProps,
        state: ODSActions,
    ): ODSDatePickerInputFieldStyle {
        val style = ODSDatePickerInputFieldStyle()
        style.gap = DSDatePickerInputFieldTokens.gap
        style.verticalAlignment = DSDatePickerInputFieldTokens.verticalAlignment
        style.horizontalAlignment = DSDatePickerInputFieldTokens.horizontalAlignment
        style.verticalArrangement = DSDatePickerInputFieldTokens.verticalArrangement
        style.inputFieldCornerRadius = DSDatePickerInputFieldTokens.inputFieldCornerRadius
        style.inputFieldClipContent = DSDatePickerInputFieldTokens.inputFieldClipContent
        style.inputFieldVerticalAlignment = DSDatePickerInputFieldTokens.inputFieldVerticalAlignment
        style.inputFieldHorizontalAlignment =
            DSDatePickerInputFieldTokens.inputFieldHorizontalAlignment
        style.inputFieldHorizontalArrangement =
            DSDatePickerInputFieldTokens.inputFieldHorizontalArrangement
        if (props.size == ODSDatePickerInputFieldSize.LARGE) {
            style.inputFieldPadding = DSDatePickerInputFieldTokens.inputFieldPaddingSizeLarge
            style.inputFieldMinHeight = DSDatePickerInputFieldTokens.inputFieldMinHeightSizeLarge
        }
        if (props.size == ODSDatePickerInputFieldSize.SMALL) {
            style.inputFieldPadding = DSDatePickerInputFieldTokens.inputFieldPaddingSizeSmall
            style.inputFieldMinHeight = DSDatePickerInputFieldTokens.inputFieldMinHeightSizeSmall
        }
        if (props.mode == ODSDatePickerInputFieldMode.STANDARD) {
            style.inputFieldBorder = DSDatePickerInputFieldTokens.inputFieldBorderModeStandard
        }
        if (props.mode == ODSDatePickerInputFieldMode.INFORMATIVE) {
            style.inputFieldBorder = DSDatePickerInputFieldTokens.inputFieldBorderModeInformative
        }
        if (!props.disabled && !props.readOnly) {
            style.inputFieldBackground = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (props.status == ODSDatePickerInputFieldStatus.UNFILLED && props.mode == ODSDatePickerInputFieldMode.INFORMATIVE && props.disabled) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.status == ODSDatePickerInputFieldStatus.UNFILLED && props.mode == ODSDatePickerInputFieldMode.STANDARD && props.disabled) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.mode == ODSDatePickerInputFieldMode.ERROR && !props.disabled && !props.readOnly) {
            style.inputFieldBorder = DSDatePickerInputFieldTokens.inputFieldBorderModeError
        }
        if (props.mode == ODSDatePickerInputFieldMode.INFORMATIVE && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.mode == ODSDatePickerInputFieldMode.STANDARD && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.status == ODSDatePickerInputFieldStatus.FILLED && props.mode == ODSDatePickerInputFieldMode.INFORMATIVE && !props.disabled && props.readOnly) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.status == ODSDatePickerInputFieldStatus.FILLED && props.mode == ODSDatePickerInputFieldMode.STANDARD && !props.disabled && props.readOnly) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.status == ODSDatePickerInputFieldStatus.FILLED && props.mode == ODSDatePickerInputFieldMode.ERROR && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (props.status == ODSDatePickerInputFieldStatus.EDITING && props.mode == ODSDatePickerInputFieldMode.ERROR && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (props.status == ODSDatePickerInputFieldStatus.UNFILLED && props.mode == ODSDatePickerInputFieldMode.ERROR && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (props.status == ODSDatePickerInputFieldStatus.FILLED && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (props.status == ODSDatePickerInputFieldStatus.UNFILLED && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (props.status == ODSDatePickerInputFieldStatus.FILLED && props.mode == ODSDatePickerInputFieldMode.ERROR && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveHovered))
        }
        style.contentPadding = DSDatePickerInputFieldTokens.contentPadding
        style.contentVerticalAlignment = DSDatePickerInputFieldTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSDatePickerInputFieldTokens.contentHorizontalAlignment
        style.contentVerticalArrangement = DSDatePickerInputFieldTokens.contentVerticalArrangement
        if (props.size == ODSDatePickerInputFieldSize.LARGE) {
            style.contentGap = DSDatePickerInputFieldTokens.contentGapSizeLarge
        }
        style.eyebrowGap = DSDatePickerInputFieldTokens.eyebrowGap
        style.eyebrowVerticalAlignment = DSDatePickerInputFieldTokens.eyebrowVerticalAlignment
        style.eyebrowHorizontalAlignment = DSDatePickerInputFieldTokens.eyebrowHorizontalAlignment
        style.eyebrowHorizontalArrangement =
            DSDatePickerInputFieldTokens.eyebrowHorizontalArrangement
        style.labelTextAlign = DSDatePickerInputFieldTokens.labelTextAlign
        style.labelOverflow = DSDatePickerInputFieldTokens.labelOverflow
        style.labelMaxLines = DSDatePickerInputFieldTokens.labelMaxLines
        if (!props.disabled) {
            style.labelColor = scheme.basicTextRecessive
        }
        if (props.size == ODSDatePickerInputFieldSize.SMALL && props.status == ODSDatePickerInputFieldStatus.UNFILLED) {
            style.labelStyle = DSDatePickerInputFieldTokens.labelStyleSizeSmallStatusUnfilled
        }
        if (props.size == ODSDatePickerInputFieldSize.LARGE && props.status == ODSDatePickerInputFieldStatus.EDITING) {
            style.labelStyle = DSDatePickerInputFieldTokens.labelStyleSizeLargeStatusEditing
        }
        if (props.size == ODSDatePickerInputFieldSize.LARGE && props.status == ODSDatePickerInputFieldStatus.FILLED) {
            style.labelStyle = DSDatePickerInputFieldTokens.labelStyleSizeLargeStatusFilled
        }
        if (props.size == ODSDatePickerInputFieldSize.LARGE && props.status == ODSDatePickerInputFieldStatus.UNFILLED && !props.readOnly) {
            style.labelStyle = DSDatePickerInputFieldTokens.labelStyleSizeLargeStatusUnfilled
        }
        if (props.size == ODSDatePickerInputFieldSize.SMALL && props.status == ODSDatePickerInputFieldStatus.FILLED && !props.disabled) {
            style.labelStyle = DSDatePickerInputFieldTokens.labelStyleSizeSmallStatusFilled
        }
        if (props.size == ODSDatePickerInputFieldSize.SMALL && props.status == ODSDatePickerInputFieldStatus.EDITING && !props.disabled) {
            style.labelStyle = DSDatePickerInputFieldTokens.labelStyleSizeSmallStatusEditing
        }
        if (props.status == ODSDatePickerInputFieldStatus.UNFILLED && props.mode == ODSDatePickerInputFieldMode.INFORMATIVE && props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.status == ODSDatePickerInputFieldStatus.UNFILLED && props.mode == ODSDatePickerInputFieldMode.STANDARD && props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.requiredTextAlign = DSDatePickerInputFieldTokens.requiredTextAlign
        if (!props.disabled) {
            style.requiredColor = scheme.basicTextRecessive
        }
        if (props.size == ODSDatePickerInputFieldSize.SMALL && props.status == ODSDatePickerInputFieldStatus.UNFILLED) {
            style.requiredStyle = DSDatePickerInputFieldTokens.requiredStyleSizeSmallStatusUnfilled
        }
        if (props.size == ODSDatePickerInputFieldSize.LARGE && props.status == ODSDatePickerInputFieldStatus.EDITING) {
            style.requiredStyle = DSDatePickerInputFieldTokens.requiredStyleSizeLargeStatusEditing
        }
        if (props.size == ODSDatePickerInputFieldSize.LARGE && props.status == ODSDatePickerInputFieldStatus.FILLED) {
            style.requiredStyle = DSDatePickerInputFieldTokens.requiredStyleSizeLargeStatusFilled
        }
        if (props.size == ODSDatePickerInputFieldSize.LARGE && props.status == ODSDatePickerInputFieldStatus.UNFILLED && !props.readOnly) {
            style.requiredStyle = DSDatePickerInputFieldTokens.requiredStyleSizeLargeStatusUnfilled
        }
        if (props.size == ODSDatePickerInputFieldSize.SMALL && props.status == ODSDatePickerInputFieldStatus.FILLED && !props.disabled) {
            style.requiredStyle = DSDatePickerInputFieldTokens.requiredStyleSizeSmallStatusFilled
        }
        if (props.size == ODSDatePickerInputFieldSize.SMALL && props.status == ODSDatePickerInputFieldStatus.EDITING && !props.disabled) {
            style.requiredStyle = DSDatePickerInputFieldTokens.requiredStyleSizeSmallStatusEditing
        }
        if (props.status == ODSDatePickerInputFieldStatus.UNFILLED && props.mode == ODSDatePickerInputFieldMode.INFORMATIVE && props.disabled && !props.readOnly) {
            style.requiredColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.status == ODSDatePickerInputFieldStatus.UNFILLED && props.mode == ODSDatePickerInputFieldMode.STANDARD && props.disabled && !props.readOnly) {
            style.requiredColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.inputValueVerticalAlignment = DSDatePickerInputFieldTokens.inputValueVerticalAlignment
        style.inputValueHorizontalAlignment =
            DSDatePickerInputFieldTokens.inputValueHorizontalAlignment
        style.inputValueHorizontalArrangement =
            DSDatePickerInputFieldTokens.inputValueHorizontalArrangement
        if (props.status == ODSDatePickerInputFieldStatus.EDITING && !props.readOnly) {
            style.placeholderColor = scheme.basicTextRecessive
            style.placeholderTextAlign =
                DSDatePickerInputFieldTokens.placeholderTextAlignStatusEditing
            style.placeholderOverflow =
                DSDatePickerInputFieldTokens.placeholderOverflowStatusEditing
            style.placeholderMaxLines =
                DSDatePickerInputFieldTokens.placeholderMaxLinesStatusEditing
        }
        if (props.status == ODSDatePickerInputFieldStatus.UNFILLED && !props.readOnly) {
            style.placeholderColor = scheme.basicTextRecessive
            style.placeholderTextAlign =
                DSDatePickerInputFieldTokens.placeholderTextAlignStatusUnfilled
            style.placeholderOverflow =
                DSDatePickerInputFieldTokens.placeholderOverflowStatusUnfilled
            style.placeholderMaxLines =
                DSDatePickerInputFieldTokens.placeholderMaxLinesStatusUnfilled
        }
        if (props.size == ODSDatePickerInputFieldSize.LARGE && props.status == ODSDatePickerInputFieldStatus.EDITING && !props.readOnly) {
            style.placeholderStyle =
                DSDatePickerInputFieldTokens.placeholderStyleSizeLargeStatusEditing
        }
        if (props.size == ODSDatePickerInputFieldSize.LARGE && props.status == ODSDatePickerInputFieldStatus.UNFILLED && !props.readOnly) {
            style.placeholderStyle =
                DSDatePickerInputFieldTokens.placeholderStyleSizeLargeStatusUnfilled
        }
        if (props.size == ODSDatePickerInputFieldSize.SMALL && props.status == ODSDatePickerInputFieldStatus.EDITING && !props.readOnly) {
            style.placeholderStyle =
                DSDatePickerInputFieldTokens.placeholderStyleSizeSmallStatusEditing
        }
        if (props.size == ODSDatePickerInputFieldSize.SMALL && props.status == ODSDatePickerInputFieldStatus.UNFILLED && !props.readOnly) {
            style.placeholderStyle =
                DSDatePickerInputFieldTokens.placeholderStyleSizeSmallStatusUnfilled
        }
        if (props.status == ODSDatePickerInputFieldStatus.FILLED && !props.disabled) {
            style.dateInputColor = scheme.basicText
            style.dateInputTextAlign = DSDatePickerInputFieldTokens.dateInputTextAlignStatusFilled
            style.dateInputOverflow = DSDatePickerInputFieldTokens.dateInputOverflowStatusFilled
            style.dateInputMaxLines = DSDatePickerInputFieldTokens.dateInputMaxLinesStatusFilled
        }
        if (props.size == ODSDatePickerInputFieldSize.LARGE && props.status == ODSDatePickerInputFieldStatus.FILLED && !props.disabled) {
            style.dateInputStyle = DSDatePickerInputFieldTokens.dateInputStyleSizeLargeStatusFilled
        }
        if (props.size == ODSDatePickerInputFieldSize.SMALL && props.status == ODSDatePickerInputFieldStatus.FILLED && !props.disabled) {
            style.dateInputStyle = DSDatePickerInputFieldTokens.dateInputStyleSizeSmallStatusFilled
        }
        style.supportTextPadding = DSDatePickerInputFieldTokens.supportTextPadding
        style.supportTextVerticalAlignment =
            DSDatePickerInputFieldTokens.supportTextVerticalAlignment
        style.supportTextHorizontalAlignment =
            DSDatePickerInputFieldTokens.supportTextHorizontalAlignment
        style.supportTextHorizontalArrangement =
            DSDatePickerInputFieldTokens.supportTextHorizontalArrangement

        // Custom addition
        style.inputValueCursorColor = scheme.basicAccent
        style.contentContainerAlignment = DSDatePickerInputFieldTokens.contentContainerAlignment
        if (props.size == ODSDatePickerInputFieldSize.LARGE) {
            style.dateInputStyle =
                DSDatePickerInputFieldTokens.dateInputStyleSizeLargeStatusFilled
        }
        if (props.size == ODSDatePickerInputFieldSize.SMALL) {
            style.dateInputStyle =
                DSDatePickerInputFieldTokens.dateInputStyleSizeSmallStatusFilled
        }
        return style
    }
}
