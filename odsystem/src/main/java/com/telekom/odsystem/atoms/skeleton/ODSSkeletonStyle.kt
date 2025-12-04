package com.telekom.odsystem.atoms.skeleton

import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSSkeletonStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var borderRadius: ODSCorners? = null
    var width: Dp? = null
    var height: Dp? = null
    var clipContent: Boolean? = null
    var maxHeight: Dp? = null
    var minHeight: Dp? = null
    var frame1Background: List<ODSColorModel>? = null
    var frame2Background: List<ODSColorModel>? = null

    fun getStyle(
        scheme: ODSTheme,
        props: ODSSkeletonProps
    ): ODSSkeletonStyle {
        var style = ODSSkeletonStyle()
        style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
        style.clipContent = DSSkeletonTokens.clipContent
        if (props.variant == ODSSkeletonVariant.FULL) {
            style.borderRadius = DSSkeletonTokens.borderRadiusVariantFull
            style.width = DSSkeletonTokens.widthVariantFull
            style.height = DSSkeletonTokens.heightVariantFull
        }
        if (props.variant == ODSSkeletonVariant.SMALL) {
            style.borderRadius = DSSkeletonTokens.borderRadiusVariantSmall
            style.maxHeight = DSSkeletonTokens.maxHeightVariantSmall
            style.minHeight = DSSkeletonTokens.minHeightVariantSmall
        }
        if (props.variant == ODSSkeletonVariant.LARGE) {
            style.borderRadius = DSSkeletonTokens.borderRadiusVariantLarge
            style.minHeight = DSSkeletonTokens.minHeightVariantLarge
        }
        if (props.variant == ODSSkeletonVariant.MEDIUM) {
            style.borderRadius = DSSkeletonTokens.borderRadiusVariantMedium
            style.maxHeight = DSSkeletonTokens.maxHeightVariantMedium
            style.minHeight = DSSkeletonTokens.minHeightVariantMedium
        }
        // Custom additions
        style.frame1Background = listOf(
            ODSColorModel(
                gradient = ODSLinearGradientModel(
                    colorStops = arrayOf(
                        DSSkeletonTokens.gradientStop1 to scheme.shadesAccentShadesAccentSubtle,
                        DSSkeletonTokens.gradientStop2 to scheme.shadesAccentShadesAccentDominant,
                        DSSkeletonTokens.gradientStop3 to scheme.shadesAccentShadesAccentRecessive,
                        DSSkeletonTokens.gradientStop4 to scheme.shadesAccentShadesAccentSubtle
                    ),
                    angleInDegrees = DSSkeletonTokens.gradientAngle,
                    opacity = DSSkeletonTokens.frame1Opacity
                )
            )
        )
        style.frame2Background = listOf(
            ODSColorModel(
                gradient = ODSLinearGradientModel(
                    colorStops = arrayOf(
                        DSSkeletonTokens.secondGradientStop1 to HexColor("F9CCE3", alpha = 0.15f),
                        DSSkeletonTokens.secondGradientStop2 to scheme.basicTextRecessive,
                        DSSkeletonTokens.secondGradientStop3 to HexColor("F9CCE3", alpha = 0.15f)
                    ),
                    angleInDegrees = DSSkeletonTokens.gradientAngle,
                    opacity = DSSkeletonTokens.frame2Opacity
                )
            )
        )
        return style
    }
}
