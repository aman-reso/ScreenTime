# ODS Token Package Reference

This document provides a comprehensive overview of the ODS (Open Design System) tokens available in your ScreenTime app from the `odsystem-defaultTheme-2.1.1` library.

## ODSTheme Data Class

The `ODSTheme` is the core data class that contains all design tokens. You access it via:

```kotlin
val scheme = neutralScheme
```

## Available Color Tokens

### Basic Colors
Use these for fundamental UI elements:

```kotlin
scheme.basicBackground              // Main app background (#ffffff in light mode)
scheme.basicBackgroundCard          // Card backgrounds (#f1f1f1 in light mode)
scheme.basicBackgroundCardSubtle    // Subtle card backgrounds
scheme.basicBackgroundSubtle        // Subtle backgrounds (#f1f1f1 in light mode)
scheme.basicAccent                  // Primary accent color (#e20074 magenta)
scheme.basicAccentSecondary         // Secondary accent (#000000 in light mode)
scheme.basicStroke                  // Border/stroke color
scheme.basicStrokeSubtle            // Subtle stroke (#b6b6b6 in light mode)
scheme.basicModalOverlay            // Modal overlay (66% black)
```

### Text Colors
```kotlin
scheme.basicText                    // Primary text (#000000 in light mode)
scheme.basicTextDominant            // Dominant/accent text (#e20074 magenta)
scheme.basicTextRecessive           // Recessive/muted text (#616161)
scheme.basicTextLink                // Link text (#e20074 magenta)
scheme.basicTextOnAccent            // Text on accent background (#ffffff)
scheme.basicTextOnAccentSecondary   // Text on secondary accent (#ffffff)
```

### Functional Colors
Semantic colors for specific purposes:

```kotlin
// Success
scheme.functionalSuccessStandard    // Success color (#007845 green)
scheme.functionalSuccessHovered     // Success hover state
scheme.functionalSuccessPressed     // Success pressed state
scheme.functionalSuccessSubtle      // Subtle success background (#ccf0e1)

// Error/Destructive
scheme.functionalDestructiveStandard // Error color (#c20d00 red)
scheme.functionalDestructiveHovered  // Error hover state
scheme.functionalDestructivePressed  // Error pressed state
scheme.functionalDestructiveSubtle   // Subtle error background (#ffdddb)

// Warning
scheme.functionalWarningStandard    // Warning color (#993300 orange)
scheme.functionalWarningHovered     // Warning hover state
scheme.functionalWarningPressed     // Warning pressed state
scheme.functionalWarningSubtle      // Subtle warning background (#ffffddcc)

// Informational
scheme.functionalInformationalStandard  // Info color (#2238df blue)
scheme.functionalInformationalHovered   // Info hover state
scheme.functionalInformationalPressed   // Info pressed state
scheme.functionalInformationalSubtle    // Subtle info background (#d3d7f9)

// Notification
scheme.functionalNotificationNotification       // Notification badge (#ff1000)
scheme.functionalNotificationTextOnNotification // Text on notification (#ffffff)
```

### Interaction States

#### Hover States
```kotlin
scheme.interactionStatesHoverAccentHover
scheme.interactionStatesHoverBackgroundHover
scheme.interactionStatesHoverTextHover
scheme.interactionStatesHoverTextLinkHover
scheme.interactionStatesHoverStrokeHover
```

#### Pressed States
```kotlin
scheme.interactionStatesPressedAccentPressed
scheme.interactionStatesPressedBackgroundPressed
scheme.interactionStatesPressedTextPressed
scheme.interactionStatesPressedTextLinkPressed
scheme.interactionStatesPressedStrokePressed
```

#### Disabled States
```kotlin
scheme.interactionStatesDisabledAccentDisabled
scheme.interactionStatesDisabledBackgroundDisabled
scheme.interactionStatesDisabledTextDisabled
scheme.interactionStatesDisabledStrokeDisabled
```

#### Focus States
```kotlin
scheme.interactionStatesFocusFocus
scheme.interactionStatesFocusStrokeActive
```

### Shades

#### Neutral Shades (Gray Scale)
```kotlin
scheme.shadesNeutralShades100  // Lightest gray
scheme.shadesNeutralShades200
scheme.shadesNeutralShades300
scheme.shadesNeutralShades400
scheme.shadesNeutralShades500  // Mid gray
scheme.shadesNeutralShades600
scheme.shadesNeutralShades700
scheme.shadesNeutralShades800
scheme.shadesNeutralShades900  // Darkest gray
```

#### Accent Shades
```kotlin
scheme.shadesAccentShadesAccentExtraDominant
scheme.shadesAccentShadesAccentDominant
scheme.shadesAccentShadesAccentSubtle
scheme.shadesAccentShadesAccentRecessive
scheme.shadesAccentShadesAccentExtraRecessive
```

