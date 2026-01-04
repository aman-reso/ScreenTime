package com.app.screentime

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.app.screentime.core.network.config.RemoteConfigManager
import com.app.screentime.login.usecase.LoginUseCase
import com.app.screentime.messaging.FCMTokenManager
import com.app.screentime.messaging.NotificationHelper
import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.network.repository.user.UserRepository
import com.app.screentime.sync.ChallengeSyncWorker
import com.app.screentime.sync.DataSyncWorker
import com.app.screentime.utils.Logger
import com.app.screentime.widget.WidgetUpdateWorker
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.telekom.odsystem.ODSThemeType
import com.telekom.odsystem.ODSystem
import com.telekom.odsystem.neutralScheme
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ScreenTimeApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var loginUseCase: LoginUseCase

    @Inject
    lateinit var remoteConfigManager: RemoteConfigManager

    @Inject
    lateinit var fcmTokenManager: FCMTokenManager

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()

        // Create notification channels
        NotificationHelper.createNotificationChannels(this)

        // Initialize ApiEndpoints with RemoteConfigManager
        ApiEndpoints.initialize(remoteConfigManager)

        CoroutineScope(Dispatchers.Default).launch {
            try {
                FirebaseApp.initializeApp(this@ScreenTimeApplication)
                FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
                FirebaseCrashlytics.getInstance().log("Firebase initialized in background")

                fcmTokenManager.fetchToken { token ->
//                     sendTokenToServer(token)
                }

                loginUseCase.registerDevice()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        ODSystem.init(this, ODSThemeType.SYSTEM);
        ODSystem.setTheme(this, ODSThemeType.SYSTEM);
        
        // Initialize Google Mobile Ads
        MobileAds.initialize(this) { initializationStatus ->
            Log.d("ScreenTimeApplication", "MobileAds initialized: ${initializationStatus.adapterStatusMap}")
        }
        // Enqueue one-time work request for immediate sync on app start
        WorkManager.getInstance(this).enqueue(DataSyncWorker.oneTimeWorkRequest())
        DataSyncWorker.schedule(this)
        WidgetUpdateWorker.schedule(this)
        ChallengeSyncWorker.syncPreExistingChallenges(this)
        ChallengeSyncWorker.rescheduleActiveChallenges(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
}