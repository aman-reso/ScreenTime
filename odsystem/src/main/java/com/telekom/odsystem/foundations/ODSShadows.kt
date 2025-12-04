package com.telekom.odsystem.foundations

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by dmarinopoulos on 6/11/23
 */

private const val RIGHT_QUADRANT_START = 90f
private const val RIGHT_QUADRANT_END = 180f
private const val LEFT_QUADRANT_START = 270f
private const val LEFT_QUADRANT_END = 0f

fun Modifier.applyODSEffect(effect: ODSEffect?, corners: ODSCorners?, borderWidth: Dp?): Modifier {
    return effect?.let { odsEffect ->
        val internalBorderWidth = borderWidth ?: 0.dp
        val internalCorners = corners ?: ODSCorners(0.dp)
        odsEffect.elevations.fold(this) { modifiedModifier, elevation ->
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) {
                if (elevation.type == ODSElevationType.DROP_SHADOW) {
                    modifiedModifier.then(
                        Modifier.shadow(
                            elevation = elevation.blur.dp,
                            shape = RoundedCornerShape(
                                topStart = internalCorners.topLeft,
                                topEnd = internalCorners.topRight,
                                bottomEnd = internalCorners.bottomRight,
                                bottomStart = internalCorners.bottomLeft
                            ),
                        )
                    )
                } else {
                    this
                }
            } else {
                when (elevation.type) {
                    ODSElevationType.INNER_SHADOW -> {
                        modifiedModifier.then(
                            Modifier.innerShadow(
                                color = elevation.color.getColor(),
                                corners = internalCorners,
                                blur = elevation.blur.dp,
                                x = elevation.x.dp,
                                y = elevation.y.dp,
                                spread = elevation.spread.dp,
                                borderWidth = internalBorderWidth
                            )
                        )
                    }

                    ODSElevationType.DROP_SHADOW -> {
                        modifiedModifier.then(
                            Modifier.dropShadow(
                                color = elevation.color.getColor(),
                                corners = internalCorners,
                                blur = elevation.blur.dp,
                                x = elevation.x.dp,
                                y = elevation.y.dp,
                                spread = elevation.spread.dp // Adjust as needed
                            )
                        )
                    }
                }
            }
        }
    } ?: this
}

fun Modifier.dropShadow(
    color: Color = Color.Black,
    corners: ODSCorners,
    blur: Dp = 0.dp,
    x: Dp = 0.dp,
    y: Dp = 0.dp,
    spread: Dp = 0.dp,
    borderWidth: Dp = 0.dp
) = drawWithContent {

    val rect = Rect(Offset.Zero, size)
    val paint = Paint()
    drawIntoCanvas {
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.isAntiAlias = true
        val spreadPixel = spread.toPx()
        val leftPixel = (0f - spreadPixel) + x.toPx()
        val topPixel = (0f - spreadPixel) + y.toPx()
        val rightPixel = (this.size.width + x.toPx() + spreadPixel)
        val bottomPixel = (this.size.height + y.toPx() + spreadPixel)

        val innerRect = RectF(
            rect.left + borderWidth.toPx(),
            rect.top + borderWidth.toPx(),
            rect.right - borderWidth.toPx(),
            rect.bottom - borderWidth.toPx()
        )
        val innerRoundedCorners = calculateRoundedCorners(corners, borderWidth, 0.dp)

        val pxOuterRoundedCorners = PxCorners(
            topLeft = innerRoundedCorners.topLeft.toPx(),
            topRight = innerRoundedCorners.topRight.toPx(),
            bottomLeft = innerRoundedCorners.bottomLeft.toPx(),
            bottomRight = innerRoundedCorners.bottomRight.toPx()
        )
        val innerPath = createRoundedPath(innerRect, pxOuterRoundedCorners)
        innerPath.close()

        if (blur > 0.dp) {
            frameworkPaint.maskFilter =
                (BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL))
        }

        frameworkPaint.color = color.toArgb()

        val roundedCorners = calculateSpreadCorners(corners, spread)

        val pxRoundedCorners = PxCorners(
            topLeft = roundedCorners.topLeft.toPx(),
            topRight = roundedCorners.topRight.toPx(),
            bottomRight = roundedCorners.bottomRight.toPx(),
            bottomLeft = roundedCorners.bottomLeft.toPx()
        )

        val outerPath = RectF(
            leftPixel,
            topPixel,
            rightPixel,
            bottomPixel
        )

        val path = createRoundedPath(outerPath, pxRoundedCorners)
        drawContent()
        it.save()
        it.clipPath(innerPath, ClipOp.Difference)
        path.close()
        it.drawPath(path, paint)
        it.restore()
    }
}

