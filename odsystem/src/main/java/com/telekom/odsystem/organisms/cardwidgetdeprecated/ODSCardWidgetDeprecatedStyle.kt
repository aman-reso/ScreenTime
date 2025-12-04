package com.telekom.odsystem.organisms.cardwidgetdeprecated

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSCardWidgetDeprecatedStyle {
    var borderRadius: ODSCorners? = null
    var minWidth: Dp? = null
    var width: Dp? = null // Not used in mobile
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var aspectContainerBorderRadius: ODSCorners? = null // Not used in mobile
    var aspectContainerVerticalAlignment: Alignment.Vertical? = null // Not used in mobile
    var aspectContainerHorizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var aspectContainerVerticalArrangement: Arrangement.Vertical? = null // Not used in mobile
    var contentContainerGap: Dp? = null
    var contentContainerPadding: ODSPadding? = null
    var contentContainerWidth: Dp? = null // Not used in mobile
    var contentContainerHeight: Dp? = null // Not used in mobile
    var contentContainerVerticalAlignment: Alignment.Vertical? = null
    var contentContainerHorizontalAlignment: Alignment.Horizontal? = null
    var contentContainerVerticalArrangement: Arrangement.Vertical? = null
    var imageWidth: Dp? = null // Not used in mobile
    var imageVerticalAlignment: Alignment.Vertical? = null // Not used in mobile
    var imageHorizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var imageVerticalArrangement: Arrangement.Vertical? = null // Not used in mobile
    var imageHeight: Dp? = null
    var imageObjectFit: ContentScale? = null // Not used in mobile
    var backgroundBackgroundColor: List<ODSColorModel>? = null
    var backgroundBorderRadius: ODSCorners? = null
    var backgroundWidth: Dp? = null // Not used in mobile
    var backgroundClipContent: Boolean? = null
    var backgroundVerticalAlignment: Alignment.Vertical? = null
    var backgroundHorizontalAlignment: Alignment.Horizontal? = null
    var backgroundVerticalArrangement: Arrangement.Vertical? = null
    var imageContainerBorderRadius: ODSCorners? = null
    var imageContainerWidth: Dp? = null // Not used in mobile
    var imageContainerClipContent: Boolean? = null // Not used in mobile
    var imageContainerVerticalAlignment: Alignment.Vertical? = null
    var imageContainerHorizontalAlignment: Alignment.Horizontal? = null
    var imageContainerVerticalArrangement: Arrangement.Vertical? = null
    var titleAndSubtitleVerticalAlignment: Alignment.Vertical? = null
    var titleAndSubtitleHorizontalAlignment: Alignment.Horizontal? = null
    var titleAndSubtitleVerticalArrangement: Arrangement.Vertical? = null
    var odsSlotHeight: Dp? = null // Not used in mobile
    var titleTextStyle: ODSTextStyle? = null
    var titleColor: HexColor? = null
    var titleTextAlign: TextAlign? = null
    var subtitleTextStyle: ODSTextStyle? = null
    var subtitleColor: HexColor? = null
    var subtitleTextAlign: TextAlign? = null

    var scaleFactor: Float? = null // Not exported from the plugin
    var imageVerticalOffset: Dp? = null // Not exported from the plugin
    var logoPadding: ODSPadding? = null // Not exported from the plugin
    var logoSize: Dp? = null // Not exported from the plugin
    var imageContainerAlignment: Alignment? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardWidgetDeprecatedProps,
        state: ODSActions,
    ): ODSCardWidgetDeprecatedStyle {
        var style = ODSCardWidgetDeprecatedStyle()
        style.borderRadius = DSCardWidgetTokens.borderRadius
        style.minWidth = DSCardWidgetTokens.minWidth
        style.width = DSCardWidgetTokens.width
        style.verticalAlignment = DSCardWidgetTokens.verticalAlignment
        style.horizontalAlignment = DSCardWidgetTokens.horizontalAlignment
        style.horizontalArrangement = DSCardWidgetTokens.horizontalArrangement
        style.aspectContainerBorderRadius = DSCardWidgetTokens.aspectContainerBorderRadius
        style.aspectContainerVerticalAlignment = DSCardWidgetTokens.aspectContainerVerticalAlignment
        style.aspectContainerHorizontalAlignment =
            DSCardWidgetTokens.aspectContainerHorizontalAlignment
        style.aspectContainerVerticalArrangement =
            DSCardWidgetTokens.aspectContainerVerticalArrangement
        style.contentContainerGap = DSCardWidgetTokens.contentContainerGap
        style.contentContainerPadding = DSCardWidgetTokens.contentContainerPadding
        style.contentContainerWidth = DSCardWidgetTokens.contentContainerWidth
        style.contentContainerHeight = DSCardWidgetTokens.contentContainerHeight
        style.contentContainerVerticalAlignment =
            DSCardWidgetTokens.contentContainerVerticalAlignment
        style.contentContainerHorizontalAlignment =
            DSCardWidgetTokens.contentContainerHorizontalAlignment
        style.contentContainerVerticalArrangement =
            DSCardWidgetTokens.contentContainerVerticalArrangement
        style.imageWidth = DSCardWidgetTokens.imageWidth
        style.imageVerticalAlignment = DSCardWidgetTokens.imageVerticalAlignment
        style.imageHorizontalAlignment = DSCardWidgetTokens.imageHorizontalAlignment
        style.imageVerticalArrangement = DSCardWidgetTokens.imageVerticalArrangement
        if (props.type == ODSCardWidgetDeprecatedType.SLOT) {
            style.imageWidth = DSCardWidgetTokens.imageWidthTypeSlot
            style.imageHeight = DSCardWidgetTokens.imageHeightTypeSlot
            style.imageObjectFit = DSCardWidgetTokens.imageObjectFitTypeSlot
        }
        if (props.type == ODSCardWidgetDeprecatedType.IMAGE) {
            style.imageWidth = DSCardWidgetTokens.imageWidthTypeImage
            style.imageHeight = DSCardWidgetTokens.imageHeightTypeImage
            style.imageObjectFit = DSCardWidgetTokens.imageObjectFitTypeImage
        }
        if (props.type == ODSCardWidgetDeprecatedType.SLOT && state == ODSActions.HOVERED) {
            style.imageWidth = DSCardWidgetTokens.imageWidthTypeSlotStateHovered
            style.imageHeight = DSCardWidgetTokens.imageHeightTypeSlotStateHovered
        }
        style.backgroundBackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        style.backgroundBorderRadius = DSCardWidgetTokens.backgroundBorderRadius
        style.backgroundWidth = DSCardWidgetTokens.backgroundWidth
        style.backgroundClipContent = DSCardWidgetTokens.backgroundClipContent
        style.backgroundVerticalAlignment = DSCardWidgetTokens.backgroundVerticalAlignment
        style.backgroundHorizontalAlignment = DSCardWidgetTokens.backgroundHorizontalAlignment
        style.backgroundVerticalArrangement = DSCardWidgetTokens.backgroundVerticalArrangement
        if (state == ODSActions.HOVERED) {
            style.backgroundWidth = DSCardWidgetTokens.backgroundWidthStateHovered
        }
        style.imageContainerWidth = DSCardWidgetTokens.imageContainerWidth
        style.imageContainerClipContent = DSCardWidgetTokens.imageContainerClipContent
        style.imageContainerVerticalAlignment = DSCardWidgetTokens.imageContainerVerticalAlignment
        style.imageContainerHorizontalAlignment =
            DSCardWidgetTokens.imageContainerHorizontalAlignment
        style.imageContainerVerticalArrangement =
            DSCardWidgetTokens.imageContainerVerticalArrangement
        if (state == ODSActions.HOVERED) {
            style.imageContainerWidth = DSCardWidgetTokens.imageContainerWidthStateHovered
        }
        if (props.type == ODSCardWidgetDeprecatedType.SLOT) {
            style.imageContainerBorderRadius = DSCardWidgetTokens.imageContainerBorderRadiusTypeSlot
        }
        if (props.type == ODSCardWidgetDeprecatedType.IMAGE) {
            style.imageContainerBorderRadius =
                DSCardWidgetTokens.imageContainerBorderRadiusTypeImage
        }
        style.titleAndSubtitleVerticalAlignment =
            DSCardWidgetTokens.titleAndSubtitleVerticalAlignment
        style.titleAndSubtitleHorizontalAlignment =
            DSCardWidgetTokens.titleAndSubtitleHorizontalAlignment
        style.titleAndSubtitleVerticalArrangement =
            DSCardWidgetTokens.titleAndSubtitleVerticalArrangement
        if (props.type == ODSCardWidgetDeprecatedType.IMAGE) {
            style.odsSlotHeight = DSCardWidgetTokens.odsSlotHeightTypeImage
        }
        style.titleTextStyle = DSCardWidgetTokens.titleTextStyle
        style.titleColor = scheme.basicText
        style.titleTextAlign = DSCardWidgetTokens.titleTextAlign
        style.subtitleTextStyle = DSCardWidgetTokens.subtitleTextStyle
        style.subtitleColor = scheme.basicTextRecessive
        style.subtitleTextAlign = DSCardWidgetTokens.subtitleTextAlign

        style.scaleFactor = DSCardWidgetTokens.scaleFactor // Not exported from the plugin
        style.imageVerticalOffset =
            DSCardWidgetTokens.verticalImageOffset // Not exported from the plugin
        style.logoPadding = DSCardWidgetTokens.logoPadding // Not exported from the plugin
        style.logoSize = DSCardWidgetTokens.logoSize // Not exported from the plugin
        style.imageContainerAlignment =
            DSCardWidgetTokens.imageContainerAlignment // Not exported from the plugin
        return style
    }
}
