package com.app.screentime.challenge.component.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Join button section.
 */
@Composable
fun JoinButtonSection(
    isJoining: Boolean,
    onJoinClick: () -> Unit,
    scheme: ODSTheme = neutralScheme
) {
    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        padding = ODSPadding(all = DSVariables.spacingComponent5)
    ) {
        ODSButton(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSButtonProps(
                label = "Join Challenge",
                variant = ODSButtonVariant.PRIMARY,
                disabled = isJoining,
                size = ODSButtonSize.LARGE,
                buttonIcon = if (isJoining) null else ODSIconModel(
                    imageVector = Icons.Default.EmojiEvents,
                    tint = scheme.basicTextOnAccent,
                    contentDescription = "Join"
                )
            ),
            onClick = onJoinClick
        )
    }
}

