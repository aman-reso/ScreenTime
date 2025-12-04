package com.telekom.odsystem.organisms.cardchoicesimple

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSCardChoiceSimpleStyle {
    var width: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var containerGap: Dp? = null
    var containerPadding: ODSPadding? = null
    var containerMinHeight: Dp? = null
    var containerVerticalAlignment: Alignment.Vertical? = null
    var containerHorizontalAlignment: Alignment.Horizontal? = null
    var containerVerticalArrangement: Arrangement.Vertical? = null
    var cardBgBackgroundColor: List<ODSColorModel>? = null
    var cardBgBorderRadius: ODSCorners? = null
    var cardBgWidth: Dp? = null
    var cardBgHeight: Dp? = null
    var cardBgClipContent: Boolean? = null
    var cardBgVerticalAlignment: Alignment.Vertical? = null
    var cardBgHorizontalAlignment: Alignment.Horizontal? = null
    var cardBgVerticalArrangement: Arrangement.Vertical? = null
    var cardBgBorder: Dp? = null
    var cardBgBorderColor: List<ODSColorModel>? = null
    var contentGap: Dp? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var leftContentGap: Dp? = null
    var leftContentVerticalAlignment: Alignment.Vertical? = null
    var leftContentHorizontalAlignment: Alignment.Horizontal? = null
    var leftContentVerticalArrangement: Arrangement.Vertical? = null
    var rightContentContainerVerticalAlignment: Alignment.Vertical? = null
    var rightContentContainerHorizontalAlignment: Alignment.Horizontal? = null
    var rightContentContainerVerticalArrangement: Arrangement.Vertical? = null
    var bottomSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var bottomSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var bottomSlotContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var labelTopTextStyle: ODSTextStyle? = null
    var labelTopColor: HexColor? = null
    var labelTopTextAlign: TextAlign? = null
    var headingTextStyle: ODSTextStyle? = null
    var headingColor: HexColor? = null
    var headingTextAlign: TextAlign? = null
    var labelBottomTextStyle: ODSTextStyle? = null
    var labelBottomColor: HexColor? = null
    var labelBottomTextAlign: TextAlign? = null
    var scaleFactor: Float? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardChoiceSimpleProps,
        state: ODSActions
    ): ODSCardChoiceSimpleStyle {
        var style = ODSCardChoiceSimpleStyle()
        style.width = DSCardChoiceSimpleTokens.width
        style.verticalAlignment = DSCardChoiceSimpleTokens.verticalAlignment
        style.horizontalAlignment = DSCardChoiceSimpleTokens.horizontalAlignment
        style.verticalArrangement = DSCardChoiceSimpleTokens.verticalArrangement
        style.containerGap = DSCardChoiceSimpleTokens.containerGap
        style.containerPadding = DSCardChoiceSimpleTokens.containerPadding
        style.containerMinHeight = DSCardChoiceSimpleTokens.containerMinHeight
        style.containerVerticalAlignment = DSCardChoiceSimpleTokens.containerVerticalAlignment
        style.containerHorizontalAlignment = DSCardChoiceSimpleTokens.containerHorizontalAlignment
        style.containerVerticalArrangement = DSCardChoiceSimpleTokens.containerVerticalArrangement
        style.cardBgBorderRadius = DSCardChoiceSimpleTokens.cardBgBorderRadius
        style.cardBgWidth = DSCardChoiceSimpleTokens.cardBgWidth
        style.cardBgHeight = DSCardChoiceSimpleTokens.cardBgHeight
        style.cardBgClipContent = DSCardChoiceSimpleTokens.cardBgClipContent
        style.cardBgVerticalAlignment = DSCardChoiceSimpleTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardChoiceSimpleTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardChoiceSimpleTokens.cardBgVerticalArrangement
        if (props.selected) {
            style.cardBgBorder = DSCardChoiceSimpleTokens.cardBgBorderSelected
        }
        if (state == ODSActions.PRESSED) {
            style.cardBgWidth = DSCardChoiceSimpleTokens.cardBgWidthStatePressed
            style.cardBgHeight = DSCardChoiceSimpleTokens.cardBgHeightStatePressed
        }
        if (state == ODSActions.HOVERED) {
            style.cardBgWidth = DSCardChoiceSimpleTokens.cardBgWidthStateHovered
            style.cardBgHeight = DSCardChoiceSimpleTokens.cardBgHeightStateHovered
        }
        if (props.type == ODSCardChoiceSimpleType.FILLED) {
            style.cardBgBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        }
        if (props.type == ODSCardChoiceSimpleType.OUTLINE) {
            style.cardBgBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackground))
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.type == ODSCardChoiceSimpleType.OUTLINE && !props.selected) {
            style.cardBgBorder = DSCardChoiceSimpleTokens.cardBgBorderVariantOutline
        }
        if (props.type == ODSCardChoiceSimpleType.FILLED && props.selected) {
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        style.contentGap = DSCardChoiceSimpleTokens.contentGap
        style.contentVerticalAlignment = DSCardChoiceSimpleTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSCardChoiceSimpleTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSCardChoiceSimpleTokens.contentHorizontalArrangement
        style.leftContentGap = DSCardChoiceSimpleTokens.leftContentGap
        style.leftContentVerticalAlignment = DSCardChoiceSimpleTokens.leftContentVerticalAlignment
        style.leftContentHorizontalAlignment =
            DSCardChoiceSimpleTokens.leftContentHorizontalAlignment
        style.leftContentVerticalArrangement =
            DSCardChoiceSimpleTokens.leftContentVerticalArrangement
        style.rightContentContainerVerticalAlignment =
            DSCardChoiceSimpleTokens.rightContentContainerVerticalAlignment
        style.rightContentContainerHorizontalAlignment =
            DSCardChoiceSimpleTokens.rightContentContainerHorizontalAlignment
        style.rightContentContainerVerticalArrangement =
            DSCardChoiceSimpleTokens.rightContentContainerVerticalArrangement
        style.bottomSlotContainerVerticalAlignment =
            DSCardChoiceSimpleTokens.bottomSlotContainerVerticalAlignment
        style.bottomSlotContainerHorizontalAlignment =
            DSCardChoiceSimpleTokens.bottomSlotContainerHorizontalAlignment
        style.bottomSlotContainerHorizontalArrangement =
            DSCardChoiceSimpleTokens.bottomSlotContainerHorizontalArrangement
        style.labelTopTextStyle = DSCardChoiceSimpleTokens.labelTopTextStyle
        style.labelTopColor = scheme.basicText
        style.labelTopTextAlign = DSCardChoiceSimpleTokens.labelTopTextAlign
        style.headingTextStyle = DSCardChoiceSimpleTokens.headingTextStyle
        style.headingColor = scheme.basicText
        style.headingTextAlign = DSCardChoiceSimpleTokens.headingTextAlign
        style.labelBottomTextStyle = DSCardChoiceSimpleTokens.labelBottomTextStyle
        style.labelBottomColor = scheme.basicText
        style.labelBottomTextAlign = DSCardChoiceSimpleTokens.labelBottomTextAlign
        style.scaleFactor = DSCardChoiceSimpleTokens.scaleFactor
        return style
    }
}
