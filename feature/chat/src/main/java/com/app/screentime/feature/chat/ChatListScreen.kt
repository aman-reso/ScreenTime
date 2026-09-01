package com.app.screentime.feature.chat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

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
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSText(
                    text = "Conversations",
                    style = ODSTextStyles.bodyMBold,
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
                        ODSText(
                            text = "💬",
                            style = ODSTextStyles.bodyLBold,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = "No active chats",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = "Start chatting with creators from Discover!",
                            style = ODSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
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
