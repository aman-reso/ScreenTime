package com.telekom.odsystem.molecules.flyoutmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.telekom.odsystem.componenttokens.DSFlyoutMenuTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSFlyoutMenuStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null // Not used in mobile
    var flyoutContainerWidth: Dp? = null // Not used in mobile
    var flyoutContainerOffset: ODSOffset? = null // Not used in mobile
    var flyoutContainerVerticalAlignment: Alignment.Vertical? = null
    var flyoutContainerHorizontalAlignment: Alignment.Horizontal? = null
    var flyoutContainerVerticalArrangement: Arrangement.Vertical? = null
    var flyoutContainerContentAlignment: Alignment? = null
    var odsFlyoutListContainerLargeLevel2ContentAlignment: Alignment? = null // Not used in mobile
    var odsFlyoutListContainerLargeLevel2Width: Dp? = null // Not used in mobile
    var odsFlyoutListContainerLargeLevel2Offset: ODSOffset? = null // Not used in mobile
    var odsFlyoutListContainerSmallLevel2ContentAlignment: Alignment? = null // Not used in mobile
    var odsFlyoutListContainerSmallLevel2Width: Dp? = null // Not used in mobile
    var odsFlyoutListContainerSmallLevel2Offset: ODSOffset? = null // Not used in mobile
    var odsFlyoutListContainerSmallWidth: Dp? = null // Not used in mobile
    var dropdownBorderRadius: ODSCorners? = null // Not exported from the plugin
    var dropdownPadding: ODSPadding? = null // Not exported from the plugin
    var dropdownOffset: DpOffset? = null // Not exported from the plugin
    var dropdownBackgroundColor: HexColor? = null // Not exported from the plugin
    var dropdownBorderColor: HexColor? = null // Not exported from the plugin
    var dropdownBorderWidth: Dp? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSFlyoutMenuProps
    ): ODSFlyoutMenuStyle {
        val style = ODSFlyoutMenuStyle()
        style.gap = DSFlyoutMenuTokens.gap
        style.verticalAlignment = DSFlyoutMenuTokens.verticalAlignment
        style.horizontalAlignment = DSFlyoutMenuTokens.horizontalAlignment
        style.verticalArrangement = DSFlyoutMenuTokens.verticalArrangement
        style.contentAlignment = DSFlyoutMenuTokens.contentAlignment
        style.flyoutContainerOffset = DSFlyoutMenuTokens.flyoutContainerOffset
        style.flyoutContainerVerticalAlignment = DSFlyoutMenuTokens.flyoutContainerVerticalAlignment
        style.flyoutContainerHorizontalAlignment =
            DSFlyoutMenuTokens.flyoutContainerHorizontalAlignment
        style.flyoutContainerVerticalArrangement =
            DSFlyoutMenuTokens.flyoutContainerVerticalArrangement
        style.flyoutContainerContentAlignment = DSFlyoutMenuTokens.flyoutContainerContentAlignment
        if (props.menuSize == ODSFlyoutMenuMenuSize.LARGE) {
            style.flyoutContainerWidth = DSFlyoutMenuTokens.flyoutContainerWidthMenuSizeLarge
        }
        if (props.menuSize == ODSFlyoutMenuMenuSize.SMALL) {
            style.flyoutContainerWidth = DSFlyoutMenuTokens.flyoutContainerWidthMenuSizeSmall
        }
        if (props.menuSize == ODSFlyoutMenuMenuSize.LARGE) {
            style.odsFlyoutListContainerLargeLevel2ContentAlignment =
                DSFlyoutMenuTokens.odsFlyoutListContainerLargeLevel2ContentAlignmentMenuSizeLarge
            style.odsFlyoutListContainerLargeLevel2Width =
                DSFlyoutMenuTokens.odsFlyoutListContainerLargeLevel2WidthMenuSizeLarge
            style.odsFlyoutListContainerLargeLevel2Offset =
                DSFlyoutMenuTokens.odsFlyoutListContainerLargeLevel2OffsetMenuSizeLarge
        }
        if (props.menuSize == ODSFlyoutMenuMenuSize.SMALL) {
            style.odsFlyoutListContainerSmallLevel2ContentAlignment =
                DSFlyoutMenuTokens.odsFlyoutListContainerSmallLevel2ContentAlignmentMenuSizeSmall
            style.odsFlyoutListContainerSmallLevel2Width =
                DSFlyoutMenuTokens.odsFlyoutListContainerSmallLevel2WidthMenuSizeSmall
            style.odsFlyoutListContainerSmallLevel2Offset =
                DSFlyoutMenuTokens.odsFlyoutListContainerSmallLevel2OffsetMenuSizeSmall
        }
//        if (props.menuSize == ODSFlyoutMenuMenuSize.SMALL && props.expanded == ODSFlyoutMenuExpanded.CLOSED && props.disabled) {
//            style.odsFlyoutListContainerSmallWidth =
//                DSFlyoutMenuTokens.odsFlyoutListContainerSmallWidthMenuSizeSmallExpandedClosedDisabled
//        }
        // Custom additions
        if (props.menuSize == ODSFlyoutMenuMenuSize.LARGE) {
            style.dropdownPadding = DSFlyoutMenuTokens.dropdownPaddingLarge
        }
        if (props.menuSize == ODSFlyoutMenuMenuSize.SMALL) {
            style.dropdownPadding = DSFlyoutMenuTokens.dropdownPaddingSmall
        }
        style.dropdownBorderRadius = DSFlyoutMenuTokens.dropdownBorderRadius
        style.dropdownOffset = DSFlyoutMenuTokens.dropdownOffset
        style.dropdownBackgroundColor = scheme.basicBackground
        style.dropdownBorderColor = scheme.basicStroke
        style.dropdownBorderWidth = DSFlyoutMenuTokens.dropdownBorderWidth
        return style
    }
}
