# Deeplink Implementation Guide

## 📋 Overview

The ScreenTime app supports deep linking through both custom URL schemes and HTTPS URLs. Deeplinks allow external sources (notifications, emails, web pages, other apps) to navigate directly to specific screens within the app.

## 🔗 Supported URL Schemes

1. **Custom Scheme**: `apptime://screen/route`
2. **HTTPS**: `https://apptime.in/route`

## 🏗️ Architecture

### Components

1. **DeeplinkParser** (`navigation/DeeplinkParser.kt`)
   - Parses URI and converts to Screen objects
   - Handles both path and query parameters
   - Determines back stack behavior

2. **MainActivity** 
   - Configured with `launchMode="singleTask"` in AndroidManifest
   - Handles deeplinks in both `onCreate()` and `onNewIntent()`
   - Uses MutableState to trigger recomposition on new intents

3. **ScreenTimeNavigation**
   - Observes deeplink URI changes
   - Manages navigation back stack
   - Applies correct back stack behavior

## 🎯 How It Works

### Flow Diagram

```
External Source → Intent → MainActivity → DeeplinkParser → Screen Object → Navigation
                    ↓
                [onCreate or onNewIntent]
                    ↓
            [Update deeplinkUriState]
                    ↓
            [Trigger recomposition]
                    ↓
        [ScreenTimeNavigation observes]
                    ↓
            [Navigate to screen]
```

### Cold Start (App Not Running)
1. User clicks deeplink
2. System creates new Intent with `data` field
3. `MainActivity.onCreate()` is called
4. `deeplinkUriState.value = intent?.data`
5. `ScreenTimeNavigation` observes change in `LaunchedEffect`
6. `DeeplinkParser.parseDeeplink()` converts URI to Screen
7. Back stack is configured based on screen type
8. Navigation occurs

### Warm Start (App in Background/Foreground)
1. User clicks deeplink
2. System brings existing activity to foreground
3. `MainActivity.onNewIntent()` is called (due to `singleTask`)
4. `deeplinkUriState.value = intent.data` triggers recomposition
5. Rest of flow same as cold start

## 📱 Implementation Details

### 1. AndroidManifest Configuration

```xml
<activity
    android:name=".MainActivity"
    android:exported="true"
    android:launchMode="singleTask">
    
    <!-- Custom Scheme -->
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:host="screen"
            android:scheme="apptime" />
    </intent-filter>

    <!-- HTTPS App Links -->
    <intent-filter android:autoVerify="true">
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:host="apptime.in"
            android:scheme="https" />
    </intent-filter>
</activity>
```

**Key Points:**
- `launchMode="singleTask"`: Ensures single instance, triggers `onNewIntent()`
- `autoVerify="true"`: Enables App Links verification for HTTPS

### 2. MainActivity Deeplink Handling

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // MutableState to track deeplink URI changes
    private val deeplinkUriState = mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set initial deeplink if present
        deeplinkUriState.value = intent?.data
        
        setContent {
            val deeplinkUri by deeplinkUriState
            
            ScreenTimeNavigation(
                scheme = scheme,
                deeplinkUri = deeplinkUri,
                isUserInIndia = CountryUtils.isUserInIndia(this@MainActivity)
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        
        // Update state to trigger recomposition
        // Critical for singleTask behavior
        deeplinkUriState.value = intent.data
    }
}
```

**Why MutableState?**
- Compose observes state changes
- When `deeplinkUriState.value` changes, recomposition triggers
- Works for both `onCreate()` and `onNewIntent()`
- No need for manual callbacks or event buses

### 3. DeeplinkParser Implementation

```kotlin
object DeeplinkParser {
    fun parseDeeplink(uri: Uri?): Screen? {
        if (uri == null) return null
        
        val pathSegments = uri.pathSegments
        if (pathSegments.isEmpty()) return null
        
        val route = pathSegments.first()
        
        return when (route) {
            "landing", "home" -> Screen.Landing
            "statistics" -> Screen.Statistics
            
            // With path parameter
            "challenge_detail" -> {
                val challengeId = pathSegments.getOrNull(1)
                    ?: uri.getQueryParameter("challengeId")
                    ?: uri.getQueryParameter("id")
                if (challengeId != null) {
                    Screen.ChallengeDetail(ChallengeDetailParams(challengeId))
                } else null
            }
            
            // ... more routes
            else -> null
        }
    }
    
