package com.app.screentime.profile.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.ui.theme.ThemeType
import com.app.screentime.ui.theme.getThemeColors

/**
 * Theme selection dialog with all theme variations
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectionDialog(
    currentTheme: String,
    onDismiss: () -> Unit,
    onThemeSelected: (String) -> Unit
) {
    val colors = LocalAppColors.current ?: return

    val lightThemes = ThemeType.values().filter { !it.isDark }
    val darkThemes = ThemeType.values().filter { it.isDark }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(15.dp),
            color = colors.card
        ) {
            AlertDialogContent(
                lightThemes = lightThemes,
                darkThemes = darkThemes,
                currentTheme = currentTheme,
                onThemeSelected = onThemeSelected,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun AlertDialogContent(
    lightThemes: List<ThemeType>,
    darkThemes: List<ThemeType>,
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Title
        AppText(
            text = stringResource(R.string.select_theme),
            style = AppTextStyle.SubTitle,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Scrollable theme list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            // Light Themes Section
            item {
                AppText(
                    text = "Light Themes",
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                )
            }
            items(lightThemes) { themeType ->
                ThemePreviewOption(
                    themeType = themeType,
                    selected = currentTheme == themeType.name,
                    onClick = { onThemeSelected(themeType.name) }
                )
            }

            // Dark Themes Section
            item {
                AppText(
                    text = "Dark Themes",
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                )
            }
            items(darkThemes) { themeType ->
                ThemePreviewOption(
                    themeType = themeType,
                    selected = currentTheme == themeType.name,
                    onClick = { onThemeSelected(themeType.name) }
                )
            }
        }

        // Close button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colors.success
                )
            ) {
                AppText(
                    text = stringResource(R.string.close),
                    style = AppTextStyle.Body,
                    color = colors.success
                )
            }
        }
    }
}


@Composable
private fun ThemePreviewOption(
    themeType: ThemeType,
    selected: Boolean,
    onClick: () -> Unit
) {
    val currentColors = LocalAppColors.current ?: return
    val themeColors = getThemeColors(themeType)
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) currentColors.success.copy(alpha = 0.15f)
                else currentColors.card
            )
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) currentColors.success else currentColors.border,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Color preview box
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(themeColors.background)
                    .border(1.dp, themeColors.border, RoundedCornerShape(8.dp))
            ) {
                // Show a small preview of the theme
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    // Background color
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(themeColors.background)
                    )
                    // Card color preview
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(themeColors.card)
                            .align(Alignment.TopCenter)
                    )
                    // Primary color accent
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(themeColors.success)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
            
            Column(modifier = Modifier.weight(1f)) {
                AppText(
                    text = themeType.displayName,
                    style = AppTextStyle.Body,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected) currentColors.success else currentColors.textPrimary
                )
                AppText(
                    text = if (themeType.isDark) "Dark Mode" else "Light Mode",
                    style = AppTextStyle.Label,
                    color = currentColors.textMuted
                )
            }
        }

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = currentColors.success,
                unselectedColor = currentColors.textMuted
            )
        )
    }
}

