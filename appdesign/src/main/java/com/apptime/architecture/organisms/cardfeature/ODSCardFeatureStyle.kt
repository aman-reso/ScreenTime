package com.telekom.odsystem.organisms.cardfeature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-10-09 (v1.33.1) - uid: 29d108b9
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=137-12140
 */

class ODSCardFeatureStyle {
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var imageZStackWidth: Dp? = null
    var imageZStackClipContent: Boolean? = null
    var imageZStackContentAlignment: Alignment? = null
    var imageCornerRadius: ODSCorners? = null
    var imageWidth: Dp? = null
    var imageClipContent: Boolean? = null
    var imageVerticalAlignment: Alignment.Vertical? = null
    var imageHorizontalAlignment: Alignment.Horizontal? = null
    var imageVerticalArrangement: Arrangement.Vertical? = null
    var imageContentAlignment: Alignment? = null
    var imageBgBackground: List<ODSColorModel>? = null
    var imageBgCornerRadius: ODSCorners? = null
    var imageBgVerticalAlignment: Alignment.Vertical? = null
    var imageBgHorizontalAlignment: Alignment.Horizontal? = null
    var imageBgHorizontalArrangement: Arrangement.Horizontal? = null
    var image2Width: Dp? = null
    var image2Height: Dp? = null
    var image2ContentScale: ContentScale? = null
    var contentZStackMinHeight: Dp? = null
    var contentZStackContentAlignment: Alignment? = null
    var contentGap: Dp? = null
    var contentPadding: ODSPadding? = null
    var contentMinHeight: Dp? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var contentContentAlignment: Alignment? = null
    var cardBgBackground: List<ODSColorModel>? = null
    var cardBgCornerRadius: ODSCorners? = null
    var cardBgVerticalAlignment: Alignment.Vertical? = null
    var cardBgHorizontalAlignment: Alignment.Horizontal? = null
    var cardBgVerticalArrangement: Arrangement.Vertical? = null
    var slotContainerGap: Dp? = null
    var slotContainerVerticalAlignment: Alignment.Vertical? = null
    var slotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var slotContainerVerticalArrangement: Arrangement.Vertical? = null
    fun getStyle(
        scheme: ODSTheme,
    ): ODSCardFeatureStyle {
        val style = ODSCardFeatureStyle()
        style.minWidth = DSCardFeatureTokens.minWidth
        style.verticalAlignment = DSCardFeatureTokens.verticalAlignment
        style.horizontalArrangement = DSCardFeatureTokens.horizontalArrangement
        style.imageZStackWidth = DSCardFeatureTokens.imageZStackWidth
        style.imageZStackClipContent = DSCardFeatureTokens.imageZStackClipContent
        style.imageZStackContentAlignment = DSCardFeatureTokens.imageZStackContentAlignment
        style.imageCornerRadius = DSCardFeatureTokens.imageCornerRadius
        style.imageWidth = DSCardFeatureTokens.imageWidth
        style.imageClipContent = DSCardFeatureTokens.imageClipContent
        style.imageVerticalAlignment = DSCardFeatureTokens.imageVerticalAlignment
        style.imageHorizontalAlignment = DSCardFeatureTokens.imageHorizontalAlignment
        style.imageVerticalArrangement = DSCardFeatureTokens.imageVerticalArrangement
        style.imageContentAlignment = DSCardFeatureTokens.imageContentAlignment
        style.imageBgBackground = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
        style.imageBgCornerRadius = DSCardFeatureTokens.imageBgCornerRadius
        style.imageBgVerticalAlignment = DSCardFeatureTokens.imageBgVerticalAlignment
        style.imageBgHorizontalAlignment = DSCardFeatureTokens.imageBgHorizontalAlignment
        style.imageBgHorizontalArrangement = DSCardFeatureTokens.imageBgHorizontalArrangement
        style.image2Width = DSCardFeatureTokens.image2Width
        style.image2Height = DSCardFeatureTokens.image2Height
        style.image2ContentScale = DSCardFeatureTokens.image2ContentScale
        style.contentZStackMinHeight = DSCardFeatureTokens.contentZStackMinHeight
        style.contentZStackContentAlignment = DSCardFeatureTokens.contentZStackContentAlignment
        style.contentGap = DSCardFeatureTokens.contentGap
        style.contentPadding = DSCardFeatureTokens.contentPadding
        style.contentMinHeight = DSCardFeatureTokens.contentMinHeight
        style.contentVerticalAlignment = DSCardFeatureTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSCardFeatureTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSCardFeatureTokens.contentHorizontalArrangement
        style.contentContentAlignment = DSCardFeatureTokens.contentContentAlignment
        style.cardBgBackground = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        style.cardBgCornerRadius = DSCardFeatureTokens.cardBgCornerRadius
        style.cardBgVerticalAlignment = DSCardFeatureTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardFeatureTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardFeatureTokens.cardBgVerticalArrangement
        style.slotContainerGap = DSCardFeatureTokens.slotContainerGap
        style.slotContainerVerticalAlignment = DSCardFeatureTokens.slotContainerVerticalAlignment
        style.slotContainerHorizontalAlignment =
            DSCardFeatureTokens.slotContainerHorizontalAlignment
        style.slotContainerVerticalArrangement =
            DSCardFeatureTokens.slotContainerVerticalArrangement
        return style
    }
}
