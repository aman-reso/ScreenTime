package com.telekom.odsystem.molecules.dialog

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.dim.ODSDim
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
/**
 * ODSDialog composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param onDismissRequest Callback triggered when action occurs.
 * @param dismissOnBackPress Parameter for customization.
 * @param dismissOnClickOutside Parameter for customization.
 * @param contentSlot Parameter for customization.
 * @param actionSlot Parameter for customization.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSDialog(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    contentSlot: (@Composable () -> Unit)? = null,
    actionSlot: (@Composable () -> Unit)? = null,
    props: ODSDialogProps = ODSDialogProps()
) {
    val style = ODSDialogStyle().getStyle(scheme = scheme)
    val shouldShowDialog = remember { mutableStateOf(true) }
    if (shouldShowDialog.value) {
        ODSDialogContainer(
            modifier = modifier,
            scheme = scheme,
            style = style,
            onDismissRequest = onDismissRequest,
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            contentSlot = contentSlot,
            actionSlot = actionSlot,
            props = props,
            shouldShowDialog = shouldShowDialog
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun ODSDialogContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSDialogStyle,
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    contentSlot: (@Composable () -> Unit)?,
    actionSlot: (@Composable () -> Unit)?,
    props: ODSDialogProps,
    shouldShowDialog: MutableState<Boolean>
) {
    val lazyListState = rememberLazyListState()
    val firstVisibleOffset by remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }

    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        ),
        onDismissRequest = {
            shouldShowDialog.value = false
            onDismissRequest()
        }
    ) {
        ODSBox(
            modifier = Modifier
                .fillMaxSize()
                .testTag("dialog"),
            contentAlignment = Alignment.Center
        ) {
            val context = LocalContext.current
            ODSDim(
                modifier = Modifier
                    .customClickable(
                        role = Role.Button,
                        onClickLabel = context.getString(R.string.semantic_close_dialog),
                        isPressed = {},
                        onClick = {
                            if (dismissOnClickOutside) onDismissRequest()
                        }
                    )
            )
            ODSColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        start = style.padding?.left ?: 0.dp,
                        end = style.padding?.right ?: 0.dp
                    )
                    .sizeWithinBounds(minHeight = style.minHeight ?: Dp.Unspecified)
                    .semantics {
                        contentDescription = context.getString(R.string.semantic_dialog)
                    },
                cornerRadius = style.borderRadius,
                verticalAlignment = style.verticalAlignment,
                horizontalAlignment = style.horizontalAlignment,
                verticalArrangement = style.verticalArrangement,
                clipContent = style.clipContent != false,
                background = style.backgroundColor,
                effect = style.boxShadow,
            ) {
                ODSHeaderContainer(
                    style = style,
                    props = props,
                    firstVisibleOffset = firstVisibleOffset,
                    shouldShowDialog = shouldShowDialog,
                    scheme = scheme,
                    onDismissRequest = onDismissRequest
                )

                ODSScrollContainer(
                    style = style,
                    props = props,
                    contentSlot = contentSlot,
                    actionSlot = actionSlot,
                    lazyListState = lazyListState
                )
            }
        }
    }
}

@Composable
private fun ODSScrollContainer(
    style: ODSDialogStyle,
    props: ODSDialogProps,
    lazyListState: LazyListState,
    contentSlot: @Composable (() -> Unit)?,
    actionSlot: @Composable (() -> Unit)?
) {

    ODSLazyColumn(
        state = lazyListState,
        gap = style.scrollContainerGap,
        clipContent = style.scrollContainerClipContent != false,
        verticalArrangement = style.scrollContainerVerticalArrangement,
        verticalAlignment = style.scrollContainerVerticalAlignment,
        horizontalAlignment = style.scrollContainerHorizontalAlignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (contentSlot != null) {
            item {
                ODSSlotContainer(
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

@Composable
private fun ODSActionSlotContainer(style: ODSDialogStyle, actionSlot: @Composable (() -> Unit)) {
    ODSColumn(
        padding = style.actionSlotContainerPadding,
        verticalAlignment = style.actionSlotContainerVerticalAlignment,
        horizontalAlignment = style.actionSlotContainerHorizontalAlignment,
        verticalArrangement = style.actionSlotContainerVerticalArrangement,
        modifier = Modifier.fillMaxWidth()
    ) {
        actionSlot()
    }
}

@Composable
private fun ODSSlotContainer(style: ODSDialogStyle, contentSlot: @Composable (() -> Unit)) {
    ODSRow(
        padding = style.slotContainerPadding,
        horizontalAlignment = style.slotContainerHorizontalAlignment,
        verticalAlignment = style.slotContainerVerticalAlignment,
        horizontalArrangement = style.slotContainerHorizontalArrangement,
        modifier = Modifier.fillMaxWidth()
    ) {
        contentSlot()
    }
}

@Composable
private fun ODSTextSectionContainer(style: ODSDialogStyle, props: ODSDialogProps) {
    ODSColumn(
        padding = style.textSectionPadding,
        clipContent = style.textSectionClipContent != false,
        verticalArrangement = style.textSectionVerticalArrangement,
        verticalAlignment = style.textSectionVerticalAlignment,
        horizontalAlignment = style.textSectionHorizontalAlignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
        ODSText(
            text = props.bodyText,
            style = style.textTextStyle,
            color = style.textColor,
            textAlign = style.textTextAlign ?: TextAlign.Start,
            overflow = style.textTextOverflow,
            modifier = Modifier
                .fillMaxWidth()
                .focusable()
        )
    }
}

@Composable
private fun ODSHeaderContainer(
    style: ODSDialogStyle,
    props: ODSDialogProps,
    shouldShowDialog: MutableState<Boolean>,
    scheme: ODSTheme,
    firstVisibleOffset: Int,
    onDismissRequest: () -> Unit,
) {
    ODSColumn(
        horizontalAlignment = style.headerContainerHorizontalAlignment,
        verticalArrangement = style.headerContainerVerticalArrangement,
        verticalAlignment = style.headerContainerVerticalAlignment,
        background = style.headerContainerBackgroundColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        ODSRow(
            horizontalAlignment = style.titleContainerHorizontalAlignment,
            verticalAlignment = style.titleContainerVerticalAlignment,
            horizontalArrangement = style.titleContainerHorizontalArrangement,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (!props.title.isNullOrEmpty()) {
                ODSTextContainer(
                    modifier = Modifier.weight(1f),
                    style = style,
                    props = props
                )
            }

            ODSHeaderButtonContainer(
                style = style,
                props = props,
                scheme = scheme,
                onDismissRequest = onDismissRequest,
                shouldShowDialog = shouldShowDialog
            )
        }


        if (!props.bodyText.isNullOrEmpty()) {
            ODSTextSectionContainer(
                style = style,
                props = props
            )
        }

        if (firstVisibleOffset > 0) {
            ODSColumn(
                verticalArrangement = style.dividerContainerVerticalArrangement,
                verticalAlignment = style.dividerContainerVerticalAlignment,
                horizontalAlignment = style.dividerContainerHorizontalAlignment
            ) {
                ODSDivider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSDividerProps(
                        variant = ODSDividerVariant.HORIZONTAL
                    )
                )
            }
        }
    }
}

@Composable
private fun ODSHeaderButtonContainer(
    style: ODSDialogStyle,
    props: ODSDialogProps,
    scheme: ODSTheme,
    onDismissRequest: () -> Unit,
    shouldShowDialog: MutableState<Boolean>
) {
    ODSRow(
        padding = style.buttonContainerPadding,
        horizontalAlignment = style.buttonContainerHorizontalAlignment,
        horizontalArrangement = style.buttonContainerHorizontalArrangement,
        verticalAlignment = style.buttonContainerVerticalAlignment
    ) {
        if (props.showCloseButton) {
            ODSButton(
                scheme = scheme,
                props = ODSButtonProps(
                    buttonType = ODSButtonButtonType.ICON_ONLY,
                    size = ODSButtonSize.SMALL,
                    variant = ODSButtonVariant.GHOST,
                    buttonIcon = ODSIconModel(
                        drawableRes = R.drawable.close_type_standard,
                        contentDescription = "close icon"
                    )
                ),

                ) {
                shouldShowDialog.value = false
                onDismissRequest()
            }
        }
    }
}

@Composable
private fun ODSTextContainer(modifier: Modifier, style: ODSDialogStyle, props: ODSDialogProps) {
    ODSRow(
        modifier = modifier,
        padding = style.textContainerPadding,
        horizontalAlignment = style.textContainerHorizontalAlignment,
        horizontalArrangement = style.textContainerHorizontalArrangement,
        verticalAlignment = style.textContainerVerticalAlignment
    ) {
        if (props.title.isNullOrEmpty().not()) {
            ODSText(
                text = props.title,
                style = style.headerTextStyle,
                color = style.headerColor,
                modifier = Modifier
                    .wrapContentWidth()
                    .semantics { heading() },
                textAlign = style.headerTextAlign ?: TextAlign.Start,
            )
        }
    }
}
