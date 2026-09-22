package com.telekom.odsystem.molecules.listrowcontrols

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSListRowControlsTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: a6fe42f
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-17463
 */

@Suppress("LongMethod")
class ODSListRowControlsStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var cornerRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var background: List<ODSColorModel>? = null
    var textContentControlGap: Dp? = null
    var textContentControlPadding: ODSPadding? = null // Added to match Base
    var textContentControlVerticalAlignment: Alignment.Vertical? = null
    var textContentControlHorizontalAlignment: Alignment.Horizontal? = null
    var textContentControlHorizontalArrangement: Arrangement.Horizontal? = null
    var labelTextContentGap: Dp? = null
    var labelTextContentPadding: ODSPadding? = null
    var labelTextContentVerticalAlignment: Alignment.Vertical? = null
    var labelTextContentHorizontalAlignment: Alignment.Horizontal? = null
    var labelTextContentVerticalArrangement: Arrangement.Vertical? = null
    var labelStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelTextColor: HexColor? = null
    var labelTextTextAlign: TextAlign? = null
    var imageCornerRadius: ODSCorners? = null
    var imageClipContent: Boolean? = null
    var imageVerticalAlignment: Alignment.Vertical? = null
    var imageHorizontalAlignment: Alignment.Horizontal? = null
    var imageHorizontalArrangement: Arrangement.Horizontal? = null
    var imageOpacity: Float? = null
    var image2CornerRadius: ODSCorners? = null
    var image2Width: Dp? = null
    var image2Height: Dp? = null
    var image2ContentScale: ContentScale? = null
    var iconContainerPadding: ODSPadding? = null
    var iconContainerWidth: Dp? = null
    var iconContainerHeight: Dp? = null
    var iconContainerVerticalAlignment: Alignment.Vertical? = null
    var iconContainerHorizontalAlignment: Alignment.Horizontal? = null
    var iconContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var iconContainerOpacity: Float? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSListRowControlsProps,
        state: ODSActions
    ): ODSListRowControlsStyle {
        val style = ODSListRowControlsStyle()
        style.gap = DSListRowControlsTokens.gap
        style.padding = DSListRowControlsTokens.padding
        style.cornerRadius = DSListRowControlsTokens.cornerRadius
        style.minHeight = DSListRowControlsTokens.minHeight
        style.verticalAlignment = DSListRowControlsTokens.verticalAlignment
        style.horizontalAlignment = DSListRowControlsTokens.horizontalAlignment
        style.horizontalArrangement = DSListRowControlsTokens.horizontalArrangement
        if (props.disabled && !props.readOnly) {
            style.background =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        }
        if (props.variant == ODSListRowControlsVariant.ICON && !props.disabled && props.readOnly) {
            style.background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (!props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.background =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (!props.disabled && !props.readOnly && state == ODSActions.PRESSED) {
            style.background =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
        }
        style.textContentControlGap = DSListRowControlsTokens.textContentControlGap
        style.textContentControlPadding = DSListRowControlsTokens.textContentControlPadding
        style.textContentControlVerticalAlignment =
            DSListRowControlsTokens.textContentControlVerticalAlignment
        style.textContentControlHorizontalAlignment =
            DSListRowControlsTokens.textContentControlHorizontalAlignment
        style.textContentControlHorizontalArrangement =
            DSListRowControlsTokens.textContentControlHorizontalArrangement
        style.labelTextContentGap = DSListRowControlsTokens.labelTextContentGap
        style.labelTextContentPadding = DSListRowControlsTokens.labelTextContentPadding
        style.labelTextContentVerticalAlignment =
            DSListRowControlsTokens.labelTextContentVerticalAlignment
        style.labelTextContentHorizontalAlignment =
            DSListRowControlsTokens.labelTextContentHorizontalAlignment
        style.labelTextContentVerticalArrangement =
            DSListRowControlsTokens.labelTextContentVerticalArrangement
        style.labelStyle = DSListRowControlsTokens.labelStyle
        style.labelTextAlign = DSListRowControlsTokens.labelTextAlign
        if (!props.disabled) {
            style.labelColor = scheme.basicText
        }
        if (props.disabled && !props.readOnly) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        style.labelTextStyle = DSListRowControlsTokens.labelTextStyle
        style.labelTextTextAlign = DSListRowControlsTokens.labelTextTextAlign
        if (!props.disabled) {
            style.labelTextColor = scheme.basicTextRecessive
        }
        if (props.disabled && !props.readOnly) {
            style.labelTextColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.variant == ODSListRowControlsVariant.IMAGE) {
            style.imageCornerRadius = DSListRowControlsTokens.imageCornerRadiusVariantImage
            style.imageClipContent = DSListRowControlsTokens.imageClipContentVariantImage
            style.imageVerticalAlignment =
                DSListRowControlsTokens.imageVerticalAlignmentVariantImage
            style.imageHorizontalAlignment =
                DSListRowControlsTokens.imageHorizontalAlignmentVariantImage
            style.imageHorizontalArrangement =
                DSListRowControlsTokens.imageHorizontalArrangementVariantImage
        }
        if (props.variant == ODSListRowControlsVariant.IMAGE && props.disabled && !props.readOnly) {
            style.imageOpacity = DSListRowControlsTokens.imageOpacityVariantImageDisabled
        }
        if (props.variant == ODSListRowControlsVariant.IMAGE) {
            style.image2CornerRadius = DSListRowControlsTokens.image2CornerRadiusVariantImage
            style.image2Width = DSListRowControlsTokens.image2WidthVariantImage
            style.image2Height = DSListRowControlsTokens.image2HeightVariantImage
            style.image2ContentScale = DSListRowControlsTokens.image2ContentScaleVariantImage
        }
        if (props.variant == ODSListRowControlsVariant.ICON) {
            style.iconContainerPadding = DSListRowControlsTokens.iconContainerPaddingVariantIcon
            style.iconContainerWidth = DSListRowControlsTokens.iconContainerWidthVariantIcon
            style.iconContainerHeight = DSListRowControlsTokens.iconContainerHeightVariantIcon
            style.iconContainerVerticalAlignment =
                DSListRowControlsTokens.iconContainerVerticalAlignmentVariantIcon
            style.iconContainerHorizontalAlignment =
                DSListRowControlsTokens.iconContainerHorizontalAlignmentVariantIcon
            style.iconContainerHorizontalArrangement =
                DSListRowControlsTokens.iconContainerHorizontalArrangementVariantIcon
        }
        if (props.variant == ODSListRowControlsVariant.ICON && props.disabled && !props.readOnly) {
            style.iconContainerOpacity =
                DSListRowControlsTokens.iconContainerOpacityVariantIconDisabled
        }
        if (props.variant == ODSListRowControlsVariant.ICON) {
            style.iconColor = scheme.basicText
            style.iconWidth = DSListRowControlsTokens.iconWidthVariantIcon
            style.iconHeight = DSListRowControlsTokens.iconHeightVariantIcon
        }
        return style
    }
}
