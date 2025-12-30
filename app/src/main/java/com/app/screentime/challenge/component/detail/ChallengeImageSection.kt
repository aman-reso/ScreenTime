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
import androidx.compose.ui.unit.Dp
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
    height: Dp = 240.dp
) {
    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
    ) {
        if (!thumbnail.isNullOrEmpty()) {
            ODSImage(
                modifier = Modifier.fillMaxSize(),
                imageModel = ODSImageModel(
                    url = thumbnail,
                    contentDescription = "Challenge image"
                ),
                cornerRadius = ODSCorners(all = DSVariables.spacingComponent2),
                contentScale = ContentScale.Crop
            )
        }
    }
}

