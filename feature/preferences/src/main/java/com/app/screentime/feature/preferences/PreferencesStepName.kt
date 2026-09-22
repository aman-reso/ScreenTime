package com.app.screentime.feature.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

/**
 * Step 1: Name Preference.
 * Recreated with 100% ODS components strictly following GEMINI.md rules.
 */

@Composable
fun NameHeader(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "First off, what should we call you?",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "This is how your name will appear on your profile card.",
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun NameInputCard(
    name: String,
    onNameChange: (String) -> Unit,
    onSubmit: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val funnelFontSemiBold = remember {
        FontFamily(Font(R.font.funnelsans_semibold, FontWeight.SemiBold))
    }

    ODSBox(
        modifier = modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
        border = ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        ),
        padding = ODSPadding(
            horizontal = ODSVariables.spacingLayout1,
            vertical = ODSVariables.spacingComponent4
        )
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent2
        ) {
            ODSText(
                text = "YOUR FIRST NAME",
                style = ODSTextStyles.microcopyBold,
                color = scheme.basicTextRecessive
            )

            BasicTextField(
                value = name,
                onValueChange = onNameChange,
                textStyle = TextStyle(
                    color = scheme.basicTextDominant.getColor(),
                    fontSize = 16.sp,
                    fontFamily = funnelFontSemiBold,
                    fontWeight = FontWeight.SemiBold
                ),
                cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun NamePrivacyNote(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent3,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSIcon(
            iconModel = ODSIconModel(
                drawableRes = R.drawable.ic_shield_check,
                contentDescription = "Verified"
            ),
            tint = scheme.basicAccent.getColor(),
            modifier = Modifier.size(ODSVariables.spacingComponent5)
        )
        ODSText(
            modifier = Modifier.weight(1f),
            text = "For security and authentic matches, names cannot be altered after photo verification.",
            style = ODSTextStyles.microcopyRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun PreferencesStepName(
    name: String,
    onNameChange: (String) -> Unit,
    onSubmit: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent6
    ) {
        NameHeader(scheme = scheme)
        NameInputCard(
            name = name,
            onNameChange = onNameChange,
            onSubmit = onSubmit,
            scheme = scheme
        )
        NamePrivacyNote(scheme = scheme)
    }
}
