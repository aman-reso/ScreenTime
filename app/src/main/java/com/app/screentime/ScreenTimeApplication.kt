package com.app.screentime

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ScreenTimeApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        initFirebase()
        initAds()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()

    private fun initAds() {
        try {
          //  MobileAds.initialize(this)
        } catch (e: Exception) {
            Log.e("Chatty", "Ads init failed: ${e.message}")
        }
    }

    private fun initFirebase() {
        try {
            FirebaseApp.initializeApp(this)
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
            FirebaseCrashlytics.getInstance().log("Chatty started")
        } catch (e: Throwable) {
            Log.e("Chatty", "Firebase init failed: ${e.message}")
        }
    }
}
