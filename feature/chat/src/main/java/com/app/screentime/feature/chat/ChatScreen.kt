package com.app.screentime.feature.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.chat.component.ChatInputBar
import com.app.screentime.feature.chat.component.ChatTopBar
import com.app.screentime.feature.chat.component.MessageBubble
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Direct Message Chat Screen (Matching media_1789923373115.png & Figma specifications).
 *
 * Rules:
 * 1. 100% ODS components and ODSLazyColumn.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Compact button sizing.
 * 4. Maximum text size 16sp across the screen with Funnel Sans font.
 * 5. All padding, margins, gaps and radii use `ODSVariables`.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    modelId: String,
    modelName: String,
    modifier: Modifier = Modifier,
    conversationId: String? = null,
    scheme: ODSTheme = zonaODSTheme,
    onBackClick: () -> Unit = {},
    onStartVoiceCall: () -> Unit = {},
    onStartVideoCall: () -> Unit = onStartVoiceCall,
    onOpenProfile: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    val resolvedName = if (modelName.isNotBlank()) modelName else "Friend"

    LaunchedEffect(modelId, conversationId) {
        viewModel.loadChat(partnerId = modelId, conversationId = conversationId)
    }

    val displayMessages = remember(uiState.messages) {
        uiState.messages.filter { it.text.isNotBlank() }.distinctBy { it.id }
    }

    LaunchedEffect(displayMessages.size) {
        if (displayMessages.isNotEmpty()) {
            listState.animateScrollToItem(displayMessages.size)
        }
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            ChatTopBar(
                modelName = resolvedName,
                scheme = scheme,
                avatarUrl = null,
                statusText = "Active now",
                isOnline = true,
                onBackClick = onBackClick,
                onAudioCallClick = onStartVoiceCall,
                onVideoCallClick = onStartVideoCall,
                onProfileClick = onOpenProfile
            )
            if (uiState.isLoading && displayMessages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ODSLoadingSpinner(
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.SMALL)
                    )
                }
            } else {
                ODSLazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    padding = ODSPadding(
                        horizontal = ODSVariables.spacingLayout1,
                        vertical = ODSVariables.spacingComponent4
                    ),
                    gap = ODSVariables.spacingComponent3
                ) {
                    if (displayMessages.isNotEmpty()) {
                        items(
                            count = displayMessages.size,
                            key = { index -> displayMessages[index].id }
                        ) { index ->
                            val msg = displayMessages[index]
                            val isMe = msg.senderId != modelId
                            MessageBubble(
                                message = msg,
                                isMe = isMe,
                                scheme = scheme,
                                avatarUrl = null,
                                onRetry = { viewModel.retrySendMessage(it) }
                            )
                        }
                    } else {
                        item {
                            ODSBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = ODSVariables.spacingLayout4),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSColumn(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    gap = ODSVariables.spacingComponent2
                                ) {
                                    ODSText(
                                        text = "Say hello to $resolvedName! 👋",
                                        style = ODSTextStyles.bodyMBold,
                                        color = scheme.basicText
                                    )
                                    ODSText(
                                        text = "Send a message below to start your conversation.",
                                        style = ODSTextStyles.bodySRegular,
                                        color = scheme.basicTextRecessive,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
            ChatInputBar(
                inputText = uiState.inputText,
                scheme = scheme,
                onInputTextChanged = { viewModel.onInputTextChanged(it) },
                onSendMessage = { viewModel.sendMessage(modelId) },
                onAttachClick = {
                    viewModel.onInputTextChanged("🌴 [photo]")
                    viewModel.sendMessage(modelId)
                }
            )
        }
    }
}
