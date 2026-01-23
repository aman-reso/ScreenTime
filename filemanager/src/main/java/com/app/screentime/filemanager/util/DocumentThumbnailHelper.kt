package com.app.screentime.filemanager.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Helper class for extracting and caching document thumbnails
 */
object DocumentThumbnailHelper {
    private const val TAG = "DocumentThumbnailHelper"
    private val thumbnailCache = mutableMapOf<String, String>() // document path -> thumbnail path

    /**
     * Get document thumbnail path (cached or newly generated)
     */
    suspend fun getDocumentThumbnailPath(
        context: Context,
        documentFile: File
    ): String? = withContext(Dispatchers.IO) {
        try {
            val documentPath = documentFile.absolutePath
            
            // Check cache first
            thumbnailCache[documentPath]?.let { cachedPath ->
                val cachedFile = File(cachedPath)
                if (cachedFile.exists()) {
                    return@withContext cachedPath
                } else {
                    // Cache entry is stale, remove it
                    thumbnailCache.remove(documentPath)
                }
            }

            // Generate thumbnail based on file type
            val thumbnail = when {
                isPdfFile(documentFile) -> extractPdfThumbnail(documentFile)
                else -> null // For other document types, return null to show icon
            } ?: return@withContext null

            // Save thumbnail to cache directory
            val cacheDir = File(context.cacheDir, "document_thumbnails")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val thumbnailFile = File(cacheDir, "${documentFile.nameWithoutExtension}_thumb.jpg")
            val outputStream = FileOutputStream(thumbnailFile)
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.close()

            // Cache the path
            thumbnailCache[documentPath] = thumbnailFile.absolutePath

            thumbnail.recycle()
            thumbnailFile.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "Error getting document thumbnail: ${e.message}", e)
            null
        }
    }

    /**
     * Extract thumbnail from PDF file (first page)
     */
    private fun extractPdfThumbnail(pdfFile: File): Bitmap? {
        var fileDescriptor: ParcelFileDescriptor? = null
        var pdfRenderer: PdfRenderer? = null
        return try {
            if (!pdfFile.exists() || !pdfFile.canRead()) {
                return null
            }

            fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(fileDescriptor)
            
            if (pdfRenderer.pageCount == 0) {
                return null
            }

            // Open first page
            val page = pdfRenderer.openPage(0)
            
            // Render at a reasonable size (e.g., 200x200 for thumbnail)
            val thumbnailWidth = 200
            val thumbnailHeight = (page.height * thumbnailWidth / page.width).toInt()
            
            val bitmap = Bitmap.createBitmap(thumbnailWidth, thumbnailHeight, Bitmap.Config.ARGB_8888)
            bitmap.eraseColor(android.graphics.Color.WHITE)
            
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT)
            page.close()
            
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting PDF thumbnail: ${e.message}", e)
            null
        } finally {
            try {
                pdfRenderer?.close()
                fileDescriptor?.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error closing PDF resources: ${e.message}", e)
            }
        }
    }

    /**
     * Check if file is a PDF
     */
    private fun isPdfFile(file: File): Boolean {
        return file.extension.lowercase() == "pdf" ||
                file.name.lowercase().endsWith(".pdf")
    }

    /**
     * Clear thumbnail cache
     */
    fun clearCache(context: Context) {
        try {
            val cacheDir = File(context.cacheDir, "document_thumbnails")
            if (cacheDir.exists()) {
                cacheDir.listFiles()?.forEach { it.delete() }
            }
            thumbnailCache.clear()
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing thumbnail cache: ${e.message}", e)
        }
    }
}

