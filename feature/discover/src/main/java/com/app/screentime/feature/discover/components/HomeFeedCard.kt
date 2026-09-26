package com.app.screentime.feature.discover.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * "For You" Feed Card Component (Figma node-id 29-4).
 * Rearranged according to Figma ODS RADD generator code while keeping animations intact.
 */
@Composable
fun HomeFeedCard(
    profile: ModelProfile,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    isLiked: Boolean = false,
    onCardClick: () -> Unit = {},
    onDislike: () -> Unit = {},
    onChat: () -> Unit = {},
    onLike: () -> Unit = {}
) {
    var showHeartBurst by remember { mutableStateOf(false) }
    var isDismissing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val cardAlpha by animateFloatAsState(
        targetValue = if (isDismissing) 0f else 1f,
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        label = "cardAlpha"
    )
    val cardScale by animateFloatAsState(
        targetValue = if (isDismissing) 0.82f else 1f,
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        label = "cardScale"
    )
    val likeButtonScale by animateFloatAsState(
        targetValue = if (isLiked || showHeartBurst) 1.22f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "likeButtonScale"
    )

    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .scale(cardScale)
            .alpha(cardAlpha)
            .padding(
                start = ODSVariables.spacingLayout1,
                end = ODSVariables.spacingLayout1,
                bottom = ODSVariables.spacingComponent4
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCardClick
            ),
        cornerRadius = ODSCorners(all = 20.dp),
        clipContent = true,
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
    ) {
        // Top Section: Image Box (320.dp height) + Overlaid Floating Actions
        ODSBox(
            modifier = Modifier.fillMaxWidth()
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = ODSCorners(
                    topLeft = 20.dp,
                    topRight = 20.dp,
                    bottomLeft = 0.dp,
                    bottomRight = 0.dp
                ),
                clipContent = true,
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                height = 320.dp
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
                                drawableRes = R.drawable.ic_user,
                                contentDescription = profile.name
                            ),
                            tint = scheme.basicTextRecessive.getColor(),
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }
            }

            // Match Percentage Badge (Top-Left)
            val matchPercentage = profile.matchedPreferences.filter { it.isDigit() }
                .ifBlank { ((profile.rating / 5.0f) * 100).toInt().coerceIn(70, 99).toString() }
            ODSRow(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        start = ODSVariables.spacingLayout1,
                        top = ODSVariables.spacingLayout1
                    ),
                padding = ODSPadding(
                    top = ODSVariables.spacingComponent2,
                    bottom = ODSVariables.spacingComponent2,
                    left = ODSVariables.spacingComponent3,
                    right = ODSVariables.spacingComponent3
                ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                gap = ODSVariables.spacingComponent1,
                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.ic_zap_filled),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(12.dp)
                )
                ODSText(
                    text = "$matchPercentage% Match",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicAccent
                )
            }

            // Overlaid Floating Action Buttons Column (Aligned to Image Baseline / Bottom-Right)
            ODSColumn(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 12.dp, end = 16.dp),
                gap = 10.dp,
                verticalAlignment = Alignment.Bottom,
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                // Dislike / Pass Action Button (with dismiss animation)
                ODSColumn(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            if (!isDismissing) {
                                isDismissing = true
                                coroutineScope.launch {
                                    delay(200.milliseconds)
                                    onDislike()
                                }
                            }
                        }
                    ),
                    cornerRadius = ODSCorners(all = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    background = listOf(ODSColorModel(hexColor = HexColor("#FFFFFF", 0.85f))),
                    effect = ODSEffect(
                        elevations = listOf(
                            ODSElevation(
                                x = 0,
                                y = 2,
                                blur = 8,
                                spread = 0,
                                color = HexColor(0x26000000),
                                type = ODSElevationType.DROP_SHADOW
                            )
                        )
                    ),
                    width = 44.dp,
                    height = 44.dp
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = R.drawable.ic_close),
                        tint = scheme.basicTextRecessive.getColor(),
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Like / Heart Action Button (with animation)
                ODSColumn(
                    modifier = Modifier
                        .scale(likeButtonScale)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                if (!isLiked) {
                                    showHeartBurst = true
                                    coroutineScope.launch {
                                        delay(1000)
                                        showHeartBurst = false
                                    }
                                }
                                onLike()
                            }
                        ),
                    cornerRadius = ODSCorners(all = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    background = listOf(
                        ODSColorModel(
                            hexColor = if (isLiked) scheme.basicAccentSecondary else HexColor(
                                "#FFFFFF",
                                0.85f
                            )
                        )
                    ),
                    effect = ODSEffect(
                        elevations = listOf(
                            ODSElevation(
                                x = 0,
                                y = 2,
                                blur = 8,
                                spread = 0,
                                color = HexColor(0x26000000),
                                type = ODSElevationType.DROP_SHADOW
                            )
                        )
                    ),
                    width = 44.dp,
                    height = 44.dp
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = if (isLiked) {
                                R.drawable.ic_heart_filled
                            } else {
                                R.drawable.ic_heart
                            }
                        ),
                        tint = if (isLiked) {
                            scheme.basicAccent.getColor()
                        } else {
                            scheme.basicTextRecessive.getColor()
                        },
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Primary Action Button (Chat / Message - At Image Baseline)
                ODSColumn(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onChat
                    ),
                    cornerRadius = ODSCorners(all = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    effect = ODSEffect(
                        elevations = listOf(
                            ODSElevation(
                                x = 0,
                                y = 2,
                                blur = 8,
                                spread = 0,
                                color = HexColor(0x33000000),
                                type = ODSElevationType.DROP_SHADOW
                            )
                        )
                    ),
                    width = 44.dp,
                    height = 44.dp
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = R.drawable.ic_message_circle),
                        tint = scheme.basicBackground.getColor(),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Bottom Info Section (Name, Subtitle/Location & Interest Pills)
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 8.dp,
            padding = ODSPadding(top = 12.dp, bottom = 12.dp, left = 16.dp, right = 16.dp),
            cornerRadius = ODSCorners(
                topLeft = 0.dp,
                topRight = 0.dp,
                bottomLeft = 20.dp,
                bottomRight = 20.dp
            ),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        ) {
            // Header Row: Name, Age + Verified Badge
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = 8.dp,
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ODSText(
                    text = "${profile.name}, ${profile.age}",
                    style = ODSTextStyles.titleS,
                    color = scheme.basicText
                )
            }

            val subtitleText = buildString {
                if (!profile.job.isNullOrBlank()) {
                    append(profile.job)
                    append(" · ")
                } else if (profile.location.isNotBlank()) {
                    append(profile.location)
                    append(" · ")
                }
                append(profile.distance)
            }
            ODSText(
                text = subtitleText,
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )

            // Interest / Attribute Pills Row
            val pills = buildList {
                profile.height?.takeIf { it.isNotBlank() }?.let { add(it) }
                profile.education?.takeIf { it.isNotBlank() }?.let { add(it) }
                profile.relationType?.takeIf { it.isNotBlank() }?.let { add(it) }
            }.ifEmpty { listOf("Coffee", "Travel", "Music") }

            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = 8.dp,
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                pills.take(3).forEach { pillText ->
                    ODSRow(
                        gap = 4.dp,
                        padding = ODSPadding(top = 4.dp, bottom = 4.dp, left = 8.dp, right = 8.dp),
                        cornerRadius = ODSCorners(all = 999.dp),
                        border = ODSBorder(
                            width = 1.dp,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                        ),
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                    ) {
                        ODSText(
                            text = pillText,
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicText
                        )
                    }
                }
            }
        }
    }
}
