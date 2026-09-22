package com.telekom.odsystem.atoms.badgenumber

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSBadgeNumberTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSBadgeNumberStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var maxWidth: Dp? = null
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var clipContent: Boolean? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var border: Dp? = null
    var borderColor: List<ODSColorModel>? = null
    var maxHeight: Dp? = null
    var digitsTextStyle: ODSTextStyle? = null
    var digitsColor: HexColor? = null
    var digitsTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSBadgeNumberProps
    ): ODSBadgeNumberStyle {
        var style = ODSBadgeNumberStyle()
        style.borderRadius = DSBadgeNumberTokens.borderRadius
        style.clipContent = DSBadgeNumberTokens.clipContent
        style.verticalAlignment = DSBadgeNumberTokens.verticalAlignment
        style.horizontalAlignment = DSBadgeNumberTokens.horizontalAlignment
        style.verticalArrangement = DSBadgeNumberTokens.verticalArrangement
        if (props.size == ODSBadgeNumberSize.LARGE) {
            style.padding = DSBadgeNumberTokens.paddingSizeLarge
            style.maxWidth = DSBadgeNumberTokens.maxWidthSizeLarge
            style.minHeight = DSBadgeNumberTokens.minHeightSizeLarge
            style.minWidth = DSBadgeNumberTokens.minWidthSizeLarge
        }
        if (props.size == ODSBadgeNumberSize.SMALL) {
            style.maxWidth = DSBadgeNumberTokens.maxWidthSizeSmall
            style.minHeight = DSBadgeNumberTokens.minHeightSizeSmall
            style.minWidth = DSBadgeNumberTokens.minWidthSizeSmall
            style.border = DSBadgeNumberTokens.borderSizeSmall
            style.borderColor = listOf(ODSColorModel(hexColor = scheme.basicBackground))
            style.maxHeight = DSBadgeNumberTokens.maxHeightSizeSmall
        }
        if (props.variant == ODSBadgeNumberVariant.NEUTRAL) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        }
        if (props.size == ODSBadgeNumberSize.STANDARD) {
            style.padding = DSBadgeNumberTokens.paddingSizeStandard
            style.maxWidth = DSBadgeNumberTokens.maxWidthSizeStandard
            style.minHeight = DSBadgeNumberTokens.minHeightSizeStandard
            style.minWidth = DSBadgeNumberTokens.minWidthSizeStandard
        }
        if (props.variant == ODSBadgeNumberVariant.NOTIFICATION) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        }
        if (props.size == ODSBadgeNumberSize.LARGE) {
            style.digitsTextStyle = DSBadgeNumberTokens.digitsTextStyleSizeLarge
            style.digitsTextAlign = DSBadgeNumberTokens.digitsTextAlignSizeLarge
        }
        if (props.size == ODSBadgeNumberSize.STANDARD) {
            style.digitsTextStyle = DSBadgeNumberTokens.digitsTextStyleSizeStandard
            style.digitsTextAlign = DSBadgeNumberTokens.digitsTextAlignSizeStandard
        }
        if (props.variant == ODSBadgeNumberVariant.NOTIFICATION && props.size == ODSBadgeNumberSize.STANDARD) {
            style.digitsColor = scheme.basicTextOnAccent
        }
        if (props.variant == ODSBadgeNumberVariant.NOTIFICATION && props.size == ODSBadgeNumberSize.LARGE) {
            style.digitsColor = scheme.basicTextOnAccent
        }
        if (props.variant == ODSBadgeNumberVariant.NEUTRAL && props.size == ODSBadgeNumberSize.STANDARD) {
            style.digitsColor = scheme.basicTextOnAccentSecondary
        }
        if (props.variant == ODSBadgeNumberVariant.NEUTRAL && props.size == ODSBadgeNumberSize.LARGE) {
            style.digitsColor = scheme.basicTextOnAccentSecondary
        }
        return style
    }
}
