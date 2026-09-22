package com.telekom.odsystem.atoms.progressstepperitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-29 (v1.33.1) - uid: 102eca88
 * Figma link: https://figma.com/design/cpaNsDgmzEjyHilbYDSKS9/Exploration File_Part 2?node-id=2032-21840
 */

@Suppress("LongMethod")
class ODSProgressStepperItemStyle {
    var background: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var cornerRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var clipContent: Boolean? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var border: Dp? = null
    var borderColor: List<ODSColorModel>? = null
    var width: Dp? = null
    var height: Dp? = null
    var digitsStyle: ODSTextStyle? = null
    var digitsColor: HexColor? = null
    var digitsTextAlign: TextAlign? = null
    var checkmarkColor: HexColor? = null
    var checkmarkWidth: Dp? = null
    var checkmarkHeight: Dp? = null
    var highPriorityColor: HexColor? = null
    var highPriorityWidth: Dp? = null
    var highPriorityHeight: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSProgressStepperItemProps,
    ): ODSProgressStepperItemStyle {
        val style = ODSProgressStepperItemStyle()
        style.cornerRadius = DSProgressStepperItemTokens.cornerRadius
        style.clipContent = DSProgressStepperItemTokens.clipContent
        style.verticalAlignment = DSProgressStepperItemTokens.verticalAlignment
        style.horizontalAlignment = DSProgressStepperItemTokens.horizontalAlignment
        style.verticalArrangement = DSProgressStepperItemTokens.verticalArrangement
        if (props.type == ODSProgressStepperItemType.NEXT) {
            style.padding = DSProgressStepperItemTokens.paddingTypeNext
            style.border = DSProgressStepperItemTokens.borderTypeNext
            style.borderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.type == ODSProgressStepperItemType.ERROR) {
            style.background =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (props.size == ODSProgressStepperItemSize.SMALL) {
            style.width = DSProgressStepperItemTokens.widthSizeSmall
            style.height = DSProgressStepperItemTokens.heightSizeSmall
        }
        if (props.type == ODSProgressStepperItemType.CURRENT) {
            style.background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
            style.padding = DSProgressStepperItemTokens.paddingTypeCurrent
        }
        if (props.type == ODSProgressStepperItemType.SUCCESS) {
            style.background = listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard))
        }
        if (props.size == ODSProgressStepperItemSize.STANDARD) {
            style.minHeight = DSProgressStepperItemTokens.minHeightSizeStandard
            style.minWidth = DSProgressStepperItemTokens.minWidthSizeStandard
        }
        if (props.type == ODSProgressStepperItemType.NEXT && props.size == ODSProgressStepperItemSize.STANDARD) {
            style.digitsStyle = DSProgressStepperItemTokens.digitsStyleTypeNextSizeStandard
            style.digitsColor = scheme.basicText
            style.digitsTextAlign = DSProgressStepperItemTokens.digitsTextAlignTypeNextSizeStandard
        }
        if (props.type == ODSProgressStepperItemType.CURRENT && props.size == ODSProgressStepperItemSize.STANDARD) {
            style.digitsStyle = DSProgressStepperItemTokens.digitsStyleTypeCurrentSizeStandard
            style.digitsColor = scheme.basicTextOnAccentSecondary
            style.digitsTextAlign =
                DSProgressStepperItemTokens.digitsTextAlignTypeCurrentSizeStandard
        }
        if (props.type == ODSProgressStepperItemType.SUCCESS) {
            style.checkmarkColor = scheme.basicTextOnAccentSecondary
        }
        if (props.type == ODSProgressStepperItemType.SUCCESS && props.size == ODSProgressStepperItemSize.STANDARD) {
            style.checkmarkWidth = DSProgressStepperItemTokens.checkmarkWidthTypeSuccessSizeStandard
            style.checkmarkHeight =
                DSProgressStepperItemTokens.checkmarkHeightTypeSuccessSizeStandard
        }
        if (props.type == ODSProgressStepperItemType.SUCCESS && props.size == ODSProgressStepperItemSize.SMALL) {
            style.checkmarkWidth = DSProgressStepperItemTokens.checkmarkWidthTypeSuccessSizeSmall
            style.checkmarkHeight = DSProgressStepperItemTokens.checkmarkHeightTypeSuccessSizeSmall
        }
        if (props.type == ODSProgressStepperItemType.ERROR) {
            style.highPriorityColor = scheme.basicTextOnAccentSecondary
        }
        if (props.type == ODSProgressStepperItemType.ERROR && props.size == ODSProgressStepperItemSize.STANDARD) {
            style.highPriorityWidth =
                DSProgressStepperItemTokens.highPriorityWidthTypeErrorSizeStandard
            style.highPriorityHeight =
                DSProgressStepperItemTokens.highPriorityHeightTypeErrorSizeStandard
        }
        if (props.type == ODSProgressStepperItemType.ERROR && props.size == ODSProgressStepperItemSize.SMALL) {
            style.highPriorityWidth =
                DSProgressStepperItemTokens.highPriorityWidthTypeErrorSizeSmall
            style.highPriorityHeight =
                DSProgressStepperItemTokens.highPriorityHeightTypeErrorSizeSmall
        }
        return style
    }
}
