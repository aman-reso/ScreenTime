# Firebase Analytics Debug Guide

## How to See Firebase Analytics Events in Logcat

### Method 1: Using ADB Command (Recommended)

Enable Firebase Analytics debug mode using ADB:

```bash
# Enable debug mode
adb shell setprop debug.firebase.analytics.app com.app.screentime

# Disable debug mode (when done)
adb shell setprop debug.firebase.analytics.app .none.
```

After enabling, you'll see events in logcat with tag `FA` or `FirebaseAnalytics`.

### Method 2: Filter Logcat in Android Studio

1. Open **Logcat** in Android Studio
2. Add filter: `tag:FirebaseAnalytics` or `tag:FA`
3. Look for logs with format:
   ```
   📊 Event: event_name | Params: param1=value1, param2=value2
   📊 Screen View: ScreenName | Params: param1=value1, param2=value2
   ```

### Method 3: View in Firebase Console (Real-time)

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Navigate to **Analytics** > **Events**
4. Use **DebugView** for real-time event monitoring (requires debug mode enabled)

### Logcat Filter Examples

**Filter by tag:**
```
tag:FirebaseAnalytics
```

**Filter by package:**
```
package:com.app.screentime
```

**Filter by both:**
```
tag:FirebaseAnalytics package:com.app.screentime
```

**Search for specific event:**
```
tag:FirebaseAnalytics app_open
```

### What You'll See in Logcat

When debug mode is enabled, you'll see logs like:

```
D/FirebaseAnalytics: 📊 Event: app_open | Params: app_version=6.2, country=IN, username=john_doe
D/FirebaseAnalytics: 📊 Screen View: Home | Params: app_version=6.2, country=IN, username=john_doe
D/FirebaseAnalytics: 📊 Event: leaderboard_click | Params: app_version=6.2, country=IN, username=john_doe
```

### Notes

- Debug logging is automatically enabled in **DEBUG builds** (BuildConfig.DEBUG = true)
- Events are logged with the `📊` emoji prefix for easy identification
- All events automatically include: `app_version`, `country`, and `username` (if available)
- Firebase Analytics debug mode via ADB works for both debug and release builds

