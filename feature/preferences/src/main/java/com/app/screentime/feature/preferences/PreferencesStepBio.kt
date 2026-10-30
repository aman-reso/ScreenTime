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
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldMode
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSupportMessageProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Step: Bio Preference.
 */
@Composable
fun PreferencesStepBio(
    bio: String,
    onBioChange: (String) -> Unit,
    onSubmit: () -> Unit = {},
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    val primaryTextColor = scheme.basicTextDominant
    val secondaryTextColor = scheme.basicTextRecessive

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = 28.dp,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 8.dp,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.pref_write_bio_title),
                style = ODSTextStyles.bodyL,
                color = scheme.basicText
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.pref_write_bio_subtitle),
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        ODSTextField(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSTextFieldProps(
                label = stringResource(R.string.pref_step_bio_label),
                placeholderText = stringResource(R.string.pref_add_a_bio),
                inputText = bio,
                mode = if (!error.isNullOrBlank()) ODSTextFieldMode.ERROR else ODSTextFieldMode.STANDARD,
                supportMessageProps = if (!error.isNullOrBlank()) ODSTextFieldSupportMessageProps(
                    message = error
                ) else null,
                size = ODSTextFieldSize.LARGE
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSubmit() }),
            onValueChange = onBioChange
        )
    }
}
