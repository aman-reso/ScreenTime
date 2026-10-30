package com.app.screentime.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene
import androidx.navigationevent.NavigationEvent

/**
 * CompositionLocal providing [SharedTransitionScope] across all app screens.
 * Enables smooth shared element transitions (e.g. avatar expansion, card-to-detail transitions).
 */
@OptIn(ExperimentalSharedTransitionApi::class)
val LocalNavSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope?> =
    staticCompositionLocalOf { null }

/**
 * Forward Navigation Transition Spec.
 *
 * Provides:
 * 1. Fast, subtle scale-fade between Bottom Navigation tabs (no horizontal slide jarring)
 * 2. Vertical slide-up sheet motion for modal screens (VoiceCall, VideoCall, Preferences)
 * 3. Smooth Material 3 Shared Axis X horizontal slide + fade for standard push navigation
 */
fun appForwardTransitionSpec(
    bottomNavRoutes: Set<Screen>
): AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform = {
    val initialScreen = initialState.key as? Screen
        ?: (initialState.entries.lastOrNull()?.contentKey as? Screen)
    val targetScreen = targetState.key as? Screen
        ?: (targetState.entries.lastOrNull()?.contentKey as? Screen)

    val isTabSwitch = initialScreen in bottomNavRoutes && targetScreen in bottomNavRoutes
    val isModalScreen = targetScreen is Screen.VoiceCall || targetScreen is Screen.VideoCall

    when {
        // Tab-to-Tab switch: quick crossfade with subtle scale
        isTabSwitch -> {
            (fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                    scaleIn(initialScale = 0.98f, animationSpec = tween(220, easing = FastOutSlowInEasing))) togetherWith
                    fadeOut(animationSpec = tween(180, easing = FastOutSlowInEasing))
        }

        // Call & Modal screens: slide up from bottom with spring physics
        isModalScreen -> {
            (slideInVertically(
                initialOffsetY = { (it * 0.35f).toInt() },
                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) + fadeIn(animationSpec = tween(260, easing = FastOutSlowInEasing))) togetherWith
                    (slideOutVertically(
                        targetOffsetY = { -(it * 0.10f).toInt() },
                        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
                    ) + fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing)))
        }

        // Standard forward navigation: slide in from right + fade
        else -> {
            (slideInHorizontally(
                initialOffsetX = { (it * 0.30f).toInt() },
                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) + fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing))) togetherWith
                    (slideOutHorizontally(
                        targetOffsetX = { -(it * 0.15f).toInt() },
                        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
                    ) + fadeOut(animationSpec = tween(220, easing = FastOutSlowInEasing)))
        }
    }
}

/**
 * Pop (Back) Navigation Transition Spec.
 *
 * Provides:
 * 1. Fast crossfade when returning between bottom tabs
 * 2. Vertical slide-down when exiting modal/call screens
 * 3. Smooth slide to right + fade out for standard back pop
 */
fun appPopTransitionSpec(
    bottomNavRoutes: Set<Screen>
): AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform = {
    val initialScreen = initialState.key as? Screen
        ?: (initialState.entries.lastOrNull()?.contentKey as? Screen)
    val targetScreen = targetState.key as? Screen
        ?: (targetState.entries.lastOrNull()?.contentKey as? Screen)

    val isTabSwitch = initialScreen in bottomNavRoutes && targetScreen in bottomNavRoutes
    val isExitingModal = initialScreen is Screen.VoiceCall || initialScreen is Screen.VideoCall

    when {
        // Tab-to-Tab switch: quick crossfade
        isTabSwitch -> {
            (fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                    scaleIn(initialScale = 0.98f, animationSpec = tween(220, easing = FastOutSlowInEasing))) togetherWith
                    fadeOut(animationSpec = tween(180, easing = FastOutSlowInEasing))
        }

        // Exiting Call & Modal screens: slide down to bottom
        isExitingModal -> {
            (slideInVertically(
                initialOffsetY = { -(it * 0.10f).toInt() },
                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) + fadeIn(animationSpec = tween(240, easing = FastOutSlowInEasing))) togetherWith
                    (slideOutVertically(
                        targetOffsetY = { (it * 0.35f).toInt() },
                        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
                    ) + fadeOut(animationSpec = tween(220, easing = FastOutSlowInEasing)))
        }

        // Standard pop back: slide in from left, slide current out to right
        else -> {
            (slideInHorizontally(
                initialOffsetX = { -(it * 0.15f).toInt() },
                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) + fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing))) togetherWith
                    (slideOutHorizontally(
                        targetOffsetX = { (it * 0.30f).toInt() },
                        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
                    ) + fadeOut(animationSpec = tween(220, easing = FastOutSlowInEasing)))
        }
    }
}

/**
 * Predictive Back Pop Transition Spec with Edge Detection & Spring Physics.
 */
fun appPredictivePopTransitionSpec(): AnimatedContentTransitionScope<Scene<NavKey>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform = { swipeEdge ->
    val slideMultiplier = if (swipeEdge == NavigationEvent.EDGE_LEFT) 1 else -1
    (slideInHorizontally(
        initialOffsetX = { (-it * 0.15f * slideMultiplier).toInt() },
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
    ) + fadeIn(animationSpec = tween(260))) togetherWith
            (slideOutHorizontally(
                targetOffsetX = { (it * 0.30f * slideMultiplier).toInt() },
                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) + scaleOut(targetScale = 0.90f) + fadeOut(animationSpec = tween(220)))
}
