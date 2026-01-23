package com.app.screentime.reward.util

import com.app.screentime.reward.model.CoinHistoryItem
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Utility class for calculating expiring coins from coin history
 */
object ExpiringCoinsCalculator {
    /**
     * Calculate the total amount of coins that will expire soon (within 30 days)
     * and find the nearest expiration date
     * 
     * @param coinHistory List of coin history items
     * @return Pair of (totalExpiringCoins, nearestExpirationDate) or null if no expiring coins
     */
    fun calculateExpiringCoins(coinHistory: List<CoinHistoryItem>): Pair<Int, Instant>? {
        val now = Instant.now()
        val thirtyDaysFromNow = now.plus(30, ChronoUnit.DAYS)
        
        // Filter coins that have expiresAt and are not expired yet
        val expiringItems = coinHistory.filter { item ->
            item.expiresAt != null && 
            item.amount > 0 && // Only positive amounts (earned coins)
            try {
                val expiresAt = Instant.parse(item.expiresAt)
                expiresAt.isAfter(now) && expiresAt.isBefore(thirtyDaysFromNow)
            } catch (e: Exception) {
                false
            }
        }
        
        if (expiringItems.isEmpty()) {
            return null
        }
        
        // Find the nearest expiration date
        val nearestExpiration = expiringItems
            .mapNotNull { item ->
                try {
                    Instant.parse(item.expiresAt)
                } catch (e: Exception) {
                    null
                }
            }
            .minOrNull()
        
        if (nearestExpiration == null) {
            return null
        }
        
        // Calculate total expiring coins
        val totalExpiringCoins = expiringItems.sumOf { it.amount }
        
        return Pair(totalExpiringCoins, nearestExpiration)
    }
    
    /**
     * Get the nearest expiring coin amount
     * This finds the coin with the nearest expiresAt date within 30 days and returns its amount
     * 
     * @param coinHistory List of coin history items
     * @return Amount of coins expiring soon (within 30 days), or null if no expiring coins
     */
    fun getNearestExpiringCoins(coinHistory: List<CoinHistoryItem>): Int? {
        val now = Instant.now()
        val thirtyDaysFromNow = now.plus(30, ChronoUnit.DAYS)
        
        // Filter coins that have expiresAt, are not expired yet, and expire within 30 days
        val expiringItems = coinHistory.filter { item ->
            item.expiresAt != null && 
            item.amount > 0 && // Only positive amounts (earned coins)
            try {
                val expiresAt = Instant.parse(item.expiresAt)
                expiresAt.isAfter(now) && expiresAt.isBefore(thirtyDaysFromNow)
            } catch (e: Exception) {
                false
            }
        }
        
        if (expiringItems.isEmpty()) {
            return null
        }
        
        // Find the item with the nearest expiration date
        val nearestItem = expiringItems.minByOrNull { item ->
            try {
                val expiresAt = Instant.parse(item.expiresAt)
                ChronoUnit.DAYS.between(now, expiresAt)
            } catch (e: Exception) {
                Long.MAX_VALUE
            }
        }
        
        return nearestItem?.amount
    }
}

