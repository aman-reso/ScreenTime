package com.telekom.odsystem.foundations

/**
 * Created by dmarinopoulos on 29/11/23
 */
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Immutable
class ODSLinearGradient(
    private val colors: List<Color>,
    private val stops: List<Float>? = null,
    private val opacity: Float = 1f,
    private val tileMode: TileMode = TileMode.Clamp,
    angleInDegrees: Float = 0f,
    useAsCssAngle: Boolean = false
) : ShaderBrush() {

    companion object {
        private const val RIGHT_QUADRANT_START = 90f
        private const val RIGHT_QUADRANT_END = 180f
        private const val LEFT_QUADRANT_START = 270f
        private const val LEFT_QUADRANT_END = 360f
    }

    private val normalizedAngle: Float = if (useAsCssAngle) {
        ((RIGHT_QUADRANT_START - angleInDegrees) % LEFT_QUADRANT_END + LEFT_QUADRANT_END) % LEFT_QUADRANT_END
    } else {
        (angleInDegrees % LEFT_QUADRANT_END + LEFT_QUADRANT_END) % LEFT_QUADRANT_END
    }
    private val angleInRadians: Float = Math.toRadians(normalizedAngle.toDouble()).toFloat()

    override fun createShader(size: Size): Shader {
        val (from, to) = getGradientCoordinates(size = size)

        val colorsWithOpacity = colors.map { it.copy(alpha = it.alpha * opacity) }

        return LinearGradientShader(
            colors = colorsWithOpacity,
            colorStops = stops,
            from = from,
            to = to,
            tileMode = tileMode
        )
    }

    private fun getGradientCoordinates(size: Size): Pair<Offset, Offset> {
        val diagonal = sqrt(size.width.pow(2) + size.height.pow(2))
        val angleBetweenDiagonalAndWidth = acos(size.width / diagonal)
        val angleBetweenDiagonalAndGradientLine =
            if ((normalizedAngle > RIGHT_QUADRANT_START && normalizedAngle < RIGHT_QUADRANT_END) ||
                (normalizedAngle > LEFT_QUADRANT_START && normalizedAngle < LEFT_QUADRANT_END)
            ) {
                PI.toFloat() - angleInRadians - angleBetweenDiagonalAndWidth
            } else {
                angleInRadians - angleBetweenDiagonalAndWidth
            }
        val halfGradientLine = abs(cos(angleBetweenDiagonalAndGradientLine) * diagonal) / 2

        val horizontalOffset = halfGradientLine * cos(angleInRadians)
        val verticalOffset = halfGradientLine * sin(angleInRadians)

        val start = size.center + Offset(-horizontalOffset, verticalOffset)
        val end = size.center + Offset(horizontalOffset, -verticalOffset)

        return start to end
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ODSLinearGradient) return false

        if (colors != other.colors) return false
        if (stops != other.stops) return false
        if (normalizedAngle != other.normalizedAngle) return false
        if (tileMode != other.tileMode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = colors.hashCode()
        result = 31 * result + (stops?.hashCode() ?: 0)
        result = 31 * result + normalizedAngle.hashCode()
        result = 31 * result + tileMode.hashCode()
        return result
    }

    override fun toString(): String {
        return "ODSLinearGradient(colors=$colors, " +
                "stops=$stops, " +
                "angle=$normalizedAngle, " +
                "tileMode=$tileMode)"
    }
}

@Stable
fun Brush.Companion.linearGradient(
    linearGradient: ODSLinearGradientModel
): Brush = ODSLinearGradient(
    colors = List(linearGradient.colorStops.size) { i -> linearGradient.colorStops[i].second.getColor() },
    stops = List(linearGradient.colorStops.size) { i -> linearGradient.colorStops[i].first },
    opacity = linearGradient.opacity,
    tileMode = TileMode.Clamp,
    angleInDegrees = linearGradient.angleInDegrees,
    useAsCssAngle = true,
)
