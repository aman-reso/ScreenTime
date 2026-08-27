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

    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        gap = 14.dp
    ) {
        // Avatar with Online dot
        Box {
            ODSBox(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape),
                background = listOf(ODSColorModel(hexColor = colorScheme.basicBackgroundSubtle)),
                contentAlignment = Alignment.Center
            ) {
                ODSText(
                    text = conv.modelName.take(1).uppercase(),
                    style = ODSTextStyles.bodyMBold,
                    color = colorScheme.basicText
                )
            }
            if (conv.isOnline) {
                ODSBox(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .align(Alignment.BottomEnd),
                    background = listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard)),
                    border = ODSBorder(
                        width = 2.dp,
                        colorList = listOf(ODSColorModel(scheme.basicBackground))
                    )
                ) {}
            }
        }

        // Name & Last Message
        ODSColumn(
            modifier = Modifier.weight(1f),
            gap = 4.dp
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSText(
                    text = conv.modelName,
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
                ODSText(
                    text = formatTime(conv.lastMessageTime),
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
            }

            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSText(
                    text = conv.lastMessage,
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive,
                    modifier = Modifier.weight(1f)
                )

                if (conv.unreadCount > 0) {
                    ODSBox(
                        background = listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard)),
                        cornerRadius = ODSCorners(all = 10.dp),
                        padding = ODSPadding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        ODSText(
                            text = "${conv.unreadCount}",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicBackground
                        )
                    }
                }
            }
        }
    }
}
