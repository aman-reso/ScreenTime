package com.app.screentime.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.app.screentime.ui.theme.NeutralBlackDark

/**
 * Reusable app card component with default styling
 * Provides a card container with dark background, elevation, and rounded corners
 * 
 * @param modifier Modifier for the card
 * @param backgroundColor Background color of the card
 * @param shape Shape of the card corners
 * @param content Content to display inside the card
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = NeutralBlackDark,
    shape: Shape = RoundedCornerShape(12.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        content()
    }
}

