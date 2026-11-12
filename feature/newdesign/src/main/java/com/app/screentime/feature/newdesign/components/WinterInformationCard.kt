package com.app.screentime.feature.newdesign.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.feature.newdesign.R

/**
 * WinterInformationCard - Pure Jetpack Compose implementation of Figma "Luma / Information" (Node 51:1363).
 *
 * @param text The informational message.
 * @param modifier Applied to container.
 */
@Composable
fun WinterInformationCard(
    text: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(WinterTokens.surfaceCard, shape)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_shield_check),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(18.dp)
            )

            Text(
                text = text,
                color = WinterTokens.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 17.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(name = "WinterInformationCard Preview", showBackground = true)
@Composable
private fun WinterInformationCardPreview() {
    Surface(color = WinterTokens.backgroundCanvas) {
        Box(modifier = Modifier.padding(16.dp)) {
            WinterInformationCard(
                text = "Calls spend coins each minute. Your 240 coins cover up to 24 audio or 12 video minutes."
            )
        }
    }
}

