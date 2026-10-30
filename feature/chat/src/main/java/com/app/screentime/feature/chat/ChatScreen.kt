package com.app.screentime.feature.chat

import com.app.screentime.core.model.ChatMessage
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.compose.runtime.DisposableEffect
import com.app.screentime.feature.chat.util.AudioRecorderHelper
import com.app.screentime.feature.chat.util.AudioPlayerManager
import kotlinx.coroutines.delay
import com.app.screentime.ads.AdConfig
import com.app.screentime.ads.InterstitialAdManager
import com.app.screentime.ads.RewardedAdManager
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.chat.component.ChatInputBar
import com.app.screentime.feature.chat.component.ChatTopBar
import com.app.screentime.feature.chat.component.ViewOncePhotoDialog
import com.app.screentime.feature.chat.component.ChatPaywallDialog
import com.app.screentime.feature.chat.component.EmojiPickerView
import com.app.screentime.feature.chat.component.InChatNativeAdBubble
import com.app.screentime.feature.chat.component.MessageBubble
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
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
    onOpenProfile: () -> Unit = {},
    onVoiceCallClick: (() -> Unit)? = null,
    onVideoCallClick: (() -> Unit)? = null,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    var isEmojiPickerVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.sendImage(context, uri, modelId)
        }
    }

    val audioRecorder = remember(context) { AudioRecorderHelper(context) }
    var isRecordingAudio by remember { mutableStateOf(false) }
    var recordingDurationSeconds by remember { mutableStateOf(0) }
    var viewingPhotoMessage by remember { mutableStateOf<ChatMessage?>(null) }

    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val started = audioRecorder.startRecording()
            if (started) {
                isRecordingAudio = true
                recordingDurationSeconds = 0
                isEmojiPickerVisible = false
            }
        }
    }

    LaunchedEffect(isRecordingAudio) {
        if (isRecordingAudio) {
            while (isRecordingAudio) {
                delay(500)
                recordingDurationSeconds = audioRecorder.getElapsedDurationSeconds()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (audioRecorder.isCurrentlyRecording()) {
                audioRecorder.cancelRecording()
            }
            AudioPlayerManager.stop()
        }
    }

    LaunchedEffect(Unit) {
        activity?.let {
            RewardedAdManager.preloadAd(it)
            InterstitialAdManager.preloadAd(it)
        }
    }

    val resolvedName = if (modelName.isNotBlank()) modelName else "Friend"

    LaunchedEffect(modelId) {
        viewModel.loadChat(partnerId = modelId, conversationId = conversationId)
        activity?.let { act ->
            if (AdConfig.isMessageAdEnabled()) {
                val showedVideo = RewardedAdManager.showVideoAd(act)
                if (!showedVideo) {
                    InterstitialAdManager.showInterstitialAd(act)
                }
            }
        }
    }

    val displayMessages = remember(uiState.messages) {
        uiState.messages.filter { it.text.isNotBlank() }.deduplicate()
    }

    var isInitialScrollDone by remember { mutableStateOf(false) }
    var lastKnownMessageId by remember { mutableStateOf<String?>(null) }
    var previousMessagesSize by remember { mutableStateOf(0) }

    LaunchedEffect(displayMessages) {
        if (displayMessages.isEmpty()) return@LaunchedEffect

        val latestMessage = displayMessages.lastOrNull()
        val latestId = latestMessage?.id

        if (!isInitialScrollDone) {
            // Initial load: jump directly to the bottom (latest message)
            listState.scrollToItem(displayMessages.size - 1)
            isInitialScrollDone = true
            lastKnownMessageId = latestId
            previousMessagesSize = displayMessages.size
        } else if (latestId != lastKnownMessageId) {
            // New message arrived at the bottom (sent or incoming via WS)
            lastKnownMessageId = latestId
            previousMessagesSize = displayMessages.size
            listState.animateScrollToItem(displayMessages.size - 1)
        } else if (displayMessages.size > previousMessagesSize) {
            // Older messages prepended at top: keep current reading position without jumping
            previousMessagesSize = displayMessages.size
        }
    }

    // Scroll-up pagination trigger: when user scrolls near top, fetch older messages
    LaunchedEffect(listState, isInitialScrollDone) {
        if (!isInitialScrollDone) return@LaunchedEffect
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { firstIndex ->
                if (firstIndex <= 2 &&
                    !uiState.isLoading &&
                    !uiState.isLoadingOlder &&
                    uiState.hasMore &&
                    displayMessages.size >= 10
                ) {
                    viewModel.loadOlderMessages()
                }
            }
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            ChatTopBar(
                modelName = resolvedName,
                scheme = scheme,
                avatarUrl = null,
                statusText = "Active now",
                isOnline = true,
                onBackClick = onBackClick,
                onProfileClick = onOpenProfile,
                onVoiceCallClick = onVoiceCallClick,
                onVideoCallClick = onVideoCallClick
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
                    if (uiState.isLoadingOlder) {
                        item(key = "loading_older_spinner") {
                            ODSBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = ODSVariables.spacingComponent2),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSLoadingSpinner(
                                    scheme = scheme,
                                    props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.SMALL)
                                )
                            }
                        }
                    }

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
                                onRetry = { viewModel.retrySendMessage(it) },
                                onViewPhotoClick = { photoMsg -> viewingPhotoMessage = photoMsg },
                                onPlayAudioClick = { audioMsg -> viewModel.playAudio(context, audioMsg) }
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
            uiState.quotaLimit?.let { quotaInfo ->
                ChatQuotaLimitBanner(
                    quotaLimitInfo = quotaInfo,
                    scheme = scheme,
                    onWatchRewardedAdClick = {
                        activity?.let { act ->
                            val rewardAmount = quotaInfo.rewardMessages ?: 5
                            val showed = RewardedAdManager.showRewardedAd(
                                activity = act,
                                onRewardEarned = {
                                    viewModel.claimAdReward(rewardAmount)
                                },
                                onAdDismissed = {
                                    RewardedAdManager.loadRewardedAd(act)
                                }
                            )
                            if (!showed) {
                                RewardedAdManager.loadRewardedAd(act)
                                viewModel.claimAdReward(rewardAmount)
                            }
                        }
                    },
                    onBuyMessagesClick = {
                        viewModel.openPaywall()
                    }
                )
            }

            ChatInputBar(
                inputText = uiState.inputText,
                scheme = scheme,
                onInputTextChanged = { viewModel.onInputTextChanged(it) },
                onSendMessage = {
                    if (uiState.inputText.trim().isNotBlank() && uiState.quotaLimit?.isLimitReached != true) {
                        viewModel.sendMessage(modelId)
                        isEmojiPickerVisible = false
                    }
                },
                isEmojiPickerVisible = isEmojiPickerVisible,
                isLimitReached = uiState.quotaLimit?.isLimitReached == true,
                onEmojiToggle = {
                    isEmojiPickerVisible = !isEmojiPickerVisible
                },
                onPickPhotoClick = {
                    isEmojiPickerVisible = false
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onInputClick = {
                    isEmojiPickerVisible = false
                },
                isRecordingAudio = isRecordingAudio,
                recordingDurationSeconds = recordingDurationSeconds,
                onStartRecordAudio = {
                    isEmojiPickerVisible = false
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission) {
                        val started = audioRecorder.startRecording()
                        if (started) {
                            isRecordingAudio = true
                            recordingDurationSeconds = 0
                        }
                    } else {
                        recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                },
                onCancelRecordAudio = {
                    audioRecorder.cancelRecording()
                    isRecordingAudio = false
                    recordingDurationSeconds = 0
                },
                onSendRecordedAudio = {
                    val file = audioRecorder.stopRecording()
                    isRecordingAudio = false
                    recordingDurationSeconds = 0
                    if (file != null) {
                        viewModel.sendAudio(file, modelId)
                    }
                }
            )
            if (isEmojiPickerVisible) {
                EmojiPickerView(
                    scheme = scheme,
                    onEmojiSelected = { emoji ->
                        viewModel.onInputTextChanged(uiState.inputText + emoji)
                    },
                    onBackspace = {
                        val text = uiState.inputText
                        if (text.isNotEmpty()) {
                            val newText = if (text.length >= 2 && Character.isSurrogatePair(
                                    text[text.length - 2],
                                    text[text.length - 1]
                                )
                            ) {
                                text.dropLast(2)
                            } else {
                                text.dropLast(1)
                            }
                            viewModel.onInputTextChanged(newText)
                        }
                    }
                )
            }

            viewingPhotoMessage?.let { msg ->
                val pUrl = msg.mediaUrl?.takeIf { it.isNotBlank() } ?: msg.text
                ViewOncePhotoDialog(
                    photoUrl = pUrl,
                    onDismiss = {
                        viewModel.consumeViewOncePhoto(msg)
                        viewingPhotoMessage = null
                    },
                    scheme = scheme
                )
            }

            if (uiState.showPaywallSheet) {
                ChatPaywallDialog(
                    packs = uiState.walletPacks,
                    isVerifying = uiState.isVerifyingPayment,
                    errorMessage = uiState.paymentError,
                    scheme = scheme,
                    onBuyClick = { pack ->
                        activity?.let { act ->
                            viewModel.launchPurchase(act, pack)
                        }
                    },
                    onDismiss = {
                        viewModel.dismissPaywall()
                    }
                )
            }
        }
    }
}

