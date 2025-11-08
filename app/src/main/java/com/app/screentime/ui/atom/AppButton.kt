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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppSecondaryButton(
    modifier: Modifier = Modifier,
    text: String = "Http",
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.83f),
            Color.White.copy(alpha = 0.44f)
        )
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = 17.dp,
                spotColor = Color(0x4722B496),
                ambientColor = Color(0x4722B496)
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = if (enabled) 0.5f else 0.2f),
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
            color = Color.White.copy(alpha = if (enabled) 1f else 0.4f),
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
    Box(
        modifier = modifier
            .shadow(
                elevation = 17.dp,
                spotColor = Color(0x4722B496),
                ambientColor = Color(0x4722B496)
            )
            .background(
                color = if (enabled) Color(0xFF22B496) else Color(0x4422B496),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        AppText(
            text = text,
            color = Color.White.copy(alpha = if (enabled) 1f else 0.4f),
            style = AppTextStyle.Body
        )
    }
}
