package com.app.screentime.feature.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileInterestsSection(
    interests: List<String>,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    if (interests.isEmpty()) return

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
                gap = ODSVariables.spacingComponent4
            ) {
                ODSText(
                    text = stringResource(R.string.profile_interests),
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextDominant
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(
                        ODSVariables.spacingComponent3
                    ),
                    verticalArrangement = Arrangement.spacedBy(ODSVariables.spacingComponent3)
                ) {
                    interests.forEachIndexed { index, interest ->
                        val isAccent = index % 2 == 0
                        ODSTagStatic(
                            scheme = scheme,
                            props = ODSTagStaticProps(
                                label = interest.replaceFirstChar { it.uppercase() },
                                type = if (isAccent) ODSTagStaticType.STRONG else ODSTagStaticType.SUBTLE
                            )
                        )
                    }
                }
            }
        }
    )
}
