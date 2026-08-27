package com.app.screentime.calling.domain.usecase

import com.app.screentime.calling.data.model.CallSocketMessage
import com.app.screentime.calling.domain.model.CallSession
import com.app.screentime.calling.domain.model.CallState
import javax.inject.Inject

/**
 * Handles 1-second server ticks, tracks 30-second billing cycle intervals,
 * and detects low balance (< 30s) and balance exhaustion events.
 */
class BillingTickHandler @Inject constructor() {

    fun processTick(
        message: CallSocketMessage,
        currentState: CallState
    ): CallState {
        if (currentState !is CallState.Active) return currentState

        val duration = message.durationSec ?: currentState.session.durationSec
        val remaining = message.remainingSec ?: currentState.session.remainingSec
        val cost = message.cost ?: currentState.session.totalCost

        val updatedSession = currentState.session.copy(
            durationSec = duration,
            remainingSec = remaining,
            totalCost = cost
        )

        val isLowBalance = remaining in 1..30
        val warning = if (isLowBalance) {
            "Low balance! Less than $remaining seconds remaining in call."
        } else {
            null
        }

        return CallState.Active(
            session = updatedSession,
            isLowBalance = isLowBalance,
            warningMessage = warning
        )
    }

    fun isThirtySecondCycle(durationSec: Int): Boolean {
        return durationSec > 0 && durationSec % 30 == 0
    }
}
