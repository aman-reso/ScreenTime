package com.app.screentime.profile.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.*

/**
 * Theme selection dialog with radio buttons
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectionDialog(
    currentTheme: String,
    onDismiss: () -> Unit,
    onThemeSelected: (String) -> Unit
) {
    val themes = listOf(
        ThemeOption(stringResource(R.string.theme_light), "Light"),
        ThemeOption(stringResource(R.string.theme_dark), "Dark"),
        ThemeOption(stringResource(R.string.theme_system), "System")
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(15.dp),
            color = CardColor
        ) {
            AlertDialogContent(
                themes = themes,
                currentTheme = currentTheme,
                onThemeSelected = onThemeSelected,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun AlertDialogContent(
    themes: List<ThemeOption>,
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppText(
            text = stringResource(R.string.select_theme),
            style = AppTextStyle.SubTitle,
            fontWeight = FontWeight.Bold,
            color = TitleTextColor
        )

        themes.forEach { theme ->
            ThemeRadioOption(
                text = theme.displayName,
                selected = currentTheme == theme.value,
                onClick = { onThemeSelected(theme.value) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = PrimaryGreen
                )
            ) {
                AppText(
                    text = stringResource(R.string.close),
                    style = AppTextStyle.Body,
                    color = PrimaryGreen
                )
            }
        }
    }
}


@Composable
private fun ThemeRadioOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) PrimaryGreen.copy(alpha = 0.15f)
                else CardColor
            )
            .border(
                width = if (selected) 1.dp else 0.dp,
                brush = Brush.linearGradient(
                    listOf(
                        PrimaryGreen.copy(alpha = 0.6f),
                        PrimaryGreen.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppText(
            text = text,
            style = AppTextStyle.Body,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) PrimaryGreen else BodyTextColor
        )

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = PrimaryGreen,
                unselectedColor = MutedTextColor
            )
        )
    }
}

private data class ThemeOption(
    val displayName: String,
    val value: String
)

