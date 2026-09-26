package com.app.screentime.feature.chat.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Message Bubble component (Matching media_1789923373115.png & Figma 10-1452, 10-1457).
 * Displays delivery status indicators:
 * - Sending: 🕒
 * - Sent: ✔️
 * - Failed: ⚠️ Retry
 */
@Composable
fun MessageBubble(
    message: ChatMessage,
    isMe: Boolean,
    scheme: ODSTheme = zonaODSTheme,
    avatarUrl: String? = null,
    onRetry: ((ChatMessage) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isImageMessage = message.text.contains("[photo]", ignoreCase = true) ||
            message.text.contains("image", ignoreCase = true) ||
            message.mediaUrl != null

    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = ODSVariables.spacingComponent2),
        gap = ODSVariables.spacingComponent3,
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        // Mini Avatar for incoming messages (28x28 dp)
        if (!isMe) {
            ODSRow(
                cornerRadius = ODSCorners(all = 14.dp),
                clipContent = true,
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                width = 28.dp,
                height = 28.dp
            ) {
                if (!avatarUrl.isNullOrBlank()) {
                    ODSImage(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        imageModel = ODSImageModel(
                            url = avatarUrl,
                            contentDescription = "avatar"
                        ),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    ODSBox(
                        modifier = Modifier.fillMaxSize(),
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = com.telekom.odsystem.R.drawable.ic_user,
                                contentDescription = "avatar"
                            ),
                            tint = scheme.basicTextRecessive.getColor(),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        if (isImageMessage && !message.mediaUrl.isNullOrBlank()) {
            val photoUrl = message.mediaUrl

            ODSBox(
                modifier = Modifier
                    .width(260.dp)
                    .height(180.dp),
                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                clipContent = true
            ) {
                ODSImage(
                    imageModel = ODSImageModel(
                        url = photoUrl!!,
                        contentDescription = "Chat Photo"
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            // Text Message Bubble
            if (!isMe) {
                // Incoming Message Bubble: Bordered card, bottomLeft = 4dp
                ODSRow(
                    padding = ODSPadding(all = ODSVariables.spacingComponent4),
                    cornerRadius = ODSCorners(
                        topLeft = ODSVariables.radiusMedium,
                        topRight = ODSVariables.radiusMedium,
                        bottomLeft = ODSVariables.spacingComponent2, // 4.dp
                        bottomRight = ODSVariables.radiusMedium
                    ),
                    border = ODSBorder(
                        width = ODSVariables.strokes1,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start,
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    width = 270.dp
                ) {
                    ODSText(
                        modifier = Modifier.weight(1f),
                        text = message.text,
                        style = ODSTextStyles.bodySRegular, // 14sp
                        color = scheme.basicText
                    )
                }
            } else {
                // Outgoing Message Bubble: Borderless card, bottomRight = 4dp with delivery status
                ODSBox(
                    padding = ODSPadding(all = ODSVariables.spacingComponent4),
                    cornerRadius = ODSCorners(
                        topLeft = ODSVariables.radiusMedium,
                        topRight = ODSVariables.radiusMedium,
                        bottomLeft = ODSVariables.radiusMedium,
                        bottomRight = ODSVariables.spacingComponent2 // 4.dp
                    ),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    width = 270.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ODSText(
                            modifier = Modifier.fillMaxWidth(),
                            text = message.text,
                            style = ODSTextStyles.bodySRegular, // 14sp
                            color = scheme.basicText
                        )
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 4.dp
                        ) {
                            when {
                                message.isSending -> {
                                    ODSText(
                                        text = "🕒",
                                        style = ODSTextStyles.microcopyRegular,
                                        color = scheme.basicTextRecessive
                                    )
                                }
                                message.isFailed -> {
                                    ODSRow(
                                        modifier = Modifier.clickable { onRetry?.invoke(message) },
                                        verticalAlignment = Alignment.CenterVertically,
                                        gap = 2.dp
                                    ) {
                                        ODSText(
                                            text = "⚠️ Retry",
                                            style = ODSTextStyles.microcopyBold,
                                            color = HexColor("#FF4D4F", 1.0f)
                                        )
                                    }
                                }
                                else -> {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = com.telekom.odsystem.R.drawable.ic_check,
                                            contentDescription = "Sent"
                                        ),
                                        tint = scheme.basicAccent.getColor(),
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
