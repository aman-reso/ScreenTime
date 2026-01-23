package com.app.screentime.filemanager.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Helper class for extracting and caching video thumbnails
 */
object VideoThumbnailHelper {
    private const val TAG = "VideoThumbnailHelper"
    private val thumbnailCache = mutableMapOf<String, String>() // video path -> thumbnail path

    /**
     * Get video thumbnail path (cached or newly generated)
     */
    suspend fun getVideoThumbnailPath(
        context: Context,
        videoFile: File
    ): String? = withContext(Dispatchers.IO) {
        try {
            val videoPath = videoFile.absolutePath
            
            // Check cache first
            thumbnailCache[videoPath]?.let { cachedPath ->
                val cachedFile = File(cachedPath)
                if (cachedFile.exists()) {
                    return@withContext cachedPath
                } else {
                    // Cache entry is stale, remove it
                    thumbnailCache.remove(videoPath)
                }
            }

            // Generate thumbnail
            val thumbnail = extractVideoThumbnail(videoFile) ?: return@withContext null

            // Save thumbnail to cache directory
            val cacheDir = File(context.cacheDir, "video_thumbnails")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val thumbnailFile = File(cacheDir, "${videoFile.nameWithoutExtension}_thumb.jpg")
            val outputStream = FileOutputStream(thumbnailFile)
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.close()

            // Cache the path
            thumbnailCache[videoPath] = thumbnailFile.absolutePath

            thumbnail.recycle()
            thumbnailFile.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "Error getting video thumbnail: ${e.message}", e)
            null
        }
    }

    /**
     * Extract thumbnail from video file
     */
    private fun extractVideoThumbnail(videoFile: File): Bitmap? {
        var retriever: MediaMetadataRetriever? = null
        return try {
            if (!videoFile.exists() || !videoFile.canRead()) {
                return null
            }

            retriever = MediaMetadataRetriever()
            retriever.setDataSource(videoFile.absolutePath)
            
            // Get frame at 1 second (or first frame if video is shorter)
            val bitmap = retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            
            if (bitmap == null) {
                // Try first frame if getting frame at 1 second failed
                retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            } else {
                bitmap
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting video thumbnail: ${e.message}", e)
            null
        } finally {
            try {
                retriever?.release()
            } catch (e: Exception) {
                Log.e(TAG, "Error releasing MediaMetadataRetriever: ${e.message}", e)
            }
        }
    }

    /**
     * Clear thumbnail cache
     */
    fun clearCache(context: Context) {
        try {
            val cacheDir = File(context.cacheDir, "video_thumbnails")
            if (cacheDir.exists()) {
                cacheDir.listFiles()?.forEach { it.delete() }
            }
            thumbnailCache.clear()
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing thumbnail cache: ${e.message}", e)
        }
    }
}

