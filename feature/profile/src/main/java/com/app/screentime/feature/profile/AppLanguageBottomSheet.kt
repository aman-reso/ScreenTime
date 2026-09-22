package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

/**
 * App Language Bottom Sheet (Figma node-id 56-120).
 * Allows users to choose the active application display language.
 *
 * Rules:
 * 1. 100% ODS components + ModalBottomSheet.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Max text size 16sp with Funnel Sans font family.
 * 4. Padding, margins, gaps, and corner radii use ODSVariables.
 * 5. Clean modular component breakdown.
 */

data class AppLanguageOption(
    val englishName: String,
    val nativeName: String
)

val DEFAULT_APP_LANGUAGES = listOf(
    AppLanguageOption(englishName = "English", nativeName = "English"),
    AppLanguageOption(englishName = "Spanish", nativeName = "Español"),
    AppLanguageOption(englishName = "French", nativeName = "Français"),
    AppLanguageOption(englishName = "German", nativeName = "Deutsch"),
    AppLanguageOption(englishName = "Japanese", nativeName = "日本語"),
    AppLanguageOption(englishName = "Hindi", nativeName = "हिन्दी")
)

@Composable
fun LanguageSheetHeader(
    onClose: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ODSText(
            text = "App Language",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )

        // Circular close button
        ODSBox(
            modifier = Modifier
                .size(36.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClose
                ),
            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
            border = ODSBorder(
                width = ODSVariables.strokes2,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
            ),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_close,
                    contentDescription = "Close"
                ),
                tint = scheme.basicTextDominant.getColor(),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun LanguageSheetItem(
    language: AppLanguageOption,
    isSelected: Boolean,
    onSelect: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect
            ),
        padding = ODSPadding(
            horizontal = ODSVariables.spacingLayout1,
            vertical = ODSVariables.spacingComponent4
        ),
        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
        border = ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(
                ODSColorModel(
                    hexColor = if (isSelected) scheme.basicAccent else scheme.basicAccentSecondary
                )
            )
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        background = listOf(
            ODSColorModel(
                hexColor = if (isSelected) scheme.basicAccentSecondary else scheme.basicBackgroundCard
            )
        )
    ) {
        ODSRow(
            gap = ODSVariables.spacingComponent3,
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSText(
                text = language.englishName,
                style = if (isSelected) ODSTextStyles.bodyMBold else ODSTextStyles.bodyMRegular,
                color = scheme.basicTextDominant
            )
            ODSText(
                text = "(${language.nativeName})",
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )
        }

        if (isSelected) {
            ODSBox(
                modifier = Modifier.size(24.dp),
                cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_check,
                        contentDescription = "Selected"
                    ),
                    tint = scheme.basicTextOnAccent.getColor(),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun LanguageSheetList(
    languages: List<AppLanguageOption>,
    selectedLanguage: String,
    onSelectLanguage: (String) -> Unit,
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
            LanguageSheetItem(
                language = language,
                isSelected = selectedLanguage.equals(language.englishName, ignoreCase = true),
                onSelect = { onSelectLanguage(language.englishName) },
                scheme = scheme
            )
        }
    }
}

/**
 * Complete Modal Bottom Sheet for selecting the App Language.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLanguageBottomSheet(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    scheme: ODSTheme = zonaODSTheme,
    languages: List<AppLanguageOption> = DEFAULT_APP_LANGUAGES
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = scheme.basicBackgroundCard.getColor(),
        scrimColor = scheme.basicTextDominant.getColor().copy(alpha = 0.5f),
        dragHandle = {
            ODSBox(
                modifier = Modifier.padding(
                    top = ODSVariables.spacingComponent4,
                    bottom = ODSVariables.spacingComponent2
                ),
                width = 40.dp,
                height = 4.dp,
                background = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull)
            )
        }
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = ODSVariables.spacingLayout2,
                    end = ODSVariables.spacingLayout2,
                    top = ODSVariables.spacingComponent3,
                    bottom = ODSVariables.spacingLayout3
                ),
            gap = ODSVariables.spacingComponent5,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // ── 1. Header ──────────────────────────────────────────────────
            LanguageSheetHeader(
                onClose = {
                    coroutineScope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                },
                scheme = scheme
            )

            // ── 2. Languages List ──────────────────────────────────────────
            LanguageSheetList(
                languages = languages,
                selectedLanguage = currentLanguage,
                onSelectLanguage = { selected ->
                    coroutineScope.launch {
                        onLanguageSelected(selected)
                        sheetState.hide()
                        onDismiss()
                    }
                },
                scheme = scheme
            )
        }
    }
}
