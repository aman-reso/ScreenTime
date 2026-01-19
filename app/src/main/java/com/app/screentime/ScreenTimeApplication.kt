package com.app.screentime

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.app.screentime.BuildConfig
import androidx.core.os.LocaleListCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.app.screentime.config.ConfigManager
import com.app.screentime.di.ApplicationScope
import com.app.screentime.login.usecase.LoginUseCase
import com.app.screentime.messaging.FCMTokenManager
import com.app.screentime.messaging.NotificationHelper
import com.app.screentime.sync.ChallengeSyncWorker
import com.app.screentime.sync.DataSyncWorker
import com.app.screentime.ui.theme.ThemeRepository
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.telekom.odsystem.ODSThemeType
import com.telekom.odsystem.ODSystem
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class ScreenTimeApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var fcmTokenManager: Provider<FCMTokenManager>

    @Inject
    lateinit var loginUseCaseProvider: Provider<LoginUseCase>

    @Inject
    lateinit var configManagerProvider: Provider<ConfigManager>

    @Inject
    @ApplicationScope
    lateinit var appScope: CoroutineScope

    @Inject
    lateinit var themeRepository: ThemeRepository

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
        try {
            val savedTheme = runBlocking {
                themeRepository.theme.first()
            }
            val themeType = when (savedTheme.lowercase()) {
                "light" -> ODSThemeType.LIGHT
                "dark" -> ODSThemeType.DARK
                else -> ODSThemeType.SYSTEM
            }
            ODSystem.init(this, defaultThemeType = themeType)
            ODSystem.setTheme(this, themeType)
        } catch (e: Exception) {
            ODSystem.init(this, defaultThemeType = ODSThemeType.SYSTEM)
            ODSystem.setTheme(this, ODSThemeType.SYSTEM)
        }
    }

    private fun initBackground() {
        appScope.launch {
            configManagerProvider.get().initialize()
            registerDevice()
            initFirebase()
            initAds()
        }
    }


    private fun initAds() {
        val requestConfig = RequestConfiguration.Builder()
//        if (BuildConfig.DEBUG) {
//            requestConfig.setTestDeviceIds(listOf("A07F51EE923FAFD06D2B957DFE6AF83E"))
//        }

        MobileAds.setRequestConfiguration(requestConfig.build())
        MobileAds.initialize(this)
    }

    private suspend fun registerDevice() {
        loginUseCaseProvider.get().registerDevice()
    }

    private fun scheduleWorkers() {
        DataSyncWorker.schedule(this)
        ChallengeSyncWorker.schedule(this)
    }

    private fun initFirebase() {
        try {
            FirebaseApp.initializeApp(this@ScreenTimeApplication)
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
            FirebaseCrashlytics.getInstance().log("ScreenTimeApplication started")
            fcmTokenManager.get().fetchToken { token ->

            }
        } catch (e: Throwable) {

        }
    }
}

fun changeLanguage(
    activity: Activity?,
    languageCode: String
) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
    if (BuildConfig.DEBUG) {
        Log.d(
            "LANG",
            AppCompatDelegate.getApplicationLocales().toLanguageTags()
        )
    }
}
