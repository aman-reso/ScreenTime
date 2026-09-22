package com.telekom.odsystem.charts.core.cartesian.marker

import com.telekom.odsystem.charts.core.cartesian.data.LineCartesianLayerModel
import com.telekom.odsystem.charts.core.cartesian.layer.LineCartesianLayer

/** Houses information on a set of [LineCartesianLayer] points to be marked. */
interface LineCartesianLayerMarkerTarget : CartesianMarker.Target {
    /** Holds [Point] instances, each of which houses information on a marked point. */
    val points: List<Point>

    /**
     * Houses information on a [LineCartesianLayer] point to be marked.
     *
     * @param entry the [LineCartesianLayerModel.Entry].
     * @param canvasY the point’s pixel _y_ coordinate.
     * @param color the [LineCartesianLayer.Line]’s color for the point.
     */
    data class Point(
        val entry: LineCartesianLayerModel.Entry,
        val canvasY: Float,
        val color: Int,
    )
}

internal data class MutableLineCartesianLayerMarkerTarget(
    override val x: Double,
    override val canvasX: Float,
    override val points: MutableList<LineCartesianLayerMarkerTarget.Point> = mutableListOf(),
) : LineCartesianLayerMarkerTarget
