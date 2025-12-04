package com.telekom.odsystem.molecules.searchbar

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

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: a705149
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-18282
 */

class ODSSearchBarStyle {
    var gap: Dp? = null
    var background: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var cornerRadius: ODSCorners? = null
    var border: Dp? = null
    var borderColor: List<ODSColorModel>? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var contentClipContent: Boolean? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var placeholderStyle: ODSTextStyle? = null
    var placeholderColor: HexColor? = null
    var placeholderTextAlign: TextAlign? = null
    var placeholderOverflow: TextOverflow? = null
    var placeholderMaxLines: Int? = null
    var inputValueStyle: ODSTextStyle? = null
    var inputValueColor: HexColor? = null
    var inputValueTextAlign: TextAlign? = null
    var inputValueOverflow: TextOverflow? = null
    var inputValueMaxLines: Int? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSSearchBarProps,
        state: ODSActions
    ): ODSSearchBarStyle {
        val style = ODSSearchBarStyle()
        style.gap = DSSearchBarTokens.gap
        style.padding = DSSearchBarTokens.padding
        style.cornerRadius = DSSearchBarTokens.cornerRadius
        style.border = DSSearchBarTokens.border
        style.verticalAlignment = DSSearchBarTokens.verticalAlignment
        style.horizontalAlignment = DSSearchBarTokens.horizontalAlignment
        style.horizontalArrangement = DSSearchBarTokens.horizontalArrangement
        if (!props.disabled) {
            style.background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
            style.borderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.disabled && !props.filled) {
            style.background =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.borderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (state == ODSActions.HOVERED && !props.disabled) {
            style.background =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        style.contentClipContent = DSSearchBarTokens.contentClipContent
        style.contentVerticalAlignment = DSSearchBarTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSSearchBarTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSSearchBarTokens.contentHorizontalArrangement
        style.placeholderStyle = DSSearchBarTokens.placeholderStyle
        style.placeholderTextAlign = DSSearchBarTokens.placeholderTextAlign
        style.placeholderOverflow = DSSearchBarTokens.placeholderOverflow
        style.placeholderMaxLines = DSSearchBarTokens.placeholderMaxLines
        if (!props.disabled) {
            style.placeholderColor = scheme.basicTextRecessive
        }
        if (props.disabled && !props.filled) {
            style.placeholderColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.inputValueStyle = DSSearchBarTokens.inputValueStyle
        style.inputValueTextAlign = DSSearchBarTokens.inputValueTextAlign
        style.inputValueOverflow = DSSearchBarTokens.inputValueOverflow
        style.inputValueMaxLines = DSSearchBarTokens.inputValueMaxLines
        if (!props.disabled) {
            style.inputValueColor = scheme.basicText
        }
        if (props.disabled && !props.filled) {
            style.inputValueColor = scheme.interactionStatesDisabledTextDisabled
        }
        return style
    }
}
