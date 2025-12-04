package com.telekom.odsystem.atoms.floatingactionbutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSFloatingActionButtonTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("All")
class ODSFloatingActionButtonStyle {
    var borderRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var backgroundColor: List<ODSColorModel>? = null
    var maxHeight: Dp? = null
    var maxWidth: Dp? = null
    var contentGap: Dp? = null
    var contentPadding: ODSPadding? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var buttonBgBackgroundColor: List<ODSColorModel>? = null
    var buttonBgBorderRadius: ODSCorners? = null
    var buttonBgBoxShadow: ODSEffect? = null
    var buttonBgWidth: Dp? = null
    var buttonBgHeight: Dp? = null
    var buttonBgVerticalAlignment: Alignment.Vertical? = null
    var buttonBgHorizontalAlignment: Alignment.Horizontal? = null
    var buttonBgHorizontalArrangement: Arrangement.Horizontal? = null
    var buttonBgBorder: Dp? = null
    var buttonBgBorderColor: List<ODSColorModel>? = null
    var leftIconVerticalAlignment: Alignment.Vertical? = null
    var leftIconHorizontalAlignment: Alignment.Horizontal? = null
    var leftIconHorizontalArrangement: Arrangement.Horizontal? = null
    var rightIconVerticalAlignment: Alignment.Vertical? = null
    var rightIconHorizontalAlignment: Alignment.Horizontal? = null
    var rightIconHorizontalArrangement: Arrangement.Horizontal? = null
    var buttonIconColor: HexColor? = null
    var buttonIconWidth: Dp? = null
    var buttonIconHeight: Dp? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var scaleFactor: Float? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSFloatingActionButtonProps,
        state: ODSActions
    ): ODSFloatingActionButtonStyle {
        var style = ODSFloatingActionButtonStyle()
        style.borderRadius = DSFloatingActionButtonTokens.borderRadius
        style.verticalAlignment = DSFloatingActionButtonTokens.verticalAlignment
        style.horizontalAlignment = DSFloatingActionButtonTokens.horizontalAlignment
        style.horizontalArrangement = DSFloatingActionButtonTokens.horizontalArrangement
        if (props.size == ODSFloatingActionButtonSize.LARGE) {
            style.minHeight = DSFloatingActionButtonTokens.minHeightSizeLarge
        }
        if (props.size == ODSFloatingActionButtonSize.SMALL) {
            style.minHeight = DSFloatingActionButtonTokens.minHeightSizeSmall
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED) {
            style.minWidth = DSFloatingActionButtonTokens.minWidthTypeExtended
        }
        if (props.type == ODSFloatingActionButtonType.STANDARD && props.size == ODSFloatingActionButtonSize.LARGE) {
            style.minWidth = DSFloatingActionButtonTokens.minWidthTypeStandardSizeLarge
            style.maxHeight = DSFloatingActionButtonTokens.maxHeightTypeStandardSizeLarge
            style.maxWidth = DSFloatingActionButtonTokens.maxWidthTypeStandardSizeLarge
        }
        if (props.type == ODSFloatingActionButtonType.STANDARD && props.size == ODSFloatingActionButtonSize.SMALL) {
            style.minWidth = DSFloatingActionButtonTokens.minWidthTypeStandardSizeSmall
            style.maxHeight = DSFloatingActionButtonTokens.maxHeightTypeStandardSizeSmall
            style.maxWidth = DSFloatingActionButtonTokens.maxWidthTypeStandardSizeSmall
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.OUTLINE && props.size == ODSFloatingActionButtonSize.LARGE && !props.disabled) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        style.contentVerticalAlignment = DSFloatingActionButtonTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSFloatingActionButtonTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement =
            DSFloatingActionButtonTokens.contentHorizontalArrangement
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.size == ODSFloatingActionButtonSize.LARGE) {
            style.contentGap = DSFloatingActionButtonTokens.contentGapTypeExtendedSizeLarge
            style.contentPadding = DSFloatingActionButtonTokens.contentPaddingTypeExtendedSizeLarge
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.size == ODSFloatingActionButtonSize.SMALL) {
            style.contentGap = DSFloatingActionButtonTokens.contentGapTypeExtendedSizeSmall
            style.contentPadding = DSFloatingActionButtonTokens.contentPaddingTypeExtendedSizeSmall
        }
        style.buttonBgBorderRadius = DSFloatingActionButtonTokens.buttonBgBorderRadius
        style.buttonBgBoxShadow = scheme.elevationFabStandard
        style.buttonBgVerticalAlignment = DSFloatingActionButtonTokens.buttonBgVerticalAlignment
        style.buttonBgHorizontalAlignment = DSFloatingActionButtonTokens.buttonBgHorizontalAlignment
        style.buttonBgHorizontalArrangement =
            DSFloatingActionButtonTokens.buttonBgHorizontalArrangement
        if (props.size == ODSFloatingActionButtonSize.LARGE) {
            style.buttonBgHeight = DSFloatingActionButtonTokens.buttonBgHeightSizeLarge
        }
        if (props.size == ODSFloatingActionButtonSize.SMALL) {
            style.buttonBgHeight = DSFloatingActionButtonTokens.buttonBgHeightSizeSmall
        }
        if (props.variant == ODSFloatingActionButtonVariant.OUTLINE) {
            style.buttonBgBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackground))
            style.buttonBgBorder = DSFloatingActionButtonTokens.buttonBgBorderVariantOutline
        }
        if (props.variant == ODSFloatingActionButtonVariant.PRIMARY && !props.disabled) {
            style.buttonBgBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        }
        if (props.variant == ODSFloatingActionButtonVariant.SECONDARY && !props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        }
        if (props.variant == ODSFloatingActionButtonVariant.PRIMARY && props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentDisabled))
        }
        if (props.variant == ODSFloatingActionButtonVariant.SECONDARY && props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentSecondaryDisabled))
        }
        if (state == ODSActions.PRESSED && !props.disabled) {
            style.buttonBgBoxShadow = scheme.elevationFabPressed
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.size == ODSFloatingActionButtonSize.LARGE) {
            style.buttonBgWidth = DSFloatingActionButtonTokens.buttonBgWidthTypeExtendedSizeLarge
        }
        if (props.type == ODSFloatingActionButtonType.STANDARD && props.size == ODSFloatingActionButtonSize.LARGE) {
            style.buttonBgWidth = DSFloatingActionButtonTokens.buttonBgWidthTypeStandardSizeLarge
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.size == ODSFloatingActionButtonSize.SMALL) {
            style.buttonBgWidth = DSFloatingActionButtonTokens.buttonBgWidthTypeExtendedSizeSmall
        }
        if (props.type == ODSFloatingActionButtonType.STANDARD && props.size == ODSFloatingActionButtonSize.SMALL) {
            style.buttonBgWidth = DSFloatingActionButtonTokens.buttonBgWidthTypeStandardSizeSmall
        }
        if (props.size == ODSFloatingActionButtonSize.LARGE && state == ODSActions.PRESSED) {
            style.buttonBgHeight = DSFloatingActionButtonTokens.buttonBgHeightSizeLargeStatePressed
        }
        if (props.size == ODSFloatingActionButtonSize.SMALL && state == ODSActions.PRESSED) {
            style.buttonBgHeight = DSFloatingActionButtonTokens.buttonBgHeightSizeSmallStatePressed
        }
        if (props.variant == ODSFloatingActionButtonVariant.OUTLINE && !props.disabled) {
            style.buttonBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.variant == ODSFloatingActionButtonVariant.OUTLINE && props.disabled) {
            style.buttonBgBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.variant == ODSFloatingActionButtonVariant.SECONDARY && state == ODSActions.HOVERED && !props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentSecondaryHover))
        }
        if (props.variant == ODSFloatingActionButtonVariant.PRIMARY && state == ODSActions.HOVERED && !props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentHover))
        }
        if (props.variant == ODSFloatingActionButtonVariant.OUTLINE && state == ODSActions.HOVERED && !props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
            style.buttonBgBorder =
                DSFloatingActionButtonTokens.buttonBgBorderVariantOutlineStateHovered
            style.buttonBgBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (props.variant == ODSFloatingActionButtonVariant.PRIMARY && state == ODSActions.PRESSED && !props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentPressed))
        }
        if (props.variant == ODSFloatingActionButtonVariant.SECONDARY && state == ODSActions.PRESSED && !props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentSecondaryPressed))
        }
        if (props.variant == ODSFloatingActionButtonVariant.OUTLINE && state == ODSActions.PRESSED && !props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
            style.buttonBgBorder =
                DSFloatingActionButtonTokens.buttonBgBorderVariantOutlineStatePressed
            style.buttonBgBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedStrokePressed))
        }
        if (props.type == ODSFloatingActionButtonType.STANDARD && state == ODSActions.HOVERED && !props.disabled) {
            style.buttonBgBoxShadow = scheme.elevationFabHovered
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.size == ODSFloatingActionButtonSize.LARGE && state == ODSActions.PRESSED) {
            style.buttonBgWidth =
                DSFloatingActionButtonTokens.buttonBgWidthTypeExtendedSizeLargeStatePressed
        }
        if (props.type == ODSFloatingActionButtonType.STANDARD && props.size == ODSFloatingActionButtonSize.LARGE && state == ODSActions.PRESSED) {
            style.buttonBgWidth =
                DSFloatingActionButtonTokens.buttonBgWidthTypeStandardSizeLargeStatePressed
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.size == ODSFloatingActionButtonSize.SMALL && state == ODSActions.PRESSED) {
            style.buttonBgWidth =
                DSFloatingActionButtonTokens.buttonBgWidthTypeExtendedSizeSmallStatePressed
        }
        if (props.type == ODSFloatingActionButtonType.STANDARD && props.size == ODSFloatingActionButtonSize.SMALL && state == ODSActions.PRESSED) {
            style.buttonBgWidth =
                DSFloatingActionButtonTokens.buttonBgWidthTypeStandardSizeSmallStatePressed
        }
