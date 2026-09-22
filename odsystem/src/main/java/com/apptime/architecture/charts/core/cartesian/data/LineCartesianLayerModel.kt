package com.telekom.odsystem.charts.core.cartesian.data

import com.telekom.odsystem.charts.core.cartesian.layer.LineCartesianLayer
import com.telekom.odsystem.charts.core.common.data.ExtraStore
import com.telekom.odsystem.charts.core.common.rangeOf
import com.telekom.odsystem.charts.core.common.rangeOfPair

/** Stores a [LineCartesianLayer]’s data. */
class LineCartesianLayerModel : CartesianLayerModel {
    private val entries: List<Entry>

    /** The series (lists of [Entry] instances). */
    val series: List<List<Entry>>

    override val id: Int

    override val minX: Double

    override val maxX: Double

    override val minY: Double

    override val maxY: Double

    override val extraStore: ExtraStore

    constructor(series: List<List<Entry>>) : this(series, ExtraStore.Empty)

    private constructor(series: List<List<Entry>>, extraStore: ExtraStore) {
        require(series.isNotEmpty()) { "At least one series should be added." }
        this.series =
            series.map { entries ->
                require(entries.isNotEmpty()) { "Series can’t be empty." }
                entries.sortedBy { entry -> entry.x }
            }
        this.entries = this.series.flatten()
        val xRange = this.series.rangeOfPair { it.first().x to it.last().x }
        val yRange = entries.rangeOf { it.y }
        this.id = this.series.hashCode()
        this.minX = xRange.start
        this.maxX = xRange.endInclusive
        this.minY = yRange.start
        this.maxY = yRange.endInclusive
        this.extraStore = extraStore
    }

    private constructor(
        entries: List<Entry>,
        series: List<List<Entry>>,
        id: Int,
        minX: Double,
        maxX: Double,
        minY: Double,
        maxY: Double,
        extraStore: ExtraStore,
    ) {
        this.entries = entries
        this.series = series
        this.id = id
        this.minX = minX
        this.maxX = maxX
        this.minY = minY
        this.maxY = maxY
        this.extraStore = extraStore
    }

    override fun getXDeltaGcd(): Double = entries.getXDeltaGcd()

    override fun copy(extraStore: ExtraStore): CartesianLayerModel =
        LineCartesianLayerModel(entries, series, id, minX, maxX, minY, maxY, extraStore)

    override fun equals(other: Any?): Boolean =
        this === other ||
                other is LineCartesianLayerModel &&
                series == other.series &&
                id == other.id &&
                minX == other.minX &&
                maxX == other.maxX &&
                minY == other.minY &&
                maxY == other.maxY &&
                extraStore == other.extraStore

    override fun hashCode(): Int {
        var result = series.hashCode()
        result = 31 * result + id
        result = 31 * result + minX.hashCode()
        result = 31 * result + maxX.hashCode()
        result = 31 * result + minY.hashCode()
        result = 31 * result + maxY.hashCode()
        result = 31 * result + extraStore.hashCode()
        return result
    }

    /** Represents a line node at ([x], [y]). */
    class Entry internal constructor(override val x: Double, val y: Double) :
        CartesianLayerModel.Entry {
        constructor(x: Number, y: Number) : this(x.toDouble(), y.toDouble())

        override fun equals(other: Any?): Boolean =
            this === other || other is Entry && x == other.x && y == other.y

        override fun hashCode(): Int = 31 * x.hashCode() + y.hashCode()
    }

    /**
     * Stores the minimum amount of data required to create a [LineCartesianLayerModel] and
     * facilitates this creation.
     */
    class Partial(private val series: List<List<Entry>>) : CartesianLayerModel.Partial {
        override fun complete(extraStore: ExtraStore): CartesianLayerModel =
            LineCartesianLayerModel(series, extraStore)

        override fun equals(other: Any?): Boolean =
            this === other || other is Partial && series == other.series

        override fun hashCode(): Int = series.hashCode()
    }

    /** Facilitates the creation of [LineCartesianLayerModel]s and [Partial]s. */
    class BuilderScope internal constructor() {
        internal val series = mutableListOf<List<Entry>>()

        /**
         * Adds a series with the provided _x_ values ([x]) and _y_ values ([y]). [x] and [y] should
         * have the same size.
         */
        fun series(x: Collection<Number>, y: Collection<Number>) {
            series.add(x.zip(y, LineCartesianLayerModel::Entry))
        }

        /** Adds a series with the provided _y_ values ([y]), using their indices as the _x_ values. */
        fun series(y: Collection<Number>) {
            series(y.indices.toList(), y)
        }

        /** Adds a series with the provided _y_ values ([y]), using their indices as the _x_ values. */
        fun series(vararg y: Number) {
            series(y.toList())
        }
    }

    companion object {
        /** Creates a [LineCartesianLayerModel]. */
        fun build(block: BuilderScope.() -> Unit): LineCartesianLayerModel =
            LineCartesianLayerModel(BuilderScope().apply(block).series)

        /** Creates a [Partial]. */
        fun partial(block: BuilderScope.() -> Unit): Partial =
            Partial(BuilderScope().apply(block).series)
    }
}

/**
 * Calls [block] to create a [LineCartesianLayerModel.Partial] and adds it to the
 * [CartesianChartModelProducer.Transaction]’s [CartesianLayerModel.Partial] list.
 */
fun CartesianChartModelProducer.Transaction.lineSeries(
    block: LineCartesianLayerModel.BuilderScope.() -> Unit
) {
    add(LineCartesianLayerModel.partial(block))
}
