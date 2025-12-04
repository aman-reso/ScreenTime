package com.telekom.odsystem.molecules.bottomsheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSLazyColumn
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
import com.telekom.odsystem.foundations.offset
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSBottomSheet composable.
 *
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param showBottomSheet Parameter for customization.
 * @param bottomSheetState Parameter for customization.
 * @param onDismissRequest Callback triggered when action occurs.
 * @param contentSlot Parameter for customization.
 * @param titleSlot Parameter for customization.
 * @param actionSlot Parameter for customization.
 * @param onCloseClicked Callback triggered when close action occurs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ODSBottomSheet(
    scheme: ODSTheme = neutralScheme,
    props: ODSBottomSheetProps = ODSBottomSheetProps(),
    showBottomSheet: Boolean = false,
    bottomSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismissRequest: () -> Unit = {},
    contentSlot: @Composable (() -> Unit)? = null,
    titleSlot: @Composable (() -> Unit)? = null,
    actionSlot: @Composable (() -> Unit)? = null,
    onCloseClicked: () -> Unit
) {
    val style = ODSBottomSheetStyle().getStyle(scheme = scheme)

    val maxHeight = with(LocalDensity.current) {
        (LocalConfiguration.current.screenHeightDp.dp * FRACTION)
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            shape = RoundedCornerShape(
                topStart = style.cornerRadius?.topLeft ?: 0.dp,
                topEnd = style.cornerRadius?.topRight ?: 0.dp,
                bottomStart = style.cornerRadius?.bottomLeft ?: 0.dp,
                bottomEnd = style.cornerRadius?.bottomRight ?: 0.dp
            ),
            scrimColor = style.dimColor?.getColor() ?: BottomSheetDefaults.ScrimColor,
            containerColor = style.background?.first()?.hexColor?.getColor()
                ?: Color.Transparent,
            contentWindowInsets = { WindowInsets.navigationBars },
            modifier = Modifier
                .testTag("bottomSheet")
                .statusBarsPadding(),
            onDismissRequest = { onDismissRequest.invoke() },
            sheetState = bottomSheetState,
            dragHandle = null,
        ) {
            ODSBottomSheetContainer(
                modifier = Modifier
                    .semantics { isTraversalGroup = true }
                    .heightIn(max = maxHeight),
                style = style,
                props = props,
                scheme = scheme,
                contentSlot = contentSlot,
                actionSlot = actionSlot,
                titleSlot = titleSlot,
                onCloseClicked = onCloseClicked
            )
        }
    }
}

@Suppress("LongMethod")
@OptIn(ExperimentalFoundationApi::class)
@Composable
/**
 * Internal layout container for the bottom sheet content.
 *
 * @param modifier Modifier applied to the outer box.
 * @param style Visual style values.
 * @param props Component properties.
 * @param scheme Color scheme for styling child components.
 * @param contentSlot Optional custom content block.
 * @param titleSlot Composable displayed as the title.
 * @param actionSlot Composable displayed in the action area.
 */
private fun ODSBottomSheetContainer(
    modifier: Modifier,
    style: ODSBottomSheetStyle = ODSBottomSheetStyle(),
    props: ODSBottomSheetProps,
    scheme: ODSTheme,
    contentSlot: @Composable (() -> Unit)? = null,
    titleSlot: @Composable (() -> Unit)? = null,
    actionSlot: @Composable (() -> Unit)? = null,
    onCloseClicked: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val firstVisibleIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
    val firstVisibleOffset by remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }
    ODSBox(
        modifier = modifier.fillMaxWidth(),
        clipContent = style.zStackClipContent != false,
        contentAlignment = style.zStackContentAlignment,
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = style.cornerRadius,
            verticalArrangement = style.verticalArrangement,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment,
            background = style.background,
            clipContent = style.clipContent != false,
        ) {
            ODSLazyColumn(
                state = lazyListState,
                verticalArrangement = style.scrollContainerVerticalArrangement,
                horizontalAlignment = style.scrollContainerHorizontalAlignment,
                verticalAlignment = style.scrollContainerVerticalAlignment,
                clipContent = style.scrollContainerClipContent != false,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (props.image != null) {
                    item {
                        ODSImageContainer(props = props, style = style)
                    }
                }
                stickyHeader {
                    ODSTitleContainer(
                        scheme = scheme,
                        style = style,
                        titleSlot = titleSlot,
                        firstVisibleIndex = firstVisibleIndex,
                        firstVisibleOffset = firstVisibleOffset,
                        props = props
                    )
                }

                if (contentSlot != null) {
                    item {
                        ODSSlotComponentContainer(
                            style = style,
                            contentSlot = contentSlot
                        )
                    }
                }

                if (actionSlot != null) {
                    item {
                        ODSActionSlotContainer(
                            style = style,
                            actionSlot = actionSlot
                        )
                    }
                }
            }
        }
        ODSButton(
            modifier = Modifier
                .align(
                    alignment = style.odsCloseButtonAbsoluteContentAlignment
                        ?: Alignment.TopEnd
                )
                .offset(offset = style.odsCloseButtonAbsoluteOffset),
            props = ODSButtonProps(
                buttonIcon = ODSIconModel(drawableRes = R.drawable.close_type_standard_size_standard),
                buttonType = ODSButtonButtonType.ICON_ONLY,
                size = ODSButtonSize.SMALL,
                variant = if (props.image != null) ODSButtonVariant.SECONDARY else ODSButtonVariant.GHOST
            ),
            onClick = onCloseClicked
        )
        if (props.showHandle) {
            ODSHandleContainer(
                modifier = Modifier
                    .align(alignment = style.handleAbsoluteContentAlignment ?: Alignment.TopCenter)
                    .offset(offset = style.handleAbsoluteOffset),
                style = style
            )
        }
    }
}

