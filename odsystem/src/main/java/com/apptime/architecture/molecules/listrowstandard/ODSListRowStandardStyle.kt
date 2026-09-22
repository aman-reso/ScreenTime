package com.telekom.odsystem.molecules.listrowstandard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSListRowStandardTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: a6fe3d3
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-17434
 */

class ODSListRowStandardStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null // Added to support Base
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var textContentGap: Dp? = null
    var textContentVerticalAlignment: Alignment.Vertical? = null
    var textContentHorizontalAlignment: Alignment.Horizontal? = null
    var textContentHorizontalArrangement: Arrangement.Horizontal? = null
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
    var descriptionTextContentGap: Dp? = null
    var descriptionTextContentPadding: ODSPadding? = null
    var descriptionTextContentVerticalAlignment: Alignment.Vertical? = null
    var descriptionTextContentHorizontalAlignment: Alignment.Horizontal? = null
    var descriptionTextContentVerticalArrangement: Arrangement.Vertical? = null
    var descriptionStyle: ODSTextStyle? = null
    var descriptionColor: HexColor? = null
    var descriptionTextAlign: TextAlign? = null
    var descriptionTextStyle: ODSTextStyle? = null
    var descriptionTextColor: HexColor? = null
    var descriptionTextTextAlign: TextAlign? = null
    var imageCornerRadius: ODSCorners? = null
    var imageClipContent: Boolean? = null
    var imageVerticalAlignment: Alignment.Vertical? = null
    var imageHorizontalAlignment: Alignment.Horizontal? = null
    var imageHorizontalArrangement: Arrangement.Horizontal? = null
    var image2CornerRadius: ODSCorners? = null
    var image2Width: Dp? = null
    var image2Height: Dp? = null
    var image2ContentScale: ContentScale? = null
    var iconContainerWidth: Dp? = null
    var iconContainerHeight: Dp? = null
    var iconContainerVerticalAlignment: Alignment.Vertical? = null
    var iconContainerHorizontalAlignment: Alignment.Horizontal? = null
    var iconContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSListRowStandardProps
    ): ODSListRowStandardStyle {
        val style = ODSListRowStandardStyle()
        style.gap = DSListRowStandardTokens.gap
        style.padding = DSListRowStandardTokens.padding
        style.minHeight = DSListRowStandardTokens.minHeight
        style.verticalAlignment = DSListRowStandardTokens.verticalAlignment
        style.horizontalAlignment = DSListRowStandardTokens.horizontalAlignment
        style.horizontalArrangement = DSListRowStandardTokens.horizontalArrangement
        style.textContentGap = DSListRowStandardTokens.textContentGap
        style.textContentVerticalAlignment = DSListRowStandardTokens.textContentVerticalAlignment
        style.textContentHorizontalAlignment =
            DSListRowStandardTokens.textContentHorizontalAlignment
        style.textContentHorizontalArrangement =
            DSListRowStandardTokens.textContentHorizontalArrangement
        style.labelTextContentGap = DSListRowStandardTokens.labelTextContentGap
        style.labelTextContentPadding = DSListRowStandardTokens.labelTextContentPadding
        style.labelTextContentVerticalAlignment =
            DSListRowStandardTokens.labelTextContentVerticalAlignment
        style.labelTextContentHorizontalAlignment =
            DSListRowStandardTokens.labelTextContentHorizontalAlignment
        style.labelTextContentVerticalArrangement =
            DSListRowStandardTokens.labelTextContentVerticalArrangement
        style.labelStyle = DSListRowStandardTokens.labelStyle
        style.labelColor = scheme.basicText
        style.labelTextAlign = DSListRowStandardTokens.labelTextAlign
        style.labelTextStyle = DSListRowStandardTokens.labelTextStyle
        style.labelTextColor = scheme.basicText
        style.labelTextTextAlign = DSListRowStandardTokens.labelTextTextAlign
        style.descriptionTextContentGap = DSListRowStandardTokens.descriptionTextContentGap
        style.descriptionTextContentPadding = DSListRowStandardTokens.descriptionTextContentPadding
        style.descriptionTextContentVerticalAlignment =
            DSListRowStandardTokens.descriptionTextContentVerticalAlignment
        style.descriptionTextContentHorizontalAlignment =
            DSListRowStandardTokens.descriptionTextContentHorizontalAlignment
        style.descriptionTextContentVerticalArrangement =
            DSListRowStandardTokens.descriptionTextContentVerticalArrangement
        style.descriptionStyle = DSListRowStandardTokens.descriptionStyle
        style.descriptionColor = scheme.basicText
        style.descriptionTextAlign = DSListRowStandardTokens.descriptionTextAlign
        style.descriptionTextStyle = DSListRowStandardTokens.descriptionTextStyle
        style.descriptionTextColor = scheme.basicTextRecessive
        style.descriptionTextTextAlign = DSListRowStandardTokens.descriptionTextTextAlign
        if (props.variant == ODSListRowStandardVariant.IMAGE) {
            style.imageCornerRadius = DSListRowStandardTokens.imageCornerRadiusVariantImage
            style.imageClipContent = DSListRowStandardTokens.imageClipContentVariantImage
            style.imageVerticalAlignment =
                DSListRowStandardTokens.imageVerticalAlignmentVariantImage
            style.imageHorizontalAlignment =
                DSListRowStandardTokens.imageHorizontalAlignmentVariantImage
            style.imageHorizontalArrangement =
                DSListRowStandardTokens.imageHorizontalArrangementVariantImage
        }
        if (props.variant == ODSListRowStandardVariant.IMAGE) {
            style.image2CornerRadius = DSListRowStandardTokens.image2CornerRadiusVariantImage
            style.image2Width = DSListRowStandardTokens.image2WidthVariantImage
            style.image2Height = DSListRowStandardTokens.image2HeightVariantImage
            style.image2ContentScale = DSListRowStandardTokens.image2ContentScaleVariantImage
        }
        if (props.variant == ODSListRowStandardVariant.ICON) {
            style.iconContainerWidth = DSListRowStandardTokens.iconContainerWidthVariantIcon
            style.iconContainerHeight = DSListRowStandardTokens.iconContainerHeightVariantIcon
            style.iconContainerVerticalAlignment =
                DSListRowStandardTokens.iconContainerVerticalAlignmentVariantIcon
            style.iconContainerHorizontalAlignment =
                DSListRowStandardTokens.iconContainerHorizontalAlignmentVariantIcon
            style.iconContainerHorizontalArrangement =
                DSListRowStandardTokens.iconContainerHorizontalArrangementVariantIcon
        }
        if (props.variant == ODSListRowStandardVariant.ICON) {
            style.iconColor = scheme.basicText
            style.iconWidth = DSListRowStandardTokens.iconWidthVariantIcon
            style.iconHeight = DSListRowStandardTokens.iconHeightVariantIcon
        }
        return style
    }
}
