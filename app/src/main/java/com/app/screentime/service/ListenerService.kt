package com.app.screentime.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.screentime.R
import com.app.screentime.applock.manager.AppLockManager
import com.app.screentime.applock.repository.AppLockRepository
import com.app.screentime.blocking.model.BlockingRule
import com.app.screentime.blocking.repository.BlockingRepository

/**
 * Service that monitors foreground apps using UsageStatsManager.
 * This is the primary mechanism for App Blocking and App Lock detection.
 * 
 * Pros of UsageStatsManager approach:
 * - Less intrusive permission (USAGE_STATS)
 * - More privacy-friendly from user perspective
 * 
 * Cons:
 * - Polling-based (less efficient than event-driven)
 * - Not real-time (up to polling interval delay)
 * 
 * Note: To monitor apps, the user must grant "Usage Access" permission.
 */
class ListenerService : Service() {

    private val handlerThread = HandlerThread("ListenerServiceThread").apply { start() }
    private val handler: Handler = Handler(handlerThread.looper)
    
    private var currentForegroundPackage: String? = null
    private var lastCheckedPackage: String? = null
    private var lastAppLockCheckTime: Long = 0
    private val APP_LOCK_CHECK_DEBOUNCE_MS = 500L // 500ms debounce
    private val lastSeenTime = mutableMapOf<String, Long>() // Track when each app was last seen
    private val APP_REOPEN_THRESHOLD_MS = 2000L // If app not seen for 2 seconds, treat as reopened
    
    // Polling interval - adjust based on battery vs responsiveness tradeoff
    // Lower = more responsive but higher battery drain
    // Higher = less battery drain but slower detection
    private val POLLING_INTERVAL_MS = 500L // 500ms polling interval
    
    private val appLockRepository by lazy { AppLockRepository(this) }
    private val blockingRepository by lazy { BlockingRepository(this) }
    private val appCounts by lazy { AppCounts(this) }
    private val appUsageTracker by lazy { AppUsageTracker(this) }
    private val appLockOverlayController by lazy { AppLockOverlayController(this) }
    private val usageStatsManager by lazy { 
        getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager 
    }
    
    private val checkRunnable = object : Runnable {
        override fun run() {
            checkForegroundApp()
            handler.postDelayed(this, POLLING_INTERVAL_MS)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "foreground",
            "Foreground Services",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "To inform the user that the app is running in the background."
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "foreground")
            .setContentTitle("App Lock Monitor")
            .setContentText("Monitoring app usage for app lock")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setOngoing(true)
            .build()
    }

