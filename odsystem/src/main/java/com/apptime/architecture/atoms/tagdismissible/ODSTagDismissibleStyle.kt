package com.telekom.odsystem.atoms.tagdismissible

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSTagDismissibleStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var dismissibleTagGap: Dp? = null
    var dismissibleTagBackgroundColor: List<ODSColorModel>? = null
    var dismissibleTagPadding: ODSPadding? = null
    var dismissibleTagBorderRadius: ODSCorners? = null
    var dismissibleTagVerticalAlignment: Alignment.Vertical? = null
    var dismissibleTagHorizontalAlignment: Alignment.Horizontal? = null
    var dismissibleTagHorizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var labelTextOverflow: TextOverflow? = null
    var closeButtonBorderRadius: ODSCorners? = null
    var closeButtonVerticalAlignment: Alignment.Vertical? = null
    var closeButtonHorizontalAlignment: Alignment.Horizontal? = null
    var closeButtonVerticalArrangement: Arrangement.Vertical? = null
    var closeButtonBackgroundColor: List<ODSColorModel>? = null
    var icon2Color: HexColor? = null
    var icon2Width: Dp? = null
    var icon2Height: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSTagDismissibleProps,
        state: ODSActions
    ): ODSTagDismissibleStyle {
        val style = ODSTagDismissibleStyle()
        style.gap = DSTagDismissibleTokens.gap
        style.padding = DSTagDismissibleTokens.padding
        style.borderRadius = DSTagDismissibleTokens.borderRadius
        style.minHeight = DSTagDismissibleTokens.minHeight
        style.minWidth = DSTagDismissibleTokens.minWidth
        style.verticalAlignment = DSTagDismissibleTokens.verticalAlignment
        style.horizontalAlignment = DSTagDismissibleTokens.horizontalAlignment
        style.horizontalArrangement = DSTagDismissibleTokens.horizontalArrangement
        style.dismissibleTagGap = DSTagDismissibleTokens.dismissibleTagGap
        style.dismissibleTagPadding = DSTagDismissibleTokens.dismissibleTagPadding
        style.dismissibleTagBorderRadius = DSTagDismissibleTokens.dismissibleTagBorderRadius
        style.dismissibleTagVerticalAlignment =
            DSTagDismissibleTokens.dismissibleTagVerticalAlignment
        style.dismissibleTagHorizontalAlignment =
            DSTagDismissibleTokens.dismissibleTagHorizontalAlignment
        style.dismissibleTagHorizontalArrangement =
            DSTagDismissibleTokens.dismissibleTagHorizontalArrangement
        if (props.type == ODSTagDismissibleType.BASIC && !props.disabled) {
            style.dismissibleTagBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (props.type == ODSTagDismissibleType.SUBTLE && !props.disabled) {
            style.dismissibleTagBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
        }
        if (props.type == ODSTagDismissibleType.STRONG && !props.disabled) {
            style.dismissibleTagBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        }
        if (props.type == ODSTagDismissibleType.BASIC && props.disabled) {
            style.dismissibleTagBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        }
        style.iconWidth = DSTagDismissibleTokens.iconWidth
        style.iconHeight = DSTagDismissibleTokens.iconHeight
        if (props.type == ODSTagDismissibleType.SUBTLE && !props.disabled) {
            style.iconColor = scheme.basicText
        }
        if (props.type == ODSTagDismissibleType.BASIC && !props.disabled) {
            style.iconColor = scheme.basicText
        }
        if (props.type == ODSTagDismissibleType.STRONG && !props.disabled) {
            style.iconColor = scheme.basicTextOnAccentSecondary
        }
        if (props.type == ODSTagDismissibleType.BASIC && props.disabled) {
            style.iconColor = scheme.interactionStatesDisabledTextDisabled
        }
        style.labelTextStyle = DSTagDismissibleTokens.labelTextStyle
        style.labelTextAlign = DSTagDismissibleTokens.labelTextAlign
        style.labelTextOverflow = DSTagDismissibleTokens.labelTextOverflow
        if (props.type == ODSTagDismissibleType.SUBTLE && !props.disabled) {
            style.labelColor = scheme.basicText
        }
        if (props.type == ODSTagDismissibleType.BASIC && !props.disabled) {
            style.labelColor = scheme.basicText
        }
        if (props.type == ODSTagDismissibleType.STRONG && !props.disabled) {
            style.labelColor = scheme.basicTextOnAccentSecondary
        }
        if (props.type == ODSTagDismissibleType.BASIC && props.disabled) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        style.closeButtonBorderRadius = DSTagDismissibleTokens.closeButtonBorderRadius
        style.closeButtonVerticalAlignment = DSTagDismissibleTokens.closeButtonVerticalAlignment
        style.closeButtonHorizontalAlignment = DSTagDismissibleTokens.closeButtonHorizontalAlignment
        style.closeButtonVerticalArrangement = DSTagDismissibleTokens.closeButtonVerticalArrangement
        if (props.type == ODSTagDismissibleType.SUBTLE && state == ODSActions.HOVERED && !props.disabled) {
            style.closeButtonBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundSubtleHover))
        }
        if (props.type == ODSTagDismissibleType.BASIC && state == ODSActions.HOVERED && !props.disabled) {
            style.closeButtonBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundSubtleHover))
        }
        if (props.type == ODSTagDismissibleType.SUBTLE && state == ODSActions.PRESSED && !props.disabled) {
            style.closeButtonBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundSubtlePressed))
        }
        if (props.type == ODSTagDismissibleType.BASIC && state == ODSActions.PRESSED && !props.disabled) {
            style.closeButtonBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundSubtlePressed))
        }
        if (props.type == ODSTagDismissibleType.STRONG && state == ODSActions.HOVERED && !props.disabled) {
            style.closeButtonBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentSecondaryHover))
        }
        if (props.type == ODSTagDismissibleType.STRONG && state == ODSActions.PRESSED && !props.disabled) {
            style.closeButtonBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentSecondaryPressed))
        }
        style.icon2Width = DSTagDismissibleTokens.icon2Width
        style.icon2Height = DSTagDismissibleTokens.icon2Height
        if (props.type == ODSTagDismissibleType.SUBTLE && !props.disabled) {
            style.icon2Color = scheme.basicText
        }
        if (props.type == ODSTagDismissibleType.BASIC && !props.disabled) {
            style.icon2Color = scheme.basicText
        }
        if (props.type == ODSTagDismissibleType.STRONG && !props.disabled) {
            style.icon2Color = scheme.basicTextOnAccentSecondary
        }
        if (props.type == ODSTagDismissibleType.BASIC && props.disabled) {
            style.icon2Color = scheme.interactionStatesDisabledTextDisabled
        }
        return style
    }
}
