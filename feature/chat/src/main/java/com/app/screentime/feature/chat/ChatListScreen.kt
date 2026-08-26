package com.app.screentime.feature.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.Conversation
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.*

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
fun ChatListScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    viewModel: ChatListViewModel = hiltViewModel(),
    onNavigateToChat: (String, String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()
    val conversations = uiState.conversations

    ODSColumn(
        modifier = modifier
            .fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        // Top Header
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PompiereTitle(
                text = "Conversations",
                scheme = scheme,
                style = ODSTextStyles.pompiereTitleL
            )
        }

        if (conversations.isEmpty() && !uiState.isLoading) {
            ODSBox(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ODSColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    gap = 12.dp
                ) {
                    ODSText(
                        text = "💬",
                        style = ODSTextStyles.pompiereDisplayL,
                        color = scheme.basicText
                    )
                    PompiereTitle(
                        text = "No chats yet",
                        scheme = scheme,
                        style = ODSTextStyles.pompiereTitleM
                    )
                    ODSText(
                        text = "Discover models and start a conversation!",
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // 1. Online Active Stories / Avatars Row
                if (conversations.isNotEmpty()) {
                    item(key = "active_stories") {
                        ODSColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            gap = 8.dp
                        ) {
                            ODSText(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                text = "Active Now",
                                style = ODSTextStyles.microcopyBold,
                                color = scheme.basicTextRecessive
                            )
                            androidx.compose.foundation.lazy.LazyRow(
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                itemsIndexed(conversations) { index, conv ->
                                    val avScheme = avatarSchemes[index % avatarSchemes.size]
                                    ODSColumn(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        gap = 6.dp,
                                        modifier = Modifier.clickable { onNavigateToChat(conv.modelId, conv.modelName) }
                                    ) {
                                        ODSBox(
                                            modifier = Modifier.size(62.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            // Outer Border Ring
                                            ODSBox(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(CircleShape),
                                                background = listOf(ODSColorModel(hexColor = if (conv.isOnline) scheme.basicAccent else scheme.basicStrokeSubtle)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                ODSBox(
                                                    modifier = Modifier
                                                        .size(56.dp)
                                                        .clip(CircleShape),
                                                    background = listOf(ODSColorModel(hexColor = avScheme.basicBackgroundSubtle)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    ODSText(
                                                        text = conv.modelName.firstOrNull()?.toString() ?: "M",
                                                        style = ODSTextStyles.pompiereTitleM,
                                                        color = scheme.basicText
                                                    )
                                                }
                                            }

                                            // Online indicator dot
                                            if (conv.isOnline) {
                                                ODSBox(
                                                    modifier = Modifier
                                                        .size(14.dp)
                                                        .clip(CircleShape)
                                                        .align(Alignment.BottomEnd),
                                                    background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    ODSBox(
                                                        modifier = Modifier
                                                            .size(10.dp)
                                                            .clip(CircleShape),
                                                        background = listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard))
                                                    ) {}
                                                }
                                            }
                                        }

                                        ODSText(
                                            text = conv.modelName,
                                            style = ODSTextStyles.microcopyRegular,
                                            color = scheme.basicText
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }

                    // 2. Chat list items
                    itemsIndexed(conversations, key = { _, it -> it.id }) { index, conv ->
                        ODSBox(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                        ) {
                            ConversationCard(
                                conversation = conv,
                                scheme = scheme,
                                avatarScheme = avatarSchemes[index % avatarSchemes.size],
                                onClick = { onNavigateToChat(conv.modelId, conv.modelName) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationCard(
    conversation: Conversation,
    scheme: ODSTheme,
    avatarScheme: ODSTheme,
    onClick: () -> Unit
) {
    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 16.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        padding = ODSPadding(all = 14.dp)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            gap = 14.dp
        ) {
            // Avatar (60dp x 60dp width & height) with online indicator
            ODSBox(
                modifier = Modifier.size(60.dp),
                contentAlignment = Alignment.Center
            ) {
                ODSBox(
                    modifier = Modifier
                        .size(width = 60.dp, height = 60.dp)
                        .clip(CircleShape),
                    background = listOf(ODSColorModel(hexColor = avatarScheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = conversation.modelName.firstOrNull()?.toString() ?: "M",
                        style = ODSTextStyles.pompiereTitleM,
                        color = scheme.basicText
                    )
                }

                if (conversation.isOnline) {
                    ODSBox(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .align(Alignment.BottomEnd),
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSBox(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape),
                            background = listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard))
                        ) {}
                    }
                }
            }

            // Chat info
            ODSColumn(
                modifier = Modifier.weight(1f),
                gap = 5.dp
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSText(
                        text = conversation.modelName,
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicText
                    )
                    ODSText(
                        text = formatTime(conversation.lastMessageTime),
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
                        modifier = Modifier.weight(1f, fill = false),
                        text = conversation.lastMessage,
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive,
                        maxLines = 1
                    )
                    if (conversation.unreadCount > 0) {
                        ODSBox(
                            modifier = Modifier.size(20.dp),
                            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                            cornerRadius = ODSCorners(all = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = conversation.unreadCount.toString(),
                                style = ODSTextStyles.microcopyBold,
                                color = scheme.basicTextOnAccent
                            )
                        }
                    }
                }
            }
        }
    }
}
