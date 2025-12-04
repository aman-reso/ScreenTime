package com.telekom.odsystem.molecules.listrownavigation

import ODSListRowNavigationProps
import ODSListRowNavigationVariant
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSListRowNavigationTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: a6fdd09
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-17292
 */

class ODSListRowNavigationStyle {
    var padding: ODSPadding? = null
    var cornerRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var background: List<ODSColorModel>? = null
    var contentGap: Dp? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var textContentGap: Dp? = null
    var textContentPadding: ODSPadding? = null
    var textContentVerticalAlignment: Alignment.Vertical? = null
    var textContentHorizontalAlignment: Alignment.Horizontal? = null
    var textContentHorizontalArrangement: Arrangement.Horizontal? = null
    var labelTextContentGap: Dp? = null
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
    var descriptionTextContentVerticalAlignment: Alignment.Vertical? = null
    var descriptionTextContentHorizontalAlignment: Alignment.Horizontal? = null
    var descriptionTextContentVerticalArrangement: Arrangement.Vertical? = null
    var descriptionStyle: ODSTextStyle? = null
    var descriptionColor: HexColor? = null
    var descriptionTextAlign: TextAlign? = null
    var descriptionTextStyle: ODSTextStyle? = null
    var descriptionTextColor: HexColor? = null
    var descriptionTextTextAlign: TextAlign? = null
    var rightCondensedColor: HexColor? = null
    var rightCondensedWidth: Dp? = null
    var rightCondensedHeight: Dp? = null
    var iconContainerWidth: Dp? = null
    var iconContainerHeight: Dp? = null
    var iconContainerClipContent: Boolean? = null
    var iconContainerVerticalAlignment: Alignment.Vertical? = null
    var iconContainerHorizontalAlignment: Alignment.Horizontal? = null
    var iconContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var imageCornerRadius: ODSCorners? = null
    var imageClipContent: Boolean? = null
    var imageVerticalAlignment: Alignment.Vertical? = null
    var imageHorizontalAlignment: Alignment.Horizontal? = null
    var imageHorizontalArrangement: Arrangement.Horizontal? = null
    var image2CornerRadius: ODSCorners? = null
    var image2Width: Dp? = null
    var image2Height: Dp? = null
    var image2ContentScale: ContentScale? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSListRowNavigationProps,
        state: ODSActions
    ): ODSListRowNavigationStyle {
        val style = ODSListRowNavigationStyle()
        style.padding = DSListRowNavigationTokens.padding
        style.cornerRadius = DSListRowNavigationTokens.cornerRadius
        style.minHeight = DSListRowNavigationTokens.minHeight
        style.verticalAlignment = DSListRowNavigationTokens.verticalAlignment
        style.horizontalAlignment = DSListRowNavigationTokens.horizontalAlignment
        style.horizontalArrangement = DSListRowNavigationTokens.horizontalArrangement
        if (state == ODSActions.HOVERED) {
            style.background =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (state == ODSActions.PRESSED) {
            style.background =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
        }
        style.contentGap = DSListRowNavigationTokens.contentGap
        style.contentVerticalAlignment = DSListRowNavigationTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSListRowNavigationTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSListRowNavigationTokens.contentHorizontalArrangement
        style.textContentGap = DSListRowNavigationTokens.textContentGap
        style.textContentPadding = DSListRowNavigationTokens.textContentPadding
        style.textContentVerticalAlignment = DSListRowNavigationTokens.textContentVerticalAlignment
        style.textContentHorizontalAlignment =
            DSListRowNavigationTokens.textContentHorizontalAlignment
        style.textContentHorizontalArrangement =
            DSListRowNavigationTokens.textContentHorizontalArrangement
        style.labelTextContentGap = DSListRowNavigationTokens.labelTextContentGap
        style.labelTextContentVerticalAlignment =
            DSListRowNavigationTokens.labelTextContentVerticalAlignment
        style.labelTextContentHorizontalAlignment =
            DSListRowNavigationTokens.labelTextContentHorizontalAlignment
        style.labelTextContentVerticalArrangement =
            DSListRowNavigationTokens.labelTextContentVerticalArrangement
        style.labelStyle = DSListRowNavigationTokens.labelStyle
        style.labelColor = scheme.basicText
        style.labelTextAlign = DSListRowNavigationTokens.labelTextAlign
        style.labelTextStyle = DSListRowNavigationTokens.labelTextStyle
        style.labelTextColor = scheme.basicTextRecessive
        style.labelTextTextAlign = DSListRowNavigationTokens.labelTextTextAlign
        style.descriptionTextContentGap = DSListRowNavigationTokens.descriptionTextContentGap
        style.descriptionTextContentVerticalAlignment =
            DSListRowNavigationTokens.descriptionTextContentVerticalAlignment
        style.descriptionTextContentHorizontalAlignment =
            DSListRowNavigationTokens.descriptionTextContentHorizontalAlignment
        style.descriptionTextContentVerticalArrangement =
            DSListRowNavigationTokens.descriptionTextContentVerticalArrangement
        style.descriptionStyle = DSListRowNavigationTokens.descriptionStyle
        style.descriptionColor = scheme.basicText
        style.descriptionTextAlign = DSListRowNavigationTokens.descriptionTextAlign
        style.descriptionTextStyle = DSListRowNavigationTokens.descriptionTextStyle
        style.descriptionTextColor = scheme.basicTextRecessive
        style.descriptionTextTextAlign = DSListRowNavigationTokens.descriptionTextTextAlign
        style.rightCondensedColor = scheme.basicText
        style.rightCondensedWidth = DSListRowNavigationTokens.rightCondensedWidth
        style.rightCondensedHeight = DSListRowNavigationTokens.rightCondensedHeight
        if (props.variant == ODSListRowNavigationVariant.ICON) {
            style.iconContainerWidth = DSListRowNavigationTokens.iconContainerWidthVariantIcon
            style.iconContainerHeight = DSListRowNavigationTokens.iconContainerHeightVariantIcon
            style.iconContainerClipContent =
                DSListRowNavigationTokens.iconContainerClipContentVariantIcon
            style.iconContainerVerticalAlignment =
                DSListRowNavigationTokens.iconContainerVerticalAlignmentVariantIcon
            style.iconContainerHorizontalAlignment =
                DSListRowNavigationTokens.iconContainerHorizontalAlignmentVariantIcon
            style.iconContainerHorizontalArrangement =
                DSListRowNavigationTokens.iconContainerHorizontalArrangementVariantIcon
        }
        if (props.variant == ODSListRowNavigationVariant.ICON) {
            style.iconColor = scheme.basicText
            style.iconWidth = DSListRowNavigationTokens.iconWidthVariantIcon
            style.iconHeight = DSListRowNavigationTokens.iconHeightVariantIcon
        }
        if (props.variant == ODSListRowNavigationVariant.IMAGE) {
            style.imageCornerRadius = DSListRowNavigationTokens.imageCornerRadiusVariantImage
            style.imageClipContent = DSListRowNavigationTokens.imageClipContentVariantImage
            style.imageVerticalAlignment =
                DSListRowNavigationTokens.imageVerticalAlignmentVariantImage
            style.imageHorizontalAlignment =
                DSListRowNavigationTokens.imageHorizontalAlignmentVariantImage
            style.imageHorizontalArrangement =
                DSListRowNavigationTokens.imageHorizontalArrangementVariantImage
        }
        if (props.variant == ODSListRowNavigationVariant.IMAGE) {
            style.image2CornerRadius = DSListRowNavigationTokens.image2CornerRadiusVariantImage
            style.image2Width = DSListRowNavigationTokens.image2WidthVariantImage
            style.image2Height = DSListRowNavigationTokens.image2HeightVariantImage
            style.image2ContentScale = DSListRowNavigationTokens.image2ContentScaleVariantImage
        }
        return style
    }
}
