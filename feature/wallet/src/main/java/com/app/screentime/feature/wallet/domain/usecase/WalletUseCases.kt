package com.app.screentime.feature.wallet.domain.usecase

import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.dto.PaginatedTransactionsData
import com.app.screentime.core.network.dto.WalletPackDto
import com.app.screentime.core.network.dto.WalletResponse
import com.app.screentime.core.network.session.SessionManager
import javax.inject.Inject

class GetWalletUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(): Result<WalletResponse> {
        val token = sessionManager.getToken() ?: ""
        return try {
            val response = api.getWallet(token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetWalletTransactionsUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(page: Int = 1, limit: Int = 20): Result<PaginatedTransactionsData> {
        val token = sessionManager.getToken() ?: ""
        return try {
            val data = api.getWalletTransactions(token = token, page = page, limit = limit)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetWalletPacksUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(): Result<List<WalletPackDto>> {
        val token = sessionManager.getToken() ?: ""
        return try {
            // Fetch server-driven products from https://connect-api-test-324708289864.us-central1.run.app/api/billing/products
            val billingRes = api.getBillingProducts(token)
            if (billingRes.isSuccess) {
                val data = billingRes.getOrNull()
                val inappPacks = data?.inapp_products?.map { it.toWalletPackDto() }.orEmpty()
                val subPacks = data?.subscriptions?.map { it.toWalletPackDto() }.orEmpty()
                val allPacks = inappPacks + subPacks
                if (allPacks.isNotEmpty()) {
                    return Result.success(allPacks)
                }
            }
            billingRes.exceptionOrNull()?.let { ex ->
                Result.failure(ex)
            } ?: Result.failure(Exception("No billing products returned from server"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class RechargeWalletUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(amount: Double): Result<Double> {
        val token = sessionManager.getToken() ?: ""
        return try {
            val wallet = api.recharge(token, amount)
            val currentBal = sessionManager.currentUser?.walletBalance ?: 0.0
            val newBal = if (wallet.balance > 0.0) wallet.balance else (currentBal + amount)
            sessionManager.currentUser = sessionManager.currentUser?.copy(walletBalance = newBal)
            Result.success(newBal)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetWalletInfoUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(): Result<com.app.screentime.core.network.dto.WalletInfoDto> {
        val token = sessionManager.getToken() ?: ""
        return try {
            val info = api.getWalletInfo(token)
            Result.success(info)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class VerifyGooglePlayPurchaseUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(
        purchaseToken: String,
        productId: String,
        type: String = "inapp",
        orderId: String? = null,
        packId: String? = null,
        credits: Int = 0,
        amount: Double = 0.0
    ): Result<com.app.screentime.core.network.dto.VerifyGooglePurchaseDataDto> {
        val token = sessionManager.getToken() ?: ""
        val request = com.app.screentime.core.network.dto.VerifyGooglePurchaseRequest(
            package_name = "com.app.screentime",
            product_id = productId,
            purchase_token = purchaseToken,
            type = type
        )
        return try {
            val verifyRes = api.verifyGooglePurchase(token, request)
            if (verifyRes.isSuccess) {
                verifyRes
            } else {
                // Fallback to legacy verify endpoint
                val legacyRes = api.verifyGooglePlayPurchase(
                    token = token,
                    purchaseToken = purchaseToken,
                    productId = productId,
                    orderId = orderId,
                    packId = packId,
                    credits = credits,
                    amount = amount
                )
                if (legacyRes.isSuccess) {
                    val legacyData = legacyRes.getOrNull()
                    Result.success(
                        com.app.screentime.core.network.dto.VerifyGooglePurchaseDataDto(
                            success = true,
                            transaction_id = orderId,
                            product_id = productId,
                            credits_added = legacyData?.credits_awarded ?: credits,
                            credit_balance = (legacyData?.balance ?: 0.0).toInt(),
                            message = legacyData?.message ?: "Purchase verified!"
                        )
                    )
                } else {
                    verifyRes
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


class GetOrderStatusUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(orderId: String): Result<com.app.screentime.core.network.dto.WalletOrderStatusDto> {
        val token = sessionManager.getToken() ?: ""
        return api.getOrderStatus(token = token, orderId = orderId)
    }
}
