@file:OptIn(ExperimentalAnimatableApi::class)

package com.telekom.odsystem.slots.cardspreferredactions

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.ExperimentalAnimatableApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.delay

/**
 * ODSCardsPreferredActions composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onMainActionButtonClick Callback triggered when action occurs.
 * @param onSecondaryActionButtonClick Callback triggered when action occurs.
 * @param onMoreButtonClick Callback triggered when action occurs.
 * @param onCloseButtonClick Callback triggered when action occurs.
 * @param onActionsListButtonClick Callback triggered when action occurs.
 */
@Suppress("LongMethod", "MaximumLineLength")
@Composable
fun ODSCardsPreferredActions(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardsPreferredActionsProps = ODSCardsPreferredActionsProps(),
    onMainActionButtonClick: () -> Unit = {},
    onSecondaryActionButtonClick: () -> Unit = {},
    onMoreButtonClick: () -> Unit = {},
    onCloseButtonClick: () -> Unit = {},
    onActionsListButtonClick: (index: Int) -> Unit = {},
) {
    val style = ODSCardsPreferredActionsStyle().getStyle(scheme = scheme, props = props)
    val mainActionProps = props.mainActionProps
    val secondaryActionProps = props.secondaryActionProps
    val utilityButtonProps = props.utilityButtonProps
    val actionButtonPropsList = props.actionButtonPropsList
    when (props.type) {
        ODSCardsPreferredActionsType.SINGLE_ACTION -> {
            if (mainActionProps != null) {
                ODSSingleAction(
                    modifier = modifier,
                    scheme = scheme,
                    style = style,
                    mainActionProps = mainActionProps,
                    onMainActionButtonClick = onMainActionButtonClick
                )
            }
        }

        ODSCardsPreferredActionsType.DOUBLE_ACTION -> {
            if (mainActionProps != null && secondaryActionProps != null) {
                ODSDoubleAction(
                    modifier = modifier,
                    scheme = scheme,
                    style = style,
                    mainActionProps = mainActionProps,
                    secondaryActionProps = secondaryActionProps,
                    onMainActionButtonClick = onMainActionButtonClick,
                    onSecondaryActionButtonClick = onSecondaryActionButtonClick
                )
            }
        }

        ODSCardsPreferredActionsType.MORE_ACTIONS,
        ODSCardsPreferredActionsType.MORE_ACTIONS_EXPANDED,
            -> {
            if (utilityButtonProps != null && actionButtonPropsList != null) {
                ODSMoreActions(
                    modifier = modifier,
                    scheme = scheme,
                    style = style,
                    buttonType = props.type,
                    showFirstAction = props.showFirstAction,
                    utilityButtonProps = utilityButtonProps,
                    actionButtonPropsList = actionButtonPropsList,
                    onClick = {
                        when (props.type) {
                            ODSCardsPreferredActionsType.MORE_ACTIONS -> {
                                onMoreButtonClick()
                            }

                            ODSCardsPreferredActionsType.MORE_ACTIONS_EXPANDED -> {
                                onCloseButtonClick()
                            }

                            else -> {}
                        }
                    },
                    onActionsListButtonClick = onActionsListButtonClick
                )
            }
        }
    }
}

