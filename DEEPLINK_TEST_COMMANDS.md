# Quick Test Commands for Deep Links

## 🔗 Supported URL Schemes
- **Custom Scheme**: `apptime://screen/route`
- **HTTPS**: `https://apptime.in/route`

## 📱 Basic Screens (No Parameters)

```bash
# Landing / Home
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/landing" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/home" com.app.screentime

# Profile
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/profile" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/profile" com.app.screentime

# Statistics
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/statistics" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/statistics" com.app.screentime

# Leaderboard
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/leaderboard" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/leaderboard" com.app.screentime

# Challenges
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenges" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/challenge_list" com.app.screentime

# Rewards
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/rewards" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/reward" com.app.screentime

# Coin History
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/coin_history" com.app.screentime

# Wallpaper
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/wallpaper" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/wallpapers" com.app.screentime

# Wallpaper Search
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/wallpaper_search" com.app.screentime

# Notifications
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/notifications" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/captured_notifications" com.app.screentime

# Control Center
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/control_center" com.app.screentime

# Location Management
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/manage_location" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/location" com.app.screentime

# File Manager
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/file_manager" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/files" com.app.screentime

# App Lock
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/app_lock" com.app.screentime

# Focus Mode
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/focus_mode" com.app.screentime

# Permission
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/permission" com.app.screentime
```

## 🎯 Parametrized Screens

### Challenge Detail
```bash
# Using path parameter
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenge_detail/challenge_123" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/challenge_detail/challenge_456" com.app.screentime

# Using query parameter
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenge_detail?challengeId=challenge_789" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/challenge_detail?id=challenge_abc" com.app.screentime
```

### App Usage Detail
```bash
# Using path parameter (Instagram)
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/app_usage_detail/com.instagram.android" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/app_details/com.instagram.android" com.app.screentime

# Using query parameter (WhatsApp)
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/app_usage_detail?packageName=com.whatsapp" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/app_usage_detail?package=com.whatsapp" com.app.screentime

# YouTube
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/app_usage_detail/com.google.android.youtube" com.app.screentime
```

### Record Detail
```bash
# Using path parameter
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/record_detail/john_doe" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/record_detail/jane_smith" com.app.screentime

# Using query parameter
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/record_detail?username=alice" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/record_detail?user=bob" com.app.screentime
```

### Reward Transaction
```bash
# Using path parameter
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/reward_transaction/12345" com.app.screentime

# Using query parameter
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/reward_transaction?transactionId=67890" com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/reward_transaction?id=11111" com.app.screentime
```

## 🧪 Testing Scenarios

### Cold Start (App Not Running)
```bash
# 1. Force stop the app
adb shell am force-stop com.app.screentime

# 2. Launch with deeplink
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/statistics" com.app.screentime

# Expected: App starts and shows Statistics screen with Landing in back stack
```

### Warm Start (App Already Running - singleTask Behavior)
```bash
# 1. Launch app normally
adb shell am start -n com.app.screentime/.MainActivity

# 2. Navigate to another screen (e.g., Profile)
# (Do this manually in the app)

# 3. Trigger deeplink while app is open
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenges" com.app.screentime

# Expected: onNewIntent() is called, app navigates to Challenges screen
# (Should NOT restart the app, just navigate)
```

### Test Back Stack Behavior
```bash
# 1. Launch Statistics via deeplink
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/statistics" com.app.screentime

# 2. Press back button
# Expected: Should go to Landing screen

# 3. Press back button again
# Expected: Should exit app
```

### Test Landing Deeplink (Clear Back Stack)
```bash
# 1. Open app and navigate to several screens
# 2. Trigger landing deeplink
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/landing" com.app.screentime

# Expected: Back stack is cleared, only Landing screen present
# Pressing back should exit app
```

### Test Parametrized Routes
```bash
# 1. Launch challenge detail
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenge_detail/test_challenge_123" com.app.screentime

# 2. Verify challenge ID is passed correctly
# Expected: Challenge detail screen shows challenge_123

# 3. Test with query parameter
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/app_usage_detail?packageName=com.instagram.android" com.app.screentime

# Expected: Shows Instagram usage details
```

