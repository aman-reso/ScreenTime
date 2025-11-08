package com.app.screentime.ui.atom

import android.content.pm.ApplicationInfo
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap

@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    appInfo: ApplicationInfo?,
    size: Dp = 24.dp,
    isGrayedOut: Boolean = false,
) {
    if (appInfo == null) return
    val useCustomIcon = appInfo.icon == 0

    val backgroundColor = if (useCustomIcon)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    else
        Color.Transparent

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (useCustomIcon) {
            ResolveIcon(
                appInfo = appInfo,
                size = size,
                isGrayedOut = isGrayedOut
            )
        } else {
            AppImageIcon(
                appInfo = appInfo,
                isGrayedOut = isGrayedOut
            )
        }
    }
}

@Composable
private fun ResolveIcon(
    appInfo: ApplicationInfo,
    size: Dp,
    isGrayedOut: Boolean
) {
    val icon = when {
        appInfo.icon == 0 -> Icons.Filled.Help
        else -> Icons.Filled.Android
    }

    Icon(
        imageVector = icon,
        contentDescription = appInfo.packageName,
        modifier = Modifier.size(size),
        tint = if (isGrayedOut) Color.Gray else MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun AppImageIcon(
    appInfo: ApplicationInfo,
    isGrayedOut: Boolean
) {
    val context = LocalContext.current
    val bitmap = remember(appInfo.icon) {
        val drawable = appInfo.loadIcon(context.packageManager)
        drawable?.toBitmap() // <-- convert any drawable to bitmap
    }
    if (bitmap != null) {
        Image(
            painter = BitmapPainter(bitmap.asImageBitmap()),
            contentDescription = appInfo.packageName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = if (isGrayedOut)
                ColorFilter.tint(Color.Gray, BlendMode.Saturation)
            else null
        )
    }

}

