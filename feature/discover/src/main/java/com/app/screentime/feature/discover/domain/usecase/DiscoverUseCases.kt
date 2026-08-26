package com.app.screentime.feature.discover.domain.usecase

import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.dto.toModelProfile
import com.app.screentime.core.network.session.SessionManager
import javax.inject.Inject

class GetModelsUseCase @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(): List<ModelProfile> {
        return getPaginated(page = 1, pageSize = 20)
    }

    suspend fun getPaginated(page: Int, pageSize: Int = 20): List<ModelProfile> {
        val token = sessionManager.token ?: ""
        return try {
            val response = api.getModels(token)
            response.models.map { it.toModelProfile() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

class GetModelDetailsUseCase @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(modelId: String): ModelProfile? {
        val token = sessionManager.token ?: ""
        return try {
            val userDto = api.getModelProfile(token, modelId)
            userDto.toModelProfile()
        } catch (e: Exception) {
            null
        }
    }
}

class ToggleFavoriteUseCase @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(modelId: String, isFav: Boolean): Boolean {
        val token = sessionManager.token ?: return !isFav
        return try {
            if (isFav) {
                api.removeFavorite(token, modelId)
                false
            } else {
                api.addFavorite(token, modelId)
                true
            }
        } catch (e: Exception) {
            !isFav
        }
    }
}
