package com.app.screentime.feature.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.Conversation
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.cheddarSecondaryScheme
import com.telekom.odsystem.tokens.tokens.hummingbirdSecondaryScheme
import com.telekom.odsystem.tokens.tokens.macawSecondaryScheme
import com.telekom.odsystem.tokens.tokens.orchidSecondaryScheme

private val avatarSchemes = listOf(
    orchidSecondaryScheme,
    cheddarSecondaryScheme,
    hummingbirdSecondaryScheme,
    macawSecondaryScheme
)

private fun formatTime(epochMs: Long): String {
    val diff = System.currentTimeMillis() - epochMs
    return when {
        diff < 60_000 -> "now"
        diff < 3_600_000 -> "${diff / 60_000}m"
        diff < 86_400_000 -> "${diff / 3_600_000}h"
        else -> "${diff / 86_400_000}d"
    }
}

@Composable
fun ConversationItem(
    index: Int,
    conv: Conversation,
    scheme: ODSTheme,
    onClick: () -> Unit
) {
    val colorScheme = avatarSchemes[index % avatarSchemes.size]

    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 22.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        padding = ODSPadding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            gap = 12.dp
        ) {
            // 1. Small Circular Avatar (40.dp)
            Box {
                ODSBox(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    background = listOf(ODSColorModel(hexColor = colorScheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = conv.modelName.take(1).uppercase(),
                        style = ODSTextStyles.bodySBold,
                        color = colorScheme.basicText
                    )
                }
                if (conv.isOnline) {
                    ODSBox(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .align(Alignment.BottomEnd),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)), // Lime dot
                        border = ODSBorder(
                            width = 1.5.dp,
                            colorList = listOf(ODSColorModel(scheme.basicBackgroundCard))
                        )
                    ) {}
                }
            }

            // 2. Name & Message Info
            ODSColumn(
                modifier = Modifier.weight(1f),
                gap = 4.dp
            ) {
                ODSText(
                    text = conv.modelName,
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )

                val messageText = if (conv.lastMessage.isNotBlank()) conv.lastMessage else "Say hi!"
                val isSayHi = conv.lastMessage.isBlank() || conv.lastMessage.equals("Say hi!", ignoreCase = true)
                ODSText(
                    text = messageText,
                    style = ODSTextStyles.bodySRegular,
                    color = if (isSayHi) scheme.basicAccent else scheme.basicTextRecessive
                )
            }

            // 3. Right Status Badge (Unread count or Online indicator)
            ODSColumn(
                horizontalAlignment = Alignment.End,
                gap = 6.dp
            ) {
                if (conv.unreadCount > 0) {
                    ODSBox(
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)), // Cyber Lime pill
                        cornerRadius = ODSCorners(all = 12.dp),
                        padding = ODSPadding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        ODSText(
                            text = "${conv.unreadCount}",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicTextOnAccent
                        )
                    }
                } else if (conv.isOnline) {
                    ODSBox(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                    ) {}
                }

                if (conv.lastMessageTime > 0) {
                    ODSText(
                        text = formatTime(conv.lastMessageTime),
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        }
    }
}
