package com.app.screentime.core.faceverification

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.app.screentime.core.faceverification.engine.FaceDetectorEngine
import com.app.screentime.core.faceverification.model.FaceVerificationResult
import com.google.mlkit.vision.face.Face
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Face Verification / Detection Manager using Google ML Kit.
 * Verifies whether a face is present in a photo prior to upload.
 */
@Singleton
class FaceVerificationManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val faceDetectorEngine: FaceDetectorEngine
) {

    companion object {
        private const val TAG = "FaceVerificationManager"
    }

    /**
     * Checks if a face is detected in the provided [bitmap].
     */
    suspend fun hasFace(bitmap: Bitmap): Boolean = withContext(Dispatchers.Default) {
        faceDetectorEngine.hasFace(bitmap)
    }

    /**
     * Checks if a face is detected in the image referenced by [uri].
     */
    suspend fun hasFace(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        faceDetectorEngine.hasFace(uri)
    }

    /**
     * Detects all faces in the provided [bitmap].
     */
    suspend fun detectFaces(bitmap: Bitmap): List<Face> = withContext(Dispatchers.Default) {
        faceDetectorEngine.detectFaces(bitmap)
    }

    /**
     * Checks if a face is present for [uri] and returns a structured [FaceVerificationResult].
     */
    suspend fun verifyFacePresent(uri: Uri): FaceVerificationResult = withContext(Dispatchers.IO) {
        try {
            val found = faceDetectorEngine.hasFace(uri)
            if (found) {
                FaceVerificationResult.FaceDetected
            } else {
                FaceVerificationResult.NoFaceDetected()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error verifying face presence for $uri: ${e.message}", e)
            FaceVerificationResult.Error(e)
        }
    }

    /**
     * Checks if a face is present in [bitmap] and returns a structured [FaceVerificationResult].
     */
    suspend fun verifyFacePresent(bitmap: Bitmap): FaceVerificationResult = withContext(Dispatchers.Default) {
        try {
            val found = faceDetectorEngine.hasFace(bitmap)
            if (found) {
                FaceVerificationResult.FaceDetected
            } else {
                FaceVerificationResult.NoFaceDetected()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error verifying face presence for bitmap: ${e.message}", e)
            FaceVerificationResult.Error(e)
        }
    }
}
