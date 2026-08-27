package com.app.screentime.feature.chat

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.ChatMessage
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessageBubble(
    message: ChatMessage,
    isMe: Boolean,
    scheme: ODSTheme
) {
    val alignment = if (isMe) Alignment.End else Alignment.Start
    val bgColor = if (isMe) {
        scheme.basicAccent
    } else {
        scheme.basicBackgroundCard
    }
    val textColor = if (isMe) scheme.basicTextOnAccent else scheme.basicText
    val timeColor = if (isMe) scheme.basicTextOnAccent else scheme.basicTextRecessive
    val corners = if (isMe) {
        ODSCorners(topLeft = 16.dp, topRight = 4.dp, bottomLeft = 16.dp, bottomRight = 16.dp)
    } else {
        ODSCorners(topLeft = 4.dp, topRight = 16.dp, bottomLeft = 16.dp, bottomRight = 16.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        ODSBox(
            modifier = Modifier.widthIn(max = 280.dp),
            background = listOf(ODSColorModel(hexColor = bgColor)),
            cornerRadius = corners,
            padding = ODSPadding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            ODSColumn(gap = 4.dp) {
                ODSText(
                    text = message.text,
                    style = ODSTextStyles.bodyMBold,
                    color = textColor
                )
                ODSText(
                    text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(message.timestamp)),
                    style = ODSTextStyles.microcopyRegular,
                    color = timeColor,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
