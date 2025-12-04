package com.telekom.odsystem.organisms.cardanchoredimage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
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
class ODSCardAnchoredImageStyle {
    var padding: ODSPadding? = null
    var boxShadow: ODSEffect? = null
    var minHeight: Dp? = null
    var width: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var cardBackgroundBackgroundColor: List<ODSColorModel>? = null
    var cardBackgroundBorderRadius: ODSCorners? = null
    var cardBackgroundWidth: Dp? = null
    var cardBackgroundHeight: Dp? = null
    var cardBackgroundClipContent: Boolean? = null
    var cardBackgroundVerticalAlignment: Alignment.Vertical? = null
    var cardBackgroundHorizontalAlignment: Alignment.Horizontal? = null
    var cardBackgroundVerticalArrangement: Arrangement.Vertical? = null
    var contentContainerGap: Dp? = null
    var contentContainerMinHeight: Dp? = null
    var contentContainerVerticalAlignment: Alignment.Vertical? = null
    var contentContainerHorizontalAlignment: Alignment.Horizontal? = null
    var contentContainerVerticalArrangement: Arrangement.Vertical? = null
    var headingLabelContainerGap: Dp? = null
    var headingLabelContainerVerticalAlignment: Alignment.Vertical? = null
    var headingLabelContainerHorizontalAlignment: Alignment.Horizontal? = null
    var headingLabelContainerVerticalArrangement: Arrangement.Vertical? = null
    var contentSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var contentSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var contentSlotContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var actionSlotContainerPadding: ODSPadding? = null
    var actionSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var actionSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var actionSlotContainerVerticalArrangement: Arrangement.Vertical? = null
    var headingTextStyle: ODSTextStyle? = null
    var headingColor: HexColor? = null
    var headingTextAlign: TextAlign? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null

