package com.app.screentime.feature.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.ui.theme.ZonaColors
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles

/**
 * Match Modal popup when both users like each other.
 */
@Composable
fun MatchModal(
    matchedModel: ModelProfile,
    onChat: () -> Unit,
    onKeepBrowsing: () -> Unit
) {
    ODSBox(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onKeepBrowsing
            ),
        background = listOf(
            ODSColorModel(ZonaColors.MediaOverlay)
        ),
        contentAlignment = Alignment.Center
    ) {
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            background = listOf(
                ODSColorModel(ZonaColors.Surface)
            ),
            cornerRadius = ODSCorners(all = 28.dp),
            border = ODSBorder(
                width = 2.dp,
                colorList = listOf(
                    ODSColorModel(ZonaColors.ActionPrimary)
                )
            ),
            padding = ODSPadding(all = 24.dp)
        ) {
            ODSColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 14.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                ODSText(
                    text = "It's a Match! ⚡",
                    style = ODSTextStyles.titleM,
                    color = ZonaColors.ActionPrimary
                )

                ODSText(
                    text = "You and ${matchedModel.name} liked each other!",
                    style = ODSTextStyles.bodyMRegular,
                    color = ZonaColors.TextSecondary
                )

                ODSImage(
                    imageModel = ODSImageModel(
                        url = matchedModel.avatarUrl.ifBlank { matchedModel.coverUrl },
                        contentDescription = matchedModel.name
                    ),
                    modifier = Modifier.size(100.dp),
                    cornerRadius = ODSCorners(all = 50.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(10.dp))

                ODSButton(
                    scheme = zonaODSTheme,
                    props = ODSButtonProps(
                        label = "Send Message",
                        variant = ODSButtonVariant.PRIMARY,
                        size = ODSButtonSize.LARGE
                    ),
                    onClick = onChat,
                    modifier = Modifier.fillMaxWidth()
                )

                ODSButton(
                    scheme = zonaODSTheme,
                    props = ODSButtonProps(
                        label = "Keep Browsing",
                        variant = ODSButtonVariant.GHOST,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onKeepBrowsing,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
