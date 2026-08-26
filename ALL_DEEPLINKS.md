# All Deeplinks for ScreenTime App

## 🔗 URL Schemes
- **Custom Scheme**: `apptime://screen/route`
- **HTTPS**: `https://apptime.in/route`
- **HTTPS (with www)**: `https://www.apptime.in/route`

---

## 📱 Simple Routes (No Parameters)

| Screen | Custom Scheme | HTTPS URL | Alternative Routes |
|--------|--------------|-----------|-------------------|
| **Landing/Home** | `apptime://screen/landing` | `https://apptime.in/home` | `landing`, `home` |
| **Profile** | `apptime://screen/profile` | `https://apptime.in/profile` | `profile` |
| **Statistics** | `apptime://screen/statistics` | `https://apptime.in/statistics` | `statistics` |
| **Leaderboard** | `apptime://screen/leaderboard` | `https://apptime.in/leaderboard` | `leaderboard` |
| **Challenges** | `apptime://screen/challenges` | `https://apptime.in/challenge_list` | `challenges`, `challenge_list` |
| **Rewards** | `apptime://screen/rewards` | `https://apptime.in/reward` | `rewards`, `reward` |
| **Coin History** | `apptime://screen/coin_history` | `https://apptime.in/coin_history` | `coin_history` |
| **Wallpaper** | `apptime://screen/wallpaper` | `https://apptime.in/wallpapers` | `wallpaper`, `wallpapers` |
| **Wallpaper Search** | `apptime://screen/wallpaper_search` | - | `wallpaper_search` |
| **Notifications** | `apptime://screen/notifications` | `https://apptime.in/captured_notifications` | `notifications`, `captured_notifications` |
| **Control Center** | `apptime://screen/control_center` | - | `control_center` |
| **Location Management** | `apptime://screen/manage_location` | `https://apptime.in/location` | `manage_location`, `location` |
| **File Manager** | `apptime://screen/file_manager` | `https://apptime.in/files` | `file_manager`, `files` |
| **App Lock** | `apptime://screen/app_lock` | - | `app_lock` |
| **Focus Mode** | `apptime://screen/focus_mode` | - | `focus_mode` |
| **Permission** | `apptime://screen/permission` | - | `permission` |
| **Customisation** | `apptime://screen/customisation` | - | `customisation`, `customize`, `settings_customize` |

---

## 🎯 Parametrized Routes

### 1. Challenge Detail
**Purpose**: Navigate to a specific challenge

**Path Parameter:**
- `apptime://screen/challenge_detail/{challengeId}`
- `https://apptime.in/challenge_detail/{challengeId}`

**Query Parameters:**
- `apptime://screen/challenge_detail?challengeId={value}`
- `apptime://screen/challenge_detail?id={value}`

**Example:**
```
apptime://screen/challenge_detail/challenge_123
https://apptime.in/challenge_detail/challenge_456
apptime://screen/challenge_detail?challengeId=challenge_789
apptime://screen/challenge_detail?id=challenge_abc
```

---

### 2. App Usage Detail
**Purpose**: Navigate to a specific app's usage details

**Path Parameter:**
- `apptime://screen/app_usage_detail/{packageName}`
- `apptime://screen/app_details/{packageName}`
- `https://apptime.in/app_details/{packageName}`

**Query Parameters:**
- `apptime://screen/app_usage_detail?packageName={value}`
- `apptime://screen/app_usage_detail?package={value}`

**Example:**
```
apptime://screen/app_usage_detail/com.instagram.android
https://apptime.in/app_details/com.instagram.android
apptime://screen/app_usage_detail?packageName=com.whatsapp
apptime://screen/app_usage_detail?package=com.google.android.youtube
```

**Common Package Names:**
- Instagram: `com.instagram.android`
- WhatsApp: `com.whatsapp`
- YouTube: `com.google.android.youtube`
- Facebook: `com.facebook.katana`
- Twitter: `com.twitter.android`

---

### 3. Record Detail
**Purpose**: Navigate to a specific user's record/profile

**Path Parameter:**
- `apptime://screen/record_detail/{username}`
- `https://apptime.in/record_detail/{username}`

**Query Parameters:**
- `apptime://screen/record_detail?username={value}`
- `apptime://screen/record_detail?user={value}`

**Example:**
```
apptime://screen/record_detail/john_doe
https://apptime.in/record_detail/jane_smith
apptime://screen/record_detail?username=alice
apptime://screen/record_detail?user=bob
```

---

### 4. Reward Transaction
**Purpose**: Navigate to a specific reward transaction

**Path Parameter:**
- `apptime://screen/reward_transaction/{transactionId}`
- `https://apptime.in/reward_transaction/{transactionId}`

**Query Parameters:**
- `apptime://screen/reward_transaction?transactionId={value}`
- `apptime://screen/reward_transaction?id={value}`

**Example:**
```
apptime://screen/reward_transaction/12345
https://apptime.in/reward_transaction?transactionId=67890
apptime://screen/reward_transaction?id=11111
```

