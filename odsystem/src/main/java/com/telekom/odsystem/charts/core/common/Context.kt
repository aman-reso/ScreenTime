package com.telekom.odsystem.charts.core.common

import android.content.Context
import android.util.TypedValue
import androidx.annotation.RestrictTo

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public fun Context.spToPx(sp: Float): Float =
  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
