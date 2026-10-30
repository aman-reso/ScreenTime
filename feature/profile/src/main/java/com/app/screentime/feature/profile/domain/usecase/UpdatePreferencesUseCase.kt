package com.app.screentime.feature.profile.domain.usecase

import com.app.screentime.core.network.dto.ProfileUpdateRequest
import com.app.screentime.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdatePreferencesUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(request: ProfileUpdateRequest): Result<Boolean> {
        return repository.updateProfile(request)
    }
}
