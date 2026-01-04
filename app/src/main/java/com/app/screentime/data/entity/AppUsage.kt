package com.app.screentime.data.entity

import android.content.pm.ApplicationInfo
import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Stable
data class AppUsage(
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val packageName: String?,
    val appName: String?,
    val appScreenTime: Long,
    val mobileDataUsage: Long = 0L,
    val wifiDataUsage: Long = 0L,
    val launchCount: Int = -1,
    val notificationCount: Int = -1
) {
    @Ignore
    var applicationInfo: ApplicationInfo? = null

    @Ignore
    var displayFormatScreenTime: String? = null
}
