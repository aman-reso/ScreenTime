package com.app.screentime.core.network.security

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.app.screentime.core.network.ApiEndpoints
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.StandardIntegrityManager
import com.google.android.play.core.integrity.StandardIntegrityManager.PrepareIntegrityTokenRequest
import com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityTokenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * PlayIntegrityManager
 * Manages Google Play Integrity app attestation tokens for ScreenTime (com.app.screentime).
 *
 * Scoped as a clean Hilt @Singleton (no static context leaks).
 */
@Singleton
class PlayIntegrityManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val appContext: Context = context.applicationContext

    companion object {
        private const val TAG = "PlayIntegrity"
        
        // Google Cloud Project Number linked to ScreenTime in Play Console
        val DEFAULT_CLOUD_PROJECT_NUMBER: Long = com.app.screentime.core.network.BuildConfig.PLAY_INTEGRITY_PROJECT_NUMBER
    }

    private val standardIntegrityManager: StandardIntegrityManager =
        IntegrityManagerFactory.createStandard(appContext)

    @Volatile
    private var tokenProvider: StandardIntegrityManager.StandardIntegrityTokenProvider? = null

    @Volatile
    private var cachedAttestationToken: String? = null

    @Volatile
    private var tokenExpiryTimestampMs: Long = 0L

    private val isInitializing = AtomicBoolean(false)
    private val mainHandler = Handler(Looper.getMainLooper())


    fun warmUp(cloudProjectNumber: Long = DEFAULT_CLOUD_PROJECT_NUMBER) {
        if (isDebuggable) {
            Log.d(TAG, "Debug build: skipping Google Play warmUp.")
            return
        }
        if (isInitializing.getAndSet(true)) return

        Log.d(TAG, "Initializing Google Play Standard Integrity Token Provider for project $cloudProjectNumber...")
        standardIntegrityManager.prepareIntegrityToken(
            PrepareIntegrityTokenRequest.builder()
                .setCloudProjectNumber(cloudProjectNumber)
                .build()
        ).addOnSuccessListener { provider ->
            tokenProvider = provider
            isInitializing.set(false)
            Log.i(TAG, "✅ Standard Integrity Token Provider ready.")
            prefetchTokenInBackground()
        }.addOnFailureListener { error ->
            isInitializing.set(false)
            Log.w(TAG, "⚠️ Failed to prepare Standard Integrity Token: ${error.message}")
        }
    }

    private fun prefetchTokenInBackground() {
        Thread {
            try {
                if (cachedAttestationToken.isNullOrEmpty()) {
                    val playToken = requestPlayIntegrityTokenSync()
                    val (backendToken, expiryMs) = exchangeWithBackend(playToken)
                    cachedAttestationToken = backendToken
                    tokenExpiryTimestampMs = expiryMs
                    Log.i(TAG, "✅ Play Integrity token pre-cached successfully.")
                }
            } catch (e: Throwable) {
                Log.w(TAG, "Background prefetch failed (will fetch on demand): ${e.message}")
            }
        }.start()
    }

    /**
     * Returns a valid cached attestation token, or null if expired or missing.
     */
    fun getCachedToken(): String? {
        if (isDebuggable) {
            return "simulated_genuine_play_token_screentime_debug"
        }
        val now = System.currentTimeMillis()
        return if (!cachedAttestationToken.isNullOrEmpty() && (now + 300_000L < tokenExpiryTimestampMs)) {
            cachedAttestationToken
        } else {
            null
        }
    }

    /**
     * Clear cached token on session invalidation.
     */
    fun invalidateCache() {
        cachedAttestationToken = null
        tokenExpiryTimestampMs = 0L
    }

    /**
     * Coroutine-based attestation retrieval.
     */
    suspend fun fetchAttestationToken(forceRefresh: Boolean = false): String = withContext(Dispatchers.IO) {
        if (isDebuggable) {
            val debugToken = "simulated_genuine_play_token_screentime_debug"
            cachedAttestationToken = debugToken
            tokenExpiryTimestampMs = System.currentTimeMillis() + 86400000L
            return@withContext debugToken
        }
        if (!forceRefresh) {
            getCachedToken()?.let { return@withContext it }
        }

        val playIntegrityToken = requestPlayIntegrityTokenFromGoogle()
        val (backendToken, expiryMs) = exchangeWithBackend(playIntegrityToken)

        cachedAttestationToken = backendToken
        tokenExpiryTimestampMs = expiryMs
        return@withContext backendToken
    }

    /**
     * Callback-based attestation retrieval.
     */
    fun getAttestationToken(
        forceRefresh: Boolean = false,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        if (isDebuggable) {
            val debugToken = "simulated_genuine_play_token_screentime_debug"
            cachedAttestationToken = debugToken
            tokenExpiryTimestampMs = System.currentTimeMillis() + 86400000L
            onSuccess(debugToken)
            return
        }
        val cached = if (!forceRefresh) getCachedToken() else null
        if (cached != null) {
            onSuccess(cached)
            return
        }

        Thread {
            try {
                val playIntegrityToken = requestPlayIntegrityTokenSync()
                val (backendToken, expiryMs) = exchangeWithBackend(playIntegrityToken)
                cachedAttestationToken = backendToken
                tokenExpiryTimestampMs = expiryMs
                mainHandler.post { onSuccess(backendToken) }
            } catch (t: Throwable) {
                mainHandler.post { onError(t) }
            }
        }.start()
    }

    private suspend fun requestPlayIntegrityTokenFromGoogle(): String {
        if (isDebuggable) {
            return "simulated_genuine_play_token_screentime_debug"
        }
        return suspendCancellableCoroutine { cont ->
        val requestHash = generateRequestHash()
        val provider = tokenProvider

        if (provider != null) {
            provider.request(
                StandardIntegrityTokenRequest.builder()
                    .setRequestHash(requestHash)
                    .build()
            ).addOnSuccessListener { response ->
                cont.resume(response.token())
            }.addOnFailureListener { error ->
                Log.w(TAG, "Standard Integrity request failed, falling back to classic: ${error.message}")
                requestClassicIntegrityToken(requestHash, cont)
            }
        } else {
            requestClassicIntegrityToken(requestHash, cont)
        }
        }
    }

    private val isDebuggable: Boolean by lazy {
        (appContext.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0 ||
        com.app.screentime.core.network.BuildConfig.DEBUG
    }

    private fun requestClassicIntegrityToken(
        nonce: String,
        cont: kotlin.coroutines.Continuation<String>
    ) {
        val classicManager = IntegrityManagerFactory.create(appContext)
        val request = com.google.android.play.core.integrity.IntegrityTokenRequest.builder()
            .setCloudProjectNumber(DEFAULT_CLOUD_PROJECT_NUMBER)
            .setNonce(nonce)
            .build()

        classicManager.requestIntegrityToken(request)
            .addOnSuccessListener { response -> cont.resume(response.token()) }
            .addOnFailureListener { error ->
                if (isDebuggable) {
                    Log.w(TAG, "Debug environment: Google Play failed, falling back to simulated integrity token: ${error.message}")
                    cont.resume("simulated_genuine_play_token_screentime_debug")
                } else {
                    cont.resumeWithException(error)
                }
            }
    }

    private fun requestPlayIntegrityTokenSync(): String {
        if (isDebuggable) {
            return "simulated_genuine_play_token_screentime_debug"
        }
        var result: String? = null
        var exception: Throwable? = null
        val latch = java.util.concurrent.CountDownLatch(1)

        val requestHash = generateRequestHash()
        val provider = tokenProvider
        if (provider != null) {
            provider.request(
                StandardIntegrityTokenRequest.builder()
                    .setRequestHash(requestHash)
                    .build()
            ).addOnSuccessListener { resp ->
                result = resp.token()
                latch.countDown()
            }.addOnFailureListener {
                requestClassicIntegrityTokenSync(requestHash) { token, err ->
                    result = token
                    exception = err
                    latch.countDown()
                }
            }
        } else {
            requestClassicIntegrityTokenSync(requestHash) { token, err ->
                result = token
                exception = err
                latch.countDown()
            }
        }

        latch.await(10, java.util.concurrent.TimeUnit.SECONDS)
        if (result != null) return result!!
        throw exception ?: RuntimeException("Play Integrity token request timed out")
    }

    private fun requestClassicIntegrityTokenSync(
        nonce: String,
        callback: (String?, Throwable?) -> Unit
    ) {
        val classicManager = IntegrityManagerFactory.create(appContext)
        val request = com.google.android.play.core.integrity.IntegrityTokenRequest.builder()
            .setCloudProjectNumber(DEFAULT_CLOUD_PROJECT_NUMBER)
            .setNonce(nonce)
            .build()

        classicManager.requestIntegrityToken(request)
            .addOnSuccessListener { resp -> callback(resp.token(), null) }
            .addOnFailureListener { err ->
                if (isDebuggable) {
                    Log.w(TAG, "Debug environment: Google Play sync failed, using simulated token: ${err.message}")
                    callback("simulated_genuine_play_token_screentime_debug", null)
                } else {
                    callback(null, err)
                }
            }
    }

    /**
     * Exchanges token with Backend /api/security/attest
     */
    private fun exchangeWithBackend(playToken: String): Pair<String, Long> {
        if (isDebuggable) {
            return Pair("simulated_genuine_play_token_screentime_debug", System.currentTimeMillis() + 86400000L)
        }
        val baseUrl = ApiEndpoints.getBaseUrl()
        val endpoint = "$baseUrl/api/security/attest"
        val url = URL(endpoint)
        val conn = url.openConnection() as HttpURLConnection

        try {
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("X-Platform", "android")
            conn.connectTimeout = 10_000
            conn.readTimeout = 10_000
            conn.doOutput = true
            conn.doInput = true

            val body = JSONObject().apply {
                put("integrity_token", playToken)
                put("device_id", UUID.randomUUID().toString())
            }

            OutputStreamWriter(conn.outputStream, Charsets.UTF_8).use { writer ->
                writer.write(body.toString())
                writer.flush()
            }

            val statusCode = conn.responseCode
            val stream = if (statusCode in 200..299) conn.inputStream else conn.errorStream
            val responseText = BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).use { it.readText() }

            if (statusCode !in 200..299) {
                throw RuntimeException("Backend attestation failed (HTTP $statusCode): $responseText")
            }

            val json = JSONObject(responseText)
            val data = json.optJSONObject("data") ?: json

            val attestToken = data.getString("attestation_token")
            val defaultExpiry = System.currentTimeMillis() + (7200 * 1000L) // 2 hours
            val expiryMs = if (data.has("expires_at")) {
                parseIsoDate(data.getString("expires_at")) ?: defaultExpiry
            } else {
                defaultExpiry
            }

            return Pair(attestToken, expiryMs)
        } finally {
            conn.disconnect()
        }
    }

    private fun generateRequestHash(): String {
        val raw = UUID.randomUUID().toString() + ":" + System.currentTimeMillis()
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(raw.toByteArray(Charsets.UTF_8))
        return android.util.Base64.encodeToString(bytes, android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING)
    }

    private fun parseIsoDate(iso: String): Long? {
        return try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                java.time.Instant.parse(iso).toEpochMilli()
            } else {
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.US)
                sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
                sdf.parse(iso)?.time
            }
        } catch (_: Exception) {
            null
        }
    }
}
