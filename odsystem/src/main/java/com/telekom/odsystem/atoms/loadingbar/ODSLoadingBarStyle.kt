package com.telekom.odsystem.atoms.loadingbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-12 (v1.31.6) - uid: 747e6161
 * Figma link: https://figma.com/design/MpQgyLR8JN6QeprILJwaD4/ODS_Feedback-Components_Exploration?node-id=1501-51354
 */

class ODSLoadingBarStyle {
    var zStackClipContent: Boolean? = null
    var zStackContentAlignment: Alignment? = null
    var cornerRadius: ODSCorners? = null
    var clipContent: Boolean? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var strokeAbsoluteOffset: ODSOffset? = null
    var strokeAbsoluteContentAlignment: Alignment? = null
    var strokeBackground: List<ODSColorModel>? = null
    var strokeCornerRadius: ODSCorners? = null // Not used in mobile
    var strokeHeight: Dp? = null
    var strokeVerticalAlignment: Alignment.Vertical? = null // Not used in mobile
    var strokeHorizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var strokeHorizontalArrangement: Arrangement.Horizontal? = null // Not used in mobile
    fun getStyle(
        scheme: ODSTheme
    ): ODSLoadingBarStyle {
        val style = ODSLoadingBarStyle()
        style.zStackClipContent = DSLoadingBarTokens.zStackClipContent
        style.zStackContentAlignment = DSLoadingBarTokens.zStackContentAlignment
        style.cornerRadius = DSLoadingBarTokens.cornerRadius
        style.clipContent = DSLoadingBarTokens.clipContent
        style.verticalAlignment = DSLoadingBarTokens.verticalAlignment
        style.horizontalAlignment = DSLoadingBarTokens.horizontalAlignment
        style.verticalArrangement = DSLoadingBarTokens.verticalArrangement
        style.contentAlignment = DSLoadingBarTokens.contentAlignment
        style.strokeAbsoluteOffset = DSLoadingBarTokens.strokeAbsoluteOffset
        style.strokeAbsoluteContentAlignment = DSLoadingBarTokens.strokeAbsoluteContentAlignment
        style.strokeBackground = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        style.strokeCornerRadius = DSLoadingBarTokens.strokeCornerRadius
        style.strokeHeight = DSLoadingBarTokens.strokeHeight
        style.strokeVerticalAlignment = DSLoadingBarTokens.strokeVerticalAlignment
        style.strokeHorizontalAlignment = DSLoadingBarTokens.strokeHorizontalAlignment
        style.strokeHorizontalArrangement = DSLoadingBarTokens.strokeHorizontalArrangement
        return style
    }
}
