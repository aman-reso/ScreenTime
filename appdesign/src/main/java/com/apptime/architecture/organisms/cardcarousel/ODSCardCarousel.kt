package com.telekom.odsystem.organisms.cardcarousel

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.molecules.carouselnavigation.ODSCarouselNavigation
import com.telekom.odsystem.molecules.carouselnavigation.ODSCarouselNavigationNavigationLeftButtonProps
import com.telekom.odsystem.molecules.carouselnavigation.ODSCarouselNavigationNavigationRightButtonProps
import com.telekom.odsystem.molecules.carouselnavigation.ODSCarouselNavigationProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("LongMethod")
/**
 * ODSCardCarousel composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param contentSlot Parameter for customization.
 */
@Composable
fun ODSCardCarousel(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardCarouselProps,
    contentSlot: @Composable (PagerScope.(page: Int) -> Unit),
    onPreviousButtonClick: (Int) -> Unit = {},
    onNextButtonClick: (Int) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val coroutineScope = rememberCoroutineScope()
    val infinitePageCount = Int.MAX_VALUE
    val middlePage = infinitePageCount / 2
    val initialPage = if (props.loop) {
        (middlePage - (middlePage % props.carouselSize)) + props.initialPage
    } else {
        props.initialPage
    }
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { if (props.loop) infinitePageCount else props.carouselSize },
    )
    val style = ODSCardCarouselStyle().getStyle(
        scheme = scheme,
        state = if (isHovered) ODSActions.HOVERED else ODSActions.DEFAULT
    )
    val lifecycleOwner = LocalLifecycleOwner.current
    var triggerKey by remember { mutableIntStateOf(0) }
    var prevBtnDisabled by remember { mutableStateOf(false) }
    var nextBtnDisabled by remember { mutableStateOf(false) }
    val isDraggedState by pagerState.interactionSource.collectIsDraggedAsState()
    if (props.loop.not()) {
        LaunchedEffect(
            key1 = pagerState,
            key2 = pagerState.currentPage,
            key3 = pagerState.targetPage
        ) {
            val currentPage = pagerState.currentPage
            val targetPage = pagerState.targetPage
            prevBtnDisabled = currentPage == 0 || targetPage == 0
            nextBtnDisabled =
                currentPage == props.carouselSize - 1 || targetPage == props.carouselSize - 1
        }
    }

    LaunchedEffect(key1 = lifecycleOwner, key2 = triggerKey, key3 = isDraggedState) {
        if (isDraggedState.not() && props.autoScroll) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                while (true) {
                    delay(props.autoScrollDuration)
                    animateScroll(
                        pagerState = pagerState,
                        page = pagerState.currentPage.inc() % pagerState.pageCount
                    )
                }
            }
        }
    }

    ODSColumn(
        gap = style.gap,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        modifier = modifier.fillMaxWidth()
    ) {
        ODSCardEngagementContainer(
            modifier = Modifier
                .hoverable(interactionSource)
                .then(if (props.fill) Modifier.weight(1f) else Modifier),
            style = style,
            pagerState = pagerState,
            contentSlot = contentSlot
        )

        ODSCarouselNavigationContainer(
            scheme = scheme,
            targetPage = pagerState.targetPage,
            carouselSize = props.carouselSize,
            prevBtnDisabled = prevBtnDisabled,
            nextBtnDisabled = nextBtnDisabled,
            onClickPreviousButton = {
                if (props.loop || pagerState.currentPage != 0) {
                    triggerKey--
                    coroutineScope.launch {
                        animateScroll(
                            pagerState = pagerState,
                            page = (pagerState.currentPage.dec()) % pagerState.pageCount
                        )
                        onPreviousButtonClick(pagerState.currentPage)
                    }
                }
            },
            onClickNextButton = {
                if (props.loop || pagerState.currentPage.plus(1) != props.carouselSize) {
                    triggerKey++
                    coroutineScope.launch {
                        animateScroll(
                            pagerState = pagerState,
                            page = pagerState.currentPage.inc() % pagerState.pageCount
                        )
                        onNextButtonClick(pagerState.currentPage)
                    }
                }
            }
        )
    }
}

@Composable
private fun ODSCarouselNavigationContainer(
    scheme: ODSTheme,
    targetPage: Int,
    carouselSize: Int,
    prevBtnDisabled: Boolean,
    nextBtnDisabled: Boolean,
    onClickPreviousButton: () -> Unit,
    onClickNextButton: () -> Unit,
) {
    val carouselNavigationProps = ODSCarouselNavigationProps(
        dots = carouselSize,
        selectedIndex = targetPage % carouselSize,
        navigationLeftButtonProps = ODSCarouselNavigationNavigationLeftButtonProps(
            disabled = prevBtnDisabled
        ),
        navigationRightButtonProps = ODSCarouselNavigationNavigationRightButtonProps(
            disabled = nextBtnDisabled
        )
    )
    ODSCarouselNavigation(
        scheme = scheme,
        props = carouselNavigationProps,
        onClickPreviousButton = onClickPreviousButton,
        onClickNextButton = onClickNextButton
    )
}

@Composable
private fun ODSCardEngagementContainer(
    modifier: Modifier,
    style: ODSCardCarouselStyle,
    pagerState: PagerState,
    contentSlot: @Composable PagerScope.(page: Int) -> Unit,
) {
    val scale by animateFloatAsState(
        targetValue = style.scaleFactor ?: DEFAULT_FACTOR,
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = ""
    )
    HorizontalPager(
        modifier = modifier
            .scale(scale)
            .semantics(mergeDescendants = true) { },
        state = pagerState,
        pageSpacing = style.pagerGap ?: DSVariables.spacingComponent3,
        pageContent = {
            ODSBox(
                modifier = Modifier.scale(1 / scale)
            ) {
                contentSlot(it)
            }
        }
    )
}

private suspend fun animateScroll(pagerState: PagerState, page: Int) {
    pagerState.animateScrollToPage(
        page = page,
        animationSpec = tween(durationMillis = DEFAULT_ANIMATION_DURATION) // Adjust the duration here
    )
}
