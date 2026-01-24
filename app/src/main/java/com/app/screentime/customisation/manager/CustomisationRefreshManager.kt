package com.app.screentime.customisation.manager

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Singleton manager to handle customisation changes and trigger screen refreshes.
 * Uses SharedFlow with replay cache to ensure the latest refresh event is always available.
 */
object CustomisationRefreshManager {
    
    private val _refreshTrigger = MutableSharedFlow<Long>(
        replay = 1, // Keep the last emitted value for new subscribers
        extraBufferCapacity = 1
    )
    
    /**
     * Flow that emits when customisation changes need to trigger a refresh.
     * Observers can collect this flow to react to customisation updates.
     */
    val refreshTrigger: SharedFlow<Long> = _refreshTrigger.asSharedFlow()
    
    /**
     * Trigger a refresh by emitting a new timestamp.
     * This will notify all collectors to reload their data.
     */
    suspend fun triggerRefresh() {
        _refreshTrigger.emit(System.currentTimeMillis())
    }
    
    /**
     * Try to trigger refresh without suspending.
     * Returns true if emission was successful, false otherwise.
     */
    fun tryEmitRefresh(): Boolean {
        return _refreshTrigger.tryEmit(System.currentTimeMillis())
    }
}