    fun shouldClearBackStack(screen: Screen?): Boolean {
        return screen is Screen.Landing
    }
    
    fun shouldAddLandingToBackStack(screen: Screen?): Boolean {
        return screen != null && 
               screen !is Screen.Landing && 
               screen !is Screen.Permission
    }
}
```

**Parameter Handling:**
- **Path parameters**: `/route/param` → `pathSegments.getOrNull(1)`
- **Query parameters**: `/route?key=value` → `uri.getQueryParameter("key")`
- **Fallback chain**: Try path first, then multiple query parameter names

### 4. ScreenTimeNavigation Integration

```kotlin
@Composable
fun ScreenTimeNavigation(
    scheme: ODSTheme = neutralScheme,
    deeplinkUri: Uri? = null
) {
    val backStack = rememberNavBackStack(
        if (isUsagePermission) Screen.Landing else Screen.Permission
    )
    
    LaunchedEffect(deeplinkUri) {
        if (deeplinkUri != null) {
            val deeplinkScreen = DeeplinkParser.parseDeeplink(deeplinkUri)
            if (deeplinkScreen != null) {
                if (DeeplinkParser.shouldClearBackStack(deeplinkScreen)) {
                    // Landing: clear everything
                    backStack.clear()
                    backStack.add(deeplinkScreen)
                } else if (DeeplinkParser.shouldAddLandingToBackStack(deeplinkScreen)) {
                    // Other screens: Landing → Target
                    backStack.clear()
                    backStack.add(Screen.Landing)
                    backStack.add(deeplinkScreen)
                } else {
                    // Permission: just navigate
                    backStack.clear()
                    backStack.add(deeplinkScreen)
                }
            }
        }
    }
    
    // ... rest of navigation
}
```

**Back Stack Rules:**
1. **Landing**: Clear all, add Landing only
2. **Permission**: Clear all, add Permission only
3. **Other screens**: Clear all, add Landing, then target screen

## 🎯 Supported Routes

### Simple Routes (No Parameters)

| Route | Custom Scheme | HTTPS | Screen |
|-------|--------------|-------|--------|
| landing, home | `apptime://screen/landing` | `https://apptime.in/home` | Landing |
| profile | `apptime://screen/profile` | `https://apptime.in/profile` | Profile |
| statistics | `apptime://screen/statistics` | `https://apptime.in/statistics` | Statistics |
| leaderboard | `apptime://screen/leaderboard` | `https://apptime.in/leaderboard` | Leaderboard |
| challenges | `apptime://screen/challenges` | `https://apptime.in/challenge_list` | Challenges |
| rewards | `apptime://screen/rewards` | `https://apptime.in/reward` | Reward |
| coin_history | `apptime://screen/coin_history` | `https://apptime.in/coin_history` | CoinHistory |
| wallpaper | `apptime://screen/wallpaper` | `https://apptime.in/wallpapers` | Wallpaper |
| wallpaper_search | `apptime://screen/wallpaper_search` | - | WallpaperSearch |
| notifications | `apptime://screen/notifications` | `https://apptime.in/captured_notifications` | Notifications |
| control_center | `apptime://screen/control_center` | - | ControlCenter |
| manage_location | `apptime://screen/manage_location` | `https://apptime.in/location` | Location |
| file_manager | `apptime://screen/file_manager` | `https://apptime.in/files` | FileManager |
| app_lock | `apptime://screen/app_lock` | - | AppLock |
| focus_mode | `apptime://screen/focus_mode` | - | FocusMode |
| permission | `apptime://screen/permission` | - | Permission |

### Parametrized Routes

#### Challenge Detail
```
apptime://screen/challenge_detail/{challengeId}
apptime://screen/challenge_detail?challengeId={value}
apptime://screen/challenge_detail?id={value}

Example: apptime://screen/challenge_detail/challenge_123
```

#### App Usage Detail
```
apptime://screen/app_usage_detail/{packageName}
apptime://screen/app_details/{packageName}
apptime://screen/app_usage_detail?packageName={value}
apptime://screen/app_usage_detail?package={value}

Example: apptime://screen/app_usage_detail/com.instagram.android
```

