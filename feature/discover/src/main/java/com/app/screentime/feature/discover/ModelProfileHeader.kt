package com.app.screentime.feature.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onBackClick: () -> Unit = {},
    onFavoriteToggle: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        // 1. Full-Bleed Model Hero Image
        val imgUrl = model.coverUrl.ifBlank { model.avatarUrl }
        if (imgUrl.isNotBlank()) {
            ODSImage(
                imageModel = ODSImageModel(url = imgUrl),
                contentScale = ContentScale.Crop,
                cornerRadius = ODSCorners(bottomLeft = 24.dp, bottomRight = 24.dp),
                modifier = Modifier.fillMaxSize()
            )
        } else {
            ODSBox(
                modifier = Modifier.fillMaxSize(),
                cornerRadius = ODSCorners(bottomLeft = 24.dp, bottomRight = 24.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                contentAlignment = Alignment.Center
            ) {
                ODSText(
                    text = model.name.take(1).uppercase(),
                    style = ODSTextStyles.titleL,
                    color = scheme.functionalDestructiveStandard
                )
            }
        }

        // 2. Top Scrim Overlay for Status Bar and Navigation Buttons contrast
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xCC000000),
                            Color(0x77000000),
                            Color.Transparent
                        )
                    )
                )
        )

        // 3. Bottom Scrim Overlay for Tag Legibility and Smooth Transition
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x881E1145),
                            Color(0xEE1E1145)
                        )
                    )
                )
        )

        // 4. Bottom Image Badges (Online Status, Rating, Rate Pill)
        ODSRow(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSRow(gap = 8.dp, verticalAlignment = Alignment.CenterVertically) {
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

            ODSBox(
                background = listOf(ODSColorModel(hexColor = HexColor(0x991E1145))),
                cornerRadius = ODSCorners(all = 8.dp),
                border = ODSBorder(
                    width = 0.5.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                ),
                padding = ODSPadding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                ODSText(
                    text = "${model.ratePerMinute}c/min",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicAccent
                )
            }
        }
    }
}

@Composable
fun ModelProfileTopBar(
    isFavorite: Boolean,
    scheme: ODSTheme,
    onBackClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        ODSBox(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .clickable(onClick = onBackClick),
            background = listOf(ODSColorModel(hexColor = HexColor(0x991E1145))),
            border = ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
            ),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(drawableRes = R.drawable.navigation_left_type_standard_size_standard),
                tint = HexColor(0xFFFFFFFF).getColor(),
                modifier = Modifier.size(22.dp)
            )
        }

        // Favorite Button
        ODSBox(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .clickable(onClick = onFavoriteToggle),
            background = listOf(ODSColorModel(hexColor = HexColor(0x991E1145))),
            border = ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
            ),
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
}
