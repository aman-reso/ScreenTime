package com.app.screentime.reward.util

/**
 * Utility class for calculating user level based on coins
 * Max coins: 100000
 * Levels: 1-10 (10 levels, 10000 coins per level)
 */
object CoinLevelCalculator {
    private const val MAX_COINS = 100000
    private const val COINS_PER_LEVEL = 10000
    private const val MAX_LEVEL = 10

    /**
     * Calculate the current level based on total coins
     * @param totalCoins Total coins earned
     * @return Current level (1-10)
     */
    fun calculateLevel(totalCoins: Int): Int {
        if (totalCoins <= 0) return 1
        if (totalCoins >= MAX_COINS) return MAX_LEVEL
        
        val level = (totalCoins / COINS_PER_LEVEL) + 1
        return level.coerceIn(1, MAX_LEVEL)
    }

    /**
     * Calculate progress within current level (0.0 to 1.0)
     * @param totalCoins Total coins earned
     * @return Progress within current level (0.0 to 1.0)
     */
    fun calculateProgressInLevel(totalCoins: Int): Float {
        if (totalCoins <= 0) return 0f
        if (totalCoins >= MAX_COINS) return 1f
        
        val currentLevel = calculateLevel(totalCoins)
        val coinsInCurrentLevel = totalCoins - ((currentLevel - 1) * COINS_PER_LEVEL)
        return (coinsInCurrentLevel.toFloat() / COINS_PER_LEVEL).coerceIn(0f, 1f)
    }

    /**
     * Get the minimum coins required for a level
     * @param level The level (1-10)
     * @return Minimum coins required for that level
     */
    fun getMinCoinsForLevel(level: Int): Int {
        if (level <= 1) return 0
        if (level > MAX_LEVEL) return MAX_COINS
        return (level - 1) * COINS_PER_LEVEL
    }

    /**
     * Get the maximum coins for a level
     * @param level The level (1-10)
     * @return Maximum coins for that level
     */
    fun getMaxCoinsForLevel(level: Int): Int {
        if (level >= MAX_LEVEL) return MAX_COINS
        return level * COINS_PER_LEVEL
    }
}

