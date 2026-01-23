package com.app.screentime.update

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.util.Log
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.requestAppUpdateInfo
import com.google.android.play.core.ktx.updatePriority
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InAppUpdateManager @Inject constructor() {

    companion object {
        private const val REQUEST_CODE_UPDATE = 1001
    }

    private var appUpdateManager: AppUpdateManager? = null

    fun initialize(activity: Activity) {
        appUpdateManager = AppUpdateManagerFactory.create(activity)
    }

    suspend fun checkForUpdate(activity: Activity) {
        val manager = appUpdateManager ?: return
        try {
            val info = manager.appUpdateInfo.await()
            when (info.updateAvailability()) {
                UpdateAvailability.UPDATE_AVAILABLE -> {
                    when {
                        info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> {
                            startImmediateUpdate(activity, manager, info)
                        }

                        info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> {
                            startFlexibleUpdate(activity, manager, info)
                        }
                    }
                }

                UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                    resumeImmediateUpdate(activity, manager, info)
                }

                else -> {

                }
            }
        } catch (_: Exception) {
        }
    }

    /* ---------------- IMMEDIATE ---------------- */

    private fun startImmediateUpdate(
        activity: Activity,
        manager: AppUpdateManager,
        info: AppUpdateInfo
    ) {
        manager.startUpdateFlowForResult(
            info,
            AppUpdateType.IMMEDIATE,
            activity,
            REQUEST_CODE_UPDATE
        )
    }

    private fun resumeImmediateUpdate(
        activity: Activity,
        manager: AppUpdateManager,
        info: AppUpdateInfo
    ) {
        manager.startUpdateFlowForResult(
            info,
            AppUpdateType.IMMEDIATE,
            activity,
            REQUEST_CODE_UPDATE
        )
    }

    /* ---------------- FLEXIBLE ---------------- */

    private fun startFlexibleUpdate(
        activity: Activity,
        manager: AppUpdateManager,
        info: AppUpdateInfo
    ) {
        manager.startUpdateFlowForResult(
            info,
            AppUpdateType.FLEXIBLE,
            activity,
            REQUEST_CODE_UPDATE
        )

        manager.registerListener(installListener)
    }

    private val installListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // 🔔 Show snackbar/dialog asking user to restart
            // Call completeUpdate() ONLY after user confirms
        }
    }

    fun completeFlexibleUpdate() {
        appUpdateManager?.completeUpdate()
    }

    fun unregisterListener() {
        appUpdateManager?.unregisterListener(installListener)
    }
}


