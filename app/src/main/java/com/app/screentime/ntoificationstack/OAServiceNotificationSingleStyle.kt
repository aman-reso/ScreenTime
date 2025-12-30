package com.app.screentime.ntoificationstack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-11-04 (v1.34.1) - uid: 5bee5665
 * Figma link: https://figma.com/design/3MbZ8LOrBNBjTZX9J3t8Lu/OneApp ODS Library?node-id=8940-4365
 */

class OAServiceNotificationSingleStyle {
    var background: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var cornerRadius: ODSCorners? = null
    var width: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var notificationGap: Dp? = null
    var notificationPadding: ODSPadding? = null
    var notificationVerticalAlignment: Alignment.Vertical? = null
    var notificationHorizontalAlignment: Alignment.Horizontal? = null
    var notificationHorizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var contentPadding: ODSPadding? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentVerticalArrangement: Arrangement.Vertical? = null
    var titleStyle: ODSTextStyle? = null
    var titleColor: HexColor? = null
    var titleTextAlign: TextAlign? = null
    var titleOverflow: TextOverflow? = null
    var spacerPadding: ODSPadding? = null
    var spacerVerticalAlignment: Alignment.Vertical? = null
    var spacerHorizontalAlignment: Alignment.Horizontal? = null
    var spacerHorizontalArrangement: Arrangement.Horizontal? = null
    var cardBgCornerRadius: ODSCorners? = null
    var cardBgBackground: List<ODSColorModel>? = null
    fun getStyle(
        scheme: ODSTheme,
    ): OAServiceNotificationSingleStyle {
        val style = OAServiceNotificationSingleStyle()
        style.background = listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
        style.padding = DSOAServiceNotificationSingleTokens.padding
        style.cornerRadius = DSOAServiceNotificationSingleTokens.cornerRadius
        style.width = DSOAServiceNotificationSingleTokens.width
        style.verticalAlignment = DSOAServiceNotificationSingleTokens.verticalAlignment
        style.horizontalArrangement = DSOAServiceNotificationSingleTokens.horizontalArrangement
        style.notificationGap = DSOAServiceNotificationSingleTokens.notificationGap
        style.notificationPadding = DSOAServiceNotificationSingleTokens.notificationPadding
        style.notificationVerticalAlignment =
            DSOAServiceNotificationSingleTokens.notificationVerticalAlignment
        style.notificationHorizontalAlignment =
            DSOAServiceNotificationSingleTokens.notificationHorizontalAlignment
        style.notificationHorizontalArrangement =
            DSOAServiceNotificationSingleTokens.notificationHorizontalArrangement
        style.iconColor = scheme.basicText
        style.iconWidth = DSOAServiceNotificationSingleTokens.iconWidth
        style.iconHeight = DSOAServiceNotificationSingleTokens.iconHeight
        style.contentPadding = DSOAServiceNotificationSingleTokens.contentPadding
        style.contentVerticalAlignment =
            DSOAServiceNotificationSingleTokens.contentVerticalAlignment
        style.contentHorizontalAlignment =
            DSOAServiceNotificationSingleTokens.contentHorizontalAlignment
        style.contentVerticalArrangement =
            DSOAServiceNotificationSingleTokens.contentVerticalArrangement
        style.titleStyle = DSOAServiceNotificationSingleTokens.titleStyle
        style.titleColor = scheme.basicText
        style.titleTextAlign = DSOAServiceNotificationSingleTokens.titleTextAlign
        style.titleOverflow = DSOAServiceNotificationSingleTokens.titleOverflow
        style.spacerPadding = DSOAServiceNotificationSingleTokens.spacerPadding
        style.spacerVerticalAlignment = DSOAServiceNotificationSingleTokens.spacerVerticalAlignment
        style.spacerHorizontalAlignment =
            DSOAServiceNotificationSingleTokens.spacerHorizontalAlignment
        style.spacerHorizontalArrangement =
            DSOAServiceNotificationSingleTokens.spacerHorizontalArrangement
        style.cardBgCornerRadius = DSOAServiceNotificationSingleTokens.cardBgCornerRadius
        style.cardBgBackground = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        return style
    }
}