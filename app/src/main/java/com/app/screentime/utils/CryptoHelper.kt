package com.app.screentime.utils

import android.security.KeyStoreException
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import android.util.Base64
import android.util.Log
import com.app.screentime.BuildConfig
import com.app.screentime.core.network.config.AppSecrets
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import javax.crypto.spec.SecretKeySpec

object CryptoHelper {

    private val KEY_ALIAS = AppSecrets.Encryption.KEY_ALIAS
    private val TRANSFORMATION = AppSecrets.Encryption.TRANSFORMATION
    private val ANDROID_KEYSTORE = AppSecrets.Encryption.ANDROID_KEYSTORE

    init {
        try {
            generateSecretKey()
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e("CryptoHelper", "Failed to generate secret key during initialization", e)
            }
            // Silently handle initialization errors - key will be generated on first use
        }
    }

    private fun generateSecretKey() {
        try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

            if (keyStore.containsAlias(KEY_ALIAS)) {
                // Verify the key is valid by trying to get it
                try {
                    keyStore.getKey(KEY_ALIAS, null)
                } catch (e: KeyStoreException) {
                    // Key exists but is corrupted, delete it and regenerate
                    if (BuildConfig.DEBUG) {
                        Log.w("CryptoHelper", "Key exists but is corrupted, deleting and regenerating", e)
                    }
                    keyStore.deleteEntry(KEY_ALIAS)
                }
            }

            // Generate new key if it doesn't exist or was deleted
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                val keyGenerator =
                    KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
                keyGenerator.init(
                    KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setUserAuthenticationRequired(false)
                        .build()
                )
                keyGenerator.generateKey()
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e("CryptoHelper", "Error in generateSecretKey", e)
            }
            throw e
        }
    }

    private fun getSecretKey(): SecretKey {
        return try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)
            val key = keyStore.getKey(KEY_ALIAS, null)
            if (key == null) {
                // Key doesn't exist, generate it
                generateSecretKey()
                keyStore.load(null)
                keyStore.getKey(KEY_ALIAS, null) as SecretKey
            } else {
                key as SecretKey
            }
        } catch (e: KeyStoreException) {
            if (BuildConfig.DEBUG) {
                Log.e("CryptoHelper", "KeyStoreException in getSecretKey, attempting to regenerate", e)
            }
            // Try to delete corrupted key and regenerate
            try {
                val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
                keyStore.load(null)
                if (keyStore.containsAlias(KEY_ALIAS)) {
                    keyStore.deleteEntry(KEY_ALIAS)
                }
                generateSecretKey()
                keyStore.load(null)
                keyStore.getKey(KEY_ALIAS, null) as SecretKey
            } catch (ex: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.e("CryptoHelper", "Failed to regenerate key", ex)
                }
                throw RuntimeException("Failed to access encryption key", ex)
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e("CryptoHelper", "Unexpected error in getSecretKey", e)
            }
            throw RuntimeException("Failed to access encryption key", e)
        }
    }

    fun encrypt(data: String): String {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val iv = cipher.iv
            val encryptedBytes = cipher.doFinal(data.toByteArray())
            val combined = iv + encryptedBytes // Store IV + Cipher text
            Base64.encodeToString(combined, Base64.DEFAULT)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e("CryptoHelper", "Encryption failed", e)
            }
            throw RuntimeException("Encryption failed", e)
        }
    }

    fun decrypt(encryptedData: String): String {
        return try {
            val decoded = Base64.decode(encryptedData, Base64.DEFAULT)
            if (decoded.size < 12) {
                throw IllegalArgumentException("Invalid encrypted data: too short")
            }
            val iv = decoded.copyOfRange(0, 12)
            val cipherBytes = decoded.copyOfRange(12, decoded.size)

            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))

            String(cipher.doFinal(cipherBytes))
        } catch (e: KeyStoreException) {
            if (BuildConfig.DEBUG) {
                Log.e("CryptoHelper", "KeyStoreException during decryption - key may be corrupted", e)
            }
            // Try to regenerate key and retry once
            try {
                val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
                keyStore.load(null)
                if (keyStore.containsAlias(KEY_ALIAS)) {
                    keyStore.deleteEntry(KEY_ALIAS)
                }
                generateSecretKey()
                // Retry decryption (but this will likely fail since data was encrypted with old key)
                val decoded = Base64.decode(encryptedData, Base64.DEFAULT)
                val iv = decoded.copyOfRange(0, 12)
                val cipherBytes = decoded.copyOfRange(12, decoded.size)
                val cipher = Cipher.getInstance(TRANSFORMATION)
                cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))
                String(cipher.doFinal(cipherBytes))
            } catch (ex: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.e("CryptoHelper", "Decryption failed after key regeneration", ex)
                }
                throw RuntimeException("Decryption failed - data may have been encrypted with a different key", ex)
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e("CryptoHelper", "Decryption failed", e)
            }
            throw RuntimeException("Decryption failed", e)
        }
    }
}


object EncryptionManager {

    val ALGORITHM = AppSecrets.Encryption.TRANSFORMATION
    private val KEY_SIZE = AppSecrets.Encryption.KEY_SIZE // 256-bit key
    val IV_SIZE = AppSecrets.Encryption.IV_SIZE  // GCM standard

    // You should store this key securely, not hardcode
    private val secretKey: SecretKey = generateRandomKey()

    private fun generateRandomKey(): SecretKey {
        val key = ByteArray(KEY_SIZE)
        SecureRandom().nextBytes(key)
        return SecretKeySpec(key, "AES")
    }

    private inline fun <reified T> encrypt(data: T): String {
        val json = Json.encodeToString(data)
        val cipher = Cipher.getInstance(ALGORITHM)

        val iv = ByteArray(IV_SIZE)
        SecureRandom().nextBytes(iv)

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(128, iv))

        val encryptedBytes = cipher.doFinal(json.toByteArray(Charsets.UTF_8))

        val combined = iv + encryptedBytes
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    private inline fun <reified T> decrypt(encryptedData: String): T {
        val decoded = Base64.decode(encryptedData, Base64.DEFAULT)

        val iv = decoded.copyOfRange(0, IV_SIZE)
        val encryptedBytes = decoded.copyOfRange(IV_SIZE, decoded.size)

        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))

        val json = cipher.doFinal(encryptedBytes).toString(Charsets.UTF_8)

        return Json.decodeFromString(json)
    }
}
