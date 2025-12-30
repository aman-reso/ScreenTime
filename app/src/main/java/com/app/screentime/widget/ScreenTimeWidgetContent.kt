package com.app.screentime.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionSendBroadcast
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.layout.wrapContentWidth
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.app.screentime.MainActivity
import com.app.screentime.R
import com.app.screentime.record.repository.formatDuration
import org.json.JSONArray


// Inter Font Family
private val monoSpace = FontFamily("sans-serif")

/**
 * Content composable for the ScreenTime widget using Glance Compose.
 */
@Preview(showBackground = true)
@SuppressLint("RestrictedApi")
@Composable
fun ScreenTimeWidgetContent(
    totalUsage: Long = 0L,
    topAppsJson: String? = null,
    context: Context? = null
) {
    // Format usage time
    val formattedTime = formatDuration(totalUsage)
    val topApps = parseTopApps(topAppsJson)
    val backgroundColor = ColorProvider(android.graphics.Color.BLACK)
    val textPrimaryColor = ColorProvider(android.graphics.Color.WHITE)
    val cornerRadiusValue = 16.dp

    val refreshAction = if (context != null) {
        actionSendBroadcast(
            Intent(WidgetRefreshReceiver.ACTION_REFRESH_WIDGET).apply {
                setPackage(context.packageName)
            }
        )
    } else {
        null
    }

    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(backgroundColor)
            .cornerRadius(cornerRadiusValue)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable(actionStartActivity<MainActivity>())
    ) {
        // Header row with title and refresh icon
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Screen Time",
                modifier = GlanceModifier.defaultWeight(),
                style = TextStyle(
                    color = textPrimaryColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = monoSpace
                )
            )

            // Refresh icon button
            if (refreshAction != null) {
                Box(
                    modifier = GlanceModifier
                        .size(28.dp)
                        .clickable(refreshAction)
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.ic_refresh),
                        colorFilter = androidx.glance.ColorFilter.tint(textPrimaryColor),
                        contentDescription = "Refresh",
                        modifier = GlanceModifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = GlanceModifier.height(6.dp))

        // Total usage time
        Text(
            text = formattedTime,
            modifier = GlanceModifier.fillMaxWidth(),
            style = TextStyle(
                color = textPrimaryColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = monoSpace,
                textAlign = TextAlign.Start
            )
        )

        Spacer(modifier = GlanceModifier.height(10.dp))

        // Top apps list
        if (topApps.isNotEmpty()) {
            topApps.take(5).forEachIndexed { index, app ->
                if (index > 0) {
                    Spacer(modifier = GlanceModifier.height(6.dp))
                }
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Usage time badge - fixed width for alignment
                    Box(
                        modifier = GlanceModifier
                            .width(62.dp)
                            .wrapContentHeight(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = formatDuration(app.totalUsageTime),
                            modifier = GlanceModifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                .cornerRadius(6.dp),
                            style = TextStyle(
                                color = textPrimaryColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = monoSpace
                            )
                        )
                    }
                    Spacer(modifier = GlanceModifier.width(8.dp))
                    // App name - takes remaining space
                    Text(
                        text = app.appName,
                        maxLines = 1,
                        modifier = GlanceModifier
                            .defaultWeight()
                            .wrapContentHeight(),
                        style = TextStyle(
                            color = textPrimaryColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = monoSpace
                        )
                    )
                }
            }
        } else {
            Text(
                text = "No app data",
                modifier = GlanceModifier.fillMaxWidth(),
                style = TextStyle(
                    color = textPrimaryColor,
                    fontSize = 12.sp,
                    fontFamily = monoSpace
                )
            )
        }
    }
}

/**
 * Parse top apps from JSON string
 */
private fun parseTopApps(topAppsJson: String?): List<TopApp> {
    if (topAppsJson == null) return emptyList()
    return try {
        val jsonArray = JSONArray(topAppsJson)
        (0 until jsonArray.length()).map { index ->
            val jsonObject = jsonArray.getJSONObject(index)
            TopApp(
                appName = jsonObject.optString("appName", "Unknown"),
                totalUsageTime = jsonObject.optLong("totalUsageTime", 0L)
            )
        }
    } catch (e: Exception) {
        emptyList()
    }
}

/**
 * Data class for top app display
 */
private data class TopApp(
    val appName: String, val totalUsageTime: Long
)
