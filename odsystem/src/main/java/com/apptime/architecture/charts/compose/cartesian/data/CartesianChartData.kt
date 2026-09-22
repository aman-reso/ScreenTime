package com.telekom.odsystem.charts.compose.cartesian.data

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.telekom.odsystem.charts.core.common.data.ExtraStore
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartModel
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartRanges

@Immutable
internal class CartesianChartData(
    val model: CartesianChartModel? = null,
    val previousModel: CartesianChartModel? = null,
    val ranges: CartesianChartRanges = CartesianChartRanges.Empty,
    val extraStore: ExtraStore = ExtraStore.Empty,
)

internal operator fun CartesianChartData.component1(): CartesianChartModel? = model

internal operator fun CartesianChartData.component2(): CartesianChartModel? = previousModel

internal operator fun CartesianChartData.component3(): CartesianChartRanges = ranges

internal operator fun CartesianChartData.component4(): ExtraStore = extraStore

internal class CartesianChartDataState : State<CartesianChartData> {
    private var previousModel: CartesianChartModel? = null

    override var value by mutableStateOf(CartesianChartData())
        private set

    fun set(model: CartesianChartModel?, ranges: CartesianChartRanges, extraStore: ExtraStore) {
        val currentModel = value.model
        if (model?.id != currentModel?.id) previousModel = currentModel
        value = CartesianChartData(model, previousModel, ranges, extraStore)
    }
}
