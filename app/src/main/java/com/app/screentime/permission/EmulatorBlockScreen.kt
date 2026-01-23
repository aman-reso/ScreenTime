package com.app.screentime.permission

import android.app.Activity
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.app.screentime.ui.theme.LocalThemeMode
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Full-screen block shown when the app is run on an emulator in release build.
 * User cannot proceed; back press triggers onBack (e.g. finish).
 */
@Composable
fun EmulatorBlockScreen(
    onBack: () -> Unit,
    scheme: ODSTheme = neutralScheme
) {
    val activity = LocalActivity.current
    val useDarkTheme = LocalThemeMode.current

    SideEffect {
        if (activity is AppCompatActivity) {
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

    BackHandler(onBack = onBack)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(DSVariables.spacingComponent4),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent4)
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    imageVector = Icons.Outlined.PhoneAndroid,
                    tint = scheme.basicTextRecessive,
                    contentDescription = null
                ),
                modifier = Modifier.size(48.dp)
            )
            ODSText(
                text = stringResource(R.string.app_not_available_on_emulator),
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicText,
                textAlign = TextAlign.Center
            )
        }
    }
}

