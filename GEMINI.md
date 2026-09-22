# ScreenTime / Zona Project Development Rules

These rules are strictly enforced across the entire codebase. Every agent and contributor must adhere to them.

## 1. Always Use ODS Components & ODSLazyColumn
- 100% of UI elements must use Telekom ODS components (`com.telekom.odsystem.*`):
  - `ODSBox`, `ODSColumn`, `ODSRow`, `ODSText`, `ODSIcon`, `ODSImage`, `ODSButton`, etc.
  - Never use standard Compose `Box`, `Column`, `Row`, `Text`, `Icon`, `Button`, or `LazyColumn`.
  - **Always use `ODSLazyColumn`** for all vertical lists and scrollable screens. Never use `Modifier.verticalScroll()` or standard Compose `LazyColumn`.

## 2. Use `scheme` for Color Picking, Not Directly
- Always pick colors from the composable's `scheme: ODSTheme` parameter, never hardcode hex colors or reference tokens directly in UI when `scheme` is passed:
  - `scheme.basicBackground` / `scheme.basicBackgroundSubtle` (Background)
  - `scheme.basicBackgroundCard` / `scheme.basicBackgroundCardSubtle` (Surfaces / Cards)
  - `scheme.basicStroke` / `scheme.basicStrokeSubtle` (Borders & Dividers)
  - `scheme.basicText` / `scheme.basicTextDominant` (Primary text & icons)
  - `scheme.basicTextRecessive` (Secondary text & inactive icons)
  - `scheme.basicAccent` (Primary action / Selected states / Magenta #D81B60)
  - `scheme.basicAccentSecondary` (Soft action / Soft pink #FCE4EC / #4A1830)
  - `scheme.functionalDestructiveStandard` (Destructive / Danger)
- For `ODSColorModel`, wrap with `ODSColorModel(hexColor = scheme.xxx)`.
- For `tint: Color?` on `ODSIcon`, use `scheme.xxx.getColor()`.

## 3. Button Variants: Always Default to Small
- When using `ODSButton`, always use `ODSButtonSize.SMALL` (`props = ODSButtonProps(size = ODSButtonSize.SMALL, ...)`) unless explicitly instructed otherwise by the user.

## 4. Maximum Text Size is 16sp with Required Font (Funnel Sans)
- **Maximum text size anywhere in the app is 16sp**.
- Never use text styles larger than 16sp (`titleL`, `titleM`, `titleS`, `subtitle`, `bodyL`, `display`, etc. are forbidden).
- Use `ODSTextStyles` backed by the required font family (`R.font.funnelsans_*`):
  - `ODSTextStyles.bodyMBold` (16sp, `funnelsans_semibold`) — for screen titles, headings, primary card titles
  - `ODSTextStyles.bodyMRegular` (16sp, `funnelsans_regular`) — for body text, bio
  - `ODSTextStyles.bodySBold` (14sp, `funnelsans_semibold`) — for section labels, tags, subheadings
  - `ODSTextStyles.bodySRegular` (14sp, `funnelsans_regular`) — for secondary details, locations
  - `ODSTextStyles.microcopyBold` (12sp, `funnelsans_semibold`) — for small caps headers, badges
  - `ODSTextStyles.microcopyRegular` (12sp, `funnelsans_regular`) — for captions

## 5. Always Use DSVariable (`ODSVariables`) for Padding & Margins
- Never use raw hardcoded dp (e.g. `16.dp`, `12.dp`, `8.dp`, `20.dp`) for padding, margins, spacers, gaps, and corner radii.
- Always use `ODSVariables`:
  - `ODSVariables.spacingComponent0` (0.dp)
  - `ODSVariables.spacingComponent1` (2.dp)
  - `ODSVariables.spacingComponent2` (4.dp)
  - `ODSVariables.spacingComponent3` (8.dp)
  - `ODSVariables.spacingComponent4` (12.dp)
  - `ODSVariables.spacingComponent5` (16.dp)
  - `ODSVariables.spacingComponent6` (20.dp)
  - `ODSVariables.spacingComponent7` (24.dp)
  - `ODSVariables.spacingComponent8` (32.dp)
  - `ODSVariables.spacingComponent9` (40.dp)
  - `ODSVariables.spacingComponent10` (48.dp)
  - `ODSVariables.spacingLayout1` (16.dp)
  - `ODSVariables.spacingLayout2` (24.dp)
  - `ODSVariables.spacingLayout3` (32.dp)
  - `ODSVariables.radiusSmall` (8.dp)
  - `ODSVariables.radiusMedium` (16.dp)
  - `ODSVariables.radiusLarge` (24.dp)
  - `ODSVariables.radiusExtraLarge` (32.dp)
  - `ODSVariables.radiusFull` (999.dp)
