package com.app.screentime.focus

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import com.app.screentime.MainActivity
import com.app.screentime.R
import com.app.screentime.database.repository.FocusTimeRepository
import com.app.screentime.database.ScreenTimeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//class FocusModeService : Service() {
//    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
//    private val usageStatsManager by lazy {
//        getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
//    }
//
//    private val prefs: SharedPreferences by lazy {
//        getSharedPreferences("focus_mode_prefs", Context.MODE_PRIVATE)
//    }
//
//    private val focusTimeRepository by lazy {
//        val database = ScreenTimeDatabase.getDatabase(applicationContext)
//        FocusTimeRepository(database.focusTimeDao())
//    }
//
//    private var startTime: Long = 0
//    private var isRunning = false
//    private var lastResetTime: Long = 0
//    private var currentSessionId: Long? = null
//
//    companion object {
//        private const val KEY_START_TIME = "focus_start_time"
//        private const val KEY_LAST_RESET_TIME = "focus_last_reset_time"
//        private const val KEY_IS_RUNNING = "focus_is_running"
//        private const val KEY_TOTAL_DAY_TIME = "focus_total_day_time"
//        private const val KEY_DAY_START = "focus_day_start"
//        const val ACTION_START = "com.app.screentime.focus.START"
//        const val ACTION_STOP = "com.app.screentime.focus.STOP"
//        private const val CHANNEL_ID = "focus_mode_channel"
//        private const val NOTIFICATION_ID = 1001
//    }
//
//    // Reading app package names (common reading apps)
//    private val readingApps = setOf(
//        "com.google.android.apps.playbooks", // Google Play Books
//        "com.amazon.kindle", // Kindle
//        "com.adobe.digitaleditions", // Adobe Digital Editions
//        "com.aldiko.android", // Aldiko
//        "com.fbreader", // FBReader
//        "com.moonreader", // Moon+ Reader
//        "com.onyx", // Onyx
//        "com.overdrive.mobile.android.libby", // Libby
//        "com.wattpad.reader", // Wattpad
//        "com.inkitt.reader", // Inkitt
//        "com.medium.reader", // Medium
//        "com.instapaper.android", // Instapaper
//        "com.pocket.android", // Pocket
//        "com.reading", // Generic reading apps
//        "com.books", // Generic books
//        "com.ebook", // Generic ebooks
//    )
//
//    override fun onCreate() {
//        super.onCreate()
//        createNotificationChannel()
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        when (intent?.action) {
//            ACTION_START -> {
//                startFocusMode()
//            }
//
//            ACTION_STOP -> {
//                stopFocusMode()
//                // Return START_NOT_STICKY to prevent automatic restart when stopped
//                return START_NOT_STICKY
//            }
//        }
//        return START_STICKY
//    }
//
//    private fun startFocusMode() {
//        if (isRunning) return
//
//        isRunning = true
//        startTime = System.currentTimeMillis()
//        lastResetTime = startTime
//
//        // Check if we need to reset daily total (new day)
//        val currentDay = android.text.format.DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()).toString()
//        val savedDay = prefs.getString(KEY_DAY_START, "")
//        if (savedDay != currentDay) {
//            // New day, reset total
//            prefs.edit {
//                putString(KEY_DAY_START, currentDay)
//                putLong(KEY_TOTAL_DAY_TIME, 0L)
//            }
//        }
//
//        // Save to preferences (for backward compatibility and quick access)
//        prefs.edit {
//            putLong(KEY_START_TIME, startTime)
//            putLong(KEY_LAST_RESET_TIME, lastResetTime)
//            putBoolean(KEY_IS_RUNNING, true)
//        }
//
//        // Save to Room database
//        serviceScope.launch {
//            currentSessionId = focusTimeRepository.startFocusSession(
//                countdownMode = false,
//                countdownDuration = 0
//            )
//        }
//
//        startForeground(NOTIFICATION_ID, createNotification(0))
//
//        serviceScope.launch {
//            var lastSavedTotal = prefs.getLong(KEY_TOTAL_DAY_TIME, 0L)
//            var lastUpdateTime = lastResetTime
//
//            while (isRunning) {
//                checkCurrentApp()
//                updateNotification()
//
//                // Update total day time - accumulate time since last update
//                val currentTime = System.currentTimeMillis()
//                val timeSinceLastUpdate = currentTime - lastUpdateTime
//                if (timeSinceLastUpdate >= 1000) {
//                    lastSavedTotal += timeSinceLastUpdate
//                    lastUpdateTime = currentTime
//                    prefs.edit {
//                        putLong(KEY_TOTAL_DAY_TIME, lastSavedTotal)
//                        putLong(KEY_LAST_RESET_TIME, lastResetTime)
//                    }
//                }
//                delay(1000) // Check every second
//            }
//        }
//    }
//
//
//    private fun stopFocusMode() {
//        // Save final total day time before stopping
//        val currentTime = System.currentTimeMillis()
//        val sessionTime = currentTime - lastResetTime
//        val currentTotal = prefs.getLong(KEY_TOTAL_DAY_TIME, 0L)
//        prefs.edit {
//            putLong(KEY_TOTAL_DAY_TIME, currentTotal + sessionTime)
//            putBoolean(KEY_IS_RUNNING, false)
//            remove(KEY_START_TIME)
//            remove(KEY_LAST_RESET_TIME)
//        }
//
//        // Update Room database
//        val sessionId = currentSessionId
//        if (sessionId != null) {
//            serviceScope.launch {
//                val duration = currentTime - startTime
//                val wasCompleted = duration >= 60 * 1000 // At least 1 minute
//                focusTimeRepository.endFocusSession(sessionId, completed = wasCompleted)
//
//                // Trigger sync if session was completed
//                if (wasCompleted) {
//                    com.app.screentime.sync.FocusSyncWorker.triggerSync(applicationContext)
//                }
//            }
//            currentSessionId = null
//        }
//
//        isRunning = false
//        stopForeground(true)
//        stopSelf()
//    }
//
//    private fun checkCurrentApp() {
//        // App switching won't reset focus mode - timer continues regardless of app changes
//        // This method is kept for potential future use but doesn't reset the timer
//    }
//
//    private fun updateNotification() {
//        val elapsedTime = System.currentTimeMillis() - lastResetTime
//        val notification = createNotification(elapsedTime)
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(NOTIFICATION_ID, notification)
//    }
//
//    private fun createNotification(elapsedTime: Long): Notification {
//        val elapsedSeconds = elapsedTime / 1000
//        val hours = elapsedSeconds / 3600
//        val minutes = (elapsedSeconds % 3600) / 60
//        val seconds = elapsedSeconds % 60
//
//        val timeText = if (hours > 0) {
//            String.format("%02d:%02d:%02d", hours, minutes, seconds)
//        } else {
//            String.format("%02d:%02d", minutes, seconds)
//        }
//
//        val intent = Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val stopIntent = Intent(this, FocusModeService::class.java).apply {
//            action = ACTION_STOP
//        }
//        val stopPendingIntent = PendingIntent.getService(
//            this, 0, stopIntent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        return NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("Focus Mode Active")
//            .setContentText("Focus time: $timeText")
//            .setSmallIcon(R.drawable.rounded_hourglass_24)
//            .setContentIntent(pendingIntent)
//            .addAction(R.drawable.rounded_hourglass_24, "Stop", stopPendingIntent)
//            .setOngoing(true)
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//            .build()
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                "Focus Mode",
//                NotificationManager.IMPORTANCE_LOW
//            ).apply {
//                description = "Shows focus mode status"
//            }
//            val notificationManager = getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//
//    override fun onBind(intent: Intent?): IBinder? = null
//}

