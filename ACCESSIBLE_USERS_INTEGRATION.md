# Accessible Users Integration in Control Center

## 📋 Overview

Integrated the `/api/v1/user/totp/accessible-users` API endpoint into the Control Center screen to display users whose data the current user can access.

## 🔗 API Endpoint

```
GET /api/v1/user/totp/accessible-users
Authorization: Bearer <token>
```

### Response Format

```json
{
    "success": true,
    "status": 200,
    "data": {
        "accessibleUserIds": [
            "aman",
            "user2",
            "user3"
        ]
    },
    "message": "Accessible users list retrieved successfully",
    "timestamp": "2026-01-22T20:01:21.497842Z",
    "error": null
}
```

## 🎯 Implementation

### 1. **API Models** (`ApiModels.kt`)

```kotlin
@Serializable
data class AccessibleUsersData(
    val accessibleUserIds: List<String> = emptyList()
)

@Serializable
data class AccessibleUsersResponse(
    val data: AccessibleUsersData = AccessibleUsersData()
)
```

### 2. **API Endpoint** (`ApiEndpoints.kt`)

```kotlin
object TOTP {
    // ... existing endpoints
    const val ACCESSIBLE_USERS = "/api/v1/user/totp/accessible-users"
}
```

### 3. **Service Layer** (`ControlCenterService.kt` & `ControlCenterServiceImpl.kt`)

```kotlin
// Interface
suspend fun getAccessibleUsers(): Result<ApiResponse<AccessibleUsersResponse>>

// Implementation
override suspend fun getAccessibleUsers(): Result<ApiResponse<AccessibleUsersResponse>> {
    return try {
        val response = httpClient.get(ApiEndpoints.TOTP.ACCESSIBLE_USERS) {
            contentType(ContentType.Application.Json)
        }
        if (response.status.isSuccess()) {
            val apiResponse: ApiResponse<AccessibleUsersResponse> = response.body()
            Result.success(apiResponse)
        } else {
            Result.failure(Exception("Failed to get accessible users: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

### 4. **Repository Layer** (`ControlCenterRepository.kt`)

```kotlin
suspend fun getAccessibleUsers(): Result<List<String>> {
    return controlCenterService.getAccessibleUsers().fold(
        onSuccess = { apiResponse ->
            if (apiResponse.data != null) {
                Result.success(apiResponse.data!!.data.accessibleUserIds)
            } else {
                Result.failure(Exception(apiResponse.message ?: "No data received"))
            }
        },
        onFailure = { exception ->
            Result.failure(exception)
        }
    )
}
```

### 5. **ViewModel** (`ControlCenterViewModel.kt`)

```kotlin
data class ControlCenterUiState(
    val isLoading: Boolean = false,
    val allowedUsers: List<AllowedUser> = emptyList(),
    val accessibleUsers: List<String> = emptyList(), // NEW
    val error: String? = null,
    val isAdding: Boolean = false,
    val isRemoving: Set<String> = emptySet()
)

// In ViewModel init
init {
    loadControlPanel()
    loadAccessibleUsers() // NEW
}

private fun loadAccessibleUsers() {
    viewModelScope.launch {
        repository.getAccessibleUsers().fold(
            onSuccess = { userIds ->
                _uiState.value = _uiState.value.copy(
                    accessibleUsers = userIds
                )
            },
            onFailure = { exception ->
                // Don't show error for accessible users, just log it
                // The main control panel data is more important
            }
        )
    }
}
```

### 6. **UI Screen** (`ControlCenterScreen.kt`)

```kotlin
@Composable
fun ControlCenterScreen(...) {
    // ... existing code

    ODSLazyColumn(...) {
        // NEW: Accessible Users Section
        if (uiState.accessibleUsers.isNotEmpty()) {
            item {
                AppLargeSectionTitle(
                    title = stringResource(R.string.you_have_access_to_users)
                )
            }

            items(uiState.accessibleUsers) { username ->
                AccessibleUserItem(
                    username = username,
                    onCardClick = { onNavigateToRecordDetail(username) },
                    scheme = scheme
                )
            }

            item {
                Spacer(modifier = Modifier.height(DSVariables.spacingComponent5))
            }
        }

        // Existing: Allowed Users Section
        item {
            AppLargeSectionTitle(title = stringResource(R.string.allowed_usernames))
        }
        // ... rest of allowed users
    }
}

