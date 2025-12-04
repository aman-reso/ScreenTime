package com.telekom.odsystem.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.foundations.ODSColorModel

fun Modifier.coloredShadow(
    color: Color,
    alpha: Float = 0.2f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = 20.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = composed {
    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparent = color.copy(alpha = 0f).toArgb()
    this.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparent
            frameworkPaint.setShadowLayer(
                shadowRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor
            )
            it.drawRoundRect(
                0f,
                0f,
                this.size.width,
                this.size.height,
                borderRadius.toPx(),
                borderRadius.toPx(),
                paint
            )
        }
    }
}

/**
 * Applies a list of backgrounds to a composable, layering them in reverse order.
 *
 * This modifier allows for complex background effects by applying multiple [ODSColorModel]
 * instances. Each `ODSColorModel` can define either a solid color (`hexColor`) or a
 * gradient (`brush`). Backgrounds are applied in reverse order of the list, meaning the
 * last item in the list will be the bottom-most layer. If a `ODSColorModel` has neither
 * a `hexColor` nor a `brush`, a transparent background will be applied for that layer.
 *
 * @param background An optional list of [ODSColorModel] objects defining the backgrounds
 *                   to apply. If null or empty, the modifier has no effect.
 * @param shape The [RoundedCornerShape] to be applied to all background layers.
 * @return A [Modifier] with the specified backgrounds applied.
 *
 * ### Example:
 * ```
 * Modifier
 *     .size(100.dp)
 *     .background(
 *         background = listOf(
 *           ODSColorModel(hexColor = "#FF0000"),
 *           ODSColorModel(hexColor = "#00FF00"),
 *         ),
 *         shape = RoundedCornerShape(8.dp)
 *     )
 *```
 */
fun Modifier.background(
    background: List<ODSColorModel>?,
    shape: RoundedCornerShape
) = this.then(
    background?.reversed()?.fold(this) { modifier, backgroundModel ->
        if (backgroundModel.hexColor != null) {
            modifier.background( // No 'then' here
                color = backgroundModel.hexColor!!.getColor(),
                shape = shape
            )
        } else if (backgroundModel.brush != null) {
            modifier.background( // No 'then' here
                brush = backgroundModel.brush!!,
                shape = shape
            )
        } else {
            modifier.background(Color.Transparent, shape = shape) // No 'then' here
        }
    } ?: this
)

inline fun <T> Modifier.ifNotNull(
    param: T?,
    block: Modifier.(param: T) -> Modifier
): Modifier {
    return if (param == null) {
        this
    } else {
        this.then(block(param))
    }
}
