package com.telekom.odsystem.organisms.cardquickaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSCardQuickActionStyle {

    var width: Dp? = null // Not used in mobile
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var height: Dp? = null // Not used in mobile
    var containerZStackContentAlignment: Alignment? = null
    var containerGap: Dp? = null
    var containerPadding: ODSPadding? = null
    var containerVerticalAlignment: Alignment.Vertical? = null
    var containerHorizontalAlignment: Alignment.Horizontal? = null
    var containerVerticalArrangement: Arrangement.Vertical? = null
    var containerContentAlignment: Alignment? = null
    var cardBgBackground: List<ODSColorModel>? = null
    var cardBgCornerRadius: ODSCorners? = null
    var cardBgVerticalAlignment: Alignment.Vertical? = null
    var cardBgHorizontalAlignment: Alignment.Horizontal? = null
    var cardBgVerticalArrangement: Arrangement.Vertical? = null
    var cardBgBorder: Dp? = null
    var cardBgBorderColor: List<ODSColorModel>? = null
    var contentGap: Dp? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var contentContainerVerticalAlignment: Alignment.Vertical? = null
    var contentContainerHorizontalAlignment: Alignment.Horizontal? = null
    var contentContainerVerticalArrangement: Arrangement.Vertical? = null
    var selectorContainerRightVerticalAlignment: Alignment.Vertical? = null
    var selectorContainerRightHorizontalAlignment: Alignment.Horizontal? = null
    var selectorContainerRightHorizontalArrangement: Arrangement.Horizontal? = null
    var selectorContainerRightPadding: ODSPadding? = null
    var arrowRightColor: HexColor? = null
    var arrowRightWidth: Dp? = null
    var arrowRightHeight: Dp? = null
    var scaleFactor: Float? = null // Not exported by plugin.

    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardQuickActionProps
    ): ODSCardQuickActionStyle {
        val style = ODSCardQuickActionStyle()
        style.width = DSCardQuickActionTokens.width
        style.verticalAlignment = DSCardQuickActionTokens.verticalAlignment
        style.horizontalAlignment = DSCardQuickActionTokens.horizontalAlignment
        style.verticalArrangement = DSCardQuickActionTokens.verticalArrangement
        /*if (props.size == ODSCardQuickActionSize.MEDIUM && props.customHeight) {
            style.height = DSCardQuickActionTokens.heightSizeMediumCustomHeight
        }
        if (props.size == ODSCardQuickActionSize.SMALL && props.customHeight) {
            style.height = DSCardQuickActionTokens.heightSizeSmallCustomHeight
        }*/
        style.containerZStackContentAlignment =
            DSCardQuickActionTokens.containerZStackContentAlignment
        style.containerGap = DSCardQuickActionTokens.containerGap
        style.containerVerticalAlignment = DSCardQuickActionTokens.containerVerticalAlignment
        style.containerHorizontalAlignment = DSCardQuickActionTokens.containerHorizontalAlignment
        style.containerVerticalArrangement = DSCardQuickActionTokens.containerVerticalArrangement
        style.containerContentAlignment = DSCardQuickActionTokens.containerContentAlignment
        if (props.size == ODSCardQuickActionSize.SMALL) {
            style.containerPadding = DSCardQuickActionTokens.containerPaddingSizeSmall
        }
        if (props.size == ODSCardQuickActionSize.MEDIUM) {
            style.containerPadding = DSCardQuickActionTokens.containerPaddingSizeMedium
        }
        style.cardBgVerticalAlignment = DSCardQuickActionTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardQuickActionTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardQuickActionTokens.cardBgVerticalArrangement
        if (props.size == ODSCardQuickActionSize.SMALL) {
            style.cardBgCornerRadius = DSCardQuickActionTokens.cardBgCornerRadiusSizeSmall
        }
        if (props.size == ODSCardQuickActionSize.MEDIUM) {
            style.cardBgCornerRadius = DSCardQuickActionTokens.cardBgCornerRadiusSizeMedium
        }
        if (props.filled && props.disabled) {
            style.cardBgBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundCardDisabled))
        }
        if (!props.filled && !props.subtle) {
            style.cardBgBorder = DSCardQuickActionTokens.cardBgBorder
        }
        if (props.filled && !props.subtle && !props.disabled) {
            style.cardBgBackground = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        }
        if (props.filled && props.subtle && !props.disabled) {
            style.cardBgBackground =
                listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle))
        }
        if (!props.filled && !props.subtle && !props.disabled) {
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        }
        if (!props.filled && !props.subtle && props.disabled) {
            style.cardBgBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeSubtleDisabled))
        }
        style.contentGap = DSCardQuickActionTokens.contentGap
        style.contentVerticalAlignment = DSCardQuickActionTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSCardQuickActionTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSCardQuickActionTokens.contentHorizontalArrangement
        style.contentContainerVerticalAlignment =
            DSCardQuickActionTokens.contentContainerVerticalAlignment
        style.contentContainerHorizontalAlignment =
            DSCardQuickActionTokens.contentContainerHorizontalAlignment
        style.contentContainerVerticalArrangement =
            DSCardQuickActionTokens.contentContainerVerticalArrangement
        style.selectorContainerRightVerticalAlignment =
            DSCardQuickActionTokens.selectorContainerRightVerticalAlignment
        style.selectorContainerRightHorizontalAlignment =
            DSCardQuickActionTokens.selectorContainerRightHorizontalAlignment
        style.selectorContainerRightHorizontalArrangement =
            DSCardQuickActionTokens.selectorContainerRightHorizontalArrangement
        if (props.size == ODSCardQuickActionSize.SMALL) {
            style.selectorContainerRightPadding =
                DSCardQuickActionTokens.selectorContainerRightPaddingSizeSmall
        }
        style.arrowRightColor = scheme.basicText
        style.arrowRightWidth = DSCardQuickActionTokens.arrowRightWidth
        style.arrowRightHeight = DSCardQuickActionTokens.arrowRightHeight
        style.scaleFactor = DSCardQuickActionTokens.scaleFactor
        return style
    }
}
