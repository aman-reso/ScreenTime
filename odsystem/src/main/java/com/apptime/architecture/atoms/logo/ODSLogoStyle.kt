package com.telekom.odsystem.atoms.logo

import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSLogoTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSLogoStyle {
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var width: Dp? = null
    var height: Dp? = null
    var iconColor: HexColor? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSLogoProps,
        state: ODSActions
    ): ODSLogoStyle {
        var style = ODSLogoStyle()
        style.minHeight = DSLogoTokens.minHeight
        style.minWidth = DSLogoTokens.minWidth
        style.width = DSLogoTokens.width
        style.height = DSLogoTokens.height
        if (props.type == ODSLogoType.PRIMARY && state == ODSActions.DEFAULT) {
            style.iconColor = scheme.basicAccent
        }
        if (props.type == ODSLogoType.PRIMARY && state == ODSActions.PRESSED) {
            style.iconColor = scheme.interactionStatesPressedAccentPressed
        }
        if (props.type == ODSLogoType.SECONDARY && state == ODSActions.PRESSED) {
            style.iconColor = scheme.interactionStatesPressedTextPressed
        }
        if (props.type == ODSLogoType.SECONDARY && state == ODSActions.DEFAULT) {
            style.iconColor = scheme.basicText
        }
        return style
    }
}
