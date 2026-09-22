package com.telekom.odsystem.atoms.tagstatic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSTagStaticTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSTagStaticStyle {
    var gap: Dp? = null
    var backgroundColor: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var labelTextOverflow: TextOverflow? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSTagStaticProps
    ): ODSTagStaticStyle {
        val style = ODSTagStaticStyle()
        style.gap = DSTagStaticTokens.gap
        style.padding = DSTagStaticTokens.padding
        style.borderRadius = DSTagStaticTokens.borderRadius
        style.verticalAlignment = DSTagStaticTokens.verticalAlignment
        style.horizontalAlignment = DSTagStaticTokens.horizontalAlignment
        style.horizontalArrangement = DSTagStaticTokens.horizontalArrangement
        if (props.type == ODSTagStaticType.BASIC && !props.disabled) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (props.type == ODSTagStaticType.SUBTLE && !props.disabled) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
        }
        if (props.type == ODSTagStaticType.STRONG && !props.disabled) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        }
        if (props.type == ODSTagStaticType.BASIC && props.disabled) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        }
        if (props.type == ODSTagStaticType.SAVINGS && !props.disabled) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.shadesAccentShadesAccentSubtle))
        }
        if (props.type == ODSTagStaticType.PROMOTION && !props.disabled) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        }
        if (props.type == ODSTagStaticType.WARNING && !props.disabled) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalWarningSubtle))
        }
        if (props.type == ODSTagStaticType.SUCCESS && !props.disabled) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalSuccessSubtle))
        }
        if (props.type == ODSTagStaticType.ERROR && !props.disabled) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveSubtle))
        }
        style.iconWidth = DSTagStaticTokens.iconWidth
        style.iconHeight = DSTagStaticTokens.iconHeight
        if (props.type == ODSTagStaticType.SUBTLE && !props.disabled) {
            style.iconColor = scheme.basicText
        }
        if (props.type == ODSTagStaticType.BASIC && !props.disabled) {
            style.iconColor = scheme.basicText
        }
        if (props.type == ODSTagStaticType.STRONG && !props.disabled) {
            style.iconColor = scheme.basicTextOnAccentSecondary
        }
        if (props.type == ODSTagStaticType.BASIC && props.disabled) {
            style.iconColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.type == ODSTagStaticType.SAVINGS && !props.disabled) {
            style.iconColor = scheme.shadesAccentShadesAccentExtraRecessive
        }
        if (props.type == ODSTagStaticType.PROMOTION && !props.disabled) {
            style.iconColor = scheme.basicTextOnAccent
        }
        if (props.type == ODSTagStaticType.WARNING && !props.disabled) {
            style.iconColor = scheme.functionalWarningStandard
        }
        if (props.type == ODSTagStaticType.SUCCESS && !props.disabled) {
            style.iconColor = scheme.functionalSuccessStandard
        }
        if (props.type == ODSTagStaticType.ERROR && !props.disabled) {
            style.iconColor = scheme.functionalDestructiveStandard
        }
        style.labelTextStyle = DSTagStaticTokens.labelTextStyle
        style.labelTextAlign = DSTagStaticTokens.labelTextAlign
        style.labelTextOverflow = DSTagStaticTokens.labelTextOverflow
        if (props.type == ODSTagStaticType.SUBTLE && !props.disabled) {
            style.labelColor = scheme.basicText
        }
        if (props.type == ODSTagStaticType.BASIC && !props.disabled) {
            style.labelColor = scheme.basicText
        }
        if (props.type == ODSTagStaticType.STRONG && !props.disabled) {
            style.labelColor = scheme.basicTextOnAccentSecondary
        }
        if (props.type == ODSTagStaticType.BASIC && props.disabled) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.type == ODSTagStaticType.SAVINGS && !props.disabled) {
            style.labelColor = scheme.shadesAccentShadesAccentExtraRecessive
        }
        if (props.type == ODSTagStaticType.PROMOTION && !props.disabled) {
            style.labelColor = scheme.basicTextOnAccent
        }
        if (props.type == ODSTagStaticType.WARNING && !props.disabled) {
            style.labelColor = scheme.functionalWarningStandard
        }
        if (props.type == ODSTagStaticType.SUCCESS && !props.disabled) {
            style.labelColor = scheme.functionalSuccessStandard
        }
        if (props.type == ODSTagStaticType.ERROR && !props.disabled) {
            style.labelColor = scheme.functionalDestructiveStandard
        }
        return style
    }
}
