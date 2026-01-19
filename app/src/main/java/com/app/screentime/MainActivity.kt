package com.app.screentime

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.ScreenTimeTheme
import com.app.screentime.ui.theme.ThemeViewModel
import com.app.screentime.update.InAppUpdateManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        analyticsUseCase.trackAppOpen()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            var deeplinkUri by remember { mutableStateOf(intent.data) }

            LaunchedEffect(intent) {
                deeplinkUri = intent.data
            }

            ScreenTimeTheme(themeViewModel) {
                val useDarkTheme = LocalThemeMode.current
                val scheme = neutralScheme
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
                    ScreenTimeNavigation(
                        scheme = scheme, deeplinkUri = deeplinkUri
                    )
                }
            }
        }
        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        appLinkIntent.action
        appLinkIntent.data
    }

    override fun onStart() {
        super.onStart()
        inAppUpdateManager.initialize(this)
        lifecycleScope.launch {
            inAppUpdateManager.checkForUpdate(this@MainActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            inAppUpdateManager.checkForUpdate(this@MainActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            if (resultCode != RESULT_OK) {

            }
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
        // Handle deeplink when app is already running
        intent.let {
            // This will be handled by LaunchedEffect in the composable
        }
    }

    private fun handleDeeplink(intent: Intent?, navController: NavHostController) {
        val uri = intent?.data
        val deeplinkRoute = intent?.getStringExtra("route")
        val deeplinkParam = intent?.getStringExtra("deeplink")

        when {
            uri != null -> {
                // Handle URI-based deeplink (apptime://screen/route or https://apptime.in/route)
                val route = uri.host ?: uri.pathSegments.firstOrNull() ?: return
                navigateFromDeeplink(navController, route, uri)
            }

            !deeplinkRoute.isNullOrEmpty() -> {
                // Handle route from notification or other source
                val challengeId = intent.getStringExtra("challengeId")
                val packageName = intent.getStringExtra("packageName")
                val username = intent.getStringExtra("username")
                navigateFromDeeplink(
                    navController,
                    deeplinkRoute,
                    null,
                    challengeId,
                    packageName,
                    username
                )
            }

            !deeplinkParam.isNullOrEmpty() -> {
                // Parse deeplink string
                parseAndNavigateDeeplink(navController, deeplinkParam)
            }
        }
    }

    private fun navigateFromDeeplink(
        navController: NavHostController,
        route: String,
        uri: Uri?,
        challengeId: String? = null,
        packageName: String? = null,
        username: String? = null
    ) {
        when (route) {
            "landing", "home" -> {
                navController.navigate("landing") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }

            "statistics" -> {
                navController.navigate("statistics") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            }

            "challenges", "challenge_list" -> {
                navController.navigate("challenges") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            }

            "challenge_detail" -> {
                val id = challengeId ?: uri?.getQueryParameter("challengeId")
                ?: uri?.pathSegments?.getOrNull(1)
                if (id != null) {
                    navController.navigate("challenge_detail/$id") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            }

            "search" -> {
                navController.navigate("search") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            }
        }
    }

    private fun parseAndNavigateDeeplink(
        navController: NavHostController,
        deeplink: String
    ) {
        when {
            deeplink.startsWith("apptime://") -> {
                val uri = deeplink.toUri()
                val route = uri.host ?: uri.pathSegments.firstOrNull() ?: return
                navigateFromDeeplink(navController, route, uri)
            }

            deeplink.contains("/") -> {
                val parts = deeplink.split("/")
                val route = parts[0]
                val param = if (parts.size > 1) parts[1] else null
                when (route) {
                    "challenge_detail" -> {
                        if (param != null) {
                            navController.navigate("challenge_detail/$param") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }

                    else -> navigateFromDeeplink(navController, route, null)
                }
            }

            else -> {
                navigateFromDeeplink(navController, deeplink, null)
            }
        }
    }


}

@Preview
@Composable
fun StorageDashboardPreview() {
}
