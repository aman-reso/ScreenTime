package com.telekom.odsystem.charts.core.cartesian.layer

import android.graphics.Paint
import com.telekom.odsystem.charts.core.cartesian.CartesianDrawingContext
import com.telekom.odsystem.charts.core.common.Fill
import com.telekom.odsystem.charts.core.common.data.ExtraStore
import com.telekom.odsystem.charts.core.cartesian.axis.Axis

internal data class SingleLineFill(val fill: Fill) : LineCartesianLayer.LineFill {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = fill.color }

    override fun draw(
        context: CartesianDrawingContext,
        halfLineThickness: Float,
        verticalAxisPosition: Axis.Position.Vertical?,
    ) {
        with(context) {
            paint.shader =
                fill.shaderProvider?.getShader(
                    this,
                    layerBounds.left,
                    layerBounds.top,
                    layerBounds.right,
                    layerBounds.bottom,
                )
            canvas.drawPaint(paint)
        }
    }
}

internal data class DoubleLineFill(
    val topFill: Fill,
    val bottomFill: Fill,
    val splitY: (ExtraStore) -> Number,
) : LineCartesianLayer.LineFill {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun draw(
        context: CartesianDrawingContext,
        halfLineThickness: Float,
        verticalAxisPosition: Axis.Position.Vertical?,
    ) {
        with(context) {
            val canvasSplitY = getCanvasSplitY(splitY, halfLineThickness, verticalAxisPosition)
            paint.color = topFill.color
            paint.shader =
                topFill.shaderProvider?.getShader(
                    this,
                    layerBounds.left,
                    layerBounds.top - halfLineThickness,
                    layerBounds.right,
                    canvasSplitY,
                )
            canvas.drawRect(
                layerBounds.left,
                layerBounds.top - halfLineThickness,
                layerBounds.right,
                canvasSplitY,
                paint,
            )
            paint.color = bottomFill.color
            paint.shader =
                bottomFill.shaderProvider?.getShader(
                    this,
                    layerBounds.left,
                    canvasSplitY,
                    layerBounds.right,
                    layerBounds.bottom + halfLineThickness,
                )
            canvas.drawRect(
                layerBounds.left,
                canvasSplitY,
                layerBounds.right,
                layerBounds.bottom + halfLineThickness,
                paint,
            )
        }
    }
}
