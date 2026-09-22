package com.telekom.odsystem.organisms.datepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSDatePickerStyle {
    var gap: Dp? = null
    var width: Dp? = null // Not used in mobile
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null // Not used in mobile
    var odsDatePickerFlyoutLargeY: Dp? = null // Not used in mobile

    // Custom additions
    var backgroundColor: HexColor? = null
    var selectedYearContentColor: HexColor? = null
    var selectedYearContainerColor: HexColor? = null
    var selectedDayContentColor: HexColor? = null
    var selectedDayContainerColor: HexColor? = null
    var disabledDayContentColor: HexColor? = null
    var disabledYearContentColor: HexColor? = null
    var todayContentColor: HexColor? = null
    var todayDateBorderColor: HexColor? = null
    var titleContentColor: HexColor? = null
    var yearContentColor: HexColor? = null
    var dividerColor: HexColor? = null
    var headlineContentColor: HexColor? = null
    var dayContentColor: HexColor? = null
    var navigationContentColor: HexColor? = null
    var weekdayContentColor: HexColor? = null
    var currentYearContentColor: HexColor? = null

    fun getStyle(
        scheme: ODSTheme,
        props: ODSDatePickerProps
    ): ODSDatePickerStyle {
        val style = ODSDatePickerStyle()
        style.gap = DSDatePickerTokens.gap
        style.width = DSDatePickerTokens.width
        style.verticalAlignment = DSDatePickerTokens.verticalAlignment
        style.horizontalAlignment = DSDatePickerTokens.horizontalAlignment
        style.verticalArrangement = DSDatePickerTokens.verticalArrangement
        style.contentAlignment = DSDatePickerTokens.contentAlignment
//        if (props.size == ODSDatePickerSize.LARGE) {
//            style.odsDatePickerFlyoutLargeY = DSDatePickerTokens.odsDatePickerFlyoutLargeYSizeLarge
//        }
//        if (props.size == ODSDatePickerSize.SMALL) {
//            style.odsDatePickerFlyoutLargeY = DSDatePickerTokens.odsDatePickerFlyoutLargeYSizeSmall
//        }

        // Custom addition
        style.backgroundColor = scheme.basicBackground
        style.selectedYearContentColor = scheme.basicTextOnAccent
        style.selectedYearContainerColor = scheme.basicAccent
        style.selectedDayContentColor = scheme.basicTextOnAccent
        style.selectedDayContainerColor = scheme.basicAccent
        style.disabledDayContentColor = scheme.interactionStatesDisabledTextDisabled
        style.disabledYearContentColor = scheme.interactionStatesDisabledTextDisabled
        style.todayContentColor = scheme.basicAccent
        style.todayDateBorderColor = scheme.basicAccent
        style.titleContentColor = scheme.basicText
        style.yearContentColor = scheme.basicText
        style.dividerColor = scheme.basicStrokeSubtle
        style.headlineContentColor = scheme.basicText
        style.dayContentColor = scheme.basicText
        style.navigationContentColor = scheme.basicText
        style.weekdayContentColor = scheme.basicText
        style.currentYearContentColor = scheme.basicAccent
        return style
    }
}
