package com.app.screentime.applock.viewmodel

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.applock.manager.AppLockManager
import com.app.screentime.applock.repository.AppLockRepository
import com.app.screentime.applock.repository.AppLockRepository.LockType
import com.app.screentime.applock.util.AppLockServiceManager
import com.app.screentime.applock.util.PermissionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Data class representing an installed app
 */
data class InstalledApp(
    val packageName: String,
    val appName: String,
    val applicationInfo: ApplicationInfo,
    val isLocked: Boolean = false
)

/**
 * UI State for App Lock Screen
 */
data class AppLockUiState(
    val installedApps: List<InstalledApp> = emptyList(),
    val lockedApps: List<InstalledApp> = emptyList(),
    val isLoading: Boolean = false,
    val hasOverlayPermission: Boolean = false,
    val hasUsageStatsPermission: Boolean = false,
    val showPinBottomSheet: Boolean = false,
    val selectedAppForLock: InstalledApp? = null,
    val isPinSet: Boolean = false,
    val isPatternSet: Boolean = false,
    val lockType: LockType = LockType.PIN
)

@HiltViewModel
class AppLockViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appLockRepository: AppLockRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppLockUiState())
    val uiState: StateFlow<AppLockUiState> = _uiState.asStateFlow()

    init {
        checkPermissions()
        checkPinStatus()
        loadInstalledApps()
    }

    /**
     * Check current permission status
     */
    fun checkPermissions() {
        _uiState.value = _uiState.value.copy(
            hasOverlayPermission = PermissionHelper.hasOverlayPermission(context),
            hasUsageStatsPermission = PermissionHelper.hasUsageStatsPermission(context)
        )
        // Check if service should be running after permission check
        checkAndStartService()
    }

    /**
     * Check if PIN or Pattern is set
     */
    private fun checkPinStatus() {
        _uiState.value = _uiState.value.copy(
            isPinSet = appLockRepository.isPinSet(),
            isPatternSet = appLockRepository.isPatternSet(),
            lockType = appLockRepository.getLockType()
        )
    }

    /**
     * Load all installed applications
     */
    fun loadInstalledApps() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val packageManager = context.packageManager
                val intent = android.content.Intent(android.content.Intent.ACTION_MAIN)
                    .addCategory(android.content.Intent.CATEGORY_LAUNCHER)
                
                val launchableApps = packageManager.queryIntentActivities(intent, 0)
                val lockedAppsSet = appLockRepository.getLockedApps()
                
                val apps = launchableApps
                    .mapNotNull { resolveInfo ->
                        try {
                            val packageName = resolveInfo.activityInfo.packageName
                            val appInfo = packageManager.getApplicationInfo(packageName, 0)
                            val appName = packageManager.getApplicationLabel(appInfo).toString()

                            InstalledApp(
                                packageName = packageName,
                                appName = appName,
                                applicationInfo = appInfo,
                                isLocked = lockedAppsSet.contains(packageName)
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    .distinctBy { it.packageName }
                    .sortedBy { it.appName }
                
                val lockedApps = apps.filter { it.isLocked }
                
                _uiState.value = _uiState.value.copy(
                    installedApps = apps,
                lockedApps = lockedApps,
                isLoading = false
            )
            
            // Check if service should be running after loading apps
            checkAndStartService()
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
    }


    /**
     * Lock app after PIN verification
     */
    fun lockAppWithPin(pin: String) {
        val selectedApp = _uiState.value.selectedAppForLock
        if (selectedApp != null && appLockRepository.validatePin(pin)) {
            appLockRepository.addLockedApp(selectedApp.packageName)
            loadInstalledApps()
            _uiState.value = _uiState.value.copy(
                showPinBottomSheet = false,
                selectedAppForLock = null
            )
            // Start service if needed
            checkAndStartService()
        }
    }
    
    /**
     * Lock app after pattern verification
     */
    fun lockAppWithPattern(pattern: String) {
        val selectedApp = _uiState.value.selectedAppForLock
        if (selectedApp != null && appLockRepository.validatePattern(pattern)) {
            appLockRepository.addLockedApp(selectedApp.packageName)
            loadInstalledApps()
            _uiState.value = _uiState.value.copy(
                showPinBottomSheet = false,
                selectedAppForLock = null
            )
            // Start service if needed
            checkAndStartService()
        }
    }

    /**
     * Toggle app lock (lock/unlock)
     */
    fun toggleAppLock(app: InstalledApp) {
        if (app.isLocked) {
            // Unlock app
            appLockRepository.removeLockedApp(app.packageName)
            AppLockManager.lockApp(app.packageName)
        } else {
            // Lock app - show PIN bottom sheet first
            _uiState.value = _uiState.value.copy(
                showPinBottomSheet = true,
                selectedAppForLock = app
            )
        }
        loadInstalledApps()
        // Check if service should be running
        checkAndStartService()
    }

    /**
     * Check and start/stop service based on locked apps and permissions
     */
    private fun checkAndStartService() {
        viewModelScope.launch {
            val hasLockedApps = appLockRepository.getLockedApps().isNotEmpty()
            val hasOverlayPermission = PermissionHelper.hasOverlayPermission(context)
            val hasUsageStatsPermission = PermissionHelper.hasUsageStatsPermission(context)

            if (AppLockServiceManager.shouldServiceRun(hasLockedApps, hasOverlayPermission, hasUsageStatsPermission)) {
                AppLockServiceManager.startService(context)
            } else {
                AppLockServiceManager.stopService(context)
            }
        }
    }

    /**
     * Set PIN for app lock
     */
    fun setPin(pin: String) {
        appLockRepository.setPin(pin)
        appLockRepository.setLockType(LockType.PIN)
        _uiState.value = _uiState.value.copy(
            isPinSet = true,
            isPatternSet = false,
            lockType = LockType.PIN
        )
    }
    
    /**
     * Set pattern for app lock
     */
    fun setPattern(pattern: String) {
        appLockRepository.setPattern(pattern)
        appLockRepository.setLockType(LockType.PATTERN)
        _uiState.value = _uiState.value.copy(
            isPatternSet = true,
            isPinSet = false,
            lockType = LockType.PATTERN
        )
    }

    /**
     * Dismiss PIN bottom sheet
     */
    fun dismissPinBottomSheet() {
        _uiState.value = _uiState.value.copy(
            showPinBottomSheet = false,
            selectedAppForLock = null
        )
    }
}

