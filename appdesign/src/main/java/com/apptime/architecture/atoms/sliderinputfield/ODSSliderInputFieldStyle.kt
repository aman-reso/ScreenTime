package com.telekom.odsystem.atoms.sliderinputfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSSliderInputFieldTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("ALL")
class ODSSliderInputFieldStyle {
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var contentGap: Dp? = null
    var contentPadding: ODSPadding? = null
    var contentBorderRadius: ODSCorners? = null
    var contentBorder: Dp? = null
    var contentBorderColor: List<ODSColorModel>? = null
    var contentMinWidth: Dp? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var contentBackgroundColor: List<ODSColorModel>? = null
    var inputValueVerticalAlignment: Alignment.Vertical? = null
    var inputValueHorizontalAlignment: Alignment.Horizontal? = null
    var inputValueHorizontalArrangement: Arrangement.Horizontal? = null
    var prefixTextStyle: ODSTextStyle? = null
    var prefixColor: HexColor? = null
    var prefixTextAlign: TextAlign? = null
    var prefixTextOverflow: TextOverflow? = null
    var inputValueTextStyle: ODSTextStyle? = null
    var inputValueColor: HexColor? = null
    var inputValueTextAlign: TextAlign? = null
    var inputValueTextOverflow: TextOverflow? = null
    var inputValueCursorColor: HexColor? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        state: ODSActions
    ): ODSSliderInputFieldStyle {
        var style = ODSSliderInputFieldStyle()
        style.minHeight = DSSliderInputFieldTokens.minHeight
        style.verticalAlignment = DSSliderInputFieldTokens.verticalAlignment
        style.horizontalAlignment = DSSliderInputFieldTokens.horizontalAlignment
        style.horizontalArrangement = DSSliderInputFieldTokens.horizontalArrangement
        style.contentGap = DSSliderInputFieldTokens.contentGap
        style.contentPadding = DSSliderInputFieldTokens.contentPadding
        style.contentBorderRadius = DSSliderInputFieldTokens.contentBorderRadius
        style.contentBorder = DSSliderInputFieldTokens.contentBorder
        style.contentBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        style.contentMinWidth = DSSliderInputFieldTokens.contentMinWidth
        style.contentVerticalAlignment = DSSliderInputFieldTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSSliderInputFieldTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSSliderInputFieldTokens.contentHorizontalArrangement
        if (state == ODSActions.HOVERED) {
            style.contentBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
            style.contentBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        style.inputValueVerticalAlignment = DSSliderInputFieldTokens.inputValueVerticalAlignment
        style.inputValueHorizontalAlignment = DSSliderInputFieldTokens.inputValueHorizontalAlignment
        style.inputValueHorizontalArrangement =
            DSSliderInputFieldTokens.inputValueHorizontalArrangement
        style.prefixTextStyle = DSSliderInputFieldTokens.prefixTextStyle
        style.prefixColor = scheme.basicTextRecessive
        style.prefixTextAlign = DSSliderInputFieldTokens.prefixTextAlign
        style.prefixTextOverflow = DSSliderInputFieldTokens.prefixTextOverflow
        style.inputValueTextStyle = DSSliderInputFieldTokens.inputValueTextStyle
        style.inputValueColor = scheme.basicText
        style.inputValueTextAlign = DSSliderInputFieldTokens.inputValueTextAlign
        style.inputValueTextOverflow = DSSliderInputFieldTokens.inputValueTextOverflow
        style.inputValueCursorColor = scheme.basicAccent // Not exported from the plugin
        return style
    }
}
