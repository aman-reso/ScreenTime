package com.app.screentime.landing.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors
import java.util.Calendar

@Preview(showBackground = true)
@Composable
fun GreetingUi(
    username: String? = null,
    onLeaderboardClick: (() -> Unit)? = null
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            AppText(text = getGreetingBasedOnTime())
            Spacer(modifier = Modifier.height(4.dp))
            AppText(
                text = username ?: "User",
                style = AppTextStyle.SubTitle
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        colors?.success?.let { successColor ->
            // Animated pulsing effect
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale"
            )
            
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = CircleShape,
                        spotColor = successColor.copy(alpha = 0.4f)
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                successColor.copy(alpha = 0.3f),
                                successColor.copy(alpha = 0.15f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .clickable { onLeaderboardClick?.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Leaderboard",
                    modifier = Modifier
                        .size(22.dp)
                        .scale(scale),
                    tint = successColor
                )
            }
        }
    }
}

@Composable
fun getGreetingBasedOnTime(): String {
    val hour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }

    return when (hour) {
        in 5..11 -> stringResource(R.string.good_morning)
        in 12..16 -> stringResource(R.string.good_afternoon)
        in 17..20 -> stringResource(R.string.good_evening)
        else -> stringResource(R.string.good_night)
    }
}
