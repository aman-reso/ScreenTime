package com.app.screentime.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * BroadcastReceiver to handle widget refresh action
 */
class WidgetRefreshReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_REFRESH_WIDGET) {
            Log.d(TAG, "WidgetRefreshReceiver: Refresh action received")
            
            // Trigger widget update via worker
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Trigger immediate widget update
                    WidgetUpdateWorker.triggerImmediateUpdate(context)
                } catch (e: Exception) {
                    Log.e(TAG, "WidgetRefreshReceiver: Error refreshing widget", e)
                }
            }
        }
    }

    companion object {
        private const val TAG = "WidgetRefreshReceiver"
        const val ACTION_REFRESH_WIDGET = "com.app.screentime.widget.REFRESH"
    }
}

