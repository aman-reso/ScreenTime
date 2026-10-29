package com.app.screentime.feature.chat.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Tracks photo messages that have been opened/viewed so they can never be shown again,
 * even across app restarts or remote message refetches.
 */
@Singleton
class ViewOncePhotoTracker @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("winter_view_once_photos", Context.MODE_PRIVATE)

    fun markAsViewed(id: String, clientMessageId: String? = null, mediaUrl: String? = null) {
        val currentSet = prefs.getStringSet(KEY_VIEWED_IDS, emptySet())?.toMutableSet() ?: mutableSetOf()
        if (id.isNotBlank()) currentSet.add(id)
        if (!clientMessageId.isNullOrBlank()) currentSet.add(clientMessageId)
        if (!mediaUrl.isNullOrBlank()) currentSet.add(mediaUrl)
        prefs.edit().putStringSet(KEY_VIEWED_IDS, currentSet).apply()
    }

    fun isViewed(id: String, clientMessageId: String? = null, mediaUrl: String? = null): Boolean {
        val set = prefs.getStringSet(KEY_VIEWED_IDS, emptySet()) ?: return false
        if (id.isNotBlank() && set.contains(id)) return true
        if (!clientMessageId.isNullOrBlank() && set.contains(clientMessageId)) return true
        if (!mediaUrl.isNullOrBlank() && set.contains(mediaUrl)) return true
        return false
    }

    companion object {
        private const val KEY_VIEWED_IDS = "viewed_photo_ids"
    }
}
