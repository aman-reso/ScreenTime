package com.app.screentime.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.radiobutton.ODSRadioButton
import com.telekom.odsystem.atoms.radiobutton.ODSRadioButtonProps
import com.telekom.odsystem.atoms.radiobutton.ODSRadioButtonSize
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * App Language Option data model.
 */
data class AppLanguageOption(
    val englishName: String,
    val nativeName: String,
    val languageCode: String = "en"
)

val DEFAULT_APP_LANGUAGES = listOf(
    AppLanguageOption(englishName = "English", nativeName = "English", languageCode = "en"),
    AppLanguageOption(englishName = "Hindi", nativeName = "हिन्दी", languageCode = "hi"),
    AppLanguageOption(englishName = "Spanish", nativeName = "Español", languageCode = "es"),
    AppLanguageOption(englishName = "French", nativeName = "Français", languageCode = "fr"),
    AppLanguageOption(englishName = "German", nativeName = "Deutsch", languageCode = "de"),
    AppLanguageOption(englishName = "Japanese", nativeName = "日本語", languageCode = "ja")
)

@Composable
fun LanguageSheetItem(
    language: AppLanguageOption,
    isSelected: Boolean,
    onSelect: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val displayLabel = if (language.englishName.equals(language.nativeName, ignoreCase = true)) {
        language.englishName
    } else {
        "${language.englishName} (${language.nativeName})"
    }

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect
            ),
        padding = ODSPadding(
            horizontal = ODSVariables.spacingLayout1,
            vertical = ODSVariables.spacingComponent2
        ),
        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
    ) {
        ODSRadioButton(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSRadioButtonProps(
                label = displayLabel,
                selected = isSelected,
                size = ODSRadioButtonSize.SMALL
            ),
            onClick = onSelect
        )
    }
}

@Composable
fun LanguageSheetList(
    languages: List<AppLanguageOption>,
    selectedLanguage: String,
    onSelectLanguage: (AppLanguageOption) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent3,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start
    ) {
        languages.forEach { language ->
            val isSelected = selectedLanguage.equals(language.languageCode, ignoreCase = true) ||
                    selectedLanguage.equals(language.englishName, ignoreCase = true) ||
                    selectedLanguage.equals(language.nativeName, ignoreCase = true)

            LanguageSheetItem(
                language = language,
                isSelected = isSelected,
                onSelect = { onSelectLanguage(language) },
                scheme = scheme
            )
        }
    }
}

/**
 * Complete Modal Bottom Sheet for selecting the App Language using ODSBottomSheet.
 * 100% ODS components aligned with Telekom Design System.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLanguageBottomSheet(
    currentLanguage: String,
    onLanguageCodeSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    scheme: ODSTheme = zonaODSTheme,
    languages: List<AppLanguageOption> = DEFAULT_APP_LANGUAGES,
    showBottomSheet: Boolean = true
) {
    if (showBottomSheet) {
        ODSBottomSheet(
            scheme = scheme,
            showBottomSheet = true,
            props = ODSBottomSheetProps(showHandle = true),
            onDismissRequest = onDismiss,
            onCloseClicked = onDismiss,
            titleSlot = {
                ODSText(
                    text = "Select Language",
                    style = ODSTextStyles.bodyLBold,
                    color = scheme.basicText
                )
            },
            contentSlot = {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(
                            start = ODSVariables.spacingLayout2,
                            end = ODSVariables.spacingLayout2,
                            bottom = ODSVariables.spacingLayout3
                        ),
                    gap = ODSVariables.spacingComponent3,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    LanguageSheetList(
                        languages = languages,
                        selectedLanguage = currentLanguage,
                        onSelectLanguage = { selected ->
                            onLanguageCodeSelected(selected.languageCode)
                            onDismiss()
                        },
                        scheme = scheme
                    )
                }
            }
        )
    }
}
