package com.telekom.odsystem.atoms.navigationitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-26 (v1.33.1) - uid: 218b2f9e
 * Figma link: https://figma.com/design/cpaNsDgmzEjyHilbYDSKS9/Exploration File_Part 2?node-id=5716-39429
 */

@Suppress("LongMethod")
class ODSNavigationItemStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var cornerRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var iconContainerZStackMinWidth: Dp? = null
    var iconContainerZStackContentAlignment: Alignment? = null
    var iconContainerPadding: ODSPadding? = null
    var iconContainerCornerRadius: ODSCorners? = null
    var iconContainerMinWidth: Dp? = null
    var iconContainerVerticalAlignment: Alignment.Vertical? = null
    var iconContainerHorizontalAlignment: Alignment.Horizontal? = null
    var iconContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var iconContainerContentAlignment: Alignment? = null
    var iconActiveColor: HexColor? = null
    var iconActiveWidth: Dp? = null
    var iconActiveHeight: Dp? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var odsBadgeNumberAbsoluteContentAlignment: Alignment? = null
    var odsBadgeNumberAbsoluteOffset: ODSOffset? = null
    var labelStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSNavigationItemProps,
        state: ODSActions,
    ): ODSNavigationItemStyle {
        val style = ODSNavigationItemStyle()
        style.gap = DSNavigationItemTokens.gap
        style.padding = DSNavigationItemTokens.padding
        style.cornerRadius = DSNavigationItemTokens.cornerRadius
        style.minHeight = DSNavigationItemTokens.minHeight
        style.minWidth = DSNavigationItemTokens.minWidth
        style.verticalAlignment = DSNavigationItemTokens.verticalAlignment
        style.horizontalAlignment = DSNavigationItemTokens.horizontalAlignment
        style.verticalArrangement = DSNavigationItemTokens.verticalArrangement
        style.iconContainerZStackMinWidth = DSNavigationItemTokens.iconContainerZStackMinWidth
        style.iconContainerZStackContentAlignment =
            DSNavigationItemTokens.iconContainerZStackContentAlignment
        style.iconContainerPadding = DSNavigationItemTokens.iconContainerPadding
        style.iconContainerCornerRadius = DSNavigationItemTokens.iconContainerCornerRadius
        style.iconContainerMinWidth = DSNavigationItemTokens.iconContainerMinWidth
        style.iconContainerVerticalAlignment = DSNavigationItemTokens.iconContainerVerticalAlignment
        style.iconContainerHorizontalAlignment =
            DSNavigationItemTokens.iconContainerHorizontalAlignment
        style.iconContainerHorizontalArrangement =
            DSNavigationItemTokens.iconContainerHorizontalArrangement
        style.iconContainerContentAlignment = DSNavigationItemTokens.iconContainerContentAlignment
        style.iconActiveColor = scheme.basicTextDominant
        style.iconActiveWidth = DSNavigationItemTokens.iconActiveWidth
        style.iconActiveHeight = DSNavigationItemTokens.iconActiveHeight
        style.iconWidth = DSNavigationItemTokens.iconWidth
        style.iconHeight = DSNavigationItemTokens.iconHeight
        if (!props.active && !props.disabled) {
            style.iconColor = scheme.basicText
        }
        if (props.active && !props.disabled) {
            style.iconColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.active && props.disabled) {
            style.iconColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (state == ODSActions.HOVERED && !props.active && !props.disabled) {
            style.iconColor = scheme.interactionStatesHoverTextRecessiveHover
        }
        if (state == ODSActions.PRESSED && !props.active && !props.disabled) {
            style.iconColor = scheme.interactionStatesPressedTextRecessivePressed
        }
        style.odsBadgeNumberAbsoluteContentAlignment =
            DSNavigationItemTokens.odsBadgeNumberAbsoluteContentAlignment
        style.odsBadgeNumberAbsoluteOffset = DSNavigationItemTokens.odsBadgeNumberAbsoluteOffset
        style.labelStyle = DSNavigationItemTokens.labelStyle
        style.labelTextAlign = DSNavigationItemTokens.labelTextAlign
        if (!props.active && !props.disabled) {
            style.labelColor = scheme.basicText
        }
        if (!props.active && props.disabled) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.active && !props.disabled) {
            style.labelColor = scheme.basicTextDominant
        }
        if (state == ODSActions.HOVERED && !props.active && !props.disabled) {
            style.labelColor = scheme.interactionStatesHoverTextRecessiveHover
        }
        if (state == ODSActions.PRESSED && !props.active && !props.disabled) {
            style.labelColor = scheme.interactionStatesPressedTextRecessivePressed
        }
        return style
    }
}
