package com.app.screentime.feature.discover

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ModelProfileBottomBar(
    scheme: ODSTheme,
    onStartChat: () -> Unit,
    onStartVoiceCall: () -> Unit,
    onStartVideoCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = 12.dp
        ) {
            // Chat Button
            ODSButton(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Chat",
                    buttonIcon = ODSIconModel(drawableRes = R.drawable.message),
                    variant = ODSButtonVariant.SECONDARY,
                    size = ODSButtonSize.SMALL,
                    buttonType = ODSButtonButtonType.ICON_ONLY
                ),
                onClick = onStartChat
            )

            // Video Call Button
            ODSButton(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Video Call",
                    buttonIcon = ODSIconModel(drawableRes = R.drawable.video),
                    variant = ODSButtonVariant.PRIMARY,
                    size = ODSButtonSize.SMALL,
                    buttonType = ODSButtonButtonType.ICON_ONLY
                ),
                onClick = onStartVideoCall
            )

            // Voice Call Button
            ODSButton(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Voice Call",
                    buttonIcon = ODSIconModel(drawableRes = R.drawable.call),
                    variant = ODSButtonVariant.OUTLINE,
                    size = ODSButtonSize.SMALL,
                    buttonType = ODSButtonButtonType.ICON_ONLY
                ),
                onClick = onStartVoiceCall
            )
        }
    }
}
