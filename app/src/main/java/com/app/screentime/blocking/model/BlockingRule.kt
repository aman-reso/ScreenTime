package com.app.screentime.blocking.model

/**
 * Represents different types of blocking rules
 */
sealed class BlockingRule {
    /**
     * Block app instantly
     */
    data class InstantBlock(
        val packageName: String,
        val appName: String
    ) : BlockingRule()

    /**
     * Block app after certain number of launches
     */
    data class LaunchBasedBlock(
        val packageName: String,
        val appName: String,
        val maxLaunches: Int,
        val currentLaunches: Int = 0
    ) : BlockingRule()

    /**
     * Block app after certain duration of usage
     */
    data class DurationBasedBlock(
        val packageName: String,
        val appName: String,
        val maxDurationMinutes: Int,
        val currentDurationMinutes: Long = 0L
    ) : BlockingRule()
}

