package com.app.screentime.filemanager.model

import java.io.File

/**
 * Represents a file or directory item in the file manager
 */
data class FileItem(
    val file: File,
    val name: String,
    val isDirectory: Boolean,
    val size: Long = 0L,
    val lastModified: Long = 0L,
    val mimeType: String? = null,
    val thumbnailPath: String? = null
) {
    companion object {
        /**
         * Create FileItem from File
         */
        fun fromFile(file: File): FileItem {
            return FileItem(
                file = file,
                name = file.name,
                isDirectory = file.isDirectory,
                size = if (file.isFile) file.length() else 0L,
                lastModified = file.lastModified(),
                mimeType = getMimeType(file),
                thumbnailPath = null
            )
        }

        /**
         * Get MIME type from file extension
         */
        private fun getMimeType(file: File): String? {
            // Get extension from filename, handling cases where extension might be empty
            val fileName = file.name.lowercase()
            val lastDotIndex = fileName.lastIndexOf('.')
            if (lastDotIndex == -1 || lastDotIndex == fileName.length - 1) {
                // No extension or extension is empty
                return null
            }
            val extension = fileName.substring(lastDotIndex + 1)
            
            return when (extension) {
                "jpg", "jpeg", "png", "gif", "bmp", "webp", "heic", "heif" -> "image/*"
                "mp4", "avi", "mkv", "mov", "wmv", "flv", "3gp", "webm", "m4v" -> "video/*"
                "mp3", "wav", "flac", "aac", "ogg", "m4a", "wma" -> "audio/*"
                "pdf" -> "application/pdf"
                "doc", "docx" -> "application/msword"
                "xls", "xlsx" -> "application/vnd.ms-excel"
                "ppt", "pptx" -> "application/vnd.ms-powerpoint"
                "txt" -> "text/plain"
                "zip", "rar", "7z", "tar", "gz" -> "application/zip"
                "apk" -> "application/vnd.android.package-archive"
                "html", "htm" -> "text/html"
                "css" -> "text/css"
                "js" -> "application/javascript"
                "json" -> "application/json"
                "xml" -> "application/xml"
                "csv" -> "text/csv"
                "rtf" -> "application/rtf"
                "exe" -> "application/x-msdownload"
                "dmg" -> "application/x-apple-diskimage"
                "iso" -> "application/x-iso9660-image"
                else -> null
            }
        }
    }
}

