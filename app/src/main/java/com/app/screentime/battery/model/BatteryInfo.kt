package com.app.screentime.battery.model

/**
 * Battery information data model
 */
data class BatteryInfo(
    val level: Int, // Battery percentage (0-100)
    val isCharging: Boolean,
    val chargingStatus: ChargingStatus,
    val estimatedTimeRemaining: Long? = null, // in milliseconds, null if charging or unknown
    val estimatedTimeToFull: Long? = null, // in milliseconds when charging, null if not charging
    val voltage: Int = 0, // in millivolts
    val temperature: Float = 0f, // in Celsius
    val currentNow: Int = 0, // in microamperes (negative when discharging)
    val dischargingRate: Float = 0f, // in mAh (calculated from current)
    val health: BatteryHealth
)

/**
 * Battery charging status
 */
enum class ChargingStatus {
    NOT_CHARGING,
    CHARGING,
    CHARGING_VIA_USB,
    CHARGING_VIA_AC,
    CHARGING_VIA_WIRELESS,
    FULL
}

/**
 * Battery health status
 */
enum class BatteryHealth {
    GOOD,
    OVERHEAT,
    DEAD,
    OVER_VOLTAGE,
    UNSPECIFIED_FAILURE,
    COLD,
    UNKNOWN
}

