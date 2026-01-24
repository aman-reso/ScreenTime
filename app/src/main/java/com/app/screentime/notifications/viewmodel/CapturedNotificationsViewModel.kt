package com.app.screentime.notifications.viewmodel

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.database.ScreenTimeDatabase
import com.app.screentime.database.entity.CapturedNotificationEntity
import com.app.screentime.analytics.AnalyticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class AppNameInfo(
    val packageName: String,
    val appName: String
)

@HiltViewModel
class CapturedNotificationsViewModel @Inject constructor(
    @ApplicationContext private val context: android.content.Context,
    private val analyticsUseCase: AnalyticsUseCase
) : ViewModel() {
    
    private val dao = ScreenTimeDatabase.getDatabase(context).capturedNotificationDao()
    private val packageManager = context.packageManager

    init {
        analyticsUseCase.trackNotificationRecoverScreen()
    }
    
    private val _selectedPackageName = MutableStateFlow<String?>(null)
    val selectedPackageName: StateFlow<String?> = _selectedPackageName
    
    val allAppNames: StateFlow<List<AppNameInfo>> = dao.getAllPackageNames()
        .map { packageNames ->
            packageNames.map { packageName ->
                AppNameInfo(
                    packageName = packageName,
                    appName = getAppName(packageName)
                )
            }.sortedBy { it.appName }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val notifications: StateFlow<List<CapturedNotificationEntity>> = _selectedPackageName
        .flatMapLatest { packageName ->
            if (packageName != null) {
                dao.getNotificationsByPackage(packageName)
            } else {
                dao.getAllCapturedNotifications()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun getAppName(packageName: String): String {
        return try {
            val appInfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName // fallback to package name
        } catch (e: Exception) {
            packageName // fallback to package name
        }
    }
    
    fun setFilter(packageName: String?) {
        _selectedPackageName.value = packageName
    }
    
    fun clearFilter() {
        _selectedPackageName.value = null
    }
    
    fun deleteNotification(notification: CapturedNotificationEntity) {
        viewModelScope.launch {
            // Delete associated image files if they exist
            notification.imagePath?.let { path ->
                try {
                    val file = File(path)
                    if (file.exists()) {
                        file.delete()
                    }
                } catch (e: Exception) {
                    // Ignore file deletion errors
                }
            }
            notification.profileImagePath?.let { path ->
                try {
                    val file = File(path)
                    if (file.exists()) {
                        file.delete()
                    }
                } catch (e: Exception) {
                    // Ignore file deletion errors
                }
            }
            dao.deleteNotification(notification)
        }
    }
    
    fun deleteNotificationById(id: Long) {
        viewModelScope.launch {
            dao.deleteNotificationById(id)
        }
    }
}
