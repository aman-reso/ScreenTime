package com.app.screentime.feature.profile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ProfileAboutMeSection(
    aboutMeText: String,
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
                gap = ODSVariables.spacingComponent3
            ) {
                ODSText(
                    text = stringResource(R.string.profile_about_me),
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextRecessive
                )
                ODSText(
                    text = aboutMeText,
                    style = ODSTextStyles.bodyL,
                    color = scheme.basicText
                )
            }
        }
    )
}