@Composable
private fun ODSHandleContainer(
    modifier: Modifier,
    style: ODSBottomSheetStyle
) {
    ODSRow(
        modifier = modifier,
        cornerRadius = style.handleCornerRadius,
        horizontalArrangement = style.handleHorizontalArrangement,
        horizontalAlignment = style.handleHorizontalAlignment,
        verticalAlignment = style.handleVerticalAlignment,
        background = style.handleBackground,
        width = style.handleWidth,
        height = style.handleHeight,
    ) {
    }
}

@Composable
private fun ODSTitleContainer(
    scheme: ODSTheme,
    style: ODSBottomSheetStyle,
    titleSlot: @Composable (() -> Unit)?,
    firstVisibleIndex: Int,
    firstVisibleOffset: Int,
    props: ODSBottomSheetProps
) {
    ODSColumn(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) { this.heading() },
        padding = style.titleContainerPadding,
        gap = style.titleContainerGap,
        background = style.titleContainerBackground,
        verticalArrangement = style.titleContainerVerticalArrangement,
        horizontalAlignment = style.titleContainerHorizontalAlignment,
        verticalAlignment = style.titleContainerVerticalAlignment
    ) {
        if (titleSlot != null) {
            ODSHeaderContainer(style = style, titleSlot = titleSlot)
        }
        ODSColumn(
            height = style.dividerContainerHeight,
            verticalArrangement = style.dividerContainerVerticalArrangement,
            horizontalAlignment = style.dividerContainerHorizontalAlignment,
            verticalAlignment = style.dividerContainerVerticalAlignment,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (showDivider(
                    props = props,
                    firstVisibleIndex = firstVisibleIndex,
                    firstVisibleOffset = firstVisibleOffset
                )
            ) {
                ODSDivider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL)
                )
            }
        }
    }
}

@Composable
private fun ODSHeaderContainer(
    style: ODSBottomSheetStyle = ODSBottomSheetStyle(),
    titleSlot: @Composable (() -> Unit)
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = style.slotContainerHorizontalArrangement,
        horizontalAlignment = style.slotContainerHorizontalAlignment,
        verticalAlignment = style.slotContainerVerticalAlignment,
        padding = style.slotContainerPadding
    ) {
        titleSlot()
    }
}

@Composable
private fun ODSSlotComponentContainer(
    style: ODSBottomSheetStyle = ODSBottomSheetStyle(),
    contentSlot: @Composable (() -> Unit)
) {

    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        padding = style.slotComponentContainerPadding,
        verticalArrangement = style.slotComponentContainerVerticalArrangement,
        verticalAlignment = style.slotComponentContainerVerticalAlignment,
        horizontalAlignment = style.slotComponentContainerHorizontalAlignment
    ) {
        contentSlot()
    }
}

@Composable
private fun ODSActionSlotContainer(
    style: ODSBottomSheetStyle = ODSBottomSheetStyle(),
    actionSlot: @Composable (() -> Unit)
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        padding = style.actionSlotContainerPadding,
        verticalArrangement = style.actionSlotContainerVerticalArrangement,
        verticalAlignment = style.actionSlotContainerVerticalAlignment,
        horizontalAlignment = style.actionSlotContainerHorizontalAlignment
    ) {
        actionSlot()
    }
}

@Composable
private fun ODSImageContainer(
    props: ODSBottomSheetProps,
    style: ODSBottomSheetStyle
) {
    ODSBox(contentAlignment = style.imageContainerContentAlignment) {
        ODSImage(
            modifier = Modifier
                .fillMaxWidth(),
            imageModel = props.image,
            aspectRatio = props.imageAspectRatio,
            contentScale = style.imageContentScale ?: ContentScale.Crop
        )
    }
}

private fun showDivider(
    props: ODSBottomSheetProps,
    firstVisibleIndex: Int,
    firstVisibleOffset: Int
): Boolean {
    return (props.image != null && firstVisibleIndex >= MINIMUM_FIRST_VISIBLE_INDEX) ||
            (props.image == null && firstVisibleOffset > MINIMUM_FIRST_VISIBLE_OFFSET)
}

private const val MINIMUM_FIRST_VISIBLE_INDEX = 1
private const val MINIMUM_FIRST_VISIBLE_OFFSET = 0
private const val FRACTION = 0.90f
