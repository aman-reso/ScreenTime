package com.telekom.odsystem.organisms.cardimage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.tokens.componenttokens.DSCardImageTokens
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSCardImageStyle {
    var boxShadow: ODSEffect? = null
    var width: Dp? = null // Not used in mobile
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var imageAspectRatioVerticalAlignment: Alignment.Vertical? = null // Not used in mobile
    var imageAspectRatioHorizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var imageAspectRatioVerticalArrangement: Arrangement.Vertical? = null // Not used in mobile
    var imageAspectRatioContentAlignment: Alignment? = null
    var imageContainerBorderRadius: ODSCorners? = null
    var imageContainerClipContent: Boolean? = null
    var imageContainerVerticalAlignment: Alignment.Vertical? = null
    var imageContainerHorizontalAlignment: Alignment.Horizontal? = null
    var imageContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var imageObjectFit: ContentScale? = null
    var logoImageContentAlignment: Alignment? = null
    var logoImageHeight: Dp? = null
    var logoImageWidth: Dp? = null
    var logoImageOffset: ODSOffset? = null
    var logoImageObjectFit: ContentScale? = null
    var contentGap: Dp? = null
    var contentPadding: ODSPadding? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentVerticalArrangement: Arrangement.Vertical? = null
    var contentContentAlignment: Alignment? = null
    var cardBgBackgroundColor: List<ODSColorModel>? = null
    var cardBgBorderRadius: ODSCorners? = null
    var cardBgClipContent: Boolean? = null
    var cardBgVerticalAlignment: Alignment.Vertical? = null
    var cardBgHorizontalAlignment: Alignment.Horizontal? = null
    var cardBgVerticalArrangement: Arrangement.Vertical? = null
    var slotContainerVerticalAlignment: Alignment.Vertical? = null
    var slotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var slotContainerVerticalArrangement: Arrangement.Vertical? = null
    var actionContainerVerticalAlignment: Alignment.Vertical? = null
    var actionContainerHorizontalAlignment: Alignment.Horizontal? = null
    var actionContainerVerticalArrangement: Arrangement.Vertical? = null
    var logoRadius: ODSCorners? = null // Not exported from plugin
    var scaleFactor: Float? = null // Not exported from plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardImageProps,
        state: ODSActions
    ): ODSCardImageStyle {
        val style = ODSCardImageStyle()
        style.boxShadow = scheme.elevationLevel4
        style.width = DSCardImageTokens.width
        style.verticalAlignment = DSCardImageTokens.verticalAlignment
        style.horizontalAlignment = DSCardImageTokens.horizontalAlignment
        style.verticalArrangement = DSCardImageTokens.verticalArrangement
        if (state == ODSActions.HOVERED) {
            style.boxShadow = scheme.elevationLevel6
        }
        if (state == ODSActions.PRESSED) {
            style.boxShadow = scheme.elevationLevel2
        }
        style.imageAspectRatioVerticalAlignment =
            DSCardImageTokens.imageAspectRatioVerticalAlignment
        style.imageAspectRatioHorizontalAlignment =
            DSCardImageTokens.imageAspectRatioHorizontalAlignment
        style.imageAspectRatioVerticalArrangement =
            DSCardImageTokens.imageAspectRatioVerticalArrangement
        style.imageAspectRatioContentAlignment = DSCardImageTokens.imageAspectRatioContentAlignment
        style.imageContainerClipContent = DSCardImageTokens.imageContainerClipContent
        style.imageContainerVerticalAlignment = DSCardImageTokens.imageContainerVerticalAlignment
        style.imageContainerHorizontalAlignment =
            DSCardImageTokens.imageContainerHorizontalAlignment
        style.imageContainerHorizontalArrangement =
            DSCardImageTokens.imageContainerHorizontalArrangement
        if (props.imagePosition == ODSCardImageImagePosition.TOP) {
            style.imageContainerBorderRadius =
                DSCardImageTokens.imageContainerBorderRadiusImagePositionTop
        }
        if (props.imagePosition == ODSCardImageImagePosition.BOTTOM) {
            style.imageContainerBorderRadius =
                DSCardImageTokens.imageContainerBorderRadiusImagePositionBottom
        }
        style.imageObjectFit = DSCardImageTokens.imageObjectFit
        style.logoImageContentAlignment = DSCardImageTokens.logoImageContentAlignment
        style.logoImageHeight = DSCardImageTokens.logoImageHeight
        style.logoImageWidth = DSCardImageTokens.logoImageWidth
        style.logoImageOffset = DSCardImageTokens.logoImageOffset
        style.logoImageObjectFit = DSCardImageTokens.logoImageObjectFit
        style.contentPadding = DSCardImageTokens.contentPadding
        style.contentHorizontalAlignment = DSCardImageTokens.contentHorizontalAlignment
        style.contentContentAlignment = DSCardImageTokens.contentContentAlignment
//        if (props.customHeight) {
//            style.contentVerticalArrangement =
//                DSCardImageTokens.contentVerticalArrangementCustomHeight
//        }
//        if (!props.customHeight) {
        style.contentGap = DSCardImageTokens.contentGap
        style.contentVerticalAlignment = DSCardImageTokens.contentVerticalAlignment
        style.contentVerticalArrangement = DSCardImageTokens.contentVerticalArrangement
//        }
        style.cardBgBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        style.cardBgClipContent = DSCardImageTokens.cardBgClipContent
        style.cardBgVerticalAlignment = DSCardImageTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardImageTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardImageTokens.cardBgVerticalArrangement
        if (props.imagePosition == ODSCardImageImagePosition.TOP) {
            style.cardBgBorderRadius = DSCardImageTokens.cardBgBorderRadiusImagePositionTop
        }
        if (props.imagePosition == ODSCardImageImagePosition.BOTTOM) {
            style.cardBgBorderRadius = DSCardImageTokens.cardBgBorderRadiusImagePositionBottom
        }
        style.slotContainerVerticalAlignment = DSCardImageTokens.slotContainerVerticalAlignment
        style.slotContainerHorizontalAlignment = DSCardImageTokens.slotContainerHorizontalAlignment
        style.slotContainerVerticalArrangement = DSCardImageTokens.slotContainerVerticalArrangement
//        if (props.customHeight) {
//            style.actionContainerVerticalAlignment =
//                DSCardImageTokens.actionContainerVerticalAlignmentCustomHeight
//            style.actionContainerHorizontalAlignment =
//                DSCardImageTokens.actionContainerHorizontalAlignmentCustomHeight
//            style.actionContainerVerticalArrangement =
//                DSCardImageTokens.actionContainerVerticalArrangementCustomHeight
//        }
//        if (!props.customHeight) {
        style.actionContainerVerticalAlignment =
            DSCardImageTokens.actionContainerVerticalAlignment
        style.actionContainerHorizontalAlignment =
            DSCardImageTokens.actionContainerHorizontalAlignment
        style.actionContainerVerticalArrangement =
            DSCardImageTokens.actionContainerVerticalArrangement
//        }
        // Not exported from the plugin
        style.cardBgBorderRadius = ODSCorners(
            topLeft = DSCardImageTokens.cardBgBorderRadiusImagePositionBottom.topLeft,
            topRight = DSCardImageTokens.cardBgBorderRadiusImagePositionBottom.topRight,
            bottomLeft = DSCardImageTokens.cardBgBorderRadiusImagePositionTop.bottomLeft,
            bottomRight = DSCardImageTokens.cardBgBorderRadiusImagePositionTop.bottomRight
        )
        style.scaleFactor = DSCardImageTokens.scaleFactor
        style.logoRadius = DSCardImageTokens.logoRadius
        return style
    }
}
