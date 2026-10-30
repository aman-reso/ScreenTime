package com.app.screentime.feature.profile

import androidx.compose.runtime.Composable
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

public typealias AppLanguageOption = com.app.screentime.core.ui.components.AppLanguageOption
public val DEFAULT_APP_LANGUAGES = com.app.screentime.core.ui.components.DEFAULT_APP_LANGUAGES

/**
 * Re-exported AppLanguageBottomSheet for feature:profile compatibility.
 */
@Composable
fun AppLanguageBottomSheet(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    scheme: ODSTheme = zonaODSTheme,
    languages: List<AppLanguageOption> = DEFAULT_APP_LANGUAGES
) {
    com.app.screentime.core.ui.components.AppLanguageBottomSheet(
        currentLanguage = currentLanguage,
        onLanguageCodeSelected = onLanguageSelected,
        onDismiss = onDismiss,
        scheme = scheme,
        languages = languages
    )
}
