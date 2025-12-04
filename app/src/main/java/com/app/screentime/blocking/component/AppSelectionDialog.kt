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
    val packageManager = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)

    return try {
        packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            .mapNotNull { resolveInfo ->
                val packageName = resolveInfo.activityInfo?.packageName ?: return@mapNotNull null
                val appName = try {
                    resolveInfo.loadLabel(packageManager).toString()
                } catch (e: Exception) {
                    packageName
                }
                val appInfo = try {
                    packageManager.getApplicationInfo(packageName, 0)
                } catch (e: Exception) {
                    null
                }
                InstalledApp(packageName, appName, appInfo)
            }
            .sortedBy { it.appName }
    } catch (e: Exception) {
        emptyList()
    }
}


