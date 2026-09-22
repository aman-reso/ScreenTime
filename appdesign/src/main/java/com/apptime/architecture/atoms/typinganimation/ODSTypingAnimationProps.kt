package com.telekom.odsystem.atoms.typinganimation

import com.telekom.odsystem.atoms.typinganimation.ODSTypingAnimationPosition.ONE
import com.telekom.odsystem.atoms.typinganimation.ODSTypingAnimationPosition.THREE
import com.telekom.odsystem.atoms.typinganimation.ODSTypingAnimationPosition.TWO

/**
 * Represents the position of the typing animation dots.
 *
 * This enum is used to control which dot in the typing animation is currently highlighted or active.
 * - [ONE]: Represents the first dot.
 * - [TWO]: Represents the second dot.
 * - [THREE]: Represents the third dot.
 */
enum class ODSTypingAnimationPosition {
    ONE,
    TWO,
    THREE,
}

/**
 * Properties for the ODS Typing Animation.
 *
 * @property position The current position of the typing animation. Defaults to [ODSTypingAnimationPosition.ONE].
 */
data class ODSTypingAnimationProps(
    var position: ODSTypingAnimationPosition = ONE,
)
