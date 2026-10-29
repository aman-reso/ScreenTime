package com.app.screentime.feature.profile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.screentime.feature.profile.ProfilePromptUiModel
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ProfilePromptCard(
    prompt: ProfilePromptUiModel,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSCardBasic(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ODSVariables.spacingLayout1),
        scheme = scheme,
        props = ODSCardBasicProps(),
        contentPadding = ODSPadding(all = ODSVariables.spacingLayout1),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent2
            ) {
                ODSText(
                    text = prompt.question,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
                ODSText(
                    text = "“${prompt.answer}”",
                    style = ODSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )
            }
        }
    )
}
