# New Tab Implementation for Wallpaper Screen

## Overview
Added a "New" category tab as the first tab in the Wallpaper screen that fetches the latest/newest wallpapers using the WallpapersCraft new images API endpoint.

## API Endpoint Used
```
GET https://api-uc.wallpaperscraft.com/images/new
```

### Parameters:
- `screen[width]`: Screen width (1080)
- `screen[height]`: Screen height (2400)
- `lang`: Language code (en)
- `limit`: Number of results (60)
- `offset`: Pagination offset (0 for first page)

## Files Modified

### 1. WallpapersCraftService.kt
**Location**: `wallpaper/src/main/java/com/app/screentime/wallpaper/api/service/WallpapersCraftService.kt`

**Added Function**:
```kotlin
/**
 * Get new images
 * @param screenWidth Screen width (default: 1080)
 * @param screenHeight Screen height (default: 2400)
 * @param lang Language code (default: "en")
 * @param limit Limit of results (default: 60)
 * @param offset Offset for pagination (default: 0)
 */
suspend fun getNewImages(
    screenWidth: Int = 1080,
    screenHeight: Int = 2400,
    lang: String = "en",
    limit: Int = 60,
    offset: Int = 0
): Result<ImagesResponse>
```

### 2. WallpaperViewModel.kt
**Location**: `wallpaper/src/main/java/com/app/screentime/wallpaper/viewmodel/WallpaperViewModel.kt`

#### Changes:

**a) Updated WallpaperUiState:**
```kotlin
data class WallpaperUiState(
    // ... existing fields ...
    val isNewSelected: Boolean = false, // NEW: Track if new tab is selected
    // ... other fields ...
)
```

**b) Added selectNew() Function:**
```kotlin
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
        // Fetch new images from API
        // Map and display wallpapers
    }
}
```

**c) Updated selectParallax():**
- Now sets `isNewSelected = false` when parallax is selected

**d) Updated selectCategory():**
- Now sets `isNewSelected = false` when a category is selected

**e) Updated loadMoreWallpapers():**
- Now checks which tab is selected (New, Parallax, or Category)
- Uses `when` statement to fetch appropriate images:
  - `isNewSelected = true` → fetch more new images
  - `isParallaxSelected = true` → fetch more parallax images
  - else → fetch more category images

