package com.app.screentime.applock.service

import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.app.screentime.applock.activity.AppLockOverlayActivity
import com.app.screentime.applock.manager.AppLockManager
import com.app.screentime.applock.repository.AppLockRepository
import com.app.screentime.applock.util.AppLockConstants
import java.util.Timer
import java.util.TimerTask

/**
 * Service that monitors app usage and shows lock screen for locked apps
 * Based on ExperimentalAppLockService implementation
 */
class AppLockMonitoringService : Service() {

    private val appLockRepository: AppLockRepository by lazy {
        AppLockRepository(applicationContext)
    }

    private val usageStatsManager: UsageStatsManager by lazy {
        getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
    }

    private val notificationManager: NotificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private var timer: Timer? = null
    private var previousForegroundPackage: String = ""

    companion object {
        private const val TAG = "AppLockMonitoringService"
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "app_lock_monitoring_channel"
        private const val POLLING_INTERVAL_MS = 250L
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        AppLockManager.isLockScreenShown.set(false)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service starting")

        if (!shouldStartService()) {
            Log.e(TAG, "Service should not run. Stopping.")
            stopSelf()
            return START_NOT_STICKY
        }

        startMonitoringTimer()
        startForegroundService()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        Log.d(TAG, "Service destroyed")
        AppLockManager.isLockScreenShown.set(false)
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun shouldStartService(): Boolean {
        val hasLockedApps = appLockRepository.getLockedApps().isNotEmpty()
        if (!hasLockedApps) {
            return false
        }

        // Check usage stats permission
        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        val hasUsagePermission = mode == AppOpsManager.MODE_ALLOWED

        return hasUsagePermission
    }

    // --- Monitoring ---

    private fun startMonitoringTimer() {
        timer?.cancel()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                if (isDeviceLocked()) {
                    AppLockManager.appUnlockTimes.clear()
                    return
                }

                val foregroundApp = getCurrentForegroundAppPackage() ?: return
                val currentPackage = foregroundApp.first
                val triggeringPackage = previousForegroundPackage
                previousForegroundPackage = currentPackage

                if (isExclusionApp(currentPackage)) return

                if (currentPackage == triggeringPackage) return

                checkAndLockApp(currentPackage, triggeringPackage, System.currentTimeMillis())
            }
        }, 0, POLLING_INTERVAL_MS)
    }

    private fun isExclusionApp(packageName: String): Boolean {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val keyboardPackages = inputMethodManager
            ?.enabledInputMethodList
            ?.map { it.packageName }
            ?: emptyList()

        return packageName == this.packageName ||
                packageName in keyboardPackages ||
                packageName in AppLockConstants.EXCLUDED_APPS
    }

    /**
     * Returns the foreground package name and class name, or null if filtered.
     */
    private fun getCurrentForegroundAppPackage(): Pair<String, String>? {
        val time = System.currentTimeMillis()
        val events = usageStatsManager.queryEvents(time - 1000 * 100, time)
        val event = UsageEvents.Event()
        var recentApp: Pair<String, String>? = null

        while (events.hasNextEvent()) {
            events.getNextEvent(event)

            if (event.eventType != UsageEvents.Event.ACTIVITY_RESUMED) continue
            if (event.className == AppLockOverlayActivity::class.java.name) continue

            if (event.className in AppLockConstants.KNOWN_RECENTS_CLASSES ||
                event.className in AppLockConstants.ADMIN_CONFIG_CLASSES ||
                event.className in AppLockConstants.ACCESSIBILITY_SETTINGS_CLASSES
            ) {
                continue
            }

            recentApp = Pair(event.packageName, event.className)
        }
        return recentApp
    }

    private fun checkAndLockApp(packageName: String, triggeringPackage: String, currentTime: Long) {
        val lockedApps = appLockRepository.getLockedApps()
        if (packageName !in lockedApps) return

        val unlockDurationMinutes = appLockRepository.getUnlockTimeDuration()
        val unlockTimestamp = AppLockManager.appUnlockTimes[packageName] ?: 0L

        Log.d(
            TAG,
            "checkAndLockApp: pkg=$packageName, duration=$unlockDurationMinutes min, unlockTime=$unlockTimestamp, currentTime=$currentTime, isLockScreenShown=${AppLockManager.isLockScreenShown.get()}"
        )

        if (unlockDurationMinutes > 0 && unlockTimestamp > 0) {
            if (unlockDurationMinutes >= 10_000) {
                return
            }

            val durationMillis = unlockDurationMinutes.toLong() * 60_000L
            val elapsedMillis = currentTime - unlockTimestamp

            Log.d(
                TAG,
                "Grace period check: elapsed=${elapsedMillis}ms (${elapsedMillis / 1000}s), duration=${durationMillis}ms (${durationMillis / 1000}s)"
            )

            if (elapsedMillis < durationMillis) {
                return
            }

            Log.d(TAG, "Unlock grace period expired for $packageName. Clearing timestamp.")
            AppLockManager.appUnlockTimes.remove(packageName)
        }

        if (AppLockManager.isLockScreenShown.get()) {
            Log.d(TAG, "Lock screen already shown, skipping")
            return
        }

        Log.d(TAG, "Locked app: $packageName. Showing overlay.")
        AppLockManager.isLockScreenShown.set(true)

        val intent = Intent(this, AppLockOverlayActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS or
                    Intent.FLAG_ACTIVITY_NO_ANIMATION or
                    Intent.FLAG_FROM_BACKGROUND or
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            putExtra("locked_package", packageName)
            putExtra("triggering_package", triggeringPackage)
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting overlay for: $packageName", e)
            AppLockManager.isLockScreenShown.set(false)
        }
    }

    private fun isDeviceLocked(): Boolean {
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isDeviceLocked
    }

    private fun startForegroundService() {
        createNotificationChannel()
        val notification = createNotification()

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            determineForegroundServiceType()
        } else 0

        if (type != 0) {
            startForeground(NOTIFICATION_ID, notification, type)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun determineForegroundServiceType(): Int {
        return android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "App Lock Monitoring",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitors app usage for app lock feature"
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("App Lock")
            .setContentText("Protecting your apps")
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setOngoing(true)
            .build()
    }
}
