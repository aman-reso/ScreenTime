package com.app.screentime.feature.newdesign.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.screentime.feature.newdesign.R

/**
 * WinterIncomingCallBanner - Pure Jetpack Compose implementation of Figma "Luma / Incoming call" (Node 51:1168).
 *
 * @param callerName Name of the caller (e.g. "Maya is calling").
 * @param callDetails Call type / cost information (e.g. "Audio · 10 coins/min").
 * @param onAccept Callback when the accept call button is clicked.
 * @param onDecline Callback when the decline call button is clicked.
 * @param avatarUrl Optional image URL for caller avatar.
 * @param modifier Applied to the container.
 */
@Composable
fun WinterIncomingCallBanner(
    callerName: String,
    callDetails: String,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier,
    avatarUrl: String? = null
) {
    val shape = RoundedCornerShape(22.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(WinterTokens.surfaceCard, shape)
            .border(1.dp, WinterTokens.borderSubtle, shape)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Caller Avatar (44dp Circle)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (avatarUrl != null) {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = callerName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(44.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.caller_maya_portrait),
                        contentDescription = callerName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            // Caller Information
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = callerName,
                    color = WinterTokens.textPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = callDetails,
                    color = WinterTokens.textSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Decline Button (36dp circle, dark surface, ic_phone_off)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(WinterTokens.surfaceRaised)
                    .border(1.dp, WinterTokens.borderSubtle, CircleShape)
                    .clickable(
                        role = Role.Button,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDecline
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_phone_off),
                    contentDescription = "Decline Call",
                    tint = WinterTokens.iconPrimary,
                    modifier = Modifier.size(17.dp)
                )
            }

            // Accept Button (36dp circle, white surface, ic_phone)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(WinterTokens.surfaceInverse)
                    .clickable(
                        role = Role.Button,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onAccept
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_phone),
                    contentDescription = "Accept Call",
                    tint = WinterTokens.neutral1000,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WinterIncomingCallBannerPreview() {
    Surface(color = WinterTokens.backgroundCanvas) {
        WinterIncomingCallBanner(
            callerName = "Maya is calling",
            callDetails = "Audio · 10 coins/min",
            onAccept = {},
            onDecline = {}
        )
    }
}
