package com.telekom.odsystem.atoms.switch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSSwitchTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSSwitchStyle {
    var gap: Dp? = null
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var padding: ODSPadding? = null
    var switchIconContainerPadding: ODSPadding? = null
    var switchIconContainerVerticalAlignment: Alignment.Vertical? = null
    var switchIconContainerHorizontalAlignment: Alignment.Horizontal? = null
    var switchIconContainerVerticalArrangement: Arrangement.Vertical? = null
    var labelContainerPadding: ODSPadding? = null
    var labelContainerVerticalAlignment: Alignment.Vertical? = null
    var labelContainerHorizontalAlignment: Alignment.Horizontal? = null
    var labelContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var switchLabelTextStyle: ODSTextStyle? = null
    var switchLabelColor: HexColor? = null
    var switchLabelTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSSwitchProps
    ): ODSSwitchStyle {
        val style = ODSSwitchStyle()
        style.minHeight = DSSwitchTokens.minHeight
        style.verticalAlignment = DSSwitchTokens.verticalAlignment
        style.horizontalAlignment = DSSwitchTokens.horizontalAlignment
        style.horizontalArrangement = DSSwitchTokens.horizontalArrangement
        if (props.size == ODSSwitchSize.LARGE) {
            style.gap = DSSwitchTokens.gapSizeLarge
        }
        if (props.size == ODSSwitchSize.SMALL) {
            style.gap = DSSwitchTokens.gapSizeSmall
            style.padding = DSSwitchTokens.paddingSizeSmall
        }
        style.switchIconContainerVerticalAlignment =
            DSSwitchTokens.switchIconContainerVerticalAlignment
        style.switchIconContainerHorizontalAlignment =
            DSSwitchTokens.switchIconContainerHorizontalAlignment
        style.switchIconContainerVerticalArrangement =
            DSSwitchTokens.switchIconContainerVerticalArrangement
        if (props.size == ODSSwitchSize.LARGE) {
            style.switchIconContainerPadding = DSSwitchTokens.switchIconContainerPaddingSizeLarge
        }
        style.labelContainerVerticalAlignment = DSSwitchTokens.labelContainerVerticalAlignment
        style.labelContainerHorizontalAlignment = DSSwitchTokens.labelContainerHorizontalAlignment
        style.labelContainerHorizontalArrangement =
            DSSwitchTokens.labelContainerHorizontalArrangement
        if (props.size == ODSSwitchSize.LARGE) {
            style.labelContainerPadding = DSSwitchTokens.labelContainerPaddingSizeLarge
        }
        if (props.size == ODSSwitchSize.SMALL) {
            style.labelContainerPadding = DSSwitchTokens.labelContainerPaddingSizeSmall
        }
        style.switchLabelTextAlign = DSSwitchTokens.switchLabelTextAlign
        if (!props.disabled) {
            style.switchLabelColor = scheme.basicText
        }
        if (props.size == ODSSwitchSize.LARGE) {
            style.switchLabelTextStyle = DSSwitchTokens.switchLabelTextStyleSizeLarge
        }
        if (props.size == ODSSwitchSize.SMALL) {
            style.switchLabelTextStyle = DSSwitchTokens.switchLabelTextStyleSizeSmall
        }
        if (!props.readOnly && props.disabled) {
            style.switchLabelColor = scheme.interactionStatesDisabledTextDisabled
        }
        return style
    }
}
