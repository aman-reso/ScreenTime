# Hardcoded Colors Report

This report lists all hardcoded color values found in the codebase that should potentially be moved to the theme system.

## Files with Hardcoded Colors

### 1. **ChallengeListScreen.kt** (Many hardcoded colors)
- `Color(0xFFEAE7EC)` - Light off-white background (lines 500, 615)
- `Color(0xFFFFD700)` - Gold/Amber (lines 483, 786, 1670, 1840)
- `Color.White` - Multiple uses
- `Color(0xFF424242)`, `Color(0xFF616161)` - Grey gradients (lines 717-718)
- `Color(0xFF2196F3)`, `Color(0xFF9C27B0)` - Icon colors (line 851)
- `Color(0xFFE3F2FD)`, `Color(0xFFF3E5F5)` - Icon backgrounds (line 852)
- `Color(0xFF4A90E2)`, `Color(0xFF50C878)` - Chart colors (lines 1265-1266)
- `Color(0xFFF5F5F5)` - Light grey background (line 1291)
- `Color(0xFFE8F5E9)` - Light green background (line 1358)
- `Color(0xFF4CAF50)` - Green (line 1368)
- `Color(0xFF2E7D32)` - Dark green (line 1375)
- `Color(0xFF757575)` - Grey (multiple lines)
- `Color(0xFFE0E0E0)` - Border color (line 1497)
- `Color(0xFFFF6B35)` - Orange (line 1841)

### 2. **ChallengesScreen.kt** (Extensive hardcoded colors)
- `Color(0xFF111315)` - Dark background (line 255)
- `Color(0xFFFDFCFF)` - Light background (line 255)
- `Color(0xFFFFF3C7)` - Light orange background (line 294)
- `Color(0xFFF59E0B)` - Gold (lines 304, 1557, 1559)
- `Color(0xFF92400E)` - Dark brown (lines 311, 1558)
- `Color(0xFFF3F4F6)` - Light grey (multiple lines)
- `Color(0xFF374151)` - Dark grey (multiple lines)
- `Color(0xFF4F46E5)` - Indigo/Purple (multiple lines)
- `Color(0xFFFFD700)` - Gold (line 412)
- `Color(0xFFA5B4FC)`, `Color(0xFF4338CA)` - Indigo gradients (lines 515-516)
- `Color(0xFFE0E7FF)` - Light Indigo (line 523)
- `Color(0xFF1F2937)` - Dark grey/black (multiple lines)
- `Color(0xFFFFC1C1)`, `Color(0xFFB3E5FC)`, `Color(0xFFFFF59D)` - Avatar colors (lines 1074-1076, 1675-1677)
- `Color(0xFF1E2124)` - Dark background (lines 1511, 1660)
- `Color(0xFFF0F4F9)` - Light background (lines 1511, 1660)
- `Color(0xFFFEF3C7)`, `Color(0xFFFDE68A)` - Yellow gradients (line 1555)
- `Color(0xFFE5E7EB)` - Grey (line 1568)
- `Color(0xFF9CA3AF)`, `Color(0xFF6B7280)` - Grey shades (lines 1569-1570, 1594-1595)
- `Color(0xFFFED7AA)`, `Color(0xFFFDBA74)` - Orange gradients (line 1579)
- `Color(0xFFEA580C)`, `Color(0xFF7C2D12)` - Orange shades (lines 1581-1582)
- `Color(0xFFD97706)` - Amber/Gold (line 1767)
- `Color(0xFFE5E7EB)` - Light grey (line 1774)

### 3. **ConsentBottomSheet.kt**
- `Color(0xFF49454F)` - Text color for Deny button (line 199)
- `Color(0xFF6750A4)` - Primary purple (line 222)

### 4. **SegmentedControl.kt**
- `Color(0xFFE0E7FF)` - Soft Indigo background (line 50)
- `Color(0xFF4338CA)` - Medium Indigo (lines 51, 53, 56, 516)
- `Color.White` - Selected text (lines 52, 58)
- `Color(0xFF6366F1)` - Lighter Indigo (line 57)
- `Color(0xFFC7D2FE)` - Light Indigo (line 59)

### 5. **LeaderboardScreen.kt**
- `Color(0xFFFFC1C1)` - Light pink avatar (line 373)
- `Color(0xFFB3E5FC)` - Light blue avatar (line 374)
- `Color(0xFFFFF59D)` - Light yellow avatar (line 375)
- `Color(0xFFC5E1A5)` - Light green avatar (line 376)
- `Color(0xFFFFCCBC)` - Light orange avatar (line 377)
- `Color(0xFFE1BEE7)` - Light purple avatar (line 378)
- `Color(0xFFB2DFDB)` - Light teal avatar (line 379)
- `Color(0xFFFFE0B2)` - Light amber avatar (line 380)

### 6. **AppButton.kt**
- `Color(0xFF6750A4)` - Solid purple background (line 94, comment line 73)

### 7. **MainActivity.kt**
- `android.graphics.Color.TRANSPARENT` - System color (lines 90-91)

### 8. **RecordDetailPreview.kt**
- `Color.White` - Fallback color (line 97)

### 9. **Color.kt** (Theme file - These are OK)
- All colors in this file are part of the theme system and should remain as is.

## Recommendations

1. **Move common colors to theme system**: Colors like `Color(0xFF6750A4)` (purple), `Color(0xFFFFD700)` (gold), and common greys should be added to `AppColors` in `Color.kt`.

2. **Use theme colors where possible**: Replace hardcoded colors with theme colors from `LocalAppColors.current` when appropriate.

3. **Create color constants**: For colors used in multiple places (like avatar colors), consider creating a constants file or adding them to the theme.

4. **Prioritize critical files**: Start with `ChallengeListScreen.kt` and `ChallengesScreen.kt` as they have the most hardcoded colors.

## Color Usage Summary

- **Total hardcoded colors found**: 100+ instances
- **Most affected files**: 
  - `ChallengeListScreen.kt` (~50 instances)
  - `ChallengesScreen.kt` (~40 instances)
  - `LeaderboardScreen.kt` (~10 instances)
  - `ConsentBottomSheet.kt` (~2 instances)
  - `SegmentedControl.kt` (~5 instances)

