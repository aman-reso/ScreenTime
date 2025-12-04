package com.telekom.odsystem.charts.core.common

import androidx.annotation.RestrictTo

internal const val ERR_REPEATING_COLLECTION_EMPTY =
  "Cannot get repeated item from empty collection."

internal const val ELLIPSIS = "…"

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public const val NEW_PRODUCER_ERROR_MESSAGE: String =
  "A new `CartesianChartModelProducer` was provided. Run data updates via `runTransaction`, not " +
    "by creating new `CartesianChartModelProducer`s."
