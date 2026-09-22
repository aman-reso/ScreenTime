package com.telekom.odsystem.atoms.cardengagement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSCardEngagementTokens
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSCardEngagementStyle {
    var borderRadius: ODSCorners? = null
    var height: Dp? = null // Not used in mobile
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var backgroundBackgroundColor: List<ODSColorModel>? = null
    var backgroundBorderRadius: ODSCorners? = null
    var backgroundWidth: Dp? = null // Not used in mobile
    var backgroundHeight: Dp? = null // Not used in mobile
    var backgroundClipContent: Boolean? = null
    var backgroundVerticalAlignment: Alignment.Vertical? = null
    var backgroundHorizontalAlignment: Alignment.Horizontal? = null
    var backgroundVerticalArrangement: Arrangement.Vertical? = null
    var labelContainerPadding: ODSPadding? = null
    var labelContainerVerticalAlignment: Alignment.Vertical? = null
    var labelContainerHorizontalAlignment: Alignment.Horizontal? = null
    var labelContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var imageContainerBorderRadius: ODSCorners? = null
    var imageContainerWidth: Dp? = null  // Not used in mobile
    var imageContainerHeight: Dp? = null
    var imageContainerClipContent: Boolean? = null  // Not used in mobile
    var imageWidth: Dp? = null
    var imageHeight: Dp? = null
    var imageObjectFit: ContentScale? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var labelTextOverflow: TextOverflow? = null
    var labelTextMaxLines: Int? = null // Not exported by plugin.
    var scaleFactor: Float? = null // Not exported by plugin.
    var imageHorizontalOffset: Float? = null // Not exported by plugin.
    var textContainerAlignment: Alignment? = null // Not exported by plugin

    fun getStyle(
        scheme: ODSTheme,
        state: ODSActions
    ): ODSCardEngagementStyle {
        var style = ODSCardEngagementStyle()
        style.borderRadius = DSCardEngagementTokens.borderRadius
        style.height = DSCardEngagementTokens.height
        style.verticalAlignment = DSCardEngagementTokens.verticalAlignment
        style.horizontalAlignment = DSCardEngagementTokens.horizontalAlignment
        style.verticalArrangement = DSCardEngagementTokens.verticalArrangement
        style.backgroundBackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        style.backgroundBorderRadius = DSCardEngagementTokens.backgroundBorderRadius
        style.backgroundWidth = DSCardEngagementTokens.backgroundWidth
        style.backgroundHeight = DSCardEngagementTokens.backgroundHeight
        style.backgroundClipContent = DSCardEngagementTokens.backgroundClipContent
        style.backgroundVerticalAlignment = DSCardEngagementTokens.backgroundVerticalAlignment
        style.backgroundHorizontalAlignment = DSCardEngagementTokens.backgroundHorizontalAlignment
        style.backgroundVerticalArrangement = DSCardEngagementTokens.backgroundVerticalArrangement
//        if (state == ODSActions.PRESSED) {
//            style.backgroundWidth = DSCardEngagementTokens.backgroundWidthStatePressed
//            style.backgroundHeight = DSCardEngagementTokens.backgroundHeightStatePressed
//        }
//        if (state == ODSActions.HOVERED) {
//            style.backgroundWidth = DSCardEngagementTokens.backgroundWidthStateHovered
//            style.backgroundHeight = DSCardEngagementTokens.backgroundHeightStateHovered
//        }
        style.labelContainerPadding = DSCardEngagementTokens.labelContainerPadding
        style.labelContainerVerticalAlignment =
            DSCardEngagementTokens.labelContainerVerticalAlignment
        style.labelContainerHorizontalAlignment =
            DSCardEngagementTokens.labelContainerHorizontalAlignment
        style.labelContainerHorizontalArrangement =
            DSCardEngagementTokens.labelContainerHorizontalArrangement
        style.imageContainerBorderRadius = DSCardEngagementTokens.imageContainerBorderRadius
        style.imageContainerWidth = DSCardEngagementTokens.imageContainerWidth
        style.imageContainerHeight = DSCardEngagementTokens.imageContainerHeight
        style.imageContainerClipContent = DSCardEngagementTokens.imageContainerClipContent
//        if (state == ODSActions.PRESSED) {
//            style.imageContainerWidth = DSCardEngagementTokens.imageContainerWidthStatePressed
//            style.imageContainerHeight = DSCardEngagementTokens.imageContainerHeightStatePressed
//        }
//        if (state == ODSActions.HOVERED) {
//            style.imageContainerWidth = DSCardEngagementTokens.imageContainerWidthStateHovered
//            style.imageContainerHeight = DSCardEngagementTokens.imageContainerHeightStateHovered
//        }
        style.imageWidth = DSCardEngagementTokens.imageWidth
        style.imageHeight = DSCardEngagementTokens.imageHeight
        style.imageObjectFit = DSCardEngagementTokens.imageObjectFit
        style.labelTextStyle = DSCardEngagementTokens.labelTextStyle
        style.labelColor = scheme.basicText
        style.labelTextAlign = DSCardEngagementTokens.labelTextAlign
        style.labelTextOverflow = DSCardEngagementTokens.labelTextOverflow
        // Not exported by plugin.
        if (state == ODSActions.HOVERED) {
            style.scaleFactor = DSCardEngagementTokens.scaleFactor
        } else {
            style.scaleFactor = DEFAULT_FACTOR
        }
        style.labelTextMaxLines = DSCardEngagementTokens.labelTextMaxLines
        style.imageHorizontalOffset = DSCardEngagementTokens.imageHorizontalOffset
        style.textContainerAlignment =
            DSCardEngagementTokens.textContainerAlignment // Not exported by plugin
        return style
    }
}
