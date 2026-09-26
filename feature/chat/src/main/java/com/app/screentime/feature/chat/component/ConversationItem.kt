package com.app.screentime.feature.chat.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.Conversation
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

private fun formatRelativeTime(epochMs: Long): String {
    val diff = System.currentTimeMillis() - epochMs
    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000}m ago"
        diff < 86_400_000 -> "${diff / 3_600_000}h ago"
        diff < 172_800_000 -> "Yesterday"
        else -> "${diff / 86_400_000}d ago"
    }
}

/**
 * Conversation List Item (Matching Figma RADD node-id 25-251).
 * 100% constructed with Telekom ODS components and Zona tokens.
 */
@Composable
fun ConversationItem(
    index: Int,
    conv: Conversation,
    showDivider: Boolean = true,
    scheme: ODSTheme = zonaODSTheme,
    onClick: () -> Unit
) {
    val hasUnread = conv.unreadCount > 0 || index == 0

    val timeText = if (conv.lastMessageTime > 0) {
        formatRelativeTime(conv.lastMessageTime)
    } else {
        when (index % 4) {
            0 -> "2m ago"
            1 -> "3h ago"
            2 -> "Yesterday"
            else -> "3d ago"
        }
    }

    val messageText = if (conv.lastMessage.isNotBlank()) conv.lastMessage else "Say hello! 👋"

    ODSColumn(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        gap = 12.dp,
        padding = ODSPadding(
            horizontal = ODSVariables.spacingComponent3
        ),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Content Row: Avatar (48.dp) + Name & Message
            ODSRow(
                modifier = Modifier.weight(1f),
                gap = 12.dp,
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // 48x48 Circular Avatar
                ODSColumn(
                    cornerRadius = ODSCorners(all = 24.dp),
                    clipContent = true,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                    width = 48.dp,
                    height = 48.dp
                ) {
                    if (conv.modelAvatarUrl.isNotBlank()) {
                        ODSImage(
                            modifier = Modifier.fillMaxSize(),
                            imageModel = ODSImageModel(
                                url = conv.modelAvatarUrl,
                                contentDescription = conv.modelName
                            ),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        ODSBox(
                            modifier = Modifier.fillMaxSize(),
                            background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = conv.modelName.take(1).uppercase(),
                                style = ODSTextStyles.bodySBold,
                                color = scheme.basicAccent
                            )
                        }
                    }
                }

                // Name & Message Column
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = 4.dp,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    ODSText(
                        text = conv.modelName,
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = messageText,
                        style = ODSTextStyles.bodySRegular,
                        color = if (hasUnread) scheme.basicText else scheme.basicTextRecessive,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }

            // Right Column: Time & Unread Indicator
            ODSColumn(
                gap = 6.dp,
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top,
                width = 60.dp
            ) {
                ODSText(
                    text = timeText,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
                if (hasUnread) {
                    ODSBox(
                        modifier = Modifier.size(8.dp),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                        cornerRadius = ODSCorners(all = 4.dp)
                    )
                }
            }
        }

        // Bottom Divider Line
        if (showDivider) {
            ODSDivider(
                scheme = scheme,
                props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL)
            )
        }
    }
}
