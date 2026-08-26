package com.app.screentime.feature.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.ModelProfile
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.skeleton.ODSSkeleton
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonProps
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonVariant
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.*
import kotlinx.coroutines.flow.distinctUntilChanged

private val modelSecondarySchemes = listOf(
    orchidSecondaryScheme,
    cheddarSecondaryScheme,
    hummingbirdSecondaryScheme,
    macawSecondaryScheme,
    dandelionSecondaryScheme,
    aperitifSecondaryScheme
)

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

    val filterChips = remember {
        listOf("All 10", "New 10", "Nearby", "Online", "Top Rated")
    }

    val filtered = remember(uiState.models, selectedTab, selectedFilter) {
        val baseList = if (selectedTab == "Matched") {
            uiState.models.filter { uiState.favoriteModelIds.contains(it.id) || it.rating >= 4.9f }
        } else {
            uiState.models
        }

        baseList.filter { model ->
            when {
                selectedFilter.startsWith("New") -> model.id in listOf("1", "2", "6", "8")
                selectedFilter == "Nearby" -> model.distance.contains("m") && !model.distance.contains("km")
                selectedFilter == "Online" -> model.isOnline
                selectedFilter == "Top Rated" -> model.rating >= 4.9f
                else -> true
            }
        }
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

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item(key = "top_location_header", contentType = "Header") {
            Option2TopBar(scheme = scheme, onNavigateToSocialDemo = onNavigateToSocialDemo)
        }

        item(key = "dual_tab_control", contentType = "Tabs") {
            Option2SegmentedTabs(
                selectedTab = selectedTab,
                scheme = scheme,
                onTabSelected = { selectedTab = it }
            )
        }

        item(key = "filter_chips_row", contentType = "Chips") {
            Option2FilterChips(
                filterChips = filterChips,
                selectedFilter = selectedFilter,
                scheme = scheme,
                onSelectFilter = { selectedFilter = it }
            )
        }

        if (uiState.isLoading && filtered.isEmpty()) {
            items(2, key = { "init_grid_skel_$it" }, contentType = { "SkeletonGrid" }) {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    gap = 12.dp
                ) {
                    Option2CardSkeleton(modifier = Modifier.weight(1f), scheme = scheme)
                    Option2CardSkeleton(modifier = Modifier.weight(1f), scheme = scheme)
                }
            }
        }

        items(
            items = filtered.chunked(2),
            key = { row -> row.map { it.id }.joinToString("_") },
            contentType = { "ModelGridRow" }
        ) { pair ->
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                gap = 12.dp
            ) {
                val model1 = pair[0]
                val colorIndex1 = remember(model1.id) { (model1.id.hashCode() and 0x7FFFFFFF) % modelSecondarySchemes.size }
                Option2ModelCard(
                    modifier = Modifier.weight(1f),
                    model = model1,
                    scheme = scheme,
                    cardScheme = modelSecondarySchemes[colorIndex1],
                    isFavorite = uiState.favoriteModelIds.contains(model1.id),
                    onToggleFavorite = { viewModel.toggleFavorite(model1.id) },
                    onClick = { onNavigateToModelProfile(model1.id) }
                )

                if (pair.size > 1) {
                    val model2 = pair[1]
                    val colorIndex2 = remember(model2.id) { (model2.id.hashCode() and 0x7FFFFFFF) % modelSecondarySchemes.size }
                    Option2ModelCard(
                        modifier = Modifier.weight(1f),
                        model = model2,
                        scheme = scheme,
                        cardScheme = modelSecondarySchemes[colorIndex2],
                        isFavorite = uiState.favoriteModelIds.contains(model2.id),
                        onToggleFavorite = { viewModel.toggleFavorite(model2.id) },
                        onClick = { onNavigateToModelProfile(model2.id) }
                    )
                } else {
                    Spacer(Modifier.weight(1f))
                }
            }
        }

        if (uiState.isLoadingMore) {
            item(key = "pagination_grid_skel", contentType = "SkeletonGrid") {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    gap = 12.dp
                ) {
                    Option2CardSkeleton(modifier = Modifier.weight(1f), scheme = scheme)
                    Option2CardSkeleton(modifier = Modifier.weight(1f), scheme = scheme)
                }
            }
        } else if (!uiState.hasMorePages && filtered.isNotEmpty()) {
            item(key = "caught_up_footer", contentType = "Footer") {
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "✨ You've seen all matched connections",
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        }
    }
}

/**
 * Option 2 Top Bar: Location Header with Notification & Menu
 */
@Composable
private fun Option2TopBar(
    scheme: ODSTheme,
    onNavigateToSocialDemo: () -> Unit = {}
) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Notification Bell on Left
        ODSBox(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(imageVector = Icons.Outlined.Notifications),
                tint = scheme.basicText.getColor()
            )
            // Red Notification Dot
            ODSBox(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
                    .clip(CircleShape),
                background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
            ) {}
        }

        // Center Location Selector
        ODSColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = 2.dp
        ) {
            ODSText(
                text = "Location",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                gap = 4.dp
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Outlined.Navigation),
                    tint = scheme.basicText.getColor()
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

        // Menu / Demo Showcase Icon on Right
        ODSBox(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onNavigateToSocialDemo),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(imageVector = Icons.Outlined.Menu),
                tint = scheme.basicText.getColor()
            )
        }
    }
}

