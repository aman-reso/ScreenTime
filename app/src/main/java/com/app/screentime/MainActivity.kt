package com.app.screentime

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.navigation.ScreenTimeNavigation
import com.app.screentime.ui.language.LanguageViewModel
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
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val languageViewModel: LanguageViewModel = hiltViewModel()
            val language by languageViewModel.language.collectAsState()

            SetLocale(language)
            ScreenTimeTheme(themeViewModel) {
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