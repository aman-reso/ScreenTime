# Deep Links for ScreenTime App

Here are the deep links configured for your application based on your `AndroidManifest.xml` and `Screen.kt` routes. You can use either the custom scheme `apptime://screen` or the web URL `https://apptime.in`.

## Base Configuration
- **Custom Scheme**: `apptime://screen`
- **Web URL**: `https://apptime.in`

## Available Deep Links

### 1. Main Screens
| Screen | Custom Scheme | Web URL |
|--------|---------------|---------|
| **Dashboard/Landing** | `apptime://screen/landing` | `https://apptime.in/landing` |
| **Profile** | `apptime://screen/profile` | `https://apptime.in/profile` |
| **Search** | `apptime://screen/search` | `https://apptime.in/search` |
| **Statistics** | `apptime://screen/statistics` | `https://apptime.in/statistics` |
| **Leaderboard** | `apptime://screen/leaderboard` | `https://apptime.in/leaderboard` |
| **Challenges** | `apptime://screen/challenges` | `https://apptime.in/challenges` |

### 2. Feature Screens
| Screen | Custom Scheme | Web URL |
|--------|---------------|---------|
| **Focus Mode** | `apptime://screen/focus_mode` | `https://apptime.in/focus_mode` |
| **App Blocking** | `apptime://screen/app_blocking` | `https://apptime.in/app_blocking` |
| **Blocked Links** | `apptime://screen/blocked_links` | `https://apptime.in/blocked_links` |
| **Permissions** | `apptime://screen/permission` | `https://apptime.in/permission` |

### 3. Detail Screens (Dynamic Parameters)
Replace `{username}`, `{packageName}`, or `{challengeId}` with actual values.

| Screen | Custom Scheme | Web URL | Example |
|--------|---------------|---------|---------|
| **Record Detail** | `apptime://screen/record_detail/{username}` | `https://apptime.in/record_detail/{username}` | `apptime://screen/record_detail/john_doe` |
| **App Details** | `apptime://screen/app_details/{packageName}` | `https://apptime.in/app_details/{packageName}` | `apptime://screen/app_details/com.instagram.android` |
| **Single App Usage** | `apptime://screen/app_usage_detail/{packageName}` | `https://apptime.in/app_usage_detail/{packageName}` | `apptime://screen/app_usage_detail/com.youtube.android` |
| **Challenge Detail** | `apptime://screen/challenge_detail/{challengeId}` | `https://apptime.in/challenge_detail/{challengeId}` | `apptime://screen/challenge_detail/123` |

## Testing Deep Links via ADB

You can test these links using the Android Debug Bridge (adb) command:

```bash
# Test Landing Screen
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/landing" com.app.screentime

# Test Profile Screen
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/profile" com.app.screentime

# Test App Details (Example: YouTube)
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/app_details/com.google.android.youtube" com.app.screentime
```
