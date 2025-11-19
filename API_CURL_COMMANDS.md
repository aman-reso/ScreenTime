# ScreenTime Android App - API Curl Commands

**Base URL:** `https://fb9bddeb8786.ngrok-free.app`  
**Authentication:** Bearer Token (sent in Authorization header)  
**Content-Type:** `application/json`  
**Special Header:** `ngrok-skip-browser-warning: true`

---

## 1. Registration & Device APIs

### 1.1 Register Device
**Endpoint:** `POST /api/users/register`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/users/register" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "deviceInfo": {
      "deviceId": "android_device_id",
      "manufacturer": "Samsung",
      "model": "SM-G950F",
      "brand": "samsung",
      "product": "dreamlte",
      "device": "dreamlte",
      "hardware": "samsungexynos8895",
      "androidVersion": "13",
      "sdkVersion": 33
    }
  }'
```

---

## 2. Profile APIs

### 2.1 Get User Profile
**Endpoint:** `GET /api/v1/user/profile/{userId}`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/v1/user/profile/USER_ID" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2.2 Get User Profile with Sync Time
**Endpoint:** `GET /api/v1/user/profile/{userId}?includeSyncTime=true`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/v1/user/profile/USER_ID?includeSyncTime=true" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2.3 Update User Profile
**Endpoint:** `PUT /api/v1/user/profile`

```bash
curl -X PUT "https://fb9bddeb8786.ngrok-free.app/api/v1/user/profile" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "userId": "user123",
    "email": "user@example.com",
    "name": "John Doe",
    "avatar": "https://example.com/avatar.jpg",
    "preferences": {
      "dailyLimit": 3600000,
      "breakReminders": true,
      "weeklyReports": true,
      "dataSharing": true,
      "timezone": "America/New_York"
    }
  }'
```

### 2.4 Get User Preferences
**Endpoint:** `GET /api/v1/user/preferences/{userId}`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/v1/user/preferences/USER_ID" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2.5 Update User Preferences
**Endpoint:** `PUT /api/v1/user/preferences/{userId}`

```bash
curl -X PUT "https://fb9bddeb8786.ngrok-free.app/api/v1/user/preferences/USER_ID" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "dailyLimit": 3600000,
    "breakReminders": true,
    "weeklyReports": true,
    "dataSharing": true,
    "timezone": "America/New_York"
  }'
```

### 2.6 Get User Settings
**Endpoint:** `GET /api/v1/user/settings`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/v1/user/settings" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2.7 Update User Settings
**Endpoint:** `PUT /api/v1/user/settings`

```bash
curl -X PUT "https://fb9bddeb8786.ngrok-free.app/api/v1/user/settings" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "userId": "user123",
    "dailyLimitReached": true,
    "breakReminders": true,
    "weeklyReports": true,
    "appBlocking": false,
    "quietHours": {
      "enabled": true,
      "startTime": "22:00",
      "endTime": "07:00",
      "days": ["monday", "tuesday", "wednesday", "thursday", "friday"]
    }
  }'
```

---

## 3. App Usage APIs

### 3.1 Submit App Usage
**Endpoint:** `POST /api/usage/submit`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/usage/submit" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "appName": "Chrome",
    "packageName": "com.android.chrome",
    "openedAt": "2024-01-15T10:30:00Z",
    "duration": 1800000,
    "isSystemApp": false,
    "totalScreenTime": 7200000
  }'
```

### 3.2 Batch Usage Events Submission
**Endpoint:** `POST /api/usage/events/batch`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/usage/events/batch" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "syncTime": "2024-01-15T10:30:00Z",
    "events": [
      {
        "packageName": "com.example.app",
        "appName": "Example App",
        "isSystemApp": false,
        "eventType": "MOVE_TO_FOREGROUND",
        "eventTimestamp": "2024-01-15T10:30:00Z",
        "duration": null
      },
      {
        "packageName": "com.example.app",
        "appName": "Example App",
        "isSystemApp": false,
        "eventType": "MOVE_TO_BACKGROUND",
        "eventTimestamp": "2024-01-15T10:35:00Z",
        "duration": 300000
      }
    ]
  }'
