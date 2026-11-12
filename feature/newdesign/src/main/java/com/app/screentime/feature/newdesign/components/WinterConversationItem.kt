package com.app.screentime.feature.newdesign.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.screentime.feature.newdesign.R

object WinterConversationDefaults {
    val Height = 92.dp
    val AvatarSize = 54.dp
    val UnreadBadgeSize = 20.dp
    val ReadCheckSize = 15.dp

    // Figma Design Tokens (Node 51:1653 "Luma / Conversation preview")
    val TextPrimary: Color @Composable @ReadOnlyComposable get() = WinterTokens.textPrimary
    val TextSecondary: Color @Composable @ReadOnlyComposable get() = WinterTokens.textSecondary
    val AccentPink: Color @Composable @ReadOnlyComposable get() = WinterTokens.actionPrimary
    val TextOnAccent: Color @Composable @ReadOnlyComposable get() = WinterTokens.textOnAccent
    val DividerColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.borderSubtle
    val AvatarPlaceholderBg: Color @Composable @ReadOnlyComposable get() = WinterTokens.surfaceRaised
}

/**
 * WinterConversationItem - Pure Jetpack Compose implementation of Figma "Luma / Conversation preview" (Node 51:1653).
 *
 * @param name Contact name (e.g. "Maya Rose").
 * @param lastMessage Snippet of the last message (e.g. "That sounds lovely. I’m free now ✨").
 * @param time Time or status (e.g. "Now", "12m", "Yesterday").
 * @param avatarUrl Optional image URL for user's profile photo.
 * @param unreadCount Number of unread messages (if > 0, displays the pink badge).
 * @param isDeliveredAndRead If true and unreadCount == 0, shows double checkmark.
 * @param onClick Optional row click listener.
 * @param showDivider Whether to render the bottom border divider.
 */
@Composable
fun WinterConversationItem(
    name: String,
    lastMessage: String,
    time: String,
    modifier: Modifier = Modifier,
    avatarUrl: String? = null,
    unreadCount: Int = 0,
    isDeliveredAndRead: Boolean = false,
    showDivider: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val hasUnread = unreadCount > 0

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(clickableModifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(WinterConversationDefaults.Height)
                .padding(vertical = 19.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Profile Photo
            Box(
                modifier = Modifier
                    .size(WinterConversationDefaults.AvatarSize)
                    .clip(CircleShape)
                    .background(WinterConversationDefaults.AvatarPlaceholderBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (avatarUrl != null) {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(WinterConversationDefaults.AvatarSize)
                    )
                } else {
                    Text(
                        text = name.take(1).uppercase(),
                        color = WinterTokens.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Info Column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                // Top Row: Name & Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        color = WinterConversationDefaults.TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = if (hasUnread) FontWeight.SemiBold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = time,
                        color = if (hasUnread) WinterConversationDefaults.AccentPink else WinterConversationDefaults.TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                // Bottom Row: Last Message & Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lastMessage,
                        color = if (hasUnread) WinterConversationDefaults.TextPrimary else WinterConversationDefaults.TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    // Trailing Indicator
                    if (hasUnread) {
                        Box(
                            modifier = Modifier
                                .size(WinterConversationDefaults.UnreadBadgeSize)
                                .clip(RoundedCornerShape(10.dp))
                                .background(WinterConversationDefaults.AccentPink),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = unreadCount.toString(),
                                color = WinterConversationDefaults.TextOnAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else if (isDeliveredAndRead) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check_check),
                            contentDescription = "Read",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(WinterConversationDefaults.ReadCheckSize)
                        )
                    }
                }
            }
        }

        // Bottom Divider
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.align(Alignment.BottomCenter),
                thickness = 1.dp,
                color = WinterConversationDefaults.DividerColor
            )
        }
    }
}

@Preview(name = "WinterConversationItem Preview", showBackground = true)
@Composable
private fun WinterConversationItemPreview() {
    Surface(color = WinterTokens.backgroundCanvas) {
        Column(modifier = Modifier.padding(16.dp)) {
            WinterConversationItem(
                name = "Maya Rose",
                lastMessage = "That sounds lovely. I’m free now ✨",
                time = "Now",
                unreadCount = 2
            )

            WinterConversationItem(
                name = "Elena Hart",
                lastMessage = "Thanks for the call!",
                time = "2h ago",
                unreadCount = 0,
                isDeliveredAndRead = true,
                showDivider = false
            )
        }
    }
}

