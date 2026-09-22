package com.telekom.odsystem.atoms.togglechip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.tokens.componenttokens.DSToggleChipTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSToggleChipStyle {
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var toggleChipGap: Dp? = null
    var toggleChipBackground: List<ODSColorModel>? = null
    var toggleChipPadding: ODSPadding? = null
    var toggleChipCornerRadius: ODSCorners? = null
    var toggleChipBorder: Dp? = null
    var toggleChipBorderColor: List<ODSColorModel>? = null
    var toggleChipMinHeight: Dp? = null
    var toggleChipMinWidth: Dp? = null
    var toggleChipVerticalAlignment: Alignment.Vertical? = null
    var toggleChipHorizontalAlignment: Alignment.Horizontal? = null
    var toggleChipHorizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var chipStyle: ODSTextStyle? = null
    var chipColor: HexColor? = null
    var chipTextAlign: TextAlign? = null
    var chipOverflow: TextOverflow? = null
    var imageCornerRadius: ODSCorners? = null
    var imageClipContent: Boolean? = null
    var imageVerticalAlignment: Alignment.Vertical? = null
    var imageHorizontalAlignment: Alignment.Horizontal? = null
    var imageHorizontalArrangement: Arrangement.Horizontal? = null
    var image2CornerRadius: ODSCorners? = null
    var image2Width: Dp? = null
    var image2Height: Dp? = null
    var image2ContentScale: ContentScale? = null // Custom type is being used
    var checkmarkColor: HexColor? = null
    var checkmarkWidth: Dp? = null
    var checkmarkHeight: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSToggleChipProps,
        state: ODSActions
    ): ODSToggleChipStyle {
        val style = ODSToggleChipStyle()
        style.minHeight = DSToggleChipTokens.minHeight
        style.minWidth = DSToggleChipTokens.minWidth
        style.verticalAlignment = DSToggleChipTokens.verticalAlignment
        style.horizontalAlignment = DSToggleChipTokens.horizontalAlignment
        style.verticalArrangement = DSToggleChipTokens.verticalArrangement
        style.toggleChipGap = DSToggleChipTokens.toggleChipGap
        style.toggleChipCornerRadius = DSToggleChipTokens.toggleChipCornerRadius
        style.toggleChipMinHeight = DSToggleChipTokens.toggleChipMinHeight
        style.toggleChipMinWidth = DSToggleChipTokens.toggleChipMinWidth
        style.toggleChipVerticalAlignment = DSToggleChipTokens.toggleChipVerticalAlignment
        style.toggleChipHorizontalAlignment = DSToggleChipTokens.toggleChipHorizontalAlignment
        style.toggleChipHorizontalArrangement = DSToggleChipTokens.toggleChipHorizontalArrangement
        if (props.showImage) {
            style.toggleChipPadding = DSToggleChipTokens.toggleChipPaddingShowImage
        }
        if (!props.selected) {
            style.toggleChipBorder = DSToggleChipTokens.toggleChipBorder
        }
        if (!props.showImage) {
            style.toggleChipPadding = DSToggleChipTokens.toggleChipPadding
        }
        if (!props.selected && !props.disabled) {
            style.toggleChipBackground = listOf(ODSColorModel(hexColor = scheme.basicBackground))
            style.toggleChipBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.selected && !props.disabled) {
            style.toggleChipBackground =
                listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        }
        if (!props.selected && props.disabled) {
            style.toggleChipBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.toggleChipBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.selected && props.disabled) {
            style.toggleChipBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentSecondaryDisabled))
        }
        if (!props.selected && !props.disabled && state == ODSActions.HOVERED) {
            style.toggleChipBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
            style.toggleChipBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (props.selected && !props.disabled && state == ODSActions.HOVERED) {
            style.toggleChipBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentSecondaryHover))
        }
        if (props.selected && !props.disabled && state == ODSActions.PRESSED) {
            style.toggleChipBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentSecondaryPressed))
        }
        if (!props.selected && !props.disabled && state == ODSActions.PRESSED) {
            style.toggleChipBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
            style.toggleChipBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedStrokePressed))
        }
        if (!props.showImage) {
            style.iconWidth = DSToggleChipTokens.iconWidth
            style.iconHeight = DSToggleChipTokens.iconHeight
        }
        if (!props.selected && !props.disabled && !props.showImage) {
            style.iconColor = scheme.basicText
        }
        if (props.selected && !props.disabled && !props.showImage) {
            style.iconColor = scheme.basicTextOnAccentSecondary
        }
        if (!props.selected && props.disabled && !props.showImage) {
            style.iconColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.selected && props.disabled && !props.showImage) {
            style.iconColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        style.chipStyle = DSToggleChipTokens.chipStyle
        style.chipTextAlign = DSToggleChipTokens.chipTextAlign
        style.chipOverflow = DSToggleChipTokens.chipOverflow
        if (!props.selected && !props.disabled) {
            style.chipColor = scheme.basicText
        }
        if (props.selected && !props.disabled) {
            style.chipColor = scheme.basicTextOnAccentSecondary
        }
        if (!props.selected && props.disabled) {
            style.chipColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.selected && props.disabled) {
            style.chipColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        if (props.showImage) {
            style.imageCornerRadius = DSToggleChipTokens.imageCornerRadiusShowImage
            style.imageClipContent = DSToggleChipTokens.imageClipContentShowImage
            style.imageVerticalAlignment = DSToggleChipTokens.imageVerticalAlignmentShowImage
            style.imageHorizontalAlignment = DSToggleChipTokens.imageHorizontalAlignmentShowImage
            style.imageHorizontalArrangement =
                DSToggleChipTokens.imageHorizontalArrangementShowImage
        }
        if (props.showImage) {
            style.image2CornerRadius = DSToggleChipTokens.image2CornerRadiusShowImage
            style.image2Width = DSToggleChipTokens.image2WidthShowImage
            style.image2Height = DSToggleChipTokens.image2HeightShowImage
            style.image2ContentScale = DSToggleChipTokens.image2ContentScaleShowImage
        }
        if (props.selected) {
            style.checkmarkWidth = DSToggleChipTokens.checkmarkWidthSelected
            style.checkmarkHeight = DSToggleChipTokens.checkmarkHeightSelected
        }
        if (props.selected && !props.disabled) {
            style.checkmarkColor = scheme.basicTextOnAccentSecondary
        }
        if (props.selected && props.disabled) {
            style.checkmarkColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        return style
    }
}
