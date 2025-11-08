# Device Registration API Integration

## Overview
The ScreenTime app now integrates with the device registration API endpoint to send device information and retrieve a userId.

## API Details

**Endpoint:** `http://localhost:8080/api/users/register`

**Method:** POST

**Content-Type:** application/json

**Request Body:**
```json
{
    "deviceId": "your-device-id"
}
```

**Response:**
```json
{
    "userId": "user-123",
    "deviceId": "your-device-id",
    "message": "Device registered successfully"
}
```

## Implementation

### Files Modified

1. **ApiEndpoints.kt** - Added device registration endpoint
2. **ApiModels.kt** - Added request/response models for device registration
3. **ApiService.kt** - Added registerDevice() method
4. **ApiServiceImpl.kt** - Implemented registerDevice() method
5. **NetworkRepository.kt** - Added registerDevice() wrapper method
6. **NetworkClient.kt** - Configured to use localhost:8080 as base URL
7. **NetworkModule.kt** - Added DeviceRegistrationHelper to DI
8. **DeviceRegistrationHelper.kt** - Created utility class for device registration
9. **ScreenTimeApplication.kt** - Calls device registration on app startup

### Usage

The device registration happens automatically when the app starts. The `DeviceRegistrationHelper` class handles:

- Getting the device ID from Android settings
- Registering the device with the backend
- Storing the userId in SharedPreferences
- Preventing re-registration on subsequent app launches

### Using DeviceRegistrationHelper

```kotlin
@Inject
lateinit var deviceRegistrationHelper: DeviceRegistrationHelper

// In your code:
val userId = deviceRegistrationHelper.ensureDeviceRegistered(context)
```

### Manual Registration

If you need to manually register a device:

```kotlin
val result = deviceRegistrationHelper.registerDevice(context)
result.fold(
    onSuccess = { response ->
        val userId = response.userId
        // Use userId for subsequent API calls
    },
    onFailure = { exception ->
        // Handle error
    }
)
```

## Important Notes

1. **Base URL:** The app uses `http://localhost:8080` as the base URL. This works on the Android emulator. For physical devices, you may need to change this to your local network IP address (e.g., `http://192.168.1.x:8080`).

2. **Stored Data:** The userId and device registration status are stored in SharedPreferences with keys:
   - `user_id` - The userId returned from the API
   - `device_id` - The Android device ID
   - `is_device_registered` - Boolean flag indicating registration status

3. **Retry Logic:** The registration only happens once per device. Subsequent app launches will use the stored userId.

## Testing

To test the integration:

1. Start your backend server on `http://localhost:8080`
2. Run the Android app
3. Check the logs for "Device registered successfully"
4. The userId will be stored and can be retrieved using:
   ```kotlin
   deviceRegistrationHelper.getUserId(context)
   ```

## Network Configuration

The API base URL can be changed in `ApiEndpoints.kt`:
- For local development: `LOCAL_BASE_URL = "http://localhost:8080"`
- For physical device testing: Update to your machine's IP address
- For production: Use `BASE_URL` or `DEV_BASE_URL`

