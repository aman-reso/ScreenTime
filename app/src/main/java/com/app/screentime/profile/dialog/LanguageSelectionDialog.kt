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
import com.app.screentime.ui.theme.LocalAppColors

/**
 * Language selection dialog with radio buttons
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    val colors = LocalAppColors.current ?: return

    val languages = listOf(
        LanguageOption(stringResource(R.string.language_english), "en", "🇬🇧"),
        LanguageOption(stringResource(R.string.language_hindi), "hi", "🇮🇳"),
        LanguageOption(stringResource(R.string.language_bengali), "bn", "🇮🇳")
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
            color = colors.card
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppText(
                    text = stringResource(R.string.select_language),
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )

                languages.forEach { language ->
                    LanguageRadioOption(
                        text = language.displayName,
                        emoji = language.emoji,
                        selected = currentLanguage == language.value,
                        onClick = { onLanguageSelected(language.value) }
                    )
                }

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
    }
}


@Composable
private fun LanguageRadioOption(
    text: String,
    emoji: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) colors.success.copy(alpha = 0.15f)
                else colors.card
            )
            .border(
                width = if (selected) 1.dp else 0.dp,
                brush = Brush.linearGradient(
                    listOf(
                        colors.success.copy(alpha = 0.6f),
                        colors.success.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppText(
                text = emoji,
                style = AppTextStyle.Title,
                modifier = Modifier.padding(start = 4.dp)
            )
            AppText(
                text = text,
                style = AppTextStyle.Body,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) colors.success else colors.textSecondary
            )
        }

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = colors.success,
                unselectedColor = colors.textMuted
            )
        )
    }
}

private data class LanguageOption(
    val displayName: String,
    val value: String,
    val emoji: String
)

