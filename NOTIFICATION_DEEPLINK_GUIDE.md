# Notification Deeplink Integration Guide

## 📋 Overview

This guide explains how to create notifications that navigate to specific screens when clicked. The system supports multiple deeplink formats and works seamlessly with both local notifications and Firebase Cloud Messaging (FCM).

## 🔗 How It Works

### Flow Diagram

```
Notification Clicked
        ↓
PendingIntent Triggered
        ↓
MainActivity Launched/Brought to Foreground
        ↓
extractDeeplinkUri() processes intent
        ↓
deeplinkUriState updated
        ↓
Compose recomposes
        ↓
Navigation triggered
        ↓
Target screen displayed
```

## 🎯 Deeplink Formats Supported

### 1. Full URI Format (Recommended)
```kotlin
val deeplink = "apptime://screen/statistics"
val deeplink = "https://apptime.in/challenge_detail/challenge_123"
```

### 2. Route Format (Auto-converted)
```kotlin
val deeplink = "statistics"           // → apptime://screen/statistics
val deeplink = "challenge_detail/123" // → apptime://screen/challenge_detail/123
```

### 3. Via Data Payload
```kotlin
val data = mapOf("deeplink" to "apptime://screen/wallpaper")
```

## 💻 Implementation

### Local Notifications

#### Basic Notification with Deeplink

```kotlin
import com.app.screentime.messaging.NotificationHelper
import com.app.screentime.messaging.NotificationType

// Simple screen navigation
NotificationHelper.showNotification(
    context = context,
    notificationId = 1001,
    title = "Daily Report Ready",
    message = "Check your screen time statistics",
    type = NotificationType.DEFAULT,
    deeplink = "apptime://screen/statistics"
)

// Navigation with image
NotificationHelper.showNotification(
    context = context,
    notificationId = 1002,
    title = "New Challenge Available!",
    message = "Join the 7-day detox challenge",
    type = NotificationType.CHALLENGE,
    imageUrl = "https://example.com/challenge.jpg",
    deeplink = "apptime://screen/challenge_detail/challenge_123"
)
```

#### Using Route Format

```kotlin
// Simple route (no parameters)
NotificationHelper.showNotification(
    context = context,
    notificationId = 1003,
    title = "New Wallpapers",
    message = "Check out our latest collection",
    deeplink = "wallpaper" // Auto-converted to apptime://screen/wallpaper
)

// Route with parameters
NotificationHelper.showNotification(
    context = context,
    notificationId = 1004,
    title = "High Usage Alert",
    message = "Instagram usage exceeded 2 hours",
    deeplink = "app_usage_detail/com.instagram.android"
    // Auto-converted to apptime://screen/app_usage_detail/com.instagram.android
)
```

#### Using Data Map

```kotlin
val notificationData = mapOf(
    "deeplink" to "apptime://screen/control_center",
    "userId" to "user_123",
    "type" to "location_request"
)

NotificationHelper.showNotification(
    context = context,
    notificationId = 1005,
    title = "Location Sharing Request",
    message = "John wants to see your location",
    data = notificationData
)
```

### Firebase Cloud Messaging (FCM)

#### Notification-Only Message (Display Message)

```json
{
  "to": "USER_FCM_TOKEN",
  "notification": {
    "title": "Daily Report Ready",
    "body": "Your screen time is 4h 23m today"
  },
  "data": {
    "deeplink": "apptime://screen/statistics",
    "type": "daily_report"
  }
}
```

#### Data-Only Message (Background Processing)

```json
{
  "to": "USER_FCM_TOKEN",
  "data": {
    "title": "New Challenge Invitation",
    "body": "Your friend invited you to a challenge",
    "deeplink": "apptime://screen/challenge_detail/challenge_abc123",
    "type": "challenge",
    "image": "https://example.com/challenge.jpg",
    "subtitle": "7-Day Screen Time Challenge"
  }
}
```

#### Topic Message (Broadcast)

```json
{
  "to": "/topics/all_users",
  "notification": {
    "title": "New Feature Available!",
    "body": "Check out our new wallpaper collection"
  },
  "data": {
    "deeplink": "https://apptime.in/wallpaper",
    "type": "feature_announcement",
    "image_url": "https://example.com/wallpapers.jpg"
  }
}
```

### Advanced FCM Examples

#### Challenge Detail with Image

