package com.app.screentime.applock.model

/**
 * Represents an app lock rule with PIN protection
 */
data class AppLockRule(
    val packageName: String,
    val appName: String,
    val isLocked: Boolean = true
)

