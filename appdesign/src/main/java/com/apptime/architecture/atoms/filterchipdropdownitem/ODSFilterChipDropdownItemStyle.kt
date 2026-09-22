package com.telekom.odsystem.atoms.filterchipdropdownitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSFilterChipDropdownItemTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSFilterChipDropdownItemStyle {
    var padding: ODSPadding? = null
    var cornerRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var background: List<ODSColorModel>? = null
    var contentFrameGap: Dp? = null
    var contentFrameVerticalAlignment: Alignment.Vertical? = null
    var contentFrameHorizontalAlignment: Alignment.Horizontal? = null
    var contentFrameHorizontalArrangement: Arrangement.Horizontal? = null
    var leftIconColor: HexColor? = null
    var leftIconWidth: Dp? = null
    var leftIconHeight: Dp? = null
    var labelStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var labelOverflow: TextOverflow? = null
    var iconContainerPadding: ODSPadding? = null
    var iconContainerWidth: Dp? = null
    var iconContainerVerticalAlignment: Alignment.Vertical? = null
    var iconContainerHorizontalAlignment: Alignment.Horizontal? = null
    var iconContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var checkmarkColor: HexColor? = null
    var checkmarkWidth: Dp? = null
    var checkmarkHeight: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSFilterChipDropdownItemProps,
        state: ODSActions
    ): ODSFilterChipDropdownItemStyle {
        val style = ODSFilterChipDropdownItemStyle()
        style.padding = DSFilterChipDropdownItemTokens.padding
        style.cornerRadius = DSFilterChipDropdownItemTokens.cornerRadius
        style.minHeight = DSFilterChipDropdownItemTokens.minHeight
        style.minWidth = DSFilterChipDropdownItemTokens.minWidth
        style.verticalAlignment = DSFilterChipDropdownItemTokens.verticalAlignment
        style.horizontalArrangement = DSFilterChipDropdownItemTokens.horizontalArrangement
        if (state == ODSActions.HOVERED && !props.disabled) {
            style.background =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (state == ODSActions.PRESSED && !props.disabled) {
            style.background =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
        }
        style.contentFrameGap = DSFilterChipDropdownItemTokens.contentFrameGap
        style.contentFrameVerticalAlignment =
            DSFilterChipDropdownItemTokens.contentFrameVerticalAlignment
        style.contentFrameHorizontalAlignment =
            DSFilterChipDropdownItemTokens.contentFrameHorizontalAlignment
        style.contentFrameHorizontalArrangement =
            DSFilterChipDropdownItemTokens.contentFrameHorizontalArrangement
        style.leftIconWidth = DSFilterChipDropdownItemTokens.leftIconWidth
        style.leftIconHeight = DSFilterChipDropdownItemTokens.leftIconHeight
        if (props.disabled) {
            style.leftIconColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.disabled) {
            style.leftIconColor = scheme.basicText
        }
        style.labelStyle = DSFilterChipDropdownItemTokens.labelStyle
        style.labelTextAlign = DSFilterChipDropdownItemTokens.labelTextAlign
        style.labelOverflow = DSFilterChipDropdownItemTokens.labelOverflow
        if (props.disabled) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.disabled) {
            style.labelColor = scheme.basicText
        }
        style.iconContainerPadding = DSFilterChipDropdownItemTokens.iconContainerPadding
        style.iconContainerWidth = DSFilterChipDropdownItemTokens.iconContainerWidth
        style.iconContainerVerticalAlignment =
            DSFilterChipDropdownItemTokens.iconContainerVerticalAlignment
        style.iconContainerHorizontalAlignment =
            DSFilterChipDropdownItemTokens.iconContainerHorizontalAlignment
        style.iconContainerHorizontalArrangement =
            DSFilterChipDropdownItemTokens.iconContainerHorizontalArrangement
        style.checkmarkWidth = DSFilterChipDropdownItemTokens.checkmarkWidth
        style.checkmarkHeight = DSFilterChipDropdownItemTokens.checkmarkHeight
        if (!props.selected) {
            style.checkmarkColor = scheme.basicText
        }
        if (state == ODSActions.HOVERED) {
            style.checkmarkColor = scheme.basicText
        }
        if (state == ODSActions.PRESSED) {
            style.checkmarkColor = scheme.basicText
        }
        if (props.selected && !props.disabled) {
            style.checkmarkColor = scheme.basicText
        }
        if (props.selected && props.disabled) {
            style.checkmarkColor = scheme.interactionStatesDisabledTextDisabled
        }
        return style
    }
}