```

**Request Body:**
- `syncTime`: ISO 8601 timestamp of when the sync occurred
- `events`: Array of usage events
  - `packageName`: Package name of the app
  - `appName`: Display name of the app
  - `isSystemApp`: Boolean indicating if it's a system app
  - `eventType`: Either "MOVE_TO_FOREGROUND" or "MOVE_TO_BACKGROUND"
  - `eventTimestamp`: ISO 8601 timestamp of when the event occurred
  - `duration`: Duration in milliseconds (only for MOVE_TO_BACKGROUND events, null for MOVE_TO_FOREGROUND)

---

### 3.3 Batch Usage Submission
**Endpoint:** `POST /api/usage/batch`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/usage/batch" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '[
    {
      "packageName": "com.android.chrome",
      "appName": "Chrome",
      "usageTime": 1800000,
      "lastUsed": 1705312200000,
      "isSystemApp": false,
      "category": "browser"
    },
    {
      "packageName": "com.whatsapp",
      "appName": "WhatsApp",
      "usageTime": 900000,
      "lastUsed": 1705313100000,
      "isSystemApp": false,
      "category": "social"
    }
  ]'
```

### 3.3 Sync Hourly Usage
**Endpoint:** `POST /api/usage/batch`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/usage/batch" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "date": "2024-01-15",
    "hourlyData": {
      "10": [
        {
          "packageName": "com.android.chrome",
          "appName": "Chrome",
          "usageTime": 1800000,
          "isSystemApp": false
        }
      ],
      "11": [
        {
          "packageName": "com.whatsapp",
          "appName": "WhatsApp",
          "usageTime": 900000,
          "isSystemApp": false
        }
      ]
    }
  }'
```

### 3.4 Get Usage Stats
**Endpoint:** `GET /api/usage/get`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/usage/get" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 3.5 Get Usage by Timeframe
**Endpoint:** `POST /api/usage/timeframe`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/usage/timeframe" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "startDate": "2024-01-01",
    "endDate": "2024-01-31",
    "includeSystemApps": false
  }'
```

### 3.6 Get Usage by Timeframe (GET with query param)
**Endpoint:** `GET /api/usage/timeframe?date=2024-01-15`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/usage/timeframe?date=2024-01-15" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 3.7 Get Complete App History
**Endpoint:** `POST /api/usage/history`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/usage/history" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "page": 1,
    "pageSize": 50
  }'
```

### 3.8 Export Data
**Endpoint:** `POST /api/usage/history`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/usage/history" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "startDate": "2024-01-01",
    "endDate": "2024-01-31",
    "format": "json"
  }'
```

### 3.9 Sync Usage Data
**Endpoint:** `POST /api/usage/sync`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/usage/sync" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "lastSyncTimestamp": 1705312200000,
    "data": {
      "appEvent": [
        {
          "packageName": "com.android.chrome",
          "appName": "Chrome",
          "eventType": "OPENED",
          "timestamp": 1705312200000
        }
      ],
      "timestamp": 1705312200000,
      "timezone": "America/New_York"
    }
  }'
```

---

## 4. Leaderboard APIs

### 4.1 Get Daily Leaderboard
**Endpoint:** `GET /api/leaderboard/daily`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/leaderboard/daily" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 4.2 Get Weekly Leaderboard
**Endpoint:** `GET /api/leaderboard/weekly`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/leaderboard/weekly" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 4.3 Get Monthly Leaderboard
**Endpoint:** `GET /api/leaderboard/monthly`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/leaderboard/monthly" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 5. Search APIs

### 5.1 Search Users
**Endpoint:** `GET /api/users/search?q={query}`

```bash
curl -X GET "https://f6b74f4a2254.ngrok-free.app/api/users/search?q=user_b002e76b" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Query Parameters:**
- `q`: Search query (username to search for)