    @SuppressLint("ForegroundServiceType", "HardwareIds")
    override fun onCreate() {
        super.onCreate()
        
        // Check if there are any active rules (locked apps or blocking rules) before starting
        val hasActiveRules = try {
            val allLockRules = appLockRepository.getAllRules()
            val hasLocks = allLockRules.any { it.isLocked }
            
            val allBlockingRules = blockingRepository.getAllRules()
            val hasBlocking = allBlockingRules.isNotEmpty()
            
            hasLocks || hasBlocking
        } catch (e: Exception) {
            Log.e("ListenerService", "Error checking active rules", e)
            false
        }
        
        if (!hasActiveRules) {
            Log.d("ListenerService", "No active rules found, stopping service")
            stopSelf()
            return
        }
        
        createNotificationChannel()
        startForeground(1, createNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        
        // Initialize locked apps from repository
        initializeLockedApps()
        
        Log.d("ListenerService", "Service created, starting app monitoring")
        handler.post(checkRunnable)
    }
    
    /**
     * Initialize locked apps from repository when service starts.
     * This ensures that locked apps are properly tracked even after service restart.
     */
    private fun initializeLockedApps() {
        try {
            val allRules = appLockRepository.getAllRules()
            allRules.forEach { rule ->
                if (rule.isLocked) {
                    AppLockManager.lockApp(rule.packageName)
                    Log.d("ListenerService", "Initialized lock for ${rule.packageName}")
                }
            }
        } catch (e: Exception) {
            Log.e("ListenerService", "Error initializing locked apps", e)
        }
    }

    /**
     * Checks the current foreground app using UsageStatsManager.
     * This method polls the usage stats to detect which app is currently in the foreground.
     */
    private fun checkForegroundApp() {
        try {
            // Check if there are still active rules - stop service if list is empty
            val hasActiveRules = try {
                val allLockRules = appLockRepository.getAllRules()
                val hasLocks = allLockRules.any { it.isLocked }
                
                val allBlockingRules = blockingRepository.getAllRules()
                val hasBlocking = allBlockingRules.isNotEmpty()
                
                val hasActive = hasLocks || hasBlocking
                if (!hasActive) {
                    Log.d("ListenerService", "Rule list is empty, stopping service")
                }
                hasActive
            } catch (e: Exception) {
                Log.e("ListenerService", "Error checking active rules", e)
                true // Continue running if error checking
            }
            
            if (!hasActiveRules) {
                Log.d("ListenerService", "No active rules remaining, stopping service")
                handler.removeCallbacks(checkRunnable)
                appLockOverlayController.destroy()
                stopSelf()
                return
            }
            
            val currentTime = System.currentTimeMillis()
            
            // Query usage stats for a small time window (last 1 second)
            // This helps identify the most recently used app
            val usageStatsList: List<UsageStats> = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                currentTime - 1000,
                currentTime
            )

            if (usageStatsList.isEmpty()) {
                return
            }

            // Find the app with the most recent lastTimeUsed
            // Filter out apps that were used very recently (within last 100ms) to avoid
            // false positives when apps are removed from background
            val recentUsageStats = usageStatsList
                .filter { 
                    // Only consider apps that were actually used (not just in the list)
                    val timeSinceUsed = currentTime - it.lastTimeUsed
                    timeSinceUsed < 2000 // Within last 2 seconds
                }
                .maxByOrNull { it.lastTimeUsed }
            
            val pkg = recentUsageStats?.packageName ?: return
            
            // Skip if it's our own app
            if (pkg == packageName) {
                return
            }
            
            // Skip home screen/launcher apps - don't show PIN when user goes to home
            val isHomeScreen = isHomeScreenPackage(pkg)
            if (isHomeScreen) {
                // If switching to home screen, just re-lock previous app but don't check lock
                if (currentForegroundPackage != null && currentForegroundPackage != pkg) {
                    val prevLockRule = appLockRepository.getRule(currentForegroundPackage!!)
                    if (prevLockRule != null && prevLockRule.isLocked) {
                        AppLockManager.lockApp(currentForegroundPackage!!)
                        lastSeenTime.remove(currentForegroundPackage)
                        if (lastCheckedPackage == currentForegroundPackage) {
                            lastCheckedPackage = null
                        }
                    }
                }
                currentForegroundPackage = pkg
                return
            }
            
            val packageChanged = pkg != currentForegroundPackage
            
            // Additional validation: Only process if the app was actually used recently
            // This prevents showing PIN when app is being removed from background
            val timeSinceLastUsed = currentTime - (recentUsageStats.lastTimeUsed)
            if (timeSinceLastUsed > 3000) {
                // App was last used more than 3 seconds ago - likely not in foreground
                // This can happen when app is removed from background
                Log.d("ListenerService", "Skipping $pkg - last used ${timeSinceLastUsed}ms ago (likely removed from background)")
                return
            }
            
            // Only process if package changed (app came to foreground)
            if (packageChanged) {
                val previousPackage = currentForegroundPackage
                val lastSeen = lastSeenTime[pkg] ?: 0L
                val timeSinceLastSeen = currentTime - lastSeen
                val isReopened = lastSeen > 0 && timeSinceLastSeen > APP_REOPEN_THRESHOLD_MS
                
                // Update currentForegroundPackage
                currentForegroundPackage = pkg
                
                // Re-lock previous app when switching AWAY from it (app goes to background)
                // This happens when package changes FROM previousPackage TO pkg
                if (previousPackage != null && previousPackage != pkg) {
                    val prevLockRule = appLockRepository.getRule(previousPackage)
                    if (prevLockRule != null && prevLockRule.isLocked) {
                        // Re-lock the previous app when user switches away (goes to background)
                        AppLockManager.lockApp(previousPackage)
                        Log.d("ListenerService", "App $previousPackage moved to background, re-locked")
                        // Clear lastSeenTime for previous app so it will be treated as reopened when it comes back
                        lastSeenTime.remove(previousPackage)
                        // Reset check so it will be checked again if user returns
                        if (lastCheckedPackage == previousPackage) {
                            lastCheckedPackage = null
                        }
                    }
                }
                
                // Update last seen time only when app comes to FOREGROUND
                // This ensures that when app is removed from recent apps and reopened,
                // it will have an old lastSeenTime and be treated as reopened
                lastSeenTime[pkg] = currentTime
                
                // If app was reopened (removed from recent apps and came back), reset the checked state
                if (isReopened) {
                    Log.d("ListenerService", "App $pkg was reopened after ${timeSinceLastSeen}ms (removed from recent apps), resetting check state")
                    if (lastCheckedPackage == pkg) {
                        lastCheckedPackage = null
                    }
                }

                // Check lock only when app comes to FOREGROUND

                // Handle Launch Count
                handleAppLaunch(pkg)
                appUsageTracker.setAppStartTime(pkg, currentTime)
            } else {
                handleAppDuration(pkg)
            }
            
            checkAppAccess(pkg)
        } catch (e: SecurityException) {
            Log.e("ListenerService", "USAGE_STATS permission not granted", e)
            // Stop polling if permission is revoked
            handler.removeCallbacks(checkRunnable)
        } catch (e: Exception) {
            Log.e("ListenerService", "Error checking foreground app", e)
        }
    }


    
    private fun handleAppLaunch(packageName: String) {
        val rule = blockingRepository.getRule(packageName) ?: return
        if (rule is BlockingRule.LaunchBasedBlock) {
             appCounts.increment(packageName)
        }
    }

