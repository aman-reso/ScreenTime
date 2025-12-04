package com.telekom.odsystem.charts.core.cartesian.data

import androidx.annotation.RestrictTo
import com.telekom.odsystem.charts.core.cartesian.CartesianChart
import com.telekom.odsystem.charts.core.common.data.CartesianLayerDrawingModel
import com.telekom.odsystem.charts.core.common.data.ExtraStore
import com.telekom.odsystem.charts.core.common.gcdWith

/** Stores a [CartesianChart]’s data. */
public class CartesianChartModel {
    /** The [CartesianLayerModel]s. */
    public val models: List<CartesianLayerModel>

    /** Identifies this [CartesianChartModel] in terms of the [CartesianLayerModel.id]s. */
    public val id: Int

    /**
     * Expresses the size of this [CartesianChartModel] in terms of the range of the _x_ values
     * covered.
     */
    public val width: Double

    /** Stores auxiliary data, including [CartesianLayerDrawingModel]s. */
    public val extraStore: ExtraStore

    /** Creates a [CartesianChartModel] consisting of the given [CartesianLayerModel]s. */
    public constructor(models: List<CartesianLayerModel>) : this(models, ExtraStore.Empty)

    /** Creates a [CartesianChartModel] consisting of the given [CartesianLayerModel]s. */
    public constructor(vararg models: CartesianLayerModel) : this(models.toList())

    internal constructor(
        models: List<CartesianLayerModel>,
        extraStore: ExtraStore,
    ) : this(
        models = models,
        id = models.map { it.id }.hashCode(),
        width = models.maxOf { it.maxX } - models.minOf { it.minX },
        extraStore = extraStore,
    )

    internal constructor(
        models: List<CartesianLayerModel>,
        id: Int,
        width: Double,
        extraStore: ExtraStore,
    ) {
        this.models = models
        this.id = id
        this.width = width
        this.extraStore = extraStore
    }

    /** Returns the greatest common divisor of the _x_ values’ differences. */
    public fun getXDeltaGcd(): Double =
        models.fold<CartesianLayerModel, Double?>(null) { gcd, layerModel ->
            val layerModelGcd = layerModel.getXDeltaGcd()
            gcd?.gcdWith(layerModelGcd) ?: layerModelGcd
        } ?: 1.0

    /**
     * Creates a copy of this [CartesianChartModel] with the given [ExtraStore], which is also applied
     * to the [CartesianLayerModel]s.
     */
    public fun copy(extraStore: ExtraStore): CartesianChartModel =
        CartesianChartModel(models.map { it.copy(extraStore) }, id, width, extraStore)

    /** Creates an immutable copy of this [CartesianChartModel]. */
    public fun toImmutable(): CartesianChartModel = this

    /** @suppress */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public companion object {
        public val Empty: CartesianChartModel =
            CartesianChartModel(
                models = emptyList(),
                id = 0,
                width = 0.0,
                extraStore = ExtraStore.Empty
            )
    }
}
