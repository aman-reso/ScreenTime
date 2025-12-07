package com.app.screentime.navigation

import android.net.Uri
import android.util.Log

/**
 * Utility object for parsing deep links and converting them to Screen objects.
 */
object DeeplinkParser {
    private const val TAG = "DeeplinkParser"

    /**
     * Parses a deep link URI and returns the corresponding Screen object.
     * 
     * Supports:
     * - apptime://screen/route
     * - https://apptime.in/route
     * 
     * @param uri The deep link URI to parse
     * @return The corresponding Screen object, or null if the URI cannot be parsed
     */
    fun parseDeeplink(uri: Uri?): Screen? {
        if (uri == null) return null

        Log.d(TAG, "Parsing deeplink: $uri")

        // Extract the route from the URI
        // For apptime://screen/route -> pathSegments = ["route", ...]
        // For https://apptime.in/route -> pathSegments = ["route", ...]
        val pathSegments = uri.pathSegments
        if (pathSegments.isEmpty()) {
            Log.w(TAG, "No path segments found in URI: $uri")
            return null
        }

        val route = pathSegments.first()
        Log.d(TAG, "Extracted route: $route, segments: $pathSegments")

        return when (route) {
            "landing", "home" -> Screen.Landing
            "profile" -> Screen.Profile
            "statistics" -> Screen.Statistics
            "challenges", "challenge_list" -> Screen.Challenges
            "leaderboard" -> Screen.Leaderboard
            "search" -> Screen.Search
            "app_blocking" -> Screen.AppBlocking
            "blocked_links" -> Screen.BlockedLinks
            "focus_mode" -> Screen.FocusMode
            "permission" -> Screen.Permission
            "reward" -> Screen.Reward

            // Parametrized routes
            "challenge_detail" -> {
                val challengeId = pathSegments.getOrNull(1)
                if (challengeId != null) {
                    Screen.ChallengeDetail(ChallengeDetailParams(challengeId))
                } else {
                    Log.w(TAG, "Missing challengeId for challenge_detail route")
                    null
                }
            }

            "app_usage_detail", "app_details" -> {
                val packageName = pathSegments.getOrNull(1)
                if (packageName != null) {
                    Screen.SingleAppUsageDetail(SingleAppUsageDetailParams(packageName))
                } else {
                    Log.w(TAG, "Missing packageName for app_usage_detail route")
                    null
                }
            }

            "record_detail" -> {
                val username = pathSegments.getOrNull(1)
                if (username != null) {
                    Screen.RecordDetail(RecordDetailParams(username))
                } else {
                    Log.w(TAG, "Missing username for record_detail route")
                    null
                }
            }

            else -> {
                Log.w(TAG, "Unknown route: $route")
                null
            }
        }
    }

    /**
     * Determines if a deep link should clear the back stack.
     * Only landing/home routes should clear the back stack.
     */
    fun shouldClearBackStack(screen: Screen?): Boolean {
        return screen is Screen.Landing
    }

    /**
     * Determines if a deep link should add Landing to the back stack first.
     * All screens except Landing and Permission should have Landing in their back stack.
     */
    fun shouldAddLandingToBackStack(screen: Screen?): Boolean {
        return screen != null && screen !is Screen.Landing && screen !is Screen.Permission
    }
}
