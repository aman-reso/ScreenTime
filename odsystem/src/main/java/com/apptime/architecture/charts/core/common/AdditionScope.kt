package com.telekom.odsystem.charts.core.common

/** Facilitates adding elements to a collection. */
fun interface AdditionScope<E> {
  /** Adds [element]. */
  fun add(element: E)
}
