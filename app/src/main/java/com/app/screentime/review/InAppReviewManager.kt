package com.app.screentime.review

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages Google Play In-App Review API flows.
 * Handles:
 * 1. Explicit user-triggered reviews (e.g. "Rate App" in Account/Settings) with fallback to Play Store.
 * 2. Automatic contextual rating prompts after key positive user milestones (e.g., mutual matches).
 * 3. Enforces sensible cooldown intervals to respect Google Play quotas and user experience.
 */
@Singleton
class InAppReviewManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val reviewManager: ReviewManager by lazy {
        ReviewManagerFactory.create(context)
    }

    private val prefs by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val TAG = "InAppReviewManager"
        private const val PREFS_NAME = "in_app_review_prefs"
        private const val KEY_LAST_REVIEW_TIMESTAMP = "last_review_timestamp"
        private const val KEY_POSITIVE_ACTIONS_COUNT = "positive_actions_count"

        // Cooldown between automated in-app review prompts (7 days)
        private const val MIN_DAYS_BETWEEN_PROMPTS = 7
        // Minimum positive interactions before prompting
        private const val POSITIVE_ACTIONS_THRESHOLD = 3
    }

    /**
     * Launch the in-app review dialog flow.
     *
     * @param activity Foreground activity to host the review dialog.
     * @param fallbackToPlayStoreOnFailure If true (e.g. user manually clicked "Rate App"),
     * opens Play Store app page if Google's in-app flow cannot be launched.
     * @param onComplete Optional callback called when flow completes.
     */
    fun launchReviewFlow(
        activity: Activity,
        fallbackToPlayStoreOnFailure: Boolean = false,
        onComplete: (() -> Unit)? = null
    ) {
        try {
            val request = reviewManager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo: ReviewInfo = task.result
                    val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
                    flow.addOnCompleteListener {
                        Log.d(TAG, "In-app review flow completed.")
                        recordReviewPrompted()
                        onComplete?.invoke()
                    }
                } else {
                    val errorMsg = task.exception?.message ?: "Unknown error"
                    Log.w(TAG, "Failed to request review flow: $errorMsg")
                    if (fallbackToPlayStoreOnFailure) {
                        openPlayStore(activity)
                    }
                    onComplete?.invoke()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during launchReviewFlow: ${e.message}", e)
            if (fallbackToPlayStoreOnFailure) {
                openPlayStore(activity)
            }
            onComplete?.invoke()
        }
    }

    /**
     * Record a positive interaction (such as a mutual match or profile like)
     * and trigger an in-app review if the milestone and cooldown criteria are satisfied.
     */
    fun recordPositiveInteractionAndPromptIfEligible(activity: Activity) {
        val count = prefs.getInt(KEY_POSITIVE_ACTIONS_COUNT, 0) + 1
        prefs.edit().putInt(KEY_POSITIVE_ACTIONS_COUNT, count).apply()

        val lastPrompt = prefs.getLong(KEY_LAST_REVIEW_TIMESTAMP, 0L)
        val daysSinceLast = (System.currentTimeMillis() - lastPrompt) / (1000 * 60 * 60 * 24)

        if (count >= POSITIVE_ACTIONS_THRESHOLD && daysSinceLast >= MIN_DAYS_BETWEEN_PROMPTS) {
            launchReviewFlow(activity, fallbackToPlayStoreOnFailure = false)
        }
    }

    private fun recordReviewPrompted() {
        prefs.edit()
            .putLong(KEY_LAST_REVIEW_TIMESTAMP, System.currentTimeMillis())
            .putInt(KEY_POSITIVE_ACTIONS_COUNT, 0)
            .apply()
    }

    /**
     * Direct fallback to open Google Play Store listing for the app.
     */
    fun openPlayStore(context: Context) {
        val packageName = context.packageName
        try {
            val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(marketIntent)
        } catch (_: ActivityNotFoundException) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
        }
    }
}
