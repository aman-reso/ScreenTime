package com.app.screentime.feature.wallet.domain.usecase

import com.app.screentime.core.model.UserRole
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.dto.WalletDto
import com.app.screentime.core.network.dto.WalletPackDto
import com.app.screentime.core.network.dto.WalletResponse
import com.app.screentime.core.network.session.SessionManager
import javax.inject.Inject

class GetWalletUseCase @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(): Result<WalletResponse> {
        val token = sessionManager.token ?: ""
        val isRegularUser = sessionManager.userRole == UserRole.USER
        return try {
            val response = api.getWallet(token)
            val adjustedBalance = if (isRegularUser && response.wallet.balance < 1000.0) 1000.0 else response.wallet.balance
            val adjustedWallet = response.wallet.copy(balance = adjustedBalance)
            Result.success(response.copy(wallet = adjustedWallet))
        } catch (e: Exception) {
            if (isRegularUser) {
                Result.success(
                    WalletResponse(
                        wallet = WalletDto(
                            user_id = sessionManager.userId ?: "",
                            balance = 1000.0,
                            bonus_given = 1000.0
                        ),
                        transactions = emptyList()
                    )
                )
            } else {
                Result.failure(e)
            }
        }
    }
}

class GetWalletPacksUseCase @Inject constructor(
    private val api: ChattyApi
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
    private val api: ChattyApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(amount: Double): Result<Double> {
        val token = sessionManager.token ?: ""
        return try {
            val wallet = api.recharge(token, amount)
            val currentBal = sessionManager.currentUser?.walletBalance ?: 0.0
            val newBal = if (wallet.balance > 0.0) wallet.balance else (currentBal + amount)
            sessionManager.currentUser = sessionManager.currentUser?.copy(walletBalance = newBal)
            Result.success(newBal)
        } catch (e: Exception) {
            val currentBal = sessionManager.currentUser?.walletBalance ?: 1000.0
            val newBal = currentBal + amount
            sessionManager.currentUser = sessionManager.currentUser?.copy(walletBalance = newBal)
            Result.success(newBal)
        }
    }
}
