package com.app.screentime.wallpaper.viewmodel

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.analytics.AnalyticsUseCase
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.wallpaper.api.model.ImageItem
import com.app.screentime.wallpaper.api.service.WallpapersCraftService
import com.app.screentime.wallpaper.mapper.WallpapersCraftMapper
import com.app.screentime.wallpaper.model.Wallpaper
import com.app.screentime.wallpaper.model.WallpaperCategory
import com.app.screentime.wallpaper.model.WallpaperType
import com.app.screentime.wallpaper.repository.WallpaperRepository
import com.app.screentime.wallpaper.utils.WallpaperUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WallpaperUiState(
    val isLoading: Boolean = false,
    val isLoadingCategories: Boolean = false,
    val isLoadingImages: Boolean = false,
    val isLoadingMore: Boolean = false, // For pagination
    val wallpapers: List<Wallpaper> = emptyList(),
    val imageItems: Map<String, ImageItem> = emptyMap(), // Map of wallpaper ID to ImageItem
    val categories: List<Pair<Int, String>> = emptyList(), // (id, title)
    val selectedCategoryId: Int? = null,
    val isParallaxSelected: Boolean = false, // Track if parallax tab is selected
    val isNewSelected: Boolean = false, // Track if new tab is selected
    val currentOffset: Int = 0, // Current pagination offset
    val hasMore: Boolean = true, // Whether there are more items to load
    val currentHomeWallpaper: Wallpaper? = null,
    val currentLockWallpaper: Wallpaper? = null,
    val error: String? = null,
    val successMessage: String? = null, // Success message for toast
    val selectedCategory: WallpaperCategory? = null,
    /** Download progress 0f..1f when downloading, null otherwise */
    val downloadProgress: Float? = null,
    /** Downloaded bytes */
    val downloadedBytes: Long = 0L,
    /** Total bytes to download */
    val totalBytes: Long = 0L
)

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: WallpaperRepository,
    private val analyticsUseCase: AnalyticsUseCase,
    private val wallpapersCraftService: WallpapersCraftService,
    private val wallpapersCraftMapper: WallpapersCraftMapper
) : ViewModel() {

    private val wallpaperUtils = WallpaperUtils(context)
    private val _uiState = MutableStateFlow(WallpaperUiState())
    val uiState: StateFlow<WallpaperUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
        loadCurrentWallpapers()
    }

    /**
     * Get screen dimensions for API calls
     */
    private fun getScreenDimensions(): Pair<Int, Int> {
        val resources: Resources = context.resources
        val widthPx = resources.displayMetrics.widthPixels
        val heightPx = resources.displayMetrics.heightPixels
        return Pair(widthPx, heightPx)
    }

    /**
     * Load categories from WallpapersCraft API
     */
    fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingCategories = true)
            try {
                val (screenWidth, screenHeight) = getScreenDimensions()
                val result = wallpapersCraftService.getCategories(
                    screenWidth = screenWidth,
                    screenHeight = screenHeight
                )
                result.fold(
                    onSuccess = { categoriesResponse ->
                        val categoryPairs = categoriesResponse.items.map { categoryItem ->
                            wallpapersCraftMapper.mapCategory(categoryItem)
                        }
                        _uiState.value = _uiState.value.copy(
                            isLoadingCategories = false,
                            categories = categoryPairs,
                            error = null
                        )
                        selectNew()
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoadingCategories = false,
                            error = exception.message ?: "Failed to load categories"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingCategories = false,
                    error = e.message
                )
            }
        }
    }

    /**
     * Select new and load new images (first page)
     */
    fun selectNew() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                selectedCategoryId = null,
                isParallaxSelected = false,
                isNewSelected = true,
                isLoadingImages = true,
                wallpapers = emptyList(),
                currentOffset = 0,
                hasMore = true
            )
            try {
                val (screenWidth, screenHeight) = getScreenDimensions()
                val result = wallpapersCraftService.getNewImages(
                    screenWidth = screenWidth,
                    screenHeight = screenHeight,
                    offset = 0,
                    limit = 60
                )
                result.fold(
                    onSuccess = { imagesResponse ->
                        val wallpapers = imagesResponse.items.map { imageItem ->
                            wallpapersCraftMapper.mapToWallpaper(imageItem)
                        }
                        // Create a map of wallpaper ID to ImageItem
                        val imageItemsMap = imagesResponse.items.associateBy { imageItem ->
                            "wallpaperscraft_${imageItem.id}"
                        }
                        // For new images, we don't have a count, so check if we got less than limit
                        val hasMore = imagesResponse.items.size >= 60
                        val currentOffset = wallpapers.size
                        _uiState.value = _uiState.value.copy(
                            isLoadingImages = false,
                            wallpapers = wallpapers,
                            imageItems = imageItemsMap,
                            currentOffset = currentOffset,
                            hasMore = hasMore,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoadingImages = false,
                            error = exception.message ?: "Failed to load new images"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingImages = false,
                    error = e.message
                )
            }
        }
    }

    /**
     * Select parallax and load parallax images (first page)
     */
    fun selectParallax() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                selectedCategoryId = null,
                isParallaxSelected = true,
                isNewSelected = false,
                isLoadingImages = true,
                wallpapers = emptyList(),
                currentOffset = 0,
                hasMore = true
            )
            try {
                val result = wallpapersCraftService.getParallaxImages(
                    offset = 0,
                    limit = 60
                )
                result.fold(
                    onSuccess = { imagesResponse ->
                        val wallpapers = imagesResponse.items.map { imageItem ->
                            wallpapersCraftMapper.mapToWallpaper(imageItem)
                        }
                        // Create a map of wallpaper ID to ImageItem
                        val imageItemsMap = imagesResponse.items.associateBy { imageItem ->
                            "wallpaperscraft_${imageItem.id}"
                        }
                        val totalCount = imagesResponse.count
                        val currentOffset = wallpapers.size
                        _uiState.value = _uiState.value.copy(
                            isLoadingImages = false,
                            wallpapers = wallpapers,
                            imageItems = imageItemsMap,
                            currentOffset = currentOffset,
                            hasMore = currentOffset < totalCount,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoadingImages = false,
                            error = exception.message ?: "Failed to load parallax images"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingImages = false,
                    error = e.message
                )
            }
        }
    }

    /**
     * Select a category and load its images (first page)
     */
    fun selectCategory(categoryId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                selectedCategoryId = categoryId,
                isParallaxSelected = false,
                isNewSelected = false,
                isLoadingImages = true,
                wallpapers = emptyList(),
                currentOffset = 0,
                hasMore = true
            )
            try {
                val (screenWidth, screenHeight) = getScreenDimensions()
                val result = wallpapersCraftService.getImages(
                    categoryId = categoryId,
                    screenWidth = screenWidth,
                    screenHeight = screenHeight,
                    offset = 0,
                    limit = 60
                )
                result.fold(
                    onSuccess = { imagesResponse ->
                        val wallpapers = imagesResponse.items.map { imageItem ->
                            wallpapersCraftMapper.mapToWallpaper(imageItem)
                        }
                        // Create a map of wallpaper ID to ImageItem
                        val imageItemsMap = imagesResponse.items.associateBy { imageItem ->
                            "wallpaperscraft_${imageItem.id}"
                        }
                        val totalCount = imagesResponse.count
                        val currentOffset = wallpapers.size
                        _uiState.value = _uiState.value.copy(
                            isLoadingImages = false,
                            wallpapers = wallpapers,
                            imageItems = imageItemsMap,
                            currentOffset = currentOffset,
                            hasMore = currentOffset < totalCount, // Check if we have more items based on total count
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoadingImages = false,
                            error = exception.message ?: "Failed to load images"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingImages = false,
                    error = e.message
                )
            }
        }
    }

    /**
     * Load more wallpapers (pagination)
     */
    fun loadMoreWallpapers() {
        val currentState = _uiState.value
        if (currentState.isLoadingMore || !currentState.hasMore) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingMore = true)
            try {
                val result = when {
                    currentState.isNewSelected -> {
                        // Load more new images
                        val (screenWidth, screenHeight) = getScreenDimensions()
                        wallpapersCraftService.getNewImages(
                            screenWidth = screenWidth,
                            screenHeight = screenHeight,
                            offset = currentState.currentOffset,
                            limit = 60
                        )
                    }

                    currentState.isParallaxSelected -> {
                        // Load more parallax images
                        wallpapersCraftService.getParallaxImages(
                            offset = currentState.currentOffset,
                            limit = 60
                        )
                    }

                    else -> {
                        // Load more category images
                        val categoryId = currentState.selectedCategoryId ?: return@launch
                        val (screenWidth, screenHeight) = getScreenDimensions()
                        wallpapersCraftService.getImages(
                            categoryId = categoryId,
                            screenWidth = screenWidth,
                            screenHeight = screenHeight,
                            offset = currentState.currentOffset,
                            limit = 60
                        )
                    }
                }

                result.fold(
                    onSuccess = { imagesResponse ->
                        val newWallpapers = imagesResponse.items.map { imageItem ->
                            wallpapersCraftMapper.mapToWallpaper(imageItem)
                        }
                        // Create a map of new wallpaper IDs to ImageItems
                        val newImageItemsMap = imagesResponse.items.associateBy { imageItem ->
                            "wallpaperscraft_${imageItem.id}"
                        }
                        val newOffset = currentState.currentOffset + newWallpapers.size
                        
                        // Determine hasMore based on response type
                        val hasMore = if (currentState.isNewSelected) {
                            // For new images, check if we got a full page
                            imagesResponse.items.size >= 60
                        } else {
                            // For other endpoints, use count
                            val totalCount = imagesResponse.count
                            newOffset < totalCount
                        }
                        
                        _uiState.value = _uiState.value.copy(
                            isLoadingMore = false,
                            wallpapers = currentState.wallpapers + newWallpapers,
                            imageItems = currentState.imageItems + newImageItemsMap,
                            currentOffset = newOffset,
                            hasMore = hasMore,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoadingMore = false,
                            error = exception.message ?: "Failed to load more images"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    error = e.message
                )
            }
        }
    }

    fun loadWallpapers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val localWallpapers = repository.getAllWallpapers()
                val remoteWallpapers = repository.fetchRemoteWallpapers()

                // Combine them, keeping local ones first or as preferred
                val allWallpapers = localWallpapers + remoteWallpapers

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    wallpapers = allWallpapers,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun loadCurrentWallpapers() {
        viewModelScope.launch {
            try {
                val homeWallpaper = repository.getCurrentHomeWallpaper()
                val lockWallpaper = repository.getCurrentLockWallpaper()
                _uiState.value = _uiState.value.copy(
                    currentHomeWallpaper = homeWallpaper,
                    currentLockWallpaper = lockWallpaper
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun addWallpaperFromUri(
        uri: Uri,
        name: String,
        category: WallpaperCategory = WallpaperCategory.CUSTOM,
        autoSetHome: Boolean = false,
        autoSetLock: Boolean = false
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Load bitmap from URI
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (bitmap == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load image"
                    )
                    return@launch
                }

                // Save to local storage
                val wallpaperId = System.currentTimeMillis().toString()
                val saveResult = wallpaperUtils.saveWallpaperToLocal(bitmap, wallpaperId)

                if (saveResult.isFailure) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = saveResult.exceptionOrNull()?.message ?: "Failed to save wallpaper"
                    )
                    return@launch
                }

                val localPath = saveResult.getOrNull() ?: return@launch

                // Create wallpaper object
                val wallpaper = Wallpaper(
                    id = wallpaperId,
                    name = name,
                    localPath = localPath,
                    category = category,
                    isLocal = true
                )

                // Save to repository
                repository.saveWallpaper(wallpaper)

                // Auto set if requested
                if (autoSetHome || autoSetLock) {
                    val type = when {
                        autoSetHome && autoSetLock -> WallpaperType.BOTH
                        autoSetHome -> WallpaperType.HOME
                        else -> WallpaperType.LOCK
                    }
                    setWallpaper(wallpaper, type)
                }

                loadWallpapers()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun setWallpaper(wallpaper: Wallpaper, type: WallpaperType) {
        // Track set wallpaper click
        analyticsUseCase.trackSetWallpaperClick()

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val result = wallpaperUtils.setWallpaper(wallpaper, type)

                if (result.isSuccess) {
                    // Save current wallpaper preference
                    val successMessage = when (type) {
                        WallpaperType.HOME -> context.getString(com.app.screentime.config.R.string.wallpaper_set_home_screen)
                        WallpaperType.LOCK -> context.getString(com.app.screentime.config.R.string.wallpaper_set_lock_screen)
                        WallpaperType.BOTH -> context.getString(com.app.screentime.config.R.string.wallpaper_set_both_screens)
                    }
                    when (type) {
                        WallpaperType.HOME -> repository.setCurrentHomeWallpaper(wallpaper.id)
                        WallpaperType.LOCK -> repository.setCurrentLockWallpaper(wallpaper.id)
                        WallpaperType.BOTH -> {
                            repository.setCurrentHomeWallpaper(wallpaper.id)
                            repository.setCurrentLockWallpaper(wallpaper.id)
                        }
                    }
                    loadCurrentWallpapers()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null,
                        successMessage = successMessage
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Failed to set wallpaper"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }


    /**
     * Get API categories as list of (id, title) pairs
     */
    fun getApiCategories(): List<Pair<Int, String>> {
        return _uiState.value.categories
    }

    /**
     * Get ImageItem by wallpaper ID
     */
    fun getImageItem(wallpaperId: String): ImageItem? {
        return _uiState.value.imageItems[wallpaperId]
    }

    /**
     * Download wallpaper to device storage
     */
    fun downloadWallpaper(wallpaper: Wallpaper) {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(downloadProgress = 0f, downloadedBytes = 0L, totalBytes = 0L)
            try {
                val imageItem = getImageItem(wallpaper.id)
                val result = wallpaperUtils.downloadWallpaperToDownloads(
                    wallpaper = wallpaper,
                    imageItem = imageItem,
                    onProgress = { progress, downloaded, total ->
                        _uiState.value = _uiState.value.copy(
                            downloadProgress = progress,
                            downloadedBytes = downloaded,
                            totalBytes = total
                        )
                    }
                )
                result.fold(
                    onSuccess = {
                        _uiState.value = _uiState.value.copy(
                            downloadProgress = null,
                            downloadedBytes = 0L,
                            totalBytes = 0L,
                            error = null
                        )
                        android.widget.Toast.makeText(
                            context,
                            "Wallpaper saved to Pictures/Wallpapers",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            downloadProgress = null,
                            error = exception.message ?: "Failed to download wallpaper"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    downloadProgress = null,
                    error = e.message
                )
            }
        }
    }

    /**
     * Search wallpapers by query
     */
    suspend fun searchWallpapers(query: String): Result<Pair<List<Wallpaper>, Map<String, ImageItem>>> {
        return try {
            val (screenWidth, screenHeight) = getScreenDimensions()
            val result = wallpapersCraftService.searchImages(
                query = query,
                screenWidth = screenWidth,
                screenHeight = screenHeight,
                offset = 0,
                limit = 60
            )
            result.fold(
                onSuccess = { imagesResponse ->
                    val wallpapers = imagesResponse.items.map { imageItem ->
                        wallpapersCraftMapper.mapToWallpaper(imageItem)
                    }
                    // Create a map of wallpaper ID to ImageItem
                    val imageItemsMap = imagesResponse.items.associateBy { imageItem ->
                        "wallpaperscraft_${imageItem.id}"
                    }
                    Result.success(Pair(wallpapers, imageItemsMap))
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

