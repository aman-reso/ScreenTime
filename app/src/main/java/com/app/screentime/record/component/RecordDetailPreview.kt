package com.app.screentime.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.screentime.network.model.AppUsageStatsData
import com.app.screentime.record.model.TimelineListItem
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.ThemeType
import com.app.screentime.ui.theme.Typography
import com.app.screentime.ui.theme.getThemeColors
import com.app.screentime.utils.DateUtils

// Preview data
private val previewTimelineItems = listOf(
    TimelineListItem.HourHeaderItem(hour = 14),
    TimelineListItem.TimelineEventItem(
        stat = AppUsageStatsData(
            packageName = "com.chrome.browser",
            appName = "Chrome",
            duration = 300000L,
            eventTimestamp = DateUtils.formatISO8601(
                DateUtils.now().withHourOfDay(14).withMinuteOfHour(30)
            ),
            eventType = "app_usage"
        ),
        isFirst = true,
        isLast = false
    ),
    TimelineListItem.TimelineEventItem(
        stat = AppUsageStatsData(
            packageName = "com.whatsapp",
            appName = "WhatsApp",
            duration = 180000L,
            eventTimestamp = DateUtils.formatISO8601(
                DateUtils.now().withHourOfDay(14).withMinuteOfHour(45)
            ),
            eventType = "app_usage"
        ),
        isFirst = false,
        isLast = false
    ),
    TimelineListItem.TimelineEventItem(
        stat = AppUsageStatsData(
            packageName = "com.linkedin.android",
            appName = "LinkedIn",
            duration = 240000L,
            eventTimestamp = DateUtils.formatISO8601(
                DateUtils.now().withHourOfDay(14).withMinuteOfHour(50)
            ),
            eventType = "app_usage"
        ),
        isFirst = false,
        isLast = true
    ),
    TimelineListItem.HourHeaderItem(hour = 15),
    TimelineListItem.TimelineEventItem(
        stat = AppUsageStatsData(
            packageName = "com.app.screentime",
            appName = "AppTime",
            duration = 120000L,
            eventTimestamp = DateUtils.formatISO8601(
                DateUtils.now().withHourOfDay(15).withMinuteOfHour(10)
            ),
            eventType = "app_usage"
        ),
        isFirst = true,
        isLast = true
    )
)

@Preview(showBackground = true)
@Composable
fun TimelineTabPreview() {
    RecordDetailPreviewTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    LocalAppColors.current?.background
                        ?: androidx.compose.ui.graphics.Color.White
                )
        ) {
            val previewColors = LocalAppColors.current ?: return@RecordDetailPreviewTheme

            // Preview the timeline UI directly without ViewModel
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                itemsIndexed(
                    items = previewTimelineItems,
                    key = { index, item ->
                        when (item) {
                            is TimelineListItem.HourHeaderItem -> "header-${item.hour}"
                            is TimelineListItem.TimelineEventItem -> "item-${item.stat.packageName}-${item.stat.eventTimestamp}-$index"
                        }
                    }
                ) { listIndex, item ->
                    when (item) {
                        is TimelineListItem.HourHeaderItem -> {
                            if (listIndex > 0) {
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                            HourRangeHeader(hour = item.hour, colors = previewColors)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        is TimelineListItem.TimelineEventItem -> {
                            TimelineItem(
                                stat = item.stat,
                                isFirst = item.isFirst,
                                isLast = item.isLast,
                                colors = previewColors
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecordDetailPreviewTheme(content: @Composable () -> Unit) {
    val previewColors = remember { getThemeColors(ThemeType.CLASSIC_LIGHT) }
    CompositionLocalProvider(
        LocalThemeMode provides false,
        LocalAppColors provides previewColors
    ) {
        MaterialTheme(
            typography = Typography,
            content = content
        )
    }
}

