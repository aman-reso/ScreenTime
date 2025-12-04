package com.telekom.odsystem.atoms.resultitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSResultItemStyle {
    var gap: Dp? = null
    var borderRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var contentAlignment: Alignment? = null
    var border: Dp? = null
    var borderColor: List<ODSColorModel>? = null
    var backgroundBackgroundColor: List<ODSColorModel>? = null
    var backgroundBorderRadius: ODSCorners? = null
    var backgroundClipContent: Boolean? = null
    var backgroundVerticalAlignment: Alignment.Vertical? = null
    var backgroundHorizontalAlignment: Alignment.Horizontal? = null
    var backgroundVerticalArrangement: Arrangement.Vertical? = null
    var backgroundContentAlignment: Alignment? = null
    var iconContainerBorderRadius: ODSCorners? = null
    var iconContainerWidth: Dp? = null
    var iconContainerHeight: Dp? = null
    var iconContainerVerticalAlignment: Alignment.Vertical? = null
    var iconContainerHorizontalAlignment: Alignment.Horizontal? = null
    var iconContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var odsAiIconContentAlignment: Alignment? = null
    var odsAiIconWidth: Dp? = null
    var odsAiIconHeight: Dp? = null
    var labelContainerGap: Dp? = null
    var labelContainerVerticalAlignment: Alignment.Vertical? = null
    var labelContainerHorizontalAlignment: Alignment.Horizontal? = null
    var labelContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var textRecessiveTextStyle: ODSTextStyle? = null
    var textRecessiveColor: HexColor? = null
    var textRecessiveTextAlign: TextAlign? = null
    var textRecessiveTextOverflow: TextOverflow? = null
    var textPrimaryTextStyle: ODSTextStyle? = null
    var textPrimaryColor: HexColor? = null
    var textPrimaryTextAlign: TextAlign? = null
    var textPrimaryTextOverflow: TextOverflow? = null
    var promptTextStyle: ODSTextStyle? = null
    var promptColor: HexColor? = null
    var promptTextAlign: TextAlign? = null
    var promptTextOverflow: TextOverflow? = null

    @Suppress("LongMethod")
    fun getStyle(
        scheme: ODSTheme,
        props: ODSResultItemProps,
        state: ODSActions,
    ): ODSResultItemStyle {
        val style = ODSResultItemStyle()
        style.gap = DSResultItemTokens.gap
        style.borderRadius = DSResultItemTokens.borderRadius
        style.minHeight = DSResultItemTokens.minHeight
        style.verticalAlignment = DSResultItemTokens.verticalAlignment
        style.horizontalAlignment = DSResultItemTokens.horizontalAlignment
        style.horizontalArrangement = DSResultItemTokens.horizontalArrangement
        style.contentAlignment = DSResultItemTokens.contentAlignment
        style.backgroundBorderRadius = DSResultItemTokens.backgroundBorderRadius
        style.backgroundClipContent = DSResultItemTokens.backgroundClipContent
        style.backgroundBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        style.backgroundVerticalAlignment = DSResultItemTokens.backgroundVerticalAlignment
        style.backgroundHorizontalAlignment = DSResultItemTokens.backgroundHorizontalAlignment
        style.backgroundVerticalArrangement = DSResultItemTokens.backgroundVerticalArrangement
        style.backgroundContentAlignment = DSResultItemTokens.backgroundContentAlignment
        if (state == ODSActions.HOVERED) {
            style.backgroundBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (state == ODSActions.PRESSED) {
            style.backgroundBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
        }
        style.iconContainerBorderRadius = DSResultItemTokens.iconContainerBorderRadius
        style.iconContainerWidth = DSResultItemTokens.iconContainerWidth
        style.iconContainerHeight = DSResultItemTokens.iconContainerHeight
        style.iconContainerVerticalAlignment = DSResultItemTokens.iconContainerVerticalAlignment
        style.iconContainerHorizontalAlignment = DSResultItemTokens.iconContainerHorizontalAlignment
        style.iconContainerHorizontalArrangement =
            DSResultItemTokens.iconContainerHorizontalArrangement
        if (!props.fragMagenta) {
            style.iconColor = scheme.basicText
            style.iconWidth = DSResultItemTokens.iconWidth
            style.iconHeight = DSResultItemTokens.iconHeight
        }
        style.labelContainerGap = DSResultItemTokens.labelContainerGap
        style.labelContainerVerticalAlignment = DSResultItemTokens.labelContainerVerticalAlignment
        style.labelContainerHorizontalAlignment =
            DSResultItemTokens.labelContainerHorizontalAlignment
        style.labelContainerHorizontalArrangement =
            DSResultItemTokens.labelContainerHorizontalArrangement
        if (props.fragMagenta) {
            style.odsAiIconContentAlignment =
                DSResultItemTokens.odsAiIconContentAlignmentFragMagenta
            style.odsAiIconWidth = DSResultItemTokens.odsAiIconWidthFragMagenta
            style.odsAiIconHeight = DSResultItemTokens.odsAiIconHeightFragMagenta
        }
        if (!props.fragMagenta) {
            style.textRecessiveTextStyle = DSResultItemTokens.textRecessiveTextStyle
            style.textRecessiveColor = scheme.basicTextRecessive
            style.textRecessiveTextAlign = DSResultItemTokens.textRecessiveTextAlign
        }
        if (!props.fragMagenta) {
            style.textPrimaryTextStyle = DSResultItemTokens.textPrimaryTextStyle
            style.textPrimaryColor = scheme.basicText
            style.textPrimaryTextAlign = DSResultItemTokens.textPrimaryTextAlign
        }
        if (props.fragMagenta) {
            style.promptTextStyle = DSResultItemTokens.promptTextStyleFragMagenta
            style.promptColor = scheme.basicTextDominant
            style.promptTextAlign = DSResultItemTokens.promptTextAlignFragMagenta
        }
        return style
    }
}
