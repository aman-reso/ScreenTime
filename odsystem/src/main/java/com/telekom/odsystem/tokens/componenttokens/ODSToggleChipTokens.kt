package com.telekom.odsystem.tokens.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSToggleChipTokens(
    val minHeight: Dp,
    val minWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val toggleChipGap: Dp,
    val toggleChipPadding: ODSPadding,
    val toggleChipPaddingShowImage: ODSPadding,
    val toggleChipCornerRadius: ODSCorners,
    val toggleChipBorder: Dp,
    val toggleChipMinHeight: Dp,
    val toggleChipMinWidth: Dp,
    val toggleChipVerticalAlignment: Alignment.Vertical,
    val toggleChipHorizontalAlignment: Alignment.Horizontal,
    val toggleChipHorizontalArrangement: Arrangement.Horizontal,
    val iconWidth: Dp,
    val iconHeight: Dp,
    val chipStyle: ODSTextStyle,
    val chipTextAlign: TextAlign,
    val chipOverflow: TextOverflow,
    val imageCornerRadiusShowImage: ODSCorners,
    val imageClipContentShowImage: Boolean,
    val imageVerticalAlignmentShowImage: Alignment.Vertical,
    val imageHorizontalAlignmentShowImage: Alignment.Horizontal,
    val imageHorizontalArrangementShowImage: Arrangement.Horizontal,
    val image2CornerRadiusShowImage: ODSCorners,
    val image2WidthShowImage: Dp,
    val image2HeightShowImage: Dp,
    val image2ContentScaleShowImage: ContentScale, // Custom type is being used
    val checkmarkWidthSelected: Dp,
    val checkmarkHeightSelected: Dp
)

val defaultODSToggleChipTokens = ODSToggleChipTokens(
    minHeight = DSVariables.spacingLayout2,
    minWidth = DSVariables.sizingComponent15,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    toggleChipGap = DSVariables.spacingComponent3,
    toggleChipPadding = ODSPadding(
        vertical = DSVariables.spacingComponent3,
        left = DSVariables.spacingComponent6,
        right = DSVariables.spacingComponent6
    ),
    toggleChipPaddingShowImage = ODSPadding(
        left = DSVariables.spacingComponent2,
        right = DSVariables.spacingComponent6
    ),
    toggleChipCornerRadius = ODSCorners(all = DSVariables.radiusSmall),
    toggleChipBorder = DSVariables.strokes1,
    toggleChipMinHeight = DSVariables.spacingLayout2,
    toggleChipMinWidth = DSVariables.sizingComponent15,
    toggleChipVerticalAlignment = Alignment.CenterVertically,
    toggleChipHorizontalAlignment = Alignment.CenterHorizontally,
    toggleChipHorizontalArrangement = Arrangement.Center,
    iconWidth = DSVariables.sizingComponent7,
    iconHeight = DSVariables.sizingComponent7,
    chipStyle = DSTextStyles.microcopyBold,
    chipTextAlign = TextAlign.Left,
    chipOverflow = TextOverflow.Ellipsis,
    imageCornerRadiusShowImage = ODSCorners(all = DSVariables.radiusSmall),
    imageClipContentShowImage = true,
    imageVerticalAlignmentShowImage = Alignment.CenterVertically,
    imageHorizontalAlignmentShowImage = Alignment.CenterHorizontally,
    imageHorizontalArrangementShowImage = Arrangement.Center,
    image2CornerRadiusShowImage = ODSCorners(all = DSVariables.radiusSmall),
    image2WidthShowImage = DSVariables.sizingComponent12,
    image2HeightShowImage = DSVariables.sizingComponent12,
    image2ContentScaleShowImage = ContentScale.Crop,
    checkmarkWidthSelected = DSVariables.sizingComponent7,
    checkmarkHeightSelected = DSVariables.sizingComponent7
)

var DSToggleChipTokens: ODSToggleChipTokens = defaultODSToggleChipTokens