#### Record Detail
```
apptime://screen/record_detail/{username}
apptime://screen/record_detail?username={value}
apptime://screen/record_detail?user={value}

Example: apptime://screen/record_detail/john_doe
```

#### Reward Transaction
```
apptime://screen/reward_transaction/{transactionId}
apptime://screen/reward_transaction?transactionId={value}
apptime://screen/reward_transaction?id={value}

Example: apptime://screen/reward_transaction/12345
```

## 🔔 Firebase Cloud Messaging Integration

### Notification Payload with Deeplink

```json
{
  "notification": {
    "title": "New Challenge Available!",
    "body": "Join the 7-day screen time challenge"
  },
  "data": {
    "deeplink": "apptime://screen/challenge_detail/challenge_123",
    "type": "challenge",
    "image": "https://example.com/challenge_image.jpg"
  }
}
```

### NotificationHelper Integration

The `NotificationHelper` automatically extracts deeplinks from FCM payloads:

```kotlin
fun showNotification(
    context: Context,
    notificationId: Int,
    title: String,
    message: String,
    data: Map<String, String>? = null,
    deeplink: String? = null
) {
    // Creates intent with deeplink
    val intent = createDeeplinkIntent(context, deeplink, data)
    val pendingIntent = PendingIntent.getActivity(...)
    
    // Build notification with pending intent
    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText(message)
        .setContentIntent(pendingIntent)
        .build()
}
```

## 🧪 Testing

See `DEEPLINK_TEST_COMMANDS.md` for comprehensive testing commands.

### Quick Test Examples

```bash
# Cold start
adb shell am force-stop com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/statistics" com.app.screentime

# Warm start (app running)
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenges" com.app.screentime

# With parameters
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenge_detail/test_123" com.app.screentime
```

## 🐛 Troubleshooting

### Deeplink Not Working When App Running

**Problem**: Deeplinks work on cold start but not when app is open.

**Solution**: Ensure `onNewIntent()` updates the state:
```kotlin
override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    deeplinkUriState.value = intent.data // ✅ This triggers recomposition
}
```

### Parameters Not Passed Correctly

**Problem**: Screen receives null parameters.

**Solution**: Check `DeeplinkParser` has fallback chain:
```kotlin
val challengeId = pathSegments.getOrNull(1)
    ?: uri.getQueryParameter("challengeId")
    ?: uri.getQueryParameter("id")
```

### Back Stack Behavior Incorrect

**Problem**: Back button doesn't go to Landing screen.

**Solution**: Verify back stack configuration in `ScreenTimeNavigation`:
```kotlin
if (DeeplinkParser.shouldAddLandingToBackStack(deeplinkScreen)) {
    backStack.clear()
    backStack.add(Screen.Landing) // ✅ Add Landing first
    backStack.add(deeplinkScreen)
}
```

### HTTPS Links Not Working

**Problem**: HTTPS deeplinks don't open app.

**Solution**: 
1. Verify App Links in AndroidManifest
2. Add `.well-known/assetlinks.json` to your domain
3. Test verification: `adb shell pm get-app-links com.app.screentime`

## 📚 Additional Resources

- [Android App Links](https://developer.android.com/training/app-links)
- [Navigation 3 Documentation](https://github.com/androidx/androidx)
- [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging)

## ✅ Implementation Checklist

- [x] AndroidManifest intent filters configured
- [x] MainActivity handles onCreate deeplinks
- [x] MainActivity handles onNewIntent for singleTask
- [x] DeeplinkParser supports all routes
- [x] DeeplinkParser handles path parameters
- [x] DeeplinkParser handles query parameters
- [x] Back stack behavior correct for all screens
- [x] NotificationHelper integration
- [x] FCM payload deeplink extraction
- [x] Testing commands documented
- [x] All screens accessible via deeplink

## 🎉 Summary

The deeplink system is fully implemented with:
- ✅ Custom scheme and HTTPS support
- ✅ singleTask activity support (warm/cold start)
- ✅ All screens accessible
- ✅ Parameter passing (path & query)
- ✅ Correct back stack management
- ✅ FCM notification integration
- ✅ Comprehensive testing suite
