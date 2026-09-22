package com.telekom.odsystem.organisms.cardnotification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSCardNotificationStyle {
    var padding: ODSPadding? = null
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var cardBgBackgroundColor: List<ODSColorModel>? = null
    var cardBgBorderRadius: ODSCorners? = null
    var cardBgClipContent: Boolean? = null
    var cardBgVerticalAlignment: Alignment.Vertical? = null
    var cardBgHorizontalAlignment: Alignment.Horizontal? = null
    var cardBgVerticalArrangement: Arrangement.Vertical? = null
    var headerContainerGap: Dp? = null
    var headerContainerPadding: ODSPadding? = null
    var headerContainerMinHeight: Dp? = null
    var headerContainerVerticalAlignment: Alignment.Vertical? = null
    var headerContainerHorizontalAlignment: Alignment.Horizontal? = null
    var headerContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var headerContentGap: Dp? = null
    var headerContentPadding: ODSPadding? = null
    var headerContentVerticalAlignment: Alignment.Vertical? = null
    var headerContentHorizontalAlignment: Alignment.Horizontal? = null
    var headerContentVerticalArrangement: Arrangement.Vertical? = null
    var headerTextStyle: ODSTextStyle? = null
    var headerColor: HexColor? = null
    var headerTextAlign: TextAlign? = null
    var textTextStyle: ODSTextStyle? = null
    var textColor: HexColor? = null
    var textTextAlign: TextAlign? = null
    var closeButtonContainerPadding: ODSPadding? = null
    var closeButtonContainerVerticalAlignment: Alignment.Vertical? = null
    var closeButtonContainerHorizontalAlignment: Alignment.Horizontal? = null
    var closeButtonContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var actionContainerGap: Dp? = null
    var actionContainerPadding: ODSPadding? = null
    var actionContainerVerticalAlignment: Alignment.Vertical? = null
    var actionContainerHorizontalAlignment: Alignment.Horizontal? = null
    var actionContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var scaleFactor: Float? = null // Not exported from the plugin

    fun getStyle(
        scheme: ODSTheme,
        showImage: Boolean
    ): ODSCardNotificationStyle {
        val style = ODSCardNotificationStyle()
        style.padding = DSCardNotificationTokens.padding
        style.minHeight = DSCardNotificationTokens.minHeight
        style.verticalAlignment = DSCardNotificationTokens.verticalAlignment
        style.horizontalAlignment = DSCardNotificationTokens.horizontalAlignment
        style.verticalArrangement = DSCardNotificationTokens.verticalArrangement
        style.contentAlignment = DSCardNotificationTokens.contentAlignment
        style.cardBgBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        style.cardBgBorderRadius = DSCardNotificationTokens.cardBgBorderRadius
        style.cardBgClipContent = DSCardNotificationTokens.cardBgClipContent
        style.cardBgVerticalAlignment = DSCardNotificationTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardNotificationTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardNotificationTokens.cardBgVerticalArrangement
        style.headerContainerPadding = DSCardNotificationTokens.headerContainerPadding
        style.headerContainerMinHeight = DSCardNotificationTokens.headerContainerMinHeight
        style.headerContainerVerticalAlignment =
            DSCardNotificationTokens.headerContainerVerticalAlignment
        style.headerContainerHorizontalAlignment =
            DSCardNotificationTokens.headerContainerHorizontalAlignment
        style.headerContainerHorizontalArrangement =
            DSCardNotificationTokens.headerContainerHorizontalArrangement
        if (showImage) {
            style.headerContainerGap = DSCardNotificationTokens.headerContainerGapShowImageSlot
        }
        if (!showImage) {
            style.headerContainerGap = DSCardNotificationTokens.headerContainerGap
        }
        style.headerContentGap = DSCardNotificationTokens.headerContentGap
        style.headerContentVerticalAlignment =
            DSCardNotificationTokens.headerContentVerticalAlignment
        style.headerContentHorizontalAlignment =
            DSCardNotificationTokens.headerContentHorizontalAlignment
        style.headerContentVerticalArrangement =
            DSCardNotificationTokens.headerContentVerticalArrangement
        if (showImage) {
            style.headerContentPadding = DSCardNotificationTokens.headerContentPaddingShowImageSlot
        }
        if (!showImage) {
            style.headerContentPadding = DSCardNotificationTokens.headerContentPadding
        }
        style.headerTextStyle = DSCardNotificationTokens.headerTextStyle
        style.headerColor = scheme.basicText
        style.headerTextAlign = DSCardNotificationTokens.headerTextAlign
        style.textTextStyle = DSCardNotificationTokens.textTextStyle
        style.textColor = scheme.basicText
        style.textTextAlign = DSCardNotificationTokens.textTextAlign
        style.closeButtonContainerPadding = DSCardNotificationTokens.closeButtonContainerPadding
        style.closeButtonContainerVerticalAlignment =
            DSCardNotificationTokens.closeButtonContainerVerticalAlignment
        style.closeButtonContainerHorizontalAlignment =
            DSCardNotificationTokens.closeButtonContainerHorizontalAlignment
        style.closeButtonContainerHorizontalArrangement =
            DSCardNotificationTokens.closeButtonContainerHorizontalArrangement
        style.actionContainerGap = DSCardNotificationTokens.actionContainerGap
        style.actionContainerVerticalAlignment =
            DSCardNotificationTokens.actionContainerVerticalAlignment
        style.actionContainerHorizontalAlignment =
            DSCardNotificationTokens.actionContainerHorizontalAlignment
        style.actionContainerHorizontalArrangement =
            DSCardNotificationTokens.actionContainerHorizontalArrangement
        if (showImage) {
            style.actionContainerPadding = DSCardNotificationTokens.actionContainerPaddingShowImageSlot
        }
        if (!showImage) {
            style.actionContainerPadding = DSCardNotificationTokens.actionContainerPadding
        }
        // custom additions
        style.scaleFactor = DSCardNotificationTokens.scaleFactor
        return style
    }
}
