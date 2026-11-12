package com.app.screentime.feature.newdesign.call

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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
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
import com.app.screentime.feature.call.components.LiveKitVideoSurface
import io.livekit.android.room.Room
import io.livekit.android.room.track.VideoTrack
import kotlinx.coroutines.delay

/**
 * WinterVideoCallScreen - Figma screen "Winter · Video call" (Node 149:15).
 *
 * Rules:
 * 1. Multiples of 4dp for all padding, margins, spacers, and sizes.
 * 2. Broken down into clean, modular composable functions.
 * 3. Dynamic color scheme tokens (WinterTokens) and typography (FunnelSans).
 * 4. Never hardcode container heights.
 */
@Composable
fun WinterVideoCallScreen(
    modifier: Modifier = Modifier,
    callerName: String = "Ananya Sharma",
    callRateText: String = "₹40 / min · Video call",
    initialDurationSeconds: Int = 0,
    durationSeconds: Int? = null,
    remainingSec: Int = 300,
    isLowBalanceWarning: Boolean = false,
    isMuted: Boolean = false,
    isCameraOff: Boolean = false,
    isFrontCamera: Boolean = true,
    room: Room? = null,
    remoteVideoTrack: VideoTrack? = null,
    localVideoTrack: VideoTrack? = null,
    onMinimizeClick: () -> Unit = {},
    onMoreOptionsClick: () -> Unit = {},
    onToggleMute: () -> Unit = {},
    onFlipCamera: () -> Unit = {},
    onToggleCamera: () -> Unit = {},
    onOpenChatClick: () -> Unit = {},
    onEndCallClick: () -> Unit = {},
    onQuickRechargeClick: () -> Unit = {}
) {
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
    val durationFormatted = "%02d:%02d".format(minutes, seconds)

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WinterTokens.backgroundCanvas)
    ) {
        // 1. Full-screen Video Background with Readability Gradient Overlay (Node 149:16)
        if (room != null && remoteVideoTrack != null) {
            LiveKitVideoSurface(
                room = room,
                videoTrack = remoteVideoTrack,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.user_ananya_profile),
                contentDescription = "Live video from $callerName",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Video readability overlay (Node 149:248)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color(0xC706050A),
                        0.30f to Color(0x0006050A),
                        0.57f to Color(0x0006050A),
                        1.0f to Color(0xF506050A)
                    )
                )
        )

        // 2. Video Call Header (Node 149:17)
        VideoCallHeader(
            callerName = callerName,
            durationFormatted = durationFormatted,
            statusBarPadding = statusBarPadding,
            onMinimizeClick = onMinimizeClick,
            onMoreOptionsClick = onMoreOptionsClick,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // 3. Floating Self Preview PiP (Node 149:18)
        VideoCallSelfPipPreview(
            isCameraOff = isCameraOff,
            isFrontCamera = isFrontCamera,
            room = room,
            localVideoTrack = localVideoTrack,
            statusBarPadding = statusBarPadding,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        // 4. Video Controls Dock (Node 149:19)
        VideoCallControlsDock(
            callRateText = callRateText,
            isMuted = isMuted,
            isCameraOff = isCameraOff,
            navBarPadding = navBarPadding,
            isLowBalanceWarning = isLowBalanceWarning || (remainingSec in 1..60),
            remainingSec = remainingSec,
            onToggleMute = onToggleMute,
            onFlipCamera = onFlipCamera,
            onToggleCamera = onToggleCamera,
            onOpenChatClick = onOpenChatClick,
            onEndCallClick = onEndCallClick,
            onQuickRechargeClick = onQuickRechargeClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * Top video call header with caller info and quality status (Node 149:17).
 */
@Composable
private fun VideoCallHeader(
    callerName: String,
    durationFormatted: String,
    statusBarPadding: Dp,
    onMinimizeClick: () -> Unit,
    onMoreOptionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = statusBarPadding + 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Minimize button (Node 149:249 - 48dp circle, surfaceControl pure black)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(WinterTokens.surfaceControl)
                    .border(1.dp, WinterTokens.borderSubtle, CircleShape)
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

            Spacer(modifier = Modifier.width(16.dp))

            // Caller name and duration left-aligned next to back button
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = callerName,
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = WinterTokens.mediaWhite
                )

                Text(
                    text = "Video call · $durationFormatted",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = WinterTokens.mediaWhite
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // More options button (Node 149:254 - 48dp circle, surfaceControl pure black)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(WinterTokens.surfaceControl)
                    .border(1.dp, WinterTokens.borderSubtle, CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onMoreOptionsClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_call_more_horizontal),
                    contentDescription = "More options",
                    tint = WinterTokens.iconPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Connection Quality Badge (Node 149:259 - 36dp height, rounded 18dp, surfaceCard #222222)
        VideoCallQualityBadge()
    }
}

/**
 * Connection quality indicator badge (Node 149:259).
 */
@Composable
private fun VideoCallQualityBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(WinterTokens.surfaceCard)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(WinterTokens.statusPositive)
            )

            Text(
                text = "Good connection",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = WinterTokens.textPrimary
            )
        }
    }
}

