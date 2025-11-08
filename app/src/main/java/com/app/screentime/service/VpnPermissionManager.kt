package com.app.screentime.service

import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.activity.result.ActivityResultLauncher

class VpnPermissionManager(private val context: Context) {

    fun hasVpnPermission(): Boolean {
        return VpnService.prepare(context) == null
    }

    fun requestVpnPermission(launcher: ActivityResultLauncher<Intent>) {
        val intent = VpnService.prepare(context)
        if (intent != null) {
            launcher.launch(intent)
        } else {
            // Permission already granted
        }
    }
}
