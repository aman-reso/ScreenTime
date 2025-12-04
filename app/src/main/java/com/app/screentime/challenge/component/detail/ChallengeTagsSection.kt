package com.app.screentime.challenge.component.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Tags section displaying challenge tags and duration.
 */
@Composable
fun ChallengeTagsSection(
    tags: List<String>,
    scheme: ODSTheme = neutralScheme
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        gap = DSVariables.spacingComponent2,
        horizontalArrangement = Arrangement.Start
    ) {
        tags.take(2).forEach { tag ->
            ODSTagStatic(
                scheme = scheme,
                props = ODSTagStaticProps(
                    label = tag,
                    type = ODSTagStaticType.SUBTLE
                )
            )
        }
    }
}

