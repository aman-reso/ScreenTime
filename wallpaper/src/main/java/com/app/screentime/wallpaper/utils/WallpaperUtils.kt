package com.app.screentime.wallpaper.utils

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.app.screentime.wallpaper.api.model.ImageItem
import com.app.screentime.wallpaper.model.Wallpaper
import com.app.screentime.wallpaper.model.WallpaperType
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlinx.coroutines.Dispatchers

class WallpaperUtils(private val context: Context) {
    private val wallpaperManager: WallpaperManager by lazy {
        WallpaperManager.getInstance(context)
    }

    /**
     * Set wallpaper from a file path
     */
    suspend fun setWallpaper(
        wallpaper: Wallpaper,
        type: WallpaperType
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val bitmap = when {
                wallpaper.localPath != null -> {
                    loadBitmapFromFile(wallpaper.localPath)
                }
                wallpaper.imageUrl != null -> {
                    downloadBitmap(wallpaper.imageUrl)
                }
                else -> null
            }

            if (bitmap == null) {
                return@withContext Result.failure(Exception("Failed to load wallpaper image"))
            }

            when (type) {
                WallpaperType.HOME -> {
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                }
                WallpaperType.LOCK -> {
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                }
                WallpaperType.BOTH -> {
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Set wallpaper from URI (for image picker)
     */
    suspend fun setWallpaperFromUri(
        uri: Uri,
        type: WallpaperType
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap == null) {
                return@withContext Result.failure(Exception("Failed to decode image from URI"))
            }

            when (type) {
                WallpaperType.HOME -> {
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                }
                WallpaperType.LOCK -> {
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                }
                WallpaperType.BOTH -> {
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Save bitmap to local storage
     */
    suspend fun saveWallpaperToLocal(
        bitmap: Bitmap,
        wallpaperId: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val wallpaperDir = File(context.filesDir, "wallpapers")
            if (!wallpaperDir.exists()) {
                wallpaperDir.mkdirs()
            }

            val file = File(wallpaperDir, "$wallpaperId.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()

            Result.success(file.absolutePath)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Load bitmap from file path
     */
    private fun loadBitmapFromFile(path: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Download bitmap using Coil
     */
    private suspend fun downloadBitmap(url: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .build()

            val result = loader.execute(request)
            if (result is SuccessResult) {
                (result.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Delete local wallpaper file
     */
    suspend fun deleteLocalWallpaper(path: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Download wallpaper to Downloads folder.
     * @param onProgress Progress callback 0f..1f, invoked on IO dispatcher.
     */
    suspend fun downloadWallpaperToDownloads(
        wallpaper: Wallpaper,
        imageItem: ImageItem? = null,
        onProgress: ((Float, Long, Long) -> Unit)? = null // progress, downloadedBytes, totalBytes
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Prefer original URL from ImageItem, then adapted, then wallpaper imageUrl
            val imageUrl = imageItem?.variations?.original?.url
                ?: imageItem?.variations?.adapted?.url
                ?: wallpaper.imageUrl

            val bitmap = when {
                wallpaper.localPath != null -> {
                    onProgress?.invoke(0.2f, 0, 1)
                    val b = loadBitmapFromFile(wallpaper.localPath)
                    onProgress?.invoke(0.5f, 1, 1)
                    b
                }
                imageUrl != null -> {
                    downloadBitmapWithProgress(imageUrl, onProgress)
                }
                else -> null
            }

            if (bitmap == null) {
                return@withContext Result.failure(Exception("Failed to load wallpaper image"))
            }

            onProgress?.invoke(0.8f, 0, 0)

            // Save to Pictures/Wallpapers folder
            val picturesDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Wallpapers")
            } else {
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Wallpapers")
            }

            if (!picturesDir.exists()) {
                picturesDir.mkdirs()
            }

            val fileName = "wallpaper_${System.currentTimeMillis()}.jpg"
            val file = File(picturesDir, fileName)
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
                outputStream.flush()
            }

            // Notify media scanner
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val mediaScanIntent = android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val contentUri = android.net.Uri.fromFile(file)
                mediaScanIntent.data = contentUri
                context.sendBroadcast(mediaScanIntent)
            }

            onProgress?.invoke(1f, file.length(), file.length())
            Result.success(file.absolutePath)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Download image from URL with progress reporting.
     */
    private suspend fun downloadBitmapWithProgress(
        url: String,
        onProgress: ((Float, Long, Long) -> Unit)?
    ): Bitmap? = withContext(Dispatchers.IO) {
        var connection: java.net.HttpURLConnection? = null
        try {
            connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
            connection.connect()
            val contentLength = connection.contentLengthLong
            connection.inputStream.use { input ->
                val buffer = ByteArray(8192)
                val out = java.io.ByteArrayOutputStream()
                var bytesRead = 0L
                var len: Int
                while (input.read(buffer).also { len = it } != -1) {
                    out.write(buffer, 0, len)
                    bytesRead += len
                    if (contentLength > 0) {
                        val progress = (bytesRead.toFloat() / contentLength * 0.7f).coerceIn(0f, 0.7f)
                        onProgress?.invoke(progress, bytesRead, contentLength)
                    }
                }
                onProgress?.invoke(0.7f, bytesRead, contentLength)
                val bytes = out.toByteArray()
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }
        } finally {
            connection?.disconnect()
        }
    }
}

