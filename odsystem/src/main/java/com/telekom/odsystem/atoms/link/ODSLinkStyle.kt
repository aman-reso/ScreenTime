package com.telekom.odsystem.atoms.link

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSLinkTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("ALL")
class ODSLinkStyle {
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var linkContainerGap: Dp? = null
    var linkContainerVerticalAlignment: Alignment.Vertical? = null
    var linkContainerHorizontalAlignment: Alignment.Horizontal? = null
    var linkContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var linkContentVerticalAlignment: Alignment.Vertical? = null // Not used in mobile
    var linkContentHorizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var linkContentVerticalArrangement: Arrangement.Vertical? = null // Not used in mobile
    var leftIconColor: HexColor? = null
    var leftIconWidth: Dp? = null
    var leftIconHeight: Dp? = null
    var rightIconColor: HexColor? = null
    var rightIconWidth: Dp? = null
    var rightIconHeight: Dp? = null
    var linkTextStyle: ODSTextStyle? = null
    var linkColor: HexColor? = null
    var linkTextAlign: TextAlign? = null
    var underlineThickness: Dp? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSLinkProps,
        state: ODSActions
    ): ODSLinkStyle {
        var style = ODSLinkStyle()
        style.minHeight = DSLinkTokens.minHeight
        style.minWidth = DSLinkTokens.minWidth
        style.verticalAlignment = DSLinkTokens.verticalAlignment
        if (props.alignment == ODSLinkAlignment.LEFT) {
            style.horizontalAlignment = DSLinkTokens.horizontalAlignmentAlignmentLeft
            style.horizontalArrangement = DSLinkTokens.horizontalArrangementAlignmentLeft
        }
        if (props.alignment == ODSLinkAlignment.RIGHT) {
            style.horizontalAlignment = DSLinkTokens.horizontalAlignmentAlignmentRight
            style.horizontalArrangement = DSLinkTokens.horizontalArrangementAlignmentRight
        }
        if (props.alignment == ODSLinkAlignment.CENTERED) {
            style.horizontalAlignment = DSLinkTokens.horizontalAlignmentAlignmentCentered
            style.horizontalArrangement = DSLinkTokens.horizontalArrangementAlignmentCentered
        }
        style.linkContainerGap = DSLinkTokens.linkContainerGap
        style.linkContainerVerticalAlignment = DSLinkTokens.linkContainerVerticalAlignment
        style.linkContainerHorizontalAlignment = DSLinkTokens.linkContainerHorizontalAlignment
        style.linkContainerHorizontalArrangement = DSLinkTokens.linkContainerHorizontalArrangement
        style.linkContentVerticalAlignment = DSLinkTokens.linkContentVerticalAlignment
        style.linkContentHorizontalAlignment = DSLinkTokens.linkContentHorizontalAlignment
        style.linkContentVerticalArrangement = DSLinkTokens.linkContentVerticalArrangement
        style.leftIconWidth = DSLinkTokens.leftIconWidth
        style.leftIconHeight = DSLinkTokens.leftIconHeight
        if (props.type == ODSLinkType.PRIMARY && !props.disabled) {
            style.leftIconColor = scheme.basicTextLink
        }
        if (props.type == ODSLinkType.PRIMARY && props.disabled) {
            style.leftIconColor = scheme.interactionStatesDisabledTextLinkDisabled
        }
        if (props.type == ODSLinkType.SECONDARY && !props.disabled) {
            style.leftIconColor = scheme.basicText
        }
        if (props.type == ODSLinkType.SECONDARY && props.disabled) {
            style.leftIconColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.type == ODSLinkType.PRIMARY && !props.disabled && state == ODSActions.HOVERED) {
            style.leftIconColor = scheme.interactionStatesHoverTextLinkHover
        }
        if (props.type == ODSLinkType.PRIMARY && !props.disabled && state == ODSActions.PRESSED) {
            style.leftIconColor = scheme.interactionStatesPressedTextLinkPressed
        }
        if (props.type == ODSLinkType.SECONDARY && !props.disabled && state == ODSActions.HOVERED) {
            style.leftIconColor = scheme.interactionStatesHoverTextHover
        }
        if (props.type == ODSLinkType.SECONDARY && !props.disabled && state == ODSActions.PRESSED) {
            style.leftIconColor = scheme.interactionStatesPressedTextPressed
        }
        style.rightIconWidth = DSLinkTokens.rightIconWidth
        style.rightIconHeight = DSLinkTokens.rightIconHeight
        if (props.type == ODSLinkType.PRIMARY && !props.disabled) {
            style.rightIconColor = scheme.basicTextLink
        }
        if (props.type == ODSLinkType.PRIMARY && props.disabled) {
            style.rightIconColor = scheme.interactionStatesDisabledTextLinkDisabled
        }
        if (props.type == ODSLinkType.SECONDARY && !props.disabled) {
            style.rightIconColor = scheme.basicText
        }
        if (props.type == ODSLinkType.SECONDARY && props.disabled) {
            style.rightIconColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.type == ODSLinkType.PRIMARY && !props.disabled && state == ODSActions.HOVERED) {
            style.rightIconColor = scheme.interactionStatesHoverTextLinkHover
        }
        if (props.type == ODSLinkType.PRIMARY && !props.disabled && state == ODSActions.PRESSED) {
            style.rightIconColor = scheme.interactionStatesPressedTextLinkPressed
        }
        if (props.type == ODSLinkType.SECONDARY && !props.disabled && state == ODSActions.HOVERED) {
            style.rightIconColor = scheme.interactionStatesHoverTextHover
        }
        if (props.type == ODSLinkType.SECONDARY && !props.disabled && state == ODSActions.PRESSED) {
            style.rightIconColor = scheme.interactionStatesPressedTextPressed
        }
        style.linkTextStyle = DSLinkTokens.linkTextStyle
        style.linkTextAlign = DSLinkTokens.linkTextAlign
        if (props.disabled) {
            style.linkColor = scheme.interactionStatesDisabledTextLinkDisabled
        }
        if (props.type == ODSLinkType.PRIMARY && !props.disabled) {
            style.linkColor = scheme.basicTextLink
        }
        if (props.type == ODSLinkType.SECONDARY && !props.disabled) {
            style.linkColor = scheme.basicText
        }
        if (props.type == ODSLinkType.PRIMARY && !props.disabled && state == ODSActions.HOVERED) {
            style.linkColor = scheme.interactionStatesHoverTextLinkHover
        }
        if (props.type == ODSLinkType.PRIMARY && !props.disabled && state == ODSActions.PRESSED) {
            style.linkColor = scheme.interactionStatesPressedTextLinkPressed
        }
        if (props.type == ODSLinkType.SECONDARY && !props.disabled && state == ODSActions.HOVERED) {
            style.linkColor = scheme.interactionStatesHoverTextHover
        }
        if (props.type == ODSLinkType.SECONDARY && !props.disabled && state == ODSActions.PRESSED) {
            style.linkColor = scheme.interactionStatesPressedTextPressed
        }
        // Not exported from the plugin
        style.underlineThickness = DSLinkTokens.underlineThickness
        if (state == ODSActions.PRESSED && !props.disabled) {
            style.underlineThickness = DSLinkTokens.underlineThicknessStatePressedDisabledFalse
        }
        if (state == ODSActions.HOVERED && !props.disabled) {
            style.underlineThickness = DSLinkTokens.underlineThicknessStateHoveredDisabledFalse
        }
        return style
    }
}