    private fun handleAppDuration(packageName: String) {
        val rule = blockingRepository.getRule(packageName) ?: return
        if (rule is BlockingRule.DurationBasedBlock) {
            val startTime = appUsageTracker.getAppStartTime(packageName)
            if (startTime == 0L) {
                 appUsageTracker.setAppStartTime(packageName, System.currentTimeMillis())
                 return
            }
            val currentTime = System.currentTimeMillis()
            val sessionDurationMinutes = (currentTime - startTime) / (1000 * 60)
            if (sessionDurationMinutes >= 1) {
                val updatedRule = rule.copy(currentDurationMinutes = rule.currentDurationMinutes + sessionDurationMinutes)
                blockingRepository.saveRule(updatedRule)
                appUsageTracker.setAppStartTime(packageName, currentTime)
            }
        }
    }

    private fun saveAppUsageDuration(packageName: String, rule: BlockingRule.DurationBasedBlock) {
        val startTime = appUsageTracker.getAppStartTime(packageName)
        if (startTime > 0) {
            val currentTime = System.currentTimeMillis()
            val sessionDurationMinutes = (currentTime - startTime) / (1000 * 60)
            if (sessionDurationMinutes > 0) {
                val updatedRule = rule.copy(currentDurationMinutes = rule.currentDurationMinutes + sessionDurationMinutes)
                blockingRepository.saveRule(updatedRule)
            }
        }
    }

    private fun checkAppAccess(packageName: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAppLockCheckTime < APP_LOCK_CHECK_DEBOUNCE_MS) { return }
        lastAppLockCheckTime = currentTime
        
