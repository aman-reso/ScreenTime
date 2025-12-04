package com.telekom.odsystem.organisms.cardchoiceimage

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
 * 2025-09-11 (v1.33.1) - uid: 4794709a
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=86-10038
 */

@Suppress("All")
class ODSCardChoiceImageStyle {
    var zStackWidth: Dp? = null // Not exported by the plugin
    var zStackContentAlignment: Alignment? = null
    var width: Dp? = null // Not exported by the plugin
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var imageAspectRatioZStackContentAlignment: Alignment? = null
    var imageAspectRatioVerticalAlignment: Alignment.Vertical? = null // Not exported by the plugin
    var imageAspectRatioHorizontalAlignment: Alignment.Horizontal? =
        null // Not exported by the plugin
    var imageAspectRatioVerticalArrangement: Arrangement.Vertical? =
        null // Not exported by the plugin
    var imageAspectRatioContentAlignment: Alignment? = null // Not exported by the plugin
    var imageContainerZStackClipContent: Boolean? = null
    var imageContainerCornerRadius: ODSCorners? = null
    var imageContainerVerticalAlignment: Alignment.Vertical? = null
    var imageContainerHorizontalAlignment: Alignment.Horizontal? = null
    var imageContainerVerticalArrangement: Arrangement.Vertical? = null
    var imageContentScale: ContentScale? = null
    var containerZStackContentAlignment: Alignment? = null
    var containerGap: Dp? = null
    var containerPadding: ODSPadding? = null
    var containerVerticalAlignment: Alignment.Vertical? = null
    var containerHorizontalAlignment: Alignment.Horizontal? = null
    var containerVerticalArrangement: Arrangement.Vertical? = null
    var containerContentAlignment: Alignment? = null
    var cardBgBackground: List<ODSColorModel>? = null
    var cardBgCornerRadius: ODSCorners? = null
    var cardBgVerticalAlignment: Alignment.Vertical? = null
    var cardBgHorizontalAlignment: Alignment.Horizontal? = null
    var cardBgVerticalArrangement: Arrangement.Vertical? = null
    var contentGap: Dp? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var contentContainerVerticalAlignment: Alignment.Vertical? = null
    var contentContainerHorizontalAlignment: Alignment.Horizontal? = null
    var contentContainerVerticalArrangement: Arrangement.Vertical? = null
    var selectorContainerRightVerticalAlignment: Alignment.Vertical? = null
    var selectorContainerRightHorizontalAlignment: Alignment.Horizontal? = null
    var selectorContainerRightHorizontalArrangement: Arrangement.Horizontal? = null
    var cardStrokeCornerRadius: ODSCorners? = null
    var cardStrokeVerticalAlignment: Alignment.Vertical? = null
    var cardStrokeHorizontalAlignment: Alignment.Horizontal? = null
    var cardStrokeVerticalArrangement: Arrangement.Vertical? = null
    var cardStrokeBorder: Dp? = null
    var cardStrokeBorderColor: List<ODSColorModel>? = null
    var scaleFactor: Float? = null // Not exported by the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardChoiceImageProps
    ): ODSCardChoiceImageStyle {
        val style = ODSCardChoiceImageStyle()
        style.zStackWidth = DSCardChoiceImageTokens.zStackWidth
        style.zStackContentAlignment = DSCardChoiceImageTokens.zStackContentAlignment
        style.width = DSCardChoiceImageTokens.width
        style.verticalAlignment = DSCardChoiceImageTokens.verticalAlignment
        style.horizontalAlignment = DSCardChoiceImageTokens.horizontalAlignment
        style.verticalArrangement = DSCardChoiceImageTokens.verticalArrangement
        style.contentAlignment = DSCardChoiceImageTokens.contentAlignment
        style.imageAspectRatioZStackContentAlignment =
            DSCardChoiceImageTokens.imageAspectRatioZStackContentAlignment
        style.imageAspectRatioVerticalAlignment =
            DSCardChoiceImageTokens.imageAspectRatioVerticalAlignment
        style.imageAspectRatioHorizontalAlignment =
            DSCardChoiceImageTokens.imageAspectRatioHorizontalAlignment
        style.imageAspectRatioVerticalArrangement =
            DSCardChoiceImageTokens.imageAspectRatioVerticalArrangement
        style.imageAspectRatioContentAlignment =
            DSCardChoiceImageTokens.imageAspectRatioContentAlignment
        style.imageContainerZStackClipContent =
            DSCardChoiceImageTokens.imageContainerZStackClipContent
        style.imageContainerCornerRadius = DSCardChoiceImageTokens.imageContainerCornerRadius
        style.imageContainerVerticalAlignment =
            DSCardChoiceImageTokens.imageContainerVerticalAlignment
        style.imageContainerHorizontalAlignment =
            DSCardChoiceImageTokens.imageContainerHorizontalAlignment
        style.imageContainerVerticalArrangement =
            DSCardChoiceImageTokens.imageContainerVerticalArrangement
        style.imageContentScale = DSCardChoiceImageTokens.imageContentScale
        style.containerZStackContentAlignment =
            DSCardChoiceImageTokens.containerZStackContentAlignment
        style.containerGap = DSCardChoiceImageTokens.containerGap
        style.containerPadding = DSCardChoiceImageTokens.containerPadding
        style.containerVerticalAlignment = DSCardChoiceImageTokens.containerVerticalAlignment
        style.containerHorizontalAlignment = DSCardChoiceImageTokens.containerHorizontalAlignment
        style.containerVerticalArrangement = DSCardChoiceImageTokens.containerVerticalArrangement
        style.containerContentAlignment = DSCardChoiceImageTokens.containerContentAlignment
        style.cardBgCornerRadius = DSCardChoiceImageTokens.cardBgCornerRadius
        style.cardBgVerticalAlignment = DSCardChoiceImageTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardChoiceImageTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardChoiceImageTokens.cardBgVerticalArrangement
        if (props.filled && !props.selected && props.disabled) {
            style.cardBgBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundCardDisabled))
        }
        if (props.filled && !props.subtle && !props.disabled && !props.readOnly) {
            style.cardBgBackground = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        }
        if (props.filled && props.subtle && !props.disabled && !props.readOnly) {
            style.cardBgBackground =
                listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle))
        }
        if (props.filled && props.selected && !props.disabled && props.readOnly) {
            style.cardBgBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundCardDisabled))
        }
        style.contentGap = DSCardChoiceImageTokens.contentGap
        style.contentVerticalAlignment = DSCardChoiceImageTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSCardChoiceImageTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSCardChoiceImageTokens.contentHorizontalArrangement
        style.contentContainerVerticalAlignment =
            DSCardChoiceImageTokens.contentContainerVerticalAlignment
        style.contentContainerHorizontalAlignment =
            DSCardChoiceImageTokens.contentContainerHorizontalAlignment
        style.contentContainerVerticalArrangement =
            DSCardChoiceImageTokens.contentContainerVerticalArrangement
        style.selectorContainerRightVerticalAlignment =
            DSCardChoiceImageTokens.selectorContainerRightVerticalAlignment
        style.selectorContainerRightHorizontalAlignment =
            DSCardChoiceImageTokens.selectorContainerRightHorizontalAlignment
        style.selectorContainerRightHorizontalArrangement =
            DSCardChoiceImageTokens.selectorContainerRightHorizontalArrangement
        style.cardStrokeCornerRadius = DSCardChoiceImageTokens.cardStrokeCornerRadius
        style.cardStrokeVerticalAlignment = DSCardChoiceImageTokens.cardStrokeVerticalAlignment
        style.cardStrokeHorizontalAlignment = DSCardChoiceImageTokens.cardStrokeHorizontalAlignment
        style.cardStrokeVerticalArrangement = DSCardChoiceImageTokens.cardStrokeVerticalArrangement
        if (props.selected && !props.disabled) {
            style.cardStrokeBorder = DSCardChoiceImageTokens.cardStrokeBorderSelected
        }
        if (props.filled && props.selected && !props.disabled) {
            style.cardStrokeBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (!props.filled && !props.subtle && !props.selected && !props.readOnly) {
            style.cardStrokeBorder = DSCardChoiceImageTokens.cardStrokeBorder
        }
        if (!props.filled && props.subtle && props.selected && !props.disabled) {
            style.cardStrokeBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (!props.filled && !props.subtle && !props.selected && !props.disabled && !props.readOnly) {
            style.cardStrokeBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        }
        if (!props.filled && !props.subtle && props.selected && !props.disabled && !props.readOnly) {
            style.cardStrokeBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (!props.filled && !props.subtle && !props.selected && props.disabled && !props.readOnly) {
            style.cardStrokeBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeSubtleDisabled))
        }
        if (!props.filled && !props.subtle && props.selected && !props.disabled && props.readOnly) {
            style.cardStrokeBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }

        // Custom addition
        style.scaleFactor = DSCardChoiceImageTokens.scaleFactor
        return style
    }
}
