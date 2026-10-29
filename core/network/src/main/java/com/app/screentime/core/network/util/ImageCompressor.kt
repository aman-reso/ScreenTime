package com.app.screentime.core.network.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import com.app.screentime.core.network.dto.UploadImagePart
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt
import androidx.core.graphics.scale

object ImageCompressor {

    private const val TAG = "ImageCompressor"
    private const val MAX_DIMENSION = 1920
    private const val INITIAL_QUALITY = 82
    private const val MAX_OUTPUT_BYTES = 1 * 1024 * 1024 // 1 MB
    private const val SKIP_COMPRESSION_BYTES = 512 * 1024 // 512 KB

    fun compressUriToPart(
        context: Context,
        uri: Uri,
        index: Int
    ): UploadImagePart? {

        // 1. Read URI stream once to prevent Android permission revocation
        val rawBytes = try {
            context.contentResolver
                .openInputStream(uri)
                ?.use { it.readBytes() }
        } catch (e: Exception) {
            Log.e(TAG, "Unable to read URI: $uri", e)
            null
        }

        if (rawBytes == null || rawBytes.isEmpty()) {
            Log.e(TAG, "Empty image: $uri")
            return null
        }

        // 2. MIME type & extension detection
        val detectedMime = try { context.contentResolver.getType(uri)?.lowercase() } catch (_: Exception) { null }
        val mimeType = when {
            detectedMime?.contains("webp") == true -> "image/webp"
            detectedMime?.contains("png") == true -> "image/png"
            detectedMime?.contains("gif") == true -> "image/gif"
            detectedMime?.contains("heic") == true || detectedMime?.contains("heif") == true -> "image/heic"
            else -> "image/jpeg"
        }
        val defaultFilename = "photo_${System.currentTimeMillis()}_$index.${if (mimeType == "image/png") "png" else "jpg"}"

        // Short-circuit: If already small or animated GIF, keep original bytes
        if (rawBytes.size <= SKIP_COMPRESSION_BYTES || mimeType == "image/gif") {
            return UploadImagePart(filename = defaultFilename, bytes = rawBytes, mimeType = mimeType)
        }

        // 3. Read bounds without allocating bitmap
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(rawBytes, 0, rawBytes.size, bounds)

        val originalWidth = bounds.outWidth
        val originalHeight = bounds.outHeight

        if (originalWidth <= 0 || originalHeight <= 0) {
            return UploadImagePart(filename = defaultFilename, bytes = rawBytes, mimeType = mimeType)
        }

        // 4. Calculate sample size
        val sampleSize = calculateSampleSize(
            width = originalWidth,
            height = originalHeight,
            maxDimension = MAX_DIMENSION
        )

        // 5. Decode sampled bitmap
        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
            inMutable = false
        }

        val decodedBitmap = try {
            BitmapFactory.decodeByteArray(rawBytes, 0, rawBytes.size, decodeOptions)
        } catch (e: Exception) {
            Log.e(TAG, "Bitmap decode failed, using raw bytes", e)
            return UploadImagePart(filename = defaultFilename, bytes = rawBytes, mimeType = mimeType)
        } ?: return UploadImagePart(filename = defaultFilename, bytes = rawBytes, mimeType = mimeType)

        // 6. Fix EXIF orientation
        val orientedBitmap = try {
            applyExifOrientation(bitmap = decodedBitmap, rawBytes = rawBytes)
        } catch (e: Exception) {
            Log.w(TAG, "EXIF orientation failed", e)
            decodedBitmap
        }

        // 7. Scale down if dimensions still exceed 1920
        val resizedBitmap = resizeIfNeeded(bitmap = orientedBitmap, maxDimension = MAX_DIMENSION)

        // 8. Adaptive JPEG Compression (Step down quality until <= 1 MB)
        var compressedBytes: ByteArray? = null
        try {
            compressedBytes = compressToJpeg(
                bitmap = resizedBitmap,
                initialQuality = INITIAL_QUALITY,
                maxBytes = MAX_OUTPUT_BYTES
            )
        } catch (e: Exception) {
            Log.e(TAG, "JPEG compression failed", e)
        } finally {
            // Precise garbage collection cleanup
            if (!resizedBitmap.isRecycled) resizedBitmap.recycle()
            if (orientedBitmap !== resizedBitmap && !orientedBitmap.isRecycled) orientedBitmap.recycle()
            if (decodedBitmap !== orientedBitmap && decodedBitmap !== resizedBitmap && !decodedBitmap.isRecycled) decodedBitmap.recycle()
        }

        // If compression succeeded, return optimized JPEG. Otherwise, fall back to rawBytes.
        return if (compressedBytes != null && compressedBytes.isNotEmpty()) {
            UploadImagePart(
                filename = "photo_${System.currentTimeMillis()}_$index.jpg",
                bytes = compressedBytes,
                mimeType = "image/jpeg"
            )
        } else {
            UploadImagePart(filename = defaultFilename, bytes = rawBytes, mimeType = mimeType)
        }
    }

    private fun calculateSampleSize(width: Int, height: Int, maxDimension: Int): Int {
        val largest = maxOf(width, height)
        if (largest <= maxDimension) return 1
        var sampleSize = 1
        while (largest / (sampleSize * 2) >= maxDimension) {
            sampleSize *= 2
        }
        return sampleSize
    }

    private fun applyExifOrientation(bitmap: Bitmap, rawBytes: ByteArray): Bitmap {
        val exif = ExifInterface(ByteArrayInputStream(rawBytes))
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if (orientation == ExifInterface.ORIENTATION_NORMAL) return bitmap

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(270f)
                matrix.postScale(-1f, 1f)
            }
            else -> return bitmap
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun resizeIfNeeded(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val largest = maxOf(bitmap.width, bitmap.height)
        if (largest <= maxDimension) return bitmap

        val ratio = maxDimension.toFloat() / largest.toFloat()
        val targetWidth = (bitmap.width * ratio).roundToInt().coerceAtLeast(1)
        val targetHeight = (bitmap.height * ratio).roundToInt().coerceAtLeast(1)

        val resized = bitmap.scale(targetWidth, targetHeight)
        if (resized !== bitmap) bitmap.recycle()
        return resized
    }

    private fun compressToJpeg(bitmap: Bitmap, initialQuality: Int, maxBytes: Int): ByteArray? {
        var quality = initialQuality
        while (quality >= 50) {
            val outputStream = ByteArrayOutputStream()
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)) return null
            val bytes = outputStream.toByteArray()
            if (bytes.size <= maxBytes) return bytes
            quality -= 5
        }
        val finalStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, finalStream)
        return finalStream.toByteArray()
    }
}
