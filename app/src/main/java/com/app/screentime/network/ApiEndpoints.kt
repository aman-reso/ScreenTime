package com.app.screentime.network

/**
 * API endpoints configuration
 */
object ApiEndpoints {

    // Base URLs
    const val BASE_URL = "https://api.example.com"
    const val DEV_BASE_URL = "https://fb9bddeb8786.ngrok-free.app"
    const val LOCALHOST_URL = DEV_BASE_URL
    const val LOCAL_BASE_URL = LOCALHOST_URL

    // Screen Time API endpoints
    object ScreenTime {
        const val USAGE_STATS = "/api/v1/screen-time/usage-stats"
        const val HOURLY_USAGE = "/api/v1/screen-time/hourly-usage"
        const val APP_USAGE = "/api/v1/screen-time/app-usage"
        const val SYNC_DATA = "/api/v1/screen-time/sync"
        const val EXPORT_DATA = "/api/v1/screen-time/export"
        const val BATCH_USAGE = "/api/usage/batch"
        const val USER_USAGE = "/api/usage/username"
    }

    // Analytics API endpoints
    object Analytics {
        const val INSIGHTS = "/api/v1/analytics/insights"
        const val TRENDS = "/api/v1/analytics/trends"
        const val REPORTS = "/api/v1/analytics/reports"
    }

    // User API endpoints
    object User {
        const val PROFILE = "/api/v1/user/profile"
        const val SETTINGS = "/api/v1/user/settings"
        const val PREFERENCES = "/api/v1/user/preferences"
        const val REGISTER_DEVICE = "/api/users/register"
        const val SEARCH = "/api/users/search"
    }

    // Notification API endpoints
    object Notifications {
        const val SEND = "/api/v1/notifications/send"
        const val HISTORY = "/api/v1/notifications/history"
        const val SETTINGS = "/api/v1/notifications/settings"
    }

    // Consent/Privacy API endpoints
    object Consent {
        const val GET_CONSENTS = "/api/consents"
        const val SUBMIT = "/api/consents" // POST to same endpoint
        const val STATUS = "/api/consent/status"
    }

    // TOTP API endpoints
    object TOTP {
        const val VERIFY = "/api/totp/verify"
    }
}
