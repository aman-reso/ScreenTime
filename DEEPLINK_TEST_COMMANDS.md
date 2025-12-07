# Quick Test Commands for Deep Links

## Basic Screens

```bash
# Landing
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/landing" com.app.screentime

# Profile
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/profile" com.app.screentime

# Statistics
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/statistics" com.app.screentime

# Challenges
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenges" com.app.screentime

# Leaderboard
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/leaderboard" com.app.screentime

# Search
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/search" com.app.screentime
```

## Parametrized Screens

```bash
# Challenge Detail (example: challenge ID 123)
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/challenge_detail/123" com.app.screentime

# App Usage Detail (example: Instagram)
adb shell am start -W -a android.intent.action.VIEW -d "https://apptime.in/app_usage_detail/com.instagram.android" com.app.screentime

# Record Detail (example: username john_doe)
adb shell am start -W -a android.intent.action.VIEW -d "apptime://screen/record_detail/john_doe" com.app.screentime
```

## Testing Checklist

- [ ] Test cold start (app not running)
- [ ] Test warm start (app already running)
- [ ] Verify back button returns to Landing screen
- [ ] Verify back button from Landing closes app
- [ ] Test both custom scheme and HTTPS URLs
- [ ] Test parametrized routes with real data
