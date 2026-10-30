package com.app.screentime.feature.models.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.models.model.ModelGirl
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ModelsTabScreen
 *
 * Displays live models in a 2-span Staggered Grid format as requested.
 * Allows users to initiate Voice and Video calls with per-minute rate transparency.
 * Allows model users to set their own audio & video call rates.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelsTabScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    onStartVoiceCall: (modelId: String, modelName: String, ratePerMin: Double, avatarUrl: String) -> Unit = { _, _, _, _ -> },
    onStartVideoCall: (modelId: String, modelName: String, ratePerMin: Double, avatarUrl: String) -> Unit = { _, _, _, _ -> },
    onNavigateToProfile: (userId: String, userName: String) -> Unit = { _, _ -> },
    viewModel: ModelsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showRateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadModels()
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // ── Top Header Bar ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Connect Live",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = scheme.basicText.getColor()
                    )
                    Text(
                        text = "Instant audio & video calls",
                        fontSize = 13.sp,
                        color = scheme.basicTextRecessive.getColor()
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (uiState.isCurrentUserModel) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(scheme.basicAccent.getColor().copy(alpha = 0.15f))
                                .clickable { showRateDialog = true }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "⚙️ My Rates",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = scheme.basicAccent.getColor()
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(scheme.basicBackgroundSubtle.getColor())
                            .clickable { viewModel.refresh() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔄", fontSize = 16.sp)
                    }
                }
            }

            // ── Content Area ──
            if (uiState.isLoading && uiState.models.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    ODSLoadingSpinner(
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(
                            size = ODSLoadingSpinnerSize.SMALL,
                            labelText = "Loading models..."
                        )
                    )
                }
            } else if (uiState.error != null && uiState.models.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Failed to load models",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = scheme.basicText.getColor()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error ?: "Please check your network and retry",
                            fontSize = 13.sp,
                            color = scheme.basicTextRecessive.getColor()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ODSButton(
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Retry",
                                buttonType = ODSButtonButtonType.STANDARD,
                                variant = ODSButtonVariant.PRIMARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = { viewModel.loadModels() }
                        )
                    }
                }
            } else if (uiState.models.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "✨ No models available right now",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = scheme.basicText.getColor()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Check back soon for live audio & video calls!",
                            fontSize = 13.sp,
                            color = scheme.basicTextRecessive.getColor()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ODSButton(
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Refresh",
                                buttonType = ODSButtonButtonType.STANDARD,
                                variant = ODSButtonVariant.SECONDARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = { viewModel.refresh() }
                        )
                    }
                }
            } else {
                // ── 2-Span Staggered Grid as requested ──
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp
                ) {
                    itemsIndexed(
                        items = uiState.models,
                        key = { _, model -> model.id }
                    ) { index, model ->
                        ModelStaggeredCard(
                            model = model,
                            index = index,
                            scheme = scheme,
                            onCardClick = { onNavigateToProfile(model.id, model.name) },
                            onAudioCallClick = {
                                onStartVoiceCall(
                                    model.id,
                                    model.name,
                                    model.audioRate,
                                    model.avatarUrl
                                )
                            },
                            onVideoCallClick = {
                                onStartVideoCall(
                                    model.id,
                                    model.name,
                                    model.videoRate,
                                    model.avatarUrl
                                )
                            }
                        )
                    }
                }
            }
        }

        // ── Model Rate Configuration Bottom Sheet ──
        if (showRateDialog) {
            ModelRateBottomSheet(
                scheme = scheme,
                onDismiss = { showRateDialog = false },
                onSave = { audioRate, videoRate ->
                    viewModel.updateMyRates(audioRate, videoRate)
                    showRateDialog = false
                }
            )
        }
    }
}

/**
 * Individual Staggered Grid Card for each Model
 */
@Composable
private fun ModelStaggeredCard(
    model: ModelGirl,
    index: Int,
    scheme: ODSTheme,
    onCardClick: () -> Unit,
    onAudioCallClick: () -> Unit,
    onVideoCallClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Dynamic height based on index for true staggered masonry layout
    val imageHeight = when (index % 4) {
        0 -> 210.dp
        1 -> 250.dp
        2 -> 230.dp
        else -> 220.dp
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = scheme.basicBackgroundCard.getColor()
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // ── Photo with Online Status and Gradient Info Overlay ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
            ) {
                if (model.avatarUrl.isNotBlank()) {
                    AsyncImage(
                        model = model.avatarUrl,
                        contentDescription = model.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(scheme.basicBackgroundSubtle.getColor()),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = model.name.take(1).uppercase(),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = scheme.basicTextRecessive.getColor()
                        )
                    }
                }

                // Dark Gradient for readable text
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.2f),
                                    Color.Black.copy(alpha = 0.85f)
                                ),
                                startY = 80f
                            )
                        )
                )

                // Online / Offline Status Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (model.isOnline) Color(0xCC10B981) else Color(0x99000000)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (model.isOnline) Color.White else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (model.isOnline) "Live" else "Offline",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Model Name & Info at bottom of photo
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "${model.name}, ${model.age}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (model.city.isNotBlank()) {
                        Text(
                            text = "📍 ${model.city}",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // ── Call Rates & Action Buttons Section ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Rates Pill Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(scheme.basicBackgroundSubtle.getColor())
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📞 🪙 ${model.audioRate.toInt()}/m",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = scheme.basicText.getColor()
                    )
                    Text(
                        text = "📹 🪙 ${model.videoRate.toInt()}/m",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = scheme.basicAccent.getColor()
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Action Call Buttons (Voice Call & Video Call)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Voice Call Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF3B82F6))
                            .clickable { onAudioCallClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📞", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Call",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Video Call Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF10B981))
                            .clickable { onVideoCallClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📹", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Video",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Bottom Sheet for Models to configure Audio & Video Call Rates
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModelRateBottomSheet(
    scheme: ODSTheme,
    onDismiss: () -> Unit,
    onSave: (audioRate: Int, videoRate: Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var audioRateInput by remember { mutableStateOf("10") }
    var videoRateInput by remember { mutableStateOf("20") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = scheme.basicBackgroundCard.getColor()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Set Your Calling Rates",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = scheme.basicText.getColor()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Decide your coins/minute charged for incoming audio & video calls",
                fontSize = 13.sp,
                color = scheme.basicTextRecessive.getColor()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Audio Rate Field
            Text(
                text = "Audio Call Rate (Coins/Min)",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = scheme.basicText.getColor()
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = audioRateInput,
                onValueChange = { audioRateInput = it.filter { ch -> ch.isDigit() } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Video Rate Field
            Text(
                text = "Video Call Rate (Coins/Min)",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = scheme.basicText.getColor()
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = videoRateInput,
                onValueChange = { videoRateInput = it.filter { ch -> ch.isDigit() } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            ODSButton(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Save Rates",
                    buttonType = ODSButtonButtonType.STANDARD,
                    variant = ODSButtonVariant.PRIMARY,
                    size = ODSButtonSize.LARGE
                ),
                onClick = {
                    val a = audioRateInput.toIntOrNull() ?: 10
                    val v = videoRateInput.toIntOrNull() ?: 20
                    onSave(a, v)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
