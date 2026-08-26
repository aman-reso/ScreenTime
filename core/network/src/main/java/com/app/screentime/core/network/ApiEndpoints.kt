package com.app.screentime.core.network

import com.app.screentime.core.network.config.AppSecrets

/**
 * Connect API endpoints configuration
 */
object ApiEndpoints {

    /**
     * Get base URL from Remote Config or fallback to default
     */
    fun getBaseUrl(): String {
        return AppSecrets.Api.DEFAULT_BASE_URL
    }

    object Auth {
        const val REGISTER = "/api/auth/register"
        const val LOGIN = "/api/auth/login"
        const val AUTH = "/api/auth"
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

