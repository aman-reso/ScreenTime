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
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor
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
        else -> "${diff / 86_400_000} days ago"
    }
}

/**
 * Conversation List Item (Matching Figma node-id 10-1225: https://figma.com/design/1TELYpr19qLAv99DexRNpi/Untitled?node-id=10-1225).
 *
 * 100% constructed with Telekom ODS components and Zona tokens.
 */
@Composable
fun ConversationItem(
    index: Int,
    conv: Conversation,
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
            else -> "3 days ago"
        }
    }

    val messageText = if (conv.lastMessage.isNotBlank()) conv.lastMessage else "Say hello! 👋"

    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        gap = 14.dp,
        padding = ODSPadding(
            horizontal = ODSVariables.spacingLayout1,
            vertical = ODSVariables.spacingComponent4
        ),
        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
        border = if (hasUnread) {
            ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
            )
        } else {
            null
        },
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        background = if (hasUnread) {
            listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        } else {
            emptyList()
        }
    ) {
        // ── 1. Circular Avatar (56.dp) ──────────────────────────────────────────
        ODSRow(
            cornerRadius = ODSCorners(all = 28.dp),
            clipContent = true,
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start,
            width = 56.dp,
            height = 56.dp
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

        // ── 2. Content Column: Name, Timestamp, Message, Unread Dot ────────────
        ODSColumn(
            modifier = Modifier.weight(1f),
            gap = 4.dp,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // Top Row: Name and Relative Time
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ODSText(
                    text = conv.modelName,
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextDominant
                )
                ODSText(
                    text = timeText,
                    style = ODSTextStyles.microcopyBold,
                    color = if (hasUnread) scheme.basicTextDominant else scheme.basicTextRecessive
                )
            }

            // Bottom Row: Message snippet and Unread Dot
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent3,
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ODSText(
                    modifier = Modifier.weight(1f),
                    text = messageText,
                    style = if (hasUnread) ODSTextStyles.bodySRegular else ODSTextStyles.bodySRegular,
                    color = if (hasUnread) scheme.basicTextDominant else scheme.basicTextRecessive,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
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
    }
}