fun Modifier.innerShadow(
    color: Color = Color.Black,
    corners: ODSCorners,
    spread: Dp = 0.dp,
    blur: Dp = 0.dp,
    x: Dp = 0.dp,
    y: Dp = 0.dp,
    borderWidth: Dp = 0.dp
) = drawWithContent {

    drawContent()

    val rect = Rect(Offset.Zero, size)
    val paint = Paint()
    drawIntoCanvas {
        it.saveLayer(rect, paint)

        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = color.toArgb()
        frameworkPaint.isAntiAlias = true

        // Draw the outer rectangle considering the border
        val outerRect = RectF(
            rect.left + borderWidth.toPx(),
            rect.top + borderWidth.toPx(),
            rect.right - borderWidth.toPx(),
            rect.bottom - borderWidth.toPx()
        )
        val outerRoundedCorners = calculateRoundedCorners(corners, borderWidth, 0.dp)
        val innerRoundedCorners = calculateRoundedCorners(corners, borderWidth, spread)
        val pxOuterRoundedCorners = PxCorners(
            topLeft = outerRoundedCorners.topLeft.toPx(),
            topRight = outerRoundedCorners.topRight.toPx(),
            bottomLeft = outerRoundedCorners.bottomLeft.toPx(),
            bottomRight = outerRoundedCorners.bottomRight.toPx()
        )
        val pxInnerRoundedCorners = PxCorners(
            topLeft = innerRoundedCorners.topLeft.toPx(),
            topRight = innerRoundedCorners.topRight.toPx(),
            bottomLeft = innerRoundedCorners.bottomLeft.toPx(),
            bottomRight = innerRoundedCorners.bottomRight.toPx()
        )
        val outerPath = createRoundedPath(outerRect, pxOuterRoundedCorners)
        outerPath.close()
        it.drawPath(outerPath, paint)

        frameworkPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        if (blur.toPx() > 0) {
            frameworkPaint.maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
        frameworkPaint.color = Color.White.toArgb()

        // Draw the inner rectangle considering the border
        val left = rect.left + x.toPx()
        val top = rect.top + y.toPx()
        val right = rect.right + x.toPx()
        val bottom = rect.bottom + y.toPx()

        val innerRect = RectF(
            left + spread.toPx() + borderWidth.toPx(),
            top + spread.toPx() + borderWidth.toPx(),
            right - spread.toPx() - borderWidth.toPx(),
            bottom - spread.toPx() - borderWidth.toPx()
        )

        val innerPath = createRoundedPath(innerRect, pxInnerRoundedCorners)
        innerPath.close()
        it.drawPath(innerPath, paint)
        frameworkPaint.xfermode = null
        frameworkPaint.maskFilter = null
    }
}

private fun calculateSpreadCorners(corners: ODSCorners, spread: Dp): ODSCorners {
    return corners.copy(
        topLeft = if (corners.topLeft > 0.dp) {
            corners.topLeft + spread
        } else {
            0.dp
        },
        topRight = if (corners.topRight > 0.dp) {
            corners.topRight + spread
        } else {
            0.dp
        },
        bottomRight = if (corners.bottomRight > 0.dp) {
            corners.bottomRight + spread
        } else {
            0.dp
        },
        bottomLeft = if (corners.bottomLeft > 0.dp) {
            corners.bottomLeft + spread
        } else {
            0.dp
        },
    )
}

private fun calculateRoundedCorners(corners: ODSCorners, borderWidth: Dp, spread: Dp): ODSCorners {
    return corners.copy(
        topLeft = maxOf(0.dp, corners.topLeft - borderWidth - spread),
        topRight = maxOf(0.dp, corners.topRight - borderWidth - spread),
        bottomRight = maxOf(0.dp, corners.bottomRight - borderWidth - spread),
        bottomLeft = maxOf(0.dp, corners.bottomLeft - borderWidth - spread)
    )
}

fun createRoundedPath(
    rect: RectF,
    pxCorners: PxCorners
): Path {
    val maxRadius = minOf(rect.width(), rect.height()) / 2 // Calculate maximum allowed radius
    val topLeftRadius = minOf(pxCorners.topLeft, maxRadius) // Limit top-left radius
    val topRightRadius = minOf(pxCorners.topRight, maxRadius) // Limit top-right radius
    val bottomRightRadius = minOf(pxCorners.bottomRight, maxRadius) // Limit bottom-right radius
    val bottomLeftRadius = minOf(pxCorners.bottomLeft, maxRadius) // Limit bottom-left radius
    val path = Path()

    // Top-left corner
    path.addArc(
        Rect(
            rect.left,
            rect.top,
            rect.left + 2 * topLeftRadius,
            rect.top + 2 * topLeftRadius
        ),
        RIGHT_QUADRANT_END,
        RIGHT_QUADRANT_START
    )

    // Top-right corner
    path.addArc(
        Rect(
            rect.right - 2 * topRightRadius,
            rect.top,
            rect.right,
            rect.top + 2 * topRightRadius
        ),
        LEFT_QUADRANT_START,
        RIGHT_QUADRANT_START
    )

    // Bottom-right corner
    path.addArc(
        Rect(
            rect.right - 2 * bottomRightRadius,
            rect.bottom - 2 * bottomRightRadius,
            rect.right,
            rect.bottom
        ),
        LEFT_QUADRANT_END,
        RIGHT_QUADRANT_START
    )

    // Bottom-left corner
    path.addArc(
        Rect(
            rect.left,
            rect.bottom - 2 * bottomLeftRadius,
            rect.left + 2 * bottomLeftRadius,
            rect.bottom
        ),
        RIGHT_QUADRANT_START,
        RIGHT_QUADRANT_START
    )

    // Connect the corners
    path.moveTo(rect.left, rect.top + topLeftRadius)
    path.lineTo(rect.left + topLeftRadius, rect.top)
    path.lineTo(rect.right - topRightRadius, rect.top)
    path.lineTo(rect.right, rect.top + topRightRadius)
    path.lineTo(rect.right, rect.bottom - bottomRightRadius)
    path.lineTo(rect.right - bottomRightRadius, rect.bottom)
    path.lineTo(rect.left + bottomLeftRadius, rect.bottom)
    path.lineTo(rect.left, rect.bottom - bottomLeftRadius)
    path.lineTo(rect.left, rect.top + topLeftRadius)
    path.close()

    return path
}

data class PxCorners(
    var topLeft: Float = 0f,
    var topRight: Float = 0f,
    var bottomLeft: Float = 0f,
    var bottomRight: Float = 0f
)
