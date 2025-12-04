# Material 3 Expressive Color System - ScreenTime App

## Design Guidelines
1. ✅ Maximum 10 colors in light mode (currently using 9)
2. ✅ Non-bright, eye-friendly colors
3. ✅ Pure white (#FFFFFF) background for all screens in light mode
4. ✅ Consistent button color everywhere (Purple #6750A4)
5. ✅ Material 3 Expressive design principles

## Light Mode Color Palette (9 colors)

### Core Colors
- **Background**: `#FFFFFF` - Pure white for screen backgrounds
- **Surface**: `#FFFBFF` - Slightly warm white for surfaces
- **Card**: `#F8F9FA` - Very subtle grey for cards
- **Component Background**: `#F5F5F7` - Neutral component background

### Text Colors
- **Primary Text**: `#1C1B1F` - Soft black (not pure black)
- **Secondary Text**: `#49454F` - Medium grey
- **Muted Text**: `#79747E` - Muted grey

### Action Colors
- **Primary Button**: `#6750A4` - M3 Primary Purple (consistent everywhere)
- **Success**: `#2E7D32` - Soft green
- **Error**: `#BA1A1A` - Soft red
- **Warning**: `#E65100` - Soft orange
- **Accent**: `#4F46E5` - Indigo accent

### Special Containers (for specific use cases)
- **Purple Container**: `#EADDFF` - Light purple for highlighted items
- **Green Container**: `#E8F5E9` - Light green for success/disclosure
- **Yellow Container**: `#FFF3C7` - Light yellow for warnings
- **Blue Container**: `#E3F2FD` - Light blue for info

### Bottom Navigation
- **Background**: `#FFFBFF` - Slightly warm white
- **Selected**: `#6750A4` - Primary purple
- **Unselected**: `#79747E` - Muted grey

## Dark Mode Color Palette

### Core Colors
- **Background**: `#1C1B1F` - True dark (not pure black)
- **Surface**: `#1C1B1F` - Dark surface
- **Card**: `#2B2930` - Elevated card
- **Component Background**: `#2B2930` - Component background

### Text Colors
- **Primary Text**: `#E6E1E5` - Soft white (not pure white)
- **Secondary Text**: `#CAC4D0` - Medium grey
- **Muted Text**: `#938F99` - Muted grey

### Action Colors
- **Primary Button**: `#D0BCFF` - M3 Primary Purple (lighter for dark)
- **Success**: `#66BB6A` - Brighter green for dark
- **Error**: `#FF5449` - Brighter red for dark
- **Warning**: `#FFB74D` - Brighter orange for dark
- **Accent**: `#818CF8` - Brighter indigo for dark

### Special Containers
- **Purple Container**: `#4A4458` - Dark purple container
- **Green Container**: `#1A3A1E` - Dark green container
- **Yellow Container**: `#4A3D1A` - Dark yellow container
- **Blue Container**: `#1A2A3D` - Dark blue container

### Bottom Navigation
- **Background**: `#1C1B1F` - Dark background
- **Selected**: `#D0BCFF` - Primary purple (light)
- **Unselected**: `#938F99` - Muted grey

## Usage in Code

### Accessing Colors
```kotlin
val appColors = LocalAppColors.current ?: return

// Use colors
modifier = Modifier.background(appColors.background)
color = appColors.textPrimary
containerColor = appColors.buttonPrimary
```

### Common Patterns
```kotlin
// Screen background (always white in light mode)
.background(appColors.background)

// Card background
Card(colors = CardDefaults.cardColors(containerColor = appColors.card))

// Component background
.background(appColors.componentBg)

// Primary button (consistent everywhere)
Button(colors = ButtonDefaults.buttonColors(containerColor = appColors.buttonPrimary))

// Text colors
AppText(color = appColors.textPrimary)  // Main text
AppText(color = appColors.textSecondary)  // Secondary text
AppText(color = appColors.textMuted)  // Muted/hint text

// Status colors
color = appColors.success  // Green for success
color = appColors.error  // Red for errors
color = appColors.warning  // Orange for warnings

// Special containers
containerColor = appColors.purpleContainer  // Highlighted items
containerColor = appColors.greenContainer  // Success/disclosure sections
containerColor = appColors.yellowContainer  // Warning sections
```

## Files Updated
- ✅ Color.kt - Centralized color system
- ✅ ConsentBottomSheet.kt - Using centralized colors

## Files To Update
- ⏳ ChallengesScreen.kt (215+ hardcoded colors)
- ⏳ LeaderboardScreen.kt
- ⏳ AppBottomNavigation.kt
- ⏳ Other screen files

## Benefits
1. **Consistency**: All colors defined in one place
2. **Maintainability**: Easy to update theme colors
3. **Accessibility**: Eye-friendly, non-bright colors
4. **Material 3**: Follows M3 Expressive guidelines
5. **Dark Mode**: Automatic theme switching
6. **Minimal**: Only 9 core colors + special containers
