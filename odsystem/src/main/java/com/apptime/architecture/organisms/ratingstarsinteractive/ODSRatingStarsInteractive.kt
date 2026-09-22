package com.telekom.odsystem.organisms.ratingstarsinteractive

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.starlistitem.ODSStarListItem
import com.telekom.odsystem.atoms.starlistitem.ODSStarListItemProps
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

const val ZERO = 0
const val FIVE = 5

/**
 * ODSRatingStarsInteractive composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onStarSelected Callback triggered when action occurs.
 */
@Suppress("LongMethod")
@Composable
fun ODSRatingStarsInteractive(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSRatingStarsInteractiveProps = ODSRatingStarsInteractiveProps(),
    onStarSelected: (Int) -> Unit = { },
) {
    val style = ODSRatingStarsInteractiveStyle().getStyle(scheme = scheme, props = props)
    ODSRatingStarsContainer(
        modifier = modifier,
        scheme = scheme,
        style = style,
        props = props,
        onStarSelected = onStarSelected
    )
}

@Suppress("LongMethod")
@Composable
private fun ODSRatingStarsContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSRatingStarsInteractiveStyle,
    props: ODSRatingStarsInteractiveProps,
    onStarSelected: (Int) -> Unit,
) {
    val context = LocalContext.current
    ODSColumn(
        modifier = modifier,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment
    ) {
        ODSLabelContainer(
            style = style,
            props = props
        )
        ODSRow(
            gap = style.ratingGap,
            horizontalAlignment = style.ratingHorizontalAlignment,
            verticalAlignment = style.ratingVerticalAlignment,
            horizontalArrangement = style.ratingHorizontalArrangement
        ) {

            ODSRow(
                modifier = Modifier.applySemanticsForDisabledAndReadOnlyStars(
                    props = props,
                    context = context
                ),
                horizontalAlignment = style.starsListContainerHorizontalAlignment,
                verticalAlignment = style.starsListContainerVerticalAlignment,
                horizontalArrangement = style.starsListContainerHorizontalArrangement
            ) {
                var pressedStars by remember { mutableIntStateOf(0) }
                var hoveredStars by remember { mutableIntStateOf(0) }
                val isAnyStarHovered = remember { mutableStateOf(false) }
                for (i in ZERO until FIVE) {
                    val starItemProps = ODSStarListItemProps(
                        disabled = props.disabled,
                        readOnly = props.readOnly,
                        selected = isSelected(
                            index = i,
                            props = props,
                            selectedStars = if (pressedStars == 0) props.stars ?: 0 else 0,
                            hoveredStars = hoveredStars
                        ),
                    )
                    ODSStarListItem(
                        modifier = if (starItemProps.readOnly || starItemProps.disabled) {
                            Modifier.clearAndSetSemantics {}
                        } else {
                            Modifier.applySemanticsForRatingSingleStar(
                                selectedStars = props.stars ?: 0,
                                context = context,
                                index = i + 1
                            )
                        },
                        scheme = scheme,
                        props = starItemProps,
                        onClick = { onStarSelected(i) },
                        onPressed = {
                            pressedStars = if (it) i + 1 else 0
                        },
                        onHovered = {
                            if (it) {
                                hoveredStars = i + 1
                                isAnyStarHovered.value = true
                            } else {
                                if (!isAnyStarHovered.value) {
                                    hoveredStars = 0
                                }
                            }
                        },
                        starState = getStartState(
                            index = i,
                            pressedStars = pressedStars,
                            hoveredStars = hoveredStars,
                            props = props
                        )
                    )
                }
                SideEffect { isAnyStarHovered.value = false }
            }
            if (!props.helperText.isNullOrEmpty()) {
                ODSText(
                    text = props.helperText,
                    style = style.helperTextTextStyle,
                    color = style.helperTextColor,
                    textAlign = style.helperTextTextAlign ?: TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ODSLabelContainer(
    style: ODSRatingStarsInteractiveStyle,
    props: ODSRatingStarsInteractiveProps
) {
    if (!props.ratingLabel.isNullOrEmpty()) {
        ODSRow(
            gap = style.labelContainerGap,
            padding = style.labelContainerPadding,
            horizontalAlignment = style.labelContainerHorizontalAlignment,
            verticalAlignment = style.labelContainerVerticalAlignment
        ) {
            ODSText(
                text = props.ratingLabel,
                style = style.ratingLabelTextStyle,
                color = style.ratingLabelColor,
                textAlign = style.ratingLabelTextAlign
            )
        }
    }
}

private fun getStartState(
    index: Int,
    pressedStars: Int,
    hoveredStars: Int,
    props: ODSRatingStarsInteractiveProps
): ODSActions {
    return when {
        isPressed(index = index, pressedStars = pressedStars, props = props) -> ODSActions.PRESSED
        isHovered(index = index, hoveredStars = hoveredStars, props = props) -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }
}

private fun isSelected(
    index: Int,
    props: ODSRatingStarsInteractiveProps,
    selectedStars: Int,
    hoveredStars: Int
): Boolean {
    if (!props.disabled) {
        return (index < selectedStars && hoveredStars == 0)
    }
    return false
}

private fun isPressed(
    index: Int,
    pressedStars: Int,
    props: ODSRatingStarsInteractiveProps
): Boolean {
    if (!props.disabled) {
        return index < pressedStars
    }
    return false
}

private fun isHovered(
    index: Int,
    hoveredStars: Int,
    props: ODSRatingStarsInteractiveProps
): Boolean {
    if (!props.disabled) {
        Log.e("Index is", index.toString())
        return index < hoveredStars
    }
    return false
}

private fun Modifier.applySemanticsForDisabledAndReadOnlyStars(
    props: ODSRatingStarsInteractiveProps,
    context: Context
): Modifier {
    return this.semantics(mergeDescendants = true) {
        var value = ""
        if (props.disabled) {
            value += context.getString(R.string.semantic_rating_disabled)
        } else if (props.readOnly) {
            value = value + (props.stars
                ?: 0).toString() + " , " + context.getString(R.string.semantic_out_of_five_stars) + " " + context.getString(
                R.string.semantic_selected
            )
            value = value + ", " + context.getString(R.string.semantic_read_only)
        }
        stateDescription = value
    }
}

private fun Modifier.applySemanticsForRatingSingleStar(
    selectedStars: Int,
    context: Context,
    index: Int?
): Modifier {
    val indexSelected = index ?: 0
    return this.semantics(mergeDescendants = true) {
        var value = ""
        value += "$selectedStars "
        value += context.getString(R.string.semantic_out_of_five_stars) + " " + context.getString(R.string.semantic_selected)
        value += ", " + context.getString(R.string.semantic_button) + ",  " + context.getString(R.string.semantic_double_tap_to_select) + " "
        value += indexSelected
        value += " " + context.getString(R.string.semantic_out_of_five_stars)
        stateDescription = value
    }
}
