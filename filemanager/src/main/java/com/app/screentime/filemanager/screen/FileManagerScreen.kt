package com.app.screentime.filemanager.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AudioFile
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R
import com.app.screentime.config.data.Feature
import com.app.screentime.filemanager.component.CategoryItem
import com.app.screentime.filemanager.component.DeleteConfirmationDialog
import com.app.screentime.filemanager.component.DuplicatesView
import com.app.screentime.filemanager.component.FileGridView
import com.app.screentime.filemanager.component.FileListView
import com.app.screentime.filemanager.component.ImageListView
import com.app.screentime.filemanager.component.FileManagerContentArea
import com.app.screentime.filemanager.component.FileManagerSidebar
import com.app.screentime.filemanager.component.FilterTabsView
import com.app.screentime.filemanager.component.MobileFileManagerView
import com.app.screentime.filemanager.component.PermissionRequestView
import com.app.screentime.filemanager.component.FullScreenImageViewer
import com.app.screentime.filemanager.component.SelectionModeActionBar
import androidx.compose.runtime.rememberCoroutineScope
import com.app.screentime.filemanager.component.SidebarNavItem
import com.app.screentime.filemanager.component.StorageInfoView
import com.app.screentime.filemanager.util.FileManagerPermissionHelper
import com.app.screentime.filemanager.viewmodel.FileManagerViewModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.extensions.onClick
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.pageheader.ODSPageHeader
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderProps
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderType
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.app.screentime.filemanager.viewmodel.FileFilterCategory
import com.telekom.odsystem.foundations.ODSColorModel
import kotlinx.coroutines.launch
import java.io.File

