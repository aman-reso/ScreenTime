package com.app.screentime.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.app.screentime.data.entity.AppUsage
import org.json.JSONArray
import org.json.JSONObject


/**
 * Utility for updating the ScreenTime widget state and UI.
 */
class ScreenTimeWidgetUpdater(private val context: Context) {

    /**
     * Update the widget with the latest app usage data.
     */
    suspend fun updateWidget(
        totalUsage: Long,
        dailyLimit: Long,
        topApps: List<AppUsage>
    ) {
        val topAppsJson = convertAppsToJson(topApps)
        val glanceAppWidget = ScreenTimeWidgetProvider()
        val glanceManager = GlanceAppWidgetManager(context)
        val glanceIds = glanceManager.getGlanceIds(ScreenTimeWidgetProvider::class.java)
        glanceIds.forEach { glanceId ->
            updateAppWidgetState(
                context = context,
                definition = PreferencesGlanceStateDefinition,
                glanceId = glanceId
            ) { prefs ->
                prefs.toMutablePreferences().apply {
                    this[WidgetDataKeys.TOTAL_USAGE_MS] = totalUsage
                    this[WidgetDataKeys.DAILY_LIMIT_MS] = dailyLimit
                    this[WidgetDataKeys.TOP_APPS] = topAppsJson
                }
            }
        }
        glanceAppWidget.updateAll(context)
    }

    /**
     * Convert list of apps to JSON string.
     */
    private fun convertAppsToJson(apps: List<AppUsage>): String {
        val jsonArray = JSONArray()
        apps.sortedByDescending { it.appScreenTime }.take(3).forEach { app ->
            jsonArray.put(
                JSONObject().apply {
                    put("packageName", app.packageName)
                    put("appName", app.appName)
                    put("usageTime", app.appScreenTime)
                }
            )
        }
        return jsonArray.toString()
    }
}
