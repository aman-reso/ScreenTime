package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: a6fdd09
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-17292
 */

data class ODSListRowNavigationTokens(
    val padding: ODSPadding,
    val cornerRadius: ODSCorners,
    val minHeight: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val contentGap: Dp,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentHorizontalArrangement: Arrangement.Horizontal,
    val textContentGap: Dp,
    val textContentPadding: ODSPadding,
    val textContentVerticalAlignment: Alignment.Vertical,
    val textContentHorizontalAlignment: Alignment.Horizontal,
    val textContentHorizontalArrangement: Arrangement.Horizontal,
    val labelTextContentGap: Dp,
    val labelTextContentVerticalAlignment: Alignment.Vertical,
    val labelTextContentHorizontalAlignment: Alignment.Horizontal,
    val labelTextContentVerticalArrangement: Arrangement.Vertical,
    val labelStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val labelTextStyle: ODSTextStyle,
    val labelTextTextAlign: TextAlign,
    val descriptionTextContentGap: Dp,
    val descriptionTextContentVerticalAlignment: Alignment.Vertical,
    val descriptionTextContentHorizontalAlignment: Alignment.Horizontal,
    val descriptionTextContentVerticalArrangement: Arrangement.Vertical,
    val descriptionStyle: ODSTextStyle,
    val descriptionTextAlign: TextAlign,
    val descriptionTextStyle: ODSTextStyle,
    val descriptionTextTextAlign: TextAlign,
    val rightCondensedWidth: Dp,
    val rightCondensedHeight: Dp,
    val iconContainerWidthVariantIcon: Dp,
    val iconContainerHeightVariantIcon: Dp,
    val iconContainerClipContentVariantIcon: Boolean,
    val iconContainerVerticalAlignmentVariantIcon: Alignment.Vertical,
    val iconContainerHorizontalAlignmentVariantIcon: Alignment.Horizontal,
    val iconContainerHorizontalArrangementVariantIcon: Arrangement.Horizontal,
    val iconWidthVariantIcon: Dp,
    val iconHeightVariantIcon: Dp,
    val imageCornerRadiusVariantImage: ODSCorners,
    val imageClipContentVariantImage: Boolean,
    val imageVerticalAlignmentVariantImage: Alignment.Vertical,
    val imageHorizontalAlignmentVariantImage: Alignment.Horizontal,
    val imageHorizontalArrangementVariantImage: Arrangement.Horizontal,
    val image2CornerRadiusVariantImage: ODSCorners,
    val image2WidthVariantImage: Dp,
    val image2HeightVariantImage: Dp,
    val image2ContentScaleVariantImage: ContentScale
)

val defaultODSListRowNavigationTokens = ODSListRowNavigationTokens(
    padding = ODSPadding(
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent7
    ),
    cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
    minHeight = DSVariables.sizingComponent14,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    contentGap = DSVariables.spacingComponent3,
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.Start,
    contentHorizontalArrangement = Arrangement.Start,
    textContentGap = DSVariables.spacingComponent3,
    textContentPadding = ODSPadding(
        top = DSVariables.spacingComponent4,
        bottom = DSVariables.spacingComponent4
    ),
    textContentVerticalAlignment = Alignment.CenterVertically,
    textContentHorizontalAlignment = Alignment.Start,
    textContentHorizontalArrangement = Arrangement.Start,
    labelTextContentGap = DSVariables.spacingComponent1,
    labelTextContentVerticalAlignment = Alignment.CenterVertically,
    labelTextContentHorizontalAlignment = Alignment.Start,
    labelTextContentVerticalArrangement = Arrangement.Center,
    labelStyle = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Left,
    labelTextStyle = DSTextStyles.bodySRegular,
    labelTextTextAlign = TextAlign.Left,
    descriptionTextContentGap = DSVariables.spacingComponent1,
    descriptionTextContentVerticalAlignment = Alignment.CenterVertically,
    descriptionTextContentHorizontalAlignment = Alignment.End,
    descriptionTextContentVerticalArrangement = Arrangement.Center,
    descriptionStyle = DSTextStyles.bodyMBold,
    descriptionTextAlign = TextAlign.Right,
    descriptionTextStyle = DSTextStyles.bodySRegular,
    descriptionTextTextAlign = TextAlign.Right,
    rightCondensedWidth = DSVariables.sizingComponent10,
    rightCondensedHeight = DSVariables.sizingComponent10,
    iconContainerWidthVariantIcon = DSVariables.sizingComponent13,
    iconContainerHeightVariantIcon = DSVariables.sizingComponent13,
    iconContainerClipContentVariantIcon = true,
    iconContainerVerticalAlignmentVariantIcon = Alignment.CenterVertically,
    iconContainerHorizontalAlignmentVariantIcon = Alignment.CenterHorizontally,
    iconContainerHorizontalArrangementVariantIcon = Arrangement.Center,
    iconWidthVariantIcon = DSVariables.sizingComponent10,
    iconHeightVariantIcon = DSVariables.sizingComponent10,
    imageCornerRadiusVariantImage = ODSCorners(all = DSVariables.radiusFull),
    imageClipContentVariantImage = true,
    imageVerticalAlignmentVariantImage = Alignment.CenterVertically,
    imageHorizontalAlignmentVariantImage = Alignment.CenterHorizontally,
    imageHorizontalArrangementVariantImage = Arrangement.Center,
    image2CornerRadiusVariantImage = ODSCorners(all = DSVariables.radiusFull),
    image2WidthVariantImage = DSVariables.sizingComponent13,
    image2HeightVariantImage = DSVariables.sizingComponent13,
    image2ContentScaleVariantImage = ContentScale.Crop
)

var DSListRowNavigationTokens: ODSListRowNavigationTokens = defaultODSListRowNavigationTokens
