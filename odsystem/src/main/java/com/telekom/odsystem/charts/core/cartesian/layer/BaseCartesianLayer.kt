package com.telekom.odsystem.charts.core.cartesian.layer

import com.telekom.odsystem.charts.core.cartesian.CartesianDrawingContext
import com.telekom.odsystem.charts.core.common.inClip
import com.telekom.odsystem.charts.core.cartesian.data.CartesianLayerModel

/** A base [CartesianLayer] implementation. */
public abstract class BaseCartesianLayer<T : CartesianLayerModel> : CartesianLayer<T> {
    private val margins: CartesianLayerMargins = CartesianLayerMargins()

    protected abstract fun drawInternal(context: CartesianDrawingContext, model: T)

    override fun draw(context: CartesianDrawingContext, model: T) {
        with(context) {
            margins.clear()
            updateLayerMargins(this, margins, layerDimensions, model)
            canvas.inClip(
                left = layerBounds.left - margins.getLeft(isLtr),
                top = layerBounds.top - margins.top,
                right = layerBounds.right + margins.getRight(isLtr),
                bottom = layerBounds.bottom + margins.bottom,
            ) {
                drawInternal(context, model)
            }
        }
    }
}
