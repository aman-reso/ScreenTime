package com.app.screentime.challenge.component.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.ui.Alignment
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Challenge image section with reward badge.
 */
@Composable
fun ChallengeImageSection(
    thumbnail: String?,
    reward: String,
    scheme: ODSTheme = neutralScheme
) {
    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
    ) {
        if (!thumbnail.isNullOrEmpty()) {
            ODSImage(
                modifier = Modifier.fillMaxSize(),
                imageModel = ODSImageModel(
                    url = thumbnail,
                    contentDescription = "Challenge image"
                ),
                cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
                contentScale = ContentScale.Crop
            )
        }
        ODSBox(
            modifier = Modifier.align(Alignment.BottomEnd),
            padding = ODSPadding(all = DSVariables.spacingComponent4),
            background = listOf(ODSColorModel(scheme.basicTextOnAccent)),
            cornerRadius = ODSCorners(all = DSVariables.radiusSmall)
        ) {
            ODSRow(
                gap = DSVariables.spacingComponent2,
                verticalAlignment = Alignment.CenterVertically,
                padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent3,
                    vertical = DSVariables.spacingComponent2
                )
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.Default.EmojiEvents,
                        tint = scheme.functionalWarningStandard,
                        contentDescription = null
                    ),
                    width = 18.dp,
                    height = 18.dp
                )
                ODSText(
                    text = reward,
                    style = DSTextStyles.bodySBold,
                    color = scheme.basicText
                )
            }
        }
    }
}