#### Secondary Accent Shades
```kotlin
scheme.shadesSecondaryAccentShadesSecondaryAccentExtraDominant
scheme.shadesSecondaryAccentShadesSecondaryAccentDominant
scheme.shadesSecondaryAccentShadesSecondaryAccentSubtle
scheme.shadesSecondaryAccentShadesSecondaryAccentRecessive
scheme.shadesSecondaryAccentShadesSecondaryAccentExtraRecessive
```

### Elevation
```kotlin
scheme.elevationLevel0  // No elevation
scheme.elevationLevel1  // Subtle elevation
scheme.elevationLevel2
scheme.elevationLevel3
scheme.elevationLevel4
scheme.elevationLevel5
scheme.elevationLevel6  // Highest elevation

// Special elevations
scheme.elevationFabStandard
scheme.elevationFabHovered
scheme.elevationFabPressed
scheme.elevationAppBarTopFlat
scheme.elevationAppBarTopRaised
scheme.elevationAppBarBottomFlat
scheme.elevationAppBarBottomRaised
```

## ODSVariables

Design system variables for spacing, sizing, and layout:

### Spacing

#### Component Spacing
```kotlin
ODSVariables.spacingComponent0   // 0.dp
ODSVariables.spacingComponent1   // 2.dp
ODSVariables.spacingComponent2   // 4.dp
ODSVariables.spacingComponent3   // 8.dp
ODSVariables.spacingComponent4   // 12.dp
ODSVariables.spacingComponent5   // 16.dp
ODSVariables.spacingComponent6   // 20.dp
ODSVariables.spacingComponent7   // 24.dp
ODSVariables.spacingComponent8   // 32.dp
ODSVariables.spacingComponent9   // 40.dp
ODSVariables.spacingComponent10  // 48.dp
```

#### Layout Spacing
```kotlin
ODSVariables.spacingLayout0   // 0.dp
ODSVariables.spacingLayout1   // 16.dp
ODSVariables.spacingLayout2   // 24.dp
ODSVariables.spacingLayout3   // 32.dp
ODSVariables.spacingLayout4   // 40.dp
ODSVariables.spacingLayout5   // 48.dp
ODSVariables.spacingLayout6   // 56.dp
ODSVariables.spacingLayout7   // 64.dp
ODSVariables.spacingLayout8   // 80.dp
ODSVariables.spacingLayout9   // 96.dp
ODSVariables.spacingLayout10  // 128.dp
```

### Border Radius
```kotlin
ODSVariables.radiusZero            // 0.dp
ODSVariables.radiusExtraSmall      // 6.dp
ODSVariables.radiusSmall           // 8.dp
ODSVariables.radiusMedium          // 16.dp
ODSVariables.radiusLarge           // 24.dp
ODSVariables.radiusExtraLarge      // 32.dp
ODSVariables.radiusExtraExtraLarge // 48.dp
ODSVariables.radiusFull            // 999.dp (fully rounded)
```

### Component Sizing
```kotlin
ODSVariables.sizingComponent0   // 0.dp
ODSVariables.sizingComponent1   // 1.dp
ODSVariables.sizingComponent2   // 2.dp
ODSVariables.sizingComponent3   // 4.dp
ODSVariables.sizingComponent4   // 8.dp
ODSVariables.sizingComponent5   // 10.dp
ODSVariables.sizingComponent6   // 12.dp
ODSVariables.sizingComponent7   // 16.dp
ODSVariables.sizingComponent8   // 20.dp
ODSVariables.sizingComponent9   // 22.dp
ODSVariables.sizingComponent10  // 24.dp
ODSVariables.sizingComponent11  // 28.dp
ODSVariables.sizingComponent12  // 32.dp
ODSVariables.sizingComponent13  // 40.dp
ODSVariables.sizingComponent14  // 48.dp
ODSVariables.sizingComponent15  // 64.dp
ODSVariables.sizingComponent16  // 72.dp
ODSVariables.sizingComponent17  // 80.dp
ODSVariables.sizingComponent18  // 88.dp
ODSVariables.sizingComponent19  // 144.dp
ODSVariables.sizingComponent20  // 160.dp
```

### Special Sizing
```kotlin
ODSVariables.sizingMinimumTappableArea  // 48.dp (Android minimum)
ODSVariables.sizingInputHeight          // 72.dp
ODSVariables.sizingViewport             // 360.dp
```

### Strokes/Borders
```kotlin
ODSVariables.strokes1  // 1.dp
ODSVariables.strokes2  // 2.dp
ODSVariables.strokes3  // 4.dp
```

