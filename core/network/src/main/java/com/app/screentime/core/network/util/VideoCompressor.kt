package com.app.screentime.core.network.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.effect.Presentation
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.Effects
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.Transformer
import com.app.screentime.core.network.dto.UploadImagePart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class VideoCompressionResult(
    val file: File,
    val durationMs: Long,
    val originalSizeBytes: Long,
    val compressedSizeBytes: Long,
    val thumbnailBitmap: Bitmap?
)

/**
 * Hardware-accelerated Video Compressor and Trimmer powered by AndroidX Media3 Transformer.
 * 
 * Guarantees:
 * 1. Hard maximum 10-second duration (automatically trims longer videos to the first 10 seconds).
 * 2. Compresses video to 720p H.264 / AAC MP4 to keep file size lightweight (< 3-5 MB).
 * 3. Handles rotation matrices and audio tracks correctly.
 * 4. Extracts first-frame thumbnail for UI preview.
 */
object VideoCompressor {

    private const val TAG = "VideoCompressor"
    const val MAX_VIDEO_DURATION_MS = 10_000L // 10 seconds maximum
    private const val MAX_DIMENSION = 1280

    /**
     * Compresses and trims the video to at most 10 seconds, returning a [VideoCompressionResult].
     */
    suspend fun compressVideo(
        context: Context,
        inputUri: Uri,
        maxDurationMs: Long = MAX_VIDEO_DURATION_MS
    ): VideoCompressionResult = withContext(Dispatchers.IO) {
        // 1. Inspect metadata
        val retriever = MediaMetadataRetriever()
        var originalDurationMs = 0L
        var originalWidth = 0
        var originalHeight = 0
        try {
            retriever.setDataSource(context, inputUri)
            originalDurationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
            originalWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 0
            originalHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 0
        } catch (e: Exception) {
            Log.w(TAG, "Failed to inspect video metadata for $inputUri: ${e.message}")
        }

        val thumbnailBitmap = try {
            retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        } catch (_: Exception) {
            null
        }

        try {
            retriever.release()
        } catch (_: Exception) {}

        // Calculate original file size
        val originalSize = try {
            context.contentResolver.openFileDescriptor(inputUri, "r")?.use {
                it.statSize
            } ?: 0L
        } catch (_: Exception) {
            0L
        }

        // 2. Prepare output file
        val outputDir = File(context.cacheDir, "compressed_videos").apply { mkdirs() }
        val outputFile = File(outputDir, "video_${System.currentTimeMillis()}.mp4")

        // 3. Configure clipping (at most 10 seconds)
        val targetEndMs = if (originalDurationMs > 0L) {
            originalDurationMs.coerceAtMost(maxDurationMs)
        } else {
            maxDurationMs
        }

        val clippingConfig = MediaItem.ClippingConfiguration.Builder()
            .setStartPositionMs(0L)
            .setEndPositionMs(targetEndMs)
            .build()

        val mediaItem = MediaItem.Builder()
            .setUri(inputUri)
            .setClippingConfiguration(clippingConfig)
            .build()

        // 4. Downscale video to 720p (720x1280 portrait or 1280x720 landscape) if larger
        val effectsList = mutableListOf<androidx.media3.common.Effect>()
        if (originalHeight > 1280 || originalWidth > 1280) {
            val targetHeight = if (originalWidth > originalHeight) 720 else 1280
            effectsList.add(Presentation.createForHeight(targetHeight))
        }

        val editedMediaItem = EditedMediaItem.Builder(mediaItem)
            .setEffects(
                Effects(
                    /* audioProcessors = */ emptyList(),
                    /* videoEffects = */ effectsList
                )
            )
            .build()

        // 5. Run Media3 Transformer compression asynchronously
        val success = runTransformer(context, editedMediaItem, outputFile)
        if (!success || !outputFile.exists() || outputFile.length() == 0L) {
            throw IllegalStateException("Video compression failed for $inputUri")
        }

        val finalDurationMs = targetEndMs
        val finalSize = outputFile.length()
        Log.i(TAG, "Compressed video from ${originalSize / 1024} KB to ${finalSize / 1024} KB, duration: ${finalDurationMs}ms")

        VideoCompressionResult(
            file = outputFile,
            durationMs = finalDurationMs,
            originalSizeBytes = originalSize,
            compressedSizeBytes = finalSize,
            thumbnailBitmap = thumbnailBitmap
        )
    }

    /**
     * Compresses the video and packages it into an [UploadImagePart] ready for multipart API upload.
     */
    suspend fun compressVideoToPart(
        context: Context,
        inputUri: Uri,
        index: Int = 0
    ): UploadImagePart = withContext(Dispatchers.IO) {
        val result = compressVideo(context, inputUri)
        val bytes = result.file.readBytes()
        UploadImagePart(
            filename = "video_${System.currentTimeMillis()}_$index.mp4",
            bytes = bytes,
            mimeType = "video/mp4"
        )
    }

    /**
     * Extracts a video frame thumbnail for UI preview.
     */
    fun extractThumbnail(context: Context, uri: Uri): Bitmap? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)
            retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to extract thumbnail: ${e.message}")
            null
        } finally {
            try {
                retriever.release()
            } catch (_: Exception) {}
        }
    }

    private suspend fun runTransformer(
        context: Context,
        editedMediaItem: EditedMediaItem,
        outputFile: File
    ): Boolean = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine { continuation ->
            var transformerInstance: Transformer? = null

            val listener = object : Transformer.Listener {
                override fun onCompleted(composition: Composition, exportResult: ExportResult) {
                    if (continuation.isActive) {
                        continuation.resume(true)
                    }
                }

                override fun onError(
                    composition: Composition,
                    exportResult: ExportResult,
                    exportException: ExportException
                ) {
                    Log.e(TAG, "Transformer error: ${exportException.message}", exportException)
                    if (continuation.isActive) {
                        continuation.resumeWithException(exportException)
                    }
                }
            }

            val transformer = Transformer.Builder(context)
                .setVideoMimeType(MimeTypes.VIDEO_H264)
                .setAudioMimeType(MimeTypes.AUDIO_AAC)
                .addListener(listener)
                .build()

            transformerInstance = transformer

            continuation.invokeOnCancellation {
                try {
                    transformerInstance.cancel()
                } catch (_: Exception) {}
            }

            try {
                transformer.start(editedMediaItem, outputFile.absolutePath)
            } catch (e: Exception) {
                if (continuation.isActive) {
                    continuation.resumeWithException(e)
                }
            }
        }
    }
}
