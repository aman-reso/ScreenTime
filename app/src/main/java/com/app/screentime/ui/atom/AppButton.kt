package com.app.screentime.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.screentime.ui.theme.LocalAppColors

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppSecondaryButton(
    modifier: Modifier = Modifier,
    text: String = "Http",
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return
    val gradient = Brush.linearGradient(
        colors = listOf(
            colors.textPrimary.copy(alpha = 0.83f),
            colors.textPrimary.copy(alpha = 0.44f)
        )
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = 17.dp,
                spotColor = colors.success.copy(alpha = 0.28f),
                ambientColor = colors.success.copy(alpha = 0.28f)
            )
            .border(
                width = 1.dp,
                color = colors.textPrimary.copy(alpha = if (enabled) 0.5f else 0.2f),
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                brush = gradient,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        AppText(
            text = text,
            color = colors.textPrimary.copy(alpha = if (enabled) 1f else 0.4f),
            style = AppTextStyle.Body
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppPrimaryButton(
    modifier: Modifier = Modifier,
    text: String = "Hello",
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return
    Box(
        modifier = modifier
            .shadow(
                elevation = 17.dp,
                spotColor = colors.success.copy(alpha = 0.28f),
                ambientColor = colors.success.copy(alpha = 0.28f)
            )
            .background(
                color = if (enabled) colors.success else colors.success.copy(alpha = 0.27f),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        AppText(
            text = text,
            color = colors.textPrimary.copy(alpha = if (enabled) 1f else 0.4f),
            style = AppTextStyle.Body
        )
    }
}
