package com.telekom.odsystem.charts.core.common

import android.graphics.RectF
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils

private const val LINE_COUNT_FIELD = "mLineCount"

@Suppress("DEPRECATION")
internal fun staticLayout(
  source: CharSequence,
  paint: TextPaint,
  width: Int,
  maxLines: Int = Int.MAX_VALUE,
  startIndex: Int = 0,
  endIndex: Int = source.length,
  spacingMultiplication: Float = 1f,
  spacingAddition: Float = 0f,
  includePadding: Boolean = true,
  ellipsize: TextUtils.TruncateAt? = null,
  ellipsizedWidth: Int = width,
  align: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
): StaticLayout =
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    StaticLayout.Builder.obtain(source, startIndex, endIndex, paint, width)
      .setAlignment(align)
      .setLineSpacing(spacingAddition, spacingMultiplication)
      .setIncludePad(includePadding)
      .setEllipsize(ellipsize)
      .setEllipsizedWidth(ellipsizedWidth)
      .setMaxLines(maxLines)
      .build()
  } else {
    StaticLayout(
        source,
        startIndex,
        endIndex,
        paint,
        width,
        align,
        spacingMultiplication,
        spacingAddition,
        includePadding,
        ellipsize,
        ellipsizedWidth,
      )
      .setLineCount(maxLines)
  }

internal fun StaticLayout.setLineCount(count: Int) = apply {
  setFieldValue(LINE_COUNT_FIELD, count)
}

internal val Layout.bounds
  get() = RectF(0f, 0f, widestLineWidth, heightWithSpacingAddition)

internal val Layout.widestLineWidth: Float
  get() = (0..<lineCount).maxOf { lineIndex -> getLineWidth(lineIndex) }

internal val Layout.heightWithSpacingAddition: Float
  get() = height.toFloat() + spacingAdd
