package com.telekom.odsystem.componenttokens

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

/**
 * Created by dmarinopoulos on 15/4/24
 */

data class ODSDismissibleChipTokens(
    val minHeight: Dp,
    val minWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val dismissibleChipGap: Dp,
    val dismissibleChipPaddingTypeWithIcon: ODSPadding,
    val dismissibleChipPaddingTypeStandard: ODSPadding,
    val dismissibleChipPaddingTypeWithPicture: ODSPadding,
    val dismissibleChipBorderRadius: ODSCorners,
    val dismissibleChipMinHeight: Dp,
    val dismissibleChipMinWidth: Dp,
    val dismissibleChipVerticalAlignment: Alignment.Vertical,
    val dismissibleChipHorizontalAlignment: Alignment.Horizontal,
    val dismissibleChipHorizontalArrangement: Arrangement.Horizontal,
    val iconWidthTypeWithIcon: Dp,
    val iconWidthTypeStandard: Dp,
    val iconHeightTypeWithIcon: Dp,
    val iconHeightTypeStandard: Dp,
    val chipTextStyle: ODSTextStyle,
    val chipTextAlign: TextAlign,
    val chipTextOverflow: TextOverflow,
    val actionBorderRadius: ODSCorners,
    val actionVerticalAlignment: Alignment.Vertical,
    val actionHorizontalAlignment: Alignment.Horizontal,
    val actionHorizontalArrangement: Arrangement.Horizontal,
    val closeWidth: Dp,
    val closeHeight: Dp,
    val imageBorderRadiusTypeWithPicture: ODSCorners,
    val imageClipContentTypeWithPicture: Boolean,
    val imageVerticalAlignmentTypeWithPicture: Alignment.Vertical,
    val imageHorizontalAlignmentTypeWithPicture: Alignment.Horizontal,
    val imageHorizontalArrangementTypeWithPicture: Arrangement.Horizontal,
    val image2BorderRadiusTypeWithPicture: ODSCorners,
    val image2WidthTypeWithPicture: Dp,
    val image2HeightTypeWithPicture: Dp,
    val image2ObjectFitTypeWithPicture: ContentScale
)

val defaultODSDismissibleChipTokens = ODSDismissibleChipTokens(
    minHeight = DSVariables.sizingMinimumTappableArea,
    minWidth = DSVariables.sizingComponent15,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    dismissibleChipGap = DSVariables.spacingComponent3,
    dismissibleChipPaddingTypeWithIcon = ODSPadding(
        left = DSVariables.spacingComponent6,
        right = DSVariables.spacingComponent6
    ),
    dismissibleChipPaddingTypeStandard = ODSPadding(
        left = DSVariables.spacingComponent6,
        right = DSVariables.spacingComponent6
    ),
    dismissibleChipPaddingTypeWithPicture = ODSPadding(
        left = DSVariables.spacingComponent2,
        right = DSVariables.spacingComponent6
    ),
    dismissibleChipBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    dismissibleChipMinHeight = DSVariables.sizingComponent13,
    dismissibleChipMinWidth = DSVariables.sizingComponent15,
    dismissibleChipVerticalAlignment = Alignment.CenterVertically,
    dismissibleChipHorizontalAlignment = Alignment.CenterHorizontally,
    dismissibleChipHorizontalArrangement = Arrangement.Center,
    iconWidthTypeWithIcon = DSVariables.sizingComponent7,
    iconWidthTypeStandard = DSVariables.sizingComponent7,
    iconHeightTypeWithIcon = DSVariables.sizingComponent7,
    iconHeightTypeStandard = DSVariables.sizingComponent7,
    chipTextStyle = DSTextStyles.bodyMBold,
    chipTextAlign = TextAlign.Left,
    chipTextOverflow = TextOverflow.Ellipsis,
    actionBorderRadius = ODSCorners(all = DSVariables.radiusExtraSmall),
    actionVerticalAlignment = Alignment.CenterVertically,
    actionHorizontalAlignment = Alignment.CenterHorizontally,
    actionHorizontalArrangement = Arrangement.Center,
    closeWidth = DSVariables.sizingComponent7,
    closeHeight = DSVariables.sizingComponent7,
    imageBorderRadiusTypeWithPicture = ODSCorners(all = DSVariables.radiusFull),
    imageClipContentTypeWithPicture = true,
    imageVerticalAlignmentTypeWithPicture = Alignment.CenterVertically,
    imageHorizontalAlignmentTypeWithPicture = Alignment.CenterHorizontally,
    imageHorizontalArrangementTypeWithPicture = Arrangement.Center,
    image2BorderRadiusTypeWithPicture = ODSCorners(all = DSVariables.radiusFull),
    image2WidthTypeWithPicture = DSVariables.sizingComponent12,
    image2HeightTypeWithPicture = DSVariables.sizingComponent12,
    image2ObjectFitTypeWithPicture = ContentScale.Crop
)

var DSDismissibleChipTokens: ODSDismissibleChipTokens = defaultODSDismissibleChipTokens
