package com.app.screentime.core.faceverification.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Lightweight Face Detection engine using Google ML Kit.
 * Checks whether one or more human faces are detected in an image.
 */
@Singleton
class FaceDetectorEngine @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    companion object {
        private const val TAG = "FaceDetectorEngine"
        private const val MAX_DECODE_DIMENSION = 1280
    }

    private val detectorOptions: FaceDetectorOptions by lazy {
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .setMinFaceSize(0.12f)
            .build()
    }

    private val detector: FaceDetector by lazy {
        FaceDetection.getClient(detectorOptions)
    }

    init {
        ensureModuleDownloaded()
    }

    /**
     * Proactively verifies that Google Play Services has downloaded the Face Detection model,
     * or triggers background download if needed.
     */
    fun ensureModuleDownloaded() {
        try {
            val moduleInstallClient = com.google.android.gms.common.moduleinstall.ModuleInstall.getClient(context)
            val optionalModuleApi = detector
            moduleInstallClient.areModulesAvailable(optionalModuleApi)
                .addOnSuccessListener { response ->
                    if (!response.areModulesAvailable()) {
                        Log.d(TAG, "Face detection model missing, requesting background download from Google Play Services")
                        moduleInstallClient.deferredInstall(optionalModuleApi)
                    } else {
                        Log.d(TAG, "Face detection model is ready in Google Play Services")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Failed checking ML Kit module availability: ${e.message}")
                }
        } catch (t: Throwable) {
            Log.w(TAG, "ModuleInstallClient not available: ${t.message}")
        }
    }

    /**
     * Detects all faces in the provided [bitmap].
     */
    suspend fun detectFaces(bitmap: Bitmap): List<Face> = withContext(Dispatchers.Default) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        detector.process(inputImage).await()
    }

    /**
     * Returns true if at least one human face is detected in the [bitmap].
     */
    suspend fun hasFace(bitmap: Bitmap): Boolean = withContext(Dispatchers.Default) {
        try {
            val faces = detectFaces(bitmap)
            faces.isNotEmpty()
        } catch (e: Exception) {
            Log.e(TAG, "Error detecting faces in bitmap: ${e.message}", e)
            false
        }
    }

    /**
     * Loads image from [uri] and returns true if at least one human face is detected.
     */
    suspend fun hasFace(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val bitmap = loadBitmapFromUri(uri)
            if (bitmap == null) {
                Log.w(TAG, "Unable to load bitmap from uri: $uri")
                return@withContext false
            }
            try {
                val faces = detectFaces(bitmap)
                Log.d(TAG, "ML Kit detected ${faces.size} face(s) for uri: $uri")
                faces.isNotEmpty()
            } finally {
                if (!bitmap.isRecycled) {
                    bitmap.recycle()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error detecting faces from uri $uri: ${e.message}", e)
            false
        }
    }

    /**
     * Loads a sampled and EXIF-oriented Bitmap safely from Uri.
     */
    fun loadBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val scheme = uri.scheme?.lowercase()
            val bytes = when (scheme) {
                "http", "https" -> {
                    val connection = URL(uri.toString()).openConnection()
                    connection.connectTimeout = 10000
                    connection.readTimeout = 10000
                    connection.getInputStream().use { it.readBytes() }
                }
                "file" -> {
                    val path = uri.path ?: uri.toString().removePrefix("file://")
                    File(path).readBytes()
                }
                "content" -> {
                    context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                }
                else -> {
                    val file = File(uri.toString())
                    if (file.exists()) {
                        file.readBytes()
                    } else {
                        context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    }
                }
            } ?: return null

            // 1. Calculate sample size
            val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, boundsOptions)

            var inSampleSize = 1
            while (boundsOptions.outWidth / inSampleSize > MAX_DECODE_DIMENSION ||
                boundsOptions.outHeight / inSampleSize > MAX_DECODE_DIMENSION
            ) {
                inSampleSize *= 2
            }

            // 2. Decode sampled bitmap
            val decodeOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
            val rawBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, decodeOptions) ?: return null

            // 3. EXIF orientation handling
            val exif = try {
                ExifInterface(ByteArrayInputStream(bytes))
            } catch (_: Throwable) {
                null
            }

            val orientation = exif?.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            ) ?: ExifInterface.ORIENTATION_NORMAL

            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
            }

            if (!matrix.isIdentity) {
                val rotated = Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
                if (rotated != rawBitmap) {
                    rawBitmap.recycle()
                }
                rotated
            } else {
                rawBitmap
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load bitmap from URI $uri: ${e.message}", e)
            null
        }
    }
}
