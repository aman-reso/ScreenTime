package com.telekom.odsystem.atoms.flyoutlistitemlarge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSFlyoutListItemLargeTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSFlyoutListItemLargeStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var backgroundColor: List<ODSColorModel>? = null
    var iconBeforeColor: HexColor? = null
    var iconBeforeWidth: Dp? = null
    var iconBeforeHeight: Dp? = null
    var textGap: Dp? = null
    var textVerticalAlignment: Alignment.Vertical? = null
    var textHorizontalAlignment: Alignment.Horizontal? = null
    var textVerticalArrangement: Arrangement.Vertical? = null
    var iconAfterColor: HexColor? = null
    var iconAfterWidth: Dp? = null
    var iconAfterHeight: Dp? = null
    var checkmarkColor: HexColor? = null
    var checkmarkWidth: Dp? = null
    var checkmarkHeight: Dp? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var labelTextOverflow: TextOverflow? = null
    var helperTextTextStyle: ODSTextStyle? = null
    var helperTextColor: HexColor? = null
    var helperTextTextAlign: TextAlign? = null
    var helperTextTextOverflow: TextOverflow? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSFlyoutListItemLargeProps,
        state: ODSActions
    ): ODSFlyoutListItemLargeStyle {
        var style = ODSFlyoutListItemLargeStyle()
        style.gap = DSFlyoutListItemLargeTokens.gap
        style.padding = DSFlyoutListItemLargeTokens.padding
        style.borderRadius = DSFlyoutListItemLargeTokens.borderRadius
        style.minHeight = DSFlyoutListItemLargeTokens.minHeight
        style.verticalAlignment = DSFlyoutListItemLargeTokens.verticalAlignment
        style.horizontalAlignment = DSFlyoutListItemLargeTokens.horizontalAlignment
        style.horizontalArrangement = DSFlyoutListItemLargeTokens.horizontalArrangement
        if (state == ODSActions.HOVERED && !props.disabled) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (state == ODSActions.PRESSED && !props.disabled) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
        }
        style.iconBeforeWidth = DSFlyoutListItemLargeTokens.iconBeforeWidth
        style.iconBeforeHeight = DSFlyoutListItemLargeTokens.iconBeforeHeight
        if (props.disabled) {
            style.iconBeforeColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.disabled) {
            style.iconBeforeColor = scheme.basicText
        }
        style.textGap = DSFlyoutListItemLargeTokens.textGap
        style.textVerticalAlignment = DSFlyoutListItemLargeTokens.textVerticalAlignment
        style.textHorizontalAlignment = DSFlyoutListItemLargeTokens.textHorizontalAlignment
        style.textVerticalArrangement = DSFlyoutListItemLargeTokens.textVerticalArrangement
        if (props.variant == ODSFlyoutListItemLargeVariant.STANDARD) {
            style.iconAfterColor = scheme.basicText
            style.iconAfterWidth = DSFlyoutListItemLargeTokens.iconAfterWidthVariantStandard
            style.iconAfterHeight = DSFlyoutListItemLargeTokens.iconAfterHeightVariantStandard
        }
        if (props.variant == ODSFlyoutListItemLargeVariant.CHECKED) {
            style.checkmarkWidth = DSFlyoutListItemLargeTokens.checkmarkWidthVariantChecked
            style.checkmarkHeight = DSFlyoutListItemLargeTokens.checkmarkHeightVariantChecked
        }
        if (props.variant == ODSFlyoutListItemLargeVariant.CHECKED && !props.disabled) {
            style.checkmarkColor = scheme.basicText
        }
        if (props.variant == ODSFlyoutListItemLargeVariant.CHECKED && props.disabled) {
            style.checkmarkColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (state == ODSActions.HOVERED && props.variant == ODSFlyoutListItemLargeVariant.CHECKED && !props.disabled) {
            style.checkmarkColor = scheme.interactionStatesHoverTextHover
        }
        if (state == ODSActions.PRESSED && props.variant == ODSFlyoutListItemLargeVariant.CHECKED && !props.disabled) {
            style.checkmarkColor = scheme.interactionStatesPressedTextPressed
        }
        style.labelTextStyle = DSFlyoutListItemLargeTokens.labelTextStyle
        style.labelTextAlign = DSFlyoutListItemLargeTokens.labelTextAlign
        style.labelTextOverflow = DSFlyoutListItemLargeTokens.labelTextOverflow
        if (props.disabled) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.disabled) {
            style.labelColor = scheme.basicText
        }
        style.helperTextTextStyle = DSFlyoutListItemLargeTokens.helperTextTextStyle
        style.helperTextTextAlign = DSFlyoutListItemLargeTokens.helperTextTextAlign
        style.helperTextTextOverflow = DSFlyoutListItemLargeTokens.helperTextTextOverflow
        if (props.disabled) {
            style.helperTextColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.disabled) {
            style.helperTextColor = scheme.basicTextRecessive
        }
        return style
    }
}
