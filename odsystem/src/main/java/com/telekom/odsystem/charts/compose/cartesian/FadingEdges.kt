package com.telekom.odsystem.charts.compose.cartesian

import android.animation.TimeInterpolator
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.charts.core.cartesian.FadingEdges
import com.telekom.odsystem.charts.core.common.Defaults.FADING_EDGE_VISIBILITY_THRESHOLD_DP
import com.telekom.odsystem.charts.core.common.Defaults.FADING_EDGE_WIDTH_DP

/** Creates and remembers a [FadingEdges] instance. */
@Composable
public fun rememberFadingEdges(
    startWidth: Dp = FadingEdgesDefaults.edgeWidth,
    endWidth: Dp = FadingEdgesDefaults.edgeWidth,
    visibilityThreshold: Dp = FadingEdgesDefaults.visibilityThreshold,
    visibilityEasing: Easing = FadingEdgesDefaults.visibilityEasing,
): FadingEdges =
    remember(startWidth, endWidth, visibilityThreshold, visibilityEasing) {
        FadingEdges(
            startWidth.value,
            endWidth.value,
            visibilityThreshold.value,
            TimeInterpolator(visibilityEasing::transform),
        )
    }

/** Creates and remembers a [FadingEdges] instance. */
@Composable
public fun rememberFadingEdges(
    width: Dp = FadingEdgesDefaults.edgeWidth,
    visibilityThreshold: Dp = FadingEdgesDefaults.visibilityThreshold,
    visibilityEasing: Easing = FadingEdgesDefaults.visibilityEasing,
): FadingEdges =
    rememberFadingEdges(
        startWidth = width,
        endWidth = width,
        visibilityThreshold = visibilityThreshold,
        visibilityEasing = visibilityEasing,
    )

private object FadingEdgesDefaults {
    val edgeWidth = FADING_EDGE_WIDTH_DP.dp
    val visibilityThreshold = FADING_EDGE_VISIBILITY_THRESHOLD_DP.dp
    val visibilityEasing = FastOutSlowInEasing
}