/**
 * Option 2 Segmented Dual Tab: [ Discovery | Matched ]
 */
@Composable
private fun Option2SegmentedTabs(
    selectedTab: String,
    scheme: ODSTheme,
    onTabSelected: (String) -> Unit
) {
    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 16.dp),
        padding = ODSPadding(all = 4.dp)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = 4.dp
        ) {
            listOf("Discovery", "Matched").forEach { tab ->
                val isSelected = selectedTab == tab
                ODSBox(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) },
                    background = listOf(
                        ODSColorModel(
                            hexColor = if (isSelected) scheme.basicBackground else scheme.basicBackgroundCard
                        )
                    ),
                    cornerRadius = ODSCorners(all = 12.dp),
                    border = if (isSelected) {
                        ODSBorder(width = 1.dp, colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle)))
                    } else null,
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

/**
 * Option 2 Filter Chips Row
 */
@Composable
private fun Option2FilterChips(
    filterChips: List<String>,
    selectedFilter: String,
    scheme: ODSTheme,
    onSelectFilter: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tune Filter Icon Chip
        item(key = "tune_button") {
            ODSBox(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                border = ODSBorder(width = 1.dp, colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Outlined.Tune),
                    tint = scheme.basicText.getColor()
                )
            }
        }

        // Category Pills
        items(filterChips, key = { it }) { filter ->
            val isSelected = selectedFilter.startsWith(filter.take(3)) || selectedFilter == filter
            ODSBox(
                modifier = Modifier.clickable { onSelectFilter(filter) },
                background = listOf(
                    ODSColorModel(
                        hexColor = if (isSelected) scheme.basicAccent else scheme.basicBackgroundCard
                    )
                ),
                cornerRadius = ODSCorners(all = 16.dp),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(
                        ODSColorModel(
                            hexColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle
                        )
                    )
                ),
                padding = ODSPadding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                ODSText(
                    text = filter,
                    style = ODSTextStyles.microcopyBold,
                    color = if (isSelected) scheme.basicTextOnAccent else scheme.basicText
                )
            }
        }
    }
}

/**
 * Option 2 2-Column Model Grid Card
 */
@Composable
private fun Option2ModelCard(
    model: ModelProfile,
    scheme: ODSTheme,
    cardScheme: ODSTheme,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 16.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        padding = ODSPadding(all = 8.dp)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 8.dp
        ) {
            // Hero Image Container (Option 2 Aspect)
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                background = listOf(ODSColorModel(hexColor = cardScheme.basicBackgroundSubtle)),
                contentAlignment = Alignment.Center
            ) {
                // Large Centered Model Initial / Monogram
                ODSText(
                    text = model.name.firstOrNull()?.toString() ?: "M",
                    style = ODSTextStyles.pompiereDisplay,
                    color = scheme.basicText
                )

                // Top Right Floating Distance Badge (Option 2: ✈ 213 m)
                ODSBox(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    background = listOf(ODSColorModel(hexColor = HexColor(0x99000000))),
                    cornerRadius = ODSCorners(all = 12.dp),
                    padding = ODSPadding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    ODSRow(
                        verticalAlignment = Alignment.CenterVertically,
                        gap = 4.dp
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(imageVector = Icons.Outlined.Navigation),
                            tint = Color.White
                        )
                        ODSText(
                            text = model.distance,
                            style = ODSTextStyles.microcopyRegular,
                            color = HexColor(0xffffffff)
                        )
                    }
                }

                // Top Left Favorite Heart Icon
                ODSBox(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onToggleFavorite),
                    background = listOf(ODSColorModel(hexColor = HexColor(0x66000000))),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                        ),
                        tint = if (isFavorite) scheme.functionalDestructiveStandard.getColor() else Color.White
                    )
                }
            }

            // Model Details Below Image
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                gap = 3.dp
            ) {
                // Name & Age with Verified Badge
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 4.dp
                ) {
                    ODSText(
                        modifier = Modifier.weight(1f, fill = false),
                        text = "${model.name}, ${model.age}",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    // Verified Coral Red Check Icon
                    ODSIcon(
                        iconModel = ODSIconModel(imageVector = Icons.Outlined.Verified),
                        tint = scheme.functionalDestructiveStandard.getColor()
                    )
                }

                // Matched Preferences Tag (Option 2: ✦ Matched 5+ Preferences)
                ODSText(
                    text = "✦ ${model.matchedPreferences}",
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * 2-Column Skeleton Shimmer Card using Telekom ODSSkeleton
 */
@Composable
private fun Option2CardSkeleton(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 16.dp),
        padding = ODSPadding(all = 8.dp)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 8.dp
        ) {
            ODSSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                props = ODSSkeletonProps(variant = ODSSkeletonVariant.FULL)
            )
            ODSSkeleton(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(16.dp),
                props = ODSSkeletonProps(variant = ODSSkeletonVariant.MEDIUM)
            )
            ODSSkeleton(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(12.dp),
                props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
            )
        }
    }
}
