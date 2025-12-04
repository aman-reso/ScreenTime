package com.telekom.odsystem.charts.compose.common.shader

import android.graphics.Shader
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.core.graphics.translationMatrix
import com.telekom.odsystem.charts.core.common.DrawingContext
import com.telekom.odsystem.charts.core.common.component.Component
import com.telekom.odsystem.charts.core.common.shader.CachingShaderProvider
import com.telekom.odsystem.charts.core.common.shader.LinearGradientShaderProvider
import com.telekom.odsystem.charts.core.common.shader.ShaderProvider

/** A [Dp] version of [ShaderProvider.component]. */
public fun ShaderProvider.Companion.component(
    component: Component,
    componentSize: Dp,
    checker: Boolean = true,
    xTileMode: Shader.TileMode = Shader.TileMode.REPEAT,
    yTileMode: Shader.TileMode = xTileMode,
): ShaderProvider = component(component, componentSize.value, checker, xTileMode, yTileMode)

/** A [Color] version of [ShaderProvider.horizontalGradient]. */
public fun ShaderProvider.Companion.horizontalGradient(
    colors: Array<Color>,
    positions: FloatArray? = null,
): ShaderProvider =
    LinearGradientShaderProvider(IntArray(colors.size) { colors[it].toArgb() }, positions, true)

/** A [Color] version of [ShaderProvider.verticalGradient]. */
public fun ShaderProvider.Companion.verticalGradient(
    colors: Array<Color>,
    positions: FloatArray? = null,
): ShaderProvider =
    LinearGradientShaderProvider(IntArray(colors.size) { colors[it].toArgb() }, positions, false)

/** Converts this [Brush] to a [ShaderProvider]. */
public fun Brush.toShaderProvider(): ShaderProvider =
    object : CachingShaderProvider() {
        override fun createShader(
            context: DrawingContext,
            left: Float,
            top: Float,
            right: Float,
            bottom: Float,
        ): Shader {
            val paint = Paint()
            applyTo(size = Size(right - left, bottom - top), p = paint, alpha = 1f)
            return paint.shader?.apply { setLocalMatrix(translationMatrix(left, top)) }
                ?: emptyShader
        }
    }

private val emptyShader = object : Shader() {}