/**
 * Self camera PiP preview window (Node 149:18 - 144x196dp, 2dp borderMuted #AAAAAA).
 */
@Composable
private fun VideoCallSelfPipPreview(
    isCameraOff: Boolean,
    isFrontCamera: Boolean = true,
    room: Room? = null,
    localVideoTrack: VideoTrack? = null,
    statusBarPadding: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(top = statusBarPadding + 120.dp, end = 16.dp)
            .size(width = 144.dp, height = 196.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, WinterTokens.borderMuted, RoundedCornerShape(20.dp))
    ) {
        if (!isCameraOff) {
            if (room != null && localVideoTrack != null) {
                LiveKitVideoSurface(
                    room = room,
                    videoTrack = localVideoTrack,
                    isMirror = isFrontCamera,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.user_self_video_preview),
                    contentDescription = "Self preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WinterTokens.surfaceCard),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Camera Off",
                    fontFamily = FunnelSans,
                    fontSize = 14.sp,
                    color = WinterTokens.textSecondary
                )
            }
        }

        // "You" Badge at the bottom-left inside PiP (Node 149:263)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
                .height(26.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(WinterTokens.surfaceCard)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "You",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = WinterTokens.textPrimary
            )
        }
    }
}

/**
 * Bottom docked video controls matching Figma Node 149:19.
 */
@Composable
private fun VideoCallControlsDock(
    callRateText: String,
    isMuted: Boolean,
    isCameraOff: Boolean,
    navBarPadding: Dp,
    isLowBalanceWarning: Boolean = false,
    remainingSec: Int = 300,
    onToggleMute: () -> Unit,
    onFlipCamera: () -> Unit,
    onToggleCamera: () -> Unit,
    onOpenChatClick: () -> Unit,
    onEndCallClick: () -> Unit,
    onQuickRechargeClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = navBarPadding + 8.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Low balance warning banner if balance is running out
        if (isLowBalanceWarning) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE53935).copy(alpha = 0.85f))
                    .border(1.dp, Color(0xFFFF8A80), RoundedCornerShape(12.dp))
                    .clickable { onQuickRechargeClick() }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "⚠️ Low balance: ${remainingSec}s left",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Text(
                    text = "Recharge Now →",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFFFFD54F)
                )
            }
        }

        // Call Rate Pill (Node 149:265 - 40dp height, rounded 20dp, surfaceCard #222222)
        Box(
            modifier = Modifier
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(WinterTokens.surfaceCard)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = callRateText,
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = WinterTokens.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Control Dock Capsule (Node 149:267 - rounded 28dp, surfaceCard #222222)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(WinterTokens.surfaceCard)
                .padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VideoCallActionItem(
                    iconRes = R.drawable.ic_call_mic,
                    label = "Mute",
                    isActive = isMuted,
                    activeBackground = WinterTokens.mediaWhite,
                    activeTint = Color.Black,
                    onClick = onToggleMute
                )

                VideoCallActionItem(
                    iconRes = R.drawable.ic_call_flip_camera,
                    label = "Flip",
                    isActive = false,
                    onClick = onFlipCamera
                )

                VideoCallActionItem(
                    iconRes = R.drawable.ic_call_video_toggle,
                    label = "Camera",
                    isActive = isCameraOff,
                    activeBackground = WinterTokens.mediaWhite,
                    activeTint = Color.Black,
                    onClick = onToggleCamera
                )

                VideoCallActionItem(
                    iconRes = R.drawable.ic_call_end_phone,
                    label = "End",
                    buttonBackground = WinterTokens.actionDestructive,
                    iconTint = WinterTokens.mediaWhite,
                    onClick = onEndCallClick
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Individual action item inside dock (Node 149:268 - 149:285: 64dp circle, 16sp label).
 */
@Composable
private fun VideoCallActionItem(
    iconRes: Int,
    label: String,
    isActive: Boolean = false,
    buttonBackground: Color = WinterTokens.surfaceControl,
    activeBackground: Color = WinterTokens.mediaWhite,
    iconTint: Color = WinterTokens.iconPrimary,
    activeTint: Color = Color.Black,
    onClick: () -> Unit
) {
    val bg = if (isActive) activeBackground else buttonBackground
    val tint = if (isActive) activeTint else iconTint

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(bg)
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
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            text = label,
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = WinterTokens.textPrimary
        )
    }
}

@Preview(name = "Winter · Video call (Node 149:15)", showBackground = true)
@Composable
private fun WinterVideoCallScreenPreview() {
    Surface(color = WinterTokens.backgroundCanvas) {
        WinterVideoCallScreen()
    }
}
