# Backward Compatibility Color Aliases

## Complete List of Color Aliases Added

All these aliases map old color names to the new Material 3 Expressive color system:

### Grey Colors
- `greyTextBlack` → `textPrimary` (#1C1B1F light, #E6E1E5 dark)
- `greyTextMuted` → `textSecondary` (#49454F light, #CAC4D0 dark)
- `greyText` → `textMuted` (#79747E light, #938F99 dark)
- `greyTextDark` → `textSecondary`
- `greyTextMedium` → `textMuted`
- `greyTextLight` → `textLight`
- `greyHandle` → `textMuted`
- `greyTrack` → `border`
- `greySurface` → `componentBg`
- `greySurfaceLight` → `componentBgSecondary`
- `greyBackground` → `card`
- `greyBorder` → `border`

### Purple/Indigo Colors
- `primaryPurple` → `buttonPrimary` (#6750A4 light, #D0BCFF dark)
- `purple` → `buttonPrimary`
- `purpleBackground` → `purpleContainer`
- `purpleGrey` → `purpleContainerVariant`
- `lavender` → `purpleContainer`
- `indigo` → `accent` (#4F46E5 light, #818CF8 dark)
- `indigoMedium` → `accent`
- `indigoLight` → `accent`
- `indigoVeryLight` → `purpleContainerVariant`
- `indigo200` → `purpleContainer`
- `indigo300` → `purpleContainer`

### Gold/Amber/Yellow Colors
- `gold` → `warning` (#E65100 light, #FFB74D dark)
- `amber` → `warning`
- `amberText` → `textPrimary`
- `amberTextDark` → `textSecondary`
- `yellowBackground` → `yellowContainer`

### Orange Colors
- `orange` → `warning`
- `orangeLight` → `yellowContainer`

### Green Colors
- `green` → `success` (#2E7D32 light, #66BB6A dark)
- `greenMedium` → `success`
- `greenDark` → `success`
- `greenBackground` → `greenContainer`
- `greenDisclosure` → `greenContainer`

### Blue Colors
- `blue` → `accentSecondary` (#0277BD light, #64B5F6 dark)
- `blueMedium` → `accentSecondary`
- `blueLight` → `blueContainer`
- `blueBackground` → `blueContainer`

### Error Colors
- `errorRed` → `error` (#BA1A1A light, #FF5449 dark)

## How It Works

These are computed properties (using `get()`) in the `AppColors` data class:

```kotlin
val greyTextBlack: Color get() = textPrimary
```

This means:
1. ✅ No memory overhead - computed on access
2. ✅ Always uses the correct theme color
3. ✅ Old code continues to work
4. ✅ New code can use the cleaner names

## Migration Strategy

You can gradually migrate from old names to new names:

**Old (still works):**
```kotlin
color = appColors.greyTextBlack
```

**New (preferred):**
```kotlin
color = appColors.textPrimary
```

Both work identically!
