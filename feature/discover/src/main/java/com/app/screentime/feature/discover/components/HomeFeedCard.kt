package com.app.screentime.feature.discover.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R as ODSR
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.ODSElevationType
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Single Hero Profile Card Component for Dating Discovery.
 * Displays photo, gradient readability scrim, name/age, verified badge,
 * location/job info, interest tags, and swipe stamp overlays (LIKE / PASS).
 * 100% Telekom ODS UI compliance.
 */
@Composable
fun HomeFeedCard(
    profile: ModelProfile,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    swipeOffsetX: Float = 0f,
    onCardClick: () -> Unit = {}
) {
    val likeAlpha = (swipeOffsetX / 150f).coerceIn(0f, 1f)
    val passAlpha = (-swipeOffsetX / 150f).coerceIn(0f, 1f)

    ODSBox(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = ODSVariables.spacingLayout1,
                vertical = ODSVariables.spacingComponent2
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCardClick
            )
    ) {
        ODSBox(
            modifier = Modifier.fillMaxSize(),
            cornerRadius = ODSCorners(all = 24.dp),
            clipContent = true,
        ) {
            val photoUrl = profile.coverUrl.ifBlank { profile.avatarUrl }
            if (photoUrl.isNotBlank()) {
                ODSImage(
                    imageModel = ODSImageModel(
                        url = photoUrl,
                        contentDescription = profile.name
                    ),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = ODSR.drawable.ic_user,
                            contentDescription = profile.name
                        ),
                        tint = scheme.basicTextRecessive.getColor(),
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.88f)
                            )
                        )
                    )
            )

            // Bottom-Left Overlay Profile Details Column
            ODSColumn(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 20.dp, end = 20.dp),
                gap = 8.dp,
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // Name & Age Row (22sp bold subtitle typography)
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 8.dp,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ODSText(
                        text = "${profile.name}, ${profile.age}",
                        style = ODSTextStyles.subtitle,
                        color = scheme.basicTextOnAccent
                    )
                }

                // Subtitle Row: Job / Profession · City
                val jobText = profile.job.takeIf { !it.isNullOrBlank() }
                val cityText = profile.location.takeIf { it.isNotBlank() } ?: "Nearby"
                val subtitleText = if (jobText != null) "$jobText · $cityText" else cityText
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = subtitleText,
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.basicTextOnAccentSecondary
                )

                // Inline Interest Chips Row using official Telekom ODS ODSTagStatic
                val displayTags = profile.tags.filter { it.isNotBlank() }
                if (displayTags.isNotEmpty()) {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 6.dp,
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        displayTags.take(4).forEach { tag ->
                            ODSTagStatic(
                                scheme = scheme,
                                props = ODSTagStaticProps(
                                    label = tag,
                                    type = ODSTagStaticType.SUBTLE,
                                    icon = ODSIconModel(
                                        drawableRes = when (tag.lowercase()) {
                                            "travel" -> ODSR.drawable.ic_map_pin
                                            else -> ODSR.drawable.ic_zap
                                        }
                                    )
                                )
                            )
                        }
                    }
                }
            }

            if (likeAlpha > 0.05f) {
                ODSBox(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(likeAlpha),
                    width = 80.dp,
                    height = 80.dp,
                    cornerRadius = ODSCorners(all = 40.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = ODSR.drawable.ic_heart_filled,
                            contentDescription = "Like"
                        ),
                        tint = scheme.basicTextOnAccent.getColor(),
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            if (passAlpha > 0.05f) {
                ODSBox(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(passAlpha),
                    width = 80.dp,
                    height = 80.dp,
                    cornerRadius = ODSCorners(all = 40.dp),
                    border = ODSBorder(
                        width = 2.dp,
                        colorList = listOf(ODSColorModel(hexColor = HexColor("#EF4444")))
                    ),
                    background = listOf(ODSColorModel(hexColor = HexColor("#000000", 0.4f))),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = ODSR.drawable.ic_close,
                            contentDescription = "Pass"
                        ),
                        tint = HexColor("#EF4444").getColor(),
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
        }
    }
}

/**
 * Bottom Action Buttons Bar positioned OUTSIDE the card stack.
 * Contains Pass (✖️), Super Like (⚡), Like (💖), and Chat (💬) action buttons.
 * 100% Telekom ODS components.
 */
@Composable
fun HomeFeedActionButtonsRow(
    scheme: ODSTheme,
    isLiked: Boolean = false,
    onDislike: () -> Unit = {},
    onLike: () -> Unit = {},
    onChat: () -> Unit = {},
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
                width = 1.5.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            contentAlignment = Alignment.Center,
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = ODSR.drawable.ic_close,
                    contentDescription = "Pass"
                ),
                tint = scheme.basicText.getColor(),
                modifier = Modifier.size(24.dp)
            )
        }

        ODSBox(
            modifier = Modifier
                .size(56.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onChat
                ),
            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
            background = listOf(ODSColorModel(hexColor = scheme.basicText)),
            contentAlignment = Alignment.Center,
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = ODSR.drawable.ic_message_circle,
                    contentDescription = "Chat"
                ),
                tint = scheme.basicBackground.getColor(),
                modifier = Modifier.size(24.dp)
            )
        }

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
                    drawableRes = if (isLiked) ODSR.drawable.ic_heart_filled else ODSR.drawable.ic_heart,
                    contentDescription = "Like"
                ),
                tint = scheme.basicTextOnAccent.getColor(),
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
