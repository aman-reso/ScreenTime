package com.app.screentime.core.faceverification.model

/**
 * Result of ML Kit Face Detection.
 */
sealed class FaceVerificationResult {

    /**
     * Face is detected in the image.
     */
    data object FaceDetected : FaceVerificationResult()

    /**
     * No face detected in the image.
     */
    data class NoFaceDetected(
        val message: String = "No face detected in photo. Please ensure your face is clearly visible."
    ) : FaceVerificationResult()

    /**
     * Error occurred during detection.
     */
    data class Error(
        val cause: Throwable,
        val message: String = cause.localizedMessage ?: "Face detection failed"
    ) : FaceVerificationResult()
}
