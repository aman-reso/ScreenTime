package com.telekom.odsystem.organisms.timepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSTimePickerStyle {
    var gap: Dp? = null // Not used in mobile
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var odsTimePickerFlyoutLargeY: Dp? = null // Not used in mobile

    // Custom addition
    var containerColor: HexColor? = null
    var clockDialColor: HexColor? = null
    var selectorColor: HexColor? = null
    var timeSelectorUnselectedContainerColor: HexColor? = null
    var timeSelectorUnselectedContentColor: HexColor? = null
    var timeSelectorSelectedContainerColor: HexColor? = null
    var timeSelectorSelectedContentColor: HexColor? = null
    var clockDialSelectedContentColor: HexColor? = null
    var clockDialUnselectedContentColor: HexColor? = null

    fun getStyle(
        scheme: ODSTheme,
        props: ODSTimePickerProps
    ): ODSTimePickerStyle {
        val style = ODSTimePickerStyle()
        style.verticalAlignment = DSTimePickerTokens.verticalAlignment
        style.horizontalAlignment = DSTimePickerTokens.horizontalAlignment
        style.verticalArrangement = DSTimePickerTokens.verticalArrangement
        style.contentAlignment = DSTimePickerTokens.contentAlignment
//        if (props.status == ODSTimePickerStatus.FILLED) {
//            style.gap = DSTimePickerTokens.gapStatusFilled
//        }
//        if (props.status == ODSTimePickerStatus.EDITING) {
//            style.gap = DSTimePickerTokens.gapStatusEditing
//        }
//        if (props.status == ODSTimePickerStatus.UNFILLED) {
//            style.gap = DSTimePickerTokens.gapStatusUnfilled
//        }
//        if (props.size == ODSTimePickerSize.LARGE && props.status == ODSTimePickerStatus.UNFILLED) {
//            style.odsTimePickerFlyoutLargeY = DSTimePickerTokens.odsTimePickerFlyoutLargeYSizeLargeStatusUnfilled
//        }
//        if (props.size == ODSTimePickerSize.LARGE && props.status == ODSTimePickerStatus.FILLED) {
//            style.odsTimePickerFlyoutLargeY = DSTimePickerTokens.odsTimePickerFlyoutLargeYSizeLargeStatusFilled
//        }
//        if (props.size == ODSTimePickerSize.LARGE && props.status == ODSTimePickerStatus.EDITING) {
//            style.odsTimePickerFlyoutLargeY = DSTimePickerTokens.odsTimePickerFlyoutLargeYSizeLargeStatusEditing
//        }
//        if (props.size == ODSTimePickerSize.SMALL && props.status == ODSTimePickerStatus.UNFILLED) {
//            style.odsTimePickerFlyoutLargeY = DSTimePickerTokens.odsTimePickerFlyoutLargeYSizeSmallStatusUnfilled
//        }
//        if (props.size == ODSTimePickerSize.SMALL && props.status == ODSTimePickerStatus.FILLED) {
//            style.odsTimePickerFlyoutLargeY = DSTimePickerTokens.odsTimePickerFlyoutLargeYSizeSmallStatusFilled
//        }
//        if (props.size == ODSTimePickerSize.SMALL && props.status == ODSTimePickerStatus.EDITING) {
//            style.odsTimePickerFlyoutLargeY = DSTimePickerTokens.odsTimePickerFlyoutLargeYSizeSmallStatusEditing
//        }

        // custom addition
        style.containerColor = scheme.basicBackground
        style.clockDialColor = scheme.basicBackgroundSubtle
        style.selectorColor = scheme.basicAccentSecondary
        style.timeSelectorUnselectedContainerColor = scheme.basicBackgroundSubtle
        style.timeSelectorUnselectedContentColor = scheme.basicText
        style.timeSelectorSelectedContainerColor = scheme.basicAccentSecondary
        style.timeSelectorSelectedContentColor = scheme.basicTextOnAccentSecondary
        style.clockDialSelectedContentColor = scheme.basicTextOnAccentSecondary
        style.clockDialUnselectedContentColor = scheme.basicText
        return style
    }
}
