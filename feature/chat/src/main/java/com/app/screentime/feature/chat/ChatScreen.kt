package com.app.screentime.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.ChatMessage
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

private val defaultModelPortraits = listOf(
    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=1200&q=85",
    "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=1200&q=85",
    "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=1200&q=85",
    "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?auto=format&fit=crop&w=1200&q=85"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    modelId: String,
    modelName: String,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onBackClick: () -> Unit = {},
    onStartVoiceCall: () -> Unit = {},
    onStartVideoCall: () -> Unit = onStartVoiceCall,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val isImeVisible = WindowInsets.isImeVisible

    val modelPortraitUrl = remember(modelId) {
        val hash = (modelId.hashCode() and 0x7FFFFFFF)
        defaultModelPortraits[hash % defaultModelPortraits.size]
    }

    LaunchedEffect(modelId) {
        viewModel.loadChat(modelId)
    }

    val displayMessages = remember(uiState.messages) {
        val valid = uiState.messages.filter { it.text.isNotBlank() }.distinctBy { it.id }
        if (valid.isNotEmpty()) {
            valid
        } else {
            listOf(
                ChatMessage(
                    id = "sample_msg_1",
                    senderId = modelId,
                    receiverId = "user",
                    text = "Hey! So glad we connected! Let's chat or hop on a call 😉",
                    timestamp = System.currentTimeMillis() - 60_000
                )
            )
        }
    }

    LaunchedEffect(displayMessages.size) {
        if (displayMessages.isNotEmpty()) {
            listState.animateScrollToItem(displayMessages.size)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(scheme.basicBackground.getColor())
    ) {
        // 1. Model Profile Portrait Background Image
        ODSImage(
            imageModel = ODSImageModel(
                url = modelPortraitUrl,
                contentDescription = modelName
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Soft Gradient Scrim Overlay for Legibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x661E1145),
                            Color(0x991E1145),
                            Color(0xFA1E1145)
                        )
                    )
                )
        )

        // 3. Main Chat Screen Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .then(if (isImeVisible) Modifier.imePadding() else Modifier.navigationBarsPadding())
        ) {
            // Modular Top Bar with Audio & Video Call Icons
            ChatTopBar(
                modelName = modelName,
                scheme = scheme,
                onBackClick = onBackClick,
                onAudioCallClick = onStartVoiceCall,
                onVideoCallClick = onStartVideoCall
            )

            // Messages Feed (No empty cards/sections)
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(displayMessages, key = { it.id }) { msg ->
                    val isMe = msg.senderId != modelId
                    MessageBubble(
                        message = msg,
                        isMe = isMe,
                        scheme = scheme,
                        avatarUrl = modelPortraitUrl
                    )
                }
            }

            // Modular Bottom Input Bar
            ChatInputBar(
                inputText = uiState.inputText,
                modelName = modelName,
                scheme = scheme,
                onInputTextChanged = { viewModel.onInputTextChanged(it) },
                onSendMessage = { viewModel.sendMessage(modelId) },
                onVoiceClick = onStartVoiceCall
            )
        }
    }
}
