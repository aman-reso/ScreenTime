package com.telekom.odsystem.atoms.videoplayer

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
//import androidx.media3.common.util.UnstableApi
//import androidx.media3.ui.PlayerView

/**
 * Displays a video player using Android's PlayerView.
 *
 * @param modifier Modifier for the player.
 * @param props Configuration for the player (defaults to ODSVideoPlayerProps()).
 * @param onPlayerViewReady Callback invoked when PlayerView is ready for further customization.
 */
//@OptIn(UnstableApi::class)
@Composable
fun ODSVideoPlayer(
    modifier: Modifier = Modifier,
  //  props: ODSVideoPlayerProps = ODSVideoPlayerProps(),
  //  onPlayerViewReady: ((PlayerView) -> Unit)? = null
) {
//    AndroidView(
//        modifier = modifier,
//        factory = { context ->
//            PlayerView(context).also { playerView ->
//                onPlayerViewReady?.invoke(playerView)
//            }
//        },
//        update = {
//            it.player = props.player
//        },
//    )
}
