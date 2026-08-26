package com.app.screentime.feature.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.ChatMessage
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.orchidSecondaryScheme
import java.text.SimpleDateFormat
import java.util.*

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
    var messageText by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("1", modelId, "user1", "Hey there! How is your day going? 😊", System.currentTimeMillis() - 60000),
            ChatMessage("2", "user1", modelId, "Pretty good! Just relaxing after a busy day.", System.currentTimeMillis() - 30000),
            ChatMessage("3", modelId, "user1", "I'm so glad to chat with you! What do you like to do for fun? 💜", System.currentTimeMillis() - 10000)
        )
    }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    ODSColumn(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        // Top Bar with ODSIcon
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            padding = ODSPadding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            gap = 10.dp
        ) {
            IconButton(onClick = onBackClick) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.navigation_left_type_standard_size_standard),
                    tint = scheme.basicText.getColor()
                )
            }
            ODSBox(
                modifier = Modifier
                    .size(width = 46.dp, height = 46.dp)
                    .clip(CircleShape),
                background = listOf(ODSColorModel(hexColor = orchidSecondaryScheme.basicBackgroundSubtle)),
                contentAlignment = Alignment.Center
            ) {
                ODSText(
                    text = modelName.firstOrNull()?.toString() ?: "M",
                    style = ODSTextStyles.pompiereTitleM,
                    color = scheme.basicText
                )
            }
            ODSColumn(modifier = Modifier.weight(1f)) {
                ODSText(
                    text = modelName,
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicText
                )
                ODSText(
                    text = "● Online · 1 coin/msg",
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.functionalSuccessStandard
                )
            }
            IconButton(onClick = onStartVoiceCall) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Filled.Phone),
                    tint = scheme.basicText.getColor()
                )
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { message ->
                val isMe = message.senderId == "user1"
                val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                val formattedTime = timeFormat.format(Date(message.timestamp))

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    ODSBox(
                        modifier = Modifier.widthIn(max = 280.dp),
                        background = listOf(
                            ODSColorModel(
                                hexColor = if (isMe) scheme.basicAccent else scheme.basicBackgroundCard
                            )
                        ),
                        cornerRadius = ODSCorners(
                            topLeft = 16.dp,
                            topRight = 16.dp,
                            bottomLeft = if (isMe) 16.dp else 4.dp,
                            bottomRight = if (isMe) 4.dp else 16.dp
                        ),
                        padding = ODSPadding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        ODSColumn(gap = 4.dp) {
                            ODSText(
                                text = message.text,
                                style = ODSTextStyles.bodySRegular,
                                color = if (isMe) scheme.basicTextOnAccent else scheme.basicText
                            )
                            ODSText(
                                text = formattedTime,
                                style = ODSTextStyles.microcopyRegular,
                                color = if (isMe) scheme.basicTextOnAccent.getColor().copy(alpha = 0.7f).let { scheme.basicTextRecessive } else scheme.basicTextRecessive,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }

        // Bottom Input Row
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = 16.dp),
            padding = ODSPadding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            gap = 8.dp
        ) {
            ODSTextField(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = ODSTextFieldProps(
                    placeholderText = "Type a message… (1 coin)",
                    inputText = messageText,
                    size = ODSTextFieldSize.SMALL
                ),
                onValueChange = { messageText = it }
            )

            ODSBox(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (messageText.isNotBlank()) {
                            messages.add(
                                ChatMessage(
                                    id = UUID.randomUUID().toString(),
                                    senderId = "user1",
                                    receiverId = modelId,
                                    text = messageText.trim(),
                                    timestamp = System.currentTimeMillis()
                                )
                            )
                            messageText = ""
                        }
                    },
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Filled.Send),
                    tint = scheme.basicTextOnAccent.getColor()
                )
            }
        }
    }
}
