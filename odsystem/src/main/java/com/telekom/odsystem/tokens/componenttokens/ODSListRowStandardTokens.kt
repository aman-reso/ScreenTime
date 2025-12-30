package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: a6fe3d3
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-17434
 */

data class ODSListRowStandardTokens(
    val gap: Dp,
    val padding: ODSPadding, // Added to match Base
    val minHeight: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val textContentGap: Dp,
    val textContentVerticalAlignment: Alignment.Vertical,
    val textContentHorizontalAlignment: Alignment.Horizontal,
    val textContentHorizontalArrangement: Arrangement.Horizontal,
    val labelTextContentGap: Dp,
    val labelTextContentPadding: ODSPadding,
    val labelTextContentVerticalAlignment: Alignment.Vertical,
    val labelTextContentHorizontalAlignment: Alignment.Horizontal,
    val labelTextContentVerticalArrangement: Arrangement.Vertical,
    val labelStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val labelTextStyle: ODSTextStyle,
    val labelTextTextAlign: TextAlign,
    val descriptionTextContentGap: Dp,
    val descriptionTextContentPadding: ODSPadding,
    val descriptionTextContentVerticalAlignment: Alignment.Vertical,
    val descriptionTextContentHorizontalAlignment: Alignment.Horizontal,
    val descriptionTextContentVerticalArrangement: Arrangement.Vertical,
    val descriptionStyle: ODSTextStyle,
    val descriptionTextAlign: TextAlign,
    val descriptionTextStyle: ODSTextStyle,
    val descriptionTextTextAlign: TextAlign,
    val imageCornerRadiusVariantImage: ODSCorners,
    val imageClipContentVariantImage: Boolean,
    val imageVerticalAlignmentVariantImage: Alignment.Vertical,
    val imageHorizontalAlignmentVariantImage: Alignment.Horizontal,
    val imageHorizontalArrangementVariantImage: Arrangement.Horizontal,
    val image2CornerRadiusVariantImage: ODSCorners,
    val image2WidthVariantImage: Dp,
    val image2HeightVariantImage: Dp,
    val image2ContentScaleVariantImage: ContentScale,
    val iconContainerWidthVariantIcon: Dp,
    val iconContainerHeightVariantIcon: Dp,
    val iconContainerVerticalAlignmentVariantIcon: Alignment.Vertical,
    val iconContainerHorizontalAlignmentVariantIcon: Alignment.Horizontal,
    val iconContainerHorizontalArrangementVariantIcon: Arrangement.Horizontal,
    val iconWidthVariantIcon: Dp,
    val iconHeightVariantIcon: Dp
)

val defaultODSListRowStandardTokens = ODSListRowStandardTokens(
    gap = DSVariables.spacingComponent3,
    padding = ODSPadding(all = 0.dp),
    minHeight = DSVariables.sizingComponent14,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    textContentGap = DSVariables.spacingComponent3,
    textContentVerticalAlignment = Alignment.CenterVertically,
    textContentHorizontalAlignment = Alignment.Start,
    textContentHorizontalArrangement = Arrangement.Start,
    labelTextContentGap = DSVariables.spacingComponent1,
    labelTextContentPadding = ODSPadding(
        top = DSVariables.spacingComponent0,
    ),
    labelTextContentVerticalAlignment = Alignment.CenterVertically,
    labelTextContentHorizontalAlignment = Alignment.Start,
    labelTextContentVerticalArrangement = Arrangement.Center,
    labelStyle = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Left,
    labelTextStyle = DSTextStyles.bodySRegular,
    labelTextTextAlign = TextAlign.Left,
    descriptionTextContentGap = DSVariables.spacingComponent1,
    descriptionTextContentPadding = ODSPadding(
        top = DSVariables.spacingComponent0,
    ),
    descriptionTextContentVerticalAlignment = Alignment.CenterVertically,
    descriptionTextContentHorizontalAlignment = Alignment.End,
    descriptionTextContentVerticalArrangement = Arrangement.Center,
    descriptionStyle = DSTextStyles.bodyMBold,
    descriptionTextAlign = TextAlign.Right,
    descriptionTextStyle = DSTextStyles.bodySRegular,
    descriptionTextTextAlign = TextAlign.Right,
    imageCornerRadiusVariantImage = ODSCorners(all = DSVariables.radiusFull),
    imageClipContentVariantImage = true,
    imageVerticalAlignmentVariantImage = Alignment.CenterVertically,
    imageHorizontalAlignmentVariantImage = Alignment.CenterHorizontally,
    imageHorizontalArrangementVariantImage = Arrangement.Center,
    image2CornerRadiusVariantImage = ODSCorners(all = DSVariables.radiusFull),
    image2WidthVariantImage = DSVariables.sizingComponent13,
    image2HeightVariantImage = DSVariables.sizingComponent13,
    image2ContentScaleVariantImage = ContentScale.Crop,
    iconContainerWidthVariantIcon = DSVariables.sizingComponent13,
    iconContainerHeightVariantIcon = DSVariables.sizingComponent13,
    iconContainerVerticalAlignmentVariantIcon = Alignment.CenterVertically,
    iconContainerHorizontalAlignmentVariantIcon = Alignment.CenterHorizontally,
    iconContainerHorizontalArrangementVariantIcon = Arrangement.Center,
    iconWidthVariantIcon = DSVariables.sizingComponent10,
    iconHeightVariantIcon = DSVariables.sizingComponent10
)

var DSListRowStandardTokens: ODSListRowStandardTokens = defaultODSListRowStandardTokens
