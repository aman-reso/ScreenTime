package com.telekom.odsystem.atoms.flyoutlistitemsmall

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSFlyoutListItemSmallTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSFlyoutListItemSmallStyle {
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
        props: ODSFlyoutListItemSmallProps,
        state: ODSActions
    ): ODSFlyoutListItemSmallStyle {
        val style = ODSFlyoutListItemSmallStyle()
        style.gap = DSFlyoutListItemSmallTokens.gap
        style.padding = DSFlyoutListItemSmallTokens.padding
        style.borderRadius = DSFlyoutListItemSmallTokens.borderRadius
        style.minHeight = DSFlyoutListItemSmallTokens.minHeight
        style.verticalAlignment = DSFlyoutListItemSmallTokens.verticalAlignment
        style.horizontalAlignment = DSFlyoutListItemSmallTokens.horizontalAlignment
        style.horizontalArrangement = DSFlyoutListItemSmallTokens.horizontalArrangement
        if (state == ODSActions.HOVERED && !props.disabled) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (state == ODSActions.PRESSED && !props.disabled) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
        }
        style.iconBeforeWidth = DSFlyoutListItemSmallTokens.iconBeforeWidth
        style.iconBeforeHeight = DSFlyoutListItemSmallTokens.iconBeforeHeight
        if (props.disabled) {
            style.iconBeforeColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.disabled) {
            style.iconBeforeColor = scheme.basicText
        }
        style.textGap = DSFlyoutListItemSmallTokens.textGap
        style.textVerticalAlignment = DSFlyoutListItemSmallTokens.textVerticalAlignment
        style.textHorizontalAlignment = DSFlyoutListItemSmallTokens.textHorizontalAlignment
        style.textVerticalArrangement = DSFlyoutListItemSmallTokens.textVerticalArrangement
        if (props.variant == ODSFlyoutListItemSmallVariant.STANDARD) {
            style.iconAfterColor = scheme.basicText
            style.iconAfterWidth = DSFlyoutListItemSmallTokens.iconAfterWidthVariantStandard
            style.iconAfterHeight = DSFlyoutListItemSmallTokens.iconAfterHeightVariantStandard
        }
        if (props.variant == ODSFlyoutListItemSmallVariant.CHECKED) {
            style.checkmarkWidth = DSFlyoutListItemSmallTokens.checkmarkWidthVariantChecked
            style.checkmarkHeight = DSFlyoutListItemSmallTokens.checkmarkHeightVariantChecked
        }
        if (props.variant == ODSFlyoutListItemSmallVariant.CHECKED && !props.disabled) {
            style.checkmarkColor = scheme.basicText
        }
        if (props.variant == ODSFlyoutListItemSmallVariant.CHECKED && props.disabled) {
            style.checkmarkColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (state == ODSActions.HOVERED && props.variant == ODSFlyoutListItemSmallVariant.CHECKED && !props.disabled) {
            style.checkmarkColor = scheme.interactionStatesHoverTextHover
        }
        if (state == ODSActions.PRESSED && props.variant == ODSFlyoutListItemSmallVariant.CHECKED && !props.disabled) {
            style.checkmarkColor = scheme.interactionStatesPressedTextPressed
        }
        style.labelTextStyle = DSFlyoutListItemSmallTokens.labelTextStyle
        style.labelTextAlign = DSFlyoutListItemSmallTokens.labelTextAlign
        style.labelTextOverflow = DSFlyoutListItemSmallTokens.labelTextOverflow
        if (props.disabled) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.disabled) {
            style.labelColor = scheme.basicText
        }
        style.helperTextTextStyle = DSFlyoutListItemSmallTokens.helperTextTextStyle
        style.helperTextTextAlign = DSFlyoutListItemSmallTokens.helperTextTextAlign
        style.helperTextTextOverflow = DSFlyoutListItemSmallTokens.helperTextTextOverflow
        if (props.disabled) {
            style.helperTextColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.disabled) {
            style.helperTextColor = scheme.basicTextRecessive
        }
        return style
    }
}