## 📋 Testing Checklist

### Basic Functionality
- [ ] Test cold start (app not running)
- [ ] Test warm start (app already running in background)
- [ ] Test with app in foreground (singleTask behavior)
- [ ] Test both custom scheme (`apptime://`) and HTTPS URLs
- [ ] Test all basic screens without parameters
- [ ] Test all parametrized screens with path parameters
- [ ] Test all parametrized screens with query parameters

### Back Stack Behavior
- [ ] Landing deeplink clears back stack
- [ ] Non-landing deeplinks add Landing to back stack
- [ ] Back button from deeplinked screen goes to Landing
- [ ] Back button from Landing exits app
- [ ] Permission screen doesn't add Landing to back stack

### Parameter Passing
- [ ] Challenge ID passed correctly (path and query)
- [ ] Package name passed correctly (path and query)
- [ ] Username passed correctly (path and query)
- [ ] Transaction ID passed correctly (path and query)
- [ ] Multiple query parameters work together

### Edge Cases
- [ ] Invalid route shows error or ignores
- [ ] Missing required parameters handled gracefully
- [ ] Malformed URLs handled gracefully
- [ ] Deep linking before permissions granted
- [ ] Rapid sequential deeplinks

## 🐛 Debugging Tips

### Check Intent Data
```bash
# View logcat for deeplink parsing
adb logcat | grep "DeeplinkParser"
```

### Check Activity Launch Mode
```bash
# Verify singleTask behavior
adb logcat | grep "MainActivity"
```

### Force Clean Start
```bash
# Clear app data and cache
adb shell pm clear com.app.screentime

# Then test deeplink
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/statistics" com.app.screentime
```

## 📱 Example Notification Deeplinks

For use in Firebase Cloud Messaging payloads:

```json
{
  "notification": {
    "title": "New Challenge!",
    "body": "Check out this challenge"
  },
  "data": {
    "deeplink": "apptime://screen/challenge_detail/new_challenge_123"
  }
}
```

```json
{
  "notification": {
    "title": "App Usage Alert",
    "body": "Instagram usage is high today"
  },
  "data": {
    "deeplink": "https://apptime.in/app_usage_detail/com.instagram.android"
  }
}
```

## 🔔 Testing Notifications with Deeplinks

### Test FCM Notification via REST API

```bash
# Replace YOUR_SERVER_KEY and USER_FCM_TOKEN
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=YOUR_SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "USER_FCM_TOKEN",
    "notification": {
      "title": "Test Challenge",
      "body": "Join the new challenge"
    },
    "data": {
      "deeplink": "apptime://screen/challenge_detail/test_123"
    }
  }'
```

### Test Notification Click Scenarios

```bash
# Scenario 1: App not running
adb shell am force-stop com.app.screentime
# Send notification and click it
# Expected: App launches and navigates to deeplink target

# Scenario 2: App in background
# 1. Open app: adb shell am start -n com.app.screentime/.MainActivity
# 2. Press home button
# 3. Send notification and click it
# Expected: App comes to foreground and navigates

# Scenario 3: App in foreground
# 1. Open app and keep it active
# 2. Send notification and click it
# Expected: App navigates to deeplink target (singleTask behavior)
```

### More FCM Examples

#### Usage Alert with Image
```json
{
  "to": "USER_FCM_TOKEN",
  "notification": {
    "title": "High Usage Alert",
    "body": "Instagram exceeded 2 hours today"
  },
  "data": {
    "deeplink": "app_usage_detail/com.instagram.android",
    "type": "usage_alert",
    "image": "https://example.com/alert.jpg"
  }
}
```

#### Data-Only Message
```json
{
  "to": "USER_FCM_TOKEN",
  "data": {
    "title": "New Wallpapers",
    "body": "Check out the latest collection",
    "deeplink": "wallpaper",
    "type": "content_update"
  }
}
```
