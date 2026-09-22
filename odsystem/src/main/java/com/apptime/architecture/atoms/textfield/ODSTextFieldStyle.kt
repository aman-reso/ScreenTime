package com.telekom.odsystem.atoms.textfield

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

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: a7adc77
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-20892
 */
@Suppress("ALL")
class ODSTextFieldStyle {
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
    var labelMaxLines: Int? = null
    var requiredStyle: ODSTextStyle? = null
    var requiredColor: HexColor? = null
    var requiredTextAlign: TextAlign? = null // Not used cause of annotated string
    var inputGap: Dp? = null
    var inputVerticalAlignment: Alignment.Vertical? = null
    var inputHorizontalAlignment: Alignment.Horizontal? = null
    var inputHorizontalArrangement: Arrangement.Horizontal? = null
    var leftIconColor: HexColor? = null
    var leftIconWidth: Dp? = null
    var leftIconHeight: Dp? = null
    var prefixStyle: ODSTextStyle? = null
    var prefixColor: HexColor? = null
    var prefixTextAlign: TextAlign? = null
    var cursorGroupVerticalAlignment: Alignment.Vertical? = null
    var cursorGroupHorizontalAlignment: Alignment.Horizontal? = null
    var cursorGroupHorizontalArrangement: Arrangement.Horizontal? = null
    var cursorGroupGap: Dp? = null
    var inputValueStyle: ODSTextStyle? = null
    var inputValueColor: HexColor? = null
    var inputValueTextAlign: TextAlign? = null
    var inputValueOverflow: TextOverflow? = null
    var inputValueMaxLines: Int? = null
    var placeholderStyle: ODSTextStyle? = null
    var placeholderColor: HexColor? = null
    var placeholderTextAlign: TextAlign? = null
    var placeholderOverflow: TextOverflow? = null
    var placeholderMaxLines: Int? = null
    var suffixStyle: ODSTextStyle? = null
    var suffixColor: HexColor? = null
    var suffixTextAlign: TextAlign? = null
    var iconContainerPadding: ODSPadding? =
        null // To be removed once with deprecated icon container
    var iconContainerWidth: Dp? = null // To be removed once with deprecated icon container
    var iconContainerHeight: Dp? = null // To be removed once with deprecated icon container
    var iconContainerVerticalAlignment: Alignment.Vertical? = null
    var iconContainerHorizontalAlignment: Alignment.Horizontal? = null
    var iconContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var rightIconColor: HexColor? = null
    var rightIconWidth: Dp? = null
    var rightIconHeight: Dp? = null
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
        props: ODSTextFieldProps,
        state: ODSActions,
    ): ODSTextFieldStyle {
        val style = ODSTextFieldStyle()
        style.gap = DSTextFieldTokens.gap
        style.verticalAlignment = DSTextFieldTokens.verticalAlignment
        style.horizontalAlignment = DSTextFieldTokens.horizontalAlignment
        style.verticalArrangement = DSTextFieldTokens.verticalArrangement
        style.inputFieldPadding = DSTextFieldTokens.inputFieldPadding
        style.inputFieldCornerRadius = DSTextFieldTokens.inputFieldCornerRadius
        style.inputFieldClipContent = DSTextFieldTokens.inputFieldClipContent
        style.inputFieldVerticalAlignment = DSTextFieldTokens.inputFieldVerticalAlignment
        style.inputFieldHorizontalAlignment = DSTextFieldTokens.inputFieldHorizontalAlignment
        style.inputFieldHorizontalArrangement = DSTextFieldTokens.inputFieldHorizontalArrangement
        if (props.size == ODSTextFieldSize.LARGE) {
            style.inputFieldMinHeight = DSTextFieldTokens.inputFieldMinHeightSizeLarge
        }
        if (props.size == ODSTextFieldSize.SMALL) {
            style.inputFieldMinHeight = DSTextFieldTokens.inputFieldMinHeightSizeSmall
        }
        if (props.mode == ODSTextFieldMode.STANDARD) {
            style.inputFieldBorder = DSTextFieldTokens.inputFieldBorderModeStandard
        }
        if (props.mode == ODSTextFieldMode.INFORMATIVE) {
            style.inputFieldBorder = DSTextFieldTokens.inputFieldBorderModeInformative
        }
        if (!props.disabled && !props.readOnly) {
            style.inputFieldBackground = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (props.mode == ODSTextFieldMode.INFORMATIVE && !props.filled && props.disabled) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.mode == ODSTextFieldMode.STANDARD && !props.filled && props.disabled) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.mode == ODSTextFieldMode.ERROR && !props.disabled && !props.readOnly) {
            style.inputFieldBorder = DSTextFieldTokens.inputFieldBorderModeError
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (props.mode == ODSTextFieldMode.INFORMATIVE && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.mode == ODSTextFieldMode.STANDARD && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.mode == ODSTextFieldMode.INFORMATIVE && props.filled && !props.disabled && props.readOnly) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.mode == ODSTextFieldMode.STANDARD && props.filled && !props.disabled && props.readOnly) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (!props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (props.mode == ODSTextFieldMode.INFORMATIVE && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (props.mode == ODSTextFieldMode.STANDARD && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (props.mode == ODSTextFieldMode.ERROR && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveHovered))
        }
        style.contentPadding = DSTextFieldTokens.contentPadding
        style.contentVerticalAlignment = DSTextFieldTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSTextFieldTokens.contentHorizontalAlignment
        style.contentVerticalArrangement = DSTextFieldTokens.contentVerticalArrangement
        if (props.size == ODSTextFieldSize.LARGE) {
            style.contentGap = DSTextFieldTokens.contentGapSizeLarge
        }
        style.eyebrowGap = DSTextFieldTokens.eyebrowGap
        style.eyebrowVerticalAlignment = DSTextFieldTokens.eyebrowVerticalAlignment
        style.eyebrowHorizontalAlignment = DSTextFieldTokens.eyebrowHorizontalAlignment
        style.eyebrowHorizontalArrangement = DSTextFieldTokens.eyebrowHorizontalArrangement
        style.labelTextAlign = DSTextFieldTokens.labelTextAlign
        style.labelMaxLines = DSTextFieldTokens.labelMaxLines
        if (!props.disabled) {
            style.labelColor = scheme.basicTextRecessive
        }
        if (props.size == ODSTextFieldSize.LARGE && props.showPlaceholder) {
            style.labelStyle = DSTextFieldTokens.labelStyleSizeLargeShowPlaceholder
        }
        if (props.size == ODSTextFieldSize.SMALL && props.showPlaceholder) {
            style.labelStyle = DSTextFieldTokens.labelStyleSizeSmallShowPlaceholder
        }
        if (props.size == ODSTextFieldSize.SMALL && !props.showPlaceholder && !props.filled) {
            style.labelStyle = DSTextFieldTokens.labelStyleSizeSmall
        }
        if (props.size == ODSTextFieldSize.LARGE && !props.showPlaceholder && props.filled) {
            style.labelStyle = DSTextFieldTokens.labelStyleSizeLargeFilled
        }
        if (props.size == ODSTextFieldSize.SMALL && !props.showPlaceholder && props.filled) {
            style.labelStyle = DSTextFieldTokens.labelStyleSizeSmallFilled
        }
        if (props.size == ODSTextFieldSize.LARGE && !props.showPlaceholder && !props.filled && !props.readOnly) {
            style.labelStyle = DSTextFieldTokens.labelStyleSizeLarge
        }
        if (props.mode == ODSTextFieldMode.INFORMATIVE && !props.filled && props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.mode == ODSTextFieldMode.STANDARD && !props.filled && props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        style.requiredTextAlign = DSTextFieldTokens.requiredTextAlign
        if (!props.disabled) {
            style.requiredColor = scheme.basicTextRecessive
        }
        if (props.size == ODSTextFieldSize.LARGE && props.showPlaceholder) {
            style.requiredStyle = DSTextFieldTokens.requiredStyleSizeLargeShowPlaceholder
        }
        if (props.size == ODSTextFieldSize.SMALL && props.showPlaceholder) {
            style.requiredStyle = DSTextFieldTokens.requiredStyleSizeSmallShowPlaceholder
        }
        if (props.size == ODSTextFieldSize.SMALL && !props.showPlaceholder && !props.filled) {
            style.requiredStyle = DSTextFieldTokens.requiredStyleSizeSmall
        }
        if (props.size == ODSTextFieldSize.LARGE && !props.showPlaceholder && props.filled) {
            style.requiredStyle = DSTextFieldTokens.requiredStyleSizeLargeFilled
        }
        if (props.size == ODSTextFieldSize.SMALL && !props.showPlaceholder && props.filled) {
            style.requiredStyle = DSTextFieldTokens.requiredStyleSizeSmallFilled
        }
        if (props.size == ODSTextFieldSize.LARGE && !props.showPlaceholder && !props.filled && !props.readOnly) {
            style.requiredStyle = DSTextFieldTokens.requiredStyleSizeLarge
        }
        if (props.mode == ODSTextFieldMode.INFORMATIVE && !props.filled && props.disabled && !props.readOnly) {
            style.requiredColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.mode == ODSTextFieldMode.STANDARD && !props.filled && props.disabled && !props.readOnly) {
            style.requiredColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.inputGap = DSTextFieldTokens.inputGap
        style.inputVerticalAlignment = DSTextFieldTokens.inputVerticalAlignment
        style.inputHorizontalAlignment = DSTextFieldTokens.inputHorizontalAlignment
        style.inputHorizontalArrangement = DSTextFieldTokens.inputHorizontalArrangement
        if (!props.disabled) {
            style.leftIconColor = scheme.basicTextRecessive
        }
        if (props.size == ODSTextFieldSize.LARGE) {
            style.leftIconWidth = DSTextFieldTokens.leftIconWidthSizeLarge
            style.leftIconHeight = DSTextFieldTokens.leftIconHeightSizeLarge
        }
        if (props.size == ODSTextFieldSize.SMALL) {
            style.leftIconWidth = DSTextFieldTokens.leftIconWidthSizeSmall
            style.leftIconHeight = DSTextFieldTokens.leftIconHeightSizeSmall
        }
        if (props.mode == ODSTextFieldMode.INFORMATIVE && !props.filled && props.disabled && !props.readOnly) {
            style.leftIconColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.mode == ODSTextFieldMode.STANDARD && !props.filled && props.disabled && !props.readOnly) {
            style.leftIconColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.prefixTextAlign = DSTextFieldTokens.prefixTextAlign
        if (!props.disabled) {
            style.prefixColor = scheme.basicTextRecessive
        }
        if (props.size == ODSTextFieldSize.LARGE) {
            style.prefixStyle = DSTextFieldTokens.prefixStyleSizeLarge
        }
        if (props.size == ODSTextFieldSize.SMALL) {
            style.prefixStyle = DSTextFieldTokens.prefixStyleSizeSmall
        }
        if (props.mode == ODSTextFieldMode.INFORMATIVE && !props.filled && props.disabled && !props.readOnly) {
            style.prefixColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.mode == ODSTextFieldMode.STANDARD && !props.filled && props.disabled && !props.readOnly) {
            style.prefixColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.cursorGroupVerticalAlignment = DSTextFieldTokens.cursorGroupVerticalAlignment
        style.cursorGroupHorizontalAlignment = DSTextFieldTokens.cursorGroupHorizontalAlignment
        style.cursorGroupHorizontalArrangement = DSTextFieldTokens.cursorGroupHorizontalArrangement
        if (props.mode == ODSTextFieldMode.STANDARD && props.showPlaceholder && !props.filled && !props.disabled && !props.readOnly) {
            style.cursorGroupGap = DSTextFieldTokens.cursorGroupGapModeStandardShowPlaceholder
        }
        if (!props.showPlaceholder) {
            style.inputValueColor = scheme.basicText
            style.inputValueTextAlign = DSTextFieldTokens.inputValueTextAlign
            style.inputValueOverflow = DSTextFieldTokens.inputValueOverflow
            style.inputValueMaxLines = DSTextFieldTokens.inputValueMaxLines
        }
        if (props.size == ODSTextFieldSize.LARGE && !props.showPlaceholder) {
            style.inputValueStyle = DSTextFieldTokens.inputValueStyleSizeLarge
        }
        if (props.size == ODSTextFieldSize.SMALL && !props.showPlaceholder) {
            style.inputValueStyle = DSTextFieldTokens.inputValueStyleSizeSmall
        }
        if (props.showPlaceholder && props.filled) {
            style.inputValueColor = scheme.basicText
            style.inputValueTextAlign = DSTextFieldTokens.inputValueTextAlignShowPlaceholderFilled
            style.inputValueOverflow = DSTextFieldTokens.inputValueOverflowShowPlaceholderFilled
            style.inputValueMaxLines = DSTextFieldTokens.inputValueMaxLinesShowPlaceholderFilled
        }
        if (props.size == ODSTextFieldSize.LARGE && props.showPlaceholder && props.filled) {
            style.inputValueStyle = DSTextFieldTokens.inputValueStyleSizeLargeShowPlaceholderFilled
        }
        if (props.size == ODSTextFieldSize.SMALL && props.showPlaceholder && props.filled) {
            style.inputValueStyle = DSTextFieldTokens.inputValueStyleSizeSmallShowPlaceholderFilled
        }
        if (props.showPlaceholder && !props.filled && !props.readOnly) {
            style.placeholderTextAlign = DSTextFieldTokens.placeholderTextAlignShowPlaceholder
            style.placeholderOverflow = DSTextFieldTokens.placeholderOverflowShowPlaceholder
            style.placeholderMaxLines = DSTextFieldTokens.placeholderMaxLinesShowPlaceholder
        }
        if (props.size == ODSTextFieldSize.LARGE && props.showPlaceholder && !props.filled && !props.readOnly) {
            style.placeholderStyle = DSTextFieldTokens.placeholderStyleSizeLargeShowPlaceholder
        }
        if (props.size == ODSTextFieldSize.SMALL && props.showPlaceholder && !props.filled && !props.readOnly) {
            style.placeholderStyle = DSTextFieldTokens.placeholderStyleSizeSmallShowPlaceholder
        }
        if (props.showPlaceholder && !props.filled && !props.disabled && !props.readOnly) {
            style.placeholderColor = scheme.basicTextRecessive
        }
        if (props.mode == ODSTextFieldMode.INFORMATIVE && props.showPlaceholder && !props.filled && props.disabled && !props.readOnly) {
            style.placeholderColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.mode == ODSTextFieldMode.STANDARD && props.showPlaceholder && !props.filled && props.disabled && !props.readOnly) {
            style.placeholderColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.suffixTextAlign = DSTextFieldTokens.suffixTextAlign
        if (!props.disabled) {
            style.suffixColor = scheme.basicTextRecessive
        }
        if (props.size == ODSTextFieldSize.LARGE) {
            style.suffixStyle = DSTextFieldTokens.suffixStyleSizeLarge
        }
        if (props.size == ODSTextFieldSize.SMALL) {
            style.suffixStyle = DSTextFieldTokens.suffixStyleSizeSmall
        }
        if (props.mode == ODSTextFieldMode.INFORMATIVE && !props.filled && props.disabled && !props.readOnly) {
            style.suffixColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.mode == ODSTextFieldMode.STANDARD && !props.filled && props.disabled && !props.readOnly) {
            style.suffixColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.iconContainerPadding = DSTextFieldTokens.iconContainerPadding
        style.iconContainerWidth = DSTextFieldTokens.iconContainerWidth
        style.iconContainerHeight = DSTextFieldTokens.iconContainerHeight
        style.iconContainerVerticalAlignment = DSTextFieldTokens.iconContainerVerticalAlignment
        style.iconContainerHorizontalAlignment = DSTextFieldTokens.iconContainerHorizontalAlignment
        style.iconContainerHorizontalArrangement =
            DSTextFieldTokens.iconContainerHorizontalArrangement
        if (!props.disabled) {
            style.rightIconColor = scheme.basicTextRecessive
        }
        if (props.size == ODSTextFieldSize.LARGE) {
            style.rightIconWidth = DSTextFieldTokens.rightIconWidthSizeLarge
            style.rightIconHeight = DSTextFieldTokens.rightIconHeightSizeLarge
        }
        if (props.size == ODSTextFieldSize.SMALL) {
            style.rightIconWidth = DSTextFieldTokens.rightIconWidthSizeSmall
            style.rightIconHeight = DSTextFieldTokens.rightIconHeightSizeSmall
        }
        if (props.mode == ODSTextFieldMode.INFORMATIVE && !props.filled && props.disabled && !props.readOnly) {
            style.rightIconColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.mode == ODSTextFieldMode.STANDARD && !props.filled && props.disabled && !props.readOnly) {
            style.rightIconColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.supportTextPadding = DSTextFieldTokens.supportTextPadding
        style.supportTextVerticalAlignment = DSTextFieldTokens.supportTextVerticalAlignment
        style.supportTextHorizontalAlignment = DSTextFieldTokens.supportTextHorizontalAlignment
        style.supportTextHorizontalArrangement = DSTextFieldTokens.supportTextHorizontalArrangement
        style.counterStyle = DSTextFieldTokens.counterStyle
        style.counterColor = scheme.basicTextRecessive
        style.counterTextAlign = DSTextFieldTokens.counterTextAlign
        style.counterWidth = DSTextFieldTokens.counterWidth

        // Custom addition
        style.inputCursorColor = scheme.basicAccent
        if (!props.disabled) {
            style.placeholderColor = scheme.basicTextRecessive
        } else {
            style.placeholderColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.placeholderTextAlign = DSTextFieldTokens.placeholderTextAlignShowPlaceholder
        style.placeholderStyle = when (props.size) {
            ODSTextFieldSize.LARGE -> DSTextFieldTokens.placeholderStyleSizeLargeShowPlaceholder
            ODSTextFieldSize.SMALL -> DSTextFieldTokens.placeholderStyleSizeSmallShowPlaceholder
        }
        style.contentContainerAlignment = DSTextFieldTokens.contentContainerAlignment
        return style
    }
}
