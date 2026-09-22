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
 * 2025-07-31 (v1.32.3) - uid: a6fe42f
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-17463
 */

data class ODSListRowControlsTokens(
    val gap: Dp,
    val padding: ODSPadding,
    val cornerRadius: ODSCorners,
    val minHeight: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val textContentControlGap: Dp,
    val textContentControlPadding: ODSPadding, // Added to match Base
    val textContentControlVerticalAlignment: Alignment.Vertical,
    val textContentControlHorizontalAlignment: Alignment.Horizontal,
    val textContentControlHorizontalArrangement: Arrangement.Horizontal,
    val labelTextContentGap: Dp,
    val labelTextContentPadding: ODSPadding,
    val labelTextContentVerticalAlignment: Alignment.Vertical,
    val labelTextContentHorizontalAlignment: Alignment.Horizontal,
    val labelTextContentVerticalArrangement: Arrangement.Vertical,
    val labelStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val labelTextStyle: ODSTextStyle,
    val labelTextTextAlign: TextAlign,
    val imageCornerRadiusVariantImage: ODSCorners,
    val imageClipContentVariantImage: Boolean,
    val imageVerticalAlignmentVariantImage: Alignment.Vertical,
    val imageHorizontalAlignmentVariantImage: Alignment.Horizontal,
    val imageHorizontalArrangementVariantImage: Arrangement.Horizontal,
    val imageOpacityVariantImageDisabled: Float,
    val image2CornerRadiusVariantImage: ODSCorners,
    val image2WidthVariantImage: Dp,
    val image2HeightVariantImage: Dp,
    val image2ContentScaleVariantImage: ContentScale,
    val iconContainerPaddingVariantIcon: ODSPadding,
    val iconContainerWidthVariantIcon: Dp,
    val iconContainerHeightVariantIcon: Dp,
    val iconContainerVerticalAlignmentVariantIcon: Alignment.Vertical,
    val iconContainerHorizontalAlignmentVariantIcon: Alignment.Horizontal,
    val iconContainerHorizontalArrangementVariantIcon: Arrangement.Horizontal,
    val iconContainerOpacityVariantIconDisabled: Float,
    val iconWidthVariantIcon: Dp,
    val iconHeightVariantIcon: Dp
)

val defaultODSListRowControlsTokens = ODSListRowControlsTokens(
    gap = DSVariables.spacingComponent3,
    padding = ODSPadding(
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent6
    ),
    cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
    minHeight = DSVariables.sizingComponent14,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    textContentControlGap = DSVariables.spacingComponent3,
    textContentControlPadding = ODSPadding(all = 0.dp),
    textContentControlVerticalAlignment = Alignment.CenterVertically,
    textContentControlHorizontalAlignment = Alignment.Start,
    textContentControlHorizontalArrangement = Arrangement.Start,
    labelTextContentGap = DSVariables.spacingComponent1,
    labelTextContentPadding = ODSPadding(
        top = DSVariables.spacingComponent4,
        bottom = DSVariables.spacingComponent4
    ),
    labelTextContentVerticalAlignment = Alignment.CenterVertically,
    labelTextContentHorizontalAlignment = Alignment.Start,
    labelTextContentVerticalArrangement = Arrangement.Center,
    labelStyle = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Left,
    labelTextStyle = DSTextStyles.bodySRegular,
    labelTextTextAlign = TextAlign.Left,
    imageCornerRadiusVariantImage = ODSCorners(all = DSVariables.radiusFull),
    imageClipContentVariantImage = true,
    imageVerticalAlignmentVariantImage = Alignment.CenterVertically,
    imageHorizontalAlignmentVariantImage = Alignment.CenterHorizontally,
    imageHorizontalArrangementVariantImage = Arrangement.Center,
    imageOpacityVariantImageDisabled = 0.50f,
    image2CornerRadiusVariantImage = ODSCorners(all = DSVariables.radiusFull),
    image2WidthVariantImage = DSVariables.sizingComponent13,
    image2HeightVariantImage = DSVariables.sizingComponent13,
    image2ContentScaleVariantImage = ContentScale.Crop,
    iconContainerPaddingVariantIcon = ODSPadding(all = DSVariables.spacingComponent3),
    iconContainerWidthVariantIcon = DSVariables.sizingComponent13,
    iconContainerHeightVariantIcon = DSVariables.sizingComponent13,
    iconContainerVerticalAlignmentVariantIcon = Alignment.CenterVertically,
    iconContainerHorizontalAlignmentVariantIcon = Alignment.Start,
    iconContainerHorizontalArrangementVariantIcon = Arrangement.Start,
    iconContainerOpacityVariantIconDisabled = 0.50f,
    iconWidthVariantIcon = DSVariables.sizingComponent10,
    iconHeightVariantIcon = DSVariables.sizingComponent10
)

var DSListRowControlsTokens: ODSListRowControlsTokens = defaultODSListRowControlsTokens
