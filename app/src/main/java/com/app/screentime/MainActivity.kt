package com.app.screentime

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.navigation.ScreenTimeNavigation
import com.app.screentime.ui.language.LanguageViewModel
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.ScreenTimeTheme
import com.app.screentime.ui.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import androidx.core.net.toUri

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val languageViewModel: LanguageViewModel = hiltViewModel()
            val language by languageViewModel.language.collectAsState()
            SetLocale(language)
            ScreenTimeTheme(themeViewModel) {
                val colors = LocalAppColors.current ?: return@ScreenTimeTheme
                val useDarkTheme = LocalThemeMode.current

                SideEffect {
                    // Set status bar and navigation bar icons based on theme
                    // Dark theme: light icons (SystemBarStyle.dark)
                    // Light theme: dark icons (SystemBarStyle.light)
                    enableEdgeToEdge(
                        statusBarStyle = if (useDarkTheme) {
                            SystemBarStyle.dark(colors.background.toArgb())
                        } else {
                            SystemBarStyle.light(
                                colors.background.toArgb(),
                                darkScrim = colors.background.toArgb()
                            )
                        },
                        navigationBarStyle = SystemBarStyle.auto(
                            android.graphics.Color.TRANSPARENT,
                            android.graphics.Color.TRANSPARENT
                        )
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.background)
                ) {
                    Box(
                        modifier = Modifier
                            .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                            .fillMaxWidth()
                            .background(colors.background)
                    )
                    ScreenTimeNavigation()
                }
            }
        }
    }


    @Composable
    fun SetLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = LocalConfiguration.current
        configuration.setLocale(locale)
        LocalConfiguration.current.updateFrom(configuration)
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ScreenTimeTheme {
            Text("Hello Android!")
        }
    }

    @Composable
    fun XYZ() {
        TrackerSettingsScreen(
            defaultPkg = "youtube",
            defaultThreshold = 1,
            onSave = { pkg, threshold ->
                Toast.makeText(
                    this,
                    "Saved for $pkg (limit $threshold)",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onRequestAccessibility = {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            },
            onRequestOverlay = {
                if (!Settings.canDrawOverlays(this)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        "package:$packageName".toUri()
                    )
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Overlay permission already granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }
}


@Composable
fun TrackerSettingsScreen(
    defaultPkg: String,
    defaultThreshold: Int,
    onSave: (String, Int) -> Unit,
    onRequestAccessibility: () -> Unit,
    onRequestOverlay: () -> Unit
) {
    var pkg by remember { mutableStateOf(defaultPkg) }
    var thresholdText by remember { mutableStateOf(defaultThreshold.toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Accessibility Tracker", style = MaterialTheme.typography.headlineSmall)

        Button(onClick = onRequestAccessibility, modifier = Modifier.fillMaxWidth()) {
            Text("Open Accessibility Settings")
        }
        Button(onClick = onRequestOverlay, modifier = Modifier.fillMaxWidth()) {
            Text("Request Overlay Permission")
        }
        Button(
            onClick = {
                val n = thresholdText.toIntOrNull() ?: 0
                if (pkg.isNotBlank() && n > 0) onSave(pkg, n)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
