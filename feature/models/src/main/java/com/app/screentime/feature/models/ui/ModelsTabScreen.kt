package com.app.screentime.feature.models.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.models.model.ModelGirl
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.carouseldot.ODSCarouselDot
import com.telekom.odsystem.atoms.carouseldot.ODSCarouselDotProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.togglechip.ODSToggleChip
import com.telekom.odsystem.atoms.togglechip.ODSToggleChipProps
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.molecules.searchbar.ODSSearchBar
import com.telekom.odsystem.molecules.searchbar.ODSSearchBarProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Filter Categories for Models
 */
private enum class ModelFilter(val label: String) {
    ALL("✨ All"),
    LIVE_NOW("🟢 Live Now"),
    AUDIO_CALL("📞 Audio"),
    VIDEO_CALL("📹 Video"),
    TOP_PICKS("⭐ Top Picks")
}

/**
 * ModelsTabScreen
 *
 * Clean, consistent UI with unified 16.dp horizontal padding and 12.dp item gaps.
 * Top area features an ODS Search Bar and quick filter chips.
 * Displays models in a 2-span Staggered Masonry Grid format.
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
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(ModelFilter.ALL) }

    LaunchedEffect(Unit) {
        viewModel.loadModels()
    }

    // Filter models based on search query and active filter chip
    val filteredModels = remember(uiState.models, searchQuery, selectedFilter) {
        uiState.models.filter { model ->
            val matchesSearch = searchQuery.isBlank() ||
                    model.name.contains(searchQuery, ignoreCase = true) ||
                    model.city.contains(searchQuery, ignoreCase = true) ||
                    model.bio.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                ModelFilter.ALL -> true
                ModelFilter.LIVE_NOW -> model.isOnline
                ModelFilter.AUDIO_CALL -> model.audioRate > 0
                ModelFilter.VIDEO_CALL -> model.videoRate > 0
                ModelFilter.TOP_PICKS -> model.audioRate <= 20
            }

            matchesSearch && matchesFilter
        }
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
            Spacer(modifier = Modifier.height(10.dp))

            // ── Top Search Bar Row (Clean & Prominent) ──
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                gap = 8.dp,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSSearchBar(
                    modifier = Modifier.weight(1f),
                    scheme = scheme,
                    props = ODSSearchBarProps(
                        placeholder = "Search companions, cities...",
                        input = searchQuery
                    ),
                    onValueChange = { searchQuery = it }
                )

                // Model rates config shortcut (only for registered models)
                if (uiState.isCurrentUserModel) {
                    Box(
                        modifier = Modifier
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(scheme.basicAccent.getColor())
                            .clickable { showRateDialog = true }
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = "⚙️ Rates",
                            style = ODSTextStyles.microcopyBold,
                            color = HexColor("#FFFFFF")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── Filter Chips Row ──
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ModelFilter.values()) { filter ->
                    ODSToggleChip(
                        scheme = scheme,
                        props = ODSToggleChipProps(
                            label = filter.label,
                            selected = selectedFilter == filter
                        ),
                        onToggle = { selectedFilter = filter }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Content Area: 2-Span Staggered Grid ──
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
                            labelText = "Loading companions..."
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
                    ODSColumn(
                        modifier = Modifier.padding(24.dp),
                        gap = 12.dp,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ODSText(
                            text = "Failed to load companions",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = uiState.error ?: "Please check your network and retry",
                            style = ODSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
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
            } else if (filteredModels.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    ODSColumn(
                        modifier = Modifier.padding(24.dp),
                        gap = 10.dp,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ODSText(
                            text = "✨ No companions found",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = "Try adjusting your search or filters to see more profiles.",
                            style = ODSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                        ODSButton(
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Reset Filters",
                                buttonType = ODSButtonButtonType.STANDARD,
                                variant = ODSButtonVariant.SECONDARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = {
                                searchQuery = ""
                                selectedFilter = ModelFilter.ALL
                                viewModel.refresh()
                            }
                        )
                    }
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp
                ) {
                    itemsIndexed(
                        items = filteredModels,
                        key = { _, model -> model.id }
                    ) { index, model ->
                        ModelMasonryCard(
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

        // ── Model Calling Rate Configuration Bottom Sheet ──
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
 * 2-Span Masonry Card for each Model with consistent padding, typography, and action buttons
 */
