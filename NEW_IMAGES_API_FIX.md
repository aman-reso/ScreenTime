# New Images API Response Structure Fix

## Issue
The new images API endpoint (`/images/new`) returns a different response structure compared to other endpoints:

### Standard Images Response:
```json
{
    "count": 120,
    "items": [...],
    "response_time": "0.123"
}
```

### New Images Response:
```json
{
    "first_published_id": 1270065,
    "items": [...]
}
```

**Key Difference**: The new images endpoint does NOT include a `count` field but has `first_published_id` instead.

## Problem
The original `ImagesResponse` model expected `count` to always be present, which would cause deserialization issues with the new images endpoint.

## Solution

### 1. Updated `ImagesResponse` Model
**File**: `wallpaper/src/main/java/com/app/screentime/wallpaper/api/model/WallpapersCraftModels.kt`

```kotlin
@Serializable
data class ImagesResponse(
    val count: Int = 0, // Made optional with default value for new images endpoint
    val items: List<ImageItem>,
    val response_time: String? = null,
    val first_published_id: Int? = null // Added for new images endpoint
)
```

**Changes:**
- Made `count` optional with default value `0`
- Added `first_published_id` field as nullable

### 2. Updated Pagination Logic for New Tab
**File**: `wallpaper/src/main/java/com/app/screentime/wallpaper/viewmodel/WallpaperViewModel.kt`

#### In `selectNew()` function:
```kotlin
result.fold(
    onSuccess = { imagesResponse ->
        // ... wallpaper mapping ...
        
        // For new images, we don't have a count, so check if we got less than limit
        val hasMore = imagesResponse.items.size >= 60
        
        _uiState.value = _uiState.value.copy(
            // ... other fields ...
            hasMore = hasMore,
            error = null
        )
    },
    // ... error handling ...
)
```

**Logic**: If we receive 60 items (the limit), assume there are more pages. If we receive less than 60, we've reached the end.

#### In `loadMoreWallpapers()` function:
```kotlin
result.fold(
    onSuccess = { imagesResponse ->
        // ... wallpaper mapping ...
        
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
            // ... other fields ...
            hasMore = hasMore,
            error = null
        )
    },
    // ... error handling ...
)
```

**Logic**: 
- For **New tab**: Check if items.size >= 60 (full page means more available)
- For **other tabs**: Use the traditional `count` field for accurate pagination

## How It Works

### Initial Load (New Tab)
1. Fetch 60 new wallpapers
2. Check: `items.size >= 60`?
   - **Yes** → `hasMore = true` (enable pagination)
   - **No** → `hasMore = false` (no more pages)

### Load More (Pagination)
1. Fetch next 60 wallpapers from offset
2. Check: `items.size >= 60`?
   - **Yes** → Keep `hasMore = true`
   - **No** → Set `hasMore = false` (last page)

### Other Tabs (Parallax, Categories)
- Use traditional `count` field
- Calculate: `newOffset < totalCount`
- More precise pagination control

## Benefits

1. **Flexible Model**: Handles both response structures
2. **Graceful Degradation**: Works without count field
3. **Accurate Pagination**: Uses appropriate logic per endpoint
4. **No Breaking Changes**: Other endpoints continue working as before
5. **API Compatibility**: Ready for future endpoint variations

## Testing Checklist

- [x] New tab loads successfully
- [x] New tab pagination works (loads more on scroll)
- [x] New tab stops pagination when no more items
- [x] Parallax tab still works with count-based pagination
- [x] Category tabs still work with count-based pagination
- [x] No deserialization errors
- [x] Full compilation successful
- [x] No linter errors

## Example Response Handling

### Full Page (60 items):
```
Received: 60 items
hasMore = (60 >= 60) = true
→ User can scroll for more
```

### Partial Page (35 items):
```
Received: 35 items
hasMore = (35 >= 60) = false
→ No more wallpapers available
```

### Empty Result:
```
Received: 0 items
hasMore = (0 >= 60) = false
→ No wallpapers or end of list
```

## Build Status
- ✅ Compilation: Success
- ✅ Linter: No errors
- ✅ Full App Build: Success
- ✅ Version: 7.3 (versionCode: 73)

## Related Files
- `WallpapersCraftModels.kt` - Updated response model
- `WallpaperViewModel.kt` - Updated pagination logic
- `WallpapersCraftService.kt` - Added Ktor logging (for debugging)

## Additional Enhancement: Debug Logging

Added Ktor logging for debugging API responses in debug builds:

```kotlin
if (BuildConfig.DEBUG) {
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.d("Ktor", message)
            }
        }
        level = LogLevel.ALL
    }
}
```

This helps verify API responses during development.
