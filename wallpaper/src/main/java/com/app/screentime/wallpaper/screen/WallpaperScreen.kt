package com.app.screentime.wallpaper.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.LocalActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.screentime.wallpaper.component.WallpaperAddDialog
import com.app.screentime.wallpaper.api.model.ImageItem
import com.app.screentime.wallpaper.component.WallpaperCard
import com.app.screentime.wallpaper.component.WallpaperSetDialog
import com.app.screentime.wallpaper.component.CategoryTabsShimmer
import com.app.screentime.wallpaper.component.WallpaperGridShimmer
import com.app.screentime.wallpaper.model.Wallpaper
import com.app.screentime.wallpaper.model.WallpaperCategory
import com.app.screentime.wallpaper.viewmodel.WallpaperViewModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.onClick
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.organisms.pageheader.ODSPageHeader
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderProps
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderType
import com.telekom.odsystem.atoms.togglechip.ODSToggleChip
import com.telekom.odsystem.atoms.togglechip.ODSToggleChipProps
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.molecules.tabs.ODSTabsVariant
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment

@Composable
fun WallpaperScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onNavigateToFullScreen: (String, ImageItem?) -> Unit = { _, _ -> },
    onNavigateToSearch: () -> Unit = {},
    viewModel: WallpaperViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showSetDialog by remember { mutableStateOf<Wallpaper?>(null) }
    var selectedUriForAdd by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedUriForAdd = uri
    }

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSBox(
            modifier = Modifier
                .height(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                )
                .fillMaxWidth(), background = listOf(ODSColorModel(scheme.basicBackgroundCard))
        ) {}

        ODSPageHeader(
            scheme = scheme, 
            onBackButtonClick = onBackClick, 
            props = ODSPageHeaderProps(
                type = ODSPageHeaderType.SUB_PAGE_HEADER
            ), 
            subPageTitleSlot = {
                ODSText(
                    text = "Wallpapers", 
                    style = DSTextStyles.bodyMBold, 
                    color = scheme.basicText
                )
            },
            actionsSlot = {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_wallpapers),
                        tint = scheme.basicText
                    ),
                    modifier = Modifier
                        .padding(end = DSVariables.spacingComponent3)
                        .onClick { onNavigateToSearch() },
                    width = 24.dp,
                    height = 24.dp
                )
            }
        )

        // Category Selection using ODSTabs
        if (uiState.isLoadingCategories) {
            CategoryTabsShimmer(scheme = scheme)
        } else if (uiState.categories.isNotEmpty()) {
            CategoryTabs(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                isParallaxSelected = uiState.isParallaxSelected,
                isNewSelected = uiState.isNewSelected,
                onCategorySelected = { categoryId ->
                    viewModel.selectCategory(categoryId)
                },
                onParallaxSelected = {
                    viewModel.selectParallax()
                },
                onNewSelected = {
                    viewModel.selectNew()
                },
                scheme = scheme
            )
        }

        WallpaperListContent(
            uiState = uiState,
            wallpapers = uiState.wallpapers,
            scheme = scheme,
            onWallpaperClick = { wallpaper ->
                val imageItem = uiState.imageItems[wallpaper.id]
                onNavigateToFullScreen(wallpaper.id, imageItem)
            },
            onLoadMore = { viewModel.loadMoreWallpapers() })
    }
    // Add Wallpaper Options Dialog
    selectedUriForAdd?.let { uri ->
        WallpaperAddDialog(
            imageUri = uri,
            scheme = scheme,
            onDismiss = { selectedUriForAdd = null },
            onSave = { name, autoHome, autoLock ->
                viewModel.addWallpaperFromUri(
                    uri = uri, name = name, autoSetHome = autoHome, autoSetLock = autoLock
                )
                selectedUriForAdd = null
            })
    }

    uiState.error?.let { error ->
        LaunchedEffect(error) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
}

@Composable
private fun CategoryTabs(
    categories: List<Pair<Int, String>>,
    selectedCategoryId: Int?,
    isParallaxSelected: Boolean,
    isNewSelected: Boolean,
    onCategorySelected: (Int) -> Unit,
    onParallaxSelected: () -> Unit,
    onNewSelected: () -> Unit,
    scheme: ODSTheme
) {
    // Add "New" as the first tab
    val tabElements = buildList {
        add(ODSTabItemModel(label = stringResource(R.string.new_tab)))
        addAll(categories.map { (_, title) ->
            ODSTabItemModel(label = title)
        })
    }

    val selectedIndex = when {
        isNewSelected -> 0 // New is the first tab
        else -> {
            selectedCategoryId?.let { categoryId ->
                categories.indexOfFirst { it.first == categoryId }
            }?.let { it + 1 } ?: 0 // +1 because New is at index 0
        }
    }

    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = DSVariables.spacingComponent4, vertical = DSVariables.spacingComponent2
            )
    ) {
        ODSTabs(
            modifier = Modifier.fillMaxWidth(), 
            scheme = scheme, 
            props = ODSTabsProps(
                tabElements = tabElements,
                size = ODSTabsSize.SMALL,
                variant = ODSTabsVariant.HUG,
                showDividerFrame = true
            ), 
            selectedTabIndex = selectedIndex, 
            onSelectedTabChange = { index ->
                when (index) {
                    0 -> onNewSelected() // New selected
                    else -> {
                        if (index - 1 < categories.size) {
                            // Category selected (subtract 1 because New is at index 0)
                            onCategorySelected(categories[index - 1].first)
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun WallpaperListContent(
    uiState: com.app.screentime.wallpaper.viewmodel.WallpaperUiState,
    wallpapers: List<Wallpaper>,
    scheme: ODSTheme,
    onWallpaperClick: (Wallpaper) -> Unit,
    onLoadMore: () -> Unit
) {
    val gridState = rememberLazyGridState()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isExpandedScreen = windowSizeClass.isWidthAtLeastBreakpoint(840)
    // Detect when user scrolls near the bottom (within 3 items)
    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // Load more when user is within 3 items from the end
            totalItems > 0 && lastVisibleItemIndex >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !uiState.isLoadingMore && uiState.hasMore && !uiState.isLoadingImages) {
            onLoadMore()
        }
    }

    if (uiState.isLoadingImages && wallpapers.isEmpty()) {
        WallpaperGridShimmer(scheme = scheme)
    } else {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(
                if (isExpandedScreen) {
                    3
                } else 2
            ),
            contentPadding = PaddingValues(DSVariables.spacingComponent4),
            horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
            verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
        ) {
            items(wallpapers) { wallpaper ->
                WallpaperCard(
                    wallpaper = wallpaper,
                    scheme = scheme,
                    onClick = {
                        onWallpaperClick.invoke(wallpaper)
                    },
                    isCurrentHome = wallpaper.id == uiState.currentHomeWallpaper?.id,
                    isCurrentLock = wallpaper.id == uiState.currentLockWallpaper?.id
                )
            }

            if (uiState.isLoadingMore) {
                item {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = DSVariables.spacingComponent4),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSLoadingSpinner(
                            modifier = Modifier.wrapContentWidth(),
                            scheme = scheme,
                            props = ODSLoadingSpinnerProps(
                                labelText = stringResource(R.string.loading),
                                size = ODSLoadingSpinnerSize.SMALL,
                                variant = ODSLoadingSpinnerVariant.STANDARD,
                                labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL
                            )
                        )
                    }
                }
            }
        }
    }
}