@Composable
private fun ModelMasonryCard(
    model: ModelGirl,
    index: Int,
    scheme: ODSTheme,
    onCardClick: () -> Unit,
    onAudioCallClick: () -> Unit,
    onVideoCallClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Dynamic height based on index for true masonry layout
    val cardHeight = when (index % 4) {
        0 -> 245.dp
        1 -> 285.dp
        2 -> 265.dp
        else -> 255.dp
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
        ) {
            // ── Photo Background ──
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
                    ODSText(
                        text = model.name.take(1).uppercase(),
                        style = ODSTextStyles.titleL,
                        color = scheme.basicTextRecessive
                    )
                }
            }

            // ── Consistent Dark Gradient for Text Contrast ──
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.45f),
                                Color.Black.copy(alpha = 0.88f)
                            ),
                            startY = 0f
                        )
                    )
            )

            // ── Top Bar: Online/City Badge (Left) & Star Favorite (Right) ──
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Online/Offline Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (model.isOnline) Color(0xCC10B981) else Color(0x99000000)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    ODSRow(
                        gap = 4.dp,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (model.isOnline) Color.White else Color.Gray)
                        )
                        ODSText(
                            text = if (model.isOnline) "Live" else if (model.city.isNotBlank()) model.city else "Offline",
                            style = ODSTextStyles.microcopyBold,
                            color = HexColor("#FFFFFF"),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Star Badge
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "⭐",
                        style = ODSTextStyles.microcopyMedium
                    )
                }
            }

            // ── Bottom Information & Direct Call Actions ──
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                gap = 4.dp
            ) {
                // Name & Age
                ODSText(
                    text = "${model.name}, ${model.age}",
                    style = ODSTextStyles.bodyMBold,
                    color = HexColor("#FFFFFF"),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Call Rates
                ODSText(
                    text = "📞 🪙${model.audioRate.toInt()}/m  •  📹 🪙${model.videoRate.toInt()}/m",
                    style = ODSTextStyles.microcopyMedium,
                    color = HexColor("#E2E8F0"),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Action Call Buttons (Voice Call & Video Call)
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 6.dp
                ) {
                    // Audio Call
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF3B82F6))
                            .clickable { onAudioCallClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        ODSRow(
                            gap = 3.dp,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSText(
                                text = "📞",
                                style = ODSTextStyles.microcopyBold,
                                color = HexColor("#FFFFFF")
                            )
                            ODSText(
                                text = "Call",
                                style = ODSTextStyles.microcopyBold,
                                color = HexColor("#FFFFFF")
                            )
                        }
                    }

                    // Video Call
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF10B981))
                            .clickable { onVideoCallClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        ODSRow(
                            gap = 3.dp,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSText(
                                text = "📹",
                                style = ODSTextStyles.microcopyBold,
                                color = HexColor("#FFFFFF")
                            )
                            ODSText(
                                text = "Video",
                                style = ODSTextStyles.microcopyBold,
                                color = HexColor("#FFFFFF")
                            )
                        }
                    }
                }

                // Carousel Dots
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    gap = 4.dp
                ) {
                    ODSCarouselDot(
                        scheme = scheme,
                        props = ODSCarouselDotProps(selected = true)
                    )
                    ODSCarouselDot(
                        scheme = scheme,
                        props = ODSCarouselDotProps(selected = false)
                    )
                    ODSCarouselDot(
                        scheme = scheme,
                        props = ODSCarouselDotProps(selected = false)
                    )
                }
            }
        }
    }
}

/**
 * Bottom Sheet for Registered Models to configure incoming Audio & Video Call Rates
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
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            gap = 12.dp
        ) {
            ODSText(
                text = "Set Your Calling Rates",
                style = ODSTextStyles.titleS,
                color = scheme.basicText
            )

            ODSText(
                text = "Decide your coins/minute charged for incoming audio & video calls.",
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Audio Rate Field
            ODSText(
                text = "Audio Call Rate (Coins/Min)",
                style = ODSTextStyles.bodySBold,
                color = scheme.basicText
            )
            OutlinedTextField(
                value = audioRateInput,
                onValueChange = { audioRateInput = it.filter { ch -> ch.isDigit() } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Video Rate Field
            ODSText(
                text = "Video Call Rate (Coins/Min)",
                style = ODSTextStyles.bodySBold,
                color = scheme.basicText
            )
            OutlinedTextField(
                value = videoRateInput,
                onValueChange = { videoRateInput = it.filter { ch -> ch.isDigit() } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

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
