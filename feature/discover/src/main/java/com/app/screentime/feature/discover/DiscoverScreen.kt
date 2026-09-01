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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.ModelProfile
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
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

private val defaultDummyModels = listOf(
    ModelProfile(
        id = "m1",
        name = "Riya Gosh",
        age = 23,
        distance = "40km",
        tags = listOf("Sing", "Friends"),
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80",
        rating = 4.9f,
        isOnline = true
    ),
    ModelProfile(
        id = "m2",
        name = "Sullyon Nake",
        age = 22,
        distance = "8km",
        tags = listOf("Sing", "Friends"),
        avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=800&q=80",
        rating = 4.8f,
        isOnline = true
    ),
    ModelProfile(
        id = "m3",
        name = "Kang Seulgi",
        age = 24,
        distance = "3km",
        tags = listOf("Sing", "Friends"),
        avatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=800&q=80",
        rating = 5.0f,
        isOnline = true
    ),
    ModelProfile(
        id = "m4",
        name = "Jeon Jung",
        age = 25,
        distance = "20km",
        tags = listOf("Sing", "Friends"),
        avatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=800&q=80",
        rating = 4.9f,
        isOnline = false
    ),
    ModelProfile(
        id = "m5",
        name = "Elena Rostova",
        age = 21,
        distance = "45km",
        tags = listOf("Dance", "Music"),
        avatarUrl = "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?auto=format&fit=crop&w=800&q=80",
        rating = 4.9f,
        isOnline = true
    ),
    ModelProfile(
        id = "m6",
        name = "Mina Thorne",
        age = 23,
        distance = "30km",
        tags = listOf("Chat", "Travel"),
        avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=800&q=80",
        rating = 4.9f,
        isOnline = true
    ),
    ModelProfile(
        id = "m7",
        name = "Jessica Alba",
        age = 26,
        distance = "12km",
        tags = listOf("Acting", "Cosplay"),
        avatarUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&w=800&q=80",
        rating = 4.8f,
        isOnline = true
    ),
    ModelProfile(
        id = "m8",
        name = "Sophia Chen",
        age = 22,
        distance = "15km",
        tags = listOf("Vlog", "Coffee"),
        avatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=800&q=80",
        rating = 4.9f,
        isOnline = true
    )
)

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

    val sourceModels = remember(uiState.models) {
        if (uiState.models.isNotEmpty()) uiState.models else defaultDummyModels
    }

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

            // 3. Staggered Dynamic Bento Cards (12.dp corners)
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

            if (uiState.isLoadingMore) {
                item(key = "pagination_grid_skel", contentType = "SkeletonGrid") {
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        gap = 10.dp
                    ) {
                        DiscoverCardSkeleton(modifier = Modifier.weight(1f), scheme = scheme)
                        DiscoverCardSkeleton(modifier = Modifier.weight(1f), scheme = scheme)
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
private fun DiscoverCardSkeleton(
    modifier: Modifier = Modifier,
    scheme: ODSTheme
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 12.dp),
        padding = ODSPadding(all = 12.dp)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize(),
            gap = 10.dp,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ODSSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                scheme = scheme,
                props = ODSSkeletonProps(variant = ODSSkeletonVariant.LARGE)
            )
            ODSSkeleton(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(18.dp),
                scheme = scheme,
                props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
            )
        }
    }
}
