package com.app.screentime.network

import com.app.screentime.config.RemoteConfigManager

/**
 * API endpoints configuration
 * All endpoints are organized by feature group
 */
object ApiEndpoints {

    // Base URL will be fetched from Firebase Remote Config
    // This is a fallback value
    private const val DEFAULT_BASE_URL = "https://dcc7871457c1.ngrok-free.app"

    // Lazy initialization of RemoteConfigManager
    private var remoteConfigManager: RemoteConfigManager? = null

    /**
     * Initialize with RemoteConfigManager
     */
    fun initialize(remoteConfigManager: RemoteConfigManager) {
        ApiEndpoints.remoteConfigManager = remoteConfigManager
    }

    /**
     * Get base URL from Remote Config or fallback to default
     */
    fun getBaseUrl(): String {
        return remoteConfigManager?.getBaseUrl() ?: DEFAULT_BASE_URL
    }

    // 1. Registration API endpoints
    object Registration {
        const val REGISTER_DEVICE = "/api/users/register"
    }

    // 2. Profile API endpoints
    object Profile {
        const val GET_PROFILE = "/api/v1/user/profile"
        const val UPDATE_PROFILE = "/api/v1/user/profile"
        const val UPDATE_USERNAME = "/api/v1/user/username"
        const val GET_SETTINGS = "/api/v1/user/settings"
        const val UPDATE_SETTINGS = "/api/v1/user/settings"
        const val GET_PREFERENCES = "/api/v1/user/preferences"
        const val UPDATE_PREFERENCES = "/api/v1/user/preferences"
    }

    // 3. App Usage API endpoints
    object AppUsage {
        const val BATCH_USAGE = "/api/usage/batch"
        const val BATCH_EVENTS = "/api/usage/events/batch"
        const val SYNC_DATA = "/api/usage/sync"
        const val GET_DAILY_STATS = "/api/usage/stats/daily"
    }

    // 4. Leaderboard API endpoints
    object Leaderboard {
        const val DAILY = "/api/leaderboard/daily"
        const val WEEKLY = "/api/leaderboard/weekly"
        const val MONTHLY = "/api/leaderboard/monthly"
    }

    // 5. Search API endpoints
    object Search {
        const val SEARCH_USERS = "/api/users/search"
    }

    // 6. TOTP API endpoints
    object TOTP {
        const val VERIFY = "/api/totp/verify"
        const val GENERATE = "/api/totp/generate"
        const val VERIFY_BY_USERNAME = "/api/users/{username}/totp/verify"
    }

    // 7. Focus Duration API endpoints
    object Focus {
        const val SUBMIT_FOCUS = "/api/focus/submit"
        const val GET_FOCUS_HISTORY = "/api/focus/history"
        const val GET_FOCUS_STATS = "/api/focus/stats"
        const val SYNC_FOCUS_MODE_STATS = "/api/focus-mode-stats"
        const val GET_FOCUS_MODE_STATS = "/api/focus-mode-stats"
    }

    // 8. Consents API endpoints
    object Consent {
        const val GET_CONSENTS = "/api/consents"
        const val SUBMIT = "/api/consents" // POST to same endpoint
        const val STATUS = "/api/consent/status"
    }

    // 9. Blocked Domains API endpoints
    object BlockedDomain {
        const val GET_BLOCKED_DOMAINS = "/api/blocked-domains"
        const val GET_DOMAIN_GROUPS = "/api/blocked-domains/groups"
        const val SUBMIT_BLOCKED_DOMAIN = "/api/blocked-domains"
        const val UPDATE_BLOCKED_DOMAIN = "/api/blocked-domains/{id}"
        const val DELETE_BLOCKED_DOMAIN = "/api/blocked-domains/{id}"
    }

    // 10. URL Search API endpoints (VPN tracking)
    object URLSearch {
        const val SUBMIT_URL_SEARCH = "/api/url-search/submit"
        const val BATCH_URL_SEARCH = "/api/url-search/batch"
        const val GET_URL_HISTORY = "/api/url-search/history"
    }

    // 11. Notifications API endpoints (for notification settings, not for storing notifications)
    object Notifications {
        const val SEND = "/api/v1/notifications/send"
        const val HISTORY = "/api/v1/notifications/history"
        const val SETTINGS = "/api/v1/notifications/settings"
    }
}
