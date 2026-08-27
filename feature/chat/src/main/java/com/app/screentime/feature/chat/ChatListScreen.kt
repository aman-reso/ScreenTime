package com.app.screentime.feature.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    viewModel: ChatListViewModel = hiltViewModel(),
    onNavigateToChat: (String, String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()
    val conversations = uiState.conversations

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.loadConversations()
    }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.loadConversations() },
        modifier = modifier.fillMaxSize()
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize(),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        ) {
            // Top Bar
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PompiereTitle(
                    text = "Conversations",
                    scheme = scheme,
                    style = ODSTextStyles.pompiereTitleL
                )
            }

            // 24-Hour Ephemeral Notice Banner
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                background = listOf(ODSColorModel(hexColor = orchidSecondaryScheme.basicBackgroundSubtle)),
                cornerRadius = ODSCorners(all = 12.dp),
                padding = ODSPadding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                ODSText(
                    text = "🔒 Ephemeral Chat: Messages disappear automatically after 24 hours.",
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicText
                )
            }

            if (conversations.isEmpty() && !uiState.isLoading) {
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ODSColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        gap = 8.dp
                    ) {
                        ODSText(text = "💬", style = ODSTextStyles.pompiereDisplayL, color = scheme.basicText)
                        PompiereTitle(text = "No active chats", scheme = scheme, style = ODSTextStyles.pompiereTitleM)
                        ODSText(text = "Start chatting with models from Discover!", style = ODSTextStyles.bodySRegular, color = scheme.basicTextRecessive)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(conversations, key = { _, conv -> conv.id }) { index, conv ->
                        ConversationItem(
                            index = index,
                            conv = conv,
                            scheme = scheme,
                            onClick = { onNavigateToChat(conv.modelId, conv.modelName) }
                        )
                    }
                }
            }
        }
    }
}
