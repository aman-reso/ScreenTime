package com.telekom.odsystem.atoms.loadingspinner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSLoadingSpinnerTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-10-30 (v1.33.1) - uid: ed4df6a
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=19997-2001
 */
@Suppress("LongMethod")
class ODSLoadingSpinnerStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var loadingSpinnerContainerWidth: Dp? = null
    var loadingSpinnerContainerHeight: Dp? = null
    var loadingSpinnerContainerVerticalAlignment: Alignment.Vertical? = null
    var loadingSpinnerContainerHorizontalAlignment: Alignment.Horizontal? = null
    var loadingSpinnerContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var labelStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var progressIndicatorStrokeWidth: Dp? = null // Not exported from the plugin
    var progressIndicatorColor: HexColor? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSLoadingSpinnerProps,
    ): ODSLoadingSpinnerStyle {
        val style = ODSLoadingSpinnerStyle()
        if (props.labelAlignment == ODSLoadingSpinnerLabelAlignment.NONE) {
            style.gap = DSLoadingSpinnerTokens.gapLabelAlignmentNone
            style.verticalAlignment = DSLoadingSpinnerTokens.verticalAlignmentLabelAlignmentNone
            style.horizontalAlignment = DSLoadingSpinnerTokens.horizontalAlignmentLabelAlignmentNone
            style.verticalArrangement = DSLoadingSpinnerTokens.verticalArrangementLabelAlignmentNone
        }
        if (props.labelAlignment == ODSLoadingSpinnerLabelAlignment.VERTICAL) {
            style.gap = DSLoadingSpinnerTokens.gapLabelAlignmentVertical
            style.verticalAlignment = DSLoadingSpinnerTokens.verticalAlignmentLabelAlignmentVertical
            style.horizontalAlignment =
                DSLoadingSpinnerTokens.horizontalAlignmentLabelAlignmentVertical
            style.verticalArrangement =
                DSLoadingSpinnerTokens.verticalArrangementLabelAlignmentVertical
        }
        if (props.labelAlignment == ODSLoadingSpinnerLabelAlignment.HORIZONTAL) {
            style.gap = DSLoadingSpinnerTokens.gapLabelAlignmentHorizontal
            style.verticalAlignment =
                DSLoadingSpinnerTokens.verticalAlignmentLabelAlignmentHorizontal
            style.horizontalAlignment =
                DSLoadingSpinnerTokens.horizontalAlignmentLabelAlignmentHorizontal
            style.horizontalArrangement =
                DSLoadingSpinnerTokens.horizontalArrangementLabelAlignmentHorizontal
        }
        style.loadingSpinnerContainerVerticalAlignment =
            DSLoadingSpinnerTokens.loadingSpinnerContainerVerticalAlignment
        style.loadingSpinnerContainerHorizontalAlignment =
            DSLoadingSpinnerTokens.loadingSpinnerContainerHorizontalAlignment
        style.loadingSpinnerContainerHorizontalArrangement =
            DSLoadingSpinnerTokens.loadingSpinnerContainerHorizontalArrangement
        if (props.size == ODSLoadingSpinnerSize.LARGE) {
            style.loadingSpinnerContainerWidth =
                DSLoadingSpinnerTokens.loadingSpinnerContainerWidthSizeLarge
            style.loadingSpinnerContainerHeight =
                DSLoadingSpinnerTokens.loadingSpinnerContainerHeightSizeLarge
            // Custom Addition
            style.progressIndicatorStrokeWidth =
                DSLoadingSpinnerTokens.progressIndicatorStrokeWidthLarge
        }
        if (props.size == ODSLoadingSpinnerSize.SMALL) {
            style.loadingSpinnerContainerWidth =
                DSLoadingSpinnerTokens.loadingSpinnerContainerWidthSizeSmall
            style.loadingSpinnerContainerHeight =
                DSLoadingSpinnerTokens.loadingSpinnerContainerHeightSizeSmall
            // Custom Addition
            style.progressIndicatorStrokeWidth =
                DSLoadingSpinnerTokens.progressIndicatorStrokeWidthSmall
        }
        if (props.size == ODSLoadingSpinnerSize.X_SMALL) {
            style.loadingSpinnerContainerWidth =
                DSLoadingSpinnerTokens.loadingSpinnerContainerWidthSizeXSmall
            style.loadingSpinnerContainerHeight =
                DSLoadingSpinnerTokens.loadingSpinnerContainerHeightSizeXSmall
            // Custom Addition
            style.progressIndicatorStrokeWidth =
                DSLoadingSpinnerTokens.progressIndicatorStrokeWidthXSmall
        }
        if (props.labelAlignment == ODSLoadingSpinnerLabelAlignment.VERTICAL) {
            style.labelStyle = DSLoadingSpinnerTokens.labelStyleLabelAlignmentVertical
            style.labelTextAlign = DSLoadingSpinnerTokens.labelTextAlignLabelAlignmentVertical
        }
        if (props.labelAlignment == ODSLoadingSpinnerLabelAlignment.HORIZONTAL) {
            style.labelStyle = DSLoadingSpinnerTokens.labelStyleLabelAlignmentHorizontal
            style.labelTextAlign = DSLoadingSpinnerTokens.labelTextAlignLabelAlignmentHorizontal
        }
        if (props.variant == ODSLoadingSpinnerVariant.BLACK && props.labelAlignment == ODSLoadingSpinnerLabelAlignment.HORIZONTAL) {
            style.labelColor = scheme.basicText
        }
        if (props.variant == ODSLoadingSpinnerVariant.BLACK && props.labelAlignment == ODSLoadingSpinnerLabelAlignment.VERTICAL) {
            style.labelColor = scheme.basicText
        }
        if (props.variant == ODSLoadingSpinnerVariant.STANDARD && props.labelAlignment == ODSLoadingSpinnerLabelAlignment.HORIZONTAL) {
            style.labelColor = scheme.basicText
        }
        if (props.variant == ODSLoadingSpinnerVariant.STANDARD && props.labelAlignment == ODSLoadingSpinnerLabelAlignment.VERTICAL) {
            style.labelColor = scheme.basicText
        }
        if (props.variant == ODSLoadingSpinnerVariant.WHITE && props.labelAlignment == ODSLoadingSpinnerLabelAlignment.HORIZONTAL) {
            style.labelColor = scheme.basicBackground
        }
        if (props.variant == ODSLoadingSpinnerVariant.WHITE && props.labelAlignment == ODSLoadingSpinnerLabelAlignment.VERTICAL) {
            style.labelColor = scheme.basicBackground
        }

        // Custom Addition
        if (props.variant == ODSLoadingSpinnerVariant.STANDARD) {
            style.progressIndicatorColor = scheme.basicAccent
        }
        if (props.variant == ODSLoadingSpinnerVariant.WHITE) {
            style.progressIndicatorColor = scheme.basicBackground
        }
        if (props.variant == ODSLoadingSpinnerVariant.BLACK) {
            style.progressIndicatorColor = scheme.basicAccentSecondary
        }

        return style
    }
}