---

## 🧪 Testing Commands

### Basic Screens
```bash
# Landing
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/landing" com.app.screentime

# Profile
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/profile" com.app.screentime

# Statistics
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/statistics" com.app.screentime

# Challenges
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenges" com.app.screentime

# Rewards
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/rewards" com.app.screentime

# Wallpaper
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/wallpaper" com.app.screentime
```

### Parametrized Screens
```bash
# Challenge Detail (path)
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenge_detail/challenge_123" com.app.screentime

# Challenge Detail (query)
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenge_detail?challengeId=challenge_456" com.app.screentime

# App Usage Detail (path)
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/app_usage_detail/com.instagram.android" com.app.screentime

# App Usage Detail (query)
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/app_usage_detail?packageName=com.whatsapp" com.app.screentime

# Record Detail (path)
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/record_detail/john_doe" com.app.screentime

# Reward Transaction (path)
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/reward_transaction/12345" com.app.screentime
```

---

## 🔔 Firebase Cloud Messaging Integration

### Notification Payload Format
```json
{
  "notification": {
    "title": "Notification Title",
    "body": "Notification Body"
  },
  "data": {
    "deeplink": "apptime://screen/challenge_detail/challenge_123",
    "type": "challenge",
    "image": "https://example.com/image.jpg"
  }
}
```

### Examples

**Challenge Notification:**
```json
{
  "notification": {
    "title": "New Challenge!",
    "body": "Join the 7-day screen time challenge"
  },
  "data": {
    "deeplink": "apptime://screen/challenge_detail/challenge_123"
  }
}
```

**App Usage Alert:**
```json
{
  "notification": {
    "title": "High Usage Alert",
    "body": "Instagram exceeded 2 hours today"
  },
  "data": {
    "deeplink": "apptime://screen/app_usage_detail/com.instagram.android"
  }
}
```

**Wallpaper Update:**
```json
{
  "notification": {
    "title": "New Wallpapers",
    "body": "Check out the latest collection"
  },
  "data": {
    "deeplink": "wallpaper"
  }
}
```

---

## 📋 Quick Reference

### All Routes Summary
1. `landing` / `home` → Landing Screen
2. `profile` → Profile Screen
3. `statistics` → Statistics Screen
4. `leaderboard` → Leaderboard Screen
5. `challenges` / `challenge_list` → Challenges Screen
6. `rewards` / `reward` → Rewards Screen
7. `coin_history` → Coin History Screen
8. `wallpaper` / `wallpapers` → Wallpaper Screen
9. `wallpaper_search` → Wallpaper Search Screen
10. `notifications` / `captured_notifications` → Notifications Screen
11. `control_center` → Control Center Screen
12. `manage_location` / `location` → Location Management Screen
13. `file_manager` / `files` → File Manager Screen
14. `app_lock` → App Lock Screen
15. `focus_mode` → Focus Mode Screen
16. `permission` → Permission Screen
17. `customisation` / `customize` / `settings_customize` → Customisation Screen
18. `challenge_detail/{id}` → Challenge Detail (with ID)
19. `app_usage_detail/{package}` / `app_details/{package}` → App Usage Detail (with package name)
20. `record_detail/{username}` → Record Detail (with username)
21. `reward_transaction/{id}` → Reward Transaction (with transaction ID)

---

## 🔍 Parameter Extraction Rules

The `DeeplinkParser` extracts parameters using the following priority:

1. **Path Parameter** (first): `pathSegments.getOrNull(1)`
2. **Query Parameter** (fallback): `uri.getQueryParameter("primaryKey")`
3. **Alternative Query Parameter** (fallback): `uri.getQueryParameter("alternativeKey")`

### Example Priority Chains:

**Challenge Detail:**
- `pathSegments[1]` → `challengeId` query → `id` query

**App Usage Detail:**
- `pathSegments[1]` → `packageName` query → `package` query

**Record Detail:**
- `pathSegments[1]` → `username` query → `user` query

**Reward Transaction:**
- `pathSegments[1]` → `transactionId` query → `id` query

---

## ✅ Back Stack Behavior

- **Landing**: Clears entire back stack, only Landing remains
- **Permission**: Clears entire back stack, only Permission remains
- **All Other Screens**: Clears back stack, adds Landing first, then target screen

This ensures pressing back from any deeplinked screen (except Landing/Permission) returns to Landing.

---

## 📝 Notes

- Both custom scheme (`apptime://`) and HTTPS (`https://apptime.in` or `https://www.apptime.in`) URLs are supported
- Parameters can be passed via path (`/route/param`) or query (`/route?key=value`)
- The app uses `singleTask` launch mode, so deeplinks work whether the app is running or not
- All deeplinks are handled by `DeeplinkParser.kt` and routed through `ScreenTimeNavigation.kt`
- Notifications automatically extract deeplinks from FCM payloads via `NotificationHelper`
