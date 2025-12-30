package com.app.screentime.challenge.component.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.R.drawable
import com.telekom.odsystem.neutralScheme

/**
 * Header with back arrow, challenge title, and share icon.
 */
@Composable
fun ChallengeHeader(
    title: String,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    scheme: ODSTheme = neutralScheme
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSButton(
            scheme = scheme,
            props = ODSButtonProps(
                buttonIcon = ODSIconModel(
                    drawableRes = drawable.left_condensed_type_standard_size_standard,
                    tint = scheme.basicText,
                    contentDescription = "Back"
                ),
                buttonType = ODSButtonButtonType.ICON_ONLY,
                variant = ODSButtonVariant.GHOST,
                size = ODSButtonSize.SMALL
            ),
            onClick = onBackClick
        )

        ODSText(
            text = title,
            style = DSTextStyles.oxBodyL,
            color = scheme.basicText,
            modifier = Modifier.weight(1f)
        )

        ODSButton(
            scheme = scheme,
            props = ODSButtonProps(
                buttonIcon = ODSIconModel(
                    drawableRes = drawable.share_type_standard_size_standard,
                    tint = scheme.basicText,
                    contentDescription = "Share"
                ),
                buttonType = ODSButtonButtonType.ICON_ONLY,
                variant = ODSButtonVariant.GHOST,
                size = ODSButtonSize.SMALL
            ),
            onClick = onShareClick
        )
    }
}

