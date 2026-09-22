package com.telekom.odsystem.charts.core.common

import kotlin.math.max
import kotlin.math.min

internal inline fun <T> Iterable<T>.rangeOf(
  selector: (T) -> Double
): ClosedFloatingPointRange<Double> {
  val iterator = iterator()
  var minValue = selector(iterator.next())
  var maxValue = minValue
  while (iterator.hasNext()) {
    val v = selector(iterator.next())
    minValue = min(minValue, v)
    maxValue = max(maxValue, v)
  }
  return minValue..maxValue
}

internal inline fun <T> Iterable<T>.rangeOfPair(
  selector: (T) -> Pair<Double, Double>
): ClosedFloatingPointRange<Double> {
  val iterator = iterator()
  var (minValue, maxValue) = selector(iterator.next())
  while (iterator.hasNext()) {
    val (negValue, posValue) = selector(iterator.next())
    minValue = min(minValue, negValue)
    maxValue = max(maxValue, posValue)
  }
  return minValue..maxValue
}
