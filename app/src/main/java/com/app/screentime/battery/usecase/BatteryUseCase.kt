package com.app.screentime.battery.usecase

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import com.app.screentime.battery.model.BatteryHealth
import com.app.screentime.battery.model.BatteryInfo
import com.app.screentime.battery.model.ChargingStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for retrieving battery information from the device
 */
@Singleton
class BatteryUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    /**
     * Get current battery information
     */
    fun getBatteryInfo(): BatteryInfo {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            ?: return getDefaultBatteryInfo()
        
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = if (level >= 0 && scale > 0) {
            (level * 100 / scale.toFloat()).toInt()
        } else {
            0
        }
        
        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
        
        val chargingStatus = when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> {
                val chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                when (chargePlug) {
                    BatteryManager.BATTERY_PLUGGED_USB -> ChargingStatus.CHARGING_VIA_USB
                    BatteryManager.BATTERY_PLUGGED_AC -> ChargingStatus.CHARGING_VIA_AC
                    BatteryManager.BATTERY_PLUGGED_WIRELESS -> ChargingStatus.CHARGING_VIA_WIRELESS
                    else -> ChargingStatus.CHARGING
                }
            }
            BatteryManager.BATTERY_STATUS_FULL -> ChargingStatus.FULL
            else -> ChargingStatus.NOT_CHARGING
        }
        
        val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
        val batteryHealth = when (health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> BatteryHealth.GOOD
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> BatteryHealth.OVERHEAT
            BatteryManager.BATTERY_HEALTH_DEAD -> BatteryHealth.DEAD
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> BatteryHealth.OVER_VOLTAGE
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> BatteryHealth.UNSPECIFIED_FAILURE
            BatteryManager.BATTERY_HEALTH_COLD -> BatteryHealth.COLD
            else -> BatteryHealth.UNKNOWN
        }
        
        val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10f
        
        // Get current (available on API 21+)
        var currentNow = 0
        var dischargingRate = 0f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            currentNow = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
            // Convert microamperes to milliamperes and calculate discharging rate
            if (currentNow < 0) {
                dischargingRate = Math.abs(currentNow / 1000f) // Convert to mA
            }
        }
        
        // Estimate time remaining (rough calculation)
        val estimatedTimeRemaining = if (!isCharging && currentNow < 0 && batteryPct > 0) {
            // Calculate based on current discharge rate
            val remainingCapacity = (batteryPct / 100f) * 3000f // Assume 3000mAh capacity (rough estimate)
            val hoursRemaining = remainingCapacity / dischargingRate
            (hoursRemaining * 3600000).toLong() // Convert to milliseconds
        } else null
        
        val estimatedTimeToFull = if (isCharging && currentNow > 0 && batteryPct < 100) {
            val remainingCapacity = ((100 - batteryPct) / 100f) * 3000f
            val chargingRate = currentNow / 1000f
            val hoursToFull = remainingCapacity / chargingRate
            (hoursToFull * 3600000).toLong()
        } else null
        
        return BatteryInfo(
            level = batteryPct,
            isCharging = isCharging,
            chargingStatus = chargingStatus,
            estimatedTimeRemaining = estimatedTimeRemaining,
            estimatedTimeToFull = estimatedTimeToFull,
            voltage = voltage,
            temperature = temperature,
            currentNow = currentNow,
            dischargingRate = dischargingRate,
            health = batteryHealth
        )
    }
    
    private fun getDefaultBatteryInfo(): BatteryInfo {
        return BatteryInfo(
            level = 0,
            isCharging = false,
            chargingStatus = ChargingStatus.NOT_CHARGING,
            health = BatteryHealth.UNKNOWN
        )
    }
}

