package com.app.screentime.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
                shape = androidx.compose.material3.MaterialTheme.shapes.medium
            )
            .background(
                brush = gradient,
                shape = androidx.compose.material3.MaterialTheme.shapes.medium
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

/**
 * Primary button component matching the consent screen style
 * - Solid purple background (#6750A4)
 * - White text
 * - Rounded corners (28dp)
 * - Supports loading state
 * - Default height: 56dp
 */
@Composable
fun AppPrimaryButton(
    modifier: Modifier = Modifier,
    text: String = "Button",
    enabled: Boolean = true,
    isLoading: Boolean = false,
    loadingText: String? = null,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6750A4), // Solid purple
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFE0E0E0),
            disabledContentColor = Color(0xFF9E9E9E)
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
            if (loadingText != null) {
                Spacer(modifier = Modifier.width(8.dp))
                AppText(
                    text = loadingText,
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        } else {
            AppText(
                text = text,
                style = AppTextStyle.Body,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
