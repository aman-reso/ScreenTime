package com.app.screentime.feature.chat.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Message Bubble component (Matching media_1789923373115.png & Figma 10-1452, 10-1457).
 *
 * Rules:
 * 1. 100% ODS components.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Compact text styles (bodySRegular: 14sp).
 * 4. Corner radius and spacing from `ODSVariables`.
 */
@Composable
fun MessageBubble(
    message: ChatMessage,
    isMe: Boolean,
    scheme: ODSTheme = zonaODSTheme,
    avatarUrl: String? = null,
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
                ODSImage(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    imageModel = ODSImageModel(
                        url = avatarUrl
                            ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80",
                        contentDescription = "avatar"
                    ),
                    contentScale = ContentScale.Crop
                )
            }
        }

        if (isImageMessage) {
            // Photo message card (e.g. Maui Beach photo with rounded corners)
            val photoUrl = message.mediaUrl?.takeIf { it.isNotBlank() }
                ?: "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&auto=format&fit=crop&q=80"

            ODSBox(
                modifier = Modifier
                    .width(260.dp)
                    .height(180.dp),
                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                clipContent = true
            ) {
                ODSImage(
                    imageModel = ODSImageModel(
                        url = photoUrl,
                        contentDescription = "Chat Photo"
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            // Text Message Bubble
            if (!isMe) {
                // Incoming Message Bubble: Bordered white card, bottomLeft = 4dp
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
                // Outgoing Message Bubble: Borderless white card, bottomRight = 4dp
                ODSRow(
                    padding = ODSPadding(all = ODSVariables.spacingComponent4),
                    cornerRadius = ODSCorners(
                        topLeft = ODSVariables.radiusMedium,
                        topRight = ODSVariables.radiusMedium,
                        bottomLeft = ODSVariables.radiusMedium,
                        bottomRight = ODSVariables.spacingComponent2 // 4.dp
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
            }
        }
    }
}
