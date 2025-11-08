package com.app.screentime.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class to retrieve comprehensive device information
 * Provides device ID, manufacturer, model, and other device details
 */
@Singleton
class DeviceInfoUtils @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Data class containing all device information
     */

    @Serializable
    data class DeviceInfo(
        val deviceId: String,
        val manufacturer: String,
        val model: String,
        val brand: String,
        val product: String,
        val device: String,
        val hardware: String,
        val androidVersion: String,
        val sdkVersion: Int,
    )

    fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            deviceId = getDeviceId(),
            manufacturer = getManufacturer(),
            model = getModel(),
            brand = getBrand(),
            product = getProduct(),
            device = getDevice(),
            hardware = getHardware(),
            androidVersion = getAndroidVersion(),
            sdkVersion = getSdkVersion()
        )
    }

    /**
     * Get Android Device ID (ANDROID_ID)
     * This is a unique identifier for the device
     */
    @SuppressLint("HardwareIds")
    private fun getDeviceId(): String {
        return try {
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            ) ?: "unknown_device"
        } catch (e: Exception) {
            UUID.randomUUID().toString()
        }
    }

    /**
     * Get device manufacturer (e.g., "Samsung", "Google", "OnePlus")
     */
    private fun getManufacturer(): String {
        return Build.MANUFACTURER ?: "unknown"
    }

    /**
     * Get device model (e.g., "SM-G950F", "Pixel 5")
     */
    private fun getModel(): String {
        return Build.MODEL ?: "unknown"
    }

    /**
     * Get device brand (e.g., "samsung", "google")
     */
    private fun getBrand(): String {
        return Build.BRAND ?: "unknown"
    }

    /**
     * Get product name
     */
    private fun getProduct(): String {
        return Build.PRODUCT ?: "unknown"
    }

    /**
     * Get device name
     */
    private fun getDevice(): String {
        return Build.DEVICE ?: "unknown"
    }

    /**
     * Get hardware name
     */
    private fun getHardware(): String {
        return Build.HARDWARE ?: "unknown"
    }

    /**
     * Get Android version string (e.g., "13", "12")
     */
    private fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE ?: "unknown"
    }

    /**
     * Get SDK version (API level)
     */
    fun getSdkVersion(): Int {
        return Build.VERSION.SDK_INT
    }


    /**
     * Get total device memory (RAM) in bytes, if available
     */
    fun getTotalMemory(): Long? {
        return try {
            val memInfo = android.app.ActivityManager.MemoryInfo()
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as? android.app.ActivityManager
            activityManager?.getMemoryInfo(memInfo)
            memInfo.totalMem
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get primary CPU ABI
     */
    fun getCpuAbi(): String {
        return Build.SUPPORTED_ABIS[0] ?: "unknown"
    }

    /**
     * Get secondary CPU ABI if available
     */
    fun getCpuAbi2(): String? {
        return if (Build.SUPPORTED_ABIS.size > 1) {
            Build.SUPPORTED_ABIS[1]
        } else {
            null
        }
    }

    /**
     * Get build fingerprint
     */
    fun getFingerprint(): String {
        return Build.FINGERPRINT ?: "unknown"
    }

}

