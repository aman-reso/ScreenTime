@file:Suppress("All")

package com.telekom.odsystem.charts.core.cartesian.layer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.annotation.FloatRange
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.telekom.odsystem.charts.core.cartesian.CartesianDrawingContext
import com.telekom.odsystem.charts.core.cartesian.CartesianMeasuringContext
import com.telekom.odsystem.charts.core.cartesian.axis.Axis
import com.telekom.odsystem.charts.core.cartesian.axis.VerticalAxis
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartRanges
import com.telekom.odsystem.charts.core.cartesian.data.CartesianLayerRangeProvider
import com.telekom.odsystem.charts.core.cartesian.data.CartesianValueFormatter
import com.telekom.odsystem.charts.core.cartesian.data.LineCartesianLayerDrawingModel
import com.telekom.odsystem.charts.core.cartesian.data.LineCartesianLayerModel
import com.telekom.odsystem.charts.core.cartesian.data.MutableCartesianChartRanges
import com.telekom.odsystem.charts.core.cartesian.data.forEachIn
import com.telekom.odsystem.charts.core.cartesian.marker.CartesianMarker
import com.telekom.odsystem.charts.core.cartesian.marker.LineCartesianLayerMarkerTarget
import com.telekom.odsystem.charts.core.cartesian.marker.MutableLineCartesianLayerMarkerTarget
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.Fill
import com.telekom.odsystem.charts.core.common.Position
import com.telekom.odsystem.charts.core.common.component.Component
import com.telekom.odsystem.charts.core.common.component.TextComponent
import com.telekom.odsystem.charts.core.common.data.CacheStore
import com.telekom.odsystem.charts.core.common.data.CartesianLayerDrawingModelInterpolator
import com.telekom.odsystem.charts.core.common.data.ExtraStore
import com.telekom.odsystem.charts.core.common.data.MutableExtraStore
import com.telekom.odsystem.charts.core.common.doubled
import com.telekom.odsystem.charts.core.common.getBitmap
import com.telekom.odsystem.charts.core.common.getStart
import com.telekom.odsystem.charts.core.common.half
import com.telekom.odsystem.charts.core.common.inBounds
import com.telekom.odsystem.charts.core.common.orZero
import com.telekom.odsystem.charts.core.common.saveLayer
import getRepeating
import java.util.Objects
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Draws the content of line charts.
 *
 * @property lineProvider provides the [Line]s.
 * @property pointSpacingDp the point spacing (in dp).
 * @property rangeProvider overrides the _x_ and _y_ ranges.
 * @property verticalAxisPosition the position of the [VerticalAxis] with which the
 *   [LineCartesianLayer] should be associated. Use this for independent [CartesianLayer] scaling.
 * @property drawingModelInterpolator interpolates the [LineCartesianLayerDrawingModel]s.
 */
