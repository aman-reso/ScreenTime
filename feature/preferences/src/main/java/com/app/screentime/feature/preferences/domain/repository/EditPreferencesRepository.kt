package com.app.screentime.feature.preferences.domain.repository

import com.app.screentime.feature.preferences.domain.model.UserPreferences

/**
 * Repository interface for managing user preferences.
 */
interface EditPreferencesRepository {
    suspend fun getPreferences(): Result<UserPreferences>
    suspend fun savePreferences(preferences: UserPreferences): Result<Unit>
    suspend fun resetPreferences(): Result<UserPreferences>
}
