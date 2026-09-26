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
    private val api: WinterApi
) {
    suspend operator fun invoke(): Result<List<WalletPackDto>> {
        return try {
            val response = api.getWalletPacks()
            Result.success(response.packs)
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
