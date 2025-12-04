package com.telekom.odsystem.atoms.listitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSListItemTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSListItemStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var markerWidth: Dp? = null
    var markerHeight: Dp? = null
    var markerVerticalAlignment: Alignment.Vertical? = null
    var markerHorizontalAlignment: Alignment.Horizontal? = null
    var markerHorizontalArrangement: Arrangement.Horizontal? = null
    var markerMinWidth: Dp? = null
    var innerCircleBackgroundColor: List<ODSColorModel>? = null
    var innerCircleBorderRadius: ODSCorners? = null
    var innerCircleWidth: Dp? = null
    var innerCircleHeight: Dp? = null
    var innerCircleBorder: Dp? = null
    var innerCircleBorderColor: List<ODSColorModel>? = null
    var marker2TextStyle: ODSTextStyle? = null
    var marker2Color: HexColor? = null
    var marker2TextAlign: TextAlign? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var labelTextDecoration: TextDecoration? = null // Not used in mobile, replaced with underlineThickness
    var underlineThickness: Dp? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSListItemProps
    ): ODSListItemStyle {
        val style = ODSListItemStyle()
        style.gap = DSListItemTokens.gap
        style.padding = DSListItemTokens.padding
        style.minHeight = DSListItemTokens.minHeight
        style.verticalAlignment = DSListItemTokens.verticalAlignment
        style.horizontalAlignment = DSListItemTokens.horizontalAlignment
        style.horizontalArrangement = DSListItemTokens.horizontalArrangement
        style.markerVerticalAlignment = DSListItemTokens.markerVerticalAlignment
        if (props.variant == ODSListItemVariant.ICON) {
            style.markerWidth = DSListItemTokens.markerWidthPrefixIcon
            style.markerHeight = DSListItemTokens.markerHeightPrefixIcon
            style.markerHorizontalAlignment = DSListItemTokens.markerHorizontalAlignmentPrefixIcon
            style.markerHorizontalArrangement =
                DSListItemTokens.markerHorizontalArrangementPrefixIcon
        }
        if (props.variant == ODSListItemVariant.NUMBER) {
            style.markerHorizontalAlignment = DSListItemTokens.markerHorizontalAlignmentPrefixNumber
            style.markerHorizontalArrangement =
                DSListItemTokens.markerHorizontalArrangementPrefixNumber
            style.markerMinWidth = DSListItemTokens.markerMinWidthPrefixNumber
        }
        if (props.variant == ODSListItemVariant.BULLETPOINT) {
            style.markerWidth = DSListItemTokens.markerWidthPrefixBulletpoint
            style.markerHeight = DSListItemTokens.markerHeightPrefixBulletpoint
            style.markerHorizontalAlignment =
                DSListItemTokens.markerHorizontalAlignmentPrefixBulletpoint
            style.markerHorizontalArrangement =
                DSListItemTokens.markerHorizontalArrangementPrefixBulletpoint
        }
        if (props.variant == ODSListItemVariant.OUTLINE_BULLET) {
            style.markerWidth = DSListItemTokens.markerWidthPrefixOutlineBullet
            style.markerHeight = DSListItemTokens.markerHeightPrefixOutlineBullet
            style.markerHorizontalAlignment =
                DSListItemTokens.markerHorizontalAlignmentPrefixOutlineBullet
            style.markerHorizontalArrangement =
                DSListItemTokens.markerHorizontalArrangementPrefixOutlineBullet
        }
        if (props.variant == ODSListItemVariant.BULLETPOINT) {
            style.innerCircleBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
            style.innerCircleBorderRadius =
                DSListItemTokens.innerCircleBorderRadiusPrefixBulletpoint
            style.innerCircleWidth = DSListItemTokens.innerCircleWidthPrefixBulletpoint
            style.innerCircleHeight = DSListItemTokens.innerCircleHeightPrefixBulletpoint
        }
        if (props.variant == ODSListItemVariant.OUTLINE_BULLET) {
            style.innerCircleBorderRadius =
                DSListItemTokens.innerCircleBorderRadiusPrefixOutlineBullet
            style.innerCircleWidth = DSListItemTokens.innerCircleWidthPrefixOutlineBullet
            style.innerCircleHeight = DSListItemTokens.innerCircleHeightPrefixOutlineBullet
            style.innerCircleBorder = DSListItemTokens.innerCircleBorderPrefixOutlineBullet
            style.innerCircleBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.variant == ODSListItemVariant.NUMBER) {
            style.marker2TextStyle = DSListItemTokens.marker2TextStylePrefixNumber
            style.marker2Color = scheme.basicText
            style.marker2TextAlign = DSListItemTokens.marker2TextAlignPrefixNumber
        }
        if (props.variant == ODSListItemVariant.ICON) {
            style.iconColor = scheme.basicText
            style.iconWidth = DSListItemTokens.iconWidthPrefixIcon
            style.iconHeight = DSListItemTokens.iconHeightPrefixIcon
        }
        style.labelColor = scheme.basicText
        style.labelTextAlign = DSListItemTokens.labelTextAlign
        if (props.link) {
            style.labelTextStyle = DSListItemTokens.labelTextStyleLink
            style.labelTextDecoration = DSListItemTokens.labelTextDecorationLink
        }
        if (!props.link) {
            style.labelTextStyle = DSListItemTokens.labelTextStyle
        }

        // Custom addition
        style.underlineThickness = DSListItemTokens.underlineThickness
        return style
    }
}
