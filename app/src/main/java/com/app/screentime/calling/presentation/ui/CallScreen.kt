package com.app.screentime.calling.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.core.ui.theme.ZonaColors
import com.app.screentime.calling.presentation.CallUiState
import com.telekom.odsystem.R

@Composable
fun CallScreen(
    uiState: CallUiState,
    onToggleMute: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onEndCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val session = uiState.activeSession
    val peerName = session?.peerName ?: "Caller"
    val durationText = session?.durationFormatted ?: "00:00"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ZonaColors.BackgroundStart.getColor(),
                        ZonaColors.BackgroundMid.getColor(),
                        ZonaColors.BackgroundEnd.getColor()
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section: Peer Info and Timers
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(ZonaColors.MediaOverlay.getColor().copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = "Peer Avatar",
                        tint = ZonaColors.TextPrimary.getColor(),
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = peerName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZonaColors.TextPrimary.getColor()
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (uiState.isCallActive) durationText else "Connecting...",
                    fontSize = 18.sp,
                    color = ZonaColors.TextSecondary.getColor()
                )

                if (session != null && uiState.isCallActive) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ZonaColors.SurfaceElevated.getColor(),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "₹%.2f/min  •  Spent: ₹%.2f".format(session.ratePerMin, session.totalCost),
                            fontSize = 14.sp,
                            color = ZonaColors.ActionPrimary.getColor(),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Warning Banner for Low Balance (<30s)
            if (uiState.isLowBalance && uiState.lowBalanceWarning != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ZonaColors.FeedbackDanger.getColor(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painter = painterResource(id = R.drawable.ic_alert_triangle), contentDescription = null, tint = ZonaColors.TextInverse.getColor())
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.lowBalanceWarning,
                            color = ZonaColors.TextInverse.getColor(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Bottom Section: Controls
            CallControls(
                isAudioMuted = session?.isAudioMuted ?: false,
                isSpeakerOn = session?.isSpeakerOn ?: false,
                onToggleMute = onToggleMute,
                onToggleSpeaker = onToggleSpeaker,
                onEndCall = onEndCall
            )
        }
    }
}