**Response:**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "username": "user_b002e76b",
      "email": null,
      "name": null,
      "createdAt": "2025-11-13T14:09:53.334793Z",
      "isActive": false
    }
  ],
  "message": "Users found: 1",
  "timestamp": "2025-11-13T16:23:22.328239Z",
  "error": null
}
```

**Response Fields:**
- `username`: Username of the user
- `email`: Email address (nullable)
- `name`: Display name (nullable)
- `createdAt`: Account creation timestamp
- `isActive`: Whether the account is active

**Note:** The search uses GET request with query parameter. Authentication is handled via Bearer token in the Authorization header (automatically added by NetworkClient).

---

## 6. TOTP APIs

### 6.1 Verify TOTP
**Endpoint:** `POST /api/totp/verify`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/totp/verify" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "secret": "JBSWY3DPEHPK3PXP",
    "code": "123456",
    "tolerance": 1
  }'
```

**Request Body:**
- `secret`: TOTP secret key
- `code`: 6-digit TOTP code to verify
- `tolerance`: Number of time steps to allow (default: 1)

**Response:**
```json
{
  "success": true,
  "status": 200,
  "data": {
    "valid": true,
    "message": "TOTP code verified successfully",
    "allowedTimePeriod": 60
  },
  "message": null,
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Response Fields:**
- `valid`: Boolean indicating if the TOTP code is valid
- `message`: Optional message string
- `time`: Time period in seconds (default: 60 seconds) - indicates how long the verification is valid

### 6.2 Verify TOTP by Username
**Endpoint:** `POST /api/users/{username}/totp/verify`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/users/john/totp/verify" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "code": "123456"
  }'
```

**Request Body:**
- `code`: 6-digit TOTP code to verify

**Response:**
```json
{
  "success": true,
  "status": 200,
  "data": {
    "valid": true,
    "time": 60,
    "message": "TOTP code is valid"
  },
  "message": "TOTP code verified successfully",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Response Fields:**
- `valid`: Boolean indicating if the TOTP code is valid
- `message`: Optional message string
- `time`: Time period in seconds (default: 60 seconds) - indicates how long the verification is valid

**Note:** This endpoint is used when searching for a user. User B clicks on a search result card, enters User A's TOTP code to verify access.

### 6.3 Generate TOTP
**Endpoint:** `POST /api/totp/generate`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/totp/generate" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 7. Focus Duration APIs

### 7.1 Submit Focus Duration
**Endpoint:** `POST /api/focus/submit`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/focus/submit" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "focusDuration": 3600000,
    "startTime": "2024-01-15T10:00:00Z",
    "endTime": "2024-01-15T11:00:00Z",
    "sessionType": "work"
  }'
```

### 7.2 Get Focus History
**Endpoint:** `POST /api/focus/history`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/focus/history" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "startDate": "2024-01-01",
    "endDate": "2024-01-31"
  }'
```

### 7.3 Get Focus Stats
**Endpoint:** `GET /api/focus/stats`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/focus/stats" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 8. Consent APIs

### 8.1 Get Consents
**Endpoint:** `GET /api/consents`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/consents" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 8.2 Submit Consents
**Endpoint:** `POST /api/consents`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/consents" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "consents": [
      {
        "id": 1,
        "value": "accepted"
      },
      {
        "id": 2,
        "value": "rejected"
      }
    ]
  }'
```

### 8.3 Submit Consent (Legacy)
**Endpoint:** `POST /api/consents`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/consents" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "username": "user123",
    "hasConsent": true,
    "dataSharing": true,
    "analytics": true,
    "marketing": false
  }'
```

### 8.4 Get Consent Status
**Endpoint:** `GET /api/consent/status?username=user123`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/consent/status?username=user123" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 9. Blocked Domain APIs

### 9.1 Get Blocked Domains
**Endpoint:** `GET /api/blocked-domains`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/blocked-domains" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 9.2 Get Domain Groups
**Endpoint:** `GET /api/blocked-domains/groups`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/blocked-domains/groups" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 9.3 Submit Blocked Domain
**Endpoint:** `POST /api/blocked-domains`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/blocked-domains" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "domain": "example.com",
    "groupId": 1,
    "isActive": true
  }'
```

### 9.4 Update Blocked Domain
**Endpoint:** `PUT /api/blocked-domains/{id}`

