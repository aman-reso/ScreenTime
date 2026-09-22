package com.telekom.odsystem.charts.core.common.data

import androidx.annotation.RestrictTo

/** Caches data. */
class CacheStore @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) constructor() {
  private var map = mutableMapOf<String, Any>()
  private var purgedMap = mutableMapOf<String, Any>()

  /**
   * Retrieves the value associated with the key belonging to the specified namespace and matching
   * the given components. If there’s no such value, `null` is returned.
   */
  fun <T : Any> getOrNull(keyNamespace: KeyNamespace, vararg keyComponents: Any?): T? {
    val key = keyNamespace.getKey(*keyComponents)
    val value = map[key]
    if (value != null) purgedMap[key] = value
    @Suppress("UNCHECKED_CAST")
    return value as T?
  }

  /** Caches [value]. */
  operator fun set(keyNamespace: KeyNamespace, vararg keyComponents: Any?, value: Any) {
    val key = keyNamespace.getKey(*keyComponents)
    map[key] = value
    purgedMap[key] = value
  }

  /**
   * Retrieves the value associated with the key belonging to the specified namespace and matching
   * the given components. If there’s no such value, [value] is called, and its result is cached and
   * returned.
   */
  fun <T : Any> getOrSet(
    keyNamespace: KeyNamespace,
    vararg keyComponents: Any?,
    value: () -> T,
  ): T =
    getOrNull(keyNamespace, *keyComponents)
      ?: value().also { set(keyNamespace, *keyComponents, value = it) }

  internal fun purge() {
    map = purgedMap
    purgedMap = mutableMapOf()
  }

  /** Identifies a key namespace. These namespaces help prevent interscope key collisions. */
  class KeyNamespace {
    internal fun getKey(vararg components: Any?) =
      components.joinToString(prefix = "${hashCode()}, ")
  }
}