```json
{
  "to": "USER_FCM_TOKEN",
  "notification": {
    "title": "Challenge Update",
    "body": "You're now #2 on the leaderboard!"
  },
  "data": {
    "deeplink": "apptime://screen/challenge_detail/challenge_xyz",
    "type": "challenge",
    "image": "https://api.apptime.in/images/leaderboard.jpg",
    "subtitle": "Daily Challenge • 25 participants",
    "challengeId": "challenge_xyz"
  }
}
```

#### App Usage Alert

```json
{
  "to": "USER_FCM_TOKEN",
  "notification": {
    "title": "Usage Alert",
    "body": "Instagram usage exceeded your daily limit"
  },
  "data": {
    "deeplink": "app_usage_detail/com.instagram.android",
    "type": "usage_alert",
    "packageName": "com.instagram.android",
    "usageTime": "7200000"
  }
}
```

#### Reward Transaction

```json
{
  "to": "USER_FCM_TOKEN",
  "notification": {
    "title": "Reward Claimed!",
    "body": "You've earned 50 coins"
  },
  "data": {
    "deeplink": "reward_transaction/12345",
    "type": "reward",
    "transactionId": "12345",
    "amount": "50"
  }
}
```

## 📱 Notification Types

```kotlin
enum class NotificationType {
    DEFAULT,    // General notifications (IMPORTANCE_DEFAULT)
    CHALLENGE,  // Challenge-related (IMPORTANCE_HIGH)
    ALERT       // Critical alerts (IMPORTANCE_HIGH)
}
```

### Usage

```kotlin
// Default notification
NotificationHelper.showNotification(
    type = NotificationType.DEFAULT,
    // ... other params
)

// Challenge notification (high priority, custom vibration)
NotificationHelper.showNotification(
    type = NotificationType.CHALLENGE,
    // ... other params
)

// Alert notification (high priority, strong vibration)
NotificationHelper.showNotification(
    type = NotificationType.ALERT,
    // ... other params
)
```

## 🧪 Testing Notifications

### Test Local Notification

```kotlin
// In your activity or fragment
Button(onClick = {
    NotificationHelper.showNotification(
        context = context,
        notificationId = System.currentTimeMillis().toInt(),
        title = "Test Notification",
        message = "Testing deeplink navigation",
        deeplink = "apptime://screen/challenges"
    )
}) {
    Text("Show Test Notification")
}
```

### Test FCM via ADB

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Send test message
firebase messaging:test-notification \
  --token "USER_FCM_TOKEN" \
  --title "Test Message" \
  --body "Testing deeplink" \
  --data '{"deeplink":"apptime://screen/statistics"}'
```

### Test FCM via REST API

```bash
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=YOUR_SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "USER_FCM_TOKEN",
    "notification": {
      "title": "Test Notification",
      "body": "Click to open Statistics"
    },
    "data": {
      "deeplink": "apptime://screen/statistics"
    }
  }'
```

### Test via Postman

1. Create new POST request to `https://fcm.googleapis.com/fcm/send`
2. Add headers:
   - `Authorization: key=YOUR_SERVER_KEY`
   - `Content-Type: application/json`
3. Body (raw JSON):
```json
{
  "to": "USER_FCM_TOKEN",
  "notification": {
    "title": "Test",
    "body": "Test message"
  },
  "data": {
    "deeplink": "apptime://screen/challenges"
  }
}
```

## 🎯 All Supported Deeplink Routes

### No Parameters

| Deeplink | Target Screen |
|----------|--------------|
| `apptime://screen/landing` | Landing |
| `apptime://screen/statistics` | Statistics |
| `apptime://screen/challenges` | Challenges |
| `apptime://screen/leaderboard` | Leaderboard |
| `apptime://screen/rewards` | Rewards |
| `apptime://screen/wallpaper` | Wallpaper |
| `apptime://screen/notifications` | Notifications |
| `apptime://screen/control_center` | Control Center |
| `apptime://screen/file_manager` | File Manager |
| `apptime://screen/app_lock` | App Lock |

### With Parameters

| Deeplink | Target Screen | Parameter |
|----------|--------------|-----------|
| `apptime://screen/challenge_detail/{id}` | Challenge Detail | Challenge ID |
| `apptime://screen/app_usage_detail/{package}` | App Usage | Package Name |
| `apptime://screen/record_detail/{username}` | Record Detail | Username |
| `apptime://screen/reward_transaction/{id}` | Transaction | Transaction ID |

