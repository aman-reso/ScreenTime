package com.app.screentime.feature.newdesign.call

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.feature.newdesign.R
import com.app.screentime.feature.newdesign.components.FunnelSans
import com.app.screentime.feature.newdesign.components.WinterTokens
import kotlinx.coroutines.delay

/**
 * WinterAudioCallScreen - Figma screen "Winter · Audio call" (Node 149:11).
 *
 * Rules:
 * 1. Multiples of 4dp for all padding, margins, and sizes.
 * 2. Broken down into clean, modular composable functions.
 * 3. Uses dynamic color scheme tokens and typography.
 * 4. Never hardcode container heights.
 */
@Composable
fun WinterAudioCallScreen(
    modifier: Modifier = Modifier,
    callerName: String = "Ananya Sharma",
    callRateText: String = "₹20 / min · Audio call",
    initialDurationSeconds: Int = 0,
    durationSeconds: Int? = null,
    remainingSec: Int = 300,
    isLowBalanceWarning: Boolean = false,
    isMuted: Boolean = false,
    isSpeakerOn: Boolean = true,
    onMinimizeClick: () -> Unit = {},
    onMoreOptionsClick: () -> Unit = {},
    onToggleMute: () -> Unit = {},
    onToggleSpeaker: () -> Unit = {},
    onToggleVideoClick: () -> Unit = {},
    onEndCallClick: () -> Unit = {},
    onQuickRechargeClick: () -> Unit = {}
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    var internalSeconds by remember { mutableIntStateOf(initialDurationSeconds) }
    LaunchedEffect(durationSeconds == null) {
        if (durationSeconds == null) {
            while (true) {
                delay(1000L)
                internalSeconds++
            }
        }
    }
    val currentSec = durationSeconds ?: internalSeconds
    val minutes = currentSec / 60
    val seconds = currentSec % 60
    val durationFormatted = String.format("%02d:%02d", minutes, seconds)

    val infiniteTransition = rememberInfiniteTransition(label = "waveformPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WinterTokens.surfaceHeader)
    ) {
        // Upper Canvas: Call Header + Caller Portrait
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = statusBarPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AudioCallTopBar(
                onMinimizeClick = onMinimizeClick,
                onMoreOptionsClick = onMoreOptionsClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            AudioCallCallerPortrait(
                callerName = callerName,
                durationText = "Connected · $durationFormatted",
                pulseScale = pulseScale
            )
        }

        // Bottom Controls Sheet
        AudioCallControlsSheet(
            callRateText = callRateText,
            isMuted = isMuted,
            isSpeakerOn = isSpeakerOn,
            isVideoActive = false,
            navBarPadding = navBarPadding,
            isLowBalanceWarning = isLowBalanceWarning || (remainingSec in 1..60),
            remainingSec = remainingSec,
            onToggleMute = onToggleMute,
            onToggleSpeaker = onToggleSpeaker,
            onToggleVideo = onToggleVideoClick,
            onEndCallClick = onEndCallClick,
            onQuickRechargeClick = onQuickRechargeClick
        )
    }
}

/**
 * Top action bar with minimize button, title, and privacy badge.
 */
@Composable
private fun AudioCallTopBar(
    onMinimizeClick: () -> Unit,
    onMoreOptionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(WinterTokens.surfaceControl)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onMinimizeClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_call_minimize_down),
                    contentDescription = "Minimize",
                    tint = WinterTokens.iconPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Audio call",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = WinterTokens.textPrimary
            )

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onMoreOptionsClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_call_more_horizontal),
                    contentDescription = "More",
                    tint = WinterTokens.iconPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_call_shield_check),
                contentDescription = "Secure",
                tint = WinterTokens.iconSecondary,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = "Private connection",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = WinterTokens.textSecondary
            )
        }
    }
}

/**
 * Caller portrait area with halo ring, caller name, and voice waveform.
 */
@Composable
private fun AudioCallCallerPortrait(
    callerName: String,
    durationText: String,
    pulseScale: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(WinterTokens.surfaceControl)
                .border(2.dp, WinterTokens.borderSubtle, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.user_ananya_profile),
                contentDescription = callerName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = callerName,
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            color = WinterTokens.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = durationText,
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = WinterTokens.textSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        AudioActivityWaveform(pulseScale = pulseScale)
    }
}

/**
 * Audio Activity Waveform with animated bars.
 */
@Composable
private fun AudioActivityWaveform(
    pulseScale: Float,
    modifier: Modifier = Modifier
) {
    val baseHeights = listOf(8, 16, 24, 16, 32, 24, 40, 24, 32, 16, 24, 16, 8)

    Row(
        modifier = modifier
            .size(width = 104.dp, height = 40.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        baseHeights.forEachIndexed { index, baseHeight ->
            val multiplier = if (index % 2 == 0) pulseScale else (2.05f - pulseScale)
            val animatedHeight = (baseHeight * multiplier).coerceIn(4f, 40f)

            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = animatedHeight.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(WinterTokens.textAccent)
            )
        }
    }
}

/**
 * Call Controls Sheet with mute, speaker, video toggle, and end call button.
 */
@Composable
private fun AudioCallControlsSheet(
    callRateText: String,
    isMuted: Boolean,
    isSpeakerOn: Boolean,
    isVideoActive: Boolean,
    navBarPadding: Dp,
    isLowBalanceWarning: Boolean = false,
    remainingSec: Int = 300,
    onToggleMute: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onToggleVideo: () -> Unit,
    onEndCallClick: () -> Unit,
    onQuickRechargeClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(WinterTokens.backgroundCanvas)
            .border(1.dp, WinterTokens.borderSubtle, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .padding(top = 24.dp, bottom = 16.dp + navBarPadding, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Low balance warning banner if balance is running out
        if (isLowBalanceWarning) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE53935).copy(alpha = 0.15f))
                    .border(1.dp, Color(0xFFE53935).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .clickable { onQuickRechargeClick() }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "⚠️ Low balance: ${remainingSec}s left",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color(0xFFFF5252)
                )
                Text(
                    text = "Recharge Now →",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = WinterTokens.actionPrimary
                )
            }
        }

        // Rate pill
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .clip(RoundedCornerShape(20.dp))
                .background(WinterTokens.surfaceCard)
                .border(1.dp, WinterTokens.borderSubtle, RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = callRateText,
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = WinterTokens.textPrimary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Controls row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            CallActionButton(
                iconRes = R.drawable.ic_call_mic,
                label = "Mute",
                isActive = isMuted,
                onClick = onToggleMute
            )

            CallActionButton(
                iconRes = R.drawable.ic_call_speaker,
                label = "Speaker",
                isActive = isSpeakerOn,
                onClick = onToggleSpeaker
            )

            CallActionButton(
                iconRes = R.drawable.ic_call_end_phone,
                label = "End Call",
                isActive = false,
                onClick = onEndCallClick
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

    }
}

/**
 * Circular call action button.
 */
@Composable
private fun CallActionButton(
    @androidx.annotation.DrawableRes iconRes: Int,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonBackground by animateColorAsState(
        targetValue = if (isActive) WinterTokens.mediaWhite else WinterTokens.surfaceControl,
        animationSpec = tween(durationMillis = 180),
        label = "callActionBg"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(buttonBackground)
                .border(1.dp, WinterTokens.borderSubtle, CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = if (isActive) Color.Black else WinterTokens.iconPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = label,
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = WinterTokens.textPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun WinterAudioCallScreenPreview() {
    WinterAudioCallScreen()
}
