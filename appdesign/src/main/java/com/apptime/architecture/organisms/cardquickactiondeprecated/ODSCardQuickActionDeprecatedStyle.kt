package com.telekom.odsystem.organisms.cardquickactiondeprecated

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSCardQuickActionDeprecatedStyle {
    var borderRadius: ODSCorners? = null
    var boxShadow: ODSEffect? = null
    var width: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var contentGap: Dp? = null
    var contentPadding: ODSPadding? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var cardContentGap: Dp? = null
    var cardContentVerticalAlignment: Alignment.Vertical? = null
    var cardContentHorizontalAlignment: Alignment.Horizontal? = null
    var cardContentVerticalArrangement: Arrangement.Vertical? = null
    var cardBgBackgroundColor: List<ODSColorModel>? = null
    var cardBgBorderRadius: ODSCorners? = null
    var cardBgWidth: Dp? = null
    var cardBgHeight: Dp? = null
    var cardBgVerticalAlignment: Alignment.Vertical? = null
    var cardBgHorizontalAlignment: Alignment.Horizontal? = null
    var cardBgVerticalArrangement: Arrangement.Vertical? = null
    var copyAndSparklineGap: Dp? = null
    var copyAndSparklineVerticalAlignment: Alignment.Vertical? = null
    var copyAndSparklineHorizontalAlignment: Alignment.Horizontal? = null
    var copyAndSparklineVerticalArrangement: Arrangement.Vertical? = null
    var tagsContainerGap: Dp? = null
    var tagsContainerVerticalAlignment: Alignment.Vertical? = null
    var tagsContainerHorizontalAlignment: Alignment.Horizontal? = null
    var tagsContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var arrowRightColor: HexColor? = null
    var arrowRightWidth: Dp? = null
    var arrowRightHeight: Dp? = null
    var logoContainerHeight: Dp? = null
    var logoContainerVerticalAlignment: Alignment.Vertical? = null
    var logoContainerHorizontalAlignment: Alignment.Horizontal? = null
    var logoContainerVerticalArrangement: Arrangement.Vertical? = null
    var logoObjectFit: ContentScale? = null // Not exported by plugin
    var titleTextStyle: ODSTextStyle? = null
    var titleColor: HexColor? = null
    var titleTextAlign: TextAlign? = null
    var subtitleTextStyle: ODSTextStyle? = null
    var subtitleColor: HexColor? = null
    var subtitleTextAlign: TextAlign? = null
    var scaleFactor: Float? = null // Not exported by plugin. Should be documented
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardQuickActionDeprecatedProps,
        state: ODSActions,
    ): ODSCardQuickActionDeprecatedStyle {
        var style = ODSCardQuickActionDeprecatedStyle()
        style.borderRadius = DSCardQuickActionTokens.borderRadius
        style.boxShadow = scheme.elevationLevel0
        style.width = DSCardQuickActionTokens.width
        style.verticalAlignment = DSCardQuickActionTokens.verticalAlignment
        style.horizontalAlignment = DSCardQuickActionTokens.horizontalAlignment
        style.horizontalArrangement = DSCardQuickActionTokens.horizontalArrangement
        style.contentGap = DSCardQuickActionTokens.contentGap
        style.contentPadding = DSCardQuickActionTokens.contentPadding
        style.contentVerticalAlignment = DSCardQuickActionTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSCardQuickActionTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSCardQuickActionTokens.contentHorizontalArrangement
        style.cardContentGap = DSCardQuickActionTokens.cardContentGap
        style.cardContentVerticalAlignment = DSCardQuickActionTokens.cardContentVerticalAlignment
        style.cardContentHorizontalAlignment =
            DSCardQuickActionTokens.cardContentHorizontalAlignment
        style.cardContentVerticalArrangement =
            DSCardQuickActionTokens.cardContentVerticalArrangement
        style.cardBgBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        style.cardBgBorderRadius = DSCardQuickActionTokens.cardBgBorderRadius
        style.cardBgWidth = DSCardQuickActionTokens.cardBgWidth
        style.cardBgHeight = DSCardQuickActionTokens.cardBgHeight
        style.cardBgVerticalAlignment = DSCardQuickActionTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardQuickActionTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardQuickActionTokens.cardBgVerticalArrangement
        if (state == ODSActions.PRESSED) {
            style.cardBgWidth = DSCardQuickActionTokens.cardBgWidthStatePressed
            style.cardBgHeight = DSCardQuickActionTokens.cardBgHeightStatePressed
        }
        style.copyAndSparklineGap = DSCardQuickActionTokens.copyAndSparklineGap
        style.copyAndSparklineVerticalAlignment =
            DSCardQuickActionTokens.copyAndSparklineVerticalAlignment
        style.copyAndSparklineHorizontalAlignment =
            DSCardQuickActionTokens.copyAndSparklineHorizontalAlignment
        style.copyAndSparklineVerticalArrangement =
            DSCardQuickActionTokens.copyAndSparklineVerticalArrangement
        style.tagsContainerGap = DSCardQuickActionTokens.tagsContainerGap
        style.tagsContainerVerticalAlignment =
            DSCardQuickActionTokens.tagsContainerVerticalAlignment
        style.tagsContainerHorizontalAlignment =
            DSCardQuickActionTokens.tagsContainerHorizontalAlignment
        style.tagsContainerHorizontalArrangement =
            DSCardQuickActionTokens.tagsContainerHorizontalArrangement
        style.arrowRightColor = scheme.basicText
        style.arrowRightWidth = DSCardQuickActionTokens.arrowRightWidth
        style.arrowRightHeight = DSCardQuickActionTokens.arrowRightHeight
        if (props.variant == ODSCardQuickActionDeprecatedVariant.BRAND) {
            style.logoObjectFit = DSCardQuickActionTokens.logoObjectFitTypeBrand
            style.logoContainerHeight = DSCardQuickActionTokens.logoContainerHeightTypeBrand
            style.logoContainerVerticalAlignment =
                DSCardQuickActionTokens.logoContainerVerticalAlignmentTypeBrand
            style.logoContainerHorizontalAlignment =
                DSCardQuickActionTokens.logoContainerHorizontalAlignmentTypeBrand
            style.logoContainerVerticalArrangement =
                DSCardQuickActionTokens.logoContainerVerticalArrangementTypeBrand
        }
        if (props.variant == ODSCardQuickActionDeprecatedVariant.TITLE) {
            style.titleTextStyle = DSCardQuickActionTokens.titleTextStyleTypeTitle
            style.titleColor = scheme.basicText
            style.titleTextAlign = DSCardQuickActionTokens.titleTextAlignTypeTitle
        }
        style.subtitleTextStyle = DSCardQuickActionTokens.subtitleTextStyle
        style.subtitleColor = scheme.basicText
        style.subtitleTextAlign = DSCardQuickActionTokens.subtitleTextAlign
        style.scaleFactor = DSCardQuickActionTokens.scaleFactor
        return style
    }
}