    var scaleFactor: Float? = null // Not exported by plugin.
    var clipContent: Boolean? = null // Not exported by plugin

    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardAnchoredImageProps,
        state: ODSActions
    ): ODSCardAnchoredImageStyle {
        var style = ODSCardAnchoredImageStyle()
        style.padding = DSCardAnchoredImageTokens.padding
        style.boxShadow = scheme.elevationLevel4
        style.width = DSCardAnchoredImageTokens.width
        style.verticalAlignment = DSCardAnchoredImageTokens.verticalAlignment
        style.horizontalAlignment = DSCardAnchoredImageTokens.horizontalAlignment
        style.verticalArrangement = DSCardAnchoredImageTokens.verticalArrangement
        if (state == ODSActions.HOVERED) {
            style.boxShadow = scheme.elevationLevel6
        }
        if (state == ODSActions.PRESSED) {
            style.boxShadow = scheme.elevationLevel2
        }
        if (props.size == ODSCardAnchoredImageSize.SMALL) {
            style.minHeight = DSCardAnchoredImageTokens.minHeightSizeSmall
        }
        if (props.size == ODSCardAnchoredImageSize.MEDIUM) {
            style.minHeight = DSCardAnchoredImageTokens.minHeightSizeMedium
        }
        style.cardBackgroundBackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        style.cardBackgroundBorderRadius = DSCardAnchoredImageTokens.cardBackgroundBorderRadius
        style.cardBackgroundWidth = DSCardAnchoredImageTokens.cardBackgroundWidth
        style.cardBackgroundClipContent = DSCardAnchoredImageTokens.cardBackgroundClipContent
        style.cardBackgroundVerticalAlignment =
            DSCardAnchoredImageTokens.cardBackgroundVerticalAlignment
        style.cardBackgroundHorizontalAlignment =
            DSCardAnchoredImageTokens.cardBackgroundHorizontalAlignment
        style.cardBackgroundVerticalArrangement =
            DSCardAnchoredImageTokens.cardBackgroundVerticalArrangement
//        if (props.customHeight) {
//            style.cardBackgroundHeight = DSCardAnchoredImageTokens.cardBackgroundHeightCustomHeight
//        }
//        if (state == ODSActions.PRESSED) {
//            style.cardBackgroundWidth = DSCardAnchoredImageTokens.cardBackgroundWidthStatePressed
//        }
//        if (state == ODSActions.HOVERED) {
//            style.cardBackgroundWidth = DSCardAnchoredImageTokens.cardBackgroundWidthStateHovered
//        }
//        if (props.size == ODSCardAnchoredImageSize.MEDIUM && !props.customHeight) {
//            style.cardBackgroundHeight = DSCardAnchoredImageTokens.cardBackgroundHeightSizeMedium
//        }
//        if (props.size == ODSCardAnchoredImageSize.SMALL && !props.customHeight) {
//            style.cardBackgroundHeight = DSCardAnchoredImageTokens.cardBackgroundHeightSizeSmall
//        }
//        if (props.customHeight && state == ODSActions.PRESSED) {
//            style.cardBackgroundHeight =
//                DSCardAnchoredImageTokens.cardBackgroundHeightCustomHeightStatePressed
//        }
//        if (props.customHeight && state == ODSActions.HOVERED) {
//            style.cardBackgroundHeight =
//                DSCardAnchoredImageTokens.cardBackgroundHeightCustomHeightStateHovered
//        }
//        if (props.size == ODSCardAnchoredImageSize.MEDIUM && !props.customHeight && state == ODSActions.PRESSED) {
//            style.cardBackgroundHeight =
//                DSCardAnchoredImageTokens.cardBackgroundHeightSizeMediumStatePressed
//        }
//        if (props.size == ODSCardAnchoredImageSize.SMALL && !props.customHeight && state == ODSActions.PRESSED) {
//            style.cardBackgroundHeight =
//                DSCardAnchoredImageTokens.cardBackgroundHeightSizeSmallStatePressed
//        }
//        if (props.size == ODSCardAnchoredImageSize.MEDIUM && !props.customHeight && state == ODSActions.HOVERED) {
//            style.cardBackgroundHeight =
//                DSCardAnchoredImageTokens.cardBackgroundHeightSizeMediumStateHovered
//        }
//        if (props.size == ODSCardAnchoredImageSize.SMALL && !props.customHeight && state == ODSActions.HOVERED) {
//            style.cardBackgroundHeight =
//                DSCardAnchoredImageTokens.cardBackgroundHeightSizeSmallStateHovered
//        }
        style.contentContainerVerticalAlignment =
            DSCardAnchoredImageTokens.contentContainerVerticalAlignment
        style.contentContainerHorizontalAlignment =
            DSCardAnchoredImageTokens.contentContainerHorizontalAlignment
        style.contentContainerVerticalArrangement =
            DSCardAnchoredImageTokens.contentContainerVerticalArrangement
        if (props.size == ODSCardAnchoredImageSize.SMALL) {
            style.contentContainerGap = DSCardAnchoredImageTokens.contentContainerGapSizeSmall
        }
        if (props.size == ODSCardAnchoredImageSize.MEDIUM) {
            style.contentContainerGap = DSCardAnchoredImageTokens.contentContainerGapSizeMedium
            style.contentContainerMinHeight =
                DSCardAnchoredImageTokens.contentContainerMinHeightSizeMedium
        }
        style.headingLabelContainerGap = DSCardAnchoredImageTokens.headingLabelContainerGap
        style.headingLabelContainerVerticalAlignment =
            DSCardAnchoredImageTokens.headingLabelContainerVerticalAlignment
        style.headingLabelContainerHorizontalAlignment =
            DSCardAnchoredImageTokens.headingLabelContainerHorizontalAlignment
        style.headingLabelContainerVerticalArrangement =
            DSCardAnchoredImageTokens.headingLabelContainerVerticalArrangement
        style.contentSlotContainerVerticalAlignment =
            DSCardAnchoredImageTokens.contentSlotContainerVerticalAlignment
        style.contentSlotContainerHorizontalAlignment =
            DSCardAnchoredImageTokens.contentSlotContainerHorizontalAlignment
        style.contentSlotContainerHorizontalArrangement =
            DSCardAnchoredImageTokens.contentSlotContainerHorizontalArrangement
        style.actionSlotContainerPadding = DSCardAnchoredImageTokens.actionSlotContainerPadding
        style.actionSlotContainerVerticalAlignment =
            DSCardAnchoredImageTokens.actionSlotContainerVerticalAlignment
        style.actionSlotContainerHorizontalAlignment =
            DSCardAnchoredImageTokens.actionSlotContainerHorizontalAlignment
        style.actionSlotContainerVerticalArrangement =
            DSCardAnchoredImageTokens.actionSlotContainerVerticalArrangement
        style.headingColor = scheme.basicText
        style.headingTextAlign = DSCardAnchoredImageTokens.headingTextAlign
        if (props.size == ODSCardAnchoredImageSize.SMALL) {
            style.headingTextStyle = DSCardAnchoredImageTokens.headingTextStyleSizeSmall
        }
        if (props.size == ODSCardAnchoredImageSize.MEDIUM) {
            style.headingTextStyle = DSCardAnchoredImageTokens.headingTextStyleSizeMedium
        }
        style.labelTextStyle = DSCardAnchoredImageTokens.labelTextStyle
        style.labelColor = scheme.basicText
        style.labelTextAlign = DSCardAnchoredImageTokens.labelTextAlign

        // Not exported by plugin
        if (props.size == ODSCardAnchoredImageSize.MEDIUM) {
            style.cardBackgroundHeight = DSCardAnchoredImageTokens.cardBackgroundHeightSizeMedium
        }
        // Not exported by plugin
        if (props.size == ODSCardAnchoredImageSize.SMALL) {
            style.cardBackgroundHeight = DSCardAnchoredImageTokens.cardBackgroundHeightSizeSmall
        }
        style.scaleFactor = DSCardAnchoredImageTokens.scaleFactor
        style.clipContent = DSCardAnchoredImageTokens.clipContent
        return style
    }
}
