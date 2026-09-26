package com.app.screentime.core.network.session

import android.content.Context
import android.os.Build
import android.util.Log
import com.app.screentime.core.network.api.WinterApi
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

import com.app.screentime.core.network.preferences.PreferencesManager

@Singleton
class FcmTokenManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val winterApi: WinterApi,
    private val preferencesManager: PreferencesManager
) {
    companion object {
        private const val PREFS_NAME = "fcm_token_prefs"
        private const val KEY_LAST_FCM_TOKEN = "last_synced_fcm_token"
        private const val KEY_LAST_FCM_USER_ID = "last_synced_fcm_user_id"
    }

    private val tag = "FcmTokenManager"
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val fcmPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun syncFcmTokenAsync(overrideFcmToken: String? = null) {
        scope.launch {
            syncFcmToken(overrideFcmToken)
        }
    }

    fun invalidateTokenSync() {
        fcmPrefs.edit().clear().apply()
    }

    suspend fun syncFcmToken(overrideFcmToken: String? = null) {
        val userAuthToken = preferencesManager.getToken()
        val currentUserId = preferencesManager.getUserId()
        if (userAuthToken.isNullOrBlank() || currentUserId.isNullOrBlank()) {
            Log.d(tag, "⏭️ Skipping FCM token sync: user is not authenticated")
            return
        }

        try {
            val fcmToken = overrideFcmToken?.ifBlank { null } ?: fetchFirebaseToken()

            if (fcmToken.isNullOrBlank()) {
                Log.w(tag, "⚠️ FCM token is null or blank, skipping registration")
                return
            }

            // Persistently check if this exact token was already registered for this user
            val savedToken = fcmPrefs.getString(KEY_LAST_FCM_TOKEN, null)
            val savedUserId = fcmPrefs.getString(KEY_LAST_FCM_USER_ID, null)

            if (fcmToken == savedToken && currentUserId == savedUserId) {
                Log.d(tag, "⏭️ FCM token already synchronized and unchanged. Skipping API call.")
                return
            }

            val deviceId = "${Build.MANUFACTURER}_${Build.MODEL}".replace("\\s+".toRegex(), "_").lowercase()
            val appVersion = try {
                val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                pInfo.versionName ?: "1.0.0"
            } catch (e: Exception) {
                "1.0.0"
            }

            Log.i(tag, "📤 Registering new/updated FCM token: fcmToken=${fcmToken.take(12)}..., deviceId=$deviceId, appVersion=$appVersion")
            val res = winterApi.registerFcmToken(
                token = userAuthToken,
                fcmToken = fcmToken,
                deviceId = deviceId,
                appVersion = appVersion
            )

            if (res != null && res.success) {
                fcmPrefs.edit()
                    .putString(KEY_LAST_FCM_TOKEN, fcmToken)
                    .putString(KEY_LAST_FCM_USER_ID, currentUserId)
                    .apply()
                Log.i(tag, "✅ FCM device token registered successfully for user: ${res.data?.user_id ?: currentUserId}")
            } else {
                Log.w(tag, "⚠️ FCM device token registration failed or returned null: ${res?.message}")
            }
        } catch (t: Throwable) {
            Log.e(tag, "❌ Error during FCM token registration: ${t.message}", t)
        }
    }

    private suspend fun fetchFirebaseToken(): String? = suspendCancellableCoroutine { continuation ->
        try {
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result)
                    } else {
                        Log.w(tag, "Failed to retrieve FCM token from Firebase SDK: ${task.exception?.message}")
                        continuation.resume(null)
                    }
                }
        } catch (e: Exception) {
            Log.e(tag, "Exception fetching Firebase token: ${e.message}", e)
            continuation.resume(null)
        }
    }
}
