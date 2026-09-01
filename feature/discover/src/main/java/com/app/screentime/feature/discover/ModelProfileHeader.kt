package com.app.screentime.feature.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.ModelProfile
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ModelProfileHeader(
    model: ModelProfile,
    isFavorite: Boolean,
    scheme: ODSTheme,
    onBackClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(340.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Main Model Hero Image
        ODSBox(
            modifier = Modifier.fillMaxSize(),
            cornerRadius = ODSCorners(all = 16.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            border = ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
            )
        ) {
            val imgUrl = model.coverUrl.ifBlank { model.avatarUrl }
            if (imgUrl.isNotBlank()) {
                ODSImage(
                    imageModel = ODSImageModel(url = imgUrl),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = model.name.take(1).uppercase(),
                        style = ODSTextStyles.titleL,
                        color = scheme.functionalDestructiveStandard
                    )
                }
            }
        }

        // Top Navigation Buttons (Back Icon: arrow_right rotated 180°, Favorite Heart)
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            ODSBox(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBackClick),
                background = listOf(ODSColorModel(hexColor = HexColor(0x991E1145))),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.arrow_right),
                    tint = HexColor(0xFFFFFFFF).getColor(),
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(180f)
                )
            }

            // Favorite Button
            ODSBox(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onFavoriteToggle),
                background = listOf(ODSColorModel(hexColor = HexColor(0x991E1145))),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder
                    ),
                    tint = if (isFavorite) scheme.functionalDestructiveStandard.getColor() else HexColor(0xFFFFFFFF).getColor(),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Bottom Image Badges (Online Status & Rating)
        ODSRow(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp),
            gap = 8.dp,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSTagStatic(
                scheme = scheme,
                props = ODSTagStaticProps(
                    label = if (model.isOnline) "● Online" else "Offline",
                    type = if (model.isOnline) ODSTagStaticType.SUCCESS else ODSTagStaticType.SUBTLE
                )
            )

            ODSTagStatic(
                scheme = scheme,
                props = ODSTagStaticProps(
                    label = "⭐ ${model.rating} (${model.reviewCount})",
                    type = ODSTagStaticType.SAVINGS
                )
            )
        }
    }
}
