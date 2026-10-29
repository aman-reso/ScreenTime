package com.app.screentime.core.faceverification.model

import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.Rect

/**
 * Data representing a face detected, cropped and aligned from an input image.
 */
data class DetectedFaceData(
    val boundingBox: Rect,
    val alignedFaceBitmap: Bitmap,
    val leftEyePosition: PointF?,
    val rightEyePosition: PointF?,
    val rotationAngleDegrees: Float,
    val confidence: Float? = null
)
