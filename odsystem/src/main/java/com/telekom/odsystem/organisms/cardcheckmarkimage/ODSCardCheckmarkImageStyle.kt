package com.telekom.odsystem.organisms.cardcheckmarkimage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-11 (v1.33.1) - uid: 5ac57ab5
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=8756-24496
 */

@Suppress("All")
class ODSCardCheckmarkImageStyle {
    var zStackWidth: Dp? = null
    var zStackContentAlignment: Alignment? = null
    var width: Dp? = null // Not used in mobile
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var imageAspectRatioZStackContentAlignment: Alignment? = null
    var imageAspectRatioVerticalAlignment: Alignment.Vertical? = null // Not used in mobile
    var imageAspectRatioHorizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var imageAspectRatioVerticalArrangement: Arrangement.Vertical? = null // Not used in mobile
    var imageAspectRatioContentAlignment: Alignment? = null // Not used in mobile
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
    var selectorContainerRightWidth: Dp? = null
    var selectorContainerRightVerticalAlignment: Alignment.Vertical? = null
    var selectorContainerRightHorizontalAlignment: Alignment.Horizontal? = null
    var selectorContainerRightHorizontalArrangement: Arrangement.Horizontal? = null
    var checkmarkRightColor: HexColor? = null
    var checkmarkRightWidth: Dp? = null
    var checkmarkRightHeight: Dp? = null
    var cardStrokeCornerRadius: ODSCorners? = null
    var cardStrokeVerticalAlignment: Alignment.Vertical? = null
    var cardStrokeHorizontalAlignment: Alignment.Horizontal? = null
    var cardStrokeVerticalArrangement: Arrangement.Vertical? = null
    var cardStrokeBorder: Dp? = null
    var cardStrokeBorderColor: List<ODSColorModel>? = null
    var scaleFactor: Float? = null // Not exported by the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardCheckmarkImageProps
    ): ODSCardCheckmarkImageStyle {
        val style = ODSCardCheckmarkImageStyle()
        style.zStackWidth = DSCardCheckmarkImageTokens.zStackWidth
        style.zStackContentAlignment = DSCardCheckmarkImageTokens.zStackContentAlignment
        style.width = DSCardCheckmarkImageTokens.width
        style.verticalAlignment = DSCardCheckmarkImageTokens.verticalAlignment
        style.horizontalAlignment = DSCardCheckmarkImageTokens.horizontalAlignment
        style.verticalArrangement = DSCardCheckmarkImageTokens.verticalArrangement
        style.contentAlignment = DSCardCheckmarkImageTokens.contentAlignment
        style.imageAspectRatioZStackContentAlignment =
            DSCardCheckmarkImageTokens.imageAspectRatioZStackContentAlignment
        style.imageAspectRatioVerticalAlignment =
            DSCardCheckmarkImageTokens.imageAspectRatioVerticalAlignment
        style.imageAspectRatioHorizontalAlignment =
            DSCardCheckmarkImageTokens.imageAspectRatioHorizontalAlignment
        style.imageAspectRatioVerticalArrangement =
            DSCardCheckmarkImageTokens.imageAspectRatioVerticalArrangement
        style.imageAspectRatioContentAlignment =
            DSCardCheckmarkImageTokens.imageAspectRatioContentAlignment
        style.imageContainerZStackClipContent =
            DSCardCheckmarkImageTokens.imageContainerZStackClipContent
        style.imageContainerCornerRadius = DSCardCheckmarkImageTokens.imageContainerCornerRadius
        style.imageContainerVerticalAlignment =
            DSCardCheckmarkImageTokens.imageContainerVerticalAlignment
        style.imageContainerHorizontalAlignment =
            DSCardCheckmarkImageTokens.imageContainerHorizontalAlignment
        style.imageContainerVerticalArrangement =
            DSCardCheckmarkImageTokens.imageContainerVerticalArrangement
        style.imageContentScale = DSCardCheckmarkImageTokens.imageContentScale
        style.containerZStackContentAlignment =
            DSCardCheckmarkImageTokens.containerZStackContentAlignment
        style.containerGap = DSCardCheckmarkImageTokens.containerGap
        style.containerPadding = DSCardCheckmarkImageTokens.containerPadding
        style.containerVerticalAlignment = DSCardCheckmarkImageTokens.containerVerticalAlignment
        style.containerHorizontalAlignment = DSCardCheckmarkImageTokens.containerHorizontalAlignment
        style.containerVerticalArrangement = DSCardCheckmarkImageTokens.containerVerticalArrangement
        style.containerContentAlignment = DSCardCheckmarkImageTokens.containerContentAlignment
        style.cardBgCornerRadius = DSCardCheckmarkImageTokens.cardBgCornerRadius
        style.cardBgVerticalAlignment = DSCardCheckmarkImageTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardCheckmarkImageTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardCheckmarkImageTokens.cardBgVerticalArrangement
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
        style.contentGap = DSCardCheckmarkImageTokens.contentGap
        style.contentVerticalAlignment = DSCardCheckmarkImageTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSCardCheckmarkImageTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSCardCheckmarkImageTokens.contentHorizontalArrangement
        style.contentContainerVerticalAlignment =
            DSCardCheckmarkImageTokens.contentContainerVerticalAlignment
        style.contentContainerHorizontalAlignment =
            DSCardCheckmarkImageTokens.contentContainerHorizontalAlignment
        style.contentContainerVerticalArrangement =
            DSCardCheckmarkImageTokens.contentContainerVerticalArrangement
        style.selectorContainerRightVerticalAlignment =
            DSCardCheckmarkImageTokens.selectorContainerRightVerticalAlignment
        style.selectorContainerRightHorizontalAlignment =
            DSCardCheckmarkImageTokens.selectorContainerRightHorizontalAlignment
        style.selectorContainerRightHorizontalArrangement =
            DSCardCheckmarkImageTokens.selectorContainerRightHorizontalArrangement
        if (!props.selected && !props.readOnly) {
            style.selectorContainerRightWidth =
                DSCardCheckmarkImageTokens.selectorContainerRightWidth
        }
        style.checkmarkRightColor = scheme.basicText
        style.checkmarkRightWidth = DSCardCheckmarkImageTokens.checkmarkRightWidth
        style.checkmarkRightHeight = DSCardCheckmarkImageTokens.checkmarkRightHeight
        style.cardStrokeCornerRadius = DSCardCheckmarkImageTokens.cardStrokeCornerRadius
        style.cardStrokeVerticalAlignment = DSCardCheckmarkImageTokens.cardStrokeVerticalAlignment
        style.cardStrokeHorizontalAlignment =
            DSCardCheckmarkImageTokens.cardStrokeHorizontalAlignment
        style.cardStrokeVerticalArrangement =
            DSCardCheckmarkImageTokens.cardStrokeVerticalArrangement
        if (props.selected && !props.disabled) {
            style.cardStrokeBorder = DSCardCheckmarkImageTokens.cardStrokeBorderSelected
        }
        if (props.filled && props.selected && !props.disabled) {
            style.cardStrokeBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (!props.filled && !props.subtle && !props.selected && !props.readOnly) {
            style.cardStrokeBorder = DSCardCheckmarkImageTokens.cardStrokeBorder
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
        style.scaleFactor = DSCardCheckmarkImageTokens.scaleFactor
        return style
    }
}
