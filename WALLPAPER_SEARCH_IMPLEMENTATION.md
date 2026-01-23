# Wallpaper Search Screen Implementation

## Overview
Created a new wallpaper search screen using the WallpapersCraft API with ODS Search Bar and LazyGrid for displaying results.

## Files Created/Modified

### 1. New Files

#### `wallpaper/src/main/java/com/app/screentime/wallpaper/screen/WallpaperSearchScreen.kt`
- **Composable**: `WallpaperSearchScreen`
- **Features**:
  - ODS Search Bar with search/clear button
  - LazyVerticalGrid for displaying search results (2 or 3 columns based on screen size)
  - Loading shimmer effect during search
  - Empty state for no results
  - Error state handling
  - Reuses `WallpaperCard` component
  - Keyboard actions for search on IME action
  - Responsive grid layout (2 columns for mobile, 3 for tablets)

### 2. Modified Files

#### `wallpaper/src/main/java/com/app/screentime/wallpaper/screen/WallpaperScreen.kt`
- **Added**: `onNavigateToSearch` parameter for navigation callback
- **Added**: Search icon button in the page header's `actionsSlot`
- **Features**:
  - Material Icons Search icon in the header
  - Clickable icon that triggers `onNavigateToSearch()` callback
  - Proper padding and sizing (24dp)
  - Uses scheme colors for theming

#### `wallpaper/src/main/java/com/app/screentime/wallpaper/api/service/WallpapersCraftService.kt`
- **Added**: `searchImages()` function
- **Parameters**:
  - `query`: Search term
  - `screenWidth`: Screen width (default: 1080)
  - `screenHeight`: Screen height (default: 2400)
  - `lang`: Language (default: "en")
  - `limit`: Results limit (default: 60)
  - `offset`: Pagination offset (default: 0)
  - `costVariant`: Cost filter (default: "free,private")
- **Returns**: `Result<ImagesResponse>`

#### `wallpaper/src/main/java/com/app/screentime/wallpaper/viewmodel/WallpaperViewModel.kt`
- **Added**: `searchWallpapers(query: String)` suspend function
- **Returns**: `Result<Pair<List<Wallpaper>, Map<String, ImageItem>>>`
- Fetches search results from API and maps them to UI models
- Uses screen dimensions from device for optimal image quality

## String Resources Added

### English (values/strings.xml)
```xml
<string name="search_wallpapers">Search Wallpapers</string>
<string name="search_placeholder">Search for wallpapers…</string>
<string name="clear_search">Clear search</string>
<string name="search">Search</string>
<string name="search_failed">Search failed</string>
<string name="no_results_found">No results found</string>
<string name="try_different_search">Try a different search term</string>
<string name="search_wallpapers_prompt">Enter a search term to find wallpapers</string>
<string name="error_occurred">An error occurred</string>
```

### Hindi (values-hi/strings.xml)
```xml
<string name="search_wallpapers">वॉलपेपर खोजें</string>
<string name="search_placeholder">वॉलपेपर खोजें…</string>
<string name="clear_search">खोज साफ़ करें</string>
<string name="search">खोजें</string>
<string name="search_failed">खोज विफल</string>
<string name="no_results_found">कोई परिणाम नहीं मिला</string>
<string name="try_different_search">एक अलग खोज शब्द का प्रयास करें</string>
<string name="search_wallpapers_prompt">वॉलपेपर खोजने के लिए खोज शब्द दर्ज करें</string>
<string name="error_occurred">कोई त्रुटि हुई</string>
```

### Bengali (values-bn/strings.xml)
```xml
<string name="search_wallpapers">ওয়ালপেপার খুঁজুন</string>
<string name="search_placeholder">ওয়ালপেপার খুঁজুন…</string>
<string name="clear_search">অনুসন্ধান সাফ করুন</string>
<string name="search">খুঁজুন</string>
<string name="search_failed">অনুসন্ধান ব্যর্থ</string>
<string name="no_results_found">কোন ফলাফল পাওয়া যায়নি</string>
<string name="try_different_search">একটি ভিন্ন অনুসন্ধান শব্দ চেষ্টা করুন</string>
<string name="search_wallpapers_prompt">ওয়ালপেপার খুঁজতে একটি অনুসন্ধান শব্দ লিখুন</string>
<string name="error_occurred">একটি ত্রুটি ঘটেছে</string>
```

