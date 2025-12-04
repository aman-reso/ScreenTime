package com.telekom.odsystem.organisms.cardchoice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-10 (v1.33.1) - uid: 22378211
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=84-11470
 */

@Suppress("All")
class ODSCardChoiceStyle {
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
    var selectorContainerLeftVerticalAlignment: Alignment.Vertical? = null
    var selectorContainerLeftHorizontalAlignment: Alignment.Horizontal? = null
    var selectorContainerLeftHorizontalArrangement: Arrangement.Horizontal? = null
    var contentContainerVerticalAlignment: Alignment.Vertical? = null
    var contentContainerHorizontalAlignment: Alignment.Horizontal? = null
    var contentContainerVerticalArrangement: Arrangement.Vertical? = null
    var selectorContainerRightVerticalAlignment: Alignment.Vertical? = null
    var selectorContainerRightHorizontalAlignment: Alignment.Horizontal? = null
    var selectorContainerRightHorizontalArrangement: Arrangement.Horizontal? = null
    var scaleFactor: Float? = null // Not exported by the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardChoiceProps
    ): ODSCardChoiceStyle {
        val style = ODSCardChoiceStyle()
        style.zStackWidth = DSCardChoiceTokens.zStackWidth
        style.zStackContentAlignment = DSCardChoiceTokens.zStackContentAlignment
        style.gap = DSCardChoiceTokens.gap
        style.padding = DSCardChoiceTokens.padding
        style.width = DSCardChoiceTokens.width
        style.verticalAlignment = DSCardChoiceTokens.verticalAlignment
        style.horizontalAlignment = DSCardChoiceTokens.horizontalAlignment
        style.verticalArrangement = DSCardChoiceTokens.verticalArrangement
        style.contentAlignment = DSCardChoiceTokens.contentAlignment
        style.cardBgCornerRadius = DSCardChoiceTokens.cardBgCornerRadius
        style.cardBgVerticalAlignment = DSCardChoiceTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardChoiceTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardChoiceTokens.cardBgVerticalArrangement
        if (props.selected && !props.disabled) {
            style.cardBgBorder = DSCardChoiceTokens.cardBgBorderSelected
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
        if (props.filled && props.subtle && !props.disabled && !props.readOnly) {
            style.cardBgBackground =
                listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle))
        }
        if (props.filled && props.selected && !props.disabled && props.readOnly) {
            style.cardBgBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundCardDisabled))
        }
        if (!props.filled && !props.subtle && !props.selected && !props.readOnly) {
            style.cardBgBorder = DSCardChoiceTokens.cardBgBorder
        }
        if (!props.filled && props.subtle && props.selected && !props.disabled) {
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (!props.filled && !props.subtle && !props.selected && !props.disabled && !props.readOnly) {
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        }
        if (!props.filled && !props.subtle && props.selected && !props.disabled && !props.readOnly) {
            style.cardBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (!props.filled && !props.subtle && !props.selected && props.disabled && !props.readOnly) {
            style.cardBgBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeSubtleDisabled))
        }
        if (!props.filled && !props.subtle && props.selected && !props.disabled && props.readOnly) {
            style.cardBgBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        style.contentGap = DSCardChoiceTokens.contentGap
        style.contentVerticalAlignment = DSCardChoiceTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSCardChoiceTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSCardChoiceTokens.contentHorizontalArrangement
        style.selectorContainerLeftHorizontalAlignment =
            DSCardChoiceTokens.selectorContainerLeftHorizontalAlignment
        style.selectorContainerLeftHorizontalArrangement =
            DSCardChoiceTokens.selectorContainerLeftHorizontalArrangement
        if (props.selectorAlignment == ODSCardChoiceSelectorAlignment.TOP) {
            style.selectorContainerLeftVerticalAlignment =
                DSCardChoiceTokens.selectorContainerLeftVerticalAlignmentSelectorAlignmentTop
        }
        if (props.selectorAlignment == ODSCardChoiceSelectorAlignment.MIDDLE) {
            style.selectorContainerLeftVerticalAlignment =
                DSCardChoiceTokens.selectorContainerLeftVerticalAlignmentSelectorAlignmentMiddle
        }
        style.contentContainerVerticalAlignment =
            DSCardChoiceTokens.contentContainerVerticalAlignment
        style.contentContainerHorizontalAlignment =
            DSCardChoiceTokens.contentContainerHorizontalAlignment
        style.contentContainerVerticalArrangement =
            DSCardChoiceTokens.contentContainerVerticalArrangement
        style.selectorContainerRightHorizontalAlignment =
            DSCardChoiceTokens.selectorContainerRightHorizontalAlignment
        style.selectorContainerRightHorizontalArrangement =
            DSCardChoiceTokens.selectorContainerRightHorizontalArrangement
        if (props.selectorAlignment == ODSCardChoiceSelectorAlignment.TOP) {
            style.selectorContainerRightVerticalAlignment =
                DSCardChoiceTokens.selectorContainerRightVerticalAlignmentSelectorAlignmentTop
        }
        if (props.selectorAlignment == ODSCardChoiceSelectorAlignment.MIDDLE) {
            style.selectorContainerRightVerticalAlignment =
                DSCardChoiceTokens.selectorContainerRightVerticalAlignmentSelectorAlignmentMiddle
        }

        // Custom addition
        style.scaleFactor = DSCardChoiceTokens.scaleFactor
        return style
    }
}
