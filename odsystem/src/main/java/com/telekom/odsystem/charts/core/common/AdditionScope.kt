package com.telekom.odsystem.charts.core.common

/** Facilitates adding elements to a collection. */
public fun interface AdditionScope<E> {
  /** Adds [element]. */
  public fun add(element: E)
}
