package com.app.screentime.messaging

import android.content.Context
import android.util.Log
import com.app.screentime.preferences.PreferencesManager
import com.app.screentime.profile.service.ProfileService
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FCMTokenManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager,
    private val profileService: ProfileService
) {
    companion object {
        private const val TAG = "FCMTokenManager"
        private val PREF_FCM_TOKEN = com.app.screentime.config.AppSecrets.Preferences.PREF_FCM_TOKEN
    }

    /**
     * Get the current FCM token
     */
    fun getCurrentToken(): String? {
        return preferencesManager.getString(PREF_FCM_TOKEN)
    }

    /**
     * Save FCM token to preferences
     */
    fun saveToken(token: String) {
        preferencesManager.putString(PREF_FCM_TOKEN, token)
        Log.d(TAG, "FCM Token saved: $token")
    }

    /**
     * Fetch and save the FCM token
     */
    fun fetchToken(onTokenReceived: ((String) -> Unit)? = null) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d(TAG, "FCM Registration Token: $token")

            // Save token
            saveToken(token)

            // Send token to backend
            sendTokenToBackend(token)

            // Callback
            onTokenReceived?.invoke(token)
        }
    }

    /**
     * Send FCM token to backend
     */
    private fun sendTokenToBackend(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = preferencesManager.getUserId()
                if (userId != null) {
                    // Update profile with firebaseToken
                    val request = com.app.screentime.network.model.ProfileUpdateRequest(
                        firebaseToken = token
                    )
                    profileService.updateProfile(request).fold(
                        onSuccess = {
                            Log.d(TAG, "Firebase token sent to backend successfully")
                        },
                        onFailure = { exception ->
                            Log.w(TAG, "Failed to send Firebase token to backend: ${exception.message}")
                        }
                    )
                } else {
                    Log.d(TAG, "User not registered yet, token will be sent during registration")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error sending Firebase token to backend: ${e.message}")
            }
        }
    }

    /**
     * Delete the FCM token (for logout scenarios)
     */
    fun deleteToken() {
        preferencesManager.remove(PREF_FCM_TOKEN)
        Log.d(TAG, "FCM Token deleted")
    }

    /**
     * Subscribe to a topic
     */
    fun subscribeToTopic(topic: String, onComplete: ((Boolean) -> Unit)? = null) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                val success = task.isSuccessful
                if (success) {
                    Log.d(TAG, "Subscribed to topic: $topic")
                } else {
                    Log.w(TAG, "Failed to subscribe to topic: $topic", task.exception)
                }
                onComplete?.invoke(success)
            }
    }

    /**
     * Unsubscribe from a topic
     */
    fun unsubscribeFromTopic(topic: String, onComplete: ((Boolean) -> Unit)? = null) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                val success = task.isSuccessful
                if (success) {
                    Log.d(TAG, "Unsubscribed from topic: $topic")
                } else {
                    Log.w(TAG, "Failed to unsubscribe from topic: $topic", task.exception)
                }
                onComplete?.invoke(success)
            }
    }
}

