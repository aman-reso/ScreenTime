package com.app.screentime.core.network

import com.app.screentime.core.network.config.AppSecrets


/**
 * API endpoints configuration
 * All endpoints are organized by feature group
 */
object ApiEndpoints {

    /**
     * Get base URL from Remote Config or fallback to default
     */
    fun getBaseUrl(): String {
        return AppSecrets.Api.DEFAULT_BASE_URL
    }

    object Registration {
        const val REGISTER_DEVICE = "/api/users/register"
    }

    object Profile {
        const val GET_PROFILE = "/api/v1/user/profile"
        const val UPDATE_PROFILE = "/api/v1/user/profile"
        const val UPDATE_USERNAME = "/api/v1/user/username"
        const val GET_PREFERENCES = "/api/v1/user/preferences"
        const val UPDATE_PREFERENCES = "/api/v1/user/preferences"
        const val GET_ALLOWED_USERS = "/api/v1/user/timeline/allowed-users"
        const val ADD_ALLOWED_USER = "/api/v1/user/timeline/allowed-users"
        const val REMOVE_ALLOWED_USER = "/api/v1/user/timeline/allowed-users/{username}"
        const val UPDATE_ALLOWED_USER = "/api/v1/user/timeline/allowed-users/{username}"
        const val GET_LOCATION = "/api/v1/user/location"
        const val UPDATE_LOCATION = "/api/v1/user/location"
        const val SHARE_LOCATION = "/api/v1/user/location/share"
    }

    object Location {
        const val SYNC = "/api/location/sync"
        const val GET_USER_LAST_LOCATION = "/api/location/user/{username}/last"
    }

    // 3. App Usage API endpoints
    object AppUsage {
        const val BATCH_EVENTS = "/api/usage/events/batch"
        const val SYNC_DATA = "/api/usage/sync"
        const val GET_DAILY_STATS = "/api/usage/stats/daily"
        const val LAST_SYNC = "/api/usage/stats/last-sync"
        const val SUMMARY_SCREENTIME = "/api/summary/screentime"
        const val APP_STATS = "/api/app-stats"
    }

    // 4. Leaderboard API endpoints
    object Leaderboard {
        const val DAILY = "/api/leaderboard/daily"
        const val WEEKLY = "/api/leaderboard/weekly"
        const val MONTHLY = "/api/leaderboard/monthly"
        const val UPDATE_STATS = "/api/leaderboard/stats/update"
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
        const val STATUS_BY_USERNAME = "/api/users/{username}/totp/status"
        const val CONTROL_PANEL = "/api/v1/user/totp/control-panel"
        const val GRANT_ACCESS = "/api/v1/user/totp/grant-access"
        const val REVOKE_ACCESS = "/api/v1/user/totp/revoke-access"
        const val EXTEND_ACCESS = "/api/v1/user/totp/extend-access"
        const val ACCESSIBLE_USERS = "/api/v1/user/totp/accessible-users"
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

    // 12. Challenges API endpoints
    object Challenges {
        const val ACTIVE = "/api/challenges/active"
        const val JOIN = "/api/challenges/join"
        const val USER = "/api/challenges/user"
        const val DETAILS = "/api/challenges/{challengeId}"
        const val RANKINGS = "/api/challenges/{challengeId}/rankings"
        const val STATS = "/api/challenges/stats"
        const val STATS_BATCH = "/api/challenges/stats/batch"
        const val LAST_SYNC = "/api/challenges/{challengeId}/last-sync"
    }

    // 13. Rewards API endpoints
    object Rewards {
        const val COINS = "/api/rewards/coins"
        const val ADD_COINS = "/api/rewards/coins/add"
        const val REWARDS = "/api/rewards"
        const val CATALOG = "/api/rewards/catalog"
        const val CLAIM = "/api/rewards/catalog/claim"
        const val TRANSACTIONS = "/api/rewards/transactions"
    }

    object WALLPAPER {
        const val GET_WALLPAPERS = "/api/wallpaper"
    }

    object Config {
        const val GET_FEATURES = "/api/features"
    }

    object Feedback {
        const val SUBMIT = "/api/feedback"
    }

}