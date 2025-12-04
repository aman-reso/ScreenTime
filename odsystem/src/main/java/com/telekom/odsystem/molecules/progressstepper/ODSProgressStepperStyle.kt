package com.telekom.odsystem.molecules.progressstepper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-29 (v1.33.1) - uid: 102ecae4
 * Figma link: https://figma.com/design/cpaNsDgmzEjyHilbYDSKS9/Exploration File_Part 2?node-id=2032-21811
 */

@Suppress("LongMethod")
class ODSProgressStepperStyle {
    var gap: Dp? = null
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var badgeDividerFrameGap: Dp? = null
    var badgeDividerFramePadding: ODSPadding? = null
    var badgeDividerFrameVerticalAlignment: Alignment.Vertical? = null
    var badgeDividerFrameHorizontalAlignment: Alignment.Horizontal? = null
    var badgeDividerFrameVerticalArrangement: Arrangement.Vertical? = null
    var badgeDividerFrameHorizontalArrangement: Arrangement.Horizontal? = null
    var contentFrameGap: Dp? = null
    var contentFramePadding: ODSPadding? = null
    var contentFrameVerticalAlignment: Alignment.Vertical? = null
    var contentFrameHorizontalAlignment: Alignment.Horizontal? = null
    var contentFrameVerticalArrangement: Arrangement.Vertical? = null
    var labelStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var textStyle: ODSTextStyle? = null
    var textColor: HexColor? = null
    var textTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSProgressStepperProps,
    ): ODSProgressStepperStyle {
        val style = ODSProgressStepperStyle()
        style.verticalAlignment = DSProgressStepperTokens.verticalAlignment
        style.horizontalAlignment = DSProgressStepperTokens.horizontalAlignment
        if (props.variant == ODSProgressStepperVariant.VERTICAL) {
            style.minHeight = DSProgressStepperTokens.minHeightVariantVertical
            style.horizontalArrangement =
                DSProgressStepperTokens.horizontalArrangementVariantVertical
        }
        if (props.variant == ODSProgressStepperVariant.HORIZONTAL) {
            style.gap = DSProgressStepperTokens.gapVariantHorizontal
            style.verticalArrangement = DSProgressStepperTokens.verticalArrangementVariantHorizontal
        }
        if (props.variant == ODSProgressStepperVariant.VERTICAL && props.size == ODSProgressStepperSize.STANDARD) {
            style.gap = DSProgressStepperTokens.gapVariantVerticalSizeStandard
        }
        if (props.variant == ODSProgressStepperVariant.VERTICAL && props.size == ODSProgressStepperSize.SMALL) {
            style.gap = DSProgressStepperTokens.gapVariantVerticalSizeSmall
        }
        style.badgeDividerFrameHorizontalAlignment =
            DSProgressStepperTokens.badgeDividerFrameHorizontalAlignment
        if (props.variant == ODSProgressStepperVariant.VERTICAL) {
            style.badgeDividerFrameGap = DSProgressStepperTokens.badgeDividerFrameGapVariantVertical
            style.badgeDividerFrameVerticalAlignment =
                DSProgressStepperTokens.badgeDividerFrameVerticalAlignmentVariantVertical
            style.badgeDividerFrameVerticalArrangement =
                DSProgressStepperTokens.badgeDividerFrameVerticalArrangementVariantVertical
        }
        if (props.variant == ODSProgressStepperVariant.HORIZONTAL) {
            style.badgeDividerFrameVerticalAlignment =
                DSProgressStepperTokens.badgeDividerFrameVerticalAlignmentVariantHorizontal
            style.badgeDividerFrameHorizontalArrangement =
                DSProgressStepperTokens.badgeDividerFrameHorizontalArrangementVariantHorizontal
        }
        if (props.variant == ODSProgressStepperVariant.HORIZONTAL && props.size == ODSProgressStepperSize.SMALL) {
            style.badgeDividerFrameGap =
                DSProgressStepperTokens.badgeDividerFrameGapVariantHorizontalSizeSmall
        }
        if (props.variant == ODSProgressStepperVariant.HORIZONTAL && props.size == ODSProgressStepperSize.STANDARD) {
            style.badgeDividerFrameGap =
                DSProgressStepperTokens.badgeDividerFrameGapVariantHorizontalSizeStandard
        }
        if (props.variant == ODSProgressStepperVariant.VERTICAL && props.size == ODSProgressStepperSize.STANDARD) {
            style.badgeDividerFramePadding =
                DSProgressStepperTokens.badgeDividerFramePaddingVariantVerticalSizeStandard
        }
        if (props.variant == ODSProgressStepperVariant.VERTICAL && props.size == ODSProgressStepperSize.SMALL) {
            style.badgeDividerFramePadding =
                DSProgressStepperTokens.badgeDividerFramePaddingVariantVerticalSizeSmall
        }
        style.contentFrameVerticalAlignment = DSProgressStepperTokens.contentFrameVerticalAlignment
        style.contentFrameHorizontalAlignment =
            DSProgressStepperTokens.contentFrameHorizontalAlignment
        style.contentFrameVerticalArrangement =
            DSProgressStepperTokens.contentFrameVerticalArrangement
        if (props.variant == ODSProgressStepperVariant.VERTICAL) {
            style.contentFrameGap = DSProgressStepperTokens.contentFrameGapVariantVertical
            style.contentFramePadding = DSProgressStepperTokens.contentFramePaddingVariantVertical
        }
        if (props.variant == ODSProgressStepperVariant.HORIZONTAL) {
            style.contentFramePadding = DSProgressStepperTokens.contentFramePaddingVariantHorizontal
        }
        if (props.variant == ODSProgressStepperVariant.HORIZONTAL && props.size == ODSProgressStepperSize.STANDARD) {
            style.contentFrameGap =
                DSProgressStepperTokens.contentFrameGapVariantHorizontalSizeStandard
        }
        style.labelStyle = DSProgressStepperTokens.labelStyle
        style.labelColor = scheme.basicText
        style.labelTextAlign = DSProgressStepperTokens.labelTextAlign
        style.textColor = scheme.basicTextRecessive
        style.textTextAlign = DSProgressStepperTokens.textTextAlign
        if (props.size == ODSProgressStepperSize.SMALL) {
            style.textStyle = DSProgressStepperTokens.textStyleSizeSmall
        }
        if (props.size == ODSProgressStepperSize.STANDARD) {
            style.textStyle = DSProgressStepperTokens.textStyleSizeStandard
        }
        return style
    }
}