/**
 * Main File Manager Screen
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FileManagerScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {

    val viewModel: FileManagerViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = LocalActivity.current

    // Detect if it's a mobile or desktop screen
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    val isMobile = !windowSize.isWidthAtLeastBreakpoint(840)

    // Selected sidebar item
    var selectedSidebarItem by remember { mutableStateOf<SidebarNavItem?>(SidebarNavItem.Storage) }

    // Sidebar collapse state
    var isSidebarCollapsed by remember { mutableStateOf(false) }

    // Reset state when screen is entered (when navigating to this screen)
    // This ensures fresh state when returning to the screen after being popped
    // The key changes each time the composable is composed (when screen is entered)
    LaunchedEffect(Unit) {
        // Reset to initial state, preserving only permissions and storage info
        viewModel.resetState()
    }

    // Handle sidebar navigation
    LaunchedEffect(selectedSidebarItem) {
        selectedSidebarItem?.let { item ->
            when (item) {
                SidebarNavItem.Recent -> viewModel.setFilter(FileFilterCategory.ALL)
                SidebarNavItem.Downloads -> {
                    // Navigate to Downloads folder
                    viewModel.loadRootDirectories()
                }

                SidebarNavItem.Documents -> viewModel.setFilter(FileFilterCategory.DOCUMENTS)
                SidebarNavItem.Images -> viewModel.setFilter(FileFilterCategory.PHOTOS)
                SidebarNavItem.Videos -> viewModel.setFilter(FileFilterCategory.VIDEOS)
                SidebarNavItem.Storage -> {
                    viewModel.loadRootDirectories()
                    viewModel.setFilter(FileFilterCategory.ALL)
                }

                SidebarNavItem.Apps -> viewModel.setFilter(FileFilterCategory.APPS)
            }
        }
    }

    // Permission launchers
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        viewModel.checkPermissions()
        if (permissions.values.all { it }) {
            viewModel.loadRootDirectories()
            viewModel.loadAllFileCategories()
        }
    }

    val manageStorageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        viewModel.checkPermissions()
        if (FileManagerPermissionHelper.hasStoragePermissions(context)) {
            viewModel.loadRootDirectories()
            viewModel.loadAllFileCategories()
        }
    }

    ODSColumn(
        background = listOf(ODSColorModel(scheme.basicBackground)),
        modifier = modifier.fillMaxSize()
    ) {
        // Status bar padding
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {}

        // Only show page header on desktop or when navigating into folders on mobile
        if (!isMobile || uiState.currentDirectory != null) {
            ODSPageHeader(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSPageHeaderProps(
                    type = ODSPageHeaderType.SUB_PAGE_HEADER
                ),
                subPageTitleSlot = {
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = uiState.currentDirectory?.name ?: stringResource(R.string.storage),
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText,
                        textAlign = TextAlign.Center
                    )
                },
                onBackButtonClick = {
                    if (uiState.currentDirectory != null) {
                        viewModel.navigateUp()
                    } else {
                        onBackClick()
                    }
                }
            )
        }

        when {
            !uiState.hasStoragePermissions -> {
                PermissionRequestView(
                    scheme = scheme,
                    needsManageStorage = uiState.needsManageStoragePermission,
                    onRequestPermission = {
                        if (FileManagerPermissionHelper.needsManageStoragePermission(context)) {
                            if (activity is Activity) {
                                manageStorageLauncher.launch(
                                    FileManagerPermissionHelper.getManageStoragePermissionIntent(
                                        context
                                    )
                                )
                            }
                        } else {
                            val permissions = FileManagerPermissionHelper.getRequiredPermissions()
                            if (activity is Activity) {
                                permissionLauncher.launch(permissions)
                            }
                        }
                    }
                )
            }

            uiState.isLoading -> {
                ODSBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    ODSLoadingSpinner(
                        modifier = Modifier.wrapContentHeight(),
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(
                            labelText = stringResource(R.string.loading_files),
                            size = ODSLoadingSpinnerSize.SMALL,
                            variant = ODSLoadingSpinnerVariant.STANDARD,
                            labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL
                        )
                    )
                }
            }

            uiState.error != null -> {
                // Error state
                ODSBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = uiState.error ?: stringResource(R.string.error_occurred),
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.functionalDestructiveStandard
                    )
                }
            }

            else -> {
                if (isMobile) {
                    // Mobile layout: Home view or grid view based on navigation state
                    if (uiState.currentDirectory == null && uiState.activeFilter == FileFilterCategory.ALL) {
                        // Show mobile home view
                        // Get recent files for horizontal scroll - get from all categories
                        val recentFiles =
                            (uiState.documents + uiState.photos + uiState.videos + uiState.audioFiles)
                                .sortedByDescending { it.lastModified }
                                .take(10) // Take up to 10 most recent files

                        // Prepare categories
                        val categories = listOf(
                            CategoryItem(
                                title = "Videos",
                                icon = Icons.Outlined.VideoLibrary,
                                itemCount = uiState.videos.size,
                                filter = FileFilterCategory.VIDEOS
                            ),
                            CategoryItem(
                                title = "Documents",
                                icon = Icons.Outlined.Description,
                                itemCount = uiState.documents.size,
                                filter = FileFilterCategory.DOCUMENTS
                            ),
                            CategoryItem(
                                title = "Images",
                                icon = Icons.Outlined.Image,
                                itemCount = uiState.photos.size,
                                filter = FileFilterCategory.PHOTOS
                            ),
                            CategoryItem(
                                title = "Music",
                                icon = Icons.Outlined.AudioFile,
                                itemCount = uiState.audioFiles.size,
                                filter = FileFilterCategory.AUDIO
                            )
                        )

                        MobileFileManagerView(
                            storageInfo = uiState.storageInfo,
                            recentFiles = recentFiles,
                            categories = categories,
                            folders = uiState.rootDirectories.filter { it.isDirectory },
                            onRecentFileClick = { fileItem ->
                                if (fileItem.isDirectory) {
                                    viewModel.navigateToDirectory(fileItem.file)
                                } else if (isImageFile(fileItem)) {
                                    viewModel.showImageViewer(fileItem)
                                } else {
                                    // TODO: Open other file types
                                }
                            },
                            onCategoryClick = { category ->
                                viewModel.setFilter(category)
                            },
                            onFolderClick = { folder ->
                                viewModel.navigateToDirectory(folder.file)
                            },
                            onSearchClick = {
                                // TODO: Open search
                            },
                            onAddClick = {
                                // TODO: Open add/create dialog
                            },
                            onNotificationClick = {
                                // TODO: Open notifications
                            },
                            isGridView = uiState.isGridView,
                            onToggleViewMode = { viewModel.toggleViewMode() },
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                        )
                    } else {
                        ODSColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                        ) {
                            // Back arrow and view toggle when in any filtered view or directory
                            if (uiState.activeFilter != FileFilterCategory.ALL ||
                                uiState.currentDirectory != null
                            ) {
                                ODSRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(DSVariables.spacingComponent4),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ODSRow(
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ODSBox(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .onClick {
                                                    if (uiState.currentDirectory != null) {
                                                        viewModel.navigateUp()
                                                    } else {
                                                        viewModel.setFilter(FileFilterCategory.ALL)
                                                    }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            ODSIcon(
                                                iconModel = ODSIconModel(
                                                    imageVector = Icons.Outlined.ArrowBack,
                                                    tint = scheme.basicText,
                                                    contentDescription = "Back"
                                                ),
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(DSVariables.spacingComponent3))
                                        ODSText(
                                            text = when (uiState.activeFilter) {
                                                FileFilterCategory.PHOTOS -> "Photos"
                                                FileFilterCategory.VIDEOS -> "Videos"
                                                FileFilterCategory.DOCUMENTS -> "Documents"
                                                FileFilterCategory.AUDIO -> "Music"
                                                FileFilterCategory.APPS -> "Apps"
                                                FileFilterCategory.GAMES -> "Games"
                                                FileFilterCategory.DUPLICATES -> "Duplicates"
                                                else -> uiState.currentDirectory?.name ?: "Files"
                                            },
                                            style = DSTextStyles.bodyMBold,
                                            color = scheme.basicText
                                        )
                                    }

                                    // View mode toggle for all categories
                                    if (uiState.activeFilter != FileFilterCategory.ALL &&
                                        uiState.activeFilter != FileFilterCategory.DUPLICATES
                                    ) {
                                        ODSBox(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .onClick {
                                                    viewModel.toggleViewMode()
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            ODSIcon(
                                                iconModel = ODSIconModel(
                                                    imageVector = if (uiState.isGridView) Icons.Outlined.List else Icons.Outlined.GridView,
                                                    tint = scheme.basicText,
                                                    contentDescription = if (uiState.isGridView) "Switch to list view" else "Switch to grid view"
                                                ),
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Show duplicates view or grid view
                            if (uiState.activeFilter == FileFilterCategory.DUPLICATES) {
                                DuplicatesView(
                                    duplicateGroups = uiState.duplicateFiles,
                                    isScanning = uiState.isScanningDuplicates,
                                    scheme = scheme,
                                    onScanClick = { viewModel.scanForDuplicates() },
                                    onDeleteGroup = { group ->
                                        viewModel.deleteDuplicateFiles(group)
                                    },
                                    onDeleteAll = { viewModel.deleteAllDuplicateFiles() }
                                )
                            } else {
                                // Grid or List view for mobile
                                // Only filter images when viewing photos, show all files for other categories
                                val showOnlyImages =
                                    uiState.activeFilter == FileFilterCategory.PHOTOS

                                Box(modifier = Modifier.weight(1f)) {
                                    if (uiState.isGridView) {
                                        // Grid view
                                        FileGridView(
                                            files = uiState.files,
                                            scheme = scheme,
                                            isSelectionMode = uiState.isSelectionMode,
                                            selectedFiles = uiState.selectedFiles,
                                            showOnlyImages = showOnlyImages,
                                            onFileClick = { fileItem ->
                                                if (uiState.isSelectionMode) {
                                                    viewModel.toggleFileSelection(fileItem)
                                                } else {
                                                    if (fileItem.isDirectory) {
                                                        viewModel.navigateToDirectory(fileItem.file)
                                                    } else {
                                                        // Check if it's an image and open viewer
                                                        if (isImageFile(fileItem)) {
                                                            viewModel.showImageViewer(fileItem)
                                                        } else {
                                                            // TODO: Open other file types
                                                        }
                                                    }
                                                }
                                            },
                                            onFileLongPress = { fileItem ->
                                                if (!uiState.isSelectionMode) {
                                                    viewModel.enterSelectionMode()
                                                    viewModel.toggleFileSelection(fileItem)
                                                }
                                            },
                                            onDeleteClick = { fileItem ->
                                                viewModel.showDeleteDialog(fileItem)
                                            }
                                        )
                                    } else {
                                        // List view
                                        ImageListView(
                                            files = uiState.files,
                                            scheme = scheme,
                                            isSelectionMode = uiState.isSelectionMode,
                                            selectedFiles = uiState.selectedFiles,
                                            showOnlyImages = showOnlyImages,
                                            onFileClick = { fileItem ->
                                                if (uiState.isSelectionMode) {
                                                    viewModel.toggleFileSelection(fileItem)
                                                } else {
                                                    if (fileItem.isDirectory) {
                                                        viewModel.navigateToDirectory(fileItem.file)
                                                    } else {
                                                        // Check if it's an image and open viewer
                                                        if (isImageFile(fileItem)) {
                                                            viewModel.showImageViewer(fileItem)
                                                        } else {
                                                            // TODO: Open other file types
                                                        }
                                                    }
                                                }
                                            },
                                            onFileLongPress = { fileItem ->
                                                if (!uiState.isSelectionMode) {
                                                    viewModel.enterSelectionMode()
                                                    viewModel.toggleFileSelection(fileItem)
                                                }
                                            },
                                            onDeleteClick = { fileItem ->
                                                viewModel.showDeleteDialog(fileItem)
                                            }
                                        )
                                    }

                                    // Selection mode action bar at bottom
                                    if (uiState.isSelectionMode) {
                                        SelectionModeActionBar(
                                            selectedCount = uiState.selectedFiles.size,
                                            totalCount = uiState.files.size,
                                            scheme = scheme,
                                            onSelectAll = { viewModel.selectAll() },
                                            onClearSelection = { viewModel.clearSelection() },
                                            onDeleteSelected = { viewModel.deleteSelectedFiles() },
                                            onCancel = { viewModel.exitSelectionMode() },
                                            modifier = Modifier.align(Alignment.BottomCenter)
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Desktop layout: Finder-style layout with Sidebar + Content Area
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        // Left Sidebar
                        FileManagerSidebar(
                            selectedItem = selectedSidebarItem,
                            scheme = scheme,
                            isCollapsed = isSidebarCollapsed,
                            onItemClick = { item ->
                                selectedSidebarItem = item
                            },
                            onToggleCollapse = {
                                isSidebarCollapsed = !isSidebarCollapsed
                            },
                            folders = uiState.rootDirectories.filter { it.isDirectory },
                            onFolderClick = { folder ->
                                viewModel.navigateToDirectory(folder.file)
                            }
                        )

                        // Main Content Area
                        ODSColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                        ) {
                            // Storage information (optional, can be shown at top)
                            uiState.storageInfo?.let { storageInfo ->
                                StorageInfoView(
                                    storageInfo = storageInfo,
                                    scheme = scheme
                                )
                            }

                            // Selection mode action bar
                            if (uiState.isSelectionMode) {
                                SelectionModeActionBar(
                                    selectedCount = uiState.selectedFiles.size,
                                    totalCount = uiState.files.size,
                                    scheme = scheme,
                                    onSelectAll = { viewModel.selectAll() },
                                    onClearSelection = { viewModel.clearSelection() },
                                    onDeleteSelected = { viewModel.deleteSelectedFiles() },
                                    onCancel = { viewModel.exitSelectionMode() }
                                )
                            }

                            // Show duplicates view or regular file list with date grouping
                            if (uiState.activeFilter == FileFilterCategory.DUPLICATES) {
                                DuplicatesView(
                                    duplicateGroups = uiState.duplicateFiles,
                                    isScanning = uiState.isScanningDuplicates,
                                    scheme = scheme,
                                    onScanClick = { viewModel.scanForDuplicates() },
                                    onDeleteGroup = { group ->
                                        viewModel.deleteDuplicateFiles(group)
                                    },
                                    onDeleteAll = { viewModel.deleteAllDuplicateFiles() }
                                )
                            } else {
                                // File list with date grouping (Finder-style)
                                FileManagerContentArea(
                                    files = uiState.files,
                                    scheme = scheme,
                                    isSelectionMode = uiState.isSelectionMode,
                                    selectedFiles = uiState.selectedFiles,
                                    onFileClick = { fileItem ->
                                        if (uiState.isSelectionMode) {
                                            viewModel.toggleFileSelection(fileItem)
                                        } else {
                                            if (fileItem.isDirectory) {
                                                viewModel.navigateToDirectory(fileItem.file)
                                            } else {
                                                // Check if it's an image and open viewer
                                                if (isImageFile(fileItem)) {
                                                    viewModel.showImageViewer(fileItem)
                                                } else {
                                                    // TODO: Open other file types
                                                }
                                            }
                                        }
                                    },
                                    onFileLongPress = { fileItem ->
                                        if (!uiState.isSelectionMode) {
                                            viewModel.enterSelectionMode()
                                            viewModel.toggleFileSelection(fileItem)
                                        }
                                    },
                                    onDeleteClick = { fileItem ->
                                        viewModel.showDeleteDialog(fileItem)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (uiState.showDeleteDialog && uiState.fileToDelete != null) {
        DeleteConfirmationDialog(
            showDialog = true,
            fileItem = uiState.fileToDelete!!,
            scheme = scheme,
            onDismiss = { viewModel.dismissDeleteDialog() },
            onConfirm = { viewModel.deleteFile() }
        )
    }

    // Full-screen image viewer
    if (uiState.showImageViewer && uiState.imageToView != null) {
        val context = LocalContext.current
        val activity = LocalActivity.current
        val coroutineScope = rememberCoroutineScope()

        // Crop launcher
        val cropLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                coroutineScope.launch {
                    //  ToastSnackbarManager.showSuccess("Image cropped successfully")
                }
                // Refresh to show cropped image
                uiState.currentDirectory?.let {
                    viewModel.navigateToDirectory(it)
                } ?: viewModel.loadRootDirectories()
            }
        }

        FullScreenImageViewer(
            fileItem = uiState.imageToView!!,
            onDismiss = { viewModel.dismissImageViewer() },
            onResize = { file, width, height ->
                coroutineScope.launch {
                    // Success - refresh directory to show resized image
                    uiState.currentDirectory?.let {
                        viewModel.navigateToDirectory(it)
                    } ?: viewModel.loadRootDirectories()
                }
            },
            onResizeError = { error ->
                coroutineScope.launch {
                    // ToastSnackbarManager.showError(error)
                }
            },
            onShare = { uri ->
                try {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, uri)
                        type = "image/*"
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    val chooser = Intent.createChooser(shareIntent, "Share image")
                    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(chooser)
                } catch (e: Exception) {
                    coroutineScope.launch {
                        //  ToastSnackbarManager.showError("Failed to share image: ${e.message}")
                    }
                }
            },
            onShareError = { error ->
                coroutineScope.launch {
                    //  ToastSnackbarManager.showError(error)
                }
            },
            onCrop = { file ->
                try {
                    // Copy file to cache first to ensure it's accessible
                    val cacheDir = context.cacheDir
                    val cropDir = File(cacheDir, "crop_images")
                    if (!cropDir.exists()) {
                        cropDir.mkdirs()
                    }

                    val cachedInputFile = File(cropDir, "input_${file.name}")
                    file.copyTo(cachedInputFile, overwrite = true)

                    val inputUri = androidx.core.content.FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        cachedInputFile
                    )

                    // Create output file in same directory as original with numbered suffix
                    val parentDir = file.parentFile
                    val baseName = file.nameWithoutExtension
                    val extension = file.extension
                    var counter = 1
                    var outputFile: File

                    do {
                        val suffix = if (counter == 1) "" else " ($counter)"
                        val outputFileName = "$baseName$suffix.$extension"
                        outputFile = File(parentDir, outputFileName)
                        counter++
                    } while (outputFile.exists() && counter < 1000)

                    // Copy output file to cache for FileProvider access
                    val cachedOutputFile = File(cropDir, "output_${outputFile.name}")

                    val outputUri = androidx.core.content.FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        cachedOutputFile
                    )

                    // Try crop intent with proper MIME type
                    val cropIntent = Intent("com.android.camera.action.CROP").apply {
                        setDataAndType(inputUri, "image/*")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        putExtra("crop", "true")
                        putExtra("aspectX", 1)
                        putExtra("aspectY", 1)
                        putExtra("outputX", 1024)
                        putExtra("outputY", 1024)
                        putExtra("scale", true)
                        putExtra("scaleUpIfNeeded", true)
                        putExtra("return-data", false)
                        putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outputUri)
                        putExtra("outputFormat", "JPEG")
                    }

                    if (cropIntent.resolveActivity(context.packageManager) != null) {
                        cropLauncher.launch(cropIntent)
                        // After crop completes, copy from cache to original location
                        coroutineScope.launch {
                            kotlinx.coroutines.delay(500) // Wait a bit for crop to complete
                            try {
                                if (cachedOutputFile.exists()) {
                                    cachedOutputFile.copyTo(outputFile, overwrite = true)
                                    cachedOutputFile.delete()
                                }
                                cachedInputFile.delete()
                            } catch (e: Exception) {
                                android.util.Log.e(
                                    "FileManager",
                                    "Error copying cropped file: ${e.message}"
                                )
                            }
                        }
                    } else {
                        // Fallback: Use generic edit intent
                        val editIntent = Intent(Intent.ACTION_EDIT).apply {
                            setDataAndType(inputUri, "image/*")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        }
                        if (editIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(editIntent)
                        } else {
                            throw Exception("No image editing app available")
                        }
                    }
                } catch (e: Exception) {
                    coroutineScope.launch {
                        // ToastSnackbarManager.showError("Crop not available: ${e.message}")
                    }
                }
            },
            onCropError = { error ->
                coroutineScope.launch {
                    //    ToastSnackbarManager.showError(error)
                }
            },
            onCompress = { compressedFile ->
                coroutineScope.launch {
                    //   ToastSnackbarManager.showSuccess("Image compressed successfully: ${compressedFile.name}")
                }
                viewModel.dismissImageViewer()
                // Refresh the current directory to show the new compressed file
                uiState.currentDirectory?.let {
                    viewModel.navigateToDirectory(it)
                } ?: viewModel.loadRootDirectories()
            },
            onCompressError = { error ->
                coroutineScope.launch {
                    //  ToastSnackbarManager.showError(error)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun isImageFile(fileItem: com.app.screentime.filemanager.model.FileItem): Boolean {
    return fileItem.mimeType?.startsWith("image/") == true ||
            fileItem.name.endsWith(".jpg", ignoreCase = true) ||
            fileItem.name.endsWith(".jpeg", ignoreCase = true) ||
            fileItem.name.endsWith(".png", ignoreCase = true) ||
            fileItem.name.endsWith(".gif", ignoreCase = true) ||
            fileItem.name.endsWith(".webp", ignoreCase = true) ||
            fileItem.name.endsWith(".heic", ignoreCase = true) ||
            fileItem.name.endsWith(".heif", ignoreCase = true)
}

private fun formatTotalSize(files: List<com.app.screentime.filemanager.model.FileItem>): String {
    val totalBytes = files.sumOf { it.size }
    if (totalBytes < 1024) return "${totalBytes}B"
    val kb = totalBytes / 1024.0
    if (kb < 1024) return String.format("%.1f KB", kb)
    val mb = kb / 1024.0
    if (mb < 1024) return String.format("%.1f MB", mb)
    val gb = mb / 1024.0
    return String.format("%.2f GB", gb)
}
