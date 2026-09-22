package com.app.screentime.feature.chat

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.chat.component.ChatInputBar
import com.app.screentime.feature.chat.component.ChatTopBar
import com.app.screentime.feature.chat.component.MessageBubble
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

private val defaultModelPortraits = listOf(
    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80",
    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=800&q=80",
    "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=800&q=80",
    "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=800&q=80"
)

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
    scheme: ODSTheme = zonaODSTheme,
    onBackClick: () -> Unit = {},
    onStartVoiceCall: () -> Unit = {},
    onStartVideoCall: () -> Unit = onStartVoiceCall,
    onOpenProfile: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val isImeVisible = WindowInsets.isImeVisible

    val resolvedName = if (modelName.isNotBlank()) modelName else "Jessica Maple"

    val modelPortraitUrl = remember(modelId) {
        val hash = (modelId.hashCode() and 0x7FFFFFFF)
        defaultModelPortraits[hash % defaultModelPortraits.size]
    }

    LaunchedEffect(modelId) {
        viewModel.loadChat(modelId)
    }

    // Default conversation matching Figma mockup (Alex & Jessica Maui conversation)
    val displayMessages = remember(uiState.messages) {
        if (uiState.messages.isNotEmpty()) {
            uiState.messages.filter { it.text.isNotBlank() }.distinctBy { it.id }
        } else {
            listOf(
                ChatMessage(
                    id = "msg_1",
                    senderId = modelId,
                    receiverId = "me",
                    text = "Hey Alex! I really loved your latest travel stories. Where was that beach? 🌴",
                    timestamp = System.currentTimeMillis() - 3600_000
                ),
                ChatMessage(
                    id = "msg_2",
                    senderId = "me",
                    receiverId = modelId,
                    text = "Thanks Jess! That was actually in Maui. Absolute paradise!",
                    timestamp = System.currentTimeMillis() - 3000_000
                ),
                ChatMessage(
                    id = "msg_3",
                    senderId = "me",
                    receiverId = modelId,
                    text = "[photo]",
                    mediaUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&auto=format&fit=crop&q=80",
                    timestamp = System.currentTimeMillis() - 1800_000
                ),
                ChatMessage(
                    id = "msg_4",
                    senderId = modelId,
                    receiverId = "me",
                    text = "Wow! I have to go there sometime. We should plan a trip together! 😉",
                    timestamp = System.currentTimeMillis() - 900_000
                )
            )
        }
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
                .then(if (isImeVisible) Modifier.imePadding() else Modifier.navigationBarsPadding())
        ) {
            // ── 1. Top App Bar ───────────────────────────────────────────────
            ChatTopBar(
                modelName = resolvedName,
                scheme = scheme,
                avatarUrl = modelPortraitUrl,
                statusText = "Active now",
                isOnline = true,
                onBackClick = onBackClick,
                onAudioCallClick = onStartVoiceCall,
                onVideoCallClick = onStartVideoCall,
                onProfileClick = onOpenProfile
            )

            // ── 2. Chat Timeline Messages using ODSLazyColumn ────────────────
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
                // "TODAY 9:41 AM" Date Header (Figma 10-1451)
                item {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = ODSVariables.spacingComponent3),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            modifier = Modifier.fillMaxWidth(),
                            text = "TODAY 9:41 AM",
                            style = ODSTextStyles.microcopyBold, // 12sp
                            color = scheme.basicTextRecessive,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Chat Messages
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
                        avatarUrl = modelPortraitUrl
                    )
                }
            }

            // ── 3. Bottom Input Bar ──────────────────────────────────────────
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
