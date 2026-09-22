package com.telekom.odsystem.atoms.sliderinputfieldgroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSSliderInputFieldGroupTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSSliderInputFieldGroupStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var labelMinGap: Dp? = null
    var labelMinVerticalAlignment: Alignment.Vertical? = null
    var labelMinHorizontalAlignment: Alignment.Horizontal? = null
    var labelMinVerticalArrangement: Arrangement.Vertical? = null
    var labelVerticalAlignment: Alignment.Vertical? = null
    var labelHorizontalAlignment: Alignment.Horizontal? = null
    var labelHorizontalArrangement: Arrangement.Horizontal? = null
    var labelMaxGap: Dp? = null
    var labelMaxVerticalAlignment: Alignment.Vertical? = null
    var labelMaxHorizontalAlignment: Alignment.Horizontal? = null
    var labelMaxVerticalArrangement: Arrangement.Vertical? = null
    var labelMaxHorizontalArrangement: Arrangement.Horizontal? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var labelTextOverflow: TextOverflow? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSSliderInputFieldGroupProps
    ): ODSSliderInputFieldGroupStyle {
        var style = ODSSliderInputFieldGroupStyle()
        style.verticalAlignment = DSSliderInputFieldGroupTokens.verticalAlignment
        style.horizontalAlignment = DSSliderInputFieldGroupTokens.horizontalAlignment
        if (props.variant == ODSSliderInputFieldGroupVariant.SINGLE) {
            style.gap = DSSliderInputFieldGroupTokens.gapVariantSingle
            style.horizontalArrangement =
                DSSliderInputFieldGroupTokens.horizontalArrangementVariantSingle
        }
        if (props.variant == ODSSliderInputFieldGroupVariant.STACKED) {
            style.gap = DSSliderInputFieldGroupTokens.gapVariantStacked
            style.verticalArrangement =
                DSSliderInputFieldGroupTokens.verticalArrangementVariantStacked
        }
        if (props.variant == ODSSliderInputFieldGroupVariant.SIDE_BY_SIDE) {
            style.gap = DSSliderInputFieldGroupTokens.gapVariantSideBySide
            style.horizontalArrangement =
                DSSliderInputFieldGroupTokens.horizontalArrangementVariantSideBySide
        }
        style.labelMinGap = DSSliderInputFieldGroupTokens.labelMinGap
        style.labelMinVerticalAlignment = DSSliderInputFieldGroupTokens.labelMinVerticalAlignment
        style.labelMinHorizontalAlignment =
            DSSliderInputFieldGroupTokens.labelMinHorizontalAlignment
        style.labelMinVerticalArrangement =
            DSSliderInputFieldGroupTokens.labelMinVerticalArrangement
        style.labelVerticalAlignment = DSSliderInputFieldGroupTokens.labelVerticalAlignment
        style.labelHorizontalAlignment = DSSliderInputFieldGroupTokens.labelHorizontalAlignment
        style.labelHorizontalArrangement = DSSliderInputFieldGroupTokens.labelHorizontalArrangement
        style.labelMaxGap = DSSliderInputFieldGroupTokens.labelMaxGap
        style.labelMaxHorizontalAlignment =
            DSSliderInputFieldGroupTokens.labelMaxHorizontalAlignment
        if (props.variant == ODSSliderInputFieldGroupVariant.SINGLE) {
            style.labelMaxVerticalAlignment =
                DSSliderInputFieldGroupTokens.labelMaxVerticalAlignmentVariantSingle
            style.labelMaxHorizontalArrangement =
                DSSliderInputFieldGroupTokens.labelMaxHorizontalArrangementVariantSingle
        }
        if (props.variant == ODSSliderInputFieldGroupVariant.STACKED) {
            style.labelMaxVerticalAlignment =
                DSSliderInputFieldGroupTokens.labelMaxVerticalAlignmentVariantStacked
            style.labelMaxVerticalArrangement =
                DSSliderInputFieldGroupTokens.labelMaxVerticalArrangementVariantStacked
        }
        if (props.variant == ODSSliderInputFieldGroupVariant.SIDE_BY_SIDE) {
            style.labelMaxVerticalAlignment =
                DSSliderInputFieldGroupTokens.labelMaxVerticalAlignmentVariantSideBySide
            style.labelMaxVerticalArrangement =
                DSSliderInputFieldGroupTokens.labelMaxVerticalArrangementVariantSideBySide
        }
        style.labelTextStyle = DSSliderInputFieldGroupTokens.labelTextStyle
        style.labelColor = scheme.basicText
        style.labelTextAlign = DSSliderInputFieldGroupTokens.labelTextAlign
        style.labelTextOverflow = DSSliderInputFieldGroupTokens.labelTextOverflow
        return style
    }
}