@Stable
open class LineCartesianLayer
protected constructor(
    protected val lineProvider: LineProvider,
    protected val pointSpacingDp: Float = Defaults.POINT_SPACING,
    protected val rangeProvider: CartesianLayerRangeProvider = CartesianLayerRangeProvider.auto(),
    protected val verticalAxisPosition: Axis.Position.Vertical? = null,
    protected val drawingModelInterpolator:
    CartesianLayerDrawingModelInterpolator<
            LineCartesianLayerDrawingModel.Entry,
            LineCartesianLayerDrawingModel,
            > =
        CartesianLayerDrawingModelInterpolator.default(),
    protected val drawingModelKey: ExtraStore.Key<LineCartesianLayerDrawingModel>,
) : BaseCartesianLayer<LineCartesianLayerModel>() {
    /**
     * Defines the appearance of a line in a line chart.
     *
     * @property fill draws the line fill.
     * @property stroke defines the style of the stroke.
     * @property areaFill draws the area fill.
     * @property pointProvider provides the [Point]s.
     * @property pointConnector connects the line’s points, thus defining its shape.
     * @property dataLabel used for the data labels.
     * @property dataLabelPosition the vertical position of the data labels relative to the points.
     * @property dataLabelValueFormatter formats the data-label values.
     * @property dataLabelRotationDegrees the data-label rotation (in degrees).
     */
    open class Line(
        protected val fill: LineFill,
        val stroke: LineStroke = LineStroke.Continuous(),
        protected val areaFill: AreaFill? = null,
        val pointProvider: PointProvider? = null,
        val pointConnector: PointConnector = PointConnector.Sharp,
        val dataLabel: TextComponent? = null,
        val dataLabelPosition: Position.Vertical = Position.Vertical.Top,
        val dataLabelValueFormatter: CartesianValueFormatter = CartesianValueFormatter.decimal(),
        val dataLabelRotationDegrees: Float = 0f,
    ) {
        protected val linePaint: Paint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }

        /** Draws the line. */
        fun draw(
            context: CartesianDrawingContext,
            path: Path,
            lineCanvas: Canvas,
            fillCanvas: Canvas,
            verticalAxisPosition: Axis.Position.Vertical?,
        ) {
            with(context) {
                stroke.apply(this, linePaint)
                val halfThickness = stroke.thicknessDp.pixels.half
                areaFill?.draw(context, path, halfThickness, verticalAxisPosition)
                lineCanvas.drawPath(path, linePaint)
                withCanvas(fillCanvas) { fill.draw(context, halfThickness, verticalAxisPosition) }
            }
        }
    }

    /** Draws a [LineCartesianLayer] line’s fill. */
    interface LineFill {
        /** Draws the line fill. */
        fun draw(
            context: CartesianDrawingContext,
            halfLineThickness: Float,
            verticalAxisPosition: Axis.Position.Vertical?,
        )

        /** Houses [LineFill] factory functions. */
        companion object {
            /** Uses a single [Fill]. */
            fun single(fill: Fill): LineFill = SingleLineFill(fill)

            /**
             * Uses [topFill] for the portions of the line that are above the [splitY] line, and
             * analogously for [bottomFill]. (The [splitY] line is an imaginary horizontal line whose _y_
             * value is determined by [splitY].)
             */
            fun double(
                topFill: Fill,
                bottomFill: Fill,
                splitY: (ExtraStore) -> Number = { 0 },
            ): LineFill = DoubleLineFill(topFill, bottomFill, splitY)
        }
    }

    /** Defines the style of a [LineCartesianLayer] line’s stroke. */
    @Immutable
    sealed interface LineStroke {

        /** The stroke thickness (in dp). */
        val thicknessDp: Float

        /** Applies the stroke style to [paint]. */
        fun apply(context: CartesianDrawingContext, paint: Paint)

        /**
         * Produces a continuous stroke.
         *
         * @property cap the stroke cap.
         */
        data class Continuous(
            override val thicknessDp: Float = Defaults.LINE_SPEC_THICKNESS_DP,
            val cap: Paint.Cap = Paint.Cap.BUTT,
        ) : LineStroke {
            override fun apply(context: CartesianDrawingContext, paint: Paint) {
                with(context) {
                    paint.strokeWidth = thicknessDp.pixels
                    paint.strokeCap = cap
                    paint.pathEffect = null
                }
            }
        }

        /**
         * Produces a dashed stroke.
         *
         * @property cap the stroke cap.
         * @property dashLengthDp the dash length (in dp).
         * @property gapLengthDp the gap length (in dp).
         */
        data class Dashed(
            override val thicknessDp: Float = Defaults.LINE_SPEC_THICKNESS_DP,
            val cap: Paint.Cap = Paint.Cap.BUTT,
            val dashLengthDp: Float = Defaults.LINE_DASH_LENGTH,
            val gapLengthDp: Float = Defaults.LINE_GAP_LENGTH,
        ) : LineStroke {
            override fun apply(context: CartesianDrawingContext, paint: Paint) {
                with(context) {
                    paint.strokeWidth = thicknessDp.pixels
                    paint.strokeCap = cap
                    paint.pathEffect =
                        DashPathEffect(floatArrayOf(dashLengthDp.pixels, gapLengthDp.pixels), 0f)
                }
            }
        }

        /** Provides access to [LineStroke] factory functions. */
        companion object
    }

    /** Draws a [LineCartesianLayer] line’s area fill. */
    interface AreaFill {
        /** Draws the area fill. */
        fun draw(
            context: CartesianDrawingContext,
            linePath: Path,
            halfLineThickness: Float,
            verticalAxisPosition: Axis.Position.Vertical?,
        )

        /** Houses [AreaFill] factory functions. */
        companion object {
            /**
             * Uses [fill] for the areas bounded by the [LineCartesianLayer] line and the [splitY] line.
             * (The [splitY] line is an imaginary horizontal line whose _y_ value is determined by
             * [splitY].)
             */
            fun single(fill: Fill, splitY: (ExtraStore) -> Number = { 0 }): AreaFill =
                SingleAreaFill(fill, splitY)

            /**
             * Uses [topFill] for those areas bounded by the [LineCartesianLayer] line and the [splitY]
             * line that are above the [splitY] line, and analogously for [bottomFill]. (The [splitY] line
             * is an imaginary horizontal line whose _y_ value is determined by [splitY].)
             */
            fun double(
                topFill: Fill,
                bottomFill: Fill,
                splitY: (ExtraStore) -> Number = { 0 },
            ): AreaFill = DoubleAreaFill(topFill, bottomFill, splitY)
        }
    }

    /** Connects a [LineCartesianLayer] line’s points, thus defining its shape. */
    fun interface PointConnector {
        /** Connects ([x1], [y2]) and ([x2], [y2]). */
        fun connect(
            context: CartesianDrawingContext,
            path: Path,
            x1: Float,
            y1: Float,
            x2: Float,
            y2: Float,
        )

        /** Houses a [PointConnector] factory function. */
        companion object {
            /** Uses line segments. */
            val Sharp: PointConnector = PointConnector { _, path, _, _, x2, y2 ->
                path.lineTo(x2, y2)
            }

            /**
             * Uses cubic Bézier curves. [curvature], which must be in ([0, 1]], defines their strength.
             */
            fun cubic(
                @FloatRange(from = 0.0, to = 1.0, fromInclusive = false) curvature: Float = 0.5f,
            ): PointConnector = CubicPointConnector(curvature)
        }
    }

    /** Provides [Line]s to [LineCartesianLayer]s. */
    fun interface LineProvider {
        /** Returns the [Line] for the specified series. */
        fun getLine(seriesIndex: Int, extraStore: ExtraStore): Line

        /** Houses [LineProvider] factory functions. */
        companion object {
            private data class Series(private val lines: List<Line>) : LineProvider {
                override fun getLine(seriesIndex: Int, extraStore: ExtraStore) =
                    lines.getRepeating(seriesIndex)
            }

            /**
             * Uses the provided [Line]s. The [Line]s and series are associated by index. If there are
             * more series than [Line]s, [lines] is iterated multiple times.
             */
            fun series(lines: List<Line>): LineProvider = Series(lines)

            /**
             * Uses the provided [Line]s. The [Line]s and series are associated by index. If there are
             * more series than [Line]s, the [Line] list is iterated multiple times.
             */
            fun series(vararg lines: Line): LineProvider = series(lines.toList())
        }
    }

    /**
     * Defines a point style.
     *
     * @param component the point [Component].
     * @property sizeDp the point size (in dp).
     */
    @Immutable
    data class Point(
        private val component: Component,
        val sizeDp: Float = Defaults.POINT_SIZE,
    ) {
        /** Draws a point at ([x], [y]). */
        fun draw(context: CartesianDrawingContext, x: Float, y: Float) {
            val halfSize = context.run { sizeDp.half.pixels }
            component.draw(
                context = context,
                left = x - halfSize,
                top = y - halfSize,
                right = x + halfSize,
                bottom = y + halfSize,
            )
        }
    }

    /** Provides [Point]s to [LineCartesianLayer]s. */
    @Immutable
    interface PointProvider {
        /** Returns the [Point] for the point with the given properties. */
        fun getPoint(
            entry: LineCartesianLayerModel.Entry,
            seriesIndex: Int,
            extraStore: ExtraStore,
        ): Point?

        /** Returns the largest [Point]. */
        fun getLargestPoint(extraStore: ExtraStore): Point?

        /** Houses a [PointProvider] factory function. */
        companion object {
            private data class Single(private val point: Point) : PointProvider {
                override fun getPoint(
                    entry: LineCartesianLayerModel.Entry,
                    seriesIndex: Int,
                    extraStore: ExtraStore,
                ) = point

                override fun getLargestPoint(extraStore: ExtraStore) = point
            }

            /** Uses [point] for each point. */
            fun single(point: Point): PointProvider = Single(point)
        }
    }

    private val _markerTargets = mutableMapOf<Double, List<MutableLineCartesianLayerMarkerTarget>>()

    protected val linePath: Path = Path()

    protected val lineCanvas: Canvas = Canvas()

    protected val lineFillCanvas: Canvas = Canvas()

    private val srcInPaint =
        Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN) }

    protected val cacheKeyNamespace: CacheStore.KeyNamespace = CacheStore.KeyNamespace()

    override val markerTargets: Map<Double, List<CartesianMarker.Target>> = _markerTargets

    /** Creates a [LineCartesianLayer]. */
    constructor(
        lineProvider: LineProvider,
        pointSpacingDp: Float = Defaults.POINT_SPACING,
        rangeProvider: CartesianLayerRangeProvider = CartesianLayerRangeProvider.auto(),
        verticalAxisPosition: Axis.Position.Vertical? = null,
        drawingModelInterpolator:
        CartesianLayerDrawingModelInterpolator<
                LineCartesianLayerDrawingModel.Entry,
                LineCartesianLayerDrawingModel,
                > =
            CartesianLayerDrawingModelInterpolator.default(),
    ) : this(
        lineProvider,
        pointSpacingDp,
        rangeProvider,
        verticalAxisPosition,
        drawingModelInterpolator,
        ExtraStore.Key(),
    )

    override fun drawInternal(context: CartesianDrawingContext, model: LineCartesianLayerModel) {
        with(context) {
            resetTempData()

            val drawingModel = extraStore.getOrNull(drawingModelKey)

            model.series.forEachIndexed { seriesIndex, series ->
                val pointInfoMap = drawingModel?.getOrNull(seriesIndex)

                linePath.rewind()
                val line = lineProvider.getLine(seriesIndex, model.extraStore)

                var prevX = layerBounds.getStart(isLtr = isLtr)
                var prevY = layerBounds.bottom

                val drawingStartAlignmentCorrection =
                    layoutDirectionMultiplier * layerDimensions.startPadding

                val drawingStart =
                    layerBounds.getStart(isLtr = isLtr) + drawingStartAlignmentCorrection - scroll

                forEachPointInBounds(
                    series = series,
                    drawingStart = drawingStart,
                    pointInfoMap = pointInfoMap,
                    drawFullLineLength = line.stroke is LineStroke.Dashed,
                ) { _, x, y, _, _ ->
                    if (linePath.isEmpty) {
                        linePath.moveTo(x, y)
                    } else {
                        line.pointConnector.connect(this, linePath, prevX, prevY, x, y)
                    }
                    prevX = x
                    prevY = y
                }

                canvas.saveLayer(opacity = drawingModel?.opacity ?: 1f)

                val lineBitmap = getBitmap(cacheKeyNamespace, seriesIndex, "line")
                lineCanvas.setBitmap(lineBitmap)
                val lineFillBitmap = getBitmap(cacheKeyNamespace, seriesIndex, "lineFill")
                lineFillCanvas.setBitmap(lineFillBitmap)
                line.draw(context, linePath, lineCanvas, lineFillCanvas, verticalAxisPosition)
                lineCanvas.drawBitmap(lineFillBitmap, 0f, 0f, srcInPaint)
                canvas.drawBitmap(lineBitmap, 0f, 0f, null)

                forEachPointInBounds(series, drawingStart, pointInfoMap) { entry, x, y, _, _ ->
                    updateMarkerTargets(entry, x, y, lineFillBitmap)
                }

                drawPointsAndDataLabels(line, series, seriesIndex, drawingStart, pointInfoMap)

                canvas.restore()
            }
        }
    }

    protected open fun CartesianDrawingContext.updateMarkerTargets(
        entry: LineCartesianLayerModel.Entry,
        canvasX: Float,
        canvasY: Float,
        lineFillBitmap: Bitmap,
    ) {
        if (canvasX <= layerBounds.left - 1 || canvasX >= layerBounds.right + 1) return
        val limitedCanvasY = canvasY.coerceIn(layerBounds.top, layerBounds.bottom)
        _markerTargets
            .getOrPut(entry.x) { listOf(MutableLineCartesianLayerMarkerTarget(entry.x, canvasX)) }
            .first()
            .points +=
            LineCartesianLayerMarkerTarget.Point(
                entry,
                limitedCanvasY,
                lineFillBitmap.getPixel(
                    canvasX
                        .roundToInt()
                        .coerceIn(ceil(layerBounds.left).toInt(), layerBounds.right.toInt() - 1),
                    limitedCanvasY.roundToInt(),
                ),
            )
    }

    protected open fun CartesianDrawingContext.drawPointsAndDataLabels(
        line: Line,
        series: List<LineCartesianLayerModel.Entry>,
        seriesIndex: Int,
        drawingStart: Float,
        pointInfoMap: Map<Double, LineCartesianLayerDrawingModel.Entry>?,
    ) {
        forEachPointInBounds(
            series = series,
            drawingStart = drawingStart,
            pointInfoMap = pointInfoMap,
        ) { chartEntry, x, y, previousX, nextX ->
            val point = line.pointProvider?.getPoint(chartEntry, seriesIndex, model.extraStore)
            point?.draw(this, x, y)

            line.dataLabel
                .takeIf {
                    chartEntry.x != ranges.minX && chartEntry.x != ranges.maxX ||
                            chartEntry.x == ranges.minX && layerDimensions.startPadding > 0 ||
                            chartEntry.x == ranges.maxX && layerDimensions.endPadding > 0
                }
                ?.let { textComponent ->
                    val distanceFromLine =
                        max(line.stroke.thicknessDp, point?.sizeDp.orZero).half.pixels

                    val text = line.dataLabelValueFormatter.format(
                        this,
                        chartEntry.y,
                        verticalAxisPosition
                    )
                    val maxWidth = getMaxDataLabelWidth(chartEntry, x, previousX, nextX)
                    val verticalPosition =
                        line.dataLabelPosition.inBounds(
                            bounds = layerBounds,
                            componentHeight =
                                textComponent.getHeight(
                                    context = this,
                                    text = text,
                                    maxWidth = maxWidth,
                                    rotationDegrees = line.dataLabelRotationDegrees,
                                ),
                            referenceY = y,
                            referenceDistance = distanceFromLine,
                        )
                    val dataLabelY =
                        y +
                                when (verticalPosition) {
                                    Position.Vertical.Top -> -distanceFromLine
                                    Position.Vertical.Center -> 0f
                                    Position.Vertical.Bottom -> distanceFromLine
                                }
                    textComponent.draw(
                        context = this,
                        x = x,
                        y = dataLabelY,
                        text = text,
                        verticalPosition = verticalPosition,
                        maxWidth = maxWidth,
                        rotationDegrees = line.dataLabelRotationDegrees,
                    )
                }
        }
    }

    protected fun CartesianDrawingContext.getMaxDataLabelWidth(
        entry: LineCartesianLayerModel.Entry,
        x: Float,
        previousX: Float?,
        nextX: Float?,
    ): Int =
        when {
            previousX != null && nextX != null -> min(x - previousX, nextX - x)
            previousX == null && nextX == null ->
                min(layerDimensions.startPadding, layerDimensions.endPadding).doubled

            nextX != null -> {
                ((entry.x - ranges.minX) / ranges.xStep * layerDimensions.xSpacing +
                        layerDimensions.startPadding)
                    .doubled
                    .toFloat()
                    .coerceAtMost(nextX - x)
            }

            else -> {
                ((ranges.maxX - entry.x) / ranges.xStep * layerDimensions.xSpacing +
                        layerDimensions.endPadding)
                    .doubled
                    .toFloat()
                    .coerceAtMost(x - previousX!!)
            }
        }.toInt()

    protected fun resetTempData() {
        _markerTargets.clear()
        linePath.rewind()
    }

    protected open fun CartesianDrawingContext.forEachPointInBounds(
        series: List<LineCartesianLayerModel.Entry>,
        drawingStart: Float,
        pointInfoMap: Map<Double, LineCartesianLayerDrawingModel.Entry>?,
        drawFullLineLength: Boolean = false,
        action:
            (
            entry: LineCartesianLayerModel.Entry,
            x: Float,
            y: Float,
            previousX: Float?,
            nextX: Float?,
        ) -> Unit,
    ) {
        val minX = ranges.minX
        val maxX = ranges.maxX
        val xStep = ranges.xStep

        var x: Float? = null
        var nextX: Float? = null

        val boundsStart = layerBounds.getStart(isLtr = isLtr)
        val boundsEnd = boundsStart + layoutDirectionMultiplier * layerBounds.width()

        fun getDrawX(entry: LineCartesianLayerModel.Entry): Float =
            drawingStart +
                    layoutDirectionMultiplier * layerDimensions.xSpacing * ((entry.x - minX) / xStep).toFloat()

        fun getDrawY(entry: LineCartesianLayerModel.Entry): Float {
            val yRange = ranges.getYRange(verticalAxisPosition)
            return layerBounds.bottom -
                    (pointInfoMap?.get(entry.x)?.y
                        ?: ((entry.y - yRange.minY) / yRange.length).toFloat()) *
                    layerBounds.height()
        }

        series.forEachIn(minX = minX, maxX = maxX, padding = 1) { entry, next ->
            val previousX = x
            val immutableX = nextX ?: getDrawX(entry)
            val immutableNextX = next?.let(::getDrawX)
            x = immutableX
            nextX = immutableNextX
            if (
                drawFullLineLength.not() &&
                immutableNextX != null &&
                (isLtr && immutableX < boundsStart || !isLtr && immutableX > boundsStart) &&
                (isLtr && immutableNextX < boundsStart || !isLtr && immutableNextX > boundsStart)
            ) {
                return@forEachIn
            }
            action(entry, immutableX, getDrawY(entry), previousX, nextX)
            if (isLtr && immutableX > boundsEnd || isLtr.not() && immutableX < boundsEnd) return
        }
    }

    override fun updateDimensions(
        context: CartesianMeasuringContext,
        dimensions: MutableCartesianLayerDimensions,
        model: LineCartesianLayerModel,
    ) {
        with(context) {
            val maxPointSize =
                (0..<model.series.size)
                    .maxOf {
                        lineProvider
                            .getLine(it, model.extraStore)
                            .pointProvider
                            ?.getLargestPoint(model.extraStore)
                            ?.sizeDp
                            .orZero
                    }
                    .pixels
            val xSpacing = maxPointSize + pointSpacingDp.pixels
            dimensions.ensureValuesAtLeast(
                xSpacing = xSpacing,
                scalableStartPadding = layerPadding.scalableStartDp.pixels,
                scalableEndPadding = layerPadding.scalableEndDp.pixels,
                unscalableStartPadding = maxPointSize.half + layerPadding.unscalableStartDp.pixels,
                unscalableEndPadding = maxPointSize.half + layerPadding.unscalableEndDp.pixels,
            )
        }
    }

    override fun updateChartRanges(
        chartRanges: MutableCartesianChartRanges,
        model: LineCartesianLayerModel,
    ) {
        chartRanges.tryUpdate(
            rangeProvider.getMinX(model.minX, model.maxX, model.extraStore),
            rangeProvider.getMaxX(model.minX, model.maxX, model.extraStore),
            rangeProvider.getMinY(model.minY, model.maxY, model.extraStore),
            rangeProvider.getMaxY(model.minY, model.maxY, model.extraStore),
            verticalAxisPosition,
        )
    }

    override fun updateLayerMargins(
        context: CartesianMeasuringContext,
        layerMargins: CartesianLayerMargins,
        layerDimensions: CartesianLayerDimensions,
        model: LineCartesianLayerModel,
    ) {
        with(context) {
            val verticalMargin =
                (0..<model.series.size)
                    .mapNotNull { lineProvider.getLine(it, model.extraStore) }
                    .maxOf {
                        max(
                            it.stroke.thicknessDp,
                            it.pointProvider?.getLargestPoint(model.extraStore)?.sizeDp.orZero,
                        )
                    }
                    .half
                    .pixels
            layerMargins.ensureValuesAtLeast(top = verticalMargin, bottom = verticalMargin)
        }
    }

    override fun prepareForTransformation(
        model: LineCartesianLayerModel?,
        ranges: CartesianChartRanges,
        extraStore: MutableExtraStore,
    ) {
        drawingModelInterpolator.setModels(
            old = extraStore.getOrNull(drawingModelKey),
            new = model?.toDrawingModel(ranges),
        )
    }

    override suspend fun transform(extraStore: MutableExtraStore, fraction: Float) {
        drawingModelInterpolator.transform(fraction)?.let { extraStore[drawingModelKey] = it }
            ?: extraStore.remove(drawingModelKey)
    }

    private fun LineCartesianLayerModel.toDrawingModel(
        ranges: CartesianChartRanges,
    ): LineCartesianLayerDrawingModel {
        val yRange = ranges.getYRange(verticalAxisPosition)
        return LineCartesianLayerDrawingModel(
            series.map { series ->
                series.associate { entry ->
                    entry.x to
                            LineCartesianLayerDrawingModel.Entry(
                                ((entry.y - yRange.minY) / yRange.length).toFloat()
                            )
                }
            }
        )
    }

    /** Creates a new [LineCartesianLayer] based on this one. */
    fun copy(
        lineProvider: LineProvider = this.lineProvider,
        pointSpacingDp: Float = this.pointSpacingDp,
        rangeProvider: CartesianLayerRangeProvider = this.rangeProvider,
        verticalAxisPosition: Axis.Position.Vertical? = this.verticalAxisPosition,
        drawingModelInterpolator:
        CartesianLayerDrawingModelInterpolator<
                LineCartesianLayerDrawingModel.Entry,
                LineCartesianLayerDrawingModel,
                > =
            this.drawingModelInterpolator,
    ): LineCartesianLayer =
        LineCartesianLayer(
            lineProvider,
            pointSpacingDp,
            rangeProvider,
            verticalAxisPosition,
            drawingModelInterpolator,
            drawingModelKey,
        )

    override fun equals(other: Any?): Boolean =
        this === other ||
                other is LineCartesianLayer &&
                lineProvider == other.lineProvider &&
                pointSpacingDp == other.pointSpacingDp &&
                rangeProvider == other.rangeProvider &&
                verticalAxisPosition == other.verticalAxisPosition &&
                drawingModelInterpolator == other.drawingModelInterpolator

    override fun hashCode(): Int =
        Objects.hash(
            lineProvider,
            pointSpacingDp,
            rangeProvider,
            verticalAxisPosition,
            drawingModelInterpolator,
        )

    /** Provides access to [Line] and [Point] factory functions. */
    companion object
}

internal fun CartesianDrawingContext.getCanvasSplitY(
    splitY: (ExtraStore) -> Number,
    halfLineThickness: Float,
    verticalAxisPosition: Axis.Position.Vertical?,
): Float {
    val yRange = ranges.getYRange(verticalAxisPosition)
    val base =
        layerBounds.bottom -
                ((splitY(model.extraStore).toDouble() - yRange.minY) / yRange.length).toFloat() *
                layerBounds.height()
    return ceil(base).coerceIn(layerBounds.top..layerBounds.bottom) + ceil(halfLineThickness)
}
