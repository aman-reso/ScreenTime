package com.app.screentime.feature.call.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.app.screentime.feature.call.ActiveCallManager
import com.app.screentime.feature.call.CallStatus
import com.app.screentime.feature.call.R
import com.app.screentime.feature.call.receiver.CallActionReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallForegroundService : Service() {

    @Inject
    lateinit var activeCallManager: ActiveCallManager

    private var isForegroundStarted = false

    companion object {
        const val CHANNEL_ID = "active_voice_call_channel"
        const val NOTIFICATION_ID = 4005 // Distinct from incoming call notification (4001)

        const val ACTION_START = "com.app.screentime.action.START_CALL_SERVICE"
        const val ACTION_UPDATE = "com.app.screentime.action.UPDATE_CALL_SERVICE"
        const val ACTION_STOP = "com.app.screentime.action.STOP_CALL_SERVICE"

        const val EXTRA_CALLER_NAME = "extra_caller_name"
        const val EXTRA_DURATION = "extra_duration"
        const val EXTRA_IS_MUTED = "extra_is_muted"

        fun start(context: Context, callerName: String, duration: String = "00:00", isMuted: Boolean = false) {
            val intent = Intent(context, CallForegroundService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_CALLER_NAME, callerName)
                putExtra(EXTRA_DURATION, duration)
                putExtra(EXTRA_IS_MUTED, isMuted)
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } catch (e: Exception) {
                Log.e("CallForegroundService", "Failed to start service: ${e.message}")
            }
        }

        fun update(context: Context, callerName: String, duration: String, isMuted: Boolean) {
            val intent = Intent(context, CallForegroundService::class.java).apply {
                action = ACTION_UPDATE
                putExtra(EXTRA_CALLER_NAME, callerName)
                putExtra(EXTRA_DURATION, duration)
                putExtra(EXTRA_IS_MUTED, isMuted)
            }
            try {
                context.startService(intent)
            } catch (e: Exception) {
                Log.e("CallForegroundService", "Failed to update service: ${e.message}")
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, CallForegroundService::class.java).apply {
                action = ACTION_STOP
            }
            try {
                context.startService(intent)
            } catch (e: Exception) {
                Log.e("CallForegroundService", "Failed to stop service: ${e.message}")
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            if (intent == null) {
                // Service was recreated by system
                val state = activeCallManager.callState.value
                if (state.status == CallStatus.ACTIVE) {
                    val durSec = state.durationSec
                    val durStr = "%02d:%02d".format(durSec / 60, durSec % 60)
                    val notification = buildNotification(
                        callerName = state.remoteUserName.ifBlank { "Ongoing Call" },
                        duration = durStr,
                        isMuted = state.isMuted
                    )
                    startForegroundWithNotification(notification)
                    isForegroundStarted = true
                    return START_STICKY
                } else {
                    stopSelf()
                    return START_NOT_STICKY
                }
            }

            when (intent.action) {
                ACTION_START -> {
                    val name = intent.getStringExtra(EXTRA_CALLER_NAME) ?: "Ongoing Call"
                    val duration = intent.getStringExtra(EXTRA_DURATION) ?: "00:00"
                    val isMuted = intent.getBooleanExtra(EXTRA_IS_MUTED, false)
                    val notification = buildNotification(name, duration, isMuted)
                    startForegroundWithNotification(notification)
                    isForegroundStarted = true
                }
                ACTION_UPDATE -> {
                    val name = intent.getStringExtra(EXTRA_CALLER_NAME) ?: "Ongoing Call"
                    val duration = intent.getStringExtra(EXTRA_DURATION) ?: "00:00"
                    val isMuted = intent.getBooleanExtra(EXTRA_IS_MUTED, false)
                    val notification = buildNotification(name, duration, isMuted)
                    if (!isForegroundStarted) {
                        startForegroundWithNotification(notification)
                        isForegroundStarted = true
                    } else {
                        try {
                            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            notificationManager.notify(NOTIFICATION_ID, notification)
                        } catch (e: Exception) {
                            Log.e("CallForegroundService", "Failed to update notification: ${e.message}")
                        }
                    }
                }
                ACTION_STOP -> {
                    isForegroundStarted = false
                    ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
                    stopSelf()
                }
            }
        } catch (e: Exception) {
            Log.e("CallForegroundService", "Error in onStartCommand: ${e.message}", e)
        }
        return START_STICKY
    }

    private fun startForegroundWithNotification(notification: Notification) {
        val hasRecordAudio = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        val hasManageOwnCalls = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.MANAGE_OWN_CALLS
        ) == PackageManager.PERMISSION_GRANTED

        val serviceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            when {
                hasRecordAudio && hasManageOwnCalls -> {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL or ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
                }
                hasManageOwnCalls -> {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL
                }
                hasRecordAudio -> {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
                }
                else -> 0
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (hasRecordAudio) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
            } else {
                0
            }
        } else {
            0
        }

        try {
            ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, serviceType)
        } catch (e: SecurityException) {
            Log.e("CallForegroundService", "SecurityException starting foreground with type $serviceType: ${e.message}")
            if (serviceType != 0) {
                try {
                    ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, 0)
                } catch (e2: Exception) {
                    Log.e("CallForegroundService", "Fallback 0 startForeground failed: ${e2.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("CallForegroundService", "startForeground failed with type $serviceType: ${e.message}", e)
            if (serviceType != 0) {
                try {
                    ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, 0)
                } catch (e2: Exception) {
                    Log.e("CallForegroundService", "Fallback 0 startForeground failed: ${e2.message}")
                }
            }
        }
    }

    private fun buildNotification(callerName: String, duration: String, isMuted: Boolean): Notification {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // End Call action
        val endCallIntent = Intent(CallActionReceiver.ACTION_HANGUP).apply {
            setPackage(packageName)
        }
        val endCallPendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            endCallIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Toggle Mute action
        val muteIntent = Intent(CallActionReceiver.ACTION_TOGGLE_MUTE).apply {
            setPackage(packageName)
        }
        val mutePendingIntent = PendingIntent.getBroadcast(
            this,
            2,
            muteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val muteActionTitle = if (isMuted) "Unmute" else "Mute"
        val appIcon = getAppIconBitmap(this)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_call_notification)
            .apply {
                if (appIcon != null) {
                    setLargeIcon(appIcon)
                }
            }
            .setContentTitle(callerName)
            .setContentText("Call in progress · $duration")
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .addAction(R.drawable.ic_call_decline_dynamic, "End Call", endCallPendingIntent)
            .addAction(android.R.drawable.stat_notify_chat, muteActionTitle, mutePendingIntent)
            .build()
    }

    private fun getAppIconBitmap(context: Context): Bitmap? {
        return try {
            val pm = context.packageManager
            val drawable = pm.getApplicationIcon(context.packageName)
            if (drawable is BitmapDrawable && drawable.bitmap != null) {
                drawable.bitmap
            } else {
                val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 128
                val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 128
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Active Voice Calls",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Ongoing call status and controls"
                setShowBadge(false)
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        // User swiped the app away from recent tasks: keep foreground call service alive in background
        Log.d("CallForegroundService", "onTaskRemoved: App swiped from recents. Keeping call running in background.")
    }
}
