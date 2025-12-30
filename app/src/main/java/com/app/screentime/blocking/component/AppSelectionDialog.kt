package com.app.screentime.blocking.component

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

data class InstalledApp(
    val packageName: String,
    val appName: String,
    val applicationInfo: ApplicationInfo?
)

fun getInstalledApps(context: Context): List<InstalledApp> {
    val pm = context.packageManager

    return try {
        pm.getInstalledApplications(0)
            .asSequence()
            .filter { appInfo ->
                pm.getLaunchIntentForPackage(appInfo.packageName) != null
            }
            .map { appInfo ->
                InstalledApp(
                    packageName = appInfo.packageName,
                    appName = pm.getApplicationLabel(appInfo).toString(),
                    applicationInfo = appInfo
                )
            }
            .sortedBy { it.appName.lowercase() }
            .toList()
    } catch (e: Exception) {
        emptyList()
    }
}



