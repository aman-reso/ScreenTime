package com.app.screentime.feature.auth.domain.usecase

import com.app.screentime.core.model.User
import java.util.UUID
import javax.inject.Inject

/**
 * Direct guest login use case generating a seamless anonymous session.
 */
class GuestLoginUseCase @Inject constructor(
    private val loginUseCase: LoginUseCase
) {
    suspend operator fun invoke(): Result<User> {
        val uniqueSuffix = UUID.randomUUID().toString().replace("-", "").take(8)
        val guestPhone = "guest_$uniqueSuffix"
        val guestName = "Guest $uniqueSuffix"
        return loginUseCase(guestPhone, guestName, "user")
    }
}
