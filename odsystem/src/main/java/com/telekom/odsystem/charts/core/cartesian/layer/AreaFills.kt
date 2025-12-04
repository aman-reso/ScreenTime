@file:Suppress("All")

package com.telekom.odsystem.charts.core.cartesian.layer

import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.telekom.odsystem.charts.core.cartesian.CartesianDrawingContext
import com.telekom.odsystem.charts.core.cartesian.axis.Axis
import com.telekom.odsystem.charts.core.common.DefaultAlpha
import com.telekom.odsystem.charts.core.common.Fill
import com.telekom.odsystem.charts.core.common.copyColor
import com.telekom.odsystem.charts.core.common.data.ExtraStore
import com.telekom.odsystem.charts.core.common.getEnd
import com.telekom.odsystem.charts.core.common.getStart
import com.telekom.odsystem.charts.core.common.shader.ShaderProvider
import com.telekom.odsystem.charts.core.common.shader.getShader

internal abstract class BaseAreaFill(open val splitY: (ExtraStore) -> Number) :
    LineCartesianLayer.AreaFill {
    private val areaBounds = RectF()
    private val areaPath = Path()
    private val clipPath = Path()
    private val fillBounds = RectF()

    open fun reset() {}

    abstract fun onTopAreasCreated(context: CartesianDrawingContext, path: Path, fillBounds: RectF)

    abstract fun onBottomAreasCreated(
        context: CartesianDrawingContext,
        path: Path,
        fillBounds: RectF,
    )

    open fun onAreasCreated(context: CartesianDrawingContext, fillBounds: RectF) {}

    override fun draw(
        context: CartesianDrawingContext,
        linePath: Path,
        halfLineThickness: Float,
        verticalAxisPosition: Axis.Position.Vertical?,
    ) {
        reset()
        @Suppress("DEPRECATION") linePath.computeBounds(areaBounds, false)
        with(context) {
            val canvasSplitY = getCanvasSplitY(splitY, halfLineThickness, verticalAxisPosition)
            if (canvasSplitY > layerBounds.top) {
                clipPath.rewind()
                fillBounds.set(layerBounds.left, layerBounds.top, layerBounds.right, canvasSplitY)
                clipPath.addRect(fillBounds, Path.Direction.CW)
                with(areaPath) {
                    set(linePath)
                    lineTo(areaBounds.getEnd(isLtr), layerBounds.bottom)
                    lineTo(areaBounds.getStart(isLtr), layerBounds.bottom)
                    close()
                    op(clipPath, Path.Op.INTERSECT)
                }
                onTopAreasCreated(this, areaPath, fillBounds)
            }
            if (canvasSplitY < layerBounds.bottom) {
                clipPath.rewind()
                fillBounds.set(
                    layerBounds.left,
                    canvasSplitY,
                    layerBounds.right,
                    layerBounds.bottom
                )
                clipPath.addRect(fillBounds, Path.Direction.CW)
                with(areaPath) {
                    set(linePath)
                    lineTo(areaBounds.getEnd(isLtr), layerBounds.top)
                    lineTo(areaBounds.getStart(isLtr), layerBounds.top)
                    close()
                    op(clipPath, Path.Op.INTERSECT)
                }
                onBottomAreasCreated(this, areaPath, fillBounds)
            }
            fillBounds.set(layerBounds)
            onAreasCreated(this, fillBounds)
        }
    }
}

internal data class SingleAreaFill(
    private val fill: Fill,
    override val splitY: (ExtraStore) -> Number,
) : BaseAreaFill(splitY) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val areaPath = Path()

    override fun reset() {
        areaPath.rewind()
    }

    override fun onTopAreasCreated(
        context: CartesianDrawingContext,
        path: Path,
        fillBounds: RectF,
    ) {
        areaPath.addPath(path)
    }

    override fun onBottomAreasCreated(
        context: CartesianDrawingContext,
        path: Path,
        fillBounds: RectF,
    ) {
        areaPath.addPath(path)
    }

    override fun onAreasCreated(context: CartesianDrawingContext, fillBounds: RectF) {
        with(context) {
            paint.color = fill.color
            paint.shader = fill.shaderProvider?.getShader(this, fillBounds)
            canvas.drawPath(areaPath, paint)
        }
    }
}

internal data class DoubleAreaFill(
    private val topFill: Fill,
    private val bottomFill: Fill,
    override val splitY: (ExtraStore) -> Number,
) : BaseAreaFill(splitY) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onTopAreasCreated(
        context: CartesianDrawingContext,
        path: Path,
        fillBounds: RectF,
    ) {
        with(context) {
            paint.color = topFill.color
            paint.shader = topFill.shaderProvider?.getShader(this, fillBounds)
            canvas.drawPath(path, paint)
        }
    }

    override fun onBottomAreasCreated(
        context: CartesianDrawingContext,
        path: Path,
        fillBounds: RectF,
    ) {
        with(context) {
            paint.color = bottomFill.color
            paint.shader = bottomFill.shaderProvider?.getShader(this, fillBounds)
            canvas.drawPath(path, paint)
        }
    }
}

private fun LineCartesianLayer.AreaFill.Companion.default(
    topColor: Int,
    bottomColor: Int,
    splitY: (ExtraStore) -> Number = { 0 },
) =
    double(
        topFill =
            Fill(
                ShaderProvider.verticalGradient(
                    topColor.copyColor(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                    topColor.copyColor(DefaultAlpha.LINE_BACKGROUND_SHADER_END),
                )
            ),
        bottomFill =
            Fill(
                ShaderProvider.verticalGradient(
                    bottomColor.copyColor(DefaultAlpha.LINE_BACKGROUND_SHADER_END),
                    bottomColor.copyColor(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                )
            ),
        splitY = splitY,
    )
