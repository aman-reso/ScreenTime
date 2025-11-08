package com.app.screentime.widget

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.layout.wrapContentWidth
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.app.screentime.MainActivity
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.ui.theme.NeutralBlackDark
import com.app.screentime.ui.theme.TitleTextColor
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
    totalUsage: Long = 0L, topAppsJson: String? = null
) {
    // Format usage time
    val formattedTime = formatDuration(totalUsage)
    val topApps = parseTopApps(topAppsJson)
    Column(
        modifier = GlanceModifier.wrapContentHeight().background(NeutralBlackDark).padding(12.dp)
            .clickable(actionStartActivity<MainActivity>())
    ) {
        Text(
            "Screen Time", modifier = GlanceModifier.defaultWeight(), style = TextStyle(
                color = ColorProvider(TitleTextColor),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = monoSpace
            )
        )
        Spacer(modifier = GlanceModifier.height(4.dp))
        Text(
            formattedTime, modifier = GlanceModifier.defaultWeight(), style = TextStyle(
                color = ColorProvider(TitleTextColor),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = monoSpace
            )
        )
        Spacer(modifier = GlanceModifier.height(8.dp))
        topApps.forEach { app ->
            Spacer(modifier = GlanceModifier.height(4.dp))
            Row(modifier = GlanceModifier.defaultWeight().wrapContentHeight()) {
                Text(
                    formatDuration(app.totalUsageTime),
                    modifier = GlanceModifier.wrapContentHeight().wrapContentWidth().background(
                        color = NeutralBlackDark
                    ).cornerRadius(24.dp),
                    style = TextStyle(
                        color = ColorProvider(TitleTextColor),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = monoSpace
                    )
                )
                Spacer(modifier = GlanceModifier.width(8.dp))
                Text(
                    app.appName,
                    modifier = GlanceModifier.wrapContentHeight().wrapContentWidth(),
                    style = TextStyle(
                        color = ColorProvider(TitleTextColor),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = monoSpace
                    )
                )
            }
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