private fun List<ChatMessage>.deduplicate(): List<ChatMessage> {
    val result = mutableListOf<ChatMessage>()
    val seenIds = mutableSetOf<String>()
    val seenClientMsgIds = mutableSetOf<String>()

    for (msg in this) {
        val hasSameId = msg.id.isNotBlank() && seenIds.contains(msg.id)
        val hasSameClientId =
            !msg.clientMessageId.isNullOrBlank() && seenClientMsgIds.contains(msg.clientMessageId)

        if (!hasSameId && !hasSameClientId) {
            result.add(msg)
            if (msg.id.isNotBlank()) seenIds.add(msg.id)
            if (!msg.clientMessageId.isNullOrBlank()) seenClientMsgIds.add(msg.clientMessageId!!)
        }
    }
    return result
}

@Composable
fun ChatQuotaLimitBanner(
    quotaLimitInfo: com.app.screentime.core.model.QuotaLimitInfo,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    onWatchRewardedAdClick: (() -> Unit)? = null,
    onBuyMessagesClick: (() -> Unit)? = null
) {
    if (quotaLimitInfo.isLimitReached) {
        val gateType = quotaLimitInfo.gateType?.lowercase()
        val showAd = (gateType == null || gateType == "rewarded_video" || gateType == "rewarded_or_billing" || gateType == "all") && onWatchRewardedAdClick != null
        val showBilling = (gateType == null || gateType == "billing" || gateType == "rewarded_or_billing" || gateType == "all") && onBuyMessagesClick != null

        ODSBox(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ODSVariables.spacingLayout1,
                    vertical = ODSVariables.spacingComponent2
                ),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            border = ODSBorder(
                width = ODSVariables.strokes1,
                colorList = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
            ),
            padding = ODSPadding(
                horizontal = ODSVariables.spacingComponent4,
                vertical = ODSVariables.spacingComponent3
            )
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                gap = ODSVariables.spacingComponent3
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = com.telekom.odsystem.R.drawable.ic_alert_triangle,
                        contentDescription = "Limit Warning"
                    ),
                    tint = scheme.functionalDestructiveStandard.getColor(),
                    modifier = Modifier.size(ODSVariables.sizingComponent8)
                )
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = ODSVariables.spacingComponent1
                ) {
                    ODSText(
                        text = if (gateType == "billing") "Premium Chat Access" else "Message Limit Reached",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.functionalDestructiveStandard
                    )
                    ODSText(
                        text = quotaLimitInfo.limitMessage ?: "You have reached your messaging quota for today.",
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = ODSVariables.spacingComponent2),
                        gap = ODSVariables.spacingComponent2
                    ) {
                        if (showAd) {
                            val rewardCount = quotaLimitInfo.rewardMessages ?: 5
                            ODSBox(
                                modifier = Modifier
                                    .clickable { onWatchRewardedAdClick?.invoke() },
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                                padding = ODSPadding(
                                    horizontal = ODSVariables.spacingComponent3,
                                    vertical = ODSVariables.spacingComponent2
                                )
                            ) {
                                ODSText(
                                    text = "🎬 Watch Ad (+$rewardCount)",
                                    style = ODSTextStyles.bodySBold,
                                    color = scheme.basicTextDominant
                                )
                            }
                        }

                        if (showBilling) {
                            ODSBox(
                                modifier = Modifier
                                    .clickable { onBuyMessagesClick?.invoke() },
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                                border = ODSBorder(
                                    width = 1.dp,
                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                                ),
                                padding = ODSPadding(
                                    horizontal = ODSVariables.spacingComponent3,
                                    vertical = ODSVariables.spacingComponent2
                                )
                            ) {
                                ODSText(
                                    text = "💎 Buy / Top-up",
                                    style = ODSTextStyles.bodySBold,
                                    color = scheme.basicText
                                )
                            }
                        }
                    }
                }
            }
        }
    } else if (quotaLimitInfo.quotaRemaining != null) {
        ODSBox(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ODSVariables.spacingLayout1,
                    vertical = ODSVariables.spacingComponent1
                ),
            contentAlignment = Alignment.Center
        ) {
            ODSRow(
                padding = ODSPadding(
                    horizontal = ODSVariables.spacingComponent4,
                    vertical = ODSVariables.spacingComponent2
                ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                verticalAlignment = Alignment.CenterVertically,
                gap = ODSVariables.spacingComponent2
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = com.telekom.odsystem.R.drawable.ic_info,
                        contentDescription = "Quota Info"
                    ),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(ODSVariables.sizingComponent6)
                )
                val remainingText = if (quotaLimitInfo.quotaLimit != null) {
                    "${quotaLimitInfo.quotaRemaining} of ${quotaLimitInfo.quotaLimit} messages remaining"
                } else {
                    "${quotaLimitInfo.quotaRemaining} messages remaining today"
                }
                ODSText(
                    text = remainingText,
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicTextDominant
                )
            }
        }
    }
}

