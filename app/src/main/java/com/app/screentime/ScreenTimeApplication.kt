package com.app.screentime

import android.app.Application
import android.os.Build
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.app.screentime.core.network.config.AppConfigManager
import com.app.screentime.core.network.security.PlayIntegrityManager
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ScreenTimeApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var appConfigManager: dagger.Lazy<AppConfigManager>

    @Inject
    lateinit var playIntegrityManager: dagger.Lazy<PlayIntegrityManager>

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .allowHardware(false)
            .networkObserverEnabled(false)
            .components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        println("app open")
        initFirebase()
        initAds()
        try {
            playIntegrityManager.get().warmUp()
        } catch (e: Throwable) {
            Log.w("Winter", "Play Integrity warm-up error: ${e.message}")
        }
        try {
            val configMgr = appConfigManager.get()
            com.app.screentime.ads.AdConfig.adsEnabled = configMgr.areAdsEnabled()
            com.app.screentime.ads.AdConfig.swipeAdsEnabled = configMgr.isSwipeAdEnabled()
            com.app.screentime.ads.AdConfig.messageAdsEnabled = configMgr.isMessageAdEnabled()
            com.app.screentime.ads.AdConfig.swipeAdInterval = configMgr.getSwipeAdInterval()
            com.app.screentime.ads.AdConfig.swipeAdMinInterval = configMgr.getSwipeAdMinInterval()
            com.app.screentime.ads.AdConfig.swipeAdMaxInterval = configMgr.getSwipeAdMaxInterval()
            com.app.screentime.ads.AdConfig.messageAdInterval = configMgr.getMessageAdInterval()

            configMgr.onConfigUpdated = { updatedCfg ->
                com.app.screentime.ads.AdConfig.adsEnabled = updatedCfg.areAdsEnabled()
                com.app.screentime.ads.AdConfig.swipeAdsEnabled = updatedCfg.isSwipeAdEnabled()
                com.app.screentime.ads.AdConfig.messageAdsEnabled = updatedCfg.isMessageAdEnabled()
                com.app.screentime.ads.AdConfig.swipeAdInterval = updatedCfg.getSwipeAdInterval()
                com.app.screentime.ads.AdConfig.swipeAdMinInterval = updatedCfg.getSwipeAdMinInterval()
                com.app.screentime.ads.AdConfig.swipeAdMaxInterval = updatedCfg.getSwipeAdMaxInterval()
                com.app.screentime.ads.AdConfig.messageAdInterval = updatedCfg.getMessageAdInterval()
            }
            configMgr.fetchConfigAsync()
        } catch (e: Throwable) {
            Log.e("Winter", "App Config API init failed: ${e.message}")
        }
    }

    private fun initAds() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                MobileAds.initialize(this@ScreenTimeApplication) {}
            } catch (e: Throwable) {
                Log.e("Winter", "MobileAds init failed: ${e.message}")
            }
        }
    }

    private fun initFirebase() {
        try {
            FirebaseApp.initializeApp(this)
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
            FirebaseCrashlytics.getInstance().log("Winter started")
        } catch (e: Throwable) {
            Log.e("Winter", "Firebase init failed: ${e.message}")
        }
    }
}
