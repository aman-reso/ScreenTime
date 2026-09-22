package com.app.screentime.calling.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.app.screentime.calling.domain.model.CallState
import com.app.screentime.core.ui.theme.ZonaColors
import com.telekom.odsystem.R

@Composable
fun IncomingCallDialog(
    state: CallState.IncomingRinging,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Dialog(onDismissRequest = onDecline) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ZonaColors.SurfaceElevated.getColor()),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(80.dp).clip(CircleShape).background(ZonaColors.MediaOverlay.getColor().copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = "Caller",
                        tint = ZonaColors.TextPrimary.getColor(),
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.callerName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZonaColors.TextPrimary.getColor()
                )

                Text(
                    text = "Incoming Voice Call • ₹%.2f/min".format(state.ratePerMin),
                    fontSize = 14.sp,
                    color = ZonaColors.TextSecondary.getColor(),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Decline
                    IconButton(
                        onClick = onDecline,
                        modifier = Modifier.size(56.dp).clip(CircleShape).background(ZonaColors.FeedbackDanger.getColor())
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_phone_off), contentDescription = "Decline", tint = ZonaColors.TextInverse.getColor())
                    }

                    // Accept
                    IconButton(
                        onClick = onAccept,
                        modifier = Modifier.size(56.dp).clip(CircleShape).background(ZonaColors.ActiveIndicator.getColor())
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_phone), contentDescription = "Accept", tint = ZonaColors.TextInverse.getColor())
                    }
                }
            }
        }
    }
}
