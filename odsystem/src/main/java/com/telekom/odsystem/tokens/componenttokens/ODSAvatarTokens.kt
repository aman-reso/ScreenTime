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
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSAvatarTokens(
    val paddingVariantIcon: ODSPadding,
    val paddingVariantInitials: ODSPadding,
    val borderRadius: ODSCorners,
    val minHeightVariantInitialsSizeLarge: Dp,
    val minHeightVariantIconSizeLarge: Dp,
    val minHeightVariantInitialsSizeMedium: Dp,
    val minHeightVariantIconSizeMedium: Dp,
    val minHeightVariantInitialsSizeSmall: Dp,
    val minHeightVariantIconSizeSmall: Dp,
    val minWidthVariantInitialsSizeLarge: Dp,
    val minWidthVariantIconSizeLarge: Dp,
    val minWidthVariantInitialsSizeMedium: Dp,
    val minWidthVariantIconSizeMedium: Dp,
    val minWidthVariantInitialsSizeSmall: Dp,
    val minWidthVariantIconSizeSmall: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val widthVariantAvatarSizeLarge: Dp,
    val widthVariantAvatarSizeMedium: Dp,
    val widthVariantAvatarSizeSmall: Dp,
    val heightVariantAvatarSizeLarge: Dp,
    val heightVariantAvatarSizeMedium: Dp,
    val heightVariantAvatarSizeSmall: Dp,
    val contentAlignmentVariantAvatarSizeLargeBadgeTypeNumber: Alignment,
    val iconWidthVariantIconSizeLarge: Dp,
    val iconWidthVariantIconSizeMedium: Dp,
    val iconWidthVariantIconSizeSmall: Dp,
    val iconHeightVariantIconSizeLarge: Dp,
    val iconHeightVariantIconSizeMedium: Dp,
    val iconHeightVariantIconSizeSmall: Dp,
    val odsBadgeNumberContentAlignmentBadgeTypeNumber: Alignment,
    val odsBadgeNumberOffsetSizeLargeBadgeTypeNumber: ODSOffset,
    val odsBadgeNumberOffsetSizeMediumBadgeTypeNumber: ODSOffset,
    val odsBadgeNumberOffsetSizeSmallBadgeTypeNumber: ODSOffset,
    val odsBadgeIconContentAlignmentBadgeTypeIcon: Alignment,
    val odsBadgeIconOffsetSizeLargeBadgeTypeIcon: ODSOffset,
    val odsBadgeIconOffsetSizeMediumBadgeTypeIcon: ODSOffset,
    val odsBadgeIconOffsetSizeSmallBadgeTypeIcon: ODSOffset,
    val initialsTextStyleVariantInitialsSizeLarge: ODSTextStyle,
    val initialsTextStyleVariantInitialsSizeMedium: ODSTextStyle,
    val initialsTextStyleVariantInitialsSizeSmall: ODSTextStyle,
    val initialsTextAlignVariantInitials: TextAlign,
    val imageBorderRadiusVariantAvatar: ODSCorners,
    val imageWidthVariantAvatarSizeLarge: Dp,
    val imageWidthVariantAvatarSizeMedium: Dp,
    val imageWidthVariantAvatarSizeSmall: Dp,
    val imageHeightVariantAvatarSizeLarge: Dp,
    val imageHeightVariantAvatarSizeMedium: Dp,
    val imageHeightVariantAvatarSizeSmall: Dp,
    val imageObjectFitVariantAvatar: ContentScale
)

