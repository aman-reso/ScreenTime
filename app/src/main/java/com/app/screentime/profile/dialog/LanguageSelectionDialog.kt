package com.app.screentime.profile.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.app.screentime.profile.utils.LanguageUtils
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.controls.ODSControlsType
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControls
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsProps
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardchoicesimple.ODSCardChoiceSimple
import com.telekom.odsystem.organisms.cardchoicesimple.ODSCardChoiceSimpleProps
import com.telekom.odsystem.organisms.cardchoicesimple.ODSCardChoiceSimpleType
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Language selection dialog with radio buttons
 * Languages are displayed alphabetically
 * Shows 2 languages per row on wider screens
 */
@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onDismiss: () -> Unit = {},
    onLanguageSelected: (String) -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isExpandedScreen = windowSizeClass.isWidthAtLeastBreakpoint(840)

    // Get sorted languages alphabetically
    val sortedLanguages = LanguageUtils.getSortedLanguages(context)
    
    /**
     * Helper function to compare language codes
     * Handles both simple codes (e.g., "en") and region-specific codes (e.g., "pt-rBR", "pt-BR")
     */
    fun isLanguageSelected(languageValue: String, currentLang: String): Boolean {
        // Normalize both codes: convert "-r" to "-" and lowercase for comparison
        // This handles cases like "pt-rBR" vs "pt-BR"
        val normalizedCurrent = currentLang.lowercase().replace("-r", "-")
        val normalizedLanguage = languageValue.lowercase().replace("-r", "-")
        
        // Direct match after normalization
        if (normalizedCurrent == normalizedLanguage) return true
        
        // Handle case where current language is base (no region) and language has region
        // e.g., currentLang = "pt", languageValue = "pt-rBR" should match
        // This happens when system returns just "pt" but we have "pt-rBR" in LanguageUtils
        val currentParts = normalizedCurrent.split("-")
        val languageParts = normalizedLanguage.split("-")
        
        // If both have the same base language and current has no region, match
        if (currentParts[0] == languageParts[0] && currentParts.size == 1 && languageParts.size > 1) {
            return true
        }
        
        return false
    }

    ODSDialog(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .heightIn(max = configuration.screenHeightDp.dp * 0.7f),
        scheme = scheme,
        onDismissRequest = onDismiss,
        props = ODSDialogProps(
            showCloseButton = true,
            showScrollbar = true,
            title = stringResource(R.string.select_language),
            bodyText = null
        ),
        contentSlot = {
            if (isExpandedScreen) {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent3
                ) {
                    sortedLanguages.chunked(2).forEach { languagePair ->
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
                        ) {
                            languagePair.forEach { language ->
                                ODSListRowControls(
                                    modifier = Modifier.weight(1f),
                                    scheme = scheme,
                                    props = ODSListRowControlsProps(
                                        variant = ODSListRowControlsVariant.STANDARD,
                                        type = ODSControlsType.RADIO_ICON,
                                        labelText = stringResource(language.displayName),
                                        selected = isLanguageSelected(language.value, currentLanguage),
                                    ),
                                    onRadioClick = {
                                        onLanguageSelected(language.value)
                                    }
                                )
                            }
                            // Add empty space if odd number of languages in last row
                            if (languagePair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            } else {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent3
                ) {
                    sortedLanguages.forEach { language ->
                        ODSListRowControls(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSListRowControlsProps(
                                variant = ODSListRowControlsVariant.STANDARD,
                                type = ODSControlsType.RADIO_ICON,
                                labelText = stringResource(language.displayName),
                                selected = isLanguageSelected(language.value, currentLanguage),
                            ),
                            onRadioClick = {
                                onLanguageSelected(language.value)
                            }
                        )
                    }
                }
            }
        }
    )
}
