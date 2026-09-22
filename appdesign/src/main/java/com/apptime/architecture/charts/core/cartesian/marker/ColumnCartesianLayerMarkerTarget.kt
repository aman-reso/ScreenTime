package com.telekom.odsystem.charts.core.cartesian.marker

import com.telekom.odsystem.charts.core.common.component.LineComponent
import com.telekom.odsystem.charts.core.cartesian.data.ColumnCartesianLayerModel
import com.telekom.odsystem.charts.core.cartesian.layer.ColumnCartesianLayer

/** Houses information on a set of [ColumnCartesianLayer] columns to be marked. */
interface ColumnCartesianLayerMarkerTarget : CartesianMarker.Target {
    /**
     * Holds [Column] instances, each of which houses information on a [ColumnCartesianLayer] column
     * to be marked.
     */
    val columns: List<Column>

    /**
     * Houses information on a [ColumnCartesianLayer] column to be marked.
     *
     * @param entry the [ColumnCartesianLayerModel.Entry].
     * @param canvasY the pixel _y_ coordinate of the column’s top or bottom edge (depending on the
     *   sign of [ColumnCartesianLayerModel.Entry.y]).
     * @param color the column [LineComponent]’s color.
     */
    data class Column(
        val entry: ColumnCartesianLayerModel.Entry,
        val canvasY: Float,
        val color: Int,
    )
}

internal data class MutableColumnCartesianLayerMarkerTarget(
    override val x: Double,
    override val canvasX: Float,
    override val columns: MutableList<ColumnCartesianLayerMarkerTarget.Column> = mutableListOf(),
) : ColumnCartesianLayerMarkerTarget
