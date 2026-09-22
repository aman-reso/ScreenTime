package com.telekom.odsystem.atoms.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSButtonTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("ALL")
class ODSButtonStyle {
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var contentAlignment: Alignment? = null
    var maxHeight: Dp? = null
    var maxWidth: Dp? = null
    var buttonBgBackgroundColor: List<ODSColorModel>? = null
    var buttonBgBorderRadius: ODSCorners? = null
    var buttonBgHeight: Dp? = null
    var buttonBgContentAlignment: Alignment? = null
    var buttonBgBorder: Dp? = null
    var buttonBgBorderColor: List<ODSColorModel>? = null
    var buttonBgWidth: Dp? = null
    var contentGap: Dp? = null
    var contentPadding: ODSPadding? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var leftIconVerticalAlignment: Alignment.Vertical? = null
    var leftIconHorizontalAlignment: Alignment.Horizontal? = null
    var leftIconHorizontalArrangement: Arrangement.Horizontal? = null
    var buttonIconColor: HexColor? = null
    var buttonIconWidth: Dp? = null
    var buttonIconHeight: Dp? = null
    var buttonLabelTextStyle: ODSTextStyle? = null
    var buttonLabelColor: HexColor? = null
    var buttonLabelTextAlign: TextAlign? = null
    var rightIconVerticalAlignment: Alignment.Vertical? = null
    var rightIconHorizontalAlignment: Alignment.Horizontal? = null
    var rightIconHorizontalArrangement: Arrangement.Horizontal? = null
    var buttonIcon2Color: HexColor? = null
    var buttonIcon2Width: Dp? = null
    var buttonIcon2Height: Dp? = null
    var buttonIcon3Color: HexColor? = null
    var buttonIcon3Width: Dp? = null
    var buttonIcon3Height: Dp? = null
    var buttonLabelTextOverflow: TextOverflow? = null // Not exported from the plugin
    var scaleFactor: Float? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSButtonProps,
        state: ODSActions
    ): ODSButtonStyle {
        val style = ODSButtonStyle()
        style.borderRadius = DSButtonTokens.borderRadius
        style.verticalAlignment = DSButtonTokens.verticalAlignment
        style.horizontalAlignment = DSButtonTokens.horizontalAlignment
        style.horizontalArrangement = DSButtonTokens.horizontalArrangement
        style.contentAlignment = DSButtonTokens.contentAlignment
        if (props.size == ODSButtonSize.LARGE) {
            style.minHeight = DSButtonTokens.minHeightSizeSmall
        }
        if (props.size == ODSButtonSize.SMALL) {
            style.minHeight = DSButtonTokens.minHeightSizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.size == ODSButtonSize.LARGE) {
            style.padding = DSButtonTokens.paddingButtonTypeStandardSizeSmall
            style.minWidth = DSButtonTokens.minWidthButtonTypeStandardSizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.size == ODSButtonSize.LARGE) {
            style.padding = DSButtonTokens.paddingButtonTypeIconOnlySizeSmall
            style.minWidth = DSButtonTokens.minWidthButtonTypeIconOnlySizeSmall
            style.maxHeight = DSButtonTokens.maxHeightButtonTypeIconOnlySizeSmall
            style.maxWidth = DSButtonTokens.maxWidthButtonTypeIconOnlySizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.size == ODSButtonSize.SMALL) {
            style.padding = DSButtonTokens.paddingButtonTypeStandardSizeSmall
            style.minWidth = DSButtonTokens.minWidthButtonTypeStandardSizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.size == ODSButtonSize.SMALL) {
            style.padding = DSButtonTokens.paddingButtonTypeIconOnlySizeSmall
            style.minWidth = DSButtonTokens.minWidthButtonTypeIconOnlySizeSmall
            style.maxHeight = DSButtonTokens.maxHeightButtonTypeIconOnlySizeSmall
            style.maxWidth = DSButtonTokens.maxWidthButtonTypeIconOnlySizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.GHOST && props.size == ODSButtonSize.LARGE) {
            style.padding = DSButtonTokens.paddingButtonTypeStandardVariantGhostSizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.GHOST && props.size == ODSButtonSize.SMALL) {
            style.padding = DSButtonTokens.paddingButtonTypeStandardVariantGhostSizeSmall
        }
        style.buttonBgBorderRadius = DSButtonTokens.buttonBgBorderRadius
        if (props.variant == ODSButtonVariant.OUTLINE) {
            style.buttonBgBorder = DSButtonTokens.buttonBgBorderVariantOutline
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD) {
            style.buttonBgContentAlignment =
                DSButtonTokens.buttonBgContentAlignmentButtonTypeStandard
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY) {
            style.buttonBgContentAlignment =
                DSButtonTokens.buttonBgContentAlignmentButtonTypeIconOnly
        }
        if (props.variant == ODSButtonVariant.PRIMARY && !props.disabled) {
            style.buttonBgBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        }
        if (props.variant == ODSButtonVariant.SECONDARY && !props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        }
        if (props.variant == ODSButtonVariant.PRIMARY && props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentDisabled))
        }
        if (props.variant == ODSButtonVariant.SECONDARY && props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentSecondaryDisabled))
        }
        if (props.variant == ODSButtonVariant.GHOST && props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        }
        if (props.variant == ODSButtonVariant.OUTLINE && props.disabled) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.buttonBgBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.variant == ODSButtonVariant.GHOST && props.size == ODSButtonSize.LARGE) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantGhostSizeLarge
        }
        if (props.variant == ODSButtonVariant.GHOST && props.size == ODSButtonSize.SMALL) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantGhostSizeSmall
        }
        if (props.variant == ODSButtonVariant.OUTLINE && !props.disabled) {
            style.buttonBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.variant == ODSButtonVariant.SECONDARY && !props.disabled && state == ODSActions.HOVERED) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentSecondaryHover))
        }
        if (props.variant == ODSButtonVariant.PRIMARY && !props.disabled && state == ODSActions.HOVERED) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentHover))
        }
        if (props.variant == ODSButtonVariant.GHOST && !props.disabled && state == ODSActions.HOVERED) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (props.variant == ODSButtonVariant.OUTLINE && !props.disabled && state == ODSActions.HOVERED) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
            style.buttonBgBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (props.variant == ODSButtonVariant.PRIMARY && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentPressed))
        }
        if (props.variant == ODSButtonVariant.SECONDARY && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentSecondaryPressed))
        }
        if (props.variant == ODSButtonVariant.GHOST && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
        }
        if (props.variant == ODSButtonVariant.OUTLINE && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
            style.buttonBgBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedStrokePressed))
        }
        if (props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.LARGE && props.disabled) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantOutlineSizeLargeDisabled
        }
        if (props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.LARGE && !props.disabled) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantOutlineSizeLarge
        }
        if (props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.LARGE && props.disabled) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantSecondarySizeLargeDisabled
        }
        if (props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.LARGE && !props.disabled) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantSecondarySizeLarge
        }
        if (props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.LARGE && props.disabled) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantPrimarySizeLargeDisabled
        }
        if (props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.LARGE && !props.disabled) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantPrimarySizeLarge
        }
        if (props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.SMALL && props.disabled) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantOutlineSizeSmallDisabled
        }
        if (props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.SMALL && !props.disabled) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantOutlineSizeSmall
        }
        if (props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.SMALL && props.disabled) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantSecondarySizeSmallDisabled
        }
        if (props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.SMALL && !props.disabled) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantSecondarySizeSmall
        }
        if (props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.SMALL && props.disabled) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantPrimarySizeSmallDisabled
        }
        if (props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.SMALL && !props.disabled) {
            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantPrimarySizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.GHOST && props.size == ODSButtonSize.LARGE) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantGhostSizeLarge
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.GHOST && props.size == ODSButtonSize.SMALL) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantGhostSizeSmall
        }
