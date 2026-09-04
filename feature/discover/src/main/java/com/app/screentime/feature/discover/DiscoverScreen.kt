package com.app.screentime.feature.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R as ConfigR
import com.app.screentime.core.model.ModelProfile
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.atoms.skeleton.ODSSkeleton
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonProps
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.flow.distinctUntilChanged

sealed class StaggeredBlock {
    data class SplitLeftTall(
        val tall: ModelProfile,
        val top: ModelProfile,
        val bottom: ModelProfile
    ) : StaggeredBlock()

    data class FullWidth(val model: ModelProfile) : StaggeredBlock()
    data class TwoHalfCards(val left: ModelProfile, val right: ModelProfile) : StaggeredBlock()
    data class SplitRightTall(
        val top: ModelProfile,
        val bottom: ModelProfile,
        val tall: ModelProfile
    ) : StaggeredBlock()
}

private fun buildStaggeredBlocks(models: List<ModelProfile>): List<StaggeredBlock> {
    val blocks = mutableListOf<StaggeredBlock>()
    var index = 0
    var patternIndex = 0
    while (index < models.size) {
        val remaining = models.size - index
        when (patternIndex % 4) {
            0 -> {
                if (remaining >= 3) {
                    blocks.add(
                        StaggeredBlock.SplitLeftTall(
                            models[index],
                            models[index + 1],
                            models[index + 2]
                        )
                    )
                    index += 3
                } else if (remaining == 2) {
                    blocks.add(StaggeredBlock.TwoHalfCards(models[index], models[index + 1]))
                    index += 2
                } else {
                    blocks.add(StaggeredBlock.FullWidth(models[index]))
                    index += 1
                }
            }

            1 -> {
                blocks.add(StaggeredBlock.FullWidth(models[index]))
                index += 1
            }

            2 -> {
                if (remaining >= 2) {
                    blocks.add(StaggeredBlock.TwoHalfCards(models[index], models[index + 1]))
                    index += 2
                } else {
                    blocks.add(StaggeredBlock.FullWidth(models[index]))
                    index += 1
                }
            }

            3 -> {
                if (remaining >= 3) {
                    blocks.add(
                        StaggeredBlock.SplitRightTall(
                            models[index],
                            models[index + 1],
                            models[index + 2]
                        )
                    )
                    index += 3
                } else if (remaining == 2) {
                    blocks.add(StaggeredBlock.TwoHalfCards(models[index], models[index + 1]))
                    index += 2
                } else {
                    blocks.add(StaggeredBlock.FullWidth(models[index]))
                    index += 1
                }
            }
        }
        patternIndex++
    }
    return blocks
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onNavigateToModelProfile: (String) -> Unit = {},
    onNavigateToSocialDemo: () -> Unit = {},
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    var selectedTab by remember { mutableStateOf("Discovery") }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedDistance by remember { mutableStateOf("Any Distance") }
    var showFilterSheet by remember { mutableStateOf(false) }

    val sourceModels = uiState.models

    val filtered = remember(sourceModels, selectedTab, selectedFilter, selectedDistance) {
        val baseList = if (selectedTab == "Matched") {
            sourceModels.filter { uiState.favoriteModelIds.contains(it.id) || it.rating >= 4.9f }
        } else {
            sourceModels
        }

        baseList.filter { model ->
            val matchesFilter = when {
                selectedFilter.startsWith("New") -> model.id in listOf(
                    "m1",
                    "m2",
                    "m6",
                    "m8",
                    "1",
                    "2"
                )

                selectedFilter.startsWith("Nearby") -> model.distance.contains("m") && !model.distance.contains(
                    "km"
                )

                selectedFilter.startsWith("Online") -> model.isOnline
                selectedFilter.startsWith("Top") -> model.rating >= 4.9f
                else -> true
            }

            val matchesDistance = when (selectedDistance) {
                "Within 5 km" -> model.distance.startsWith("3") || (model.distance.contains("m") && !model.distance.contains(
                    "km"
                ))

                "Within 10 km" -> model.distance.startsWith("3") || model.distance.startsWith("8") || model.distance.startsWith(
                    "1"
                )

                "Within 25 km" -> !model.distance.startsWith("4") && !model.distance.startsWith("5")
                else -> true
            }

            matchesFilter && matchesDistance
        }
    }

    val staggeredBlocks = remember(filtered) {
        buildStaggeredBlocks(filtered)
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val total = listState.layoutInfo.totalItemsCount
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            total > 0 && last >= total - 2
        }
            .distinctUntilChanged()
            .collect { shouldLoad ->
                if (shouldLoad && !uiState.isLoadingMore && uiState.hasMorePages && !uiState.isLoading) {
                    viewModel.loadNextPage()
                }
            }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refresh() },
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // 1. Top Location Bar
            item(key = "top_location_header", contentType = "Header") {
                DiscoverTopBar(
                    scheme = scheme,
                    hasActiveFilter = selectedFilter != "All" || selectedDistance != "Any Distance",
                    onOpenFilter = { showFilterSheet = true }
                )
            }

            // 2. Segmented Dual Tabs (12.dp corners)
            item(key = "dual_tab_control", contentType = "Tabs") {
                DiscoverSegmentedTabs(
                    selectedTab = selectedTab,
                    scheme = scheme,
                    onTabSelected = { selectedTab = it }
                )
            }

            // 3. Content: Loading Shimmer Bento Grid vs Empty State vs Dynamic Bento Cards
            if (uiState.isLoading || (uiState.models.isEmpty() && uiState.error == null && !uiState.isRefreshing)) {
                item(key = "discover_bento_shimmer", contentType = "Shimmer") {
                    DiscoverBentoShimmer(scheme = scheme)
                }
            } else if (filtered.isEmpty()) {
                item(key = "discover_empty_state", contentType = "EmptyState") {
                    DiscoverEmptyState(
                        scheme = scheme,
                        onRefresh = { viewModel.loadInitialModels() }
                    )
                }
            } else {
                items(
                    items = staggeredBlocks,
                    key = { block ->
                        when (block) {
                            is StaggeredBlock.SplitLeftTall -> "slt_${block.tall.id}_${block.top.id}_${block.bottom.id}"
                            is StaggeredBlock.FullWidth -> "fw_${block.model.id}"
                            is StaggeredBlock.TwoHalfCards -> "thc_${block.left.id}_${block.right.id}"
                            is StaggeredBlock.SplitRightTall -> "srt_${block.tall.id}_${block.top.id}_${block.bottom.id}"
                        }
                    },
                    contentType = { "StaggeredBlock" }
                ) { block ->
                    when (block) {
                        is StaggeredBlock.SplitLeftTall -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                StaggeredModelCard(
                                    model = block.tall,
                                    height = 360.dp,
                                    scheme = scheme,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onNavigateToModelProfile(block.tall.id) })
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    StaggeredModelCard(
                                        model = block.top,
                                        height = 175.dp,
                                        scheme = scheme,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onNavigateToModelProfile(block.top.id) })
                                    StaggeredModelCard(
                                        model = block.bottom,
                                        height = 175.dp,
                                        scheme = scheme,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onNavigateToModelProfile(block.bottom.id) })
                                }
                            }
                        }

                        is StaggeredBlock.FullWidth -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                StaggeredModelCard(
                                    model = block.model,
                                    height = 280.dp,
                                    scheme = scheme,
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { onNavigateToModelProfile(block.model.id) })
                            }
                        }

                        is StaggeredBlock.TwoHalfCards -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                StaggeredModelCard(
                                    model = block.left,
                                    height = 230.dp,
                                    scheme = scheme,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onNavigateToModelProfile(block.left.id) })
                                StaggeredModelCard(
                                    model = block.right,
                                    height = 230.dp,
                                    scheme = scheme,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onNavigateToModelProfile(block.right.id) })
                            }
                        }

                        is StaggeredBlock.SplitRightTall -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    StaggeredModelCard(
                                        model = block.top,
                                        height = 175.dp,
                                        scheme = scheme,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onNavigateToModelProfile(block.top.id) })
                                    StaggeredModelCard(
                                        model = block.bottom,
                                        height = 175.dp,
                                        scheme = scheme,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onNavigateToModelProfile(block.bottom.id) })
                                }
                                StaggeredModelCard(
                                    model = block.tall,
                                    height = 360.dp,
                                    scheme = scheme,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onNavigateToModelProfile(block.tall.id) })
                            }
                        }
                    }
                }
            }

            if (uiState.isLoadingMore) {
                item(key = "pagination_loader", contentType = "Loader") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSLoadingSpinner(
                            scheme = scheme,
                            props = ODSLoadingSpinnerProps(
                                size = ODSLoadingSpinnerSize.SMALL,
                                variant = ODSLoadingSpinnerVariant.STANDARD
                            )
                        )
                    }
                }
            }
        }

        if (showFilterSheet) {
            DiscoverFilterBottomSheet(
                scheme = scheme,
                selectedTab = selectedTab,
                selectedFilter = selectedFilter,
                selectedDistance = selectedDistance,
                onTabSelected = { selectedTab = it },
                onFilterSelected = { selectedFilter = it },
                onDistanceSelected = { selectedDistance = it },
                onReset = {
                    selectedFilter = "All"
                    selectedDistance = "Any Distance"
                    showFilterSheet = false
                },
                onApply = { showFilterSheet = false },
                onDismiss = { showFilterSheet = false }
            )
        }
    }
}

