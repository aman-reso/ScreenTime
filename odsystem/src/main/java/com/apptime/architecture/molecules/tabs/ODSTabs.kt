package com.telekom.odsystem.molecules.tabs

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.tabitem.ODSTabItem
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.offset
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

/**
 * ODSTabs composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param selectedTabIndex Parameter for customization.
 * @param onSelectedTabChange Callback triggered when action occurs.
 */
@Composable
fun ODSTabs(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSTabsProps = ODSTabsProps(),
    selectedTabIndex: Int = 0,
    onSelectedTabChange: (Int) -> Unit = {},
) {
    var tabItemState by remember(props.tabElements) {
        mutableStateOf(
            Pair(
                selectedTabIndex,
                ODSActions.DEFAULT
            )
        )
    }
    val style = ODSTabsStyle().getStyle(
        scheme = scheme,
        props = props,
        tabSelected = tabItemState.first == selectedTabIndex,
        state = tabItemState.second,
    )
    ODSTabsContainer(
        modifier = modifier,
        scheme = scheme,
        style = style,
        props = props,
        selectedTabIndex = selectedTabIndex,
        onSelectedTabChange = onSelectedTabChange,
        tabItemState = { index, state ->
            tabItemState = Pair(index, state)
        }
    )
}

@Composable
private fun ODSTabsContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSTabsStyle,
    props: ODSTabsProps,
    selectedTabIndex: Int,
    onSelectedTabChange: (Int) -> Unit,
    tabItemState: (Int, ODSActions) -> Unit,
) {
    ODSBox(modifier = modifier, contentAlignment = style.contentAlignment) {
        if (props.showDividerFrame) {
            ODSDividerFrame(style = style)
        }
        ODSListContainer(
            scheme = scheme,
            style = style,
            props = props,
            selectedTabIndex = selectedTabIndex,
            onSelectedTabChange = onSelectedTabChange,
            tabItemState = tabItemState
        )
    }
}

@Composable
private fun BoxScope.ODSDividerFrame(style: ODSTabsStyle) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .offset(style.dividerFrameOffset)
            .align(alignment = style.dividerFrameContentAlignment ?: Alignment.BottomStart),
        cornerRadius = style.dividerFrameBorderRadius,
        horizontalAlignment = style.dividerFrameHorizontalAlignment,
        verticalAlignment = style.dividerFrameVerticalAlignment,
        horizontalArrangement = style.dividerFrameHorizontalArrangement,
        background = style.dividerFrameBackgroundColor,
        height = style.dividerFrameHeight
    ) { }
}

