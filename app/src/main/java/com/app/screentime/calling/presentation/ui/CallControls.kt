package com.app.screentime.calling.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
                .background(if (isAudioMuted) Color(0xFFFF5252) else Color(0x33FFFFFF))
        ) {
            Icon(
                imageVector = if (isAudioMuted) Icons.Default.MicOff else Icons.Default.Mic,
                contentDescription = "Toggle Mute",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        // End Call Button
        IconButton(
            onClick = onEndCall,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFE53935))
        ) {
            Icon(
                imageVector = Icons.Default.CallEnd,
                contentDescription = "End Call",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        // Speaker Button
        IconButton(
            onClick = onToggleSpeaker,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (isSpeakerOn) Color(0xFF4CAF50) else Color(0x33FFFFFF))
        ) {
            Icon(
                imageVector = if (isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                contentDescription = "Toggle Speaker",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