**f) Updated loadCategories():**
- Changed to load new images by default: `selectNew()` instead of `selectParallax()`

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
    isParallaxSelected: Boolean,
    isNewSelected: Boolean,              // NEW
    onCategorySelected: (Int) -> Unit,
    onParallaxSelected: () -> Unit,
    onNewSelected: () -> Unit,           // NEW
    scheme: ODSTheme
)
```

**Tab Structure:**
```kotlin
val tabElements = buildList {
    add(ODSTabItemModel(label = "New"))      // First tab
    add(ODSTabItemModel(label = "Parallax")) // Second tab
    addAll(categories.map { (_, title) ->
        ODSTabItemModel(label = title)
    })
}
```

**Selection Logic:**
```kotlin
val selectedIndex = when {
    isNewSelected -> 0      // New is the first tab
    isParallaxSelected -> 1 // Parallax is the second tab
    else -> {
        selectedCategoryId?.let { categoryId ->
            categories.indexOfFirst { it.first == categoryId }
        }?.let { it + 2 } ?: 0 // +2 because New and Parallax are at index 0 and 1
    }
}
```

**Click Handler:**
```kotlin
onSelectedTabChange = { index ->
    when (index) {
        0 -> onNewSelected()       // New selected
        1 -> onParallaxSelected()  // Parallax selected
        else -> {
            if (index - 2 < categories.size) {
                // Category selected (subtract 2 because New and Parallax occupy indices 0 and 1)
                onCategorySelected(categories[index - 2].first)
            }
        }
    }
}
```

**b) Updated CategoryTabs Usage:**
```kotlin
CategoryTabs(
    categories = uiState.categories,
    selectedCategoryId = uiState.selectedCategoryId,
    isParallaxSelected = uiState.isParallaxSelected,
    isNewSelected = uiState.isNewSelected,        // NEW
    onCategorySelected = { categoryId ->
        viewModel.selectCategory(categoryId)
    },
    onParallaxSelected = {
        viewModel.selectParallax()
    },
    onNewSelected = {                             // NEW
        viewModel.selectNew()
    },
    scheme = scheme
)
```

## Tab Order

The tabs now appear in the following order:
1. **New** (Latest wallpapers)
2. **Parallax** (Parallax effect wallpapers)
3. **Nature** (Category)
4. **Architecture** (Category)
5. **Abstract** (Category)
6. ... (Other categories)

## Features

### Tab Behavior
1. **"New" Tab**: Always appears as the first tab - shows latest wallpapers
2. **"Parallax" Tab**: Always appears as the second tab - shows parallax wallpapers
3. **Category Tabs**: Follow after New and Parallax
4. **Default Selection**: New tab is selected by default on screen load
5. **Visual Indication**: Selected tab is highlighted using ODS tab styling

### Data Loading
1. **Initial Load**: Fetches 60 new wallpapers
2. **Pagination**: Loads more wallpapers when scrolling near bottom
3. **Seamless Switching**: Switching between tabs clears current list and loads new data
4. **Loading States**: Shows shimmer effect during initial load
5. **Error Handling**: Displays error messages if API fails

### Image Quality
- Uses screen dimensions (1080x2400) for optimal image quality
- Maintains consistent quality across all wallpapers

## User Flow

1. **Open Wallpaper Screen** → New tab is selected by default → 60 new wallpapers load
2. **Click Parallax Tab** → Parallax wallpapers load → New state cleared
3. **Click Category Tab** → Category wallpapers load → Previous states cleared
4. **Click New Tab Again** → Returns to new wallpapers → Reloads from start
5. **Scroll Down** → Pagination loads more wallpapers based on current tab selection

## API Response Handling

The new images API returns the same `ImagesResponse` structure as other endpoints:
- `items`: Array of `ImageItem` objects
- `count`: Total count for pagination

Each wallpaper is:
1. Mapped to `Wallpaper` model using `WallpapersCraftMapper`
2. Stored with unique ID: `"wallpaperscraft_${imageItem.id}"`
3. Associated with its `ImageItem` for full-screen viewing

## State Management

The ViewModel now tracks three mutually exclusive states:
- `isNewSelected`: True when New tab is active
- `isParallaxSelected`: True when Parallax tab is active  
- `selectedCategoryId != null`: When a category tab is active

Only one of these can be true at any time.

## Testing Checklist

- [x] New tab appears as first tab
- [x] New is selected by default on load
- [x] New wallpapers load successfully
- [x] Switching to Parallax tab works
- [x] Switching to category tab works
- [x] Switching back to New tab works
- [x] Pagination works for new wallpapers
- [x] Pagination works for parallax wallpapers after switching
- [x] Pagination works for category wallpapers after switching
- [x] Loading shimmer displays correctly
- [x] Error handling works
- [x] Click on wallpaper opens full-screen view
- [x] Full compilation successful

## API Response Structure Note

⚠️ **Important**: The new images endpoint has a different response structure:
- Does NOT include `count` field
- Includes `first_published_id` field instead
- Pagination is determined by checking if `items.size >= limit`

See `NEW_IMAGES_API_FIX.md` for detailed information on how this is handled.

## Build Status
- ✅ Compilation: Success
- ✅ Linter: No errors
- ✅ Full App Build: Success
- ✅ Version: 7.3 (versionCode: 73)
- ✅ API Response Handling: Fixed

## Benefits

1. **Latest Content First**: Users see the newest wallpapers immediately
2. **Easy Discovery**: New wallpapers are prominently featured
3. **Multiple Options**: Users can choose between New, Parallax, or Category-specific wallpapers
4. **Seamless Integration**: Uses existing wallpaper display and interaction logic
5. **Efficient API Usage**: Proper pagination reduces unnecessary API calls
6. **Consistent UI**: Follows ODS design system for tabs and wallpaper cards

## Comparison with Previous Implementation

**Before:**
- Parallax → Categories

**After:**
- **New** → Parallax → Categories

The New tab now takes priority and loads by default, giving users immediate access to the latest wallpapers.
