package com.telekom.odsystem.charts.compose.cartesian

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayerPadding

/** Creates a [CartesianLayerPadding] instance. */
fun cartesianLayerPadding(
    scalableStart: Dp = 0.dp,
    scalableEnd: Dp = 0.dp,
    unscalableStart: Dp = 0.dp,
    unscalableEnd: Dp = 0.dp,
): CartesianLayerPadding =
    CartesianLayerPadding(
        scalableStart.value,
        scalableEnd.value,
        unscalableStart.value,
        unscalableEnd.value,
    )
