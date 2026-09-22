package com.app.screentime.calling.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.ZonaColors
import com.telekom.odsystem.R

@Composable
fun CallControls(
    isAudioMuted: Boolean,
    isSpeakerOn: Boolean,
    onToggleMute: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onEndCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mute Button
        IconButton(
            onClick = onToggleMute,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (isAudioMuted) ZonaColors.FeedbackDanger.getColor() else ZonaColors.MediaOverlay.getColor().copy(alpha = 0.2f))
        ) {
            Icon(
                painter = painterResource(id = if (isAudioMuted) R.drawable.ic_mic_off else R.drawable.ic_mic),
                contentDescription = "Toggle Mute",
                tint = ZonaColors.TextPrimary.getColor(),
                modifier = Modifier.size(28.dp)
            )
        }

        // End Call Button
        IconButton(
            onClick = onEndCall,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(ZonaColors.FeedbackDanger.getColor())
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_phone_off),
                contentDescription = "End Call",
                tint = ZonaColors.TextInverse.getColor(),
                modifier = Modifier.size(36.dp)
            )
        }

        // Speaker Button
        IconButton(
            onClick = onToggleSpeaker,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (isSpeakerOn) ZonaColors.ActiveIndicator.getColor() else ZonaColors.MediaOverlay.getColor().copy(alpha = 0.2f))
        ) {
            Icon(
                painter = painterResource(id = if (isSpeakerOn) R.drawable.ic_volume_2 else R.drawable.ic_volume_2),
                contentDescription = "Toggle Speaker",
                tint = if (isSpeakerOn) ZonaColors.TextInverse.getColor() else ZonaColors.TextPrimary.getColor(),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
