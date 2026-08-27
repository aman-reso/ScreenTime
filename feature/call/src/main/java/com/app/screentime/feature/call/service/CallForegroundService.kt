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
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.app.screentime.feature.call.ActiveCallManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallForegroundService : Service() {

    @Inject
    lateinit var activeCallManager: ActiveCallManager

    companion object {
        const val CHANNEL_ID = "active_voice_call_channel"
        const val NOTIFICATION_ID = 4001

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
        when (intent?.action) {
            ACTION_START -> {
                val name = intent.getStringExtra(EXTRA_CALLER_NAME) ?: "Ongoing Call"
                val duration = intent.getStringExtra(EXTRA_DURATION) ?: "00:00"
                val isMuted = intent.getBooleanExtra(EXTRA_IS_MUTED, false)
                val notification = buildNotification(name, duration, isMuted)

                val hasRecordAudio = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED

                var serviceType = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (hasRecordAudio) {
                        serviceType = serviceType or ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        serviceType = serviceType or ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL
                    }
                }

                try {
                    ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, serviceType)
                } catch (e: SecurityException) {
                    Log.w("CallForegroundService", "SecurityException starting FGS: ${e.message}, falling back to 0")
                    try {
                        ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, 0)
                    } catch (e2: Exception) {
                        Log.e("CallForegroundService", "Fallback startForeground failed: ${e2.message}")
                    }
                }
            }
            ACTION_UPDATE -> {
                val name = intent.getStringExtra(EXTRA_CALLER_NAME) ?: "Ongoing Call"
                val duration = intent.getStringExtra(EXTRA_DURATION) ?: "00:00"
                val isMuted = intent.getBooleanExtra(EXTRA_IS_MUTED, false)
                val notification = buildNotification(name, duration, isMuted)
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
            ACTION_STOP -> {
                ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun buildNotification(callerName: String, duration: String, isMuted: Boolean): Notification {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // End Call action
        val endCallIntent = Intent("com.app.screentime.ACTION_CALL_HANGUP").apply {
            setPackage(packageName)
        }
        val endCallPendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            endCallIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Toggle Mute action
        val muteIntent = Intent("com.app.screentime.ACTION_CALL_MUTE_TOGGLE").apply {
            setPackage(packageName)
        }
        val mutePendingIntent = PendingIntent.getBroadcast(
            this,
            2,
            muteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val muteActionTitle = if (isMuted) "Unmute" else "Mute"

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_phone_call)
            .setContentTitle(callerName)
            .setContentText("Call in progress · $duration")
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "End Call", endCallPendingIntent)
            .addAction(android.R.drawable.stat_notify_chat, muteActionTitle, mutePendingIntent)
            .build()
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
        // If app was killed/swiped from recents, terminate call cleanly
        activeCallManager.endCall("App closed")
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
}
