package com.app.screentime.core.model

/**
 * Global tracker for the currently active 1-on-1 chat partner.
 * Used to suppress noisy push notifications when the user is already on the ChatScreen.
 */
object ActiveChatTracker {
    @Volatile
    var activePartnerId: String? = null
}
