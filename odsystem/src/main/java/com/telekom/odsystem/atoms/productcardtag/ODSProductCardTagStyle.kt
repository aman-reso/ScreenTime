package com.telekom.odsystem.atoms.productcardtag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSProductCardTagStyle {
    var background: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var cornerRadius: ODSCorners? = null
    var height: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var labelStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var labelTextOverflow: TextOverflow? = null // Not exported from the plugin

    fun getStyle(
        scheme: ODSTheme,
        props: ODSProductCardTagProps
    ): ODSProductCardTagStyle {
        val style = ODSProductCardTagStyle()
        style.padding = DSProductCardTagTokens.padding
        style.cornerRadius = DSProductCardTagTokens.cornerRadius
        style.height = DSProductCardTagTokens.height
        style.verticalAlignment = DSProductCardTagTokens.verticalAlignment
        style.horizontalAlignment = DSProductCardTagTokens.horizontalAlignment
        style.horizontalArrangement = DSProductCardTagTokens.horizontalArrangement
        if (props.variant == ODSProductCardTagVariant.DEFAULT) {
            style.background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (props.variant == ODSProductCardTagVariant.DISCOUNT) {
            style.background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        }
        if (props.variant == ODSProductCardTagVariant.PRE_ORDER) {
            style.background = listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard))
        }
        style.labelStyle = DSProductCardTagTokens.labelStyle
        style.labelTextAlign = DSProductCardTagTokens.labelTextAlign
        if (props.variant == ODSProductCardTagVariant.DEFAULT) {
            style.labelColor = scheme.basicText
        }
        if (props.variant == ODSProductCardTagVariant.DISCOUNT) {
            style.labelColor = scheme.basicTextOnAccent
        }
        if (props.variant == ODSProductCardTagVariant.PRE_ORDER) {
            style.labelColor = scheme.basicTextOnAccent
        }
        // Custom addition
        labelTextOverflow = DSProductCardTagTokens.labelTextOverflow
        return style
    }
}
