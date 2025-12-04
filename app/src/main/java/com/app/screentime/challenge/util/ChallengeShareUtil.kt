package com.app.screentime.challenge.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.graphics.createBitmap

/**
 * Utility class for sharing challenge content
 */
@Singleton
class ChallengeShareUtil @Inject constructor() {

    /**
     * Generate deep link URL for a challenge
     */
    fun generateDeepLink(challengeId: String): String {
        // Use web URL for better compatibility across platforms
        return "https://apptime.in/challenge_detail/$challengeId"
    }

    /**
     * Generate share text with challenge details
     */
    fun generateShareText(
        title: String,
        prize: String,
        deepLink: String
    ): String {
        return buildString {
            append("🎯 $title\n\n")
            append("💰 Prize: $prize\n\n")
            append("Join this challenge and compete with others!\n\n")
            append("🔗 $deepLink")
        }
    }

    /**
     * Share challenge with image, title, prize, and deeplink
     *
     * @param challengeId Challenge ID for deeplink
     * @param title Challenge title
     * @param prize Prize amount/description
     * @param imageUrl Optional image URL to include in share
     */
    suspend fun shareChallenge(
        challengeId: String,
        title: String,
        prize: String,
        imageUrl: String? = null, context: Context
    ) {
        withContext(Dispatchers.IO) {
            try {
                val deepLink = generateDeepLink(challengeId)
                val shareText = generateShareText(title, prize, deepLink)

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    putExtra(Intent.EXTRA_SUBJECT, title)

                    // If image URL is provided, try to include it
                    imageUrl?.let { url ->
                        try {
                            val imageFile = downloadImage(url, context)
                            if (imageFile != null) {
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    imageFile
                                )
                                type = "image/*"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                        } catch (e: Exception) {
                            type = "text/plain"
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    val chooserIntent = Intent.createChooser(shareIntent, "Share Challenge")
                    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(chooserIntent)
                }
            } catch (e: Exception) {
                // Fallback: simple text share
                withContext(Dispatchers.Main) {
                    val fallbackIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            generateShareText(title, prize, generateDeepLink(challengeId))
                        )
                        putExtra(Intent.EXTRA_SUBJECT, title)
                    }
                    context.startActivity(Intent.createChooser(fallbackIntent, "Share Challenge"))
                }
            }
        }
    }

    /**
     * Download image from URL to a temporary file
     */
    private suspend fun downloadImage(imageUrl: String, context: Context): File? {
        return try {
            val imageLoader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .build()

            val result = imageLoader.execute(request)
            if (result is SuccessResult) {
                val drawable = result.drawable
                val bitmap = drawableToBitmap(drawable)

                // Save to cache directory
                val cacheDir = File(context.cacheDir, "share_images")
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs()
                }

                val imageFile = File(cacheDir, "challenge_${System.currentTimeMillis()}.png")
                FileOutputStream(imageFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }

                imageFile
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Convert Drawable to Bitmap
     */
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable && drawable.bitmap != null) {
            return drawable.bitmap
        }

        val bitmap = if (drawable.intrinsicWidth > 0 && drawable.intrinsicHeight > 0) {
            createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
        } else {
            createBitmap(1, 1)
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}

