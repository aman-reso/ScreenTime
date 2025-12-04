package com.telekom.odsystem.organisms.pageheader

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Displays a customizable page header with options for logo, titles, actions, and tabs.
 *
 * The header can adapt its layout based on the provided [props] and [scrollOffset].
 *
 * @param modifier Modifier for styling.
 * @param scheme Color scheme.
 * @param scrollOffset Vertical scroll offset to enable animations like title collapse.
 * @param props Configuration for header type and elements like tabs.
 * @param actionsSlot Slot for custom action items (e.g., buttons).
 * @param logoSlot Slot for the logo.
 * @param mainPageTitleSlot Slot for the main page title (can collapse).
 * @param topSectionMainPageTitleSlot Slot for a persistent title in the top section.
 * @param subPageTitleSlot Slot for the sub-page title.
 * @param onBackButtonClick Callback for the back button click.
 * @param selectedTabIndex Current selected tab index.
 * @param onSelectedTabChange Callback for tab selection changes.
 */
@Composable
fun ODSPageHeader(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    scrollOffset: Float = 0f,
    props: ODSPageHeaderProps = ODSPageHeaderProps(),
    actionsSlot: @Composable (() -> Unit)? = null,
    logoSlot: @Composable (() -> Unit)? = null,
    mainPageTitleSlot: @Composable (() -> Unit)? = null,
    topSectionMainPageTitleSlot: @Composable (() -> Unit)? = null,
    subPageTitleSlot: @Composable (() -> Unit)? = null,
    onBackButtonClick: () -> Unit = {},
    selectedTabIndex: Int = 0,
    onSelectedTabChange: (Int) -> Unit = {},
) {
    val style = ODSPageHeaderStyle().getStyle(scheme = scheme, props = props)
    ODSColumn(
        gap = style.gap,
        padding = style.padding,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        background = style.background,
        modifier = modifier.fillMaxWidth()
    ) {
        when (props.type) {
            ODSPageHeaderType.MAIN_PAGE_HEADER_STANDARD -> {
                ODSMainPageHeaderStandard(
                    style = style,
                    props = props,
                    scrollOffset = scrollOffset,
                    logoSlot = logoSlot,
                    mainPageTitleSlot = mainPageTitleSlot,
                    topSectionMainPageTitleSlot = topSectionMainPageTitleSlot,
                    actionsSlot = actionsSlot
                )
            }

            ODSPageHeaderType.MAIN_PAGE_HEADER_SLIM -> {
                if (props.showTopSection) {
                    ODSMainPageHeaderSlim(
                        style = style,
                        props = props,
                        logoSlot = logoSlot,
                        topSectionMainPageTitleSlot = topSectionMainPageTitleSlot,
                        actionsSlot = actionsSlot
                    )
                }
            }

            ODSPageHeaderType.SUB_PAGE_HEADER -> {
                ODSSubPageHeader(
                    scheme = scheme,
                    style = style,
                    subPageTitleSlot = subPageTitleSlot,
                    actionsSlot = actionsSlot,
                    onBackButtonClick = onBackButtonClick
                )
            }
        }

        props.tabsProps?.let {
            ODSTabsContainer(
                scheme = scheme,
                style = style,
                tabsProps = it,
                selectedTabIndex = selectedTabIndex,
                onSelectedTabChange = onSelectedTabChange
            )
        }
        AnimatedVisibility(visible = scrollOffset > 0) {
            ODSDividerContainer(scheme = scheme, style = style)
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun ODSMainPageHeaderStandard(
    style: ODSPageHeaderStyle,
    props: ODSPageHeaderProps,
    scrollOffset: Float,
    logoSlot: @Composable (() -> Unit)?,
    mainPageTitleSlot: @Composable (() -> Unit)?,
    topSectionMainPageTitleSlot: @Composable (() -> Unit)?,
    actionsSlot: @Composable (() -> Unit)?,
) {
    if (props.showTopSection) {
        ODSMainPageHeaderTopSection(
            style = style,
            props = props,
            scrollOffset = scrollOffset,
            logoSlot = logoSlot,
            topSectionMainPageTitleSlot = topSectionMainPageTitleSlot,
            actionsSlot = actionsSlot
        )
    }
    if (mainPageTitleSlot != null) {
        ODSMainPageTitleSlotContainer(
            style = style,
            scrollOffset = scrollOffset,
            mainPageTitleSlot = mainPageTitleSlot
        )
    }
}

@Composable
private fun ODSMainPageHeaderSlim(
    style: ODSPageHeaderStyle,
    props: ODSPageHeaderProps,
    logoSlot: @Composable (() -> Unit)?,
    topSectionMainPageTitleSlot: @Composable (() -> Unit)?,
    actionsSlot: @Composable (() -> Unit)?,
) {
    ODSRow(
        padding = style.mainPageHeaderTopSectionPadding,
        horizontalArrangement = style.mainPageHeaderTopSectionHorizontalArrangement,
        verticalAlignment = style.mainPageHeaderTopSectionVerticalAlignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (props.showLogo) {
            ODSLogoSlotContainer(style = style, logoSlot = logoSlot)
        }
        topSectionMainPageTitleSlot?.let {
            ODSTopSectionMainPageTitleSlotContainer(
                modifier = Modifier.weight(1f),
                style = style,
                topSectionMainPageTitleSlot = it
            )
        }
        actionsSlot?.let { ODSPreferredActionsSlotContainer(style = style, actionsSlot = it) }
    }
}

@Composable
private fun ODSSubPageHeader(
    scheme: ODSTheme,
    style: ODSPageHeaderStyle,
    subPageTitleSlot: @Composable (() -> Unit)?,
    actionsSlot: @Composable (() -> Unit)?,
    onBackButtonClick: () -> Unit,
) {
    ODSRow(
        padding = style.subPageHeaderTopSectionPadding,
        horizontalArrangement = style.subPageHeaderTopSectionHorizontalArrangement,
        verticalAlignment = style.subPageHeaderTopSectionVerticalAlignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        ODSButton(
            scheme = scheme,
            props = ODSButtonProps(
                buttonIcon = ODSIconModel(
                    drawableRes = R.drawable.left_condensed_type_standard,
                    contentDescription = stringResource(R.string.semantic_navigation_back_icon)
                ),
                buttonType = ODSButtonButtonType.ICON_ONLY,
                size = ODSButtonSize.SMALL,
                variant = ODSButtonVariant.GHOST
            ),
            onClick = onBackButtonClick
        )
        subPageTitleSlot?.let {
            ODSSubPageTitleSlotContainer(
                modifier = Modifier.weight(1f),
                style = style,
                subPageTitleSlot = it
            )
        }
        ODSColumn(
            horizontalAlignment = style.preferredActionsSlotContainer2HorizontalAlignment,
            verticalAlignment = style.preferredActionsSlotContainer2VerticalAlignment,
            verticalArrangement = style.preferredActionsSlotContainer2VerticalArrangement,
            minWidth = style.preferredActionsSlotContainer2MinWidth
        ) {
            actionsSlot?.invoke()
        }
    }
}

@Composable
private fun ODSMainPageHeaderTopSection(
    style: ODSPageHeaderStyle,
    props: ODSPageHeaderProps,
    scrollOffset: Float,
    logoSlot: @Composable (() -> Unit)?,
    topSectionMainPageTitleSlot: @Composable (() -> Unit)?,
    actionsSlot: @Composable (() -> Unit)?,
) {
    ODSRow(
        padding = style.mainPageHeaderTopSectionPadding,
        horizontalArrangement = style.mainPageHeaderTopSectionHorizontalArrangement,
        verticalAlignment = style.mainPageHeaderTopSectionVerticalAlignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (props.showLogo) {
            ODSLogoSlotContainer(style = style, logoSlot = logoSlot)
        }
        topSectionMainPageTitleSlot?.let { slot ->
            ExpandingContainerByScroll(
                scrollOffset = scrollOffset,
                modifier = Modifier.weight(1f)
            ) { childModifier ->
                ODSTopSectionMainPageTitleSlotContainer(
                    modifier = childModifier,
                    style = style,
                    topSectionMainPageTitleSlot = slot
                )
            }
        }
        actionsSlot?.let { ODSPreferredActionsSlotContainer(style = style, actionsSlot = it) }
    }
}

@Composable
private fun ExpandingContainerByScroll(
    scrollOffset: Float,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    SubcomposeLayout(
        modifier = modifier
            .clipToBounds()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = DEFAULT_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing
                )
            )
    ) { constraints ->
        val measurePlaceables =
            subcompose("measure") { content(Modifier) }.map { it.measure(constraints) }
        val maxWidth = constraints.maxWidth
        val naturalHeight = measurePlaceables.maxOfOrNull { it.height } ?: 0
        val fraction =
            if (naturalHeight > 0) normalizeValue(scrollOffset, naturalHeight.toFloat()) else 0f
        val eased = FastOutSlowInEasing.transform(fraction)
        val layoutHeight = (naturalHeight * eased).toInt().coerceAtLeast(0)
        val placeables = subcompose("content") {
            content(Modifier.graphicsLayer { alpha = eased })
        }.map { it.measure(constraints) }
        layout(maxWidth, layoutHeight) {
            placeables.forEach { it.place(0, 0) }
        }
    }
}

@Composable
private fun CollapsingContainerByScroll(
    scrollOffset: Float,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    SubcomposeLayout(
        modifier = modifier
            .clipToBounds()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = DEFAULT_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing
                )
            )
    ) { constraints ->
        val measurePlaceables =
            subcompose("measure") { content(Modifier) }.map { it.measure(constraints) }
        val maxWidth = constraints.maxWidth
        val naturalHeight = measurePlaceables.maxOfOrNull { it.height } ?: 0
        val fraction =
            if (naturalHeight > 0) normalizeValue(scrollOffset, naturalHeight.toFloat()) else 0f
        val eased = FastOutSlowInEasing.transform(fraction)
        val layoutHeight = (naturalHeight * (1 - eased)).toInt().coerceAtLeast(0)
        val placeables = subcompose("content") {
            content(Modifier.graphicsLayer { alpha = 1 - eased })
        }.map { it.measure(constraints) }
        layout(maxWidth, layoutHeight) {
            placeables.forEach { it.place(0, 0) }
        }
    }
}

