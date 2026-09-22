package com.telekom.odsystem.charts.compose.common

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.charts.core.common.Insets

/** Creates an [Insets] instance. */
fun insets(all: Dp = 0.dp): Insets = Insets(all.value)

/** Creates an [Insets] instance. */
fun insets(horizontal: Dp = 0.dp, vertical: Dp = 0.dp): Insets =
    Insets(horizontal.value, vertical.value)

/** Creates an [Insets] instance. */
fun insets(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp): Insets =
    Insets(start.value, top.value, end.value, bottom.value)
