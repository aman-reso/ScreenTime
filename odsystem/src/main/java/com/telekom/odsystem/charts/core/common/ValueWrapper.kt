package com.telekom.odsystem.charts.core.common

import androidx.annotation.RestrictTo
import kotlin.reflect.KProperty

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) public class ValueWrapper<T>(public var value: T)

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public operator fun <T> ValueWrapper<T>.getValue(thisObj: Any?, property: KProperty<*>): T = value

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public operator fun <T> ValueWrapper<T>.setValue(thisObj: Any?, property: KProperty<*>, value: T) {
  this.value = value
}

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public operator fun <T> ValueWrapper<T>.component1(): T = value

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public operator fun <T> ValueWrapper<T>.component2(): (T) -> Unit = { value = it }