## 🔍 Debugging

### Check if Notification Created Intent Correctly

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Log intent data
    Log.d("MainActivity", "Intent data: ${intent.data}")
    Log.d("MainActivity", "Intent deeplink extra: ${intent.getStringExtra("deeplink")}")
    Log.d("MainActivity", "All extras: ${intent.extras?.keySet()}")
    
    // ... rest of code
}
```

### Enable FCM Debug Logging

```bash
# Enable debug logging
adb shell setprop log.tag.FCM DEBUG
adb shell setprop log.tag.FA VERBOSE
adb shell setprop log.tag.FA-SVC VERBOSE

# View logs
adb logcat -v time | grep -E "FCM|FirebaseMessaging"
```

### Check Deeplink Parsing

```bash
# View deeplink parser logs
adb logcat | grep "DeeplinkParser"
```

## 💡 Best Practices

### 1. Use Full URI Format for External Sources
```kotlin
// ✅ Good - explicit and portable
"apptime://screen/statistics"

// ❌ Avoid - requires context conversion
"statistics"
```

### 2. Always Include Notification Type
```kotlin
// ✅ Good - proper priority and channel
NotificationHelper.showNotification(
    type = NotificationType.CHALLENGE,
    // ...
)

// ❌ Avoid - uses default everywhere
NotificationHelper.showNotification(
    // ...
)
```

### 3. Add Subtitle for Context
```kotlin
// ✅ Good - provides context
NotificationHelper.showNotification(
    title = "Challenge Update",
    message = "You're now #2!",
    subtitle = "7-Day Challenge • 25 participants",
    // ...
)
```

### 4. Include Image URLs
```kotlin
// ✅ Good - visual engagement
NotificationHelper.showNotification(
    imageUrl = "https://api.apptime.in/challenges/123/image.jpg",
    // ...
)
```

### 5. Validate Deeplinks
```kotlin
// ✅ Good - check before sending
val isValid = deeplink.startsWith("apptime://") || 
              deeplink.startsWith("https://apptime.in/")
if (isValid) {
    NotificationHelper.showNotification(deeplink = deeplink, ...)
}
```

## 🚨 Common Issues

### Issue: Notification Shows But Doesn't Navigate

**Problem**: Deeplink string is malformed or not set

**Solution**: 
```kotlin
// ✅ Ensure deeplink is set
NotificationHelper.showNotification(
    deeplink = "apptime://screen/statistics", // Don't forget this!
    // ...
)
```

### Issue: App Crashes on Notification Click

**Problem**: Invalid route or missing parameters

**Solution**:
```kotlin
// ✅ Use valid routes from DeeplinkParser
val validRoute = "challenge_detail/valid_id" // Not just "challenge_detail"
```

### Issue: FCM Data Not Received

**Problem**: Background vs Foreground message handling

**Solution**:
- **Foreground**: Use `onMessageReceived()` in FCM service
- **Background**: Data must be in `"data"` payload, not just `"notification"`

```json
{
  "notification": { ... },
  "data": {
    "deeplink": "..." // ✅ Include here for background handling
  }
}
```

## 📚 Related Files

- `MainActivity.kt` - Processes deeplinks from notifications
- `NotificationHelper.kt` - Creates notifications with deeplinks
- `ScreenTimeFirebaseMessagingService.kt` - Handles FCM messages
- `DeeplinkParser.kt` - Parses deeplink URIs

## ✅ Testing Checklist

- [ ] Local notification with simple route
- [ ] Local notification with full URI
- [ ] Local notification with parameters
- [ ] Local notification with image
- [ ] FCM notification (app in foreground)
- [ ] FCM notification (app in background)
- [ ] FCM notification (app not running)
- [ ] FCM with data-only payload
- [ ] All supported routes work
- [ ] Parameters passed correctly
- [ ] Back stack correct after navigation
- [ ] Works with singleTask activity

## 🎉 Summary

✅ Notifications support deeplinks in multiple formats
✅ Works with both local notifications and FCM
✅ Automatically handles app states (foreground/background/not running)
✅ singleTask activity properly triggers navigation
✅ All app screens accessible via notifications
✅ Comprehensive testing and debugging support
