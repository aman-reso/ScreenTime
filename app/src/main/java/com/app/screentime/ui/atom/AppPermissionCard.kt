package com.app.screentime.ui.atom

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
// Add this import for stringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
// Make sure to import your R file
import com.app.screentime.R
import com.app.screentime.ui.theme.KakaoYellow
import com.app.screentime.ui.theme.lightTextColor

val permissionBlackCardBg = Color(0xFF292929)

// A custom shadow modifier for a more diffused look
fun Modifier.shadow(
    color: Color = permissionBlackCardBg,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp,
) = this.drawBehind {
    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        val spreadPixel = spread.toPx()
        val leftPixel = (0f - spreadPixel) + offsetX.toPx()
        val topPixel = (0f - spreadPixel) + offsetY.toPx()
        val rightPixel = (this.size.width + spreadPixel)
        val bottomPixel = (this.size.height + spreadPixel)

        if (blurRadius != 0.dp) {
            frameworkPaint.maskFilter =
                (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
        }
        frameworkPaint.color = color.toArgb()
        it.drawRoundRect(
            left = leftPixel,
            top = topPixel,
            right = rightPixel,
            bottom = bottomPixel,
            radiusX = borderRadius.toPx(),
            radiusY = borderRadius.toPx(),
            paint
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppPermissionCard(
    modifier: Modifier = Modifier,
    hasUsageStats: Boolean = true,
    hasNotification: Boolean = false,
    hasVpn: Boolean = false,
    onRequestUsageStats: () -> Unit = {},
    onRequestNotification: () -> Unit = {},
    onRequestVpn: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(15.dp))
            .background(permissionBlackCardBg)
            // 2. Add a soft border
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(15.dp)
            )
            .shadow(
                color = Color.Black.copy(alpha = 0.2f),
                borderRadius = 15.dp,
                blurRadius = 15.dp,
                offsetY = 4.dp,
                spread = 2.dp
            )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                modifier = Modifier.size(32.dp),
                contentDescription = stringResource(R.string.content_description_permissions_icon),
                tint = KakaoYellow
            )

            Spacer(modifier = Modifier.width(8.dp))
            Column {
                AppText(
                    text = stringResource(R.string.permissions_required),
                    style = AppTextStyle.SubTitle,
                )
                Spacer(modifier = Modifier.height(4.dp))
                AppText(
                    text = stringResource(R.string.permissions_card_description),
                    color = lightTextColor,
                    style = AppTextStyle.Label
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = lightTextColor
        )

        Spacer(modifier = Modifier.height(24.dp))

        PermissionItem(
            title = stringResource(R.string.permission_usage_stats_title),
            description = stringResource(R.string.permission_usage_stats_description),
            isGranted = hasUsageStats,
            onRequest = onRequestUsageStats
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Notification Permission
        PermissionItem(
            title = stringResource(R.string.permission_notifications_title),
            description = stringResource(R.string.permission_notifications_description),
            isGranted = hasNotification,
            onRequest = onRequestNotification
        )

        Spacer(modifier = Modifier.height(12.dp))

        // VPN Permission
        PermissionItem(
            title = stringResource(R.string.permission_vpn_title),
            description = stringResource(R.string.permission_vpn_description),
            isGranted = hasVpn,
            onRequest = onRequestVpn
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PermissionItem(
    title: String, description: String,
    isGranted: Boolean,
    onRequest: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            AppText(
                text = title,
                style = AppTextStyle.Body,
                fontWeight = FontWeight.Medium,
                color = Color.White // Adjusted for better contrast
            )
            AppText(
                text = description,
                style = AppTextStyle.Label,
                color = lightTextColor
            )
        }

        AppSwitch(
            checked = isGranted,
            onCheckedChange = { onRequest() }
        )
    }
}
