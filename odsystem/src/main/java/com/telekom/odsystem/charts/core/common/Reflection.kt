package com.telekom.odsystem.charts.core.common

internal inline fun <reified T, V> T.setFieldValue(fieldName: String, value: V) {
  val field = T::class.java.getDeclaredField(fieldName)
  val wasAccessible = field.isAccessible
  field.isAccessible = true
  field.set(this, value)
  field.isAccessible = wasAccessible
}
