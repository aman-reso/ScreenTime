# Navigation: India vs Apart from India

## Overview

The app uses **different bottom navigation** based on whether the user is in **India** or **not**.  
Region is determined by `CountryUtils.isUserInIndia(context)` and passed as `isUserInIndia` into `ScreenTimeNavigation` and `RegistrationsScreen`.

---

## India vs Non-India: What Changes

| Aspect | India | Apart from India |
|--------|--------|-------------------|
| **Bottom nav (mobile)** | Home, Statistics, **Challenges**, Profile | Home, Statistics, **Wallpaper**, Profile |
| **Bottom nav (tablet)** | Home, **Challenges**, Leaderboard, Profile | Home, **Leaderboard**, Wallpaper, Profile |
| **Reward FAB** | Shown (Landing, Challenges, ChallengeDetail, Profile) | Hidden |
| **Landing challenge tab** | Challenge tab + ChallengeBanner (India only) | No challenge tab content |
| **Deeplinks** | Same routes; Challenges/Reward screens available | Same routes; Challenges/Reward can be opened via deeplink but not from nav |

---

## Where It’s Implemented

### 1. **MainActivity**
- `isUserInIndia = CountryUtils.isUserInIndia(this@MainActivity)`
- Passed to `ScreenTimeNavigation(isUserInIndia = …)` and `RegistrationsScreen(isUserInIndia = …)`.

### 2. **ScreenTimeNavigationTokens.kt**
- **India – Mobile:** `mobileIndiaScreenTimeNavigationToken`  
  Routes: `Landing`, `Statistics`, `Challenges`, `Profile`.
- **India – Tablet:** `tabletIndiaScreenTimeNavigationToken`  
  Routes: `Landing`, `Challenges`, `Leaderboard`, `Profile`.
- **Non-India – Mobile:** `mobileNotIndiaScreenTimeNavigationToken`  
  Routes: `Landing`, `Statistics`, `Wallpaper`, `Profile`.
- **Non-India – Tablet:** `tabletNotIndiaScreenTimeNavigationToken`  
  Routes: `Landing`, `Leaderboard`, `Wallpaper`, `Profile`.

### 3. **ScreenTimeNavigationProps.kt**
- **India – Mobile:** `MobileNavigationIndiaItems`  
  Home, Statistics, Challenges, Profile (labels + icons).
- **India – Tablet:** `tabletNavigationIndiaItems`  
  Home, Challenges, Leaderboard, Profile.
- **Non-India – Mobile:** `defaultNavigationMobileNotIndiaItems`  
  Home, Statistics, Wallpaper, Profile.
- **Non-India – Tablet:** `tabletNavigationNotIndiaItems`  
  Home, Leaderboard, Wallpaper, Profile.

### 4. **ScreenTimeNavigation.kt**
- `navigationTokens = getNavigationTokens(isUserInIndia)`
- `navigationItems = getNavigationItems(isUserInIndia)`
- Reward FAB only when `isUserInIndia` and on allowed screens.
- Bottom bar uses `navigationTokens.bottomNavigationRoutes` (already region-specific; no extra filter needed).

### 5. **LandingScreenV2**
- Challenge tab content and ChallengeBanner only when `CountryUtils.isUserInIndia(context)`.

---

## Summary Table

| Region | Mobile bottom nav | Tablet bottom nav | Reward FAB |
|--------|-------------------|-------------------|------------|
| **India** | Home, Statistics, Challenges, Profile | Home, Challenges, Leaderboard, Profile | Yes |
| **Non-India** | Home, Statistics, Wallpaper, Profile | Home, Leaderboard, Wallpaper, Profile | No |

---

## Adding or Changing Tabs

1. **Tokens:** Edit `ScreenTimeNavigationTokens.kt`  
   - Add/remove routes in `routeToIndexMap` and `bottomNavigationRoutes` for the right token (`mobileIndia…`, `mobileNotIndia…`, `tabletIndia…`, `tabletNotIndia…`).
2. **Items (labels/icons):** Edit `ScreenTimeNavigationProps.kt`  
   - Add/remove `ODSBottomNavigationItemProps` in the corresponding list (`MobileNavigationIndiaItems`, `defaultNavigationMobileNotIndiaItems`, etc.).
3. **Order:** Keep the order of routes in tokens and the order of items in props **the same** (index 0 = first tab, etc.).

---

## Region Detection

- **CountryUtils.isUserInIndia(context)**  
  Implemented in `app/src/main/java/com/app/screentime/utils/CountryUtils.kt`.  
  Typically uses SIM country, system locale, or similar; see that file for the exact logic.
