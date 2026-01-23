package com.app.screentime.filemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.filemanager.model.FileItem
import com.app.screentime.filemanager.repository.FileManagerRepository
import com.app.screentime.filemanager.repository.FileManagerRepository.StorageInfo
import com.app.screentime.filemanager.util.FileManagerPermissionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Inject

/**
 * File filter categories
 */
enum class FileFilterCategory {
    ALL,
    VIDEOS,
    PHOTOS,
    APPS,
    DOCUMENTS,
    AUDIO,
    GAMES,
    DUPLICATES
}

/**
 * UI State for File Manager Screen
 */
data class FileManagerUiState(
    val currentDirectory: File? = null,
    val files: List<FileItem> = emptyList(),
    val rootDirectories: List<FileItem> = emptyList(),
    val photos: List<FileItem> = emptyList(),
    val videos: List<FileItem> = emptyList(),
    val audioFiles: List<FileItem> = emptyList(),
    val apps: List<FileItem> = emptyList(),
    val documents: List<FileItem> = emptyList(),
    val games: List<FileItem> = emptyList(),
    val duplicateFiles: List<FileManagerRepository.DuplicateFileGroup> = emptyList(),
    val isLoading: Boolean = false,
    val isScanningDuplicates: Boolean = false,
    val hasStoragePermissions: Boolean = false,
    val needsManageStoragePermission: Boolean = false,
    val error: String? = null,
    val storageInfo: StorageInfo? = null,
    val showDeleteDialog: Boolean = false,
    val fileToDelete: FileItem? = null,
    val isSelectionMode: Boolean = false,
    val selectedFiles: Set<String> = emptySet(), // Set of file absolute paths
    val activeFilter: FileFilterCategory = FileFilterCategory.ALL,
    val showImageViewer: Boolean = false,
    val imageToView: FileItem? = null,
    val isGridView: Boolean = true // true for grid, false for list
)

