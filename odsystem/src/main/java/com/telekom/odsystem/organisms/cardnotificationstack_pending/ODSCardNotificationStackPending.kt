// package com.telekom.odsystem.organisms.cardnotificationstack
//
// import androidx.compose.animation.Animatable
// import androidx.compose.animation.animateContentSize
// import androidx.compose.animation.core.EaseInOut
// import androidx.compose.animation.core.animateDpAsState
// import androidx.compose.animation.core.animateFloatAsState
// import androidx.compose.animation.core.tween
// import androidx.compose.foundation.layout.fillMaxWidth
// import androidx.compose.foundation.layout.height
// import androidx.compose.foundation.layout.offset
// import androidx.compose.foundation.layout.padding
// import androidx.compose.foundation.layout.wrapContentHeight
// import androidx.compose.runtime.Composable
// import androidx.compose.runtime.LaunchedEffect
// import androidx.compose.runtime.MutableState
// import androidx.compose.runtime.getValue
// import androidx.compose.runtime.mutableStateOf
// import androidx.compose.runtime.remember
// import androidx.compose.runtime.rememberCoroutineScope
// import androidx.compose.runtime.setValue
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.draw.alpha
// import androidx.compose.ui.layout.onGloballyPositioned
// import androidx.compose.ui.platform.LocalDensity
// import androidx.compose.ui.unit.dp
// import androidx.compose.ui.zIndex
// import com.telekom.odsystem.R
// import com.telekom.odsystem.atoms.ODSBox
// import com.telekom.odsystem.atoms.ODSColumn
// import com.telekom.odsystem.atoms.ODSRow
// import com.telekom.odsystem.atoms.button.ODSButton
// import com.telekom.odsystem.atoms.button.ODSButtonButtonType
// import com.telekom.odsystem.atoms.button.ODSButtonProps
// import com.telekom.odsystem.atoms.button.ODSButtonSize
// import com.telekom.odsystem.atoms.button.ODSButtonVariant
// import com.telekom.odsystem.atoms.icon.ODSIconModel
// import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
// import com.telekom.odsystem.foundations.toHexColor
// import com.telekom.odsystem.neutralScheme
// import com.telekom.odsystem.organisms.cardnotification_pending.CardNotificationAnimationConstants
// import com.telekom.odsystem.organisms.cardnotification_pending.ODSCardNotification
// import com.telekom.odsystem.organisms.cardnotification_pending.ODSCardNotificationProps
// import com.telekom.odsystem.tokens.ODSTheme
// import kotlinx.coroutines.delay
// import kotlinx.coroutines.launch
//
// @Suppress("LongMethod")
// @Composable
// fun ODSCardNotificationStack(
//    modifier: Modifier = Modifier,
//    scheme: ODSTheme = neutralScheme,
//    props: MutableState<ODSCardNotificationStackProps> = mutableStateOf(
//        ODSCardNotificationStackProps()
//    ),
//    onDismiss: (ODSCardNotificationModel?) -> Unit = {}
// ) {
//    val style = ODSCardNotificationStackStyle().getStyle(scheme = scheme, props = props.value)
//    val scope = rememberCoroutineScope()
//
//    val isExpanded = props.value.expanded
//    var expandedAnimationFinished by remember { mutableStateOf(props.value.expanded) }
//
//    var card1Height by remember { mutableStateOf(0.dp) }
//    var card2Height by remember { mutableStateOf(0.dp) }
//    var card3Height by remember { mutableStateOf(0.dp) }
//    var card4Height by remember { mutableStateOf(0.dp) }
//    var card4Width by remember { mutableStateOf(0.dp) }
//    val density = LocalDensity.current
//    var isCardEntering by remember { mutableStateOf(false) }
//
//    var firstCard by remember {
//        mutableStateOf(props.value.notificationCards?.getOrNull(0))
//    }
//    var secondCard by remember {
//        mutableStateOf(props.value.notificationCards?.getOrNull(1))
//    }
//    var thirdCard by remember {
//        mutableStateOf(props.value.notificationCards?.getOrNull(2))
//    }
//    var fourthCard by remember {
//        mutableStateOf(props.value.notificationCards?.getOrNull(FOURTH_CARD))
//    }
//
//    val hasTwoCards = secondCard != null
//    val hasThreeCards = thirdCard != null
//
//    if (props.value.notificationCards?.isEmpty() == true) {
//        return
//    }
//
//    fun setupCards() {
//        firstCard = props.value.notificationCards?.getOrNull(0)
//        secondCard = props.value.notificationCards?.getOrNull(1)
//        thirdCard = props.value.notificationCards?.getOrNull(2)
//        fourthCard = props.value.notificationCards?.getOrNull(FOURTH_CARD)
//    }
//
//    fun updateList(cardModel: ODSCardNotificationModel?) {
//        val index = props.value.notificationCards?.indexOf(cardModel) ?: -1
//        props.value = props.value.copy(
//            notificationCards = props.value.notificationCards?.filter { card -> card != cardModel }
//        )
//
//        if (isExpanded) {
//            isCardEntering = true
//            if (index == 0) {
//                firstCard = null
//            } else if (index == 1) {
//                secondCard = null
//            }
//        } else {
//            setupCards()
//        }
//    }
//
//    LaunchedEffect(isCardEntering) {
//        if (isCardEntering) {
//            delay(DEFAULT_ANIMATION_DURATION.toLong())
//            setupCards()
//            isCardEntering = false
//        }
//    }
//
//    ODSColumn(
//        modifier = modifier
//            .fillMaxWidth()
//            .wrapContentHeight(),
//        gap = style.gap,
//        verticalArrangement = style.verticalArrangement,
//        verticalAlignment = style.verticalAlignment,
//        horizontalAlignment = style.horizontalAlignment,
//        clipContent = false
//    ) {
//
//        ODSBox(
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentHeight()
//                .animateContentSize(),
//            clipContent = false,
//        ) {
//            val gap = style.gap ?: 0.dp
//            var isDismissing by remember {
//                mutableStateOf(false)
//            }
//            var startDismissAnimation by remember {
//                mutableStateOf(false)
//            }
//
//            val card2ExpandedOffset = card1Height + if (hasTwoCards && isExpanded) gap else 0.dp
//            val card3ExpandedOffset =
//                card2ExpandedOffset + card2Height + if (hasThreeCards) gap else 0.dp
//
//            val card3Offset =
//                if (isExpanded && !isCardEntering) {
//                    card3ExpandedOffset
//                } else {
//                    ((2f * (style.cardHolderHeight?.value
//                        ?: 0f)).dp)
//                }
//
//            val card3Padding by animateDpAsState(
//                if (isExpanded) 0.dp else 32.dp,
//                animationSpec = tween(
//                    durationMillis = DEFAULT_ANIMATION_DURATION,
//                    easing = EaseInOut
//                ),
//                label = ""
//            )
//            val card2Offset =
//                if (isExpanded) card2ExpandedOffset else style.cardHolderHeight ?: 0.dp
//
//            val card2Padding by animateDpAsState(
//                if (isExpanded) 0.dp else 16.dp,
//                animationSpec = tween(
//                    durationMillis = DEFAULT_ANIMATION_DURATION,
//                    easing = EaseInOut
//                ),
//                label = ""
//            )
//
//            val placeholderExpandedHeight =
//                card3ExpandedOffset + card3Height + if (isCardEntering && hasThreeCards) card4Height else 0.dp
//            println(placeholderExpandedHeight)
//            val placeholderCollapsedHeight =
//                card1Height + if (hasThreeCards) card3Offset else card2Offset
//
//            val placeholderBoxHeight by animateDpAsState(
//                if (isExpanded) placeholderExpandedHeight else (placeholderCollapsedHeight),
//                label = "",
//            )
//
//            ODSBox(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(placeholderBoxHeight)
//            ) {
//            }
//
//            if (firstCard != null) {
//                ODSCardNotification(
//                    modifier = Modifier
//                        .zIndex(FIRST_CARD_Z_INDEX)
//                        .animateContentSize(),
//                    internalModifier = Modifier.onGloballyPositioned { coordinates ->
//                        val heightPx = coordinates.size.height
//                        card1Height = with(density) { heightPx.toDp() }
//                    },
//                    scheme = scheme,
//                    props = firstCard?.notificationCardProps
//                        ?: ODSCardNotificationProps(),
//                    backgroundColor = scheme.basicBackgroundCard,
//                    onDismiss = {
//                        if (!isExpanded && secondCard != null) {
//                            isDismissing = true
//                        } else {
//                            updateList(firstCard)
//                        }
//                    })
//            }
//
//            val heightModifierCard = if (isExpanded) {
//                Modifier.wrapContentHeight()
//            } else {
//                Modifier.height(card1Height)
//            }
//            val alphaForHiddenCards by animateFloatAsState(if (isExpanded) 1f else 0f, label = "")
//
//            if (secondCard != null) {
//                val secondCardOffset by animateDpAsState(
//                    targetValue = if (firstCard == null && isCardEntering) 0.dp else card2Offset,
//                    animationSpec = tween(DEFAULT_ANIMATION_DURATION, easing = EaseInOut),
//                    finishedListener = {
//                        expandedAnimationFinished = true
//                    },
//                    label = ""
//                )
//
//                if (isDismissing && !isExpanded) {
//                    val easing = EaseInOut
//
//                    val colorAnimatable =
//                        remember { Animatable(scheme.shadesNeutralShades300.getColor()) }
//                    LaunchedEffect(isDismissing) {
//                        scope.launch {
//                            colorAnimatable.animateTo(
//                                targetValue = if (!startDismissAnimation) scheme.basicBackgroundCard.getColor() else scheme.shadesNeutralShades300.getColor(),
//                                animationSpec = tween(
//                                    durationMillis = CardNotificationAnimationConstants.DismissCollapsed.DURATION,
//                                    delayMillis = CardNotificationAnimationConstants.DismissCollapsed.DELAY,
//                                    easing = easing
//                                )
//                            )
//                            delay(CardNotificationAnimationConstants.DismissCollapsed.DELAY.toLong())
//                            startDismissAnimation = true
//                            delay(CardNotificationAnimationConstants.DismissCollapsed.DURATION.toLong())
//                            updateList(firstCard)
//                            isDismissing = false
//                            startDismissAnimation = false
//                        }
//                    }
//
//                    val cardPlaceholderOffset by animateDpAsState(
//                        if (startDismissAnimation) 0.dp else style.cardHolderHeight ?: 0.dp,
//                        animationSpec = tween(
//                            durationMillis = CardNotificationAnimationConstants.DismissCollapsed.DURATION,
//                            easing = easing
//                        ),
//                        label = ""
//                    )
//                    val cardPlaceholderPadding by animateDpAsState(
//                        if (startDismissAnimation) 0.dp else 16.dp,
//                        animationSpec = tween(
//                            durationMillis = CardNotificationAnimationConstants.DismissCollapsed.DURATION,
//                            easing = easing
//                        ),
//                        label = ""
//                    )
//                    val cardPlaceholderAlpha by animateFloatAsState(
//                        if (startDismissAnimation) 1f else 0f,
//                        animationSpec = tween(
//                            durationMillis = CardNotificationAnimationConstants.DismissCollapsed.DURATION * 2,
//                            easing = easing
//                        ),
//                        label = ""
//                    )
//
//                    ODSCardNotification(
//                        modifier = Modifier
//                            .zIndex(SECOND_CARD_Z_INDEX)
//                            .offset(y = cardPlaceholderOffset),
//                        internalModifier = Modifier
//                            .height(card2Height)
//                            .onGloballyPositioned { coordinates ->
//                                val heightPx = coordinates.size.height
//                                card2Height = with(density) { heightPx.toDp() }
//                            }
//                            .padding(horizontal = cardPlaceholderPadding),
//                        scheme = scheme,
//                        props = secondCard?.notificationCardProps
//                            ?: ODSCardNotificationProps(),
//                        backgroundColor = if (startDismissAnimation) colorAnimatable.value.toHexColor() else scheme.shadesNeutralShades300,
//                        onDismiss = {},
//                        alpha = cardPlaceholderAlpha
//                    )
//                }
//
//                ODSCardNotification(
//                    modifier = Modifier
//                        .zIndex(2f)
//                        .offset(
//                            y = if (firstCard == null) {
//                                secondCardOffset
//                            } else {
//                                if (!expandedAnimationFinished) {
//                                    secondCardOffset
//                                } else {
//                                    card2Offset
//                                }
//                            }
//                        )
//                        .animateContentSize(),
//                    internalModifier = Modifier
//                        .then(
//                            heightModifierCard
//                        )
//                        .onGloballyPositioned { coordinates ->
//                            val heightPx = coordinates.size.height
//                            card2Height = with(density) { heightPx.toDp() }
//                        }
//                        .padding(horizontal = card2Padding),
//                    scheme = scheme,
//                    props = secondCard?.notificationCardProps
//                        ?: ODSCardNotificationProps(),
//                    backgroundColor = if (isExpanded) scheme.basicBackgroundCard else scheme.shadesNeutralShades300,
//                    onDismiss = {
//                        updateList(cardModel = secondCard)
//                    },
//                    alpha = alphaForHiddenCards
//                )
//            } else {
//                card2Height = 0.dp
//            }
//
//            if (thirdCard != null) {
//                val thirdCardOffset by animateDpAsState(
//                    targetValue = if ((firstCard == null || secondCard == null) && isCardEntering) card2Offset + gap else card3Offset,
//                    animationSpec = tween(
//                        if (fourthCard != null) DEFAULT_ANIMATION_DURATION else THIRD_CARD_ANIMATION_DURATION,
//                        easing = EaseInOut
//                    ),
//                    label = ""
//                )
//
//                ODSCardNotification(
//                    modifier = Modifier
//                        .zIndex(1f)
//                        .offset(
//                            y = if (firstCard == null || secondCard == null) {
//                                thirdCardOffset
//                            } else {
//                                if (!expandedAnimationFinished) thirdCardOffset else card3Offset
//                            }
//                        )
//                        .animateContentSize(),
//                    internalModifier = Modifier
//                        .then(
//                            heightModifierCard
//                        )
//                        .onGloballyPositioned { coordinates ->
//                            val heightPx = coordinates.size.height
//                            card3Height = with(density) { heightPx.toDp() }
//                        }
//                        .padding(horizontal = card3Padding),
//                    scheme = scheme,
//                    props = thirdCard?.notificationCardProps
//                        ?: ODSCardNotificationProps(),
//                    backgroundColor = if (isExpanded) scheme.basicBackgroundCard else scheme.shadesNeutralShades400,
//                    onDismiss = {
//                        updateList(cardModel = thirdCard)
//                    },
//                    alpha = alphaForHiddenCards
//                )
//            } else {
//                card3Height = 0.dp
//            }
//
//            if (fourthCard != null && isExpanded) {
//                ODSCardNotification(
//                    modifier = Modifier
//                        .alpha(0f)
//                        .zIndex(1f)
//                        .offset(
//                            y = card3Offset,
//                        )
//                        .animateContentSize(),
//                    internalModifier = Modifier
//                        .then(
//                            heightModifierCard
//                        )
//                        .onGloballyPositioned { coordinates ->
//                            val heightPx = coordinates.size.height
//                            val widthPx = coordinates.size.width
//                            card4Height = with(density) { heightPx.toDp() }
//                            card4Width = with(density) { widthPx.toDp() }
//                        }
//                        .padding(horizontal = card3Padding),
//                    scheme = scheme,
//                    props = fourthCard?.notificationCardProps
//                        ?: ODSCardNotificationProps(),
//                    onDismiss = {
//                    },
//                )
//            } else {
//                card4Height = 0.dp
//            }
//        }
//
//        ODSCardNotificationActionContainer(
//            props = props.value,
//            style = style,
//            scheme = scheme,
//            hasTwoCards = hasTwoCards,
//            onShowMoreClicked = {
//                props.value = props.value.copy(expanded = true)
//            },
//            onCollapseClicked = {
//                props.value = props.value.copy(expanded = false)
//                expandedAnimationFinished = false
//            },
//            onViewAllClicked = {}
//        )
//    }
// }
//
// @Suppress("LongMethod")
// @Composable
// private fun ODSCardNotificationActionContainer(
//    props: ODSCardNotificationStackProps,
//    style: ODSCardNotificationStackStyle,
//    scheme: ODSTheme,
//    hasTwoCards: Boolean,
//    onShowMoreClicked: () -> Unit,
//    onCollapseClicked: () -> Unit,
//    onViewAllClicked: () -> Unit
// ) {
//    if (hasTwoCards && !props.expanded) {
//        ODSColumn(
//            modifier = Modifier.fillMaxWidth(),
//            padding = style.actionPadding,
//            verticalArrangement = style.actionVerticalArrangement,
//            verticalAlignment = style.actionVerticalAlignment,
//            horizontalAlignment = style.actionHorizontalAlignment,
//        ) {
//            ODSButton(
//                scheme = scheme,
//                props = ODSButtonProps(
//                    buttonIcon = ODSIconModel(drawableRes = R.drawable.collapse_down_type_standard),
//                    buttonLabel = "Show more",
//                    buttonType = ODSButtonButtonType.STANDARD,
//                    leftIcon = false,
//                    rightIcon = true,
//                    size = ODSButtonSize.SMALL,
//                    variant = ODSButtonVariant.GHOST
//                )
//            ) {
//                onShowMoreClicked()
//            }
//        }
//    }
//
//    if (hasTwoCards && props.expanded) {
//        ODSRow(
//            modifier = Modifier.fillMaxWidth(),
//            padding = style.actionPadding,
//            horizontalArrangement = style.actionHorizontalArrangement,
//            verticalAlignment = style.actionVerticalAlignment,
//            horizontalAlignment = style.actionHorizontalAlignment,
//        ) {
//            ODSButton(
//                scheme = scheme,
//                props = ODSButtonProps(
//                    buttonIcon = ODSIconModel(drawableRes = R.drawable.collapse_up_type_standard),
//                    buttonLabel = "Collapse",
//                    buttonType = ODSButtonButtonType.STANDARD,
//                    leftIcon = false,
//                    rightIcon = true,
//                    size = ODSButtonSize.SMALL,
//                    variant = ODSButtonVariant.GHOST
//                )
//            ) {
//                onCollapseClicked()
//            }
//            ODSButton(
//                scheme = scheme,
//                props = ODSButtonProps(
//                    buttonIcon = ODSIconModel(drawableRes = R.drawable.navigation_right_type_standard),
//                    buttonLabel = "View all",
//                    buttonType = ODSButtonButtonType.STANDARD,
//                    leftIcon = false,
//                    rightIcon = true,
//                    size = ODSButtonSize.SMALL,
//                    variant = ODSButtonVariant.GHOST
//                )
//            ) {
//                onViewAllClicked()
//            }
//        }
//    }
// }
//
// private const val FOURTH_CARD = 3
// private const val FIRST_CARD_Z_INDEX = 3f
// private const val SECOND_CARD_Z_INDEX = 4f
// private const val THIRD_CARD_ANIMATION_DURATION = 150
