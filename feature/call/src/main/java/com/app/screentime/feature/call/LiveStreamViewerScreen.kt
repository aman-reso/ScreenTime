package com.app.screentime.feature.call

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import io.livekit.android.LiveKit
import io.livekit.android.room.track.RemoteVideoTrack
import io.livekit.android.room.track.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class LiveChatMessage(
    val id: String,
    val senderName: String,
    val text: String,
    val isTip: Boolean = false,
    val tipAmount: Double = 0.0
)

data class GiftOption(
    val id: String,
    val name: String,
    val icon: String,
    val coins: Double
)

@Composable
fun LiveStreamViewerScreen(
    streamId: String,
    hostId: String,
    hostName: String,
    hostAvatar: String,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    viewModel: LiveViewModel = hiltViewModel(),
    onExit: () -> Unit
) {
    val api = viewModel.api
    val sessionManager = viewModel.sessionManager
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val room = remember { LiveKit.create(context) }

    var remoteVideoTrack by remember { mutableStateOf<RemoteVideoTrack?>(null) }
    var isConnected by remember { mutableStateOf(false) }
    var viewerCount by remember { mutableIntStateOf(128) }
    var messageText by remember { mutableStateOf("") }
    var showGiftSheet by remember { mutableStateOf(false) }
    var tipNotice by remember { mutableStateOf<String?>(null) }

    val chatMessages = remember {
        mutableStateListOf(
            LiveChatMessage("1", "Aman", "Hey everyone! 👋"),
            LiveChatMessage("2", "Vikram", "Super clear stream! ❤️"),
            LiveChatMessage("3", "Sneha", "Loving the music tonight ✨")
        )
    }
    val listState = rememberLazyListState()

    val gifts = remember {
        listOf(
            GiftOption("g1", "Rose", "🌹", 10.0),
            GiftOption("g2", "Heart", "💖", 50.0),
            GiftOption("g3", "Crown", "👑", 100.0),
            GiftOption("g4", "Diamond", "💎", 500.0)
        )
    }

    // Connect to LiveKit Room as Subscriber
    LaunchedEffect(streamId) {
        try {
            val token = sessionManager.token ?: ""
            val joinResp = try {
                api.joinLiveStream(token, streamId)
            } catch (e: Exception) {
                null
            }

            val livekitUrl = joinResp?.livekit_url ?: "wss://connecto-7sxi06vp.livekit.cloud"
            val roomToken = joinResp?.token?.ifBlank { null } ?: generateDirectLiveKitViewerToken(
                apiKey = "APImr59LGqwEVuj",
                apiSecret = "cvdsoq3pKQusl4HfAHPxSeGXvHcM5atVOWQ2WozyxF2",
                identity = sessionManager.userId ?: "user_${System.currentTimeMillis()}",
                roomName = streamId
            )

            room.connect(livekitUrl, roomToken)
            isConnected = true

            while (true) {
                val remotePub = room.remoteParticipants.values
                    .firstOrNull()
                    ?.getTrackPublication(Track.Source.CAMERA)
                remoteVideoTrack = remotePub?.track as? RemoteVideoTrack
                delay(500)
            }
        } catch (e: Exception) {
            isConnected = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            scope.launch {
                try {
                    room.disconnect()
                    room.release()
                } catch (_: Exception) {
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0418))
    ) {
        // ── Fullscreen Video Feed or Scrim ───────────────────────────────────
        if (remoteVideoTrack != null) {
            LiveKitVideoSurface(
                room = room,
                videoTrack = remoteVideoTrack,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Placeholder host image
            ODSImage(
                imageModel = ODSImageModel(
                    url = if (hostAvatar.isNotBlank()) hostAvatar
                    else "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=1200&q=85",
                    contentDescription = hostName
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Dark gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x88000000),
                            Color(0x00000000),
                            Color(0xDD000000)
                        )
                    )
                )
        )

        // ── Top Header ────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Host info pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0x66000000))
                    .padding(4.dp)
            ) {
                ODSImage(
                    imageModel = ODSImageModel(
                        url = hostAvatar.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb" },
                        contentDescription = hostName
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.padding(end = 12.dp)) {
                    ODSText(
                        text = hostName.ifBlank { "Creator" },
                        style = ODSTextStyles.bodyMBold,
                        color = HexColor(0xFFFFFFFF)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF334B))
                        )
                        ODSText(
                            text = "LIVE",
                            style = ODSTextStyles.microcopyBold,
                            color = HexColor(0xFFFF334B)
                        )
                    }
                }
            }

            // Right side: Viewer Count & Close
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x66000000))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Viewers",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        ODSText(
                            text = "$viewerCount",
                            style = ODSTextStyles.microcopyBold,
                            color = HexColor(0xFFFFFFFF)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x66000000))
                        .clickable { onExit() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // ── Tip Announcement Banner (if any) ─────────────────────────────────
        AnimatedVisibility(
            visible = tipNotice != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 70.dp)
        ) {
            tipNotice?.let { notice ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFFF8A00), Color(0xFFE52E71))
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    ODSText(
                        text = notice,
                        style = ODSTextStyles.bodyMBold,
                        color = HexColor(0xFFFFFFFF)
                    )
                }
            }
        }

        // ── Bottom Content: Live Chat & Action Bar ───────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            // Live Messages list
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(chatMessages) { msg ->
                    if (msg.isTip) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x99E52E71))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            ODSText(
                                text = "🎁 ${msg.senderName} sent ₹${msg.tipAmount.toInt()} Tip!",
                                style = ODSTextStyles.microcopyBold,
                                color = HexColor(0xFFFFFFFF)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x55000000))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                ODSText(
                                    text = "${msg.senderName}: ",
                                    style = ODSTextStyles.microcopyBold,
                                    color = HexColor(0xFFBC96FF)
                                )
                                ODSText(
                                    text = msg.text,
                                    style = ODSTextStyles.microcopyRegular,
                                    color = HexColor(0xFFFFFFFF)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Bar (Chat Input + Gift Button + Heart Button)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Input Field
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = {
                        ODSText(
                            text = "Say something...",
                            style = ODSTextStyles.microcopyRegular,
                            color = HexColor(0x88FFFFFF)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x66000000),
                        unfocusedContainerColor = Color(0x66000000),
                        focusedBorderColor = Color(0xFFBC96FF),
                        unfocusedBorderColor = Color(0x33BC96FF),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp),
                    trailingIcon = {
                        if (messageText.isNotBlank()) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = Color(0xFFBC96FF),
                                modifier = Modifier
                                    .clickable {
                                        val myName = sessionManager.userName ?: "You"
                                        chatMessages.add(
                                            LiveChatMessage(
                                                System.currentTimeMillis().toString(),
                                                myName,
                                                messageText
                                            )
                                        )
                                        messageText = ""
                                        scope.launch {
                                            listState.animateScrollToItem(chatMessages.size - 1)
                                        }
                                    }
                                    .padding(8.dp)
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                )

                // Gift / Tip Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFFF8A00), Color(0xFFE52E71))
                            )
                        )
                        .clickable { showGiftSheet = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CardGiftcard,
                        contentDescription = "Send Gift",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Quick Heart Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0x66FF334B))
                        .clickable {
                            val myName = sessionManager.userName ?: "You"
                            chatMessages.add(
                                LiveChatMessage(
                                    System.currentTimeMillis().toString(),
                                    myName,
                                    "❤️ Loved this live!"
                                )
                            )
                            scope.launch {
                                listState.animateScrollToItem(chatMessages.size - 1)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Heart",
                        tint = Color(0xFFFF334B),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // ── Gift / Coin Tipping Drawer ─────────────────────────────────────────
        if (showGiftSheet) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000))
                    .clickable { showGiftSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(Color(0xFF1E1145))
                        .padding(20.dp)
                        .clickable(enabled = false) {}
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSText(
                            text = "Send a Tip to $hostName",
                            style = ODSTextStyles.bodyMBold,
                            color = HexColor(0xFFFFFFFF)
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.clickable { showGiftSheet = false }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        gifts.forEach { gift ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF2B1764))
                                    .clickable {
                                        showGiftSheet = false
                                        val myName = sessionManager.userName ?: "You"
                                        scope.launch {
                                            try {
                                                val token = sessionManager.token ?: ""
                                                api.sendLiveTip(
                                                    token,
                                                    streamId,
                                                    gift.coins,
                                                    gift.name
                                                )
                                            } catch (_: Exception) {
                                            }

                                            chatMessages.add(
                                                LiveChatMessage(
                                                    id = System.currentTimeMillis().toString(),
                                                    senderName = myName,
                                                    text = "Sent ${gift.name}",
                                                    isTip = true,
                                                    tipAmount = gift.coins
                                                )
                                            )
                                            tipNotice =
                                                "🎉 You sent ${gift.name} (₹${gift.coins.toInt()})!"
                                            delay(3000)
                                            tipNotice = null
                                        }
                                    }
                                    .padding(14.dp)
                            ) {
                                ODSText(
                                    text = gift.icon,
                                    style = ODSTextStyles.bodyL,
                                    color = HexColor(0xFFFFFFFF)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ODSText(
                                    text = gift.name,
                                    style = ODSTextStyles.microcopyBold,
                                    color = HexColor(0xFFFFFFFF)
                                )
                                ODSText(
                                    text = "₹${gift.coins.toInt()}",
                                    style = ODSTextStyles.microcopyRegular,
                                    color = HexColor(0xFFD7FF81)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun generateDirectLiveKitViewerToken(
    apiKey: String,
    apiSecret: String,
    identity: String,
    roomName: String
): String {
    val header = """{"alg":"HS256","typ":"JWT"}"""
    val iat = System.currentTimeMillis() / 1000
    val exp = iat + (6 * 3600)
    val payload =
        """{"iss":"$apiKey","sub":"$identity","name":"$identity","iat":$iat,"exp":$exp,"nbf":$iat,"video":{"roomJoin":true,"room":"$roomName","canPublish":false,"canSubscribe":true}}"""

    val base64Header = android.util.Base64.encodeToString(
        header.toByteArray(),
        android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
    )
    val base64Payload = android.util.Base64.encodeToString(
        payload.toByteArray(),
        android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
    )
    val dataToSign = "$base64Header.$base64Payload"

    val mac = javax.crypto.Mac.getInstance("HmacSHA256")
    val secretKey = javax.crypto.spec.SecretKeySpec(apiSecret.toByteArray(), "HmacSHA256")
    mac.init(secretKey)
    val signature = mac.doFinal(dataToSign.toByteArray())
    val base64Signature = android.util.Base64.encodeToString(
        signature,
        android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
    )

    return "$dataToSign.$base64Signature"
}
