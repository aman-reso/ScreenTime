package com.app.screentime.update

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InAppUpdateManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val appUpdateManager: AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(context)
    }

    private var installStateUpdatedListener: InstallStateUpdatedListener? = null
    var onUpdateDownloaded: (() -> Unit)? = null

    companion object {
        private const val TAG = "InAppUpdateManager"
        const val UPDATE_REQUEST_CODE = 1001
    }

    fun initialize(activity: Activity) {
        if (installStateUpdatedListener == null) {
            installStateUpdatedListener = InstallStateUpdatedListener { state ->
                when (state.installStatus()) {
                    InstallStatus.DOWNLOADED -> {
                        Log.d(TAG, "Update downloaded, ready to complete update")
                        onUpdateDownloaded?.invoke() ?: completeUpdate()
                    }
                    InstallStatus.FAILED -> {
                        Log.e(TAG, "Update download failed: ${state.installErrorCode()}")
                    }
                    else -> {}
                }
            }
            installStateUpdatedListener?.let {
                appUpdateManager.registerListener(it)
            }
        }
        checkForUpdate(activity)
    }

    fun checkForUpdate(activity: Activity) {
        try {
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    val updateType = when {
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> AppUpdateType.FLEXIBLE
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> AppUpdateType.IMMEDIATE
                        else -> null
                    }
                    if (updateType != null) {
                        try {
                            appUpdateManager.startUpdateFlow(
                                appUpdateInfo,
                                activity,
                                AppUpdateOptions.newBuilder(updateType).build()
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "Error starting update flow: ${e.message}")
                        }
                    }
                }
            }.addOnFailureListener { e ->
                Log.e(TAG, "Failed to check for update: ${e.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking for update: ${e.message}")
        }
    }

    fun onResume(activity: Activity) {
        try {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    onUpdateDownloaded?.invoke() ?: completeUpdate()
                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlow(
                        appUpdateInfo,
                        activity,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onResume update check: ${e.message}")
        }
    }

    fun completeUpdate() {
        try {
            appUpdateManager.completeUpdate()
        } catch (e: Exception) {
            Log.e(TAG, "Error completing update: ${e.message}")
        }
    }

    fun onDestroy() {
        installStateUpdatedListener?.let {
            try {
                appUpdateManager.unregisterListener(it)
            } catch (_: Exception) {}
            installStateUpdatedListener = null
        }
    }
}
