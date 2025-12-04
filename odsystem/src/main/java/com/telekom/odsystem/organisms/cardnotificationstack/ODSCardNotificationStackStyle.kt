package com.telekom.odsystem.organisms.cardnotificationstack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSCardNotificationStackStyle {
    var minWidth: Dp? = null
    var width: Dp? = null // Not used in mobile
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var cardContainersVerticalAlignment: Alignment.Vertical? = null
    var cardContainersHorizontalAlignment: Alignment.Horizontal? = null
    var cardContainersVerticalArrangement: Arrangement.Vertical? = null
    var cardHolder1Padding: ODSPadding? = null
    var cardHolder1VerticalAlignment: Alignment.Vertical? = null
    var cardHolder1HorizontalAlignment: Alignment.Horizontal? = null
    var cardHolder1VerticalArrangement: Arrangement.Vertical? = null
    var cardBgBackgroundColor: List<ODSColorModel>? = null
    var cardBgBorderRadius: ODSCorners? = null
    var cardBgHeight: Dp? = null
    var cardBgClipContent: Boolean? = null
    var cardBgVerticalAlignment: Alignment.Vertical? = null
    var cardBgHorizontalAlignment: Alignment.Horizontal? = null
    var cardBgVerticalArrangement: Arrangement.Vertical? = null
    var cardHolder2Padding: ODSPadding? = null
    var cardHolder2VerticalAlignment: Alignment.Vertical? = null
    var cardHolder2HorizontalAlignment: Alignment.Horizontal? = null
    var cardHolder2VerticalArrangement: Arrangement.Vertical? = null
    var cardBg2BackgroundColor: List<ODSColorModel>? = null
    var cardBg2BorderRadius: ODSCorners? = null
    var cardBg2Height: Dp? = null
    var cardBg2ClipContent: Boolean? = null
    var cardBg2VerticalAlignment: Alignment.Vertical? = null
    var cardBg2HorizontalAlignment: Alignment.Horizontal? = null
    var cardBg2VerticalArrangement: Arrangement.Vertical? = null
    var viewAllVerticalAlignment: Alignment.Vertical? = null
    var viewAllHorizontalAlignment: Alignment.Horizontal? = null
    var viewAllVerticalArrangement: Arrangement.Vertical? = null

    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardNotificationStackProps
    ): ODSCardNotificationStackStyle {
        val style = ODSCardNotificationStackStyle()
        style.minWidth = DSCardNotificationStackTokens.minWidth
        style.width = DSCardNotificationStackTokens.width
        style.verticalAlignment = DSCardNotificationStackTokens.verticalAlignment
        style.horizontalAlignment = DSCardNotificationStackTokens.horizontalAlignment
        style.verticalArrangement = DSCardNotificationStackTokens.verticalArrangement
        style.cardContainersVerticalAlignment =
            DSCardNotificationStackTokens.cardContainersVerticalAlignment
        style.cardContainersHorizontalAlignment =
            DSCardNotificationStackTokens.cardContainersHorizontalAlignment
        style.cardContainersVerticalArrangement =
            DSCardNotificationStackTokens.cardContainersVerticalArrangement
        style.cardHolder1Padding = DSCardNotificationStackTokens.cardHolder1Padding
        style.cardHolder1VerticalAlignment =
            DSCardNotificationStackTokens.cardHolder1VerticalAlignment
        style.cardHolder1HorizontalAlignment =
            DSCardNotificationStackTokens.cardHolder1HorizontalAlignment
        style.cardHolder1VerticalArrangement =
            DSCardNotificationStackTokens.cardHolder1VerticalArrangement
        style.cardBgBackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades300))
        style.cardBgBorderRadius = DSCardNotificationStackTokens.cardBgBorderRadius
        style.cardBgHeight = DSCardNotificationStackTokens.cardBgHeight
        style.cardBgClipContent = DSCardNotificationStackTokens.cardBgClipContent
        style.cardBgVerticalAlignment = DSCardNotificationStackTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardNotificationStackTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardNotificationStackTokens.cardBgVerticalArrangement
        style.cardHolder2Padding = DSCardNotificationStackTokens.cardHolder2Padding
        style.cardHolder2VerticalAlignment =
            DSCardNotificationStackTokens.cardHolder2VerticalAlignment
        style.cardHolder2HorizontalAlignment =
            DSCardNotificationStackTokens.cardHolder2HorizontalAlignment
        style.cardHolder2VerticalArrangement =
            DSCardNotificationStackTokens.cardHolder2VerticalArrangement
        style.cardBg2BackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades400))
        style.cardBg2BorderRadius = DSCardNotificationStackTokens.cardBg2BorderRadius
        style.cardBg2Height = DSCardNotificationStackTokens.cardBg2Height
        style.cardBg2ClipContent = DSCardNotificationStackTokens.cardBg2ClipContent
        style.cardBg2VerticalAlignment = DSCardNotificationStackTokens.cardBg2VerticalAlignment
        style.cardBg2HorizontalAlignment = DSCardNotificationStackTokens.cardBg2HorizontalAlignment
        style.cardBg2VerticalArrangement = DSCardNotificationStackTokens.cardBg2VerticalArrangement
        style.viewAllVerticalAlignment = DSCardNotificationStackTokens.viewAllVerticalAlignment
        style.viewAllVerticalArrangement = DSCardNotificationStackTokens.viewAllVerticalArrangement
        if (props.linkAlignment == ODSCardNotificationStackLinkAlignment.CENTERED) {
            style.viewAllHorizontalAlignment =
                DSCardNotificationStackTokens.viewAllHorizontalAlignmentViewAllCentered
        }
        if (props.linkAlignment == ODSCardNotificationStackLinkAlignment.LEFT_SIDE) {
            style.viewAllHorizontalAlignment =
                DSCardNotificationStackTokens.viewAllHorizontalAlignmentViewAllLeftSide
        }
        if (props.linkAlignment == ODSCardNotificationStackLinkAlignment.RIGHT_SIDE) {
            style.viewAllHorizontalAlignment =
                DSCardNotificationStackTokens.viewAllHorizontalAlignmentViewAllRightSide
        }
        return style
    }
}