### German (values-de/strings.xml)
```xml
<string name="search_wallpapers">Hintergrundbilder suchen</string>
<string name="search_placeholder">Hintergrundbilder suchen…</string>
<string name="clear_search">Suche löschen</string>
<string name="search">Suchen</string>
<string name="search_failed">Suche fehlgeschlagen</string>
<string name="no_results_found">Keine Ergebnisse gefunden</string>
<string name="try_different_search">Versuchen Sie einen anderen Suchbegriff</string>
<string name="search_wallpapers_prompt">Geben Sie einen Suchbegriff ein, um Hintergrundbilder zu finden</string>
<string name="error_occurred">Ein Fehler ist aufgetreten</string>
```

## API Endpoint Used
```
GET https://api-uc.wallpaperscraft.com/images
```

### Parameters:
- `screen[width]`: 1080
- `screen[height]`: 2400
- `lang`: en
- `limit`: 60
- `types[]`: free,private
- `offset`: 0
- `query`: {search_term}
- `cost_variant`: free,private

## UI Components Used

### ODS Components
1. **ODSSearchBar** - Search input with button
2. **ODSPageHeader** - Page header with back button
3. **ODSBox** - Container layouts
4. **ODSColumn** - Vertical layouts
5. **ODSText** - Text elements
6. **ODSLoadingSpinner** - Loading states (via WallpaperGridShimmer)

### Layout Components
1. **LazyVerticalGrid** - Grid layout for wallpaper cards
2. **GridCells.Fixed** - 2 or 3 columns based on screen size
3. **currentWindowAdaptiveInfo** - Responsive layout detection

### Reused Components
1. **WallpaperCard** - Individual wallpaper card display
2. **WallpaperGridShimmer** - Loading skeleton

## Features

### Search Functionality
- Real-time search query input
- Search button or IME action triggers search
- Clear button to reset search
- Keyboard auto-hide on search

### States Handled
1. **Initial State**: Shows prompt to enter search term
2. **Loading State**: Displays shimmer effect
3. **Results State**: Shows wallpapers in grid
4. **Empty State**: "No results found" message
5. **Error State**: Error message display

### Responsive Design
- 2 columns on mobile devices
- 3 columns on tablets (width >= 840dp)
- Proper spacing and padding using DSVariables

### Navigation
- Back button to previous screen
- Click on wallpaper navigates to full-screen view
- Passes ImageItem data for full functionality

## Navigation Setup

### Wallpaper Screen with Search Icon

```kotlin
WallpaperScreen(
    modifier = Modifier.fillMaxSize(),
    onBackClick = { navController.popBackStack() },
    onNavigateToFullScreen = { wallpaperId, imageItem ->
        navController.navigate("fullscreen/$wallpaperId")
    },
    onNavigateToSearch = {
        navController.navigate("wallpaper_search")
    },
    viewModel = hiltViewModel(),
    scheme = neutralScheme
)
```

### Wallpaper Search Screen

```kotlin
WallpaperSearchScreen(
    modifier = Modifier.fillMaxSize(),
    onBackClick = { navController.popBackStack() },
    onNavigateToFullScreen = { wallpaperId, imageItem ->
        navController.navigate("fullscreen/$wallpaperId")
    },
    viewModel = hiltViewModel(),
    scheme = neutralScheme
)
```

## Testing Checklist

### Wallpaper Screen
- [ ] Search icon appears in header
- [ ] Search icon click navigates to search screen
- [ ] Back button from search returns to wallpaper screen

### Search Screen
- [ ] Search with valid query (e.g., "nature", "abstract")
- [ ] Search with invalid/no results query
- [ ] Clear button functionality
- [ ] Keyboard IME action triggers search
- [ ] Loading state displays correctly
- [ ] Empty state displays correctly
- [ ] Error state displays correctly
- [ ] Responsive grid (2/3 columns)
- [ ] Click on wallpaper navigates correctly
- [ ] Back button works
- [ ] Test on different screen sizes
- [ ] Test with different locales (en, hi, bn, de)

## Version
- Version Code: 73
- Version Name: 7.3
