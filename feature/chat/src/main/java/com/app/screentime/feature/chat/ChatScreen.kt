package com.app.screentime.feature.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.*

@Composable
fun ChatScreen(
    modelId: String,
    modelName: String,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onBackClick: () -> Unit = {},
    onStartVoiceCall: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(modelId) {
        viewModel.loadChat(modelId)
    }

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    ODSColumn(
        modifier = modifier.fillMaxSize().imePadding(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        // Top App Bar
        ODSRow(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSRow(verticalAlignment = Alignment.CenterVertically, gap = 10.dp, modifier = Modifier.weight(1f)) {
                IconButton(onClick = onBackClick) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.navigation_left_type_standard_size_standard),
                        tint = scheme.basicText.getColor()
                    )
                }

                ODSBox(
                    modifier = Modifier.size(40.dp).clip(CircleShape),
                    background = listOf(ODSColorModel(hexColor = orchidSecondaryScheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = modelName.take(1).uppercase(),
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                }

                ODSColumn(gap = 2.dp) {
                    ODSText(text = modelName, style = ODSTextStyles.bodyMBold, color = scheme.basicText)
                    ODSText(text = "Online • ₹1/msg", style = ODSTextStyles.microcopyRegular, color = scheme.functionalSuccessStandard)
                }
            }

            IconButton(onClick = onStartVoiceCall) {
                ODSIcon(iconModel = ODSIconModel(imageVector = Icons.Default.Phone), tint = scheme.basicAccent.getColor())
            }
        }

        // 24-Hour Ephemeral Notice
        ODSBox(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = 10.dp),
            padding = ODSPadding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            ODSText(
                text = "🔒 Ephemeral Session: Messages delete automatically after 24 hours.",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }

        // Messages Feed
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(uiState.messages, key = { it.id }) { msg ->
                val isMe = msg.receiverId == modelId
                MessageBubble(message = msg, isMe = isMe, scheme = scheme)
            }
        }

        // Input & Send Bar
        ODSRow(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            gap = 8.dp
        ) {
            ODSTextField(
                onValueChange = { viewModel.onInputTextChanged(it) },
                modifier = Modifier.weight(1f),
                props = ODSTextFieldProps(placeholderText = "Message ${modelName.split(" ").firstOrNull() ?: ""}...", inputText = uiState.inputText),
            )

            ODSBox(
                modifier = Modifier.size(44.dp).clip(CircleShape).clickable { viewModel.sendMessage(modelId) },
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(iconModel = ODSIconModel(imageVector = Icons.Default.Send), tint = scheme.basicTextOnAccent.getColor())
            }
        }
    }
}