//        if (props.size == ODSFloatingActionButtonSize.LARGE && state == ODSActions.HOVERED && !props.disabled) {
//            style.buttonBgHeight = DSFloatingActionButtonTokens.buttonBgHeightSizeLargeStateHovered
//        }
//        if (props.size == ODSFloatingActionButtonSize.SMALL && state == ODSActions.HOVERED && !props.disabled) {
//            style.buttonBgHeight = DSFloatingActionButtonTokens.buttonBgHeightSizeSmallStateHovered
//        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.OUTLINE && state == ODSActions.HOVERED && !props.disabled) {
            style.buttonBgBoxShadow = scheme.elevationFabHovered
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.SECONDARY && state == ODSActions.HOVERED && !props.disabled) {
            style.buttonBgBoxShadow = scheme.elevationFabHovered
        }
//        if (props.type == ODSFloatingActionButtonType.STANDARD && props.size == ODSFloatingActionButtonSize.LARGE && state == ODSActions.HOVERED && !props.disabled) {
//            style.buttonBgWidth =
//                DSFloatingActionButtonTokens.buttonBgWidthTypeStandardSizeLargeStateHovered
//        }
//        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.size == ODSFloatingActionButtonSize.LARGE && state == ODSActions.HOVERED && !props.disabled) {
//            style.buttonBgWidth =
//                DSFloatingActionButtonTokens.buttonBgWidthTypeExtendedSizeLargeStateHovered
//        }
//        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.size == ODSFloatingActionButtonSize.SMALL && state == ODSActions.HOVERED && !props.disabled) {
//            style.buttonBgWidth =
//                DSFloatingActionButtonTokens.buttonBgWidthTypeExtendedSizeSmallStateHovered
//        }
//        if (props.type == ODSFloatingActionButtonType.STANDARD && props.size == ODSFloatingActionButtonSize.SMALL && state == ODSActions.HOVERED && !props.disabled) {
//            style.buttonBgWidth =
//                DSFloatingActionButtonTokens.buttonBgWidthTypeStandardSizeSmallStateHovered
//        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.PRIMARY && props.size == ODSFloatingActionButtonSize.SMALL && state == ODSActions.HOVERED && !props.disabled) {
            style.buttonBgBoxShadow = scheme.elevationFabHovered
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED) {
            style.leftIconVerticalAlignment =
                DSFloatingActionButtonTokens.leftIconVerticalAlignmentTypeExtended
            style.leftIconHorizontalAlignment =
                DSFloatingActionButtonTokens.leftIconHorizontalAlignmentTypeExtended
            style.leftIconHorizontalArrangement =
                DSFloatingActionButtonTokens.leftIconHorizontalArrangementTypeExtended
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED) {
            style.rightIconVerticalAlignment =
                DSFloatingActionButtonTokens.rightIconVerticalAlignmentTypeExtended
            style.rightIconHorizontalAlignment =
                DSFloatingActionButtonTokens.rightIconHorizontalAlignmentTypeExtended
            style.rightIconHorizontalArrangement =
                DSFloatingActionButtonTokens.rightIconHorizontalArrangementTypeExtended
        }
        if (props.size == ODSFloatingActionButtonSize.LARGE) {
            style.buttonIconWidth = DSFloatingActionButtonTokens.buttonIconWidthSizeLarge
            style.buttonIconHeight = DSFloatingActionButtonTokens.buttonIconHeightSizeLarge
        }
        if (props.size == ODSFloatingActionButtonSize.SMALL) {
            style.buttonIconWidth = DSFloatingActionButtonTokens.buttonIconWidthSizeSmall
            style.buttonIconHeight = DSFloatingActionButtonTokens.buttonIconHeightSizeSmall
        }
        if (props.variant == ODSFloatingActionButtonVariant.PRIMARY && !props.disabled) {
            style.buttonIconColor = scheme.basicTextOnAccent
        }
        if (props.variant == ODSFloatingActionButtonVariant.OUTLINE && !props.disabled) {
            style.buttonIconColor = scheme.basicText
        }
        if (props.variant == ODSFloatingActionButtonVariant.PRIMARY && props.disabled) {
            style.buttonIconColor = scheme.interactionStatesDisabledTextOnAccentDisabled
        }
        if (props.variant == ODSFloatingActionButtonVariant.SECONDARY && props.disabled) {
            style.buttonIconColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        if (props.variant == ODSFloatingActionButtonVariant.OUTLINE && props.disabled) {
            style.buttonIconColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.SECONDARY && !props.disabled) {
            style.buttonIconColor = scheme.basicTextOnAccentSecondary
        }
        if (props.variant == ODSFloatingActionButtonVariant.SECONDARY && state == ODSActions.HOVERED && !props.disabled) {
            style.buttonIconColor = scheme.interactionStatesHoverTextOnAccentSecondaryHover
        }
        if (props.variant == ODSFloatingActionButtonVariant.PRIMARY && state == ODSActions.HOVERED && !props.disabled) {
            style.buttonIconColor = scheme.interactionStatesHoverTextOnAccentHover
        }
        if (props.variant == ODSFloatingActionButtonVariant.PRIMARY && state == ODSActions.PRESSED && !props.disabled) {
            style.buttonIconColor = scheme.interactionStatesPressedTextOnAccentPressed
        }
        if (props.type == ODSFloatingActionButtonType.STANDARD && props.variant == ODSFloatingActionButtonVariant.SECONDARY && props.size == ODSFloatingActionButtonSize.SMALL && !props.disabled) {
            style.buttonIconColor = scheme.basicTextOnAccentSecondary
        }
        if (props.type == ODSFloatingActionButtonType.STANDARD && props.variant == ODSFloatingActionButtonVariant.SECONDARY && props.size == ODSFloatingActionButtonSize.LARGE && !props.disabled) {
            style.buttonIconColor = scheme.basicTextOnAccentSecondary
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.SECONDARY && state == ODSActions.PRESSED && !props.disabled) {
            style.buttonIconColor = scheme.interactionStatesPressedTextOnAccentSecondaryPressed
        }
        if (props.type == ODSFloatingActionButtonType.STANDARD && props.variant == ODSFloatingActionButtonVariant.SECONDARY && props.size == ODSFloatingActionButtonSize.LARGE && state == ODSActions.PRESSED && !props.disabled) {
            style.buttonIconColor = scheme.basicTextOnAccentSecondary
        }
        if (props.type == ODSFloatingActionButtonType.STANDARD && props.variant == ODSFloatingActionButtonVariant.SECONDARY && props.size == ODSFloatingActionButtonSize.SMALL && state == ODSActions.PRESSED && !props.disabled) {
            style.buttonIconColor = scheme.interactionStatesPressedTextOnAccentSecondaryPressed
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED) {
            style.labelTextAlign = DSFloatingActionButtonTokens.labelTextAlignTypeExtended
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.size == ODSFloatingActionButtonSize.LARGE) {
            style.labelTextStyle = DSFloatingActionButtonTokens.labelTextStyleTypeExtendedSizeLarge
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.size == ODSFloatingActionButtonSize.SMALL) {
            style.labelTextStyle = DSFloatingActionButtonTokens.labelTextStyleTypeExtendedSizeSmall
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.PRIMARY && !props.disabled) {
            style.labelColor = scheme.basicTextOnAccent
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.SECONDARY && !props.disabled) {
            style.labelColor = scheme.basicTextOnAccentSecondary
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.OUTLINE && !props.disabled) {
            style.labelColor = scheme.basicText
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.PRIMARY && props.disabled) {
            style.labelColor = scheme.interactionStatesDisabledTextOnAccentDisabled
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.SECONDARY && props.disabled) {
            style.labelColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.OUTLINE && props.disabled) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.PRIMARY && state == ODSActions.HOVERED && !props.disabled) {
            style.labelColor = scheme.interactionStatesHoverTextOnAccentHover
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.SECONDARY && state == ODSActions.HOVERED && !props.disabled) {
            style.labelColor = scheme.interactionStatesHoverTextOnAccentSecondaryHover
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.PRIMARY && state == ODSActions.PRESSED && !props.disabled) {
            style.labelColor = scheme.interactionStatesPressedTextOnAccentPressed
        }
        if (props.type == ODSFloatingActionButtonType.EXTENDED && props.variant == ODSFloatingActionButtonVariant.SECONDARY && state == ODSActions.PRESSED && !props.disabled) {
            style.labelColor = scheme.interactionStatesPressedTextOnAccentSecondaryPressed
        }
        style.scaleFactor = DSFloatingActionButtonTokens.scaleFactor
        return style
    }
}