@Composable
private fun ODSMainPageTitleSlotContainer(
    style: ODSPageHeaderStyle,
    scrollOffset: Float,
    mainPageTitleSlot: @Composable () -> Unit,
) {
    CollapsingContainerByScroll(
        scrollOffset = scrollOffset,
        modifier = Modifier.fillMaxWidth()
    ) { childModifier ->
        ODSColumn(
            padding = style.mainPageTitleSlotContainerPadding,
            verticalArrangement = style.mainPageTitleSlotContainerVerticalArrangement,
            verticalAlignment = style.mainPageTitleSlotContainerVerticalAlignment,
            horizontalAlignment = style.mainPageTitleSlotContainerHorizontalAlignment,
            modifier = childModifier
                .fillMaxWidth()
                .wrapContentHeight(unbounded = true)
        ) {
            mainPageTitleSlot()
        }
    }
}

@Composable
private fun ODSSubPageTitleSlotContainer(
    modifier: Modifier,
    style: ODSPageHeaderStyle,
    subPageTitleSlot: @Composable () -> Unit,
) {
    ODSColumn(
        gap = style.subPageTitleSlotContainerGap,
        verticalArrangement = style.subPageTitleSlotContainerVerticalArrangement,
        verticalAlignment = style.subPageTitleSlotContainerVerticalAlignment,
        horizontalAlignment = style.subPageTitleSlotContainerHorizontalAlignment,
        modifier = modifier
    ) {
        subPageTitleSlot()
    }
}