@Composable
private fun DiscoverTopBar(
    scheme: ODSTheme,
    hasActiveFilter: Boolean = false,
    onOpenFilter: () -> Unit = {}
) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSBox(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(drawableRes = R.drawable.notification),
                tint = scheme.basicText.getColor()
            )
            ODSBox(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
                    .clip(CircleShape),
                background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
            ) {}
        }
        ODSColumn(horizontalAlignment = Alignment.CenterHorizontally, gap = 2.dp) {
            ODSRow(verticalAlignment = Alignment.CenterVertically, gap = 4.dp) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.location),
                    tint = scheme.basicText.getColor(),
                    height = 20.dp,
                    width = 20.dp
                )
                ODSText(
                    text = "Bekasi, Jawa Barat",
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicText
                )
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Outlined.KeyboardArrowDown),
                    tint = scheme.basicTextRecessive.getColor()
                )
            }
        }
        ODSBox(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onOpenFilter),
            background = listOf(ODSColorModel(hexColor = if (hasActiveFilter) scheme.basicAccent else scheme.basicBackgroundCard)),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(drawableRes = R.drawable.filter),
                tint = scheme.basicText.getColor(),
                height = 20.dp,
                width = 20.dp
            )
        }
    }
}

@Composable
private fun DiscoverSegmentedTabs(
    selectedTab: String,
    scheme: ODSTheme,
    onTabSelected: (String) -> Unit
) {
    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 12.dp),
        padding = ODSPadding(all = 4.dp)
    ) {
        ODSRow(modifier = Modifier.fillMaxWidth(), gap = 4.dp) {
            listOf("Discovery", "Matched").forEach { tab ->
                val isSelected = selectedTab == tab
                ODSBox(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) },
                    background = listOf(ODSColorModel(hexColor = if (isSelected) scheme.basicBackground else scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = 10.dp),
                    border = if (isSelected) ODSBorder(
                        width = 1.dp,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                    ) else null,
                    padding = ODSPadding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = tab,
                        style = if (isSelected) ODSTextStyles.bodySBold else ODSTextStyles.bodySRegular,
                        color = if (isSelected) scheme.basicText else scheme.basicTextRecessive
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscoverBentoShimmer(scheme: ODSTheme) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Pattern 0: SplitLeftTall skeleton (Left: 360dp, Right: two 175dp cards)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DiscoverBentoCardSkeleton(
                height = 360.dp,
                scheme = scheme,
                modifier = Modifier.weight(1f)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DiscoverBentoCardSkeleton(
                    height = 175.dp,
                    scheme = scheme,
                    modifier = Modifier.fillMaxWidth()
                )
                DiscoverBentoCardSkeleton(
                    height = 175.dp,
                    scheme = scheme,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Pattern 1: FullWidth skeleton (280dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            DiscoverBentoCardSkeleton(
                height = 280.dp,
                scheme = scheme,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Pattern 2: TwoHalfCards skeleton (230dp each)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DiscoverBentoCardSkeleton(
                height = 230.dp,
                scheme = scheme,
                modifier = Modifier.weight(1f)
            )
            DiscoverBentoCardSkeleton(
                height = 230.dp,
                scheme = scheme,
                modifier = Modifier.weight(1f)
            )
        }

        // Pattern 3: SplitRightTall skeleton (Left: two 175dp cards, Right: 360dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DiscoverBentoCardSkeleton(
                    height = 175.dp,
                    scheme = scheme,
                    modifier = Modifier.fillMaxWidth()
                )
                DiscoverBentoCardSkeleton(
                    height = 175.dp,
                    scheme = scheme,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            DiscoverBentoCardSkeleton(
                height = 360.dp,
                scheme = scheme,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DiscoverBentoCardSkeleton(
    height: Dp,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 12.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        clipContent = true
    ) {
        // Base shimmer card placeholder taking the full card surface
        ODSSkeleton(
            modifier = Modifier.fillMaxSize(),
            scheme = scheme,
            props = ODSSkeletonProps(variant = ODSSkeletonVariant.LARGE)
        )

        // Overlay with bottom placeholder row mirroring model info & action buttons
        ODSRow(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            ODSColumn(gap = 6.dp) {
                ODSSkeleton(
                    modifier = Modifier
                        .size(width = 80.dp, height = 14.dp),
                    scheme = scheme,
                    props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
                )
                ODSSkeleton(
                    modifier = Modifier
                        .size(width = 48.dp, height = 10.dp),
                    scheme = scheme,
                    props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
                )
            }

            ODSRow(gap = 6.dp, verticalAlignment = Alignment.CenterVertically) {
                ODSSkeleton(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape),
                    scheme = scheme,
                    props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
                )
                ODSSkeleton(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape),
                    scheme = scheme,
                    props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
                )
            }
        }
    }
}

@Composable
private fun DiscoverCardSkeleton(
    modifier: Modifier = Modifier,
    height: Dp = 230.dp,
    scheme: ODSTheme
) {
    DiscoverBentoCardSkeleton(
        height = height,
        scheme = scheme,
        modifier = modifier
    )
}

@Composable
private fun DiscoverEmptyState(
    scheme: ODSTheme,
    onRefresh: () -> Unit
) {
    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        ODSColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = 16.dp
        ) {
            ODSBox(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.discovery),
                    tint = scheme.basicAccent.getColor(),
                    height = 32.dp,
                    width = 32.dp
                )
            }
            ODSText(
                text = stringResource(ConfigR.string.discover_no_models_found),
                style = ODSTextStyles.titleS,
                color = scheme.basicText
            )
            ODSText(
                text = stringResource(ConfigR.string.discover_no_models_desc),
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )
            ODSButton(
                onClick = onRefresh,
                scheme = scheme,
                props = ODSButtonProps(
                    label = stringResource(ConfigR.string.discover_refresh),
                    variant = ODSButtonVariant.SECONDARY,
                    size = ODSButtonSize.SMALL
                )
            )
        }
    }
}
