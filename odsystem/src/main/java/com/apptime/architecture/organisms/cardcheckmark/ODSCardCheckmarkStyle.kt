package com.telekom.odsystem.organisms.cardcheckmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-09 (v1.33.1) - uid: 5ac49cde
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=8756-22767
 */

@Suppress("All")
class ODSCardCheckmarkStyle {
    var zStackWidth: Dp? = null
    var zStackContentAlignment: Alignment? = null
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var width: Dp? = null // Not used in mobile
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var cardBgBackground: List<ODSColorModel>? = null
    var cardBgCornerRadius: ODSCorners? = null
    var cardBgVerticalAlignment: Alignment.Vertical? = null
    var cardBgHorizontalAlignment: Alignment.Horizontal? = null
    var cardBgVerticalArrangement: Arrangement.Vertical? = null
    var cardBgBorder: Dp? = null
    var cardBgBorderColor: List<ODSColorModel>? = null
    var contentGap: Dp? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var contentPadding: ODSPadding? = null
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
    var scaleFactor: Float? = null // Not exported by the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardCheckmarkProps
    ): ODSCardCheckmarkStyle {
        val style = ODSCardCheckmarkStyle()
        style.zStackWidth = DSCardCheckmarkTokens.zStackWidth
        style.zStackContentAlignment = DSCardCheckmarkTokens.zStackContentAlignment
        style.gap = DSCardCheckmarkTokens.gap
        style.padding = DSCardCheckmarkTokens.padding
        style.width = DSCardCheckmarkTokens.width
        style.verticalAlignment = DSCardCheckmarkTokens.verticalAlignment
        style.horizontalAlignment = DSCardCheckmarkTokens.horizontalAlignment
        style.verticalArrangement = DSCardCheckmarkTokens.verticalArrangement
        style.contentAlignment = DSCardCheckmarkTokens.contentAlignment
        style.cardBgCornerRadius = DSCardCheckmarkTokens.cardBgCornerRadius
        style.cardBgVerticalAlignment = DSCardCheckmarkTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardCheckmarkTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardCheckmarkTokens.cardBgVerticalArrangement
        if (props.selected && !props.disabled) {
            style.cardBgBorder = DSCardCheckmarkTokens.cardBgBorderSelected
        }
        if (props.filled && !props.selected && props.disabled) {
            style.cardBgBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundCardDisabled))
        }
        if (props.filled && props.selected && !props.disabled) {
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.filled && !props.subtle && !props.disabled && !props.readOnly) {
            style.cardBgBackground = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        }
        if (props.filled && props.selected && !props.disabled && props.readOnly) {
            style.cardBgBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundCardDisabled))
        }
        if (props.filled && props.subtle && !props.disabled && !props.readOnly) {
            style.cardBgBackground =
                listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle))
        }
        if (!props.filled && !props.subtle && !props.selected && !props.readOnly) {
            style.cardBgBorder = DSCardCheckmarkTokens.cardBgBorder
        }
        if (!props.filled && props.subtle && props.selected && !props.disabled) {
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (!props.filled && !props.subtle && props.selected && !props.disabled && !props.readOnly) {
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (!props.filled && !props.subtle && !props.selected && !props.disabled && !props.readOnly) {
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        }
        if (!props.filled && !props.subtle && !props.selected && props.disabled && !props.readOnly) {
            style.cardBgBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeSubtleDisabled))
        }
        if (!props.filled && !props.subtle && props.selected && !props.disabled && props.readOnly) {
            style.cardBgBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        style.contentGap = DSCardCheckmarkTokens.contentGap
        style.contentVerticalAlignment = DSCardCheckmarkTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSCardCheckmarkTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSCardCheckmarkTokens.contentHorizontalArrangement
        if (props.selected && !props.disabled) {
            style.contentPadding = DSCardCheckmarkTokens.contentPaddingSelected
        }
        style.contentContainerVerticalAlignment =
            DSCardCheckmarkTokens.contentContainerVerticalAlignment
        style.contentContainerHorizontalAlignment =
            DSCardCheckmarkTokens.contentContainerHorizontalAlignment
        style.contentContainerVerticalArrangement =
            DSCardCheckmarkTokens.contentContainerVerticalArrangement
        style.selectorContainerRightHorizontalAlignment =
            DSCardCheckmarkTokens.selectorContainerRightHorizontalAlignment
        style.selectorContainerRightHorizontalArrangement =
            DSCardCheckmarkTokens.selectorContainerRightHorizontalArrangement
        if (props.selectorAlignment == ODSCardCheckmarkSelectorAlignment.TOP) {
            style.selectorContainerRightVerticalAlignment =
                DSCardCheckmarkTokens.selectorContainerRightVerticalAlignmentSelectorAlignmentTop
        }
        if (props.selectorAlignment == ODSCardCheckmarkSelectorAlignment.MIDDLE) {
            style.selectorContainerRightVerticalAlignment =
                DSCardCheckmarkTokens.selectorContainerRightVerticalAlignmentSelectorAlignmentMiddle
        }
        if (!props.selected && !props.readOnly) {
            style.selectorContainerRightWidth = DSCardCheckmarkTokens.selectorContainerRightWidth
        }
        style.checkmarkRightColor = scheme.basicText
        style.checkmarkRightWidth = DSCardCheckmarkTokens.checkmarkRightWidth
        style.checkmarkRightHeight = DSCardCheckmarkTokens.checkmarkRightHeight

        // Custom addition
        style.scaleFactor = DSCardCheckmarkTokens.scaleFactor
        return style
    }
}
