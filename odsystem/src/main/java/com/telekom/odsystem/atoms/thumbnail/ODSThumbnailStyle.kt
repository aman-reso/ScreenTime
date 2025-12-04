package com.telekom.odsystem.atoms.thumbnail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-01 (v1.32.3) - uid: 1dc2b9ea
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=7904-10948
 */

class ODSThumbnailStyle {
    var cornerRadius: ODSCorners? = null
    var width: Dp? = null
    var height: Dp? = null
    var clipContent: Boolean? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var imageContentScale: ContentScale? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSThumbnailProps
    ): ODSThumbnailStyle {
        val style = ODSThumbnailStyle()
        style.width = DSThumbnailTokens.width
        style.height = DSThumbnailTokens.height
        style.clipContent = DSThumbnailTokens.clipContent
        style.verticalAlignment = DSThumbnailTokens.verticalAlignment
        style.horizontalAlignment = DSThumbnailTokens.horizontalAlignment
        style.horizontalArrangement = DSThumbnailTokens.horizontalArrangement
        if (props.type == ODSThumbnailType.IMAGE) {
            style.cornerRadius = DSThumbnailTokens.cornerRadiusTypeImage
        }
        if (props.type == ODSThumbnailType.IMAGE) {
            style.imageContentScale = DSThumbnailTokens.imageContentScaleTypeImage
        }
        if (props.type == ODSThumbnailType.ICON) {
            style.iconColor = scheme.basicText
            style.iconWidth = DSThumbnailTokens.iconWidthTypeIcon
            style.iconHeight = DSThumbnailTokens.iconHeightTypeIcon
        }
        return style
    }
}
