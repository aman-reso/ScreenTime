package com.app.screentime.ui.atom

import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Help
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.record.repository.toReadableDataSize
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.link.ODSLink
import com.telekom.odsystem.atoms.link.ODSLinkAlignment
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.link.ODSLinkType
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun LazyListScope.appUsageListUi(
    appUsageList: List<AppUsage>,
    scheme: ODSTheme = neutralScheme,
    onClick: (AppUsage) -> Unit = {},
    showExpandCollapse: Boolean = true,
    initialItemCount: Int = 10,
    isExpanded: Boolean = false,
    onExpandCollapseToggle: (() -> Unit)? = null
) {
    if (appUsageList.isEmpty()) {
        return
    }

    val displayList =
        if (showExpandCollapse && !isExpanded && appUsageList.size > initialItemCount) {
            appUsageList.take(initialItemCount)
        } else {
            appUsageList
        }

    itemsIndexed(
        items = displayList,
        key = { _, appUsage -> appUsage.packageName ?: appUsage.id.toString() },
        contentType = { _, _ -> "app_usage_item" }
    ) { index, appUsage ->
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
            padding = ODSPadding(
                horizontal = DSVariables.spacingComponent4,
                vertical = DSVariables.spacingComponent3
            )
        ) {
            AppUsageItem(
                appUsage = appUsage,
                scheme = scheme,
                onClick = { onClick(appUsage) }
            )
        }
    }

    if (showExpandCollapse && appUsageList.size > initialItemCount && onExpandCollapseToggle != null) {
        item {
            ODSBox(
                modifier = Modifier.fillMaxWidth(),
                padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent4
                ),
                contentAlignment = Alignment.BottomEnd
            ) {
                ODSLink(
                    modifier = Modifier.wrapContentWidth(),
                    scheme = scheme,
                    props = ODSLinkProps(
                        type = ODSLinkType.SECONDARY,
                        alignment = ODSLinkAlignment.RIGHT,
                        label = if (isExpanded) "Collapse All" else "Show All",
                    ),
                    onClick = onExpandCollapseToggle
                )
            }
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

    // Pre-compute formatted strings to use as keys for remember
    val screenTimeText = appUsage.displayFormatScreenTime
        ?: formatDuration(appUsage.appScreenTime)
    val appNameText = appUsage.appName ?: appUsage.packageName ?: "Unknown App"
    val dataUsageText = if ((appUsage.wifiDataUsage + appUsage.mobileDataUsage) > 0) {
        appUsage.wifiDataUsage.plus(appUsage.mobileDataUsage).toReadableDataSize()
    } else {
        "0 B"
    }

    // Determine variant and image/icon - remember to avoid recreation
    val (variant, image, icon) = remember(
        appIconBitmap,
        appUsage.applicationInfo?.icon,
        appUsage.appName
    ) {
        if (appIconBitmap != null && appUsage.applicationInfo?.icon != 0) {
            Triple(
                ODSListRowStandardVariant.IMAGE,
                appIconBitmap,
                null
            )
        } else {
            val fallbackIcon = when {
                appUsage.applicationInfo == null -> Icons.AutoMirrored.Filled.Help
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
    }

    ODSListRowStandard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        scheme = scheme,
        props = ODSListRowStandardProps(
            variant = variant,
            descriptionTitle = screenTimeText,
            labelText = appNameText,
            descriptionText = dataUsageText,
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
    if (appInfo == null) return null

    val context = LocalContext.current
    val packageName = appInfo.packageName

    val bitmapState = produceState<Bitmap?>(initialValue = null, key1 = packageName) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                val drawable = appInfo.loadIcon(context.packageManager)
                drawable?.toBitmap()
            }.getOrNull()
        }
    }

    return bitmapState.value?.let {
        ODSImageModel(bitmap = it)
    }
}


//@Composable
//fun AppImageIcon(
//    appInfo: ApplicationInfo?
//): ODSImageModel? {
//    if (appInfo == null) {
//        return null
//    }
//    val context = LocalContext.current
//    return remember(appInfo.icon) {
//        val drawable = appInfo.loadIcon(context.packageManager)
//        drawable?.toBitmap()
//    }?.let {
//        ODSImageModel(bitmap = it)
//    }
//}

