package com.telekom.odsystem.atoms.badgeicon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSBadgeIconTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSBadgeIconStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var borderRadius: ODSCorners? = null
    var width: Dp? = null
    var height: Dp? = null
    var clipContent: Boolean? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var border: Dp? = null
    var borderColor: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var checkmarkColor: HexColor? = null
    var checkmarkWidth: Dp? = null
    var checkmarkHeight: Dp? = null
    var errorColor: HexColor? = null
    var errorWidth: Dp? = null
    var errorHeight: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSBadgeIconProps
    ): ODSBadgeIconStyle {
        var style = ODSBadgeIconStyle()
        style.borderRadius = DSBadgeIconTokens.borderRadius
        style.clipContent = DSBadgeIconTokens.clipContent
        style.verticalAlignment = DSBadgeIconTokens.verticalAlignment
        style.horizontalAlignment = DSBadgeIconTokens.horizontalAlignment
        style.verticalArrangement = DSBadgeIconTokens.verticalArrangement
        if (props.mode == ODSBadgeIconMode.ERROR) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
            style.padding = DSBadgeIconTokens.paddingTypeError
        }
        if (props.size == ODSBadgeIconSize.LARGE) {
            style.width = DSBadgeIconTokens.widthSizeLarge
            style.height = DSBadgeIconTokens.heightSizeLarge
        }
        if (props.size == ODSBadgeIconSize.SMALL) {
            style.width = DSBadgeIconTokens.widthSizeSmall
            style.height = DSBadgeIconTokens.heightSizeSmall
            style.border = DSBadgeIconTokens.borderSizeSmall
            style.borderColor = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (props.mode == ODSBadgeIconMode.SUCCESS) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard))
        }
        if (props.size == ODSBadgeIconSize.STANDARD) {
            style.width = DSBadgeIconTokens.widthSizeStandard
            style.height = DSBadgeIconTokens.heightSizeStandard
        }
        if (props.mode == ODSBadgeIconMode.SUCCESS && props.size == ODSBadgeIconSize.LARGE) {
            style.checkmarkColor = scheme.basicTextOnAccentSecondary
            style.checkmarkWidth = DSBadgeIconTokens.checkmarkWidthTypeSuccessSizeLarge
            style.checkmarkHeight = DSBadgeIconTokens.checkmarkHeightTypeSuccessSizeLarge
        }
        if (props.mode == ODSBadgeIconMode.SUCCESS && props.size == ODSBadgeIconSize.STANDARD) {
            style.checkmarkColor = scheme.basicTextOnAccentSecondary
            style.checkmarkWidth = DSBadgeIconTokens.checkmarkWidthTypeSuccessSizeStandard
            style.checkmarkHeight = DSBadgeIconTokens.checkmarkHeightTypeSuccessSizeStandard
        }
        if (props.mode == ODSBadgeIconMode.ERROR && props.size == ODSBadgeIconSize.LARGE) {
            style.errorColor = scheme.basicTextOnAccentSecondary
            style.errorWidth = DSBadgeIconTokens.errorWidthTypeErrorSizeLarge
            style.errorHeight = DSBadgeIconTokens.errorHeightTypeErrorSizeLarge
        }
        if (props.mode == ODSBadgeIconMode.ERROR && props.size == ODSBadgeIconSize.STANDARD) {
            style.errorColor = scheme.basicTextOnAccentSecondary
            style.errorWidth = DSBadgeIconTokens.errorWidthTypeErrorSizeStandard
            style.errorHeight = DSBadgeIconTokens.errorHeightTypeErrorSizeStandard
        }
        return style
    }
}
