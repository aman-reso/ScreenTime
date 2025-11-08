package com.app.screentime.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import android.util.Base64
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import javax.crypto.spec.SecretKeySpec

object CryptoHelper {

    private const val KEY_ALIAS = "device_encryption_key"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"

    init {
        generateSecretKey()
    }

    private fun generateSecretKey() {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

        if (keyStore.containsAlias(KEY_ALIAS)) return

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

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        val combined = iv + encryptedBytes // Store IV + Cipher text
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    fun decrypt(encryptedData: String): String {
        val decoded = Base64.decode(encryptedData, Base64.DEFAULT)
        val iv = decoded.copyOfRange(0, 12)
        val cipherBytes = decoded.copyOfRange(12, decoded.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))

        return String(cipher.doFinal(cipherBytes))
    }
}


object EncryptionManager {

    const val ALGORITHM = "AES/GCM/NoPadding"
    private const val KEY_SIZE = 32 // 256-bit key
    const val IV_SIZE = 12  // GCM standard

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
