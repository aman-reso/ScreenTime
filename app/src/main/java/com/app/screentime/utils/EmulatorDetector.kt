package com.app.screentime.utils

import android.os.Build

/**
 * Detects if the app is running on an Android emulator.
 * Used in release builds to block app usage on emulators.
 */
object EmulatorDetector {

    /**
     * Returns true if the app is running on an emulator.
     * Based on Build properties and ro.kernel.qemu system property.
     */
    @JvmStatic
    fun isEmulator(): Boolean {
        if (isEmulatorByBuild()) return true
        if (isEmulatorByQemu()) return true
        return false
    }

    private fun isEmulatorByBuild(): Boolean {
        val fp = Build.FINGERPRINT
        val model = Build.MODEL
        val product = Build.PRODUCT
        val hw = Build.HARDWARE
        val device = Build.DEVICE
        return fp.startsWith("generic")
            || fp.startsWith("unknown")
            || fp.contains("vbox")
            || model.contains("sdk")
            || model.contains("Emulator")
            || model.contains("Android SDK built for x86")
            || model.contains("sdk_gphone")
            || Build.MANUFACTURER.contains("Genymotion")
            || (Build.BRAND.startsWith("generic") && device.startsWith("generic"))
            || "google_sdk" == product
            || product.contains("sdk")
            || product.contains("emulator")
            || hw.contains("goldfish")
            || hw.contains("ranchu")
            || hw.contains("vbox")
            || hw.contains("vexpress")
            || hw.contains("sdk_gphone")
            || hw.contains("emulator")
            || Build.BOARD.contains("unknown")
            || device.contains("generic")
            || device.contains("vbox")
            || device.contains("emulator")
    }

    private fun isEmulatorByQemu(): Boolean {
        return try {
            val clazz = Class.forName("android.os.SystemProperties")
            val get = clazz.getMethod("get", String::class.java)
            "1" == get.invoke(null, "ro.kernel.qemu") as? String
        } catch (e: Exception) {
            false
        }
    }
}
