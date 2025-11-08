package com.app.screentime

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.app.screentime.login.usecase.LoginUseCase
import com.app.screentime.sync.DataSyncWorker
import com.app.screentime.utils.Logger
import com.app.screentime.widget.WidgetUpdateWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltAndroidApp
class ScreenTimeApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var loginUseCase: LoginUseCase

    override fun onCreate() {
        super.onCreate()
//        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
//        MobileAds.initialize(this)

//        DataSyncWorker.schedule(applicationContext)
        DataSyncWorker.oneTimeWorkRequest()

        WorkManager.getInstance(this)
            .getWorkInfosByTagLiveData("AppWorker")
            .observeForever { infos ->
                infos.forEach { info ->
                    logger.d("WorkStatus", "State: ${info.state}, stopReason: ${info.stopReason}")
                }
            }

        // Schedule periodic widget updates
        WidgetUpdateWorker.schedule(this)
        CoroutineScope(Dispatchers.IO).launch {
            loginUseCase.registerDevice()
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
}