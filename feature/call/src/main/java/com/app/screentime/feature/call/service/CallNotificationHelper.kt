package com.app.screentime.feature.call.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import com.app.screentime.feature.call.R
import com.app.screentime.feature.call.receiver.CallActionReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object CallNotificationHelper {
    const val INCOMING_CALL_CHANNEL_ID = "chatty_incoming_call_channel"
    const val MISSED_CALL_CHANNEL_ID = "chatty_missed_call_channel"
    const val INCOMING_CALL_NOTIFICATION_ID = 4001
    const val MISSED_CALL_NOTIFICATION_ID = 4002

    private var fallbackRingtone: Ringtone? = null
    private var fallbackRingJob: Job? = null
    private val helperScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun areNotificationsEnabled(context: Context): Boolean {
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) return false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = manager.getNotificationChannel(INCOMING_CALL_CHANNEL_ID)
            if (channel != null && channel.importance == NotificationManager.IMPORTANCE_NONE) {
                return false
            }
        }
        return true
    }

    private fun startFallbackRingtoneIfNotificationsOff(context: Context) {
        if (areNotificationsEnabled(context)) {
            return
        }

        stopFallbackRingtone(context)

        fallbackRingJob = helperScope.launch {
            try {
                val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

                val ringtone = RingtoneManager.getRingtone(context.applicationContext, ringtoneUri)?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        audioAttributes = AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                            .build()
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        isLooping = true
                    }
                }
                fallbackRingtone = ringtone

                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                    vm?.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                }

                ringtone?.play()
                val pattern = longArrayOf(0, 1000, 800, 1000, 800, 1000, 800, 1000, 800, 1000)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(pattern, -1)
                }

                Log.d("CallNotificationHelper", "Notifications turned off: ringing phone for 10 seconds")

                // Ring phone for 10 seconds
                delay(10_000)

                stopFallbackRingtone(context)
            } catch (e: Exception) {
                Log.w("CallNotificationHelper", "Fallback ringtone error: ${e.message}")
            }
        }
    }

    fun stopFallbackRingtone(context: Context? = null) {
        fallbackRingJob?.cancel()
        fallbackRingJob = null
        try {
            fallbackRingtone?.stop()
        } catch (_: Exception) {}
        fallbackRingtone = null

        context?.let { ctx ->
            try {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vm = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                    vm?.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                }
                vibrator?.cancel()
            } catch (_: Exception) {}
        }
    }

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build()

            val incomingChannel = NotificationChannel(
                INCOMING_CALL_CHANNEL_ID,
                "Incoming Calls",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Full-screen alerts and ringtones for incoming audio and video calls"
                setSound(ringtoneUri, audioAttributes)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 1000, 600, 1000, 600, 1000)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            // Missed call channel
            val missedChannel = NotificationChannel(
                MISSED_CALL_CHANNEL_ID,
                "Missed Calls",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for missed audio and video calls"
            }

            notificationManager.createNotificationChannel(incomingChannel)
            notificationManager.createNotificationChannel(missedChannel)
        }
    }

    fun showIncomingCallNotification(
        context: Context,
        callId: String,
        callerId: String,
        callerName: String,
        callType: String = "voice"
    ) {
        createChannels(context)

        val launchIntent =
            context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
                action = CallActionReceiver.ACTION_ACCEPT_CALL
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("call_id", callId)
                putExtra("caller_id", callerId)
                putExtra("caller_name", callerName)
                putExtra("call_type", callType)
            }

        val acceptPendingIntent = PendingIntent.getActivity(
            context,
            110,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val rejectIntent = Intent(context, CallActionReceiver::class.java).apply {
            action = CallActionReceiver.ACTION_REJECT_CALL
            putExtra("call_id", callId)
            putExtra("caller_id", callerId)
        }
        val rejectPendingIntent = PendingIntent.getBroadcast(
            context,
            111,
            rejectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val isVideo = callType.equals("video", ignoreCase = true)
        val title = if (isVideo) "Incoming Video Call" else "Incoming Voice Call"
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

        val appIcon = getAppIconBitmap(context)

        val callerPerson = Person.Builder()
            .setName(callerName)
            .setImportant(true)
            .build()

        val callStyle = NotificationCompat.CallStyle.forIncomingCall(
            callerPerson,
            rejectPendingIntent,
            acceptPendingIntent
        )

        val notification = NotificationCompat.Builder(context, INCOMING_CALL_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_call_notification)
            .apply {
                if (appIcon != null) {
                    setLargeIcon(appIcon)
                }
            }
            .setStyle(callStyle)
            .setContentTitle(title)
            .setContentText("$callerName is calling you...")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setOngoing(true)
            .setAutoCancel(false)
            .setSound(ringtoneUri)
            .setVibrate(longArrayOf(0, 1000, 600, 1000, 600, 1000))
            .setFullScreenIntent(acceptPendingIntent, true)
            .setContentIntent(acceptPendingIntent)
            .addAction(
                R.drawable.ic_call_decline_dynamic,
                "Reject",
                rejectPendingIntent
            )
            .addAction(
                R.drawable.ic_call_answer_dynamic,
                "Accept",
                acceptPendingIntent
            )
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            notificationManager.notify(INCOMING_CALL_NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Log.e("CallNotificationHelper", "Failed to notify: ${e.message}")
        }

        // If notifications are turned off by user/system, ring phone directly for 10 seconds
        startFallbackRingtoneIfNotificationsOff(context)
    }

    fun cancelIncomingCallNotification(context: Context) {
        stopFallbackRingtone(context)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            notificationManager.cancel(INCOMING_CALL_NOTIFICATION_ID)
        } catch (_: Exception) {}
    }

    fun showMissedCallNotification(context: Context, callerName: String) {
        createChannels(context)

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val contentIntent = PendingIntent.getActivity(
            context,
            112,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val appIcon = getAppIconBitmap(context)

        val notification = NotificationCompat.Builder(context, MISSED_CALL_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_call_missed)
            .apply {
                if (appIcon != null) {
                    setLargeIcon(appIcon)
                }
            }
            .setContentTitle("Missed Call")
            .setContentText("You missed a call from $callerName")
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            notificationManager.notify(MISSED_CALL_NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Log.e("CallNotificationHelper", "Failed to post missed call notification: ${e.message}")
        }
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
}