        var shouldLock = false
        var isBlockingRule = false
        
        val lockRule = appLockRepository.getRule(packageName)
        if (lockRule != null && lockRule.isLocked) { shouldLock = true }
        
        // Blocking rules override App Lock (stricter)
        val blockingRule = blockingRepository.getRule(packageName)
        if (blockingRule != null) {
            when (blockingRule) {
                is BlockingRule.InstantBlock -> {
                    shouldLock = true
                    isBlockingRule = true
                }
                is BlockingRule.LaunchBasedBlock -> {
                    if (appCounts.get(packageName) >= blockingRule.maxLaunches) {
                        shouldLock = true
                        isBlockingRule = true
                    }
                }
                is BlockingRule.DurationBasedBlock -> {
                    val startTime = appUsageTracker.getAppStartTime(packageName)
                    val sessionDuration = if (startTime > 0) (currentTime - startTime) / (1000 * 60) else 0
                    if (blockingRule.currentDurationMinutes + sessionDuration >= blockingRule.maxDurationMinutes) {
                        shouldLock = true
                        isBlockingRule = true
                    }
                }
            }
        }

        if (!shouldLock) return

        if (!AppLockManager.getLockedApps().contains(packageName)) {
             AppLockManager.lockApp(packageName)
        }

        if (!AppLockManager.isAppLocked(packageName)) return
        
        if (appLockOverlayController.isOverlayShowing() && 
            appLockOverlayController.currentPackageName == packageName) return

        if (!Settings.canDrawOverlays(this)) {
            goToHomeScreen()
            return
        }

        val appName = (lockRule?.appName ?: blockingRule?.appName ?: "App").toString()

        if (isBlockingRule) {
            appLockOverlayController.showBlockOverlay(
                packageName = packageName,
                appName = appName,
                onClose = {
                    goToHomeScreen()
                },
                onEmergency = {
                    // Optional: Allow emergency unlock or just show toast
                }
            )
        } else {
            appLockOverlayController.showPINOverlay(
                packageName = packageName,
                appName = appName,
                onPINVerified = {
                    AppLockManager.unlockApp(packageName)
                    appLockOverlayController.hideOverlay()
                    try {
                        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                        launchIntent?.let {
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(it)
                        }
                    } catch (e: Exception) { Log.e("ListenerService", "Error launching app", e) }
                },
                onCancel = {
                    goToHomeScreen()
                    appLockOverlayController.hideOverlay()
                }
            )
        }
    }

    private fun goToHomeScreen() {
        try {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("ListenerService", "Error going to home screen", e)
        }
    }
    
    /**
     * Checks if the given package is a home screen/launcher app.
     * We should not show PIN overlay when user navigates to home screen.
     */
    private fun isHomeScreenPackage(packageName: String): Boolean {
        // Common home screen/launcher package names
        val homeScreenPackages = setOf(
            "com.android.launcher",
            "com.android.launcher2",
            "com.android.launcher3",
            "com.google.android.launcher",
            "com.samsung.android.launcher",
            "com.miui.home",
            "com.huawei.android.launcher",
            "com.oneplus.launcher",
            "com.oppo.launcher",
            "com.vivo.launcher",
            "com.realme.launcher",
            "com.nothing.launcher"
        )
        
        // Check if it's a known launcher
        if (homeScreenPackages.any { packageName.startsWith(it) }) {
            return true
        }
        
        // Check if it handles HOME intent (more reliable)
        try {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
            }
            val resolveInfos = packageManager.queryIntentActivities(intent, 0)
            return resolveInfos.any { it.activityInfo.packageName == packageName }
        } catch (e: Exception) {
            Log.e("ListenerService", "Error checking home screen", e)
            return false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(checkRunnable)
        handlerThread.quitSafely()
        appLockOverlayController.destroy()
        Log.d("ListenerService", "Service destroyed")
        stopSelf()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY // Restart service if killed
    }
}