@Composable
private fun ODSSingleAction(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSCardsPreferredActionsStyle,
    mainActionProps: ODSButtonProps,
    onMainActionButtonClick: () -> Unit,
) {
    ODSColumn(modifier = modifier) {
        ODSRow(
            gap = style.gap,
            horizontalArrangement = style.horizontalArrangement,
            horizontalAlignment = style.horizontalAlignment,
            verticalAlignment = style.verticalAlignment,
        ) {
            ODSButton(
                scheme = scheme,
                props = mainActionProps,
                onClick = onMainActionButtonClick
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ODSDoubleAction(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSCardsPreferredActionsStyle,
    mainActionProps: ODSButtonProps,
    secondaryActionProps: ODSButtonProps,
    onMainActionButtonClick: () -> Unit,
    onSecondaryActionButtonClick: () -> Unit,
) {
    ODSColumn(modifier = modifier) {
        ODSWrap(
            horizontalGap = style.gap,
            horizontalArrangement = style.horizontalArrangement,
            horizontalAlignment = style.horizontalAlignment,
            verticalAlignment = style.verticalAlignment,
        ) {
            ODSButton(
                scheme = scheme,
                props = secondaryActionProps,
                onClick = onSecondaryActionButtonClick
            )
            ODSButton(
                scheme = scheme,
                props = mainActionProps,
                onClick = onMainActionButtonClick
            )
        }
    }
}

@Composable
private fun ODSMoreActions(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSCardsPreferredActionsStyle,
    buttonType: ODSCardsPreferredActionsType,
    showFirstAction: Boolean,
    utilityButtonProps: ODSCardsPreferredActionsUtilityButtonProps,
    actionButtonPropsList: List<ODSActionButtonModel>,
    onClick: () -> Unit,
    onActionsListButtonClick: (index: Int) -> Unit,
) {
    ODSColumn(modifier = modifier) {
        ODSRow(
            gap = style.gap,
            horizontalArrangement = style.horizontalArrangement,
            horizontalAlignment = style.horizontalAlignment,
            verticalAlignment = style.verticalAlignment,
        ) {
            val context = LocalContext.current
            var utilityButtonWidth by remember { mutableIntStateOf(0) }
            val actionButtonAnimationOffset =
                -(utilityButtonWidth + (style.gap?.value?.toInt() ?: 0))
            ODSButton(
                modifier = Modifier
                    .onGloballyPositioned { utilityButtonWidth = it.size.width }
                    .zIndex(1f),
                scheme = scheme,
                props = utilityButtonProps.toODSButtonProps(
                    buttonIcon = getButtonIconModel(context = context, buttonType = buttonType)
                ),
                onClick = onClick
            )
            if (showFirstAction) {
                ODSMoreActionsFirstButton(
                    modifier = Modifier.zIndex(0f),
                    scheme = scheme,
                    style = style,
                    actionButtonAnimationOffset = actionButtonAnimationOffset,
                    buttonType = buttonType,
                    actionButtonPropsList = actionButtonPropsList,
                    onActionsListButtonClick = onActionsListButtonClick
                )
            }
        }
        ODSExpandedButtonsContainer(
            scheme = scheme,
            style = style,
            buttonType = buttonType,
            actionButtonPropsList = actionButtonPropsList,
            onActionsListButtonClick = onActionsListButtonClick
        )
    }
}

@Composable
private fun ODSMoreActionsFirstButton(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSCardsPreferredActionsStyle,
    actionButtonAnimationOffset: Int,
    buttonType: ODSCardsPreferredActionsType,
    actionButtonPropsList: List<ODSActionButtonModel>,
    onActionsListButtonClick: (index: Int) -> Unit,
) {
    ODSRow(
        modifier = modifier,
        gap = style.actionsListContainerGap,
        horizontalArrangement = style.actionsListContainerHorizontalArrangement,
        horizontalAlignment = style.actionsListContainerHorizontalAlignment,
        verticalAlignment = style.actionsListContainerVerticalAlignment
    ) {
        val scaleX by animateFloatAsState(
            targetValue = if (buttonType == ODSCardsPreferredActionsType.MORE_ACTIONS) 1f else 0f,
            animationSpec = tween(durationMillis = DEFAULT_ANIMATION_DURATION)
        )
        actionButtonPropsList.firstOrNull()?.buttonProps?.let { buttonProps ->
            AnimatedVisibility(
                visible = buttonType == ODSCardsPreferredActionsType.MORE_ACTIONS,
                enter = fadeIn(animationSpec = tween(DEFAULT_ANIMATION_DURATION)) + slideInHorizontally(
                    initialOffsetX = { actionButtonAnimationOffset },
                    animationSpec = tween(durationMillis = DEFAULT_ANIMATION_DURATION)
                ),
                exit = fadeOut(animationSpec = tween(DEFAULT_ANIMATION_DURATION)) + slideOutHorizontally(
                    targetOffsetX = { actionButtonAnimationOffset },
                    animationSpec = tween(durationMillis = DEFAULT_ANIMATION_DURATION)
                )
            ) {
                ODSButton(
                    modifier = Modifier.graphicsLayer {
                        this.scaleX = scaleX
                        transformOrigin =
                            TransformOrigin(PIVOT_FRACTION_X, PIVOT_FRACTION_Y)
                    },
                    scheme = scheme,
                    props = buttonProps,
                    onClick = { onActionsListButtonClick(0) }
                )
            }
        }
    }
}

@Composable
private fun ODSExpandedButtonsContainer(
    scheme: ODSTheme,
    style: ODSCardsPreferredActionsStyle,
    buttonType: ODSCardsPreferredActionsType,
    actionButtonPropsList: List<ODSActionButtonModel>,
    onActionsListButtonClick: (index: Int) -> Unit,
) {
    ODSBox {
        val isExpanded = buttonType == ODSCardsPreferredActionsType.MORE_ACTIONS_EXPANDED
        ODSBox(
            modifier = Modifier.animateContentSize(
                animationSpec = tween(
                    DEFAULT_ANIMATION_DURATION
                )
            )
        ) {
            if (isExpanded) {
                ODSExpandedButtonsAbsoluteFrame(
                    scheme = scheme,
                    style = style,
                    actionButtonPropsList = actionButtonPropsList,
                )
            }
        }
        if (isExpanded) {
            ODSExpandedButtons(
                scheme = scheme,
                style = style,
                buttonType = buttonType,
                actionButtonPropsList = actionButtonPropsList,
                onActionsListButtonClick = onActionsListButtonClick
            )
        }
    }
}

@Composable
private fun ODSExpandedButtonsAbsoluteFrame(
    scheme: ODSTheme,
    style: ODSCardsPreferredActionsStyle,
    actionButtonPropsList: List<ODSActionButtonModel>,
) {
    ODSColumn(
        modifier = Modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {}
        },
        gap = style.actionsListContainerGap,
        verticalArrangement = style.actionsListContainerVerticalArrangement,
        horizontalAlignment = style.actionsListContainerHorizontalAlignment,
        verticalAlignment = style.actionsListContainerVerticalAlignment
    ) {
        actionButtonPropsList.forEachIndexed { index, actionButtonProps ->
            actionButtonProps.buttonProps?.let {
                ODSButton(
                    scheme = scheme,
                    props = it,
                ) { }
            }
        }
    }
}

@Composable
private fun ODSExpandedButtons(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    style: ODSCardsPreferredActionsStyle,
    buttonType: ODSCardsPreferredActionsType,
    actionButtonPropsList: List<ODSActionButtonModel>,
    onActionsListButtonClick: (index: Int) -> Unit,
) {
    ODSColumn(
        modifier = modifier,
        gap = style.actionsListContainerGap,
        verticalArrangement = style.actionsListContainerVerticalArrangement,
        horizontalAlignment = style.actionsListContainerHorizontalAlignment,
        verticalAlignment = style.actionsListContainerVerticalAlignment
    ) {
        var visibleItems by remember { mutableIntStateOf(0) }
        if (buttonType == ODSCardsPreferredActionsType.MORE_ACTIONS_EXPANDED) {
            LaunchedEffect(Unit) {
                actionButtonPropsList.forEachIndexed { index, _ ->
                    delay(ANIMATION_DELAY)
                    visibleItems = index + 1
                }
            }
        }
        actionButtonPropsList.forEachIndexed { index, actionButtonProps ->
            actionButtonProps.buttonProps?.let {
                AnimatedVisibility(
                    visible = index < visibleItems,
                    enter = fadeIn(animationSpec = tween(durationMillis = DEFAULT_ANIMATION_DURATION / 2)) +
                            slideInHorizontally(animationSpec = tween(durationMillis = DEFAULT_ANIMATION_DURATION)),
                    exit = fadeOut(animationSpec = tween(durationMillis = DEFAULT_ANIMATION_DURATION))
                ) {
                    ODSButton(
                        scheme = scheme,
                        props = it,
                    ) {
                        onActionsListButtonClick(index)
                    }
                }
            }
        }
    }
}

private fun getButtonIconModel(
    context: Context,
    buttonType: ODSCardsPreferredActionsType,
): ODSIconModel {
    return if (buttonType == ODSCardsPreferredActionsType.MORE_ACTIONS) {
        ODSIconModel(
            contentDescription = context.getString(R.string.semantics_more),
            drawableRes = R.drawable.more_type_standard
        )
    } else {
        ODSIconModel(
            contentDescription = context.getString(R.string.semantics_close),
            drawableRes = R.drawable.close_type_standard
        )
    }
}

private const val ANIMATION_DELAY = (DEFAULT_ANIMATION_DURATION / 4).toLong()
private const val PIVOT_FRACTION_X = 0.2F
private const val PIVOT_FRACTION_Y = 0.5F
