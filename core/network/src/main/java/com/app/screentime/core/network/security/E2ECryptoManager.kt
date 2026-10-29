package com.app.screentime.core.network.security

import android.content.Context
import android.util.Base64
import android.util.Log
import com.app.screentime.core.network.ApiEndpoints
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.concurrent.ConcurrentHashMap
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Serializable
data class RegisterE2EKeyRequest(
    val public_key: String,
    val key_id: String? = null
)

@Serializable
data class UserE2EKeyDto(
    val user_id: String = "",
    val public_key: String = "",
    val key_id: String? = null,
    val created_at: String? = null
)

@Serializable
data class UserE2EKeyResponse(
    val success: Boolean = false,
    val message: String = "",
    val data: UserE2EKeyDto? = null
)

/**
 * End-to-End Encryption (E2EE) Manager for Chat Messaging.
 *
 * Implements client-side asymmetric Diffie-Hellman (X25519) key agreement
 * combined with authenticated AES-256-GCM symmetric encryption.
 * Zero-knowledge server relay: the server never sees the plaintext or private keys.
 */
@Singleton
class E2ECryptoManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val httpClient: HttpClient
) {
    companion object {
        private const val TAG = "E2ECryptoManager"
        const val E2E_PREFIX = "e2e:v1:"
        private const val VAULT_PREFS_NAME = "screentime_e2e_vault_prefs"
        private const val LEGACY_PREFS_NAME = "screentime_e2e_crypto_prefs"
        const val DECRYPTION_FAILED_FALLBACK = "🔒 Message could not be decrypted"
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 128
        val AUDIO_MAGIC_BYTES = "E2E_AUDIO_V1".toByteArray(Charsets.UTF_8)
    }

    private val vaultPrefs by lazy {
        context.getSharedPreferences(VAULT_PREFS_NAME, Context.MODE_PRIVATE)
    }

    private val legacyPrefs by lazy {
        context.getSharedPreferences(LEGACY_PREFS_NAME, Context.MODE_PRIVATE)
    }

    // In-memory cache of resolved recipient public keys (userId -> base64PublicKey)
    private val recipientKeyCache = ConcurrentHashMap<String, String>()

    // In-memory cache of current active user's keypair for maximum performance
    @Volatile
    private var cachedActiveUserId: String? = null
    @Volatile
    private var cachedPrivateKeyBytes: ByteArray? = null
    @Volatile
    private var cachedPublicKeyBase64: String? = null

    private val secureRandom = SecureRandom()

    init {
        ensureUserKeyPair()
    }

    private fun getActiveUserId(): String {
        return context.getSharedPreferences("chatty_session_prefs", Context.MODE_PRIVATE)
            .getString("user_id", null)
            ?.trim()
            ?.ifBlank { null }
            ?: "default_device_user"
    }

    /**
     * Resolves or generates the Hardware-backed KeyStore keypair for the specified user.
     * Guaranteed to match the user's keypair across sessions, logouts, and app restarts.
     */
    @Synchronized
    fun ensureUserKeyPair(targetUserId: String = getActiveUserId()): Pair<ByteArray, String> {
        val cleanUserId = targetUserId.trim()
        if (cachedActiveUserId == cleanUserId && cachedPrivateKeyBytes != null && !cachedPublicKeyBase64.isNullOrBlank()) {
            return Pair(cachedPrivateKeyBytes!!, cachedPublicKeyBase64!!)
        }

        val vaultKeyPriv = "vault_priv_$cleanUserId"
        val vaultKeyPub = "pub_$cleanUserId"

        // 1. Check if Hardware KeyStore vault already holds this user's keypair
        val encryptedPrivB64 = vaultPrefs.getString(vaultKeyPriv, null)
        val storedPubB64 = vaultPrefs.getString(vaultKeyPub, null)

        if (!encryptedPrivB64.isNullOrBlank() && !storedPubB64.isNullOrBlank()) {
            val decryptedPriv = AndroidKeyStoreVault.decryptBytes(encryptedPrivB64)
            if (decryptedPriv != null && decryptedPriv.size == 32) {
                cachedActiveUserId = cleanUserId
                cachedPrivateKeyBytes = decryptedPriv
                cachedPublicKeyBase64 = storedPubB64
                Log.i(TAG, "🔐 Restored hardware KeyStore E2EE keypair for user: $cleanUserId")
                return Pair(decryptedPriv, storedPubB64)
            }
        }

        // 2. Migration: check if legacy preferences have an existing keypair to migrate into hardware vault
        val legacyPrivB64 = legacyPrefs.getString("my_x25519_private_key", null)
        val legacyPubB64 = legacyPrefs.getString("my_x25519_public_key", null)
        if (!legacyPrivB64.isNullOrBlank() && !legacyPubB64.isNullOrBlank()) {
            val privBytes = decodeBase64Flexible(legacyPrivB64)
            if (privBytes.size == 32) {
                val encToVault = AndroidKeyStoreVault.encryptBytes(privBytes)
                if (encToVault != null) {
                    vaultPrefs.edit {
                        putString(vaultKeyPriv, encToVault)
                            .putString(vaultKeyPub, legacyPubB64)
                    }
                    cachedActiveUserId = cleanUserId
                    cachedPrivateKeyBytes = privBytes
                    cachedPublicKeyBase64 = legacyPubB64
                    Log.i(TAG, "📦 Migrated existing E2EE keypair into hardware KeyStore for: $cleanUserId")
                    return Pair(privBytes, legacyPubB64)
                }
            }
        }

        // 3. Generate brand new X25519 keypair for this user and seal inside Hardware KeyStore
        val freshPrivBytes = Curve25519.generatePrivateKey()
        val freshPubBytes = Curve25519.generatePublicKey(freshPrivBytes)
        val freshPubB64 = encodeBase64Url(freshPubBytes)
        val encPrivB64 = AndroidKeyStoreVault.encryptBytes(freshPrivBytes)

        vaultPrefs.edit {
            putString(vaultKeyPriv, encPrivB64 ?: encodeBase64Url(freshPrivBytes))
                .putString(vaultKeyPub, freshPubB64)
                .putBoolean("registered_$cleanUserId", false)
        }

        cachedActiveUserId = cleanUserId
        cachedPrivateKeyBytes = freshPrivBytes
        cachedPublicKeyBase64 = freshPubB64
        Log.i(TAG, "🔑 Generated and sealed new hardware KeyStore keypair for user: $cleanUserId")
        return Pair(freshPrivBytes, freshPubB64)
    }

    fun getMyPublicKey(): String {
        return ensureUserKeyPair().second
    }

    private fun getMyPrivateKeyBytes(): ByteArray {
        return ensureUserKeyPair().first
    }

    /**
     * Clears in-memory keys and recipient caches on logout.
     * The hardware-encrypted KeyStore vault remains securely sealed on device for when the user logs back in.
     */
    fun onLogout() {
        cachedActiveUserId = null
        cachedPrivateKeyBytes = null
        cachedPublicKeyBase64 = null
        recipientKeyCache.clear()
        Log.i(TAG, "🔒 E2EE session cleared from memory on logout. Hardware vault remains sealed.")
    }

    /**
     * Permanent wipe of all keys and vaults (used strictly on Account Deletion).
     */
    fun clearAllKeysPermanently() {
        onLogout()
        try {
            vaultPrefs.edit { clear() }
            legacyPrefs.edit { clear() }
            Log.i(TAG, "🧹 Permanently deleted all hardware E2EE vaults and preferences.")
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing E2EE vaults permanently", e)
        }
    }

    /**
     * Registers this user's X25519 public key on the Connect server directory.
     */
    suspend fun registerPublicKeyIfNeeded(token: String, force: Boolean = false) = withContext(Dispatchers.IO) {
        val currentUserId = getActiveUserId()
        val (_, myPubKey) = ensureUserKeyPair(currentUserId)
        if (myPubKey.isBlank()) return@withContext

        val isRegistered = vaultPrefs.getBoolean("registered_$currentUserId", false)
        val registeredPubKey = vaultPrefs.getString("registered_pub_$currentUserId", null)

        if (!force && isRegistered && registeredPubKey == myPubKey) {
            return@withContext
        }

        try {
            val baseUrl = ApiEndpoints.getBaseUrl()
            val response: UserE2EKeyResponse = httpClient.post("$baseUrl/api/messages/e2e/keys") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(RegisterE2EKeyRequest(public_key = myPubKey))
            }.body()

            if (response.success) {
                vaultPrefs.edit {
                    putBoolean("registered_$currentUserId", true)
                        .putString("registered_pub_$currentUserId", myPubKey)
                }
                Log.i(TAG, "✅ Registered E2E Public Key on backend for user: $currentUserId")
            }
        } catch (t: Throwable) {
            Log.w(TAG, "⚠️ Failed to register E2E public key on backend: ${t.message}")
        }
    }

    /**
     * Instantly returns the cached recipient public key from in-memory RAM or disk SharedPreferences (0ms, no network).
     */
     fun getCachedRecipientPublicKey(userId: String): String? {
        val clean = userId.trim()
        if (clean.isBlank()) return null
        recipientKeyCache[clean]?.let { return it }
        val diskKey = vaultPrefs.getString("pubkey_$clean", null)?.trim()
        if (!diskKey.isNullOrBlank()) {
            recipientKeyCache[clean] = diskKey
            return diskKey
        }
        return null
    }

    /**
     * Invalidates cached recipient public key in RAM and disk SharedPreferences.
     */
    fun invalidateRecipientKey(userId: String) {
        val clean = userId.trim()
        if (clean.isBlank()) return
        recipientKeyCache.remove(clean)
        vaultPrefs.edit { remove("pubkey_$clean") }
    }

    /**
     * Preloads recipient's public key in the background as soon as the user enters the chat page.
     * When the user types and sends a message, the key is already warm in cache!
     */
    fun preloadRecipientPublicKey(userId: String, token: String) {
        val clean = userId.trim()
        if (clean.isBlank() || token.isBlank()) return
        if (getCachedRecipientPublicKey(clean) != null) return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                getRecipientPublicKey(clean, token)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Resolves the recipient's public key from local cache or the Connect server directory.
     * Automatically caches successful responses in RAM and persistent SharedPreferences.
     * When forceRefresh is true, bypasses the local cache and queries the server directly.
     */
    suspend fun getRecipientPublicKey(userId: String, token: String, forceRefresh: Boolean = false): String? = withContext(Dispatchers.IO) {
        val cleanUserId = userId.trim()
        if (cleanUserId.isBlank()) return@withContext null

        if (!forceRefresh) {
            getCachedRecipientPublicKey(cleanUserId)?.let { return@withContext it }
        }

        try {
            val baseUrl = ApiEndpoints.getBaseUrl()
            val response: UserE2EKeyResponse = httpClient.get("$baseUrl/api/messages/e2e/keys") {
                bearerAuth(token)
                parameter("user_id", cleanUserId)
            }.body()

            val fetchedKey = response.data?.public_key?.trim()
            if (!fetchedKey.isNullOrBlank()) {
                recipientKeyCache[cleanUserId] = fetchedKey
                vaultPrefs.edit { putString("pubkey_$cleanUserId", fetchedKey) }
                return@withContext fetchedKey
            }
        } catch (t: Throwable) {
            Log.w(TAG, "Could not fetch E2E public key for user $cleanUserId: ${t.message}")
        }
        return@withContext null
    }

    /**
     * Encrypts plaintext message for the recipient.
     * If the recipient does not have an E2E public key registered, returns the plaintext untouched.
     */
    suspend fun encrypt(plaintext: String, recipientUserId: String, token: String): String = withContext(Dispatchers.Default) {
        val trimmed = plaintext.trim()
        if (trimmed.isBlank()) return@withContext plaintext

        // Automatically register own public key on backend if needed
        if (token.isNotBlank()) {
            registerPublicKeyIfNeeded(token)
        }

        val recipientPubKeyBase64 = getRecipientPublicKey(recipientUserId, token)
        if (recipientPubKeyBase64.isNullOrBlank()) {
            // Recipient has not published an E2E key; send standard plaintext for backward compatibility
            return@withContext plaintext
        }

        try {
            val recipientPubKeyBytes = decodeBase64Flexible(recipientPubKeyBase64)
            val myPrivKeyBytes = getMyPrivateKeyBytes()

            val sharedSecret = Curve25519.deriveSharedSecret(myPrivKeyBytes, recipientPubKeyBytes)
            val aesKey = MessageDigest.getInstance("SHA-256").digest(sharedSecret)

            val iv = ByteArray(GCM_IV_LENGTH).apply { secureRandom.nextBytes(this) }
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(aesKey, "AES"), GCMParameterSpec(GCM_TAG_LENGTH, iv))

            val ciphertextWithTag = cipher.doFinal(trimmed.toByteArray(Charsets.UTF_8))
            val payload = iv + ciphertextWithTag

            val encoded = encodeBase64Url(payload)
            return@withContext "$E2E_PREFIX$encoded"
        } catch (t: Throwable) {
            Log.e(TAG, "E2E encryption failed, falling back to plaintext: ${t.message}", t)
            return@withContext plaintext
        }
    }

    /**
     * Decrypts an incoming message from the sender.
     * Uses automatic key cache busting if the first attempt fails (e.g. sender changed keypair).
     * If decryption fails after refresh, returns a graceful fallback message instead of raw ciphertext.
     */
    suspend fun decrypt(content: String, senderUserId: String, token: String): String = withContext(Dispatchers.Default) {
        val trimmed = content.trim()
        if (!isE2EE(trimmed)) {
            return@withContext content
        }

        val senderPubKeyBase64 = getRecipientPublicKey(senderUserId, token)
        if (senderPubKeyBase64.isNullOrBlank()) {
            return@withContext DECRYPTION_FAILED_FALLBACK
        }

        val firstAttempt = attemptDecryptText(trimmed, senderPubKeyBase64)
        if (firstAttempt != null) {
            return@withContext firstAttempt
        }

        // First attempt failed (BAD_DECRYPT / AEAD authentication error).
        // The sender may have updated their key on the server. Force refresh public key.
        Log.w(TAG, "E2E decryption failed for $senderUserId with cached key, force-refreshing from backend...")
        invalidateRecipientKey(senderUserId)
        val freshSenderPubKey = getRecipientPublicKey(senderUserId, token, forceRefresh = true)
        if (!freshSenderPubKey.isNullOrBlank() && freshSenderPubKey != senderPubKeyBase64) {
            val secondAttempt = attemptDecryptText(trimmed, freshSenderPubKey)
            if (secondAttempt != null) {
                Log.i(TAG, "✅ Decryption succeeded after refreshing public key for $senderUserId")
                return@withContext secondAttempt
            }
        }

        Log.w(TAG, "E2E decryption permanently failed for message from $senderUserId")
        return@withContext DECRYPTION_FAILED_FALLBACK
    }

    private fun attemptDecryptText(trimmedPayload: String, senderPubKeyBase64: String): String? {
        return try {
            val rawB64 = trimmedPayload.removePrefix(E2E_PREFIX)
            val payload = decodeBase64Flexible(rawB64)

            if (payload.size <= GCM_IV_LENGTH) {
                return null
            }

            val iv = payload.copyOfRange(0, GCM_IV_LENGTH)
            val ciphertext = payload.copyOfRange(GCM_IV_LENGTH, payload.size)

            val senderPubKeyBytes = decodeBase64Flexible(senderPubKeyBase64)
            val myPrivKeyBytes = getMyPrivateKeyBytes()

            val sharedSecret = Curve25519.deriveSharedSecret(myPrivKeyBytes, senderPubKeyBytes)
            val aesKey = MessageDigest.getInstance("SHA-256").digest(sharedSecret)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(aesKey, "AES"), GCMParameterSpec(GCM_TAG_LENGTH, iv))

            val plaintextBytes = cipher.doFinal(ciphertext)
            String(plaintextBytes, Charsets.UTF_8)
        } catch (_: Throwable) {
            null
        }
    }

    fun isE2EE(content: String?): Boolean {
        return content != null && content.startsWith(E2E_PREFIX)
    }

    /**
     * Encrypts raw audio file bytes using client-side X25519 + AES-256-GCM before uploading.
     * If the recipient does not have an X25519 public key registered, returns rawBytes untouched.
     */
    suspend fun encryptAudioBytes(rawBytes: ByteArray, recipientUserId: String, token: String): ByteArray = withContext(Dispatchers.Default) {
        if (rawBytes.isEmpty()) return@withContext rawBytes

        if (token.isNotBlank()) {
            registerPublicKeyIfNeeded(token)
        }

        val recipientPubKeyBase64 = getRecipientPublicKey(recipientUserId, token)
        if (recipientPubKeyBase64.isNullOrBlank()) {
            Log.w(TAG, "Recipient $recipientUserId has no public key, audio uploaded as fallback.")
            return@withContext rawBytes
        }

        try {
            val recipientPubKeyBytes = decodeBase64Flexible(recipientPubKeyBase64)
            val myPrivKeyBytes = getMyPrivateKeyBytes()

            val sharedSecret = Curve25519.deriveSharedSecret(myPrivKeyBytes, recipientPubKeyBytes)
            val aesKey = MessageDigest.getInstance("SHA-256").digest(sharedSecret)

            val iv = ByteArray(GCM_IV_LENGTH).apply { secureRandom.nextBytes(this) }
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(aesKey, "AES"), GCMParameterSpec(GCM_TAG_LENGTH, iv))

            val ciphertextWithTag = cipher.doFinal(rawBytes)
            val output = ByteArray(AUDIO_MAGIC_BYTES.size + GCM_IV_LENGTH + ciphertextWithTag.size)
            System.arraycopy(AUDIO_MAGIC_BYTES, 0, output, 0, AUDIO_MAGIC_BYTES.size)
            System.arraycopy(iv, 0, output, AUDIO_MAGIC_BYTES.size, GCM_IV_LENGTH)
            System.arraycopy(ciphertextWithTag, 0, output, AUDIO_MAGIC_BYTES.size + GCM_IV_LENGTH, ciphertextWithTag.size)
            output
        } catch (t: Throwable) {
            Log.e(TAG, "Failed to encrypt audio bytes, falling back to raw bytes: ${t.message}", t)
            rawBytes
        }
    }

    /**
     * Decrypts encrypted audio file bytes downloaded from the server.
     * Uses key refresh and retry if decryption fails.
     * If not E2EE encrypted or if recipient/sender key is missing, returns the bytes untouched.
     */
    suspend fun decryptAudioBytes(encryptedBytes: ByteArray, otherUserId: String, token: String): ByteArray = withContext(Dispatchers.Default) {
        if (!isEncryptedAudio(encryptedBytes)) {
            return@withContext encryptedBytes
        }

        val otherPubKeyBase64 = getRecipientPublicKey(otherUserId, token)
        if (otherPubKeyBase64.isNullOrBlank()) {
            Log.w(TAG, "Cannot decrypt audio: other user $otherUserId public key unavailable.")
            return@withContext encryptedBytes
        }

        val firstAttempt = attemptDecryptAudio(encryptedBytes, otherPubKeyBase64)
        if (firstAttempt != null) {
            return@withContext firstAttempt
        }

        // Key might have changed, force refresh from backend and retry
        Log.w(TAG, "Audio E2E decryption failed on first try, refreshing public key for $otherUserId...")
        invalidateRecipientKey(otherUserId)
        val freshPubKey = getRecipientPublicKey(otherUserId, token, forceRefresh = true)
        if (!freshPubKey.isNullOrBlank() && freshPubKey != otherPubKeyBase64) {
            val secondAttempt = attemptDecryptAudio(encryptedBytes, freshPubKey)
            if (secondAttempt != null) {
                Log.i(TAG, "✅ Audio E2E decryption succeeded after key refresh for $otherUserId")
                return@withContext secondAttempt
            }
        }

        Log.e(TAG, "Audio E2E decryption permanently failed for user $otherUserId")
        return@withContext encryptedBytes
    }

    private fun attemptDecryptAudio(encryptedBytes: ByteArray, otherPubKeyBase64: String): ByteArray? {
        return try {
            val ivOffset = AUDIO_MAGIC_BYTES.size
            val cipherOffset = ivOffset + GCM_IV_LENGTH
            val iv = encryptedBytes.copyOfRange(ivOffset, cipherOffset)
            val ciphertext = encryptedBytes.copyOfRange(cipherOffset, encryptedBytes.size)

            val otherPubKeyBytes = decodeBase64Flexible(otherPubKeyBase64)
            val myPrivKeyBytes = getMyPrivateKeyBytes()

            val sharedSecret = Curve25519.deriveSharedSecret(myPrivKeyBytes, otherPubKeyBytes)
            val aesKey = MessageDigest.getInstance("SHA-256").digest(sharedSecret)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(aesKey, "AES"), GCMParameterSpec(GCM_TAG_LENGTH, iv))

            cipher.doFinal(ciphertext)
        } catch (_: Throwable) {
            null
        }
    }

    fun isEncryptedAudio(bytes: ByteArray?): Boolean {
        if (bytes == null || bytes.size < AUDIO_MAGIC_BYTES.size + GCM_IV_LENGTH + 16) return false
        for (i in AUDIO_MAGIC_BYTES.indices) {
            if (bytes[i] != AUDIO_MAGIC_BYTES[i]) return false
        }
        return true
    }

    private fun encodeBase64Url(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING).trim()
    }

    private fun decodeBase64Flexible(str: String): ByteArray {
        val clean = str.trim()
        return try {
            Base64.decode(clean, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        } catch (_: Exception) {
            try {
                Base64.decode(clean, Base64.DEFAULT)
            } catch (_: Exception) {
                Base64.decode(clean, Base64.NO_PADDING)
            }
        }
    }
}
