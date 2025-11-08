package com.app.screentime.widget

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.currentState
import androidx.glance.state.PreferencesGlanceStateDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * App widget showing total screen time and top apps.
 */
class ScreenTimeWidgetProvider : GlanceAppWidget() {

    // Use PreferencesDataStore for widget state
    override val stateDefinition = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val prefs = currentState<Preferences>()
            val totalUsage = prefs[WidgetDataKeys.TOTAL_USAGE_MS] ?: 0L
            val dailyLimit = prefs[WidgetDataKeys.DAILY_LIMIT_MS] ?: (3 * 60 * 60 * 1000L)
            val topAppsJson = prefs[WidgetDataKeys.TOP_APPS] ?: "[]"
            ScreenTimeWidgetContent(
                totalUsage = totalUsage,
                topAppsJson = topAppsJson
            )
        }
    }
}

/**
 * Widget receiver that handles lifecycle events.
 */
class ScreenTimeWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = ScreenTimeWidgetProvider()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            android.appwidget.AppWidgetManager.ACTION_APPWIDGET_ENABLED -> {
                // Widget was added - ensure worker is scheduled
                WidgetUpdateWorker.schedule(context)
                // Trigger an immediate widget update via the worker
                // The worker will fetch latest data and update the widget
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        WidgetUpdateWorker.schedule(context)
                    } catch (e: Exception) {
                        // If scheduling fails, at least try to update with current data
                        ScreenTimeWidgetProvider().updateAll(context)
                    }
                }
            }

            android.appwidget.AppWidgetManager.ACTION_APPWIDGET_DELETED -> {
                // Clean up preferences if needed
                // Check if any widgets are still active
                val glanceManager = androidx.glance.appwidget.GlanceAppWidgetManager(context)
//                val glanceIds = glanceManager.getGlanceIds(ScreenTimeWidgetProvider::class.java)
//                if (glanceIds.isEmpty()) {
//                    // No widgets left - could cancel worker, but we'll keep it running
//                    // in case user adds widget again
//                }
            }
        }
    }
}

/**
 * Preference keys for storing widget data.
 */
object WidgetDataKeys {
    val TOTAL_USAGE_MS = longPreferencesKey("total_usage_ms")
    val DAILY_LIMIT_MS = longPreferencesKey("daily_limit_ms")
    val TOP_APPS = stringPreferencesKey("top_apps")
}
