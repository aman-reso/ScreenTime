package com.telekom.odsystem.slots.textfieldicon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-10-20 (v1.33.1) - uid: 3e102fec
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=147-12487
 */

class ODSTextFieldIconStyle {
    var width: Dp? = null
    var height: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSTextFieldIconProps,
    ): ODSTextFieldIconStyle {
        val style = ODSTextFieldIconStyle()
        style.width = DSTextFieldIconTokens.width
        style.height = DSTextFieldIconTokens.height
        style.verticalAlignment = DSTextFieldIconTokens.verticalAlignment
        style.horizontalAlignment = DSTextFieldIconTokens.horizontalAlignment
        style.horizontalArrangement = DSTextFieldIconTokens.horizontalArrangement
        if (props.type == ODSTextFieldIconType.ICON_CONTAINER) {
            style.iconColor = scheme.basicTextRecessive
        }
        if (props.size == ODSTextFieldIconSize.LARGE && props.type == ODSTextFieldIconType.ICON_CONTAINER) {
            style.iconWidth = DSTextFieldIconTokens.iconWidthSizeLargeTypeIconContainer
            style.iconHeight = DSTextFieldIconTokens.iconHeightSizeLargeTypeIconContainer
        }
        if (props.size == ODSTextFieldIconSize.SMALL && props.type == ODSTextFieldIconType.ICON_CONTAINER) {
            style.iconWidth = DSTextFieldIconTokens.iconWidthSizeSmallTypeIconContainer
            style.iconHeight = DSTextFieldIconTokens.iconHeightSizeSmallTypeIconContainer
        }
        return style
    }
}