//        if (props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.PRESSED) {
//            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantOutlineSizeLargeStatePressed
//        }
//        if (props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.PRESSED) {
//            style.buttonBgHeight =
//                DSButtonTokens.buttonBgHeightVariantSecondarySizeLargeStatePressed
//        }
//        if (props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.PRESSED) {
//            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantPrimarySizeLargeStatePressed
//        }
//        if (props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.HOVERED) {
//            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantOutlineSizeLargeStateHovered
//        }
//        if (props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.HOVERED) {
//            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantPrimarySizeLargeStateHovered
//        }
//        if (props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.HOVERED) {
//            style.buttonBgHeight =
//                DSButtonTokens.buttonBgHeightVariantSecondarySizeLargeStateHovered
//        }
//        if (props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.PRESSED) {
//            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantOutlineSizeSmallStatePressed
//        }
//        if (props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.PRESSED) {
//            style.buttonBgHeight =
//                DSButtonTokens.buttonBgHeightVariantSecondarySizeSmallStatePressed
//        }
//        if (props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.PRESSED) {
//            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantPrimarySizeSmallStatePressed
//        }
//        if (props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.HOVERED) {
//            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantOutlineSizeSmallStateHovered
//        }
//        if (props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.HOVERED) {
//            style.buttonBgHeight =
//                DSButtonTokens.buttonBgHeightVariantSecondarySizeSmallStateHovered
//        }
//        if (props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.HOVERED) {
//            style.buttonBgHeight = DSButtonTokens.buttonBgHeightVariantPrimarySizeSmallStateHovered
//        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.LARGE && props.disabled) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantOutlineSizeLargeDisabled
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.LARGE && !props.disabled) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantOutlineSizeLarge
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.LARGE && props.disabled) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantSecondarySizeLargeDisabled
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.LARGE && !props.disabled) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantSecondarySizeLarge
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.LARGE && props.disabled) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantPrimarySizeLargeDisabled
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.LARGE && !props.disabled) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantPrimarySizeLarge
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.SMALL && props.disabled) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantOutlineSizeSmallDisabled
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.SMALL && !props.disabled) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantOutlineSizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.SMALL && props.disabled) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantSecondarySizeSmallDisabled
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.SMALL && !props.disabled) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantSecondarySizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.SMALL && props.disabled) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantPrimarySizeSmallDisabled
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.SMALL && !props.disabled) {
            style.buttonBgWidth =
                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantPrimarySizeSmall
        }
