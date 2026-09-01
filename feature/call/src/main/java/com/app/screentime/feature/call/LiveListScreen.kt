package com.app.screentime.feature.call

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.network.dto.LiveStreamDto
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
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.cheddarSecondaryScheme

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun LiveListScreen(
    onNavigateToViewer: (streamId: String, hostId: String, hostName: String, hostAvatar: String) -> Unit,
    onNavigateToHost: () -> Unit,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    viewModel: LiveViewModel = hiltViewModel()
) {
    val api = viewModel.api
    val sessionManager = viewModel.sessionManager
    var streams by remember { mutableStateOf<List<LiveStreamDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val isModel = sessionManager.userRole == UserRole.MODEL

    LaunchedEffect(Unit) {
        val fallbackMockStreams = listOf(
            LiveStreamDto(
                stream_id = "live_demo_1",
                host_id = "m1",
                host_name = "Riya Gosh",
                host_avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=600&q=80",
                title = "Late night vibe & music 🎵",
                viewer_count = 142
            ),
            LiveStreamDto(
                stream_id = "live_demo_2",
                host_id = "m2",
                host_name = "Ananya Sharma",
                host_avatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=600&q=80",
                title = "Q&A and chill chat ✨",
                viewer_count = 89
            ),
            LiveStreamDto(
                stream_id = "live_demo_3",
                host_id = "m3",
                host_name = "Priya Kapoor",
                host_avatar = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=600&q=80",
                title = "Singing your favorite songs 🎤",
                viewer_count = 230
            )
        )

        while (isActive) {
            try {
                val token = sessionManager.token ?: ""
                val fetched = api.getLiveStreams(token)
                if (fetched.isNotEmpty()) {
                    streams = fetched
                } else if (streams.isEmpty()) {
                    streams = fallbackMockStreams
                }
            } catch (e: Exception) {
                if (streams.isEmpty()) {
                    streams = fallbackMockStreams
                }
            } finally {
                isLoading = false
            }
            delay(3000) // Poll every 3 seconds for newly started broadcasts
        }
    }

    ODSBox(
        modifier = modifier
            .fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            gap = 12.dp
        ) {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding())
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ODSColumn(
                    modifier = Modifier.weight(1f, fill = false),
                    gap = 2.dp
                ) {
                    ODSRow(
                        verticalAlignment = Alignment.CenterVertically,
                        gap = 8.dp
                    ) {
                        // Pulsing Live Indicator
                        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                        val pulseScale by infiniteTransition.animateFloat(
                            initialValue = 0.85f,
                            targetValue = 1.15f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(700, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "scale"
                        )

                        ODSBox(
                            modifier = Modifier
                                .size(10.dp)
                                .scale(pulseScale)
                                .clip(CircleShape),
                            background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
                        ) {}

                        ODSText(
                            text = "Live Shows",
                            style = ODSTextStyles.bodyLBold,
                            color = scheme.basicText
                        )
                    }

                    ODSText(
                        text = "Watch creators live, send tips & chat in real time",
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }

                // Header Compact "Go Live" Button for Models
                if (isModel) {
                    ODSBox(
                        modifier = Modifier
                            .clickable(onClick = onNavigateToHost),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                        cornerRadius = ODSCorners(all = 16.dp),
                        padding = ODSPadding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 6.dp
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(drawableRes = R.drawable.video),
                                tint = HexColor(0xFFFFFFFF).getColor(),
                                modifier = Modifier.size(14.dp)
                            )
                            ODSText(
                                text = "Go Live",
                                style = ODSTextStyles.microcopyBold,
                                color = HexColor(0xFFFFFFFF)
                            )
                        }
                    }
                }
            }

            if (isLoading) {
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = scheme.basicAccent.getColor())
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(streams) { stream ->
                        LiveStreamCard(
                            stream = stream,
                            scheme = scheme,
                            onClick = {
                                onNavigateToViewer(
                                    stream.stream_id,
                                    stream.host_id,
                                    stream.host_name,
                                    stream.host_avatar
                                )
                            }
                        )
                    }
                }
            }
        }

        // Floating Compact "Go Live" Pill for Models
        if (isModel) {
            ODSBox(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 85.dp)
                    .clickable(onClick = onNavigateToHost),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = HexColor(0x66FFFFFF)))
                ),
                cornerRadius = ODSCorners(all = 20.dp),
                padding = ODSPadding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 6.dp
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = R.drawable.video),
                        tint = HexColor(0xFFFFFFFF).getColor(),
                        modifier = Modifier.size(16.dp)
                    )
                    ODSText(
                        text = "Go Live",
                        style = ODSTextStyles.microcopyBold,
                        color = HexColor(0xFFFFFFFF)
                    )
                }
            }
        }
    }
}

@Composable
fun LiveStreamCard(
    stream: LiveStreamDto,
    scheme: ODSTheme,
    onClick: () -> Unit
) {
    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        background = listOf(ODSColorModel(hexColor = cheddarSecondaryScheme.basicBackgroundSubtle)),
        cornerRadius = ODSCorners(all = 16.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        )
    ) {
        // Thumbnail Image
        ODSImage(
            imageModel = ODSImageModel(
                url = if (stream.host_avatar.isNotBlank()) stream.host_avatar
                else "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=600&q=80",
                contentDescription = stream.host_name
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark gradient scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x33000000),
                            Color(0x00000000),
                            Color(0xCC0A0418)
                        )
                    )
                )
        )

        // Top Badges (LIVE + Viewer Count)
        ODSRow(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "● LIVE" Badge
            ODSBox(
                background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard)),
                cornerRadius = ODSCorners(all = 8.dp),
                padding = ODSPadding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                ODSText(
                    text = "● LIVE",
                    style = ODSTextStyles.microcopyBold,
                    color = HexColor(0xFFFFFFFF)
                )
            }

            // Viewer Count Badge
            ODSBox(
                background = listOf(ODSColorModel(hexColor = HexColor(0x99000000))),
                cornerRadius = ODSCorners(all = 8.dp),
                padding = ODSPadding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 4.dp
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = R.drawable.happy_person_type_bold_size_standard),
                        tint = HexColor(0xFFFFFFFF).getColor(),
                        modifier = Modifier.size(12.dp)
                    )
                    ODSText(
                        text = "${stream.viewer_count}",
                        style = ODSTextStyles.microcopyBold,
                        color = HexColor(0xFFFFFFFF)
                    )
                }
            }
        }

        // Bottom Info (Title & Creator Name)
        ODSColumn(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(10.dp),
            gap = 2.dp
        ) {
            ODSText(
                text = stream.title.ifBlank { "Live Stream" },
                style = ODSTextStyles.bodyMBold,
                color = HexColor(0xFFFFFFFF)
            )
            ODSText(
                text = stream.host_name.ifBlank { "Creator" },
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicAccent
            )
        }
    }
}
