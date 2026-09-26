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
        return overrideBaseUrl ?: AppSecrets.Api.DEFAULT_BASE_URL.ifBlank { "https://connect-api-324708289864.us-central1.run.app" }
    }


    object Auth {
        const val REGISTER = "/api/auth/register"
        const val LOGIN = "/api/auth/login"
        const val AUTH = "/api/auth"
        const val GOOGLE = "/api/auth/google"
        const val PROFILE = "/api/profile"
        const val FCM_TOKEN = "/api/auth/fcm-token"
    }

    object Discovery {
        const val FEED = "/api/feed"
        const val CANDIDATES = "/api/discovery"
        const val INTERACT = "/api/discovery/interact"
        const val MATCHES = "/api/discovery/matches"
    }

    object Messages {
        const val CONVERSATIONS = "/api/messages/conversations"
        fun conversationDetail(conversationId: String) = "/api/messages/conversations/$conversationId"
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

    object Upload {
        const val IMAGES = "/api/upload/images"
    }

    object Reports {
        const val CREATE = "/api/reports"
        const val MODEL_REPORTS = "/api/reports/model"
    }
}

