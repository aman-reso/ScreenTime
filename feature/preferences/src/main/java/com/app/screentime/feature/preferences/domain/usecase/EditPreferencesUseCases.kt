package com.app.screentime.feature.preferences.domain.usecase

import com.app.screentime.feature.preferences.domain.model.UserPreferences
import com.app.screentime.feature.preferences.domain.repository.EditPreferencesRepository
import javax.inject.Inject

class GetEditPreferencesUseCase @Inject constructor(
    private val repository: EditPreferencesRepository
) {
    suspend operator fun invoke(): Result<UserPreferences> = repository.getPreferences()
}

class SaveEditPreferencesUseCase @Inject constructor(
    private val repository: EditPreferencesRepository
) {
    suspend operator fun invoke(preferences: UserPreferences): Result<Unit> = repository.savePreferences(preferences)
}

class ResetEditPreferencesUseCase @Inject constructor(
    private val repository: EditPreferencesRepository
) {
    suspend operator fun invoke(): Result<UserPreferences> = repository.resetPreferences()
}
