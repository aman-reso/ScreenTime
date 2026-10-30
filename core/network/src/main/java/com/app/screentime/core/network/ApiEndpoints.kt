package com.app.screentime.core.network

import com.app.screentime.core.network.config.AppSecrets
import com.app.screentime.core.network.security.NativeSecurityVault

/**
 * Connect API endpoints configuration
 */
object ApiEndpoints {

    @Volatile
    private var overrideBaseUrl: String? = null

    fun setBaseUrl(url: String) {
        overrideBaseUrl = url.trimEnd('/').removeSuffix("/api")
    }

    /**
     * Get base URL from custom override, or natively resolved AppSecrets
     */
    fun getBaseUrl(): String {
        return overrideBaseUrl ?: AppSecrets.Api.DEFAULT_BASE_URL
    }

    object Config {
        const val CONFIG = "/api/config"
    }


    object Auth {
        const val REGISTER = "/api/auth/register"
        const val LOGIN = "/api/auth/login"
        const val AUTH = "/api/auth"
        const val GOOGLE = "/api/auth/google"
        const val OTP_SEND = "/api/auth/otp/send"
        const val OTP_VERIFY = "/api/auth/otp/verify"
        const val PROFILE = "/api/profile"
        const val VISIBILITY = "/api/profile/visibility"
        const val FCM_TOKEN = "/api/auth/fcm-token"
    }

    object Preferences {
        const val PREFERENCES = "/api/preferences"
    }

    object Discovery {
        const val FEED = "/api/feed"
        const val CANDIDATES = "/api/discovery"
        const val INTERACT = "/api/discovery/interact"
        const val MATCHES = "/api/discovery/matches"
    }

    object Messages {
        // v2 Endpoints (Strict Header-based Bearer JWT authentication)
        const val CONVERSATIONS_V2 = "/api/v2/messages/conversations"
        fun conversationMessagesV2(conversationId: String) = "/api/v2/messages/conversations/$conversationId"
        const val SEND_V2 = "/api/v2/messages/send"

        // Legacy v1 Endpoints (Fallback)
        const val CONVERSATIONS = "/api/messages/conversations"
        fun conversationDetail(conversationId: String) = "/api/messages/conversations/$conversationId"
        fun conversationMessages(conversationId: String) = "/api/messages/conversations/$conversationId"
        fun legacyConversationMessages(conversationId: String) = "/api/messages/conversations/$conversationId"
        const val SEND = "/api/messages/send"
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
        const val INFO = "/api/wallet/info"
        const val TRANSACTIONS = "/api/wallet/transactions"
        const val PACKAGES = "/api/wallet/packages"
        const val PACKS = "/api/wallet/packs"
        const val ORDER_STATUS = "/api/wallet/order/status"
        const val GOOGLE_PLAY_VERIFY = "/api/wallet/google-play/verify"
    }

    object Billing {
        val BASE_URL: String get() = NativeSecurityVault.getBillingBaseUrl()
        const val PRODUCTS = "/api/billing/products"
        const val GOOGLE_VERIFY = "/api/billing/google/verify"

        fun getProductsUrl(): String = "$BASE_URL$PRODUCTS"
        fun getVerifyUrl(): String = "$BASE_URL$GOOGLE_VERIFY"
    }

    object History {
        const val CALLS = "/api/history/calls"
    }

    object Payments {
        const val ORDER = "/api/payments/order"
        const val CALLBACK = "/api/payments/callback"
        const val WEBHOOK = "/api/payments/webhook"
        const val RETRY = "/api/payments/retry"
        const val REFUND = "/api/payments/refund"
        const val TIMELINE = "/api/payments/timeline"
    }

    object Onboarding {
        const val SUBMIT = "/api/model/onboarding"
        const val STATUS = "/api/model/onboarding/status"
    }

    object Upload {
        const val IMAGES = "/api/upload/images"
    }

    object Reports {
        const val CREATE = "/api/reports"
        const val MODEL_REPORTS = "/api/reports/model"
    }
}

