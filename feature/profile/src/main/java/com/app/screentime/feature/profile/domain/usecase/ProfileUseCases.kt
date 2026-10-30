package com.app.screentime.feature.profile.domain.usecase

import android.net.Uri
import com.app.screentime.core.model.User
import com.app.screentime.core.network.dto.ProfileVisibilityResponseData
import com.app.screentime.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateProfileVisibilityUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(visibility: String): Result<ProfileVisibilityResponseData> {
        return repository.updateProfileVisibility(visibility)
    }
}

@Singleton
class GetCurrentUserUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(): User {
        return repository.getCurrentUser()
    }
}

@Singleton
class FetchUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String? = null): Result<User> {
        return repository.fetchUserProfile(userId)
    }
}

@Singleton
class LogoutUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}

@Singleton
class SubmitModelOnboardingUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(bio: String, voiceRate: Double, chatRate: Double): Result<Boolean> {
        return repository.submitModelOnboarding(bio, voiceRate, chatRate)
    }
}

@Singleton
class UploadImagesUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(uris: List<Uri>): Result<List<String>> {
        return repository.uploadImages(uris)
    }
}

@Singleton
class UploadVideoUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(uri: Uri): Result<String> {
        return repository.uploadVideo(uri)
    }
}

@Singleton
class DeleteAccountUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return repository.deleteAccount()
    }
}