```bash
curl -X PUT "https://fb9bddeb8786.ngrok-free.app/api/blocked-domains/123" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "domain": "example.com",
    "groupId": 1,
    "isActive": false
  }'
```

### 9.5 Delete Blocked Domain
**Endpoint:** `DELETE /api/blocked-domains/{id}`

```bash
curl -X DELETE "https://fb9bddeb8786.ngrok-free.app/api/blocked-domains/123" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 10. URL Search APIs (VPN Tracking)

### 10.1 Submit URL Search
**Endpoint:** `POST /api/url-search/submit`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/url-search/submit" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "url": "https://example.com/page",
    "domain": "example.com",
    "searchedAt": "2024-01-15T10:30:00Z",
    "searchType": "web"
  }'
```

### 10.2 Batch Submit URL Search
**Endpoint:** `POST /api/url-search/batch`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/url-search/batch" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "urlSearches": [
      {
        "url": "https://example.com/page1",
        "domain": "example.com",
        "searchedAt": "2024-01-15T10:30:00Z",
        "searchType": "web"
      },
      {
        "url": "https://example.com/page2",
        "domain": "example.com",
        "searchedAt": "2024-01-15T10:35:00Z",
        "searchType": "web"
      }
    ]
  }'
```

### 10.3 Get URL History
**Endpoint:** `POST /api/url-search/history`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/url-search/history" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "startDate": "2024-01-01",
    "endDate": "2024-01-31",
    "domain": "example.com"
  }'
```

---

## 11. Notification APIs

### 11.1 Send Notification
**Endpoint:** `POST /api/v1/notifications/send`

```bash
curl -X POST "https://fb9bddeb8786.ngrok-free.app/api/v1/notifications/send" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "userId": "user123",
    "message": "You have reached your daily limit",
    "type": "daily_limit"
  }'
```

### 11.2 Get Notification History
**Endpoint:** `GET /api/v1/notifications/history?userId=user123`

```bash
curl -X GET "https://fb9bddeb8786.ngrok-free.app/api/v1/notifications/history?userId=user123" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 11.3 Update Notification Settings
**Endpoint:** `PUT /api/v1/notifications/settings`

```bash
curl -X PUT "https://fb9bddeb8786.ngrok-free.app/api/v1/notifications/settings" \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "userId": "user123",
    "dailyLimitReached": true,
    "breakReminders": true,
    "weeklyReports": true,
    "appBlocking": false,
    "quietHours": {
      "enabled": true,
      "startTime": "22:00",
      "endTime": "07:00",
      "days": ["monday", "tuesday", "wednesday", "thursday", "friday"]
    }
  }'
```

---

## Notes

1. **Authentication**: Replace `YOUR_TOKEN` with the actual Bearer token. The token is typically the `userId` stored in PreferencesManager.

2. **Base URL**: The current base URL is `https://fb9bddeb8786.ngrok-free.app`. This is a development URL and may change.

3. **Request IDs**: Replace placeholder values like `USER_ID`, `user123`, etc. with actual values.

4. **Timestamps**: Use ISO 8601 format for timestamps (e.g., `2024-01-15T10:30:00Z`).

5. **Dates**: Use `YYYY-MM-DD` format for date fields.

6. **Duration**: All time durations are in milliseconds.

7. **Error Handling**: All APIs return responses wrapped in `ApiResponse<T>` format:
   ```json
   {
     "success": true,
     "status": 200,
     "data": {...},
     "message": "Success",
     "timestamp": "2024-01-15T10:30:00Z",
     "error": null
   }
   ```

---

## Quick Reference

| Category | Endpoints | Count |
|----------|-----------|-------|
| Registration | 1 | 1 |
| Profile | 7 | 7 |
| App Usage | 9 | 9 |
| Leaderboard | 3 | 3 |
| Search | 1 | 1 |
| TOTP | 2 | 2 |
| Focus | 3 | 3 |
| Consent | 4 | 4 |
| Blocked Domain | 5 | 5 |
| URL Search | 3 | 3 |
| Notifications | 3 | 3 |
| **Total** | **41** | **41** |