@Composable
private fun ODSLogoSlotContainer(style: ODSPageHeaderStyle, logoSlot: @Composable (() -> Unit)?) {
    ODSColumn(
        verticalArrangement = style.logoSlotContainerVerticalArrangement,
        verticalAlignment = style.logoSlotContainerVerticalAlignment,
        horizontalAlignment = style.logoSlotContainerHorizontalAlignment,
        width = style.logoSlotContainerWidth,
        height = style.logoSlotContainerHeight
    ) {
        logoSlot?.invoke()
    }
}

@Composable
private fun ODSTopSectionMainPageTitleSlotContainer(
    modifier: Modifier = Modifier,
    style: ODSPageHeaderStyle,
    topSectionMainPageTitleSlot: @Composable () -> Unit,
) {
    ODSColumn(
        verticalArrangement = style.topSectionMainPageTitleSlotContainerVerticalArrangement,
        verticalAlignment = style.topSectionMainPageTitleSlotContainerVerticalAlignment,
        horizontalAlignment = style.topSectionMainPageTitleSlotContainerHorizontalAlignment,
        modifier = modifier
    ) {
        topSectionMainPageTitleSlot()
    }
}

@Composable
private fun ODSPreferredActionsSlotContainer(
    style: ODSPageHeaderStyle,
    actionsSlot: @Composable () -> Unit,
) {
    ODSColumn(
        horizontalAlignment = style.preferredActionsSlotContainerHorizontalAlignment,
        verticalAlignment = style.preferredActionsSlotContainerVerticalAlignment,
        verticalArrangement = style.preferredActionsSlotContainerVerticalArrangement,
        minWidth = style.preferredActionsSlotContainerMinWidth
    ) {
        actionsSlot()
    }
}

@Composable
private fun ODSTabsContainer(
    scheme: ODSTheme,
    style: ODSPageHeaderStyle,
    tabsProps: ODSTabsProps,
    selectedTabIndex: Int,
    onSelectedTabChange: (Int) -> Unit,
) {
    ODSColumn(
        gap = style.tabsContainerGap,
        padding = style.tabsContainerPadding,
        verticalArrangement = style.tabsContainerVerticalArrangement,
        verticalAlignment = style.tabsContainerVerticalAlignment,
        horizontalAlignment = style.tabsContainerHorizontalAlignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        ODSTabs(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = tabsProps,
            selectedTabIndex = selectedTabIndex,
            onSelectedTabChange = onSelectedTabChange
        )
    }
}

@Composable
private fun ODSDividerContainer(scheme: ODSTheme, style: ODSPageHeaderStyle) {
    ODSColumn(
        verticalArrangement = style.dividerContainerVerticalArrangement,
        verticalAlignment = style.dividerContainerVerticalAlignment,
        horizontalAlignment = style.dividerContainerHorizontalAlignment,
        height = style.dividerContainerHeight,
        modifier = Modifier.fillMaxWidth()
    ) {
        ODSDivider(
            scheme = scheme,
            props = ODSDividerProps(
                inset = false,
                spacing = false,
                variant = ODSDividerVariant.HORIZONTAL
            )
        )
    }
}

private fun normalizeValue(input: Float, maxInput: Float) = (input / maxInput).coerceIn(0f, 1f)
