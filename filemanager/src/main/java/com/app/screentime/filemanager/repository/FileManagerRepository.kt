package com.app.screentime.filemanager.repository

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import com.app.screentime.filemanager.model.FileItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for file management operations
 */
@Singleton
class FileManagerRepository @Inject constructor(
    private val context: Context
) {

    /**
     * Get root directories - shows all files and folders in external storage root
     */
    suspend fun getRootDirectories(): List<FileItem> = withContext(Dispatchers.IO) {
        val externalStorage = Environment.getExternalStorageDirectory()
        
        if (!externalStorage.exists() || !externalStorage.canRead()) {
            return@withContext emptyList()
        }

        // Get all files and directories in the external storage root
        val files = externalStorage.listFiles() ?: return@withContext emptyList()

        files.mapNotNull { file ->
            try {
                FileItem.fromFile(file)
            } catch (e: Exception) {
                null // Skip files that can't be accessed
            }
        }.sortedWith(compareBy(
            { !it.isDirectory }, // Directories first
            { it.name.lowercase() } // Then alphabetically
        ))
    }

    /**
     * Get files and directories in a given directory
     */
    suspend fun getFilesInDirectory(directory: File): List<FileItem> = withContext(Dispatchers.IO) {
        if (!directory.exists() || !directory.isDirectory || !directory.canRead()) {
            return@withContext emptyList()
        }

        val files = directory.listFiles() ?: return@withContext emptyList()

        files.mapNotNull { file ->
            try {
                // Ensure file is accessible before creating FileItem
                if (!file.exists()) {
                    return@mapNotNull null
                }
                FileItem.fromFile(file)
            } catch (e: SecurityException) {
                // Permission denied - skip this file
                null
            } catch (e: Exception) {
                // Other errors - skip this file
                null
            }
        }.sortedWith(compareBy(
            { !it.isDirectory }, // Directories first
            { it.name.lowercase() } // Then alphabetically
        ))
    }

    /**
     * Get photos using MediaStore
     */
    suspend fun getPhotos(): List<FileItem> = withContext(Dispatchers.IO) {
        queryMediaFiles(
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            android.provider.MediaStore.Images.Media.DATA,
            android.provider.MediaStore.Images.Media.DATE_MODIFIED
        )
    }

    /**
     * Get videos using MediaStore
     */
    suspend fun getVideos(): List<FileItem> = withContext(Dispatchers.IO) {
        queryMediaFiles(
            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            android.provider.MediaStore.Video.Media.DATA,
            android.provider.MediaStore.Video.Media.DATE_MODIFIED
        )
    }

    /**
     * Get audio files using MediaStore
     */
    suspend fun getAudioFiles(): List<FileItem> = withContext(Dispatchers.IO) {
        queryMediaFiles(
            android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            android.provider.MediaStore.Audio.Media.DATA,
            android.provider.MediaStore.Audio.Media.DATE_MODIFIED
        )
    }

    /**
     * Helper to query media files from MediaStore
     */
    private fun queryMediaFiles(uri: android.net.Uri, dataColumn: String, dateColumn: String): List<FileItem> {
        val fileItems = mutableListOf<FileItem>()
        val projection = arrayOf(dataColumn)
        
        try {
            context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                "$dateColumn DESC"
            )?.use { cursor ->
                val columnIndex = cursor.getColumnIndexOrThrow(dataColumn)
                while (cursor.moveToNext()) {
                    val path = cursor.getString(columnIndex)
                    if (path != null) {
                        val file = File(path)
                        if (file.exists() && file.canRead()) {
                            try {
                                fileItems.add(FileItem.fromFile(file))
                            } catch (e: Exception) {
                                // Skip invalid files
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return fileItems
    }

    /**
     * Get all APK files from Downloads and other common directories
     */
    suspend fun getApps(): List<FileItem> = withContext(Dispatchers.IO) {
        val apps = mutableListOf<FileItem>()
        val externalStorage = Environment.getExternalStorageDirectory()
        
        // Common directories where APKs might be stored
        val searchDirs = listOf(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            externalStorage
        )
        
        searchDirs.forEach { dir ->
            if (dir.exists() && dir.canRead()) {
                apps.addAll(getApkFilesRecursive(dir))
            }
        }
        
        apps.sortedByDescending { it.lastModified }
    }

    /**
     * Get all APK files recursively from a directory
     */
    private suspend fun getApkFilesRecursive(directory: File): List<FileItem> = withContext(Dispatchers.IO) {
        val apkFiles = mutableListOf<FileItem>()
        
        fun traverseDir(dir: File) {
            if (!dir.exists() || !dir.isDirectory || !dir.canRead()) return
            
            dir.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    traverseDir(file)
                } else if (file.extension.lowercase() == "apk") {
                    try {
                        apkFiles.add(FileItem.fromFile(file))
                    } catch (e: Exception) {
                        // Skip files that can't be accessed
                    }
                }
            }
        }
        
        traverseDir(directory)
        apkFiles
    }

    /**
     * Get all document files (PDF, DOC, DOCX, XLS, XLSX, PPT, PPTX, TXT, etc.)
     * Filters out duplicates based on absolute file path
     */
    suspend fun getDocuments(): List<FileItem> = withContext(Dispatchers.IO) {
        val documents = mutableListOf<FileItem>()
        val seenPaths = mutableSetOf<String>()
        val externalStorage = Environment.getExternalStorageDirectory()
        
        val searchDirs = listOf(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            externalStorage
        )
        
        searchDirs.forEach { dir ->
            if (dir.exists() && dir.canRead()) {
                getDocumentFilesRecursive(dir).forEach { fileItem ->
                    try {
                        val absolutePath = fileItem.file.absolutePath
                        // Only add if we haven't seen this file path before and file still exists
                        if (absolutePath.isNotEmpty() && 
                            fileItem.file.exists() && 
                            seenPaths.add(absolutePath)) {
                            documents.add(fileItem)
                        }
                    } catch (e: Exception) {
                        // Skip invalid file items
                    }
                }
            }
        }
        
        // Remove any duplicates that might have slipped through and sort
        documents.distinctBy { it.file.absolutePath }
            .sortedByDescending { it.lastModified }
    }

    /**
     * Get all document files recursively from a directory
     */
    private suspend fun getDocumentFilesRecursive(directory: File): List<FileItem> = withContext(Dispatchers.IO) {
        val docFiles = mutableListOf<FileItem>()
        
        fun traverseDir(dir: File) {
            if (!dir.exists() || !dir.isDirectory || !dir.canRead()) return
            
            dir.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    traverseDir(file)
                } else if (isDocumentFile(file)) {
                    try {
                        if (file.exists() && file.canRead()) {
                            docFiles.add(FileItem.fromFile(file))
                        }
                    } catch (e: SecurityException) {
                        // Permission denied - skip this file
                    } catch (e: Exception) {
                        // Skip files that can't be accessed
                    }
                }
            }
        }
        
        traverseDir(directory)
        docFiles
    }

    /**
     * Check if file is a document
     */
    private fun isDocumentFile(file: File): Boolean {
        val extension = file.extension.lowercase()
        return extension in listOf(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", 
            "txt", "rtf", "odt", "ods", "odp", "csv"
        )
    }

    /**
     * Get all game files (APK files that might be games, or game-specific file types)
     * For now, we'll use APK files as games since we can't easily distinguish games from other apps
     */
    suspend fun getGames(): List<FileItem> = withContext(Dispatchers.IO) {
        // For now, return APK files as games
        // In a real implementation, you might want to check package names or use Play Games API
        getApps()
    }

    /**
     * Delete a file or directory
     */
    suspend fun deleteFile(file: File): Boolean = withContext(Dispatchers.IO) {
        try {
            if (file.isDirectory) {
                file.deleteRecursively()
            } else {
                file.delete()
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get storage information
     */
    suspend fun getStorageInfo(): StorageInfo =
        withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
                val statsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
                try {
                    val path = Environment.getDataDirectory()
                    val uuid: UUID = storageManager.getUuidForPath(path)
                    val totalBytes = statsManager.getTotalBytes(uuid)
                    val freeBytes = statsManager.getFreeBytes(uuid)
                    StorageInfo(
                        totalBytes = totalBytes,
                        usedBytes = totalBytes - freeBytes,
                        availableBytes = freeBytes
                    )
                } catch (e: IOException) {
                    // Fallback to StatFs if there's an IOException
                    val path = Environment.getDataDirectory()
                    val stat = StatFs(path.path)
                    val totalBytes = stat.totalBytes
                    val freeBytes = stat.availableBytes
                    StorageInfo(
                        totalBytes = totalBytes,
                        usedBytes = totalBytes - freeBytes,
                        availableBytes = freeBytes
                    )
                }
            } else {
                // API < 26 fallback
                val path = Environment.getDataDirectory()
                val stat = StatFs(path.path)
                val totalBytes = stat.totalBytes
                val freeBytes = stat.availableBytes
                StorageInfo(
                    totalBytes = totalBytes,
                    usedBytes = totalBytes - freeBytes,
                    availableBytes = freeBytes
                )
            }
        }



    /**
     * Data class for storage information
     */
    data class StorageInfo(
        val totalBytes: Long,
        val usedBytes: Long,
        val availableBytes: Long
    )

    /**
     * Data class representing a group of duplicate files
     */
    data class DuplicateFileGroup(
        val files: List<FileItem>,
        val totalSize: Long
    )

    /**
     * Find duplicate files by name and size (faster) or by content hash (more accurate)
     * @param useContentHash If true, uses MD5 hash for exact duplicate detection. If false, uses name and size.
     */
    suspend fun findDuplicateFiles(useContentHash: Boolean = false): List<DuplicateFileGroup> = withContext(Dispatchers.IO) {
        val externalStorage = Environment.getExternalStorageDirectory()
        val allFiles = mutableListOf<FileItem>()
        
        // Collect all files recursively
        fun collectFiles(dir: File) {
            if (!dir.exists() || !dir.isDirectory || !dir.canRead()) return
            
            dir.listFiles()?.forEach { file ->
                if (file.isFile && file.canRead()) {
                    try {
                        allFiles.add(FileItem.fromFile(file))
                    } catch (e: Exception) {
                        // Skip files that can't be accessed
                    }
                } else if (file.isDirectory) {
                    collectFiles(file)
                }
            }
        }
        
        collectFiles(externalStorage)
        
        if (useContentHash) {
            // Group by content hash (MD5)
            findDuplicatesByHash(allFiles)
        } else {
            // Group by name and size (faster)
            findDuplicatesByNameAndSize(allFiles)
        }
    }

    /**
     * Find duplicates by file name and size
     */
    private fun findDuplicatesByNameAndSize(files: List<FileItem>): List<DuplicateFileGroup> {
        val fileMap = mutableMapOf<String, MutableList<FileItem>>()
        
        files.forEach { file ->
            if (file.size > 0) { // Only check files with size > 0
                val key = "${file.name}_${file.size}"
                fileMap.getOrPut(key) { mutableListOf() }.add(file)
            }
        }
        
        return fileMap.values
            .filter { it.size > 1 } // Only groups with duplicates
            .map { group ->
                DuplicateFileGroup(
                    files = group,
                    totalSize = group.first().size * (group.size - 1) // Size that can be freed
                )
            }
            .sortedByDescending { it.totalSize }
    }

    /**
     * Find duplicates by content hash (MD5)
     */
    private fun findDuplicatesByHash(files: List<FileItem>): List<DuplicateFileGroup> {
        val hashMap = mutableMapOf<String, MutableList<FileItem>>()
        
        files.forEach { file ->
            if (file.size > 0 && file.size < 100 * 1024 * 1024) { // Skip files larger than 100MB for performance
                try {
                    val hash = calculateMD5(file.file)
                    hashMap.getOrPut(hash) { mutableListOf() }.add(file)
                } catch (e: Exception) {
                    // Skip files that can't be hashed
                }
            }
        }
        
        return hashMap.values
            .filter { it.size > 1 } // Only groups with duplicates
            .map { group ->
                DuplicateFileGroup(
                    files = group,
                    totalSize = group.first().size * (group.size - 1) // Size that can be freed
                )
            }
            .sortedByDescending { it.totalSize }
    }

    /**
     * Calculate MD5 hash of a file
     */
    private fun calculateMD5(file: File): String {
        val md = MessageDigest.getInstance("MD5")
        FileInputStream(file).use { fis ->
            val buffer = ByteArray(8192)
            var read: Int
            while (fis.read(buffer).also { read = it } != -1) {
                md.update(buffer, 0, read)
            }
        }
        return md.digest().joinToString("") { "%02x".format(it) }
    }
}

