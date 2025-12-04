# Replace Card with Box - Material 3 Expressive Guide

## Why Replace Card with Box?

### Problems with Card:
- ❌ Extra boilerplate (`CardDefaults.cardColors`, `CardDefaults.cardElevation`)
- ❌ Unnecessary elevation/shadow by default
- ❌ Less control over styling
- ❌ More verbose code

### Benefits of Box:
- ✅ **Simpler code** - Less boilerplate
- ✅ **Better control** - Direct background, clip, border control
- ✅ **Consistent styling** - Uses centralized colors
- ✅ **Material 3 Expressive** - Flat design, no shadows
- ✅ **Cleaner** - No unnecessary defaults

## Replacement Pattern

### ❌ Before (Card):
```kotlin
Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
        containerColor = appColors.card
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Content
    }
}
```

### ✅ After (Box):
```kotlin
Box(
    modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(appColors.card)
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Content
    }
}
```

## Common Patterns

### 1. Simple Card → Box
```kotlin
// Before
Card(
    colors = CardDefaults.cardColors(containerColor = appColors.card)
) { /* content */ }

// After
Box(
    modifier = Modifier.background(appColors.card)
) { /* content */ }
```

### 2. Card with Border → Box with Border
```kotlin
// Before
Card(
    border = BorderStroke(1.dp, appColors.border),
    colors = CardDefaults.cardColors(containerColor = appColors.card)
) { /* content */ }

// After
Box(
    modifier = Modifier
        .background(appColors.card)
        .border(1.dp, appColors.border, RoundedCornerShape(12.dp))
) { /* content */ }
```

### 3. Card with Rounded Corners → Box with Clip
```kotlin
// Before
Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = appColors.purpleContainer)
) { /* content */ }

// After
Box(
    modifier = Modifier
        .clip(RoundedCornerShape(20.dp))
        .background(appColors.purpleContainer)
) { /* content */ }
```

### 4. Clickable Card → Clickable Box
```kotlin
// Before
Card(
    onClick = { /* action */ },
    colors = CardDefaults.cardColors(containerColor = appColors.card)
) { /* content */ }

// After
Box(
    modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .background(appColors.card)
        .clickable { /* action */ }
) { /* content */ }
```

### 5. Card with Elevation → Box with Shadow (if needed)
```kotlin
// Before
Card(
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    colors = CardDefaults.cardColors(containerColor = appColors.card)
) { /* content */ }

// After (Material 3 Expressive - usually no shadow)
Box(
    modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .background(appColors.card)
        // Only add shadow if absolutely necessary
        // .shadow(4.dp, RoundedCornerShape(12.dp))
) { /* content */ }
```

## Required Imports

When replacing Card with Box, ensure you have:

```kotlin
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
```

Remove if no longer needed:
```kotlin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
```

## Files to Update

Found Card usage in these files:
1. ConsentScreen.kt (2 instances) ✅ Already updated by user
2. BlockedSitesBottomSheet.kt (1 instance)
3. HelpSupportBottomSheet.kt (1 instance)
4. SearchScreen.kt (1 instance)
5. FocusModeScreen.kt (3 instances)
6. BlockedLinksScreen.kt (1 instance)
7. SummaryTab.kt (1 instance)
8. TimelineTab.kt (1 instance)
9. SingleAppUsageDetailScreen.kt (2 instances)
10. LandingScreen.kt (2 instances)
11. AppBottomNavigation.kt (1 instance)
12. StatisticsScreen.kt (1 instance)

## Material 3 Expressive Guidelines

### Use Box for:
- ✅ Container backgrounds
- ✅ Colored sections
- ✅ List items
- ✅ Info panels
- ✅ Disclosure boxes

### Styling Guidelines:
- **Background**: Use `appColors.card` for cards, `appColors.componentBg` for components
- **Corners**: Use `RoundedCornerShape(12.dp)` or `16.dp` for most cases
- **Borders**: Use `appColors.border` with 1.dp width
- **Shadows**: Avoid in Material 3 Expressive (flat design)
- **Padding**: Apply inside the Box with `Modifier.padding()`

## Example: Complete Replacement

### Before (Verbose Card):
```kotlin
Card(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
        containerColor = appColors.greenContainer
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppText(text = "Title", style = AppTextStyle.SubTitle)
        AppText(text = "Description", style = AppTextStyle.Body)
    }
}
```

### After (Clean Box):
```kotlin
Box(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(appColors.greenContainer)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppText(text = "Title", style = AppTextStyle.SubTitle)
        AppText(text = "Description", style = AppTextStyle.Body)
    }
}
```

**Result**: 
- 4 fewer lines of code
- More readable
- Better control
- Material 3 Expressive compliant
