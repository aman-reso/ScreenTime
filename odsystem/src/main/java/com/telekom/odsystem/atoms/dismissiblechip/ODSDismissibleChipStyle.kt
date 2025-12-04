package com.telekom.odsystem.atoms.dismissiblechip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSDismissibleChipTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSDismissibleChipStyle {
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var dismissibleChipGap: Dp? = null
    var dismissibleChipBackgroundColor: List<ODSColorModel>? = null
    var dismissibleChipPadding: ODSPadding? = null
    var dismissibleChipBorderRadius: ODSCorners? = null
    var dismissibleChipMinHeight: Dp? = null
    var dismissibleChipMinWidth: Dp? = null
    var dismissibleChipVerticalAlignment: Alignment.Vertical? = null
    var dismissibleChipHorizontalAlignment: Alignment.Horizontal? = null
    var dismissibleChipHorizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var chipTextStyle: ODSTextStyle? = null
    var chipColor: HexColor? = null
    var chipTextAlign: TextAlign? = null
    var chipTextOverflow: TextOverflow? = null
    var actionBorderRadius: ODSCorners? = null
    var actionVerticalAlignment: Alignment.Vertical? = null
    var actionHorizontalAlignment: Alignment.Horizontal? = null
    var actionHorizontalArrangement: Arrangement.Horizontal? = null
    var actionBackgroundColor: List<ODSColorModel>? = null
    var closeColor: HexColor? = null
    var closeWidth: Dp? = null
    var closeHeight: Dp? = null
    var imageBorderRadius: ODSCorners? = null
    var imageClipContent: Boolean? = null
    var imageVerticalAlignment: Alignment.Vertical? = null
    var imageHorizontalAlignment: Alignment.Horizontal? = null
    var imageHorizontalArrangement: Arrangement.Horizontal? = null
    var image2BorderRadius: ODSCorners? = null
    var image2Width: Dp? = null
    var image2Height: Dp? = null
    var image2ObjectFit: ContentScale? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSDismissibleChipProps,
        state: ODSActions
    ): ODSDismissibleChipStyle {
        val style = ODSDismissibleChipStyle()
        style.minHeight = DSDismissibleChipTokens.minHeight
        style.minWidth = DSDismissibleChipTokens.minWidth
        style.verticalAlignment = DSDismissibleChipTokens.verticalAlignment
        style.horizontalAlignment = DSDismissibleChipTokens.horizontalAlignment
        style.verticalArrangement = DSDismissibleChipTokens.verticalArrangement
        style.dismissibleChipGap = DSDismissibleChipTokens.dismissibleChipGap
        style.dismissibleChipBorderRadius = DSDismissibleChipTokens.dismissibleChipBorderRadius
        style.dismissibleChipMinHeight = DSDismissibleChipTokens.dismissibleChipMinHeight
        style.dismissibleChipMinWidth = DSDismissibleChipTokens.dismissibleChipMinWidth
        style.dismissibleChipVerticalAlignment =
            DSDismissibleChipTokens.dismissibleChipVerticalAlignment
        style.dismissibleChipHorizontalAlignment =
            DSDismissibleChipTokens.dismissibleChipHorizontalAlignment
        style.dismissibleChipHorizontalArrangement =
            DSDismissibleChipTokens.dismissibleChipHorizontalArrangement
        if (props.disabled) {
            style.dismissibleChipBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentSecondaryDisabled))
        }
        if (!props.disabled) {
            style.dismissibleChipBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        }
        if (props.variant == ODSDismissibleChipVariant.STANDARD) {
            style.dismissibleChipPadding =
                DSDismissibleChipTokens.dismissibleChipPaddingTypeStandard
        }
        if (props.variant == ODSDismissibleChipVariant.WITH_ICON) {
            style.dismissibleChipPadding =
                DSDismissibleChipTokens.dismissibleChipPaddingTypeWithIcon
        }
        if (props.variant == ODSDismissibleChipVariant.WITH_IMAGE) {
            style.dismissibleChipPadding =
                DSDismissibleChipTokens.dismissibleChipPaddingTypeWithPicture
        }
        if (props.variant == ODSDismissibleChipVariant.STANDARD) {
            style.iconWidth = DSDismissibleChipTokens.iconWidthTypeStandard
            style.iconHeight = DSDismissibleChipTokens.iconHeightTypeStandard
        }
        if (props.variant == ODSDismissibleChipVariant.WITH_ICON) {
            style.iconWidth = DSDismissibleChipTokens.iconWidthTypeWithIcon
            style.iconHeight = DSDismissibleChipTokens.iconHeightTypeWithIcon
        }
        if (props.variant == ODSDismissibleChipVariant.STANDARD && !props.disabled) {
            style.iconColor = scheme.basicTextOnAccentSecondary
        }
        if (props.variant == ODSDismissibleChipVariant.WITH_ICON && !props.disabled) {
            style.iconColor = scheme.basicTextOnAccentSecondary
        }
        if (props.variant == ODSDismissibleChipVariant.STANDARD && props.disabled) {
            style.iconColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        if (props.variant == ODSDismissibleChipVariant.WITH_ICON && props.disabled) {
            style.iconColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        style.chipTextStyle = DSDismissibleChipTokens.chipTextStyle
        style.chipTextAlign = DSDismissibleChipTokens.chipTextAlign
        style.chipTextOverflow = DSDismissibleChipTokens.chipTextOverflow
        if (props.disabled) {
            style.chipColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        if (!props.disabled) {
            style.chipColor = scheme.basicTextOnAccentSecondary
        }
        style.actionBorderRadius = DSDismissibleChipTokens.actionBorderRadius
        style.actionVerticalAlignment = DSDismissibleChipTokens.actionVerticalAlignment
        style.actionHorizontalAlignment = DSDismissibleChipTokens.actionHorizontalAlignment
        style.actionHorizontalArrangement = DSDismissibleChipTokens.actionHorizontalArrangement
        if (!props.disabled && state == ODSActions.HOVERED) {
            style.actionBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentSecondaryHover))
        }
        if (!props.disabled && state == ODSActions.PRESSED) {
            style.actionBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentSecondaryPressed))
        }
        style.closeWidth = DSDismissibleChipTokens.closeWidth
        style.closeHeight = DSDismissibleChipTokens.closeHeight
        if (props.disabled) {
            style.closeColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        if (!props.disabled) {
            style.closeColor = scheme.basicTextOnAccentSecondary
        }
        if (props.variant == ODSDismissibleChipVariant.WITH_IMAGE) {
            style.imageBorderRadius = DSDismissibleChipTokens.imageBorderRadiusTypeWithPicture
            style.imageClipContent = DSDismissibleChipTokens.imageClipContentTypeWithPicture
            style.imageVerticalAlignment =
                DSDismissibleChipTokens.imageVerticalAlignmentTypeWithPicture
            style.imageHorizontalAlignment =
                DSDismissibleChipTokens.imageHorizontalAlignmentTypeWithPicture
            style.imageHorizontalArrangement =
                DSDismissibleChipTokens.imageHorizontalArrangementTypeWithPicture
        }
        if (props.variant == ODSDismissibleChipVariant.WITH_IMAGE) {
            style.image2BorderRadius = DSDismissibleChipTokens.image2BorderRadiusTypeWithPicture
            style.image2Width = DSDismissibleChipTokens.image2WidthTypeWithPicture
            style.image2Height = DSDismissibleChipTokens.image2HeightTypeWithPicture
            style.image2ObjectFit = DSDismissibleChipTokens.image2ObjectFitTypeWithPicture
        }
        return style
    }
}
