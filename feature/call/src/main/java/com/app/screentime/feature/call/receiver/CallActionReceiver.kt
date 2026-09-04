package com.app.screentime.feature.call.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.screentime.feature.call.ActiveCallManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var activeCallManager: ActiveCallManager

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_HANGUP -> {
                com.app.screentime.feature.call.service.CallNotificationHelper.cancelIncomingCallNotification(context ?: return)
                activeCallManager.endCall("Ended by user")
            }
            ACTION_TOGGLE_MUTE -> {
                activeCallManager.toggleMute()
            }
            ACTION_REJECT_CALL -> {
                com.app.screentime.feature.call.service.CallNotificationHelper.cancelIncomingCallNotification(context ?: return)
                activeCallManager.rejectIncomingCall()
            }
            ACTION_ACCEPT_CALL -> {
                com.app.screentime.feature.call.service.CallNotificationHelper.cancelIncomingCallNotification(context ?: return)
                activeCallManager.acceptIncomingCall()
            }
        }
    }

    companion object {
        const val ACTION_HANGUP = "com.app.screentime.ACTION_CALL_HANGUP"
        const val ACTION_TOGGLE_MUTE = "com.app.screentime.ACTION_CALL_MUTE_TOGGLE"
        const val ACTION_ACCEPT_CALL = "com.app.screentime.ACTION_ACCEPT_CALL"
        const val ACTION_REJECT_CALL = "com.app.screentime.ACTION_REJECT_CALL"
    }
}
