package com.app.screentime.feature.call.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.screentime.feature.call.receiver.CallActionReceiver

object CallNotificationHelper {
    const val INCOMING_CALL_CHANNEL_ID = "chatty_incoming_call_channel"
    const val MISSED_CALL_CHANNEL_ID = "chatty_missed_call_channel"
    const val INCOMING_CALL_NOTIFICATION_ID = 4001
    const val MISSED_CALL_NOTIFICATION_ID = 4002

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build()

            // High priority ringing incoming call channel
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

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            action = CallActionReceiver.ACTION_ACCEPT_CALL
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
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

        val notification = NotificationCompat.Builder(context, INCOMING_CALL_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_phone_call)
            .setContentTitle(title)
            .setContentText("$callerName is calling you...")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setOngoing(true)
            .setAutoCancel(true)
            .setSound(ringtoneUri)
            .setVibrate(longArrayOf(0, 1000, 600, 1000, 600, 1000))
            .setFullScreenIntent(acceptPendingIntent, true)
            .setContentIntent(acceptPendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Decline", rejectPendingIntent)
            .addAction(android.R.drawable.stat_sys_phone_call, "Answer", acceptPendingIntent)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(INCOMING_CALL_NOTIFICATION_ID, notification)
    }

    fun cancelIncomingCallNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(INCOMING_CALL_NOTIFICATION_ID)
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

        val notification = NotificationCompat.Builder(context, MISSED_CALL_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.sym_call_missed)
            .setContentTitle("Missed Call")
            .setContentText("You missed a call from $callerName")
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(MISSED_CALL_NOTIFICATION_ID, notification)
    }
}
