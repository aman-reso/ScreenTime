package com.app.screentime.feature.call

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Global incoming call overlay styled as an iOS Dynamic Island / Call Banner capsule at the top of the screen.
 * Displays caller avatar, call type / rate, caller name, and prominent red (decline) and green (accept) circular buttons.
 */
@Composable
fun IncomingCallGlobalOverlay(
    modifier: Modifier = Modifier,
    callState: CallUiState,
    isModel: Boolean,
    scheme: ODSTheme = neutralScheme,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
) {
    val callTypeLabel = buildString {
        append(if (callState.callType == CallType.VIDEO) "Winter Video" else "Winter Audio")
        if (isModel && callState.ratePerMin > 0.0) {
            append(" • 🪙 ${callState.ratePerMin.toInt()}/m")
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(durationMillis = 250)
            ) + fadeOut(animationSpec = tween(durationMillis = 250))
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(40.dp),
                        spotColor = Color.Black.copy(alpha = 0.6f)
                    )
                    .clip(RoundedCornerShape(40.dp))
                    .border(
                        BorderStroke(1.dp, Color(0xFF2C2C2E)),
                        shape = RoundedCornerShape(40.dp)
                    ),
                shape = RoundedCornerShape(40.dp),
                color = Color.Black
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ── Caller Avatar ────────────────────────────────────
                    if (!callState.remoteUserAvatar.isNullOrBlank()) {
                        AsyncImage(
                            model = callState.remoteUserAvatar,
                            contentDescription = callState.remoteUserName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2C2C2E))
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2C2C2E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = callState.remoteUserName.trim().take(1).uppercase().ifBlank { "?" },
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // ── Call Info (Type & Caller Name) ───────────────────
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = callTypeLabel,
                            color = Color(0xFF8E8E93),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = callState.remoteUserName.ifBlank { "Incoming Call" },
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // ── Action Buttons (Decline & Accept) ────────────────
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Decline Button (Red Circle with hang-up icon)
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF3B30))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onDecline
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_call_decline_dynamic),
                                contentDescription = "Decline Call",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Accept Button (Green Circle with answer icon)
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF34C759))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onAccept
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_call_answer_dynamic),
                                contentDescription = "Accept Call",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
