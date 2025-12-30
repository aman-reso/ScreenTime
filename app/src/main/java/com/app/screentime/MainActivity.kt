package com.app.screentime

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.navigation.ScreenTimeNavigation
import com.app.screentime.ui.language.LanguageViewModel
import com.app.screentime.update.InAppUpdateManager
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.ScreenTimeTheme
import com.app.screentime.ui.theme.ThemeViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.app.screentime.ui.theme.ColorPalette
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.magentaScheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var inAppUpdateManager: InAppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val languageViewModel: LanguageViewModel = hiltViewModel()
            val language by languageViewModel.language.collectAsState()
            SetLocale(language)

            // Track deeplink URI from intent
            var deeplinkUri by remember { mutableStateOf(intent.data) }

            // Update deeplink when intent changes
            LaunchedEffect(intent) {
                deeplinkUri = intent.data
            }

            ScreenTimeTheme(themeViewModel) {
                val useDarkTheme = LocalThemeMode.current
                val scheme = neutralScheme

                SideEffect {
                    // Set status bar and navigation bar icons based on theme
                    // Dark theme: light icons (SystemBarStyle.dark)
                    // Light theme: dark icons (SystemBarStyle.light)
                    enableEdgeToEdge(
                        statusBarStyle = if (useDarkTheme) {
                            SystemBarStyle.dark(scheme.basicBackground.getIntColor())
                        } else {
                            SystemBarStyle.light(
                                scheme.basicBackground.getIntColor(),
                                darkScrim = scheme.basicBackground.getIntColor()
                            )
                        },
                        navigationBarStyle = SystemBarStyle.auto(
                            Color.TRANSPARENT,
                            Color.TRANSPARENT
                        )
                    )
                }

                ODSColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    background = listOf(ODSColorModel(scheme.basicBackground)),
                    verticalArrangement = Arrangement.Top
                ) {
                    ScreenTimeNavigation(
                        scheme = scheme,
                        deeplinkUri = deeplinkUri
                    )
                }
            }
        }
        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
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
        // Check for update completion when app resumes
        lifecycleScope.launch {
            if (inAppUpdateManager.isUpdateInProgress(this@MainActivity)) {
                inAppUpdateManager.completeUpdate()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Handle in-app update result
        if (requestCode == 1001) {
            if (resultCode != RESULT_OK) {
                // Update flow was cancelled or failed
                // You can show a message or retry logic here
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Handle deeplink when app is already running
        intent.let {
            // This will be handled by LaunchedEffect in the composable
        }
    }
//
//    private fun handleDeeplink(intent: Intent?, navController: NavHostController) {
//        val uri = intent?.data
//        val deeplinkRoute = intent?.getStringExtra("route")
//        val deeplinkParam = intent?.getStringExtra("deeplink")
//
//        when {
//            uri != null -> {
//                // Handle URI-based deeplink (apptime://screen/route or https://apptime.in/route)
//                val route = uri.host ?: uri.pathSegments.firstOrNull() ?: return
//                navigateFromDeeplink(navController, route, uri)
//            }
//
//            !deeplinkRoute.isNullOrEmpty() -> {
//                // Handle route from notification or other source
//                val challengeId = intent.getStringExtra("challengeId")
//                val packageName = intent.getStringExtra("packageName")
//                val username = intent.getStringExtra("username")
//                navigateFromDeeplink(
//                    navController,
//                    deeplinkRoute,
//                    null,
//                    challengeId,
//                    packageName,
//                    username
//                )
//            }
//
//            !deeplinkParam.isNullOrEmpty() -> {
//                // Parse deeplink string
//                parseAndNavigateDeeplink(navController, deeplinkParam)
//            }
//        }
//    }
//
//    private fun navigateFromDeeplink(
//        navController: NavHostController,
//        route: String,
//        uri: Uri?,
//        challengeId: String? = null,
//        packageName: String? = null,
//        username: String? = null
//    ) {
//        when (route) {
//            "landing", "home" -> {
//                navController.navigate("landing") {
//                    popUpTo(navController.graph.startDestinationId) {
//                        inclusive = true
//                    }
//                    launchSingleTop = true
//                }
//            }
//
//            "statistics" -> {
//                navController.navigate("statistics") {
//                    popUpTo(navController.graph.startDestinationId) {
//                        inclusive = false
//                    }
//                    launchSingleTop = true
//                }
//            }
//
//            "challenges", "challenge_list" -> {
//                navController.navigate("challenges") {
//                    popUpTo(navController.graph.startDestinationId) {
//                        inclusive = false
//                    }
//                    launchSingleTop = true
//                }
//            }
//
//            "challenge_detail" -> {
//                val id = challengeId ?: uri?.getQueryParameter("challengeId")
//                ?: uri?.pathSegments?.getOrNull(1)
//                if (id != null) {
//                    navController.navigate("challenge_detail/$id") {
//                        popUpTo(navController.graph.startDestinationId) {
//                            inclusive = false
//                        }
//                        launchSingleTop = true
//                    }
//                }
//            }
//
//            "search" -> {
//                navController.navigate("search") {
//                    popUpTo(navController.graph.startDestinationId) {
//                        inclusive = false
//                    }
//                    launchSingleTop = true
//                }
//            }
//        }
//    }
//
//    private fun parseAndNavigateDeeplink(
//        navController: NavHostController,
//        deeplink: String
//    ) {
//        when {
//            deeplink.startsWith("apptime://") -> {
//                val uri = deeplink.toUri()
//                val route = uri.host ?: uri.pathSegments.firstOrNull() ?: return
//                navigateFromDeeplink(navController, route, uri)
//            }
//
//            deeplink.contains("/") -> {
//                val parts = deeplink.split("/")
//                val route = parts[0]
//                val param = if (parts.size > 1) parts[1] else null
//                when (route) {
//                    "challenge_detail" -> {
//                        if (param != null) {
//                            navController.navigate("challenge_detail/$param") {
//                                popUpTo(navController.graph.startDestinationId) {
//                                    inclusive = false
//                                }
//                                launchSingleTop = true
//                            }
//                        }
//                    }
//
//                    else -> navigateFromDeeplink(navController, route, null)
//                }
//            }
//
//            else -> {
//                navigateFromDeeplink(navController, deeplink, null)
//            }
//        }
//    }


    @Composable
    private fun SetLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = LocalConfiguration.current
        configuration.setLocale(locale)
        LocalConfiguration.current.updateFrom(configuration)
    }
}