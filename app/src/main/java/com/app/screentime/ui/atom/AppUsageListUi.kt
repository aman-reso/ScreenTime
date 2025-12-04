package com.app.screentime.ui.atom

import android.content.pm.ApplicationInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.record.repository.toReadableDataSize
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

fun LazyListScope.appUsageListUi(
    appUsageList: List<AppUsage>,
    scheme: ODSTheme = neutralScheme,
    onClick: (AppUsage) -> Unit = {}
) {
    if (appUsageList.isEmpty()) {
        return
    }
    itemsIndexed(
        items = appUsageList,
        key = { _, appUsage -> appUsage.packageName ?: appUsage.id.toString() },
        contentType = { _, _ -> "app_usage_item" }
    ) { index, appUsage ->
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
            padding = ODSPadding(horizontal = DSVariables.spacingComponent4)
        ) {
            AppUsageItem(
                appUsage = appUsage,
                scheme = scheme,
                onClick = { onClick(appUsage) }
            )
        }
    }
}

@Composable
private fun AppUsageItem(
    appUsage: AppUsage,
    scheme: ODSTheme,
    onClick: () -> Unit
) {
    // Get app icon bitmap
    val appIconBitmap = AppImageIcon(appInfo = appUsage.applicationInfo)
    // Build description text
    // Determine variant and image/icon
    val (variant, image, icon) = if (appIconBitmap != null && appUsage.applicationInfo?.icon != 0) {
        Triple(
            ODSListRowStandardVariant.IMAGE,
            second = AppImageIcon(appUsage.applicationInfo),
            null
        )
    } else {
        val fallbackIcon = when {
            appUsage.applicationInfo == null -> Icons.Filled.Help
            appUsage.applicationInfo?.icon == 0 -> Icons.Filled.Help
            else -> Icons.Filled.Android
        }
        Triple(
            ODSListRowStandardVariant.ICON,
            null,
            ODSIconModel(
                imageVector = fallbackIcon,
                contentDescription = appUsage.appName
            )
        )
    }

    ODSListRowStandard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        scheme = scheme,
        props = ODSListRowStandardProps(
            variant = variant,
            descriptionTitle = appUsage.displayFormatScreenTime
                ?: formatDuration(appUsage.appScreenTime),
            labelText = appUsage.appName ?: appUsage.packageName ?: "Unknown App",
            descriptionText = if ((appUsage.wifiDataUsage + appUsage.mobileDataUsage) > 0) {
                appUsage.wifiDataUsage.plus(appUsage.mobileDataUsage).toReadableDataSize()
            } else {
                "0 B"
            },
            image = image,
            icon = icon,
            showDescriptionTitle = true
        )
    )
}

@Composable
fun AppImageIcon(
    appInfo: ApplicationInfo?
): ODSImageModel? {
    if (appInfo == null) {
        return null
    }
    val context = LocalContext.current
    return remember(appInfo.icon) {
        val drawable = appInfo.loadIcon(context.packageManager)
        drawable?.toBitmap()
    }?.let {
        ODSImageModel(bitmap = it)
    }
}