val defaultODSAvatarTokens = ODSAvatarTokens(
    paddingVariantIcon = ODSPadding(all = DSVariables.spacingComponent1),
    paddingVariantInitials = ODSPadding(all = DSVariables.spacingComponent1),
    borderRadius = ODSCorners(all = DSVariables.radiusFull),
    minHeightVariantInitialsSizeLarge = DSVariables.sizingComponent14,
    minHeightVariantIconSizeLarge = DSVariables.sizingComponent14,
    minHeightVariantInitialsSizeMedium = DSVariables.sizingComponent13,
    minHeightVariantIconSizeMedium = DSVariables.sizingComponent13,
    minHeightVariantInitialsSizeSmall = DSVariables.sizingComponent10,
    minHeightVariantIconSizeSmall = DSVariables.sizingComponent10,
    minWidthVariantInitialsSizeLarge = DSVariables.sizingComponent14,
    minWidthVariantIconSizeLarge = DSVariables.sizingComponent14,
    minWidthVariantInitialsSizeMedium = DSVariables.sizingComponent13,
    minWidthVariantIconSizeMedium = DSVariables.sizingComponent13,
    minWidthVariantInitialsSizeSmall = DSVariables.sizingComponent10,
    minWidthVariantIconSizeSmall = DSVariables.sizingComponent10,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    widthVariantAvatarSizeLarge = DSVariables.sizingComponent14,
    widthVariantAvatarSizeMedium = DSVariables.sizingComponent13,
    widthVariantAvatarSizeSmall = DSVariables.sizingComponent10,
    heightVariantAvatarSizeLarge = DSVariables.sizingComponent14,
    heightVariantAvatarSizeMedium = DSVariables.sizingComponent13,
    heightVariantAvatarSizeSmall = DSVariables.sizingComponent10,
    contentAlignmentVariantAvatarSizeLargeBadgeTypeNumber = Alignment.Center,
    iconWidthVariantIconSizeLarge = DSVariables.sizingComponent12,
    iconWidthVariantIconSizeMedium = DSVariables.sizingComponent11,
    iconWidthVariantIconSizeSmall = DSVariables.sizingComponent7,
    iconHeightVariantIconSizeLarge = DSVariables.sizingComponent12,
    iconHeightVariantIconSizeMedium = DSVariables.sizingComponent11,
    iconHeightVariantIconSizeSmall = DSVariables.sizingComponent7,
    odsBadgeNumberContentAlignmentBadgeTypeNumber = Alignment.TopEnd,
    odsBadgeNumberOffsetSizeLargeBadgeTypeNumber = ODSOffset(x = 10.dp, y = -2.dp),
    odsBadgeNumberOffsetSizeMediumBadgeTypeNumber = ODSOffset(x = 4.dp, y = -2.dp),
    odsBadgeNumberOffsetSizeSmallBadgeTypeNumber = ODSOffset(x = 4.dp, y = 0.dp),
    odsBadgeIconContentAlignmentBadgeTypeIcon = Alignment.TopEnd,
    odsBadgeIconOffsetSizeLargeBadgeTypeIcon = ODSOffset(x = 10.dp, y = -2.dp),
    odsBadgeIconOffsetSizeMediumBadgeTypeIcon = ODSOffset(x = 4.dp, y = -2.dp),
    odsBadgeIconOffsetSizeSmallBadgeTypeIcon = ODSOffset(x = 4.dp, y = 0.dp),
    initialsTextStyleVariantInitialsSizeLarge = DSTextStyles.bodyL,
    initialsTextStyleVariantInitialsSizeMedium = DSTextStyles.bodyMBold,
    initialsTextStyleVariantInitialsSizeSmall = DSTextStyles.bodyMBold,
    initialsTextAlignVariantInitials = TextAlign.Center,
    imageBorderRadiusVariantAvatar = ODSCorners(all = DSVariables.radiusFull),
    imageWidthVariantAvatarSizeLarge = DSVariables.sizingComponent14,
    imageWidthVariantAvatarSizeMedium = DSVariables.sizingComponent13,
    imageWidthVariantAvatarSizeSmall = DSVariables.sizingComponent10,
    imageHeightVariantAvatarSizeLarge = DSVariables.sizingComponent14,
    imageHeightVariantAvatarSizeMedium = DSVariables.sizingComponent13,
    imageHeightVariantAvatarSizeSmall = DSVariables.sizingComponent10,
    imageObjectFitVariantAvatar = ContentScale.Crop
)

var DSAvatarTokens: ODSAvatarTokens = defaultODSAvatarTokens
