//package com.telekom.odsystem.organisms.cardpromo
//
//import android.view.View
//import androidx.annotation.OptIn
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.DisposableEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateMapOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.unit.dp
//import androidx.media3.common.Player
//import androidx.media3.common.Timeline
//import androidx.media3.common.util.UnstableApi
//import androidx.media3.ui.PlayerView.SHOW_BUFFERING_ALWAYS
//import com.telekom.odsystem.atoms.ODSBox
//import com.telekom.odsystem.atoms.ODSColumn
//import com.telekom.odsystem.atoms.ODSRow
//import com.telekom.odsystem.atoms.carouseltimer.ODSCarouselTimerProps
//import com.telekom.odsystem.atoms.carouseltimer.ODSSegmentDurationModel
//import com.telekom.odsystem.atoms.videoplayer.ODSVideoPlayer
//import com.telekom.odsystem.foundations.offset
//import com.telekom.odsystem.neutralScheme
//import com.telekom.odsystem.slots.contentpanel.ODSContentPanel
//import com.telekom.odsystem.tokens.tokens.ODSTheme
//import com.telekom.odsystem.tokens.blackScheme
//
///**
// * ODS Card Promo.
// *
// * Displays a promotional card with a video player and content panel.
// * Available in CARD (bottom fade) or FADE (top fade) types.
// * Configurable video player (aspect ratio, resize mode) and content panel (title, interactions).
// *
// * @param modifier The modifier to be applied to the component.
// * @param scheme The ODSTheme for styling.
// * @param props Configuration properties for the promo card.
// * @param odsContentPanelTitleSlot Slot for the content panel's title.
// * @param onClick Callback for card click.
// * @param onPreviousClick Callback for previous button click.
// * @param onNextClick Callback for next button click.
// * @param onPlayPauseClick Callback for play/pause button click.
// * @param segmentCompleted Callback when a video segment completes.
// *
// * Code generated with ODS RADD Code Generator
// * 2025-08-04 (v1.32.3) - uid: 769d14c7
// * Figma link: https://figma.com/design/NUZnzm5wsZynHDKmg3KfVv/Untitled?node-id=669-2734
// */
//
//@Suppress("LongMethod")
//@OptIn(UnstableApi::class)
//@Composable
//fun ODSCardPromo(
//    modifier: Modifier = Modifier,
//    scheme: ODSTheme = neutralScheme,
//    props: ODSCardPromoProps = ODSCardPromoProps(),
//    odsContentPanelTitleSlot: (@Composable () -> Unit)? = null,
//    onClick: () -> Unit = {},
//    onPreviousClick: () -> Unit = {},
//    onNextClick: () -> Unit = {},
//    onPlayPauseClick: () -> Unit = {},
//    segmentCompleted: (Int) -> Unit = {}
//) {
//
//    val style = ODSCardPromoStyle().getStyle(scheme = scheme, props = props)
//
//    ODSBox(
//        modifier = modifier,
//        clipContent = style.zStackClipContent != false,
//        contentAlignment = style.zStackContentAlignment,
//        cornerRadius = style.cornerRadius, // This has been added to add radius to video player
//    ) {
//        props.videoPlayerProps?.let {
//            ODSVideoPlayer(
//                modifier = Modifier
//                    .matchParentSize()
//                    .aspectRatio(props.aspectRatio.value)
//                    .clip(
//                        style.cornerRadius?.getRoundedCornerShape() ?: RoundedCornerShape(0.dp)
//                    ), // Video player radius needs to match card radius
//                props = it,
//                onPlayerViewReady = { playerView ->
//                    playerView.apply {
//                        useController = false
//                        setShowBuffering(SHOW_BUFFERING_ALWAYS)
//                        subtitleView?.visibility = View.GONE
//                        resizeMode = props.resizeMode.toAspectRatioFrameLayout()
//                    }
//                }
//            )
//        }
//
//        if (props.type == ODSCardPromoType.FADE) {
//            ODSColumn(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .align(alignment = style.topFadeAbsoluteContentAlignment ?: Alignment.TopStart)
//                    .offset(offset = style.topFadeAbsoluteOffset),
//                verticalAlignment = style.topFadeVerticalAlignment,
//                horizontalAlignment = style.topFadeHorizontalAlignment,
//                verticalArrangement = style.topFadeVerticalArrangement,
//                background = style.topFadeBackground,
//                height = style.topFadeHeight
//            ) {
//            }
//        }
//        ODSColumn(
//            modifier = Modifier.fillMaxWidth(),
//            cornerRadius = style.cornerRadius,
//            clipContent = style.clipContent != false,
//            verticalAlignment = style.verticalAlignment,
//            horizontalAlignment = style.horizontalAlignment,
//            verticalArrangement = style.verticalArrangement,
//            minWidth = style.minWidth
//        ) {
//            ODSRow(
//                modifier = Modifier.fillMaxWidth(),
//                clipContent = style.spacerClipContent != false,
//                horizontalAlignment = style.spacerHorizontalAlignment,
//                verticalAlignment = style.spacerVerticalAlignment,
//                horizontalArrangement = style.spacerHorizontalArrangement,
//                minHeight = style.spacerMinHeight
//            ) {
//            }
//            ODSColumn(
//                modifier = Modifier.fillMaxWidth(),
//                padding = style.bottomFadePadding,
//                verticalAlignment = style.bottomFadeVerticalAlignment,
//                horizontalAlignment = style.bottomFadeHorizontalAlignment,
//                verticalArrangement = style.bottomFadeVerticalArrangement,
//                background = style.bottomFadeBackground
//            ) {
//                ODSColumn(
//                    modifier = Modifier.fillMaxWidth(),
//                    padding = style.contentPadding,
//                    cornerRadius = style.contentCornerRadius,
//                    clipContent = style.contentClipContent != false,
//                    verticalAlignment = style.contentVerticalAlignment,
//                    horizontalAlignment = style.contentHorizontalAlignment,
//                    verticalArrangement = style.contentVerticalArrangement,
//                    background = style.contentBackground
//                ) {
//                    val player = props.videoPlayerProps?.player
//                    var isVideoPlaying by remember { mutableStateOf(false) }
//                    val segmentDurationMap = remember { mutableStateMapOf<Int, Long>() }
//                    player?.let {
//                        PlayerStateObserver(
//                            player = it,
//                            onIsPlayingChanged = { playing ->
//                                isVideoPlaying = playing
//                            },
//                            onSegmentDurationFound = { index, duration ->
//                                segmentDurationMap[index] = duration
//                            }
//                        )
//                    }
//                    props.contentPanelProps?.let {
//                        ODSContentPanel(
//                            scheme = if (props.type == ODSCardPromoType.CARD) scheme else blackScheme,
//                            titleSlot = odsContentPanelTitleSlot,
//                            props = it.toODSContentPanelProps(
//                                isRunning = isVideoPlaying,
//                                carouselTimerProps = ODSCarouselTimerProps(
//                                    segmentsDuration = segmentDurationMap.toSegmentDurationModels()
//                                )
//                            ),
//                            onClick = onClick,
//                            onPreviousClick = onPreviousClick,
//                            onNextClick = onNextClick,
//                            onPlayPauseClick = onPlayPauseClick,
//                            segmentCompleted = segmentCompleted
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun PlayerStateObserver(
//    player: Player,
//    onIsPlayingChanged: (Boolean) -> Unit,
//    onSegmentDurationFound: (Int, Long) -> Unit
//) {
//    DisposableEffect(player) {
//        val listener = object : Player.Listener {
//            override fun onIsPlayingChanged(playing: Boolean) {
//                onIsPlayingChanged(playing)
//            }
//
//            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
//                for (i in 0 until timeline.windowCount) {
//                    val window = Timeline.Window()
//                    timeline.getWindow(i, window)
//                    val duration = window.durationMs
//                    onSegmentDurationFound(i, duration)
//                }
//            }
//        }
//        player.addListener(listener)
//        onDispose { player.removeListener(listener) }
//    }
//}
//
//fun Map<Int, Long>.toSegmentDurationModels(): List<ODSSegmentDurationModel> {
//    return this.toSortedMap().values.map { value ->
//        ODSSegmentDurationModel(duration = value.toInt())
//    }
//}
