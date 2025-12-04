// package com.telekom.odsystem.organisms.cardnotification
//
// import androidx.compose.animation.core.Animatable
// import androidx.compose.animation.core.EaseInOut
// import androidx.compose.animation.core.TweenSpec
// import androidx.compose.animation.core.animateFloatAsState
// import androidx.compose.animation.core.tween
// import androidx.compose.foundation.gestures.detectHorizontalDragGestures
// import androidx.compose.foundation.interaction.MutableInteractionSource
// import androidx.compose.foundation.interaction.collectIsHoveredAsState
// import androidx.compose.foundation.layout.ExperimentalLayoutApi
// import androidx.compose.foundation.layout.fillMaxWidth
// import androidx.compose.foundation.layout.offset
// import androidx.compose.foundation.layout.padding
// import androidx.compose.runtime.Composable
// import androidx.compose.runtime.LaunchedEffect
// import androidx.compose.runtime.getValue
// import androidx.compose.runtime.mutableStateOf
// import androidx.compose.runtime.remember
// import androidx.compose.runtime.rememberCoroutineScope
// import androidx.compose.runtime.setValue
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.draw.alpha
// import androidx.compose.ui.draw.scale
// import androidx.compose.ui.input.pointer.pointerInput
// import androidx.compose.ui.layout.ContentScale
// import androidx.compose.ui.layout.onGloballyPositioned
// import androidx.compose.ui.platform.LocalDensity
// import androidx.compose.ui.semantics.Role
// import androidx.compose.ui.unit.dp
// import com.telekom.odsystem.R
// import com.telekom.odsystem.atoms.ODSBox
// import com.telekom.odsystem.atoms.ODSColumn
// import com.telekom.odsystem.atoms.ODSImage
// import com.telekom.odsystem.atoms.ODSRow
// import com.telekom.odsystem.atoms.ODSText
// import com.telekom.odsystem.atoms.ODSWrap
// import com.telekom.odsystem.atoms.button.ODSButton
// import com.telekom.odsystem.atoms.button.ODSButtonButtonType
// import com.telekom.odsystem.atoms.button.ODSButtonProps
// import com.telekom.odsystem.atoms.button.ODSButtonSize
// import com.telekom.odsystem.atoms.button.ODSButtonVariant
// import com.telekom.odsystem.atoms.icon.ODSIconModel
// import com.telekom.odsystem.foundations.DEFAULT_FACTOR
// import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
// import com.telekom.odsystem.foundations.HexColor
// import com.telekom.odsystem.foundations.ODSActions
// import com.telekom.odsystem.foundations.ODSColorModel
// import com.telekom.odsystem.foundations.SCALE_FACTOR
// import com.telekom.odsystem.foundations.customClickable
// import com.telekom.odsystem.neutralScheme
// import com.telekom.odsystem.tokens.ODSTheme
// import kotlinx.coroutines.delay
// import kotlinx.coroutines.launch
//
// @Composable
// fun ODSCardNotification(
//    modifier: Modifier = Modifier,
//    internalModifier: Modifier,
//    scheme: ODSTheme = neutralScheme,
//    props: ODSCardNotificationProps = ODSCardNotificationProps(),
//    onClick: () -> Unit = {},
//    onDismiss: () -> Unit,
//    onFirstButtonClick: () -> Unit = {},
//    onSecondButtonClick: () -> Unit = {},
//    backgroundColor: HexColor = neutralScheme.basicBackgroundCard,
//    alpha: Float = 1f
// ) {
//
//    val style = ODSCardNotificationStyle().getStyle(
//        scheme = scheme,
//        props = props,
//        state = ODSActions.DEFAULT
//    )
//
//    ODSCardNotificationContainer(
//        modifier = modifier,
//        internalModifier = internalModifier,
//        props = props,
//        style = style,
//        scheme = scheme,
//        onClick = onClick,
//        onDismiss = onDismiss,
//        onFirstButtonClick = onFirstButtonClick,
//        onSecondButtonClick = onSecondButtonClick,
//        backgroundColor = backgroundColor,
//        alpha = alpha
//    )
// }
//
// @Suppress("LongMethod")
// @Composable
// private fun ODSCardNotificationContainer(
//    modifier: Modifier,
//    internalModifier: Modifier,
//    props: ODSCardNotificationProps,
//    style: ODSCardNotificationStyle,
//    scheme: ODSTheme,
//    onClick: () -> Unit,
//    onFirstButtonClick: () -> Unit,
//    onSecondButtonClick: () -> Unit,
//    onDismiss: () -> Unit,
//    backgroundColor: HexColor,
//    alpha: Float = 1f
// ) {
//    var cardWidth by remember { mutableStateOf(0) }
//    val density = LocalDensity.current
//    var isDismissed by remember { mutableStateOf(false) }
//    val cardXOffset = remember { Animatable(initialValue = 0f) }
//    val scope = rememberCoroutineScope()
//    val interactionSource = remember { MutableInteractionSource() }
//    val isHovered by interactionSource.collectIsHoveredAsState()
//    var pressed by remember { mutableStateOf(false) }
//
//    val scale by animateFloatAsState(
//        targetValue = if (isHovered && !pressed) {
//            style.scaleFactor
//                ?: SCALE_FACTOR
//        } else {
//            DEFAULT_FACTOR
//        },
//        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
//        label = ""
//    )
//
//    LaunchedEffect(isDismissed) {
//        if (isDismissed) {
//            scope.launch {
//                cardXOffset.animateTo(
//                    targetValue = (1.5f * cardWidth),
//                    animationSpec = TweenSpec(durationMillis = CardNotificationAnimationConstants.DismissSwipe.DURATION, easing = EaseInOut)
//                )
//                // Wait in order to ensure the animation completes smoothly
//                delay(CardNotificationAnimationConstants.DismissSwipe.DURATION.toLong())
//                onDismiss()
//
//                // Wait until the dismiss collapsed animation takes place
//                delay(CardNotificationAnimationConstants.DismissCollapsed.DURATION * 2L)
//
//                isDismissed = false
//                cardXOffset.snapTo(0f)
//            }
//        }
//    }
//
//    ODSColumn(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = modifier
//            .onGloballyPositioned { coordinates ->
//                val widthPx = coordinates.size.width
//                cardWidth = with(density) { widthPx.toDp().value.toInt() }
//            }
//            .offset(x = -cardXOffset.value.dp)
//            .pointerInput(Unit) {
//                detectHorizontalDragGestures(
//                    onHorizontalDrag = { change, dragAmount ->
//                        // check if change is towards left
//                        if (dragAmount < 0) {
//                            change.consume()
//                            scope.launch {
//                                val newOffset =
//                                    cardXOffset.value - (dragAmount * DRAG_OFFSET) // Slow down the drag by applying a fraction
//                                cardXOffset.snapTo(newOffset)
//                            }
//                        }
//                    },
//                    onDragEnd = {
//                        scope.launch {
//                            if (cardXOffset.value > cardWidth * DRAG_END_OFFSET) {
//                                isDismissed = true
//                            } else {
//                                cardXOffset.animateTo(
//                                    targetValue = 0f,
//                                    animationSpec = TweenSpec(durationMillis = 300)
//                                )
//                            }
//                        }
//                    }
//                )
//            }
//    ) {
//        ODSBox(
//            modifier = internalModifier
//                .customClickable(
//                    isPressed = {
//                        pressed = it
//                    },
//                    interactionSource = interactionSource,
//                    onClick = onClick,
//                    role = Role.Button
//                )
//        ) {
//            ODSColumn(
//                modifier = Modifier.matchParentSize().scale(scale),
//                verticalArrangement = style.cardBgVerticalArrangement,
//                verticalAlignment = style.cardBgVerticalAlignment,
//                horizontalAlignment = style.cardBgHorizontalAlignment,
//                background = listOf(ODSColorModel(hexColor = backgroundColor)),
//                cornerRadius = style.cardBgBorderRadius,
//            ) {}
//
//            ODSColumn(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .alpha(alpha),
//                padding = style.padding,
//                verticalArrangement = style.verticalArrangement,
//                verticalAlignment = style.verticalAlignment,
//                horizontalAlignment = style.horizontalAlignment,
//            ) {
//                ODSHeaderContainer(
//                    props = props,
//                    style = style,
//                    scheme = scheme,
//                    onDismiss = {
//                        isDismissed = true
//                    }
//                )
//
//                ODSActionContainer(
//                    props = props,
//                    style = style,
//                    scheme = scheme,
//                    onFirstButtonClick = onFirstButtonClick,
//                    onSecondButtonClick = onSecondButtonClick
//                )
//            }
//            ODSBox(
//                modifier = Modifier.matchParentSize(),
//                clipContent = true
//            ) {
//                if (props.showImage && props.image != null) {
//                    ODSImage(
//                        modifier = Modifier
//                            .align(Alignment.BottomEnd)
//                            .padding(end = style.imageContainerEndPadding ?: 0.dp)
//                            .offset(y = style.imageContainerVerticalOffset ?: 0.dp),
//                        width = style.imageWidth,
//                        height = style.imageHeight,
//                        imageModel = props.image,
//                        contentScale = style.imageObjectFit ?: ContentScale.Crop,
//                    )
//                }
//            }
//        }
//    }
// }
//
// @Composable
// private fun ODSHeaderContainer(
//    props: ODSCardNotificationProps,
//    style: ODSCardNotificationStyle,
//    scheme: ODSTheme,
//    onDismiss: () -> Unit,
// ) {
//    ODSRow(
//        padding = style.headerContainerPadding,
//        horizontalArrangement = style.headerContainerHorizontalArrangement,
//        horizontalAlignment = style.headerContainerHorizontalAlignment,
//        verticalAlignment = style.headerContainerVerticalAlignment
//    ) {
//        ODSRow(
//            modifier = Modifier.weight(1f),
//            padding = style.textContainerPadding,
//            horizontalArrangement = style.textContainerHorizontalArrangement,
//            horizontalAlignment = style.textContainerHorizontalAlignment,
//            verticalAlignment = style.textContainerVerticalAlignment
//        ) {
//            if (!props.text.isNullOrEmpty()) {
//                ODSText(
//                    modifier = Modifier.weight(1f),
//                    text = props.text,
//                    style = style.headerTextStyle,
//                    color = style.headerColor,
//                    textAlign = style.headerTextAlign
//                )
//            }
//        }
//        ODSRow(
//            padding = style.buttonContainerPadding,
//            horizontalArrangement = style.buttonContainerHorizontalArrangement,
//            horizontalAlignment = style.buttonContainerHorizontalAlignment,
//            verticalAlignment = style.buttonContainerVerticalAlignment,
//            height = style.buttonContainerHeight,
//        ) {
//            ODSButton(
//                scheme = scheme,
//                props = ODSButtonProps(
//                    buttonIcon = ODSIconModel(drawableRes = R.drawable.close_type_standard),
//                    buttonType = ODSButtonButtonType.ICON_ONLY,
//                    size = ODSButtonSize.SMALL,
//                    variant = ODSButtonVariant.GHOST
//                )
//            ) {
//                onDismiss()
//            }
//        }
//    }
// }
//
// @OptIn(ExperimentalLayoutApi::class)
// @Composable
// private fun ODSActionContainer(
//    props: ODSCardNotificationProps,
//    style: ODSCardNotificationStyle,
//    scheme: ODSTheme,
//    onFirstButtonClick: () -> Unit,
//    onSecondButtonClick: () -> Unit
// ) {
//    ODSWrap(
//        gap = style.actionGap,
//        padding = style.actionPadding,
//        horizontalArrangement = style.actionHorizontalArrangement,
//        horizontalAlignment = style.actionHorizontalAlignment,
//        verticalAlignment = style.actionVerticalAlignment
//    ) {
//        props.firstButtonProps?.let {
//            ODSButton(
//                scheme = scheme,
//                props = it
//            ) {
//                onFirstButtonClick()
//            }
//        }
//        props.secondButtonProps?.let {
//            ODSButton(
//                scheme = scheme,
//                props = it
//            ) {
//                onSecondButtonClick()
//            }
//        }
//    }
// }
//
// object CardNotificationAnimationConstants {
//    object DismissCollapsed {
//        const val DURATION = 300
//        const val DELAY = 50
//    }
//
//    object DismissSwipe {
//        const val DURATION = 100
//    }
// }
//
// private const val DRAG_OFFSET = 0.4f
// private const val DRAG_END_OFFSET = 0.5f
