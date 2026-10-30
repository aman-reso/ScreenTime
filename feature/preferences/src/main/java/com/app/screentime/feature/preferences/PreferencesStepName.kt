package com.app.screentime.feature.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldMode
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSupportMessageProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Step 1: Name Preference.
 * Matches Figma node-id 23-6.
 */
@Composable
fun PreferencesStepName(
    name: String,
    onNameChange: (String) -> Unit,
    onSubmit: () -> Unit = {},
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    val primaryTextColor = scheme.basicText
    val secondaryTextColor = scheme.basicTextRecessive

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = 28.dp,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // ── 1. Heading & Subtitle ──────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 8.dp,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.pref_whats_your_name),
                style = ODSTextStyles.bodyL,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.pref_whats_your_name_subtitle),
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        // ── 2. Name Input Card ─────────────────────────────────────────────
        ODSTextField(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSTextFieldProps(
                label = stringResource(R.string.pref_step_name_label),
                placeholderText = stringResource(R.string.pref_enter_your_first_name),
                inputText = name,
                mode = if (!error.isNullOrBlank()) ODSTextFieldMode.ERROR else ODSTextFieldMode.STANDARD,
                supportMessageProps = if (!error.isNullOrBlank()) ODSTextFieldSupportMessageProps(message = error) else null,
                size = ODSTextFieldSize.LARGE
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSubmit() }),
            onValueChange = onNameChange
        )
    }
}
