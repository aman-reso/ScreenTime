package com.app.screentime.feature.chat.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.model.MessageType
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.chat.util.AudioPlayerManager
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
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
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Message Bubble component.
 * Chat bubble colors: FEF2F2 for outgoing (isMe), E4E4E7 for incoming (!isMe).
 * Time and delivery status indicators are shown outside the chat bubble on the end side.
 */
@Composable
fun MessageBubble(
    message: ChatMessage,
    isMe: Boolean,
    scheme: ODSTheme = zonaODSTheme,
    avatarUrl: String? = null,
    onRetry: ((ChatMessage) -> Unit)? = null,
    onViewPhotoClick: ((ChatMessage) -> Unit)? = null,
    onPlayAudioClick: ((ChatMessage) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isAudioMessage = message.type == MessageType.AUDIO || (
        (message.mediaUrl?.takeIf { it.isNotBlank() } ?: message.text).let { url ->
            url.endsWith(".m4a", ignoreCase = true) ||
            url.endsWith(".mp3", ignoreCase = true) ||
            url.endsWith(".aac", ignoreCase = true) ||
            url.endsWith(".wav", ignoreCase = true) ||
            url.endsWith(".ogg", ignoreCase = true) ||
            url.endsWith(".3gp", ignoreCase = true) ||
            url.endsWith(".enc", ignoreCase = true) ||
            url.contains("voice_note", ignoreCase = true) ||
            url.contains("/audio/", ignoreCase = true)
        }
    )
    val audioUrl = message.mediaUrl?.takeIf { it.isNotBlank() } ?: message.text

    val isImageMessage = !isAudioMessage && (
        message.type == MessageType.IMAGE ||
        !message.mediaUrl.isNullOrBlank() || (
            message.text.startsWith("http", ignoreCase = true) && (
                message.text.endsWith(".jpg", ignoreCase = true) ||
                message.text.endsWith(".jpeg", ignoreCase = true) ||
                message.text.endsWith(".png", ignoreCase = true) ||
                message.text.endsWith(".webp", ignoreCase = true) ||
                message.text.contains("/uploads/images/", ignoreCase = true) ||
                message.text.contains("storage.googleapis.com", ignoreCase = true)
            )
        )
    )
    val photoUrl = message.mediaUrl?.takeIf { it.isNotBlank() } ?: message.text

    val context = LocalContext.current
    val urlRegex = remember {
        Regex("(https?://[\\w-]+(\\.[\\w-]+)+(:\\d+)?(/[^\\s]*)?|www\\.[\\w-]+(\\.[\\w-]+)+(:\\d+)?(/[^\\s]*)?)", RegexOption.IGNORE_CASE)
    }
    val detectedUrls = remember(message.text, isImageMessage, isAudioMessage) {
        if (!isImageMessage && !isAudioMessage) urlRegex.findAll(message.text).map { it.value }.toList() else emptyList()
    }
    val annotatedText = remember(message.text, scheme.basicAccent, isImageMessage, isAudioMessage) {
        if (isImageMessage || isAudioMessage) {
            AnnotatedString(message.text)
        } else {
            val builder = AnnotatedString.Builder(message.text)
            urlRegex.findAll(message.text).forEach { match ->
                builder.addStyle(
                    style = SpanStyle(
                        color = scheme.basicAccent.getColor(),
                        textDecoration = TextDecoration.Underline
                    ),
                    start = match.range.first,
                    end = match.range.last + 1
                )
            }
            builder.toAnnotatedString()
        }
    }

    val timeString = remember(message.timestamp) {
        if (message.timestamp > 0) {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            sdf.format(Date(message.timestamp))
        } else ""
    }

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
                                drawableRes = R.drawable.ic_user,
                                contentDescription = "avatar"
                            ),
                            tint = scheme.basicTextRecessive.getColor(),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        ODSColumn(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
            gap = 2.dp
        ) {
            if (isAudioMessage && audioUrl.isNotBlank()) {
                val currentlyPlayingId by AudioPlayerManager.currentlyPlayingId.collectAsState()
                val isPlaying by AudioPlayerManager.isPlaying.collectAsState()
                val isLoadingAudio by AudioPlayerManager.isLoadingAudio.collectAsState()
                val playbackProgress by AudioPlayerManager.playbackProgress.collectAsState()
                val currentPositionSec by AudioPlayerManager.currentPositionSec.collectAsState()

                val isThisPlaying = currentlyPlayingId == message.id && isPlaying
                val isThisLoading = isLoadingAudio == message.id
                val progress = if (currentlyPlayingId == message.id) playbackProgress else 0f

                ODSBox(
                    modifier = Modifier.widthIn(min = 200.dp, max = 270.dp),
                    padding = ODSPadding(
                        top = ODSVariables.spacingComponent3,
                        bottom = ODSVariables.spacingComponent2,
                        left = ODSVariables.spacingComponent4,
                        right = ODSVariables.spacingComponent4
                    ),
                    cornerRadius = ODSCorners(
                        topLeft = ODSVariables.radiusMedium,
                        topRight = ODSVariables.radiusMedium,
                        bottomLeft = if (isMe) ODSVariables.radiusMedium else ODSVariables.spacingComponent2,
                        bottomRight = if (isMe) ODSVariables.spacingComponent2 else ODSVariables.radiusMedium
                    ),
                    border = if (!isMe) ODSBorder(
                        width = ODSVariables.strokes1,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ) else null,
                    background = listOf(ODSColorModel(hexColor = if (isMe) HexColor("#FEF2F2", 1.0f) else HexColor("#E4E4E7", 1.0f)))
                ) {
                    ODSColumn(
                        gap = ODSVariables.spacingComponent2,
                        horizontalAlignment = Alignment.End
                    ) {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            gap = ODSVariables.spacingComponent3
                        ) {
                            ODSBox(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable {
                                        if (onPlayAudioClick != null) {
                                            onPlayAudioClick(message)
                                        } else {
                                            AudioPlayerManager.togglePlay(message.id, audioUrl)
                                        }
                                    },
                                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (message.isSending || isThisLoading) {
                                    ODSLoadingSpinner(
                                        scheme = scheme,
                                        props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.SMALL)
                                    )
                                } else {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = if (isThisPlaying) {
                                                com.telekom.odsystem.R.drawable.pause_type_standard_size_standard
                                            } else {
                                                com.telekom.odsystem.R.drawable.play_type_standard_size_standard
                                            },
                                            contentDescription = if (isThisPlaying) "Pause" else "Play"
                                        ),
                                        tint = scheme.basicTextOnAccent.getColor(),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            ODSColumn(
                                modifier = Modifier.weight(1f),
                                gap = 2.dp
                            ) {
                                ODSRow(
                                    verticalAlignment = Alignment.CenterVertically,
                                    gap = 4.dp
                                ) {
                                    ODSBox(
                                        modifier = Modifier.size(16.dp),
                                        cornerRadius = ODSCorners(all = 8.dp),
                                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        ODSText(
                                            text = "1",
                                            style = ODSTextStyles.microcopyBold,
                                            color = scheme.basicTextOnAccent
                                        )
                                    }
                                    ODSText(
                                        text = "Voice message",
                                        style = ODSTextStyles.bodySBold,
                                        color = scheme.basicText
                                    )
                                }

                                ODSBox(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp),
                                    cornerRadius = ODSCorners(all = 2.dp),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                ) {
                                    if (progress > 0f) {
                                        ODSBox(
                                            modifier = Modifier
                                                .fillMaxWidth(fraction = progress)
                                                .fillMaxHeight(),
                                            cornerRadius = ODSCorners(all = 2.dp),
                                            background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                                        )
                                    }
                                }

                                val durationText = if (isThisPlaying) {
                                    val mins = currentPositionSec / 60
                                    val secs = currentPositionSec % 60
                                    String.format(Locale.getDefault(), "%02d:%02d", mins, secs)
                                } else {
                                    "Tap to play once"
                                }
                                ODSText(
                                    text = durationText,
                                    style = ODSTextStyles.microcopyRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }

                        ODSRow(
                            modifier = Modifier.align(Alignment.End),
                            horizontalAlignment = Alignment.End,
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 4.dp
                        ) {
                            if (timeString.isNotBlank()) {
                                ODSText(
                                    text = timeString,
                                    style = ODSTextStyles.microcopyRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                            if (isMe) {
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
                                                drawableRes = R.drawable.ic_check,
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
            } else if (isImageMessage && photoUrl.isNotBlank()) {
                ODSBox(
                    modifier = Modifier
                        .widthIn(min = 200.dp, max = 270.dp)
                        .clickable {
                            if (!message.isSending && !message.isFailed) {
                                onViewPhotoClick?.invoke(message)
                            }
                        },
                    padding = ODSPadding(
                        top = ODSVariables.spacingComponent3,
                        bottom = ODSVariables.spacingComponent2,
                        left = ODSVariables.spacingComponent4,
                        right = ODSVariables.spacingComponent4
                    ),
                    cornerRadius = ODSCorners(
                        topLeft = ODSVariables.radiusMedium,
                        topRight = ODSVariables.radiusMedium,
                        bottomLeft = if (isMe) ODSVariables.radiusMedium else ODSVariables.spacingComponent2,
                        bottomRight = if (isMe) ODSVariables.spacingComponent2 else ODSVariables.radiusMedium
                    ),
                    border = if (!isMe) ODSBorder(
                        width = ODSVariables.strokes1,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ) else null,
                    background = listOf(ODSColorModel(hexColor = if (isMe) HexColor("#FEF2F2", 1.0f) else HexColor("#E4E4E7", 1.0f)))
                ) {
                    ODSColumn(
                        gap = ODSVariables.spacingComponent2,
                        horizontalAlignment = Alignment.End
                    ) {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            gap = ODSVariables.spacingComponent3
                        ) {
                            ODSBox(
                                modifier = Modifier.size(36.dp),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                                border = ODSBorder(
                                    width = ODSVariables.strokes1,
                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                                ),
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (message.isSending) {
                                    ODSLoadingSpinner(
                                        scheme = scheme,
                                        props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.SMALL)
                                    )
                                } else {
                                    ODSRow(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        gap = 2.dp
                                    ) {
                                        ODSIcon(
                                            iconModel = ODSIconModel(
                                                drawableRes = R.drawable.ic_camera,
                                                contentDescription = "View once photo"
                                            ),
                                            tint = scheme.basicAccent.getColor(),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        ODSText(
                                            text = "1",
                                            style = ODSTextStyles.microcopyBold,
                                            color = scheme.basicAccent
                                        )
                                    }
                                }
                            }

                            ODSColumn(
                                modifier = Modifier.weight(1f),
                                gap = 2.dp
                            ) {
                                ODSText(
                                    text = "Photo",
                                    style = ODSTextStyles.bodySBold,
                                    color = scheme.basicText
                                )
                                ODSText(
                                    text = if (message.isSending) "Sending..." else "Tap to view once",
                                    style = ODSTextStyles.microcopyRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }

                        ODSRow(
                            modifier = Modifier.align(Alignment.End),
                            horizontalAlignment = Alignment.End,
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 4.dp
                        ) {
                            if (timeString.isNotBlank()) {
                                ODSText(
                                    text = timeString,
                                    style = ODSTextStyles.microcopyRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                            if (isMe) {
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
                                                drawableRes = R.drawable.ic_check,
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
            } else {
                if (!isMe) {
                    // Incoming Message Bubble: #E4E4E7 background, dynamic width up to 280dp
                    ODSBox(
                        modifier = Modifier.widthIn(min = 68.dp, max = 280.dp),
                        padding = ODSPadding(
                            top = ODSVariables.spacingComponent3,
                            bottom = ODSVariables.spacingComponent2,
                            left = ODSVariables.spacingComponent4,
                            right = ODSVariables.spacingComponent4
                        ),
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
                        background = listOf(ODSColorModel(hexColor = HexColor("#E4E4E7", 1.0f)))
                    ) {
                        ODSColumn(
                            gap = 2.dp,
                            horizontalAlignment = Alignment.End
                        ) {
                            ODSText(
                                modifier = Modifier.align(Alignment.Start),
                                text = annotatedText,
                                style = ODSTextStyles.bodySRegular, // 14sp
                                color = scheme.basicText
                            )
                            if (detectedUrls.isNotEmpty()) {
                                detectedUrls.forEach { rawUrl ->
                                    val cleanUrl = if (!rawUrl.startsWith("http://", ignoreCase = true) && !rawUrl.startsWith("https://", ignoreCase = true)) {
                                        "https://$rawUrl"
                                    } else {
                                        rawUrl
                                    }
                                    ODSRow(
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .clickable {
                                                try {
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(cleanUrl))
                                                    context.startActivity(intent)
                                                } catch (_: Exception) {}
                                            }
                                            .padding(top = 2.dp),
                                        gap = 4.dp,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ODSIcon(
                                            iconModel = ODSIconModel(
                                                drawableRes = R.drawable.ic_globe,
                                                contentDescription = "Link"
                                            ),
                                            tint = scheme.basicAccent.getColor(),
                                            modifier = Modifier.size(12.dp)
                                        )
                                        ODSText(
                                            text = "Open Link",
                                            style = ODSTextStyles.microcopyBold,
                                            color = scheme.basicAccent,
                                            textDecoration = TextDecoration.Underline
                                        )
                                    }
                                }
                            }
                            if (timeString.isNotBlank()) {
                                ODSText(
                                    modifier = Modifier.align(Alignment.End),
                                    text = timeString,
                                    style = ODSTextStyles.microcopyRegular, // 12sp
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }
                    }
                } else {
                    // Outgoing Message Bubble: #FEF2F2 background, dynamic width up to 280dp
                    ODSBox(
                        modifier = Modifier.widthIn(min = 78.dp, max = 280.dp),
                        padding = ODSPadding(
                            top = ODSVariables.spacingComponent3,
                            bottom = ODSVariables.spacingComponent2,
                            left = ODSVariables.spacingComponent4,
                            right = ODSVariables.spacingComponent4
                        ),
                        cornerRadius = ODSCorners(
                            topLeft = ODSVariables.radiusMedium,
                            topRight = ODSVariables.radiusMedium,
                            bottomLeft = ODSVariables.radiusMedium,
                            bottomRight = ODSVariables.spacingComponent2 // 4.dp
                        ),
                        background = listOf(ODSColorModel(hexColor = HexColor("#FEF2F2", 1.0f)))
                    ) {
                        ODSColumn(
                            gap = 2.dp,
                            horizontalAlignment = Alignment.End
                        ) {
                            ODSText(
                                modifier = Modifier.align(Alignment.Start),
                                text = annotatedText,
                                style = ODSTextStyles.bodySRegular, // 14sp
                                color = scheme.basicText
                            )
                            if (detectedUrls.isNotEmpty()) {
                                detectedUrls.forEach { rawUrl ->
                                    val cleanUrl = if (!rawUrl.startsWith("http://", ignoreCase = true) && !rawUrl.startsWith("https://", ignoreCase = true)) {
                                        "https://$rawUrl"
                                    } else {
                                        rawUrl
                                    }
                                    ODSRow(
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .clickable {
                                                try {
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(cleanUrl))
                                                    context.startActivity(intent)
                                                } catch (_: Exception) {}
                                            }
                                            .padding(top = 2.dp),
                                        gap = 4.dp,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ODSIcon(
                                            iconModel = ODSIconModel(
                                                drawableRes = R.drawable.ic_globe,
                                                contentDescription = "Link"
                                            ),
                                            tint = scheme.basicAccent.getColor(),
                                            modifier = Modifier.size(12.dp)
                                        )
                                        ODSText(
                                            text = "Open Link",
                                            style = ODSTextStyles.microcopyBold,
                                            color = scheme.basicAccent,
                                            textDecoration = TextDecoration.Underline
                                        )
                                    }
                                }
                            }
                            ODSRow(
                                modifier = Modifier.align(Alignment.End),
                                horizontalAlignment = Alignment.End,
                                verticalAlignment = Alignment.CenterVertically,
                                gap = 4.dp
                            ) {
                                if (timeString.isNotBlank()) {
                                    ODSText(
                                        text = timeString,
                                        style = ODSTextStyles.microcopyRegular,
                                        color = scheme.basicTextRecessive
                                    )
                                }
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
                                                drawableRes = R.drawable.ic_check,
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
}
