package com.telekom.odsystem.charts.compose.cartesian.axis

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.charts.core.cartesian.axis.BaseAxis

/** Creates a [BaseAxis.Size.Auto] instance. */
public fun BaseAxis.Size.Companion.auto(
    min: Dp = 0.dp,
    max: Dp = Float.MAX_VALUE.dp,
): BaseAxis.Size.Auto = BaseAxis.Size.Auto(min.value, max.value)

/** Creates a [BaseAxis.Size.Fixed] instance. */
public fun BaseAxis.Size.Companion.fixed(value: Dp): BaseAxis.Size.Fixed =
    BaseAxis.Size.Fixed(value.value)

/** Creates a [BaseAxis.Size.Fraction] instance. */
public fun BaseAxis.Size.Companion.fraction(fraction: Float): BaseAxis.Size.Fraction =
    BaseAxis.Size.Fraction(fraction)

/** Creates a [BaseAxis.Size.Text] instance. */
public fun BaseAxis.Size.Companion.text(text: CharSequence): BaseAxis.Size.Text =
    BaseAxis.Size.Text(text)
