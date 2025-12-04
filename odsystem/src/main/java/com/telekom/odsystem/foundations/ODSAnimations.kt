package com.telekom.odsystem.foundations

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween

/**
Created by sarthakgupta on 15/04/25
 **/

val scaleAnimationSpec: TweenSpec<Float> =
    tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut)
