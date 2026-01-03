package com.app.screentime.applock.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.app.screentime.applock.model.AppLockRule
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class AppLockRuleData(
    val packageName: String,
    val appName: String,
    val isLocked: Boolean = true
)

@Singleton
class AppLockRepository @Inject constructor(private val context: Context) {
    private val prefs: SharedPreferences by lazy {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                "app_lock_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            context.getSharedPreferences("app_lock_prefs", Context.MODE_PRIVATE)
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    private val PIN_KEY = "app_lock_pin"
    private val PIN_SET_KEY = "app_lock_pin_set"

    fun saveRule(rule: AppLockRule) {
        val ruleData = AppLockRuleData(
            packageName = rule.packageName,
            appName = rule.appName,
            isLocked = rule.isLocked
        )

        prefs.edit {
            putString("rule_${rule.packageName}", json.encodeToString(ruleData))
        }
    }

    fun getRule(packageName: String): AppLockRule? {
        val jsonString = prefs.getString("rule_$packageName", null) ?: return null
        val ruleData = json.decodeFromString<AppLockRuleData>(jsonString)

        return AppLockRule(
            packageName = ruleData.packageName,
            appName = ruleData.appName,
            isLocked = ruleData.isLocked
        )
    }

    fun getAllRules(): List<AppLockRule> {
        val allRules = mutableListOf<AppLockRule>()
        prefs.all.keys.filter { it.startsWith("rule_") }.forEach { key ->
            val packageName = key.removePrefix("rule_")
            getRule(packageName)?.let { allRules.add(it) }
        }
        return allRules
    }

    fun deleteRule(packageName: String) {
        prefs.edit {
            remove("rule_$packageName")
        }
    }

    fun savePIN(pin: String) {
        prefs.edit {
            putString(PIN_KEY, pin)
            putBoolean(PIN_SET_KEY, true)
        }
    }

    fun getPIN(): String? {
        return prefs.getString(PIN_KEY, null)
    }

    fun isPINSet(): Boolean {
        return prefs.getBoolean(PIN_SET_KEY, false)
    }

    fun verifyPIN(pin: String): Boolean {
        val storedPIN = getPIN()
        return storedPIN != null && storedPIN == pin
    }

    fun clearPIN() {
        prefs.edit {
            remove(PIN_KEY)
            putBoolean(PIN_SET_KEY, false)
        }
    }
}

