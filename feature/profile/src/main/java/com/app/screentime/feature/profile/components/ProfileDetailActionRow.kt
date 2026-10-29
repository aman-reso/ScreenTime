package com.app.screentime.feature.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Bottom action dock matching HomeFeedScreen styling:
 * Contains Pass (✖️) and Like (💖) circular 56dp action buttons.
 */
@Composable
fun ProfileDetailActionRow(
    isLiked: Boolean,
    scheme: ODSTheme,
    onDislike: () -> Unit,
    onLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = ODSVariables.spacingLayout1,
                vertical = ODSVariables.spacingComponent3
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ODSBox(
            modifier = Modifier
                .size(56.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDislike
                ),
            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
            border = ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
            contentAlignment = Alignment.Center,
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_close,
                    contentDescription = "Not Interested"
                ),
                tint = scheme.basicText.getColor(),
                modifier = Modifier.size(24.dp)
            )
        }

        // Like Button
        ODSBox(
            modifier = Modifier
                .size(56.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onLike
                ),
            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
            contentAlignment = Alignment.Center,
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = if (isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart,
                    contentDescription = "Like"
                ),
                tint = scheme.basicTextOnAccent.getColor(),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
