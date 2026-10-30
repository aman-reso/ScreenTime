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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.sp
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
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
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
 * Designed with modern aesthetic matching user's requested layout:
 * - Friendly greeting ("Hello [Name] 👋") & Live counter widget
 * - Tagline ("TALK & CONNECT")
 * - Big bold headline ("Share Your Feelings", "Talk to someone real")
 * - Search bar ("Search companions, moods, languages...")
 * - Quick category toggle chips
 * - 2-span Staggered Grid with rich visual cards, status indicators, and call buttons
 * - ODS Design System components used throughout
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

    val liveCount = remember(uiState.models) {
        uiState.models.count { it.isOnline }
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
            // ── Top Header Section: Greeting & Live Counter Widget ──
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSText(
                    text = "Hello ${uiState.userName} 👋",
                    style = ODSTextStyles.titleS,
                    color = scheme.basicText
                )

                ODSRow(
                    gap = 8.dp,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Live Status Widget (matching weather pill from inspiration layout)
                    ODSBox(
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                        cornerRadius = ODSCorners(all = 20.dp),
                        padding = ODSPadding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        ODSRow(
                            gap = 6.dp,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSBox(
                                width = 8.dp,
                                height = 8.dp,
                                cornerRadius = ODSCorners(all = 4.dp),
                                background = listOf(ODSColorModel(hexColor = HexColor("#10B981")))
                            )
                            ODSText(
                                text = if (liveCount > 0) "$liveCount Live" else "Offline",
                                style = ODSTextStyles.microcopyBold,
                                color = scheme.basicText
                            )
                        }
                    }

                    // My Rates Button (only visible if logged-in user is a registered model)
                    if (uiState.isCurrentUserModel) {
                        ODSBox(
                            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                            cornerRadius = ODSCorners(all = 20.dp),
                            padding = ODSPadding(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.clickable { showRateDialog = true }
                        ) {
                            ODSText(
                                text = "⚙️ Rates",
                                style = ODSTextStyles.microcopyBold,
                                color = HexColor("#FFFFFF")
                            )
                        }
                    }

                    // Refresh Button
                    ODSBox(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(scheme.basicBackgroundSubtle.getColor())
                            .clickable { viewModel.refresh() },
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = "🔄",
                            style = ODSTextStyles.bodySBold
                        )
                    }
                }
            }

            // ── Inspiring Headline & Tagline (as requested: "Share Your Feelings", "Talk") ──
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                gap = 2.dp
            ) {
                ODSRow(
                    gap = 6.dp,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSText(
                        text = "💬",
                        style = ODSTextStyles.microcopyBold
                    )
                    ODSText(
                        text = "REAL CONVERSATIONS",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicAccent
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                ODSText(
                    text = "Share Your Feelings",
                    style = ODSTextStyles.titleL,
                    color = scheme.basicText
                )

                ODSText(
                    text = "Talk to someone special right now",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextRecessive
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── ODS Search Bar ──
            ODSSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                scheme = scheme,
                props = ODSSearchBarProps(
                    placeholder = "Search companions, moods, languages...",
                    input = searchQuery
                ),
                onValueChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ── Filter Chips (Scrollable Row) ──
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

            Spacer(modifier = Modifier.height(10.dp))

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
                            labelText = "Connecting with companions..."
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
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
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
 * 2-Span Masonry Card for Each Model
 * Follows the aesthetics of the inspiration layout with badges, gradients, and action buttons.
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
    // Staggered dynamic heights for natural masonry feel
    val cardHeight = when (index % 4) {
        0 -> 245.dp
        1 -> 285.dp
        2 -> 265.dp
        else -> 255.dp
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onCardClick() },
        shape = RoundedCornerShape(18.dp),
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
            // ── Background Photo ──
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

            // ── Smooth Dark Gradient for Text Legibility ──
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.25f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.45f),
                                Color.Black.copy(alpha = 0.88f)
                            ),
                            startY = 0f
                        )
                    )
            )

            // ── Top Bar: Status Badge (Left) & Star/Favorite Icon (Right) ──
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Online/Location Badge
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

                // Glassmorphic Star / Favorite Circle
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

            // ── Bottom Information & Calling Action Buttons ──
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
                    style = ODSTextStyles.bodyLBold,
                    color = HexColor("#FFFFFF"),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Rates transparent row
                ODSText(
                    text = "📞 🪙${model.audioRate.toInt()}/m  •  📹 🪙${model.videoRate.toInt()}/m",
                    style = ODSTextStyles.microcopyMedium,
                    color = HexColor("#E2E8F0"),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Instant Call Buttons (Audio & Video)
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 6.dp
                ) {
                    // Audio Call Action
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

                    // Video Call Action
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

                // Carousel Dots Indicator (as seen at the bottom of the reference cards)
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
