package com.app.screentime.blocking.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.app.screentime.blocking.manager.AppBlockManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppBlockerService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AppBlockerService", "Service started")

        serviceScope.launch {
            while (true) {
                val blockedApps = AppBlockManager.getBlockedApps()
                if (blockedApps.isNotEmpty()) {
                    // TODO: Check foreground app and block if necessary
                    Log.d("AppBlockerService", "Blocked apps: $blockedApps")
                }
                delay(1000)
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("AppBlockerService", "Service destroyed")
    }
}

