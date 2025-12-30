package com.app.screentime.permission

import android.app.AppOpsManager
import android.content.Intent
import android.graphics.Color
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.app.screentime.permission.component.bottombar.BottomBar
import com.app.screentime.permission.component.bottombar.BottomBarProps
import com.app.screentime.permission.component.herosection.HeroSection
import com.app.screentime.permission.component.herosection.HeroSectionProps
import com.app.screentime.permission.component.infocard.InfoCardList
import com.app.screentime.permission.component.infocard.InfoCardProps
import com.app.screentime.ui.theme.LocalThemeMode
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * App Permission Screen - Complete permission request screen using component architecture.
 */
@Composable
fun AppPermissionScreen(
    modifier: Modifier = Modifier,
    onAllPermissionsGranted: () -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
    val activity = LocalActivity.current
    // Get theme mode for status bar styling
    val useDarkTheme = LocalThemeMode.current
    SideEffect {
        if (activity is ComponentActivity) {
            activity.enableEdgeToEdge(
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
    }

    val context = LocalContext.current

    // Permission state
    var hasUsageStatsPermission by remember { mutableStateOf(false) }

    // Check permission on launch
    LaunchedEffect(Unit) {
        hasUsageStatsPermission = checkUsageStatsPermission(context)
        if (hasUsageStatsPermission) {
            onAllPermissionsGranted()
        }
    }

    // Usage stats permission launcher
    val usageStatsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        hasUsageStatsPermission = checkUsageStatsPermission(context)
        if (hasUsageStatsPermission) {
            onAllPermissionsGranted()
        }
    }

    // Handle Allow button click
    val handleAllowClick = {
        if (!hasUsageStatsPermission) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intent.data = "package:${context.packageName}".toUri()
            usageStatsPermissionLauncher.launch(intent)
        } else {
            onAllPermissionsGranted()
        }
    }

    val scrollState = rememberScrollState()
    println("ScrollValue-->" + scrollState.value)
    ODSBox(modifier = modifier, clipContent = true) {
        ODSBox(modifier = Modifier) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                ODSBox(
                    modifier = Modifier
                        .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                        .fillMaxWidth()
                ) {}
                ODSColumn(
                    modifier = Modifier,
                    clipContent = true,
                    padding = ODSPadding(
                        horizontal = DSVariables.spacingComponent3,
                        top = DSVariables.spacingLayout4,
                        bottom = DSVariables.spacingLayout9 + DSVariables.spacingLayout4
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HeroSection(
                        scheme = scheme,
                        props = HeroSectionProps(),
                        scrollState = scrollState
                    )

                    ODSBox(height = DSVariables.spacingLayout4) {}

                    InfoCardList(
                        scheme = scheme
                    )
                }
            }

            BottomBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                scheme = scheme,
                props = BottomBarProps(),
                onAllowClick = handleAllowClick
            )
        }
    }
}

/**
 * Helper function to check usage stats permission
 */
private fun checkUsageStatsPermission(context: android.content.Context): Boolean {
    val appOps = context.getSystemService(android.content.Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
}
