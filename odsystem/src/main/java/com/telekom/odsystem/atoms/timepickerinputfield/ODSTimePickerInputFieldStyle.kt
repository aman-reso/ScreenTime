package com.telekom.odsystem.atoms.timepickerinputfield

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
 * 2025-08-01 (v1.32.3) - uid: 14fd7671
 * Figma link: https://figma.com/design/ZSwasQrEi7Qi0JRbX3dMuB/Untitled?node-id=37-5394
 */

class ODSTimePickerInputFieldStyle {
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
    var inputValueGap: Dp? = null
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
        props: ODSTimePickerInputFieldProps,
        state: ODSActions,
    ): ODSTimePickerInputFieldStyle {
        val style = ODSTimePickerInputFieldStyle()
        style.gap = DSTimePickerInputFieldTokens.gap
        style.verticalAlignment = DSTimePickerInputFieldTokens.verticalAlignment
        style.horizontalAlignment = DSTimePickerInputFieldTokens.horizontalAlignment
        style.verticalArrangement = DSTimePickerInputFieldTokens.verticalArrangement
        style.inputFieldCornerRadius = DSTimePickerInputFieldTokens.inputFieldCornerRadius
        style.inputFieldClipContent = DSTimePickerInputFieldTokens.inputFieldClipContent
        style.inputFieldVerticalAlignment = DSTimePickerInputFieldTokens.inputFieldVerticalAlignment
        style.inputFieldHorizontalAlignment =
            DSTimePickerInputFieldTokens.inputFieldHorizontalAlignment
        style.inputFieldHorizontalArrangement =
            DSTimePickerInputFieldTokens.inputFieldHorizontalArrangement
        if (props.size == ODSTimePickerInputFieldSize.LARGE) {
            style.inputFieldPadding = DSTimePickerInputFieldTokens.inputFieldPaddingSizeLarge
            style.inputFieldMinHeight = DSTimePickerInputFieldTokens.inputFieldMinHeightSizeLarge
        }
        if (props.size == ODSTimePickerInputFieldSize.SMALL) {
            style.inputFieldPadding = DSTimePickerInputFieldTokens.inputFieldPaddingSizeSmall
            style.inputFieldMinHeight = DSTimePickerInputFieldTokens.inputFieldMinHeightSizeSmall
        }
        if (props.mode == ODSTimePickerInputFieldMode.STANDARD) {
            style.inputFieldBorder = DSTimePickerInputFieldTokens.inputFieldBorderModeStandard
        }
        if (props.mode == ODSTimePickerInputFieldMode.INFORMATIVE) {
            style.inputFieldBorder = DSTimePickerInputFieldTokens.inputFieldBorderModeInformative
        }
        if (!props.disabled && !props.readOnly) {
            style.inputFieldBackground = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (props.status == ODSTimePickerInputFieldStatus.UNFILLED && props.mode == ODSTimePickerInputFieldMode.INFORMATIVE && props.disabled) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.status == ODSTimePickerInputFieldStatus.UNFILLED && props.mode == ODSTimePickerInputFieldMode.STANDARD && props.disabled) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.mode == ODSTimePickerInputFieldMode.ERROR && !props.disabled && !props.readOnly) {
            style.inputFieldBorder = DSTimePickerInputFieldTokens.inputFieldBorderModeError
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (props.mode == ODSTimePickerInputFieldMode.INFORMATIVE && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.mode == ODSTimePickerInputFieldMode.STANDARD && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.status == ODSTimePickerInputFieldStatus.FILLED && props.mode == ODSTimePickerInputFieldMode.INFORMATIVE && !props.disabled && props.readOnly) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.status == ODSTimePickerInputFieldStatus.FILLED && props.mode == ODSTimePickerInputFieldMode.STANDARD && !props.disabled && props.readOnly) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.status == ODSTimePickerInputFieldStatus.FILLED && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (props.status == ODSTimePickerInputFieldStatus.UNFILLED && props.mode == ODSTimePickerInputFieldMode.INFORMATIVE && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (props.status == ODSTimePickerInputFieldStatus.UNFILLED && props.mode == ODSTimePickerInputFieldMode.STANDARD && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (props.status == ODSTimePickerInputFieldStatus.FILLED && props.mode == ODSTimePickerInputFieldMode.ERROR && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveHovered))
        }
        style.contentPadding = DSTimePickerInputFieldTokens.contentPadding
        style.contentVerticalAlignment = DSTimePickerInputFieldTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSTimePickerInputFieldTokens.contentHorizontalAlignment
        style.contentVerticalArrangement = DSTimePickerInputFieldTokens.contentVerticalArrangement
        if (props.size == ODSTimePickerInputFieldSize.LARGE) {
            style.contentGap = DSTimePickerInputFieldTokens.contentGapSizeLarge
        }
        style.eyebrowGap = DSTimePickerInputFieldTokens.eyebrowGap
        style.eyebrowVerticalAlignment = DSTimePickerInputFieldTokens.eyebrowVerticalAlignment
        style.eyebrowHorizontalAlignment = DSTimePickerInputFieldTokens.eyebrowHorizontalAlignment
        style.eyebrowHorizontalArrangement =
            DSTimePickerInputFieldTokens.eyebrowHorizontalArrangement
        style.labelTextAlign = DSTimePickerInputFieldTokens.labelTextAlign
        style.labelOverflow = DSTimePickerInputFieldTokens.labelOverflow
        style.labelMaxLines = DSTimePickerInputFieldTokens.labelMaxLines
        if (!props.disabled) {
            style.labelColor = scheme.basicTextRecessive
        }
        if (props.size == ODSTimePickerInputFieldSize.SMALL && props.status == ODSTimePickerInputFieldStatus.UNFILLED) {
            style.labelStyle = DSTimePickerInputFieldTokens.labelStyleSizeSmallStatusUnfilled
        }
        if (props.size == ODSTimePickerInputFieldSize.LARGE && props.status == ODSTimePickerInputFieldStatus.EDITING) {
            style.labelStyle = DSTimePickerInputFieldTokens.labelStyleSizeLargeStatusEditing
        }
        if (props.size == ODSTimePickerInputFieldSize.LARGE && props.status == ODSTimePickerInputFieldStatus.FILLED) {
            style.labelStyle = DSTimePickerInputFieldTokens.labelStyleSizeLargeStatusFilled
        }
        if (props.size == ODSTimePickerInputFieldSize.LARGE && props.status == ODSTimePickerInputFieldStatus.UNFILLED && !props.readOnly) {
            style.labelStyle = DSTimePickerInputFieldTokens.labelStyleSizeLargeStatusUnfilled
        }
        if (props.size == ODSTimePickerInputFieldSize.SMALL && props.status == ODSTimePickerInputFieldStatus.FILLED && !props.disabled) {
            style.labelStyle = DSTimePickerInputFieldTokens.labelStyleSizeSmallStatusFilled
        }
        if (props.size == ODSTimePickerInputFieldSize.SMALL && props.status == ODSTimePickerInputFieldStatus.EDITING && !props.disabled) {
            style.labelStyle = DSTimePickerInputFieldTokens.labelStyleSizeSmallStatusEditing
        }
        if (props.status == ODSTimePickerInputFieldStatus.UNFILLED && props.mode == ODSTimePickerInputFieldMode.INFORMATIVE && props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.status == ODSTimePickerInputFieldStatus.UNFILLED && props.mode == ODSTimePickerInputFieldMode.STANDARD && props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.requiredTextAlign = DSTimePickerInputFieldTokens.requiredTextAlign
        if (!props.disabled) {
            style.requiredColor = scheme.basicTextRecessive
        }
        if (props.size == ODSTimePickerInputFieldSize.SMALL && props.status == ODSTimePickerInputFieldStatus.UNFILLED) {
            style.requiredStyle = DSTimePickerInputFieldTokens.requiredStyleSizeSmallStatusUnfilled
        }
        if (props.size == ODSTimePickerInputFieldSize.LARGE && props.status == ODSTimePickerInputFieldStatus.EDITING) {
            style.requiredStyle = DSTimePickerInputFieldTokens.requiredStyleSizeLargeStatusEditing
        }
        if (props.size == ODSTimePickerInputFieldSize.LARGE && props.status == ODSTimePickerInputFieldStatus.FILLED) {
            style.requiredStyle = DSTimePickerInputFieldTokens.requiredStyleSizeLargeStatusFilled
        }
        if (props.size == ODSTimePickerInputFieldSize.LARGE && props.status == ODSTimePickerInputFieldStatus.UNFILLED && !props.readOnly) {
            style.requiredStyle = DSTimePickerInputFieldTokens.requiredStyleSizeLargeStatusUnfilled
        }
        if (props.size == ODSTimePickerInputFieldSize.SMALL && props.status == ODSTimePickerInputFieldStatus.FILLED && !props.disabled) {
            style.requiredStyle = DSTimePickerInputFieldTokens.requiredStyleSizeSmallStatusFilled
        }
        if (props.size == ODSTimePickerInputFieldSize.SMALL && props.status == ODSTimePickerInputFieldStatus.EDITING && !props.disabled) {
            style.requiredStyle = DSTimePickerInputFieldTokens.requiredStyleSizeSmallStatusEditing
        }
        if (props.status == ODSTimePickerInputFieldStatus.UNFILLED && props.mode == ODSTimePickerInputFieldMode.INFORMATIVE && props.disabled && !props.readOnly) {
            style.requiredColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.status == ODSTimePickerInputFieldStatus.UNFILLED && props.mode == ODSTimePickerInputFieldMode.STANDARD && props.disabled && !props.readOnly) {
            style.requiredColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.inputValueGap = DSTimePickerInputFieldTokens.inputValueGap
        style.inputValueVerticalAlignment = DSTimePickerInputFieldTokens.inputValueVerticalAlignment
        style.inputValueHorizontalAlignment =
            DSTimePickerInputFieldTokens.inputValueHorizontalAlignment
        style.inputValueHorizontalArrangement =
            DSTimePickerInputFieldTokens.inputValueHorizontalArrangement
        if (props.status == ODSTimePickerInputFieldStatus.EDITING && !props.readOnly) {
            style.placeholderColor = scheme.basicTextRecessive
            style.placeholderTextAlign =
                DSTimePickerInputFieldTokens.placeholderTextAlignStatusEditing
            style.placeholderOverflow =
                DSTimePickerInputFieldTokens.placeholderOverflowStatusEditing
            style.placeholderMaxLines =
                DSTimePickerInputFieldTokens.placeholderMaxLinesStatusEditing
        }
        if (props.status == ODSTimePickerInputFieldStatus.UNFILLED && !props.readOnly) {
            style.placeholderColor = scheme.basicTextRecessive
            style.placeholderTextAlign =
                DSTimePickerInputFieldTokens.placeholderTextAlignStatusUnfilled
            style.placeholderOverflow =
                DSTimePickerInputFieldTokens.placeholderOverflowStatusUnfilled
            style.placeholderMaxLines =
                DSTimePickerInputFieldTokens.placeholderMaxLinesStatusUnfilled
        }
        if (props.size == ODSTimePickerInputFieldSize.LARGE && props.status == ODSTimePickerInputFieldStatus.EDITING && !props.readOnly) {
            style.placeholderStyle =
                DSTimePickerInputFieldTokens.placeholderStyleSizeLargeStatusEditing
        }
        if (props.size == ODSTimePickerInputFieldSize.LARGE && props.status == ODSTimePickerInputFieldStatus.UNFILLED && !props.readOnly) {
            style.placeholderStyle =
                DSTimePickerInputFieldTokens.placeholderStyleSizeLargeStatusUnfilled
        }
        if (props.size == ODSTimePickerInputFieldSize.SMALL && props.status == ODSTimePickerInputFieldStatus.EDITING && !props.readOnly) {
            style.placeholderStyle =
                DSTimePickerInputFieldTokens.placeholderStyleSizeSmallStatusEditing
        }
        if (props.size == ODSTimePickerInputFieldSize.SMALL && props.status == ODSTimePickerInputFieldStatus.UNFILLED && !props.readOnly) {
            style.placeholderStyle =
                DSTimePickerInputFieldTokens.placeholderStyleSizeSmallStatusUnfilled
        }
        if (props.status == ODSTimePickerInputFieldStatus.FILLED && !props.disabled) {
            style.dateInputColor = scheme.basicText
            style.dateInputTextAlign = DSTimePickerInputFieldTokens.dateInputTextAlignStatusFilled
            style.dateInputOverflow = DSTimePickerInputFieldTokens.dateInputOverflowStatusFilled
            style.dateInputMaxLines = DSTimePickerInputFieldTokens.dateInputMaxLinesStatusFilled
        }
        if (props.size == ODSTimePickerInputFieldSize.LARGE && props.status == ODSTimePickerInputFieldStatus.FILLED && !props.disabled) {
            style.dateInputStyle = DSTimePickerInputFieldTokens.dateInputStyleSizeLargeStatusFilled
        }
        if (props.size == ODSTimePickerInputFieldSize.SMALL && props.status == ODSTimePickerInputFieldStatus.FILLED && !props.disabled) {
            style.dateInputStyle = DSTimePickerInputFieldTokens.dateInputStyleSizeSmallStatusFilled
        }
        style.supportTextPadding = DSTimePickerInputFieldTokens.supportTextPadding
        style.supportTextVerticalAlignment =
            DSTimePickerInputFieldTokens.supportTextVerticalAlignment
        style.supportTextHorizontalAlignment =
            DSTimePickerInputFieldTokens.supportTextHorizontalAlignment
        style.supportTextHorizontalArrangement =
            DSTimePickerInputFieldTokens.supportTextHorizontalArrangement

        // Custom addition
        style.inputValueCursorColor = scheme.basicAccent
        style.contentContainerAlignment = DSTimePickerInputFieldTokens.contentContainerAlignment
        if (props.size == ODSTimePickerInputFieldSize.LARGE) {
            style.dateInputStyle =
                DSTimePickerInputFieldTokens.dateInputStyleSizeLargeStatusFilled
        }
        if (props.size == ODSTimePickerInputFieldSize.SMALL) {
            style.dateInputStyle =
                DSTimePickerInputFieldTokens.dateInputStyleSizeSmallStatusFilled
        }
        return style
    }
}
