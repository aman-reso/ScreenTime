package com.telekom.odsystem.organisms.cardswitch

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
class ODSCardSwitchStyle {
    var borderRadius: ODSCorners? = null
    var boxShadow: ODSEffect? = null
    var width: Dp? = null // Not used in mobile
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
    var cardBgWidth: Dp? = null // Not used in mobile
    var cardBgHeight: Dp? = null // Not used in mobile
    var cardBgClipContent: Boolean? = null
    var cardBgVerticalAlignment: Alignment.Vertical? = null
    var cardBgHorizontalAlignment: Alignment.Horizontal? = null
    var cardBgVerticalArrangement: Arrangement.Vertical? = null
    var cardBgBorder: Dp? = null
    var cardBgBorderColor: List<ODSColorModel>? = null
    var copyGap: Dp? = null
    var copyVerticalAlignment: Alignment.Vertical? = null
    var copyHorizontalAlignment: Alignment.Horizontal? = null
    var copyVerticalArrangement: Arrangement.Vertical? = null
    var tagsContainerGap: Dp? = null
    var tagsContainerVerticalAlignment: Alignment.Vertical? = null
    var tagsContainerHorizontalAlignment: Alignment.Horizontal? = null
    var tagsContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var logoContainerHeight: Dp? = null
    var logoContainerClipContent: Boolean? = null
    var logoContainerVerticalAlignment: Alignment.Vertical? = null
    var logoContainerHorizontalAlignment: Alignment.Horizontal? = null
    var logoContainerVerticalArrangement: Arrangement.Vertical? = null
    var titleTextStyle: ODSTextStyle? = null
    var titleColor: HexColor? = null
    var titleTextAlign: TextAlign? = null
    var subtitleTextStyle: ODSTextStyle? = null
    var subtitleColor: HexColor? = null
    var subtitleTextAlign: TextAlign? = null
    var logoObjectFit: ContentScale? = null // Not exported from the plugin
    var scaleFactor: Float? = null // Not exported by plugin. Should be documented
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardSwitchProps,
        state: ODSActions
    ): ODSCardSwitchStyle {
        var style = ODSCardSwitchStyle()
        style.borderRadius = DSCardSwitchTokens.borderRadius
        style.boxShadow = scheme.elevationLevel0
        style.width = DSCardSwitchTokens.width
        style.verticalAlignment = DSCardSwitchTokens.verticalAlignment
        style.horizontalAlignment = DSCardSwitchTokens.horizontalAlignment
        style.horizontalArrangement = DSCardSwitchTokens.horizontalArrangement
        style.contentGap = DSCardSwitchTokens.contentGap
        style.contentPadding = DSCardSwitchTokens.contentPadding
        style.contentVerticalAlignment = DSCardSwitchTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSCardSwitchTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSCardSwitchTokens.contentHorizontalArrangement
        style.cardContentGap = DSCardSwitchTokens.cardContentGap
        style.cardContentVerticalAlignment = DSCardSwitchTokens.cardContentVerticalAlignment
        style.cardContentHorizontalAlignment = DSCardSwitchTokens.cardContentHorizontalAlignment
        style.cardContentVerticalArrangement = DSCardSwitchTokens.cardContentVerticalArrangement
        style.cardBgBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        style.cardBgBorderRadius = DSCardSwitchTokens.cardBgBorderRadius
        style.cardBgWidth = DSCardSwitchTokens.cardBgWidth
        style.cardBgHeight = DSCardSwitchTokens.cardBgHeight
        style.cardBgClipContent = DSCardSwitchTokens.cardBgClipContent
        style.cardBgVerticalAlignment = DSCardSwitchTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardSwitchTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardSwitchTokens.cardBgVerticalArrangement
        if (props.selected) {
            style.cardBgBorder = DSCardSwitchTokens.cardBgBorderSelected
        }
        if (state == ODSActions.PRESSED) {
            style.cardBgWidth = DSCardSwitchTokens.cardBgWidthStatePressed
            style.cardBgHeight = DSCardSwitchTokens.cardBgHeightStatePressed
        }
        if (props.variant == ODSCardSwitchVariant.TITLE && props.selected) {
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.variant == ODSCardSwitchVariant.BRAND && props.selected) {
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
        if (props.variant == ODSCardSwitchVariant.BRAND && props.selected && state == ODSActions.PRESSED) {
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
        style.copyGap = DSCardSwitchTokens.copyGap
        style.copyVerticalAlignment = DSCardSwitchTokens.copyVerticalAlignment
        style.copyHorizontalAlignment = DSCardSwitchTokens.copyHorizontalAlignment
        style.copyVerticalArrangement = DSCardSwitchTokens.copyVerticalArrangement
        style.tagsContainerGap = DSCardSwitchTokens.tagsContainerGap
        style.tagsContainerVerticalAlignment = DSCardSwitchTokens.tagsContainerVerticalAlignment
        style.tagsContainerHorizontalAlignment = DSCardSwitchTokens.tagsContainerHorizontalAlignment
        style.tagsContainerHorizontalArrangement =
            DSCardSwitchTokens.tagsContainerHorizontalArrangement
        if (props.variant == ODSCardSwitchVariant.BRAND) {
            style.logoObjectFit =
                DSCardSwitchTokens.logoObjectFitTypeBrand // Not exported from the plugin
            style.logoContainerHeight = DSCardSwitchTokens.logoContainerHeightTypeBrand
            style.logoContainerClipContent = DSCardSwitchTokens.logoContainerClipContentTypeBrand
            style.logoContainerVerticalAlignment =
                DSCardSwitchTokens.logoContainerVerticalAlignmentTypeBrand
            style.logoContainerHorizontalAlignment =
                DSCardSwitchTokens.logoContainerHorizontalAlignmentTypeBrand
            style.logoContainerVerticalArrangement =
                DSCardSwitchTokens.logoContainerVerticalArrangementTypeBrand
        }
        if (props.variant == ODSCardSwitchVariant.TITLE) {
            style.titleTextStyle = DSCardSwitchTokens.titleTextStyleTypeTitle
            style.titleColor = scheme.basicText
            style.titleTextAlign = DSCardSwitchTokens.titleTextAlignTypeTitle
        }
        style.subtitleTextStyle = DSCardSwitchTokens.subtitleTextStyle
        style.subtitleColor = scheme.basicText
        style.subtitleTextAlign = DSCardSwitchTokens.subtitleTextAlign
        style.scaleFactor = DSCardSwitchTokens.scaleFactor // Not exported from the plugin
        return style
    }
}
