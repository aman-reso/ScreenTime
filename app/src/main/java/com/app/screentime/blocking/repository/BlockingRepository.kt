package com.app.screentime.blocking.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.app.screentime.blocking.model.BlockingRule
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

@Serializable
data class BlockingRuleData(
    val packageName: String,
    val appName: String,
    val ruleType: String, // "instant", "launch", "duration"
    val maxLaunches: Int? = null,
    val currentLaunches: Int = 0,
    val maxDurationMinutes: Int? = null,
    val currentDurationMinutes: Long = 0L
)

class BlockingRepository(private val context: Context) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("blocking_rules", Context.MODE_PRIVATE)
    }

    private val json = Json { ignoreUnknownKeys = true }

    fun saveRule(rule: BlockingRule) {
        val ruleData = when (rule) {
            is BlockingRule.InstantBlock -> BlockingRuleData(
                packageName = rule.packageName,
                appName = rule.appName,
                ruleType = "instant"
            )
            is BlockingRule.LaunchBasedBlock -> BlockingRuleData(
                packageName = rule.packageName,
                appName = rule.appName,
                ruleType = "launch",
                maxLaunches = rule.maxLaunches,
                currentLaunches = rule.currentLaunches
            )
            is BlockingRule.DurationBasedBlock -> BlockingRuleData(
                packageName = rule.packageName,
                appName = rule.appName,
                ruleType = "duration",
                maxDurationMinutes = rule.maxDurationMinutes,
                currentDurationMinutes = rule.currentDurationMinutes
            )
        }

        prefs.edit {
            putString("rule_${ruleData.packageName}", json.encodeToString(ruleData))
        }
    }

    fun getRule(packageName: String): BlockingRule? {
        val jsonString = prefs.getString("rule_$packageName", null) ?: return null
        val ruleData = json.decodeFromString<BlockingRuleData>(jsonString)

        return when (ruleData.ruleType) {
            "instant" -> BlockingRule.InstantBlock(
                packageName = ruleData.packageName,
                appName = ruleData.appName
            )
            "launch" -> BlockingRule.LaunchBasedBlock(
                packageName = ruleData.packageName,
                appName = ruleData.appName,
                maxLaunches = ruleData.maxLaunches ?: 0,
                currentLaunches = ruleData.currentLaunches
            )
            "duration" -> BlockingRule.DurationBasedBlock(
                packageName = ruleData.packageName,
                appName = ruleData.appName,
                maxDurationMinutes = ruleData.maxDurationMinutes ?: 0,
                currentDurationMinutes = ruleData.currentDurationMinutes
            )
            else -> null
        }
    }

    fun getAllRules(): List<BlockingRule> {
        val allRules = mutableListOf<BlockingRule>()
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

    fun updateLaunchCount(packageName: String, count: Int) {
        getRule(packageName)?.let { rule ->
            if (rule is BlockingRule.LaunchBasedBlock) {
                val updatedRule = rule.copy(currentLaunches = count)
                saveRule(updatedRule)
            }
        }
    }

    fun updateDuration(packageName: String, durationMinutes: Long) {
        getRule(packageName)?.let { rule ->
            if (rule is BlockingRule.DurationBasedBlock) {
                val updatedRule = rule.copy(currentDurationMinutes = durationMinutes)
                saveRule(updatedRule)
            }
        }
    }
}

