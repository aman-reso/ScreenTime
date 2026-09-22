//package com.telekom.odsystem.organisms.cardpromo
//
//import ODSContentPanelButtonProps
//import ODSContentPanelProps
//import androidx.annotation.OptIn
//import androidx.media3.common.util.UnstableApi
//import androidx.media3.ui.AspectRatioFrameLayout
//import com.telekom.odsystem.atoms.carouseltimer.ODSCarouselTimerProps
//import com.telekom.odsystem.atoms.videoplayer.ODSVideoPlayerProps
//import com.telekom.odsystem.foundations.ODSAspectRatio
//import com.telekom.odsystem.organisms.cardpromo.ODSCardPromoType.CARD
//import com.telekom.odsystem.organisms.cardpromo.ODSCardPromoType.FADE
//
///**
// * Defines the type of the card promo.
// * - [CARD]: The content is displayed as a card.
// * - [FADE]: The content fades in and out.
// */
//enum class ODSCardPromoType {
//    CARD,
//    FADE,
//}
//
///**
// * Defines how video content resizes within the ODSCardPromo.
// *
// * Mirrors `AspectRatioFrameLayout` resize modes.
// * - `RESIZE_MODE_FIT`: Fits within bounds, keeps aspect ratio.
// * - `RESIZE_MODE_FIXED_WIDTH`: Fits width, keeps aspect ratio.
// * - `RESIZE_MODE_FIXED_HEIGHT`: Fits height, keeps aspect ratio.
// * - `RESIZE_MODE_FILL`: Fills view, may crop.
// * - `RESIZE_MODE_ZOOM`: Fills view, keeps aspect ratio, may crop.
// */
//// Not exported from plugin
//enum class ODSCardPromoResizeMode {
//    RESIZE_MODE_FIT,
//    RESIZE_MODE_FIXED_WIDTH,
//    RESIZE_MODE_FIXED_HEIGHT,
//    RESIZE_MODE_FILL,
//    RESIZE_MODE_ZOOM
//}
//
//data class ODSCardPromoContentPanelProps(
//    var segmentText: String? = null,
//    var isInProgressElementIndex: Int = 0, // Not exported from plugin
//    var buttonProps: ODSContentPanelButtonProps? = null,
//)
//
//internal fun ODSCardPromoContentPanelProps.toODSContentPanelProps(
//    isRunning: Boolean,
//    carouselTimerProps: ODSCarouselTimerProps
//): ODSContentPanelProps {
//    return ODSContentPanelProps(
//        segmentText = segmentText,
//        isInProgressElementIndex = isInProgressElementIndex,
//        carouselTimerProps = carouselTimerProps,
//        buttonProps = buttonProps,
//        isRunning = isRunning
//    )
//}
//
///**
// * Defines the properties for the ODSCardPromo component.
// *
// * @param type Visual style of the card promo.
// * @param contentPanelProps Properties for the card's content panel.
// * @param aspectRatio Aspect ratio of the card.
// * @param resizeMode How content (e.g., video) is resized.
// * @param videoPlayerProps Properties for the video player, if applicable.
// */
//
//data class ODSCardPromoProps(
//    var type: ODSCardPromoType = CARD,
//    var contentPanelProps: ODSCardPromoContentPanelProps? = null,
//    var aspectRatio: ODSAspectRatio = ODSAspectRatio.VALUE_9_16, // Not exported from plugin
//    var resizeMode: ODSCardPromoResizeMode = ODSCardPromoResizeMode.RESIZE_MODE_FILL, // Not exported from plugin
//    var videoPlayerProps: ODSVideoPlayerProps? = null // Not exported from plugin
//)
//
//@OptIn(UnstableApi::class)
//fun ODSCardPromoResizeMode.toAspectRatioFrameLayout() = when (this) {
//    ODSCardPromoResizeMode.RESIZE_MODE_FIT -> AspectRatioFrameLayout.RESIZE_MODE_FIT
//    ODSCardPromoResizeMode.RESIZE_MODE_FIXED_WIDTH -> AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
//    ODSCardPromoResizeMode.RESIZE_MODE_FIXED_HEIGHT -> AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
//    ODSCardPromoResizeMode.RESIZE_MODE_FILL -> AspectRatioFrameLayout.RESIZE_MODE_FILL
//    ODSCardPromoResizeMode.RESIZE_MODE_ZOOM -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
//}
