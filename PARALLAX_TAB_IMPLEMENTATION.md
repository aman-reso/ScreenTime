# Parallax Tab Implementation for Wallpaper Screen

## Overview
Added a "Parallax" category tab as the first tab in the Wallpaper screen that fetches parallax wallpapers using the WallpapersCraft parallax-images API endpoint.

## API Endpoint Used
```
GET https://api-uc.wallpaperscraft.com/parallax-images
```

### Parameters:
- `resolution`: fhd (Full HD)
- `offset`: Pagination offset (default: 0)
- `limit`: Number of results (default: 60)
- `cost_variant`: free

## Files Modified

### 1. WallpapersCraftService.kt
**Location**: `wallpaper/src/main/java/com/app/screentime/wallpaper/api/service/WallpapersCraftService.kt`

**Added Function**:
```kotlin
/**
 * Get parallax images
 * @param resolution Resolution (default: "fhd")
 * @param offset Offset for pagination (default: 0)
 * @param limit Limit of results (default: 60)
 * @param costVariant Cost variant (default: "free")
 */
suspend fun getParallaxImages(
    resolution: String = "fhd",
    offset: Int = 0,
    limit: Int = 60,
    costVariant: String = "free"
): Result<ImagesResponse>
```

### 2. WallpaperViewModel.kt
**Location**: `wallpaper/src/main/java/com/app/screentime/wallpaper/viewmodel/WallpaperViewModel.kt`

#### Changes:

**a) Updated WallpaperUiState:**
```kotlin
data class WallpaperUiState(
    // ... existing fields ...
    val isParallaxSelected: Boolean = false, // NEW: Track if parallax tab is selected
    // ... other fields ...
)
```

**b) Added selectParallax() Function:**
```kotlin
/**
 * Select parallax and load parallax images (first page)
 */
fun selectParallax() {
    viewModelScope.launch {
        _uiState.value = _uiState.value.copy(
            selectedCategoryId = null,
            isParallaxSelected = true,
            isLoadingImages = true,
            wallpapers = emptyList(),
            currentOffset = 0,
            hasMore = true
        )
        // Fetch parallax images from API
        // Map and display wallpapers
    }
}
```

**c) Updated selectCategory():**
- Now sets `isParallaxSelected = false` when a regular category is selected

**d) Updated loadMoreWallpapers():**
- Now checks if parallax is selected
- Fetches more parallax images if `isParallaxSelected = true`
- Otherwise fetches category images as before

**e) Updated loadCategories():**
- ~~Changed to load parallax by default: `selectParallax()` instead of selecting first category~~
- **Updated**: Now loads new images by default: `selectNew()` (after New tab was added)

### 3. WallpaperScreen.kt
**Location**: `wallpaper/src/main/java/com/app/screentime/wallpaper/screen/WallpaperScreen.kt`

#### Changes:

**a) Updated CategoryTabs Component:**

**New Parameters:**
```kotlin
@Composable
private fun CategoryTabs(
    categories: List<Pair<Int, String>>,
    selectedCategoryId: Int?,
    isParallaxSelected: Boolean,          // NEW
    onCategorySelected: (Int) -> Unit,
    onParallaxSelected: () -> Unit,       // NEW
    scheme: ODSTheme
)
```

**Tab Structure:**
```kotlin
val tabElements = buildList {
    add(ODSTabItemModel(label = "Parallax"))  // First tab
    addAll(categories.map { (_, title) ->
        ODSTabItemModel(label = title)
    })
}
```

**Selection Logic:**
```kotlin
val selectedIndex = if (isParallaxSelected) {
    0 // Parallax is the first tab
} else {
    selectedCategoryId?.let { categoryId ->
        categories.indexOfFirst { it.first == categoryId }
    }?.let { it + 1 } ?: 0 // +1 because Parallax is at index 0
}
```

**Click Handler:**
```kotlin
onSelectedTabChange = { index ->
    if (index == 0) {
        // Parallax selected
        onParallaxSelected()
    } else if (index - 1 < categories.size) {
        // Category selected (subtract 1 because Parallax is at index 0)
        onCategorySelected(categories[index - 1].first)
    }
}
```

**b) Updated CategoryTabs Usage:**
```kotlin
CategoryTabs(
    categories = uiState.categories,
    selectedCategoryId = uiState.selectedCategoryId,
    isParallaxSelected = uiState.isParallaxSelected,  // NEW
    onCategorySelected = { categoryId ->
        viewModel.selectCategory(categoryId)
    },
    onParallaxSelected = {                            // NEW
        viewModel.selectParallax()
    },
    scheme = scheme
)
```

## Features

### Tab Behavior
1. **"Parallax" Tab**: Always appears as the first tab
2. **Category Tabs**: Follow after the Parallax tab
3. **Default Selection**: Parallax tab is selected by default on screen load
4. **Visual Indication**: Selected tab is highlighted using ODS tab styling

### Data Loading
1. **Initial Load**: Fetches 60 parallax wallpapers
2. **Pagination**: Loads more parallax wallpapers when scrolling near bottom
3. **Seamless Switching**: Switching between Parallax and categories clears current list and loads new data
4. **Loading States**: Shows shimmer effect during initial load
5. **Error Handling**: Displays error messages if API fails

### Image Quality
- Uses FHD (Full HD) resolution for parallax wallpapers
- Maintains consistent image quality across all wallpapers

## User Flow

~~1. **Open Wallpaper Screen** → Parallax tab is selected by default → 60 parallax wallpapers load~~
**Updated**: Now the New tab is selected by default (see NEW_TAB_IMPLEMENTATION.md)

1. **Click Parallax Tab** → Parallax wallpapers load
2. **Click Category Tab** → Category wallpapers load → Parallax state cleared
3. **Click Parallax Tab Again** → Returns to parallax wallpapers → Reloads from start
4. **Scroll Down** → Pagination loads more wallpapers based on current tab selection

## API Response Handling

The parallax API returns the same `ImagesResponse` structure as category images:
- `items`: Array of `ImageItem` objects
- `count`: Total count for pagination

Each wallpaper is:
1. Mapped to `Wallpaper` model using `WallpapersCraftMapper`
2. Stored with unique ID: `"wallpaperscraft_${imageItem.id}"`
3. Associated with its `ImageItem` for full-screen viewing

## Testing Checklist

- [x] Parallax tab appears as first tab
- [x] Parallax is selected by default on load
- [x] Parallax wallpapers load successfully
- [x] Switching to category tab works
- [x] Switching back to parallax tab works
- [x] Pagination works for parallax wallpapers
- [x] Pagination works for category wallpapers after switching
- [x] Loading shimmer displays correctly
- [x] Error handling works
- [x] Click on wallpaper opens full-screen view
- [x] Full compilation successful

## Build Status
- ✅ Compilation: Success
- ✅ Linter: No errors
- ✅ Full App Build: Success
- ✅ Version: 7.3 (versionCode: 73)

## Benefits

1. **Enhanced User Experience**: Users get access to high-quality parallax wallpapers
2. **Easy Access**: Parallax is prominently featured as the first tab
3. **Seamless Integration**: Uses existing wallpaper display and interaction logic
4. **Efficient API Usage**: Proper pagination reduces unnecessary API calls
5. **Consistent UI**: Follows ODS design system for tabs and wallpaper cards
