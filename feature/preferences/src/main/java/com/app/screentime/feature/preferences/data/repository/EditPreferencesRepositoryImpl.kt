package com.app.screentime.feature.preferences.data.repository

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.dto.UpdatePreferencesRequest
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.feature.preferences.domain.model.UserPreferences
import com.app.screentime.feature.preferences.domain.repository.EditPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class EditPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionManager: SessionManager,
    private val api: WinterApi
) : EditPreferencesRepository {

    companion object {
        private const val TAG = "EditPreferencesRepo"
        private const val PREFS_NAME = "user_edit_preferences"
        private const val KEY_SHOW_ME = "pref_show_me"
        private const val KEY_MIN_AGE = "pref_min_age"
        private const val KEY_MAX_AGE = "pref_max_age"
        private const val KEY_MAX_DISTANCE = "pref_max_distance"
        private const val KEY_GLOBAL = "pref_global"
        private const val KEY_RELIGION = "pref_religion"
        private const val KEY_HEIGHT_RANGE = "pref_height_range"
        private const val KEY_MIN_HEIGHT = "pref_min_height"
        private const val KEY_MAX_HEIGHT = "pref_max_height"
        private const val KEY_EDUCATION = "pref_education"
        private const val KEY_DRINKING = "pref_drinking"
        private const val KEY_SMOKING = "pref_smoking"
        private const val KEY_CHILDREN = "pref_children"

        fun parseHeightRange(range: String): Pair<Int?, Int?> = when (range.trim()) {
            "4ft 10in - 5ft 4in" -> 147 to 163
            "5ft 0in - 5ft 8in" -> 152 to 173
            "5ft 2in - 6ft 0in" -> 160 to 185
            "5ft 6in - 6ft 2in" -> 168 to 188
            "5ft 10in - 6ft 6in" -> 178 to 198
            "6ft 0in+" -> 183 to 220
            "No Preference" -> null to null
            else -> null to null
        }

        fun formatReligion(rel: String): String = when (rel.trim().lowercase()) {
            "hindu", "hinduism" -> "Hinduism (🕉️)"
            "muslim", "islam" -> "Islam (☪️)"
            "christian", "christianity" -> "Christianity (✝️)"
            "sikh", "sikhism" -> "Sikhism (ੴ)"
            "jain", "jainism" -> "Jainism (🛕)"
            "buddhist", "buddhism" -> "Buddhism (☸️)"
            "parsi", "parsi / zoroastrian" -> "Parsi / Zoroastrian (🕯️)"
            "jewish", "judaism" -> "Judaism (✡️)"
            "spiritual" -> "Spiritual (🕊️)"
            "agnostic" -> "Agnostic (❓)"
            "atheist" -> "Atheist (⚛️)"
            "other" -> "Other (🌟)"
            else -> rel
        }

        fun formatEducation(edu: String): String = when (edu.trim().lowercase()) {
            "bachelor's degree", "bachelor’s degree" -> "Bachelor’s degree"
            "master's degree", "master’s degree" -> "Master’s degree"
            "phd / doctorate", "doctorate" -> "Doctorate"
            "trade / tech school", "trade school" -> "Trade School"
            else -> edu
        }
    }

    private val sharedPrefs by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override suspend fun getPreferences(): Result<UserPreferences> = withContext(Dispatchers.IO) {
        try {
            val storedHeightRange = sharedPrefs.getString(KEY_HEIGHT_RANGE, "5ft 2in - 6ft 0in") ?: "5ft 2in - 6ft 0in"
            val (parsedMinH, parsedMaxH) = parseHeightRange(storedHeightRange)
            val storedMinH = if (sharedPrefs.contains(KEY_MIN_HEIGHT)) sharedPrefs.getInt(KEY_MIN_HEIGHT, 160) else (parsedMinH ?: 160)
            val storedMaxH = if (sharedPrefs.contains(KEY_MAX_HEIGHT)) sharedPrefs.getInt(KEY_MAX_HEIGHT, 185) else (parsedMaxH ?: 185)

            val prefs = UserPreferences(
                showMe = sharedPrefs.getString(KEY_SHOW_ME, "Men") ?: "Men",
                minAge = sharedPrefs.getFloat(KEY_MIN_AGE, 22f),
                maxAge = sharedPrefs.getFloat(KEY_MAX_AGE, 30f),
                maxDistanceKm = sharedPrefs.getFloat(KEY_MAX_DISTANCE, 50f),
                isGlobal = sharedPrefs.getBoolean(KEY_GLOBAL, false),
                religion = sharedPrefs.getString(KEY_RELIGION, "No Preference") ?: "No Preference",
                heightRange = storedHeightRange,
                minHeight = storedMinH,
                maxHeight = storedMaxH,
                education = sharedPrefs.getString(KEY_EDUCATION, "Any") ?: "Any",
                drinking = sharedPrefs.getString(KEY_DRINKING, "Social Drinker") ?: "Social Drinker",
                smoking = sharedPrefs.getString(KEY_SMOKING, "Never") ?: "Never",
                children = sharedPrefs.getString(KEY_CHILDREN, "Open to Kids") ?: "Open to Kids"
            )
            Result.success(prefs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun savePreferences(preferences: UserPreferences): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Save locally
            sharedPrefs.edit {
                putString(KEY_SHOW_ME, preferences.showMe)
                putFloat(KEY_MIN_AGE, preferences.minAge)
                putFloat(KEY_MAX_AGE, preferences.maxAge)
                putFloat(KEY_MAX_DISTANCE, preferences.maxDistanceKm)
                putBoolean(KEY_GLOBAL, preferences.isGlobal)
                putString(KEY_RELIGION, preferences.religion)
                putString(KEY_HEIGHT_RANGE, preferences.heightRange)
                preferences.minHeight?.let { putInt(KEY_MIN_HEIGHT, it) }
                preferences.maxHeight?.let { putInt(KEY_MAX_HEIGHT, it) }
                putString(KEY_EDUCATION, preferences.education)
                putString(KEY_DRINKING, preferences.drinking)
                putString(KEY_SMOKING, preferences.smoking)
                putString(KEY_CHILDREN, preferences.children)
            }

            // Sync with backend via dedicated PUT /api/preferences if authenticated
            val token = sessionManager.getToken()
            if (!token.isNullOrBlank()) {
                val (parsedMinH, parsedMaxH) = parseHeightRange(preferences.heightRange)
                val finalMinH = preferences.minHeight ?: parsedMinH
                val finalMaxH = preferences.maxHeight ?: parsedMaxH

                val updateRequest = UpdatePreferencesRequest(
                    show_me = preferences.showMe,
                    min_age = preferences.minAge.roundToInt(),
                    max_age = preferences.maxAge.roundToInt(),
                    distance = preferences.maxDistanceKm.roundToInt(),
                    min_height = finalMinH,
                    max_height = finalMaxH,
                    religion = formatReligion(preferences.religion),
                    education = formatEducation(preferences.education),
                    drinking = preferences.drinking,
                    smoking = preferences.smoking
                )
                try {
                    val response = api.updatePreferences(token, updateRequest)
                    if (!response.success) {
                        Log.w(TAG, "Preferences API responded with success=false: ${response.message}")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to update preferences on /api/preferences", e)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPreferences(): Result<UserPreferences> = withContext(Dispatchers.IO) {
        try {
            val defaults = UserPreferences()
            sharedPrefs.edit { clear() }
            savePreferences(defaults)
            Result.success(defaults)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
