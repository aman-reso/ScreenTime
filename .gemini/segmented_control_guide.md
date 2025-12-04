# SegmentedControl - Material 3 Expressive Tab Component

## Overview
Use `SegmentedControl` consistently across the entire app for all tab-like selections. This component follows Material 3 Expressive design guidelines and uses the centralized color system.

## Design Features
✅ **Material 3 Expressive** - Modern pill-style design
✅ **Centralized Colors** - Uses `AppColors` system
✅ **Consistent Styling** - Same look everywhere
✅ **Smooth Animations** - Automatic theme transitions
✅ **Accessible** - Proper contrast ratios

## Visual Design

### Light Mode:
- **Container Background**: Subtle grey (`componentBg` - #F5F5F7)
- **Selected Tab**: Purple (`buttonPrimary` - #6750A4)
- **Selected Text**: White (`textOnButton`)
- **Unselected Text**: Grey (`textSecondary` - #49454F)

### Dark Mode:
- **Container Background**: Dark grey (`componentBg` - #2B2930)
- **Selected Tab**: Light purple (`buttonPrimary` - #D0BCFF)
- **Selected Text**: Black (`textOnButton`)
- **Unselected Text**: Light grey (`textSecondary` - #CAC4D0)

## Usage Examples

### Basic Usage (2 tabs)
```kotlin
var selectedTab by remember { mutableStateOf(0) }

SegmentedControl(
    items = listOf("Daily", "Weekly"),
    selectedIndex = selectedTab,
    onItemSelected = { selectedTab = it }
)
```

### Three Tabs
```kotlin
var selectedTab by remember { mutableStateOf(0) }

SegmentedControl(
    items = listOf("All", "Active", "Completed"),
    selectedIndex = selectedTab,
    onItemSelected = { selectedTab = it }
)
```

### Four Tabs
```kotlin
var selectedTab by remember { mutableStateOf(0) }

SegmentedControl(
    items = listOf("Today", "Week", "Month", "Year"),
    selectedIndex = selectedTab,
    onItemSelected = { selectedTab = it }
)
```

### With Custom Modifier
```kotlin
SegmentedControl(
    items = listOf("Tab 1", "Tab 2"),
    selectedIndex = selectedTab,
    onItemSelected = { selectedTab = it },
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
)
```

## Where to Use

### ✅ Use SegmentedControl for:
1. **Time period selection** - Daily/Weekly/Monthly
2. **Filter options** - All/Active/Completed
3. **View modes** - List/Grid
4. **Data types** - Apps/Websites/Categories
5. **Status filters** - Pending/Approved/Rejected
6. **Any 2-5 mutually exclusive options**

### ❌ Don't Use SegmentedControl for:
1. **Navigation** - Use Bottom Navigation instead
2. **More than 5 tabs** - Use Dropdown or Chips instead
3. **Non-exclusive options** - Use Checkboxes instead
4. **Single selection from many** - Use Radio buttons or Dropdown

## Replace Old Tab Implementations

### Before (Old TabRow):
```kotlin
TabRow(
    selectedTabIndex = selectedTab,
    containerColor = Color(0xFF...), // Hardcoded
    contentColor = Color(0xFF...)    // Hardcoded
) {
    Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
        Text("Tab 1")
    }
    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
        Text("Tab 2")
    }
}
```

### After (SegmentedControl):
```kotlin
SegmentedControl(
    items = listOf("Tab 1", "Tab 2"),
    selectedIndex = selectedTab,
    onItemSelected = { selectedTab = it }
)
```

## Benefits
1. **Cleaner Code** - Less boilerplate
2. **Consistent Design** - Same look everywhere
3. **Automatic Theming** - Works in light/dark mode
4. **No Hardcoded Colors** - Uses centralized system
5. **Better UX** - Modern pill-style design

## Files to Update

Search for these patterns and replace with SegmentedControl:
- `TabRow`
- `Tab(`
- Custom tab implementations
- Hardcoded tab colors

## Component Location
```
/ui/atom/SegmentedControl.kt
```

Import:
```kotlin
import com.app.screentime.ui.atom.SegmentedControl
```
