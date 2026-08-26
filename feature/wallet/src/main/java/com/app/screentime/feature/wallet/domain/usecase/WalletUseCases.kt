package com.app.screentime.feature.wallet.domain.usecase

import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.dto.WalletResponse
import com.app.screentime.core.network.session.SessionManager
import javax.inject.Inject

class GetWalletUseCase @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(): Result<WalletResponse> {
        val token = sessionManager.token ?: ""
        return try {
            val response = api.getWallet(token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class RechargeWalletUseCase @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(amount: Double): Result<Double> {
        val token = sessionManager.token ?: ""
        return try {
            val wallet = api.recharge(token, amount)
            Result.success(wallet.balance)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
