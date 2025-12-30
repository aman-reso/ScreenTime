package com.app.screentime.ntoificationstack

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ManageServiceNotificationStack(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    notifications: List<ODSCardNotificationModel>,
    viewAllText: String? = null,
    collapseAllText: String? = null,
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val measuredHeights = remember(
        configuration.orientation,
        LocalWindowInfo.current.containerSize,
        LocalWindowInfo.current.containerSize
    ) {
        mutableStateMapOf<Int, Dp>()
    }
    var isExpandedState by remember { mutableStateOf(false) }

    LaunchedEffect(configuration.orientation) {
        isExpandedState = false
    }

    if (measuredHeights.isEmpty() && notifications.isNotEmpty()) {
        notifications.forEachIndexed { index, notification ->
            MeasureComposableHeight(
                key = "${configuration.orientation}_${LocalWindowInfo.current.containerSize}_$index",
                content = {
                    OANotificationCardWithPaddingAsParent(
                        model = notification, showDismissIcon = false
                    )
                }) onHeightMeasured@{ height ->
                if (height != 0) {
                    with(density) {
                        measuredHeights[index] = height.toDp()
                    }
                }
            }
        }
    }

    val transition =
        updateTransition(targetState = isExpandedState, label = "StackToListTransition")

    val verticalSpacing = if (isExpandedState) {
        DSVariables.spacingComponent3
    } else {
        DSVariables.spacingComponent4
    }

    val displayList = if (isExpandedState) notifications else notifications.take(3)

    val hasValidMeasurements by remember(measuredHeights) {
        derivedStateOf { measuredHeights.isNotEmpty() && measuredHeights.values.any { it > 0.dp } }
    }

    val targetHeight by remember(measuredHeights, isExpandedState) {
        derivedStateOf {
            if (!hasValidMeasurements) {
                return@derivedStateOf null
            }

            var totalHeight = 0.dp
            if (isExpandedState) {
                for (index in notifications.indices) {
                    val height = measuredHeights[index] ?: 0.dp
                    if (height > 0.dp) {
                        totalHeight += height + verticalSpacing
                    }
                }
                if (totalHeight > 0.dp) {
                    totalHeight -= verticalSpacing
                }
            } else {
                val firstHeight = measuredHeights[0] ?: 0.dp
                if (firstHeight > 0.dp) {
                    totalHeight = firstHeight + verticalSpacing * (displayList.size - 1)
                }
            }
            totalHeight
        }
    }

    val animatedHeight by transition.animateDp(
        label = "boxHeight", transitionSpec = { tween(200) }) { targetHeight ?: 0.dp }

    ODSColumn(
        modifier = modifier
    ) {
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (hasValidMeasurements) {
                        Modifier.height(animatedHeight)
                    } else {
                        Modifier.wrapContentHeight()
                    }
                )
        ) {
            displayList.forEachIndexed { index, notification ->
                val targetOffset = if (isExpandedState) {
                    var offset = 0.dp
                    for (i in 0 until index) {
                        offset += (measuredHeights[i] ?: 0.dp) + verticalSpacing
                    }
                    offset.value
                } else {
                    index * verticalSpacing.value
                }

                val animatedOffset by transition.animateDp(
                    label = "offsetAnimation$index",
                    transitionSpec = { tween(durationMillis = 200) }) { targetOffset.dp }

                val backgroundColor = if (isExpandedState) {
                    scheme.shadesNeutralShades200
                } else {
                    when (index) {
                        0 -> scheme.shadesNeutralShades200
                        1 -> scheme.shadesNeutralShades300
                        2 -> scheme.shadesNeutralShades400
                        else -> scheme.shadesNeutralShades200
                    }
                }

                val targetScaleX = if (isExpandedState) {
                    1f
                } else {
                    when (index) {
                        0 -> 1f
                        1 -> 0.95f
                        2 -> 0.9f
                        else -> 1f
                    }
                }

                val animatedScaleX by transition.animateFloat(
                    label = "scaleXAnimation$index",
                    transitionSpec = { tween(durationMillis = 200) }) { targetScaleX }

                OAServiceNotificationSingle(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            if (isExpandedState) {
                                measuredHeights[index] ?: 0.dp
                            } else {
                                measuredHeights[0] ?: 0.dp
                            }
                        )
                        .graphicsLayer {
                            translationY = animatedOffset.toPx()
                            scaleX = animatedScaleX
                        }
                        .zIndex((displayList.size - index).toFloat()),
                    colorModel = listOf(ODSColorModel(backgroundColor)),
                    props = notification.notificationProps,
                    scheme = scheme)
            }
        }

        if (notifications.size > 1) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ODSButton(
                    scheme = scheme, props = ODSButtonProps(
                        rightIcon = true,
                        size = ODSButtonSize.SMALL,
                        buttonType = ODSButtonButtonType.STANDARD,
                        variant = ODSButtonVariant.GHOST,
                        label = if (isExpandedState) {
                            collapseAllText
                        } else {
                            viewAllText
                        },
                        buttonIcon = ODSIconModel(
                            drawableRes = if (isExpandedState) {
                                com.telekom.odsystem.R.drawable.collapse_up_type_standard
                            } else {
                                com.telekom.odsystem.R.drawable.collapse_down_type_standard
                            }
                        ),
                    ), modifier = Modifier
                ) {
                    isExpandedState = !isExpandedState
                }
            }
        }
    }
}


@Composable
private fun MeasureComposableHeight(
    key: String = "measuredContent",
    content: @Composable () -> Unit,
    onHeightMeasured: (Int) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val measuredChild = subcompose(key, content).first().measure(
            constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)
        )
        onHeightMeasured(measuredChild.height)

        layout(0, 0) {}
    }
}

internal fun Color.toHexColor(): HexColor {
    val alpha = (this.alpha * 255).toInt()
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    val hexString = String.format("#%02X%02X%02X%02X", alpha, red, green, blue)
    return HexColor(hexString, this.alpha)
}