//        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.PRESSED) {
//            style.buttonBgWidth =
//                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantOutlineSizeLargeStatePressed
//        }
//        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.PRESSED) {
//            style.buttonBgWidth =
//                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantSecondarySizeLargeStatePressed
//        }
//        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.PRESSED) {
//            style.buttonBgWidth =
//                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantPrimarySizeLargeStatePressed
//        }
//        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.HOVERED) {
//            style.buttonBgWidth =
//                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantOutlineSizeLargeStateHovered
//        }
//        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.HOVERED) {
//            style.buttonBgWidth =
//                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantPrimarySizeLargeStateHovered
//        }
//        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.HOVERED) {
//            style.buttonBgWidth =
//                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantSecondarySizeLargeStateHovered
//        }
//        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.PRESSED) {
//            style.buttonBgWidth =
//                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantOutlineSizeSmallStatePressed
//        }
//        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.PRESSED) {
//            style.buttonBgWidth =
//                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantSecondarySizeSmallStatePressed
//        }
//        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.PRESSED) {
//            style.buttonBgWidth =
//                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantPrimarySizeSmallStatePressed
//        }
//        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.OUTLINE && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.HOVERED) {
//            style.buttonBgWidth =
//                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantOutlineSizeSmallStateHovered
//        }
//        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.HOVERED) {
//            style.buttonBgWidth =
//                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantSecondarySizeSmallStateHovered
//        }
//        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.PRIMARY && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.HOVERED) {
//            style.buttonBgWidth =
//                DSButtonTokens.buttonBgWidthButtonTypeIconOnlyVariantPrimarySizeSmallStateHovered
//        }
        style.contentVerticalAlignment = DSButtonTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSButtonTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSButtonTokens.contentHorizontalArrangement
        if (props.size == ODSButtonSize.LARGE) {
            style.contentGap = DSButtonTokens.contentGapSizeLarge
        }
        if (props.size == ODSButtonSize.SMALL) {
            style.contentGap = DSButtonTokens.contentGapSizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY) {
            style.contentPadding = DSButtonTokens.contentPaddingButtonTypeIconOnly
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.size == ODSButtonSize.LARGE) {
            style.contentPadding = DSButtonTokens.contentPaddingButtonTypeStandardSizeLarge
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.size == ODSButtonSize.SMALL) {
            style.contentPadding = DSButtonTokens.contentPaddingButtonTypeStandardSizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD) {
            style.leftIconVerticalAlignment =
                DSButtonTokens.leftIconVerticalAlignmentButtonTypeStandard
            style.leftIconHorizontalAlignment =
                DSButtonTokens.leftIconHorizontalAlignmentButtonTypeStandard
            style.leftIconHorizontalArrangement =
                DSButtonTokens.leftIconHorizontalArrangementButtonTypeStandard
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.size == ODSButtonSize.LARGE) {
            style.buttonIconWidth = DSButtonTokens.buttonIconWidthButtonTypeStandardSizeLarge
            style.buttonIconHeight = DSButtonTokens.buttonIconHeightButtonTypeStandardSizeLarge
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.size == ODSButtonSize.SMALL) {
            style.buttonIconWidth = DSButtonTokens.buttonIconWidthButtonTypeStandardSizeSmall
            style.buttonIconHeight = DSButtonTokens.buttonIconHeightButtonTypeStandardSizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.PRIMARY && !props.disabled) {
            style.buttonIconColor = scheme.basicTextOnAccent
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.SECONDARY && !props.disabled) {
            style.buttonIconColor = scheme.basicTextOnAccentSecondary
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.GHOST && !props.disabled) {
            style.buttonIconColor = scheme.basicText
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.OUTLINE && !props.disabled) {
            style.buttonIconColor = scheme.basicText
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.PRIMARY && props.disabled) {
            style.buttonIconColor = scheme.interactionStatesDisabledTextOnAccentDisabled
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.SECONDARY && props.disabled) {
            style.buttonIconColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.GHOST && props.disabled) {
            style.buttonIconColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.OUTLINE && props.disabled) {
            style.buttonIconColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.PRIMARY && !props.disabled && state == ODSActions.HOVERED) {
            style.buttonIconColor = scheme.interactionStatesHoverTextOnAccentHover
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.SECONDARY && !props.disabled && state == ODSActions.HOVERED) {
            style.buttonIconColor = scheme.interactionStatesHoverTextOnAccentSecondaryHover
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.PRIMARY && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonIconColor = scheme.interactionStatesPressedTextOnAccentPressed
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.SECONDARY && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonIconColor = scheme.interactionStatesPressedTextOnAccentSecondaryPressed
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD) {
            style.buttonLabelTextAlign = DSButtonTokens.buttonLabelTextAlignButtonTypeStandard
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.size == ODSButtonSize.LARGE) {
            style.buttonLabelTextStyle =
                DSButtonTokens.buttonLabelTextStyleButtonTypeStandardSizeLarge
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.size == ODSButtonSize.SMALL) {
            style.buttonLabelTextStyle =
                DSButtonTokens.buttonLabelTextStyleButtonTypeStandardSizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.PRIMARY && !props.disabled) {
            style.buttonLabelColor = scheme.basicTextOnAccent
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.SECONDARY && !props.disabled) {
            style.buttonLabelColor = scheme.basicTextOnAccentSecondary
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.GHOST && !props.disabled) {
            style.buttonLabelColor = scheme.basicText
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.OUTLINE && !props.disabled) {
            style.buttonLabelColor = scheme.basicText
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.PRIMARY && props.disabled) {
            style.buttonLabelColor = scheme.interactionStatesDisabledTextOnAccentDisabled
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.SECONDARY && props.disabled) {
            style.buttonLabelColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.GHOST && props.disabled) {
            style.buttonLabelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.OUTLINE && props.disabled) {
            style.buttonLabelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.PRIMARY && !props.disabled && state == ODSActions.HOVERED) {
            style.buttonLabelColor = scheme.interactionStatesHoverTextOnAccentHover
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.SECONDARY && !props.disabled && state == ODSActions.HOVERED) {
            style.buttonLabelColor = scheme.interactionStatesHoverTextOnAccentSecondaryHover
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.PRIMARY && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonLabelColor = scheme.interactionStatesPressedTextOnAccentPressed
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.SECONDARY && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonLabelColor = scheme.interactionStatesPressedTextOnAccentSecondaryPressed
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD) {
            style.rightIconVerticalAlignment =
                DSButtonTokens.rightIconVerticalAlignmentButtonTypeStandard
            style.rightIconHorizontalAlignment =
                DSButtonTokens.rightIconHorizontalAlignmentButtonTypeStandard
            style.rightIconHorizontalArrangement =
                DSButtonTokens.rightIconHorizontalArrangementButtonTypeStandard
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.size == ODSButtonSize.LARGE) {
            style.buttonIcon2Width = DSButtonTokens.buttonIcon2WidthButtonTypeStandardSizeLarge
            style.buttonIcon2Height = DSButtonTokens.buttonIcon2HeightButtonTypeStandardSizeLarge
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.size == ODSButtonSize.SMALL) {
            style.buttonIcon2Width = DSButtonTokens.buttonIcon2WidthButtonTypeStandardSizeSmall
            style.buttonIcon2Height = DSButtonTokens.buttonIcon2HeightButtonTypeStandardSizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.PRIMARY && !props.disabled) {
            style.buttonIcon2Color = scheme.basicTextOnAccent
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.SECONDARY && !props.disabled) {
            style.buttonIcon2Color = scheme.basicTextOnAccentSecondary
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.GHOST && !props.disabled) {
            style.buttonIcon2Color = scheme.basicText
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.OUTLINE && !props.disabled) {
            style.buttonIcon2Color = scheme.basicText
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.PRIMARY && props.disabled) {
            style.buttonIcon2Color = scheme.interactionStatesDisabledTextOnAccentDisabled
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.SECONDARY && props.disabled) {
            style.buttonIcon2Color = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.GHOST && props.disabled) {
            style.buttonIcon2Color = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.OUTLINE && props.disabled) {
            style.buttonIcon2Color = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.PRIMARY && !props.disabled && state == ODSActions.HOVERED) {
            style.buttonIcon2Color = scheme.interactionStatesHoverTextOnAccentHover
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.SECONDARY && !props.disabled && state == ODSActions.HOVERED) {
            style.buttonIcon2Color = scheme.interactionStatesHoverTextOnAccentSecondaryHover
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.PRIMARY && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonIcon2Color = scheme.interactionStatesPressedTextOnAccentPressed
        }
        if (props.buttonType == ODSButtonButtonType.STANDARD && props.variant == ODSButtonVariant.SECONDARY && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonIcon2Color = scheme.interactionStatesPressedTextOnAccentSecondaryPressed
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.size == ODSButtonSize.LARGE) {
            style.buttonIcon3Width = DSButtonTokens.buttonIcon3WidthButtonTypeIconOnlySizeLarge
            style.buttonIcon3Height = DSButtonTokens.buttonIcon3HeightButtonTypeIconOnlySizeLarge
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.size == ODSButtonSize.SMALL) {
            style.buttonIcon3Width = DSButtonTokens.buttonIcon3WidthButtonTypeIconOnlySizeSmall
            style.buttonIcon3Height = DSButtonTokens.buttonIcon3HeightButtonTypeIconOnlySizeSmall
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.PRIMARY && !props.disabled) {
            style.buttonIcon3Color = scheme.basicTextOnAccent
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.GHOST && !props.disabled) {
            style.buttonIcon3Color = scheme.basicText
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.OUTLINE && !props.disabled) {
            style.buttonIcon3Color = scheme.basicText
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.PRIMARY && props.disabled) {
            style.buttonIcon3Color = scheme.interactionStatesDisabledTextOnAccentDisabled
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.disabled) {
            style.buttonIcon3Color = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.GHOST && props.disabled) {
            style.buttonIcon3Color = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.OUTLINE && props.disabled) {
            style.buttonIcon3Color = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && !props.disabled && state == ODSActions.HOVERED) {
            style.buttonIcon3Color = scheme.interactionStatesHoverTextOnAccentSecondaryHover
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.PRIMARY && !props.disabled && state == ODSActions.HOVERED) {
            style.buttonIcon3Color = scheme.interactionStatesHoverTextOnAccentHover
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.SMALL && !props.disabled) {
            style.buttonIcon3Color = scheme.basicTextOnAccentSecondary
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.LARGE && !props.disabled) {
            style.buttonIcon3Color = scheme.basicTextOnAccentSecondary
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.PRIMARY && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonIcon3Color = scheme.interactionStatesPressedTextOnAccentPressed
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.LARGE && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonIcon3Color = scheme.basicTextOnAccentSecondary
        }
        if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.variant == ODSButtonVariant.SECONDARY && props.size == ODSButtonSize.SMALL && !props.disabled && state == ODSActions.PRESSED) {
            style.buttonIcon3Color = scheme.interactionStatesPressedTextOnAccentSecondaryPressed
        }
        // Custom addition
        style.scaleFactor = DSButtonTokens.scaleFactor
        style.buttonLabelTextOverflow = DSButtonTokens.buttonLabelTextOverflow
        return style
    }
}
