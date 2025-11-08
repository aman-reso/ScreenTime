package com.app.screentime.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Helper class to set up widgets programmatically
 */
object WidgetSetupHelper {

    /**
     * Request to pin/add the widget to the home screen.
     * On Android 8.0+ (API 26+), this uses requestPinAppWidget.
     * On older versions, it shows a message to guide the user.
     */
    suspend fun requestWidgetSetup(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestPinWidget(context)
        } else {
            // For older Android versions, show a guide message
            openWidgetPicker(context)
        }
    }

    /**
     * Request to pin widget on Android 8.0+ (API 26+)
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun requestPinWidget(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, ScreenTimeWidgetReceiver::class.java)

        // Check if widget is already pinned using Glance
        val glanceManager = GlanceAppWidgetManager(context)
        val glanceIds = glanceManager.getGlanceIds(ScreenTimeWidgetProvider::class.java)

        if (glanceIds.isNotEmpty()) {
            // Widget already exists, just update it
            CoroutineScope(Dispatchers.Main).launch {
                ScreenTimeWidgetProvider().updateAll(context)
            }
            android.widget.Toast.makeText(
                context,
                "Widget already added. Updating...",
                android.widget.Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Request to pin the widget
        val successCallback = android.app.PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, WidgetPinResultReceiver::class.java),
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or
                    android.app.PendingIntent.FLAG_IMMUTABLE
        )

        appWidgetManager.requestPinAppWidget(componentName, null, successCallback)
    }

    /**
     * Show message for older Android versions
     * Note: On older Android versions, we can't programmatically add widgets,
     * so we show a toast message to guide the user
     */
    private fun openWidgetPicker(context: Context) {
        // For older Android versions, we can't programmatically add widgets
        // Show a message to guide the user
        android.widget.Toast.makeText(
            context,
            "Please long-press on home screen and select Widgets to add Screen Time widget",
            android.widget.Toast.LENGTH_LONG
        ).show()
    }
}

/**
 * Broadcast receiver to handle widget pin result
 */
class WidgetPinResultReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: android.content.Context, intent: android.content.Intent) {
        // Handle widget pin result if needed
        // You can show a toast or update UI here
    }
}

