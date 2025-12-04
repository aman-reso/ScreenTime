package com.telekom.odsystem.atoms.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSAvatarTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSAvatarStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var width: Dp? = null
    var height: Dp? = null
    var contentAlignment: Alignment? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var odsBadgeNumberContentAlignment: Alignment? = null
    var odsBadgeNumberOffset: ODSOffset? = null
    var odsBadgeIconContentAlignment: Alignment? = null
    var odsBadgeIconOffset: ODSOffset? = null
    var initialsTextStyle: ODSTextStyle? = null
    var initialsColor: HexColor? = null
    var initialsTextAlign: TextAlign? = null
    var imageBorderRadius: ODSCorners? = null
    var imageWidth: Dp? = null
    var imageHeight: Dp? = null
    var imageObjectFit: ContentScale? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSAvatarProps
    ): ODSAvatarStyle {
        val style = ODSAvatarStyle()
        style.borderRadius = DSAvatarTokens.borderRadius
        style.verticalAlignment = DSAvatarTokens.verticalAlignment
        style.horizontalAlignment = DSAvatarTokens.horizontalAlignment
        style.horizontalArrangement = DSAvatarTokens.horizontalArrangement
        if (props.variant == ODSAvatarVariant.ICON) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
            style.padding = DSAvatarTokens.paddingVariantIcon
        }
        if (props.variant == ODSAvatarVariant.INITIALS) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
            style.padding = DSAvatarTokens.paddingVariantInitials
        }
        if (props.variant == ODSAvatarVariant.INITIALS && props.size == ODSAvatarSize.LARGE) {
            style.minHeight = DSAvatarTokens.minHeightVariantInitialsSizeLarge
            style.minWidth = DSAvatarTokens.minWidthVariantInitialsSizeLarge
        }
        if (props.variant == ODSAvatarVariant.ICON && props.size == ODSAvatarSize.LARGE) {
            style.minHeight = DSAvatarTokens.minHeightVariantIconSizeLarge
            style.minWidth = DSAvatarTokens.minWidthVariantIconSizeLarge
        }
        if (props.variant == ODSAvatarVariant.INITIALS && props.size == ODSAvatarSize.MEDIUM) {
            style.minHeight = DSAvatarTokens.minHeightVariantInitialsSizeMedium
            style.minWidth = DSAvatarTokens.minWidthVariantInitialsSizeMedium
        }
        if (props.variant == ODSAvatarVariant.ICON && props.size == ODSAvatarSize.MEDIUM) {
            style.minHeight = DSAvatarTokens.minHeightVariantIconSizeMedium
            style.minWidth = DSAvatarTokens.minWidthVariantIconSizeMedium
        }
        if (props.variant == ODSAvatarVariant.INITIALS && props.size == ODSAvatarSize.SMALL) {
            style.minHeight = DSAvatarTokens.minHeightVariantInitialsSizeSmall
            style.minWidth = DSAvatarTokens.minWidthVariantInitialsSizeSmall
        }
        if (props.variant == ODSAvatarVariant.ICON && props.size == ODSAvatarSize.SMALL) {
            style.minHeight = DSAvatarTokens.minHeightVariantIconSizeSmall
            style.minWidth = DSAvatarTokens.minWidthVariantIconSizeSmall
        }
        if (props.variant == ODSAvatarVariant.AVATAR && props.size == ODSAvatarSize.LARGE) {
            style.width = DSAvatarTokens.widthVariantAvatarSizeLarge
            style.height = DSAvatarTokens.heightVariantAvatarSizeLarge
        }
        if (props.variant == ODSAvatarVariant.AVATAR && props.size == ODSAvatarSize.MEDIUM) {
            style.width = DSAvatarTokens.widthVariantAvatarSizeMedium
            style.height = DSAvatarTokens.heightVariantAvatarSizeMedium
        }
        if (props.variant == ODSAvatarVariant.AVATAR && props.size == ODSAvatarSize.SMALL) {
            style.width = DSAvatarTokens.widthVariantAvatarSizeSmall
            style.height = DSAvatarTokens.heightVariantAvatarSizeSmall
        }
        if (props.variant == ODSAvatarVariant.AVATAR && props.size == ODSAvatarSize.LARGE && props.badgeType == ODSAvatarBadgeType.NUMBER) {
            style.contentAlignment =
                DSAvatarTokens.contentAlignmentVariantAvatarSizeLargeBadgeTypeNumber
        }
        if (props.variant == ODSAvatarVariant.ICON) {
            style.iconColor = scheme.basicText
        }
        if (props.variant == ODSAvatarVariant.ICON && props.size == ODSAvatarSize.LARGE) {
            style.iconWidth = DSAvatarTokens.iconWidthVariantIconSizeLarge
            style.iconHeight = DSAvatarTokens.iconHeightVariantIconSizeLarge
        }
        if (props.variant == ODSAvatarVariant.ICON && props.size == ODSAvatarSize.MEDIUM) {
            style.iconWidth = DSAvatarTokens.iconWidthVariantIconSizeMedium
            style.iconHeight = DSAvatarTokens.iconHeightVariantIconSizeMedium
        }
        if (props.variant == ODSAvatarVariant.ICON && props.size == ODSAvatarSize.SMALL) {
            style.iconWidth = DSAvatarTokens.iconWidthVariantIconSizeSmall
            style.iconHeight = DSAvatarTokens.iconHeightVariantIconSizeSmall
        }
        if (props.badgeType == ODSAvatarBadgeType.NUMBER) {
            style.odsBadgeNumberContentAlignment =
                DSAvatarTokens.odsBadgeNumberContentAlignmentBadgeTypeNumber
        }
        if (props.size == ODSAvatarSize.LARGE && props.badgeType == ODSAvatarBadgeType.NUMBER) {
            style.odsBadgeNumberOffset = DSAvatarTokens.odsBadgeNumberOffsetSizeLargeBadgeTypeNumber
        }
        if (props.size == ODSAvatarSize.MEDIUM && props.badgeType == ODSAvatarBadgeType.NUMBER) {
            style.odsBadgeNumberOffset =
                DSAvatarTokens.odsBadgeNumberOffsetSizeMediumBadgeTypeNumber
        }
        if (props.size == ODSAvatarSize.SMALL && props.badgeType == ODSAvatarBadgeType.NUMBER) {
            style.odsBadgeNumberOffset = DSAvatarTokens.odsBadgeNumberOffsetSizeSmallBadgeTypeNumber
        }
        if (props.badgeType == ODSAvatarBadgeType.ICON) {
            style.odsBadgeIconContentAlignment =
                DSAvatarTokens.odsBadgeIconContentAlignmentBadgeTypeIcon
        }
        if (props.size == ODSAvatarSize.LARGE && props.badgeType == ODSAvatarBadgeType.ICON) {
            style.odsBadgeIconOffset = DSAvatarTokens.odsBadgeIconOffsetSizeLargeBadgeTypeIcon
        }
        if (props.size == ODSAvatarSize.MEDIUM && props.badgeType == ODSAvatarBadgeType.ICON) {
            style.odsBadgeIconOffset = DSAvatarTokens.odsBadgeIconOffsetSizeMediumBadgeTypeIcon
        }
        if (props.size == ODSAvatarSize.SMALL && props.badgeType == ODSAvatarBadgeType.ICON) {
            style.odsBadgeIconOffset = DSAvatarTokens.odsBadgeIconOffsetSizeSmallBadgeTypeIcon
        }
        if (props.variant == ODSAvatarVariant.INITIALS) {
            style.initialsColor = scheme.basicText
            style.initialsTextAlign = DSAvatarTokens.initialsTextAlignVariantInitials
        }
        if (props.variant == ODSAvatarVariant.INITIALS && props.size == ODSAvatarSize.LARGE) {
            style.initialsTextStyle = DSAvatarTokens.initialsTextStyleVariantInitialsSizeLarge
        }
        if (props.variant == ODSAvatarVariant.INITIALS && props.size == ODSAvatarSize.MEDIUM) {
            style.initialsTextStyle = DSAvatarTokens.initialsTextStyleVariantInitialsSizeMedium
        }
        if (props.variant == ODSAvatarVariant.INITIALS && props.size == ODSAvatarSize.SMALL) {
            style.initialsTextStyle = DSAvatarTokens.initialsTextStyleVariantInitialsSizeSmall
        }
        if (props.variant == ODSAvatarVariant.AVATAR) {
            style.imageBorderRadius = DSAvatarTokens.imageBorderRadiusVariantAvatar
            style.imageObjectFit = DSAvatarTokens.imageObjectFitVariantAvatar
        }
        if (props.variant == ODSAvatarVariant.AVATAR && props.size == ODSAvatarSize.LARGE) {
            style.imageWidth = DSAvatarTokens.imageWidthVariantAvatarSizeLarge
            style.imageHeight = DSAvatarTokens.imageHeightVariantAvatarSizeLarge
        }
        if (props.variant == ODSAvatarVariant.AVATAR && props.size == ODSAvatarSize.MEDIUM) {
            style.imageWidth = DSAvatarTokens.imageWidthVariantAvatarSizeMedium
            style.imageHeight = DSAvatarTokens.imageHeightVariantAvatarSizeMedium
        }
        if (props.variant == ODSAvatarVariant.AVATAR && props.size == ODSAvatarSize.SMALL) {
            style.imageWidth = DSAvatarTokens.imageWidthVariantAvatarSizeSmall
            style.imageHeight = DSAvatarTokens.imageHeightVariantAvatarSizeSmall
        }
        return style
    }
}
