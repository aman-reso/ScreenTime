package com.telekom.odsystem.foundations

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusProperties
import androidx.compose.ui.focus.FocusPropertiesModifierNode
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.input.InputModeManager
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics

/**
 * Created by dmarinopoulos on 2/2/24
 */
enum class ODSActions {
    DEFAULT,
    PRESSED,
    HOVERED
}

@Suppress("LongMethod")
@Composable
fun Modifier.customClickable(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    isPressed: (Boolean) -> Unit,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    role: Role? = null,
    disabled: Boolean = false,
    readOnly: Boolean = false
): Modifier {
    if (onClick == null) {
        return this
    }
    val isHoveredState = interactionSource.collectIsHoveredAsState()
    val click by rememberUpdatedState(onClick)
    val pressed by rememberUpdatedState(isPressed)
    return if (!disabled && !readOnly) {
        semantics(mergeDescendants = true) {
            if (role != null) {
                this.role = role
            }
            onClick(
                action = { click(); true },
                label = onClickLabel
            )
        }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed(true)
                        if (this.tryAwaitRelease()) {
                            click()
                            pressed(false)
                        } else {
                            pressed(false)
                        }
                    }
                )
            }
            .hoverable(interactionSource, true)
            .let {
                if (!isHoveredState.value) {
                    it.indication(
                        interactionSource,
                        LocalIndication.current
                    )
                } else {
                    it
                }
            } // Disable focus indication while hovering
            .focusableInNonTouchMode(true, interactionSource)
            .onKeyEvent { event ->
                if (event.isPress) {
                    pressed(true)
                    true
                } else if (event.isClick) {
                    click()
                    pressed(false)
                    true
                } else {
                    false // Does not consume the event
                }
            }
    } else {
        semantics(mergeDescendants = true) {
            if (role != null) {
                this.role = role
            }
            if (disabled) {
                this.disabled()
            }
        }
    }
}

internal val KeyEvent.isPress: Boolean
    get() = type == KeyEventType.KeyDown && isEnter

internal val KeyEvent.isClick: Boolean
    get() = type == KeyEventType.KeyUp && isEnter

private val KeyEvent.isEnter: Boolean
    get() = when (key.nativeKeyCode) {
        android.view.KeyEvent.KEYCODE_DPAD_CENTER, android.view.KeyEvent.KEYCODE_ENTER, android.view.KeyEvent.KEYCODE_NUMPAD_ENTER -> true
        else -> false
    }

internal fun Modifier.focusableInNonTouchMode(
    enabled: Boolean,
    interactionSource: MutableInteractionSource?
) = then(if (enabled) FocusableInNonTouchModeElement else Modifier)
    .focusable(enabled, interactionSource)

private val FocusableInNonTouchModeElement =
    object : ModifierNodeElement<FocusableInNonTouchMode>() {
        override fun create(): FocusableInNonTouchMode =
            FocusableInNonTouchMode()

        override fun update(node: FocusableInNonTouchMode) {}

        override fun hashCode(): Int = System.identityHashCode(this)

        override fun equals(other: Any?): Boolean = this === other

        override fun InspectorInfo.inspectableProperties() {
            name = "focusableInNonTouchMode"
        }
    }

private class FocusableInNonTouchMode : Modifier.Node(), CompositionLocalConsumerModifierNode,
    FocusPropertiesModifierNode {

    private val inputModeManager: InputModeManager
        get() = currentValueOf(LocalInputModeManager)

    override fun applyFocusProperties(focusProperties: FocusProperties) {
        focusProperties.apply {
            canFocus = inputModeManager.inputMode != InputMode.Touch
        }
    }
}
