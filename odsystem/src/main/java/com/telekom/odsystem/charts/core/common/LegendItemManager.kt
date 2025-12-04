package com.telekom.odsystem.charts.core.common

import com.telekom.odsystem.charts.core.cartesian.CartesianMeasuringContext
import com.telekom.odsystem.charts.core.common.data.ExtraStore

internal class LegendItemManager(
  private val items: AdditionScope<LegendItem>.(ExtraStore) -> Unit
) {
  private val _itemList = mutableListOf<LegendItem>()
  val itemList: List<LegendItem> = _itemList
  private val itemScope = AdditionScope(_itemList::add)
  private var previousExtraStoreHashCode: Int? = null

  fun addItems(context: MeasuringContext) {
    with(context) {
      require(this is CartesianMeasuringContext) { "Unexpected `MeasuringContext` implementation." }
      val extraStoreHashCode = model.extraStore.hashCode()
      if (extraStoreHashCode != previousExtraStoreHashCode) {
        _itemList.clear()
        items(itemScope, model.extraStore)
        previousExtraStoreHashCode = extraStoreHashCode
      }
    }
  }
}
