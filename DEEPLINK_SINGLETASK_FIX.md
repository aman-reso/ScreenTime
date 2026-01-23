# Deeplink singleTask Fix

## 🐛 Problem

The MainActivity is configured with `launchMode="singleTask"` in AndroidManifest. This means:
- When app is NOT running → `onCreate()` is called
- When app IS running → `onNewIntent()` is called (activity is NOT recreated)

**Issue**: The original implementation only set the deeplink URI in `onCreate()`. When the app was already open and a deeplink was triggered, `onNewIntent()` was called but the deeplink wasn't processed because the Compose state wasn't updated.

## ✅ Solution

### Key Changes

#### 1. Added MutableState to Track Deeplink URI
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // MutableState to track deeplink URI changes for singleTask activity
    private val deeplinkUriState = mutableStateOf<Uri?>(null)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set initial deeplink if present
        deeplinkUriState.value = intent?.data
        
        setContent {
            val deeplinkUri by deeplinkUriState // ✅ Observe state
            
            ScreenTimeNavigation(
                deeplinkUri = deeplinkUri,
                // ... other params
            )
        }
    }
}
```

#### 2. Updated onNewIntent to Trigger Recomposition
```kotlin
override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    
    // ✅ Update state to trigger recomposition
    // This is crucial for singleTask activity
    deeplinkUriState.value = intent.data
}
```

### How It Works

#### Before (Broken) 🔴
```
App Running → Deeplink Clicked → onNewIntent() → setIntent()
                                                      ↓
                                              State NOT updated
                                                      ↓
                                              No recomposition
                                                      ↓
                                              Deeplink ignored ❌
```

#### After (Fixed) ✅
```
App Running → Deeplink Clicked → onNewIntent() → setIntent()
                                                      ↓
                                         deeplinkUriState.value = intent.data
                                                      ↓
                                              State updated
                                                      ↓
                                         Recomposition triggered
                                                      ↓
                                    LaunchedEffect(deeplinkUri) observes change
                                                      ↓
                                              DeeplinkParser.parseDeeplink()
                                                      ↓
                                              Navigation happens ✅
```

## 🧪 Testing Scenarios

### Scenario 1: Cold Start (App Not Running)
```bash
adb shell am force-stop com.app.screentime
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/statistics" com.app.screentime
```
**Expected**: App launches, shows Statistics screen
**Works**: ✅ Both before and after fix (onCreate handles it)

### Scenario 2: Warm Start (App in Background)
```bash
# 1. Open app normally
adb shell am start -n com.app.screentime/.MainActivity

# 2. Press home button (app goes to background)

# 3. Trigger deeplink
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenges" com.app.screentime
```
**Expected**: App comes to foreground, shows Challenges screen
**Before**: ❌ Shows previous screen (deeplink ignored)
**After**: ✅ Shows Challenges screen (deeplink processed)

### Scenario 3: App in Foreground
```bash
# 1. Open app and use it
adb shell am start -n com.app.screentime/.MainActivity

# 2. While app is open, trigger deeplink from another app or adb
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/wallpaper" com.app.screentime
```
**Expected**: App navigates to Wallpaper screen
**Before**: ❌ Stays on current screen (deeplink ignored)
**After**: ✅ Navigates to Wallpaper screen

## 🔍 Technical Details

### Why MutableState?

1. **Compose Observation**: Compose automatically observes state changes
2. **Automatic Recomposition**: When state changes, composables recompose
3. **Activity Lifecycle Independent**: Works regardless of onCreate/onNewIntent
4. **No Manual Callbacks**: No need for custom listeners or event buses

### Alternative Approaches (Not Used)

❌ **LiveData/Flow**: Requires ViewModel, overkill for simple state
❌ **Manual Callbacks**: Error-prone, harder to maintain
❌ **Event Bus**: Adds complexity, couples components
✅ **MutableState**: Simple, idiomatic Compose, lifecycle-aware

### Lifecycle Flow

```
Cold Start:
onCreate() → deeplinkUriState.value = intent.data → Compose observes → Navigate

Warm Start (Background):
onNewIntent() → deeplinkUriState.value = intent.data → Recompose → Navigate

Warm Start (Foreground):
onNewIntent() → deeplinkUriState.value = intent.data → Recompose → Navigate
```

## 📋 What Changed

### Files Modified
1. **MainActivity.kt**
   - Added `deeplinkUriState` property
   - Updated `onCreate()` to set initial state
   - Updated `onNewIntent()` to update state
   - Changed Compose to observe state

### Files Created
1. **DEEPLINK_SINGLETASK_FIX.md** (this file)
2. **DEEPLINK_IMPLEMENTATION.md** (comprehensive guide)
3. **DEEPLINK_TEST_COMMANDS.md** (updated with all routes)

### Files Updated
1. **DeeplinkParser.kt** - Added all routes
2. **DEEPLINK_TEST_COMMANDS.md** - Comprehensive test commands

## ✅ Benefits

1. **Works in All Scenarios**: Cold start, warm start, foreground
2. **Simple Implementation**: Just one MutableState property
3. **Idiomatic Compose**: Uses standard state management
4. **No Breaking Changes**: Existing deeplinks continue to work
5. **Easy to Test**: Clear state changes, observable behavior

## 🎯 Key Takeaways

### For singleTask Activities with Compose:
1. **Never rely only on onCreate for dynamic data**
2. **Always update observable state in onNewIntent**
3. **Use MutableState for simple activity-level state**
4. **Test both cold and warm start scenarios**

### Pattern to Follow:
```kotlin
// 1. Declare state at activity level
private val yourState = mutableStateOf<YourType?>(null)

// 2. Set initial value in onCreate
override fun onCreate(savedInstanceState: Bundle?) {
    yourState.value = getInitialValue()
    setContent {
        val value by yourState // Observe
        YourComposable(value = value)
    }
}

// 3. Update state in onNewIntent
override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    yourState.value = getUpdatedValue(intent) // Triggers recomposition
}
```

## 🚀 Result

✅ Deeplinks now work in ALL scenarios:
- Cold start (app not running)
- Warm start (app in background)
- Hot update (app in foreground)
- From notifications
- From external apps
- From web browsers

✅ Build Status: Success
✅ No breaking changes
✅ All existing functionality preserved
✅ Comprehensive documentation added

## 📚 Related Files

- `MainActivity.kt` - Main fix implementation
- `DeeplinkParser.kt` - Route parsing logic
- `ScreenTimeNavigation.kt` - Navigation handling
- `DEEPLINK_IMPLEMENTATION.md` - Full documentation
- `DEEPLINK_TEST_COMMANDS.md` - Testing guide
