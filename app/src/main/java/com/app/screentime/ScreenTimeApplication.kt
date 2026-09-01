package com.app.screentime

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ScreenTimeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        println("app open")
        initFirebase()
    }

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