@HiltViewModel
class FileManagerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: FileManagerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FileManagerUiState())
    val uiState: StateFlow<FileManagerUiState> = _uiState.asStateFlow()

    init {
        checkPermissions()
        loadRootDirectories()
        loadStorageInfo()
        // Load all file categories for "Recently Opened" section
        loadAllFileCategories()
    }
    
    /**
     * Load all file categories (photos, documents, videos, audio) for the home view
     */
    fun loadAllFileCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Load all categories in parallel
                val photosDeferred = async { repository.getPhotos() }
                val documentsDeferred = async { repository.getDocuments() }
                val videosDeferred = async { repository.getVideos() }
                val audioDeferred = async { repository.getAudioFiles() }
                
                _uiState.value = _uiState.value.copy(
                    photos = photosDeferred.await(),
                    documents = documentsDeferred.await(),
                    videos = videosDeferred.await(),
                    audioFiles = audioDeferred.await()
                )
            } catch (e: Exception) {
                // Silently fail - these are for the home view only
            }
        }
    }

    /**
     * Check storage permissions
     */
    fun checkPermissions() {
        _uiState.value = _uiState.value.copy(
            hasStoragePermissions = FileManagerPermissionHelper.hasStoragePermissions(context),
            needsManageStoragePermission = FileManagerPermissionHelper.needsManageStoragePermission(context)
        )
    }

    /**
     * Load root directories
     */
    fun loadRootDirectories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val directories = repository.getRootDirectories()
                _uiState.value = _uiState.value.copy(
                    currentDirectory = null, // Explicitly set to null for root view
                    rootDirectories = directories,
                    files = directories,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load directories"
                )
            }
        }
    }

    /**
     * Navigate to a directory
     */
    fun navigateToDirectory(directory: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val files = repository.getFilesInDirectory(directory)
                _uiState.value = _uiState.value.copy(
                    currentDirectory = directory,
                    files = files,
                    activeFilter = FileFilterCategory.ALL, // Reset filter when navigating to directory
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load directory"
                )
            }
        }
    }

    /**
     * Navigate back to parent directory
     */
    fun navigateUp() {
        val currentDir = _uiState.value.currentDirectory ?: return
        val parent = currentDir.parentFile ?: return

        if (parent.exists() && parent.canRead()) {
            navigateToDirectory(parent)
        } else {
            // Go back to root
            _uiState.value = _uiState.value.copy(activeFilter = FileFilterCategory.ALL)
            loadRootDirectories()
        }
    }

    /**
     * Load photos
     */
    fun loadPhotos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val photos = repository.getPhotos()
                _uiState.value = _uiState.value.copy(
                    photos = photos,
                    files = photos,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load photos"
                )
            }
        }
    }

    /**
     * Load videos
     */
    fun loadVideos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val videos = repository.getVideos()
                _uiState.value = _uiState.value.copy(
                    videos = videos,
                    files = videos,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load videos"
                )
            }
        }
    }

    /**
     * Load audio files
     */
    fun loadAudioFiles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val audioFiles = repository.getAudioFiles()
                _uiState.value = _uiState.value.copy(
                    audioFiles = audioFiles,
                    files = audioFiles,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load audio files"
                )
            }
        }
    }

    /**
     * Load apps (APK files)
     */
    fun loadApps() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val apps = repository.getApps()
                _uiState.value = _uiState.value.copy(
                    apps = apps,
                    files = apps,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load apps"
                )
            }
        }
    }

    /**
     * Load documents
     */
    fun loadDocuments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val documents = repository.getDocuments()
                _uiState.value = _uiState.value.copy(
                    documents = documents,
                    files = documents,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load documents"
                )
            }
        }
    }

    /**
     * Load games
     */
    fun loadGames() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val games = repository.getGames()
                _uiState.value = _uiState.value.copy(
                    games = games,
                    files = games,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load games"
                )
            }
        }
    }

    /**
     * Set active filter and load corresponding files
     */
    fun setFilter(filter: FileFilterCategory) {
        _uiState.value = _uiState.value.copy(activeFilter = filter)
        
        when (filter) {
            FileFilterCategory.ALL -> {
                if (_uiState.value.currentDirectory == null) {
                    loadRootDirectories()
                } else {
                    navigateToDirectory(_uiState.value.currentDirectory!!)
                }
            }
            FileFilterCategory.PHOTOS -> loadPhotos()
            FileFilterCategory.VIDEOS -> loadVideos()
            FileFilterCategory.AUDIO -> loadAudioFiles()
            FileFilterCategory.APPS -> loadApps()
            FileFilterCategory.DOCUMENTS -> loadDocuments()
            FileFilterCategory.GAMES -> loadGames()
            FileFilterCategory.DUPLICATES -> {
                // If duplicates are already scanned, show them, otherwise scan
                if (_uiState.value.duplicateFiles.isEmpty()) {
                    scanForDuplicates()
                }
            }
        }
    }

    /**
     * Scan for duplicate files
     */
    fun scanForDuplicates(useContentHash: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isScanningDuplicates = true,
                error = null,
                duplicateFiles = emptyList()
            )
            try {
                val duplicates = repository.findDuplicateFiles(useContentHash)
                _uiState.value = _uiState.value.copy(
                    duplicateFiles = duplicates,
                    isScanningDuplicates = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isScanningDuplicates = false,
                    error = e.message ?: "Failed to scan for duplicates"
                )
            }
        }
    }

    /**
     * Delete duplicate files (keeps the first one, deletes the rest)
     */
    fun deleteDuplicateFiles(duplicateGroup: FileManagerRepository.DuplicateFileGroup) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // Keep the first file, delete the rest
                val filesToDelete = duplicateGroup.files.drop(1)
                var deletedCount = 0
                
                filesToDelete.forEach { fileItem ->
                    if (repository.deleteFile(fileItem.file)) {
                        deletedCount++
                    }
                }
                
                // Remove this group from duplicates list
                val updatedDuplicates = _uiState.value.duplicateFiles.filter { it != duplicateGroup }
                _uiState.value = _uiState.value.copy(
                    duplicateFiles = updatedDuplicates,
                    isLoading = false
                )
                
                // Reload storage info
                loadStorageInfo()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to delete duplicate files"
                )
            }
        }
    }

    /**
     * Delete all duplicate files (keeps first file in each group, deletes the rest)
     */
    fun deleteAllDuplicateFiles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                var totalDeleted = 0
                var totalFreed = 0L
                
                _uiState.value.duplicateFiles.forEach { group ->
                    val filesToDelete = group.files.drop(1)
                    filesToDelete.forEach { fileItem ->
                        if (repository.deleteFile(fileItem.file)) {
                            totalDeleted++
                            totalFreed += fileItem.size
                        }
                    }
                }
                
                _uiState.value = _uiState.value.copy(
                    duplicateFiles = emptyList(),
                    isLoading = false
                )
                
                // Reload storage info
                loadStorageInfo()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to delete duplicate files"
                )
            }
        }
    }

    /**
     * Load storage information
     */
    fun loadStorageInfo() {
        viewModelScope.launch {
            try {
                val storageInfo = repository.getStorageInfo()
                _uiState.value = _uiState.value.copy(storageInfo = storageInfo)
            } catch (e: Exception) {
                // Silently fail - storage info is not critical
            }
        }
    }

    /**
     * Show delete confirmation dialog
     */
    fun showDeleteDialog(fileItem: FileItem) {
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = true,
            fileToDelete = fileItem
        )
    }

    /**
     * Dismiss delete dialog
     */
    fun dismissDeleteDialog() {
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = false,
            fileToDelete = null
        )
    }

    /**
     * Delete file or folder
     */
    fun deleteFile() {
        val fileToDelete = _uiState.value.fileToDelete ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val success = repository.deleteFile(fileToDelete.file)
                if (success) {
                    // Refresh current directory or root directories
                    val currentDir = _uiState.value.currentDirectory
                    if (currentDir != null) {
                        navigateToDirectory(currentDir)
                    } else {
                        loadRootDirectories()
                    }
                    // Reload storage info after deletion
                    loadStorageInfo()
                    dismissDeleteDialog()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to delete ${fileToDelete.name}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to delete file"
                )
            }
        }
    }

    /**
     * Enter selection mode
     */
    fun enterSelectionMode() {
        _uiState.value = _uiState.value.copy(isSelectionMode = true, selectedFiles = emptySet())
    }

    /**
     * Exit selection mode
     */
    fun exitSelectionMode() {
        _uiState.value = _uiState.value.copy(isSelectionMode = false, selectedFiles = emptySet())
    }

    /**
     * Toggle file selection
     */
    fun toggleFileSelection(fileItem: FileItem) {
        val currentSelected = _uiState.value.selectedFiles.toMutableSet()
        val filePath = fileItem.file.absolutePath
        
        if (currentSelected.contains(filePath)) {
            currentSelected.remove(filePath)
        } else {
            currentSelected.add(filePath)
        }
        
        _uiState.value = _uiState.value.copy(selectedFiles = currentSelected)
    }

    /**
     * Select all files
     */
    fun selectAll() {
        val allPaths = _uiState.value.files.map { it.file.absolutePath }.toSet()
        _uiState.value = _uiState.value.copy(selectedFiles = allPaths)
    }

    /**
     * Clear all selections
     */
    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedFiles = emptySet())
    }

    /**
     * Delete selected files
     */
    fun deleteSelectedFiles() {
        val selectedPaths = _uiState.value.selectedFiles
        if (selectedPaths.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                var successCount = 0
                var failCount = 0

                selectedPaths.forEach { path ->
                    val file = File(path)
                    if (file.exists()) {
                        val success = repository.deleteFile(file)
                        if (success) {
                            successCount++
                        } else {
                            failCount++
                        }
                    }
                }

                // Refresh current directory or root directories
                val currentDir = _uiState.value.currentDirectory
                if (currentDir != null) {
                    navigateToDirectory(currentDir)
                } else {
                    loadRootDirectories()
                }
                
                // Reload storage info after deletion
                loadStorageInfo()
                
                // Exit selection mode
                exitSelectionMode()

                if (failCount > 0) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to delete $failCount item(s)"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to delete files"
                )
            }
        }
    }

    /**
     * Toggle between grid and list view
     */
    fun toggleViewMode() {
        _uiState.value = _uiState.value.copy(isGridView = !_uiState.value.isGridView)
    }

    /**
     * Set view mode (grid or list)
     */
    fun setViewMode(isGrid: Boolean) {
        _uiState.value = _uiState.value.copy(isGridView = isGrid)
    }

    /**
     * Reset ViewModel state to initial state
     * This should be called when the screen is entered to clear previous state
     * Preserves permissions and storage info as they're useful to keep
     */
    fun resetState() {
        _uiState.value = FileManagerUiState(
            hasStoragePermissions = _uiState.value.hasStoragePermissions,
            needsManageStoragePermission = _uiState.value.needsManageStoragePermission,
            storageInfo = _uiState.value.storageInfo // Keep storage info as it's useful
        )
        // Reload root directories after reset
        loadRootDirectories()
    }

    /**
     * Show image viewer
     */
    fun showImageViewer(fileItem: FileItem) {
        _uiState.value = _uiState.value.copy(
            showImageViewer = true,
            imageToView = fileItem
        )
    }

    /**
     * Dismiss image viewer
     */
    fun dismissImageViewer() {
        _uiState.value = _uiState.value.copy(
            showImageViewer = false,
            imageToView = null
        )
    }
}