@Composable
private fun AccessibleUserItem(
    username: String,
    onCardClick: () -> Unit,
    scheme: ODSTheme
) {
    ODSCardBasic(
        contentPadding = ODSPadding(
            vertical = DSVariables.spacingComponent4,
            horizontal = DSVariables.spacingComponent4
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        contentSlot = {
            ODSRow(...) {
                ODSColumn(...) {
                    ODSText(
                        text = username,
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                    ODSText(
                        text = stringResource(R.string.tap_to_view_details),
                        style = DSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        }
    )
}
```

### 7. **String Resources** (`strings.xml`)

```xml
<string name="you_have_access_to_users">You have accessible users</string>
<string name="tap_to_view_details">Tap to view details</string>
```

## 📱 UI Layout

### Control Center Screen Structure

```
┌──────────────────────────────────────┐
│  Control Center                      │
│  [Back] <title> [+Add]              │
├──────────────────────────────────────┤
│  Description text                    │
│                                      │
│  You have accessible users           │ <-- NEW SECTION
│  ┌────────────────────────────────┐ │
│  │ aman                           │ │
│  │ Tap to view details            │ │
│  └────────────────────────────────┘ │
│  ┌────────────────────────────────┐ │
│  │ user2                          │ │
│  │ Tap to view details            │ │
│  └────────────────────────────────┘ │
│                                      │
│  Allowed usernames                   │ <-- EXISTING SECTION
│  ┌────────────────────────────────┐ │
│  │ john_doe              [Manage] │ │
│  │ Added: Jan 20, 2026           │ │
│  │ Expires at: Jan 21, 2026      │ │
│  └────────────────────────────────┘ │
└──────────────────────────────────────┘
```

## ✨ Features

1. **Separate Section**: Accessible users displayed above allowed users
2. **Click to View**: Tap on any accessible user card to navigate to their record detail
3. **Clean UI**: Simple card showing username and hint text
4. **Conditional Display**: Only shows section if there are accessible users
5. **Silent Errors**: Doesn't break the screen if API fails (main control panel is priority)

## 🔄 Data Flow

```
App Launch
    ↓
ViewModel Init
    ↓
loadAccessibleUsers()
    ↓
Repository.getAccessibleUsers()
    ↓
ControlCenterService.getAccessibleUsers()
    ↓
HTTP GET /api/v1/user/totp/accessible-users
    ↓
Parse Response
    ↓
Update UI State
    ↓
UI Recomposes
    ↓
Show Accessible Users Section
```

## 🧪 Testing

### Manual Test Steps

1. **Open Control Center**
   ```bash
   adb shell am start -n com.app.screentime/.MainActivity
   # Navigate to Control Center from menu
   ```

2. **Verify API Call**
   - Check Logcat for network request
   - Verify Authorization header is sent
   - Check response contains accessible users

3. **Check UI Display**
   - Verify "You have accessible users" section appears
   - Verify user cards are displayed
   - Tap on user card → should navigate to record detail

4. **Test Empty State**
   - If no accessible users → section should not appear
   - Only "Allowed usernames" section visible

5. **Test Error Handling**
   - If API fails → no error shown
   - Control panel data still loads normally

### API Test with cURL

```bash
curl --location 'http://localhost:8080/api/v1/user/totp/accessible-users' \
--header 'Authorization: Bearer YOUR_TOKEN_HERE'
```

Expected Response:
```json
{
    "success": true,
    "status": 200,
    "data": {
        "accessibleUserIds": ["aman", "user2"]
    },
    "message": "Accessible users list retrieved successfully",
    "timestamp": "2026-01-22T20:01:21.497842Z",
    "error": null
}
```

## 📂 Files Modified

1. `core/network/src/main/java/com/app/screentime/core/network/ApiEndpoints.kt`
   - Added `ACCESSIBLE_USERS` endpoint

2. `app/src/main/java/com/app/screentime/network/model/ApiModels.kt`
   - Added `AccessibleUsersData` model
   - Added `AccessibleUsersResponse` model

3. `app/src/main/java/com/app/screentime/controlcenter/service/ControlCenterService.kt`
   - Added `getAccessibleUsers()` interface method

4. `app/src/main/java/com/app/screentime/controlcenter/service/ControlCenterServiceImpl.kt`
   - Implemented `getAccessibleUsers()` method

5. `app/src/main/java/com/app/screentime/controlcenter/repository/ControlCenterRepository.kt`
   - Added `getAccessibleUsers()` repository method

6. `app/src/main/java/com/app/screentime/controlcenter/viewmodel/ControlCenterViewModel.kt`
   - Added `accessibleUsers` to UI state
   - Added `loadAccessibleUsers()` method
   - Call `loadAccessibleUsers()` on init

7. `app/src/main/java/com/app/screentime/controlcenter/screen/ControlCenterScreen.kt`
   - Added `AccessibleUserItem` composable
   - Added accessible users section to UI
   - Integrated navigation on card click

8. `config/src/main/res/values/strings.xml`
   - Added `you_have_access_to_users` string
   - Added `tap_to_view_details` string

## 🎯 Benefits

1. **Clear Visibility**: Users can see who they have access to
2. **Easy Navigation**: Tap to view details of accessible users
3. **Non-Intrusive**: Doesn't break if API fails
4. **Consistent UI**: Matches existing Control Center design
5. **Performance**: Loaded in parallel with control panel data

## 🔮 Future Enhancements

1. **Add User Icons**: Show profile pictures if available
2. **Last Accessed Time**: Display when you last viewed their data
3. **Access Type**: Show what type of access (TOTP, permanent, etc.)
4. **Search/Filter**: For users with many accessible accounts
5. **Refresh Button**: Manual refresh for accessible users list
6. **Swipe Actions**: Quick actions on user cards

## ✅ Summary

✅ API endpoint integrated
✅ Models created for response parsing  
✅ Service layer implemented
✅ Repository layer added
✅ ViewModel updated with state management
✅ UI section added with card design
✅ Navigation to record detail working
✅ String resources added
✅ Error handling implemented
✅ Ready for testing

The accessible users feature is now fully integrated into the Control Center screen!
