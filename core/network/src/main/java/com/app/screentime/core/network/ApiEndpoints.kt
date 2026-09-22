package com.app.screentime.core.network

import com.app.screentime.core.network.config.AppSecrets

/**
 * Connect API endpoints configuration
 */
object ApiEndpoints {

    @Volatile
    private var overrideBaseUrl: String? = null

    fun setBaseUrl(url: String) {
        overrideBaseUrl = url.trimEnd('/')
    }

    /**
     * Get base URL from custom override, AppSecrets, or fallback to production vercel url
     */
    fun getBaseUrl(): String {
        return overrideBaseUrl ?: AppSecrets.Api.DEFAULT_BASE_URL.ifBlank { "https://connect-omega-six.vercel.app" }
    }

    object ConnectAuth {
        const val OTP_REQUEST = "/api/auth/otp/request"
        const val OTP_VERIFY = "/api/auth/otp/verify"
        const val ME = "/api/auth/me"
    }

    object ConnectTemplates {
        const val LIST = "/api/templates"
        const val DETAIL = "/api/templates/"
    }

    object ConnectSites {
        const val BASE = "/api/sites"
        const val CHECK_SLUG = "/api/sites/check-slug"
        fun detail(siteId: String) = "/api/sites/$siteId"
        fun publish(siteId: String) = "/api/sites/$siteId/publish"
        fun unpublish(siteId: String) = "/api/sites/$siteId/unpublish"
        fun analytics(siteId: String) = "/api/sites/$siteId/analytics"
        fun toggleAds(siteId: String) = "/api/sites/$siteId/ads/toggle"
        fun earnings(siteId: String) = "/api/sites/$siteId/earnings"
        fun catalog(siteId: String) = "/api/sites/$siteId/catalog"
        fun catalogItem(siteId: String, itemId: String) = "/api/sites/$siteId/catalog/$itemId"
    }

    object ConnectVoice {
        const val SESSION = "/api/voice/session"
        const val PATCH = "/api/voice/patch"
    }

    object ConnectAI {
        const val PARSE = "/api/ai/parse"
        const val GENERATE_AND_CREATE = "/api/ai/generate-and-create"
    }

    object ConnectMonetization {
        const val PAYOUTS = "/api/payouts"
        const val SIMULATE_AD_REVENUE = "/api/simulate/ad-revenue"
    }

    object Auth {
        const val REGISTER = "/api/auth/register"
        const val LOGIN = "/api/auth/login"
        const val AUTH = "/api/auth"
        const val GOOGLE = "/api/auth/google"
    }

    object Models {
        const val LIST = "/api/models"
        const val DETAIL = "/api/models/{id}"
        const val TOGGLE_FAVORITE = "/api/models/favorite"
        const val GET_FAVORITES = "/api/models/favorites"
        const val GET_FAVORITE_IDS = "/api/models/favorite-ids"
    }

    object Rooms {
        const val LIST_OR_CREATE = "/api/rooms"
        const val JOIN = "/api/rooms/{id}/join"
        const val LEAVE = "/api/rooms/{id}/leave"
    }

    object Wallet {
        const val GET_OR_RECHARGE = "/api/wallet"
        const val RECHARGE_ALT = "/api/wallet/recharge"
    }

    object History {
        const val CALLS = "/api/history/calls"
    }

    object Payments {
        const val ORDER = "/api/payments/order"
        const val CALLBACK = "/api/payments/callback"
        const val RETRY = "/api/payments/retry"
        const val REFUND = "/api/payments/refund"
        const val TIMELINE = "/api/payments/timeline"
    }

    object Onboarding {
        const val SUBMIT = "/api/model/onboarding"
        const val STATUS = "/api/model/onboarding/status"
    }

    object Reports {
        const val CREATE = "/api/reports"
        const val MODEL_REPORTS = "/api/reports/model"
    }
}

