package com.app.screentime.core.network.model

import com.app.screentime.core.network.utils.DeviceInfoUtils
import kotlinx.serialization.Serializable

/**
 * Device registration request
 */
@Serializable
data class DeviceRegistrationRequest(
    val deviceInfo: DeviceInfoUtils.DeviceInfo,
    val firebaseToken: String? = null
)
