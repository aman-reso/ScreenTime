package com.telekom.odsystem.atoms.skeleton

import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners

data class ODSSkeletonTokens(
    var borderRadiusVariantFull: ODSCorners,
    var borderRadiusVariantSmall: ODSCorners,
    var borderRadiusVariantMedium: ODSCorners,
    var borderRadiusVariantLarge: ODSCorners,
    var widthVariantFull: Dp,
    var heightVariantFull: Dp,
    var clipContent: Boolean,
    var maxHeightVariantSmall: Dp,
    var maxHeightVariantMedium: Dp,
    var minHeightVariantSmall: Dp,
    var minHeightVariantMedium: Dp,
    var minHeightVariantLarge: Dp,
    var gradientStop1: Float,
    var gradientStop2: Float,
    var gradientStop3: Float,
    var gradientStop4: Float,
    var frame1Opacity: Float,
    var secondGradientStop1: Float, // Not exported by plugin
    var secondGradientStop2: Float, // Not exported by plugin
    var secondGradientStop3: Float, // Not exported by plugin
    var frame2Opacity: Float, // Not exported by plugin
    var gradientAngle: Float, // Not exported by plugin
)

var defaultODSSkeletonTokens = ODSSkeletonTokens(
    borderRadiusVariantFull = ODSCorners(all = DSVariables.radiusMedium),
    borderRadiusVariantSmall = ODSCorners(all = DSVariables.radiusSmall),
    borderRadiusVariantMedium = ODSCorners(all = DSVariables.radiusMedium),
    borderRadiusVariantLarge = ODSCorners(all = DSVariables.radiusLarge),
    widthVariantFull = DSVariables.sizingMinimumTappableArea,
    heightVariantFull = DSVariables.sizingMinimumTappableArea,
    clipContent = true,
    maxHeightVariantSmall = DSVariables.sizingComponent14,
    maxHeightVariantMedium = DSVariables.sizingComponent18,
    minHeightVariantSmall = DSVariables.sizingComponent2,
    minHeightVariantMedium = DSVariables.sizingComponent14,
    minHeightVariantLarge = DSVariables.sizingComponent18,
    gradientStop1 = 0.15f,
    gradientStop2 = 0.40f,
    gradientStop3 = 0.60f,
    gradientStop4 = 0.85f,
    frame1Opacity = 0.15f,
    secondGradientStop1 = 0.20f,
    secondGradientStop2 = 0.40f,
    secondGradientStop3 = 0.60f,
    frame2Opacity = 0.75f,
    gradientAngle = 90f
)

var DSSkeletonTokens: ODSSkeletonTokens = defaultODSSkeletonTokens
