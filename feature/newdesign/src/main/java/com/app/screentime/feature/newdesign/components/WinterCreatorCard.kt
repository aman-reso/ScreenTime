package com.app.screentime.feature.newdesign.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

enum class CreatorStatus {
    AVAILABLE,
    IN_A_CALL
}

object WinterCreatorCardDefaults {
    val Width: Dp = 171.dp
    val Height: Dp = 228.dp
    val CornerRadius: Dp = 22.dp

    // Figma Design Tokens (Node 51:1636 "Luma / Creator card")
    val PillBackground: Color @Composable @ReadOnlyComposable get() = Color(0x99000000) // rgba(0,0,0,0.60)
    val PillBorder: Color @Composable @ReadOnlyComposable get() = Color(0x21FFFFFF)     // rgba(255,255,255,0.13)
    val DotAvailable: Color @Composable @ReadOnlyComposable get() = WinterTokens.statusSuccess
    val DotInCall: Color @Composable @ReadOnlyComposable get() = WinterTokens.controlThumb
    val NameColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.controlThumb
    val SubtitleColor: Color @Composable @ReadOnlyComposable get() = Color(0xFFAAAAAA)
    val GradientStart: Color @Composable @ReadOnlyComposable get() = WinterTokens.overlayPortraitClear
    val GradientEnd: Color @Composable @ReadOnlyComposable get() = WinterTokens.overlayPortraitDark
}

/**
 * WinterCreatorCard - Pure Jetpack Compose implementation of Figma "Luma / Creator card" (Node 51:293).
 *
 * @param name Creator display name (e.g. "Maya Rose").
 * @param subtitle Age & profile CTA (e.g. "28 · View profile ↗").
 * @param photoUrl URL for the creator's full portrait photograph.
 * @param drawableResId Optional local drawable resource ID for instant high-fidelity asset rendering.
 * @param status Availability status ([CreatorStatus.AVAILABLE] or [CreatorStatus.IN_A_CALL]).
 * @param onClick Optional tap callback.
 * @param modifier Applied to card container.
 */
@Composable
fun WinterCreatorCard(
    name: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    photoUrl: String? = null,
    drawableResId: Int? = null,
    status: CreatorStatus = CreatorStatus.AVAILABLE,
    onClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(WinterCreatorCardDefaults.CornerRadius)
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .height(WinterCreatorCardDefaults.Height)
            .clip(shape)
            .background(WinterTokens.surfaceCard, shape)
            .then(clickableModifier)
    ) {
        // Background Photo
        if (drawableResId != null) {
            Image(
                painter = painterResource(id = drawableResId),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (photoUrl != null) {
            AsyncImage(
                model = photoUrl,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Default placeholder surface gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(WinterTokens.surfaceRaised, WinterTokens.backgroundCanvas)
                        )
                    )
            )
        }

        // Bottom Portrait Shading Gradient (124dp from Figma node 51:288)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(124.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            WinterCreatorCardDefaults.GradientStart,
                            WinterCreatorCardDefaults.GradientEnd
                        )
                    )
                )
        )

        // Top Availability Status Pill (Figma node 51:285)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(WinterCreatorCardDefaults.PillBackground)
                .border(1.dp, WinterCreatorCardDefaults.PillBorder, RoundedCornerShape(100.dp))
                .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Status dot (5dp)
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(
                            if (status == CreatorStatus.AVAILABLE) {
                                WinterCreatorCardDefaults.DotAvailable
                            } else {
                                WinterCreatorCardDefaults.DotInCall
                            }
                        )
                )

                Text(
                    text = if (status == CreatorStatus.AVAILABLE) "Available" else "In a call",
                    color = WinterTokens.controlThumb,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 14.sp
                )
            }
        }

        // Bottom Creator Identity Column (Figma node 51:289)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = name,
                color = WinterCreatorCardDefaults.NameColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.3).sp,
                lineHeight = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = subtitle,
                color = WinterCreatorCardDefaults.SubtitleColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WinterCreatorCardPreview() {
    Surface(color = WinterTokens.backgroundCanvas) {
        WinterCreatorCard(
            name = "Maya Rose",
            subtitle = "28 · View profile ↗",
            photoUrl = null,
            status = CreatorStatus.AVAILABLE
        )
    }
}
