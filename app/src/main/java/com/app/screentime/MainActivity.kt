package com.app.screentime

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.app.screentime.analytics.AnalyticsUseCase
import com.app.screentime.config.language.AppLanguageManager
import com.app.screentime.navigation.ScreenTimeNavigation
import com.app.screentime.permission.EmulatorBlockScreen
import com.app.screentime.permission.checkUsageStatsPermission
import com.app.screentime.permission.viewmodel.PermissionViewModel
import com.app.screentime.permission.viewmodel.RegistrationViewModel
import com.app.screentime.registrations.screen.RegistrationsScreen
import com.app.screentime.utils.EmulatorDetector
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.ScreenTimeTheme
import com.app.screentime.ui.theme.ThemeViewModel
import com.app.screentime.update.InAppUpdateManager
import com.app.screentime.utils.CountryUtils
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var inAppUpdateManager: InAppUpdateManager

    @Inject
    lateinit var analyticsUseCase: AnalyticsUseCase

    @Inject
    lateinit var appLanguageManager: AppLanguageManager

    private val registrationViewModel: RegistrationViewModel by viewModels()
    
    private val deeplinkUriState = mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        analyticsUseCase.trackAppOpen()
        
        // Handle deeplink from intent (URI format or from notification)
        deeplinkUriState.value = extractDeeplinkUri(intent)
        
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val deeplinkUri by deeplinkUriState
            ScreenTimeTheme(themeViewModel) {
                val useDarkTheme = LocalThemeMode.current
                val scheme = neutralScheme

                if (!BuildConfig.DEBUG && EmulatorDetector.isEmulator()) {
                    EmulatorBlockScreen(onBack = { finish() }, scheme = scheme)
                } else {
                    enableEdgeToEdge(
                        statusBarStyle = if (useDarkTheme) {
                            SystemBarStyle.dark(scheme.basicBackground.getIntColor())
                        } else {
                            SystemBarStyle.light(
                                scheme.basicBackground.getIntColor(),
                                darkScrim = scheme.basicBackground.getIntColor()
                            )
                        }, navigationBarStyle = SystemBarStyle.auto(
                            Color.TRANSPARENT, Color.TRANSPARENT
                        )
                    )

                    ODSColumn(
                        modifier = Modifier.fillMaxSize(),
                        background = listOf(ODSColorModel(scheme.basicBackground)),
                        verticalArrangement = Arrangement.Top
                    ) {
                        if (registrationViewModel.isLoginRequired() || !checkUsageStatsPermission(
                                this@MainActivity
                            )
                        ) {
                            RegistrationsScreen(
                                scheme = scheme,
                                registrationViewModel = registrationViewModel,
                                isUserInIndia = CountryUtils.isUserInIndia(this@MainActivity)
                            )
                        } else {
                            ScreenTimeNavigation(
                                scheme = scheme,
                                deeplinkUri = deeplinkUri,
                                isUserInIndia = CountryUtils.isUserInIndia(this@MainActivity)
                            )
                        }
                    }
                }
            }
        }
        val appLinkIntent: Intent = intent
        appLinkIntent.action
        appLinkIntent.data
    }

    override fun onStart() {
        super.onStart()
        inAppUpdateManager.initialize(this)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            inAppUpdateManager.checkForUpdate(this@MainActivity)
        }
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.let {
            val uiMode = it.uiMode
            it.setTo(baseContext.resources.configuration)
            it.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        deeplinkUriState.value = extractDeeplinkUri(intent)
    }
    
    /**
     * Extract deeplink URI from intent
     * Supports both direct URI data and notification extras
     */
    private fun extractDeeplinkUri(intent: Intent?): Uri? {
        if (intent == null) return null
        intent.data?.let { return it }
        
        val deeplinkString = intent.getStringExtra("deeplink")
        if (!deeplinkString.isNullOrEmpty()) {
            return try {
                when {
                    deeplinkString.startsWith("apptime://") || 
                    deeplinkString.startsWith("https://") -> deeplinkString.toUri()
                    else -> "apptime://screen/$deeplinkString".toUri()
                }
            } catch (e: Exception) {
                null
            }
        }
        return null
    }
}
