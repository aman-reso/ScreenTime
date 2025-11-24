package com.app.screentime.blocking.component

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.glassBottomSheetBackground
import com.app.screentime.ui.theme.LocalAppColors

import android.content.pm.ApplicationInfo

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


