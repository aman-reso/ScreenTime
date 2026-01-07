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
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.network.repository.user.UserRepository
import com.app.screentime.sync.ChallengeSyncWorker
import com.app.screentime.sync.DataSyncWorker
import com.app.screentime.utils.Logger
import com.app.screentime.widget.WidgetUpdateWorker
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.app.screentime.ads.AdConfig
import com.app.screentime.ads.InterstitialAdManager
import com.app.screentime.di.ApplicationScope
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
import javax.inject.Provider

@HiltAndroidApp
class ScreenTimeApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var remoteConfigManager: Provider<RemoteConfigManager>

    @Inject
    lateinit var fcmTokenManager: Provider<FCMTokenManager>

    @Inject
    lateinit var loginUseCaseProvider: Provider<LoginUseCase>

    @Inject
    lateinit var preferencesManager: PreferencesManager

    @Inject
    @ApplicationScope
    lateinit var appScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        initCritical()
        initBackground()
        scheduleWorkers()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()


    private fun initCritical() {
        NotificationHelper.createNotificationChannels(this)
        ODSystem.init(this, ODSThemeType.SYSTEM)
        ODSystem.setTheme(this, ODSThemeType.SYSTEM)
        AdConfig.initialize(preferencesManager)
    }

    private fun initBackground() {
        appScope.launch {
            registerDevice()
            initFirebase()
            initRemoteConfigAndAds()
        }
    }

    private suspend fun initRemoteConfigAndAds() {
        remoteConfigManager.get().fetch()
        try {
            val adsAppVersion = remoteConfigManager.get().getString("ads_app_version")
            AdConfig.setRemoteConfigAdsAppVersion(adsAppVersion)
            if (AdConfig.areAdsEnabled()) {
                initAds()
            }
        } catch (e: Throwable) {
            e.message
        }
    }

    private fun initAds() {
        val requestConfig = RequestConfiguration.Builder()
        if (BuildConfig.DEBUG) {
            requestConfig.setTestDeviceIds(listOf("A07F51EE923FAFD06D2B957DFE6AF83E"))
        }

        MobileAds.setRequestConfiguration(requestConfig.build())
        MobileAds.initialize(this)
    }

    private suspend fun registerDevice() {
        loginUseCaseProvider.get().registerDevice()
    }

    private fun scheduleWorkers() {
        DataSyncWorker.schedule(this)
        WidgetUpdateWorker.schedule(this)
        ChallengeSyncWorker.schedule(this)
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this@ScreenTimeApplication)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
        FirebaseCrashlytics.getInstance().log("ScreenTimeApplication started")
        fcmTokenManager.get().fetchToken { token ->
            FirebaseCrashlytics.getInstance()
                .log("FCM token fetched")
        }
    }

}