@OptIn(ExperimentalFoundationApi::class)
@Suppress("LongMethod", "MultiLineIfElse")
@Composable
private fun ODSListContainer(
    scheme: ODSTheme,
    style: ODSTabsStyle,
    props: ODSTabsProps,
    selectedTabIndex: Int,
    onSelectedTabChange: (index: Int) -> Unit,
    tabItemState: (index: Int, ODSActions) -> Unit,
) {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidth = with(density) { windowInfo.containerSize.width.toFloat() }
    val coroutineScope = rememberCoroutineScope()
    val viewRequesterList =
        remember(props.tabElements) { props.tabElements.map { BringIntoViewRequester() } }
    val widthList = remember(props.tabElements) { Array(props.tabElements.size) { 0.dp } }
    val offsetList = remember(props.tabElements) { Array(props.tabElements.size) { 0.dp } }
    ODSColumn(
        verticalAlignment = style.verticalAlignment,
        verticalArrangement = style.verticalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        modifier = Modifier.applyScrollIfHug(variant = props.variant),
    ) {

        var targetWidth by remember { mutableStateOf(0.dp) }
        var targetOffset by remember { mutableStateOf(0.dp) }

        var animate by remember { mutableStateOf(false) }
        LaunchedEffect(selectedTabIndex, props) {
            targetWidth = widthList.getOrNull(selectedTabIndex) ?: 0.dp
            targetOffset = offsetList.getOrNull(selectedTabIndex) ?: 0.dp
        }

        val indicatorWidth by animateDpAsState(
            targetValue = targetWidth,
            animationSpec = if (animate) tween(durationMillis = DEFAULT_ANIMATION_DURATION) else snap(),
            label = ""
        )

        val indicatorOffset by animateDpAsState(
            targetValue = targetOffset,
            animationSpec = if (animate) tween(durationMillis = DEFAULT_ANIMATION_DURATION) else snap(),
            label = ""
        )
        ODSRow(
            modifier = Modifier
                .applyFillMaxWidthIfFillVariant(variant = props.variant)
                .selectableGroup(),
            gap = style.listContainerGap,
            horizontalAlignment = style.listContainerHorizontalAlignment,
            verticalAlignment = style.listContainerVerticalAlignment,
            horizontalArrangement = style.listContainerHorizontalArrangement
        ) {
            props.tabElements.forEachIndexed { index, it ->
                ODSTabItem(
                    modifier = Modifier
                        .then(if (props.variant != ODSTabsVariant.HUG) Modifier.weight(1f) else Modifier)
                        .storeTabWidthAndOffset(
                            density = density,
                            index = index,
                            widthList = widthList,
                            offsetList = offsetList
                        )
                        .bringIntoViewRequester(viewRequesterList[index]),
                    scheme = scheme,
                    props = props.tabElements[index].toODSTabItemProps(
                        selected = selectedTabIndex == index,
                        size = props.size,
                        variant = props.variant
                    ),
                    onClick = {
                        animate = true
                        if (selectedTabIndex != index) {
                            coroutineScope.launch {
                                bringTabIntoView(
                                    viewRequester = viewRequesterList[index],
                                    screenWidth = screenWidth
                                )
                            }
                        }
                        onSelectedTabChange(index)
                    },
                    tabItemState = { tabItemState(index, it) }
                )
            }
        }
        ODSLineContainer(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(width = indicatorWidth),
            style = style
        )
    }
}

@Composable
private fun ODSLineContainer(modifier: Modifier, style: ODSTabsStyle) {
    ODSRow(
        modifier = modifier,
        cornerRadius = style.lineContainerBorderRadius,
        horizontalArrangement = style.lineContainerHorizontalArrangement,
        horizontalAlignment = style.lineContainerHorizontalAlignment,
        verticalAlignment = style.lineContainerVerticalAlignment,
        background = style.lineContainerBackgroundColor,
        height = style.lineContainerHeight
    ) {
    }
}

private fun Modifier.applyFillMaxWidthIfFillVariant(variant: ODSTabsVariant): Modifier =
    if (variant == ODSTabsVariant.FILL) fillMaxWidth() else this

@Suppress("MultiLineIfElse")
@Composable
private fun Modifier.applyScrollIfHug(variant: ODSTabsVariant): Modifier =
    if (variant == ODSTabsVariant.HUG) {
        this.horizontalScroll(rememberScrollState())
    } else {
        this.fillMaxWidth()
    }

private fun Modifier.storeTabWidthAndOffset(
    density: Density,
    index: Int,
    widthList: Array<Dp>,
    offsetList: Array<Dp>,
): Modifier = this.onGloballyPositioned { layoutCoordinates ->
    val width = with(density) { layoutCoordinates.size.width.toDp() }
    val offset = layoutCoordinates.positionInParent().x
    if (width > 0.dp && !offset.isNaN()) {
        if (widthList[index] != width) widthList[index] = width
        val offsetDp = with(density) { offset.toDp() }
        if (offsetList[index] != offsetDp) offsetList[index] = offsetDp
    }
}

@OptIn(ExperimentalFoundationApi::class)
private suspend fun bringTabIntoView(
    viewRequester: BringIntoViewRequester,
    screenWidth: Float,
) {
    viewRequester.bringIntoView(
        rect =
            Rect(
                left = -screenWidth / 2, // Move left edge to center
                top = 0f,
                right = screenWidth / 2, // Move right edge to center
                bottom = 0f
            )
    )
}