### Grid System
```kotlin
ODSVariables.gridMargins          // 24.dp
ODSVariables.gridMarginsOverflow  // 8.dp
ODSVariables.gridGutters          // 4.dp

// Column widths (1-14 columns)
ODSVariables.columns1Columns   // 49.dp
ODSVariables.columns2Columns   // 101.dp
ODSVariables.columns3Columns   // 154.dp
ODSVariables.columns4Columns   // 207.dp
ODSVariables.columns5Columns   // 259.dp
ODSVariables.columns6Columns   // 312.dp
// ... columns 7-14 are all 312.dp (max width)
```

## Available Themes

The ODS system includes multiple pre-defined themes:

### Primary Themes
- `blackScheme` - Dark theme with black background
- `magentaScheme` - Magenta accent theme
- `whiteScheme` - Light theme with white background
- `lightMode` - Standard light mode
- `darkMode` - Standard dark mode (if available)

### Secondary Color Schemes
Themed variations with different accent colors:
- `aperitifSecondaryScheme`
- `basketballSecondaryScheme`
- `cheddarSecondaryScheme`
- `dandelionSecondaryScheme`
- `eggSecondaryScheme`
- `frogSecondaryScheme`
- `guacamoleSecondaryScheme`
- `hummingbirdSecondaryScheme`
- `iguanaSecondaryScheme`
- `jacuzziSecondaryScheme`
- `kingfisherSecondaryScheme`
- `lagoonSecondaryScheme`
- `macawSecondaryScheme`
- `nebulaSecondaryScheme`
- `orchidSecondaryScheme`

## Color Mapping Reference

Based on your existing usage, here's the recommended mapping:

| Old `appColors` | New `scheme` Property |
|-----------------|----------------------|
| `appColors.background` | `scheme.basicBackground` |
| `appColors.card` | `scheme.basicBackgroundCard` |
| `appColors.surface` | `scheme.basicBackgroundCard` |
| `appColors.textPrimary` | `scheme.basicText` |
| `appColors.textSecondary` | `scheme.basicTextRecessive` |
| `appColors.textMuted` | `scheme.basicTextRecessive` |
| `appColors.textOnButton` | `scheme.basicTextOnAccent` |
| `appColors.buttonPrimary` | `scheme.basicAccent` |
| `appColors.success` | `scheme.functionalSuccessStandard` |
| `appColors.error` | `scheme.functionalDestructiveStandard` |
| `appColors.errorRed` | `scheme.functionalDestructiveStandard` |
| `appColors.border` | `scheme.basicStrokeSubtle` |
| `appColors.white` | `scheme.basicBackground` (in light themes) |
| `appColors.greenContainer` | `scheme.functionalSuccessSubtle` |
| `appColors.purpleContainer` | `scheme.basicBackgroundCardSubtle` or `scheme.basicAccent.copy(alpha = 0.2f)` |

## Usage Examples

### Using Colors
```kotlin
@Composable
fun MyComponent() {
    val scheme = neutralScheme
    
    Box(
        modifier = Modifier
            .background(scheme.basicBackground)
            .border(1.dp, scheme.basicStrokeSubtle)
    ) {
        Text(
            text = "Hello",
            color = scheme.basicText
        )
    }
}
```

### Using Spacing
```kotlin
Column(
    modifier = Modifier.padding(ODSVariables.spacingLayout2), // 24.dp
    verticalArrangement = Arrangement.spacedBy(ODSVariables.spacingComponent5) // 16.dp
) {
    // Content
}
```

### Using Border Radius
```kotlin
Box(
    modifier = Modifier
        .clip(RoundedCornerShape(ODSVariables.radiusMedium)) // 16.dp
        .background(scheme.basicBackgroundCard)
)
```

## Best Practices

1. **Always use scheme tokens** instead of hardcoded colors
2. **Use semantic functional colors** (success, error, warning) for their specific purposes
3. **Use interaction state colors** for hover, pressed, and disabled states
4. **Use ODSVariables** for consistent spacing and sizing
5. **Access scheme via neutralScheme** in Composables
6. **Pass scheme as parameter** to non-Composable functions if needed

## Converting HexColor to Compose Color

Some scheme properties return `HexColor` type. To use them in Compose:

```kotlin
// If needed, convert to Compose Color
val color = scheme.basicBackground.getColor()  // Returns androidx.compose.ui.graphics.Color
```

## Component Tokens

ODS also provides component-specific tokens in the `componenttokens` package for specialized components like:
- ODSButton
- ODSCard
- ODSBottomSheet
- ODSDialog
- ODSChip
- And many more...

These are used internally by ODS components but can be referenced if you need component-specific styling.
