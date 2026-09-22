package com.telekom.odsystem.atoms.typinganimation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.delay

/**
 * The ODSTypingAnimation is used to display a typing indicator with three animated dots.
 * The animation cycles through three positions, highlighting each dot sequentially.
 * This component is useful for indicating activity or loading states.
 *
 * @param modifier The modifier to be applied to the animation.
 * @param scheme The ODSTheme to be used for styling the animation. Defaults to neutralScheme.
 */
@Composable
fun ODSTypingAnimation(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
) {
    var position by remember { mutableStateOf(ODSTypingAnimationPosition.ONE) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(DEFAULT_ANIMATION_DURATION.toLong())
            position = when (position) {
                ODSTypingAnimationPosition.ONE -> ODSTypingAnimationPosition.TWO
                ODSTypingAnimationPosition.TWO -> ODSTypingAnimationPosition.THREE
                ODSTypingAnimationPosition.THREE -> ODSTypingAnimationPosition.ONE
            }
        }
    }
    val style = ODSTypingAnimationStyle().getStyle(
        scheme = scheme,
        props = ODSTypingAnimationProps(position = position)
    )
    val context = LocalContext.current

    ODSRow(
        modifier = modifier.semantics {
            contentDescription = context.getString(R.string.semantics_typing)
        },
        gap = style.gap,
        padding = style.padding,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement,
    ) {
        ODSBox(
            cornerRadius = style.dotCornerRadius,
            clipContent = style.dotClipContent != false,
            background = style.dotBackground,
            width = style.dotWidth,
            height = style.dotHeight
        ) {
        }
        ODSBox(
            cornerRadius = style.dot2CornerRadius,
            clipContent = style.dot2ClipContent != false,
            background = style.dot2Background,
            width = style.dot2Width,
            height = style.dot2Height
        ) {
        }
        ODSBox(
            cornerRadius = style.dot3CornerRadius,
            clipContent = style.dot3ClipContent != false,
            background = style.dot3Background,
            width = style.dot3Width,
            height = style.dot3Height
        ) {
        }
    }
}
