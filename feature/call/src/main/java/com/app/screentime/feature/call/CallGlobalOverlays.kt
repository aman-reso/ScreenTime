package com.app.screentime.feature.call

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.cheddarSecondaryScheme

/**
 * Global incoming call popup/overlay that appears on ANY screen when an incoming call arrives.
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
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.65f))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        ODSBox(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .shadow(24.dp, RoundedCornerShape(28.dp)),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = 28.dp),
            padding = ODSPadding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 16.dp
            ) {
                // Pulsing Ring & Avatar
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(110.dp)
                ) {
                    // Outer ripple
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(
                                scheme.functionalSuccessStandard.getColor().copy(alpha = ringAlpha)
                            )
                    )
                    // Inner glowing circle
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        scheme.functionalSuccessStandard.getColor(),
                                        scheme.functionalSuccessStandard.getColor().copy(alpha = 0.8f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(imageVector = Icons.Filled.Call),
                            tint = Color.White
                        )
                    }
                }

                // Caller Information
                ODSColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    gap = 4.dp
                ) {
                    ODSText(
                        text = "INCOMING VOICE CALL",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicAccent
                    )
                    ODSText(
                        text = callState.remoteUserName.ifBlank { "Incoming Call" },
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                    ODSText(
                        text = if (isModel) {
                            "+₹${callState.ratePerMin.toInt()}/min (Earnings)"
                        } else {
                            "₹${callState.ratePerMin.toInt()}/min"
                        },
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Action Buttons Row (Decline vs Accept)
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Decline Button
                    ODSColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        gap = 6.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(scheme.functionalDestructiveStandard.getColor())
                                .clickable { onDecline() },
                            contentAlignment = Alignment.Center
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(imageVector = Icons.Filled.CallEnd),
                                tint = Color.White
                            )
                        }
                        ODSText(
                            text = "Decline",
                            style = ODSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    // Accept Button
                    ODSColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        gap = 6.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(scheme.functionalSuccessStandard.getColor())
                                .clickable { onAccept() },
                            contentAlignment = Alignment.Center
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(imageVector = Icons.Filled.Call),
                                tint = Color.White
                            )
                        }
                        ODSText(
                            text = "Accept",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.functionalSuccessStandard
                        )
                    }
                }
            }
        }
    }
}

/**
 * Sleek floating active call banner docked at the top of the screen on ANY tab.
 * Shows live duration timer, remote name, mute toggle, and hang up button.
 */
@Composable
fun ActiveCallGlobalBanner(
    callState: CallUiState,
    scheme: ODSTheme = neutralScheme,
    onExpand: () -> Unit,
    onEndCall: () -> Unit,
    onToggleMute: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "liveDot")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )

    val minutes = callState.durationSec / 60
    val seconds = callState.durationSec % 60
    val formattedDuration = "%02d:%02d".format(minutes, seconds)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .clickable { onExpand() },
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = 20.dp),
            padding = ODSPadding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Live Call Indicator & Remote Username & Duration
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 10.dp,
                    modifier = Modifier.weight(1f)
                ) {
                    // Green Live Dot
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(scheme.functionalSuccessStandard.getColor().copy(alpha = dotAlpha))
                    )

                    ODSColumn(gap = 2.dp) {
                        ODSRow(verticalAlignment = Alignment.CenterVertically, gap = 6.dp) {
                            ODSText(
                                text = callState.remoteUserName.ifBlank { "Ongoing Call" },
                                style = ODSTextStyles.bodyMBold,
                                color = scheme.basicText
                            )
                        }
                        ODSRow(verticalAlignment = Alignment.CenterVertically, gap = 6.dp) {
                            ODSText(
                                text = "In Call · $formattedDuration",
                                style = ODSTextStyles.microcopyBold,
                                color = scheme.functionalSuccessStandard
                            )
                        }
                    }
                }

                // Right: Quick Controls (Mute + Hang Up)
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 8.dp
                ) {
                    // Mute Button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (callState.isMuted) cheddarSecondaryScheme.basicBackgroundSubtle.getColor()
                                else scheme.basicBackground.getColor()
                            )
                            .clickable { onToggleMute() },
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = if (callState.isMuted) Icons.Filled.MicOff else Icons.Outlined.Mic
                            ),
                            tint = if (callState.isMuted) scheme.basicAccent.getColor() else scheme.basicText.getColor()
                        )
                    }

                    // Hang Up Button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(scheme.functionalDestructiveStandard.getColor())
                            .clickable { onEndCall() },
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(imageVector = Icons.Filled.CallEnd),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
