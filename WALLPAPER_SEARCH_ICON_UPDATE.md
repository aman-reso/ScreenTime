# Wallpaper Screen - Search Icon Addition

## Overview
Added a search icon to the Wallpaper screen header that navigates to the WallpaperSearchScreen when clicked.

## Changes Made

### File Modified
**`wallpaper/src/main/java/com/app/screentime/wallpaper/screen/WallpaperScreen.kt`**

### 1. Added Navigation Parameter
```kotlin
@Composable
fun WallpaperScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onNavigateToFullScreen: (String, ImageItem?) -> Unit = { _, _ -> },
    onNavigateToSearch: () -> Unit = {},  // NEW PARAMETER
    viewModel: WallpaperViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
)
```

### 2. Added Search Icon in Header
Added the search icon using ODSPageHeader's `actionsSlot`:

```kotlin
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
```

### 3. Added Import
```kotlin
import androidx.compose.material.icons.filled.Search
```

## Implementation Details

### Icon
- **Type**: Material Icons `Search`
- **Size**: 24dp x 24dp
- **Color**: Uses `scheme.basicText` for theme consistency
- **Padding**: Right padding of `DSVariables.spacingComponent3`

### Interaction
- **Click Handler**: Uses `.onClick { onNavigateToSearch() }` extension
- **Navigation**: Triggers the `onNavigateToSearch` callback
- **Accessibility**: Includes content description from localized strings

## How to Use

When implementing navigation in your app, pass the navigation callback:

```kotlin
// In your navigation composable
WallpaperScreen(
    onBackClick = { navController.popBackStack() },
    onNavigateToFullScreen = { wallpaperId, imageItem ->
        navController.navigate("fullscreen/$wallpaperId")
    },
    onNavigateToSearch = {
        navController.navigate("wallpaper_search")
    }
)
```

## Visual Design
- The search icon appears in the top-right corner of the Wallpaper screen header
- Follows ODS design principles using ODSIcon component
- Maintains consistency with the app's color scheme
- Responsive and touch-friendly (24dp size)

## Build Status
- ✅ Compilation: Success
- ✅ Linter: No errors
- ✅ Full App Build: Success

## Related Files
- `WallpaperSearchScreen.kt` - The destination screen for search
- `strings.xml` - Contains `search_wallpapers` localized string

## Version
- Version Code: 73
- Version Name: 7.3
